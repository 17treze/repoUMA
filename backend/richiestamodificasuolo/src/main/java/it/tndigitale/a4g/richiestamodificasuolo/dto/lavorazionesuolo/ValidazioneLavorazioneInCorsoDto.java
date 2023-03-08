package it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class ValidazioneLavorazioneInCorsoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2790423696662818360L;
	private Long idLavorazione;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataValidazione;

	private List<SuoloDichiaratoLavorazioneDto> poligoniDichiaratoSenzaEsito;
	private List<SuoloDichiaratoLavorazioneDto> poligoniDichiaratoRichiestaCancellata;
	private List<WorkspaceLavSuoloDto> poligoniSuoloAttributiMancanti;
	private List<AnomaliaValidazioneDto> poligoniAnomaliaSovrapposizioni;
	private List<AnomaliaPoligoniWithRootWorkspaceDto> poligoniAnomaliaDebordanoAreaDiLavoro;
	private List<AnomaliaPoligoniWithRootWorkspaceDto> poligoniAnomalieOracle;

	public Long getIdLavorazione() {
		return idLavorazione;
	}

	public void setIdLavorazione(Long idLavorazione) {
		this.idLavorazione = idLavorazione;
	}

	public LocalDateTime getDataValidazione() {
		return dataValidazione;
	}

	public void setDataValidazione(LocalDateTime dataValidazione) {
		this.dataValidazione = dataValidazione;
	}

	public List<SuoloDichiaratoLavorazioneDto> getPoligoniDichiaratoSenzaEsito() {
		return poligoniDichiaratoSenzaEsito;
	}

	public void setPoligoniDichiaratoSenzaEsito(List<SuoloDichiaratoLavorazioneDto> poligoniDichiaratoSenzaEsito) {
		this.poligoniDichiaratoSenzaEsito = poligoniDichiaratoSenzaEsito;
	}

	public List<SuoloDichiaratoLavorazioneDto> getPoligoniDichiaratoRichiestaCancellata() {
		return poligoniDichiaratoRichiestaCancellata;
	}

	public void setPoligoniDichiaratoRichiestaCancellata(List<SuoloDichiaratoLavorazioneDto> poligoniDichiaratoRichiestaCancellata) {
		this.poligoniDichiaratoRichiestaCancellata = poligoniDichiaratoRichiestaCancellata;
	}

	public List<WorkspaceLavSuoloDto> getPoligoniSuoloAttributiMancanti() {
		return poligoniSuoloAttributiMancanti;
	}

	public void setPoligoniSuoloAttributiMancanti(List<WorkspaceLavSuoloDto> poligoniSuoloAttributiMancanti) {
		this.poligoniSuoloAttributiMancanti = poligoniSuoloAttributiMancanti;
	}

	public List<AnomaliaValidazioneDto> getPoligoniAnomaliaSovrapposizioni() {
		return poligoniAnomaliaSovrapposizioni;
	}

	public void setPoligoniAnomaliaSovrapposizioni(List<AnomaliaValidazioneDto> poligoniAnomaliaSovrapposizioni) {
		this.poligoniAnomaliaSovrapposizioni = poligoniAnomaliaSovrapposizioni;
	}

	public List<AnomaliaPoligoniWithRootWorkspaceDto> getPoligoniAnomaliaDebordanoAreaDiLavoro() {
		return poligoniAnomaliaDebordanoAreaDiLavoro;
	}

	public void setPoligoniAnomaliaDebordanoAreaDiLavoro(List<AnomaliaPoligoniWithRootWorkspaceDto> poligoniAnomaliaDebordanoAreaDiLavoro) {
		this.poligoniAnomaliaDebordanoAreaDiLavoro = poligoniAnomaliaDebordanoAreaDiLavoro;
	}

	public List<AnomaliaPoligoniWithRootWorkspaceDto> getPoligoniAnomalieOracle() {
		return poligoniAnomalieOracle;
	}

	public void setPoligoniAnomalieOracle(List<AnomaliaPoligoniWithRootWorkspaceDto> poligoniAnomalieOracle) {
		this.poligoniAnomalieOracle = poligoniAnomalieOracle;
	}

	public boolean isValidazioneCorretta() {

		if ((getPoligoniDichiaratoSenzaEsito() == null || getPoligoniDichiaratoSenzaEsito().size() == 0)
				&& (getPoligoniDichiaratoRichiestaCancellata() == null || getPoligoniDichiaratoRichiestaCancellata().size() == 0)
				&& (getPoligoniSuoloAttributiMancanti() == null || getPoligoniSuoloAttributiMancanti().size() == 0)
				&& (getPoligoniAnomaliaSovrapposizioni() == null || getPoligoniAnomaliaSovrapposizioni().size() == 0)
				&& (getPoligoniAnomaliaDebordanoAreaDiLavoro() == null || getPoligoniAnomaliaDebordanoAreaDiLavoro().size() == 0)
				&& (getPoligoniAnomalieOracle() == null || getPoligoniAnomalieOracle().size() == 0)) {
			return true;
		}
		return false;
	}
}
