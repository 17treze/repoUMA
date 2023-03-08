package it.tndigitale.a4gistruttoria.service.businesslogic.verbale;

import it.tndigitale.a4gistruttoria.dto.DatiDomandaStampa;
import it.tndigitale.a4gistruttoria.dto.VariabileStampa;
import it.tndigitale.a4gistruttoria.dto.domandaunica.ErroreControlloRicevibilitaDomanda;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.ErroreRicevibilitaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.*;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

abstract class VerbaleIstruttoriaService {
	
	@Autowired
	protected IstruttoriaDao istruttoriaDao;
	
	@Autowired
	private ErroreRicevibilitaDao erroreRicevibilitaDao;
	
    public abstract byte[] stampa(Long idIstruttoria) throws Exception;
    public final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy");

    protected DomandaUnicaModel getDomandaUnica(Long idIstruttoria) {
    	Optional<IstruttoriaModel> istruttoriaOpt = istruttoriaDao.findById(idIstruttoria);
		if (istruttoriaOpt.isPresent()) {
			IstruttoriaModel istruttoriaModel = istruttoriaOpt.get();
			return istruttoriaModel.getDomandaUnicaModel();
		} else {
			return null;
		}
    }

	private void popolaErroriRicevibilita(DatiDomandaStampa datiDomandaStampa, List<ErroreRicevibilitaModel> erroreRicevibilita) {
		erroreRicevibilita.forEach(errore -> {
			ErroreControlloRicevibilitaDomanda tipologiaControllo = errore.getTipologiaControllo();
			
			if (tipologiaControllo.equals(ErroreControlloRicevibilitaDomanda.DOMANDA_CARTACEA_FIRMATA)) {
				datiDomandaStampa.setFirmaDomanda("NO");
			} else {
				datiDomandaStampa.setFirmaDomanda("SI");
			}
			if (tipologiaControllo.equals(ErroreControlloRicevibilitaDomanda.ARCHIVIAZIONE_DOCUMENTI_OK)) {
				datiDomandaStampa.setArchiviazioneDomanda("NO");
			} else {
				datiDomandaStampa.setArchiviazioneDomanda("SI");
			}
			if (tipologiaControllo.equals(ErroreControlloRicevibilitaDomanda.FASCICOLO_AGGIORNATO)) {
				datiDomandaStampa.setAggiornamentoFascicolo("NO");
			} else {
				datiDomandaStampa.setAggiornamentoFascicolo("SI");
			}
			if (tipologiaControllo.equals(ErroreControlloRicevibilitaDomanda.PRESENTAZIONE_NEI_TERMINI)) {
				datiDomandaStampa.setDataPresentazione("NO");
			} else {
				datiDomandaStampa.setDataPresentazione("SI");
			}
		});
	}

    protected void getEsitoRicevibilita(DatiDomandaStampa datiDomandaStampa, DomandaUnicaModel domandaUnica) {
		// Esito controlli ricevibilita'
		List<ErroreRicevibilitaModel> esitiRicevibilita = erroreRicevibilitaDao.findByDomandaUnicaModel(domandaUnica);
		if (esitiRicevibilita.isEmpty()) {
			datiDomandaStampa.setFirmaDomanda("SI");
			datiDomandaStampa.setArchiviazioneDomanda("SI");
			datiDomandaStampa.setAggiornamentoFascicolo("SI");
			datiDomandaStampa.setDataPresentazione("SI");
		} else {
			popolaErroriRicevibilita(datiDomandaStampa, esitiRicevibilita);
		}
    }

	protected DatiDomandaStampa getDatiDomandaStampa(IstruttoriaModel istruttoriaModel) {
		DomandaUnicaModel domandaUnicaModel = istruttoriaModel.getDomandaUnicaModel();
		DatiDomandaStampa datiDomandaStampa = new DatiDomandaStampa();
		// Dati di Domanda
		LocalDate localDate = LocalDate.now();
		datiDomandaStampa.setModulo(domandaUnicaModel.getDescModuloDomanda());
		datiDomandaStampa.setCuaa(domandaUnicaModel.getCuaaIntestatario());
		datiDomandaStampa.setNumeroDomanda(domandaUnicaModel.getNumeroDomanda().toString());
		datiDomandaStampa.setAnnoCampagna(domandaUnicaModel.getCampagna().toString());
		datiDomandaStampa.setTipoIstruttoria(istruttoriaModel.getTipologia().name());
		datiDomandaStampa.setEnteCompilatore(domandaUnicaModel.getDescEnteCompilatore());
		datiDomandaStampa.setDtPresentazione(
				domandaUnicaModel.getDtPresentazione().format(VerbaleIstruttoriaService.dateTimeFormatter));
		datiDomandaStampa.setDenominazione(domandaUnicaModel.getRagioneSociale());
		datiDomandaStampa.setData(localDate);
		return datiDomandaStampa;
	}

	public static Optional<TransizioneIstruttoriaModel> getUltimaTransizioneCalcolo(
    		final IstruttoriaModel istruttoria) {
    	Set<TransizioneIstruttoriaModel> transizioni = istruttoria.getTransizioni();
//    	2 'RICHIESTO', 'CONTROLLI_CALCOLO_KO', 'CONTROLLI_CALCOLO_OK','CONTROLLI_INTERSOSTEGNO_OK', 'INTEGRATO'
    	List<String> filteredStatesInitial = Arrays.asList(
    			StatoIstruttoria.RICHIESTO.getStatoIstruttoria(),
    			StatoIstruttoria.INTEGRATO.getStatoIstruttoria(),
    			StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria(),
				StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria());
    	
//    	1 'CONTROLLI_CALCOLO_KO', 'CONTROLLI_CALCOLO_OK'
    	List<String> filteredStatesFinal = Arrays.asList(
				StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria(),
				StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria());
    	return transizioni.stream()
    			.filter(t -> filteredStatesInitial.contains(t.getA4gdStatoLavSostegno2().getIdentificativo()))
    			.filter(t -> filteredStatesFinal.contains(t.getA4gdStatoLavSostegno1().getIdentificativo()))
    			.max((ta, tb) -> ta.getDataEsecuzione().compareTo(tb.getDataEsecuzione()));
    }

    public static Optional<TransizioneIstruttoriaModel> getUltimaTransizioneLiquidazione(
    		final IstruttoriaModel istruttoria) {
    	Set<TransizioneIstruttoriaModel> transizioni = istruttoria.getTransizioni();
    	// 2 'LIQUIDABILE', 'CONTROLLI_LIQUIDABILE_KO', 'CONTROLLI_CALCOLO_OK'
    	List<String> filteredStatesInitial = Arrays.asList(
    			StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria(),
    			StatoIstruttoria.CONTROLLI_LIQUIDABILE_KO.getStatoIstruttoria(),
    			StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria());
    	
    	// 1 'LIQUIDABILE', 'CONTROLLI_LIQUIDABILE_KO'
    	List<String> filteredStatesFinal = Arrays.asList(
				StatoIstruttoria.CONTROLLI_LIQUIDABILE_KO.getStatoIstruttoria(),
				StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
    	return transizioni.stream()
    			.filter(t -> filteredStatesInitial.contains(t.getA4gdStatoLavSostegno2().getIdentificativo()))
    			.filter(t -> filteredStatesFinal.contains(t.getA4gdStatoLavSostegno1().getIdentificativo()))
    			.max((ta, tb) -> ta.getDataEsecuzione().compareTo(tb.getDataEsecuzione()));
    }
    
    public Optional<TransizioneIstruttoriaModel> getUltimaTransizione(final IstruttoriaModel istruttoria, final StatoIstruttoria statoIstruttoria) {
        return istruttoria.getTransizioni().stream()
                .filter(transizione -> transizione.getA4gdStatoLavSostegno1().getIdentificativo().equals(statoIstruttoria.getStatoIstruttoria()))
                .max(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione));
    }
    
    
    protected final void setOrdineVariabileDaStampare(VariabileCalcolo v, VariabileStampa vStampa,TipologiaPassoTransizione tipologiaPasso) {
    	
    	//per ogni variabile di stampa riprendere l'ordine delle variabili valorizzato in creaDatiDisciplinaFianaziaria 
    	vStampa.setOrdine(v.getTipoVariabile().getOrdine());
		if (v.getTipoVariabile().getOrdinePasso() != null && v.getTipoVariabile().getOrdinePasso().get(tipologiaPasso) != null) {
		   vStampa.setOrdine(v.getTipoVariabile().getOrdinePasso().get(tipologiaPasso));
		}
    }
    
}