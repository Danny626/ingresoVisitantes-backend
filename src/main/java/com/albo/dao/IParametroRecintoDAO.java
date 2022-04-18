package com.albo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.albo.model.ParametroRecinto;

@Repository
public interface IParametroRecintoDAO extends JpaRepository<ParametroRecinto, Integer> {

	// busca parametro por nombre y recinto
	@Query("FROM ParametroRecinto pr WHERE pr.parametro.pNombre = :nombreParam AND pr.recinto.recCod LIKE :recCod")
	public ParametroRecinto buscarXNombreParamXRecinto(@Param("nombreParam") String nombreParam,
			@Param("recCod") String recCod);

	// busca parametros generales por nombre
	@Query("FROM ParametroRecinto pr WHERE pr.parametro.pNombre = :nombreParam AND pr.recinto.recCod IS NULL")
	public ParametroRecinto buscarXNombreParamGeneral(@Param("nombreParam") String nombreParam);

}
