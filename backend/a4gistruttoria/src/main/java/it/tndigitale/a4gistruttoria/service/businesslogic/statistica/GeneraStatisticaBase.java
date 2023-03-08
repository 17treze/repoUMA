package it.tndigitale.a4gistruttoria.service.businesslogic.statistica;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.A4gistruttoriaConfigurazione;
import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria.ConfIstruttoriaRicevibilitaDto;
import it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria.ConfIstruttorieDto;
import it.tndigitale.a4gistruttoria.dto.lavorazione.Cuaa;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.CampioneDao;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.NutsDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gdNuts;
import it.tndigitale.a4gistruttoria.repository.model.AmbitoCampione;
import it.tndigitale.a4gistruttoria.repository.model.CampioneModel;
import it.tndigitale.a4gistruttoria.repository.model.CampioneStatistico;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.service.StatisticheService;
import it.tndigitale.a4gistruttoria.service.businesslogic.GeneraDatiAnnualiStrategy;
import it.tndigitale.a4gistruttoria.service.businesslogic.TransizioneIstruttoriaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.configurazioneistruttoria.ConfigurazioneIstruttoriaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.DisaccoppiatoService;
import it.tndigitale.a4gistruttoria.service.businesslogic.domanda.ElaborazioneDomanda;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneDomandaException;
import it.tndigitale.a4gistruttoria.util.CustomCollectors;
import it.tndigitale.a4gistruttoria.util.NumberUtil;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.TipologiaStatistica;

public abstract class GeneraStatisticaBase 
	implements GeneraDatiAnnualiStrategy<TipologiaStatistica>, ElaborazioneDomanda{
	
	private static final Logger logger = LoggerFactory.getLogger(GeneraStatisticaBase.class);
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private A4gistruttoriaConfigurazione configurazione;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	NutsDao nutsDao;
	
	@Autowired
	DomandeService domandeService;
	
	@Autowired
	TransizioneIstruttoriaService transizioneService;

	@Autowired
	DomandaUnicaDao domandaDao;
	
	@Autowired
	IstruttoriaDao istruttoriaDao;
	
	@Autowired
	CampioneDao campioneDao;
	
	@Autowired
	StatisticheService statisticheService;
	@Autowired
	IstruttoriaComponent istruttoriaComponent;
	@Autowired 
	DisaccoppiatoService disaccoppiatoService;
	@Autowired
	protected ConfigurazioneIstruttoriaService confIstruttoriaService;
	
	protected abstract String getSigla();

	@Override
	public void cancellaDatiEsistenti(TipologiaStatistica tipoDatoAnnuale, Integer annoCampagna) {
		statisticheService.cancellaStatisticheEsistenti(getTipoDatoAnnuale(), annoCampagna, null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<IstruttoriaModel> caricaIstruttorie(Integer annoCampagna) {
		List<IstruttoriaModel> istruttorie = new ArrayList<IstruttoriaModel>();
		List<DomandaUnicaModel> domande = domandaDao.findByCampagna(annoCampagna);

		if (domande == null || domande.isEmpty()) {
			return istruttorie;
		}

		logger.info("per ogni domanda [# {}] dell'anno di campagna {} carico le istruttorie relative al sostegno {}",
				domande.size(), annoCampagna, getSostegno());

		for (DomandaUnicaModel domanda : domande) {
			IstruttoriaModel ultimaIstruttoria = istruttoriaComponent.getUltimaIstruttoria(domanda, getSostegno());

			if (ultimaIstruttoria != null) {
				istruttorie.add(ultimaIstruttoria);
			}
		}
		logger.info("completato caricamento delle relative istruttorie [# {}]", istruttorie.size());

		return istruttorie;
	}

	protected abstract Sostegno getSostegno();

	protected List<PassoTransizioneModel> recuperaPassiLavorazioneDomanda(IstruttoriaModel istruttoria) throws Exception {
		StatoIstruttoria stato = recuperaStatoLavorazione(istruttoria);
		List<PassoTransizioneModel> result =
				recuperaPassiLavorazioneControlli(istruttoria, stato);
		PassoTransizioneModel passoDisciplina = recuperaPassoDisciplinaFinanziaria(istruttoria).orElse(null);
		if (passoDisciplina != null)
			result.add(passoDisciplina);
		return result;
	}

	protected StatoIstruttoria recuperaStatoLavorazione(IstruttoriaModel istruttoria) {
		StatoIstruttoria result =
				StatoIstruttoria.valueOfByIdentificativo(istruttoria.getA4gdStatoLavSostegno().getIdentificativo());

		if (StatoIstruttoria.CONTROLLI_CALCOLO_OK.compareTo(result) == 0) {
			return StatoIstruttoria.CONTROLLI_CALCOLO_OK;
		}

		if (StatoIstruttoria.NON_AMMISSIBILE.compareTo(result) < 0) {
			return StatoIstruttoria.CONTROLLI_CALCOLO_OK;
		} else {
			return StatoIstruttoria.CONTROLLI_CALCOLO_KO;
		}
	}

	protected List<PassoTransizioneModel> recuperaPassiLavorazioneControlli(
			IstruttoriaModel istruttoria, StatoIstruttoria stato) throws Exception {
		List<PassoTransizioneModel> passi = new ArrayList<>();
		try {
			TransizioneIstruttoriaModel transizione = transizioneService.caricaUltimaTransizione(istruttoria, stato);
			passi = new ArrayList<>(transizione.getPassiTransizione());
			if (passi.isEmpty())
				throw new EntityNotFoundException(String.format("Non ci sono passi lavorazione validi per l'istruttoria id: %d", istruttoria.getId()));
			return passi;
		} catch (EntityNotFoundException e) {
			logger.debug("Errore caricando l'ultima transizione per l'istruttoria {}: {}", istruttoria.getId(), e.getMessage());
		} catch (Exception e) {
			logger.error("recuperaPassiLavorazione: errore caricando le transizioni e i passi di lavorazione dell'istruttoria " + istruttoria.getId(), e);
			throw e;
		}
		return passi;
	}

	protected Optional<PassoTransizioneModel> recuperaPassoDisciplinaFinanziaria(
			IstruttoriaModel istruttoria) throws Exception {
		try {
			TransizioneIstruttoriaModel transizione =
					transizioneService.caricaUltimaTransizione(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
			List<PassoTransizioneModel> passiDisciplinaFinanziaria = transizione.getPassiTransizione().stream()
					.filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA))
					.collect(Collectors.toList());
			return Optional.ofNullable(passiDisciplinaFinanziaria.isEmpty() ? null : passiDisciplinaFinanziaria.get(0));
		} catch (EntityNotFoundException e) {
			logger.debug("Non esistono transizioni / passi per il recupero della disciplina finanziaria per l'istruttoria {}",
					istruttoria.getId(), e);
		} catch (Exception e) {
			logger.error("Errore nel recupero del passo DISCIPLINA_FINANZIARIA per l'istruttoria {}",
					istruttoria.getId(), e);
			throw e;
		}
		return Optional.empty();
	}

	protected Cuaa getInfoAnagraficheCuaa(String cuaaIntestatario) throws Exception {
		Cuaa result = new Cuaa();
		try {
			String resource = configurazione.getUriAgs().concat(ApiUrls.AGS_CUAA).concat("?cuaa=").concat(cuaaIntestatario);
			result = restTemplate.getForObject(new URI(resource), Cuaa.class);
		} catch (Exception e) {
			logger.warn("Errore recupero informazioni anagrafiche per il cuaa {}", cuaaIntestatario);
		}
		return result;
	}

	protected String getNumeroDomandaFormattato(Integer annoCampagna, Long idDomanda) {
		// TODO Lorenzo per mia ignoranza lascio intValue anche se secondo me si puo' togliere
		return String.format("009%d00%d", annoCampagna.intValue(), idDomanda);
	}
	
	protected String getRitardoPresentazioneDomanda(LocalDate dataProtocollazione,ConfIstruttorieDto confInstruttorieDto, ConfIstruttoriaRicevibilitaDto ricevibilita) {
		if (dataProtocollazione.compareTo(confInstruttorieDto.getDtScadenzaDomandeIniziali()) <= 0) {
			return "1";
		} else if (dataProtocollazione.compareTo(ricevibilita.getDataScadenzaDomandaInizialeInRitardo()) <= 0) {
			return "2";
		} else {
			return "3";
		}
	}
	
	protected String getObblighiInverdimento(Boolean greAziBio, Boolean greEseSem,BigDecimal gresuppp) {
		if (greAziBio) { 
			return "3";
		} else if ((greEseSem != null && greEseSem == false) || BigDecimal.ZERO.compareTo(BigDecimal.valueOf(NumberUtil.round(gresuppp.doubleValue(),2))) < 0) {
			return "1";
		} else {
			return "2";
		}
	}
	
	protected Float getQuantitaMisurata(Long numeroDomanda, Float bpsSupImp, Float bpsSupSigeco, Boolean isCamp, Boolean domControlloChiuso, Float bpsSupDetIst, Float bpsSupEle) {
		
		boolean domCampione =  disaccoppiatoService.isCampione(domandaDao.findByNumeroDomanda(BigDecimal.valueOf(numeroDomanda)));
		if (domCampione) {
			if (isCamp.booleanValue()) {
				if (domControlloChiuso.booleanValue()) {
					return bpsSupDetIst != null ? bpsSupDetIst : bpsSupSigeco;
				} else {
					return bpsSupImp;
				}
			} else {
				return bpsSupDetIst != null ? bpsSupDetIst : bpsSupEle;
			}
		}else {
			return null;
		}
	}
	
	protected Float getQuantitaDeterminata(Float supAmm, Float supRic, Boolean isCamp, Boolean domSigecoChiusa) {
		if (isCamp && !domSigecoChiusa) {
			return supRic;
		} else {
			return supAmm;
		}
	}

	protected Float getImportoQuantitaDeterminata(Float impAmm, Float impRidRit, Float impRic, Boolean isCamp, Boolean domSigecoChiusa) {
		if (isCamp && !domSigecoChiusa) {
			return impRic;
		} else {
			if (impAmm.equals(0f))
				return BigDecimal.ZERO.floatValue();
			BigDecimal var1 = BigDecimal.valueOf(impAmm);
			BigDecimal var2 = BigDecimal.valueOf(impRidRit);
			return var1.subtract(var2).setScale(4, RoundingMode.HALF_UP).floatValue();
		}
	}
	
	protected Float getSanzioneIrrogata(Float bpsImpSanz, Float bpsImpSanzRec) {
		BigDecimal var1 = BigDecimal.valueOf(bpsImpSanz);
		BigDecimal var2 = BigDecimal.valueOf(bpsImpSanzRec);
		return var1.add(var2).setScale(4, RoundingMode.HALF_UP).negate().floatValue();
	}
	
	protected String getControlloInLoco(CampioneModel campione, Boolean flagConv) {
		if (campione != null && campione.getAmbitoCampione() != null) {
			try {
				if (AmbitoCampione.SUPERFICIE.equals(campione.getAmbitoCampione())) {
					if (flagConv) {
						return "FT";
					} else {
						return "T";
					}
				} else {
					return "N";
				}
			} catch (Exception e) {
				logger.error("Errore getControlloInLoco", e);
			}
		}
		return "N";
	}
	
	protected String getMetodoSelezioneControlliInLoco(CampioneModel campione) {
		if (campione != null && campione.getAmbitoCampione() != null) {
			try {
				if (AmbitoCampione.SUPERFICIE.equals(campione.getAmbitoCampione())) {
					if (CampioneStatistico.CASUALE.equals(campione.getCampioneStatistico())) {
						return "1"; 
					} else if (CampioneStatistico.RISCHIO.equals(campione.getCampioneStatistico())) {
						return "2";
					} else if (CampioneStatistico.MANUALE.equals(campione.getCampioneStatistico())) {
						return "3";
					}
				} else {
					return null;
				}
			} catch (Exception e) {
				logger.error("Errore getMetodoSelezioneControlliInLoco", e);
			}
		}
		return null;
	}
	
	protected A4gdNuts getNutsPerProvincia(Cuaa infoCuaa) {
		if (infoCuaa != null && infoCuaa.getSiglaProvRecapito() != null)
			return nutsDao.findBySiglaProvincia(infoCuaa.getSiglaProvRecapito())
				.orElseThrow(() -> new EntityNotFoundException("Impossibile recuperare il codice NUTS3 associato alla provincia ".concat(infoCuaa.getSiglaProvRecapito())));
		return new A4gdNuts();
	}

	protected Map<TipoVariabile, VariabileCalcolo> recuperaValoriVariabili(List<PassoTransizioneModel> passiBps) {
		EnumMap<TipoVariabile, VariabileCalcolo> result = new EnumMap<>(TipoVariabile.class);
		try {
			result.put(TipoVariabile.BPSIMPAMM, getVariabile(TipoVariabile.BPSIMPAMM, passiBps, TipologiaPassoTransizione.RIDUZIONI_BPS).orElse(null)); // CONTROLLI_FINALI
			result.put(TipoVariabile.GREAZIBIO, getVariabile(TipoVariabile.GREAZIBIO, passiBps, TipologiaPassoTransizione.GREENING, false, true).orElse(null));
			result.put(TipoVariabile.GREESESEM, getVariabile(TipoVariabile.GREESESEM, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.GREESEDIV, getVariabile(TipoVariabile.GREESEDIV, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.BPSSUPIMP, getVariabile(TipoVariabile.BPSSUPIMP, passiBps, TipologiaPassoTransizione.AMMISSIBILITA).orElse(null));
			result.put(TipoVariabile.BPSSUPRIC, getVariabile(TipoVariabile.BPSSUPRIC, passiBps, TipologiaPassoTransizione.AMMISSIBILITA).orElse(null));
			result.put(TipoVariabile.BPSIMPRIC, getVariabile(TipoVariabile.BPSIMPRIC, passiBps, TipologiaPassoTransizione.AMMISSIBILITA).orElse(null));
			result.put(TipoVariabile.BPSSUPSIGECO, getVariabile(TipoVariabile.BPSSUPSIGECO, passiBps, TipologiaPassoTransizione.RIDUZIONI_BPS).orElse(null));
			result.put(TipoVariabile.ISCAMP, getVariabile(TipoVariabile.ISCAMP, passiBps, TipologiaPassoTransizione.RIDUZIONI_BPS).orElse(null));
			result.put(TipoVariabile.DOMSIGECOCHIUSA, getVariabile(TipoVariabile.DOMSIGECOCHIUSA, passiBps, TipologiaPassoTransizione.RIDUZIONI_BPS).orElse(null));
			result.put(TipoVariabile.BPSSUPAMM, getVariabile(TipoVariabile.BPSSUPAMM, passiBps, TipologiaPassoTransizione.RIDUZIONI_BPS).orElse(null));
			result.put(TipoVariabile.BPSIMPRIDRIT, getVariabile(TipoVariabile.BPSIMPRIDRIT, passiBps, TipologiaPassoTransizione.CONTROLLI_FINALI).orElse(null));	// RIDUZIONI_BPS? 
			result.put(TipoVariabile.BPSIMPSANZ, getVariabile(TipoVariabile.BPSIMPSANZ, passiBps, TipologiaPassoTransizione.SANZIONI_BPS).orElse(null));
			result.put(TipoVariabile.BPSIMPSANZREC, getVariabile(TipoVariabile.BPSIMPSANZREC, passiBps, TipologiaPassoTransizione.SANZIONI_BPS).orElse(null));
			result.put(TipoVariabile.BPSSUPDETIST, getVariabile(TipoVariabile.BPSSUPDETIST, passiBps, TipologiaPassoTransizione.RIDUZIONI_BPS).orElse(null));
			result.put(TipoVariabile.BPSSUPELE, getVariabile(TipoVariabile.BPSSUPELE, passiBps, TipologiaPassoTransizione.RIDUZIONI_BPS).orElse(null));
		} catch (Exception e) {
			logger.debug("Errore in recupero variabili", e);
		}
		return result;
	}
	
	protected Optional<VariabileCalcolo> getVariabile(
			TipoVariabile tipoVariabile,
			List<PassoTransizioneModel> passi,
			TipologiaPassoTransizione codicePasso,
			Boolean required,
			Boolean onlyDatiOutput) throws Exception {
		try {
			PassoTransizioneModel passo = passi.stream()
					.filter(p -> p.getCodicePasso().equals(codicePasso))
					.collect(CustomCollectors.toSingleton());
			DatiInput input = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
			DatiOutput output = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

			if (onlyDatiOutput.booleanValue()) {
				return Optional.ofNullable(output.getVariabiliCalcolo().stream()
						.filter(p -> p.getTipoVariabile().equals(tipoVariabile))
						.collect(CustomCollectors.toSingleton()));	
			} else {
				return Optional.ofNullable(Stream.concat(input.getVariabiliCalcolo().stream(), output.getVariabiliCalcolo().stream())
						.filter(p -> p.getTipoVariabile().equals(tipoVariabile))
						.collect(CustomCollectors.toSingleton()));		
			}
		} catch (Exception e) {
			logger.debug("Variabile {} [required? {}] non presente al passo {}", tipoVariabile.name(), required, codicePasso);
			if (required.booleanValue())
				throw e;
		}
		return Optional.empty();
	}
	
	protected Optional<VariabileCalcolo> getVariabile(TipoVariabile tipoVariabile,List<PassoTransizioneModel> passi,TipologiaPassoTransizione codicePasso,Boolean required) throws Exception {
		// Chiamata quando ci sono dei TipoVariabile replicati sia in datiInput che in DatiOutput.
		// se true prende solo datiOutput
		// se false prende datiInput e datiOutput (default)
		return getVariabile(tipoVariabile, passi, codicePasso, required, false);
	}
	
	protected Optional<VariabileCalcolo> getVariabile(TipoVariabile tipoVariabile, List<PassoTransizioneModel> passi, TipologiaPassoTransizione codicePasso) throws Exception {
		// Se non riesco a recuperare il valore univoco di una variabile, di default 
		// la considero "non bloccante" (vado avanti con l'elaborazione della riga per la domanda) 
		return getVariabile(tipoVariabile, passi, codicePasso, false);
	}
	
	protected <T> T getValoreVariabilePrecaricata(Map<TipoVariabile, VariabileCalcolo> variabili, TipoVariabile tipoVariabile, Class<T> type) {
		return getValoreVariabilePrecaricata(variabili, tipoVariabile, type, true);
	}
	
	protected <T> T getValoreVariabilePrecaricata(Map<TipoVariabile, VariabileCalcolo> variabili, TipoVariabile tipoVariabile, Class<T> type, boolean isDefault) {
		if (variabili.get(tipoVariabile) == null && !isDefault)
			return null;
		
		if (Float.class.isAssignableFrom(type)) {
			Float value = variabili.get(tipoVariabile) != null && variabili.get(tipoVariabile).getValNumber() != null 
					? variabili.get(tipoVariabile).getValNumber().floatValue() : 0;
			return type.cast(value);
		}
		if (Boolean.class.isAssignableFrom(type)) {
			Boolean value = variabili.get(tipoVariabile) != null && variabili.get(tipoVariabile).getValBoolean();
			return type.cast(value);
		}
		if (String.class.isAssignableFrom(type)) {
			String value = variabili.get(tipoVariabile) != null ? variabili.get(tipoVariabile).getValString() : "";
			return type.cast(value);
		}
		if (BigDecimal.class.isAssignableFrom(type)) {
			BigDecimal value = variabili.get(tipoVariabile) != null ? variabili.get(tipoVariabile).getValNumber() : BigDecimal.ZERO;
			return type.cast(value);
		}
		return null;
	}
	
	protected Float toNegativeValue(Float value) {
		BigDecimal v = BigDecimal.valueOf(value);
		return v.negate().floatValue();
	}
	
	protected StatisticheInputData caricaDatiInput(IstruttoriaModel istruttoria, Integer annoCampagna) {
		StatisticheInputData result = new StatisticheInputData();
		try {
			result.setNumeroDomanda(istruttoria.getDomandaUnicaModel().getNumeroDomanda().longValue());
			result.setDataProtocollazione(domandeService.getDataProtocollazioneDomanda(istruttoria.getDomandaUnicaModel()).toLocalDate());
			result.setInfoCuaa(getInfoAnagraficheCuaa(istruttoria.getDomandaUnicaModel().getCuaaIntestatario()));
			result.setNutsEntity(getNutsPerProvincia(result.getInfoCuaa()));
			result.setFlagConv(domandeService.recuperaFlagConvSigeco(istruttoria.getDomandaUnicaModel().getNumeroDomanda()));
			result.setCampioneEntities(campioneDao.findByCuaaAndAnnoCampagna(istruttoria.getDomandaUnicaModel().getCuaaIntestatario(), annoCampagna));
			result.setPassiLavorazioneEntities(recuperaPassiLavorazioneDomanda(istruttoria));
		 	result.setVariabiliCalcolo(recuperaValoriVariabili(result.getPassiLavorazioneEntities()));
		 	result.setConfRicevibilita(confIstruttoriaService.getConfIstruttoriaRicevibilita(annoCampagna));
		 	result.setConfIstruttorieDto(confIstruttoriaService.getConfIstruttorie(annoCampagna));
		} catch (Exception e) {
			logger.warn("Errore recupero dati input per istruttoria ".concat(istruttoria.getId().toString()), e);
		}
		return result;
	}
	
	protected final class StatisticheInputData {

		private Long numeroDomanda;
		private LocalDate dataProtocollazione;
		private Cuaa infoCuaa;
		private A4gdNuts nutsEntity;
		private Boolean flagConv;
		private List<CampioneModel> campioneEntities;
		private List<PassoTransizioneModel> passiLavorazioneEntities;
		private Map<TipoVariabile, VariabileCalcolo> variabiliCalcolo;
		private ConfIstruttoriaRicevibilitaDto confRicevibilita;
		private ConfIstruttorieDto confIstruttorieDto;
		
		
		
		public ConfIstruttorieDto getConfIstruttorieDto() {
			return confIstruttorieDto;
		}

		public void setConfIstruttorieDto(ConfIstruttorieDto confIstruttorieDto) {
			this.confIstruttorieDto = confIstruttorieDto;
		}

		public Long getNumeroDomanda() {
			return numeroDomanda;
		}
		
		public void setNumeroDomanda(Long numeroDomanda) {
			this.numeroDomanda = numeroDomanda;
		}
		
		public LocalDate getDataProtocollazione() {
			return dataProtocollazione;
		}
		
		public void setDataProtocollazione(LocalDate dataProtocollazione) {
			this.dataProtocollazione = dataProtocollazione;
		}
		
		public Cuaa getInfoCuaa() {
			return infoCuaa;
		}
		
		public void setInfoCuaa(Cuaa infoCuaa) {
			this.infoCuaa = infoCuaa;
		}
		
		public A4gdNuts getNutsEntity() {
			return nutsEntity;
		}
		
		public void setNutsEntity(A4gdNuts nutsEntity) {
			this.nutsEntity = nutsEntity;
		}
		
		public Boolean getFlagConv() {
			return flagConv;
		}
		
		public void setFlagConv(Boolean flagConv) {
			this.flagConv = flagConv;
		}
		
		public List<CampioneModel> getCampioneEntities() {
			return campioneEntities;
		}
		
		public void setCampioneEntities(List<CampioneModel> campioneEntities) {
			this.campioneEntities = campioneEntities;
		}
		
		public List<PassoTransizioneModel> getPassiLavorazioneEntities() {
			return passiLavorazioneEntities;
		}
		
		public void setPassiLavorazioneEntities(List<PassoTransizioneModel> passiLavorazioneEntities) {
			this.passiLavorazioneEntities = passiLavorazioneEntities;
		}
		
		public Map<TipoVariabile, VariabileCalcolo> getVariabiliCalcolo() {
			return variabiliCalcolo;
		}
		
		public void setVariabiliCalcolo(Map<TipoVariabile, VariabileCalcolo> variabiliCalcolo) {
			this.variabiliCalcolo = variabiliCalcolo;
		}
		
		public CampioneModel getCampioneSuperficiEntity() {
			if (this.campioneEntities != null && !this.campioneEntities.isEmpty()) {
				return this.campioneEntities.stream()
						.filter(p -> p.getAmbitoCampione().equals(AmbitoCampione.SUPERFICIE))
						.findFirst()
						.orElse(null);
			}
			return null;
		}
		
		public CampioneModel getCampioneZootecniaEntity() {
			if (this.campioneEntities != null && !this.campioneEntities.isEmpty()) {
				return this.campioneEntities.stream()
						.filter(p -> p.getAmbitoCampione().equals(AmbitoCampione.ZOOTECNIA))
						.findFirst()
						.orElse(null);
			}
			return null;
		}

		public ConfIstruttoriaRicevibilitaDto getConfRicevibilita() {
			return confRicevibilita;
		}

		public void setConfRicevibilita(ConfIstruttoriaRicevibilitaDto confRicevibilita) {
			this.confRicevibilita = confRicevibilita;
		}
	}
	
	
	/**
	 * 
	 * Accetta in ingresso un idIstruttoria a differenza della altre implementazioni
	 *
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ElaborazioneDomandaException.class)
	public void elabora(Long idIstruttoria) throws ElaborazioneDomandaException {
		IstruttoriaModel istruttoria = istruttoriaComponent.load(idIstruttoria);
		generaDatiPerIstruttoria(
				istruttoria, 
				istruttoria.getDomandaUnicaModel().getCampagna()
				);
	}

	/**
	 * 
	 * Per non intaccare il codice pre-esistente vengono recuperate tutte le domande e poi 
	 * collezzionati tutti gli ID ISTRUTTORIA da passare al metodo elabora. Questo differisce dalle altre implementazioni 
	 *
	 */
	@Override
	public List<Long> caricaIdDaElaborare(Integer annoCampagna) throws ElaborazioneDomandaException {
		cancellaDatiEsistenti(getTipoDatoAnnuale(), annoCampagna);
		List<IstruttoriaModel> istruttorie = caricaIstruttorie(annoCampagna);
		logger.info("Avvio generazione dati sincronizzazione per {} istruttorie per tipo {} anno {}",
				istruttorie.size(), getTipoDatoAnnuale(), annoCampagna);
		return istruttorie.stream()
				.filter(Objects::nonNull)
				.map(IstruttoriaModel::getId)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
	
}
