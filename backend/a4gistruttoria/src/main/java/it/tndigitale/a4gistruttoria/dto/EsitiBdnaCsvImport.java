/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author a.pasca
 *
 */
public class EsitiBdnaCsvImport {
	private Date dataInserimento;
	private String codiceFiscale;
	private String numProtocollo;
	private String statoRichiesta;
	private BigDecimal valoreAppalto;
	private String tipoRichiesta;
	private String tipoMotivazioneRichiesta;
	private String username;
	private String noteElaborazione;

	public EsitiBdnaCsvImport(String linea) {
		if(linea == null || linea.trim().equals("")) {
			return;
		}
		String[] splitted = linea.split(";");
		try {
			this.setDataInserimento(new SimpleDateFormat("dd/MM/yyyy").parse(splitted[0]));
		} catch (Exception e) {
			return;
		}
		try {
			this.setCodiceFiscale(splitted[1]);
		} catch (Exception e) {
			return;
		}
		try {
			this.setNumProtocollo(splitted[2]);
		} catch (Exception e) {
			return;
		}
		try {
			this.setStatoRichiesta(splitted[3]);
		} catch (Exception e) {
			return;
		}
		try {
			this.setValoreAppalto(new BigDecimal(splitted[4].replaceAll(",",".")));
		} catch (Exception e) {
			return;
		}
		try {
			this.setTipoRichiesta(splitted[5]);
		} catch (Exception e) {
			return;
		}
		try {
			this.setTipoMotivazioneRichiesta(splitted[6]);
		} catch (Exception e) {
			return;
		}
		try {
			this.setUsername(splitted[7]);
		} catch (Exception e) {
			return;
		}
		try {
			this.setNoteElaborazione(splitted[8]);
		} catch (Exception e) {
			return;
		}
	}
	
	public Date getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getNumProtocollo() {
		return numProtocollo;
	}

	public void setNumProtocollo(String numProtocollo) {
		this.numProtocollo = numProtocollo;
	}
	
	public void setStatoRichiesta(String statoRichiesta) {
		this.statoRichiesta = statoRichiesta;
	}
	
	public String getStatoRichiesta() {
		return statoRichiesta;
	}

	public BigDecimal getValoreAppalto() {
		return valoreAppalto;
	}

	public void setValoreAppalto(BigDecimal valoreAppalto) {
		this.valoreAppalto = valoreAppalto;
	}

	public String getTipoRichiesta() {
		return tipoRichiesta;
	}

	public void setTipoRichiesta(String tipoRichiesta) {
		this.tipoRichiesta = tipoRichiesta;
	}

	public String getTipoMotivazioneRichiesta() {
		return tipoMotivazioneRichiesta;
	}

	public void setTipoMotivazioneRichiesta(String tipoMotivazioneRichiesta) {
		this.tipoMotivazioneRichiesta = tipoMotivazioneRichiesta;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNoteElaborazione() {
		return noteElaborazione;
	}

	public void setNoteElaborazione(String noteElaborazione) {
		this.noteElaborazione = noteElaborazione;
	}

	public Boolean validate() {
		if (this.dataInserimento == null) {
			return Boolean.FALSE;
		}
		if (this.codiceFiscale == null || this.codiceFiscale.isEmpty()) {
			return Boolean.FALSE;
		}
		if (this.numProtocollo == null) {
			return Boolean.FALSE;
		}
		if (this.statoRichiesta == null || this.statoRichiesta.isEmpty()) {
			return Boolean.FALSE;
		}
		if (this.valoreAppalto == null || this.statoRichiesta.isEmpty()) {
			return Boolean.FALSE;
		}
		if (this.tipoRichiesta == null || this.tipoRichiesta.isEmpty()) {
			return Boolean.FALSE;
		}
		if (this.tipoMotivazioneRichiesta == null || this.tipoMotivazioneRichiesta.isEmpty()) {
			return Boolean.FALSE;
		}
		if (this.username == null || this.username.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}
