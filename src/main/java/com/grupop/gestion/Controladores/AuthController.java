package com.grupop.gestion.Controladores;

import com.grupop.gestion.Servicios.CobroServicio;
import com.grupop.gestion.Servicios.CompraServicio;
import com.grupop.gestion.Servicios.PagoServicio;
import com.grupop.gestion.Servicios.VentaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class AuthController {

    private final VentaServicio ventaServicio;
    private final CompraServicio compraServicio;
    private final CobroServicio cobroServicio;
    private final PagoServicio pagoServicio;

    @GetMapping
    public ModelAndView home(){
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("totalVendido", ventaServicio.obtenerTotalMensual() );
        mav.addObject("totalComprado", compraServicio.obtenerTotalMensual());
        mav.addObject("totalCobrado", cobroServicio.obtenerTotalMensual());
        mav.addObject("totalPagado", pagoServicio.obtenerTotalMensual());
        return mav;
    }

    @GetMapping("/xxx")
    public ModelAndView query(){ return new ModelAndView("xxx"); };

}
