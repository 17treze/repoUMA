package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.InfoGeneraliDomanda;
import it.tndigitale.a4g.ags.utils.DecodificaSettorePAC;

import static it.tndigitale.a4g.ags.model.StatoDomanda.from;

public class InfoGeneraliRowMapper implements RowMapper {

	public InfoGeneraliRowMapper() {
		super();
	}

	public InfoGeneraliRowMapper(DecodificaSettorePAC decodificaSettorePAC) {
		super();
		this.decodificaSettorePAC = decodificaSettorePAC;
	}

	private DecodificaSettorePAC decodificaSettorePAC;

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

		DecodificaSettorePAC.PACKey pacKey = decodificaSettorePAC.getValori().get(rs.getString("SETTORE"));

		InfoGeneraliDomanda infoGeneraliDomanda = new InfoGeneraliDomanda();
		infoGeneraliDomanda.setPac(pacKey.getPac());
		infoGeneraliDomanda.setTipoDomanda(pacKey.getTipoDomanda());
		infoGeneraliDomanda.setCampagna(rs.getBigDecimal("CAMPAGNA"));
		infoGeneraliDomanda.setModulo(rs.getString("MODULO_DOMANDA"));
		infoGeneraliDomanda.setCodModulo(rs.getString("COD_MODULO"));
		infoGeneraliDomanda.setCuaaIntestatario(rs.getString("CUAA"));
		infoGeneraliDomanda.setRagioneSociale(rs.getString("RAGIONE_SOCIALE"));
		infoGeneraliDomanda.setNumeroDomanda(rs.getLong("ID_DOMANDA"));
		infoGeneraliDomanda.setDataPresentazione(rs.getDate("DATA_PRESENTAZIONE"));
		infoGeneraliDomanda.setNumeroDomandaRettificata(rs.getLong("ID_DOMANDA_RETTIFICATA") == 0 ? null : rs.getLong("ID_DOMANDA_RETTIFICATA"));
		infoGeneraliDomanda.setCodEnteCompilatore(rs.getString("COD_ENTE"));
		infoGeneraliDomanda.setEnteCompilatore(rs.getString("ENTE"));
		infoGeneraliDomanda.setDataProtocollazione(rs.getDate("DATA_PROTOCOLLAZIONE"));
		infoGeneraliDomanda.setDataPresentazOriginaria(rs.getDate("DATA_PRESEN_DOM_RETTIFICATA") == null ? null : rs.getDate("DATA_PRESEN_DOM_RETTIFICATA"));
		infoGeneraliDomanda.setDataProtocollazOriginaria(rs.getDate("DATA_PROTOC_DOM_RETTIFICATA") == null ? null : rs.getDate("DATA_PROTOC_DOM_RETTIFICATA"));
		infoGeneraliDomanda.setDataPassaggioStato(rs.getDate("DATA_PASSAGGIO_STATO") == null ? null : rs.getDate("DATA_PASSAGGIO_STATO"));
		String dbValueStato = rs.getString("STATO_DOMANDA");
		infoGeneraliDomanda.setStato(from(dbValueStato));
		infoGeneraliDomanda.setDtProtocollazioneUltimaModifica(rs.getDate("DT_PROTOCOLLAZ_ULT_MODIFICA"));
		infoGeneraliDomanda.setNumeroDomandaUltimaModifica(rs.getBigDecimal("NUMERO_DOMANDA_ULT_MODIFICA"));

		return infoGeneraliDomanda;
	}

}
