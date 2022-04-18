package com.albo.service;

import java.util.List;

import com.albo.model.Recinto;

public interface IRecintoService extends ICRUD<Recinto> {

	List<Recinto> buscarRecintoXAduana(Integer aduCod);

}
