package com.albo.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.albo.dao.IVisitaDAO;
import com.albo.model.Visita;
import com.albo.service.IVisitaService;

@Service
public class VisitaServiceImpl implements IVisitaService {

	@Autowired
	private IVisitaDAO visitaDao;

	@Override
	public Visita registrar(Visita t) {
		return visitaDao.save(t);
	}

	@Override
	public Visita modificar(Visita t) {
		return visitaDao.save(t);
	}

	@Override
	public void eliminar(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Visita listarId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Visita> listar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Visita> buscarXCiNoTieneSalida(String ci) {
		return visitaDao.buscarXCiNoTieneSalida(ci);
	}

	@Override
	public Visita buscaXCiUltimoSinSalida(String ci, String recCod) {
		return visitaDao.buscaXCiUltimoSinSalida(ci, recCod);
	}

	@Override
	public Page<Visita> visitantesSinSalida(Pageable pageable, LocalDateTime fechaInicio, LocalDateTime fechaFin,
			String recinto, Long areaRecinto) {
		return visitaDao.buscarVisitantesSinSalida(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("visIngreso").ascending()),
				fechaInicio, fechaFin, recinto, areaRecinto);
	}

	@Override
	public Page<Visita> visitantesConSalida(Pageable pageable, LocalDateTime fechaInicio, LocalDateTime fechaFin,
			String recinto, Long areaRecinto) {
		return visitaDao.buscarVisitantesConSalida(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("visIngreso").ascending()),
				fechaInicio, fechaFin, recinto, areaRecinto);
	}

	@Override
	public Page<Visita> visitantesSinSalida2(Pageable pageable, LocalDateTime fechaInicio, LocalDateTime fechaFin,
			String recinto) {
		return visitaDao.buscarVisitantesSinSalida2(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("visIngreso").ascending()),
				fechaInicio, fechaFin, recinto);
	}

	@Override
	public Page<Visita> visitantesConSalida2(Pageable pageable, LocalDateTime fechaInicio, LocalDateTime fechaFin,
			String recinto) {
		return visitaDao.buscarVisitantesConSalida2(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("visIngreso").ascending()),
				fechaInicio, fechaFin, recinto);
	}

	@Override
	public Page<Visita> buscarXCiConSalida(Pageable pageable, String ci, LocalDateTime fechaInicio,
			LocalDateTime fechaFin, String recinto) {
		return visitaDao.buscarXCiConSalida(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("visIngreso").ascending()), ci,
				fechaInicio, fechaFin, recinto);
	}

	@Override
	public Page<Visita> buscarXCiConSalidaAreaRecinto(Pageable pageable, String ci, LocalDateTime fechaInicio,
			LocalDateTime fechaFin, String recinto, Long areaRecinto) {
		return visitaDao.buscarXCiConSalidaAreaRecinto(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("visIngreso").ascending()), ci,
				fechaInicio, fechaFin, recinto, areaRecinto);
	}

	@Override
	public Page<Visita> buscarXCiSinSalida(Pageable pageable, String ci, LocalDateTime fechaInicio,
			LocalDateTime fechaFin, String recinto) {
		return visitaDao.buscarXCiSinSalida(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("visIngreso").ascending()), ci,
				fechaInicio, fechaFin, recinto);
	}

	@Override
	public Page<Visita> buscarXCiSinSalidaAreaRecinto(Pageable pageable, String ci, LocalDateTime fechaInicio,
			LocalDateTime fechaFin, String recinto, Long areaRecinto) {
		return visitaDao.buscarXCiSinSalidaAreaRecinto(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("visIngreso").ascending()), ci,
				fechaInicio, fechaFin, recinto, areaRecinto);
	}

}
