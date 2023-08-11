package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Cobro;
import com.grupop.gestion.Entidades.Pago;
import com.grupop.gestion.Repositorios.PagoRepo;
import lombok.RequiredArgsConstructor;
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
            if(formaDePagoDetalleServicio.validarExistencia(p.getId(), 4l) == 0){ //Si no existe crea el detalleDePago
                if(p.getFormaDePago().getId() != 54){ // Si no es a detallar crea el detalleDePago con su subDetalle
                    formaDePagoDetalleServicio.crear(p.getId(), p.getTipoOperacion().getId(), p.getTotal(), p.getFormaDePago());
                }else{                                // Si es a detallar crea el detalleDePagoVacio
                    formaDePagoDetalleServicio.crearSinSubDetalle(p.getId(), p.getTipoOperacion().getId(), p.getTotal());
                }
            }else{                                   // Si ya existe el detalleDePago verifico si cambia la formaDePago para operar
                if(idFormaDePagoAnterior != p.getFormaDePago().getId()){
                    if(p.getFormaDePago().getId() == 54){ //La forma de pago ha cambiado y si es aDetallar => eliminar items detalleDePago
                        formaDePagoDetalleServicio.eliminarSubDetalles(p.getId(), p.getTipoOperacion().getId());
                    }else{
                        formaDePagoDetalleServicio.eliminarSubDetalles(p.getId(), p.getTipoOperacion().getId());
                        formaDePagoDetalleServicio.crearSubDetalleAutomatico(p.getId(), p.getTipoOperacion().getId(), p.getTotal(), p.getFormaDePago());
                    }
                }
            }
        }else{

            formaDePagoDetalleServicio.actualizarMonto(p.getId(), p.getTipoOperacion().getId(), p.getTotal());
            System.out.println("La forma de pago no ha cambiado");
        }
    }

    @Transactional(readOnly = true)
    public List<Pago> obtenerTodos(){
        return pagoRepo.findAll();
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
        BigDecimal resultado = new BigDecimal(0);
        Pago p = pagoRepo.findById(idPago).get();
        BigDecimal totalCtaCte = pagoRepo.obtenerTotalCtaCte(idPago);
        BigDecimal totalImp = pagoRepo.obtenerTotalImp(idPago);

        if(totalCtaCte!=null && totalImp !=null){
            resultado = totalCtaCte.add(totalImp);
        }else if(totalCtaCte!=null){
            resultado = totalCtaCte;
        }else if(totalImp!=null){
            resultado = totalImp;
        }
        p.setTotal(resultado);
        pagoRepo.save(p);
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerTotalMensual(){
        return pagoRepo.obtenerTotalPagadoMensual();
    }
}
