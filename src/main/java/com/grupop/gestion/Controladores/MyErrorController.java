package com.grupop.gestion.Controladores;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import static jakarta.servlet.RequestDispatcher.ERROR_STATUS_CODE;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("error");
        Integer status = (int)request.getAttribute(ERROR_STATUS_CODE);
        String message;

        switch (status){
            case 403:
                message = "No tienes los permisos suficientes para ingresar a esta seccion";
                break;
            case 404:
                message = "El recurso requerido no fue encontrado";
                break;
            case 500:
                message = "Error interno del servidor";
                break;
            default:
                message= "Error inesperado";
        }

        mav.addObject("message", message);
        mav.addObject("status", status);
        return mav;
    }

}
