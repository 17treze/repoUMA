package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import io.swagger.annotations.ApiModelProperty;

public class PersonaFisicaDto extends PersonaDto {
	private static final long serialVersionUID = -6516246565766404422L;

	@ApiModelProperty(value = "I dati dell'eventuale ditta individuale")
	private AnagraficaDto anagrafica;

	@ApiModelProperty(value = "Domicilio fiscale della persona fisica")
	private IndirizzoDto domicilioFiscale;

	@ApiModelProperty(value = "I dati dell'eventuale ditta individuale")
	private ImpresaIndividualeDto impresaIndividuale;

	public AnagraficaDto getAnagrafica() {
		return anagrafica;
	}

	public void setAnagrafica(AnagraficaDto anagrafica) {
		this.anagrafica = anagrafica;
	}

	public IndirizzoDto getDomicilioFiscale() {
		return domicilioFiscale;
	}

	public void setDomicilioFiscale(IndirizzoDto domicilioFiscale) {
		this.domicilioFiscale = domicilioFiscale;
	}

	public ImpresaIndividualeDto getImpresaIndividuale() {
		return impresaIndividuale;
	}

	public void setImpresaIndividuale(ImpresaIndividualeDto impresaIndividuale) {
		this.impresaIndividuale = impresaIndividuale;
	}
}
