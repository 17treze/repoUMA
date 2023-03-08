package it.tndigitale.a4g.fascicolo.antimafia.repository.model;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the A4GT_NOTA database table.
 * 
 */
@Entity
@Table(name = "A4GT_NOTA")
public class A4gtNota extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = 626228826841242073L;

	@Lob
	@Column(name = "NOTA")
	private String nota;

	@Column(name = "TIPO")
	private String tipo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_INSERIMENTO")
	private Date dataInserimento;

	@Column(name = "CHIAVE_ESTERNA")
	private String chiaveEsterna;
	
	@Column(name = "UTENTE")
	private String utente;
	
	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getChiaveEsterna() {
		return chiaveEsterna;
	}

	public void setChiaveEsterna(String chiaveEsterna) {
		this.chiaveEsterna = chiaveEsterna;
	}
	
	public Date getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

}