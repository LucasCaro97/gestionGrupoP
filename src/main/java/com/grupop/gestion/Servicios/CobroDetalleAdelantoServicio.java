package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.CobroDetalleAdelanto;
import com.grupop.gestion.Entidades.EntidadBase;
import com.grupop.gestion.Repositorios.CobroDetalleAdelantoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CobroDetalleAdelantoServicio {

    private final CobroDetalleAdelantoRepo cobroDetalleAdelantoRepo;
    private final ClienteServicio clienteServicio;
    private final CobroServicio cobroServicio;

    @Transactional
    public void crear(Long idCliente, Long idOperacion, BigDecimal importeAdelanto, String detalle) {
        CobroDetalleAdelanto c = new CobroDetalleAdelanto();
        c.setDetalle(detalle);
        c.setCliente(clienteServicio.buscarPorId(idCliente));
        c.setCobroId(cobroServicio.obtenerPorId(idOperacion));
        c.setImporte(importeAdelanto);
        cobroDetalleAdelantoRepo.save(c);

        clienteServicio.actualizarSaldoAFavor(idCliente, importeAdelanto);
        cobroServicio.actualizarTotal(idOperacion);

    }

    @Transactional
    public void eliminarPorId(Long id) {
        CobroDetalleAdelanto c = cobroDetalleAdelantoRepo.findById(id).get();
        clienteServicio.descontarSaldoAFavor(c.getCliente().getId(), c.getImporte());
        cobroDetalleAdelantoRepo.deleteById(id);
        cobroServicio.actualizarTotal(c.getCobroId().getId());
    }

    @Transactional
    public List<CobroDetalleAdelanto> obtenerPorCobro(Long id) {
        return cobroDetalleAdelantoRepo.obtenerPorCobro(id);
    }

    @Transactional(readOnly = true)
    public CobroDetalleAdelanto obtenerPorId(Long idAdelanto) {
        return cobroDetalleAdelantoRepo.findById(idAdelanto).get();
    }
}
