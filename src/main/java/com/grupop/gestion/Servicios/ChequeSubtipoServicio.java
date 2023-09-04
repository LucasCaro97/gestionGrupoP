package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.ChequeSubtipo;
import com.grupop.gestion.Repositorios.ChequeSubtipoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChequeSubtipoServicio {

    private final ChequeSubtipoRepo chequeSubtipoRepo;


    @Transactional
    public void crear(ChequeSubtipo dto){
        ChequeSubtipo c = new ChequeSubtipo();
        c.setDescripcion(dto.getDescripcion());
        c.setChequeTipo(dto.getChequeTipo());
        chequeSubtipoRepo.save(c);

    }

    @Transactional
    public void actualizar(ChequeSubtipo dto){
        ChequeSubtipo c = chequeSubtipoRepo.findById(dto.getId()).get();
        c.setDescripcion(dto.getDescripcion());
        c.setChequeTipo(dto.getChequeTipo());
        chequeSubtipoRepo.save(c);

    }

    @Transactional(readOnly = true)
    public List<ChequeSubtipo> obtenerTodos(){ return chequeSubtipoRepo.findAll(); }

    @Transactional(readOnly = true)
    public ChequeSubtipo obtenerPorId(Long id) { return chequeSubtipoRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id) { chequeSubtipoRepo.deleteById(id); }

    @Transactional (readOnly = true)
    public List<ChequeSubtipo> obtenerPorTipo(Long id){
        return chequeSubtipoRepo.findByChequeTipo(id);
    }


}
