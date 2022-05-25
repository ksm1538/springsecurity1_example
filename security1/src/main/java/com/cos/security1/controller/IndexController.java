package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping({"", "/"})
    public String index(){
        // mustache default folder : src/main/resources/
        // view resolver setting : templates (prefix), .mustache (suffix)
        return "index"; // src/main/resources/templates/index.mustache
    }
}
