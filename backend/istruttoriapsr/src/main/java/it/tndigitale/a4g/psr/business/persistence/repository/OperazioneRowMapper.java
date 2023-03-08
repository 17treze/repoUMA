package it.tndigitale.a4g.psr.business.persistence.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import it.tndigitale.a4g.psr.dto.CodiceMisureIntervento;
import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.psr.dto.Operazione;

class OperazioneRowMapper implements RowMapper<Operazione>{
	private final static HashMap<String, CodiceMisureIntervento> MAPPING_MISURE_INTERVENTO = populateMisureIntervento();

	@Override
	public Operazione mapRow(ResultSet rs, int rowNum) throws SQLException {
		Operazione op = new Operazione();
		op.setCodiceMisureIntervento(MAPPING_MISURE_INTERVENTO.get(rs.getString("cod_operazione")));
		return op;
	}

	private static HashMap<String, CodiceMisureIntervento> populateMisureIntervento() {
		HashMap<String, CodiceMisureIntervento> map = new HashMap<>();
		map.put("10.1.1", CodiceMisureIntervento.M10_O1_1); // Indennità compensativa per gli agricoltori delle zone montane
		map.put("10.1.2", CodiceMisureIntervento.M10_O1_2); // Gestione aree prative miglioramento della biodiversità legata ai prati permanenti
		map.put("10.1.3", CodiceMisureIntervento.M10_O1_3); // Gestione delle superfici a pascolo: aiuti a favore dell’alpeggio
		map.put("10.1.4", CodiceMisureIntervento.M10_O1_4); // Allevamento di razze animali minacciate di estinzione
		map.put("11.1", CodiceMisureIntervento.M11_O1_1); // Coltivazione di specie vegetali minacciate di erosione genetica
		map.put("11.2", CodiceMisureIntervento.M11_O2_1); // Sostegno all’introduzione del metodo biologico
		map.put("13", CodiceMisureIntervento.M13_O1_1); // Mantenimento del metodo biologico

		return map;
	}

}
