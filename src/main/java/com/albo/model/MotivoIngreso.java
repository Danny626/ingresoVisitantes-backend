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
@Table(name = "motivo_ingreso", schema = "PUBLIC")
public class MotivoIngreso implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Inicio Campos BD **/
	@Id
	@Column(name = "MVO_COD", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer mvoCod;

	@Column(name = "MVO_NOMBRE", length = 50)
	private String mvoNombre;

	@Column(name = "MVO_DESCRIPCION", length = 100)
	private String mvoDescripcion;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motivoIngreso")
	private List<Visita> visitas = new ArrayList<Visita>();

	/** Fin Campos BD **/

	/** Inicio Getters y setters **/

	public Integer getMvoCod() {
		return mvoCod;
	}

	public void setMvoCod(Integer mvoCod) {
		this.mvoCod = mvoCod;
	}

	public String getMvoNombre() {
		return mvoNombre;
	}

	public void setMvoNombre(String mvoNombre) {
		this.mvoNombre = mvoNombre;
	}

	public String getMvoDescripcion() {
		return mvoDescripcion;
	}

	public void setMvoDescripcion(String mvoDescripcion) {
		this.mvoDescripcion = mvoDescripcion;
	}

	public List<Visita> getVisitas() {
		return visitas;
	}

	public void setVisitas(List<Visita> visitas) {
		this.visitas = visitas;
	}

	/** Fin Getters y setters **/

}
