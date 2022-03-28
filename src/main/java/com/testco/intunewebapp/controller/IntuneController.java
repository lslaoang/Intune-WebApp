package com.testco.intunewebapp.controller;

import com.testco.intunewebapp.model.response.Accepted;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class IntuneController {

    @GetMapping("/verify")
    public ResponseEntity<?> verify(){
        return new ResponseEntity<>(new Accepted(), HttpStatus.ACCEPTED);
    }
}
