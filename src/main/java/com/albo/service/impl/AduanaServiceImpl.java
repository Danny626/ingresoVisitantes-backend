package com.albo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albo.dao.IAduanaDAO;
import com.albo.model.Aduana;
import com.albo.service.IAduanaService;

@Service
public class AduanaServiceImpl implements IAduanaService {

	@Autowired
	private IAduanaDAO aduanaDao;

	@Override
	public Aduana registrar(Aduana t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Aduana modificar(Aduana t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminar(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Aduana listarId(String id) {
		return aduanaDao.findById(Integer.valueOf(id)).orElse(null);
	}

	@Override
	public List<Aduana> listar() {
		return aduanaDao.findAll();
	}

	@Override
	public List<Aduana> buscarAduanaXRecinto(String recCod) {
		return aduanaDao.buscarAduanaXRecinto(recCod);
	}

}
