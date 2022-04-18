package com.albo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.albo.model.TipoDocumento;

@Repository
public interface ITipoDocumentoDAO extends JpaRepository<TipoDocumento, Integer> {

}
