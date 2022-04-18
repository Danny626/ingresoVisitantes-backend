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
import javax.persistence.Version;

@Entity
@Table(name = "parametro_recinto", schema = "PUBLIC")
public class ParametroRecinto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** inicio campos bd **/
	@Id
	@Column(name = "PAR_COD", nullable = false, length = 5)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long parCod;

	/* valor que tendra el parametro por recinto */
	@Column(name = "PAR_VALOR")
	private String parValor;

	/* parametro al q se encuentra relacionado */
	@ManyToOne
	@JoinColumn(name = "P_COD", referencedColumnName = "P_COD")
	private Parametro parametro;

	/* recinto al que pertenece el parámetro */
	@ManyToOne
	@JoinColumn(name = "REC_COD", nullable = true, referencedColumnName = "REC_COD")
	private Recinto recinto;

	@Version
	private Long version;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	/** fin campos bd **/

	public Long getParCod() {
		return parCod;
	}

	public void setParCod(Long parCod) {
		this.parCod = parCod;
	}

	public String getParValor() {
		return parValor;
	}

	public void setParValor(String parValor) {
		this.parValor = parValor;
	}

	public Parametro getParametro() {
		return parametro;
	}

	public void setParametro(Parametro parametro) {
		this.parametro = parametro;
	}

	public Recinto getRecinto() {
		return recinto;
	}

	public void setRecinto(Recinto recinto) {
		this.recinto = recinto;
	}

	/** fin campos bd **/

}
