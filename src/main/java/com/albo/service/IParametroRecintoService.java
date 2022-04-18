package com.albo.service;

import com.albo.model.ParametroRecinto;

public interface IParametroRecintoService extends ICRUD<ParametroRecinto> {

	public ParametroRecinto buscarXNombreParamXRecinto(String nombreParam, String recCod);

	public ParametroRecinto buscarXNombreParamGeneral(String nombreParam);

}
