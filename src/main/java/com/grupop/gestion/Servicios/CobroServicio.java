package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Cobro;
import com.grupop.gestion.Entidades.EntidadBase;
import com.grupop.gestion.Repositorios.CobroRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CobroServicio {


    private final CobroRepo cobroRepo;
    private final TalonarioServicio talonarioServicio;
    private final TipoOperacionServicio tipoOperacionServicio;
    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;
    private final ClienteServicio clienteServicio;

    @Transactional
    public void crear(Cobro dto, String fechaComprobante) {
        Cobro c = new Cobro();
        c.setCliente(dto.getCliente());
        c.setCuit(dto.getCuit());
        c.setTipoIva(dto.getTipoIva());
        c.setFechaComprobante(LocalDate.parse(fechaComprobante));
        c.setTipoComprobante(dto.getTipoComprobante());
        c.setTalonario(dto.getTalonario());
        c.setNroComprobante(dto.getNroComprobante());
        c.setSector(dto.getSector());
        c.setMoneda(dto.getMoneda());
        c.setFormaDePago(dto.getFormaDePago());
        c.setObservaciones(dto.getObservaciones());
        c.setTotal(new BigDecimal(0));
        c.setTipoOperacion(tipoOperacionServicio.obtenerPorId(3l));
        talonarioServicio.aumentarUltimoNro(dto.getTalonario());
        cobroRepo.save(c);

        Cobro ultimoCobro = cobroRepo.findTopByOrderByIdDesc();
        formaDePagoDetalleServicio.crearSinSubDetalle(ultimoCobro.getId(), ultimoCobro.getTipoOperacion().getId(), ultimoCobro.getTotal());
    }

    @Transactional
    public void actualizar(Cobro dto, String fechaComprobante) {
        Cobro c = cobroRepo.findById(dto.getId()).get();
        Long idFormaDePagoAnterior;
        try {
            idFormaDePagoAnterior = c.getFormaDePago().getId();
        } catch (Exception e) {
            idFormaDePagoAnterior = null;
        }

        c.setCliente(dto.getCliente());
        c.setCuit(dto.getCuit());
        c.setTipoIva(dto.getTipoIva());
        c.setFechaComprobante(LocalDate.parse(fechaComprobante));
        c.setTipoComprobante(dto.getTipoComprobante());
        if (c.getTalonario().getNroTalonario() != dto.getTalonario().getNroTalonario()) {
            talonarioServicio.aumentarUltimoNro(dto.getTalonario());
            c.setTalonario(dto.getTalonario());
        }
        c.setNroComprobante(dto.getNroComprobante());
        c.setSector(dto.getSector());
        c.setMoneda(dto.getMoneda());
        c.setFormaDePago(dto.getFormaDePago());
        c.setObservaciones(dto.getObservaciones());
        cobroRepo.save(c);

        if (c.getFormaDePago() != null) {
            // Si ya existe el detalleDePago verifico si cambia la formaDePago para operar
            if ( idFormaDePagoAnterior != c.getFormaDePago().getId() ) {
                if (c.getFormaDePago().getId() == 53) { //La forma de pago ha cambiado y si es aDetallar => eliminar items detalleDePago
                    formaDePagoDetalleServicio.eliminarSubDetalles(c.getId(), c.getTipoOperacion().getId());
                } else {            // Si no es aDetallar => generar automaticamente el item detalleDepago
                    formaDePagoDetalleServicio.eliminarSubDetalles(c.getId(), c.getTipoOperacion().getId());
                    formaDePagoDetalleServicio.crearSubDetalleAutomatico(c.getId(), c.getTipoOperacion().getId(), c.getTotal(), c.getFormaDePago());
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Cobro> obtenerTodos(){ return cobroRepo.findAll(); }

    @Transactional(readOnly = true)
    public Cobro obtenerPorId(Long id){ return cobroRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ // Cuando elimino subdetalles debo descontar el importe del saldo de la cuenta Banco/Tarjeta
        formaDePagoDetalleServicio.eliminarSubDetalles(id,3l);
        formaDePagoDetalleServicio.eliminarMaestro(id, 3l);
        cobroRepo.deleteById(id); }

    @Transactional(readOnly = true)
    public Long buscarUltimoId() { return cobroRepo.buscarUltimoId();    }

    @Transactional
    public void actualizarTotal(Long idCobro) {

        BigDecimal resultado = BigDecimal.ZERO;
        Cobro c = cobroRepo.findById(idCobro).get();
        BigDecimal totalCuota = cobroRepo.obtenerTotalCuotas(idCobro).orElse(BigDecimal.ZERO);
        BigDecimal totalCtaCte = cobroRepo.obtenerTotalCtaCte(idCobro).orElse(BigDecimal.ZERO);
        BigDecimal totalAdelanto = cobroRepo.obtenerTotalAdelanto(idCobro).orElse(BigDecimal.ZERO);


        resultado = totalCuota.add(totalCtaCte).add(totalAdelanto);
        c.setTotal(resultado);
        formaDePagoDetalleServicio.actualizarTotal(c.getId(), 3l, resultado);
        cobroRepo.save(c);

    }

    @Transactional
    public BigDecimal obtenerTotalPorId(Long id) {
        return cobroRepo.obtenerTotalPorId(id);
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerTotalMensual(){
        return cobroRepo.obtenerTotalCobradoMensual();
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerSaldoCliente(Long idOperacion){
        Long fkCliente = cobroRepo.obtenerCliente(idOperacion);
        return clienteServicio.obtenerSaldo(fkCliente);
    }

    @Transactional(readOnly = true)
    public Long obtenerCliente(Long idOperacion) {
        return cobroRepo.obtenerCliente(idOperacion);
    }
}
