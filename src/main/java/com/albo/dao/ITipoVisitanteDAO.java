package com.albo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.albo.model.TipoVisitante;

@Repository
public interface ITipoVisitanteDAO extends JpaRepository<TipoVisitante, Integer> {

}
