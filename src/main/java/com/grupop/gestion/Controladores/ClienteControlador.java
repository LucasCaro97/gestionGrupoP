package com.grupop.gestion.Controladores;

import com.grupop.gestion.Servicios.ClienteServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cliente")
public class ClienteControlador {

    private final ClienteServicio clienteServicio;

    @PostMapping("/create")
    public void create(){
        try{
            System.out.printf("Cliente generado");
        } catch(Exception e){
            e.getMessage();
        }
    }

}
