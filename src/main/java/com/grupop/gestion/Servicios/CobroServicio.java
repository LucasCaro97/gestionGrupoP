package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Cobro;
import com.grupop.gestion.Repositorios.CobroRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CobroServicio {


    private final CobroRepo cobroRepo;
    private final TalonarioServicio talonarioServicio;
    private final TipoOperacionServicio tipoOperacionServicio;

    @Transactional
    public void crear(Cobro dto){
        Cobro c = new Cobro();
        c.setCliente(dto.getCliente());
        c.setCuit(dto.getCuit());
        c.setTipoIva(dto.getTipoIva());
        c.setFechaComprobante(dto.getFechaComprobante());
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
    public void actualizar(Cobro dto){
        Cobro c = cobroRepo.findById(dto.getId()).get();
        c.setCliente(dto.getCliente());
        c.setCuit(dto.getCuit());
        c.setTipoIva(dto.getTipoIva());
        c.setFechaComprobante(dto.getFechaComprobante());
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
    }

    @Transactional(readOnly = true)
    public List<Cobro> obtenerTodos(){ return cobroRepo.findAll(); }

    @Transactional(readOnly = true)
    public Cobro obtenerPorId(Long id){ return cobroRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ cobroRepo.deleteById(id); }

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
        cobroRepo.save(c);
    }

    @Transactional
    public BigDecimal obtenerTotalPorId(Long id) {
        return cobroRepo.obtenerTotalPorId(id);
    }
}
