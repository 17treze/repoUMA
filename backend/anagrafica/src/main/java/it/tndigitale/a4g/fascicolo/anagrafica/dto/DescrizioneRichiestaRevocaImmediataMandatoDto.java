package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;
import java.time.LocalDate;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.StatoRevocaImmediata;

public class DescrizioneRichiestaRevocaImmediataMandatoDto  implements Serializable {

	private static final long serialVersionUID = 801504784777757253L;

	private String cuaa;

	private String sedeSportelloCaa;
	
	private String denominazioneSportelloCaa;
	
	private String sedeAmministrativaCaa;
	
	private String idProtocollo;
	
	private String denominazioneAzienda;
	
	private String denominazioneSedeAmministrativaCaa;
	
	private String causaRichiesta;
	
	private LocalDate dataSottoscrizione;
	
	private String codiceFiscaleRappresentante;
	
	private String motivazioneRifiuto;
	
	private LocalDate dataValutazione;
	
	private StatoRevocaImmediata esito;
	

	public String getCodiceFiscaleRappresentante() {
		return codiceFiscaleRappresentante;
	}

	public void setCodiceFiscaleRappresentante(String codiceFiscaleRappresentante) {
		this.codiceFiscaleRappresentante = codiceFiscaleRappresentante;
	}

	public String getMotivazioneRifiuto() {
		return motivazioneRifiuto;
	}

	public void setMotivazioneRifiuto(String motivazioneRifiuto) {
		this.motivazioneRifiuto = motivazioneRifiuto;
	}

	public LocalDate getDataValutazione() {
		return dataValutazione;
	}

	public void setDataValutazione(LocalDate dataValutazione) {
		this.dataValutazione = dataValutazione;
	}

	public StatoRevocaImmediata getEsito() {
		return esito;
	}

	public void setEsito(StatoRevocaImmediata esito) {
		this.esito = esito;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getSedeSportelloCaa() {
		return sedeSportelloCaa;
	}

	public void setSedeSportelloCaa(String sedeSportelloCaa) {
		this.sedeSportelloCaa = sedeSportelloCaa;
	}

	public String getSedeAmministrativaCaa() {
		return sedeAmministrativaCaa;
	}

	public void setSedeAmministrativaCaa(String sedeAmministrativaCaa) {
		this.sedeAmministrativaCaa = sedeAmministrativaCaa;
	}

	public String getIdProtocollo() {
		return idProtocollo;
	}

	public void setIdProtocollo(String idProtocollo) {
		this.idProtocollo = idProtocollo;
	}

	public String getDenominazioneAzienda() {
		return denominazioneAzienda;
	}

	public void setDenominazioneAzienda(String denominazione) {
		this.denominazioneAzienda = denominazione;
	}

	public String getDenominazioneSportelloCaa() {
		return denominazioneSportelloCaa;
	}

	public void setDenominazioneSportelloCaa(String denominazioneSportelloCaa) {
		this.denominazioneSportelloCaa = denominazioneSportelloCaa;
	}

	public String getDenominazioneSedeAmministrativaCaa() {
		return denominazioneSedeAmministrativaCaa;
	}

	public void setDenominazioneSedeAmministrativaCaa(String denominazioneSedeAmministrativaCaa) {
		this.denominazioneSedeAmministrativaCaa = denominazioneSedeAmministrativaCaa;
	}

	public String getCausaRichiesta() {
		return causaRichiesta;
	}

	public void setCausaRichiesta(String causaRichiesta) {
		this.causaRichiesta = causaRichiesta;
	}

	public LocalDate getDataSottoscrizione() {
		return dataSottoscrizione;
	}

	public void setDataSottoscrizione(LocalDate dataSottoscrizione) {
		this.dataSottoscrizione = dataSottoscrizione;
	}
}
