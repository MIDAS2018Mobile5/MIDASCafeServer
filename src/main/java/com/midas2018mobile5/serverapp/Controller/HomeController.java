package com.midas2018mobile5.serverapp.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String Index() {
        return "hello I'm Midas Cafe Server Application";
    }
}
