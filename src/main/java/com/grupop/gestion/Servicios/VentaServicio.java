package com.grupop.gestion.Servicios;

import com.grupop.gestion.DTO.VentasDTO;
import com.grupop.gestion.Entidades.*;
import com.grupop.gestion.Repositorios.TalonarioRepo;
import com.grupop.gestion.Repositorios.VentaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaServicio {


    private final VentaRepo ventaRepo;
    private final TalonarioServicio talonarioServicio;
    private final IndiceCacServicio indiceCacServicio;
    private final TipoOperacionServicio tipoOperacionServicio;
    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;
    private final ClienteServicio clienteServicio;
    private final EntidadBaseServicio entidadBaseServicio;

    @Transactional
    public void crear(Venta dto, String fechaComprobante){
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
        vta.setFechaComprobante(LocalDate.parse(fechaComprobante));
        vta.setTotal(new BigDecimal(0));
        vta.setBloqueada(false);
        vta.setIndiceBase(dto.getIndiceBase());
        vta.setTipoOperacion(tipoOperacionServicio.obtenerPorId(1l));
        talonarioServicio.aumentarUltimoNro(talonarioServicio.obtenerPorNroTalonario(dto.getTalonario()));

        ventaRepo.save(vta);


        Venta ultimaVenta = ventaRepo.findTopByOrderByIdDesc();

        formaDePagoDetalleServicio.crearSinSubDetalle(ultimaVenta.getId(), ultimaVenta.getTipoOperacion().getId(), ultimaVenta.getTotal());
    }

    @Transactional
    public void actualizar(Venta dto, String fechaComprobante){
        Venta vta = ventaRepo.findById(dto.getId()).get();
        Long idFormaDePagoAnterior;
        try{
            idFormaDePagoAnterior = vta.getFormaDePago().getId();
        }catch (Exception e){
            idFormaDePagoAnterior = null;
        }


        if(vta.isBloqueada()){

            vta.setObservaciones(dto.getObservaciones());
            ventaRepo.save(vta);

        }else{
            vta.setCliente(dto.getCliente());
            vta.setCuit(dto.getCuit());
            vta.setTipoIva(dto.getTipoIva());
            vta.setTipoComprobante(dto.getTipoComprobante());
            if(vta.getTalonario() != dto.getTalonario() ){
                talonarioServicio.aumentarUltimoNro(talonarioServicio.obtenerPorNroTalonario(dto.getTalonario()));
                vta.setTalonario(dto.getTalonario());
            }
            vta.setNroComprobante(dto.getNroComprobante());
            vta.setSector(dto.getSector());
            vta.setMoneda(dto.getMoneda());
            vta.setFormaDePago(dto.getFormaDePago());
            vta.setObservaciones(dto.getObservaciones());
            vta.setFechaComprobante(LocalDate.parse(fechaComprobante));
            vta.setIndiceBase(dto.getIndiceBase());
            ventaRepo.save(vta);

            if(vta.getFormaDePago() != null) {
                    // Si ya existe el detalleDePago verifico si cambia la formaDePago para operar
                    if( idFormaDePagoAnterior != vta.getFormaDePago().getId() ){
                        if(vta.getFormaDePago().getId() == 51){ //La forma de pago ha cambiado y si es aDetallar => eliminar items detalleDePago
                            formaDePagoDetalleServicio.eliminarSubDetalles(vta.getId(), vta.getTipoOperacion().getId());
                        }else{      // Si no es aDetallar => generar automaticamente el item detalleDepago
                            formaDePagoDetalleServicio.eliminarSubDetalles(vta.getId(), vta.getTipoOperacion().getId());
                            formaDePagoDetalleServicio.crearSubDetalleAutomatico(vta.getId(), vta.getTipoOperacion().getId(), vta.getTotal(), vta.getFormaDePago());
                        }
                    }
            }
        }
    }


    @Transactional(readOnly = true)
    public Page<Venta> obtenerTodas(int page, int size){ return ventaRepo.findAllByOrderByIdDesc(PageRequest.of(page, size)); }

    @Transactional(readOnly = true)
    public Venta obtenerPorId(Long id){ return ventaRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ // Cuando elimino subdetalles debo descontar el importe del saldo de la cuenta Banco/Tarjeta
        formaDePagoDetalleServicio.eliminarSubDetalles(id, 1l);
        formaDePagoDetalleServicio.eliminarMaestro(id, 1l);
        ventaRepo.deleteById(id);
    }

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
        BigDecimal resultado = BigDecimal.ZERO;
        Venta v = ventaRepo.findById(idVenta).get();
        BigDecimal totalProd = ventaRepo.obtenerTotalProductos(v.getId()).orElse(BigDecimal.ZERO);
        BigDecimal totalImp = ventaRepo.obtenerTotalImputacion(v.getId()).orElse(BigDecimal.ZERO);

        resultado = totalProd.add(totalImp);
        v.setTotal(resultado);
        formaDePagoDetalleServicio.actualizarTotal(v.getId(), 1l, resultado);
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

    @Transactional(readOnly = true)
    public BigDecimal obtenerTotalMensual(){
        return ventaRepo.obtenerTotalVendidoMensual();
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerSaldoCliente(Long idOperacion){
        Long fkCliente = ventaRepo.obtenerCliente(idOperacion);
        return clienteServicio.obtenerSaldo(fkCliente);
    }

    @Transactional(readOnly = true)
    public List<VentasDTO> obtenerOperaciones(String fechaDesde, String fechaHasta, Long sectorId, Integer talDesde, Integer talHasta, Boolean excluirTal, Long idFormaPago) {
     if(excluirTal){
         System.out.println("Excluir talonarios");
         List<Venta> listaVentas = ventaRepo.obtenerOperacionesExcluyendoTalonario(fechaDesde, fechaHasta, sectorId, talDesde, talHasta, idFormaPago);
         List<VentasDTO> listaVentasDTO = new ArrayList<>();

         for (Venta v: listaVentas) {

            VentasDTO ventasDto = new VentasDTO();
            ventasDto.setId(v.getId());
            ventasDto.setCliente(entidadBaseServicio.obtenerNombrePorFkCliente(v.getCliente().getId()).getRazonSocial());
            ventasDto.setFechaComprobante(v.getFechaComprobante());
            ventasDto.setTalonario(v.getTalonario());
            ventasDto.setNroComprobante(v.getNroComprobante());
            ventasDto.setSector(v.getSector().getDescripcion());
            ventasDto.setFormaPago(v.getFormaDePago().getDescripcion());
            ventasDto.setTotal(v.getTotal());
            listaVentasDTO.add(ventasDto);
         }


         return listaVentasDTO;
     }else{
         System.out.println("No excluir talonarios");
         List<Venta> listaVentas = ventaRepo.obtenerOperaciones(fechaDesde, fechaHasta, sectorId, talDesde, talHasta, idFormaPago);
         List<VentasDTO> listaVentasDTO = new ArrayList<>();

         for (Venta v: listaVentas) {

             VentasDTO ventasDto = new VentasDTO();
             ventasDto.setId(v.getId());
             ventasDto.setCliente(entidadBaseServicio.obtenerNombrePorFkCliente(v.getCliente().getId()).getRazonSocial());
             ventasDto.setFechaComprobante(v.getFechaComprobante());
             ventasDto.setTalonario(v.getTalonario());
             ventasDto.setNroComprobante(v.getNroComprobante());
             ventasDto.setSector(v.getSector().getDescripcion());
             ventasDto.setFormaPago(v.getFormaDePago().getDescripcion());
             ventasDto.setTotal(v.getTotal());
             listaVentasDTO.add(ventasDto);

         }
         return listaVentasDTO;
        }
    }

}

