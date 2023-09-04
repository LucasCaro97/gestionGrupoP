package com.grupop.gestion.Controladores;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupop.gestion.Entidades.ChequeSubtipo;
import com.grupop.gestion.Entidades.ChequeTipo;
import com.grupop.gestion.Servicios.ChequeSubtipoServicio;
import com.grupop.gestion.Servicios.ChequeTipoServicio;
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

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tipoSubcheque")
public class ChequeSubtipoControlador {

    private final ChequeSubtipoServicio chequeSubtipoServicio;
    private final ChequeTipoServicio chequeTipoServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-tipoSubcheques");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaTipoSubcheque", chequeSubtipoServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-tipoSubcheques");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap != null){
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("tipoSubcheque", inputFlashMap.get("tipoSubcheque"));
        }else {
            mav.addObject("tipoSubcheque", new ChequeSubtipo());
        }
        mav.addObject("action" , "create");
        mav.addObject("listaCategorias", chequeTipoServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-tipoSubcheques");
        mav.addObject("action", "update");
        mav.addObject("tipoSubcheque", chequeSubtipoServicio.obtenerPorId(id));
        mav.addObject("listaCategorias", chequeTipoServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create (ChequeSubtipo dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/tipoSubcheque");
        try{
            chequeSubtipoServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado el registro correctamente");
        }catch (Exception e){
            attributes.addFlashAttribute("tipoSubcheque", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            r.setUrl("/tipoSubcheque/form");
        }
        return r;
    }

    @PostMapping("/update")
    public RedirectView update (ChequeSubtipo dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/tipoSubcheque");
        try{
            chequeSubtipoServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado el registro correctamente");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("tipoSubcheque", dto);
            r.setUrl("/tipoSubcheque/form");
        }
        return r;
    }

    @GetMapping("/delete/{id}")
    public RedirectView delete (@PathVariable Long id, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/tipoSubcheque");
        chequeSubtipoServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado el registro correctamente");
        return r;
    }

    @GetMapping("/obtenerPorTipo/{idTipo}")
    public ResponseEntity<String> obtenerPorTipo(@PathVariable Long idTipo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(chequeSubtipoServicio.obtenerPorTipo(idTipo));
        return ResponseEntity.ok(jsonString);
    }

}
