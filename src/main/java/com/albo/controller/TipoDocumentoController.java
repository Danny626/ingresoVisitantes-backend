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

import com.albo.model.TipoDocumento;
import com.albo.service.ITipoDocumentoService;

@RestController
@RequestMapping("/tipoDocumento")
public class TipoDocumentoController {

	@Autowired
	private ITipoDocumentoService tipoDocumentoService;

	/**
	 * Método que lista los tipoDocumento
	 * 
	 * @return Devuelve una lista de TipoDocumento.
	 */
	@GetMapping(value = "/lista", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TipoDocumento>> listaTipoDocumento() {
		List<TipoDocumento> lista = new ArrayList<>();

		lista = tipoDocumentoService.listar();

		return new ResponseEntity<List<TipoDocumento>>(lista, HttpStatus.OK);
	}
}
