package com.grupop.gestion.Servicios;

import com.grupop.gestion.DTO.CreditoDetalleDto;
import com.grupop.gestion.Entidades.*;
import com.grupop.gestion.Repositorios.CreditoRepo;
import jdk.swing.interop.SwingInterOpUtils;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CreditoServicio {

    private final CreditoRepo creditoRepo;
    private final VentaServicio ventaServicio;
    private final PlanPagoServicio planPagoServicio;
    private final CreditoDetalleServicio creditoDetalleServicio;
    private final FormaDePagoDetalleSubDetalleServicio formaDePagoDetalleSubDetalleServicio;
    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;
    private final EmailService emailService;
    private final EntidadBaseServicio entidadBaseServicio;
    private final IndiceCacServicio indiceCacServicio;
    private final EstadoCreditoServicio estadoCreditoServicio;


    @Transactional
    public void crear(Credito dto, String venceLosDias) {
        PlanPago plan = planPagoServicio.obtenerPorId(dto.getPlanPago().getId());

        Credito c = new Credito();
        c.setEstadoCredito(estadoCreditoServicio.obtenerPorId(1l));
        c.setCliente(dto.getCliente());
        c.setFecha(dto.getFecha());
        c.setSector(dto.getSector());
        c.setTipoComprobante(dto.getTipoComprobante());
        c.setTalonario(dto.getTalonario());
        c.setNroComprobante(dto.getNroComprobante());
        c.setVenta(dto.getVenta());
        c.setPlanPago(dto.getPlanPago());
        c.setCantCuotas(dto.getCantCuotas());
        c.setPorcentajeInteres(dto.getPorcentajeInteres());
        c.setVencimiento(dto.getVencimiento());
        c.setInteresesTotales(dto.getCapital().multiply(dto.getPorcentajeInteres()).divide(new BigDecimal(100)));
        c.setGastosAdministrativos(dto.getCapital().multiply(plan.getPorcentajeGastos()).divide(new BigDecimal(100)));
        c.setCapital(dto.getCapital());
        c.setTotalCredito(c.getCapital().add(c.getInteresesTotales()).add(c.getGastosAdministrativos()));
        c.setObservaciones(dto.getObservaciones());
        c.setBloqueado(false);
        c.setDetallePago(formaDePagoDetalleSubDetalleServicio.obtenerPorIdOperacionAndIdTipoOperacion(c.getId(), 1l));
        c.setRefinancia(BigDecimal.ZERO);
        creditoRepo.save(c);


        //GENERACION DE LA PRIMER FECHA DE VENCIMIENTO DEL CREDITO
        //DESESTRUCTURACION DE LA FECHA DE VENCIMIENTO POR DEFOULT
        LocalDate fechaActual = LocalDate.now();
        int anioActual = fechaActual.getYear();
        Month mesActual = fechaActual.getMonth();
        int diaVencimiento = Integer.parseInt(venceLosDias);

        //SI LA FECHA ACTUAL SE PASA DE LA FECHA DE VENC POR DEFOULT PASO AL SGTE MES
        if (fechaActual.getDayOfMonth() >= diaVencimiento) {
            mesActual = mesActual.plus(1);
            if (mesActual == Month.JANUARY) {
                anioActual = anioActual + 1;
            }
        }

        Credito credito = buscarUltimoCredito();


        for (int i = 1; i <= credito.getCantCuotas(); i++) {

            LocalDate fechaVencimiento = LocalDate.of(anioActual, mesActual, diaVencimiento);
            creditoDetalleServicio.generarCuotas(credito, i, credito.getTotalCredito().divide(new BigDecimal(credito.getCantCuotas())), fechaVencimiento, credito.getCliente(), credito.getGastosAdministrativos().divide(new BigDecimal(credito.getCantCuotas()), 4, RoundingMode.UP), c.getEstadoCredito());


            if (mesActual == Month.DECEMBER) {
                mesActual = mesActual.plus(1);
                anioActual++;
            } else {
                mesActual = mesActual.plus(1);
            }
        }

//        CIERRO LA VENTA PARA QUE NO SE PUEDA MODIFICAR NADA
        ventaServicio.cerrarVenta(dto.getVenta().getId(), true);
//        CIERRO EL DETALLE DE PAGO DE LA VENTA
        formaDePagoDetalleServicio.cerrarDetallePago(dto.getVenta().getId(), dto.getVenta().getTipoOperacion().getId());
//    HAGO UN ENVIO DE EMAIL
//        String email="lucascaro97@gmail.com";
//        emailService.send(email, "credito", c.getVenta().getNroComprobante(), c.getCliente().getId(), c.getTotalCredito(), c.getPlanPago());
    }

    @Transactional(rollbackFor = Exception.class)
    public void actualizar(Credito dto) throws Exception {
        Credito c = creditoRepo.findById(dto.getId()).get();

        if (c.isBloqueado()) {
            c.setObservaciones(dto.getObservaciones());
            creditoRepo.save(c);
        } else {

            PlanPago plan = planPagoServicio.obtenerPorId(dto.getPlanPago().getId());

            c.setCliente(dto.getCliente());
            c.setFecha(dto.getFecha());
            c.setSector(dto.getSector());
            c.setTipoComprobante(dto.getTipoComprobante());
            c.setTalonario(dto.getTalonario());
            c.setNroComprobante(dto.getNroComprobante());
            c.setVenta(dto.getVenta());
            c.setCapital(dto.getCapital());
            c.setPlanPago(dto.getPlanPago());
            c.setCantCuotas(dto.getCantCuotas());
            c.setPorcentajeInteres(dto.getPorcentajeInteres());
            c.setVencimiento(dto.getVencimiento());
            c.setInteresesTotales(c.getCapital().multiply(c.getPorcentajeInteres()).divide(new BigDecimal(100)));
            c.setGastosAdministrativos(dto.getCapital().multiply(plan.getPorcentajeGastos()).divide(new BigDecimal(100)));
            c.setTotalCredito(c.getCapital().add(c.getInteresesTotales()).add(c.getGastosAdministrativos()));
            c.setObservaciones(dto.getObservaciones());

            if(c.getEstadoCredito().getId() != dto.getEstadoCredito().getId()){
                try{
                    if (dto.getEstadoCredito().getId() != null )    c.setEstadoCredito(dto.getEstadoCredito());
                    if(dto.getEstadoCredito().getId() != 1l && dto.getEstadoCredito().getId() != null) c.setBloqueado(true);
                    if(dto.getEstadoCredito().getId() == 5 && dto.getEstadoCredito().getId() != null){
                        System.out.println("Marcar lote como disponible");
                        ventaServicio.alterarEstadoLotes(c.getVenta().getId(), 1l);
                    }
                    creditoRepo.save(c);
                    creditoDetalleServicio.actualizarEstadoCuotasConSaldo(dto);

                }catch (Exception e){
                    throw new Exception(e);
                }

            }else{
                creditoRepo.save(c);
            }

        }
    }

    @Transactional(readOnly = true)
    public Page<Credito> obtenerTodos(int page, int size) {
        return creditoRepo.findAllByOrderByIdDesc(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Credito obtenerPorId(Long id) {
        return creditoRepo.findById(id).get();
    }

    @Transactional
    public void eliminarPorId(Long id) {
        creditoRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Integer validarExistenciaPorVenta(Long idVenta) {
        return creditoRepo.existByIdVenta(idVenta);
    }


    @Transactional(readOnly = true)
    public Long buscarUltimoId() {
        return creditoRepo.buscarUltimoId();
    }

    @Transactional(readOnly = true)
    public Credito buscarUltimoCredito() {
        return creditoRepo.findFirstByOrderByIdDesc();
    }

    public void regenerarCuotas(Long idCredito, List<CreditoDetalleDto> arrayListB) {
        Credito c = creditoRepo.findById(idCredito).get();
        List<CreditoDetalle> arrayListA = creditoDetalleServicio.obtenerLineasDetalle(c.getId());

        boolean montoHaCambiado = false;
        boolean fechaHaCambiado = false;
        LocalDate nuevaFecha = LocalDate.now();
        BigDecimal acumulado = BigDecimal.ZERO;
        Integer contador = 0;
        BigDecimal montoCuotasRestantes = BigDecimal.ZERO;

        for (int i = 0; i < arrayListA.size(); i++) {
            BigDecimal montoA = arrayListA.get(i).getMonto();
            BigDecimal montoB = arrayListB.get(i).getMonto();
            LocalDate fechaA = arrayListA.get(i).getVencimiento();
            LocalDate fechaB = arrayListB.get(i).getVencimiento();


            if (!montoA.equals(montoB)) {
                montoHaCambiado = true;
                arrayListA.get(i).setMonto(montoB);
                arrayListA.get(i).setCapital(montoB.subtract(arrayListA.get(i).getGastoAdm()));
                arrayListA.get(i).setSaldo(montoB);

                acumulado = acumulado.add(montoB);
                contador++;
            }

            if (!fechaA.equals(fechaB)) {
                fechaHaCambiado = true;
                nuevaFecha = fechaB;
            }
        }

        if (montoHaCambiado) {
            montoCuotasRestantes = c.getTotalCredito().subtract(acumulado).divide(new BigDecimal(c.getCantCuotas() - contador), 2, RoundingMode.HALF_UP);

            for (int i = 0; i < arrayListA.size(); i++) {
                Integer nroCuota = arrayListA.get(i).getNroCuota();
                if (nroCuota > contador) {

                    arrayListA.get(i).setMonto(montoCuotasRestantes);
                    arrayListA.get(i).setCapital(montoCuotasRestantes.subtract(arrayListA.get(i).getGastoAdm()));
                    arrayListA.get(i).setSaldo(montoCuotasRestantes);
                }
            }

            creditoDetalleServicio.actualizarCuotas(arrayListA);

        } else {
            System.out.println("No es necesario recalcular cuotas ya que ninguna ha cambiado");
        }

        if (fechaHaCambiado) {
            for (int i = 0; i < arrayListA.size(); i++) {
                arrayListA.get(i).setVencimiento(nuevaFecha);
                nuevaFecha = nuevaFecha.plusMonths(1);
            }
            creditoDetalleServicio.actualizarCuotasFechas(arrayListA);
        } else {
            System.out.println("No es necesario recalcular fechas ya que ninguna ha cambiado");
        }
    }

    @Transactional(readOnly = true)
    public boolean validarEstado(Long idCred) {
        return creditoRepo.validarEstado(idCred);
    }

    @Transactional
    public void marcarComoBloqueado(Long idCred) {
        Credito c = creditoRepo.findById(idCred).get();
        c.setBloqueado(true);
        creditoRepo.save(c);
    }

    public static String generarNombreArchivo(Long idCredito) {
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String fechaHoraFormateada = fechaHoraActual.format(formateador);
        return "ReporteCredito_" + idCredito + "_" + fechaHoraFormateada + ".pdf";
    }

    public ResponseEntity<byte[]> exportInvoice(Long idCredito) {
        try {
            InputStream jasperStream = CreditoServicio.class.getResourceAsStream("/static/reportes/ReporteCredito.jasper");

            Credito credito = creditoRepo.findById(idCredito).get();
            List<CreditoDetalle> listaCuotas = creditoDetalleServicio.obtenerLineasDetalle(idCredito);
            CreditoDetalle cd = new CreditoDetalle();
            cd.setId(null);
            cd.setCreditoId(null);
            cd.setCliente(null);
            cd.setNroCuota(null);
            cd.setCapital(null);
            cd.setGastoAdm(null);
            cd.setMonto(null);
            cd.setVencimiento(null);
            cd.setSaldo(null);
            cd.setCobrado(null);
            listaCuotas.add(0, cd);

            JRDataSource dataSource = new JRBeanCollectionDataSource(listaCuotas);
            InputStream logoStream = CreditoServicio.class.getResourceAsStream("/static/img/logo_grupop.png");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("logoEmpresa", logoStream);
            parametros.put("ds", dataSource);
            parametros.put("cliente", entidadBaseServicio.obtenerNombrePorFkCliente(credito.getCliente().getId()).getRazonSocial());
            parametros.put("plan", credito.getPlanPago().getDescripcion());
            parametros.put("cantCuotas", credito.getPlanPago().getCantCuota());

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parametros, dataSource);

            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", generarNombreArchivo(idCredito));
            System.out.println("Reporte generado exitosamente");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);

        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }
    }

    @Transactional(readOnly = true)
    public List<CreditoDetalleDto> obtenerCuotasCobrarMensual() {
        List<CreditoDetalle> listaCuotas = creditoDetalleServicio.obtenerCuotasCobrarMensual();
        List<CreditoDetalleDto> listaCuotasDTO = new ArrayList<>();



        for (CreditoDetalle c: listaCuotas ) {
            CreditoDetalleDto creditoDetalleDto = new CreditoDetalleDto();
            creditoDetalleDto.setId(c.getId());
            creditoDetalleDto.setCliente(entidadBaseServicio.obtenerNombrePorFkCliente(c.getCliente().getId()).getRazonSocial());
            creditoDetalleDto.setIdCredito(c.getCreditoId().getId());
            creditoDetalleDto.setNroCuota(c.getNroCuota());
            creditoDetalleDto.setCapital(c.getCapital());
            creditoDetalleDto.setGastoAdm(c.getGastoAdm());
            creditoDetalleDto.setMonto(c.getMonto());
            creditoDetalleDto.setVencimiento(c.getVencimiento());
            creditoDetalleDto.setSaldo(c.getSaldo());
            //CALCULO SI TIENE DIAS ATRASADOS
            Long diferenciaEnDias = ChronoUnit.DAYS.between(creditoDetalleDto.getVencimiento(), LocalDate.now());
            creditoDetalleDto.setDiasAtrasados( (diferenciaEnDias > 0 ) ? diferenciaEnDias.toString() : "0");
            creditoDetalleDto.setAjusteCac(calcularAjuste(creditoDetalleDto.getIdCredito(), creditoDetalleDto.getSaldo()));
            //SI TIENE DIAS ATRASADOS, CALCULO EL INTERES PUNITORIO DEPENDIENDO DEL % ESTIPULADO EN EL PLAN DE PAGO
            creditoDetalleDto.setInteresPun((diferenciaEnDias > 0) ? calcularInteresPunitorio(creditoDetalleDto.getIdCredito(), creditoDetalleDto.getSaldo(), diferenciaEnDias, creditoDetalleDto.getAjusteCac()) : BigDecimal.ZERO);
            creditoDetalleDto.setTotal(creditoDetalleDto.getAjusteCac().add(creditoDetalleDto.getInteresPun()).add(creditoDetalleDto.getSaldo()));
            listaCuotasDTO.add(creditoDetalleDto);

            System.out.println(creditoDetalleDto);
        }

        return listaCuotasDTO;




    }

    public BigDecimal calcularInteresPunitorio(Long idCredito, BigDecimal saldo, Long diasVencidos, BigDecimal ajuste){
        Credito credito = creditoRepo.findById(idCredito).get();
        PlanPago plan = planPagoServicio.obtenerPorId(credito.getPlanPago().getId());

        BigDecimal saldoPlusAjuste = saldo.add(ajuste);
        BigDecimal interesPun = saldoPlusAjuste.multiply(plan.getInteresPunitorio()).multiply(new BigDecimal(diasVencidos)).divide(new BigDecimal(100), 2, RoundingMode.UP);
        return interesPun;
    }

    public BigDecimal calcularAjuste(Long idCredito, BigDecimal saldo){
        Credito credito = creditoRepo.findById(idCredito).get();
        PlanPago plan = planPagoServicio.obtenerPorId(credito.getPlanPago().getId());

        if(plan.getTablaCac()){
            LocalDate fechaActual = LocalDate.now();
            fechaActual = fechaActual.minusMonths(2);
            BigDecimal indiceActual = null;
            BigDecimal indiceBase = null;


            //OBTENGO EL INDICE ACTUAL
            do{
                indiceActual = indiceCacServicio.obtenerIndiceBase(fechaActual.getMonthValue(), fechaActual.getYear());
                fechaActual = fechaActual.minusMonths(1);
            }while(indiceActual == null);

            // 3 variantes de formas de obtener el indice BASE
            //PRIMERO VERIFICO QUE EL INDICE BASE ESTE ESTIPULADO EN LA OPERACION DE VENTA SI NO ESTA ==> 0
            indiceBase = ventaServicio.obtenerIndiceBase(credito.getVenta().getId());
            //SEGUNDO OBTENGO EL INDICE BASE DEL PRIMER VENCIMIENTO  (MES - 2)
            if(indiceBase == BigDecimal.ZERO ){
            LocalDate fechaPrimerVencimiento = creditoDetalleServicio.obtenerFechaPrimerVencimiento(credito.getId());
            fechaPrimerVencimiento = fechaPrimerVencimiento.minusMonths(2);
            indiceBase = indiceCacServicio.obtenerIndiceBase(fechaPrimerVencimiento.getMonthValue(), fechaPrimerVencimiento.getYear());
            }
            //TERCERO OBTENGO EL INDICE BASE DEL PRIMER VENCIMIENTO -3
            if(indiceBase.compareTo(indiceActual) > 0 ){
                LocalDate fechaPrimerVencimiento = creditoDetalleServicio.obtenerFechaPrimerVencimiento(credito.getId());
                fechaPrimerVencimiento = fechaPrimerVencimiento.minusMonths(3);
                indiceBase = indiceCacServicio.obtenerIndiceBase(fechaPrimerVencimiento.getMonthValue() , fechaPrimerVencimiento.getYear());
            }


            BigDecimal indice = indiceActual.divide(indiceBase, 16, RoundingMode.UP);
            BigDecimal ajuste = saldo.multiply(indice).subtract(saldo);
            return ajuste.setScale(2, RoundingMode.DOWN) ;
        }else{
            return BigDecimal.ZERO;
        }

    }


    @Transactional
    public void alterarEstado(Long idCredito, Long idEstado) {
        Credito c = creditoRepo.findById(idCredito).get();
        c.setEstadoCredito(estadoCreditoServicio.obtenerPorId(idEstado));
        creditoRepo.save(c);
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerImporteRefinancia(Long id) {
        return creditoRepo.obtenerImporteRefinancia(id);
    }

    @Transactional(readOnly = true)
    public boolean verificarSiHayUnCreditoActivoPorFkVenta(Long id) {
        if(creditoRepo.verificarSiHayUnCreditoActivoPorFkVenta(id) == 0) return false;
        else return true;
    }
}