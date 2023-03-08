package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.TipoConduzione;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
	@JsonSubTypes.Type(value = DettaglioFabbricatoDto.class, name = "FABBRICATO"),
	@JsonSubTypes.Type(value = DettaglioStrumentaliDto.class, name = "STRUMENTALE"),
	@JsonSubTypes.Type(value = DettaglioSerreDto.class, name = "SERRA"),
	@JsonSubTypes.Type(value = DettaglioStoccaggioDto.class, name = "STOCCAGGIO")
})
// Openapi-generator non supporta OneOf nel request body delle chiamate REST. E' necessario adottare un workaround indicando al generatore come discriminare tra le sottoclassi.
@Schema(discriminatorProperty = "type", discriminatorMapping = {
		@DiscriminatorMapping(schema = DettaglioFabbricatoDto.class, value = "FABBRICATO"),
		@DiscriminatorMapping(schema = DettaglioStrumentaliDto.class, value = "STRUMENTALE"),
		@DiscriminatorMapping(schema = DettaglioSerreDto.class, value = "SERRA"),
		@DiscriminatorMapping(schema = DettaglioStoccaggioDto.class, value = "STOCCAGGIO")
})
public abstract class DettaglioFabbricatoAbstract {

	@JsonProperty("type")
	private NameTypeFabbricato type;

	private Long id;
	private SottotipoDto sottotipo;
	private String denominazione;
	private String indirizzo;
	private String comune;
	private Long volume;
	private Long superficie;
	private String descrizione;
	private TipoConduzione tipoConduzione;
	private List<DatiCatastaliDto> datiCatastali;

	public Long getId() {
		return id;
	}
	public DettaglioFabbricatoAbstract setId(Long id) {
		this.id = id;
		return this;
	}
	public SottotipoDto getSottotipo() {
		return sottotipo;
	}
	public DettaglioFabbricatoAbstract setSottotipo(SottotipoDto sottotipo) {
		this.sottotipo = sottotipo;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public DettaglioFabbricatoAbstract setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public DettaglioFabbricatoAbstract setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
		return this;
	}
	public String getComune() {
		return comune;
	}
	public DettaglioFabbricatoAbstract setComune(String comune) {
		this.comune = comune;
		return this;
	}
	public Long getVolume() {
		return volume;
	}
	public DettaglioFabbricatoAbstract setVolume(Long volume) {
		this.volume = volume;
		return this;
	}
	public Long getSuperficie() {
		return superficie;
	}
	public DettaglioFabbricatoAbstract setSuperficie(Long superficie) {
		this.superficie = superficie;
		return this;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public DettaglioFabbricatoAbstract setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}
	public NameTypeFabbricato getType() {
		return type;
	}
	public DettaglioFabbricatoAbstract setType(NameTypeFabbricato type) {
		this.type = type;
		return this;
	}
	public List<DatiCatastaliDto> getDatiCatastali() {
		return datiCatastali;
	}
	public DettaglioFabbricatoAbstract setDatiCatastali(List<DatiCatastaliDto> datiCatastali) {
		this.datiCatastali = datiCatastali;
		return this;
	}
	public TipoConduzione getTipoConduzione() {
		return tipoConduzione;
	}
	public DettaglioFabbricatoAbstract setTipoConduzione(TipoConduzione tipoConduzione) {
		this.tipoConduzione = tipoConduzione;
		return this;
	}
}
