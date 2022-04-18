package com.albo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albo.dao.IMotivoIngresoDAO;
import com.albo.model.MotivoIngreso;
import com.albo.service.IMotivoIngresoService;

@Service
public class MotivoIngresoServiceImpl implements IMotivoIngresoService {

	@Autowired
	private IMotivoIngresoDAO motivoIngresoDao;

	@Override
	public MotivoIngreso registrar(MotivoIngreso t) {
		return motivoIngresoDao.save(t);
	}

	@Override
	public MotivoIngreso modificar(MotivoIngreso t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminar(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public MotivoIngreso listarId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MotivoIngreso> listar() {
		return motivoIngresoDao.findAll();
	}

}
