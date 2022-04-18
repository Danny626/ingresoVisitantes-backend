package com.albo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.albo.model.MotivoIngreso;

@Repository
public interface IMotivoIngresoDAO extends JpaRepository<MotivoIngreso, Integer> {

}
