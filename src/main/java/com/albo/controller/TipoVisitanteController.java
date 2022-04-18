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

import com.albo.model.TipoVisitante;
import com.albo.service.ITipoVisitanteService;

@RestController
@RequestMapping("/tipoVisitante")
public class TipoVisitanteController {

	@Autowired
	private ITipoVisitanteService tipoVisitanteService;

	/**
	 * Método que lista los tipos de Visitante
	 * 
	 * @return Devuelve una lista de TipoVisitante.
	 */
	@GetMapping(value = "/lista", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TipoVisitante>> listaTipoVisitante() {
		List<TipoVisitante> lista = new ArrayList<>();

		lista = tipoVisitanteService.listar();

		return new ResponseEntity<List<TipoVisitante>>(lista, HttpStatus.OK);
	}
}
