package com.ohgiraffers.springdata.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/test")
    public ResponseEntity<String> test(){

        return ResponseEntity.status(400).body("안녕");

    }
}
