package com.api.fascinareventos.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("api/v1")
    public String index(Model model) {
        model.addAttribute("home.welcome", "Bem-vindo ao Gerenciador da Fascinar Eventos");
        return "publica-index";
    }
}
