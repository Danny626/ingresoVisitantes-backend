package com.albo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.albo.model.DocumentoIngreso;

@Repository
public interface IDocumentoIngresoDAO extends JpaRepository<DocumentoIngreso, Integer> {

	@Query("FROM DocumentoIngreso docIng WHERE docIng.visita.visCod = :visita")
	List<DocumentoIngreso> buscarXVisita(@Param("visita") Long visita);

}
