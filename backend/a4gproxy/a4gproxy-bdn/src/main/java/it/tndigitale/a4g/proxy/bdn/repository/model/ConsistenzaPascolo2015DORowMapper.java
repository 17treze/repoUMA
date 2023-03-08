package it.tndigitale.a4g.proxy.bdn.repository.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.proxy.bdn.dto.ConsistenzaAlPascoloPAC2015DO;

public class ConsistenzaPascolo2015DORowMapper  implements RowMapper {

	@Override
	public ConsistenzaAlPascoloPAC2015DO mapRow(ResultSet row, int rowNum) throws SQLException {
		
		ConsistenzaAlPascoloPAC2015DO foundOnDB = new ConsistenzaAlPascoloPAC2015DO();

		foundOnDB.setIdCpws(row.getBigDecimal("ID_CPWS") == null ? 0 : (row.getBigDecimal("ID_CPWS")).longValue());
		foundOnDB.setCodiFiscSogg(row.getString("CODI_FISC_SOGG"));
		foundOnDB.setNumeCamp(row.getBigDecimal("NUME_CAMP") == null ? 0 : (row.getBigDecimal("NUME_CAMP")).longValue());
		foundOnDB.setCodiPasc(row.getString("CODI_PASC"));
		foundOnDB.setFascEtaa(row.getString("FASC_ETAA"));
		foundOnDB.setNumeCapi(row.getBigDecimal("NUME_CAPI") == null ? 0.0 : (row.getBigDecimal("NUME_CAPI")).longValue());
		foundOnDB.setNumeCapiMedi(row.getBigDecimal("NUME_CAPI_MEDI") == null ? 0.0 : (row.getBigDecimal("NUME_CAPI_MEDI")).longValue());
		foundOnDB.setGiorPasc(row.getBigDecimal("GIOR_PASC") == null ? 0 : (row.getBigDecimal("GIOR_PASC")).longValue());
		foundOnDB.setCodiGrupSpec(row.getString("CODI_GRUP_SPEC"));
		foundOnDB.setCodiSpec(row.getString("CODI_SPEC"));
		foundOnDB.setDescSpec(row.getString("DESC_SPEC"));
		foundOnDB.setCoorLati(row.getBigDecimal("COOR_LATI") == null ? 0 : (row.getBigDecimal("COOR_LATI")).longValue());
		foundOnDB.setCoorLong(row.getBigDecimal("COOR_LONG") == null ? 0 : (row.getBigDecimal("COOR_LONG")).longValue());
		foundOnDB.setNumeFogl(row.getBigDecimal("NUME_FOGL") == null ? -1 : (row.getBigDecimal("NUME_FOGL")).longValue());
		foundOnDB.setNumePart(row.getString("NUME_PART"));
		foundOnDB.setCodiSezi(row.getString("CODI_SEZI"));
		foundOnDB.setCodiSuba(row.getString("CODI_SUBA"));
		foundOnDB.setCodiSiglProv(row.getString("CODI_SIGL_PROV"));
		foundOnDB.setCodiProv(row.getString("CODI_PROV"));
		foundOnDB.setCodiComu(row.getString("CODI_COMU"));
		foundOnDB.setDescLoca(row.getString("DESC_LOCA"));
		foundOnDB.setCodiFiscResp(row.getString("CODI_FISC_RESP"));
		foundOnDB.setDecoStat(row.getBigDecimal("DECO_STAT") == null ? 0 : (row.getBigDecimal("DECO_STAT")).longValue());
		foundOnDB.setDataIniz(row.getTimestamp("DATA_INIZ"));
		foundOnDB.setDataFine(row.getTimestamp("DATA_FINE"));
		foundOnDB.setDataAggi(row.getTimestamp("DATA_AGGI"));
		foundOnDB.setUserName(row.getString("USER_NAME"));
		
		return foundOnDB;
	}

}
