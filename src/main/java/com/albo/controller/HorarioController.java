package com.albo.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.albo.exception.list.PersistException;
import com.albo.model.Horario;
import com.albo.service.IHorarioService;

@RestController
@RequestMapping("/horario")
public class HorarioController {

	private static final Logger logger = LogManager.getLogger(HorarioController.class);

	@Autowired
	private IHorarioService horarioService;

	/**
	 * Lista Horario por recinto, tipo visitante, nombre horario y día de la semana.
	 * 
	 * @param recCod    parámetro que indica el recinto al que pertenece.
	 * @param tviCod    parámetro que indica el tipo de visitante al que pertenece.
	 * @param horNombre nombre del horario
	 * @param dia       dia de la semana
	 * @return Devuelve un listado de Horario
	 */
	@RequestMapping(value = "/listaPorRecinto", method = RequestMethod.GET, params = {
			"recCod" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Horario>> buscarPorRecintoTVisHorNombre(@RequestParam("recCod") String recCod,
			Long tviCod, String horNombre, String dia) {
		List<Horario> lista;

		if (recCod.equals("todos")) {
			recCod = "%";
		}

		if (horNombre.equals("todos")) {
			horNombre = "%";
		}

		if (dia.equals("todos")) {
			dia = "%";
		}

		lista = horarioService.buscarXRecTVisitante(recCod, tviCod, horNombre, dia);

		return new ResponseEntity<List<Horario>>(lista, HttpStatus.OK);
	}

	@Transactional
	@PostMapping(value = "/registrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Horario> registrarHorario(@RequestBody Horario horario) {

		horario.setHorEstado("ACT");

		/* registramos en bd el objeto */
		Horario respHorario = horarioService.registrar(horario);

		/* Verificamos si el registro se guardó correctamente */
		if (respHorario == null) {
			logger.error("Error al guardar el Horario");
			throw new PersistException("Error al guardar el Horario");
		} else {
			return new ResponseEntity<Horario>(respHorario, HttpStatus.CREATED);
		}
	}

}
