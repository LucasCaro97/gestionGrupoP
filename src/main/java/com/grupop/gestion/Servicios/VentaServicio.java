package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.*;
import com.grupop.gestion.Repositorios.TalonarioRepo;
import com.grupop.gestion.Repositorios.VentaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaServicio {


    private final VentaRepo ventaRepo;
    private final TalonarioServicio talonarioServicio;
    private final IndiceCacServicio indiceCacServicio;
    private final TipoOperacionServicio tipoOperacionServicio;
    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;

    @Transactional
    public void crear(Venta dto){
        Venta vta = new Venta();
        vta.setCliente(dto.getCliente());
        vta.setCuit(dto.getCuit());
        vta.setTipoIva(dto.getTipoIva());
        vta.setTipoComprobante(dto.getTipoComprobante());
        vta.setTalonario(dto.getTalonario());
        vta.setNroComprobante(dto.getNroComprobante());
        vta.setSector(dto.getSector());
        vta.setMoneda(dto.getMoneda());
        vta.setFormaDePago(dto.getFormaDePago());
        vta.setObservaciones(dto.getObservaciones());
        vta.setFechaComprobante(dto.getFechaComprobante());
        vta.setTotal(new BigDecimal(0));
        vta.setBloqueada(false);
        vta.setIndiceBase(dto.getIndiceBase());
        vta.setTipoOperacion(tipoOperacionServicio.obtenerPorId(1l));
        talonarioServicio.aumentarUltimoNro(dto.getTalonario());

        ventaRepo.save(vta);
    }

    @Transactional //AGREGAR VALIDACIONES PARA QUE NO SE PUEDA MODIFICAR CUANDO YA CONTIENE UN COBRO
    public void actualizar(Venta dto){
        Venta vta = ventaRepo.findById(dto.getId()).get();

        if(vta.isBloqueada()){

            vta.setObservaciones(dto.getObservaciones());
            ventaRepo.save(vta);
        }else{
            vta.setCliente(dto.getCliente());
            vta.setCuit(dto.getCuit());
            vta.setTipoIva(dto.getTipoIva());
            vta.setTipoComprobante(dto.getTipoComprobante());
            if(vta.getTalonario().getNroTalonario()!=dto.getTalonario().getNroTalonario()){
                talonarioServicio.aumentarUltimoNro(dto.getTalonario());
                vta.setTalonario(dto.getTalonario());
            }
            vta.setNroComprobante(dto.getNroComprobante());
            vta.setSector(dto.getSector());
            vta.setMoneda(dto.getMoneda());
            vta.setFormaDePago(dto.getFormaDePago());
            vta.setObservaciones(dto.getObservaciones());
            vta.setFechaComprobante(dto.getFechaComprobante());
            vta.setIndiceBase(dto.getIndiceBase());
            ventaRepo.save(vta);

            System.out.println("ID FP : " + vta.getFormaDePago().getId());
            if(vta.getFormaDePago() != null) {
                if (vta.getFormaDePago().getId() != 51) { //Validar que no exista ya uno, si ya existe no actualizar
                    System.out.println("Creando Detalle de Pago!");
                    formaDePagoDetalleServicio.crear(vta.getId(), vta.getTipoOperacion().getId(), vta.getTotal(), vta.getFormaDePago());
                    System.out.println(vta);
                }
            }
        }
    }


    @Transactional(readOnly = true)
    public List<Venta> obtenerTodas(){ return ventaRepo.findAll(); }

    @Transactional(readOnly = true)
    public Venta obtenerPorId(Long id){ return ventaRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ ventaRepo.deleteById(id); }

    @Transactional(readOnly = true)
    public Long buscarUltimoId(){ return ventaRepo.findLastId(); }


    @Transactional(readOnly = true)
    public Double obtenerTotalPorId(Long id){
        return ventaRepo.obtenerTotalPorId(id);
    }

    @Transactional
    public void cerrarVenta(Long idVenta){
        Venta vta = ventaRepo.findById(idVenta).get();
        vta.setBloqueada(true);
        ventaRepo.save(vta);
    }

    @Transactional(readOnly = true)
    public Boolean validarEstado(Long idVenta) {
        return ventaRepo.validarEstado(idVenta);
    }

    @Transactional(readOnly = true)
    public List<Venta> obtenerVentasSinCreditoPorCliente(Long id){
        return ventaRepo.obtenerVentasSinCreditoPorCliente(id);
    }

    @Transactional
    public void actualizarTotalNuevo(Long idVenta){
        BigDecimal resultado = new BigDecimal(0);
        Venta v = ventaRepo.findById(idVenta).get();
        BigDecimal totalProd = ventaRepo.obtenerTotalProductos(v.getId());
        BigDecimal totalImp = ventaRepo.obtenerTotalImputacion(v.getId());

        if(totalProd!=null && totalImp !=null){
            resultado = totalProd.add(totalImp);
        }else if(totalProd!=null){
            resultado = totalProd;
        }else if(totalImp!=null){
            resultado = totalImp;
        }

        v.setTotal(resultado);
        ventaRepo.save(v);
    }

    @Transactional(readOnly = true)
    public List<Venta> obtenerVtasCtaCtePorCliente(Long id) {
        return ventaRepo.obtenerVentasCtaCtePorCliente(id);
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerIndiceBase(Long idVenta) {

        Long idIndiceCac = ventaRepo.obtenerIndiceBase(idVenta);
        if(idIndiceCac == null){
            return BigDecimal.ZERO;
        }else{
            IndiceCAC indiceBase = indiceCacServicio.obtenerPorId(idIndiceCac);
            return indiceBase.getIndice();
        }
    }
}

