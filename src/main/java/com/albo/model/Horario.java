package com.albo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "horario", schema = "PUBLIC")
public class Horario implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Inicio Campos BD **/
	@Id
	@Column(name = "HOR_COD", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long horCod;

	@Column(name = "HOR_NOMBRE", length = 100)
	private String horNombre;

	@Column(name = "HOR_DESCRIPCION", length = 200)
	private String horDescripcion;

	@Column(name = "HOR_DIAS", length = 300)
	private String horDias;

	@Column(name = "HOR_HORA_ENTRADA", nullable = false)
	private Integer horHoraEntrada;

	@Column(name = "HOR_MIN_ENTRADA", nullable = false)
	private Integer horMinEntrada;

	@Column(name = "HOR_HORA_SALIDA", nullable = false)
	private Integer horHoraSalida;

	@Column(name = "HOR_MIN_SALIDA", nullable = false)
	private Integer horMinSalida;

	@Column(name = "HOR_ESTADO", length = 10)
	private String horEstado;

	/* recinto al que pertenece el Área */
	@ManyToOne
	@JoinColumn(name = "REC_COD", nullable = false, referencedColumnName = "REC_COD")
	private Recinto recinto;

	/* tipo de visitante al que pertenece el horario */
	@ManyToOne
	@JoinColumn(name = "TVI_COD", nullable = false, referencedColumnName = "TVI_COD")
	private TipoVisitante tipoVisitante;

//	@JsonIgnore
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "horario")
//	private List<Visita> visitas = new ArrayList<Visita>();

	public Long getHorCod() {
		return horCod;
	}

	public void setHorCod(Long horCod) {
		this.horCod = horCod;
	}

	public String getHorNombre() {
		return horNombre;
	}

	public void setHorNombre(String horNombre) {
		this.horNombre = horNombre;
	}

	public String getHorDescripcion() {
		return horDescripcion;
	}

	public void setHorDescripcion(String horDescripcion) {
		this.horDescripcion = horDescripcion;
	}

	public Integer getHorHoraEntrada() {
		return horHoraEntrada;
	}

	public void setHorHoraEntrada(Integer horHoraEntrada) {
		this.horHoraEntrada = horHoraEntrada;
	}

	public Integer getHorMinEntrada() {
		return horMinEntrada;
	}

	public void setHorMinEntrada(Integer horMinEntrada) {
		this.horMinEntrada = horMinEntrada;
	}

	public Integer getHorHoraSalida() {
		return horHoraSalida;
	}

	public void setHorHoraSalida(Integer horHoraSalida) {
		this.horHoraSalida = horHoraSalida;
	}

	public Integer getHorMinSalida() {
		return horMinSalida;
	}

	public void setHorMinSalida(Integer horMinSalida) {
		this.horMinSalida = horMinSalida;
	}

	public String getHorEstado() {
		return horEstado;
	}

	public void setHorEstado(String horEstado) {
		this.horEstado = horEstado;
	}

	public Recinto getRecinto() {
		return recinto;
	}

	public void setRecinto(Recinto recinto) {
		this.recinto = recinto;
	}

	public TipoVisitante getTipoVisitante() {
		return tipoVisitante;
	}

	public void setTipoVisitante(TipoVisitante tipoVisitante) {
		this.tipoVisitante = tipoVisitante;
	}

	public String getHorDias() {
		return horDias;
	}

	public void setHorDias(String horDias) {
		this.horDias = horDias;
	}

}
