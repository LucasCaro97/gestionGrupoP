package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Compra;
import com.grupop.gestion.Repositorios.CompraRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraServicio {

    private final CompraRepo compraRepo;
    private final TalonarioServicio talonarioServicio;

    @Transactional
    public void crear(Compra dto){
        Compra compra = new Compra();
        compra.setProveedor(dto.getProveedor());
        compra.setFechaComprobante(dto.getFechaComprobante());
        compra.setTipoComprobante(dto.getTipoComprobante());
        compra.setTalonario(dto.getTalonario());
        compra.setNroComprobante(dto.getNroComprobante());
        compra.setSector(dto.getSector());
        compra.setFormaDePago(dto.getFormaDePago());
        compra.setObservaciones(dto.getObservaciones());
        compra.setTotal(0D);
        talonarioServicio.aumentarUltimoNro(dto.getTalonario());
        compraRepo.save(compra);
    }

    @Transactional
    public void actualizar(Compra dto){
        Compra compra = compraRepo.findById(dto.getId()).get();
        compra.setProveedor(dto.getProveedor());
        compra.setFechaComprobante(dto.getFechaComprobante());
        compra.setTipoComprobante(dto.getTipoComprobante());
        compra.setTalonario(dto.getTalonario());
        compra.setNroComprobante(dto.getNroComprobante());
        compra.setSector(dto.getSector());
        compra.setFormaDePago(dto.getFormaDePago());
        compra.setObservaciones(dto.getObservaciones());
        compraRepo.save(compra);
    }

    @Transactional(readOnly = true)
    public List<Compra> obtenerTodas(){ return compraRepo.findAll(); }

    @Transactional(readOnly = true)
    public Compra obtenerPorId(Long id){ return compraRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ compraRepo.deleteById(id); }

    @Transactional(readOnly = true)
    public Long buscarUltimoId(){ return compraRepo.findLastId(); }

    @Transactional
    public void actualizarTotal(Long idCompra, Double total){
        Compra cpa = compraRepo.findById(idCompra).get();
        cpa.setTotal(total);
        compraRepo.save(cpa);
    }

    @Transactional(readOnly = true)
    public Double obtenerTotalPorId(Long id) {
        return compraRepo.obtenerTotalPorId(id); }




}
