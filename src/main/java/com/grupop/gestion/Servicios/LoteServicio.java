package com.grupop.gestion.Servicios;


import com.grupop.gestion.Entidades.EstadoLote;
import com.grupop.gestion.Entidades.Lote;
import com.grupop.gestion.Entidades.Producto;
import com.grupop.gestion.Repositorios.LoteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoteServicio {

    private final LoteRepo loteRepo;
    private final ProductoServicio productoServicio;
    private final EstadoLoteServicio estadoLoteServicio;
    private final ManzanaServicio manzanaServicio;
    private final UrbanizacionServicio urbanizacionServicio;

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
        lote.setNroLote(manzanaServicio.asignarUltimoNroLote(lote.getManzana().getId()));
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
    public Page<Lote> findAll(Pageable pageable){
        return loteRepo.findAll(pageable);
    }


    @Transactional(readOnly = true)
    public Page<Lote> obtenerTodos(Long idUrbanizacion, Long idManzana, int page, int size) {
        Page<Lote> listaLotes;

        if (idUrbanizacion != null && idManzana != null) {
           listaLotes = loteRepo.searchByUrbanizacionAndManzana(idUrbanizacion, idManzana, PageRequest.of(page,size));
        } else if (idUrbanizacion != null) {
            listaLotes = loteRepo.searchByUrbanizacion(idUrbanizacion, PageRequest.of(page,size));
        } else {
             listaLotes = loteRepo.findAllByOrderByUrbanizacionDescripcionAscManzanaDescripcionAscNroLoteAsc(PageRequest.of(page,size));
        }
    return  listaLotes;
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

    @Transactional
    public void alterarEstado(Long idProd, Long idEstado){

        Producto prod = productoServicio.buscarPorId(idProd);
        //ALTERO EL ESTADO DEL PRODUCTO UNICAMENTE SI ES DEL TIPO LOTE - LOS DEMAS PERMANECEN SIEMPRE ACTIVOS, SALVO QUE SE ELIMINEN
        if(prod.getTipoProducto().getId() == 2){
            Lote lote = loteRepo.findById(prod.getLote().getId()).get();
            EstadoLote estado = estadoLoteServicio.buscarPorId(idEstado);
            lote.setEstado(estado);
            loteRepo.save(lote);
                if(idEstado==3){
                    System.out.println("Alterando estado de producto: false/vendido");
                    productoServicio.actualizarEstadoPorId(idProd, 0);
                }else if(idEstado==1){
                    System.out.println("Alterando estado de producto: true/disponible");
                    productoServicio.actualizarEstadoPorId(idProd, 1);
                }

        }else{
            System.out.println("El producto no es un lote - No se modifica estado");
        }
    }

    @Transactional
    public Integer obtenerStockPorUrb(Long idUrb){
        return loteRepo.obtenerStockPorUrb(idUrb);
    }
    @Transactional
    public Integer obtenerDisponiblesPorUrb(Long idUrb){
        return loteRepo.obtenerDisponiblesPorUrb(idUrb);
    }

    @Transactional
    public Integer obtenerVendidosPorUrb(Long idUrb){
        return loteRepo.obtenerVendidosPorUrb(idUrb);
    }


    @Transactional
    public void crearGrupoDeLotes(Long idUrb, Long idManzana, Integer cantLotes) {
     for (int i = 0; i < cantLotes ; i++){
        Lote lote = new Lote();
        lote.setUrbanizacion(urbanizacionServicio.obtenerPorId(idUrb));
        lote.setManzana(manzanaServicio.obtenerPorId(idManzana));
        lote.setNroLote(manzanaServicio.asignarUltimoNroLote(idManzana));
        lote.setDescripcion("Lote " + lote.getNroLote() + " de la manzana " + lote.getManzana().getDescripcion() + " urb " + lote.getUrbanizacion().getId());
        lote.setEstado(estadoLoteServicio.buscarPorId(1l));
        loteRepo.save(lote);
     }

    }
}