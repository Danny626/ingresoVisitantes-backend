package com.albo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.albo.model.Visitante;

public interface IVisitanteService extends ICRUD<Visitante> {

	Visitante verificarCorreo(String correo);

	byte[] leerArchivo(String pathFoto);

	Page<Visitante> buscarXNombre(Pageable pageable, String vteNombre);

	Page<Visitante> listarPageable(Pageable pageable);
	
	List<Visitante> buscarXCi(String ci);

}
