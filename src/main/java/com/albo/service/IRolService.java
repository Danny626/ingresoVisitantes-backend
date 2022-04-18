package com.albo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.albo.model.Rol;

public interface IRolService extends ICRUD<Rol> {

	Page<Rol> listarPageable(Pageable pageable);
}
