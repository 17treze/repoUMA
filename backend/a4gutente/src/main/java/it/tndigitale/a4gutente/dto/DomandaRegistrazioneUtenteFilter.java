package it.tndigitale.a4gutente.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Objects;

import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.APPROVATA;
import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.RIFIUTATA;

@ApiModel("Rappresenta il modello dei filtri di ricerca delle domande di richieste di accesso al sistema")
public class DomandaRegistrazioneUtenteFilter {

	@ApiParam(value="Stato delle domande da ricercare", required = false)
	private StatoDomandaRegistrazioneUtente stato;
	@ApiParam(value="Filtro generico su Nome, Cognome, Codice fiscale e Protocollo", required = false)
	private String filtroGenerico;
	@ApiParam(value="Codice fiscale a cui fanno riferimento le domande da ricercare (puntuale, non in upperlike)", required = false)
	private String codiceFiscale;
	@ApiParam(value="Nome dell'utente a cui fanno riferimento le domande da ricercare", required = false)
	private String nome;
	@ApiParam(value="Cognome dell'utente a cui fanno riferimento le domande da ricercare", required = false)
	private String cognome;
	@ApiParam(value="Identificativo del protocollo a cui fanno riferimento le domande da ricercare", required = false)
	private String idProtocollo;
	@ApiParam(value = "Data inizio del periodo di riferimento relativo la data di protocollazione")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate dataInizio;
	@ApiParam(value = "Data fine del periodo di riferimento relativo la data di protocollazione")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate dataFine;
	@ApiParam(value="Codice fiscale a cui fanno riferimento le domande da ricercare (in upperlike)", required = false)
	private String codiceFiscaleUpperLike;

	public String getCodiceFiscaleUpperLike() {
		return codiceFiscaleUpperLike;
	}

	public DomandaRegistrazioneUtenteFilter setCodiceFiscaleUpperLike(String codiceFiscaleUpperLike) {
		this.codiceFiscaleUpperLike = codiceFiscaleUpperLike;
		return this;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public DomandaRegistrazioneUtenteFilter setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public StatoDomandaRegistrazioneUtente getStato() {
		return stato;
	}

	public DomandaRegistrazioneUtenteFilter setStato(StatoDomandaRegistrazioneUtente stato) {
		this.stato = stato;
		return this;
	}

	public String getFiltroGenerico() {
		return filtroGenerico;
	}

	public DomandaRegistrazioneUtenteFilter setFiltroGenerico(String filtroGenerico) {
		this.filtroGenerico = filtroGenerico;
		return this;
	}

	public String getNome() {
		return nome;
	}

	public DomandaRegistrazioneUtenteFilter setNome(String nome) {
		this.nome = nome;
		return this;
	}

	public String getCognome() {
		return cognome;
	}

	public DomandaRegistrazioneUtenteFilter setCognome(String cognome) {
		this.cognome = cognome;
		return this;
	}

	public String getIdProtocollo() {
		return idProtocollo;
	}

	public DomandaRegistrazioneUtenteFilter setIdProtocollo(String idProtocollo) {
		this.idProtocollo = idProtocollo;
		return this;
	}

	public LocalDate getDataInizio() {
		return dataInizio;
	}

	public DomandaRegistrazioneUtenteFilter setDataInizio(LocalDate dataInizio) {
		this.dataInizio = dataInizio;
		return this;
	}

	public LocalDate getDataFine() {
		return dataFine;
	}

	public DomandaRegistrazioneUtenteFilter setDataFine(LocalDate dataFine) {
		this.dataFine = dataFine;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DomandaRegistrazioneUtenteFilter that = (DomandaRegistrazioneUtenteFilter) o;
		return stato == that.stato &&
				Objects.equals(filtroGenerico, that.filtroGenerico) &&
				Objects.equals(codiceFiscale, that.codiceFiscale) &&
				Objects.equals(nome, that.nome) &&
				Objects.equals(cognome, that.cognome) &&
				Objects.equals(idProtocollo, that.idProtocollo) &&
				Objects.equals(dataInizio, that.dataInizio) &&
				Objects.equals(dataFine, that.dataFine) &&
				Objects.equals(codiceFiscaleUpperLike, that.codiceFiscaleUpperLike);
	}

	@Override
	public int hashCode() {
		return Objects.hash(stato, filtroGenerico, codiceFiscale, nome, cognome, idProtocollo, dataInizio, dataFine, codiceFiscaleUpperLike);
	}

	public boolean isToOrder() {
		return APPROVATA.equals(stato) || RIFIUTATA.equals(stato);
	}
}
