package it.tndigitale.a4g.proxy.bdn.repository.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.tndigitale.a4g.proxy.bdn.dto.istruttoria.ConsistenzaPascolo2015Dto;
import org.springframework.jdbc.core.RowMapper;

public class ConsistenzaPascolo2015RowMapper implements RowMapper {

	public ConsistenzaPascolo2015RowMapper() {
		super();
	}

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

		ConsistenzaPascolo2015Dto consistenzaPascolo = new ConsistenzaPascolo2015Dto();
		consistenzaPascolo.setAnnoCampagna(rs.getBigDecimal("NUME_CAMP"));
		consistenzaPascolo.setCodiceFiscaleResponsabile(rs.getString("CODI_FISC_RESP"));
		consistenzaPascolo.setCodiceFiscaleSoggetto(rs.getString("CODI_FISC_SOGG"));
		consistenzaPascolo.setCodicePascolo(rs.getString("CODI_PASC"));
		consistenzaPascolo.setFasciaEta(rs.getString("FASC_ETAA"));
		consistenzaPascolo.setGiorniAlPascolo(rs.getBigDecimal("GIOR_PASC"));
		consistenzaPascolo.setNumeroCapi(rs.getBigDecimal("NUME_CAPI"));
		consistenzaPascolo.setNumeroCapiMedi(rs.getBigDecimal("NUME_CAPI_MEDI"));
		consistenzaPascolo.setProvincia(rs.getString("CODI_SIGL_PROV"));
		consistenzaPascolo.setSpecie(rs.getString("DESC_SPEC"));

		return consistenzaPascolo;
	}

}
