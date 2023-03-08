package it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.Carica;

/**
 * Modella dati secondo logiche di AGS da cui recupera i dati. Per questo motivo viene messo nel package legacy.
 * Nel nuovo fascicolo una persona avra' una lista di cariche legate ad una persona giuridica + la sua eventuale ditta individuale.
 */
@ApiModel(description = "Rappresenta una carica che una persona fisica ha in una persona giuridica o in una ditta individuale")
public class CaricaAgsDto {
	@ApiModelProperty(value = "Codice fiscale di una persona fisica")
	private String codiceFiscale;

	@ApiModelProperty(value = "Carica ricoperta dalla persona fisica nella persona identificata dal cuaa")
	private Carica carica;

	@ApiModelProperty(value = "Cuaa della pesona in cui la persona fisica ha la carica")
	private String cuaa;

	@ApiModelProperty(value = "Denominazione della persona")
	private String denominazione;

	@ApiModelProperty(value = "nome della persona")
	private String nome;

	@ApiModelProperty(value = "cognome della persona")
	private String cognome;

	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public CaricaAgsDto setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}
	public Carica getCarica() {
		return carica;
	}
	public CaricaAgsDto setCarica(Carica carica) {
		this.carica = carica;
		return this;
	}
	public String getCuaa() {
		return cuaa;
	}
	public CaricaAgsDto setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public CaricaAgsDto setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public String getNome() {
		return nome;
	}
	public CaricaAgsDto setNome(String nome) {
		this.nome = nome;
		return this;
	}
	public String getCognome() {
		return cognome;
	}
	public CaricaAgsDto setCognome(String cognome) {
		this.cognome = cognome;
		return this;
	}

}
