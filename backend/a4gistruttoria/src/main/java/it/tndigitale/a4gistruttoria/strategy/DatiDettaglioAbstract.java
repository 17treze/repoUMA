package it.tndigitale.a4gistruttoria.strategy;

import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.TransizioneIstruttoriaService;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

public abstract class DatiDettaglioAbstract {
	
	private static final Logger logger = LoggerFactory.getLogger(DatiDettaglioAbstract.class);
	
	private static final String NO_CALCOLO_ACS = "NO_CALCOLO_ACS";
	private static final String NO_CALCOLO_ACZ = "NO_CALCOLO_ACZ";
	protected static final String INPUT_SUFFIX = "_INPUT";
	protected static final String OUTPUT_SUFFIX = "_OUTPUT";
	
	@Autowired
	private DomandaUnicaDao domandaUnicaDao;
	
	@Autowired
	private TransizioneIstruttoriaService transizioneAccoppiatoService;
	
	protected DomandaUnicaModel caricaDomanda(Long idDomanda) {
		// recupero la domanda o rilancio eccezione
		return domandaUnicaDao.findById(idDomanda)
				.orElseThrow(() -> new RuntimeException(String.format("Nessuna domanda trovata per id: %d", idDomanda)));
	}

	public Set<PassoTransizioneModel> recuperaPassiLavorazione(IstruttoriaModel istruttoria) {
		Sostegno sostegno = istruttoria.getSostegno();
		try {
			StatoIstruttoria stato = adjustStatoLavorazioneSostegno(StatoIstruttoria.valueOfByIdentificativo(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()));
			TransizioneIstruttoriaModel transizione = transizioneAccoppiatoService.caricaUltimaTransizione(istruttoria,stato);
			Set<PassoTransizioneModel> passi = transizione.getPassiTransizione();
			if (passi.isEmpty())
				throw new EntityNotFoundException(String.format("Non ci sono passi lavorazione validi per l'istruttoria id: %d", istruttoria.getId()));
			return passi;
		} catch (EntityNotFoundException e) {
			// Se non è stata recuperata una domanda, una transizione o un passo di lavorazione
			// consideriamo che non sia stato effettuato nessun calcolo della domanda per l'accoppiato superficie
			logger.warn("Errore caricando l'ultima transizione per la domanda {}: {}", istruttoria.getId(), e.getMessage());
			throw new RuntimeException(recuperaCodiceErrorePerSostegno(sostegno));
		} catch (Exception e) {
			logger.error("recuperaUltimaLavorazione: errore caricando le transizioni e i passi di lavorazione della domanda " + istruttoria.getId(), e);
			throw new RuntimeException(recuperaCodiceErrorePerSostegno(sostegno));
		}
	}
	
	protected String recuperaCodiceErrorePerSostegno(Sostegno sostegno) {
		switch (sostegno) {
		case ZOOTECNIA:
			return NO_CALCOLO_ACZ;
		case SUPERFICIE:
			return NO_CALCOLO_ACS;
		default:
			return "";
		}
	}
	
	protected abstract StatoIstruttoria adjustStatoLavorazioneSostegno(StatoIstruttoria stato);
}
