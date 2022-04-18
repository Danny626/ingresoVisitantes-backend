package com.albo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.albo.model.AreaRecinto;
import com.albo.service.IAreaRecintoService;

@RestController
@RequestMapping("/areaRecinto")
public class AreaRecintoController {

	@Autowired
	private IAreaRecintoService areaRecintoService;

	/**
	 * Lista AreaRecinto por recinto.
	 * 
	 * @param recCod parámetro que indica el recinto al que pertenece.
	 * @return Devuelve un listado de AreaRecinto
	 */
	@RequestMapping(value = "/listaPorRecinto", method = RequestMethod.GET, params = {
			"recCod" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AreaRecinto>> buscarPorRecinto(@RequestParam("recCod") String recCod) {
		List<AreaRecinto> lista = new ArrayList<>();

		lista = areaRecintoService.buscarXRecinto(recCod);

		return new ResponseEntity<List<AreaRecinto>>(lista, HttpStatus.OK);
	}

}
