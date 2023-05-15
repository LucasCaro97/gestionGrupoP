package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.ContadorTotalizadora;
import com.grupop.gestion.Repositorios.ContadorTotalizadoraRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContadorTotalizadoraServicio {

    private final ContadorTotalizadoraRepo contadorTotalizadora;

    @Transactional(readOnly = true)
    public List<ContadorTotalizadora> obtenerTodos(){
      return contadorTotalizadora.findAll();
    }

}
