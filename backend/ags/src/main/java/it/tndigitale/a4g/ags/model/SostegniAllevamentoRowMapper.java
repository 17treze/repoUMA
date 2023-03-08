package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.domandaunica.SostegniAllevamento;

public class SostegniAllevamentoRowMapper implements RowMapper<SostegniAllevamento> {

	public SostegniAllevamentoRowMapper() {
		super();
	}

	public SostegniAllevamento mapRow(ResultSet rs, int rowNum) throws SQLException {
		SostegniAllevamento sostegniAllevamento = new SostegniAllevamento();

		sostegniAllevamento.setSostegno(rs.getString("SOSTEGNO"));
		sostegniAllevamento.setIdIntervento(rs.getLong("ID_INTERVENTO"));
		sostegniAllevamento.setCodIntervento(rs.getString("COD_INTERVENTO"));
		sostegniAllevamento.setDescIntervento(rs.getString("DS_INTERVENTO"));
		sostegniAllevamento.setIdAllevamento(rs.getLong("ID_ALLEVAMENTO"));
		sostegniAllevamento.setCodIdAllevamento(rs.getString("COD_ID_AZIENDALE"));
		sostegniAllevamento.setCodIdBdn(rs.getString("COD_ID_BDN"));
		sostegniAllevamento.setDescAllevamento(rs.getString("DE_ALLEVAMENTO"));
		sostegniAllevamento.setSpecie(rs.getString("SPECIE"));
		sostegniAllevamento.setComune(rs.getString("COMUNE"));
		sostegniAllevamento.setIndirizzo(rs.getString("INDIRIZZO"));
		sostegniAllevamento.setCodFiscaleProprietario(rs.getString("COD_FISC_PROPR"));
		sostegniAllevamento.setDenominazioneProprietario(rs.getString("DENOM_PROPR"));
		sostegniAllevamento.setCodFiscaleDetentore(rs.getString("COD_FISC_DETENT"));
		sostegniAllevamento.setDenominazioneDetentore(rs.getString("DENOM_DETENT"));
		return sostegniAllevamento;

	}

}
