package com.albo.service;

import java.util.List;

import com.albo.model.DocumentoIngreso;

public interface IDocumentoIngresoService extends ICRUD<DocumentoIngreso> {

	byte[] leerArchivo(String pathFoto);

	List<DocumentoIngreso> buscarXVisita(Long visita);

}
