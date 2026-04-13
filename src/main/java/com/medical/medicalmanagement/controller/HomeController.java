package com.medical.medicalmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Kullanıcı anasayfaya ( / veya /index ) girmek istediğinde bu metod çalışır
    @GetMapping({"/", "/index"})
    public String home() {

        return "index";
    }
}