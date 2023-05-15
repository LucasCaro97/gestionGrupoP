package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.TipoTotalizadora;
import com.grupop.gestion.Repositorios.TipoOperacionRepo;
import com.grupop.gestion.Repositorios.TipoTotalizadoraRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoTotalizadoraServicio {


    private final TipoTotalizadoraRepo tipoTotalizadoraRepo;

    @Transactional
    public void crear(TipoTotalizadora dto){
        TipoTotalizadora tot = new TipoTotalizadora();
        tot.setDescripcion(dto.getDescripcion());
        tipoTotalizadoraRepo.save(tot);
    }

    @Transactional
    public void actualizar(TipoTotalizadora dto){
        TipoTotalizadora tot = tipoTotalizadoraRepo.findById(dto.getId()).get();
        tot.setDescripcion(dto.getDescripcion());
        tipoTotalizadoraRepo.save(tot);
    }

    @Transactional(readOnly = true)
    public List<TipoTotalizadora> obtenerTodos(){   return tipoTotalizadoraRepo.findAll(); }

    @Transactional(readOnly = true)
    public TipoTotalizadora obtenerPorId(Long id) { return tipoTotalizadoraRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id) {  tipoTotalizadoraRepo.deleteById(id); }



}
