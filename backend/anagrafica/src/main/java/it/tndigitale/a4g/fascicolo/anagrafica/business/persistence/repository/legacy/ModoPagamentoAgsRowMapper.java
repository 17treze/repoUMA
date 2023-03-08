package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.ModoPagamentoAgsRowMapper.ModoPagamentoAgsRow;

import org.springframework.jdbc.core.RowMapper;

public class ModoPagamentoAgsRowMapper implements RowMapper<ModoPagamentoAgsRow> {

	@Override
	public ModoPagamentoAgsRow mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModoPagamentoAgsRow modoPagamentoAgsRow = new ModoPagamentoAgsRow();

		return modoPagamentoAgsRow
				.setIdAgs(rs.getLong("ID"))
				.setIban(rs.getString("COD_IBAN"))
				.setDenominazioneIstituto(rs.getString("DE_BANCA"))
				.setDenominazioneFiliale(rs.getString("DE_FILIALE"))
				.setDataInizio(rs.getTimestamp("DT_INIZIO").toLocalDateTime())
				.setDataFine(rs.getTimestamp("DT_FINE").toLocalDateTime());
	}


	protected class ModoPagamentoAgsRow {

		private Long idAgs;
		private String iban;
		private String denominazioneIstituto;
		private String denominazioneFiliale;
		private LocalDateTime dataInizio;
		private LocalDateTime dataFine;

		public Long getIdAgs() {
			return idAgs;
		}
		public ModoPagamentoAgsRow setIdAgs(Long idAgs) {
			this.idAgs = idAgs;
			return this;
		}
		public String getIban() {
			return iban;
		}
		public ModoPagamentoAgsRow setIban(String iban) {
			this.iban = iban;
			return this;
		}
		public String getDenominazioneIstituto() {
			return denominazioneIstituto;
		}
		public ModoPagamentoAgsRow setDenominazioneIstituto(String denominazioneIstituto) {
			this.denominazioneIstituto = denominazioneIstituto;
			return this;
		}
		public String getDenominazioneFiliale() {
			return denominazioneFiliale;
		}
		public ModoPagamentoAgsRow setDenominazioneFiliale(String denominazioneFiliale) {
			this.denominazioneFiliale = denominazioneFiliale;
			return this;
		}
		public LocalDateTime getDataInizio() {
			return dataInizio;
		}
		public ModoPagamentoAgsRow setDataInizio(LocalDateTime dataInizio) {
			this.dataInizio = dataInizio;
			return this;
		}
		public LocalDateTime getDataFine() {
			return dataFine;
		}
		public ModoPagamentoAgsRow setDataFine(LocalDateTime dataFine) {
			this.dataFine = dataFine;
			return this;
		}
		
	}
}
