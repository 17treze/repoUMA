package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import static it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.helper.IstruttoriaDisaccoppiatoHelper.STATI_FINALI;
import static it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.helper.IstruttoriaDisaccoppiatoHelper.STATI_FINALI_INTER;
import static it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.helper.IstruttoriaDisaccoppiatoHelper.STATI_FINALI_LIQ;
import static it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.helper.IstruttoriaDisaccoppiatoHelper.STATI_INIZALI;
import static it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.helper.IstruttoriaDisaccoppiatoHelper.STATI_INIZALI_INTER;
import static it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.helper.IstruttoriaDisaccoppiatoHelper.STATI_INIZALI_LIQ;
import static it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.helper.IstruttoriaDisaccoppiatoHelper.addVariabiliCalcolo;
import static it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.helper.IstruttoriaDisaccoppiatoHelper.getControlliSostegnoFromPassi;
import static it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.helper.IstruttoriaDisaccoppiatoHelper.getDatiDomandaFromPassi;
import static it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.helper.IstruttoriaDisaccoppiatoHelper.predicatoFiltroPerStati;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.dto.PremioPagamentiStatoIstruttoriaNettoLordoDto;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ControlloFrontend;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.repository.dao.DatiIstruttoreDisDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@Service
public class IstruttoriaDisaccoppiatoService {
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	
	@Autowired
	private DatiIstruttoreDisDao datiIstruttoreDisDao;

	/**
	 * 
	 * metodo richiamato dal dettaglio CONTROLLI DI SOSTEGNO del Disaccoppiato
	 *  
	 * @param idIstruttoria
	 * @return lista di controlli da visualizzare
	 * @throws Exception
	 * 
	 */
	public List<ControlloFrontend>  getControlliSostegno(final Long idIstruttoria) throws Exception {
		Optional<IstruttoriaModel> istruttoriaOpt = istruttoriaDao.findById(idIstruttoria);
		List<ControlloFrontend> listControlliSostegno = new ArrayList<>();
		if (istruttoriaOpt.isPresent()) {
			IstruttoriaModel istruttoria=istruttoriaOpt.get();
			Set<TransizioneIstruttoriaModel> transizioni=istruttoria.getTransizioni();
			// Gestione Controlli e passi lavorazione Calcolo
			Optional<TransizioneIstruttoriaModel> transizioneIstruttoria = transizioni.stream().filter(predicatoFiltroPerStati(STATI_INIZALI,STATI_FINALI)).findAny();
			if (transizioneIstruttoria.isPresent()) {
				Set<PassoTransizioneModel> infoCalcoli = transizioneIstruttoria.get().getPassiTransizione();
				if (infoCalcoli != null && !infoCalcoli.isEmpty()) {
					listControlliSostegno.addAll(getControlliSostegnoFromPassi(new ArrayList<>(infoCalcoli)));
				}
			}
			// Gestione Controlli liquidabilita
			Optional<TransizioneIstruttoriaModel> a4gtTransizioneLiquidabilita = transizioni.stream().filter(predicatoFiltroPerStati(STATI_INIZALI_LIQ,STATI_FINALI_LIQ)).findAny();
			if (a4gtTransizioneLiquidabilita.isPresent()) {
				Set<PassoTransizioneModel> infoLiquidabilita = a4gtTransizioneLiquidabilita.get().getPassiTransizione();
				if (infoLiquidabilita != null && !infoLiquidabilita.isEmpty()) {
					listControlliSostegno.addAll(getControlliSostegnoFromPassi(new ArrayList<>(infoLiquidabilita)));
				}
			}		
			// Gestione Controlli intersostegno e passo lavorazione Disciplina Finanziaria
			Optional<TransizioneIstruttoriaModel> a4gtTransizioneIntersostegno = transizioni.stream().filter(predicatoFiltroPerStati(STATI_INIZALI_INTER,STATI_FINALI_INTER)).findAny();
			if (a4gtTransizioneIntersostegno.isPresent()) {
				Set<PassoTransizioneModel> infoInterSostegno = a4gtTransizioneIntersostegno.get().getPassiTransizione();
				if (infoInterSostegno != null && !infoInterSostegno.isEmpty()) {
					listControlliSostegno.addAll(getControlliSostegnoFromPassi(new ArrayList<>(infoInterSostegno)));
				}
			}		
		}
		return listControlliSostegno;
	}
	
	/**
	 * 
	 * metodo richiamato dal dettaglio DATI DI DOMANDA del Disaccoppiato
	 * 
	 * @param idIstruttoria
	 * @return lista di controlli da visualizzare
	 * @throws Exception
	 * 
	 */
	public List<ControlloFrontend>  getDatiDomanda(Long idIstruttoria) throws Exception {
		Optional<IstruttoriaModel> istruttoriaOpt = istruttoriaDao.findById(idIstruttoria);
		if (!istruttoriaOpt.isPresent()) return Collections.emptyList();
		List<ControlloFrontend> listDatiDomanda = new ArrayList<>();
		IstruttoriaModel istruttoria=istruttoriaOpt.get();
		Set<TransizioneIstruttoriaModel> transizioni=istruttoria.getTransizioni();
		// Gestione Controlli e passi lavorazione Calcolo
		Optional<TransizioneIstruttoriaModel> transizioneIstruttoria = transizioni.stream().filter(predicatoFiltroPerStati(STATI_INIZALI,STATI_FINALI)).findAny();
		if (transizioneIstruttoria.isPresent()) {
			Set<PassoTransizioneModel> infoCalcoli = transizioneIstruttoria.get().getPassiTransizione();
			if (infoCalcoli != null && !infoCalcoli.isEmpty()) {
				listDatiDomanda.addAll(getDatiDomandaFromPassi(new ArrayList<>(infoCalcoli)));
			}
		}
		
		// Gestione Controlli intersostegno e passo lavorazione Disciplina Finanziaria
		if (StatoIstruttoria.PAGAMENTO_AUTORIZZATO.equals(istruttoria.getStato())) {
			Optional<TransizioneIstruttoriaModel> a4gtTransizioneIntersostegno = transizioni.stream().filter(predicatoFiltroPerStati(STATI_INIZALI_INTER,STATI_FINALI_INTER)).findAny();
			if (a4gtTransizioneIntersostegno.isPresent()) {
				Optional<PassoTransizioneModel> infoDisciplinaFinanziaria = a4gtTransizioneIntersostegno.get().getPassiTransizione().stream()
						.filter((p -> p.getCodicePasso().equals(TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA))).findAny();
				if (infoDisciplinaFinanziaria.isPresent()) {
					PassoTransizioneModel passoIntersostegno = infoDisciplinaFinanziaria.get();
					List<ControlloFrontend> controlli = new ArrayList<>();
					TipologiaPassoTransizione passo = passoIntersostegno.getCodicePasso();
					addVariabiliCalcolo(passoIntersostegno.getDatiInput(), DatiInput.class, controlli, passo, "Input", "DATI IN INGRESSO", 1);
					addVariabiliCalcolo(passoIntersostegno.getDatiOutput(), DatiOutput.class, controlli, passo, "Output", "DATI IN USCITA", 2);
					listDatiDomanda.addAll(controlli);
				}
			}			
		}

		return listDatiDomanda;
	}

	public List<PremioPagamentiStatoIstruttoriaNettoLordoDto> getValoriPremioLordoNettoPerStato(
			final Integer annoCampagna, final Sostegno tipoSostegno, final TipoIstruttoria tipoIstruttoria) {
		List<Object[]> results = null;
		switch (tipoSostegno) {
		case DISACCOPPIATO:
			results = istruttoriaDao.getValoriPremioLordoNettoPerStatoDisaccoppiato(annoCampagna, tipoIstruttoria.toString());			
			break;
		case SUPERFICIE:
			results = istruttoriaDao.getValoriPremioLordoNettoPerStatoSuperfici(annoCampagna, tipoIstruttoria.toString());
			break;
		case ZOOTECNIA:
			results = istruttoriaDao.getValoriPremioLordoNettoPerStatoAccoppiatoZootecnia(annoCampagna, tipoIstruttoria.toString());
			break;
		}
		List<PremioPagamentiStatoIstruttoriaNettoLordoDto> collect = PremioPagamentiStatoIstruttoriaNettoLordoDto.build(tipoSostegno, results);
		return collect;
	}	
}
