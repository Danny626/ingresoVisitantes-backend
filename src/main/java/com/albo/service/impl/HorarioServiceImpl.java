package com.albo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albo.dao.IHorarioDAO;
import com.albo.model.Horario;
import com.albo.service.IHorarioService;

@Service
public class HorarioServiceImpl implements IHorarioService {

	@Autowired
	private IHorarioDAO horarioDao;

	@Override
	public Horario registrar(Horario t) {
		return horarioDao.save(t);
	}

	@Override
	public Horario modificar(Horario t) {
		return horarioDao.save(t);
	}

	@Override
	public void eliminar(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Horario listarId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Horario> listar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Horario> buscarXRecTVisitante(String recCod, Long tviCod, String horNombre, String dia) {
		return horarioDao.buscarXRecTVisitante(recCod, tviCod, horNombre, dia);
	}

}
