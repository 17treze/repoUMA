/**
 * 
 */
package it.tndigitale.a4gutente.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.tndigitale.a4gutente.repository.model.A4gtProfilo;
import it.tndigitale.a4gutente.service.builder.ProfiloBuilder;
import it.tndigitale.a4gutente.utility.ListSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static it.tndigitale.a4gutente.service.builder.ProfiloBuilder.creaProfiloDisattivo;
import static it.tndigitale.a4gutente.utility.ListSupport.emptyIfNull;

/**
 * @author it417
 *
 */
@ApiModel(description = "Rappresenta il modello per rappresentare un utente del sistema")
public class Utente {

	@ApiModelProperty(value = "Id del database")
	private Long id;
	@ApiModelProperty(value = "Identificativo dell'utente")
	private String identificativo;
	@ApiModelProperty(value = "Codice fiscale dell'utente")
	private String codiceFiscale;
	@ApiModelProperty(value = "Nome dell'utente")
	private String nome;
	@ApiModelProperty(value = "Cognome dell'utente")
	private String cognome;
	@ApiModelProperty(value = "Lista dei profili")
	private List<Profilo> profili = new ArrayList<>();
	@ApiModelProperty(value = "Lista delle sedi Caa di appartenenza")
	private List<EnteSede> sedi;
	@ApiModelProperty(value = "Lista dei distributori")
	private List<Distributore> distributori;
	@ApiModelProperty(value = "Lista delle aziende")
	private List<Azienda> aziende;
	@ApiModelProperty(value = "Motivazione di un eventuale disattivazione")
	private MotivazioneDisattivazione motivazioneDisattivazione;

	public String getIdentificativo() {
		return identificativo;
	}

	public Utente setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
		return this;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public Utente setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public List<Profilo> getProfili() {
		return profili;
	}

	public Utente setProfili(List<Profilo> profili) {
		this.profili = profili;
		return this;
	}

	public Long getId() {
		return id;
	}

	public Utente setId(Long id) {
		this.id = id;
		return this;
	}

	public String getNome() {
		return nome;
	}

	public Utente setNome(String nome) {
		this.nome = nome;
		return this;
	}

	public String getCognome() {
		return cognome;
	}

	public Utente setCognome(String cognome) {
		this.cognome = cognome;
		return this;
	}

	public List<Azienda> getAziende() {
		return aziende;
	}

	public Utente setAziende(List<Azienda> aziende) {
		this.aziende = aziende;
		return this;
	}

	public List<EnteSede> getSedi() {
		return sedi;
	}

	public Utente setSedi(List<EnteSede> sedi) {
		this.sedi = sedi;
		return this;
	}

	public List<Distributore> getDistributori() {
		return distributori;
	}

	public Utente setDistributori(List<Distributore> distributori) {
		this.distributori = distributori;
		return this;
	}

	public MotivazioneDisattivazione getMotivazioneDisattivazione() {
		return motivazioneDisattivazione;
	}

	public Utente setMotivazioneDisattivazione(MotivazioneDisattivazione motivazioneDisattivazione) {
		this.motivazioneDisattivazione = motivazioneDisattivazione;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Utente utente = (Utente) o;
		return Objects.equals(id, utente.id) &&
				Objects.equals(identificativo, utente.identificativo) &&
				Objects.equals(codiceFiscale, utente.codiceFiscale) &&
				Objects.equals(nome, utente.nome) &&
				Objects.equals(cognome, utente.cognome) &&
				Objects.equals(profili, utente.profili) &&
				Objects.equals(sedi, utente.sedi) &&
				Objects.equals(aziende, utente.aziende) &&
				motivazioneDisattivazione == utente.motivazioneDisattivazione;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, identificativo, codiceFiscale,
				nome, cognome, profili, sedi,
				aziende, motivazioneDisattivazione);
	}

	public Utente disableProfiles(List<A4gtProfilo> profiliDisattivati) {
		List<Profilo> profili = ProfiloBuilder.creaProfiliDisattivi(profiliDisattivati);
		this.profili.addAll(profili);
		return this;
	}

}