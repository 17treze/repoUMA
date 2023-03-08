package it.tndigitale.a4g.proxy.dto;

import org.springframework.http.HttpStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.tndigitale.a4g.proxy.services.pagopa.CheckIbanResponse;
import it.tndigitale.a4g.proxy.services.pagopa.VerificaIbanException;

@ApiModel(description = "Rappresenta le informazioni su un Iban fornite da PagoPA")
public class PagoPaIbanDettaglioDto {
	@ApiModelProperty(value = "Iban di cui si e' richiesta la verifica")
	private String iban;

	@ApiModelProperty(value = "BIC del conto")
	private String bic;

	@ApiModelProperty(value = "Denominazione Istituto di credito associato all'iban")
	private String denominazioneIstituto;

	@ApiModelProperty(value = "Denominazione della filiale dell'Istituto di credito associato all'iban")
	private String denominazioneFiliale;

	@ApiModelProperty(value = "Sede della filiale dell'Istituto di credito associato all'iban")
	private String cittaFiliale;

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getDenominazioneIstituto() {
		return denominazioneIstituto;
	}

	public void setDenominazioneIstituto(String denominazioneIstituto) {
		this.denominazioneIstituto = denominazioneIstituto;
	}

	public String getDenominazioneFiliale() {
		return denominazioneFiliale;
	}

	public void setDenominazioneFiliale(String denominazioneFiliale) {
		this.denominazioneFiliale = denominazioneFiliale;
	}

	public String getCittaFiliale() {
		return cittaFiliale;
	}

	public void setCittaFiliale(String cittaFiliale) {
		this.cittaFiliale = cittaFiliale;
	}

	public static PagoPaIbanDettaglioDto buildFrom(String iban, CheckIbanResponse checkIbanResponse) throws VerificaIbanException {
		if (!checkIbanResponse.getValidation()) {
			throw new VerificaIbanException(HttpStatus.NOT_ACCEPTABLE, "PagoPA non certifica l’IBAN: non è possibile salvarlo a sistema.");
		}
		PagoPaIbanDettaglioDto dto = new PagoPaIbanDettaglioDto();
		dto.setIban(iban);
		if (checkIbanResponse.getInfo() != null) {
			dto.setDenominazioneIstituto(checkIbanResponse.getInfo().getBanca());
			dto.setBic(checkIbanResponse.getInfo().getBic());
			dto.setCittaFiliale(checkIbanResponse.getInfo().getSede());
			dto.setDenominazioneFiliale(checkIbanResponse.getInfo().getFiliale());
		} else {
			dto.setDenominazioneIstituto("Informazione non certificata da PagoPA");
			dto.setBic("Dato non disponibile");
			dto.setCittaFiliale("Dato non disponibile");
			dto.setDenominazioneFiliale("Dato non disponibile");
		}

		return dto;
	}

}
