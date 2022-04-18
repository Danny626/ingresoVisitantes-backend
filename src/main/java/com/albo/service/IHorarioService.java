package com.albo.service;

import java.util.List;

import com.albo.model.Horario;

public interface IHorarioService extends ICRUD<Horario> {

	List<Horario> buscarXRecTVisitante(String recCod, Long tviCod, String horNombre, String dia);

}
