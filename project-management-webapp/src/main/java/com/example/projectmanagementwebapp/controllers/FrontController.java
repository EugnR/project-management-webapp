package com.example.projectmanagementwebapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/v1")
public class FrontController {
    @GetMapping
    public String processGet(){
        return "0";
    }

    @PostMapping
    public String processPost(){
        return "0";
    }

    @PutMapping
    public String processPut(){
        return "0";
    }

    @DeleteMapping
    public String processDelete(){
        return "0";
    }
}
