package com.albo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "recinto", schema = "PUBLIC")
public class Recinto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** inicio campos bd **/
	@Id
	@Column(name = "REC_COD", nullable = false, length = 5)
	private String recCod;

	@Column(name = "REC_NOMBRE", length = 50)
	private String recNombre;

	@Column(name = "REC_NOMBREA", length = 50)
	private String recNombrea;

	@Column(name = "REC_ESTADO", length = 5)
	private String recEstado;

	@Column(name = "REC_TIPO", length = 10)
	private String recTipo;

	@ManyToOne
	@JoinColumn(name = "ADU_COD", referencedColumnName = "ADU_COD")
	private Aduana aduana;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "recinto")
	private List<ParametroRecinto> parametroRecintos = new ArrayList<ParametroRecinto>();

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "recinto")
	private List<Horario> horarios = new ArrayList<Horario>();

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "recinto")
	private List<AreaRecinto> areaRecintos = new ArrayList<AreaRecinto>();

	/** fin campos bd **/

	/** inicio getters y setters **/

	public String getRecCod() {
		return recCod;
	}

	public void setRecCod(String recCod) {
		this.recCod = recCod;
	}

	public String getRecNombre() {
		return recNombre;
	}

	public void setRecNombre(String recNombre) {
		this.recNombre = recNombre;
	}

	public String getRecNombrea() {
		return recNombrea;
	}

	public void setRecNombrea(String recNombrea) {
		this.recNombrea = recNombrea;
	}

	public String getRecEstado() {
		return recEstado;
	}

	public void setRecEstado(String recEstado) {
		this.recEstado = recEstado;
	}

	public String getRecTipo() {
		return recTipo;
	}

	public void setRecTipo(String recTipo) {
		this.recTipo = recTipo;
	}

	public Aduana getAduana() {
		return aduana;
	}

	public void setAduana(Aduana aduana) {
		this.aduana = aduana;
	}

	public List<ParametroRecinto> getParametroRecintos() {
		return parametroRecintos;
	}

	public void setParametroRecintos(List<ParametroRecinto> parametroRecintos) {
		this.parametroRecintos = parametroRecintos;
	}

	public List<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}

	public List<AreaRecinto> getAreaRecintos() {
		return areaRecintos;
	}

	public void setAreaRecintos(List<AreaRecinto> areaRecintos) {
		this.areaRecintos = areaRecintos;
	}

	/** fin getters y setters **/

}
