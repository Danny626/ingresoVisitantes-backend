package com.albo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albo.dao.IRecintoDAO;
import com.albo.model.Recinto;
import com.albo.service.IRecintoService;

@Service
public class RecintoServiceImpl implements IRecintoService {

	@Autowired
	private IRecintoDAO recintoDao;

	@Override
	public Recinto registrar(Recinto t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Recinto modificar(Recinto t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminar(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Recinto listarId(String id) {
		return recintoDao.findById(id).orElse(null);
	}

	@Override
	public List<Recinto> listar() {
		return recintoDao.findAll();
	}

	@Override
	public List<Recinto> buscarRecintoXAduana(Integer aduCod) {
		return recintoDao.buscarRecintoXAduana(aduCod);
	}

}
