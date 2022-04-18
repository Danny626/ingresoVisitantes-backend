package com.albo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.albo.dao.IRolDAO;
import com.albo.model.Rol;
import com.albo.service.IRolService;

@Service
public class RolServiceImpl implements IRolService {

	@Autowired
	private IRolDAO dao;

	@Override
	public Rol registrar(Rol r) {
		return dao.save(r);
	}

	@Override
	public Rol modificar(Rol r) {
		return dao.save(r);
	}

	@Override
	public void eliminar(String id) {
		dao.deleteById(Integer.valueOf(id));
	}

	@Override
	public Rol listarId(String id) {
		return dao.findById(Integer.valueOf(id)).orElse(null);
	}

	@Override
	public List<Rol> listar() {
		return dao.findAll();
	}

	@Override
	public Page<Rol> listarPageable(Pageable pageable) {
		return dao.findAll(pageable);
	}
}
