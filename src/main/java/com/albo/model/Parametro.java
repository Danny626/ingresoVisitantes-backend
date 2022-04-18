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
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "parametro", schema = "PUBLIC")
public class Parametro implements Serializable {

	private static final long serialVersionUID = 1L;

	/** inicio campos bd **/

	/* correlativo numerico */
	@Id
	@Column(name = "P_COD", nullable = false, length = 5)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pCod;

	/* tipo de parametro */
	@Column(name = "P_TIPO", nullable = false)
	private String pTipo;

	/* nombre del parametro */
	@Column(name = "P_NOMBRE", nullable = false)
	private String pNombre;

	/* descripcion del parametro */
	@Column(name = "P_DESCRIPCION", nullable = false)
	private String pDescripcion;

	/* estado del parametro */
	@Column(name = "P_ESTADO", nullable = false)
	private String pEstado;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parametro")
	private List<ParametroRecinto> parametroRecintos = new ArrayList<ParametroRecinto>();

	@Version
	private Long version;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getpCod() {
		return pCod;
	}

	public void setpCod(Long pCod) {
		this.pCod = pCod;
	}

	public String getpTipo() {
		return pTipo;
	}

	public void setpTipo(String pTipo) {
		this.pTipo = pTipo;
	}

	public String getpNombre() {
		return pNombre;
	}

	public void setpNombre(String pNombre) {
		this.pNombre = pNombre;
	}

	public String getpDescripcion() {
		return pDescripcion;
	}

	public void setpDescripcion(String pDescripcion) {
		this.pDescripcion = pDescripcion;
	}

	public String getpEstado() {
		return pEstado;
	}

	public void setpEstado(String pEstado) {
		this.pEstado = pEstado;
	}

	public List<ParametroRecinto> getParametroRecintos() {
		return parametroRecintos;
	}

	public void setParametroRecintos(List<ParametroRecinto> parametroRecintos) {
		this.parametroRecintos = parametroRecintos;
	}

	/** fin campos bd **/

}
