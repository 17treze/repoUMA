package it.tndigitale.a4g.richiestamodificasuolo.business.service.evento;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.RichiestaModificaSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;

@Component
public class LavorazioneSuoloEventoHandler {

	private static final Logger log = LoggerFactory.getLogger(LavorazioneSuoloEventoHandler.class);

	@EventListener
	public void handle(AvvioLavorazioneEvento lavorazioneAvviata) {
		LavorazioneSuoloModel lavorazione = lavorazioneAvviata.getLavorazione();
		List<SuoloDichiaratoModel> dichiarati = lavorazione.getSuoloDichiaratoModel();
		if (dichiarati != null) {
			Set<RichiestaModificaSuoloModel> listaRichiesteDaValutare = dichiarati.stream().map(dich -> dich.getRichiestaModificaSuolo()).collect(Collectors.toSet());
			for (RichiestaModificaSuoloModel richiesta : listaRichiesteDaValutare) {
				passaStatoInLavorazioneRichiesta(richiesta);
			}
		}
		// da qua mi aggancio a recuperare le richieste da notificare
	}

	@EventListener
	public void handle(CancellazioneLavorazioneEvento lavorazioneEliminata) {
		LavorazioneSuoloModel lavorazione = lavorazioneEliminata.getLavorazione();
		log.debug("Arrivata notifica di cancellazione lavorazione {}", lavorazione.getId());
		List<SuoloDichiaratoModel> dichiarati = lavorazione.getSuoloDichiaratoModel();
		if (dichiarati != null) {
			log.debug("Trovati {} dichiarati per lavorazione cancellata {}", dichiarati.size(), lavorazione.getId());
			Set<RichiestaModificaSuoloModel> listaRichiesteDaValutare = dichiarati.stream().map(dich -> dich.getRichiestaModificaSuolo()).collect(Collectors.toSet());
			for (RichiestaModificaSuoloModel richiesta : listaRichiesteDaValutare) {
				passaStatoLavorazioneRichiesta(richiesta);
			}
		}
	}

	protected void passaStatoInLavorazioneRichiesta(RichiestaModificaSuoloModel richiesta) {
		if (StatoRichiestaModificaSuolo.LAVORABILE.equals(richiesta.getStato())) {
			richiesta.setStato(StatoRichiestaModificaSuolo.IN_LAVORAZIONE);
		}
	}

	protected void passaStatoLavorazioneRichiesta(RichiestaModificaSuoloModel richiesta) {
		log.debug("Controllo la richiesta {}", richiesta.getId());
		boolean rollbackStato = richiesta.getSuoloDichiaratoModel().stream().allMatch(dich -> dich.getLavorazioneSuolo() == null);
		log.debug("La richiesta {} risulta da passare a lavorabile? {} ", richiesta.getId(), rollbackStato);
		if (rollbackStato) {
			richiesta.setStato(StatoRichiestaModificaSuolo.LAVORABILE);
		}
	}
}
