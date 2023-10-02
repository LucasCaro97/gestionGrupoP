package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.FormaDePagoDetalle;
import com.grupop.gestion.Entidades.FormaDePagoDetalleSubDetalle;
import com.grupop.gestion.Servicios.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping("formaDePagoDetalleSubdetalle")
public class FormaDePagoDetalleSubdetalleControlador {

    private final FormaDePagoDetalleSubDetalleServicio formaDePagoDetalleSubDetalleServicio;
    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;
    private final CobroServicio cobroServicio;
    private final PagoServicio pagoServicio;
    private final ClienteServicio clienteServicio;
    private final ProveedorServicio proveedorServicio;


    @PostMapping("/alta/{idOperacion}/{idTipoOperacion}/{importe}/{idFormaDePago}")
    public ResponseEntity<String> altaDetalle(@PathVariable Long idOperacion, @PathVariable Long idTipoOperacion, @PathVariable BigDecimal importe, @PathVariable Long idFormaDePago){
        try{

            if(idFormaDePago == 16){
                //ADELANTO DE CLIENTES ( COBRO )
                formaDePagoDetalleServicio.crearSubdetalleManual(idOperacion, idTipoOperacion, idFormaDePago, importe);
//                Long idCliente = cobroServicio.obtenerCliente(idOperacion);
//                clienteServicio.descontarSaldoAFavor(idCliente, importe);
            } else if (idFormaDePago == 33) {
                //ADELANTO A PROVEEDORES ( PAGO )
                formaDePagoDetalleServicio.crearSubdetalleManual(idOperacion, idTipoOperacion, idFormaDePago, importe);
//                Long idProveedor = pagoServicio.obtenerProveedor(idOperacion);
//                proveedorServicio.descontarSaldoAFavor(idProveedor, importe);
            }else{
                formaDePagoDetalleServicio.crearSubdetalleManual(idOperacion, idTipoOperacion, idFormaDePago, importe);
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Se ha creado el registro de pago correctamente");
    }

    @GetMapping("baja/{id}")
    public RedirectView bajaDetalle(@PathVariable Long id){
        RedirectView r = new RedirectView();
        try{
            FormaDePagoDetalleSubDetalle fsub = formaDePagoDetalleSubDetalleServicio.obtenerPorId(id);
            FormaDePagoDetalle f = formaDePagoDetalleServicio.obtenerPorId(fsub.getFormaDePagoDetalle().getIdOperacion(), fsub.getFormaDePagoDetalle().getTipoOperacion());
            r.setUrl("/detalleDePago/getForm/" + f.getIdOperacion() + "/" + f.getTipoOperacion());

            if(fsub.getFormaPago().getId() == 16){
                Long idCliente = cobroServicio.obtenerCliente(f.getIdOperacion());
//                clienteServicio.devolverSaldoAFavor(idCliente, fsub.getMonto());
            } else if (fsub.getFormaPago().getId() == 33) {
                Long idProveedor = pagoServicio.obtenerProveedor(f.getIdOperacion());
                proveedorServicio.devolverSaldoAFavor(idProveedor, fsub.getMonto());
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            formaDePagoDetalleSubDetalleServicio.eliminarPorId(id);

        }

    return r;
    }

}
