package top.javahai.demojdes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.javahai.demojdes.service.ContentService;

import java.util.List;
import java.util.Map;

/**
 * @author Hai
 * @program: demo-jp-es
 * @description:
 * @create 2020/12/20 - 21:15
 **/
@RestController
public class ContentController {
    @Autowired
    ContentService contentService;

    @GetMapping("/parse/{keyword}")
    public boolean getGoods(@PathVariable("keyword") String keyword){
        return contentService.parseAndInsert(keyword);
    }

    @GetMapping("/search/{pageNo}/{pageSize}")
    public List<Map<String, Object>> search(@RequestParam("keyword") String keyword,
                                            @PathVariable("pageNo") int pageNo,
                                            @PathVariable("pageSize") int pageSize){
        return contentService.searchOfPage(keyword,pageNo,pageSize);
    }
}
