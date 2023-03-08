package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.AttivitaAtecoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.CaricaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DestinazioneUsoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FonteDatoAnagrafico;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.ImportanzaAttivita;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.IndirizzoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.IscrizioneRegistroImpreseModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.IscrizioneSezioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.OrganismoPagatoreEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.UnitaTecnicoEconomicheModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.AttivitaAtecoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.CaricaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.DestinazioneUsoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.IscrizioneSezioneDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaFisicaConCaricaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaFisicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaGiuridicaConCaricaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaGiuridicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.UnitaTecnicoEconomicheDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.CaricheRappresentanteLegaleMap;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.StatusMessagesEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaProxyClient;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.proxy.client.model.AttivitaDto;
import it.tndigitale.a4g.proxy.client.model.IndirizzoDto;
import it.tndigitale.a4g.proxy.client.model.IscrizioneRepertorioEconomicoDto;
import it.tndigitale.a4g.proxy.client.model.IscrizioneSezioneDto;
import it.tndigitale.a4g.proxy.client.model.IscrizioneSezioneDto.SezioneEnum;
import it.tndigitale.a4g.proxy.client.model.UnitaLocaleDto;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
abstract class FascicoloAbstractComponent<P extends PersonaModel> {
	@Autowired
	private FascicoloDao fascicoloDao;

	@Autowired
	private PersonaDao personaDao;

	@Autowired
	private IscrizioneSezioneDao iscrizioneSezioneDao;

	@Autowired
	private FascicoloValidatorComponent validator;

	@Autowired
	private AnagraficaProxyClient anagraficaProxyClient;

	@Autowired
	UtenteComponent utenteComponent;

	@Autowired
	private PersonaFisicaDao personaFisicaDao;

	@Autowired
	private PersonaGiuridicaDao personaGiuridicaDao;

	@Autowired
	private UnitaTecnicoEconomicheDao unitaTecnicoEconomicheDao;

	@Autowired
	private DestinazioneUsoDao destinazioneUsoDao;

	@Autowired
	private AttivitaAtecoDao attivitaAtecoDao;

	@Autowired
	private CaricaDao caricaDao;

	@Autowired
	private PersonaFisicaConCaricaDao personaFisicaConCaricaDao;

	@Autowired
	private PersonaGiuridicaConCaricaDao personaGiuridicaConCaricaDao;

	private FascicoloModel fascicolo;
	private String codiceFiscaleDaCaa;
	private List<CaricaModel> caricaConFirmatarioPrecedenteList = new ArrayList<CaricaModel>();

	protected abstract String getDenominazioneFascicolo() throws FascicoloValidazioneException;

	protected abstract P getPersona() throws FascicoloValidazioneException;

	protected abstract FascicoloModel persistiFascicolo(FascicoloModel fascicolo);

	protected abstract List<FascicoloCreationAnomalyEnum> validaOperazioneFascicolo(final FascicoloOperationEnum fascicoloOperationEnum) throws FascicoloValidazioneException;

	protected abstract DatiAperturaFascicoloDto getDatiPerAperturaFascicolo() throws FascicoloValidazioneException;

	protected abstract List<FascicoloCreationAnomalyEnum> verificaAperturaFascicoloDetenzioneAutonoma(final FascicoloOperationEnum fascicoloOperationEnum) throws FascicoloValidazioneException;

	abstract String getCodiceFiscaleDaAnagrafeTributaria();

	public FascicoloModel apri(LocalDate dataApertura) throws FascicoloValidazioneException {
		fascicolo = new FascicoloModel();
		P persona = getPersona();
		fascicolo.setCuaa(persona.getCodiceFiscale()).setStato(StatoFascicoloEnum.IN_AGGIORNAMENTO).setOrganismoPagatore(OrganismoPagatoreEnum.APPAG).setDenominazione(getDenominazioneFascicolo())
				.setPersona(persona);

		PersonaModel newPersona = fascicolo.getPersona();
		personaDao.save(newPersona);

		List<IscrizioneSezioneModel> iscrizioniModel = newPersona.getIscrizioniSezione();
		if (iscrizioniModel != null && !iscrizioniModel.isEmpty()) {
			iscrizioneSezioneDao.saveAll(iscrizioniModel);
		}

		String username = utenteComponent.username();
		fascicolo.setUtenteModifica(username).setDataApertura(dataApertura);
		fascicoloDao.save(fascicolo);

		return persistiFascicolo(fascicolo);
	}

	public FascicoloModel trasferisci() throws FascicoloValidazioneException {
		fascicolo = new FascicoloModel();
		P persona = getPersona();
		String username = utenteComponent.username();
		fascicolo.setCuaa(persona.getCodiceFiscale()).setStato(StatoFascicoloEnum.IN_ATTESA_TRASFERIMENTO).setOrganismoPagatore(OrganismoPagatoreEnum.ALTRO_OP)
				.setDenominazione(getDenominazioneFascicolo()).setPersona(persona).setUtenteModifica(username).setDataApertura(LocalDate.now());

		PersonaModel newPersona = fascicolo.getPersona();
		personaDao.save(newPersona);

		return fascicoloDao.save(fascicolo);
	}

	private void deletePersona(PersonaModel persona) {
		iscrizioneSezioneDao.deleteAll(persona.getIscrizioniSezione());

		if (persona instanceof PersonaGiuridicaModel) {
			// eliminazione di cariche e persone con carica associate alla persona giuridica
			deleteCaricheEPersoneConCarica(((PersonaGiuridicaModel) persona).getCariche());

			// cancellazione a cascata di unita tecnico economiche
			deleteUteCascade(((PersonaGiuridicaModel) persona).getUnitaTecnicoEconomiche());

			personaGiuridicaDao.delete((PersonaGiuridicaModel) persona);
		} else if (persona instanceof PersonaFisicaModel) {
			// eliminazione di cariche e persone con carica associate alla persona fisica
			deleteCaricheEPersoneConCarica(((PersonaFisicaModel) persona).getCariche());

			// cancellazione a cascata di unita tecnico economiche
			deleteUteCascade(((PersonaFisicaModel) persona).getUnitaTecnicoEconomiche());

			personaFisicaDao.delete((PersonaFisicaModel) persona);
		}
		personaDao.delete(persona);
	}

	public FascicoloModel aggiorna() throws FascicoloAllaFirmaAziendaException, FascicoloAllaFirmaCAAException, FascicoloValidazioneException {
		fascicolo = fascicoloDao.findByCuaaAndIdValidazione(codiceFiscaleDaCaa, 0).orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));
		if (fascicolo.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA)) {
			throw new FascicoloAllaFirmaAziendaException();
		}
		if (fascicolo.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_CAA)) {
			throw new FascicoloAllaFirmaCAAException();
		}

		if (fascicolo.getStato().equals(StatoFascicoloEnum.VALIDATO)) {
			fascicolo.setIdSchedaValidazione(null);
			fascicolo.setSchedaValidazione(null);
		}
		String username = utenteComponent.username();
		/*
		 * 1) elimino il vecchio elemento persona
		 */
		PersonaModel oldPersona = fascicolo.getPersona();
		fascicolo.setPersona(null);
		if (oldPersona != null) {
			deletePersona(oldPersona);
		}
		fascicoloDao.flush();
		/*
		 * 2) richiamo il procedimento di creazione persona e sostituisco il risultato a quello vecchio
		 */
		P newPersona = getPersona();
		personaDao.save(newPersona);
		// verifica se la persona e' deceduta; in tal caso si deve aggiornare lo stato del fascicolo IN_CHIUSURA
		if (newPersona instanceof PersonaFisicaModel && fascicolo.getStato() != StatoFascicoloEnum.IN_CHIUSURA) {
			if (((PersonaFisicaModel) newPersona).getDeceduto()) {
				fascicolo.setStato(StatoFascicoloEnum.IN_CHIUSURA);
			}
		}
		List<IscrizioneSezioneModel> iscrizioniModel = newPersona.getIscrizioniSezione();
		if (iscrizioniModel != null && !iscrizioniModel.isEmpty()) {
			iscrizioneSezioneDao.saveAll(iscrizioniModel);
		}

		// se è in chiusura ci rimane
		StatoFascicoloEnum statoFinale = (fascicolo.getStato() == StatoFascicoloEnum.IN_CHIUSURA ? StatoFascicoloEnum.IN_CHIUSURA : StatoFascicoloEnum.IN_AGGIORNAMENTO);

		fascicolo.setCuaa(newPersona.getCodiceFiscale()).setDataModifica(LocalDate.now()).setPersona(newPersona).setUtenteModifica(username).setStato(statoFinale)
				.setDtAggiornamentoFontiEsterne(LocalDateTime.now());
		fascicoloDao.save(fascicolo);
		return fascicolo;
	}

	String getCodiceFiscaleDaCaa() {
		return codiceFiscaleDaCaa;
	}

	FascicoloAbstractComponent<P> setCodiceFiscaleDaCaa(final String codiceFiscale) {
		this.codiceFiscaleDaCaa = codiceFiscale;
		return this;
	}

	protected IscrizioneRegistroImpreseModel getIscrizioneRegistroImprese(IscrizioneRepertorioEconomicoDto iscrizioneRegistroDto) {
		var iscrizione = new IscrizioneRegistroImpreseModel();
		iscrizione.setNumeroRepertorioEconomicoAmministrativo(iscrizioneRegistroDto.getCodiceRea()).setProvinciaCameraCommercio(iscrizioneRegistroDto.getProvinciaRea())
				.setDataIscrizione(iscrizioneRegistroDto.getDataIscrizione()).setCessata(iscrizioneRegistroDto.isCessata());
		return iscrizione;
	}

	protected AnagraficaProxyClient getAnagraficaProxyClient() {
		return anagraficaProxyClient;
	}

	protected FascicoloValidatorComponent getValidator() {
		return validator;
	}

	protected UnitaTecnicoEconomicheModel buildUnitaLocale(UnitaLocaleDto unitaLocaleDto, PersonaModel personaModel, FonteDatoAnagrafico fonte) {
		if (unitaLocaleDto == null)
			return null;

		var ute = new UnitaTecnicoEconomicheModel();
		ute.setAttivita(unitaLocaleDto.getAttivita()).setAttivitaAteco(buildAttivitaUte(unitaLocaleDto.getAttivitaAteco(), ute, fonte)).setCausaleCessazione(unitaLocaleDto.getCausaleCessazione())
				.setDataApertura(unitaLocaleDto.getDataApertura()).setDataCessazione(unitaLocaleDto.getDataCessazione()).setDataDenunciaCessazione(unitaLocaleDto.getDataDenunciaCessazione())
				.setDataDenunciaInizioAttivita(unitaLocaleDto.getDataDenunciaInizioAttivita()).setDestinazioneUso(buildDestinazioneUso(unitaLocaleDto.getDestinazioniUso(), ute))
				.setIdentificativoUte(unitaLocaleDto.getIdentificativo()).setIndirizzoPec(unitaLocaleDto.getIndirizzoPec()).setPersona(personaModel)
				.setSettoreMerceologico(unitaLocaleDto.getSettoreMerceologico()).setTelefono(unitaLocaleDto.getTelefono());

		if (unitaLocaleDto.getIndirizzo() != null) {
			ute.setCap(unitaLocaleDto.getIndirizzo().getCap()).setCodiceIstatComune(unitaLocaleDto.getIndirizzo().getCodiceIstat()).setComune(unitaLocaleDto.getIndirizzo().getComune())
					.setFrazione(unitaLocaleDto.getIndirizzo().getFrazione()).setNumeroCivico(unitaLocaleDto.getIndirizzo().getCivico()).setProvincia(unitaLocaleDto.getIndirizzo().getProvincia())
					.setToponimo(unitaLocaleDto.getIndirizzo().getToponimo()).setVia(unitaLocaleDto.getIndirizzo().getVia());
		}

		return ute;
	}

	protected IndirizzoModel buildIndirizzo(IndirizzoDto indirizzoDto) {
		if (indirizzoDto == null) {
			return null;
		}
		var indirizzo = new IndirizzoModel();
		indirizzo.setVia(indirizzoDto.getVia()).setCap(indirizzoDto.getCap()).setCodiceIstat(indirizzoDto.getCodiceIstat()).setDescrizioneEstesa(indirizzoDto.getDenominazioneEstesa())
				.setFrazione(indirizzoDto.getFrazione()).setNumeroCivico(indirizzoDto.getCivico()).setProvincia(indirizzoDto.getProvincia()).setToponimo(indirizzoDto.getToponimo())
				.setComune(indirizzoDto.getComune());
		return indirizzo;
	}

	protected IscrizioneSezioneModel buildIscrizioneSezione(final PersonaModel personaModel, final IscrizioneSezioneDto source) {
		var dest = new IscrizioneSezioneModel();
		dest.setColtivatoreDiretto(source.getColtivatoreDiretto());
		dest.setDataIscrizione(source.getDataIscrizione());
		dest.setQualifica(source.getQualifica());
		dest.setPersona(personaModel);
		SezioneEnum sezione = source.getSezione();
		if (sezione.equals(SezioneEnum.ORDINARIA)) {
			dest.setSezione(it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.SezioneEnum.ORDINARIA);
		} else {
			dest.setSezione(it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.SezioneEnum.SPECIALE);
		}
		return dest;
	}

	protected List<IscrizioneSezioneModel> buildIscrizioniSezione(final PersonaModel personaModel, final List<IscrizioneSezioneDto> list) {
		if (list == null) {
			return new ArrayList<>();
		}
		return list.stream().map(source -> this.buildIscrizioneSezione(personaModel, source)).collect(Collectors.toList());
	}

	protected List<DestinazioneUsoModel> buildDestinazioneUso(List<String> destinazioniUso, UnitaTecnicoEconomicheModel ute) {
		List<DestinazioneUsoModel> result = new ArrayList<>();
		if (destinazioniUso != null && !destinazioniUso.isEmpty()) {
			destinazioniUso.forEach(uso -> {
				var d = new DestinazioneUsoModel();
				d.setDescrizione(uso);
				d.setUnitaTecnicoEconomiche(ute);
				result.add(d);
			});
		}
		return result;
	}

	protected List<AttivitaAtecoModel> buildAttivitaUte(List<AttivitaDto> attivitaAteco, UnitaTecnicoEconomicheModel ute, FonteDatoAnagrafico fonte) {
		/*
		 * TODO delete e re-inserimento??
		 */
		if (CollectionUtils.isEmpty(attivitaAteco))
			return null;

		List<AttivitaAtecoModel> attivita = new ArrayList<>();
		attivita.addAll(attivitaAteco.stream()
				// .filter(distinctByKey(AttivitaDto::getCodice))//così da evitare doppioni nelle attivita
				.map(attivitaMap -> mapAttivitaUte(attivitaMap, ute, fonte)).collect(Collectors.toList()));
		return attivita;

	}

	private AttivitaAtecoModel mapAttivitaUte(AttivitaDto attivitaDto, UnitaTecnicoEconomicheModel ute, FonteDatoAnagrafico fonte) {
		var attivitaAtecoModel = new AttivitaAtecoModel();
		attivitaAtecoModel.setCodice(attivitaDto.getCodice());
		attivitaAtecoModel.setDescrizione(attivitaDto.getDescrizione());
		attivitaAtecoModel.setImportanza(attivitaDto.getImportanza() == null ? null : ImportanzaAttivita.valueOf(attivitaDto.getImportanza().name()));
		attivitaAtecoModel.setFonte(fonte);
		attivitaAtecoModel.setUnitaTecnicoEconomiche(ute);
		return attivitaAtecoModel;
	}

	protected List<AttivitaAtecoModel> buildAttivita(List<AttivitaDto> attivitaAteco, PersonaModel personaModel, FonteDatoAnagrafico fonte) {
		/*
		 * TODO delete e re-inserimento??
		 */
		if (CollectionUtils.isEmpty(attivitaAteco))
			return null;

		List<AttivitaAtecoModel> attivita = new ArrayList<>();
		attivita.addAll(attivitaAteco.stream()
				// .filter(distinctByKey(AttivitaDto::getCodice))//così da evitare doppioni nelle attivita
				.map(attivitaMap -> mapAttivita(attivitaMap, personaModel, fonte)).collect(Collectors.toList()));
		return attivita;
	}

	private AttivitaAtecoModel mapAttivita(AttivitaDto attivitaDto, PersonaModel personaModel, FonteDatoAnagrafico fonte) {
		var attivitaAtecoModel = new AttivitaAtecoModel();
		attivitaAtecoModel.setCodice(attivitaDto.getCodice());
		attivitaAtecoModel.setDescrizione(attivitaDto.getDescrizione());
		attivitaAtecoModel.setImportanza(attivitaDto.getImportanza() == null ? null : ImportanzaAttivita.valueOf(attivitaDto.getImportanza().name()));
		attivitaAtecoModel.setFonte(fonte);
		attivitaAtecoModel.setPersonaModel(personaModel);
		attivitaAtecoModel.setIdValidazione(0);
		return attivitaAtecoModel;
	}

	private void deleteUteCascade(List<UnitaTecnicoEconomicheModel> uteList) {
		if (uteList != null && !uteList.isEmpty()) {
			uteList.forEach(ute -> {

				if (ute.getDestinazioneUso() != null && !ute.getDestinazioneUso().isEmpty()) {
					ute.getDestinazioneUso().forEach(uso -> {
						destinazioneUsoDao.delete(uso);
					});
				}

				if (ute.getAttivitaAteco() != null && !ute.getAttivitaAteco().isEmpty()) {
					ute.getAttivitaAteco().forEach(ateco -> {
						attivitaAtecoDao.delete(ateco);
					});
				}

				unitaTecnicoEconomicheDao.delete(ute);
			});
		}
	}

	/*
	 * 1. per ciascuna carica recupero lista delle cariche relative alla persona fisica/giuridica con carica e filtro la lista per le cariche collegate a fascicoli differenti da quello che sto
	 * processando 2. se la persona fisica/giuridica con carica ha cariche anche in altri fascicoli allora cancello solo la carica, NON cancello la persona con carica 3. se la persona fisica/giuridica
	 * con carica non ha cariche in altri fascicoli verifico se ha più cariche all'interno del fascicolo che sto processando: se ha una sola carica allora posso cancellare anche la persona con carica
	 */
	private void deleteCaricheEPersoneConCarica(List<CaricaModel> caricheList) {
		if (caricheList != null && !caricheList.isEmpty()) {
			caricheList.forEach(carica -> {

				if (carica.getPersonaFisicaConCaricaModel() != null) {
					List<CaricaModel> cariche = caricaDao.findByPersonaFisicaConCaricaModel(carica.getPersonaFisicaConCaricaModel());
					List<CaricaModel> result = new ArrayList<CaricaModel>();
					if (carica.getPersonaFisicaModel() != null) {
						result = cariche.stream().filter(person -> !person.getPersonaFisicaModel().equals(carica.getPersonaFisicaModel())).collect(Collectors.toList());
					} else {
						result = cariche.stream().filter(person -> !person.getPersonaGiuridicaModel().equals(carica.getPersonaGiuridicaModel())).collect(Collectors.toList());
					}
					if (result != null && !result.isEmpty()) {
						caricaDao.delete(carica);
					} else {
						List<CaricaModel> personaFisicaConCarica = caricaDao.findByPersonaFisicaConCaricaModel(carica.getPersonaFisicaConCaricaModel());
						caricaDao.delete(carica);
						if (personaFisicaConCarica != null && !personaFisicaConCarica.isEmpty() && personaFisicaConCarica.size() == 1) {
							personaFisicaConCaricaDao.delete(personaFisicaConCarica.get(0).getPersonaFisicaConCaricaModel());
						}
					}
				} else if (carica.getPersonaGiuridicaConCaricaModel() != null) {
					List<CaricaModel> cariche = caricaDao.findByPersonaGiuridicaConCaricaModel(carica.getPersonaGiuridicaConCaricaModel());
					List<CaricaModel> result = new ArrayList<CaricaModel>();
					if (carica.getPersonaFisicaModel() != null) {
						result = cariche.stream().filter(person -> !person.getPersonaFisicaModel().equals(carica.getPersonaFisicaModel())).collect(Collectors.toList());
					} else {
						result = cariche.stream().filter(person -> !person.getPersonaGiuridicaModel().equals(carica.getPersonaGiuridicaModel())).collect(Collectors.toList());
					}
					if (result != null && !result.isEmpty()) {
						caricaDao.delete(carica);
					} else {
						List<CaricaModel> personaFisicaConCarica = caricaDao.findByPersonaGiuridicaConCaricaModel(carica.getPersonaGiuridicaConCaricaModel());
						caricaDao.delete(carica);
						if (personaFisicaConCarica != null && !personaFisicaConCarica.isEmpty() && personaFisicaConCarica.size() == 1) {
							personaGiuridicaConCaricaDao.delete(personaFisicaConCarica.get(0).getPersonaGiuridicaConCaricaModel());
						}
					}
				}
			});
		}
	}

	protected void salvataggioFirmatario() throws FascicoloAllaFirmaAziendaException, FascicoloAllaFirmaCAAException {
		FascicoloModel fascicolo = fascicoloDao.findByCuaaAndIdValidazione(getCodiceFiscaleDaCaa(), 0)
				.orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));
		if (fascicolo.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA)) {
			throw new FascicoloAllaFirmaAziendaException();
		}
		if (fascicolo.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_CAA)) {
			throw new FascicoloAllaFirmaCAAException();
		}
		// un firmatario puo' avere piu' cariche
		if (fascicolo.getPersona() instanceof PersonaFisicaModel) {
			caricaConFirmatarioPrecedenteList = ((PersonaFisicaModel) fascicolo.getPersona()).getCariche().stream().filter(carica -> carica.isFirmatario().equals(true)).collect(Collectors.toList());
		} else {
			caricaConFirmatarioPrecedenteList = ((PersonaGiuridicaModel) fascicolo.getPersona()).getCariche().stream().filter(carica -> carica.isFirmatario().equals(true))
					.collect(Collectors.toList());
		}
	}

	// 1) nel caso in cui il rappresentante legale con diritto di firma indicato dal CAA non abbia più i requisiti (ad es. la carica aggiornata non corrisponde a quella salvata, non è presente una
	// carica con firmatario, ecc) dopo l’aggiornamento,
	// il sistema lo sostituisce con quello di Anagrafe tributaria; in questo caso non sara' impostato firmatario = 1 ad una carica.
	// 2) se il firmatario salvato e' valido allora si deve impostare firmatario = 1 in base a persona fisica/giuridica con carica (cf,idvalidazione =0)
	protected boolean firmatarioPersonaFisicaOGiuridicaValido(CaricaModel carica) {
		if (caricaConFirmatarioPrecedenteList != null || !caricaConFirmatarioPrecedenteList.isEmpty()) {
			for (CaricaModel caricaConFirmatarioPrecedente : caricaConFirmatarioPrecedenteList) {
				var personaFisicaConCaricaPrecedente = caricaConFirmatarioPrecedente.getPersonaFisicaConCaricaModel();
				if (personaFisicaConCaricaPrecedente != null && carica.getPersonaFisicaConCaricaModel() != null
						&& carica.getIdentificativo().equalsIgnoreCase(caricaConFirmatarioPrecedente.getIdentificativo())
						&& carica.getPersonaFisicaConCaricaModel().getCodiceFiscale().equalsIgnoreCase(personaFisicaConCaricaPrecedente.getCodiceFiscale())
						&& CaricheRappresentanteLegaleMap.getCaricheRappresentantiLegali().get(caricaConFirmatarioPrecedente.getIdentificativo()) != null) {
					return true;
				}
				var personaGiuridicaConCaricaPrecedente = caricaConFirmatarioPrecedente.getPersonaGiuridicaConCaricaModel();
				if (personaGiuridicaConCaricaPrecedente != null && carica.getPersonaGiuridicaConCaricaModel() != null
						&& carica.getPersonaGiuridicaConCaricaModel().getCodiceFiscale().equalsIgnoreCase(personaGiuridicaConCaricaPrecedente.getCodiceFiscale())
						&& CaricheRappresentanteLegaleMap.getCaricheRappresentantiLegali().get(caricaConFirmatarioPrecedente.getIdentificativo()) != null) {
					return true;
				}
			}
		}
		return false;
	}
}
