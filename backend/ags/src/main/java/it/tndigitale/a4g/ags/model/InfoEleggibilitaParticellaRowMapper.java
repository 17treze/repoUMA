package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.InfoEleggibilitaParticella;
import it.tndigitale.a4g.ags.dto.domandaunica.Particella;

public class InfoEleggibilitaParticellaRowMapper implements RowMapper {

	private static final Logger logger = LoggerFactory.getLogger(SostegniSuperficiRowMapper.class);

	public InfoEleggibilitaParticellaRowMapper() {
		super();
	}

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

		InfoEleggibilitaParticella infoElegParticella = new InfoEleggibilitaParticella();
		infoElegParticella.setSostegno(rs.getString("ID_GRUPPO"));
		infoElegParticella.setCodIntervento(rs.getString("COD_DESTINAZIONE").equalsIgnoreCase("BPS1") ? "BPS" : rs.getString("COD_DESTINAZIONE"));

		Particella particella = new Particella();
		particella.setIdParticella(rs.getLong("ID_PARTICELLA"));
		particella.setComune(rs.getString("COMUNE"));
		particella.setCodNazionale(rs.getString("COD_NAZIONALE"));
		particella.setFoglio(rs.getLong("FOGLIO"));
		particella.setParticella(rs.getString("PARTICELLA"));
		particella.setSub(rs.getString("SUB"));

		infoElegParticella.setParticella(particella);
		infoElegParticella.setCodColtura3(rs.getString("EXT_CODE"));
		infoElegParticella.setCodColtura5(rs.getString("COL_COLT5"));
		infoElegParticella.setSuperficieGis(rs.getLong("SUP_ELEGGIBILE"));
		infoElegParticella.setSuperficieSigeco(rs.getLong("SUP_SIGECO"));
		// infoElegParticella.setIdParcella(rs.getLong("ID_PARC"));
		infoElegParticella.setLivello(rs.getLong("COD_LIVELLO"));
		infoElegParticella.setSuperficieAnomaliaCoor(rs.getLong("FG_AN_COOR"));

		return infoElegParticella;
	}

}
