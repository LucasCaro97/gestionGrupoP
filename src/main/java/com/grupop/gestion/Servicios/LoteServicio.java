package com.grupop.gestion.Servicios;


import com.grupop.gestion.Entidades.Lote;
import com.grupop.gestion.Entidades.Producto;
import com.grupop.gestion.Repositorios.LoteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoteServicio {

    private final LoteRepo loteRepo;
    private final ProductoServicio productoServicio;

    @Transactional
    public void crear(Lote dto) {
        Lote lote = new Lote();
        lote.setDescripcion(dto.getDescripcion());
        lote.setSuperficieM2(dto.getSuperficieM2());
        lote.setNroPartida(dto.getNroPartida());
        lote.setNroPlano(dto.getNroPlano());
        lote.setUrbanizacion(dto.getUrbanizacion());
        lote.setManzana(dto.getManzana());
        lote.setImpuestoProv(dto.getImpuestoProv());
        lote.setImpuestoMun(dto.getImpuestoMun());
        lote.setUbicacion(dto.getUbicacion());
        lote.setEstado(dto.getEstado());
        lote.setEsProducto(false);
        loteRepo.save(dto);
    }

    @Transactional
    public void actualizar(Lote dto) {
        Lote lote = loteRepo.findById(dto.getId()).get();
        lote.setDescripcion(dto.getDescripcion());
        lote.setSuperficieM2(dto.getSuperficieM2());
        lote.setNroPartida(dto.getNroPartida());
        lote.setNroPlano(dto.getNroPlano());
        lote.setUrbanizacion(dto.getUrbanizacion());
        lote.setManzana(dto.getManzana());
        lote.setImpuestoProv(dto.getImpuestoProv());
        lote.setImpuestoMun(dto.getImpuestoMun());
        lote.setUbicacion(dto.getUbicacion());
        lote.setEstado(dto.getEstado());
        loteRepo.save(dto);
    }

    @Transactional(readOnly = true)
    public List<Lote> obtenerTodos(Long idUrbanizacion, Long idManzana) {
        //Sort sort = Sort.by("id").descending();
        if (idUrbanizacion != null && idManzana != null) {
            return loteRepo.searchByUrbanizacionAndManzana(idUrbanizacion, idManzana);
        } else if (idUrbanizacion != null) {
            return loteRepo.searchByUrbanizacion(idUrbanizacion);
        } else {
            return loteRepo.findAll();
        }
    }

    @Transactional(readOnly = true)
    public Lote obtenerPorId(Long id) {
        return loteRepo.findById(id).get();
    }

    @Transactional
    public void eliminarPorId(Long id) {
        loteRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Lote> obtenerPorUrbanizacion(Long id) {
        return loteRepo.obtenerPorUrb(id);
    }

    @Transactional
    public void altaComoProducto(Long id) throws Exception {

        Lote lote = loteRepo.findById(id).get();
        productoServicio.crearProductoLote(lote);
        lote.setEsProducto(true);
        loteRepo.save(lote);

    }

    @Transactional
    public void bajaProducto(Producto p) {
        Lote l = loteRepo.findById(p.getLote().getId()).get();
        l.setEsProducto(false);
        loteRepo.save(l);
    }

    public void altaGrupoProducto(Long id) throws Exception {
        List<Lote> listaLotes = loteRepo.obtenerPorUrb(id);
        for (Lote lote : listaLotes) {
            if (lote.isEsProducto() == false) {
                System.out.println("El lote " + lote.getId() + " - No es producto: (" + lote.isEsProducto() + ") - Registrando como producto");
                productoServicio.crearProductoLote(lote);
                lote.setEsProducto(true);
                loteRepo.save(lote);
            } else {
                System.out.println("Se omitio el lote " + lote.getId() + " - Es producto: (" + lote.isEsProducto() + ")");
            }
            //System.out.println("Dando de alta al grupo de lotes");
        }
    }
}