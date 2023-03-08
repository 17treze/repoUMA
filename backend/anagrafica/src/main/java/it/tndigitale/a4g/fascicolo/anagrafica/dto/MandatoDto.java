package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.SportelloCAADto;

@ApiModel(description = "Dati del Mandato di un fascicolo ")
public class MandatoDto extends DetenzioneDto {

	private static final long serialVersionUID = -3671223164827549257L;
	@ApiModelProperty(value = "Dati dello sportello")
	private SportelloCAADto sportello;
	@ApiModelProperty(value = "Atto di riconoscimento")
	private String attoRiconoscimento;
	@ApiModelProperty(value = "Societa servizi")
	private String societaServizi;
	@ApiModelProperty(value = "Sede Amministrativa Caa")
	private IndirizzoSedeAmministrativaDto indirizzoSedeAmministrativa;
	@ApiModelProperty(value = "Data sottoscrizione")
	private LocalDate dataSottoscrizione;
	@ApiModelProperty(value = "Partita IVA")
	private String partitaIva;

	public SportelloCAADto getSportello() {
		return sportello;
	}
	public void setSportello(SportelloCAADto sportello) {
		this.sportello = sportello;
	}
	public String getAttoRiconoscimento() {
		return attoRiconoscimento;
	}
	public void setAttoRiconoscimento(String attoRiconoscimento) {
		this.attoRiconoscimento = attoRiconoscimento;
	}
	public String getSocietaServizi() {
		return societaServizi;
	}
	public void setSocietaServizi(String societaServizi) {
		this.societaServizi = societaServizi;
	}
	public IndirizzoSedeAmministrativaDto getIndirizzoSedeAmministrativa() {
		return indirizzoSedeAmministrativa;
	}
	public void setIndirizzoSedeAmministrativa(IndirizzoSedeAmministrativaDto indirizzoSedeAmministrativa) {
		this.indirizzoSedeAmministrativa = indirizzoSedeAmministrativa;
	}
	public LocalDate getDataSottoscrizione() {
		return dataSottoscrizione;
	}
	public void setDataSottoscrizione(LocalDate dataSottoscrizione) {
		this.dataSottoscrizione = dataSottoscrizione;
	}
	public String getPartitaIva() {
		return partitaIva;
	}
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}
}
