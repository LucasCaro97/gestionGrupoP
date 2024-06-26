package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.EntidadBase;
import com.grupop.gestion.Entidades.FormaDePago;
import com.grupop.gestion.Entidades.FormaDePagoDetalle;
import com.grupop.gestion.Entidades.FormaDePagoDetalleSubDetalle;
import com.grupop.gestion.Repositorios.FormaDePagoADetallarRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FormaDePagoDetalleServicio {

    private final FormaDePagoADetallarRepo formaDePagoADetallarRepo;
    private final FormaDePagoDetalleSubDetalleServicio formaDePagoDetalleSubDetalleServicio;
    private final FormaDePagoServicio formaDePagoServicio;

    @Transactional
    //Crear Detalle de Pago automatico
    public void actualizar(Long idOperacion, Long idTipoOperacion, BigDecimal importe, FormaDePago formaDePago) {
        FormaDePagoDetalle f = formaDePagoADetallarRepo.findByIdOperacionAndTipoOperacion(idOperacion, idTipoOperacion);
        f.setTotalOperacion(importe);
        formaDePagoADetallarRepo.save(f);

        formaDePagoDetalleSubDetalleServicio.crear(f, formaDePago, importe);
    }

    @Transactional
    public void actualizarMonto(Long idOperacion, Long idTipoOperacion, BigDecimal importe){
        FormaDePagoDetalle f = formaDePagoADetallarRepo.findByIdOperacionAndTipoOperacion(idOperacion, idTipoOperacion);
        f.setTotalOperacion(importe);
        formaDePagoADetallarRepo.save(f);
    }

    @Transactional
    //Crear Detalle de Pago automatico sin detalle para carga manual
    public void crearSinSubDetalle(Long idOperacion, Long idTipoOperacion, BigDecimal importe) {
       try{
           FormaDePagoDetalle f = new FormaDePagoDetalle();
           f.setIdOperacion(idOperacion);
           f.setTipoOperacion(idTipoOperacion);
           f.setTotalOperacion(importe);
           formaDePagoADetallarRepo.save(f);
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    @Transactional(readOnly = true)
    public FormaDePagoDetalle obtenerPorId(Long idOperacion, Long tipoOperacion) {
        return formaDePagoADetallarRepo.findByIdOperacionAndTipoOperacion(idOperacion, tipoOperacion);
    }

    @Transactional
    //crea los items del detalle de pago cargados por el usuario
    public void crearSubdetalleManual(Long idOperacion, Long idTipoOperacion, Long idFormaDePago, BigDecimal importe) {
        FormaDePagoDetalle f = formaDePagoADetallarRepo.findByIdOperacionAndTipoOperacion(idOperacion, idTipoOperacion);
        FormaDePagoDetalleSubDetalle fsub = new FormaDePagoDetalleSubDetalle();
        fsub.setFormaDePagoDetalle(f);
        fsub.setFormaPago(formaDePagoServicio.obtenerPorId(idFormaDePago));
        fsub.setMonto(importe);
        formaDePagoDetalleSubDetalleServicio.crearDetalleManual(fsub);
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerTotalDetallePago(Long idOperacion, Long idTipoOperacion) {
        return formaDePagoADetallarRepo.obtenerTotal(idOperacion, idTipoOperacion);
    }

    public Integer validarExistencia(Long idOperacion, Long idTipoOperacion) {
        return formaDePagoADetallarRepo.validarExistencia(idOperacion, idTipoOperacion);
    }

    @Transactional
    public void eliminarSubDetalles(Long idOperacion, Long tipoOperacion) {
        formaDePagoDetalleSubDetalleServicio.eliminarPorIdOperacionAndTipoOperacion(idOperacion, tipoOperacion);
    }

    @Transactional
    public void crearSubDetalleAutomatico(Long idOperacion, Long idTipoOperacion, BigDecimal importe, FormaDePago formaDePago) {
        FormaDePagoDetalle f = formaDePagoADetallarRepo.findByIdOperacionAndTipoOperacion(idOperacion, idTipoOperacion);
        formaDePagoDetalleSubDetalleServicio.crear(f, formaDePago, importe);
    }

    @Transactional(readOnly = true)
    public Integer validarExistenciaSubDetalle(Long idOperacion, Long idTipoOperacion) {
        return formaDePagoADetallarRepo.validarExistenciaSubDetalle(idOperacion, idTipoOperacion);
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerCapitalCredito(Long idOperacion, Long idTipoOperacion){
        return formaDePagoADetallarRepo.obtenerCapitalCredito(idOperacion, idTipoOperacion);
    }


    @Transactional
    public void cerrarDetallePago(Long idOperacion, Long idTipoOperacion) {
        FormaDePagoDetalle f = obtenerPorId(idOperacion, idTipoOperacion);
        f.setBloqueado(true);
        formaDePagoADetallarRepo.save(f);
    }

    @Transactional(readOnly = true)
    public boolean validarEstado(Long idOperacion, Long idTipoOperacion){
        return formaDePagoADetallarRepo.obtenerEstado(idOperacion, idTipoOperacion);
    }

    public void eliminarMaestro(Long id, Long l) {
        formaDePagoADetallarRepo.eliminarPorIdOperacionAndIdTipoOperacion(id, 1l);
    }

    public void actualizarTotal(Long idOperacion, Long idTipoOperacion, BigDecimal total ){
        FormaDePagoDetalle f =  formaDePagoADetallarRepo.findByIdOperacionAndTipoOperacion(idOperacion, idTipoOperacion);
        f.setTotalOperacion(total);
        formaDePagoADetallarRepo.save(f);
    };

    @Transactional(readOnly = true)
    public Integer existsByIdOperacionAndTipoOperacionId(Long idOperacion, Long idTipoOperacion) {
       return formaDePagoADetallarRepo.existsByIdOperacionAndTipoOperacionId(idOperacion, idTipoOperacion);
    }



}
