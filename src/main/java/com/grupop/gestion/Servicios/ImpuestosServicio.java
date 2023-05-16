package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Impuestos;
import com.grupop.gestion.Repositorios.ImpuestosRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImpuestosServicio {

    private final ImpuestosRepo impuestosRepo;

    @Transactional
    public void crear(Impuestos dto){
        Impuestos imp = new Impuestos();
        imp.setDescripcion(dto.getDescripcion());
        imp.setPorcentaje(dto.getPorcentaje());
        impuestosRepo.save(imp);
    }

    @Transactional
    public void actualizar(Impuestos dto){
        Impuestos imp = impuestosRepo.findById(dto.getId()).get();
        imp.setDescripcion(dto.getDescripcion());
        imp.setPorcentaje(dto.getPorcentaje());
        impuestosRepo.save(imp);
    }

    @Transactional(readOnly = true)
    public List<Impuestos> obtenerTodos(){ return impuestosRepo.findAll(); }

    @Transactional(readOnly = true)
    public Impuestos obtenerPorId(Long id){ return impuestosRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ impuestosRepo.deleteById(id);}

}
