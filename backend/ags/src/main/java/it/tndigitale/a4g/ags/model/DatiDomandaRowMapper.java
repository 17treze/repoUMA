package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.domandaunica.DatiDomanda;
import it.tndigitale.a4g.ags.dto.domandaunica.DatiDomanda.TipoModulo;

import static it.tndigitale.a4g.ags.model.StatoDomanda.from;

public class DatiDomandaRowMapper implements RowMapper<DatiDomanda> {

	public DatiDomandaRowMapper() {
		super();
	}

	public DatiDomanda mapRow(ResultSet rs, int rowNum) throws SQLException {

		DatiDomanda domanda = new DatiDomanda();
		domanda.setCampagna(rs.getInt("CAMPAGNA"));
		String codModulo = rs.getString("COD_MODULO");
		TipoModulo modulo = null;
		if (codModulo.startsWith("BPS_RITTOT")) {
			modulo = TipoModulo.RITIRO_TOTALE;
		}
		if (codModulo.startsWith("BPS_RITPARZ")) {
			modulo = TipoModulo.RITIRO_PARZIALE;
		}
		domanda.setModulo(modulo);
		domanda.setCuaaIntestatario(rs.getString("CUAA"));
		domanda.setRagioneSociale(rs.getString("RAGIONE_SOCIALE"));
		domanda.setNumeroDomanda(rs.getLong("ID_DOMANDA"));
		domanda.setDataPresentazione(rs.getTimestamp("DATA_PRESENTAZIONE").toLocalDateTime());
		domanda.setNumeroDomandaRettificata(rs.getLong("ID_DOMANDA_RETTIFICATA") == 0 ? null : rs.getLong("ID_DOMANDA_RETTIFICATA"));
		domanda.setCodEnteCompilatore(rs.getString("COD_ENTE"));
		domanda.setEnteCompilatore(rs.getString("ENTE"));
		domanda.setDataProtocollazione(rs.getDate("DATA_PROTOCOLLAZIONE").toLocalDate().atStartOfDay());
		domanda.setDataPresentazioneOriginaria(rs.getDate("DATA_PRESEN_DOM_RETTIFICATA") == null ? null : rs.getTimestamp("DATA_PRESEN_DOM_RETTIFICATA").toLocalDateTime());
		domanda.setDataProtocollazioneOriginaria(rs.getDate("DATA_PROTOC_DOM_RETTIFICATA") == null ? null : rs.getTimestamp("DATA_PROTOC_DOM_RETTIFICATA").toLocalDateTime());
		domanda.setDataPassaggioStato(rs.getDate("DATA_PASSAGGIO_STATO") == null ? null : rs.getTimestamp("DATA_PASSAGGIO_STATO").toLocalDateTime());
		String dbValueStato = rs.getString("STATO_DOMANDA");
		domanda.setStato(from(dbValueStato));
		domanda.setDtProtocollazUltModifica(rs.getDate("DT_PROTOCOLLAZ_ULT_MODIFICA").toLocalDate().atStartOfDay());
		domanda.setNumeroDomandaUltModifica(rs.getBigDecimal("NUMERO_DOMANDA_ULT_MODIFICA"));
		return domanda;
	}

}
