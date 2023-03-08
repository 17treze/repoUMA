package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.FascicoloAgsRowMapper.FascicoloAgsRow;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.StatoFascicoloLegacy;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.TipoDetenzioneAgs;

public class FascicoloAgsRowMapper implements RowMapper<FascicoloAgsRow> {

	@Override
	public FascicoloAgsRow mapRow(ResultSet rs, int rowNum) throws SQLException {
		FascicoloAgsRow fascicoloAgsRow = new FascicoloAgsRow();

		String statoLegacy = rs.getString("STATO");

		if (statoLegacy.equalsIgnoreCase("VALIDO")) {
			fascicoloAgsRow.setStato(StatoFascicoloLegacy.VALIDO);
		} else if (statoLegacy.equalsIgnoreCase("AGGIOR")) {
			fascicoloAgsRow.setStato(StatoFascicoloLegacy.IN_LAVORAZIONE);
		} else if (statoLegacy.equalsIgnoreCase("CESSAT")) {
			fascicoloAgsRow.setStato(StatoFascicoloLegacy.CESSATO);
		} else if (statoLegacy.equalsIgnoreCase("CHIUSO")) {
			fascicoloAgsRow.setStato(StatoFascicoloLegacy.CHIUSO);
		} else if (statoLegacy.equalsIgnoreCase("ANOMAL")) {
			fascicoloAgsRow.setStato(StatoFascicoloLegacy.IN_ANOMALIA);
		}
		String tipoAssociazione = rs.getString("TIPO_ASSOCIAZIONE");
		if (tipoAssociazione.equalsIgnoreCase("MAN")) {
			fascicoloAgsRow.setTipoDetenzione(TipoDetenzioneAgs.MANDATO);
		} else if (tipoAssociazione.equalsIgnoreCase("DEL")) {
			fascicoloAgsRow.setTipoDetenzione(TipoDetenzioneAgs.DELEGA);
		}

		return fascicoloAgsRow
				.setIdAgs(rs.getLong("ID"))
				.setCuaa(rs.getString("CUAA"))
				.setDenominazione(rs.getString("DENOMINAZIONE"))
				.setDataMorte(rs.getTimestamp("DATA_MORTE") != null ? rs.getTimestamp("DATA_MORTE").toLocalDateTime() : null)
				.setOrganismoPagatore(rs.getString("ORGANISMO_PAGATORE"))
				.setCaa(rs.getString("CAA"))
				.setSportello(rs.getString("SPORTELLO"))
				.setIdentificativoSportello(rs.getLong("IDENTIFICATIVO_SPORTELLO"))
				.setDataCostituzione(rs.getTimestamp("DATA_COSTITUZIONE").toLocalDateTime())
				.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO").toLocalDateTime())
				.setDataValidazione(rs.getTimestamp("DATA_VALIDAZIONE") != null ? rs.getTimestamp("DATA_VALIDAZIONE").toLocalDateTime(): null)
				.setDataInizioDetenzione(rs.getTimestamp("DATA_INIZIO_DETENZIONE") != null ? rs.getTimestamp("DATA_INIZIO_DETENZIONE").toLocalDateTime(): null)
				.setDataFineDetenzione(rs.getTimestamp("DATA_FINE_DETENZIONE") != null ? rs.getTimestamp("DATA_FINE_DETENZIONE").toLocalDateTime(): null)
				.setIscrittoSezioneSpecialeAgricola("S".equals(rs.getString("SEZIONE")))
				.setNonIscrittoSezioneSpecialeAgricola("S".equals(rs.getString("UMA_NO_SEZIONE")))
				.setPec(rs.getString("PEC"));
	}


	protected class FascicoloAgsRow {

		private Long idAgs;
		private String cuaa;
		private String denominazione;
		private LocalDateTime dataMorte;
		private StatoFascicoloLegacy stato;
		private String organismoPagatore;
		private String caa;
		private LocalDateTime dataInizioDetenzione;
		private LocalDateTime dataFineDetenzione;
		private TipoDetenzioneAgs tipoDetenzione;
		private String sportello;
		private Long identificativoSportello;
		private LocalDateTime dataCostituzione;
		private LocalDateTime dataAggiornamento;
		private LocalDateTime dataValidazione;
		private boolean iscrittoSezioneSpecialeAgricola;
		private boolean nonIscrittoSezioneSpecialeAgricola;
		private String pec;

		public Long getIdAgs() {
			return idAgs;
		}
		public FascicoloAgsRow setIdAgs(Long idAgs) {
			this.idAgs = idAgs;
			return this;
		}
		public String getCuaa() {
			return cuaa;
		}
		public FascicoloAgsRow setCuaa(String cuaa) {
			this.cuaa = cuaa;
			return this;
		}
		public String getDenominazione() {
			return denominazione;
		}
		public FascicoloAgsRow setDenominazione(String denominazione) {
			this.denominazione = denominazione;
			return this;
		}
		public StatoFascicoloLegacy getStato() {
			return stato;
		}
		public FascicoloAgsRow setStato(StatoFascicoloLegacy stato) {
			this.stato = stato;
			return this;
		}
		public String getOrganismoPagatore() {
			return organismoPagatore;
		}
		public FascicoloAgsRow setOrganismoPagatore(String organismoPagatore) {
			this.organismoPagatore = organismoPagatore;
			return this;
		}
		public String getCaa() {
			return caa;
		}
		public FascicoloAgsRow setCaa(String caa) {
			this.caa = caa;
			return this;
		}
		public TipoDetenzioneAgs getTipoDetenzione() {
			return tipoDetenzione;
		}
		public FascicoloAgsRow setTipoDetenzione(TipoDetenzioneAgs tipoDetenzione) {
			this.tipoDetenzione = tipoDetenzione;
			return this;
		}
		public String getSportello() {
			return sportello;
		}
		public FascicoloAgsRow setSportello(String sportello) {
			this.sportello = sportello;
			return this;
		}
		public Long getIdentificativoSportello() {
			return identificativoSportello;
		}
		public FascicoloAgsRow setIdentificativoSportello(Long identificativoSportello) {
			this.identificativoSportello = identificativoSportello;
			return this;
		}
		public LocalDateTime getDataInizioDetenzione() {
			return dataInizioDetenzione;
		}
		public FascicoloAgsRow setDataInizioDetenzione(LocalDateTime dataInizioDetenzione) {
			this.dataInizioDetenzione = dataInizioDetenzione;
			return this;
		}
		public LocalDateTime getDataFineDetenzione() {
			return dataFineDetenzione;
		}
		public FascicoloAgsRow setDataFineDetenzione(LocalDateTime dataFineDetenzione) {
			this.dataFineDetenzione = dataFineDetenzione;
			return this;
		}
		public LocalDateTime getDataCostituzione() {
			return dataCostituzione;
		}
		public FascicoloAgsRow setDataCostituzione(LocalDateTime dataCostituzione) {
			this.dataCostituzione = dataCostituzione;
			return this;
		}
		public LocalDateTime getDataAggiornamento() {
			return dataAggiornamento;
		}
		public FascicoloAgsRow setDataAggiornamento(LocalDateTime dataAggiornamento) {
			this.dataAggiornamento = dataAggiornamento;
			return this;
		}
		public LocalDateTime getDataValidazione() {
			return dataValidazione;
		}
		public FascicoloAgsRow setDataValidazione(LocalDateTime dataValidazione) {
			this.dataValidazione = dataValidazione;
			return this;
		}
		public boolean isIscrittoSezioneSpecialeAgricola() {
			return iscrittoSezioneSpecialeAgricola;
		}
		public FascicoloAgsRow setIscrittoSezioneSpecialeAgricola(boolean iscrittoSezioneSpecialeAgricola) {
			this.iscrittoSezioneSpecialeAgricola = iscrittoSezioneSpecialeAgricola;
			return this;
		}
		public boolean isNonIscrittoSezioneSpecialeAgricola() {
			return nonIscrittoSezioneSpecialeAgricola;
		}
		public FascicoloAgsRow setNonIscrittoSezioneSpecialeAgricola(boolean nonIscrittoSezioneSpecialeAgricola) {
			this.nonIscrittoSezioneSpecialeAgricola = nonIscrittoSezioneSpecialeAgricola;
			return this;
		}
		public String getPec() {
			return pec;
		}
		public FascicoloAgsRow setPec(String pec) {
			this.pec = pec;
			return this;
		}
		public LocalDateTime getDataMorte() {
			return dataMorte;
		}
		public FascicoloAgsRow setDataMorte(LocalDateTime dataMorte) {
			this.dataMorte = dataMorte;
			return this;
		}
	}
}
