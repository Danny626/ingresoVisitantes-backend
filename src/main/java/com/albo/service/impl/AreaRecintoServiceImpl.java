package com.albo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albo.dao.IAreaRecintoDAO;
import com.albo.model.AreaRecinto;
import com.albo.service.IAreaRecintoService;

@Service
public class AreaRecintoServiceImpl implements IAreaRecintoService {

	@Autowired
	private IAreaRecintoDAO areaRecintoDao;

	@Override
	public AreaRecinto registrar(AreaRecinto t) {
		return areaRecintoDao.save(t);
	}

	@Override
	public AreaRecinto modificar(AreaRecinto t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminar(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public AreaRecinto listarId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AreaRecinto> listar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AreaRecinto> buscarXRecinto(String recCod) {
		return areaRecintoDao.buscarXRecinto(recCod);
	}

}
