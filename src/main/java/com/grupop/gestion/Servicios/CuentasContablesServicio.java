package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.CuentasContables;
import com.grupop.gestion.Entidades.Impuestos;
import com.grupop.gestion.Repositorios.CuentasContablesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentasContablesServicio {

    private final CuentasContablesRepo cuentasContablesRepo;

    @Transactional
    public void crear(CuentasContables dto){
        CuentasContables cta = new CuentasContables();
        cta.setDescripcion(dto.getDescripcion());
        cta.setClasificacionCta(dto.getClasificacionCta());
        cta.setCtaTotalizadora(dto.getCtaTotalizadora());
        cta.setMoneda(dto.getMoneda());
        cta.setCodigo(dto.getCodigo());
        cta.setImpuestos(dto.getImpuestos());
        cuentasContablesRepo.save(cta);
        System.out.println(cta.getImpuestos());
    }

    @Transactional
    public void actualizar(CuentasContables dto){
        CuentasContables cta = cuentasContablesRepo.findById(dto.getId()).get();
        cta.setDescripcion(dto.getDescripcion());
        cta.setClasificacionCta(dto.getClasificacionCta());
        cta.setCtaTotalizadora(dto.getCtaTotalizadora());
        cta.setMoneda(dto.getMoneda());
        cta.setCodigo(dto.getCodigo());
        cta.setImpuestos(dto.getImpuestos());
        cuentasContablesRepo.save(cta);
        System.out.println(cta.getImpuestos());
    }

    @Transactional(readOnly = true)
    public List<Impuestos> getImpuestosByCuentaContableId(Long idCuenta){
        return cuentasContablesRepo.findImpuestosByCuentaContableId(idCuenta);
    }

    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerTodos(){ return cuentasContablesRepo.findAll(); }

    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerTodos(Long id){ return cuentasContablesRepo.searchByTotalizadora(id); }

    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentasUrbanizacion(){ return cuentasContablesRepo.obtenerCuentasUrbanizacion(); }
    @Transactional(readOnly = true)
    public CuentasContables obtenerPorid(Long id) { return cuentasContablesRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id) { cuentasContablesRepo.deleteById(id); }

    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1111(){
        return cuentasContablesRepo.obtenerCuentas1111();
    }

    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1112(){
        return cuentasContablesRepo.obtenerCuentas1112();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1113(){
        return cuentasContablesRepo.obtenerCuentas1113();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1121(){
        return cuentasContablesRepo.obtenerCuentas1121();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1122(){
        return cuentasContablesRepo.obtenerCuentas1122();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1131(){
        return cuentasContablesRepo.obtenerCuentas1131();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1132(){
        return cuentasContablesRepo.obtenerCuentas1132();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1133(){
        return cuentasContablesRepo.obtenerCuentas1133();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1134(){
        return cuentasContablesRepo.obtenerCuentas1134();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1141(){
        return cuentasContablesRepo.obtenerCuentas1141();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1142(){
        return cuentasContablesRepo.obtenerCuentas1142();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1143(){
        return cuentasContablesRepo.obtenerCuentas1143();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1144(){
        return cuentasContablesRepo.obtenerCuentas1144();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1211(){
        return cuentasContablesRepo.obtenerCuentas1211();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1212(){
        return cuentasContablesRepo.obtenerCuentas1212();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1221(){
        return cuentasContablesRepo.obtenerCuentas1221();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1222(){
        return cuentasContablesRepo.obtenerCuentas1222();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1223(){
        return cuentasContablesRepo.obtenerCuentas1223();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas123(){
        return cuentasContablesRepo.obtenerCuentas123();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas124(){
        return cuentasContablesRepo.obtenerCuentas124();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas211(){
        return cuentasContablesRepo.obtenerCuentas211();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas212(){
        return cuentasContablesRepo.obtenerCuentas212();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas213(){
        return cuentasContablesRepo.obtenerCuentas213();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas214(){
        return cuentasContablesRepo.obtenerCuentas214();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas215(){
        return cuentasContablesRepo.obtenerCuentas215();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas216(){
        return cuentasContablesRepo.obtenerCuentas216();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas221(){
        return cuentasContablesRepo.obtenerCuentas221();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas222(){
        return cuentasContablesRepo.obtenerCuentas222();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas223(){
        return cuentasContablesRepo.obtenerCuentas223();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas224(){
        return cuentasContablesRepo.obtenerCuentas224();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas225(){
        return cuentasContablesRepo.obtenerCuentas225();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas31(){
        return cuentasContablesRepo.obtenerCuentas31();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas32(){return cuentasContablesRepo.obtenerCuentas32();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas411(){
        return cuentasContablesRepo.obtenerCuentas411();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas412(){
        return cuentasContablesRepo.obtenerCuentas412();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas413(){
        return cuentasContablesRepo.obtenerCuentas413();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas421(){
        return cuentasContablesRepo.obtenerCuentas421();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas422(){
        return cuentasContablesRepo.obtenerCuentas422();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas423(){
        return cuentasContablesRepo.obtenerCuentas423();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas424(){
        return cuentasContablesRepo.obtenerCuentas424();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas425(){
        return cuentasContablesRepo.obtenerCuentas425();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas426(){
        return cuentasContablesRepo.obtenerCuentas426();
    }
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas427(){return cuentasContablesRepo.obtenerCuentas427();}
    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas43(){
        return cuentasContablesRepo.obtenerCuentas43();
    }

    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerPorDescripcion(String descripcion) {
        return cuentasContablesRepo.obtenerPorDescripcion(descripcion);
    }
}
