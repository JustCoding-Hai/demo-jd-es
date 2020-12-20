package top.javahai.demojdes.service;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import top.javahai.demojdes.bean.Content;
import top.javahai.demojdes.utils.HtmlParseUtil;

import javax.management.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Hai
 * @program: demo-jp-es
 * @description:
 * @create 2020/12/20 - 21:15
 **/
@Service
public class ContentService {

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    /**
     * 获取前端传入的keyword，爬取京东的数据，放入到ES中
     * @return
     */
    public boolean parseAndInsert(String keyword){
        List<Content> contents = HtmlParseUtil.parseJdGoodsHtml(keyword);
        //插入到ES中
        BulkRequest bulkRequest = new BulkRequest();
        for (Content content : contents) {
            bulkRequest.add(new IndexRequest("jd_goods").source(JSON.toJSONString(content), XContentType.JSON));
        }
        BulkResponse bulkResponse = null;
        try {
            bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return !bulkResponse.hasFailures();
    }

    /**
     * 用match分页查询 ，根据keyword获取数据
     */
    public List<Map<String,Object>> searchOfPage(String keyword,int pageNo,int pageSize){
        if (pageNo<=1){
            pageNo=1;
        }
        //条件查询
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        //构建查询条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //设置分页查询
        sourceBuilder.from(pageNo).size(pageSize);
        //设置关键词匹配
        //TermQueryBuilder queryBuilder = QueryBuilders.termQuery("title", keyword);
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("title", keyword);
        sourceBuilder.query(queryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //构建高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title")
                //关闭多个高亮显示
                .requireFieldMatch(false)
                .preTags("<span style='color:red'>")
                .postTags("</span>");

        sourceBuilder.highlighter(highlightBuilder);
        //执行搜索
        searchRequest.source(sourceBuilder);

        //获取返回结果
        ArrayList<Map<String, Object>> resultList = new ArrayList<>();
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //获取高亮
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField highlightField = highlightFields.get("title");
                //替换查询结果的title字段
                if (highlightField!=null){
                    Text[] fragments = highlightField.fragments();
                    //构建结果
                    StringBuilder highLightTitle = new StringBuilder();
                    for (Text fragment : fragments) {
                        highLightTitle.append(fragment);
                    }
                    sourceAsMap.put("title",highLightTitle);
                }
                System.out.println(hit.getSourceAsMap());
                resultList.add(sourceAsMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }



}
