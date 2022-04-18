package com.albo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.albo.dao.IEmpresaDAO;
import com.albo.model.Empresa;
import com.albo.service.IEmpresaService;

@Service
public class EmpresaServiceImpl implements IEmpresaService {

	@Autowired
	private IEmpresaDAO empresaDao;

	@Override
	public Empresa registrar(Empresa t) {
		return empresaDao.save(t);
	}

	@Override
	public Empresa modificar(Empresa t) {
		return empresaDao.save(t);
	}

	@Override
	public void eliminar(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Empresa listarId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Empresa> listar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Empresa> listarPageable(Pageable pageable) {
		return empresaDao.findAll(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("empNombre").ascending()));
	}

	@Override
	public Page<Empresa> buscarXNombre(Pageable pageable, String empNombre) {
		return empresaDao.buscarXNombre(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("empNombre").ascending()),
				empNombre);
	}

}
