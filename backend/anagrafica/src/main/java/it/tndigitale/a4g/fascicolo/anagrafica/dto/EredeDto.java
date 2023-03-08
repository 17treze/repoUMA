package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.EredeModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.IndirizzoModel;

@ApiModel(description = "Erede del titolare (deceduto) di un fascicolo")
public class EredeDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "Identificativo erede")
	private Long id;
	@ApiModelProperty(value = "CUAA fascicolo")
	private String cuaa;
	@ApiModelProperty(value = "Codice fiscale erede")
	private String cfErede;
	@ApiModelProperty(value = "Firmatario")
	private Boolean firmatario;
	@ApiModelProperty(value = "Cognome")
	private String cognome;
	@ApiModelProperty(value = "Nome")
	private String nome;
	@ApiModelProperty(value = "Data di nascita")
	private LocalDate dataNascita;
	@ApiModelProperty(value = "Comune di nascita")
	private String comuneNascita;
	@ApiModelProperty(value = "Provincia di nascita")
	private String provinciaNascita;
	@ApiModelProperty(value = "Indirizzo di residenza")
	private IndirizzoModel indirizzo;

	public Boolean getFirmatario() {
		return firmatario;
	}

	public void setFirmatario(Boolean firmatario) {
		this.firmatario = firmatario;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getCfErede() {
		return cfErede;
	}

	public void setCfErede(String cfErede) {
		this.cfErede = cfErede;
	}
	
	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getComuneNascita() {
		return comuneNascita;
	}

	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}
	
	public String getProvinciaNascita() {
		return provinciaNascita;
	}

	public void setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
	}

	public IndirizzoModel getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(IndirizzoModel indirizzo) {
		this.indirizzo = indirizzo;
	}

	public static List<EredeDto> build(final List<EredeModel> input) {
		List<EredeDto> output = new ArrayList<>();
		if (input == null || input.isEmpty()) {
			return output;
		}
		input.forEach(erede -> {
			output.add(EredeDto.mapper(erede));
		});
		return output;
	}
	
	private static EredeDto mapper(EredeModel input) {
		EredeDto dto = new EredeDto();
		dto.setId(input.getId());
		dto.setFirmatario(input.isFirmatario());
		dto.setCfErede(input.getPersonaFisica().getCodiceFiscale());
		dto.setCuaa(input.getFascicolo().getCuaa());
		dto.setCognome(input.getPersonaFisica().getCognome());
		dto.setNome(input.getPersonaFisica().getNome());
		dto.setDataNascita(input.getPersonaFisica().getDataNascita());
		dto.setComuneNascita(input.getPersonaFisica().getComuneNascita());
		dto.setProvinciaNascita(input.getPersonaFisica().getProvinciaNascita());
		dto.setIndirizzo(input.getPersonaFisica().getDomicilioFiscale());
		return dto;
	}
}
