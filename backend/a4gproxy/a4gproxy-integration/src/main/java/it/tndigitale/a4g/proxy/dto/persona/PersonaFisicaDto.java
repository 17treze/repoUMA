package it.tndigitale.a4g.proxy.dto.persona;

import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class PersonaFisicaDto extends PersonaDto {

	@ApiModelProperty(value = "I dati dell'eventuale ditta individuale")
	private AnagraficaDto anagrafica;

	@ApiModelProperty(value = "Domicilio fiscale della persona fisica")
	private IndirizzoDto domicilioFiscale;

	@ApiModelProperty(value = "Se si tratta di una pesona deceduta", required = true)
	private Boolean deceduta;

	@ApiModelProperty(value = "Data di morte", required = true)
	private LocalDate dataMorte;

	@ApiModelProperty(value = "I dati dell'eventuale ditta individuale")
	private ImpresaIndividualeDto impresaIndividuale;

	private List<PersonaFisicaConCaricaDto> personeFisicheConCarica;

	private List<PersonaGiuridicaConCaricaDto> personeGiuridicheConCarica;

	public Boolean getDeceduta() {
		return deceduta;
	}

	public PersonaFisicaDto setDeceduta(Boolean deceduta) {
		this.deceduta = deceduta;
		return this;
	}

	public AnagraficaDto getAnagrafica() {
		return anagrafica;
	}

	public PersonaFisicaDto setAnagrafica(AnagraficaDto anagrafica) {
		this.anagrafica = anagrafica;
		return this;
	}

	public ImpresaIndividualeDto getImpresaIndividuale() {
		return impresaIndividuale;
	}

	public PersonaFisicaDto setImpresaIndividuale(ImpresaIndividualeDto impresaIndividuale) {
		this.impresaIndividuale = impresaIndividuale;
		return this;
	}

	public IndirizzoDto getDomicilioFiscale() {
		return domicilioFiscale;
	}

	public PersonaFisicaDto setDomicilioFiscale(IndirizzoDto domicilioFiscale) {
		this.domicilioFiscale = domicilioFiscale;
		return this;
	}

	public List<PersonaFisicaConCaricaDto> getPersoneFisicheConCarica() {
		return personeFisicheConCarica;
	}

	public PersonaFisicaDto setPersoneFisicheConCarica(List<PersonaFisicaConCaricaDto> personeFisicheConCarica) {
		this.personeFisicheConCarica = personeFisicheConCarica;
		return this;
	}

	public List<PersonaGiuridicaConCaricaDto> getPersoneGiuridicheConCarica() {
		return personeGiuridicheConCarica;
	}

	public PersonaFisicaDto setPersoneGiuridicheConCarica(List<PersonaGiuridicaConCaricaDto> personeGiuridicheConCarica) {
		this.personeGiuridicheConCarica = personeGiuridicheConCarica;
		return this;
	}
	public LocalDate getDataMorte() {
		return dataMorte;
	}
	public PersonaFisicaDto setDataMorte(LocalDate dataMorte) {
		this.dataMorte = dataMorte;
		return this;
	}
}
