package it.tndigitale.a4gistruttoria.dto.domandaunica;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.tndigitale.a4g.soc.client.model.ImportoLiquidato;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

public class ImportiIstruttoriaBuilder {

	private ImportiIstruttoriaBuilder() {}

	private static final Logger logger = LoggerFactory.getLogger(ImportiIstruttoriaBuilder.class);

	// CI-02-01-02-03-01
	public static ImportiIstruttoria from(List<TransizioneIstruttoriaModel> transizioniSostegno, IstruttoriaModel lavorazioneSostegno) {
		ImportiIstruttoria importiIstruttoria = new ImportiIstruttoria();
		// recupera importi calcolati
		if (transizioniSostegno.isEmpty()) {
			return importiIstruttoria;
		}
		// recupera ImportoCalcolato
		Optional<TransizioneIstruttoriaModel> transizioneControlloCalcoloOkOpt = TransizioniHelper.recuperaTransizioneSostegnoByStato(transizioniSostegno, StatoIstruttoria.CONTROLLI_CALCOLO_OK);
		// verifica validità calcolo
		if (transizioneControlloCalcoloOkOpt.isPresent() && StatoIstruttoria.isStatoCalcoloValido(StatoIstruttoria.valueOfByIdentificativo(lavorazioneSostegno.getA4gdStatoLavSostegno().getIdentificativo()))) {
			// variabile scritta solo nelle transizioni con esito finale CONTROLLI_CALCOLO_OK e con codicePasso CONTROLLI_FINALI
			importiIstruttoria.setImportoCalcolato(recuperaImportoCalcolatoBySostegno(transizioneControlloCalcoloOkOpt.get(),lavorazioneSostegno));
		}

		// recupera ImportoAutorizzato 
		Optional<TransizioneIstruttoriaModel> transizioneControlliIntersostegnoOkOpt = TransizioniHelper.recuperaTransizioneSostegnoByStato(transizioniSostegno, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);

		// verifica validità calcolo
		if (transizioneControlliIntersostegnoOkOpt.isPresent() && StatoIstruttoria.PAGAMENTO_AUTORIZZATO.equals(StatoIstruttoria.valueOfByIdentificativo(lavorazioneSostegno.getA4gdStatoLavSostegno().getIdentificativo()))) {
			// variabile scritta solo nelle transizioni con esito finale CONTROLLI_INTERSOSTEGNO_OK e con codicePasso DISCIPLINA_FINANZIARIA
			importiIstruttoria.setImportoAutorizzato(recuperaImportoAutorizzatoBySostegno(transizioneControlliIntersostegnoOkOpt.get(), lavorazioneSostegno));
		}
		return importiIstruttoria;
	}

	private static BigDecimal recuperaImportoCalcolatoBySostegno(TransizioneIstruttoriaModel transizione ,IstruttoriaModel istruttoria) {
		Sostegno stato = istruttoria.getSostegno();
		switch (stato) {
		case DISACCOPPIATO:
			return TransizioniHelper.recuperaImporto(TipoVariabile.IMPCALCFIN, TipologiaPassoTransizione.CONTROLLI_FINALI , transizione);
		case ZOOTECNIA:
			return TransizioniHelper.recuperaImporto(TipoVariabile.ACZIMPCALCTOT , TipologiaPassoTransizione.CALCOLO_ACZ , transizione);
		case SUPERFICIE:
			return TransizioniHelper.recuperaImporto(TipoVariabile.ACSIMPCALCTOT, TipologiaPassoTransizione.CALCOLO_ACS, transizione);
		default:
			logger.error("Nessun sostegno trovato");
			return null;
		}
	}
	private static BigDecimal recuperaImportoAutorizzatoBySostegno(TransizioneIstruttoriaModel transizione ,IstruttoriaModel istruttoria) {
		Sostegno stato = istruttoria.getSostegno();
		switch (stato) {
		case DISACCOPPIATO:
			return TransizioniHelper.recuperaImporto(TipoVariabile.DFIMPLIQDIS, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA , transizione);
		case ZOOTECNIA:
			return TransizioniHelper.recuperaImporto(TipoVariabile.DFIMPLIQACZ , TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA , transizione);
		case SUPERFICIE:
			return TransizioniHelper.recuperaImporto(TipoVariabile.DFIMPLIQACS , TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA , transizione);
		default:
			logger.error("Nessun sostegno trovato");
			return null;
		}
	}
	
	//IDU-EVO-28 Visualizzazione dati di Liquidazione
	public static void setDatiLiquidazione(ImportiIstruttoria importiIstruttoria, ImportoLiquidato imp) {
		importiIstruttoria.setAnnoEsercizio(imp.getAnno());
		importiIstruttoria.setProgressivoCredito(imp.getProgressivo());
		importiIstruttoria.setNumeroAutorizzazione(imp.getNumeroAutorizzazione());
		importiIstruttoria.setDataAutorizzazione(imp.getDataAutorizzazione());
		importiIstruttoria.setProgressivoPagamento(imp.getProgressivoPagamento());
		importiIstruttoria.setImportoLiquidato(imp.getIncassatoNetto());
	}

}
