package it.tndigitale.a4g.srt.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.sql.DataSource;
import javax.validation.constraints.Null;


import it.tndigitale.a4g.srt.dto.PsrAnticipoDto;
import it.tndigitale.a4g.srt.dto.PsrBandoDto;
import it.tndigitale.a4g.srt.dto.PsrDomandeDto;
import it.tndigitale.a4g.srt.dto.PsrFinanziabilitaDto;
import it.tndigitale.a4g.srt.dto.PsrPagamentoDto;
import it.tndigitale.a4g.srt.dto.PsrCostiContributiDto;
import it.tndigitale.a4g.srt.dto.PsrProgettoCostiInvestimentiDto;
import it.tndigitale.a4g.srt.dto.PsrProgettoDescrizioneDto;
import it.tndigitale.a4g.srt.dto.PsrProgettoImpresaDescrizioneDto;
import it.tndigitale.a4g.srt.dto.PsrProtocolloDataProgetto;
import it.tndigitale.a4g.srt.dto.PsrAccontoSaldoDto;
import it.tndigitale.a4g.srt.dto.PsrInterventoDettaglioDto;
import it.tndigitale.a4g.srt.dto.PsrSanzioneDto;
import it.tndigitale.a4g.srt.dto.PsrFatturaDto;
import it.tndigitale.a4g.srt.dto.PsrVarianteDto;
import it.tndigitale.a4g.srt.dto.PsrInvestimentoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4g.srt.utils.PsrProgettoDescrizioneConverter;

@Service
public class DomandeServiceImpl extends JdbcDaoSupport {

	public enum TipologiaDatiPagamento {
		SALDO,
		ACCONTO,
		FINANZIABILITA
	}

	private static final Logger logger = LoggerFactory.getLogger(DomandeServiceImpl.class);

	@Autowired
    private DataSource dataSource;

	@Autowired
	private EntityManager em;

	@Autowired private PsrProgettoDescrizioneConverter psrProgettoDescrizioneConverter;

	@PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

	class PsrQueryResult {
		Integer idBando;
		LocalDate anticipoRichiestaData;
		BigDecimal anticipoContributoRichiesto;
		BigDecimal anticipoContributoAmmesso;
		BigDecimal pagamentoContributoRichiesto;
		BigDecimal pagamentoContributoAmmesso;
		BigDecimal pagamentoImportoRichiesto;
		LocalDate pagamentoData;
		Integer idDomandaPagamento;
		String tipoDomanda;
		Character codiceStatoProgetto;
		String statoProgetto;
		String segnatura;
		Boolean finanziatoPat;
		String socNumeroDomanda;
		LocalDate dataIstruttoria;
		LocalDate dataFinanziabilita;
		Boolean approvata;
		String segnaturaApprovazione;
        String codice;

		PsrQueryResult(final Object[] res) {
			idBando = (Integer)res[1];
			anticipoRichiestaData = LocalDateConverter.from((Timestamp)res[2]);
			anticipoContributoRichiesto = (BigDecimal)res[3];
			anticipoContributoAmmesso = (BigDecimal)res[4];
			pagamentoContributoRichiesto = (BigDecimal)res[5];
			pagamentoContributoAmmesso = (BigDecimal)res[6];
			pagamentoImportoRichiesto = (BigDecimal)res[7];
			pagamentoData = LocalDateConverter.from((Timestamp)res[8]);
			idDomandaPagamento = (Integer)res[9];
			tipoDomanda = (String)res[10];
			codiceStatoProgetto = (Character)res[11];
			statoProgetto = (String)res[12];
			segnatura = (String)res[13];
			finanziatoPat = (Boolean)res[15];
			socNumeroDomanda = (String)res[16];
			socNumeroDomanda = socNumeroDomanda != null ? socNumeroDomanda.trim() : "";
			dataIstruttoria = LocalDateConverter.from((Timestamp)res[17]);
			dataFinanziabilita = LocalDateConverter.from((Timestamp)res[18]);
			approvata = (Boolean)res[19];
			segnaturaApprovazione = (String)res[20];
			codice = (String)res[21];
		}
	}

	public List<PsrBandoDto> getPsrBandiImpresaByCuaa(final String cuaa) {
		List<PsrBandoDto> retList = new ArrayList<>();
		StoredProcedureQuery query = em.createStoredProcedureQuery("A4GSpPsrBandiImpresaCuaa");
		query.registerStoredProcedureParameter("CUAA", String.class, ParameterMode.IN);
		query.setParameter("CUAA", cuaa);
		if (query.execute()) {
			for (Object[] result : (List<Object[]>)query.getResultList()) {
				try {
					PsrBandoDto psrBandoDto = PsrBandoDto.mapToDto(result);
					retList.add(psrBandoDto);
				} catch (Exception e) {
					logger.error("Errore nella gestione del dato ottenuto da SRT", e);
				}
			}
		}
		for(PsrBandoDto bando : retList) {
			if ("H".equals(bando.getCodiceStatoProgetto())) {
				List<Pair<Character, String>> storico = this.getPsrStoricoStati(bando.getIdProgetto());
				if (storico.size() > 1) {
					bando.setCodiceStatoProgetto(storico.get(1).getFirst().toString());
				}
			}
		}
		return retList;
	}

	public List<Pair<Character, String>> getPsrStoricoStati(final Integer idProgetto) {
		String query = "select COD_STATO, A4GMSP.DESCRIZIONE from PROGETTO_STORICO " +
				"inner join A4G_REL_N_MAP_STATO_PROGETTO A4GRNMSP on " +
				"A4GRNMSP.CODICE_STATO_PROGETTO = PROGETTO_STORICO.COD_STATO " +
				"inner join A4G_MAP_STATO_PROGETTO A4GMSP on A4GMSP.ID = A4GRNMSP.ID_A4G_MAP_STATO_PROGETTO " +
				"where ID_PROGETTO=?1 order by DATA desc;";

		List result = em.createNativeQuery(query)
				.setParameter(1, idProgetto).getResultList();
		List<Pair<Character, String>> resultList = new ArrayList<>();
		for(Object obj: result) {
			Object[] res = (Object[]) obj;
			resultList.add(Pair.of(
					(Character) res[0],
					String.valueOf(res[1])
			));
		}
		return resultList;
	}

	public List<PsrProgettoCostiInvestimentiDto> getPsrInvestimentiContributiByIdProgetto(final Integer idProgetto) {
		List<PsrProgettoCostiInvestimentiDto> retList = new ArrayList<>();
		StoredProcedureQuery query = em.createStoredProcedureQuery("SpPsrProgettoCostiInvestimenti");
		query.registerStoredProcedureParameter("ID_PROGETTO", Integer.class, ParameterMode.IN);
		query.setParameter("ID_PROGETTO", idProgetto);
		if (query.execute()) {
			for (Object[] result: (List<Object[]>)query.getResultList()) {
				try {
					PsrProgettoCostiInvestimentiDto psrBandoDto = PsrProgettoCostiInvestimentiDto.mapToDto(result);
					retList.add(psrBandoDto);
				} catch (Exception e) {
					logger.error("Errore nella gestione del dato ottenuto da SRT", e);
				}
			}
		}
		return retList;
	}

	public List<PsrProgettoImpresaDescrizioneDto> getPsrDescrizioneImpresaByIdProgetto(final Integer idProgetto) {
		List<PsrProgettoImpresaDescrizioneDto> retList = new ArrayList<>();
		StoredProcedureQuery query = em.createStoredProcedureQuery("SpA4gPsrDescrizioneImpresaByProgetto")
				.registerStoredProcedureParameter("ID_PROGETTO", Integer.class, ParameterMode.IN)
				.setParameter("ID_PROGETTO", idProgetto);
		if (query.execute()) {
			for (Object[] result: (List<Object[]>)query.getResultList()) {
				try {
					PsrProgettoImpresaDescrizioneDto psrDto = PsrProgettoImpresaDescrizioneDto.mapToDto(result);
					if ("H".equals(psrDto.getCodiceStatoProgetto())) {
						List<Pair<Character, String>> storico = this.getPsrStoricoStati(psrDto.getIdProgetto());
						if (storico.size() > 1) {
							Pair<Character, String> rightState = storico.get(1);
							psrDto.setCodiceStatoProgetto(rightState.getFirst().toString());
							psrDto.setStatoProgetto(rightState.getSecond());
						}
					}
					retList.add(psrDto);
				} catch (Exception e) {
					logger.error("Errore nella gestione del dato ottenuto da SRT", e);
				}
			}
		}
		return retList;
	}

	public List<PsrCostiContributiDto> getRiepilogoCostiContributi(Integer idProgetto) {
		List<PsrCostiContributiDto> retList = new ArrayList<>();
		List<Object[]> resultList = em.createNativeQuery(
				"SELECT id_progetto, id_bando, costo_totale, contributo_totale, contributo_rimanente, DATA_VALUTAZIONE"
				+ " FROM GRADUATORIA_PROGETTI"
				+ " WHERE id_progetto = ?1").setParameter(1, idProgetto).getResultList();
		for (Object[] res : resultList) {
			PsrCostiContributiDto psr = new PsrCostiContributiDto();
			psr.setIdProgetto((Integer) res[0]);
			psr.setIdBando((Integer) res[1]);
			psr.setCostoTotale((BigDecimal) res[2]);
			psr.setContributoTotale((BigDecimal) res[3]);
			psr.setContributoRimanente((BigDecimal) res[4]);
			psr.setData(LocalDateConverter.from((Timestamp)res[5]));
			retList.add(psr);
		}
		return retList;
	}

    private void populateValoreStoredProcedureFinanziabilitaPerIdProgetto(
            final String storedProcedureName, final Integer idProgetto, final PsrDomandeDto psr, boolean filterByState) throws Exception {
        PsrFinanziabilitaDto psrFinanziabilitaDto = psr.getFinanziabilita();
        StoredProcedureQuery query = em.createStoredProcedureQuery(storedProcedureName);
        query.registerStoredProcedureParameter("ID_PROGETTO", Integer.class, ParameterMode.IN);
        query.setParameter("ID_PROGETTO", idProgetto);
        List<Object[]> resultList = (List<Object[]>) query.getResultList();

        if(filterByState) {
			String stato = psr.getCodiceStatoProgetto().toString();
			resultList = resultList.stream().filter(r -> r[3].equals(stato)).collect(Collectors.toList());
		}
        if (resultList.isEmpty()) {
            psrFinanziabilitaDto.setCostoRichiesto(null);
            psrFinanziabilitaDto.setContributoRichiesto(null);
            psrFinanziabilitaDto.setContributoAmmesso(null);
            return;
        } else if (resultList.size() > 1) {
            throw new Exception("Errore: eccessivo numero di risultati per progetto");
        }

        Object[] result = resultList.get(0);
        psrFinanziabilitaDto.setCostoRichiesto((BigDecimal) result[0]);
        psrFinanziabilitaDto.setContributoRichiesto((BigDecimal) result[1]);
        psrFinanziabilitaDto.setContributoAmmesso((BigDecimal) result[2]);
    }

    private void setContributoRichiestoIfCodiceIs611(PsrDomandeDto psr) {
        if(psr.getCodice().equals("6.1.1")){
            psr.getFinanziabilita().setContributoRichiesto(BigDecimal.valueOf(40000));
        }
    }

    private BigDecimal getValoreQueryPerIdProgetto(final String query, final Integer idProgetto) throws Exception {
		List<PsrCostiContributiDto> retList = new ArrayList<>();
		List<Object> resultList = em.createNativeQuery(query)
				.setParameter(1, idProgetto).getResultList();
		BigDecimal valore = null;
		if (resultList.isEmpty()) {
			return null;
		} else if (resultList.size() != 1) {
			throw new Exception("Errore: eccessivo numero di risultati per progetto");
		}
		valore = (BigDecimal)resultList.get(0);
		return valore;
	}

	private BigDecimal getSaldoContributoRichiesto(final Integer idProgetto) throws Exception {
		String query = "SELECT BTI.IMPORTO_MAX" +
			" FROM PROGETTO AS P" +
			" INNER JOIN BANDO_TIPO_INVESTIMENTI AS BTI ON P.ID_BANDO = BTI.ID_BANDO" +
			" WHERE BTI.COD_TIPO_INVESTIMENTO = 3 AND P.ID_PROGETTO = ?1";
		return getValoreQueryPerIdProgetto(query, idProgetto);
	}

	private BigDecimal getSaldoContributoLiquidabile(final Integer idProgetto) throws Exception {
		String query = "SELECT BTI.IMPORTO_MAX - (SELECT isnull(AR.CONTRIBUTO_AMMESSO, 0)" +
				" FROM anticipi_richiesti AS ar" +
				" INNER JOIN domanda_di_pagamento AS ddp1 ON ddp1.id_domanda_pagamento = ar.id_domanda_pagamento" +
				" WHERE ddp1.id_progetto = ddp.id_progetto AND ddp1.cod_tipo = 'ant' AND ddp1.annullata = 0)" +
				" FROM bando as b INNER JOIN progetto AS p ON b.id_bando = p.id_bando" +
				" INNER JOIN domanda_di_pagamento AS ddp ON ddp.id_progetto = p.id_progetto" +
				" INNER JOIN BANDO_TIPO_INVESTIMENTI AS BTI ON P.ID_BANDO = BTI.ID_BANDO" +
				" LEFT JOIN anticipi_richiesti AS AR ON ddp.id_domanda_pagamento = ar.id_domanda_pagamento" +
				" WHERE BTI.COD_TIPO_INVESTIMENTO = 3 AND DDP.id_progetto = ?1 AND COD_TIPO = 'SLD' AND ddp.APPROVATA = 1";
		return getValoreQueryPerIdProgetto(query, idProgetto);
	}

	private boolean getIsPremioContoCapitale(Integer idProgetto) throws Exception {
		String query = "SELECT 1 FROM PROGETTO AS P\n" +
				" INNER JOIN BANDO_TIPO_INVESTIMENTI AS BTI ON P.ID_BANDO = BTI.ID_BANDO" +
				" WHERE BTI.COD_TIPO_INVESTIMENTO = 3 AND P.ID_PROGETTO = ?1";
		List<Object> resultList = em.createNativeQuery(query)
				.setParameter(1, idProgetto).getResultList();
		if (resultList.size() == 0) {
			return false;
		} else if (resultList.size() != 1) {
			throw new Exception("Errore: eccessivo numero di risultati per progetto");
		}
		return true;
	}

	/*
	 * WIP, manca implementazione per conto capitale con altra stored procedure:
	 * SpA4GPsrDescrizioneDomandaByProgettoContoCapitale
	 */
	public List<PsrProgettoDescrizioneDto> getDescrizioneDomandaByProgetto(final Integer idProgetto) throws Exception {
		List<PsrProgettoDescrizioneDto> retList = new ArrayList<>();
		boolean isPremioContoCapitale = getIsPremioContoCapitale(idProgetto);
		if (isPremioContoCapitale) {
			StoredProcedureQuery query = em.createStoredProcedureQuery("SpA4GPsrDescrizioneDomandaByProgettoContoCapitale");
			query.registerStoredProcedureParameter("ID_PROGETTO", Integer.class, ParameterMode.IN);
			query.setParameter("ID_PROGETTO", idProgetto);
			if (query.execute()) {
				for (Object[] result: (List<Object[]>)query.getResultList()) {
					try {
						PsrProgettoDescrizioneDto psrBandoDto = psrProgettoDescrizioneConverter.fromDb(result);
						retList.add(psrBandoDto);
					} catch (Exception e) {
						logger.error("Errore nella gestione del dato ottenuto da SRT", e);
					}
				}
			}
		} else {
			StoredProcedureQuery query = em.createStoredProcedureQuery("SpA4GPsrDescrizioneDomandaByProgettoGenerica");
			query.registerStoredProcedureParameter("ID_PROGETTO", Integer.class, ParameterMode.IN);
			query.setParameter("ID_PROGETTO", idProgetto);
			if (query.execute()) {
				for (Object[] result: (List<Object[]>)query.getResultList()) {
					try {
						PsrProgettoDescrizioneDto psrBandoDto = psrProgettoDescrizioneConverter.fromDb(result);
						retList.add(psrBandoDto);
					} catch (Exception e) {
						logger.error("Errore nella gestione del dato ottenuto da SRT", e);
					}
				}
			}
		}
		return retList;
	}

	private void popolaDomandaPsrTipoAcconto(final PsrQueryResult queryRes, final List<PsrAccontoSaldoDto> acconti) {
		Optional<PsrAccontoSaldoDto> optAcconto = acconti.stream().filter(
				acc -> acc.getIdDomandaPagamento().equals(queryRes.idDomandaPagamento)).findFirst();
		PsrAccontoSaldoDto acconto;
		if (optAcconto.isPresent()) {
			acconto = optAcconto.get();
		} else {
			acconto = new PsrAccontoSaldoDto();
			acconti.add(acconto);
			acconto.setIdDomandaPagamento(queryRes.idDomandaPagamento);
			acconto.setContributoRichiesto(new BigDecimal(0));
			acconto.setCostoInvestimentoRichiesto(new BigDecimal(0));
			acconto.setContributoLiquidabile(new BigDecimal(0));
		}
		acconto.iterazioneSommatoriaPopolaTipoDomanda(
				queryRes.idDomandaPagamento,
				queryRes.pagamentoImportoRichiesto,
				queryRes.pagamentoContributoRichiesto,
				queryRes.pagamentoContributoAmmesso,
				queryRes.pagamentoData,
				queryRes.socNumeroDomanda);
		popolaSanzioniDomanda(acconto, acconto.getIdDomandaPagamento());
	}

	public Map<String, BigDecimal> getTotaliVariante(Integer idProgetto, Integer idVariante) {
		Map<String, BigDecimal> resultMap = new HashMap<>();
		String query = "SELECT SUM(CONTRIBUTO_RICHIESTO) as spesa_richiesta, " +
				"SUM(COSTO_INVESTIMENTO) + SUM(SPESE_GENERALI) as contributo_richiesto " +
				"FROM PIANO_INVESTIMENTI " +
				"WHERE ID_PROGETTO = ?1 AND ID_VARIANTE = ?2";

		List resultList = em.createNativeQuery(query)
				.setParameter(1, idProgetto)
				.setParameter(2, idVariante)
				.getResultList();
		if (resultList.size() > 0) {
			Object[] values = (Object[]) resultList.get(0);
			resultMap.put("spesaRichiesta", (BigDecimal) values[1]);
			resultMap.put("contributoRichiesto", (BigDecimal) values[0]);
		}
		return resultMap;
	}

	private void popolaDomandaPsrTipoSaldo(final PsrDomandeDto psr, boolean isPremioContoCapitale,
			final PsrQueryResult queryRes) throws Exception {
		PsrAccontoSaldoDto saldo = new PsrAccontoSaldoDto();
		psr.setSaldo(saldo);
		if (isPremioContoCapitale) {
			Integer idProgetto = psr.getIdProgetto();
			saldo.popolaTipoDomanda(
					queryRes.idDomandaPagamento,
					queryRes.pagamentoImportoRichiesto,
					getSaldoContributoRichiesto(idProgetto),
					getSaldoContributoLiquidabile(idProgetto),
					queryRes.pagamentoData,
					queryRes.socNumeroDomanda);
		} else {
			saldo.popolaTipoDomanda(
					queryRes.idDomandaPagamento,
					queryRes.pagamentoImportoRichiesto,
					queryRes.pagamentoContributoRichiesto,
					queryRes.pagamentoContributoAmmesso,
					queryRes.pagamentoData,
					queryRes.socNumeroDomanda);
		}
		popolaSanzioniDomanda(saldo, saldo.getIdDomandaPagamento());
	}

	private void popolaDomandaPsrTipoAnticipo(final PsrDomandeDto psr, final PsrQueryResult queryRes) {
		if (queryRes.anticipoRichiestaData != null) {
			PsrAnticipoDto anticipo = new PsrAnticipoDto();
			psr.setAnticipo(anticipo);
			anticipo.setIdDomandaPagamento(queryRes.idDomandaPagamento);
			anticipo.setData(queryRes.anticipoRichiestaData);
			anticipo.setAnticipoRichiesto(queryRes.anticipoContributoRichiesto);
			anticipo.setAnticipoLiquidabile(queryRes.anticipoContributoAmmesso);
			anticipo.setData(queryRes.pagamentoData);
			anticipo.setSocNumeroDomanda(queryRes.socNumeroDomanda);
		}
	}

	private void popolaDomandaPsrPerTipo(final PsrDomandeDto psr,
			boolean isPremioContoCapitale, final PsrQueryResult queryRes) throws Exception {
		final List<PsrAccontoSaldoDto> acconti = psr.getAcconti();
		if (isDomandaValidAndIstruita(queryRes)) {
			return;
		}
		switch (queryRes.tipoDomanda) {
		case "SLD": // saldo
			popolaDomandaPsrTipoSaldo(psr, isPremioContoCapitale, queryRes);
			break;
		case "SAL": // acconto
			popolaDomandaPsrTipoAcconto(queryRes, acconti);
			break;
		case "ANT": // anticipo
			popolaDomandaPsrTipoAnticipo(psr, queryRes);
			break;
		default:
			throw new Exception("Tipo domanda di PSR non gestita");
		}
	}

	private boolean isDomandaValidAndIstruita(final PsrQueryResult queryRes) {
		return queryRes.tipoDomanda == null || (queryRes.approvata == null && queryRes.segnaturaApprovazione == null);
	}

	private void popolaDomandaPsr(final PsrDomandeDto psr, boolean isPremioContoCapitale, final Object[] res) throws Exception {
		PsrQueryResult queryRes = new PsrQueryResult(res);
		if (psr.getIdBando() == null) {
			psr.setIdBando(queryRes.idBando);
		}
		if (psr.getCodiceStatoProgetto() == null) {
			psr.setCodiceStatoProgetto(queryRes.codiceStatoProgetto);
		}
		if (psr.getStatoProgetto() == null) {
			psr.setStatoProgetto(queryRes.statoProgetto);
			if (!(queryRes.statoProgetto.equals("Anticipo") ||
					queryRes.statoProgetto.equals("Acconto") ||
					queryRes.statoProgetto.equals("Saldo") ||
					queryRes.statoProgetto.equals("Finanziabile"))) {
				psr.getFinanziabilita().setData(queryRes.dataIstruttoria);
			} else {
				psr.getFinanziabilita().setData(queryRes.dataFinanziabilita);
			}
		}
		if (psr.getDataProtocollazione() == null) {
			PsrProtocolloDataProgetto datiProt = PsrProtocolloDataProgetto.parseSegnaturaProgetto(queryRes.segnatura);
			psr.setProtocolloProgetto(datiProt.getProtocollo());
			psr.setDataProtocollazione(datiProt.getData());
		}
		if (psr.getIsFinanziatoPat() == null) {
			psr.setIsFinanziatoPat(queryRes.finanziatoPat);
		}
        if (psr.getCodice() == null) {
            psr.setCodice(queryRes.codice);
        }
		popolaDomandaPsrPerTipo(psr, isPremioContoCapitale, queryRes);
	}

	public PsrDomandeDto getDettaglioDomandeByIdProgetto(Integer idProgetto) throws Exception {
		StoredProcedureQuery query = em.createStoredProcedureQuery("SpPsrDettaglioProgetto");
		query.registerStoredProcedureParameter("ID_PROGETTO", Integer.class, ParameterMode.IN);
		query.setParameter("ID_PROGETTO", idProgetto);
		if (!query.execute()) {
			return null;
		}
		List<Object[]> resultList = (List<Object[]>)query.getResultList();
		PsrDomandeDto psr = new PsrDomandeDto();
		psr.setIdProgetto(idProgetto);
		boolean isPremioContoCapitale = getIsPremioContoCapitale(idProgetto);

		List<PsrAccontoSaldoDto> acconti = new ArrayList<>();
		psr.setAcconti(acconti);
		List<PsrCostiContributiDto> costiContributiList = getRiepilogoCostiContributi(idProgetto);
		PsrFinanziabilitaDto finanziabilita = new PsrFinanziabilitaDto();
		if (!costiContributiList.isEmpty()) {
			PsrCostiContributiDto costiContributi = costiContributiList.get(0);
			finanziabilita.setContributoAmmesso(costiContributi.getContributoTotale());
			finanziabilita.setCostoRichiesto(getCostoTotaleProgetto(idProgetto));
		}
		psr.setFinanziabilita(finanziabilita);
		List<PsrProgettoCostiInvestimentiDto> costiInvestimenti = getPsrInvestimentiContributiByIdProgetto(idProgetto);
		BigDecimal contributoRichiesto = BigDecimal.ZERO;
		for (PsrProgettoCostiInvestimentiDto costoInvestimento : costiInvestimenti) {
			contributoRichiesto = PsrPagamentoDto.addIfNotNull(contributoRichiesto, costoInvestimento.getContributoRichiesto());
		}
		finanziabilita.setContributoRichiesto(contributoRichiesto);
		for (Object[] res : resultList) {
			popolaDomandaPsr(psr, isPremioContoCapitale, res);
		}
		if (psr.getCodiceStatoProgetto().toString().matches("A|F")) {
			// riscrivi i valori di Costo richiesto, contributo richiesto, contributo ammesso
			populateValoreStoredProcedureFinanziabilitaPerIdProgetto(
					"SpA4gPsrCostiContributi_AF", idProgetto, psr, false);
		} else if (psr.getCodiceStatoProgetto().toString().matches("L|I|Q|R|B")) {
			// riscrivi i valori di Costo richiesto, contributo richiesto, contributo ammesso
			populateValoreStoredProcedureFinanziabilitaPerIdProgetto(
					"SpA4gPsrCostiContributi_LIQRB", idProgetto, psr, true);
		}
		if ('H' == psr.getCodiceStatoProgetto()) {
			List<Pair<Character, String>> storico = this.getPsrStoricoStati(psr.getIdProgetto());
			if (storico.size() > 1) {
				Pair<Character, String> rightState = storico.get(1);
				psr.setCodiceStatoProgetto(rightState.getFirst());
				psr.setStatoProgetto(rightState.getSecond());
			}
		}
        setContributoRichiestoIfCodiceIs611(psr);
		return psr;
	}

	private static final String SELECT_DETTAGLIO_FINANZIABILITA_BY_PROGETTO = "SELECT\n" +
            "PINV.ID_INVESTIMENTO as ID,\n" +
            "PINV.ID_INVESTIMENTO_ORIGINALE,\n" +
            "cin.DESCRIZIONE as DESC_CODIFICA,\n" +
            "din.DESCRIZIONE as DESC_INVESTIMENTO,\n" +
            "SUM(CASE WHEN PINV.ID_INVESTIMENTO_ORIGINALE IS NULL THEN COSTO_INVESTIMENTO + SPESE_GENERALI ELSE 0 END) AS CONTRIBUTO_INVESTIMENTO_RICHIESTO,\n" +
            "SUM(CASE WHEN PINV.ID_INVESTIMENTO_ORIGINALE IS NULL THEN CONTRIBUTO_RICHIESTO ELSE 0 END) AS CONTRIBUTO_RICHIESTO,\n" +
            "SUM(CASE WHEN PINV.ID_INVESTIMENTO_ORIGINALE IS NOT NULL THEN CONTRIBUTO_RICHIESTO ELSE 0 END) AS CONTRIBUTO_AMMESSO\n" +
            "FROM\n" +
            "PIANO_INVESTIMENTI AS PINV\n" +
            "JOIN CODIFICA_INVESTIMENTO as cin ON\n" +
            "PINV.ID_CODIFICA_INVESTIMENTO = cin.ID_CODIFICA_INVESTIMENTO\n" +
            "JOIN DETTAGLIO_INVESTIMENTI as din ON\n" +
            "pinv.ID_DETTAGLIO_INVESTIMENTO = din.ID_DETTAGLIO_INVESTIMENTO\n" +
            "WHERE\n" +
            "PINV.ID_PROGETTO = ?1\n" +
            "AND PINV.ID_VARIANTE IS NULL\n" +
            "GROUP BY\n" +
            "din.DESCRIZIONE,\n" +
            "cin.DESCRIZIONE,\n" +
            "PINV.ID_INVESTIMENTO,\n" +
            "PINV.ID_INVESTIMENTO_ORIGINALE;";

	private static final String SELECT_DETTAGLIO_SALDO_BY_PROGETTO =
			"SELECT\n" +
			"PINV.ID_INVESTIMENTO as ID,\n" +
			"PINV.ID_INVESTIMENTO_ORIGINALE,\n" +
			"cin.DESCRIZIONE as DESC_CODIFICA,\n" +
			"din.DESCRIZIONE as DESC_INVESTIMENTO,\n" +
			"pr.importo_richiesto as contributo_investimento_richiesto,\n" +
			"pr.contributo_richiesto as contributo_richiesto,\n" +
			"pr.contributo_ammesso as contributo_liquidabile\n" +
			"FROM \n" +
			"domanda_di_pagamento as ddp LEFT JOIN pagamenti_richiesti as pr ON ddp.id_domanda_pagamento = pr.id_domanda_pagamento\n" +
			"LEFT JOIN PIANO_INVESTIMENTI AS PINV ON pr.id_investimento = PINV.id_investimento\n" +
			"LEFT JOIN CODIFICA_INVESTIMENTO AS cin ON PINV.ID_CODIFICA_INVESTIMENTO = cin.ID_CODIFICA_INVESTIMENTO\n" +
			"LEFT JOIN DETTAGLIO_INVESTIMENTI AS din ON pinv.ID_DETTAGLIO_INVESTIMENTO = din.ID_DETTAGLIO_INVESTIMENTO\n" +
			"WHERE\n" +
			"ddp.id_progetto = ?1 AND ddp.cod_tipo = ?2\n" +
			"GROUP BY\n" +
			"ddp.ID_DOMANDA_PAGAMENTO, pr.importo_richiesto, pr.contributo_richiesto, pr.contributo_ammesso, PINV.ID_INVESTIMENTO, PINV.ID_INVESTIMENTO_ORIGINALE, cin.DESCRIZIONE, din.DESCRIZIONE, ddp.id_progetto";

	private static final String SELECT_DETTAGLIO_ACCONTO_BY_PROGETTO =
			"SELECT\n" +
			"PINV.ID_INVESTIMENTO as ID,\n" +
			"PINV.ID_INVESTIMENTO_ORIGINALE,\n" +
			"cin.DESCRIZIONE as DESC_CODIFICA,\n" +
			"din.DESCRIZIONE as DESC_INVESTIMENTO,\n" +
			"pr.importo_richiesto as contributo_investimento_richiesto,\n" +
			"pr.contributo_richiesto as contributo_richiesto,\n" +
			"pr.contributo_ammesso as contributo_liquidabile\n" +
			"FROM \n" +
			"domanda_di_pagamento as ddp LEFT JOIN pagamenti_richiesti as pr ON ddp.id_domanda_pagamento = pr.id_domanda_pagamento\n" +
			"LEFT JOIN PIANO_INVESTIMENTI AS PINV ON pr.id_investimento = PINV.id_investimento\n" +
			"LEFT JOIN CODIFICA_INVESTIMENTO AS cin ON PINV.ID_CODIFICA_INVESTIMENTO = cin.ID_CODIFICA_INVESTIMENTO\n" +
			"LEFT JOIN DETTAGLIO_INVESTIMENTI AS din ON pinv.ID_DETTAGLIO_INVESTIMENTO = din.ID_DETTAGLIO_INVESTIMENTO\n" +
			"WHERE\n" +
			"ddp.id_progetto = ?1 AND ddp.cod_tipo = ?2 AND ddp.id_domanda_pagamento = ?3\n" +
			"GROUP BY\n" +
			"ddp.ID_DOMANDA_PAGAMENTO, pr.importo_richiesto, pr.contributo_richiesto, pr.contributo_ammesso, PINV.ID_INVESTIMENTO, PINV.ID_INVESTIMENTO_ORIGINALE, cin.DESCRIZIONE, din.DESCRIZIONE, ddp.id_progetto";

	public List<PsrInterventoDettaglioDto> getDettaglioByProgettoAndTipo(Integer idProgetto,
																		 TipologiaDatiPagamento tipologiaDatiPagamento,
																		 @Null Integer idDomandaPagamento) throws NoSuchFieldException {

        final List<Object[]> resultList;
        switch (tipologiaDatiPagamento) {
            case FINANZIABILITA:
                resultList = em.createNativeQuery(SELECT_DETTAGLIO_FINANZIABILITA_BY_PROGETTO)
                        .setParameter(1, idProgetto)
                        .getResultList();

                List<PsrInterventoDettaglioDto> psrInterventoDettaglioDtos = mergeDettaglioRowsIfDescriptionsAreTheSame(resultList);
                return psrInterventoDettaglioDtos;
			case ACCONTO:
				resultList = em.createNativeQuery(SELECT_DETTAGLIO_ACCONTO_BY_PROGETTO)
							   .setParameter(1, idProgetto)
							   .setParameter(2, "SAL")
							   .setParameter(3, idDomandaPagamento)
							   .getResultList();
				break;
			case SALDO:
				resultList = em.createNativeQuery(SELECT_DETTAGLIO_SALDO_BY_PROGETTO)
							   .setParameter(1, idProgetto)
							   .setParameter(2, "SLD")
							   .getResultList();
				break;
			default:
				throw new RuntimeException("Not yet implemented");
		}

        return parseResultsToPsrInterventionDetailDto(resultList);
	}

    private List<PsrInterventoDettaglioDto> mergeDettaglioRowsIfDescriptionsAreTheSame(List<Object[]> resultList) throws NoSuchFieldException {
        List<PsrInterventoDettaglioDto> psrInterventoDettaglioDtos = parseResultsToPsrInterventionDetailDto(resultList);
        List<PsrInterventoDettaglioDto> filteredInterventoDettaglioDtos = new LinkedList<>();
        Map<Integer, PsrInterventoDettaglioDto> dettaglioDtoMap = psrInterventoDettaglioDtos.stream().collect(Collectors.toMap(PsrInterventoDettaglioDto::getIdIntervento, Function.identity(), (a, b) -> b));
        for (PsrInterventoDettaglioDto psrInterventoDettaglioDto : psrInterventoDettaglioDtos) {
            if (psrInterventoDettaglioDto.getIdInvestimentoOriginale() != null) {
                PsrInterventoDettaglioDto investimentoOriginale = dettaglioDtoMap.get(psrInterventoDettaglioDto.getIdInvestimentoOriginale());
                if (investimentoOriginale == null) {
                    throw new NoSuchFieldException("Investimento originale con id: " + psrInterventoDettaglioDto.getIdInvestimentoOriginale() + " non presente");
                }
                investimentoOriginale.addCostoInvestimentoRichiesto(psrInterventoDettaglioDto.getCostoInvestimentoRichiesto());
                investimentoOriginale.addContributoAmmesso(psrInterventoDettaglioDto.getContributoAmmesso());
                investimentoOriginale.addContributoRichiesto(psrInterventoDettaglioDto.getContributoRichiesto());
            } else {
                filteredInterventoDettaglioDtos.add(psrInterventoDettaglioDto);
            }
        }
        return filteredInterventoDettaglioDtos;
    }

    private List<PsrInterventoDettaglioDto> parseResultsToPsrInterventionDetailDto(List<Object[]> resultList) {
        List<PsrInterventoDettaglioDto> result = new LinkedList<>();
        for (Object[] item : resultList) {
            PsrInterventoDettaglioDto psrIterventoDettaglioDto = new PsrInterventoDettaglioDto();
            psrIterventoDettaglioDto.setIdIntervento((Integer) item[0]);
            psrIterventoDettaglioDto.setIdInvestimentoOriginale((Integer) item[1]);
            psrIterventoDettaglioDto.setCodifica((String) item[2]);
            psrIterventoDettaglioDto.setDettaglio((String) item[3]);

            psrIterventoDettaglioDto.setCostoInvestimentoRichiesto((BigDecimal) item[4]);
            psrIterventoDettaglioDto.setContributoRichiesto((BigDecimal) item[5]);
            psrIterventoDettaglioDto.setContributoAmmesso((BigDecimal) item[6]);

            result.add(psrIterventoDettaglioDto);
        }
        return result;
    }

    private BigDecimal getCostoTotaleProgetto(Integer idProgetto) {
		Query query = em.createNativeQuery("SELECT dbo.calcoloCostoTotaleProgetto(?,?,?)");
		query.setParameter(1, idProgetto).setParameter(2, 0).setParameter(3, 0);
		BigDecimal costoTotale = (BigDecimal) query.getSingleResult();
		return costoTotale;
	}

	private static final String SELECT_SANZIONI_DOMANDA = "SELECT AMMONTARE, CONVERT(varchar(MAX),DESCRIZIONE) " +
			"FROM VSANZIONI WHERE ID_DOMANDA_PAGAMENTO= ?1";

	private void popolaSanzioniDomanda(PsrAccontoSaldoDto psr, Integer idDomanda) {
		List<Object[]> queryResult = em.createNativeQuery(SELECT_SANZIONI_DOMANDA)
				.setParameter(1, idDomanda)
				.getResultList();
		List<PsrSanzioneDto> psrSanzioneDtoList = new LinkedList<>();
		for (Object[] item : queryResult) {
			PsrSanzioneDto psrSanzioneDto = new PsrSanzioneDto();
			psrSanzioneDto.setAmmontareSanzione((BigDecimal) item[0]);
			psrSanzioneDto.setDescrizioneSanzione((String) item[1]);
			psrSanzioneDtoList.add(psrSanzioneDto);
		}
		psr.setSanzioni(psrSanzioneDtoList);
	}

	private static final String SELECT_FATTURE_INTERVENTO_SALDO =
			"SELECT\n" +
			"PAGAMENTI_RICHIESTI.ID_DOMANDA_PAGAMENTO,\n" +
			"DOMANDA_DI_PAGAMENTO.COD_TIPO,\n" +
			"TIPO_GIUSTIFICATIVO.DESCRIZIONE AS TIPO_FATTURA,\n" +
			"GIUSTIFICATIVI.NUMERO,\n" +
			"GIUSTIFICATIVI.DATA AS DATA_FATTURA,\n" +
			"GIUSTIFICATIVI.DESCRIZIONE AS OGGETTO,\n" +
			"GIUSTIFICATIVI.IMPONIBILE AS VALORE_FATTURA,\n" +
			"PAGAMENTI_BENEFICIARIO.IMPORTO_RICHIESTO AS SPESA_RICHIESTA,\n" +
			"PAGAMENTI_BENEFICIARIO.IMPORTO_AMMESSO AS SPESA_AMMESSA,\n" +
			"PIANO_INVESTIMENTI.QUOTA_CONTRIBUTO_RICHIESTO\n" +
			"FROM PAGAMENTI_BENEFICIARIO\n" +
			"INNER JOIN GIUSTIFICATIVI\n" +
			"ON PAGAMENTI_BENEFICIARIO.ID_GIUSTIFICATIVO = GIUSTIFICATIVI.ID_GIUSTIFICATIVO\n" +
			"INNER JOIN TIPO_GIUSTIFICATIVO\n" +
			"ON GIUSTIFICATIVI.COD_TIPO = TIPO_GIUSTIFICATIVO.COD_TIPO\n" +
			"INNER JOIN PAGAMENTI_RICHIESTI\n" +
			"ON PAGAMENTI_RICHIESTI.ID_PAGAMENTO_RICHIESTO = PAGAMENTI_BENEFICIARIO.ID_PAGAMENTO_RICHIESTO\n" +
			"INNER JOIN PIANO_INVESTIMENTI\n" +
			"ON PAGAMENTI_RICHIESTI.ID_INVESTIMENTO = PIANO_INVESTIMENTI.ID_INVESTIMENTO\n" +
			"INNER JOIN DOMANDA_DI_PAGAMENTO\n" +
			"ON  DOMANDA_DI_PAGAMENTO.ID_DOMANDA_PAGAMENTO = PAGAMENTI_RICHIESTI.ID_DOMANDA_PAGAMENTO\n" +
			"WHERE\n" +
			"DOMANDA_DI_PAGAMENTO.ID_PROGETTO = ?1 AND\n" +
			"DOMANDA_DI_PAGAMENTO.COD_TIPO = ?2 AND \n" +
			"PIANO_INVESTIMENTI.ID_INVESTIMENTO = ?3";

	private static final String SELECT_FATTURE_INTERVENTO_ACCONTO = SELECT_FATTURE_INTERVENTO_SALDO +
			"\n AND DOMANDA_DI_PAGAMENTO.ID_DOMANDA_PAGAMENTO = ?4";

	public List<PsrFatturaDto> getFattureByIdProgettoAndIntervento(Integer idProgetto, TipologiaDatiPagamento tipologia, Integer idIntervento, Integer idDomandaPagamento) {
		List<PsrFatturaDto> result = new LinkedList<>();

		final List<Object[]> resultList;

		switch (tipologia) {
			case ACCONTO:
				resultList = em.createNativeQuery(SELECT_FATTURE_INTERVENTO_ACCONTO)
						.setParameter(1, idProgetto)
						.setParameter(2, "SAL")
						.setParameter(3, idIntervento)
						.setParameter(4, idDomandaPagamento)
						.getResultList();
				break;
			case SALDO:
				resultList = em.createNativeQuery(SELECT_FATTURE_INTERVENTO_SALDO)
						.setParameter(1, idProgetto)
						.setParameter(2, "SLD")
						.setParameter(3, idIntervento)
						.getResultList();
				break;
			default:
				throw new RuntimeException("Not yet implemented");
		}

		for (Object[] item : resultList) {
			PsrFatturaDto psrFatturaDto = new PsrFatturaDto();
			psrFatturaDto.setIdDomandaPagamento((Integer) item[0]);
			psrFatturaDto.setCodifica((String) item[1]);
			psrFatturaDto.setTipoFattura((String) item[2]);
			psrFatturaDto.setNumeroFattura((String) item[3]);
			psrFatturaDto.setDataFattura(LocalDateConverter.from((Timestamp)item[4]));
			psrFatturaDto.setOggetto((String) item[5]);
			psrFatturaDto.setValoreFattura((BigDecimal) item[6]);
			psrFatturaDto.setSpesaRichiesta((BigDecimal) item[7]);
			psrFatturaDto.setSpesaAmmessa((BigDecimal) item[8]);
			psrFatturaDto.setQuotaContributoRichiesto((BigDecimal) item[9]);
			
			result.add(psrFatturaDto);
		}

		return result;
	}

	private static final String SELECT_VARIANTI_DOMANDA = "SELECT ID_VARIANTE, DATA_INSERIMENTO, DATA_MODIFICA, APPROVATA, DATA_APPROVAZIONE, ANNULLATA, COD_TIPO  \n" +
			"FROM VARIANTI \n" +
			"WHERE ANNULLATA = 0 AND \n" +
			"ID_PROGETTO = ?1\n" +
			"ORDER BY DATA_MODIFICA DESC\n" +
			"\n";

	public List<PsrVarianteDto> getVariantiByProgetto(Integer idDomanda) {
		List<Object[]> queryResult = em.createNativeQuery(SELECT_VARIANTI_DOMANDA)
				.setParameter(1, idDomanda)
				.getResultList();
		List<PsrVarianteDto> psrVarianteDtos = new LinkedList<>();
		for (Object[] item : queryResult) {
			PsrVarianteDto psrVarianteDto = new PsrVarianteDto();
			psrVarianteDto.setIdVariante((Integer) item[0]);
			psrVarianteDto.setDataInserimento(((Timestamp) item[1]).toLocalDateTime().toLocalDate());
			psrVarianteDto.setDataModifica(((Timestamp) item[2]).toLocalDateTime().toLocalDate());
			psrVarianteDto.setApprovata((Boolean) item[3]);
			if(item[4] != null) {
				psrVarianteDto.setDataApprovazione(((Timestamp) item[4]).toLocalDateTime().toLocalDate());
			}
			psrVarianteDto.setAnnullata((Boolean) item[5]);
            psrVarianteDto.setCodTipo((String) item[6]);
			psrVarianteDtos.add(psrVarianteDto);
		}
		filterVarianteByCodiceTipo(psrVarianteDtos);
		return psrVarianteDtos;
	}

    private void filterVarianteByCodiceTipo(List<PsrVarianteDto> psrVarianteDtos) {
        psrVarianteDtos.removeIf(psrVarianteDto -> Set.of("AR", "AT", "VI").contains(psrVarianteDto.getCodTipo()));
    }

    private static final String SELECT_INVESTIMENTO_BY_ID_VARIANTE = "SELECT  PV.ID_INVESTIMENTO, PV.ID_PROGETTO, PV.DESCRIZIONE," +
      " PV.COSTO_INVESTIMENTO, PV.SPESE_GENERALI, PV.CONTRIBUTO_RICHIESTO," +
      " PV.QUOTA_CONTRIBUTO_RICHIESTO, PV.ID_VARIANTE, PV.ID_INVESTIMENTO_ORIGINALE," +
      " CI.DESCRIZIONE AS DESCRIZIONE_CODIFICA_INVESTIMENTO," +
      " DI.DESCRIZIONE AS DESCRIZIONE_DETTAGLIO_INVESTIMENTI," +
      " SP.DESCRIZIONE AS DESCRIZIONE_SETTORI_PRODUTTIVI\n" +
      "\tFROM PIANO_INVESTIMENTI AS PV" +
      " LEFT JOIN CODIFICA_INVESTIMENTO AS CI ON PV.ID_CODIFICA_INVESTIMENTO = CI.ID_CODIFICA_INVESTIMENTO" +
      " LEFT JOIN DETTAGLIO_INVESTIMENTI AS DI ON DI.ID_DETTAGLIO_INVESTIMENTO = PV.ID_DETTAGLIO_INVESTIMENTO" + 
      " LEFT JOIN SETTORI_PRODUTTIVI AS SP ON SP.ID_SETTORE_PRODUTTIVO = PV.ID_SETTORE_PRODUTTIVO\n" +
      "\tWHERE PV.ID_VARIANTE = ?1";

	public List<PsrInvestimentoDto> getInvestimentiByIdVariante(Integer idVariante) {
		List<Object[]> queryResult = em.createNativeQuery(SELECT_INVESTIMENTO_BY_ID_VARIANTE)
				.setParameter(1, idVariante)
				.getResultList();
		List<PsrInvestimentoDto> psrInvestimentoDtos = new LinkedList<>();
		for (Object[] item : queryResult) {
			PsrInvestimentoDto psrVarianteDto = new PsrInvestimentoDto();
			psrVarianteDto.setIdInvestimento((Integer) item[0]);
			psrVarianteDto.setIdProgetto((Integer) item[1]);
			psrVarianteDto.setDescrizione((String) item[2]);
			psrVarianteDto.setCostoInvestimento((BigDecimal) item[3]);
			psrVarianteDto.setSpeseGenerali((BigDecimal) item[4]);
			psrVarianteDto.setContributoRichiesto((BigDecimal) item[5]);
			psrVarianteDto.setQuotaContributoRichiesto((BigDecimal) item[6]);
			psrVarianteDto.setIdVariante((Integer) item[7]);
			psrVarianteDto.setIdInvestimentoOriginale((Integer) item[8]);
			psrVarianteDto.setDescrizioneCodificaInvestimento((String) item[9]);
			psrVarianteDto.setDescrizioneDettaglioInvestimenti((String) item[10]);
			psrVarianteDto.setDescrizioneSettoriProduttivi((String) item[11]);
			
			psrInvestimentoDtos.add(psrVarianteDto);
		}
		return psrInvestimentoDtos;
	}

    private static final String  SELECT_CUAA_BY_ID_PROGETTO = "SELECT i.CUAA FROM vPROGETTO vp JOIN IMPRESA i ON i.ID_IMPRESA = vp.ID_IMPRESA WHERE ID_PROGETTO = ?1";

    public String getCuaaByIdProgetto(Integer idProgetto) {
        Object queryResult = em.createNativeQuery(SELECT_CUAA_BY_ID_PROGETTO)
                .setParameter(1, idProgetto)
                .getSingleResult();
        return (String) queryResult;
    }
}
