package com.puccampinassi.pi4.t2g09.onconnect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteAPI {

    @GetMapping("/")
    public String home() {
        return "Onconnect API funcionando!";
    }
}