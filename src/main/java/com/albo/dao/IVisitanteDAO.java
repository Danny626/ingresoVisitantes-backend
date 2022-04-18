package com.albo.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.albo.model.Visitante;

@Repository
public interface IVisitanteDAO extends JpaRepository<Visitante, String> {

	@Query("FROM Visitante where vteCorreo = :correo")
	Visitante verificarCorreo(@Param("correo") String correo);

	@Query("FROM Visitante vte WHERE vte.vteNombre LIKE :vteNombre")
	Page<Visitante> buscarXNombre(Pageable pageable, @Param("vteNombre") String vteNombre);
	
	@Query("FROM Visitante vte WHERE vte.vteCi LIKE :ci")
	List<Visitante> buscarXCi(@Param("ci") String ci);

}
