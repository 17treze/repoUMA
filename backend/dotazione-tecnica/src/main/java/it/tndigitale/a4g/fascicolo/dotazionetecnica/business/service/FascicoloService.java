package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.event.StartControlloCompletezzaEvent;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.ControlloCompletezzaModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.DatiCatastaliModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.FabbricatoModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MacchinaModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MacchinaMotorizzataModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.SerreModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.StoccaggioModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.StrumentaliModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.ControlloCompletezzaDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.DatiCatastaliDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.exceptions.CatastoEdificialeClasseCatastaleNonAmmessaException;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.exceptions.CatastoParticellaEstintaException;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.FabbricatiConParticelleInvalide;
import it.tndigitale.a4g.framework.component.dto.EsitoControlloDto;
import it.tndigitale.a4g.framework.component.dto.SegnalazioneDto;
import it.tndigitale.a4g.framework.component.dto.TipoSegnalazioneEnum;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;

@Service
public class FascicoloService {

	@Autowired
	private ControlloCompletezzaDao controlloCompletezzaDao;
	@Autowired
	private FascicoloDao fascicoloDao;
	@Autowired
	private UtenteComponent utenteComponent;
	@Autowired
	private EventBus eventBus;
	@Autowired
	private FabbricatiService fabbricatiService;
	@Autowired
	private DatiCatastaliDao datiCatastaliDao;

	private static final Logger logger = LoggerFactory.getLogger(FascicoloService.class);

	public Map<ControlliFascicoloDotazioneTecnicaCompletoEnum, EsitoControlloDto> queryControlloCompletezzaFascicolo(final String cuaa) {
		var controlliList = controlloCompletezzaDao.findByCuaa(cuaa);
		var resultMap = new HashMap<ControlliFascicoloDotazioneTecnicaCompletoEnum, EsitoControlloDto>();
		if (controlliList == null || controlliList.isEmpty()) {
			return resultMap;
		}
		var controlliFilteredList = controlliList.stream().filter(c -> contains(c.getTipoControllo())).collect(Collectors.toList());
		for (ControlloCompletezzaModel controlloCompletezzaModel : controlliFilteredList) {
			var esitoDto = new EsitoControlloDto();
			esitoDto.setEsito(controlloCompletezzaModel.getEsito());
			if (controlloCompletezzaModel.getIdControllo() != null) {
				esitoDto.setIdControllo(Long.valueOf(controlloCompletezzaModel.getIdControllo()));
			}
			if (controlloCompletezzaModel.getTipoControllo().equals(ControlliFascicoloDotazioneTecnicaCompletoEnum.IS_FABBRICATI_CONSISTENTI.name())) {
				List<FabbricatiConParticelleInvalide> fabbricatiConParticelleInvalide = fascicoloDao.getFabbricatiConParticelleInvalidePerCuaaSql(cuaa);
				List<SegnalazioneDto> segnalazioni = new ArrayList<SegnalazioneDto>();
				fabbricatiConParticelleInvalide.forEach(fabbricato -> {
					SegnalazioneDto segnalazione = new SegnalazioneDto("Il fabbricato di tipologia " + fabbricato.getTipologia() + ", ubicato a " + fabbricato.getComune() + ", con superficie "
							+ fabbricato.getSuperficie() + " mq, contiene particelle invalide", TipoSegnalazioneEnum.ERRORE);
					segnalazioni.add(segnalazione);
				});
				esitoDto.setSegnalazioni(segnalazioni);
			}
			resultMap.put(ControlliFascicoloDotazioneTecnicaCompletoEnum.valueOf(controlloCompletezzaModel.getTipoControllo()), esitoDto);
		}
		return resultMap;
	}

	@Transactional
	public void rimozioneControlliCompletezza(String cuaa) {
		List<ControlloCompletezzaModel> resultList = controlloCompletezzaDao.findByCuaa(cuaa);
		if (resultList != null && resultList.size() > 0) {
			controlloCompletezzaDao.deleteInBatch(resultList);
		}
	}

	@Transactional
	public void startControlloCompletezzaFascicoloAsincrono(final String cuaa, final Integer idValidazione) {
		try {
			// controllo se esiste localmente un fascicolo associato al cuaa, altrimenti si lancia eccezione e si esce
			fascicoloDao.findByCuaaAndIdValidazione(cuaa, idValidazione).orElseThrow();

			// check dell'esistenza di un controllo di completezza esistente. In tal caso il record viene eliminato
			var controlloCompletezzaList = controlloCompletezzaDao.findByCuaa(cuaa);
			if (controlloCompletezzaList.size() > 0) {
				controlloCompletezzaDao.deleteInBatch(controlloCompletezzaList);
			}

			// salvare nel db i dati del controllo di completezza tranne l'esito che verrà gestito dal listener
			for (ControlliFascicoloDotazioneTecnicaCompletoEnum controllo : ControlliFascicoloDotazioneTecnicaCompletoEnum.values()) {
				var controlloCompletezzaModel = new ControlloCompletezzaModel();
				controlloCompletezzaModel.setCuaa(cuaa);
				controlloCompletezzaModel.setTipoControllo(controllo.name());
				controlloCompletezzaModel.setUtente(utenteComponent.username());
				controlloCompletezzaModel.setDataEsecuzione(LocalDateTime.now());
				controlloCompletezzaDao.save(controlloCompletezzaModel);
			}

			var event = new StartControlloCompletezzaEvent(cuaa, idValidazione);
			eventBus.publishEvent(event);
		} catch (NoSuchElementException | EntityNotFoundException e) {
			logger.warn("cuaa non censito: {}", cuaa);
		}
	}

	@Transactional
	public Map<ControlliFascicoloDotazioneTecnicaCompletoEnum, EsitoControlloDto> getControlloCompletezzaFascicolo(final String cuaa) {
		var fascicolo = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0).orElseThrow();
		return getControlloCompletezzaFascicolo(fascicolo);
	}

	public Map<ControlliFascicoloDotazioneTecnicaCompletoEnum, EsitoControlloDto> getControlloCompletezzaFascicolo(final FascicoloModel fascicoloModel) {
		var retList = new EnumMap<ControlliFascicoloDotazioneTecnicaCompletoEnum, EsitoControlloDto>(ControlliFascicoloDotazioneTecnicaCompletoEnum.class);
		retList.put(ControlliFascicoloDotazioneTecnicaCompletoEnum.IS_MACCHINARI_CONSISTENTI, isMacchinariConsistenti(fascicoloModel));
		retList.put(ControlliFascicoloDotazioneTecnicaCompletoEnum.IS_FABBRICATI_CONSISTENTI, isFabbricatiConsistenti(fascicoloModel));
		return retList;
	}

	private EsitoControlloDto areDocumentiPossessoPresenti(final FascicoloModel fascicoloModel) {
		var ok = fascicoloModel.getMacchine().stream().allMatch(macchina -> macchina.getDocumentoPossesso() != null);
		var esito = new EsitoControlloDto();
		esito.setEsito(ok ? 0 : -3);
		esito.setIdControllo(0L);
		return esito;
	}

	private EsitoControlloDto isMacchinariConsistenti(final FascicoloModel fascicoloModel) {

		boolean allMatch = fascicoloModel.getMacchine().stream().allMatch(isMacchinaConsistente);
		var esito = new EsitoControlloDto();
		esito.setEsito(allMatch ? 0 : -3); // esito ok : esito bloccante (non è possibile validare il fascicolo)
		esito.setIdControllo(0L); // id controllo è relativo al recupero dati da ags

		return esito;
	}

	private EsitoControlloDto isFabbricatiConsistenti(final FascicoloModel fascicoloModel) {
		boolean allMatch = fascicoloModel.getFabbricati().stream().allMatch(isFabbricatoConsistente);
		var esito = new EsitoControlloDto();
		esito.setEsito(allMatch ? 0 : -3); // esito ok : esito bloccante (non è possibile validare il fascicolo)
		esito.setIdControllo(0L); // id controllo è relativo al recupero dati da ags
		List<SegnalazioneDto> segnalazioni = new ArrayList<SegnalazioneDto>();
		esito.setSegnalazioni(segnalazioni);
		return esito;
	}

	/*
	 * Dati obbligatori macchinario. Mezzo Agricolo: tipologia sottotipologia marca modello targa numero matricola o telaio anno immatricolazione
	 * 
	 * se ha il Motore: alimentazione potenza
	 * 
	 * Possesso: tipo possesso documento di possesso
	 */
	private Predicate<MacchinaModel> isMacchinaConsistente = macchina -> {
		List<Boolean> consistenza = new ArrayList<>();
		try {

			MacchinaMotorizzataModel macchinaMotorizzata = (MacchinaMotorizzataModel) null;

			if (macchina instanceof MacchinaMotorizzataModel) {
				macchinaMotorizzata = (MacchinaMotorizzataModel) macchina;
			}

			// TODO: nei prossimi task sostituire stringhe con enum (eliminazione TipologiaDao)
			var tipologia = macchina.getSottotipoMacchinario().getClassefunzionale().getTipologia().getDescrizione();

			if (tipologia.equals("MACCHINE E ATTREZZATURE ESCLUSE DA CARBURANTE AGEVOLATO PER USO AGRICOLO")) {
				// verifico campi obbligatori

			} else if (tipologia.equals("TRATTRICI AGRICOLE") || tipologia.equals("MACCHINE AGRICOLE OPERATRICI SEMOVENTI A DUE O PIU ASSI")) {
				consistenza.add(macchina.getTipoPossesso() != null); // verifico tipologia possesso
				consistenza.add(macchina.getTarga() != null);// verifico targa

				consistenza.add(macchinaMotorizzata.getAlimentazione() != null);// verifico alimentazione
				if (macchinaMotorizzata.getAlimentazione().equals("BENZINA")) {
					consistenza.add(macchinaMotorizzata.getPotenza() != null); // Verifico la potenza se alimentazione=benzina
				}

			} else if (tipologia.equals("MACCHINE AGRICOLE OPERATRICI SEMOVENTI AD UN ASSE")) {
				consistenza.add(macchina.getTipoPossesso() != null); // verifico tipologia possesso
				consistenza.add(macchinaMotorizzata.getAlimentazione() != null);// verifico alimentazione
				consistenza.add(macchina.getNumeroTelaio() != null);// verifico numero telaio

			} else if (tipologia.equals("MACCHINE AGRICOLE OPERATRICI TRAINATE") || tipologia.equals("ATTREZZATURE PORTATE O SEMIPORTATE")
					|| tipologia.equals("RIMORCHI AGRICOLI CON MASSA COMPLESSIVA A PIENO CARICO FINO A 1,5T")) {
				consistenza.add(macchina.getTipoPossesso() != null); // verifico tipologia possesso
				consistenza.add(macchina.getNumeroTelaio() != null); // verifico numero telaio

				if (macchinaMotorizzata != null) {
					if (macchinaMotorizzata.getAlimentazione().equals("BENZINA")) {
						consistenza.add(macchinaMotorizzata.getPotenza() != null); // Verifico la potenza se alimentazione=benzina
					}
				}

			} else if (tipologia.equals("RIMORCHI AGRICOLI CON MASSA COMPLESSIVA A PIENO CARICO SUPERIORE A 1,5T")) {
				consistenza.add(macchina.getTipoPossesso() != null); // verifico tipologia possesso
				consistenza.add(macchina.getTarga() != null); // verifico targa

				if (macchinaMotorizzata != null) {
					if (macchinaMotorizzata.getAlimentazione().equals("BENZINA")) {
						consistenza.add(macchinaMotorizzata.getPotenza() != null); // Verifico la potenza se alimentazione=benzina
					}
				}

			} else if (tipologia.equals("MACCHINE OPERATRICI ADIBITE E ATTREZZATE PERMANENTEMENTE PER LAVORI AGRICOLI")) {

				consistenza.add(macchina.getTipoPossesso() != null); // verifico tipologia possesso
				consistenza.add(macchina.getTarga() != null);// verifico targa
				consistenza.add(macchinaMotorizzata.getAlimentazione() != null);// verifico alimentazione
				if (macchinaMotorizzata.getAlimentazione().equals("BENZINA")) {
					consistenza.add(macchinaMotorizzata.getPotenza() != null); // Verifico la potenza se alimentazione=benzina
				}

			} else if (tipologia.equals("IMPIANTI ED ATTREZZATURE DESTINATI AD ESSERE IMPIEGATI NELLE ATTIVITA' AGRICOLE E FORESTALI")
					|| tipologia.equals("MACCHINE PER LA PRIMA TRASFORMAZIONE DEI PRODOTTI AGRICOLI")
					|| tipologia.equals("IMPIANTI DI RISCALDAMENTO DELLE SERRE E DEI LOCALI ADIBITI AD ATTIVITA' DI PRODUZIONE")) {
				consistenza.add(macchina.getTipoPossesso() != null); // verifico tipologia possesso
				consistenza.add(macchinaMotorizzata.getAlimentazione() != null);// verifico alimentazione
				// Condizione alternata per numeroTelaio e numeroMotore

				if ((!macchina.getNumeroTelaio().isEmpty()) && (!macchinaMotorizzata.getMatricola().isEmpty())) {
					consistenza.add(macchina.getNumeroTelaio() != null);
					consistenza.add(macchinaMotorizzata.getMatricola() != null);

					if (macchina.getNumeroTelaio() == null) {
						System.out.println("getNumeroTelaio() NON presente");
					}
					if (macchinaMotorizzata.getMatricola() == null) {
						System.out.println("macchinaMotorizzata.getMatricola NON presente");
					}

				}
				if (macchinaMotorizzata != null) {
					if (macchinaMotorizzata.getAlimentazione().equals("BENZINA")) {
						consistenza.add(macchinaMotorizzata.getPotenza() != null);

						if (macchinaMotorizzata.getPotenza() == null) {
							System.out.println("getPotenza NON presente");
						}

					}
				}

			}

			consistenza.add(macchina.getTipoPossesso() != null);

		} catch (Exception e) {
			consistenza.add(false);
			logger.warn("Macchina non valida {}: {}", macchina.getId(), e.getMessage());
		}
		return consistenza.stream().allMatch(Boolean.TRUE::equals);
	};

	private Predicate<FabbricatoModel> isFabbricatoConsistente = fabbricato -> {
		List<Boolean> consistenza = new ArrayList<>();
		try {
			// validazione dati catastali
			if (CollectionUtils.isEmpty(fabbricato.getDatiCatastali())) {
				consistenza.add(false);
			} else {
				fabbricato.getDatiCatastali().stream().filter(DatiCatastaliModel::getInTrentino).forEach(p -> {
					consistenza.add(p.getComune() != null);
					consistenza.add(p.getParticella() != null);
					consistenza.add(p.getTipologia() != null);
					try {
						if (p.getSub() != null) {
							fabbricatiService.getInfoFabbricati(p.getParticella(), p.getDenominatore(), p.getTipologia(), Integer.parseInt(p.getComune()), Integer.parseInt(p.getSub()));
						} else {
							fabbricatiService.getInfoFabbricati(p.getParticella(), p.getDenominatore(), p.getTipologia(), Integer.parseInt(p.getComune()), null);
						}
						p.setEsito(EsitoValidazioneParticellaCatastoEnum.VALIDA);
					} catch (EntityNotFoundException | NumberFormatException | CatastoParticellaEstintaException | CatastoEdificialeClasseCatastaleNonAmmessaException e) {
						logger.warn("particella invalida: {}", e);
						p.setEsito(EsitoValidazioneParticellaCatastoEnum.INVALIDA);
					}
					datiCatastaliDao.save(p);
					consistenza.add(p.getEsito().equals(EsitoValidazioneParticellaCatastoEnum.VALIDA));
				});

				fabbricato.getDatiCatastali().stream().filter(p -> !p.getInTrentino()).forEach(p -> {
					consistenza.add(p.getSezione() != null);
					consistenza.add(p.getFoglio() != null);
					consistenza.add(p.getTipologia() != null);
					consistenza.add(p.getParticella() != null);
				});
			}

			// validazione fabbricato
			consistenza.add(fabbricato.getSottotipo() != null);
			consistenza.add(fabbricato.getSottotipo().getTipologia() != null);
			consistenza.add(fabbricato.getComune() != null);
			consistenza.add(fabbricato.getTipoConduzione() != null);

			// TODO: nei prossimi task sostituire stringhe con enum (eliminazione TipologiaDao)
			var tipologia = fabbricato.getSottotipo().getTipologia().getDescrizione();
			if (tipologia.equals("STALLE") || tipologia.equals("AREE_SCOPERTE")) {
				consistenza.add(fabbricato.getSuperficie() != null);

			}

			if (fabbricato instanceof StrumentaliModel) {
				var fabbricatoStrumentale = (StrumentaliModel) fabbricato;
				consistenza.add(fabbricatoStrumentale.getSuperficie() != null);
			}
			if (fabbricato instanceof SerreModel) {
				var serra = (SerreModel) fabbricato;
				consistenza.add(serra.getVolume() != null);
			}
			if (fabbricato instanceof StoccaggioModel) {
				var stoccaggio = (StoccaggioModel) fabbricato;
				consistenza.add(stoccaggio.getCopertura() != null);
				consistenza.add(stoccaggio.getSuperficie() != null);
				consistenza.add(stoccaggio.getAltezza() != null);
				consistenza.add(stoccaggio.getVolume() != null);
			}
		} catch (Exception e) {
			consistenza.add(false);
			logger.warn("Fabbricato non valido: {} {}", fabbricato.getId(), e.getMessage());
		}
		return consistenza.stream().allMatch(Boolean.TRUE::equals);
	};

	private static boolean contains(String test) {
		if (StringUtils.isBlank(test)) {
			return false;
		}
		for (ControlliFascicoloDotazioneTecnicaCompletoEnum c : ControlliFascicoloDotazioneTecnicaCompletoEnum.values()) {
			if (c.name().equals(test)) {
				return true;
			}
		}
		return false;
	}
}