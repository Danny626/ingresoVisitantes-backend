package com.albo.model;

import java.io.Serializable;
import java.time.LocalDateTime;
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

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
@Table(name = "visita", schema = "PUBLIC")
public class Visita implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Inicio Campos BD **/
	@Id
	@Column(name = "VIS_COD", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long visCod;

	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "VIS_INGRESO")
	private LocalDateTime visIngreso;

	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "VIS_SALIDA")
	private LocalDateTime visSalida;

	@Column(name = "VIS_OBS", length = 400)
	private String visObs;

	@Column(name = "VIS_ESTADO", length = 10)
	private String visEstado;

	/* visitante al que pertenece la visita */
	@ManyToOne
	@JoinColumn(name = "VTE_CI", nullable = false, referencedColumnName = "VTE_CI")
	private Visitante visitante;

	/* visitante al que pertenece la visita */
	@ManyToOne
	@JoinColumn(name = "AREA_COD", nullable = false, referencedColumnName = "AREA_COD")
	private AreaRecinto areaRecinto;

	/* Documento con el cual el visitante está ingresando */
//	@ManyToOne
//	@JoinColumn(name = "DOI_COD", nullable = false, referencedColumnName = "DOI_COD")
//	private DocumentoIngreso documentoIngreso;

	@ManyToOne
	@JoinColumn(name = "MVO_COD", nullable = false, referencedColumnName = "MVO_COD")
	private MotivoIngreso motivoIngreso;

	/* Documentos con los cuales el visitante está ingresando */
//	@JsonIgnore
//	@JsonBackReference
	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "visita")
	private List<DocumentoIngreso> documentosIngreso = new ArrayList<DocumentoIngreso>();

//	/* horario al que pertenece la visita */
//	@ManyToOne
//	@JoinColumn(name = "HOR_COD", nullable = false, referencedColumnName = "HOR_COD")
//	private Horario horario;

	public Long getVisCod() {
		return visCod;
	}

	public void setVisCod(Long visCod) {
		this.visCod = visCod;
	}

	public LocalDateTime getVisIngreso() {
		return visIngreso;
	}

	public void setVisIngreso(LocalDateTime visIngreso) {
		this.visIngreso = visIngreso;
	}

	public LocalDateTime getVisSalida() {
		return visSalida;
	}

	public void setVisSalida(LocalDateTime visSalida) {
		this.visSalida = visSalida;
	}

	public String getVisObs() {
		return visObs;
	}

	public void setVisObs(String visObs) {
		this.visObs = visObs;
	}

	public String getVisEstado() {
		return visEstado;
	}

	public void setVisEstado(String visEstado) {
		this.visEstado = visEstado;
	}

	public Visitante getVisitante() {
		return visitante;
	}

	public void setVisitante(Visitante visitante) {
		this.visitante = visitante;
	}

	public AreaRecinto getAreaRecinto() {
		return areaRecinto;
	}

	public void setAreaRecinto(AreaRecinto areaRecinto) {
		this.areaRecinto = areaRecinto;
	}

	public MotivoIngreso getMotivoIngreso() {
		return motivoIngreso;
	}

	public void setMotivoIngreso(MotivoIngreso motivoIngreso) {
		this.motivoIngreso = motivoIngreso;
	}

	public List<DocumentoIngreso> getDocumentosIngreso() {
		return documentosIngreso;
	}

	@JsonProperty(value = "documentosIngreso", access = JsonProperty.Access.WRITE_ONLY)
	public void setDocumentosIngreso(List<DocumentoIngreso> documentosIngreso) {
		this.documentosIngreso = documentosIngreso;
	}

}
