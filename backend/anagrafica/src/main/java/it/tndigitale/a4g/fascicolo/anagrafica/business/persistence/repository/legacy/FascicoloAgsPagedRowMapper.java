package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.FascicoloAgsPagedRowMapper.FascicoloAgsPagedRow;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.StatoFascicoloLegacy;

public class FascicoloAgsPagedRowMapper implements RowMapper<FascicoloAgsPagedRow> {

	@Override
	public FascicoloAgsPagedRow mapRow(ResultSet rs, int rowNum) throws SQLException {
		FascicoloAgsPagedRow fascicoloAgsPagedRow = new FascicoloAgsPagedRow();

		String statoLegacy = rs.getString("STATO");

		if (statoLegacy.equalsIgnoreCase("VALIDO")) {
			fascicoloAgsPagedRow.setStato(StatoFascicoloLegacy.VALIDO);
		} else if (statoLegacy.equalsIgnoreCase("AGGIOR")) {
			fascicoloAgsPagedRow.setStato(StatoFascicoloLegacy.IN_LAVORAZIONE);
		} else if (statoLegacy.equalsIgnoreCase("CESSAT")) {
			fascicoloAgsPagedRow.setStato(StatoFascicoloLegacy.CESSATO);
		} else if (statoLegacy.equalsIgnoreCase("CHIUSO")) {
			fascicoloAgsPagedRow.setStato(StatoFascicoloLegacy.CHIUSO);
		} else if (statoLegacy.equalsIgnoreCase("ANOMAL")) {
			fascicoloAgsPagedRow.setStato(StatoFascicoloLegacy.IN_ANOMALIA);
		}

		fascicoloAgsPagedRow.setIdAgs(rs.getLong("ID"));
		fascicoloAgsPagedRow.setCuaa(rs.getString("CUAA"));
		fascicoloAgsPagedRow.setDenominazione(rs.getString("DENOMINAZIONE"));
		fascicoloAgsPagedRow.setOrganismoPagatore(rs.getString("ORGANISMO_PAGATORE"));
		fascicoloAgsPagedRow.setCaa(rs.getString("CAA"));
		fascicoloAgsPagedRow.setSportello(rs.getString("SPORTELLO"));
		fascicoloAgsPagedRow.setIdentificativoSportello(rs.getLong("IDENTIFICATIVO_SPORTELLO"));
		fascicoloAgsPagedRow.setDataCostituzione(rs.getTimestamp("DATA_COSTITUZIONE").toLocalDateTime());
		fascicoloAgsPagedRow.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO").toLocalDateTime());
		fascicoloAgsPagedRow.setDataValidazione(rs.getTimestamp("DATA_VALIDAZIONE").toLocalDateTime());
		fascicoloAgsPagedRow.setIscrittoSezioneSpecialeAgricola("S".equals(rs.getString("SEZIONE")));
		fascicoloAgsPagedRow.setNonIscrittoSezioneSpecialeAgricola("S".equals(rs.getString("UMA_NO_SEZIONE")));
		fascicoloAgsPagedRow.setPec(rs.getString("PEC"));
		fascicoloAgsPagedRow.setTotale(rs.getInt("TOTALE"));
		return fascicoloAgsPagedRow;
	}

	protected class FascicoloAgsPagedRow {

		private Long idAgs;
		private String cuaa;
		private String denominazione;
		private StatoFascicoloLegacy stato;
		private String organismoPagatore;
		private String caa;
		private String sportello;
		private Long identificativoSportello;
		private LocalDateTime dataCostituzione;
		private LocalDateTime dataAggiornamento;
		private LocalDateTime dataValidazione;
		private boolean iscrittoSezioneSpecialeAgricola;
		private boolean nonIscrittoSezioneSpecialeAgricola;
		private String pec;
		private Integer totale;

		public Integer getTotale() {
			return totale;
		}
		public FascicoloAgsPagedRow setTotale(Integer totale) {
			this.totale = totale;
			return this;
		}
		public Long getIdAgs() {
			return idAgs;
		}
		public FascicoloAgsPagedRow setIdAgs(Long idAgs) {
			this.idAgs = idAgs;
			return this;
		}
		public String getCuaa() {
			return cuaa;
		}
		public FascicoloAgsPagedRow setCuaa(String cuaa) {
			this.cuaa = cuaa;
			return this;
		}
		public String getDenominazione() {
			return denominazione;
		}
		public FascicoloAgsPagedRow setDenominazione(String denominazione) {
			this.denominazione = denominazione;
			return this;
		}
		public StatoFascicoloLegacy getStato() {
			return stato;
		}
		public FascicoloAgsPagedRow setStato(StatoFascicoloLegacy stato) {
			this.stato = stato;
			return this;
		}
		public String getOrganismoPagatore() {
			return organismoPagatore;
		}
		public FascicoloAgsPagedRow setOrganismoPagatore(String organismoPagatore) {
			this.organismoPagatore = organismoPagatore;
			return this;
		}
		public String getCaa() {
			return caa;
		}
		public FascicoloAgsPagedRow setCaa(String caa) {
			this.caa = caa;
			return this;
		}
		public String getSportello() {
			return sportello;
		}
		public FascicoloAgsPagedRow setSportello(String sportello) {
			this.sportello = sportello;
			return this;
		}
		public Long getIdentificativoSportello() {
			return identificativoSportello;
		}
		public FascicoloAgsPagedRow setIdentificativoSportello(Long identificativoSportello) {
			this.identificativoSportello = identificativoSportello;
			return this;
		}
		public LocalDateTime getDataCostituzione() {
			return dataCostituzione;
		}
		public FascicoloAgsPagedRow setDataCostituzione(LocalDateTime dataCostituzione) {
			this.dataCostituzione = dataCostituzione;
			return this;
		}
		public LocalDateTime getDataAggiornamento() {
			return dataAggiornamento;
		}
		public FascicoloAgsPagedRow setDataAggiornamento(LocalDateTime dataAggiornamento) {
			this.dataAggiornamento = dataAggiornamento;
			return this;
		}
		public LocalDateTime getDataValidazione() {
			return dataValidazione;
		}
		public FascicoloAgsPagedRow setDataValidazione(LocalDateTime dataValidazione) {
			this.dataValidazione = dataValidazione;
			return this;
		}
		public boolean isIscrittoSezioneSpecialeAgricola() {
			return iscrittoSezioneSpecialeAgricola;
		}
		public FascicoloAgsPagedRow setIscrittoSezioneSpecialeAgricola(boolean iscrittoSezioneSpecialeAgricola) {
			this.iscrittoSezioneSpecialeAgricola = iscrittoSezioneSpecialeAgricola;
			return this;
		}
		public boolean isNonIscrittoSezioneSpecialeAgricola() {
			return nonIscrittoSezioneSpecialeAgricola;
		}
		public FascicoloAgsPagedRow setNonIscrittoSezioneSpecialeAgricola(boolean nonIscrittoSezioneSpecialeAgricola) {
			this.nonIscrittoSezioneSpecialeAgricola = nonIscrittoSezioneSpecialeAgricola;
			return this;
		}
		public String getPec() {
			return pec;
		}
		public FascicoloAgsPagedRow setPec(String pec) {
			this.pec = pec;
			return this;
		}
	}

}
