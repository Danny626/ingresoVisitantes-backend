package com.albo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.albo.model.Aduana;

@Repository
public interface IAduanaDAO extends JpaRepository<Aduana, Integer> {

	// busca codigo de aduana por recinto
	@Query("SELECT adu FROM Aduana adu LEFT JOIN adu.recintos rec WHERE rec.recCod = :recCod")
	List<Aduana> buscarAduanaXRecinto(@Param("recCod") String recCod);

}
