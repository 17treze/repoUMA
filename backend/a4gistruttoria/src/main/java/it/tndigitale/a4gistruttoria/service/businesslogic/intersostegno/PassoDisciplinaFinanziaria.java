package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.component.CaricaPremioCalcolatoSostegno;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiPassoLavorazione;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.ConfigurazioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.*;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaboraPassoIstruttoria;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;

public abstract class PassoDisciplinaFinanziaria extends ElaboraPassoIstruttoria {

	private static final Logger logger = LoggerFactory.getLogger(PassoDisciplinaFinanziariaDisaccoppiato.class);
	
	private static final BigDecimal SOGLIA_DISCIPLINA = BigDecimal.valueOf(2000d);
	
	@Autowired
	private TransizioneIstruttoriaDao transizioneIstruttoriaDao;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ConfigurazioneIstruttoriaDao confDao;

	private static Map<Sostegno, TipoVariabile> mappaSostegnoVariabileFranchigia =
			new HashMap<Sostegno, TipoVariabile>();
	static {
		mappaSostegnoVariabileFranchigia.put(Sostegno.DISACCOPPIATO, TipoVariabile.DFFRPAGDIS);
		mappaSostegnoVariabileFranchigia.put(Sostegno.SUPERFICIE, TipoVariabile.DFFRPAGACS);
		mappaSostegnoVariabileFranchigia.put(Sostegno.ZOOTECNIA, TipoVariabile.DFFRPAGACZ);
	}
	
	// Franchigia precedentemente applicata ad i sostegni
	private BiFunction<DomandaUnicaModel, Sostegno, BigDecimal> valorePrecedentePerFranchigia = (domanda, identificativoSostegno) -> {

		// prendo tutte le istruttorie della domanda
		Set<IstruttoriaModel> istruttorieDomande = domanda.getA4gtLavorazioneSostegnos(); 
		
		return istruttorieDomande.stream()
			// voglio le istruttorie PAGATE del mio sostegno 
			.filter(ist -> identificativoSostegno.equals(ist.getSostegno()))
			.filter(ist -> StatoIstruttoria.PAGAMENTO_AUTORIZZATO.getStatoIstruttoria().equals(ist.getA4gdStatoLavSostegno().getIdentificativo()))
//			// MI INTERESSA LA PIU' RECENTE (PAGATA PER SOSTEGNO) -> no le prendo tutte e sommo
//			.sorted(Comparator.comparingLong(IstruttoriaModel::getId).reversed())
//			.findFirst()
			.map(istruttoriaUltimoPagamento -> {
				List<TransizioneIstruttoriaModel> transizioniIntersostegno = transizioneIstruttoriaDao.findTransizioneControlloIntersostegno(istruttoriaUltimoPagamento);
				if (CollectionUtils.isEmpty(transizioniIntersostegno)) {
					return BigDecimal.ZERO;
				}
				return transizioniIntersostegno.stream()
						.max(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione))
						.map(TransizioneIstruttoriaModel::getPassiTransizione)
						.get()
						.stream()
						.filter(passo -> TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA.equals(passo.getCodicePasso()))
						.findAny()
						.map(p -> {   
										try {
											return objectMapper.readValue(p.getDatiOutput(), DatiOutput.class);
										} catch (Exception e) {
											throw new RuntimeException(e);
										}
						})
						.map(x -> x.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(mappaSostegnoVariabileFranchigia.get(identificativoSostegno)))
								.findFirst().get())
								.map(variabileFranchigia -> variabileFranchigia.getValNumber())
						.orElse(BigDecimal.ZERO);
			})
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	};
	
	// Disciplina - Franchigia sostegno accoppiato superficie
	// MIN((DFFR - (DFFRAPPDIS+DFFRAPPACZ+DFFRAPPACS));ACSIMPCALC)
	protected BiFunction<MapVariabili, TipoVariabile, BigDecimal> calcolaFranchigiaSostegno = (variabiliInput, variabileImportoCalcolato) -> {
		return variabiliInput.min(variabileImportoCalcolato,
				(variabiliInput.subtract(TipoVariabile.DFFR, variabiliInput.add(TipoVariabile.DFFRAPPDIS, TipoVariabile.DFFRAPPACZ, TipoVariabile.DFFRAPPACS))));
		
	};
	
	
	public void elaboraPasso(TransizioneIstruttoriaModel transizione) throws Exception {
		IstruttoriaModel istruttoria = transizione.getIstruttoria();
		MapVariabili mapVariabiliInput = creaInizializzaVariabiliInput(istruttoria);
		MapVariabili mapVariabiliOutput = applicaFranchigia(mapVariabiliInput, istruttoria);
		
		DatiPassoLavorazione passo = new DatiPassoLavorazione();
		passo.setIdTransizione(transizione.getId());
		passo.setPasso(getPasso());
		for (VariabileCalcolo v : mapVariabiliInput.getVariabiliCalcolo().values()) {
			passo.getDatiInput().getVariabiliCalcolo().add(v);
		}
		for (VariabileCalcolo v : mapVariabiliOutput.getVariabiliCalcolo().values()) {
			passo.getDatiOutput().getVariabiliCalcolo().add(v);
		}
		passo.setCodiceEsito("no esito");
		passo.setEsito(true);
		salvaPassoLavorazioneSostegno(passo);
	}
	
	protected MapVariabili creaInizializzaVariabiliInput(IstruttoriaModel istruttoria) {
		MapVariabili mapVariabiliInput = new MapVariabili();
		initVariabiliDisciplina(mapVariabiliInput, istruttoria);
		initVariabiliSostegno(mapVariabiliInput, istruttoria);
		initVariabiliFranchigiaGiaApplicata(mapVariabiliInput, istruttoria);
		return mapVariabiliInput;
	}
	
	protected abstract MapVariabili applicaFranchigia(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria);
	
	protected void initVariabiliDisciplina(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria) {
		Double valorePercentualeDaConfigurazione = confDao.findByCampagna(istruttoria.getDomandaUnicaModel().getCampagna())
				.map(ConfigurazioneIstruttoriaModel::getPercentualeDisciplinaFinanziaria)
				.orElseThrow(() -> new RuntimeException("La disciplina finanziaria non è stata configurata per l'anno "
						+ istruttoria.getDomandaUnicaModel().getCampagna()));
		mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFPERC, BigDecimal.valueOf(valorePercentualeDaConfigurazione)));
		mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFFR, SOGLIA_DISCIPLINA));
	}
	
	protected void initVariabiliSostegno(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria) {
		CaricaPremioCalcolatoSostegno caricatorePremi = getCalcolatore();
		TipoVariabile[] variabiliPremi = getVariabiliPremi();
		if (variabiliPremi != null && variabiliPremi.length > 0) {
			Map<TipoVariabile, BigDecimal> importiPremi = 
					caricatorePremi.getImportoPremioCalcolato(istruttoria, variabiliPremi);
			BigDecimal totalePremi = BigDecimal.ZERO;
			BigDecimal premioMisura = BigDecimal.ZERO;
			for (TipoVariabile variabilePremio : variabiliPremi) {
				/**
				 *  Betty 11.06.2020: caso di saldi e integrazioni con 
				 *  premio giovane negativo.
				 *  
				 *  Recupero il premio per la variabile (0 se non presente) 
				 *  e prendo il MAX tra il premio calcolato e 0 
				 */
				premioMisura = importiPremi.getOrDefault(variabilePremio, BigDecimal.ZERO).max(BigDecimal.ZERO);
				TipoVariabile nomeVariabile = mappaTipoVariabile(variabilePremio);
				logger.debug("Trovata variabile {} con valore {}", nomeVariabile, premioMisura);
				mapVariabiliInput.add(new VariabileCalcolo(nomeVariabile, premioMisura));
				totalePremi = totalePremi.add(premioMisura);
			}
			logger.debug("Totale valore {}", totalePremi);
			mapVariabiliInput.add(new VariabileCalcolo(getNomeVariabilePremioTotale(), totalePremi));
		}
	}
	
	protected TipoVariabile mappaTipoVariabile(TipoVariabile nome) {
		return nome;
	}

	protected void initVariabiliFranchigiaGiaApplicata(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFFRAPPDIS, valorePrecedentePerFranchigia.apply(domanda, Sostegno.DISACCOPPIATO)));
		mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFFRAPPACS, valorePrecedentePerFranchigia.apply(domanda, Sostegno.SUPERFICIE)));
		mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFFRAPPACZ, valorePrecedentePerFranchigia.apply(domanda, Sostegno.ZOOTECNIA)));
	}

	@Override
	public TipologiaPassoTransizione getPasso() {
		return TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA;
	}
	
	public abstract CaricaPremioCalcolatoSostegno getCalcolatore();

	public abstract TipoVariabile[] getVariabiliPremi();

	public abstract TipoVariabile getNomeVariabilePremioTotale();
}
