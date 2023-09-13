package com.optimagrowth.organization.controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("demo")
    public String demo(){
        var a = SecurityContextHolder.getContext().getAuthentication();
        return "Demo!";
    }
}
