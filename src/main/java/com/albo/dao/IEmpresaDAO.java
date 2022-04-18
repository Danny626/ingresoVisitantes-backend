package com.albo.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.albo.model.Empresa;

@Repository
public interface IEmpresaDAO extends JpaRepository<Empresa, Integer> {

	@Query("FROM Empresa emp WHERE emp.empNombre LIKE :empNombre")
	Page<Empresa> buscarXNombre(Pageable pageable, @Param("empNombre") String empNombre);
}
