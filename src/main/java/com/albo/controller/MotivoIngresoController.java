package com.albo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.albo.model.MotivoIngreso;
import com.albo.service.IMotivoIngresoService;

@RestController
@RequestMapping("/motivoIngreso")
public class MotivoIngresoController {

	@Autowired
	private IMotivoIngresoService motivoIngresoService;

	/**
	 * Método que lista los motivo de ingreso
	 * 
	 * @return Devuelve una lista de MotivoIngreso.
	 */
	@GetMapping(value = "/lista", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MotivoIngreso>> listaMotivoIngreso() {
		List<MotivoIngreso> lista = new ArrayList<>();

		lista = motivoIngresoService.listar();

		return new ResponseEntity<List<MotivoIngreso>>(lista, HttpStatus.OK);
	}
}
