package com.grupop.gestion.Servicios;

import com.grupop.gestion.DTO.CobroDetalleCuotasDto;
import com.grupop.gestion.DTO.CobroDetalleCuotasReporteDto;
import com.grupop.gestion.DTO.CobrosDTO;
import com.grupop.gestion.DTO.OperacionesDTO;
import com.grupop.gestion.Entidades.Cobro;
import com.grupop.gestion.Entidades.CobroDetalleCuotas;
import com.grupop.gestion.Entidades.Credito;
import com.grupop.gestion.Entidades.CreditoDetalle;
import com.grupop.gestion.Repositorios.CobroRepo;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void actualizar(Cobro dto, String fechaComprobante, MultipartFile photo) throws Exception {
        if (dto.getFormaDePago() == null && existsByCobro(dto.getId())) {
            throw new Exception("Antes de guardar debe seleccionar una forma de pago");
        } else {
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
            if (photo != null && !photo.isEmpty()) c.setImage(imageService.copy(photo));
            cobroRepo.save(c);

            if (c.getFormaDePago() != null) {
                // Si ya existe el detalleDePago verifico si cambia la formaDePago para operar
                if (idFormaDePagoAnterior != c.getFormaDePago().getId()) {
                    if (c.getFormaDePago().getId() == 53) { //La forma de pago ha cambiado y si es aDetallar => eliminar items detalleDePago
                        formaDePagoDetalleServicio.eliminarSubDetalles(c.getId(), c.getTipoOperacion().getId());
                    } else {            // Si no es aDetallar => generar automaticamente el item detalleDepago
                        formaDePagoDetalleServicio.eliminarSubDetalles(c.getId(), c.getTipoOperacion().getId());
                        formaDePagoDetalleServicio.crearSubDetalleAutomatico(c.getId(), c.getTipoOperacion().getId(), c.getTotal(), c.getFormaDePago());
                    }
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
            List<CobrosDTO> listaOperacionesDTO = new ArrayList<>();

            for ( Cobro c : listaCobro ) {
                CobrosDTO opDto = new CobrosDTO();
                opDto.setId(c.getId());
                opDto.setEntidad(entidadBaseServicio.obtenerNombrePorFkCliente(c.getCliente().getId()).getRazonSocial());
                opDto.setFechaComprobante(c.getFechaComprobante());
                opDto.setTalonario(c.getTalonario());
                opDto.setNroComprobante(c.getNroComprobante());
                opDto.setSector(c.getSector().getDescripcion());
                opDto.setFormaPago(c.getFormaDePago().getDescripcion());
                opDto.setTotal(c.getTotal());
                listaOperacionesDTO.add(opDto);
            }
            return listaOperacionesDTO;
        }else {

            List<Cobro> listaCobro = cobroRepo.obtenerOperaciones(fechaDesde, fechaHasta, sectorId, talDesde, talHasta, idFormaPago);
            List<CobrosDTO> listaOperacionesDTO = new ArrayList<>();

            for (Cobro c: listaCobro) {
                CobrosDTO opDto = new CobrosDTO();
                opDto.setId(c.getId());
                opDto.setEntidad(entidadBaseServicio.obtenerNombrePorFkCliente(c.getCliente().getId()).getRazonSocial());
                opDto.setFechaComprobante(c.getFechaComprobante());
                opDto.setTalonario(c.getTalonario());
                opDto.setNroComprobante(c.getNroComprobante());
                opDto.setSector(c.getSector().getDescripcion());
                opDto.setFormaPago(c.getFormaDePago().getDescripcion());
                opDto.setTotal(c.getTotal());
                listaOperacionesDTO.add(opDto);
            }
            return listaOperacionesDTO;
        }

    }


    @Transactional(readOnly = true)
    public boolean existsByCobro(Long idCobro){
        if(cobroRepo.existsByCobro(idCobro) == 0 && cobroRepo.existsImputacionByCobro(idCobro) == 0) return false;
        else return true;
    }


    public ResponseEntity<byte[]> exportInvoice(Long idCobro) {
        try {
            InputStream jasperStream = CreditoServicio.class.getResourceAsStream("/static/reportes/ReporteRecibo.jasper");

            Cobro cobro = cobroRepo.findById(idCobro).get();

//            List<CobroDetalleCuotas> listaItems = cobroRepo.obtenerLineasDetalle(idCobro);
            List<CobroDetalleCuotasReporteDto> listaItemsDTO = new ArrayList<>();
            BigDecimal subtotal = BigDecimal.ZERO;

            CobroDetalleCuotasReporteDto cd = new CobroDetalleCuotasReporteDto();
            cd.setDescripcion(null);
            cd.setIntereses(null);
            cd.setTotal(null);
            listaItemsDTO.add(0, cd);

//            for (CobroDetalleCuotas c:listaItems ) {
//                CobroDetalleCuotasReporteDto cDTO = new CobroDetalleCuotasReporteDto();
//                cDTO.setDescripcion("Venta: " + c.getVentaId().getId() + " Credito: " + c.getCreditoId().getId() + "("+ c.getCreditoId().getTalonario() + "-"+ c.getCreditoId().getNroComprobante() +")" +
//                        " Cuota Nro. " + c.getNroCuota() +"/" + c.getCreditoId().getPlanPago().getCantCuota() + " Fecha Venc. " + c.getFechaVencimiento());
//                cDTO.setIntereses(BigDecimal.ZERO);
//                cDTO.setTotal(c.getImporteFinal());
//                subtotal = subtotal.add(c.getImporteFinal());
//                listaItemsDTO.add(cDTO);
//            }



            JRDataSource dataSource = new JRBeanCollectionDataSource(listaItemsDTO);
            InputStream logoStream = CreditoServicio.class.getResourceAsStream("/static/img/logo_grupop.png");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("logoEmpresa", logoStream);
            parametros.put("ds", dataSource);
            parametros.put("cliente", entidadBaseServicio.obtenerNombrePorFkCliente(cobro.getCliente().getId()).getRazonSocial());
            parametros.put("cobroId", cobro.getId());
            parametros.put("nroRecibo", (cobro.getTalonario()+"-"+cobro.getNroComprobante()).toString() ) ;
            parametros.put("subtotal", subtotal);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parametros, dataSource);

            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", generarNombreArchivo(cobro.getTalonario(), cobro.getNroComprobante()));
            System.out.println("Reporte generado exitosamente");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);

        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }
    }

    public static String generarNombreArchivo(Integer talonario , String nroComprobante) {
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String fechaHoraFormateada = fechaHoraActual.format(formateador);
        return "ReporteReciboCobro" + talonario+ "-" + nroComprobante + "_" + fechaHoraFormateada + ".pdf";
    }


}
