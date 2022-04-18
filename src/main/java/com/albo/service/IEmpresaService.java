package com.albo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.albo.model.Empresa;

public interface IEmpresaService extends ICRUD<Empresa> {

	Page<Empresa> buscarXNombre(Pageable pageable, String empNombre);

	Page<Empresa> listarPageable(Pageable pageable);
}
