package it.tndigitale.a4g.soc.business.persistence.repository;

import static it.tndigitale.a4g.soc.business.persistence.builder.LiquidazioneBuilder.paramsOfCalcoloDebitiXImportoLiquidato;
import static it.tndigitale.a4g.soc.business.persistence.builder.LiquidazioneBuilder.paramsOfCalcoloImportoLiquidato;
import static it.tndigitale.a4g.soc.business.persistence.converter.LiquidazioneConverter.debitoFrom;
import static it.tndigitale.a4g.soc.business.persistence.converter.LiquidazioneConverter.importoLiquidatoFrom;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.soc.business.dto.Debito;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidato;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidatoFilter;
import it.tndigitale.a4g.soc.business.service.LiquidazioneHandler;
import it.tndigitale.a4g.soc.config.CapitoliConfig;

@Repository
public class LiquidazioneCustomDao extends NamedParameterJdbcDaoSupport {
	
	@Autowired
	private DataSource dataSource;
	@Autowired
	private CapitoliConfig capitoli;

	
	private static final String DEBITI_IMPORTO_LIQUIDATO_QUERY =
			"SELECT DISTINCT DESCRIZIONE_CAPITOLO as descrizioneCapitolo, IMPORTO_INCASSO as importIncasso " +
		    "FROM A4G_IMPORTO_RECUPERATO " +
			"WHERE TIPO_BILANCIO = :tipoBilancio AND ANNO_ESERCIZIO = :annoEsercizio AND PROGRESSIVO_CREDITO = :progressivoCredito";

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	public List<ImportoLiquidato> calcolaImportoLiquidato(ImportoLiquidatoFilter importoLiquidatoFilter) {
		Map<String, Object> params = paramsOfCalcoloImportoLiquidato(importoLiquidatoFilter);
		String queryString = LiquidazioneHandler.getQuery(
				importoLiquidatoFilter.getTipoDomanda(),
				importoLiquidatoFilter.getIdElencoLiquidazione() != null,
				importoLiquidatoFilter.getAnno() != null,
				importoLiquidatoFilter.getTipoPagamento() != null);
		return getNamedParameterJdbcTemplate().query(
				queryString, params,
				(rs, i) -> importoLiquidatoFrom(rs, capitoli.getCapitoli(), importoLiquidatoFilter.getTipoDomanda()));
	}
	
	public List<Debito> calcolaDebitiImportoLiquidato(ImportoLiquidato importoLiquidato) {
		Map<String, Object> params = paramsOfCalcoloDebitiXImportoLiquidato(importoLiquidato);
		return getNamedParameterJdbcTemplate().query(
				DEBITI_IMPORTO_LIQUIDATO_QUERY,
				params, (rs, i) -> debitoFrom(rs));
	}

}
