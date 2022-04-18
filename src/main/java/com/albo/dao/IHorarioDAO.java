package com.albo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.albo.model.Horario;

@Repository
public interface IHorarioDAO extends JpaRepository<Horario, Integer> {

	// busca Horario por recinto, tipo visitante y tipo dia
	@Query("FROM Horario hor WHERE hor.recinto.recCod LIKE :recCod " + "AND hor.tipoVisitante.tviCod = :tviCod "
			+ "AND hor.horNombre LIKE :horNombre " + "AND hor.horDias LIKE :dia")
	List<Horario> buscarXRecTVisitante(@Param("recCod") String recCod, @Param("tviCod") Long tviCod,
			@Param("horNombre") String horNombre, @Param("dia") String dia);

}
