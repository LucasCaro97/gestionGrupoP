package com.grupop.gestion.Controladores;

import com.fasterxml.jackson.core.JsonToken;
import com.grupop.gestion.Entidades.Cheque;
import com.grupop.gestion.Servicios.ChequeServicio;
import com.grupop.gestion.Servicios.EntidadBaseServicio;
import com.grupop.gestion.Servicios.TipoOperacionServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cheque")
public class ChequeControlador {

    private final ChequeServicio chequeServicio;
    private final TipoOperacionServicio tipoOperacionServicio;
    private final EntidadBaseServicio entidadBaseServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-cheque");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaCheques", chequeServicio.obtenerTodos());

        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-cheque");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null){
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("cheque", inputFlashMap.get("cheque"));
        }else{
            mav.addObject("cheque", new Cheque());
        }
        mav.addObject("action", "create");
        mav.addObject("listaTipoOperacion", tipoOperacionServicio.obtenerTodos());
        mav.addObject("listaEntidadBase", entidadBaseServicio.obtenerTodos());

        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-cheque");
        Cheque c = chequeServicio.obtenerPorId(id);
        mav.addObject("cheque", c);
        mav.addObject("action", "update");
        mav.addObject("listaTipoOperacion", tipoOperacionServicio.obtenerTodos());
        mav.addObject("listaEntidadBase", entidadBaseServicio.obtenerTodos());
        mav.addObject("fechaEmision", c.getFechaEmision() );
        mav.addObject("fechaPago", c.getFechaPago());
        mav.addObject("fechaRecepcion", c.getFechaRecepcion());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(HttpServletRequest request, Cheque dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/cheque");
        String fechaEmision = request.getParameter("fechaEmision");
        String fechaPago = request.getParameter("fechaPago");
        String fechaRecepcion = request.getParameter("fechaRecepcion");
        try {
            chequeServicio.crear(dto, fechaEmision, fechaPago, fechaRecepcion);
            attributes.addFlashAttribute("exito", "Se ha registrado el cheque correctamente");
        } catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("cheque", dto);
            r.setUrl("/cheque/form");
        }
        return r;
    }

    @PostMapping("/update")
    public RedirectView update(HttpServletRequest request, Cheque dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/cheque");
        String fechaEmision = request.getParameter("fechaEmision");
        String fechaPago = request.getParameter("fechaPago");
        String fechaRecepcion = request.getParameter("fechaRecepcion");
        try {
            chequeServicio.actualizar(dto, fechaEmision, fechaPago, fechaRecepcion);
            attributes.addFlashAttribute("exito", "Se ha actualizado el cheque correctamente");
        } catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("cheque", dto);
            r.setUrl("/cheque/form");
        }
        return r;
    }

    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/cheque");
        chequeServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente el cheque");
        return r;
    }

    @GetMapping("/obtenerTotalDisponible")
    public ResponseEntity<BigDecimal> obtenerTotalDisponible(){
        return ResponseEntity.ok(chequeServicio.obtenerTotalDisponible());
    }


}
