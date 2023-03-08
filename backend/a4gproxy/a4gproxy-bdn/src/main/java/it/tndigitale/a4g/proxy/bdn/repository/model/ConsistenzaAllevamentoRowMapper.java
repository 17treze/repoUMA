package it.tndigitale.a4g.proxy.bdn.repository.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.tndigitale.a4g.proxy.bdn.dto.ConsistenzaAllevamentoDO;
import org.springframework.jdbc.core.RowMapper;

public class ConsistenzaAllevamentoRowMapper implements RowMapper {

	public ConsistenzaAllevamentoRowMapper() {
		super();
	}

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

		ConsistenzaAllevamentoDO consistenzaAllevamento = new ConsistenzaAllevamentoDO();
		consistenzaAllevamento.setIdCaws(rs.getLong("ID_CAWS"));
		consistenzaAllevamento.setCodiFiscSogg(rs.getString("CODI_FISC_SOGG"));
		consistenzaAllevamento.setNumeCamp(rs.getLong("NUME_CAMP"));
		consistenzaAllevamento.setIdAlleBdn(rs.getLong("ID_ALLE_BDN"));
		consistenzaAllevamento.setCodiSpec(rs.getString("CODI_SPEC"));
		consistenzaAllevamento.setCodiAsll(rs.getString("CODI_ASLL"));
		consistenzaAllevamento.setCodiFiscProp(rs.getString("CODI_FISC_PROP"));
		consistenzaAllevamento.setCodiFiscDete(rs.getString("CODI_FISC_DETE"));
		consistenzaAllevamento.setConsCapi06(rs.getLong("CONS_CAPI_0_6"));
		consistenzaAllevamento.setConsCapi624(rs.getLong("CONS_CAPI_6_24"));
		consistenzaAllevamento.setConsCapiOver24(rs.getLong("CONS_CAPI_OVER_24"));
		consistenzaAllevamento.setConsTota(rs.getLong("CONS_TOTA"));
		consistenzaAllevamento.setConsVaccOver20(rs.getLong("CONS_VACC_OVER_20"));
		consistenzaAllevamento.setDataInizDete(rs.getDate("DATA_INIZ_DETE"));
		consistenzaAllevamento.setDataFineDete(rs.getDate("DATA_FINE_DETE"));
		consistenzaAllevamento.setDecoStat(rs.getLong("DECO_STAT"));
		consistenzaAllevamento.setDataIniz(rs.getDate("DATA_INIZ"));
		consistenzaAllevamento.setDataFine(rs.getDate("DATA_FINE"));
		consistenzaAllevamento.setDataAggi(rs.getDate("DATA_AGGI"));
		consistenzaAllevamento.setUserName(rs.getString("USER_NAME"));
		
		// Betty 19.06.2019: aggiunto codice comune allevamento segnalazione #70
		consistenzaAllevamento.setCodiceComune(rs.getString("CODICE_COMUNE"));
		

		return consistenzaAllevamento;
	}

}
