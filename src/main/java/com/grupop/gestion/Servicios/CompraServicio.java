package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Compra;
import com.grupop.gestion.Repositorios.CompraRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraServicio {

    private final CompraRepo compraRepo;
    private final TalonarioServicio talonarioServicio;
    private final TipoOperacionServicio tipoOperacionServicio;

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
        compra.setTotal(new BigDecimal(0));
        compra.setBloqueado(false);
        compra.setTipoOperacion(tipoOperacionServicio.obtenerPorId(2l));
        talonarioServicio.aumentarUltimoNro(dto.getTalonario());
        compraRepo.save(compra);
    }

    @Transactional
    public void actualizar(Compra dto){
        Compra compra = compraRepo.findById(dto.getId()).get();

        if(compra.isBloqueado()){
            compra.setObservaciones(dto.getObservaciones());
            compraRepo.save(compra);
        }else{

        compra.setProveedor(dto.getProveedor());
        compra.setFechaComprobante(dto.getFechaComprobante());
        compra.setTipoComprobante(dto.getTipoComprobante());
        if(compra.getTalonario().getNroTalonario()!=dto.getTalonario().getNroTalonario()){
            talonarioServicio.aumentarUltimoNro(dto.getTalonario());
            compra.setTalonario(dto.getTalonario());
        }
        compra.setNroComprobante(dto.getNroComprobante());
        compra.setSector(dto.getSector());
        compra.setFormaDePago(dto.getFormaDePago());
        compra.setObservaciones(dto.getObservaciones());
        compraRepo.save(compra);
        }
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
    public void actualizarTotal(Long idCompra, BigDecimal total){
        Compra cpa = compraRepo.findById(idCompra).get();
        cpa.setTotal(total);
        compraRepo.save(cpa);
    }

    @Transactional
    public void actualizarTotal(Long idCompra){
        BigDecimal resultado = new BigDecimal(0);
        Compra compra = compraRepo.findById(idCompra).get();
        BigDecimal totalProd = compraRepo.obtenerTotalProductos(compra.getId());
        BigDecimal totalImp = compraRepo.obtenerTotalImputacion(compra.getId());


        if(totalProd!=null && totalImp !=null){
            resultado = totalProd.add(totalImp);
        }else if(totalProd!=null){
            resultado = totalProd;
        }else if(totalImp!=null){
            resultado = totalImp;
        }

        compra.setTotal(resultado);
        compraRepo.save(compra);
    }

    @Transactional(readOnly = true)
    public Double obtenerTotalPorId(Long id) {
        return compraRepo.obtenerTotalPorId(id); }


    @Transactional(readOnly = true)
    public List<Compra> obtenerComprasPendientesPagoPorProveedor(Long id) {
        return compraRepo.obtenerComprasPendientesPagoPorProveedor(id);
    }

    @Transactional
    public void marcarComoBloqueado(Long id){
        Compra c = compraRepo.findById(id).get();
        c.setBloqueado(true);
        compraRepo.save(c);
    }

    @Transactional
    public void marcarComoDesbloqueado(Long id) {
        Compra c = compraRepo.findById(id).get();
        c.setBloqueado(false);
        compraRepo.save(c);
    }

    @Transactional
    public Boolean validarEstado(Long idCompra) {
        return compraRepo.validarEstado(idCompra);
    }
}
