package jpa.study.hellojpa.shoppingMallProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Controller1 {

    @GetMapping(value = "/layout")
    public String go(){
        return "/layouts/layout1";
    }
}