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
@Table(name = "tipo_documento", schema = "PUBLIC")
public class TipoDocumento implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Inicio Campos BD **/
	@Id
	@Column(name = "TDO_COD", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tdoCod;

	@Column(name = "TDO_NOMBRE", length = 50)
	private String tdoNombre;

	@Column(name = "TDO_DESCRIPCION", length = 100)
	private String tdoDescripcion;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tipoDocumento")
	private List<DocumentoIngreso> documentosIngreso = new ArrayList<DocumentoIngreso>();

	/** Fin Campos BD **/

	/** Inicio Getters y setters **/

	public Integer getTdoCod() {
		return tdoCod;
	}

	public void setTdoCod(Integer tdoCod) {
		this.tdoCod = tdoCod;
	}

	public String getTdoNombre() {
		return tdoNombre;
	}

	public void setTdoNombre(String tdoNombre) {
		this.tdoNombre = tdoNombre;
	}

	public String getTdoDescripcion() {
		return tdoDescripcion;
	}

	public void setTdoDescripcion(String tdoDescripcion) {
		this.tdoDescripcion = tdoDescripcion;
	}

	public List<DocumentoIngreso> getDocumentosIngreso() {
		return documentosIngreso;
	}

	public void setDocumentosIngreso(List<DocumentoIngreso> documentosIngreso) {
		this.documentosIngreso = documentosIngreso;
	}

	/** Fin Getters y setters **/

}
