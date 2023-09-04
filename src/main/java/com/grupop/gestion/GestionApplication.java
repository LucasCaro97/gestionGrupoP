package com.grupop.gestion;

import com.grupop.gestion.Entidades.Venta;
import com.grupop.gestion.Repositorios.VentaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDate;

@SpringBootApplication
@EnableAsync
public class GestionApplication {


	public static void main(String[] args) {



		SpringApplication.run(GestionApplication.class, args);


	}
}
