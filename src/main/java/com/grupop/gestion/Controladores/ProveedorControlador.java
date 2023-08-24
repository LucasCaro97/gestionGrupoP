package com.grupop.gestion.Controladores;

import com.grupop.gestion.Servicios.ProveedorServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/proveedor")
public class ProveedorControlador {

    private final ProveedorServicio proveedorServicio;

    @GetMapping("/obtenerNombre/{id}")
    public ResponseEntity<String> obtenerNombre(@PathVariable Long id){
        return ResponseEntity.ok(proveedorServicio.obtenerNombre(id));
    }

    @GetMapping("/obtenerSaldo/{idProveedor}")
    public ResponseEntity<BigDecimal> obtenerSaldo(@PathVariable Long idProveedor){
        return ResponseEntity.ok(proveedorServicio.obtenerSaldo(idProveedor));
    }

}
