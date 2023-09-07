package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Cobro;
import com.grupop.gestion.Entidades.EntidadBase;
import com.grupop.gestion.Entidades.Pago;
import com.grupop.gestion.Repositorios.PagoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoServicio {

    private final PagoRepo pagoRepo;
    private final TipoOperacionServicio tipoOperacionServicio;
    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;
    private final TalonarioServicio talonarioServicio;
    private final ProveedorServicio proveedorServicio;

    @Transactional
    public void crear(Pago dto, String fechaComprobante){
        Pago p = new Pago();
        p.setProveedor(dto.getProveedor());
        p.setFechaComprobante(LocalDate.parse(fechaComprobante));
        p.setTipoComprobante(dto.getTipoComprobante());
        p.setTalonario(dto.getTalonario());
        p.setNroComprobante(dto.getNroComprobante());
        p.setSector(dto.getSector());
        p.setObservaciones(dto.getObservaciones());
        p.setTotal(new BigDecimal(0));
        p.setFormaDePago(dto.getFormaDePago());
        p.setTipoOperacion(tipoOperacionServicio.obtenerPorId(4l));
        pagoRepo.save(p);

        Pago ultimoPago = pagoRepo.findTopByOrderByIdDesc();
        formaDePagoDetalleServicio.crearSinSubDetalle(ultimoPago.getId(), ultimoPago.getTipoOperacion().getId(), ultimoPago.getTotal());
    }

    @Transactional
    public void actualizar(Pago dto, String fechaComprobante){
        Pago p = pagoRepo.findById(dto.getId()).get();
        Long idFormaDePagoAnterior;
        try{
            idFormaDePagoAnterior = p.getFormaDePago().getId();
        }catch (Exception e){
            idFormaDePagoAnterior = null;
        }

        p.setProveedor(dto.getProveedor());
        p.setFechaComprobante(LocalDate.parse(fechaComprobante));
        p.setTipoComprobante(dto.getTipoComprobante());
        if(p.getTalonario().getNroTalonario()!=dto.getTalonario().getNroTalonario()){
            talonarioServicio.aumentarUltimoNro(dto.getTalonario());
            p.setTalonario(dto.getTalonario());
        }
        p.setNroComprobante(dto.getNroComprobante());
        p.setSector(dto.getSector());
        p.setObservaciones(dto.getObservaciones());
        p.setFormaDePago(dto.getFormaDePago());
        pagoRepo.save(p);

        if(p.getFormaDePago() != null){
            // Si ya existe el detalleDePago verifico si cambia la formaDePago para operar
            if(idFormaDePagoAnterior != p.getFormaDePago().getId()){
                if(p.getFormaDePago().getId() == 54){ //La forma de pago ha cambiado y si es aDetallar => eliminar items detalleDePago
                    formaDePagoDetalleServicio.eliminarSubDetalles(p.getId(), p.getTipoOperacion().getId());
                }else{      // Si no es aDetallar => generar automaticamente el item detalleDepago
                    formaDePagoDetalleServicio.eliminarSubDetalles(p.getId(), p.getTipoOperacion().getId());
                    formaDePagoDetalleServicio.crearSubDetalleAutomatico(p.getId(), p.getTipoOperacion().getId(), p.getTotal(), p.getFormaDePago());
                }
            }
        }

    }

    @Transactional(readOnly = true)
    public Page<Pago> obtenerTodos(int page, int size){
        return pagoRepo.findAllByOrderByIdDesc(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Pago obtenerPorId(Long id){
        return pagoRepo.findById(id).get();
    }

    @Transactional
    public void eliminarPorId(Long id){
        pagoRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Long buscarUltimoId() { return pagoRepo.findLastId(); }

    @Transactional(readOnly = true)
    public Double obtenerTotalPorId(Long id) { return pagoRepo.obtenerTotalPorId(id);   }


    @Transactional
    public void actualizarTotal(Long idPago) {
        BigDecimal resultado = BigDecimal.ZERO;
        Pago p = pagoRepo.findById(idPago).get();
        BigDecimal totalCtaCte = pagoRepo.obtenerTotalCtaCte(idPago).orElse(BigDecimal.ZERO);
        BigDecimal totalImp = pagoRepo.obtenerTotalImp(idPago).orElse(BigDecimal.ZERO);
        BigDecimal totalAdelanto = pagoRepo.obtenerTotalAdelanto(idPago).orElse(BigDecimal.ZERO);

        resultado = totalCtaCte.add(totalImp).add(totalAdelanto);
        p.setTotal(resultado);

        formaDePagoDetalleServicio.actualizarTotal(p.getId(), 4l, resultado);
        pagoRepo.save(p);
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerTotalMensual(){
        return pagoRepo.obtenerTotalPagadoMensual();
    }


    @Transactional(readOnly = true)
    public BigDecimal obtenerSaldoProveedor(Long idOperacion){
        Long fkProveedor = pagoRepo.obtenerProveedor(idOperacion);
        return proveedorServicio.obtenerSaldo(fkProveedor);
    }

    @Transactional(readOnly = true)
    public Long obtenerProveedor(Long idOperacion) {
        return pagoRepo.obtenerProveedor(idOperacion);

    }
}
