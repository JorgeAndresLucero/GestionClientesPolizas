package com.insurance.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot para el sistema de gestión de seguros.
 * <p>
 * Esta clase configura y lanza la aplicación utilizando la autoconfiguración de Spring Boot.
 * La aplicación proporciona una API REST para la gestión de clientes, pólizas de vida,
 * pólizas de salud, pólizas de vehículos y beneficiarios.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @since 2026
 */
@SpringBootApplication
public class InsuranceApiApplication {

	/**
	 * Método principal que inicia la aplicación Spring Boot.
	 *
	 * @param args argumentos de línea de comandos pasados a la aplicación
	 */
	public static void main(String[] args) {
		SpringApplication.run(InsuranceApiApplication.class, args);
	}

}
