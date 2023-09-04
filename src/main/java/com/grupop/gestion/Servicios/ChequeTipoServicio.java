package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.ChequeTipo;
import com.grupop.gestion.Repositorios.ChequeTipoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChequeTipoServicio {

    private final ChequeTipoRepo chequeTipoRepo;

    @Transactional
    public void crear (ChequeTipo dto){
        ChequeTipo c = new ChequeTipo();
        c.setDescripcion(dto.getDescripcion());
        chequeTipoRepo.save(c);
    }

    @Transactional
    public void actualizar (ChequeTipo dto){
        ChequeTipo c = chequeTipoRepo.findById(dto.getId()).get();
        c.setDescripcion(dto.getDescripcion());
        chequeTipoRepo.save(c);
    }

    @Transactional(readOnly = true)
    public List<ChequeTipo> obtenerTodos(){ return chequeTipoRepo.findAll(); }

    @Transactional(readOnly = true)
    public ChequeTipo obtenerPorId(Long id){ return chequeTipoRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ chequeTipoRepo.deleteById(id); }


}
