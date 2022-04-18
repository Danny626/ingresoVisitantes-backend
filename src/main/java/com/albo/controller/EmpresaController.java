package com.albo.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.albo.exception.list.PersistException;
import com.albo.model.Empresa;
import com.albo.service.IEmpresaService;

@RestController
@RequestMapping("/empresa")
public class EmpresaController {

	private static final Logger logger = LogManager.getLogger(EmpresaController.class);

	@Autowired
	private IEmpresaService empresaService;

	/**
	 * Método que lista las eempresas
	 * 
	 * @return Devuelve una lista de Empresa.
	 */
	@GetMapping(value = "/lista", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Empresa>> listaEmpresa(Pageable pageable) {
		Page<Empresa> lista;

		lista = empresaService.listarPageable(pageable);

		return new ResponseEntity<Page<Empresa>>(lista, HttpStatus.OK);
	}

	/**
	 * Método para guardar una empresa.
	 * 
	 * @param empresa parámetro que contiene al objeto empresa.
	 * @return Devuelve el objeto Empresa guardado.
	 */
	@Transactional
	@PostMapping(value = "/registrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Empresa> registro(@RequestBody Empresa empresa) {

		Empresa respEmpresa = new Empresa();

		if (empresa.getEmpNombre() != null) {

			/* registramos en bd el objeto */
			respEmpresa = empresaService.registrar(empresa);

			/* Verificamos si el registro se guardó correctamente */
			if (respEmpresa == null) {
				logger.error("Error al guardar la Empresa");
				throw new PersistException("Error al guardar la Empresa");
			} else {

				return new ResponseEntity<Empresa>(respEmpresa, HttpStatus.CREATED);
			}
		} else {
			throw new RuntimeException("La Empresa se encuentra vacia");
		}
	}


	/**
	 * Método para actualizar una empresa.
	 * 
	 * @param empresa parámetro que contiene al objeto empresa.
	 * @return Devuelve el objeto Empresa actualizado.
	 */
	@PutMapping(value = "/actualizar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Empresa> actualizar(@RequestBody Empresa empresa) {
		Empresa obj = new Empresa();

		obj = empresaService.modificar(empresa);
		return new ResponseEntity<Empresa>(obj, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/buscarPorNombre", method = RequestMethod.GET, params = {
			"nombre" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Empresa>> buscarPorNombre(@RequestParam("nombre") String nombre, Pageable pageable) {
		Page<Empresa> lista;

		lista = empresaService.buscarXNombre(pageable, nombre + "%");

		return new ResponseEntity<Page<Empresa>>(lista, HttpStatus.OK);
	}

}
