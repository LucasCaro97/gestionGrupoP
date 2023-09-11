package com.grupop.gestion.Servicios;

import com.grupop.gestion.DTO.ComprasDTO;
import com.grupop.gestion.Entidades.Cobro;
import com.grupop.gestion.Entidades.Compra;
import com.grupop.gestion.Entidades.EntidadBase;
import com.grupop.gestion.Repositorios.CompraRepo;
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
public class CompraServicio {

    private final CompraRepo compraRepo;
    private final TalonarioServicio talonarioServicio;
    private final TipoOperacionServicio tipoOperacionServicio;
    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;
    private final ProveedorServicio proveedorServicio;
    private final ImageService imageService;
    private final EntidadBaseServicio entidadBaseServicio;

    @Transactional
    public void crear(Compra dto, String fechaComprobante, MultipartFile photo){
        Compra compra = new Compra();
        compra.setProveedor(dto.getProveedor());
        compra.setFechaComprobante(LocalDate.parse(fechaComprobante));
        compra.setTipoComprobante(dto.getTipoComprobante());
        compra.setTalonario(dto.getTalonario());
        compra.setNroComprobante(dto.getNroComprobante());
        compra.setSector(dto.getSector());
        compra.setFormaDePago(dto.getFormaDePago());
        compra.setObservaciones(dto.getObservaciones());
        compra.setTotal(new BigDecimal(0));
        compra.setBloqueado(false);
        compra.setTipoOperacion(tipoOperacionServicio.obtenerPorId(2l));
        talonarioServicio.aumentarUltimoNro(talonarioServicio.obtenerPorNroTalonario(dto.getTalonario()));
        if(photo != null && !photo.isEmpty()) compra.setImage(imageService.copy(photo));
        compraRepo.save(compra);

        Compra ultimaCompra = compraRepo.findTopByOrderByIdDesc();
        formaDePagoDetalleServicio.crearSinSubDetalle(ultimaCompra.getId(), ultimaCompra.getTipoOperacion().getId(), ultimaCompra.getTotal());
    }

    @Transactional
    public void actualizar(Compra dto, String fechaComprobante, MultipartFile photo){
        Compra compra = compraRepo.findById(dto.getId()).get();
        Long idFormaDePagoAnterior;
        try{
            idFormaDePagoAnterior = compra.getFormaDePago().getId();
        }catch (Exception e){
            idFormaDePagoAnterior = null;
        }


        if(compra.isBloqueado()){
            compra.setObservaciones(dto.getObservaciones());
            if(photo != null && !photo.isEmpty()) compra.setImage(imageService.copy(photo));
            compraRepo.save(compra);
        }else{

        compra.setProveedor(dto.getProveedor());
        compra.setFechaComprobante(LocalDate.parse(fechaComprobante));
        compra.setTipoComprobante(dto.getTipoComprobante());
        if(compra.getTalonario() != dto.getTalonario()){
            talonarioServicio.aumentarUltimoNro(talonarioServicio.obtenerPorNroTalonario(dto.getTalonario()));
            compra.setTalonario(dto.getTalonario());
        }
        compra.setNroComprobante(dto.getNroComprobante());
        compra.setSector(dto.getSector());
        compra.setFormaDePago(dto.getFormaDePago());
        compra.setObservaciones(dto.getObservaciones());
        if(photo != null && !photo.isEmpty()) compra.setImage(imageService.copy(photo));
        compraRepo.save(compra);

            if(compra.getFormaDePago() != null){
                // Si ya existe el detalleDePago verifico si cambia la formaDePago para operar
                if(idFormaDePagoAnterior != compra.getFormaDePago().getId()){
                    if(compra.getFormaDePago().getId() == 52){ //La forma de pago ha cambiado y si es aDetallar => eliminar items detalleDePago
                        formaDePagoDetalleServicio.eliminarSubDetalles(compra.getId(), compra.getTipoOperacion().getId());
                    }else{
                        formaDePagoDetalleServicio.eliminarSubDetalles(compra.getId(), compra.getTipoOperacion().getId());
                        formaDePagoDetalleServicio.crearSubDetalleAutomatico(compra.getId(), compra.getTipoOperacion().getId(), compra.getTotal(), compra.getFormaDePago());
                    }
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<Compra> obtenerTodas(int page, int size){ return compraRepo.findAllByOrderByIdDesc(PageRequest.of(page,size)); }

    @Transactional(readOnly = true)
    public Compra obtenerPorId(Long id){ return compraRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ compraRepo.deleteById(id); }

    @Transactional(readOnly = true)
    public Long buscarUltimoId(){ return compraRepo.findLastId(); }

    @Transactional
    public void actualizarTotal(Long idCompra, BigDecimal total){
        Compra cpa = compraRepo.findById(idCompra).get();
        cpa.setTotal(total);
        compraRepo.save(cpa);
    }

    @Transactional
    public void actualizarTotal(Long idCompra){
        BigDecimal resultado = BigDecimal.ZERO;
        Compra compra = compraRepo.findById(idCompra).get();
        BigDecimal totalProd = compraRepo.obtenerTotalProductos(compra.getId()).orElse(BigDecimal.ZERO);
        BigDecimal totalImp = compraRepo.obtenerTotalImputacion(compra.getId()).orElse(BigDecimal.ZERO);

        resultado = totalProd.add(totalImp);
        compra.setTotal(resultado);
        compraRepo.save(compra);

        formaDePagoDetalleServicio.actualizarTotal(compra.getId(), compra.getTipoOperacion().getId(), compra.getTotal());
    }

    @Transactional(readOnly = true)
    public Double obtenerTotalPorId(Long id) {
        return compraRepo.obtenerTotalPorId(id); }


    @Transactional(readOnly = true)
    public List<Compra> obtenerComprasPendientesPagoPorProveedor(Long id) {
        return compraRepo.obtenerComprasPendientesPagoPorProveedor(id);
    }

    @Transactional
    public void marcarComoBloqueado(Long id){
        Compra c = compraRepo.findById(id).get();
        c.setBloqueado(true);
        compraRepo.save(c);
    }

    @Transactional
    public void marcarComoDesbloqueado(Long id) {
        Compra c = compraRepo.findById(id).get();
        c.setBloqueado(false);
        compraRepo.save(c);
    }

    @Transactional
    public Boolean validarEstado(Long idCompra) {
        return compraRepo.validarEstado(idCompra);
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerTotalMensual(){
        return compraRepo.obtenerTotalCompradoMensual();
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerSaldoProveedor(Long idOperacion){
        Long fkProveedor = compraRepo.obtenerCliente(idOperacion);
        return proveedorServicio.obtenerSaldo(fkProveedor);
    }


    public List<ComprasDTO> obtenerOperaciones(String fechaDesde, String fechaHasta, Long sectorId, Integer talDesde, Integer talHasta, Boolean excluirTal, Long idFormaPago ){
        if(excluirTal){
            List<Compra> listaCompra = compraRepo.obtenerOperacionesExcluyendoTalonario(fechaDesde, fechaHasta, sectorId, talDesde, talHasta, idFormaPago);
            List<ComprasDTO> listaCompraDTO = new ArrayList<>();

            for (Compra c: listaCompra) {
                ComprasDTO comprasDTO = new ComprasDTO();
                comprasDTO.setId(c.getId());
                comprasDTO.setProveedor(entidadBaseServicio.obtenerNombrePorFkProveedor(c.getProveedor().getId()).getRazonSocial());
                comprasDTO.setFechaComprobante(c.getFechaComprobante());
                comprasDTO.setTalonario(c.getTalonario());
                comprasDTO.setNroComprobante(c.getNroComprobante());
                comprasDTO.setSector(c.getSector().getDescripcion());
                comprasDTO.setFormaPago(c.getFormaDePago().getDescripcion());
                comprasDTO.setTotal(c.getTotal());
                listaCompraDTO.add(comprasDTO);
            }
            return listaCompraDTO;

        }else{
            List<Compra> listaCompra = compraRepo.obtenerOperaciones(fechaDesde, fechaHasta, sectorId, talDesde, talHasta, idFormaPago);
            List<ComprasDTO> listaCompraDTO = new ArrayList<>();

            for (Compra c: listaCompra) {
                ComprasDTO comprasDTO = new ComprasDTO();
                comprasDTO.setId(c.getId());
                comprasDTO.setProveedor(entidadBaseServicio.obtenerNombrePorFkProveedor(c.getProveedor().getId()).getRazonSocial());
                comprasDTO.setFechaComprobante(c.getFechaComprobante());
                comprasDTO.setTalonario(c.getTalonario());
                comprasDTO.setNroComprobante(c.getNroComprobante());
                comprasDTO.setSector(c.getSector().getDescripcion());
                comprasDTO.setFormaPago(c.getFormaDePago().getDescripcion());
                comprasDTO.setTotal(c.getTotal());
                listaCompraDTO.add(comprasDTO);
            }
            return listaCompraDTO;
        }

    }



}

