package com.albo.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.albo.model.Visita;

@Repository
public interface IVisitaDAO extends JpaRepository<Visita, Integer> {

	// busca Visitas por CI y si visSalida es null
	@Query("FROM Visita vis WHERE vis.visitante.vteCi LIKE :ci AND vis.visSalida = null")
	List<Visita> buscarXCiNoTieneSalida(@Param("ci") String ci);

	// busca la última Visita por CI y visSalida null
	@Query("FROM Visita vis WHERE vis.visitante.vteCi LIKE :ci AND vis.visSalida = null "
			+ "AND vis.areaRecinto.recinto.recCod = :recCod " + "AND vis.visCod = (SELECT MAX(v.visCod) "
			+ "FROM Visita v WHERE v.visitante.vteCi LIKE :ci "
			+ "AND v.areaRecinto.recinto.recCod = :recCod AND v.visSalida = null)")
	Visita buscaXCiUltimoSinSalida(@Param("ci") String ci, @Param("recCod") String recCod);

	// busca Visitas que ingresaron
	@Query("FROM Visita vis WHERE vis.visSalida = null AND vis.visEstado = 'ACT' "
			+ "AND vis.visIngreso >= :fechaInicio AND vis.visIngreso <= :fechaFin "
			+ "AND vis.areaRecinto.recinto.recCod = :recinto AND vis.areaRecinto.areaCod = :areaRecinto")
	Page<Visita> buscarVisitantesSinSalida(Pageable pageable, @Param("fechaInicio") LocalDateTime fechaInicio,
			@Param("fechaFin") LocalDateTime fechaFin, @Param("recinto") String recinto,
			@Param("areaRecinto") Long areaRecinto);

	// busca Visitas que salieron
	@Query("FROM Visita vis WHERE vis.visSalida != null AND vis.visEstado = 'ACT' "
			+ "AND vis.visIngreso >= :fechaInicio AND vis.visIngreso <= :fechaFin "
			+ "AND vis.areaRecinto.recinto.recCod = :recinto AND vis.areaRecinto.areaCod = :areaRecinto")
	Page<Visita> buscarVisitantesConSalida(Pageable pageable, @Param("fechaInicio") LocalDateTime fechaInicio,
			@Param("fechaFin") LocalDateTime fechaFin, @Param("recinto") String recinto,
			@Param("areaRecinto") Long areaRecinto);

	// busca Visitas que ingresaron
	@Query("FROM Visita vis WHERE vis.visSalida = null AND vis.visEstado = 'ACT' "
			+ "AND vis.visIngreso >= :fechaInicio AND vis.visIngreso <= :fechaFin "
			+ "AND vis.areaRecinto.recinto.recCod = :recinto")
	Page<Visita> buscarVisitantesSinSalida2(Pageable pageable, @Param("fechaInicio") LocalDateTime fechaInicio,
			@Param("fechaFin") LocalDateTime fechaFin, @Param("recinto") String recinto);

	// busca Visitas que salieron
	@Query("FROM Visita vis WHERE vis.visSalida != null AND vis.visEstado = 'ACT' "
			+ "AND vis.visIngreso >= :fechaInicio AND vis.visIngreso <= :fechaFin "
			+ "AND vis.areaRecinto.recinto.recCod = :recinto")
	Page<Visita> buscarVisitantesConSalida2(Pageable pageable, @Param("fechaInicio") LocalDateTime fechaInicio,
			@Param("fechaFin") LocalDateTime fechaFin, @Param("recinto") String recinto);

	@Query("FROM Visita vis WHERE vis.visSalida != null AND vis.visitante.vteCi = :ci "
			+ "AND vis.visIngreso >= :fechaInicio AND vis.visIngreso <= :fechaFin "
			+ "AND vis.areaRecinto.recinto.recCod = :recinto "
			+ "AND vis.visEstado = 'ACT' AND vis.visitante.vteEstado = 'ACT'")
	Page<Visita> buscarXCiConSalida(Pageable pageable, @Param("ci") String ci,
			@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin,
			@Param("recinto") String recinto);

	@Query("FROM Visita vis WHERE vis.visSalida != null AND vis.visitante.vteCi = :ci "
			+ "AND vis.visIngreso >= :fechaInicio AND vis.visIngreso <= :fechaFin "
			+ "AND vis.areaRecinto.recinto.recCod = :recinto "
			+ "AND vis.visEstado = 'ACT' AND vis.visitante.vteEstado = 'ACT' "
			+ "AND vis.areaRecinto.areaCod = :areaRecinto")
	Page<Visita> buscarXCiConSalidaAreaRecinto(Pageable pageable, @Param("ci") String ci,
			@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin,
			@Param("recinto") String recinto, @Param("areaRecinto") Long areaRecinto);

	@Query("FROM Visita vis WHERE vis.visSalida = null AND vis.visitante.vteCi = :ci "
			+ "AND vis.visIngreso >= :fechaInicio AND vis.visIngreso <= :fechaFin "
			+ "AND vis.areaRecinto.recinto.recCod = :recinto "
			+ "AND vis.visEstado = 'ACT' AND vis.visitante.vteEstado = 'ACT'")
	Page<Visita> buscarXCiSinSalida(Pageable pageable, @Param("ci") String ci,
			@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin,
			@Param("recinto") String recinto);

	@Query("FROM Visita vis WHERE vis.visSalida = null AND vis.visitante.vteCi = :ci "
			+ "AND vis.visIngreso >= :fechaInicio AND vis.visIngreso <= :fechaFin "
			+ "AND vis.areaRecinto.recinto.recCod = :recinto "
			+ "AND vis.visEstado = 'ACT' AND vis.visitante.vteEstado = 'ACT' "
			+ "AND vis.areaRecinto.areaCod = :areaRecinto")
	Page<Visita> buscarXCiSinSalidaAreaRecinto(Pageable pageable, @Param("ci") String ci,
			@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin,
			@Param("recinto") String recinto, @Param("areaRecinto") Long areaRecinto);

}
