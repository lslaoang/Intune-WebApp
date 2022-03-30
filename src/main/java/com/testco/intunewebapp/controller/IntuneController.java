package com.testco.intunewebapp.controller;

import com.testco.intunewebapp.model.response.Accepted;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;

@RestController
@RequestMapping("${api.endpoints.base-path}")
public class IntuneController {

    @GetMapping(path ="/verify")
    public ResponseEntity<?> verify(){
        return new ResponseEntity<>( new Accepted(), HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/check")
    public ResponseEntity<?> check(){
        return new ResponseEntity<>( "All Good!", HttpStatus.ACCEPTED);
    }
}
