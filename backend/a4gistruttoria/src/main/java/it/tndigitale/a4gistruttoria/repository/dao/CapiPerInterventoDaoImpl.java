package it.tndigitale.a4gistruttoria.repository.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.dto.StatisticaZootecnia;

@Repository
public class CapiPerInterventoDaoImpl extends NamedParameterJdbcDaoSupport implements CapiPerInterventoDao {

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
	private static final  String SQL_CAPI_INT=
			"SELECT \r\n" + 
			"	i.CODICE_AGEA AS codiceAgea, i.DESCRIZIONE_BREVE AS descrizioneBreve ,  \r\n" + 
			"	COUNT(es.ID) totali ,  \r\n" + 
			"	SUM(CASE WHEN es.ESITO = 'AMMISSIBILE'  \r\n" + 
			"	 	THEN 1 ELSE 0 END) ammissibili ,  \r\n" + 
			"	SUM(CASE WHEN es.ESITO = 'NON_AMMISSIBILE'  \r\n" + 
			"	 	THEN 1 ELSE 0 END) nonAmmissibili ,  \r\n" + 
			"	SUM(CASE WHEN es.ESITO = 'AMMISSIBILE_CON_SANZIONE'  \r\n" + 
			"	 	THEN 1 ELSE 0 END) ammissibiliConSanzione  \r\n" + 
			"FROM \r\n" + 
			"	A4GT_ESITO_CALCOLO_CAPO es , A4GT_ALLEVAMENTO_IMPEGNATO al , A4GD_INTERVENTO i , A4GT_DOMANDA d  \r\n" + 
			"WHERE \r\n" + 
			"	es.RICHIESTO = 1 AND\r\n" + 
			"	es.ID_ALLEVAM_DU = al.ID AND al.ID_INTERVENTO = i.ID AND al.ID_DOMANDA = d.ID  \r\n" + 
			"	AND d.ANNO_CAMPAGNA = :annoCampagna GROUP BY i.CODICE_AGEA, i.DESCRIZIONE_BREVE	";
	
	
	@Override
	public List<StatisticaZootecnia> getCapiPerIntervento(Integer annoCampagna) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("annoCampagna", annoCampagna);
		return getNamedParameterJdbcTemplate()
				.<StatisticaZootecnia>query(SQL_CAPI_INT, parameters,
						new BeanPropertyRowMapper<StatisticaZootecnia>(StatisticaZootecnia.class));
	}

}
