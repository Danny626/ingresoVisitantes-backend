package com.albo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albo.dao.ITipoDocumentoDAO;
import com.albo.model.TipoDocumento;
import com.albo.service.ITipoDocumentoService;

@Service
public class TipoDocumentoServiceImpl implements ITipoDocumentoService {

	@Autowired
	public ITipoDocumentoDAO tipoDocumentoDao;

	@Override
	public TipoDocumento registrar(TipoDocumento t) {
		return tipoDocumentoDao.save(t);
	}

	@Override
	public TipoDocumento modificar(TipoDocumento t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminar(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public TipoDocumento listarId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TipoDocumento> listar() {
		return tipoDocumentoDao.findAll();
	}

}
