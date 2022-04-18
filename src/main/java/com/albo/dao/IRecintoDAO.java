package com.albo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.albo.model.Recinto;

@Repository
public interface IRecintoDAO extends JpaRepository<Recinto, String> {

	// busca recinto por codigo de aduana
	@Query("FROM Recinto rec where rec.aduana.aduCod = :aduCod")
	List<Recinto> buscarRecintoXAduana(@Param("aduCod") Integer aduCod);

}
