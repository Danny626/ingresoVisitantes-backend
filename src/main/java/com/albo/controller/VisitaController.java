package com.albo.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.albo.exception.list.ModelNotFoundException;
import com.albo.exception.list.PersistException;
import com.albo.model.DocumentoIngreso;
import com.albo.model.Horario;
import com.albo.model.ParametroRecinto;
import com.albo.model.Visita;
import com.albo.service.IDocumentoIngresoService;
import com.albo.service.IHorarioService;
import com.albo.service.IParametroRecintoService;
import com.albo.service.IVisitaService;
import com.albo.util.CryptoUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/visita")
public class VisitaController {

	private static final Logger logger = LogManager.getLogger(VisitaController.class);

	private static final String parametroPathFotosDocumentosIngreso = "PATH_FOTOS_DOCUMENTOS_INGRESO";

	@Autowired
	private IVisitaService visitaService;

	@Autowired
	private IHorarioService horarioService;

	@Autowired
	private IDocumentoIngresoService documentoIngresoService;

	@Autowired
	private IParametroRecintoService parametroRecintoService;

	public String getPathFotosDocumentoIngreso() {
		return getPathFotosDocumentoIngresoParam(parametroPathFotosDocumentosIngreso);
	}

	/**
	 * Método para guardar una Visita.
	 * 
	 * @param visita, parámetro que contiene al objeto Visita a guardarse.
	 * @return Devuelve el objeto Visita guardado.
	 */
//	@Transactional
//	@PostMapping(value = "/registrarIngreso", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Visita> registrarVisita(@RequestBody Visita visita) {

	@Transactional
	@PostMapping(value = "/registrarIngreso", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Visita> registrarVisita(@RequestParam("vis") String vis,
			@RequestParam(value = "files", required = false) MultipartFile[] files) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		Visita visita = mapper.readValue(vis, Visita.class);

		String error = "";
		Visita respVisita = new Visita();

		visita.setVisEstado("ACT");
		visita.setVisIngreso(LocalDateTime.now());

		// verifica si el dato entrante esta vacio
		if (visita.getVisitante().getVteCi() == null) {
			error = "La Visita se encuentra vacia";
		}

		// verifica si el visitante ya registro su ingreso pero no su salida e intenta
		// reingresar
		if (visitaService.buscarXCiNoTieneSalida(visita.getVisitante().getVteCi()).size() > 0) {
			error = "El Visitante tiene Salidas Pendientes";
		}

		// verificamos si la visita está dentro de la regla de horario
		LocalDateTime ahora = LocalDateTime.now();
		String diaSemana = ahora.getDayOfWeek().toString();
		List<Horario> horarios = new ArrayList<>();
		horarios = horarioService.buscarXRecTVisitante(visita.getAreaRecinto().getRecinto().getRecCod(),
				visita.getVisitante().getTipoVisitante().getTviCod(), "%", "%" + diaSemana + "%");

		if (horarios.size() == 0) {
			error = "Se encuentra fuera de los días de entrada asignados o no existe un horario asignado.";
		} else {
			Integer horaEntrada = horarios.get(0).getHorHoraEntrada() * 100 + horarios.get(0).getHorMinEntrada();
			Integer horaSalida = horarios.get(0).getHorHoraSalida() * 100 + horarios.get(0).getHorMinSalida();

			Integer horaActual = ahora.getHour() * 100 + ahora.getMinute();

			if (horaActual < horaEntrada || horaActual >= horaSalida) {
				error = "Se encuentra fuera de la hora de entrada y salida asignada";
			}
		}

		// guardamos el documento de ingreso
//		if (error.equals("")) {
//			// guardamos la foto del documento de ingreso
//			DocumentoIngreso respDocumentoIngreso = documentoIngresoService.registrar(visita.getDocumentoIngreso());
//
//			if (respDocumentoIngreso != null) {
//				visita.setDocumentoIngreso(respDocumentoIngreso);
//				// subimos la foto del documento
//				this.subirImagen(respDocumentoIngreso, file);
//			} else {
//				error = "Ocurrió un error al registrar el documento de ingreso.";
//			}
//
//		}

		if (error.equals("")) {

			/* asignamos el objeto visita a cada documentoIngreso */
			visita.getDocumentosIngreso().forEach(doc -> {
				doc.setVisita(visita);
			});

			/* registramos en bd el objeto */
			respVisita = visitaService.registrar(visita);

			/* Verificamos si el registro se guardó correctamente */
			if (respVisita == null) {
				logger.error("Error al guardar la Visita");
				throw new PersistException("Error al guardar la Visita");
			} else {
				/* guardamos las fotos de los documentos */
				String respDocs = guardarDocumentos(files, respVisita.getDocumentosIngreso());
				System.out.println("docs guardados: " + respDocs);

				return new ResponseEntity<Visita>(respVisita, HttpStatus.CREATED);
			}
		} else {
			throw new RuntimeException(error);
		}
	}

	/**
	 * registra la salida de la visita modificando el campo visSalida con la fecha
	 * del momento con la llave del QR
	 * 
	 * @param recCod recinto desde donde se hace el registro
	 * @param llave  código q proviene de la lectura del QR
	 * @return devuelve el objeto visita modificado
	 */
	@Transactional
	@RequestMapping(value = "/registrarSalida", method = RequestMethod.POST, params = {
			"recCod" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Visita> registrarSalida(@RequestParam("recCod") String recCod, String llave) {

		Visita respVisita = new Visita();

		String cadenaQR = CryptoUtil.decrypt(llave, "albosa");
		String[] cadena = cadenaQR.split("\\|");

		respVisita = this.guardarSalida(recCod, cadena[0]);

		return new ResponseEntity<Visita>(respVisita, HttpStatus.OK);

	}

	/**
	 * registra la salida de la visita modificando el campo visSalida con la fecha
	 * del momento, pero mediante el ci del visitante.
	 * 
	 * @param recCod recinto desde donde se hace el registro
	 * @param ci     CI del visitante q deja el recinto
	 * @return devuelve el objeto visita modificado
	 */
	@Transactional
	@RequestMapping(value = "/registrarSalidaXCi", method = RequestMethod.POST, params = {
			"recCod" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Visita> registrarSalidaXCi(@RequestParam("recCod") String recCod, String ci) {

		Visita respVisita = new Visita();

		respVisita = this.guardarSalida(recCod, ci);

		return new ResponseEntity<Visita>(respVisita, HttpStatus.OK);

	}

	/**
	 * Lista Visitantes q ingresaron.
	 * 
	 * @param fechaInicio parámetro para filtrar la fecha de ingreso inicial.
	 * @param fechaFin    parámetro para filtrar la fecha de ingreso fin.
	 * @param recinto     parámetro consulta.
	 * @param areaRecinto parámetro consulta.
	 * @return Devuelve una lista de Visitantes q se encuentran dentro
	 */
	// @PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/visitantesSinSalida", method = RequestMethod.GET, params = {
			"fechaInicio" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Visita>> visitantesSinSalida(@RequestParam("fechaInicio") String fechaInicio,
			String fechaFin, String recinto, Long areaRecinto, Pageable pageable) {

		Page<Visita> visitas;

		fechaInicio = fechaInicio + " 00:00:00.000";
		fechaFin = fechaFin + " 23:59:59.999";

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS");
		LocalDateTime fechaI = LocalDateTime.parse(fechaInicio, formatter);
		LocalDateTime fechaF = LocalDateTime.parse(fechaFin, formatter);

		if (areaRecinto == 0) {
			visitas = visitaService.visitantesSinSalida2(pageable, fechaI, fechaF, recinto);
		} else {
			visitas = visitaService.visitantesSinSalida(pageable, fechaI, fechaF, recinto, areaRecinto);
		}

		return new ResponseEntity<Page<Visita>>(visitas, HttpStatus.OK);
	}

	/**
	 * Lista Visitantes q salieron.
	 * 
	 * @param fechaInicio parámetro para filtrar la fecha de ingreso inicial.
	 * @param fechaFin    parámetro para filtrar la fecha de ingreso fin.
	 * @param recinto     parámetro consulta.
	 * @param areaRecinto parámetro consulta.
	 * @return Devuelve una lista de Visitantes q se encuentran dentro
	 */
	// @PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/visitantesConSalida", method = RequestMethod.GET, params = {
			"fechaInicio" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Visita>> visitantesConSalida(@RequestParam("fechaInicio") String fechaInicio,
			String fechaFin, String recinto, Long areaRecinto, Pageable pageable) {

		Page<Visita> visitas;

		fechaInicio = fechaInicio + " 00:00:00.000";
		fechaFin = fechaFin + " 23:59:59.999";

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS");
		LocalDateTime fechaI = LocalDateTime.parse(fechaInicio, formatter);
		LocalDateTime fechaF = LocalDateTime.parse(fechaFin, formatter);

		if (areaRecinto == 0) {
			visitas = visitaService.visitantesConSalida2(pageable, fechaI, fechaF, recinto);
		} else {
			visitas = visitaService.visitantesConSalida(pageable, fechaI, fechaF, recinto, areaRecinto);
		}

		return new ResponseEntity<Page<Visita>>(visitas, HttpStatus.OK);
	}

	/**
	 * Buscar estado de la visita de Visitante por ci.
	 * 
	 * @param ci          parámetro llave del visitante.
	 * @param fechaInicio fecha inicial.
	 * @param fechaFin    fecha final.
	 * @param recinto     recinto donde ingreso el visitante.
	 * @param areaRecinto area del recinto donde ingreso el visitante.
	 * @param salida      busca visitas con o sin salida.
	 * @return Devuelve un objeto Visita
	 */
	// @PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/buscarXCi", method = RequestMethod.GET, params = {
			"ci" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Visita>> buscarPorCi(@RequestParam("ci") String ci, String fechaInicio, String fechaFin,
			String recinto, Long areaRecinto, boolean salida, Pageable pageable) {
		Page<Visita> visitas;

		fechaInicio = fechaInicio + " 00:00:00.000";
		fechaFin = fechaFin + " 23:59:59.999";

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS");
		LocalDateTime fechaI = LocalDateTime.parse(fechaInicio, formatter);
		LocalDateTime fechaF = LocalDateTime.parse(fechaFin, formatter);

		if (salida) {
			if (areaRecinto == 0) {
				visitas = visitaService.buscarXCiConSalida(pageable, ci, fechaI, fechaF, recinto);
			} else {
				visitas = visitaService.buscarXCiConSalidaAreaRecinto(pageable, ci, fechaI, fechaF, recinto,
						areaRecinto);
			}
		} else {
			if (areaRecinto == 0) {
				visitas = visitaService.buscarXCiSinSalida(pageable, ci, fechaI, fechaF, recinto);
			} else {
				visitas = visitaService.buscarXCiSinSalidaAreaRecinto(pageable, ci, fechaI, fechaF, recinto,
						areaRecinto);
			}
		}

		if (visitas == null) {
			throw new ModelNotFoundException("ID: " + ci);
		}
		return new ResponseEntity<Page<Visita>>(visitas, HttpStatus.OK);
	}

	public Visita guardarSalida(String recCod, String ci) {
		String error = "";
		Visita respVisita = new Visita();

		// busca el ultimo ingreso sin salida
		Visita ultimoIngresoSinSalida = visitaService.buscaXCiUltimoSinSalida(ci, recCod);

		if (ultimoIngresoSinSalida == null) {
			error = "No tiene registrado ningún ingreso o ya registró su salida.";
		}

		if (error.equals("")) {
			/* modificamos la fecha de salida de la visita */
			ultimoIngresoSinSalida.setVisSalida(LocalDateTime.now());

			respVisita = visitaService.modificar(ultimoIngresoSinSalida);

			/* Verificamos si el registro se guardó correctamente */
			if (respVisita == null) {
				logger.error("Error al modificar la Visita");
				throw new PersistException("Error al modificar la Visita");
			}
		} else {
			throw new RuntimeException(error);
		}

		return respVisita;
	}

	/**
	 * sube la foto del documento de ingreso al disco del servidor y modifica su
	 * ubicación en imagen
	 * 
	 * @param documentoIngreso
	 * @param file
	 * @return el objeto usuario con la ubicación de su foto actualizada
	 * @throws IOException
	 */
	public DocumentoIngreso subirImagen(DocumentoIngreso documentoIngreso, MultipartFile file) throws IOException {

		DocumentoIngreso respDocumentoIngreso = new DocumentoIngreso();
		String pathFoto = this.guardarImagenEnDisco(file);

		if (pathFoto.equals("")) {
			throw new RuntimeException("Existió un error al guardar la imágen en disco");
		} else {

			documentoIngreso.setDoiImagen(pathFoto);
			respDocumentoIngreso = documentoIngresoService.modificar(documentoIngreso);

			if (respDocumentoIngreso == null) {
				logger.error("Error al modificar el Documento de Ingreso");
				throw new PersistException("Error al modificar el Documento de Ingreso");
			} else {
				return respDocumentoIngreso;
			}
		}
	}

	/**
	 * Guarda la imágen en disco
	 * 
	 * @param file
	 * @return la dirección de la imagen guardada en disco
	 */
	public String guardarImagenEnDisco(MultipartFile file) {
		String path = "";
		String fileName = file.getOriginalFilename();

		File outputFile = new File(this.getPathFotosDocumentoIngreso() + fileName);

		FileOutputStream fos = null;

		try {

			fos = new FileOutputStream(outputFile);

			// Writes bytes from the specified byte array to this file output stream
			fos.write(file.getBytes());

		} catch (FileNotFoundException e) {
			path = "";
			logger.error("Archivo no encontrado" + e);
		} catch (IOException ioe) {
			path = "";
			logger.error("Ocurrió una excepción mientras se escribía el archivo " + ioe);
		} finally {
			// close the streams using close method
			try {

				if (fos != null) {
					fos.close();
				}

				path = renombrarArchivo(outputFile).getFileName().toString();

			} catch (IOException ioe) {
				path = "";
				logger.error("Error mientras se cerraba el stream: " + ioe);
			}
		}
		return path;
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
	 * renombra una archivo
	 * 
	 * @param oldFile el archivo q se quiere renombrar
	 * @return el objeto Path del archivo renombrado
	 */
	public Path renombrarArchivo(File oldFile) {

		String ext = oldFile.getName().substring(oldFile.getName().length() - 3, oldFile.getName().length());
		String name = oldFile.getName().substring(0, oldFile.getName().length() - 4);

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH_mm_ss_SSS");

		String ruta2 = this.getPathFotosDocumentoIngreso() + name + "_" + now.format(formatter) + "." + ext;

		Path source = oldFile.toPath();
		Path newFile = Paths.get(ruta2);
		try {
			Files.move(source, source.resolveSibling(newFile.getFileName()));
			logger.info("Archivo: " + oldFile.getName() + " Renombrado a: " + newFile.getFileName());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("¡Lo siento! el archivo no puede ser renombrado");
			throw new RuntimeException("¡Lo siento! el archivo no puede ser renombrado");
		}
		return newFile;
	}

	public String guardarDocumentos(MultipartFile[] files, List<DocumentoIngreso> listaDocs) {
		String message = "";

		try {
			List<String> fileNames = new ArrayList<>();

			Arrays.asList(files).stream().forEach(file -> {
				listaDocs.forEach(doc -> {
					if (doc.getDoiImagen().equals(file.getOriginalFilename())) {
						try {
							// subimos la foto del documento
							this.subirImagen(doc, file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				fileNames.add(file.getOriginalFilename());
			});

			message = "Uploaded the files successfully: " + fileNames;
			return message;
		} catch (Exception e) {
			message = "Fail to upload files!";
			return message;
		}
	}

}
