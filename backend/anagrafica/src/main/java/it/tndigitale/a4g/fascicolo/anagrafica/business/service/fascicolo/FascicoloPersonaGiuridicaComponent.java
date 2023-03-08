package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.AttivitaAtecoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.CaricaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DocumentoIdentitaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FonteDatoAnagrafico;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaConCaricaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaConCaricaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SedeModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.UnitaTecnicoEconomicheModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.AttivitaAtecoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.CaricaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.DestinazioneUsoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.DocumentoIdentitaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaFisicaConCaricaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaGiuridicaConCaricaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.UnitaTecnicoEconomicheDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.PersonaConCaricaService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAnagraficiDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.IndirizzoDto;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.proxy.client.model.CaricaDto;
import it.tndigitale.a4g.proxy.client.model.PersonaFisicaConCaricaDto;
import it.tndigitale.a4g.proxy.client.model.PersonaGiuridicaConCaricaDto;
import it.tndigitale.a4g.proxy.client.model.PersonaGiuridicaDto;
import it.tndigitale.a4g.proxy.client.model.UnitaLocaleDto;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FascicoloPersonaGiuridicaComponent extends FascicoloAbstractComponent<PersonaGiuridicaModel> {

	private static final Logger logger = LoggerFactory.getLogger(FascicoloPersonaGiuridicaComponent.class);

	@Autowired
	private FascicoloDao fascicoloDao;

	@Autowired
	private PersonaFisicaConCaricaDao personaFisicaConCaricaDao;
	
	@Autowired
	private PersonaGiuridicaConCaricaDao personaGiuridicaConCaricaDao;
	
	@Autowired
	private PersonaConCaricaService personaConCaricaService;
	
	@Autowired
	private UnitaTecnicoEconomicheDao unitaTecnicoEconomicheDao;
	
	@Autowired
	private DestinazioneUsoDao destinazioneUsoDao;
	
	@Autowired
	private AttivitaAtecoDao attivitaAtecoDao;

	@Autowired
	private CaricaDao caricaDao;
	
	@Autowired
	private UtenteComponent utenteComponent;
	
	@Autowired
	private DocumentoIdentitaDao documentoIdentitaDao;
	
	private PersonaGiuridicaDto personaGiuridicaDto;
	
	@Override
	String getCodiceFiscaleDaAnagrafeTributaria() {
		if (personaGiuridicaDto == null) {
			return null;
		} else return personaGiuridicaDto.getCodiceFiscale();
	}
	
	@Override
	public List<FascicoloCreationAnomalyEnum> validaOperazioneFascicolo(final FascicoloOperationEnum fascicoloOperationEnum) throws FascicoloValidazioneException {
		// verifico in a4g presenza fascicolo
		// verifico in sian
		// chiamo anagrafe tributaria e creo il model
		var personaGiuridicaDtoFromAT = getPersonaGiuridicaFromAnagrafeTributaria();
		var anomalies = new ArrayList<FascicoloCreationAnomalyEnum>();
		esistePersona(personaGiuridicaDtoFromAT);
		FascicoloValidatorComponent validator = getValidator();
		String codiceFiscaleDaCaa = getCodiceFiscaleDaCaa();
		String codiceFiscaleDaAnagrafeTributaria = personaGiuridicaDtoFromAT.getCodiceFiscale();
		
		validator.controlliMismatchAnagrafeTributariaECaa(fascicoloOperationEnum, anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa);
		if (fascicoloOperationEnum.equals(FascicoloOperationEnum.VERIFICA_APERTURA)) {
			verificaApertura(validator, anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa, personaGiuridicaDtoFromAT);
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.APRI)) {
			verificaApertura(validator, anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa, personaGiuridicaDtoFromAT);
			anomalies.addAll(verificaAnomaliaCodiceFiscaleATCameraDiCommercio(personaGiuridicaDtoFromAT));
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.VERIFICA_TRASFERIMENTO_CHIUSURA) || fascicoloOperationEnum.equals(FascicoloOperationEnum.TRASFERISCI_E_CHIUDI)) {
			// non sono stati distinti i casi di VERIFICA_TRASFERIMENTO_CHIUSURA e TRASFERISCI_E_CHIUDI poiché nessuno dei due chiama Parix
			verificaTrasferimentoChiusura(validator, codiceFiscaleDaCaa);
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.VERIFICA_APERTURA_TRASFERIMENTO) || fascicoloOperationEnum.equals(FascicoloOperationEnum.APRI_E_TRASFERISCI)) {
			// non sono stati distinti i casi di VERIFICA_APERTURA_TRASFERIMENTO e APRI_E_TRASFERISCI poiché nessuno dei due chiama Parix
			verificaAperturaTrasferimento(validator, anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa, personaGiuridicaDtoFromAT);
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.VERIFICA_TRASFERIMENTO)) {
			verificaTrasferimento(validator, codiceFiscaleDaCaa, personaGiuridicaDtoFromAT);
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.TRASFERISCI)) {
			verificaTrasferimento(validator, codiceFiscaleDaCaa, personaGiuridicaDtoFromAT);
			anomalies.addAll(verificaAnomaliaCodiceFiscaleATCameraDiCommercio(personaGiuridicaDtoFromAT));
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.VERIFICA_AGGIORNAMENTO)) {
			verificaAggiornamento(validator, codiceFiscaleDaCaa);
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.AGGIORNA)) {	
			verificaAggiornamento(validator, codiceFiscaleDaCaa);
			anomalies.addAll(verificaAnomaliaCodiceFiscaleATCameraDiCommercio(personaGiuridicaDtoFromAT));
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.VERIFICA_RIAPERTURA)) {
			verificaRiapertura(validator, anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa, personaGiuridicaDtoFromAT);
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.RIAPRI)) {
			verificaRiapertura(validator, anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa, personaGiuridicaDtoFromAT);
			anomalies.addAll(verificaAnomaliaCodiceFiscaleATCameraDiCommercio(personaGiuridicaDtoFromAT));
		}
		return anomalies;
	}
	
	public void verificaApertura(final FascicoloValidatorComponent validator, final List<FascicoloCreationAnomalyEnum> anomalies, final String codiceFiscaleDaAnagrafeTributaria, final String codiceFiscaleDaCaa, final PersonaGiuridicaDto personaGiuridicaDtoFromAT) throws FascicoloValidazioneException {
		validator.controlloMismatchAnagrafeTributariaESian(anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa);
		validator.nonEsisteFascicoloOrganismoPagatoreLocale(codiceFiscaleDaCaa);
		verificaUbicazioneProvinciaTrento(personaGiuridicaDtoFromAT);
	}
	
	public void verificaAggiornamento(final FascicoloValidatorComponent validator, final String codiceFiscaleDaCaa) throws FascicoloValidazioneException {
		validator.verificaOperatoreAppagFascicoloLocale(codiceFiscaleDaCaa);
	}
	
	public void verificaAperturaTrasferimento(final FascicoloValidatorComponent validator, final List<FascicoloCreationAnomalyEnum> anomalies, final String codiceFiscaleDaAnagrafeTributaria, final String codiceFiscaleDaCaa, final PersonaGiuridicaDto personaGiuridicaDtoFromAT) throws FascicoloValidazioneException {
		validator.controlloMismatchAnagrafeTributariaESian(anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa);
		validator.verificaRequisitiAperturaTrasferimentoFascicolo(
				codiceFiscaleDaCaa, personaGiuridicaDtoFromAT.getSedeLegale().getIndirizzo().getProvincia());	
	}
	
	public void verificaTrasferimento(final FascicoloValidatorComponent validator, final String codiceFiscaleDaCaa, final PersonaGiuridicaDto personaGiuridicaDtoFromAT) throws FascicoloValidazioneException {
		validator.verificaRequisitiTrasferimentoFascicolo(
				codiceFiscaleDaCaa, personaGiuridicaDtoFromAT.getSedeLegale().getIndirizzo().getProvincia());
	}
	
	public void verificaTrasferimentoChiusura(final FascicoloValidatorComponent validator, final String codiceFiscaleDaCaa) throws FascicoloValidazioneException {
		validator.verificaTrasferimentoChiusuraFascicolo(codiceFiscaleDaCaa);
	}
	
	public void verificaRiapertura(final FascicoloValidatorComponent validator, final List<FascicoloCreationAnomalyEnum> anomalies, final String codiceFiscaleDaAnagrafeTributaria, final String codiceFiscaleDaCaa, final PersonaGiuridicaDto personaGiuridicaDtoFromAT) throws FascicoloValidazioneException {
		validator.controlloMismatchAnagrafeTributariaESian(anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa);
		verificaUbicazioneProvinciaTrento(personaGiuridicaDtoFromAT);
		validator.verificaStatoChiusoFascicoloLocale(codiceFiscaleDaCaa);
	}
	
	private List<FascicoloCreationAnomalyEnum> verificaAnomaliaCodiceFiscaleATCameraDiCommercio(PersonaGiuridicaDto personaGiuridicaDtoFromAT){
		var anomalies = new ArrayList<FascicoloCreationAnomalyEnum>();
		var codiceFiscaleDaAnagrafeTributaria = personaGiuridicaDtoFromAT.getCodiceFiscale();
		var personaGiuridicaDtoParix = getAnagraficaProxyClient().getPersonaGiuridicaAnagraficaImpresa(
				codiceFiscaleDaAnagrafeTributaria,
				personaGiuridicaDtoFromAT.getSedeLegale().getIndirizzo().getProvincia());
		if (!codiceFiscaleDaAnagrafeTributaria.equals(getCodiceFiscaleDaCaa()) && personaGiuridicaDtoParix == null) {
			personaGiuridicaDtoParix = getAnagraficaProxyClient().getPersonaGiuridicaAnagraficaImpresa(
					getCodiceFiscaleDaCaa(),
					personaGiuridicaDtoFromAT.getSedeLegale().getIndirizzo().getProvincia());
			if (personaGiuridicaDtoParix != null &&
					!personaGiuridicaDtoParix.getSedeLegale().getIscrizioneRegistroImprese().isCessata()) {
				anomalies.add(FascicoloCreationAnomalyEnum.CUAA_MISMATCH_ANAGRAFE_TRIBUTARIA_CAMERA_COMMERCIO);
			}
		}
		return anomalies;
	}
	
	private void persistiUnitaTecnicoEconomicheFascicolo(final List<UnitaTecnicoEconomicheModel> unitaTecnicoEconomiche) {
		if (unitaTecnicoEconomiche != null) {
			unitaTecnicoEconomiche.forEach(ute -> {
				unitaTecnicoEconomicheDao.save(ute);
				List<AttivitaAtecoModel> attivitaAteco = ute.getAttivitaAteco();
				if (attivitaAteco != null) {
					attivitaAteco.forEach(ateco -> attivitaAtecoDao.save(ateco));
				}
				if (ute.getDestinazioneUso() != null) {
					ute.getDestinazioneUso().forEach(uso -> destinazioneUsoDao.save(uso));
				}
			});
		}
	}

//	Nel caso in cui il rappresentante legale con diritto di firma indicato dal CAA non abbia più i requisiti (ad es. la carica aggiornata non corrisponde a quella salvata, non è presente una carica con firmatario, ecc) dopo l’aggiornamento, il sistema lo sostituisce con quello di Anagrafe tributaria.
	@Override
	protected FascicoloModel persistiFascicolo(final FascicoloModel fascicolo) {
		Set<PersonaFisicaConCaricaModel> personaFisicaConCaricaSet = new HashSet<>();
		Set<PersonaGiuridicaConCaricaModel> personaGiuridicaConCaricaSet = new HashSet<>();
		var personaGiuridicaModel = (PersonaGiuridicaModel)fascicolo.getPersona();
		List<CaricaModel> carichePersona = personaGiuridicaModel.getCariche();
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
		List<UnitaTecnicoEconomicheModel> unitaTecnicoEconomiche = personaGiuridicaModel.getUnitaTecnicoEconomiche();
		persistiUnitaTecnicoEconomicheFascicolo(unitaTecnicoEconomiche);
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
		PersonaGiuridicaDto personaGiuridicaFromAnagrafeTributaria = getPersonaGiuridicaFromAnagrafeTributaria();
		if (personaGiuridicaFromAnagrafeTributaria == null) {
			return null;
		}
		var datiApertura = new DatiAperturaFascicoloDto();
		datiApertura.setDatiAnagraficiRappresentante(new DatiAnagraficiDto());

		datiApertura.getDatiAnagraficiRappresentante()
				.setNominativo(personaGiuridicaFromAnagrafeTributaria.getRappresentanteLegale().getNominativo())
				.setCodiceFiscale(personaGiuridicaFromAnagrafeTributaria.getRappresentanteLegale().getCodiceFiscale());

		datiApertura.setCodiceFiscale(personaGiuridicaFromAnagrafeTributaria.getCodiceFiscale())
				.setPartitaIva(personaGiuridicaFromAnagrafeTributaria.getPartitaIva())
				.setDenominazione(personaGiuridicaFromAnagrafeTributaria.getDenominazione())
				.setNaturaGiuridica(personaGiuridicaFromAnagrafeTributaria.getFormaGiuridica())
				.setDenominazioneFascicolo(getDenominazioneFascicolo());

		datiApertura.setUbicazioneDitta(new IndirizzoDto());
		it.tndigitale.a4g.proxy.client.model.IndirizzoDto indirizzo =
				personaGiuridicaFromAnagrafeTributaria.getSedeLegale().getIndirizzo();
		datiApertura.getUbicazioneDitta()
				.setCap(indirizzo.getCap())
				.setComune(indirizzo.getComune())
				.setToponimo(indirizzo.getDenominazioneEstesa())
				.setLocalita(indirizzo.getFrazione())
				.setProvincia(indirizzo.getProvincia());
		return datiApertura;
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
	
	
	private PersonaGiuridicaConCaricaModel from(PersonaGiuridicaConCaricaDto personaGiuridicaConCaricaDto, final Integer idValidazione) {
		PersonaGiuridicaConCaricaModel personaGiuridicaConCaricaModel = new PersonaGiuridicaConCaricaModel();
//		salvare solo codice fiscale. Piu' avanti nel flusso ad eventi si useranno i dati di AT
		personaGiuridicaConCaricaModel.setCodiceFiscale(personaGiuridicaConCaricaDto.getCodiceFiscale());
		personaGiuridicaConCaricaModel.setDenominazione(personaGiuridicaConCaricaDto.getDenominazione());
		
		return personaGiuridicaConCaricaModel;
	}
	
	@Override
	protected PersonaGiuridicaModel getPersona() throws FascicoloValidazioneException {
		var personaGiuridica = new PersonaGiuridicaModel();
		PersonaGiuridicaDto auxPersGiurDto = getPersonaGiuridicaFromAnagrafeTributaria();
		String provinciaSedeLegale = auxPersGiurDto.getSedeLegale().getIndirizzo().getProvincia();
		String codiceFiscaleDaCaa = getCodiceFiscaleDaCaa();
		String codiceFiscaleDaAnagrafeTributaria = getCodiceFiscaleDaAnagrafeTributaria();
		personaGiuridica.setCodiceFiscale(codiceFiscaleDaAnagrafeTributaria);
		personaGiuridica.setPartitaIVA(auxPersGiurDto.getPartitaIva())
			.setDenominazione(auxPersGiurDto.getDenominazione())
			.setFormaGiuridica(auxPersGiurDto.getFormaGiuridica())
			.setCodiceFiscaleRappresentanteLegale(auxPersGiurDto.getRappresentanteLegale().getCodiceFiscale())
			.setNominativoRappresentanteLegale(auxPersGiurDto.getRappresentanteLegale().getNominativo());
		var sede = new SedeModel();
		sede.setIndirizzo(buildIndirizzo(auxPersGiurDto.getSedeLegale().getIndirizzo()));
		personaGiuridica.setSedeLegale(sede);
		
		var personaGiuridicaDtoParix = getAnagraficaProxyClient().getPersonaGiuridicaAnagraficaImpresa(
				codiceFiscaleDaAnagrafeTributaria,
				auxPersGiurDto.getSedeLegale().getIndirizzo().getProvincia());
		if (!codiceFiscaleDaAnagrafeTributaria.equals(codiceFiscaleDaCaa) && personaGiuridicaDtoParix == null) {
			personaGiuridicaDtoParix = getAnagraficaProxyClient().getPersonaGiuridicaAnagraficaImpresa(
					codiceFiscaleDaCaa, provinciaSedeLegale);
		}
		// Ho un'iscrizione alla camera di commercio
		if (personaGiuridicaDtoParix != null &&
				!personaGiuridicaDtoParix.getSedeLegale().getIscrizioneRegistroImprese().isCessata()) {
			personaGiuridica
					.setCapitaleSocialeDeliberato(personaGiuridicaDtoParix.getCapitaleSocialeDeliberato())
					.setOggettoSociale(personaGiuridicaDtoParix.getOggettoSociale())
					.setDataCostituzione(personaGiuridicaDtoParix.getDataCostituzione())
					.setDataTermine(personaGiuridicaDtoParix.getDataTermine());
			personaGiuridica.getSedeLegale()
					.setIscrizioneRegistroImprese(
							getIscrizioneRegistroImprese(personaGiuridicaDtoParix.getSedeLegale().getIscrizioneRegistroImprese()))
					.setIndirizzoCameraCommercio(buildIndirizzo(personaGiuridicaDtoParix.getSedeLegale().getIndirizzo()))
					.setPec(personaGiuridicaDtoParix.getSedeLegale().getIndirizzoPec())
					.setTelefono(personaGiuridicaDtoParix.getSedeLegale().getTelefono());
			personaGiuridica.getSedeLegale().setAttivita(buildAttivita(
						personaGiuridicaDtoParix.getSedeLegale().getAttivitaAteco(),
						personaGiuridica,
						FonteDatoAnagrafico.CAMERA_COMMERCIO));
			personaGiuridica.setIscrizioniSezione(buildIscrizioniSezione(personaGiuridica, personaGiuridicaDtoParix.getIscrizioniSezione()));
			List<CaricaModel> caricaModelList = new ArrayList<>();
//			memorizzazione informazioni per persone fisiche con carica nella persona giuridica
			if (personaGiuridicaDtoParix.getPersoneFisicheConCarica() != null) {
				personaGiuridicaDtoParix.getPersoneFisicheConCarica().forEach(personaFisicaConCaricaDto -> {
					PersonaFisicaConCaricaModel personaFisicaConCarica = from(personaFisicaConCaricaDto, 0);
					personaFisicaConCaricaDto.getListaCarica().forEach(caricaDto ->
							caricaModelList.add(from(caricaDto)
									.setPersonaGiuridicaModel(personaGiuridica)
									.setPersonaFisicaConCaricaModel(personaFisicaConCarica)));
				});
			}
//			memorizzazione informazioni per persone giuridiche con carica nella persona giuridica
			if (personaGiuridicaDtoParix.getPersoneGiuridicheConCarica() != null) {
				personaGiuridicaDtoParix.getPersoneGiuridicheConCarica().forEach(personaGiuridicaConCaricaDto ->{
					PersonaGiuridicaConCaricaModel personaGiuridicaConCarica = from(personaGiuridicaConCaricaDto, 0);
					personaGiuridicaConCaricaDto.getListaCarica().forEach(caricaDto ->
							caricaModelList.add(from(caricaDto)
									.setPersonaGiuridicaModel(personaGiuridica)
									.setPersonaGiuridicaConCaricaModel(personaGiuridicaConCarica)));
				});
			}
				personaGiuridica.setCariche(caricaModelList);
			
			List<UnitaLocaleDto> unitaLocali = personaGiuridicaDtoParix.getUnitaLocali();
			if (unitaLocali != null && !unitaLocali.isEmpty()) {
//				salva le unita locali
				List<UnitaTecnicoEconomicheModel> elencoUnita = new ArrayList<>();
				unitaLocali.forEach(unitaLocale -> {
					UnitaTecnicoEconomicheModel u = buildUnitaLocale(unitaLocale, personaGiuridica, FonteDatoAnagrafico.CAMERA_COMMERCIO);
					if (u != null) {
						elencoUnita.add(u);
					}
				});
				personaGiuridica.setUnitaTecnicoEconomiche(elencoUnita);
			}
		} else {
			personaGiuridica.getSedeLegale().setAttivita(buildAttivita(
					auxPersGiurDto.getSedeLegale().getAttivitaAteco(),
					personaGiuridica,
					FonteDatoAnagrafico.ANAGRAFE_TRIBUTARIA));
		}
		return personaGiuridica;
	}

	@Override
	protected String getDenominazioneFascicolo() throws FascicoloValidazioneException {
		PersonaGiuridicaModel persona = getPersona();
		if (persona.getDenominazione() == null || persona.getDenominazione().isEmpty()) {
			return persona.getCodiceFiscale();
		}
		return persona.getDenominazione();
	}
	
	private PersonaGiuridicaDto getPersonaGiuridicaFromAnagrafeTributaria() throws FascicoloValidazioneException {
		if (personaGiuridicaDto == null) {
			try {
				personaGiuridicaDto = getAnagraficaProxyClient().getPersonaGiuridicaAnagrafeTributaria(getCodiceFiscaleDaCaa());
			} catch (Exception e) {
				logger.error("Errore nell'interazione con proxy", e);
				throw new FascicoloValidazioneException(FascicoloValidazioneEnum.ANAGRAFE_TRIBUTARIA_NON_DISPONIBILE);
			}
		}
		return personaGiuridicaDto;
	}

	// BR6 - Verificare che  la residenza del titolare dell’azienda individuale (persona fisica) oppure
	// la sede legale della società (persona giuridica) sia  in provincia di Trento.
	// inoltre verisica che sia presente in anagrafe tributaria e non sia deceduto
	private void esistePersona(final PersonaGiuridicaDto personaGiuridicaDto) throws FascicoloValidazioneException {
		logger.debug("Start - esistePersonaGiuridica");
		if (personaGiuridicaDto == null) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.CUAA_NON_PRESENTE);
		}
	}

	// BR6 - Verificare che  la residenza del titolare dell’azienda individuale (persona fisica) oppure
	// la sede legale della società (persona giuridica) sia  in provincia di Trento.
	// inoltre verisica che sia presente in anagrafe tributaria e non sia deceduto
	private void verificaUbicazioneProvinciaTrento(final PersonaGiuridicaDto personaGiuridicaDto) throws FascicoloValidazioneException {
		logger.debug("Start - verificaUbicazioneProvinciaTrento {}", personaGiuridicaDto.getCodiceFiscale());
		if (personaGiuridicaDto.getSedeLegale().getIndirizzo().getProvincia() == null ||
				!personaGiuridicaDto.getSedeLegale().getIndirizzo().getProvincia().equals(FascicoloValidatorComponent.PROV_TRENTO))
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.PROVINCIA_DIVERSA_DA_TRENTO);
	}
	
	@Override
	public List<FascicoloCreationAnomalyEnum> verificaAperturaFascicoloDetenzioneAutonoma(final FascicoloOperationEnum fascicoloOperationEnum) throws FascicoloValidazioneException {
		// verifico in a4g presenza fascicolo
		// verifico in sian
		// chiamo anagrafe tributaria e creo il model
		String codiceFiscaleRappresentanteLegale = getPersonaGiuridicaFromAnagrafeTributaria().getRappresentanteLegale().getCodiceFiscale();
		if (!utenteComponent.username().equals(codiceFiscaleRappresentanteLegale)) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.CUAA_DIVERSO_CODICEFISCALE_UTENTE);
		}
		return validaOperazioneFascicolo(fascicoloOperationEnum);
	}
}
