package it.tndigitale.a4g.fascicolo.territorio.business.persistence.repository.legacy.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.fascicolo.territorio.business.persistence.repository.legacy.PianoColtureAgsDao;
import it.tndigitale.a4g.fascicolo.territorio.dto.TitoloConduzione;
import it.tndigitale.a4g.fascicolo.territorio.dto.legacy.CriterioMantenimento;
import it.tndigitale.a4g.fascicolo.territorio.dto.legacy.PianoColturaleAgsDto;
import it.tndigitale.a4g.framework.support.CustomCollectors;

public class PianoColturaleRowMapper  implements RowMapper<PianoColturaleAgsDto>  {

	public PianoColturaleRowMapper() {
		super();
	}

	@Override
	public PianoColturaleAgsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		String subalterno = rs.getString("SUB").isBlank() ? null : rs.getString("SUB").trim();

		var titoloInt = rs.getInt("TIPO_TITOLO");
		Map<TitoloConduzione, Integer> mappaTitoloConduzione = new PianoColtureAgsDao().getMappaTitoloConduzione();
		TitoloConduzione titolo = mappaTitoloConduzione.entrySet().stream()
				.filter(m -> m.getValue().equals(titoloInt))
				.map(Entry::getKey)
				.collect(CustomCollectors.toSingleton());

		return new PianoColturaleAgsDto()
				.setCodiceNazionale(rs.getString("COD_NAZIONALE"))
				.setIdColtura(rs.getLong("ID_COLTURA"))
				.setSuperficieAccertata(rs.getInt("SUP_ACCERTATA"))
				.setSuperficieDichiarata(rs.getInt("SUP_DICHIARATA"))
				.setFoglio(rs.getString("FOGLIO"))
				.setParticella(rs.getString("PARTICELLA"))
				.setSubalterno(subalterno)
				.setIdParticella(rs.getLong("ID_PARTICELLA"))
				.setCodiceDestinazioneUso(rs.getString("CODI_DEST_USO"))
				.setCodiceProdotto(rs.getString("CODI_PROD"))
				.setCodiceQualita(rs.getString("CODI_QUAL"))
				.setCodiceUso(rs.getString("CODI_USO"))
				.setCodiceVarieta(rs.getString("CODI_VARI"))
				.setTitoloConduzione(titolo)
				.setTipoAtto(rs.getInt("fkid_tipo_doc"))
				.setDescrizioneAtto(rs.getString("descrizione"))
				.setCriterioMantenimento(rs.getString("CRIT_MANT") != null ? CriterioMantenimento.valueOf(rs.getString("CRIT_MANT").replaceAll(" ", "_").toUpperCase()) : null);
	}
}
