package com.albo.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.albo.model.DocumentoIngreso;
import com.albo.model.ParametroRecinto;
import com.albo.service.IDocumentoIngresoService;
import com.albo.service.IParametroRecintoService;

@RestController
@RequestMapping("/documentoIngreso")
public class DocumentoIngresoController {

	private static final Logger logger = LogManager.getLogger(DocumentoIngresoController.class);

	private static final String parametroPathFotosDocumentosIngreso = "PATH_FOTOS_DOCUMENTOS_INGRESO";

	@Autowired
	private IDocumentoIngresoService documentoIngresoService;

	@Autowired
	private IParametroRecintoService parametroRecintoService;

	public String getPathFotosDocumentoIngreso() {
		return getPathFotosDocumentoIngresoParam(parametroPathFotosDocumentosIngreso);
	}

	/**
	 * obtiene la foto del documento de ingreso
	 * 
	 * @param foto nombre del archivo de la foto de ingreso.
	 * @return la imágen de la foto
	 */
	@RequestMapping(value = "/mostrarFoto", method = RequestMethod.GET, params = {
			"foto" }, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> mostrarFoto(@RequestParam("foto") String foto) {

		String pathFoto = this.getPathFotosDocumentoIngreso() + foto;
		System.out.println(pathFoto);
		byte[] data = null;

		data = documentoIngresoService.leerArchivo(pathFoto);
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}

	/**
	 * busca el valor del parametro PATH_FOTOS_DOCUMENTOS_INGRESO en la bd
	 * 
	 * @return el valor del parametro PATH_FOTOS_DOCUMENTOS_INGRESO
	 */
	public String getPathFotosDocumentoIngresoParam(String parametroPathFotos) {

		ParametroRecinto parametroRecinto = new ParametroRecinto();
		parametroRecinto = parametroRecintoService.buscarXNombreParamGeneral(parametroPathFotos);

		if (parametroRecinto != null) {
			return parametroRecinto.getParValor();
		} else {
			logger.error("¡Error! No se encontró el parámetro PATH_FOTOS_DOCUMENTOS_INGRESO");
			throw new RuntimeException("¡Error! No se encontró el parámetro PATH_FOTOS_DOCUMENTOS_INGRESO");
		}

	}

	/**
	 * Buscar Documentos de ingreso por visita.
	 * 
	 * @param visita parámetro llave de la visita.
	 * @return Devuelve una lista Documentos de ingreso
	 */
	// @PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/buscarXVisita", method = RequestMethod.GET, params = {
			"visita" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DocumentoIngreso>> buscarPorVisita(@RequestParam("visita") String visita) {
		List<DocumentoIngreso> lista = new ArrayList<>();

		lista = documentoIngresoService.buscarXVisita(Long.valueOf(visita));

		return new ResponseEntity<List<DocumentoIngreso>>(lista, HttpStatus.OK);
	}

}
