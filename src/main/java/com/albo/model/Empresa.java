package com.albo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "empresa", schema = "PUBLIC")
public class Empresa implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Inicio Campos BD **/
	@Id
	@Column(name = "EMP_COD", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer empCod;

	@Column(name = "EMP_NOMBRE", length = 100)
	private String empNombre;

	@Column(name = "EMP_OBS", length = 200)
	private String empObs;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "empresa")
	private List<Visitante> visitantes = new ArrayList<Visitante>();

	public Integer getEmpCod() {
		return empCod;
	}

	public void setEmpCod(Integer empCod) {
		this.empCod = empCod;
	}

	public String getEmpNombre() {
		return empNombre;
	}

	public void setEmpNombre(String empNombre) {
		this.empNombre = empNombre;
	}

	public String getEmpObs() {
		return empObs;
	}

	public void setEmpObs(String empObs) {
		this.empObs = empObs;
	}

	public List<Visitante> getVisitantes() {
		return visitantes;
	}

	public void setVisitantes(List<Visitante> visitantes) {
		this.visitantes = visitantes;
	}

}
