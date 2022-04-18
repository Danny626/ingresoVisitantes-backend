package com.albo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albo.dao.ITipoVisitanteDAO;
import com.albo.model.TipoVisitante;
import com.albo.service.ITipoVisitanteService;

@Service
public class TipoVisitanteServiceImpl implements ITipoVisitanteService {

	@Autowired
	private ITipoVisitanteDAO tipoVisitanteDao;

	@Override
	public TipoVisitante registrar(TipoVisitante t) {
		return tipoVisitanteDao.save(t);
	}

	@Override
	public TipoVisitante modificar(TipoVisitante t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminar(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public TipoVisitante listarId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TipoVisitante> listar() {
		return tipoVisitanteDao.findAll();
	}

}
