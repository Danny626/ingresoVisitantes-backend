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

import com.albo.dao.IVisitanteDAO;
import com.albo.model.Visitante;
import com.albo.service.IVisitanteService;

@Service
public class VisitanteServiceImpl implements IVisitanteService {

	@Autowired
	private IVisitanteDAO visitanteDao;

	@Override
	public Visitante registrar(Visitante t) {
		return visitanteDao.save(t);
	}

	@Override
	public Visitante modificar(Visitante t) {
		return visitanteDao.save(t);
	}

	@Override
	public void eliminar(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Visitante listarId(String id) {
		return visitanteDao.findById(id).orElse(null);
	}

	@Override
	public List<Visitante> listar() {
		return visitanteDao.findAll();
	}

	@Override
	public Visitante verificarCorreo(String correo) {
		Visitante vte = null;
		try {
			vte = visitanteDao.verificarCorreo(correo);
			vte = vte != null ? vte : new Visitante();
		} catch (Exception e) {
			vte = new Visitante();
		}
		return vte;
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

	@Override
	public Page<Visitante> buscarXNombre(Pageable pageable, String vteNombre) {
		return visitanteDao.buscarXNombre(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("vteApellidos").ascending()),
				vteNombre);
	}

	@Override
	public Page<Visitante> listarPageable(Pageable pageable) {
		return visitanteDao.findAll(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("vteApellidos").ascending()));
	}

	@Override
	public List<Visitante> buscarXCi(String ci) {
		return visitanteDao.buscarXCi(ci);
	}

}
