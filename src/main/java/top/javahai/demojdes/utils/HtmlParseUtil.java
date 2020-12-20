package top.javahai.demojdes.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.javahai.demojdes.bean.Content;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hai
 * @program: demo-jp-es
 * @description:
 * @create 2020/12/20 - 20:44
 **/
public class HtmlParseUtil {

    public static void main(String[] args) throws Exception {
        List<Content> contents = HtmlParseUtil.parseJdGoodsHtml(" ");
        contents.forEach(System.out::println);


    }

    public static List<Content> parseJdGoodsHtml(String keyword)  {
        String url="https://search.jd.com/Search?keyword="+keyword+"&enc=utf-8";
        ArrayList<Content> contents = new ArrayList<>();
        //解析网页,返回的document对象就是浏览器的document对象
        try {
            Document document = Jsoup.parse(new URL(url), 30000);
            //通过id获取元素
            Element divElement = document.getElementById("J_goodsList");
            //获取所有的ul下的li
            Elements liElements = divElement.getElementsByTag("li");
            //获取liElements下的内容
            for (Element liElement : liElements) {
                String img = liElement.getElementsByTag("img").eq(0).attr("data-lazy-img");
                String price = liElement.getElementsByClass("p-price").eq(0).text();
                String title = liElement.getElementsByClass("p-name").eq(0).text();
                Content content = new Content(img, title, price);
                contents.add(content);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return contents;
    }
}
