package com.albo.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albo.dao.IDocumentoIngresoDAO;
import com.albo.model.DocumentoIngreso;
import com.albo.service.IDocumentoIngresoService;

@Service
public class DocumentoIngresoServiceImpl implements IDocumentoIngresoService {

	@Autowired
	private IDocumentoIngresoDAO documentoIngresoDao;

	@Override
	public DocumentoIngreso registrar(DocumentoIngreso t) {
		return documentoIngresoDao.save(t);
	}

	@Override
	public DocumentoIngreso modificar(DocumentoIngreso t) {
		return documentoIngresoDao.save(t);
	}

	@Override
	public void eliminar(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public DocumentoIngreso listarId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentoIngreso> listar() {
		// TODO Auto-generated method stub
		return null;
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
	public List<DocumentoIngreso> buscarXVisita(Long visita) {
		return documentoIngresoDao.buscarXVisita(visita);
	}
}
