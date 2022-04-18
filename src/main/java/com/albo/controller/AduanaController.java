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

import com.albo.model.Aduana;
import com.albo.service.IAduanaService;

@RestController
@RequestMapping("/aduana")
public class AduanaController {

	@Autowired
	private IAduanaService aduanaService;

	/**
	 * Método que lista las aduanas
	 * 
	 * @return Devuelve una lista de Aduanas.
	 */
	@GetMapping(value = "/lista", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Aduana>> listaAduana() {
		List<Aduana> lista = new ArrayList<>();

		lista = aduanaService.listar();

		return new ResponseEntity<List<Aduana>>(lista, HttpStatus.OK);
	}

}
