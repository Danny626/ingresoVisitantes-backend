package com.albo.service;

import java.util.List;

import com.albo.model.Aduana;

public interface IAduanaService extends ICRUD<Aduana> {

	List<Aduana> buscarAduanaXRecinto(String recCod);

}
