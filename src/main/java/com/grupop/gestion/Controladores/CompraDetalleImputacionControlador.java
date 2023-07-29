package com.grupop.gestion.Controladores;

import com.grupop.gestion.DTO.CompraDetalleImputacionDto;
import com.grupop.gestion.Entidades.Impuestos;
import com.grupop.gestion.Mappers.ImpuestoMapper;
import com.grupop.gestion.Servicios.CompraDetalleImputacionServicio;
import com.grupop.gestion.Servicios.CompraServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("compraDetalleImputacion")
public class CompraDetalleImputacionControlador {

    private final CompraDetalleImputacionServicio compraDetalleImputacionServicio;
    private final CompraServicio compraServicio;

    @PostMapping("altaDetalle")
    public ResponseEntity<String> altaDetalle(@RequestBody CompraDetalleImputacionDto request){
        try{
            Long compraId = request.getCompraId();
            Long cuentaContableid = request.getCuentaContableId();
            BigDecimal importeBase = request.getImporteBase();
            List<Long> impuestosIds = request.getImpuestosIds();
            compraDetalleImputacionServicio.crear(compraId, cuentaContableid, importeBase,impuestosIds);

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado correctamente");
    }

    @PostMapping("/bajaDetalle/{idCompra}/{idCuenta}")
    public ResponseEntity<String> bajaDetalle(@PathVariable Long idCompra, @PathVariable Long idCuenta, RedirectAttributes attributes){
        try{
            compraDetalleImputacionServicio.eliminar(idCompra, idCuenta);
            attributes.addFlashAttribute("exito", "Se guardaron los cambios correctamente");
        } catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro eliminado correctamente");
    }
}
