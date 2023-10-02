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
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendCobranza(String to, String tipoCorreo, String cliente, LocalDate fechaVencimiento, BigDecimal montoTotal, String subject){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(from);
        message.setSubject(subject);
        message.setText(generarMensajeCobranza(tipoCorreo,cliente,fechaVencimiento, montoTotal));
        sender.send(message);
    }


    public String generarMensajeCobranza(String tipoCorreo, String cliente, LocalDate fechaVencimiento, BigDecimal montoTotal){
        String texto = "";
        switch (tipoCorreo){
            case "atrasados":
                texto = "Buenas tardes cliente " + cliente +", le informamos que tiene cuotas vencidas (" + fechaVencimiento + ") con un valor de $" + montoTotal +
                ". Le agradecemos pueda acercarse a la sucursal mas cercana o comunicar su intencion de pago. Muchas gracias. ";
                break;
            case "mensualidad":
                texto = "Buenas tardes cliente " + cliente +", le informamos que su cuota actual esta proxima a vencer (" + fechaVencimiento + ") con un valor de $" + montoTotal +
                        ". Le agradecemos pueda acercarse a la sucursal mas cercana o comunicar su intencion de pago. Muchas gracias. ";
                break;
        }

        return texto;

    }



}
