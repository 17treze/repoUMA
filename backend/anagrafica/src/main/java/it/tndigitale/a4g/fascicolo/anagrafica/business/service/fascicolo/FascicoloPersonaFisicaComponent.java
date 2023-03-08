package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.*;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.*;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.PersonaConCaricaService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAnagraficiDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.IndirizzoDto;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.proxy.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class FascicoloPersonaFisicaComponent extends FascicoloAbstractComponent<PersonaFisicaModel> {

	private static final Logger logger = LoggerFactory.getLogger(FascicoloPersonaFisicaComponent.class);
	@Autowired
	private UnitaTecnicoEconomicheDao unitaTecnicoEconomicheDao;
	
	@Autowired
	private DestinazioneUsoDao destinazioneUsoDao;
	
	@Autowired
	private AttivitaAtecoDao attivitaAtecoDao;
	
	@Autowired
	private UtenteComponent utenteComponent;

	@Autowired
	private PersonaFisicaConCaricaDao personaFisicaConCaricaDao;

	@Autowired
	private PersonaConCaricaService personaConCaricaService;

	@Autowired
	private PersonaGiuridicaConCaricaDao personaGiuridicaConCaricaDao;

	@Autowired
	private CaricaDao caricaDao;

	@Autowired
	private FascicoloDao fascicoloDao;
	
	@Autowired
	private DocumentoIdentitaDao documentoIdentitaDao;

	private PersonaFisicaDto personaFisicaDto;

	@Override
	String getCodiceFiscaleDaAnagrafeTributaria() {
		if (personaFisicaDto == null) {
			return null;
		} else return personaFisicaDto.getCodiceFiscale();
	}

	/**
	 * Le verifiche fatte ad oggi sono le seguenti:
	 * - verifico che in camera di commercio sia stato aggiornato il codice fiscale in caso di cambio segnalato da Anagrafe tributaria
	 *
	 * @param personaFisicaDtoFromAT i dati di anagrafe tributaria
	 * @return elenco di anomalie
	 */
	private List<FascicoloCreationAnomalyEnum> verificaAnomaliaCodiceFiscaleATCameraDiCommercio(
			final PersonaFisicaDto personaFisicaDtoFromAT){
		var anomalies = new ArrayList<FascicoloCreationAnomalyEnum>();
		var codiceFiscaleDaAnagrafeTributaria = personaFisicaDtoFromAT.getCodiceFiscale();
		
		String provinciaDaAnagrafeTributaria = null; 
		ImpresaIndividualeDto impresaIndividuale = personaFisicaDtoFromAT.getImpresaIndividuale();
		if (impresaIndividuale != null) {
			provinciaDaAnagrafeTributaria = impresaIndividuale.getSedeLegale().getIndirizzo().getProvincia();			
		} else {
			provinciaDaAnagrafeTributaria = personaFisicaDtoFromAT.getDomicilioFiscale().getProvincia();
		}

		// Chiama Camera di commercio (PARIX)
		var personaFisicaDtoParix = getAnagraficaProxyClient().getPersonaFisicaAnagraficaImpresa(
				codiceFiscaleDaAnagrafeTributaria,
				provinciaDaAnagrafeTributaria);

		/**
		 * Nel caso di cambio di codice fiscale possono esserci due scenari se PARIX non restituisce dati:
		 * - nessuna iscrizione in camera di commercio
		 * - in camera di commercio c'e' il vecchio codice fiscale
		 * Per evitare dubbi faccio la chiamata anche col codice fiscale vecchio.
		 */
		if (!codiceFiscaleDaAnagrafeTributaria.equals(getCodiceFiscaleDaCaa()) && personaFisicaDtoParix == null) {
			personaFisicaDtoParix = getAnagraficaProxyClient().getPersonaFisicaAnagraficaImpresa(
					getCodiceFiscaleDaCaa(), provinciaDaAnagrafeTributaria);
			if (personaFisicaDtoParix != null &&
					!personaFisicaDtoParix.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese().isCessata()) {
				anomalies.add(FascicoloCreationAnomalyEnum.CUAA_MISMATCH_ANAGRAFE_TRIBUTARIA_CAMERA_COMMERCIO);
			}
		}
		return anomalies;
	}
	
	@Override
	public List<FascicoloCreationAnomalyEnum> validaOperazioneFascicolo(final FascicoloOperationEnum fascicoloOperationEnum) throws FascicoloValidazioneException {
		// verifico in a4g presenza fascicolo
		// verifico in sian
		// chiamo anagrafe tributaria e creo il model
		var anomalies = new ArrayList<FascicoloCreationAnomalyEnum>();
		var personaFisicaDtoFromAT = getPersonaFisicaFromAnagrafeTributaria();
		esistePersona(personaFisicaDtoFromAT);
		FascicoloValidatorComponent validator = getValidator();
		String codiceFiscaleDaCaa = getCodiceFiscaleDaCaa();
		String codiceFiscaleDaAnagrafeTributaria = personaFisicaDtoFromAT.getCodiceFiscale();
		
		validator.controlliMismatchAnagrafeTributariaECaa(fascicoloOperationEnum, anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa);
		if (fascicoloOperationEnum.equals(FascicoloOperationEnum.VERIFICA_APERTURA)) {
			verificaApertura(validator, anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa, personaFisicaDtoFromAT);
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.APRI)) {
			verificaApertura(validator, anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa, personaFisicaDtoFromAT);
			anomalies.addAll(verificaAnomaliaCodiceFiscaleATCameraDiCommercio(personaFisicaDtoFromAT));
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.VERIFICA_TRASFERIMENTO_CHIUSURA) || fascicoloOperationEnum.equals(FascicoloOperationEnum.TRASFERISCI_E_CHIUDI)) {
			// non sono stati distinti i casi di VERIFICA_TRASFERIMENTO_CHIUSURA e TRASFERISCI_E_CHIUDI poiché nessuno dei due chiama Parix
			verificaTrasferimentoChiusura(validator, codiceFiscaleDaCaa);
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.VERIFICA_APERTURA_TRASFERIMENTO) || fascicoloOperationEnum.equals(FascicoloOperationEnum.APRI_E_TRASFERISCI)) {
			// non sono stati distinti i casi di VERIFICA_APERTURA_TRASFERIMENTO e APRI_E_TRASFERISCI poiché nessuno dei due chiama Parix
			verificaAperturaTrasferimento(validator, anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa, personaFisicaDtoFromAT);
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.VERIFICA_TRASFERIMENTO)) {
			verificaTrasferimento(validator, codiceFiscaleDaCaa, personaFisicaDtoFromAT);	
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.TRASFERISCI)) {
			verificaTrasferimento(validator, codiceFiscaleDaCaa, personaFisicaDtoFromAT);
			anomalies.addAll(verificaAnomaliaCodiceFiscaleATCameraDiCommercio(personaFisicaDtoFromAT));
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.VERIFICA_AGGIORNAMENTO)) {
			verificaAggiornamento(validator, codiceFiscaleDaCaa);
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.AGGIORNA)) {
			verificaAggiornamento(validator, codiceFiscaleDaCaa);
			anomalies.addAll(verificaAnomaliaCodiceFiscaleATCameraDiCommercio(personaFisicaDtoFromAT));
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.VERIFICA_RIAPERTURA)) {
			verificaRiapertura(validator, anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa, personaFisicaDtoFromAT);
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.RIAPRI)) {
			verificaRiapertura(validator, anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa, personaFisicaDtoFromAT);
			anomalies.addAll(verificaAnomaliaCodiceFiscaleATCameraDiCommercio(personaFisicaDtoFromAT));
		}
		// verifico se è stato chiesto di aggiornare un fascicolo in chiusura (di persona deceduta)
		validator.verificaFascicoloInChiusura(personaFisicaDtoFromAT);
		// personaDeceduta(personaFisicaDtoFromAT);
		return anomalies;
	}
	
	public void verificaApertura(final FascicoloValidatorComponent validator, final List<FascicoloCreationAnomalyEnum> anomalies, final String codiceFiscaleDaAnagrafeTributaria, final String codiceFiscaleDaCaa, final PersonaFisicaDto personaFisicaDtoFromAT) throws FascicoloValidazioneException {
		validator.controlloMismatchAnagrafeTributariaESian(anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa);
		validator.nonEsisteFascicoloOrganismoPagatoreLocale(codiceFiscaleDaCaa);
		verificaUbicazioneProvinciaTrento(personaFisicaDtoFromAT);
	}
	
	public void verificaAggiornamento(final FascicoloValidatorComponent validator, final String codiceFiscaleDaCaa) throws FascicoloValidazioneException {
		validator.verificaOperatoreAppagFascicoloLocale(codiceFiscaleDaCaa);
	}
	
	public void verificaAperturaTrasferimento(final FascicoloValidatorComponent validator, final List<FascicoloCreationAnomalyEnum> anomalies, final String codiceFiscaleDaAnagrafeTributaria, final String codiceFiscaleDaCaa, final PersonaFisicaDto personaFisicaDtoFromAT) throws FascicoloValidazioneException {
		validator.controlloMismatchAnagrafeTributariaESian(anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa);
		validator.verificaRequisitiAperturaTrasferimentoFascicolo(
				codiceFiscaleDaCaa, personaFisicaDtoFromAT.getDomicilioFiscale().getProvincia());
	}
	
	public void verificaTrasferimento(final FascicoloValidatorComponent validator, final String codiceFiscaleDaCaa, final PersonaFisicaDto personaFisicaDtoFromAT) throws FascicoloValidazioneException {
		validator.verificaRequisitiTrasferimentoFascicolo(
				codiceFiscaleDaCaa, personaFisicaDtoFromAT.getDomicilioFiscale().getProvincia());
	}
	
	public void verificaTrasferimentoChiusura(final FascicoloValidatorComponent validator, final String codiceFiscaleDaCaa) throws FascicoloValidazioneException {
		validator.verificaTrasferimentoChiusuraFascicolo(codiceFiscaleDaCaa);
	}
	
	public void verificaRiapertura(final FascicoloValidatorComponent validator, final List<FascicoloCreationAnomalyEnum> anomalies, final String codiceFiscaleDaAnagrafeTributaria, final String codiceFiscaleDaCaa, final PersonaFisicaDto personaFisicaDtoFromAT) throws FascicoloValidazioneException {
		validator.controlloMismatchAnagrafeTributariaESian(anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa);
		verificaUbicazioneProvinciaTrento(personaFisicaDtoFromAT);
		validator.verificaStatoChiusoFascicoloLocale(codiceFiscaleDaCaa);
	}
	
	@Override
	protected FascicoloModel persistiFascicolo(final FascicoloModel fascicolo) {
		// salvataggio unita tecnico economiche
		List<UnitaTecnicoEconomicheModel> unitaTecnicoEconomiche = ((PersonaFisicaModel)fascicolo.getPersona()).getUnitaTecnicoEconomiche();
		if (unitaTecnicoEconomiche != null) {
			unitaTecnicoEconomiche.forEach(ute -> {
				unitaTecnicoEconomicheDao.save(ute);
				List<AttivitaAtecoModel> attivitaAteco = ute.getAttivitaAteco();
				if (attivitaAteco != null) {
					attivitaAteco.forEach(ateco -> attivitaAtecoDao.save(ateco));
				}
				List<DestinazioneUsoModel> destinazioneUso = ute.getDestinazioneUso();
				if (destinazioneUso != null) {
					destinazioneUso.forEach(uso -> destinazioneUsoDao.save(uso));
				}
			});
		}

//		persone fisiche/giuridiche con carica associata alla persona fisica con impresa individuale iscritta alla Camera di Commercio.
//		Puo' accadere che ci sia il titolare e un curatore fallimentare come persona con carica (ad. es. BZZRME30P29A116G)
		Set<PersonaFisicaConCaricaModel> personaFisicaConCaricaSet = new HashSet<>();
		Set<PersonaGiuridicaConCaricaModel> personaGiuridicaConCaricaSet = new HashSet<>();
		var personaFisicaModel = (PersonaFisicaModel)fascicolo.getPersona();
		List<CaricaModel> carichePersona = personaFisicaModel.getCariche();
		List<Boolean> isFirmatarioPresente = new ArrayList<Boolean>();
		isFirmatarioPresente.add(false);
		if (carichePersona != null && !carichePersona.isEmpty()) {
			carichePersona.forEach(carica -> {
				//- se id_persona_fisica_con_carica != null (e ovviamente id_persona_giuridica_con_carica == null) -> persona fisica con carica
				//- se id_persona_giuridica_con_carica != null (e ovviamente id_persona_fisica_con_carica == null) -> persona giuridica con carica
				var personaFisicaConCaricaModel = carica.getPersonaFisicaConCaricaModel();
				if (personaFisicaConCaricaModel != null) {
					personaFisicaConCaricaSet.add(personaFisicaConCaricaModel);
				} else {
					var personaGiuridicaConCaricaModel = carica.getPersonaGiuridicaConCaricaModel();
					if (personaGiuridicaConCaricaModel != null) {
						personaGiuridicaConCaricaSet.add(personaGiuridicaConCaricaModel);
					}
				}
				if (super.firmatarioPersonaFisicaOGiuridicaValido(carica)) {
					carica.setFirmatario(true);
					isFirmatarioPresente.set(0, true);
				}
			});

			personaFisicaConCaricaSet.forEach(personaFisicaConCarica -> {
				personaFisicaConCaricaDao.save(personaFisicaConCarica);
			});

//			invio di un unico evento asincrono di aggiornamento AT di un elenco di persone fisiche
			personaConCaricaService.sendEvent(fascicolo, personaFisicaConCaricaSet);

			//salvataggio persona giuridica con carica
			personaGiuridicaConCaricaSet.forEach(
					personaGiuridicaConCarica -> personaGiuridicaConCaricaDao.save(personaGiuridicaConCarica));

			carichePersona.forEach(
					carica -> caricaDao.save(carica));

//			la data aggiornamento fonti esterne viene aggiornata in PersoneFisicheConCaricaListener
			fascicolo.setDtAggiornamentoFontiEsterne(null);
			if (!isFirmatarioPresente.get(0)) {
//				in questo caso di aggiornamento dati anagrafici non c'è nessun firmatario, quindi il sistema di default considera quello di Anagrafe tributaria.
//				Quindi si devono eliminare le informazioni sul documento di identita'
				DocumentoIdentitaModel documento = fascicolo.getDocumentoIdentita();
				if (documento != null) {
					documentoIdentitaDao.deleteById(new EntitaDominioFascicoloId(documento.getId(),documento.getIdValidazione()));
					fascicolo.setDocumentoIdentita(null);
				}
				
			}
			fascicoloDao.save(fascicolo);
		}
		return fascicolo;
	}
	
	@Override
	public FascicoloModel aggiorna() throws FascicoloAllaFirmaAziendaException, FascicoloAllaFirmaCAAException, FascicoloValidazioneException {
//		salvare eventuale firmatario prima dell'aggiornamento dei dati
		super.salvataggioFirmatario();
		FascicoloModel fascicolo = super.aggiorna();
		return persistiFascicolo(fascicolo);
	}

	@Override
	public DatiAperturaFascicoloDto getDatiPerAperturaFascicolo() throws FascicoloValidazioneException {
		PersonaFisicaDto personaFisicaFromAnagrafeTributaria = getPersonaFisicaFromAnagrafeTributaria();
		if (personaFisicaFromAnagrafeTributaria == null) {
			return null;
		}
		var datiApertura = new DatiAperturaFascicoloDto();
		datiApertura.setDatiAnagraficiRappresentante(new DatiAnagraficiDto());
		
		AnagraficaDto anagrafica = personaFisicaFromAnagrafeTributaria.getAnagrafica();
		datiApertura.getDatiAnagraficiRappresentante()
				.setNominativo(anagrafica.getCognome() + " " + anagrafica.getNome())
				.setCodiceFiscale(personaFisicaFromAnagrafeTributaria.getCodiceFiscale())
				.setComuneNascita(anagrafica.getComuneNascita())
				.setProvinciaNascita(anagrafica.getProvinciaNascita())
				.setDataNascita(anagrafica.getDataNascita());
		datiApertura.setDenominazioneFascicolo(getDenominazioneFascicolo());
		datiApertura.setDomicilioFiscaleRappresentante(new IndirizzoDto());
		datiApertura.getDomicilioFiscaleRappresentante()
				.setCap(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getCap())
				.setComune(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getComune())
				.setToponimo(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getDenominazioneEstesa())
				.setLocalita(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getFrazione())
				.setProvincia(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getProvincia());
		if (personaFisicaFromAnagrafeTributaria.getImpresaIndividuale() != null) {
			ImpresaIndividualeDto impresa = personaFisicaFromAnagrafeTributaria.getImpresaIndividuale();
			datiApertura.setCodiceFiscale(personaFisicaFromAnagrafeTributaria.getCodiceFiscale())
					.setPartitaIva(impresa.getPartitaIva()).setDenominazione(impresa.getDenominazione())
					.setNaturaGiuridica(impresa.getFormaGiuridica() != null ? impresa.getFormaGiuridica() : "Impresa individuale");
			datiApertura.setUbicazioneDitta(new IndirizzoDto());
			SedeDto sede = impresa.getSedeLegale();
			datiApertura.getUbicazioneDitta()
					.setCap(sede.getIndirizzo().getCap())
					.setComune(sede.getIndirizzo().getComune())
					.setToponimo(sede.getIndirizzo().getDenominazioneEstesa())
					.setLocalita(sede.getIndirizzo().getFrazione()).setProvincia(sede.getIndirizzo().getProvincia());
		}
		return datiApertura;
	}
	
	@Override
	protected PersonaFisicaModel getPersona() throws FascicoloValidazioneException {
		var personaModel = new PersonaFisicaModel();
		PersonaFisicaDto personaFisicaFromAnagrafeTributaria = getPersonaFisicaFromAnagrafeTributaria();
		ImpresaIndividualeDto impresaIndividualeDto = personaFisicaFromAnagrafeTributaria.getImpresaIndividuale();
		personaModel.setNome(personaFisicaFromAnagrafeTributaria.getAnagrafica().getNome())
				.setCognome(personaFisicaFromAnagrafeTributaria.getAnagrafica().getCognome())
				.setComuneNascita(personaFisicaFromAnagrafeTributaria.getAnagrafica().getComuneNascita())
				.setDataNascita(personaFisicaFromAnagrafeTributaria.getAnagrafica().getDataNascita())
				.setDomicilioFiscale(buildIndirizzo(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale()))
				.setDeceduto(personaFisicaFromAnagrafeTributaria.isDeceduta())
				.setDataMorte(personaFisicaFromAnagrafeTributaria.getDataMorte())
				.setProvinciaNascita(personaFisicaFromAnagrafeTributaria.getAnagrafica().getProvinciaNascita())
				.setSesso(Sesso.valueOf(personaFisicaFromAnagrafeTributaria.getAnagrafica().getSesso().name()))
				.setCodiceFiscale(personaFisicaFromAnagrafeTributaria.getCodiceFiscale())
				.setIscrizioniSezione(buildIscrizioniSezione(personaModel, personaFisicaFromAnagrafeTributaria.getIscrizioniSezione()));
		
		boolean hasImpresaIndividuale = hasImpresaIndividuale(impresaIndividualeDto);
		if (hasImpresaIndividuale) {
			String provinciaDto = impresaIndividualeDto.getSedeLegale().getIndirizzo().getProvincia();
			personaModel.setImpresaIndividuale(new ImpresaIndividualeModel());
			personaModel.getImpresaIndividuale().setDenominazione(impresaIndividualeDto.getDenominazione()).setPartitaIVA(impresaIndividualeDto.getPartitaIva());

			var sede = new SedeModel();
			sede.setIndirizzo(buildIndirizzo(impresaIndividualeDto.getSedeLegale().getIndirizzo()));
			personaModel.getImpresaIndividuale().setSedeLegale(sede);

			// Ho un'iscrizione alla camera di commercio?
			PersonaFisicaDto personaFisicaParixDto = getAnagraficaProxyClient().getPersonaFisicaAnagraficaImpresa(
					personaFisicaFromAnagrafeTributaria.getCodiceFiscale(), provinciaDto);
			if (personaFisicaParixDto != null 
					&& personaFisicaParixDto.getImpresaIndividuale() != null 
					&& !personaFisicaParixDto.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese().isCessata().booleanValue()) {
				personaModel.getImpresaIndividuale().setFormaGiuridica(personaFisicaParixDto.getImpresaIndividuale().getFormaGiuridica());
				personaModel.getImpresaIndividuale().getSedeLegale()
						.setIscrizioneRegistroImprese(
								getIscrizioneRegistroImprese(personaFisicaParixDto.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese()))
						.setIndirizzoCameraCommercio(buildIndirizzo(personaFisicaParixDto.getImpresaIndividuale().getSedeLegale().getIndirizzo()))
						.setPec(personaFisicaParixDto.getImpresaIndividuale().getSedeLegale().getIndirizzoPec())
						.setTelefono(personaFisicaParixDto.getImpresaIndividuale().getSedeLegale().getTelefono());
				personaModel.getImpresaIndividuale().getSedeLegale().setAttivita(
						buildAttivita(personaFisicaParixDto.getImpresaIndividuale().getSedeLegale()
								.getAttivitaAteco(),
								personaModel,
								FonteDatoAnagrafico.CAMERA_COMMERCIO)
						);
				if (personaFisicaParixDto.getImpresaIndividuale().getUnitaLocali() != null && personaFisicaParixDto.getImpresaIndividuale().getUnitaLocali().size() > 0) {
//					salva le unita locali
					List<UnitaTecnicoEconomicheModel> elencoUnita = new ArrayList<>();
					personaFisicaParixDto.getImpresaIndividuale().getUnitaLocali().forEach(unitaLocale ->{
						UnitaTecnicoEconomicheModel u = buildUnitaLocale(unitaLocale, personaModel, FonteDatoAnagrafico.CAMERA_COMMERCIO);
						if(u != null) {
							elencoUnita.add(u);
						}
					});
					personaModel.setUnitaTecnicoEconomiche(elencoUnita);
				}
				//impresaIndividualeDto
				List<IscrizioneSezioneModel> buildIscrizioniSezione = buildIscrizioniSezione(personaModel, personaFisicaParixDto.getIscrizioniSezione());
				personaModel.setIscrizioniSezione(buildIscrizioniSezione);

//				TODO [BEGIN]
				List<CaricaModel> caricaModelList = new ArrayList<>();
//			memorizzazione informazioni per persone fisiche con carica nella persona giuridica
				if (personaFisicaParixDto.getPersoneFisicheConCarica() != null) {
					personaFisicaParixDto.getPersoneFisicheConCarica().forEach(personaFisicaConCaricaDto -> {
						PersonaFisicaConCaricaModel personaFisicaConCarica = from(personaFisicaConCaricaDto, 0);
						personaFisicaConCaricaDto.getListaCarica().forEach(caricaDto ->
								caricaModelList.add(from(caricaDto)
										.setPersonaFisicaModel(personaModel)
										.setPersonaFisicaConCaricaModel(personaFisicaConCarica)));
					});
				}
//				TODO [END]
				personaModel.setCariche(caricaModelList);
			} else {
				personaModel.getImpresaIndividuale().getSedeLegale().setAttivita(
						buildAttivita(impresaIndividualeDto.getSedeLegale()
								.getAttivitaAteco(),
								personaModel,
								FonteDatoAnagrafico.ANAGRAFE_TRIBUTARIA)
						);
			}
		}
		return personaModel;
	}

	private CaricaModel from(CaricaDto caricaDto) {
		return (new CaricaModel()).setDataInizio(caricaDto.getDataInizio()).setDescrizione(caricaDto.getDescrizione()).setIdentificativo(caricaDto.getIdentificativo());
	}

	private PersonaFisicaConCaricaModel from (PersonaFisicaConCaricaDto personaFisicaConCaricaDto, final Integer idValidazione) {
		PersonaFisicaConCaricaModel personaFisicaConCaricaModel = new PersonaFisicaConCaricaModel();
//		salvare solo codice fiscale. Piu' avanti nel flusso ad eventi si useranno i dati di AT
		personaFisicaConCaricaModel.setCodiceFiscale(personaFisicaConCaricaDto.getCodiceFiscale());
		return personaFisicaConCaricaModel;
	}

	/**
	 * Persona fisica: - SI ditta individuale: Denominazione Se nullo Cognome + " " + Nome - NO ditta individuale: Cognome + " " + Nome
	 *
	 * @return la denominazione del fascicolo
	 * @throws FascicoloValidazioneException 
	 */
	@Override
	protected String getDenominazioneFascicolo() throws FascicoloValidazioneException {
		PersonaFisicaModel persona = getPersona();
		if (persona.getImpresaIndividuale() != null && persona.getImpresaIndividuale().getDenominazione() != null)
			return persona.getImpresaIndividuale().getDenominazione();

		return persona.getCognome() + " " + persona.getNome();
	}
	
	private PersonaFisicaDto getPersonaFisicaFromAnagrafeTributaria() throws FascicoloValidazioneException {
		if (personaFisicaDto == null) {
			try {
				personaFisicaDto = getAnagraficaProxyClient().getPersonaFisicaAnagrafeTributaria(this.getCodiceFiscaleDaCaa());				
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new FascicoloValidazioneException(FascicoloValidazioneEnum.ANAGRAFE_TRIBUTARIA_NON_DISPONIBILE);
			}
		}
		return personaFisicaDto;
	}
	
	private boolean hasImpresaIndividuale(ImpresaIndividualeDto impresa) {
		return impresa != null && impresa.getPartitaIva() != null;
	}

	// BR6 - Verificare che la residenza del titolare dell’azienda individuale (persona fisica) oppure
	// la sede legale della società (persona giuridica) sia in provincia di Trento.
	// inoltre verisica che sia presente in anagrafe tributaria e non sia deceduto
	private void esistePersona(PersonaFisicaDto personaFisicaDto) throws FascicoloValidazioneException {
		logger.debug("Start - esistePersonaFisica");
		if (personaFisicaDto == null) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.CUAA_NON_PRESENTE);
		}
	}

	// BR6 - Verificare che sia presente in anagrafe tributaria e non sia deceduto
	private void personaDeceduta(final PersonaFisicaDto personaFisicaDto) throws FascicoloValidazioneException {
		logger.debug("Start - personaFisicaDeceduta {}", personaFisicaDto.getCodiceFiscale());
		if (personaFisicaDto.isDeceduta().booleanValue()) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.DECEDUTO);
		}
	}

	// BR6 - Verificare che la residenza del titolare dell’azienda individuale (persona fisica) oppure
	// la sede legale della società (persona giuridica) sia in provincia di Trento.
	// inoltre verisica che sia presente in anagrafe tributaria e non sia deceduto
	private void verificaUbicazioneProvinciaTrento(final PersonaFisicaDto personaFisicaDto) throws FascicoloValidazioneException {
		logger.debug("Start - verificaUbicazioneProvinciaTrento {}", personaFisicaDto.getCodiceFiscale());
		if (personaFisicaDto.getImpresaIndividuale() != null && (personaFisicaDto.getImpresaIndividuale().getSedeLegale().getIndirizzo().getProvincia() == null
				|| !personaFisicaDto.getImpresaIndividuale().getSedeLegale().getIndirizzo().getProvincia().equals(FascicoloValidatorComponent.PROV_TRENTO)))
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.PROVINCIA_DIVERSA_DA_TRENTO);
		if (personaFisicaDto.getImpresaIndividuale() == null
				&& (personaFisicaDto.getDomicilioFiscale().getProvincia() == null
				|| !personaFisicaDto.getDomicilioFiscale().getProvincia().equals(FascicoloValidatorComponent.PROV_TRENTO)))
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.PROVINCIA_DIVERSA_DA_TRENTO);
	}

	@Override
	public List<FascicoloCreationAnomalyEnum> verificaAperturaFascicoloDetenzioneAutonoma(
			final FascicoloOperationEnum fascicoloOperationEnum) throws FascicoloValidazioneException {
		String codiceFiscaleDaCaa = getCodiceFiscaleDaCaa();
		// controllo codiceFiscaleDaCaa == codice fiscale utente
		if (!utenteComponent.username().equals("USER_AUTOMAZIONE_APPROVAZIONE") && !utenteComponent.username().equals(codiceFiscaleDaCaa)) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.CUAA_DIVERSO_CODICEFISCALE_UTENTE);
		}
		// verifico in a4g presenza fascicolo
		// verifico in sian
		// chiamo anagrafe tributaria e creo il model
		return validaOperazioneFascicolo(fascicoloOperationEnum);
	}
}
