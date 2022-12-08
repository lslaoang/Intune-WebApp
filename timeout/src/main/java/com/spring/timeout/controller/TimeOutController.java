package com.spring.timeout.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeOutController {

    @GetMapping("/timeout")
    public ResponseEntity requestTimeOut() throws InterruptedException {
        System.out.println("Sleeping ...");
        Thread.sleep(60000);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
