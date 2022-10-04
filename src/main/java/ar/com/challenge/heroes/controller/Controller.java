package ar.com.challenge.heroes.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        return "<h1>Algo salió mal o la API no está disponible!</h1>";
    }
 
    @Override
    public String getErrorPath() {
        return "/error";
    }
}