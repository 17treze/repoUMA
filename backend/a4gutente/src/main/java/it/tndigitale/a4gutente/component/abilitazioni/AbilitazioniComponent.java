package it.tndigitale.a4gutente.component.abilitazioni;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;

import static it.tndigitale.a4gutente.codici.Ruoli.*;

@Component("abilitazioni")
public class AbilitazioniComponent {

    private UtenteComponent utenteComponent;

    @Autowired
    public AbilitazioniComponent setUtenteComponent(UtenteComponent utenteComponent) {
        this.utenteComponent = utenteComponent;
        return this;
    }

    public boolean checkImportazioneMassivaUtenti() {
        return utenteComponent.haUnRuolo(IMPORTAZIONE_MASSIVA_UTENTI);
    }

    public boolean checkCreaUtente() {
        return utenteComponent.haUnRuolo(CREA_UTENTE);
    }

    public boolean checkVisualizzaDomande() {
        return utenteComponent.haUnRuolo(VISUALIZZA_DOMANDE);
    }

    public boolean checkEditaPropriDatiPersonali() {
        return utenteComponent.haUnRuolo(EDITA_PROPRI_DATI_PERSONALI);
    }

    public boolean checkEditaDatiTuttePersone() {
        return utenteComponent.haUnRuolo(EDITA_TUTTI_I_DATI_PERSONALI);
    }

    public boolean checkEditaDomande() {
        return utenteComponent.haUnRuolo(EDITA_DOMANDE);
    }

    public boolean checkVisualizzaIstruttoriaDomanda() {
        return utenteComponent.haUnRuolo(VISUALIZZA_ISTRUTTORIA_DOMANDA);
    }

    public boolean checkEditaIstruttoriaDomanda() {
        return utenteComponent.haUnRuolo(EDITA_ISTRUTTORIA_DOMANDA);
    }

    public boolean checkVisualizzaProfiliUtente() {
        return utenteComponent.haUnRuolo(VISUALIZZA_PROFILI_UTENTE);
    }

}
