package it.tndigitale.a4g.uma.dto.richiesta.stampa;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteCompletoDto;
import it.tndigitale.a4g.uma.dto.richiesta.MacchinaDto;

@JsonInclude(Include.NON_EMPTY)
public class StampaRichiestaCarburanteDto {
	private Long idRichiesta;
	private Long campagna;
	private String cuaa;
	private String denominazione;
	private List<MacchinaDto> macchine;
	private List<StampaRaggruppamentoLavorazioniDto> raggruppamentoLavorazioneSuperficie;
	private List<StampaRaggruppamentoLavorazioniDto> raggruppamentoLavorazioneFabbricati;
	private List<StampaRaggruppamentoLavorazioniDto> raggruppamentoLavorazioneSerre;
	private List<StampaRaggruppamentoLavorazioniDto> raggruppamentoLavorazioneZootecnia;
	private List<StampaRaggruppamentoLavorazioniDto> raggruppamentoLavorazioneAltro;
	private CarburanteCompletoDto residuo;
	private CarburanteDto ammissibile;
	private CarburanteCompletoDto richiesto;
	private CarburanteDto prelevato;
	private String note;
	private String data;
	private boolean isRettifica;

	public Long getIdRichiesta() {
		return idRichiesta;
	}

	public void setIdRichiesta(Long idRichiesta) {
		this.idRichiesta = idRichiesta;
	}

	public Long getCampagna() {
		return campagna;
	}

	public void setCampagna(Long campagna) {
		this.campagna = campagna;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public List<MacchinaDto> getMacchine() {
		return macchine;
	}

	public void setMacchine(List<MacchinaDto> macchine) {
		this.macchine = macchine;
	}

	public List<StampaRaggruppamentoLavorazioniDto> getRaggruppamentoLavorazioneSuperficie() {
		return raggruppamentoLavorazioneSuperficie;
	}

	public void setRaggruppamentoLavorazioneSuperficie(List<StampaRaggruppamentoLavorazioniDto> raggruppamentoLavorazioneSuperficie) {
		this.raggruppamentoLavorazioneSuperficie = raggruppamentoLavorazioneSuperficie;
	}

	public List<StampaRaggruppamentoLavorazioniDto> getRaggruppamentoLavorazioneAltro() {
		return raggruppamentoLavorazioneAltro;
	}

	public void setRaggruppamentoLavorazioneAltro(List<StampaRaggruppamentoLavorazioniDto> raggruppamentoLavorazioneAltro) {
		this.raggruppamentoLavorazioneAltro = raggruppamentoLavorazioneAltro;
	}

	public List<StampaRaggruppamentoLavorazioniDto> getRaggruppamentoLavorazioneSerre() {
		return raggruppamentoLavorazioneSerre;
	}

	public void setRaggruppamentoLavorazioneSerre(List<StampaRaggruppamentoLavorazioniDto> raggruppamentoLavorazioneSerre) {
		this.raggruppamentoLavorazioneSerre = raggruppamentoLavorazioneSerre;
	}

	public List<StampaRaggruppamentoLavorazioniDto> getRaggruppamentoLavorazioneFabbricati() {
		return raggruppamentoLavorazioneFabbricati;
	}

	public void setRaggruppamentoLavorazioneFabbricati(List<StampaRaggruppamentoLavorazioniDto> raggruppamentoLavorazioneFabbricati) {
		this.raggruppamentoLavorazioneFabbricati = raggruppamentoLavorazioneFabbricati;
	}

	public List<StampaRaggruppamentoLavorazioniDto> getRaggruppamentoLavorazioneZootecnia() {
		return raggruppamentoLavorazioneZootecnia;
	}

	public void setRaggruppamentoLavorazioneZootecnia(List<StampaRaggruppamentoLavorazioniDto> raggruppamentoLavorazioneZootecnia) {
		this.raggruppamentoLavorazioneZootecnia = raggruppamentoLavorazioneZootecnia;
	}

	public CarburanteCompletoDto getRichiesto() {
		return richiesto;
	}

	public void setRichiesto(CarburanteCompletoDto richiesto) {
		this.richiesto = richiesto;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public CarburanteCompletoDto getResiduo() {
		return residuo;
	}

	public void setResiduo(CarburanteCompletoDto residuo) {
		this.residuo = residuo;
	}

	public CarburanteDto getAmmissibile() {
		return ammissibile;
	}

	public void setAmmissibile(CarburanteDto ammissibile) {
		this.ammissibile = ammissibile;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean getIsRettifica() {
		return isRettifica;
	}

	public void setIsRettifica(boolean isRettifica) {
		this.isRettifica = isRettifica;
	}

	public CarburanteDto getPrelevato() {
		return prelevato;
	}

	public void setPrelevato(CarburanteDto prelevato) {
		this.prelevato = prelevato;
	}

}
