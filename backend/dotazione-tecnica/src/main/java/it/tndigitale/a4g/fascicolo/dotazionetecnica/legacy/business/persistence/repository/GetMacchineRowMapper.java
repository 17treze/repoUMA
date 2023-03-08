package it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.business.persistence.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.MacchinaAgsDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.TipoCarburante;

public class GetMacchineRowMapper implements RowMapper<MacchinaAgsDto> {

	public GetMacchineRowMapper() {
		super();
	}

	@Override
	public MacchinaAgsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		MacchinaAgsDto macchina = new MacchinaAgsDto();
		macchina.setIdAgs(rs.getLong("ID_MACCHINA"));
		macchina.setTarga(rs.getString("targa"));
		macchina.setDescrizione(rs.getString("descrizione"));
		macchina.setMarca(rs.getString("marca"));
		macchina.setPossesso(rs.getString("possesso"));
		macchina.setAlimentazione(TipoCarburante.customValueOf((rs.getString("alimentazione"))));
		macchina.setClasse(rs.getString("classe"));
		macchina.setSottoClasse(rs.getString("sottoclasse"));
		macchina.setCodiceClasse(rs.getString("codiceclasse"));
		macchina.setCodiceSottoClasse(rs.getString("codicesottoclasse"));
		macchina.setTelaio(rs.getString("TELAIO"));
		macchina.setMatricola(rs.getString("MATRICOLA"));
		macchina.setTipoMotore(rs.getString("TIPO_MOTORE"));
		macchina.setMarcaMotore(rs.getString("MARCA_MOTORE"));
		macchina.setPotenzaKw(rs.getDouble("POTENZA_KW"));
		macchina.setIdMacchina(rs.getLong("ID_MACCHINA"));
		macchina.setIdTipoMacchina(rs.getLong("ID_TIPO_MACCHINA"));
		return macchina;
	}

}
