package com.albo.service;

import java.util.List;

import com.albo.model.AreaRecinto;

public interface IAreaRecintoService extends ICRUD<AreaRecinto> {

	List<AreaRecinto> buscarXRecinto(String recCod);

}
