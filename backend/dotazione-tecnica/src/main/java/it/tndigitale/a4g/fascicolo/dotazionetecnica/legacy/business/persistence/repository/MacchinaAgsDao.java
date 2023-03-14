package it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.business.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.MacchinaAgsDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.MacchinaAgsFilter;
import it.tndigitale.a4g.framework.config.DataSourceConfig;
import it.tndigitale.a4g.framework.time.LocalDateConverter;

@Repository
public class MacchinaAgsDao extends NamedParameterJdbcDaoSupport {

	@Autowired
	private DataSourceConfig dataSourceConfig;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSourceConfig.secondaryDataSource());
	}

	public List<MacchinaAgsDto> getMacchine(MacchinaAgsFilter filter) {

		StringBuilder sb = new StringBuilder("");
		Map<String, Object> params = new HashMap<String, Object>();

//		// filtro per cuaa
//		if (filter.getCuaa() != null) {
//			params.put("cuaa", filter.getCuaa().toUpperCase());
//			sb.append("AND csv.CUAA = :cuaa ");
//		}
//		// filtro per carburanti
//		if (!CollectionUtils.isEmpty(filter.getTipiCarburante())) {
//			List<String> codiciCarburante = filter.getTipiCarburante().stream().map(carburante -> {
//				switch (carburante) {
//				case BENZINA:
//					return "000002";
//				case GASOLIO:
//					return "000001";
//				default:
//					return "000999";
//				}
//			}).collect(Collectors.toList());
//			params.put("codicicarburante", codiciCarburante);
//			sb.append("AND m.COD_ALIMENTAZIONE = 'ALIMAC' ");
//			sb.append("AND m.SCO_ALIMENTAZIONE IN (:codicicarburante) ");
//		}
//
//		// Data di riferimento alla quale le macchine ritornate sono valide nel fascicolo
//		params.put("data", LocalDateConverter.to(filter.getData()));

		// comunque reperisce le macchine che vanno a GASOLIO o BENZINA
		// else {
		// sb.append("AND m.COD_ALIMENTAZIONE = 'ALIMAC' ");
		// sb.append("AND m.SCO_ALIMENTAZIONE IN ('000001' , '000002') ");
		// }

		String query = getMacchineValideFascicoloSql().concat(sb.toString());
		logger.debug("[Macchina Ags Dao] - get macchine Ags: ".concat(query));
		try {
			return getNamedParameterJdbcTemplate().<MacchinaAgsDto>query(query, params, new GetMacchineRowMapper());
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("[MacchinaAgsDao] - getMacchine: ", e);
			return new ArrayList<>();
		}
	}

	private String getMacchineValideFascicoloSql() {
//		return "SELECT m.ID_MACCHINA, m.DE_MACCHINA AS descrizione, m.TARGA AS targa, " + "m.SCO_CLASSE AS codiceclasse, " + "m.SCO_SOTTO_CLASSE AS codicesottoclasse, "
//				+ decodifica("m.COD_CLASSE", "m.SCO_CLASSE") + "AS classe, " + decodifica("m.COD_SOTTO_CLASSE", "m.SCO_SOTTO_CLASSE") + "AS sottoclasse, " + decodifica("m.COD_MARCA", "m.SCO_MARCA")
//				+ "AS marca, " + decodifica("m.COD_ALIMENTAZIONE", "m.SCO_ALIMENTAZIONE") + "AS alimentazione, " + decodifica("m.COD_POSSESSO", "m.SCO_POSSESSO") + "AS possesso, "
//				+ "m.MATRICOLA AS MATRICOLA, " + "m.TELAIO AS TELAIO, " + "m.DE_MARCA_MOTORE AS MARCA_MOTORE, " + "m.DE_TIPO_MOTORE AS TIPO_MOTORE, " + "m.POTENZA_KW AS POTENZA_KW,"
//				+ "m.ID_TIPO_MACCHINA," + "m.ID_MACCHINA "
//				// + "ROUND(m.POTENZA_KW) AS POTENZA_KW "
//				+ "FROM TMACCHINA m " + "INNER JOIN cons_sogg_viw csv on csv.pk_cuaa = m.id_soggetto " + "INNER JOIN TFASCICOLO f ON f.ID_SOGGETTO = csv.PK_CUAA AND  f.ID_SOGGETTO = m.ID_SOGGETTO "
//				+ "WHERE :data between csv.data_inizio_val and csv.data_fine_val " + "AND :data between m.dt_inizio and m.dt_fine " + "AND :data BETWEEN m.DT_INSERT AND m.DT_DELETE ";
		return "SELECT m.id as ID_MACCHINA,m.denominazione AS descrizione, m.TARGA AS targa, tm.descrizione AS codiceclasse, \r\n"
				+ "    sm.descrizione AS codicesottoclasse, null AS classe, sm.descrizione AS sottoclasse, m.MARCA AS marca, \r\n"
				+ "    null AS alimentazione, m.tipo_POSSESSO AS possesso, m.numero_MATRICOLA AS MATRICOLA, m.numero_TELAIO AS TELAIO, \r\n"
				+ "    null AS MARCA_MOTORE, null AS TIPO_MOTORE, null AS POTENZA_KW, tm.id as ID_TIPO_MACCHINA, m.id as ID_MACCHINA     \r\n"
				+ "FROM A4GT_MACCHINA m \r\n"
				+ "    INNER JOIN A4GT_FASCICOLO f ON f.ID = M.ID_FASCICOLO AND f.id_validazione = m.id_validazione_fascicolo\r\n"
				+ "    left outer join a4gd_sottotipo sm on sm.id = m.id_sottotipo\r\n"
				+ "    left outer join a4gd_tipologia tm on tm.id = sm.id_tipologia\r\n"
				+ "";
	}

	private String decodifica(String codice, String sottoCodice) {
		StringBuilder sb = new StringBuilder();
		sb.append("(SELECT DS_DECODIFICA FROM TDECODIFICA WHERE CODICE = ");
		sb.append(codice);
		sb.append(" AND SOTTO_CODICE = ");
		sb.append(sottoCodice);
		sb.append(" ) ");
		return sb.toString();
	}
}
