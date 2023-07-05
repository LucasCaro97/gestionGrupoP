package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Venta;
import com.grupop.gestion.Servicios.ComisionServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comision")
public class ComisionControlador {

    private final ComisionServicio comisionServicio;


    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-comisiones");
        mav.addObject("listaComisiones", comisionServicio.obtenerTodas());
        return mav;
    }

    @PostMapping("/generarComision/{idVendedor}/{idVenta}/{baseImp}/{porcentajeComision}")
    public ResponseEntity<String> altaComision(@PathVariable Long idVendedor, @PathVariable Long idVenta, @PathVariable BigDecimal baseImp,
                                               @PathVariable BigDecimal porcentajeComision, RedirectAttributes attributes){
        try{
            comisionServicio.crear(idVendedor,idVenta,baseImp,porcentajeComision);
            attributes.addFlashAttribute("exito", "Se guardaron los cambios de detalle correctamente");
            System.out.println("Exito al generar la comision");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado exitosamente");
    }

    @GetMapping("/bajaComision/{idComision}")
    public RedirectView bajaComision(@PathVariable Long idComision){
        Venta vta = comisionServicio.obtenerPorId(idComision).getVenta();
        RedirectView r = new RedirectView("/ventas/form/" + vta.getId());
        comisionServicio.eliminarPorId(idComision);
        return r;
    }

}
