package com.albo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.albo.model.AreaRecinto;

@Repository
public interface IAreaRecintoDAO extends JpaRepository<AreaRecinto, Integer> {

	// busca por recinto
	@Query("FROM AreaRecinto WHERE recinto.recCod = :recCod")
	List<AreaRecinto> buscarXRecinto(@Param("recCod") String recCod);

}
