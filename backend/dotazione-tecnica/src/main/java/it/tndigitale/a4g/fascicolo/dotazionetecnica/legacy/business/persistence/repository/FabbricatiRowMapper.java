package it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.business.persistence.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.FabbricatoAgsDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.TitoloConduzioneAgs;

public class FabbricatiRowMapper implements RowMapper<FabbricatoAgsDto> {

	@Override
	public FabbricatoAgsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		FabbricatoAgsDto dto = new FabbricatoAgsDto()
				.setIdAgs(rs.getLong("ID_FABBRICATO"))
				.setVolume(rs.getInt("VOLUME"))
				.setSuperficie(rs.getInt("SUPERFICIE"))
				.setComuneCatastale(rs.getString("COMUNE_CATASTALE"))
				.setSezione(rs.getString("SEZIONE"))
				.setFoglio(rs.getInt("FOGLIO"))
				.setDescrizione(rs.getString("DESCRIZIONE"))
				.setTipoFabbricatoCodice(rs.getString("CODICE_FABBRICATO"))
				.setParticella(remove_zeros(rs.getString("PARTICELLA").replace(".", "")))
				.setComune(rs.getString("COMUNE"))
				.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"))
				.setProvincia(rs.getString("PROVINCIA"))
				.setTipoFabbricato(rs.getString("TIPO_FABBRICATO"))
				.setTitoloConduzione(rs.getString("TITOLO_CONDUZIONE"))
				.setNote(rs.getString("NOTE"));

		String subalterno = rs.getString("SUBALTERNO");

		if (StringUtils.isEmpty(subalterno)) {
			subalterno = null;
		}

		dto.setSubalterno(subalterno);

		final String codiceTitoloConduzioneColumn = "CODICE_TITOLO_CONDUZIONE";
		final String codiceTitoloConduzione = rs.getString(codiceTitoloConduzioneColumn);

		if (codiceTitoloConduzione.equals("000000")) {
			dto.setTitoloConduzione(TitoloConduzioneAgs.NON_DEFINITO.value);
		}
		if (codiceTitoloConduzione.equals("000001")) {
			dto.setTitoloConduzione(TitoloConduzioneAgs.PROPRIETA.value);
		}
		if (codiceTitoloConduzione.equals("000002")) {
			dto.setTitoloConduzione(TitoloConduzioneAgs.AFFITTO.value);
		}
		if (codiceTitoloConduzione.equals("000003")) {
			dto.setTitoloConduzione(TitoloConduzioneAgs.MEZZADRIA.value);
		}
		if (codiceTitoloConduzione.equals("000004")) {
			dto.setTitoloConduzione(TitoloConduzioneAgs.ALTRE_FORME.value);
		}
		if (codiceTitoloConduzione.equals("000005")) {
			dto.setTitoloConduzione(TitoloConduzioneAgs.NON_DEFINITO.value);
		}
		if (codiceTitoloConduzione.equals("000006")) {
			dto.setTitoloConduzione(TitoloConduzioneAgs.NON_DEFINITO.value);
		}
		if (codiceTitoloConduzione.equals("000007")) {
			dto.setTitoloConduzione(TitoloConduzioneAgs.NON_DEFINITO.value);
		}
		if (codiceTitoloConduzione.equals("000008")) {
			dto.setTitoloConduzione(TitoloConduzioneAgs.NON_DEFINITO.value);
		}
		if (codiceTitoloConduzione.equals("000009")) {
			dto.setTitoloConduzione(TitoloConduzioneAgs.NON_DEFINITO.value);
		}
		if (codiceTitoloConduzione.equals("000010")) {
			dto.setTitoloConduzione(TitoloConduzioneAgs.COMODATO.value);
		}
		if (codiceTitoloConduzione.equals("000999")) {
			dto.setTitoloConduzione(TitoloConduzioneAgs.NON_CONDOTTA.value);
		}

		return dto;
	}

	public String remove_zeros(String str) {
		if (str.length() > 0) {
			if (str.charAt(0)=='0') {
				return remove_zeros(str.substring(1));
			}
		}
		return str;
	}

}
