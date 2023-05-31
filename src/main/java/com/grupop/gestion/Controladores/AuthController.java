package com.grupop.gestion.Controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class AuthController {

    @GetMapping
    public ModelAndView home(){
        return new ModelAndView("index");
    }

    @GetMapping("/xxx")
    public ModelAndView query(){ return new ModelAndView("xxx"); };

}
