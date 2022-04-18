package com.albo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albo.dao.IParametroRecintoDAO;
import com.albo.model.ParametroRecinto;
import com.albo.service.IParametroRecintoService;

@Service
public class ParametroRecintoServiceImpl implements IParametroRecintoService {

	@Autowired
	private IParametroRecintoDAO parametroRecintoDao;

	@Override
	public ParametroRecinto registrar(ParametroRecinto t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParametroRecinto modificar(ParametroRecinto t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminar(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public ParametroRecinto listarId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ParametroRecinto> listar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParametroRecinto buscarXNombreParamXRecinto(String nombreParam, String recCod) {
		return parametroRecintoDao.buscarXNombreParamXRecinto(nombreParam, recCod);
	}

	@Override
	public ParametroRecinto buscarXNombreParamGeneral(String nombreParam) {
		return parametroRecintoDao.buscarXNombreParamGeneral(nombreParam);
	}

}
