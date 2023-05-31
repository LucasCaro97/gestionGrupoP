package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.TipoProducto;
import com.grupop.gestion.Repositorios.TipoProductoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoProductoServicio {

    private final TipoProductoRepo tipoProductoRepo;

    @Transactional
    public void crear(TipoProducto dto){
        TipoProducto tp = new TipoProducto();
        tp.setDescripcion(dto.getDescripcion());
        tipoProductoRepo.save(tp);
    }

    @Transactional
    public void actualizar(TipoProducto dto){
        TipoProducto tp = tipoProductoRepo.findById(dto.getId()).get();
        tp.setDescripcion(dto.getDescripcion());
        tipoProductoRepo.save(tp);
    }

    @Transactional(readOnly = true)
    public List<TipoProducto> obtenerTodos(){ return tipoProductoRepo.findAll(); }

    @Transactional(readOnly = true)
    public TipoProducto obtenerPorId(Long id){ return tipoProductoRepo.findById(id).get();}

    @Transactional
    public void eliminarPorId(Long id) { tipoProductoRepo.deleteById(id);}

    public TipoProducto obtenerIdTipoLote(String nombre) { return tipoProductoRepo.obtenerTipoLote(nombre); }
}
