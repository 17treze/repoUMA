package it.tndigitale.a4g.fascicolo.territorio.dto.conduzione;

import java.io.Serializable;
import java.util.List;

/**
 * DTO della conduzione del terreno recuperato dal SIAN
 */
public class ConduzioneSianDto implements Serializable {

	private static final long serialVersionUID = 2917841080906509730L;

	private String superficieCondotta;
	private String codiceTipoConduzione;
	private String descrizioneTipoConduzione;
	private String dataInizioConduzione;
	private String dataFineConduzione;
	private List<String> codiceFiscaleProprietarioList;
	private ParticellaSianDto datiParticella;
	private CaratteristicheZonaDto caratteristicheZona;
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

	public ParticellaSianDto getDatiParticella() {
		return datiParticella;
	}

	public void setDatiParticella(ParticellaSianDto datiParticella) {
		this.datiParticella = datiParticella;
	}

	public CaratteristicheZonaDto getCaratteristicheZona() {
		return caratteristicheZona;
	}

	public void setCaratteristicheZona(CaratteristicheZonaDto caratteristicheZona) {
		this.caratteristicheZona = caratteristicheZona;
	}

	public String getDescrizioneTipoConduzione() {
		return descrizioneTipoConduzione;
	}

	public void setDescrizioneTipoConduzione(String descrizioneTipoConduzione) {
		this.descrizioneTipoConduzione = descrizioneTipoConduzione;
	}

	public List<DocumentoConduzioneDto> getDocumentiConduzione() {
		return documentiConduzione;
	}

	public void setDocumentiConduzione(List<DocumentoConduzioneDto> documentiConduzione) {
		this.documentiConduzione = documentiConduzione;
	}
}

