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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "area_recinto", schema = "PUBLIC")
public class AreaRecinto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Inicio Campos BD **/
	@Id
	@Column(name = "AREA_COD", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long areaCod;

	@Column(name = "AREA_NOMBRE", length = 100)
	private String areaNombre;

	@Column(name = "AREA_DESCRIPCION", length = 300)
	private String areaDescripcion;

	@Column(name = "AREA_ESTADO", length = 10)
	private String areaEstado;

	/* recinto al que pertenece el Área */
	@ManyToOne
	@JoinColumn(name = "REC_COD", nullable = false, referencedColumnName = "REC_COD")
	private Recinto recinto;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "areaRecinto")
	private List<Visita> visitas = new ArrayList<Visita>();

	/** Fin Campos BD **/

	public Long getAreaCod() {
		return areaCod;
	}

	public void setAreaCod(Long areaCod) {
		this.areaCod = areaCod;
	}

	public String getAreaNombre() {
		return areaNombre;
	}

	public void setAreaNombre(String areaNombre) {
		this.areaNombre = areaNombre;
	}

	public String getAreaDescripcion() {
		return areaDescripcion;
	}

	public void setAreaDescripcion(String areaDescripcion) {
		this.areaDescripcion = areaDescripcion;
	}

	public String getAreaEstado() {
		return areaEstado;
	}

	public void setAreaEstado(String areaEstado) {
		this.areaEstado = areaEstado;
	}

	public Recinto getRecinto() {
		return recinto;
	}

	public void setRecinto(Recinto recinto) {
		this.recinto = recinto;
	}

	public List<Visita> getVisitas() {
		return visitas;
	}

	public void setVisitas(List<Visita> visitas) {
		this.visitas = visitas;
	}

}
