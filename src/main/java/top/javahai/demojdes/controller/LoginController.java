package top.javahai.demojdes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Hai
 * @program: demo-jp-es
 * @description:
 * @create 2020/12/20 - 18:54
 **/
@Controller
public class LoginController {

    @GetMapping(value = {"/","/index"})
    public String index(){
        return "index";
    }
}
