package it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(description = "DTO della conduzione del terreno recuperato dal SIAN")
public class ConduzioneDto implements Serializable{

	@ApiModelProperty(value = "SuperficieCondotta")
	private String superficieCondotta;
	@ApiModelProperty(value = "CodiceTipoConduzione")
	private String codiceTipoConduzione;
	@ApiModelProperty(value = "descrizioneTipoConduzione")
	private String descrizioneTipoConduzione;
	@ApiModelProperty(value = "DataInizioConduzione")
	private String dataInizioConduzione;
	@ApiModelProperty(value = "DataFineConduzione")
	private String dataFineConduzione;
	@ApiModelProperty(value = "Elenco Codice Fiscale dei Proprietari")
	private List<String> codiceFiscaleProprietarioList;
	@ApiModelProperty(value = "Dati Particella")
	private ParticellaDto datiParticella;
	@ApiModelProperty(value = "Caratteristiche della zona")
	private CaratteristicheZonaDto caratteristicheZona;
	@ApiModelProperty(value = "Elenco documenti giustificativi sulla conduzione terreni")
	private List<DocumentoConduzioneDto> documentiConduzione;

	public String getSuperficieCondotta() {
		return superficieCondotta;
	}

	public void setSuperficieCondotta(String superficieCondotta) {
		this.superficieCondotta = superficieCondotta;
	}

	public String getCodiceTipoConduzione() {
		return codiceTipoConduzione;
	}

	public void setCodiceTipoConduzione(String codiceTipoConduzione) {
		this.codiceTipoConduzione = codiceTipoConduzione;
	}

	public String getDescrizioneTipoConduzione() {
		return descrizioneTipoConduzione;
	}

	public void setDescrizioneTipoConduzione(String descrizioneTipoConduzione) {
		this.descrizioneTipoConduzione = descrizioneTipoConduzione;
	}

	public String getDataInizioConduzione() {
		return dataInizioConduzione;
	}

	public void setDataInizioConduzione(String dataInizioConduzione) {
		this.dataInizioConduzione = dataInizioConduzione;
	}

	public String getDataFineConduzione() {
		return dataFineConduzione;
	}

	public void setDataFineConduzione(String dataFineConduzione) {
		this.dataFineConduzione = dataFineConduzione;
	}

	public List<String> getCodiceFiscaleProprietarioList() {
		return codiceFiscaleProprietarioList;
	}

	public void setCodiceFiscaleProprietarioList(List<String> codiceFiscaleProprietarioList) {
		this.codiceFiscaleProprietarioList = codiceFiscaleProprietarioList;
	}

	public ParticellaDto getDatiParticella() {
		return datiParticella;
	}

	public void setDatiParticella(ParticellaDto datiParticella) {
		this.datiParticella = datiParticella;
	}

	public CaratteristicheZonaDto getCaratteristicheZona() {
		return caratteristicheZona;
	}

	public void setCaratteristicheZona(CaratteristicheZonaDto caratteristicheZona) {
		this.caratteristicheZona = caratteristicheZona;
	}

	public List<DocumentoConduzioneDto> getDocumentiConduzione() {
		return documentiConduzione;
	}

	public void setDocumentiConduzione(List<DocumentoConduzioneDto> documentiConduzione) {
		this.documentiConduzione = documentiConduzione;
	}
}
