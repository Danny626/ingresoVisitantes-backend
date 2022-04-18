package com.albo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//INICIO entorno desarrollo
//@SpringBootApplication
//public class IngresoVisitantesBackendApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(IngresoVisitantesBackendApplication.class, args);
//	}
//
//}
//FIN entorno desarrollo

//INICIO despliege
@SpringBootApplication
public class IngresoVisitantesBackendApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(IngresoVisitantesBackendApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(IngresoVisitantesBackendApplication.class);
	}
}
//FIN despliege