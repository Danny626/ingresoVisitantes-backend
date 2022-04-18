package com.albo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.albo.model.Usuario;

public interface IUsuarioService extends ICRUD<Usuario> {

	Page<Usuario> listarPageable(Pageable pageable);

	Usuario listarPorUsername(String username);

	Page<Usuario> buscarXUsername(Pageable pageable, String username);
	
	byte[] leerArchivo(String pathFoto);
}
