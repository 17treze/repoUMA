package it.tndigitale.a4gistruttoria.service.businesslogic.statistica;

import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.PassoTransizioneDao;
import it.tndigitale.a4gistruttoria.repository.model.*;
import it.tndigitale.a4gistruttoria.repository.model.AmbitoCampione;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.TipologiaStatistica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;


abstract class GeneraStatisticaCs27 extends GeneraStatisticaBase {
	
	private static final Logger logger = LoggerFactory.getLogger(GeneraStatisticaCs27.class);
	
	@Autowired
	private PassoTransizioneDao passiLavSostegnoDao;

	protected abstract TipologiaPassoTransizione getCodicePassoByIdentificativoSostegno();

	protected abstract Map<String, String> getSuffissoVariabileMisura();
	
	@Override
	public TipologiaStatistica getTipoDatoAnnuale() {
		return TipologiaStatistica.CS27;
	}
	
	@Override
	protected String getSigla() {
		return "CS-27";
	}

	@Override
	protected StatisticheInputData caricaDatiInput(IstruttoriaModel istruttoria, Integer annoCampagna) {
		StatisticheInputData result = super.caricaDatiInput(istruttoria, annoCampagna);
		try {
			List<PassoTransizioneModel> passi = new ArrayList<>();
			recuperaUltimoPassoLavorazioneCalcoloPremio(istruttoria).ifPresent(passi::add);
			recuperaPassoDisciplinaFinanziaria(istruttoria).ifPresent(passi::add);
			result.setPassiLavorazioneEntities(passi);
			result.setVariabiliCalcolo(recuperaValoriVariabili(result.getPassiLavorazioneEntities()));
			result.setConfRicevibilita(confIstruttoriaService.getConfIstruttoriaRicevibilita(annoCampagna));
			return result;
		} catch (Exception e) {
			logger.warn("Errore recupero dati input per istruttoria ".concat(istruttoria.getId().toString()), e);
		}
		return result;
	}
	
	protected Float getQuantitaNonPagataASeguitoDiControlli(Float impAmm, Float impRidRit) {
		BigDecimal var1 = BigDecimal.valueOf(impAmm);
		BigDecimal var2 = BigDecimal.valueOf(impRidRit);
		return var1.subtract(var2).setScale(4, RoundingMode.HALF_UP).floatValue();
	}
	
	protected String getMetodoSelezioneControlliInLocoAccoppiati(CampioneModel campione) {
		if (campione == null || campione.getAmbitoCampione() == null) {
			return null;
		}
		try {
			if (AmbitoCampione.SUPERFICIE.equals(campione.getAmbitoCampione())
					|| AmbitoCampione.ZOOTECNIA.equals(campione.getAmbitoCampione())) {
				if (CampioneStatistico.CASUALE.equals(campione.getCampioneStatistico())) {
					return "1";
				} else if (CampioneStatistico.RISCHIO.equals(campione.getCampioneStatistico())) {
					return "2";
				} else if (CampioneStatistico.MANUALE.equals(campione.getCampioneStatistico())) {
					return "3";
				}
			} else {
				return "N";
			}
		} catch (Exception e) {
			logger.error("Errore getMetodoSelezioneControlliInLocoAccoppiati", e);
		}
		return null;
	}
	
	protected TipoVariabile getTipo(String prefisso, String suffisso) {
		return TipoVariabile.valueOf(prefisso.concat("_").concat(suffisso));
	}
	
	private Optional<PassoTransizioneModel> recuperaUltimoPassoLavorazioneCalcoloPremio(IstruttoriaModel istruttoria) {
		try {
			TransizioneIstruttoriaModel ultimaTransizione =
					transizioneService.caricaUltimaTransizione(istruttoria, recuperaStatoLavorazione(istruttoria));
			return ultimaTransizione.getPassiTransizione().stream()
					.filter(passo -> passo.getCodicePasso().equals(getCodicePassoByIdentificativoSostegno()))
					.max(Comparator.comparing(PassoTransizioneModel::getId));
		} catch (EntityNotFoundException notFound) {
			logger.debug("Recupero dei passi lavorazione di calcolo per l'istruttoria {} non riuscito. Probabile non sia stato ancora lanciato il calcolo", istruttoria.getId());			
		} catch (Exception e) {
			logger.warn("Errore generico nel recupero dei passi lavorazione di calcolo per l'istruttoria {}", istruttoria.getId(),e);
		}
		return Optional.empty();
	}
	
	/**
	 * Recupera e aggrega (somma) le potenzialmente multiple variabili per tutti gli interventi
	 * associati alla misura, se esistono.
	 * P.es.: per la misura M19 a cui sono associati gli interventi 316 e 318
	 * recupero tutte le variabili e le sommo tra di loro
	 */
	protected Float getValoreVariabileMisuraOrDefault(Map<TipoVariabile, VariabileCalcolo> variabili, String prefissoVariabile, String misura) {
		return keys(getSuffissoVariabileMisura(), misura).map(intervento -> {
			TipoVariabile tipoVariabile = TipoVariabile.valueOf(prefissoVariabile.concat("_").concat(intervento));
			return variabili.get(tipoVariabile) != null ? variabili.get(tipoVariabile).getValNumber() : BigDecimal.ZERO;
		}).reduce(BigDecimal.ZERO, BigDecimal::add).floatValue();
	}

	protected <K, V> Stream<K> keys(Map<K, V> map, V value) {
		return map
				.entrySet()
				.stream()
				.filter(entry -> value.equals(entry.getValue()))
				.map(Map.Entry::getKey);
	}
	
	@Override
	public void cancellaDatiEsistenti(TipologiaStatistica tipoDatoAnnuale, Integer annoCampagna) {
		statisticheService.cancellaStatisticheEsistenti(getTipoDatoAnnuale(), annoCampagna, getMisure());
	}
	
	protected abstract List<String> getMisure();
}
