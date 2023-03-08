package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.domandaunica.DichiarazioniDomandaUnica;

public class DichiarazioniDomandaUnicaRowMapper implements RowMapper<DichiarazioniDomandaUnica> {
	private final Integer anno;

	public DichiarazioniDomandaUnicaRowMapper(final Integer anno) {
		super();
		this.anno = anno;
	}

	@Override
	public DichiarazioniDomandaUnica mapRow(ResultSet rs, int rowNum) throws SQLException {
		Map<String, String> tipoDichiarazioni = new HashMap<>();
		tipoDichiarazioni.put("DU_DICH_".concat(String.valueOf(anno)), "GENERALE");
		tipoDichiarazioni.put("DU_GIOVANE_AGR_".concat(String.valueOf(anno)), "GIOVANE_AGRICOLTORE");
		tipoDichiarazioni.put("DU_RISERVA_".concat(String.valueOf(anno)), "RISERVA_NAZIONALE");
		tipoDichiarazioni.put("DU_DATI_AGG_".concat(String.valueOf(anno)), "AGGIUNTIVA");
		tipoDichiarazioni.put("DU_SMALL_FARM", "PICCOLO_AGRICOLTORE");

		DichiarazioniDomandaUnica dichiarazioniDomandaUnica = new DichiarazioniDomandaUnica();
		dichiarazioniDomandaUnica.setCodDocumento(tipoDichiarazioni.get(rs.getString("COD_DOCUMENTO")) != null ? tipoDichiarazioni.get(rs.getString("COD_DOCUMENTO")) : rs.getString("COD_DOCUMENTO"));
		dichiarazioniDomandaUnica.setIdCampo(rs.getString("ID_RIGA"));
		dichiarazioniDomandaUnica.setDescCampo(rs.getString("DE_DECODIFICA"));
		dichiarazioniDomandaUnica.setValCheck(rs.getString("VAL_CHECK") == null ? null : (rs.getString("VAL_CHECK").equalsIgnoreCase("S") ? true : false));
		dichiarazioniDomandaUnica.setValDate(rs.getDate("VAL_DATA") == null ? null : rs.getDate("VAL_DATA"));
		dichiarazioniDomandaUnica.setValNumber(rs.getLong("VAL_NUM"));
		if (rs.wasNull()) {
			dichiarazioniDomandaUnica.setValNumber(null);
		}
		dichiarazioniDomandaUnica.setValString(rs.getString("LEGALE") != null ? rs.getString("LEGALE") : rs.getString("VAL_TESTO"));
		dichiarazioniDomandaUnica.setOrdine(rs.getLong("ORDINE"));

		return dichiarazioniDomandaUnica;
	}

}
