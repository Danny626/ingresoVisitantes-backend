package com.albo.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.albo.dao.IUsuarioDAO;
import com.albo.model.Usuario;
import com.albo.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	private IUsuarioDAO dao;

	@Override
	public Usuario registrar(Usuario u) {
		return dao.save(u);
	}

	@Override
	public Usuario modificar(Usuario u) {
		return dao.save(u);
	}

	@Override
	public void eliminar(String id) {
		dao.deleteById(Integer.valueOf(id));
	}

	@Override
	public Usuario listarId(String id) {
		return dao.findById(Integer.valueOf(id)).orElse(null);
	}

	@Override
	public List<Usuario> listar() {
		return dao.findAll();
	}

	@Override
	public Page<Usuario> listarPageable(Pageable pageable) {
		return dao.findAll(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("username").ascending()));
	}

	@Override
	public Usuario listarPorUsername(String username) {
		return dao.listarPorUsername(username);
	}

	@Override
	public Page<Usuario> buscarXUsername(Pageable pageable, String username) {
		return dao.buscarXNombre(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("username").ascending()),
				username);
	}

	@Override
	public byte[] leerArchivo(String pathFoto) {
		Path path = Paths.get(pathFoto);
		byte[] bArray = null;

		try {
			bArray = Files.readAllBytes(path);
		} catch (IOException e) {
			throw new RuntimeException("Error. El archivo no puede ser abierto porq no existe");
		}
		return bArray;
	}

}
