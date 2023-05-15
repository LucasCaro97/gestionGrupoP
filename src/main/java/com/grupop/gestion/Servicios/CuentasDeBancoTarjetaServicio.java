package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.CuentasDeBancoTarjeta;
import com.grupop.gestion.Repositorios.CuentasDeBancoTarjetaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentasDeBancoTarjetaServicio {

    private final CuentasDeBancoTarjetaRepo cuentasDeBancoTarjetaRepo;


    @Transactional
    public void crear(CuentasDeBancoTarjeta dto){
        CuentasDeBancoTarjeta cta = new CuentasDeBancoTarjeta();
        cta.setDescripcion(dto.getDescripcion());
        cta.setCuenta(dto.getCuenta());
        cta.setTipoCuentaBanco(dto.getTipoCuentaBanco());
        cta.setProveedor(dto.getProveedor());
        cta.setCuentaAcreditacion(dto.getCuentaAcreditacion());
        cta.setCBU(dto.getCBU());
        cta.setAlias(dto.getAlias());
        cuentasDeBancoTarjetaRepo.save(cta);
    }

    @Transactional
    public void actualizar(CuentasDeBancoTarjeta dto){
        CuentasDeBancoTarjeta cta = cuentasDeBancoTarjetaRepo.findById(dto.getId()).get();
        cta.setDescripcion(dto.getDescripcion());
        cta.setCuenta(dto.getCuenta());
        cta.setTipoCuentaBanco(dto.getTipoCuentaBanco());
        cta.setProveedor(dto.getProveedor());
        cta.setCuentaAcreditacion(dto.getCuentaAcreditacion());
        cta.setCBU(dto.getCBU());
        cta.setAlias(dto.getAlias());
        cuentasDeBancoTarjetaRepo.save(cta);
    }

    @Transactional(readOnly = true)
    public List<CuentasDeBancoTarjeta> obtenerTodos(){ return cuentasDeBancoTarjetaRepo.findAll();   }

    @Transactional(readOnly = true)
    public CuentasDeBancoTarjeta obtenerPorId(Long id){ return cuentasDeBancoTarjetaRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ cuentasDeBancoTarjetaRepo.deleteById(id);}

}
