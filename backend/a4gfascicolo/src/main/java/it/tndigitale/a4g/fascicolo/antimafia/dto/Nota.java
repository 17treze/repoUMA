/**
 * 
 */
package it.tndigitale.a4g.fascicolo.antimafia.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author S.DeLuca
 *
 */
public class Nota implements Serializable {

	private static final long serialVersionUID = 5361534521003204466L;
	private Long id;
	private TipoNotaEnum tipoNota;
	private String chiaveEsterna;
	private String nota;
	private String utente;
	private Date dataInserimento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoNotaEnum getTipoNota() {
		return tipoNota;
	}

	public void setTipoNota(TipoNotaEnum tipoNota) {
		this.tipoNota = tipoNota;
	}

	public String getChiaveEsterna() {
		return chiaveEsterna;
	}

	public void setChiaveEsterna(String chiaveEsterna) {
		this.chiaveEsterna = chiaveEsterna;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public Date getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

}
