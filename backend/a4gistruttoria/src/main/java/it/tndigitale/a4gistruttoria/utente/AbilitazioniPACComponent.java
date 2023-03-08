package it.tndigitale.a4gistruttoria.utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4gistruttoria.Ruoli;

@Component("abilitazioniPACComponent")
public class AbilitazioniPACComponent {

	@Autowired
	private UtenteComponent utenteComponent;
	

	public boolean checkEditaRielaboraDati() {
		return utenteComponent.haUnRuolo(Ruoli.RIELABORA_DATI_ISTRUTTORIA);
	}

	public boolean checkEditaDU() {
		return utenteComponent.haUnRuolo(Ruoli.EDITA_PAC_DU);
	}

	public boolean checkEditaSTAT() {
		return utenteComponent.haUnRuolo(Ruoli.EDITA_PAC_STAT);
	}

	public boolean checkEditaIstruttoria() {
		return utenteComponent.haUnRuolo(Ruoli.EDITA_PAC_ISTRUTT);
	}

	public boolean checkEditaDomandaIntegrativa() {
		return utenteComponent.haUnRuolo(Ruoli.EDITA_PAC_DU_DI);
	}

	public boolean checkVisualizzaIstruttoria() {
		return utenteComponent.haUnRuolo(Ruoli.VISUALIZZA_ISTRUTTORIA_DU, Ruoli.VISUALIZZA_ISTRUTTORIA_DU_UTENTE);
	}
}
