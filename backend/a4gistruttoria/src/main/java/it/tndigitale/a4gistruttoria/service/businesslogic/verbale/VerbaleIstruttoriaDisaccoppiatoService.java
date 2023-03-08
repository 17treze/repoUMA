package it.tndigitale.a4gistruttoria.service.businesslogic.verbale;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.component.StampaComponent;
import it.tndigitale.a4gistruttoria.dto.DatiIstruttoriaDisaccoppiatoStampa;
import it.tndigitale.a4gistruttoria.dto.InfoLiquidazioneDomandaStampa;
import it.tndigitale.a4gistruttoria.dto.PassoLavorazioneStampa;
import it.tndigitale.a4gistruttoria.dto.VariabileStampa;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiCalcoli;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.builder.DatiIstruttoriaBuilder;
import it.tndigitale.a4gistruttoria.repository.dao.PassoTransizioneDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.ElencoLiquidazioneModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.DisaccoppiatoService;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.helper.IstruttoriaDisaccoppiatoHelper;
import it.tndigitale.a4gistruttoria.util.AmbitoVariabile;
import it.tndigitale.a4gistruttoria.util.FormatoVariabile;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@Service
@Transactional(readOnly = true)
public class VerbaleIstruttoriaDisaccoppiatoService extends VerbaleIstruttoriaService {
	
	private static final Logger logger = LoggerFactory.getLogger(VerbaleIstruttoriaDisaccoppiatoService.class);
	
	@Autowired
	private PassoTransizioneDao daoPassiLavSostegno;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private DisaccoppiatoService disaccoppiatoService;

	@Autowired
	private StampaComponent stampaComponent;
	
	private static final String RISERVA_NON_RICHIESTA = "NON RICHIESTA";
	
    @Override
    public byte[] stampa(final Long idIstruttoria) throws Exception {
		Optional<IstruttoriaModel> istruttoriaOpt = istruttoriaDao.findById(idIstruttoria);
		DomandaUnicaModel domandaUnicaModel = getDomandaUnica(idIstruttoria);
		InfoLiquidazioneDomandaStampa infoLiquidazione = new InfoLiquidazioneDomandaStampa();
		infoLiquidazione.setDomandaNonRicevibile(false);
		infoLiquidazione.setDomandaNonAmmissibile(false);
		infoLiquidazione.setDomandaNonLiquidabile(false);


		DatiIstruttoriaDisaccoppiatoStampa datiDomandaStampa = new DatiIstruttoriaDisaccoppiatoStampa(getDatiDomandaStampa(istruttoriaOpt.get()));
		getEsitoRicevibilita(datiDomandaStampa, domandaUnicaModel);

		if (domandaUnicaModel.getStato().equals(StatoDomanda.NON_RICEVIBILE)) {
			infoLiquidazione.setDomandaNonRicevibile(true);
		} else if (istruttoriaOpt.isPresent()) {
			IstruttoriaModel istruttoria = istruttoriaOpt.get();
			popolaPassoLavorazioneIntersostegno(datiDomandaStampa, istruttoria);
			popolaTransizioneCalcolo(datiDomandaStampa, domandaUnicaModel, infoLiquidazione, istruttoria);
		}
		datiDomandaStampa.setInfoLiquidazione(infoLiquidazione);
		String jsonToken = mapper.writeValueAsString(datiDomandaStampa);
		return stampaComponent.stampaPDF_A(
				jsonToken, "template/verbaleIstruttoriaDisaccoppiato.docx");
    }

	private void popolaInfoLiquidazione(final InfoLiquidazioneDomandaStampa infoLiquidazione, final IstruttoriaModel istruttoria) {
		ElencoLiquidazioneModel elencoLiquidazione = istruttoria.getElencoLiquidazione();
		if (elencoLiquidazione != null) {
			infoLiquidazione.setCodiceElencoLiquidazione(elencoLiquidazione.getCodElenco());
		} else {
			if (istruttoria.getStato().equals(StatoIstruttoria.NON_AMMISSIBILE)) {
				infoLiquidazione.setDomandaNonAmmissibile(true);
			}
			if (istruttoria.getStato().equals(StatoIstruttoria.NON_LIQUIDABILE)) {
				infoLiquidazione.setDomandaNonLiquidabile(true);
			}
		}
	}

	private void popolaTransizioneCalcolo(
			final DatiIstruttoriaDisaccoppiatoStampa datiDomandaStampa,
			final DomandaUnicaModel datiDomanda,
			final InfoLiquidazioneDomandaStampa infoLiquidazione,
			final IstruttoriaModel istruttoria) {

		Optional<TransizioneIstruttoriaModel> transizioneCalcoloOpt = getUltimaTransizioneCalcolo(istruttoria);
		if (transizioneCalcoloOpt.isPresent()) {
			TransizioneIstruttoriaModel transizione = transizioneCalcoloOpt.get();
			List<PassoTransizioneModel> a4gtPassiLavSostegno = daoPassiLavSostegno.findByTransizioneIstruttoria(transizione);
			if (!a4gtPassiLavSostegno.isEmpty()) {
				List<PassoLavorazioneStampa> passiLavorazione = new ArrayList<>();
				a4gtPassiLavSostegno.stream().filter(p -> !TipologiaPassoTransizione.ANOMALIE_MANTENIMENTO.equals(p.getCodicePasso())
						&& !p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO) 
						&& !p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA))
					.forEach(p -> popolaPassoLavorazioneStampa(datiDomandaStampa, passiLavorazione, p));
				popolaInfoLiquidazione(infoLiquidazione, istruttoria);

				popolaSintesiCalcolo(datiDomandaStampa, a4gtPassiLavSostegno);

				DatiIstruttoria datiIstruttoria = DatiIstruttoriaBuilder.from(istruttoria.getDatiIstruttoreDisModel());
				datiDomandaStampa.setPassoLavStampa(passiLavorazione);
				datiDomandaStampa.setDatiIstruttoria(datiIstruttoria);
				datiDomandaStampa.setCampione(disaccoppiatoService.isCampione(datiDomanda) ? "SI" : "NO");
				String riserva = disaccoppiatoService.getRiserva(datiDomanda);
				datiDomandaStampa.setAccessoAllaRiserva(riserva != null ? riserva : RISERVA_NON_RICHIESTA);
			} else {
				logger.error(
						"Stampa domanda : transizione di calcolo disaccoppiato non presente per la domanda {}",
						datiDomanda.getId());
			}
		}
	}

	private void popolaSintesiCalcolo(DatiIstruttoriaDisaccoppiatoStampa datiIstruttoriaDisaccoppiatoStampa, List<PassoTransizioneModel> passiTransizione) {
		try {
			popolaImportoFinaleSintesi(datiIstruttoriaDisaccoppiatoStampa, passiTransizione);
			popolaImportoRiduzioniSintesi(datiIstruttoriaDisaccoppiatoStampa, passiTransizione);
			datiIstruttoriaDisaccoppiatoStampa.setImportoRiduzionePremioBase(IstruttoriaDisaccoppiatoHelper.recuperaVariabile(passiTransizione, TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSIMPRID, AmbitoVariabile.OUTPUT).recuperaValoreString());
			datiIstruttoriaDisaccoppiatoStampa.setImportoRiduzioneGreening(IstruttoriaDisaccoppiatoHelper.recuperaVariabile(passiTransizione, TipologiaPassoTransizione.GREENING, TipoVariabile.GREIMPRID, AmbitoVariabile.OUTPUT).recuperaValoreString());
			datiIstruttoriaDisaccoppiatoStampa.setImportoRiduzioneGiovane(IstruttoriaDisaccoppiatoHelper.recuperaVariabile(passiTransizione, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPRID, AmbitoVariabile.OUTPUT).recuperaValoreString());
			datiIstruttoriaDisaccoppiatoStampa.setImportoRiduzioneRitardo(IstruttoriaDisaccoppiatoHelper.recuperaVariabile(passiTransizione, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.IMPRIDRIT, AmbitoVariabile.OUTPUT).recuperaValoreString());
			datiIstruttoriaDisaccoppiatoStampa.setImportoRiduzioneCapping(IstruttoriaDisaccoppiatoHelper.recuperaVariabile(passiTransizione, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.IMPRIDCAP, AmbitoVariabile.OUTPUT).recuperaValoreString());
			datiIstruttoriaDisaccoppiatoStampa.setImportoSanzioni(IstruttoriaDisaccoppiatoHelper.recuperaVariabile(passiTransizione, TipologiaPassoTransizione.RIEPILOGO_SANZIONI, TipoVariabile.SANZTOT, AmbitoVariabile.OUTPUT).recuperaValoreString());
			popolaSanzionePremioBase(datiIstruttoriaDisaccoppiatoStampa, passiTransizione);
			datiIstruttoriaDisaccoppiatoStampa.setImportoSanzioniGreening(IstruttoriaDisaccoppiatoHelper.recuperaVariabile(passiTransizione, TipologiaPassoTransizione.GREENING, TipoVariabile.GREIMPSANZ, AmbitoVariabile.OUTPUT).recuperaValoreString());
			popolaSanzioneGiovane(datiIstruttoriaDisaccoppiatoStampa, passiTransizione);
		} catch (Exception e) {
			logger.error(
					"Stampa domanda : errore nella popola sintesi calcolo per la domanda {}",
					datiIstruttoriaDisaccoppiatoStampa.getNumeroDomanda());
		}
	}

	private void popolaImportoFinaleSintesi(DatiIstruttoriaDisaccoppiatoStampa datiIstruttoriaDisaccoppiatoStampa, List<PassoTransizioneModel> passiTransizione) throws Exception {
		BigDecimal importoFinale = IstruttoriaDisaccoppiatoHelper.getValoreVariabile(passiTransizione, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.IMPCALCFIN, AmbitoVariabile.OUTPUT);
		if (importoFinale.compareTo(new BigDecimal(0)) > 0) {
			datiIstruttoriaDisaccoppiatoStampa.setImportoFinale(VariabileCalcolo.recuperaValoreStringNumeric(false, false, TipoVariabile.IMPCALCFIN, importoFinale));
		}
	}

	private void popolaImportoRiduzioniSintesi(DatiIstruttoriaDisaccoppiatoStampa datiIstruttoriaDisaccoppiatoStampa, List<PassoTransizioneModel> passiTransizione) throws Exception {
		BigDecimal importoRiduzioni = IstruttoriaDisaccoppiatoHelper.getValoreVariabile(passiTransizione, TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSIMPRID, AmbitoVariabile.OUTPUT)
				.add(IstruttoriaDisaccoppiatoHelper.getValoreVariabile(passiTransizione, TipologiaPassoTransizione.GREENING, TipoVariabile.GREIMPRID, AmbitoVariabile.OUTPUT))
				.add(IstruttoriaDisaccoppiatoHelper.getValoreVariabile(passiTransizione, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPRID, AmbitoVariabile.OUTPUT))
				.add(IstruttoriaDisaccoppiatoHelper.getValoreVariabile(passiTransizione, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.IMPRIDRIT, AmbitoVariabile.OUTPUT))
				.add(IstruttoriaDisaccoppiatoHelper.getValoreVariabile(passiTransizione, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.IMPRIDCAP, AmbitoVariabile.OUTPUT));
		if (importoRiduzioni.compareTo(new BigDecimal(0)) > 0) {
			datiIstruttoriaDisaccoppiatoStampa.setImportoRiduzioni(VariabileCalcolo.recuperaValoreStringNumeric(false, false, TipoVariabile.BPSIMPRID, importoRiduzioni));
		}
	}

	private void popolaSanzionePremioBase(DatiIstruttoriaDisaccoppiatoStampa datiIstruttoriaDisaccoppiatoStampa, List<PassoTransizioneModel> passiTransizione) throws Exception {
		BigDecimal sanzionePremioBase = IstruttoriaDisaccoppiatoHelper.getValoreVariabile(passiTransizione, TipologiaPassoTransizione.SANZIONI_BPS, TipoVariabile.BPSIMPSANZ, AmbitoVariabile.OUTPUT)
				.add(IstruttoriaDisaccoppiatoHelper.getValoreVariabile(passiTransizione, TipologiaPassoTransizione.SANZIONI_BPS, TipoVariabile.BPSIMPSANZREC, AmbitoVariabile.OUTPUT));
		if (sanzionePremioBase.compareTo(new BigDecimal(0)) > 0) {
			datiIstruttoriaDisaccoppiatoStampa.setImportoFinale(VariabileCalcolo.recuperaValoreStringNumeric(false, false, TipoVariabile.BPSIMPSANZ, sanzionePremioBase));
		}
	}

	private void popolaSanzioneGiovane(DatiIstruttoriaDisaccoppiatoStampa datiIstruttoriaDisaccoppiatoStampa, List<PassoTransizioneModel> passiTransizione) throws Exception {
		BigDecimal sanzioneGiovane = IstruttoriaDisaccoppiatoHelper.getValoreVariabile(passiTransizione, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPSANZ, AmbitoVariabile.OUTPUT)
				.add(IstruttoriaDisaccoppiatoHelper.getValoreVariabile(passiTransizione, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPSANZREC, AmbitoVariabile.OUTPUT));
		if (sanzioneGiovane.compareTo(new BigDecimal(0)) > 0) {
			datiIstruttoriaDisaccoppiatoStampa.setImportoSanzioniGiovane(VariabileCalcolo.recuperaValoreStringNumeric(false, false, TipoVariabile.GIOIMPSANZ, sanzioneGiovane));
		}
	}

	private void popolaEsitiControlli(
			DatiIstruttoriaDisaccoppiatoStampa datiDomandaStampa, PassoTransizioneModel passoTransizione,
			DatiSintesi sintesi) throws IOException {
		// Recupero esiti controlli ammissibilita dal passo ammissibilita
		// TODO Lorenzo vederlo con Betty
		if (passoTransizione.getCodicePasso().equals(TipologiaPassoTransizione.AMMISSIBILITA)) {
			if (passoTransizione.getDatiSintesiLavorazione() != null) {
				sintesi = mapper.readValue(passoTransizione.getDatiSintesiLavorazione(), DatiSintesi.class);
			}
			sintesi.getEsitiControlli().forEach(esitiControlli -> {
				TipoControllo tipoControllo = esitiControlli.getTipoControllo();
				switch (tipoControllo) {
				case BRIDUSDC010_agricoltoreAttivo:
					datiDomandaStampa.setAgriAttivo(esitiControlli.recuperaValoreString());
					break;
				case BRIDUSDC012_superficieMinima:
					datiDomandaStampa.setSuperficieMinima(esitiControlli.recuperaValoreString());
					break;
				case BRIDUSDC011_impegnoTitoli:
					datiDomandaStampa.setImpegnoTitoli(esitiControlli.recuperaValoreString());
					break;
				default:
					break;
				}
			});
		}
	}

	private void popolaPassoLavorazioneStampa(
			DatiIstruttoriaDisaccoppiatoStampa datiDomandaStampa, List<PassoLavorazioneStampa> passiLavorazione,
			PassoTransizioneModel p) {
		PassoLavorazioneStampa passoLavStampa = new PassoLavorazioneStampa();
		DatiInput input = new DatiInput();
		DatiOutput output = new DatiOutput();
		DatiSintesi sintesi = new DatiSintesi();
		try {
			if (p.getDatiInput() != null) {
				input = mapper.readValue(p.getDatiInput(), DatiInput.class);
			}
			if (p.getDatiOutput() != null) {
				output = mapper.readValue(p.getDatiOutput(), DatiOutput.class);
			}
			popolaEsitiControlli(datiDomandaStampa, p, sintesi);
			List<VariabileStampa> variabiliInput = popolaVariabiliCalcoloInput(input,p.getCodicePasso());
			List<VariabileStampa> variabiliOutput = popolaVariabiliCalcoloOutput(datiDomandaStampa, output,p.getCodicePasso());
			passoLavStampa.setVariabiliOutput(variabiliOutput);
			passoLavStampa.setVariabiliInput(variabiliInput);
			passoLavStampa.setTipoPassoLavorazione(p.getCodicePasso().getDescrizione());
			if (TipologiaPassoTransizione.AMMISSIBILITA.equals(p.getCodicePasso())) {
				passoLavStampa.setOrdine(8);
			}
			if (TipologiaPassoTransizione.ANOMALIE_MANTENIMENTO.equals(p.getCodicePasso())) {
				passoLavStampa.setOrdine(7);
			}
			if (TipologiaPassoTransizione.RIDUZIONI_BPS.equals(p.getCodicePasso())) {
				passoLavStampa.setOrdine(6);
			}
			if (TipologiaPassoTransizione.SANZIONI_BPS.equals(p.getCodicePasso())) {
				passoLavStampa.setOrdine(5);
			}
			if (TipologiaPassoTransizione.GREENING.equals(p.getCodicePasso())) {
				passoLavStampa.setOrdine(4);
			}
			if (TipologiaPassoTransizione.GIOVANE_AGRICOLTORE.equals(p.getCodicePasso())) {
				passoLavStampa.setOrdine(3);
			}
			if (TipologiaPassoTransizione.RIEPILOGO_SANZIONI.equals(p.getCodicePasso())) {
				passoLavStampa.setOrdine(2);
			}
			if (TipologiaPassoTransizione.CONTROLLI_FINALI.equals(p.getCodicePasso())) {
				passoLavStampa.setOrdine(1);
			}
		} catch (IOException e) {
			logger.error("Stampa domande : errore non previsto", e);
		}
		passiLavorazione.add(passoLavStampa);
		passiLavorazione.sort(Comparator.comparing(PassoLavorazioneStampa::getOrdine).reversed());
	}

	private boolean isFormatoVariabileNumerico(VariabileCalcolo v) {
		FormatoVariabile formato = v.getTipoVariabile().getFormato();
		return formato.equals(FormatoVariabile.NUMERO2DECIMALI)
				|| formato.equals(FormatoVariabile.NUMERO4DECIMALI)
				|| formato.equals(FormatoVariabile.NUMEROINTERO)
				|| formato.equals(FormatoVariabile.PERCENTUALE)
				|| formato.equals(FormatoVariabile.PERCENTUALE6DECIMALI);
	}

	private void popolaDatiDomandaStampa(
			final DatiIstruttoriaDisaccoppiatoStampa datiDomandaStampa, final VariabileStampa vStampa) {
		if (vStampa.getValore() != null) {
			return;
		}
		String codiceVariabile = vStampa.getCodiceVariabile();
		if (codiceVariabile.equalsIgnoreCase(TipoVariabile.IMPCALCFIN.name())) {
			datiDomandaStampa.setImportoFinale(vStampa.getValore());
		} else if (codiceVariabile.equalsIgnoreCase(TipoVariabile.BPSIMPRIDRIT.name())
				|| codiceVariabile.equalsIgnoreCase(TipoVariabile.GREIMPRIDRIT.name())
				|| codiceVariabile.equalsIgnoreCase(TipoVariabile.GIOIMPRIDRIT.name())) {
			datiDomandaStampa.setImportoRiduzioneRitardo(vStampa.getValore());
		} else if (codiceVariabile.equalsIgnoreCase(TipoVariabile.BPSIMPRIDCAP50.name())
				|| codiceVariabile.equalsIgnoreCase(TipoVariabile.BPSIMPRIDCAP100.name())) {
			datiDomandaStampa.setImportoRiduzioneCapping(vStampa.getValore());
		} else if (codiceVariabile.equalsIgnoreCase(TipoVariabile.BPSIMPRID.name())) {
			datiDomandaStampa.setImportoRiduzioni(vStampa.getValore());
		} else if (codiceVariabile.equalsIgnoreCase(TipoVariabile.BPSIMPSANZ.name())
				|| codiceVariabile.equalsIgnoreCase(TipoVariabile.BPSIMPSANZREC.name())) {
			datiDomandaStampa.setImportoSanzioniPremioBase(vStampa.getValore());	
		} else if (codiceVariabile.equalsIgnoreCase(TipoVariabile.GREIMPRID.name())) {
			datiDomandaStampa.setImportoRiduzioneGreening(vStampa.getValore());
		} else if (codiceVariabile.equalsIgnoreCase(TipoVariabile.GIOIMPRID.name())) {
			datiDomandaStampa.setImportoRiduzioneGiovane(vStampa.getValore());
		} else if (codiceVariabile.equalsIgnoreCase(TipoVariabile.SANZTOT.name())) {
			datiDomandaStampa.setImportoSanzioni(vStampa.getValore());
		} else if (codiceVariabile.equalsIgnoreCase(TipoVariabile.GREIMPSANZ.name())) {
			datiDomandaStampa.setImportoSanzioniGreening(vStampa.getValore());
		} else if (codiceVariabile.equalsIgnoreCase(TipoVariabile.GIOIMPSANZ.name())
				|| codiceVariabile.equalsIgnoreCase(TipoVariabile.GIOIMPSANZREC.name())) {
			datiDomandaStampa.setImportoSanzioniGiovane(vStampa.getValore());
		}
	}

	private List<VariabileStampa> popolaVariabiliCalcoloOutput(DatiIstruttoriaDisaccoppiatoStampa datiDomandaStampa, DatiOutput output,TipologiaPassoTransizione tipologiaPasso) {
		// Rivedere il recupero e settaggio della vStampa per quanto riguarda i dati di Sintesi Calcolo
		List<VariabileStampa> variabiliOutput = new ArrayList<>();
		output.getVariabiliCalcoloDaStampare().forEach(v -> {
			VariabileStampa vStampa = new VariabileStampa();
			vStampa.setCodiceVariabile(v.getTipoVariabile().toString());
			vStampa.setDescrizioneVariabile(v.getTipoVariabile().getDescrizione());
			vStampa.setValore(v.recuperaValoreString());	
			setOrdineVariabileDaStampare(v,vStampa,tipologiaPasso);
			
			popolaDatiDomandaStampa(datiDomandaStampa, vStampa);
			variabiliOutput.add(vStampa);
		});
		variabiliOutput.sort(Comparator.comparingInt(VariabileStampa::getOrdine));
		return variabiliOutput;
	}

	private List<VariabileStampa> popolaVariabiliCalcoloInput(DatiInput input,TipologiaPassoTransizione tipologiaPasso) {
		// variabili calcolo input da stampare
		List<VariabileStampa> variabiliInput = new ArrayList<>();
		input.getVariabiliCalcoloDaStampare().forEach(v -> {
			VariabileStampa vStampa = new VariabileStampa();
			vStampa.setCodiceVariabile(v.getTipoVariabile().toString());
			vStampa.setDescrizioneVariabile(v.getTipoVariabile().getDescrizione());
			vStampa.setValore(v.recuperaValoreString());	
			setOrdineVariabileDaStampare(v,vStampa,tipologiaPasso);
			
			if (vStampa.getValore() != null) {
				variabiliInput.add(vStampa);
			}
		});
		
		variabiliInput.sort(Comparator.comparingInt(VariabileStampa::getOrdine));
		return variabiliInput;
	}

	private void popolaPassoLavorazioneIntersostegno(DatiIstruttoriaDisaccoppiatoStampa datiDomandaStampa, IstruttoriaModel istruttoria) {
		Optional<TransizioneIstruttoriaModel> transizioneLiquidazione = getUltimaTransizione(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		if (transizioneLiquidazione.isPresent()) {
			TransizioneIstruttoriaModel transizione = transizioneLiquidazione.get();
			List<PassoTransizioneModel> a4gtPassiLavSostegno = daoPassiLavSostegno.findByTransizioneIstruttoria(transizione);
			if (!a4gtPassiLavSostegno.isEmpty()) {
				List<PassoLavorazioneStampa> passiLavorazioneInterSostegno = new ArrayList<>();
				a4gtPassiLavSostegno.stream().filter(p -> !p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO)
						&& !p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO) 
						&& !p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA)).forEach(p -> {
					PassoLavorazioneStampa passoLavInterSostegno = new PassoLavorazioneStampa();
					DatiInput input = new DatiInput();
					DatiOutput output = new DatiOutput();
					List<VariabileStampa> variabiliOutput = new ArrayList<>();
					List<VariabileStampa> variabiliInput = new ArrayList<>();
					try {
						if (p.getDatiInput() != null) {
							input = mapper.readValue(p.getDatiInput(), DatiInput.class);
						}
						if (p.getDatiOutput() != null) {
							output = mapper.readValue(p.getDatiOutput(), DatiOutput.class);
						}
						popolaVariabiliCalcoloDaStampare(input, variabiliInput, p.getCodicePasso());
						popolaVariabiliCalcoloDaStampare(output, variabiliOutput, p.getCodicePasso());
						passoLavInterSostegno.setTipoPassoLavorazione(p.getCodicePasso().getDescrizione());
						passoLavInterSostegno.setVariabiliOutput(variabiliOutput);
						passoLavInterSostegno.setVariabiliInput(variabiliInput);
						//variabiliInput.forEach(i -> {
						//});
						passiLavorazioneInterSostegno.add(passoLavInterSostegno);
					} catch (IOException e) {
						logger.error("Stampa domande : errore non previsto", e);
					}
				});
				datiDomandaStampa.setPassoLavInterSostegno(passiLavorazioneInterSostegno);
			}
		}
	}

	private void popolaVariabiliCalcoloDaStampare(final DatiCalcoli datiCalcoli, final List<VariabileStampa> listVariabiliStampa,TipologiaPassoTransizione tipologiaPasso) {
		datiCalcoli.getVariabiliCalcoloDaStampare().forEach(v -> {
			VariabileStampa vStampa = new VariabileStampa();
			vStampa.setCodiceVariabile(v.getTipoVariabile().toString());
			vStampa.setDescrizioneVariabile(v.getTipoVariabile().getDescrizione());
			vStampa.setValore(v.recuperaValoreString());
			
			if (vStampa.getValore() != null) {
				listVariabiliStampa.add(vStampa);
			}
			setOrdineVariabileDaStampare(v,vStampa,tipologiaPasso);
		});
		listVariabiliStampa.sort(Comparator.comparingInt(VariabileStampa::getOrdine));
	}
	
}
