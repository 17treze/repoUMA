package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaConCaricaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.Sesso;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.VerificaCodiceFiscale;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.builder.persona.IndirizzoDtoBuilder;
import it.tndigitale.a4g.proxy.client.model.AnagraficaDto.SessoEnum;

public class PersonaFisicaConCaricaDto extends PersonaDto {

	private static final long serialVersionUID = 5281682083384167962L;

	private String nome;

    private String cognome;

    private List<CaricaDto> cariche;
    
    private SessoEnum sesso;
    
    private String comuneNascita;
    
    private String siglaProvinciaNascita;
    
    private LocalDate dataNascita;
    
    private IndirizzoDto domicilioFiscale;

    /**
     * VerificaCodiceFiscaleEnum
     */
    public enum VerificaCodiceFiscaleEnum {
		CORRETTO("CORRETTO"),
		NON_TROVATO_IN_AT("NON_TROVATO_IN_AT"),
    	PARIX_DIVERSO_DA_AT("PARIX_DIVERSO_DA_AT");
    	
    	private String value;

    	VerificaCodiceFiscaleEnum(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static VerificaCodiceFiscaleEnum fromValue(String text) {
			for (VerificaCodiceFiscaleEnum b : VerificaCodiceFiscaleEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
    }

    
    private VerificaCodiceFiscaleEnum verificaCodiceFiscale;

	public String getNome() {
		return nome;
	}

	public PersonaFisicaConCaricaDto setNome(String nome) {
		this.nome = nome;
		return this;
	}

	public String getCognome() {
		return cognome;
	}

	public PersonaFisicaConCaricaDto setCognome(String cognome) {
		this.cognome = cognome;
		return this;
	}
	
	public List<CaricaDto> getCariche() {
		return cariche;
	}

	public PersonaFisicaConCaricaDto setCariche(List<CaricaDto> cariche) {
		this.cariche = cariche;
		return this;
	}

	public SessoEnum getSesso() {
		return sesso;
	}

	public void setSesso(SessoEnum sesso) {
		this.sesso = sesso;
	}

	public IndirizzoDto getDomicilioFiscale() {
		return domicilioFiscale;
	}

	public void setDomicilioFiscale(IndirizzoDto domicilioFiscale) {
		this.domicilioFiscale = domicilioFiscale;
	}

	public String getComuneNascita() {
		return comuneNascita;
	}

	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}

	public String getSiglaProvinciaNascita() {
		return siglaProvinciaNascita;
	}

	public void setSiglaProvinciaNascita(String siglaProvinciaNascita) {
		this.siglaProvinciaNascita = siglaProvinciaNascita;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}
	
	public VerificaCodiceFiscaleEnum getVerificaCodiceFiscale() {
		return this.verificaCodiceFiscale;
	}

	public void setVerificaCodiceFiscale(VerificaCodiceFiscaleEnum verificaCodiceFiscale) {
		this.verificaCodiceFiscale = verificaCodiceFiscale;
	}

	public static PersonaFisicaConCaricaDto toDto(
			final PersonaFisicaConCaricaModel model) {
		IndirizzoDtoBuilder indirizzoDtoBuilder = new IndirizzoDtoBuilder();
		PersonaFisicaConCaricaDto dto = new PersonaFisicaConCaricaDto();
		dto.setCodiceFiscale(model.getCodiceFiscale());
		dto.setNome(model.getNome());
		dto.setCognome(model.getCognome());
		Sesso sessoEnum = model.getSesso();
		if (sessoEnum != null) {
			dto.setSesso(SessoEnum.fromValue(sessoEnum.name()));			
		}
		dto.setComuneNascita(model.getComuneNascita());
		dto.setSiglaProvinciaNascita(model.getProvinciaNascita());
		dto.setDataNascita(model.getDataNascita());
		dto.setDomicilioFiscale(indirizzoDtoBuilder.withIndirizzo(model.getIndirizzo()).build());
		VerificaCodiceFiscale vcfEnum = model.getVerificaCodiceFiscale();
		if (vcfEnum != null) {
			dto.setVerificaCodiceFiscale(VerificaCodiceFiscaleEnum.fromValue(vcfEnum.name()));			
		}
		return dto;
	}
}
