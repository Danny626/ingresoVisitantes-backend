package com.albo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.albo.model.Rol;
import com.albo.service.IRolService;

@RestController
@RequestMapping("/roles")
public class RolController {

	@Autowired
	private IRolService service;

	// @PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Rol>> listar() {
		List<Rol> roles = new ArrayList<>();
		roles = service.listar();
		return new ResponseEntity<List<Rol>>(roles, HttpStatus.OK);
	}

	@GetMapping(value = "/pageable", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Rol>> listarPageable(Pageable pageable) {
		Page<Rol> roles = null;
		roles = service.listarPageable(pageable);
		return new ResponseEntity<Page<Rol>>(roles, HttpStatus.OK);
	}

	// @PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rol> listarId(@PathVariable("id") Integer id) {
		Rol rol = new Rol();
		rol = service.listarId(String.valueOf(id));
		if (rol == null) {
//			throw new ModeloNotFoundException("ID: " + id);
		}
		return new ResponseEntity<Rol>(rol, HttpStatus.OK);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rol> registrar(@Valid @RequestBody Rol rol) { // Valid para validar lo anotado en el modelo
																		// Signo: @Min; @Max; @Size
		Rol ro = new Rol();
		ro = service.registrar(rol);
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(ro.getId())
//				.toUri();
		return new ResponseEntity<Rol>(ro, HttpStatus.CREATED);
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rol> actualizar(@RequestBody Rol rol) {
		Rol ro = new Rol();
		ro = service.modificar(rol);
		return new ResponseEntity<Rol>(ro, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void eliminar(@PathVariable Integer id) {
		Rol ro = service.listarId(String.valueOf(id));
		if (ro == null) {
//			throw new ModeloNotFoundException("ID: " + id);
		} else {
			service.eliminar(String.valueOf(id));
		}
	}
}
