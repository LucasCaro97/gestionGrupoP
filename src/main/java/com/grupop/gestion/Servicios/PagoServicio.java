package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Cobro;
import com.grupop.gestion.Entidades.Pago;
import com.grupop.gestion.Repositorios.PagoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoServicio {

    private final PagoRepo pagoRepo;
    private final TipoOperacionServicio tipoOperacionServicio;

    @Transactional
    public void crear(Pago dto){
        Pago p = new Pago();
        p.setProveedor(dto.getProveedor());
        p.setFechaComprobante(dto.getFechaComprobante());
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
    public void actualizar(Pago dto){
        Pago p = pagoRepo.findById(dto.getId()).get();
        p.setProveedor(dto.getProveedor());
        p.setFechaComprobante(dto.getFechaComprobante());
        p.setTipoComprobante(dto.getTipoComprobante());
        p.setTalonario(dto.getTalonario());
        p.setNroComprobante(dto.getNroComprobante());
        p.setSector(dto.getSector());
        p.setObservaciones(dto.getObservaciones());
        p.setFormaDePago(dto.getFormaDePago());
        pagoRepo.save(p);
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
}
