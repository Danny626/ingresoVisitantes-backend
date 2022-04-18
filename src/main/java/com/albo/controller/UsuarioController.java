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
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.albo.exception.list.PersistException;
import com.albo.model.ParametroRecinto;
import com.albo.model.Usuario;
import com.albo.service.IParametroRecintoService;
import com.albo.service.IUsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	private static final Logger logger = LogManager.getLogger(UsuarioController.class);

	private static final String parametroPathFotosUsr = "PATH_FOTOS_USUARIOS";

	@Autowired
	private IUsuarioService service;

	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@Autowired
	private IParametroRecintoService parametroRecintoService;

	public String getPathFotosUsr() {
		return getPathFotosUsrParam(parametroPathFotosUsr);
	}

	// @PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Usuario>> listar() {
		List<Usuario> usuarios = new ArrayList<>();
		usuarios = service.listar();
		return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);
	}

	@GetMapping(value = "/lista", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Usuario>> listarPageable(Pageable pageable) {
		Page<Usuario> usuarios;
		usuarios = service.listarPageable(pageable);
		return new ResponseEntity<Page<Usuario>>(usuarios, HttpStatus.OK);
	}

	// @PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Usuario> listarId(@PathVariable("id") Integer id) {
		Usuario usuario = new Usuario();
		usuario = service.listarId(String.valueOf(id));
		if (usuario == null) {
//			throw new ModeloNotFoundException("ID: " + id);
		}
		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
	}

	// servicio busca usuario por nombre de usuario requiriendo el parametro con
	// http://localhost:8080/usuarios?username=danny@albo.com.bo
	@RequestMapping(params = "username", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Usuario> listarXUsername(@RequestParam String username) throws Exception {
		Usuario usuario = new Usuario();
		usuario = service.listarPorUsername(username);
		if (usuario == null) {
//			throw new ModeloNotFoundException("Usuario: " + username);
		} else {
			usuario.setPassword(":)");
		}
		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
	}

//	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Usuario> registrar(@Valid @RequestBody Usuario usuario) { // Valid para validar lo anotado en
//																					// el modelo Signo: @Min; @Max;
//																					// @Size
//		Usuario obj = new Usuario();
//		usuario.setPassword(bcrypt.encode(usuario.getPassword()));
//		obj = service.registrar(usuario);
//		if (obj == null) {
//			System.out.println("error al registrar?????");
////			throw new InternalException("Error al registrar");
//		}
//		return new ResponseEntity<Usuario>(obj, HttpStatus.CREATED);
//	}

	@Transactional
	@PostMapping(value = "/registrar", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Usuario> registrarUsuario(@RequestParam("usr") String usr,
			@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

		Usuario respUsuario = new Usuario();

		ObjectMapper mapper = new ObjectMapper();
		Usuario usuario = mapper.readValue(usr, Usuario.class);

		usuario.setState("ACT");
		usuario.setPassword(bcrypt.encode(usuario.getPassword()));

		if (usuario.getUsername() != null) {

			/* registramos en bd el objeto */
			respUsuario = service.registrar(usuario);

			/* Verificamos si el registro se guardó correctamente */
			if (respUsuario == null) {
				logger.error("Error al guardar el Usuario");
				throw new PersistException("Error al guardar el Usuario");
			} else {

				// guardamos la foto del usuario en disco
				this.subirImagen(respUsuario, file);

				return new ResponseEntity<Usuario>(respUsuario, HttpStatus.CREATED);
			}
		} else {
			throw new RuntimeException("El Usuario se encuentra vacio");
		}
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Usuario> actualizar(@RequestBody Usuario usuario) {
		Usuario obj = new Usuario();
		if (usuario.getPassword() != null) {
			usuario.setPassword(bcrypt.encode(usuario.getPassword()));
		}
		obj = service.modificar(usuario);
		return new ResponseEntity<Usuario>(obj, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void eliminar(@PathVariable Integer id) {
		Usuario usr = service.listarId(String.valueOf(id));
		if (usr == null) {
//			throw new ModeloNotFoundException("ID: " + id);
		} else {
			service.eliminar(String.valueOf(id));
		}
	}

	@RequestMapping(value = "/buscarPorUsername", method = RequestMethod.GET, params = {
			"username" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Usuario>> buscarPorUsername(@RequestParam("username") String username,
			Pageable pageable) {
		Page<Usuario> lista;

		lista = service.buscarXUsername(pageable, username + "%");

		return new ResponseEntity<Page<Usuario>>(lista, HttpStatus.OK);
	}

	/**
	 * obtiene la foto del usuario
	 * 
	 * @param foto nombre del archivo de la foto del usuario.
	 * @return la imágen de la foto
	 */
	@RequestMapping(value = "/mostrarFoto", method = RequestMethod.GET, params = {
			"foto" }, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> mostrarFoto(@RequestParam("foto") String foto) {

		String pathFoto = this.getPathFotosUsr() + foto;
		System.out.println(pathFoto);
		byte[] data = null;

		data = service.leerArchivo(pathFoto);
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}

	/**
	 * sube la foto del usuario al disco del servidor y modifica su ubicación en
	 * imagen
	 * 
	 * @param usuario
	 * @param file
	 * @return el objeto usuario con la ubicación de su foto actualizada
	 * @throws IOException
	 */
	public Usuario subirImagen(Usuario usuario, MultipartFile file) throws IOException {

		Usuario respUsuario = new Usuario();
		String pathFoto = this.guardarImagenEnDisco(file);

		if (pathFoto.equals("")) {
			throw new RuntimeException("Existió un error al guardar la imágen en disco");
		} else {

			usuario.setPic(pathFoto);
			respUsuario = service.modificar(usuario);

			if (respUsuario == null) {
				logger.error("Error al modificar el Usuario");
				throw new PersistException("Error al modificar el Usuario");
			} else {
				return respUsuario;
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

		File outputFile = new File(this.getPathFotosUsr() + fileName);

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
	 * busca el valor del parametro PATH_FOTOS_USUARIOS en la bd
	 * 
	 * @return el valor del parametro PATH_FOTOS_USUARIOS
	 */
	public String getPathFotosUsrParam(String parametroPathFotos) {

		ParametroRecinto parametroRecinto = new ParametroRecinto();
		parametroRecinto = parametroRecintoService.buscarXNombreParamGeneral(parametroPathFotos);

		if (parametroRecinto != null) {
			return parametroRecinto.getParValor();
		} else {
			logger.error("¡Error! No se encontró el parámetro PATH_FOTOS_USUARIOS");
			throw new RuntimeException("¡Error! No se encontró el parámetro PATH_FOTOS_USUARIOS");
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

		String ruta2 = this.getPathFotosUsr() + name + "_" + now.format(formatter) + "." + ext;

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

}
