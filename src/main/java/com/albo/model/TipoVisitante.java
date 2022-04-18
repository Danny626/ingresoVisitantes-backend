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
@Table(name = "tipo_visitante", schema = "PUBLIC")
public class TipoVisitante implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Inicio Campos BD **/
	@Id
	@Column(name = "TVI_COD", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tviCod;

	@Column(name = "TVI_NOMBRE", length = 100)
	private String tviNombre;

	@Column(name = "TVI_DESCRIPCION", length = 300)
	private String tviDescripcion;

	@Column(name = "TVI_ESTADO", length = 10)
	private String horEstado;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tipoVisitante")
	private List<Horario> horarios = new ArrayList<Horario>();

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tipoVisitante")
	private List<Visitante> visitantes = new ArrayList<Visitante>();

	public Long getTviCod() {
		return tviCod;
	}

	public void setTviCod(Long tviCod) {
		this.tviCod = tviCod;
	}

	public String getTviNombre() {
		return tviNombre;
	}

	public void setTviNombre(String tviNombre) {
		this.tviNombre = tviNombre;
	}

	public String getTviDescripcion() {
		return tviDescripcion;
	}

	public void setTviDescripcion(String tviDescripcion) {
		this.tviDescripcion = tviDescripcion;
	}

	public String getHorEstado() {
		return horEstado;
	}

	public void setHorEstado(String horEstado) {
		this.horEstado = horEstado;
	}

	public List<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}

	public List<Visitante> getVisitantes() {
		return visitantes;
	}

	public void setVisitantes(List<Visitante> visitantes) {
		this.visitantes = visitantes;
	}

}
