package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.SostegniSuperficie;
import it.tndigitale.a4g.ags.dto.domandaunica.Particella;

public class SostegniSuperficiRowMapper implements RowMapper {

	private static final Logger logger = LoggerFactory.getLogger(SostegniSuperficiRowMapper.class);

	public SostegniSuperficiRowMapper() {
		super();
	}

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

		SostegniSuperficie sostegnoSuperficie = new SostegniSuperficie();
		Particella particella = new Particella();
		sostegnoSuperficie.setSostegno(rs.getString("ID_GRUPPO"));
		sostegnoSuperficie.setIdIntervento(rs.getLong("ID_DESTINAZIONE"));
		sostegnoSuperficie.setCodIntervento(rs.getString("COD_DESTINAZIONE").equalsIgnoreCase("BPS1") ? "BPS" : rs.getString("COD_DESTINAZIONE"));
		sostegnoSuperficie.setDescIntervento(rs.getString("DS_DESTINAZIONE"));

		particella.setIdParticella(rs.getLong("ID_PARTICELLA"));
		particella.setComune(rs.getString("COMUNE"));
		particella.setCodNazionale(rs.getString("COD_NAZIONALE"));
		particella.setFoglio(rs.getLong("FOGLIO"));
		particella.setParticella(rs.getString("PARTICELLA"));
		particella.setSub(rs.getString("SUB"));
		sostegnoSuperficie.setParticella(particella);

		sostegnoSuperficie.setIdPianoColture(rs.getLong("ID_PIANO_COLTURE"));
		sostegnoSuperficie.setSupDichiarata(rs.getLong("SUP_DICHIARATA"));
		sostegnoSuperficie.setIdParcella(rs.getLong("ID_PARC"));
		sostegnoSuperficie.setIdIsola(rs.getLong("ID_ISOLA"));
		sostegnoSuperficie.setCodIsola(rs.getString("COD_ISOLA"));
		sostegnoSuperficie.setIdColtura(rs.getLong("ID_COLTURA"));
		sostegnoSuperficie.setCodColtura3(rs.getString("EXT_CODE"));
		sostegnoSuperficie.setDescColtura(rs.getString("DE_COLTURA"));
		sostegnoSuperficie.setCoeffTara(rs.getFloat("COEFF_TARA"));
		sostegnoSuperficie.setCodColtura5(rs.getString("COL_COLT5"));
		sostegnoSuperficie.setCodLivello(rs.getString("COD_LIVELLO"));
		sostegnoSuperficie.setSupImpegnata(rs.getLong("SUP_IMPEGNO"));
		sostegnoSuperficie.setDescMantenimento(rs.getString("DESC_MANTENIMENTO"));
		return sostegnoSuperficie;
	}

}
