package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Errores;
import com.grupop.gestion.Repositorios.ErroresRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ErroresServicio {

    private final ErroresRepo erroresRepo;

    @Transactional
    public void crear(Errores dto){
        Errores e = new Errores();
        e.setNombreUsuario(dto.getNombreUsuario());
        e.setDescripcion(dto.getDescripcion());
        erroresRepo.save(e);
    }

    @Transactional
    public void actualizar(Errores dto){
        Errores e = erroresRepo.findById(dto.getId()).get();
        e.setNombreUsuario(dto.getNombreUsuario());
        e.setDescripcion(dto.getDescripcion());
        erroresRepo.save(e);
    }

    @Transactional
    public void eliminarPorId(Long id){
        erroresRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Errores> obtenerTodos(){
        return erroresRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Errores obtenerError(Long id){
        return erroresRepo.findById(id).get();
    }
}
