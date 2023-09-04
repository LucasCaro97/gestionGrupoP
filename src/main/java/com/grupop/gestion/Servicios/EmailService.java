package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.PlanPago;
import com.grupop.gestion.Entidades.TipoOperacion;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;

    private static final String SUBJECT = "Welcome email";
    private static final String TEXT = "Welcome to our Page. Thank you for registering!";

    @Async
    public void send(String to, String tipoCorreo, String nroComprobante, Long cliente, BigDecimal importe, PlanPago planPago){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(from);
        message.setSubject(SUBJECT);
        message.setText(generarMensaje(tipoCorreo,nroComprobante,cliente,importe,planPago));
        sender.send(message);
    }

    public String generarMensaje(String tipoCorreo, String nroComprobante, Long cliente, BigDecimal importe, PlanPago planPago){
        String texto = "";
        switch (tipoCorreo){
            case "credito":
                texto = "Se ha creado un nuevo credito, correspondiente a la venta -" + nroComprobante + " cliente Nro-" + cliente + " con un importe de " + importe +
                        " con el plan de pago " + planPago.getDescripcion();
                break;
        }

        return texto;

    }

}
