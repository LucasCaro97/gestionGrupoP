package com.grupop.gestion.Controladores;

import com.grupop.gestion.DTO.CobroDetalleCuotasDto;
import com.grupop.gestion.Servicios.CobroDetalleCuotasServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cobroDetalleCuotas")
public class CobroDetalleCuotasControlador {


    private final CobroDetalleCuotasServicio cobroDetalleCuotasServicio;

    @PostMapping("/alta/{idVenta}/{idCred}/{nroCuota}/{fechaVenc}/{cuotaBase}/{ajuste}/{importePun}/{importeBonif}/{importeFinal}/{idCobro}/{cobrado}")
    public ResponseEntity<String> altaDetalle(@PathVariable Long idVenta, @PathVariable Long idCred, @PathVariable Integer nroCuota,
                                              @PathVariable LocalDate fechaVenc, @PathVariable BigDecimal cuotaBase, @PathVariable BigDecimal ajuste,
                                              @PathVariable BigDecimal importePun, @PathVariable BigDecimal importeBonif, @PathVariable BigDecimal importeFinal,
                                              @PathVariable Long idCobro, @PathVariable BigDecimal cobrado){

        try{
            cobroDetalleCuotasServicio.crear(idVenta, idCred, nroCuota, fechaVenc, cuotaBase, ajuste, importePun, importeBonif, importeFinal, idCobro, cobrado);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado exitosamente");

    }

    @PostMapping("/alta")
    public ResponseEntity<String> altaDetalle(@RequestBody List<CobroDetalleCuotasDto> listaItems){

        try{
            cobroDetalleCuotasServicio.crearNuevo(listaItems);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado exitosamente");

    }

    @PostMapping("delete/{idCred}/{nroCuota}/{idCobro}/{importeCobrado}")
    public ResponseEntity<String> bajaDetalle ( @PathVariable Long idCred, @PathVariable Integer nroCuota, @PathVariable Long idCobro, @PathVariable BigDecimal importeCobrado){
        try{
            cobroDetalleCuotasServicio.eliminarLineaDetalle(idCred, nroCuota, idCobro, importeCobrado);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro eliminado correctamente");
    }
}




