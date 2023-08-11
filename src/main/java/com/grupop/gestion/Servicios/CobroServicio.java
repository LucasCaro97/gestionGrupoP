package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Cobro;
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

    @Transactional
    public void crear(Cobro dto,  String fechaComprobante){
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
    }

    @Transactional
    public void actualizar(Cobro dto, String fechaComprobante){
        Cobro c = cobroRepo.findById(dto.getId()).get();
       Long idFormaDePagoAnterior;
       try{
           idFormaDePagoAnterior = c.getFormaDePago().getId();
       }catch (Exception e){
           idFormaDePagoAnterior = null;
       }

        c.setCliente(dto.getCliente());
        c.setCuit(dto.getCuit());
        c.setTipoIva(dto.getTipoIva());
        c.setFechaComprobante(LocalDate.parse(fechaComprobante));
        c.setTipoComprobante(dto.getTipoComprobante());
        if(c.getTalonario().getNroTalonario()!=dto.getTalonario().getNroTalonario()){
            talonarioServicio.aumentarUltimoNro(dto.getTalonario());
            c.setTalonario(dto.getTalonario());
        }
        c.setNroComprobante(dto.getNroComprobante());
        c.setSector(dto.getSector());
        c.setMoneda(dto.getMoneda());
        c.setFormaDePago(dto.getFormaDePago());
        c.setObservaciones(dto.getObservaciones());
        cobroRepo.save(c);

        if(c.getFormaDePago() != null){
            if(formaDePagoDetalleServicio.validarExistencia(c.getId(), 3l) == 0){ //Si no existe crea el detalleDePago
                if(c.getFormaDePago().getId() != 53){ // Si no es a detallar crea el detalleDePago con su subDetalle
                    formaDePagoDetalleServicio.crear(c.getId(), c.getTipoOperacion().getId(), c.getTotal(), c.getFormaDePago());
                }else{                                // Si es a detallar crea el detalleDePagoVacio
                    formaDePagoDetalleServicio.crearSinSubDetalle(c.getId(), c.getTipoOperacion().getId(), c.getTotal());
                }
            }else{                                   // Si ya existe el detalleDePago verifico si cambia la formaDePago para operar
                if(idFormaDePagoAnterior != c.getFormaDePago().getId()){
                    if(c.getFormaDePago().getId() == 53){ //La forma de pago ha cambiado y si es aDetallar => eliminar items detalleDePago
                        formaDePagoDetalleServicio.eliminarSubDetalles(c.getId(), c.getTipoOperacion().getId());
                    }else{
                        formaDePagoDetalleServicio.eliminarSubDetalles(c.getId(), c.getTipoOperacion().getId());
                        formaDePagoDetalleServicio.crearSubDetalleAutomatico(c.getId(), c.getTipoOperacion().getId(), c.getTotal(), c.getFormaDePago());
                    }
                }
            }
        }else{

            formaDePagoDetalleServicio.actualizarMonto(c.getId(), c.getTipoOperacion().getId(), c.getTotal());
            System.out.println("La forma de pago no ha cambiado");
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
        BigDecimal resultado = new BigDecimal(0);
        Cobro c = cobroRepo.findById(idCobro).get();
        BigDecimal totalCuota = cobroRepo.obtenerTotalCuotas(idCobro);
        BigDecimal totalCtaCte = cobroRepo.obtenerTotalCtaCte(idCobro);

        if(totalCuota!=null && totalCtaCte !=null){
            resultado = totalCuota.add(totalCtaCte);
        }else if(totalCuota!=null){
            resultado = totalCuota;
        }else if(totalCtaCte!=null){
            resultado = totalCtaCte;
        }
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
}
