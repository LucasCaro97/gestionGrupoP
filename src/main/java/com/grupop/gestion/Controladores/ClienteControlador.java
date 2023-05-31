package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Cliente;
import com.grupop.gestion.Servicios.ClienteServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cliente")
public class ClienteControlador {

    private final ClienteServicio clienteServicio;

    @GetMapping("/obtenerNombre/{id}")
    public ResponseEntity<String> obtenerNombre(@PathVariable Long id){
        //System.out.println(clienteServicio.obtenerNombre(id));
        return ResponseEntity.ok(clienteServicio.obtenerNombre(id));
    }


}
