package com.grupop.gestion.Servicios;

import com.grupop.gestion.DTO.CobrosDTO;
import com.grupop.gestion.Entidades.Cobro;
import com.grupop.gestion.Repositorios.CobroRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CobroServicio {


    private final CobroRepo cobroRepo;
    private final TalonarioServicio talonarioServicio;
    private final TipoOperacionServicio tipoOperacionServicio;
    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;
    private final ClienteServicio clienteServicio;
    private final ImageService imageService;
    private final EntidadBaseServicio entidadBaseServicio;

    @Transactional
    public void crear(Cobro dto, String fechaComprobante, MultipartFile photo) {
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

        if(photo != null && !photo.isEmpty()) c.setImage(imageService.copy(photo));

        talonarioServicio.aumentarUltimoNro(talonarioServicio.obtenerPorNroTalonario(dto.getTalonario()));
        cobroRepo.save(c);

        Cobro ultimoCobro = cobroRepo.findTopByOrderByIdDesc();
        formaDePagoDetalleServicio.crearSinSubDetalle(ultimoCobro.getId(), ultimoCobro.getTipoOperacion().getId(), ultimoCobro.getTotal());
    }

    @Transactional
    public void actualizar(Cobro dto, String fechaComprobante, MultipartFile photo) {
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
        if (c.getTalonario() != dto.getTalonario()) {
            talonarioServicio.aumentarUltimoNro(talonarioServicio.obtenerPorNroTalonario(dto.getTalonario()));
            c.setTalonario(dto.getTalonario());
        }
        c.setNroComprobante(dto.getNroComprobante());
        c.setSector(dto.getSector());
        c.setMoneda(dto.getMoneda());
        c.setFormaDePago(dto.getFormaDePago());
        c.setObservaciones(dto.getObservaciones());
        if(photo != null && !photo.isEmpty()) c.setImage(imageService.copy(photo));
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
    public Page<Cobro> obtenerTodos(int page, int size){ return cobroRepo.findAllByOrderByIdDesc(PageRequest.of(page, size)); }

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


    @Transactional(readOnly = true)
    public List<CobrosDTO> obtenerOperaciones(String fechaDesde, String fechaHasta, Long sectorId, Integer talDesde, Integer talHasta, Boolean excluirTal, Long idFormaPago ){
        if(excluirTal){
            List<Cobro> listaCobro = cobroRepo.obtenerOperacionesExcluyendoTalonario(fechaDesde, fechaHasta, sectorId, talDesde, talHasta, idFormaPago);
            List<CobrosDTO> listaCobrosDTO = new ArrayList<>();

            for ( Cobro c : listaCobro ) {
                CobrosDTO cobrosDTO = new CobrosDTO();
                cobrosDTO.setId(c.getId());
                cobrosDTO.setCliente(entidadBaseServicio.obtenerNombrePorFkCliente(c.getCliente().getId()).getRazonSocial());
                cobrosDTO.setFechaComprobante(c.getFechaComprobante());
                cobrosDTO.setTalonario(c.getTalonario());
                cobrosDTO.setNroComprobante(c.getNroComprobante());
                cobrosDTO.setSector(c.getSector().getDescripcion());
                cobrosDTO.setFormaPago(c.getFormaDePago().getDescripcion());
                cobrosDTO.setTotal(c.getTotal());
                listaCobrosDTO.add(cobrosDTO);
            }
            return listaCobrosDTO;
        }else {

            List<Cobro> listaCobro = cobroRepo.obtenerOperaciones(fechaDesde, fechaHasta, sectorId, talDesde, talHasta, idFormaPago);
            List<CobrosDTO> listaCobrosDTO = new ArrayList<>();

            for (Cobro c: listaCobro) {
                CobrosDTO cobrosDTO = new CobrosDTO();
                cobrosDTO.setId(c.getId());
                cobrosDTO.setCliente(entidadBaseServicio.obtenerNombrePorFkCliente(c.getCliente().getId()).getRazonSocial());
                cobrosDTO.setFechaComprobante(c.getFechaComprobante());
                cobrosDTO.setTalonario(c.getTalonario());
                cobrosDTO.setNroComprobante(c.getNroComprobante());
                cobrosDTO.setSector(c.getSector().getDescripcion());
                cobrosDTO.setFormaPago(c.getFormaDePago().getDescripcion());
                cobrosDTO.setTotal(c.getTotal());
                listaCobrosDTO.add(cobrosDTO);
            }
            return listaCobrosDTO;
        }

    }


}
