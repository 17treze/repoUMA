package it.tndigitale.a4g.psr.business.persistence.repository;

import it.tndigitale.a4g.psr.dto.DettaglioPagametoPsrRow;
import it.tndigitale.a4g.psr.dto.DomandaPsr;
import it.tndigitale.a4g.psr.dto.ImpegnoRichiestoPSRSuperficie;
import it.tndigitale.a4g.psr.dto.ImpegnoZooPascoloPsr;
import it.tndigitale.a4g.psr.dto.ImportiDomandaPsr;
import it.tndigitale.a4g.psr.dto.Operazione;
import it.tndigitale.a4g.psr.dto.PascoloPsr;
import it.tndigitale.a4g.psr.dto.StatoDomandaPsr;
import it.tndigitale.a4g.psr.dto.UbaAlpeggiatePsr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DomandePsrDao extends NamedParameterJdbcDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger(DomandePsrDao.class);

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	private final static List<String> LISTA_MODULI_CONSULTAZIONE = populateListaModuli();

	private final static List<String> LISTA_STATI_CONSULTAZIONE =
			new ArrayList<>(DomandaPsrRowMapper.populateStatoDomanda().keySet());

	public List<DomandaPsr> recuperaDomandePsr(String cuaa) {
		// String predicatoAnno = "> 2015"; // handleQueryAnno().apply("2015", ">");
		logger.debug("chiamata recuperaDomandePsr {}", cuaa);
		if (StringUtils.isEmpty(cuaa)) {
			throw new IllegalArgumentException("ESISTENZA_DOMANDA_CUAA_NULL");
		}
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("cuaa", cuaa.toUpperCase());
		parameters.addValue("listaStati", LISTA_STATI_CONSULTAZIONE);
		String moduliSql = LISTA_MODULI_CONSULTAZIONE.stream().map(modulo -> "\'" + modulo + "\'||modulo.anno")
				.collect(Collectors.joining(","));
		String query = NativeQueryString.RECUPERA_DOMANDE_PSR;
		query = query.replace(":moduli", moduliSql);
		query += "AND modulo.anno > 2015";
		return getNamedParameterJdbcTemplate().query(query, parameters, new DomandaPsrRowMapper());
	}

    public DomandaPsr getDomandaPsrSuperficieById(Integer idDomanda) {
        logger.debug("chiamata per recuperare Domanda Psr Superficie {}", idDomanda);
        if (idDomanda == null) {
            throw new IllegalArgumentException("ID_DOMANDA_SUPERFICIE_NULL");
        }

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("idDomanda", idDomanda);
        parameters.addValue("listaStati", LISTA_STATI_CONSULTAZIONE);

        String moduliSql =
                LISTA_MODULI_CONSULTAZIONE.stream()
                        .map(modulo -> "\'" + modulo + "\'||modulo.anno")
                        .collect(Collectors.joining(","));
        String query = NativeQueryString.RECUPERA_DOMANDA_PSR_SUPERFICIE_BY_ID_DOMANDA;
        query = query.replace(":moduli", moduliSql);
        query += "AND modulo.anno > 2015";

        List<DomandaPsr> domandePsrList =
                getNamedParameterJdbcTemplate().query(query, parameters, new DomandaPsrRowMapper());
        return domandePsrList.get(0);
    }

	public List<Operazione> recuperaMisureInterventoDomandaPsr(Long numeroDomanda) {
		logger.debug("chiamata recuperaMisureInterventoDomandaPsr {}", numeroDomanda);
		if (numeroDomanda == null) {
			throw new IllegalArgumentException("MISURE_INTERVENTO_NUMERO_DOMANDA_NULL");
		}
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("numeroDomanda", numeroDomanda);

		return getNamedParameterJdbcTemplate().query(NativeQueryString.RECUPERA_MISURE_INTERVENTO, parameters, new OperazioneRowMapper());
	}

	private static List<String> populateListaModuli() {
		List<String> moduli = new ArrayList<>();
		//gli anni vengono aggiunti dinamicamente nella query
		moduli.add("PSR_");//-Pagamenti agro-climatico, Agricoltura Biologica (Misure 10, 11) ed Indennità compensativa (Misura 13)
		moduli.add("PSR_ART_15_");//DOMANDA DI MODIFICA AI SENSI DELL'ART. 15 DEL REG. UE 809/2014 (Misure 10, 11 e Misura 13)
		moduli.add("PSR_RIT_PRZ_");//DOMANDA DI RITIRO PARZIALE AI SENSI DELL ART. 3 DEL REG. (UE) 809/2014
		moduli.add("PSR_RITIRO_");//PSR_RITIRO_2015: messo questo ulteriore modulo solo per il 2015.
		return moduli;
	}

	public List<ImpegnoRichiestoPSRSuperficie> getImpegniRichiestiPSRSuperficie(Integer idDomanda) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("idDomanda", idDomanda);

		return getNamedParameterJdbcTemplate().query(NativeQueryString.IMPEGNI_RICHIEST_PSR_SUPERFICIE_PER_DOMANDA,
				parameters, new ImpegniRichiestiPSRSuperficieRowMapper());
	}

    public Optional<String> getCuaaByIdDomanda(Integer idDomanda) {
	    logger.info("getting Cuaa by idDomanda: {}", idDomanda);
        if (idDomanda == null) {
            throw new IllegalArgumentException("ID_DOMANDA_CUAA_NULL");
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("idDomanda", idDomanda);
        return getNamedParameterJdbcTemplate().query(NativeQueryString.RECUPERA_CUAA_DA_ID_DOMANDA,
                    parameters, (resultSet, i) -> resultSet.getString("cuaa")).stream().findFirst();
    }

    public Optional<UbaAlpeggiatePsr> getUBAAlpeggiatePsrSuperficie(Integer idDomanda) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("idDomanda", idDomanda);

         List<UbaAlpeggiatePsr> ubaAlpeggiatePSR = getNamedParameterJdbcTemplate().query(NativeQueryString.UBA_ALPEGGIATE_PSR_SUPERFICIE_PER_DOMANDA,
                parameters, new UbaAlpeggiatePSRSuperficieRowMapper());
         if (ubaAlpeggiatePSR.size() > 1){
             throw new IllegalArgumentException("QUERY_UBA_RETURNED_MORE_THAN_ONE_RESULT");
         }
        return ubaAlpeggiatePSR.stream().findFirst();
    }

    public List<PascoloPsr> getQuadroPascoliByIdDomanda(Integer idDomanda) {
        logger.info("getting Pascoli by idDomanda: {}", idDomanda);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("idDomanda", idDomanda);

        return getNamedParameterJdbcTemplate().query(NativeQueryString.RECUPERA_QUADRO_PASCOLI_DA_ID_DOMANDA,
                parameters, new QuadroPascoliPsrRowMapper());
    }

    public List<ImpegnoZooPascoloPsr> getImpegniZooPascoliByIdDomanda(Integer idDomanda) {
        logger.info("getting Pascoli by idDomanda: {}", idDomanda);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("idDomanda", idDomanda);

        return getNamedParameterJdbcTemplate().query(NativeQueryString.RECUPERA_IMPEGNIZOO_PASCOLI_PASCOLI_DA_ID_DOMANDA,
                parameters, new ImpegnoZooPascoloPsrRowMapper());
    }

    public List<StatoDomandaPsr> getStatiOperazioneByIdDomanda(Integer idDomanda) {
        logger.info("getting stato operazioni by idDomanda: {} ", idDomanda);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("idDomanda", idDomanda);

        return getNamedParameterJdbcTemplate().query(NativeQueryString.RECUPERA_STATO_OPERAZIONE_DA_ID_DOMANDA,
                parameters, new StatoDomandaPsrRowMapper());
    }


    public Optional<Boolean> isCodOperazioneForIdDomandaCampione(String idDomanda, String codOperazione) {
        logger.info("getting stato operazioni by idDomanda: {} and operazione: {} ", idDomanda, codOperazione);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("idDomanda", idDomanda);
        parameters.addValue("codOperazione", codOperazione);

        return getNamedParameterJdbcTemplate().query(NativeQueryString.RECUPERA_STATO_OPERAZIONE_DA_ID_DOMANDA_AND_COD_OPERAZIONE,
                parameters, new DettaglioPagamentoCampioneDomandaPsr()).stream().findFirst();
    }

    public ImportiDomandaPsr getImportiByIdDomandaAndAnno(String cuaa, Integer annoDiCampagna) {
        logger.info("getting importi by domanda with cuaa: {} and anno: {}", cuaa, annoDiCampagna);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("cuaa", cuaa);
        parameters.addValue("codModulo", "PSR_RIT_TOT_" + annoDiCampagna);
        parameters.addValue("anno", annoDiCampagna);

        BigDecimal importoTotaleRichiesto = getNamedParameterJdbcTemplate().queryForObject(NativeQueryString.IMPORTO_TOTALE_RICHIESTO_DA_ANNO_MODULO_E_CUAA,
                parameters, new ImportoRichiestoDomandaPsrRowMapper());

        ImportiDomandaPsr importiDomandaPsr = new ImportiDomandaPsr();
        importiDomandaPsr.setImportoRichiesto(importoTotaleRichiesto);
        return importiDomandaPsr;
    }

    public List<DettaglioPagametoPsrRow> getDettaglioPagamento(String idDomanda, Integer annoDiCampagna, String codiceOperazione, String tipoPagamento) {
        logger.info("getting dettaglio pagamento by domanda {}, anno: {}, codice operazione {}, tipo di pagamento {}",
                idDomanda, annoDiCampagna, codiceOperazione, tipoPagamento);

        String codiceCalcolo = String.format("PSR_%s_%s_%s", annoDiCampagna, codiceOperazione, tipoPagamento);

        String queryWithAnnoDiCampagnatable = String.format(NativeQueryString.RECUPERA_DETTAGLIO_PAGAMENTO_DA_ID_DOMANDA_E_CODICE, annoDiCampagna);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ID_DOMANDA", idDomanda);
        parameters.addValue("COD_CALCOLO", codiceCalcolo);

        final List<DettaglioPagametoPsrRow> dettaglioPagametoPsrRow = getNamedParameterJdbcTemplate().query(queryWithAnnoDiCampagnatable,
                parameters, new DettaglioPagamentoPsrRowMapper());

        return dettaglioPagametoPsrRow;
  }

    public BigDecimal getImportoCalcolatoByIdDomandaMisuraAndTipoPagamento(Integer idDomanda, String codMisura, String tipoPagamento) {
        logger.info("getting importoCalcolato by idDomanda: {},  misura: {} and tipoPAgamento: {}", idDomanda, codMisura, tipoPagamento);
        if (idDomanda == null || codMisura == null || tipoPagamento == null) {
            throw new IllegalArgumentException("ONE_OR_MORE_PARAMS_NULL");
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ID_DOMANDA", idDomanda);
        parameters.addValue("COD_MISURA", codMisura);
        parameters.addValue("TIPO_PAGAMENTO", tipoPagamento);
        List<BigDecimal> queryresult = getNamedParameterJdbcTemplate().query(NativeQueryString.RECUPERA_IMPORTO_CALCOLATO_DA_ID_DOMANDA_CODICE_E_TIPO_PAGAMENTO,
                parameters, (resultSet, i) -> resultSet.getBigDecimal(1));
        return queryresult.get(0);

    }
}
