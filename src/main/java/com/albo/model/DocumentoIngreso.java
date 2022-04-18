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

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "documento_ingreso", schema = "PUBLIC")
public class DocumentoIngreso implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Inicio Campos BD **/
	@Id
	@Column(name = "DOI_COD", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long doiCod;

	@Column(name = "DOI_IMAGEN", length = 300)
	private String doiImagen;

	@Column(name = "DOI_DOCUMENTO", length = 500)
	private String doiDocumento;

	@ManyToOne
	@JoinColumn(name = "TDO_COD", referencedColumnName = "TDO_COD")
	private TipoDocumento tipoDocumento;

	@ManyToOne
//	@JsonManagedReference
	@JsonBackReference
	@JoinColumn(name = "VIS_COD", referencedColumnName = "VIS_COD")
	private Visita visita;

//	@JsonIgnore
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "documentoIngreso")
//	private List<Visita> visitas = new ArrayList<Visita>();

	/** Fin Campos BD **/

	/** Inicio Getters y setters **/

	public Long getDoiCod() {
		return doiCod;
	}

	public void setDoiCod(Long doiCod) {
		this.doiCod = doiCod;
	}

	public String getDoiImagen() {
		return doiImagen;
	}

	public void setDoiImagen(String doiImagen) {
		this.doiImagen = doiImagen;
	}

	public String getDoiDocumento() {
		return doiDocumento;
	}

	public void setDoiDocumento(String doiDocumento) {
		this.doiDocumento = doiDocumento;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Visita getVisita() {
		return visita;
	}

	public void setVisita(Visita visita) {
		this.visita = visita;
	}

	/** Fin Getters y setters **/

}
