package com.albo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.albo.model.Visita;

public interface IVisitaService extends ICRUD<Visita> {

	List<Visita> buscarXCiNoTieneSalida(String ci);

	Page<Visita> visitantesSinSalida(Pageable pageable, LocalDateTime fechaInicio, LocalDateTime fechaFin,
			String recinto, Long areaRecinto);

	Page<Visita> visitantesConSalida(Pageable pageable, LocalDateTime fechaInicio, LocalDateTime fechaFin,
			String recinto, Long areaRecinto);

	Page<Visita> visitantesSinSalida2(Pageable pageable, LocalDateTime fechaInicio, LocalDateTime fechaFin,
			String recinto);

	Page<Visita> visitantesConSalida2(Pageable pageable, LocalDateTime fechaInicio, LocalDateTime fechaFin,
			String recinto);

	Visita buscaXCiUltimoSinSalida(String ci, String recCod);

	Page<Visita> buscarXCiConSalida(Pageable pageable, String ci, LocalDateTime fechaInicio, LocalDateTime fechaFin,
			String recinto);

	Page<Visita> buscarXCiConSalidaAreaRecinto(Pageable pageable, String ci, LocalDateTime fechaInicio,
			LocalDateTime fechaFin, String recinto, Long areaRecinto);

	Page<Visita> buscarXCiSinSalida(Pageable pageable, String ci, LocalDateTime fechaInicio, LocalDateTime fechaFin,
			String recinto);

	Page<Visita> buscarXCiSinSalidaAreaRecinto(Pageable pageable, String ci, LocalDateTime fechaInicio,
			LocalDateTime fechaFin, String recinto, Long areaRecinto);

}
