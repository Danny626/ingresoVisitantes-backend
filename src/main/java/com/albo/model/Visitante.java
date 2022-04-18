package com.albo.model;

import java.io.Serializable;
import java.time.LocalDateTime;
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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

@Entity
@Table(name = "visitante", schema = "PUBLIC")
public class Visitante implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Inicio Campos BD **/
	@Id
	@Column(name = "VTE_CI", nullable = false)
	private String vteCi;

	@Column(name = "VTE_CORREO", length = 100)
	private String vteCorreo;

	@Column(name = "VTE_IMAGEN", length = 300)
	private String vteImagen;

	@Column(name = "VTE_NOMBRE", length = 200)
	private String vteNombre;

	@Column(name = "VTE_APELLIDOS", length = 200)
	private String vteApellidos;

	@Column(name = "VTE_TELEFONO", length = 100)
	private String vteTelefono;

	@Column(name = "VTE_DIRECCION", length = 300)
	private String vteDireccion;

	@Column(name = "VTE_ESTADO", length = 10)
	private String vteEstado;

	@Column(name = "VTE_LLAVE", length = 300)
	private String vteLlave;

	@Column(name = "VTE_FECHA")
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime vteFecha;

	/* tipo de visitante al que pertenece el visitante */
	@ManyToOne
	@JoinColumn(name = "TVI_COD", nullable = false, referencedColumnName = "TVI_COD")
	private TipoVisitante tipoVisitante;

	/* empresa al que pertenece el visitante */
	@ManyToOne
	@JoinColumn(name = "EMP_COD", nullable = false, referencedColumnName = "EMP_COD")
	private Empresa empresa;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "visitante")
	private List<Visita> visitas = new ArrayList<Visita>();

	public String getVteCi() {
		return vteCi;
	}

	public void setVteCi(String vteCi) {
		this.vteCi = vteCi;
	}

	public String getVteCorreo() {
		return vteCorreo;
	}

	public void setVteCorreo(String vteCorreo) {
		this.vteCorreo = vteCorreo;
	}

	public String getVteImagen() {
		return vteImagen;
	}

	public void setVteImagen(String vteImagen) {
		this.vteImagen = vteImagen;
	}

	public String getVteNombre() {
		return vteNombre;
	}

	public void setVteNombre(String vteNombre) {
		this.vteNombre = vteNombre;
	}

	public String getVteApellidos() {
		return vteApellidos;
	}

	public void setVteApellidos(String vteApellidos) {
		this.vteApellidos = vteApellidos;
	}

	public String getVteTelefono() {
		return vteTelefono;
	}

	public void setVteTelefono(String vteTelefono) {
		this.vteTelefono = vteTelefono;
	}

	public String getVteDireccion() {
		return vteDireccion;
	}

	public void setVteDireccion(String vteDireccion) {
		this.vteDireccion = vteDireccion;
	}

	public String getVteEstado() {
		return vteEstado;
	}

	public void setVteEstado(String vteEstado) {
		this.vteEstado = vteEstado;
	}

	public TipoVisitante getTipoVisitante() {
		return tipoVisitante;
	}

	public void setTipoVisitante(TipoVisitante tipoVisitante) {
		this.tipoVisitante = tipoVisitante;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<Visita> getVisitas() {
		return visitas;
	}

	public void setVisitas(List<Visita> visitas) {
		this.visitas = visitas;
	}

	public String getVteLlave() {
		return vteLlave;
	}

	public void setVteLlave(String vteLlave) {
		this.vteLlave = vteLlave;
	}

	public LocalDateTime getVteFecha() {
		return vteFecha;
	}

	public void setVteFecha(LocalDateTime vteFecha) {
		this.vteFecha = vteFecha;
	}

}
