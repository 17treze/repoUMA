package it.tndigitale.a4g.fascicolo.mediator.dto;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;

public class MigrazioneFascicoloDto {

	private String cuaa;
	private String codiceFiscaleRappresentante;
	private Long identificativoSportello;
	private ByteArrayResource contratto;
	private List<ByteArrayResource> allegati;
	private Boolean migraModoPagamento;
	private Boolean migraMacchinari;
	private Boolean migraFabbricati;
	private String utenteConnesso;
	
	public String getUtenteConnesso() {
		return utenteConnesso;
	}
	public void setUtenteConnesso(String utenteConnesso) {
		this.utenteConnesso = utenteConnesso;
	}
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public String getCodiceFiscaleRappresentante() {
		return codiceFiscaleRappresentante;
	}
	public void setCodiceFiscaleRappresentante(String codiceFiscaleRappresentante) {
		this.codiceFiscaleRappresentante = codiceFiscaleRappresentante;
	}
	public Long getIdentificativoSportello() {
		return identificativoSportello;
	}
	public void setIdentificativoSportello(Long identificativoSportello) {
		this.identificativoSportello = identificativoSportello;
	}
	public ByteArrayResource getContratto() {
		return contratto;
	}
	public MigrazioneFascicoloDto setContratto(ByteArrayResource contratto) {
		this.contratto = contratto;
		return this;
	}
	public List<ByteArrayResource> getAllegati() {
		return allegati;
	}
	public MigrazioneFascicoloDto setAllegati(List<ByteArrayResource> allegati) {
		this.allegati = allegati;
		return this;
	}
	public Boolean getMigraModoPagamento() {
		return migraModoPagamento;
	}
	public void setMigraModoPagamento(Boolean migraModoPagamento) {
		this.migraModoPagamento = migraModoPagamento;
	}
	public Boolean getMigraMacchinari() {
		return migraMacchinari;
	}
	public void setMigraMacchinari(Boolean migraMacchinari) {
		this.migraMacchinari = migraMacchinari;
	}
	public Boolean getMigraFabbricati() {
		return migraFabbricati;
	}
	public void setMigraFabbricati(Boolean migraFabbricati) {
		this.migraFabbricati = migraFabbricati;
	}
}
