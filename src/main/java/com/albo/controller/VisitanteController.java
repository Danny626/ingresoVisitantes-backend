package com.albo.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

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
import org.springframework.web.multipart.MultipartFile;

import com.albo.exception.list.ModelNotFoundException;
import com.albo.exception.list.PersistException;
import com.albo.model.ParametroRecinto;
import com.albo.model.Visitante;
import com.albo.service.IParametroRecintoService;
import com.albo.service.IVisitanteService;
import com.albo.util.CryptoUtil;
import com.albo.util.EmailService;
import com.albo.util.Mail;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@RestController
@RequestMapping("/visitante")
public class VisitanteController {

	private static final Logger logger = LogManager.getLogger(VisitanteController.class);

	private static final String parametroPathFotosVis = "PATH_FOTOS_VISITANTES";

	private byte[] byteArrayImage;

	@Autowired
	private IVisitanteService visitanteService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private IParametroRecintoService parametroRecintoService;

	// cadena q contiene el parámetro del PATH_FOTOS_VISITANTES
//	private static final String pathFotosVis = "C:\\fotosVisitantes\\";

	public String getPathFotosVis() {

		return getPathFotosVisParam(parametroPathFotosVis);
	}

	public byte[] getByteArrayImage() {
		return byteArrayImage;
	}

	public void setByteArrayImage(byte[] byteArrayImage) {
		this.byteArrayImage = byteArrayImage;
	}

	/**
	 * Método que lista los Visitantes
	 * 
	 * @return Devuelve una lista de Visitante.
	 */
	@GetMapping(value = "/lista", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Visitante>> listaVisitante(Pageable pageable) {
		Page<Visitante> lista;

		lista = visitanteService.listarPageable(pageable);

		return new ResponseEntity<Page<Visitante>>(lista, HttpStatus.OK);
	}

	/**
	 * Buscar Visitantes por ci.
	 * 
	 * @param ci parámetro llave del visitante.
	 * @return Devuelve una lista Visitante
	 */
	// @PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/buscarXCi", method = RequestMethod.GET, params = {
			"ci" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Visitante>> buscarPorCi(@RequestParam("ci") String ci) {
		List<Visitante> lista = new ArrayList<>();
		
		lista = visitanteService.buscarXCi(ci + "%");
		
		return new ResponseEntity<List<Visitante>>(lista, HttpStatus.OK);
	}

	/**
	 * busca un visitante por la llave del QR
	 * 
	 * @param llave
	 * @return un objeto visitante
	 */
	@Transactional
	@RequestMapping(value = "/buscarXQR", method = RequestMethod.GET, params = {
			"llave" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Visitante> buscarPorQR(@RequestParam("llave") String llave) {

		Visitante visitante = new Visitante();

		String cadenaQR = CryptoUtil.decrypt(llave, "albosa");
		String[] cadena = cadenaQR.split("\\|");

		visitante = visitanteService.listarId(cadena[0]);

		if (visitante == null) {
			throw new ModelNotFoundException("ID: " + cadena[0]);
		}
		return new ResponseEntity<Visitante>(visitante, HttpStatus.OK);
	}

//	@PostMapping(value = "/registrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Visitante> registrarVisitante(@RequestBody VisitanteFotoDTO visitanteFotoDTO)
//			throws IOException {

	/**
	 * Método para guardar un Visitante.
	 * 
	 * @param vis   parámetro que contiene al objeto Visitante a guardarse pero en
	 *              una cadena de texto..
	 * @param files
	 * @return Devuelve el objeto Visitante guardado.
	 * @throws IOException
	 */
	@Transactional
	@PostMapping(value = "/registrar", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Visitante> registrarVisitante(@RequestParam("vte") String vte,
			@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

		Visitante respVisitante = new Visitante();

		ObjectMapper mapper = new ObjectMapper();
		Visitante visitante = mapper.readValue(vte, Visitante.class);

		visitante.setVteEstado("ACT");
		visitante.setVteFecha(LocalDateTime.now());
		String llave = visitante.getVteCi() + "|" + visitante.getVteCorreo() + "|" + visitante.getVteApellidos() + "|"
				+ this.obtenerFechaActual();
		// codificamos los datos de arriba para guardarlos en el campo vteLlave
		visitante.setVteLlave(CryptoUtil.encrypt(llave, "albosa"));

		if (visitante.getVteCi() != null) {
			
			/* verifiacamos que el correo no sea repetido */
			Visitante vis = null;
			try {
				vis = visitanteService.verificarCorreo(visitante.getVteCorreo());
				vis = vis != null ? vis : new Visitante();
			} catch (Exception e) {
				vis = new Visitante();
			}
			
			if(vis.getVteCi() == null) {
				/* registramos en bd el objeto */
				respVisitante = visitanteService.registrar(visitante);

				/* Verificamos si el registro se guardó correctamente */
				if (respVisitante == null) {
					logger.error("Error al guardar al Visitante");
					throw new PersistException("Error al guardar al Visitante");
				} else {

					// guardamos la foto del visitante en disco
					this.subirImagen(respVisitante, file);

					// enviamos el correo electrónico con sus datos de ingreso al visitante
					this.enviarCorreo(respVisitante.getVteCorreo());

					return new ResponseEntity<Visitante>(respVisitante, HttpStatus.CREATED);
				}
			} else {
				throw new RuntimeException("El correo del visitante ya existe, por favor ingrese otro");
			}
		} else {
			throw new RuntimeException("El Visitante se encuentra vacio");
		}
	}

	/**
	 * Actualiza los datos del visitante excepto la imágen
	 * 
	 * @param visitante
	 * @return devuelve el objeto visitante actualizado
	 */
	@Transactional
	@PutMapping(value = "/editar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Visitante> editarVisitante(@RequestBody Visitante visitante) {

		if (visitante.getVteCi() != null) {

			Visitante respVisitante = new Visitante();

			Visitante vteAntiguo = visitanteService.listarId(visitante.getVteCi());
			boolean cambiarLlave = false;

			// verificamos si el correo o los apellidos se modificaron para asi proceder a
			// cambiar la llave de ingreso a recinto
			if (!vteAntiguo.getVteCorreo().equals(visitante.getVteCorreo())) {
				cambiarLlave = true;
			}

			if (!vteAntiguo.getVteApellidos().equals(visitante.getVteApellidos())) {
				cambiarLlave = true;
			}

			if (cambiarLlave) {
				String llave = visitante.getVteCi() + "|" + visitante.getVteCorreo() + "|" + visitante.getVteApellidos()
						+ "|" + this.obtenerFechaActual();

				// codificamos los datos de arriba para guardarlos en el campo vteLlave
				visitante.setVteLlave(CryptoUtil.encrypt(llave, "albosa"));
			}

			/* registramos en bd el objeto */
			respVisitante = visitanteService.modificar(visitante);

			/* Verificamos si el registro se guardó correctamente */
			if (respVisitante == null) {
				logger.error("Error al modificar al Visitante");
				throw new PersistException("Error al modificar al Visitante");
			} else {

				// si la llave cambió, enviamos el correo electrónico con sus datos de ingreso
				// al visitante
				if (cambiarLlave) {
					this.enviarCorreo(respVisitante.getVteCorreo());
				}

				return new ResponseEntity<Visitante>(respVisitante, HttpStatus.OK);
			}
		} else {
			throw new RuntimeException("El Visitante se encuentra vacio");
		}
	}

	/**
	 * sube la nueva imágen al disco y el path a la bd
	 * 
	 * @param vte  visitante
	 * @param file archivo con la imágen
	 * @return retorna el visitante con la ubicación de la imágen modificada
	 * @throws IOException
	 */
	@Transactional
	@PostMapping(value = "/subirImagen", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Visitante> registrarImagen(@RequestParam("vte") String vte,
			@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

		Visitante respVisitante = new Visitante();

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		Visitante visitante = mapper.readValue(vte, Visitante.class);

		if (visitante.getVteCi() != null) {

			// borrar antigua imágen
			File fileAntiguo = new File(this.getPathFotosVis() + visitante.getVteImagen());
			this.eliminarImagen(fileAntiguo);

			// guardamos la foto del visitante en disco
			respVisitante = this.subirImagen(visitante, file);

			/* Verificamos si el registro se guardó correctamente */
			if (respVisitante == null) {
				logger.error("Error al guardar la nueva imágen");
				throw new PersistException("Error al guardar la nueva imágen");
			} else {

				return new ResponseEntity<Visitante>(respVisitante, HttpStatus.OK);
			}
		} else {
			throw new RuntimeException("El Visitante se encuentra vacio");
		}
	}

	/**
	 * Elimina un archivo
	 * 
	 * @param file archivo a ser eliminado
	 * @return true si existió algun error de lo contrario false
	 */
	public boolean eliminarImagen(File file) {
		boolean error = false;

		FileInputStream readImage = null;
		try {
			readImage = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			error = true;
			e.printStackTrace();
			throw new RuntimeException("Error, archivo no encontrado");
		} finally {
			try {
				readImage.close();
			} catch (IOException e) {
				error = true;
				e.printStackTrace();
				throw new RuntimeException("Error al cerrar el stream");
			}

			if (!file.delete()) {
				error = true;
			}
		}

		return error;
	}

	/**
	 * Obtiene una cadena de texto con la fecha actual
	 * 
	 * @return devuelve un string con la fecha actual "05-05-2020 08:57:24"
	 */
	public String obtenerFechaActual() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		return now.format(formatter);
	}

	/**
	 * Envía un email al visitante con el código QR para su ingreso
	 * 
	 * @param correo correo perteneciente al visitante
	 * @return retorna 0 en caso de fallo y 1 en caso de success
	 */
//	@PostMapping(value = "/enviarCorreoIngreso", consumes = MediaType.TEXT_PLAIN_VALUE)
//	public ResponseEntity<Integer> enviarCorreo(@RequestBody String correo) {
	@RequestMapping(value = "/enviarCorreoIngreso", method = RequestMethod.GET, params = {
			"correo" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> enviarCorreo(@RequestParam("correo") String correo) {
		int rpta = 0;
		try {

			Visitante vte = visitanteService.verificarCorreo(correo);

			if (vte != null && vte.getVteCi() != null) {
				Mail mail = new Mail();
				mail.setFrom("info@albo.com.bo");
				mail.setTo(vte.getVteCorreo());
				mail.setSubject("Datos de Ingreso - ALBO S.A.");

				Map<String, Object> model = new HashMap<>();
				model.put("user", "Hola " + vte.getVteNombre() + ",");
				String src = this.convertBufferedImageToBase64(this.generateQRCodeImage(vte.getVteLlave()));
				model.put("src", src);
				mail.setModel(model);
				mail.setAdjunto(this.byteArrayImage);
				emailService.sendEmail(mail, "email-datosIngreso-template");
				rpta = 1;
			}
		} catch (Exception e) {
			return new ResponseEntity<Integer>(rpta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Integer>(rpta, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/enviarCorreoIngreso2", method = RequestMethod.GET, params = {
	"correo" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> enviarCorreo2(@RequestParam("correo") String correo) {
		int rpta = 0;
		try {
		
			Visitante vte = visitanteService.verificarCorreo(correo);
		
			if (vte != null && vte.getVteCi() != null) {
				System.out.println("CORREO VISITANTE AQUIIIIII " + vte.getVteCorreo());
				
				Mail mail = new Mail();
				mail.setFrom("info@albo.com.bo");
				mail.setTo(vte.getVteCorreo());
				mail.setSubject("Datos de Ingreso - ALBO S.A.");
		
				Map<String, Object> model = new HashMap<>();
				model.put("user", "Hola " + vte.getVteNombre() + ",");
				String src = this.convertBufferedImageToBase64(this.generateQRCodeImage(vte.getVteLlave()));
				model.put("src", src);
				mail.setModel(model);
				mail.setAdjunto(this.byteArrayImage);
				System.out.println("MAIL AQUIIIIII " + mail.getAdjunto());
				emailService.sendEmail(mail, "email-datosIngreso-template");
				rpta = 1;
			}
		} catch (Exception e) {
			return new ResponseEntity<Integer>(rpta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Integer>(rpta, HttpStatus.OK);
	}
	
	
	/**
	 * obtiene la foto del visitante
	 * 
	 * @param foto nombre del archivo de la foto del visitante.
	 * @return la imágen de la foto
	 */
	@RequestMapping(value = "/mostrarFoto", method = RequestMethod.GET, params = {
			"foto" }, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> mostrarFoto(@RequestParam("foto") String foto) {

		String pathFoto = this.getPathFotosVis() + foto;
		byte[] data = null;

		data = visitanteService.leerArchivo(pathFoto);
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}

	/**
	 * obtiene el QR del visitante
	 * 
	 * @param ci documento de identidad del visitante.
	 * @return la imágen QR del visitante
	 */
	@RequestMapping(value = "/mostrarQR", method = RequestMethod.GET, params = {
			"ci" }, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> mostrarQR(@RequestParam("ci") String ci) {

		Visitante visitante = new Visitante();
		visitante = visitanteService.listarId(ci);

		if (visitante == null) {
			throw new ModelNotFoundException("ID: " + ci);
		} else {
			try {
				this.convertBufferedImageToBase64(this.generateQRCodeImage(visitante.getVteLlave()));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return new ResponseEntity<byte[]>(this.byteArrayImage, HttpStatus.OK);
	}

	/**
	 * obtiene listado de visitantes
	 * 
	 * @param nombre nombre del visitante.
	 * @return lista de visitantes
	 */
	@RequestMapping(value = "/buscarPorNombre", method = RequestMethod.GET, params = {
			"nombre" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Visitante>> buscarPorNombre(@RequestParam("nombre") String nombre, Pageable pageable) {
		Page<Visitante> lista;

		lista = visitanteService.buscarXNombre(pageable, nombre + "%");

		return new ResponseEntity<Page<Visitante>>(lista, HttpStatus.OK);
	}

	/**
	 * Genera un cod. QR a partir de un texto
	 * 
	 * @param barcodeText texto a convertir a QR
	 * @return Retorna un BufferedImage
	 * @throws Exception
	 */
	public BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
		QRCodeWriter barcodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);

		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}

	/**
	 * Convierte el BufferedImage a Base64
	 * 
	 * @param imageBytes
	 * @return retorna un String con el src del QR en base 64
	 * @throws IOException
	 */
	public String convertBufferedImageToBase64(BufferedImage imageBytes) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(imageBytes, "JPEG", out);
		byte[] bytes = out.toByteArray();
		this.setByteArrayImage(bytes);
		String base64bytes = Base64.getEncoder().encodeToString(bytes);
		return "data:image/jpeg;base64," + base64bytes;
	}

	/**
	 * sube la foto del visitante al disco del servidor y modifica su ubicación en
	 * vteImagen
	 * 
	 * @param visitante
	 * @param file
	 * @return el objeto visitante con la ubicación de su foto actualizada
	 * @throws IOException
	 */
	public Visitante subirImagen(Visitante visitante, MultipartFile file) throws IOException {

		Visitante respVisitante = new Visitante();
		String pathFoto = this.guardarImagenEnDisco(file);

		if (pathFoto.equals("")) {
			throw new RuntimeException("Existió un error al guardar la imágen en disco");
		} else {

			visitante.setVteImagen(pathFoto);
			respVisitante = visitanteService.modificar(visitante);

			if (respVisitante == null) {
				logger.error("Error al modificar el Visitante");
				throw new PersistException("Error al modificar el Visitante");
			} else {
				return respVisitante;
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

		File outputFile = new File(this.getPathFotosVis() + fileName);

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

		String ruta2 = this.getPathFotosVis() + name + "_" + now.format(formatter) + "." + ext;

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

	/**
	 * busca el valor del parametro PATH_FOTOS_VISITANTES en la bd
	 * 
	 * @return el valor del parametro PATH_FOTOS_VISITANTES
	 */
	public String getPathFotosVisParam(String parametroPathFotos) {

		ParametroRecinto parametroRecinto = new ParametroRecinto();
		parametroRecinto = parametroRecintoService.buscarXNombreParamGeneral(parametroPathFotos);

		if (parametroRecinto != null) {
			return parametroRecinto.getParValor();
		} else {
			logger.error("¡Error! No se encontró el parámetro PATH_FOTOS_VISITANTES");
			throw new RuntimeException("¡Error! No se encontró el parámetro PATH_FOTOS_VISITANTES");
		}

	}
}
