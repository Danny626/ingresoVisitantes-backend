package com.albo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.albo.model.Rol;

@Repository
public interface IRolDAO extends JpaRepository<Rol, Integer> {

}
