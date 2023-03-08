package it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class SuoloNonAssociabileLavorazioneDto implements Serializable {

	private static final long serialVersionUID = 376716733790598407L;

	private SuoloVigente suoloVigente;

	private List<Long> idSuoloDichiarato;

	public void setIdSuoloDichiarato(List<Long> idSuoloDichiarato) {
		this.idSuoloDichiarato = idSuoloDichiarato;
	}

	public List<Long> getIdSuoloDichiarato() {
		return idSuoloDichiarato;
	}

	public SuoloVigente getSuoloVigente() {
		return suoloVigente;
	}

	public void setSuoloVigente(SuoloVigente suoloVigente) {
		this.suoloVigente = suoloVigente;
	}

	public SuoloVigente newInstance() {
		this.suoloVigente = new SuoloVigente();
		return this.suoloVigente;
	}

	public class SuoloVigente {
		private Long idSuoloVigente;
		private Long idLavorazione;
		private String utente;
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		private LocalDateTime dataUltimaLavorazione;

		public LocalDateTime getDataUltimaLavorazione() {
			return dataUltimaLavorazione;
		}

		public void setDataUltimaLavorazione(LocalDateTime dataUltimaLavorazione) {
			this.dataUltimaLavorazione = dataUltimaLavorazione;
		}

		public Long getIdSuoloVigente() {
			return idSuoloVigente;
		}

		public void setIdSuoloVigente(Long idSuoloVigente) {
			this.idSuoloVigente = idSuoloVigente;
		}

		public Long getIdLavorazione() {
			return idLavorazione;
		}

		public void setIdLavorazione(Long idLavorazione) {
			this.idLavorazione = idLavorazione;
		}

		public String getUtente() {
			return utente;
		}

		public void setUtente(String utente) {
			this.utente = utente;
		}
	}

}
