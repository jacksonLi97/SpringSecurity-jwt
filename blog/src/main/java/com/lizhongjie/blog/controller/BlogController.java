package com.lizhongjie.blog.controller;

import com.lizhongjie.blog.entity.Blog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogController {

    @RequestMapping(value = "/blog", method = RequestMethod.GET)
    public Blog blog(){
        Blog blog = new Blog();
        blog.setTitle("axios get test");
        blog.setContent("success!");
        return blog;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}
