package it.tndigitale.a4gutente.component.abilitazioni;

import static it.tndigitale.a4gutente.codici.Ruoli.CREA_UTENTE;
import static it.tndigitale.a4gutente.codici.Ruoli.EDITA_DOMANDE;
import static it.tndigitale.a4gutente.codici.Ruoli.EDITA_ISTRUTTORIA_DOMANDA;
import static it.tndigitale.a4gutente.codici.Ruoli.EDITA_PROPRI_DATI_PERSONALI;
import static it.tndigitale.a4gutente.codici.Ruoli.EDITA_TUTTI_I_DATI_PERSONALI;
import static it.tndigitale.a4gutente.codici.Ruoli.IMPORTAZIONE_MASSIVA_UTENTI;
import static it.tndigitale.a4gutente.codici.Ruoli.VISUALIZZA_DOMANDE;
import static it.tndigitale.a4gutente.codici.Ruoli.VISUALIZZA_ISTRUTTORIA_DOMANDA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;

public class AbilitazioniComponentTest {

    private AbilitazioniComponent abilitazioniComponent;
    private UtenteComponent utenteComponent;

    public AbilitazioniComponentTest() {
        utenteComponent = mock(UtenteComponent.class);

        abilitazioniComponent = new AbilitazioniComponent().setUtenteComponent(utenteComponent);
    }

    @Test
    public void checkImportazioneMassivaUtentiTRUE() {
        when(utenteComponent.haUnRuolo(IMPORTAZIONE_MASSIVA_UTENTI)).thenReturn(Boolean.TRUE);

        boolean check = abilitazioniComponent.checkImportazioneMassivaUtenti();

        assertThat(check).isTrue();
    }

    @Test
    public void checkImportazioneMassivaUtentiFALSE() {
        when(utenteComponent.haUnRuolo(IMPORTAZIONE_MASSIVA_UTENTI)).thenReturn(Boolean.FALSE);

        boolean check = abilitazioniComponent.checkImportazioneMassivaUtenti();

        assertThat(check).isFalse();
    }

    @Test
    public void checkCreaUtenteTRUE() {
        when(utenteComponent.haUnRuolo(CREA_UTENTE)).thenReturn(Boolean.TRUE);

        boolean check = abilitazioniComponent.checkCreaUtente();

        assertThat(check).isTrue();
    }

    @Test
    public void checkCreaUtenteFALSE() {
        when(utenteComponent.haUnRuolo(CREA_UTENTE)).thenReturn(Boolean.FALSE);

        boolean check = abilitazioniComponent.checkCreaUtente();

        assertThat(check).isFalse();
    }

    @Test
    public void checkVisualizzaDomandeTRUE() {
        when(utenteComponent.haUnRuolo(VISUALIZZA_DOMANDE)).thenReturn(Boolean.TRUE);

        boolean check = abilitazioniComponent.checkVisualizzaDomande();

        assertThat(check).isTrue();
    }

    @Test
    public void checkVisualizzaDomandeFALSE() {
        when(utenteComponent.haUnRuolo(VISUALIZZA_DOMANDE)).thenReturn(Boolean.FALSE);

        boolean check = abilitazioniComponent.checkVisualizzaDomande();

        assertThat(check).isFalse();
    }

    @Test
    public void checkEditaPropriDatiPersonaliTRUE() {
        when(utenteComponent.haUnRuolo(EDITA_PROPRI_DATI_PERSONALI)).thenReturn(Boolean.TRUE);

        boolean check = abilitazioniComponent.checkEditaPropriDatiPersonali();

        assertThat(check).isTrue();
    }

    @Test
    public void checkEditaPropriDatiPersonaliFALSE() {
        when(utenteComponent.haUnRuolo(EDITA_PROPRI_DATI_PERSONALI)).thenReturn(Boolean.FALSE);

        boolean check = abilitazioniComponent.checkEditaPropriDatiPersonali();

        assertThat(check).isFalse();
    }

    @Test
    public void checkEditaDatiTuttePersoneTRUE() {
        when(utenteComponent.haUnRuolo(EDITA_TUTTI_I_DATI_PERSONALI)).thenReturn(Boolean.TRUE);

        boolean check = abilitazioniComponent.checkEditaDatiTuttePersone();

        assertThat(check).isTrue();
    }

    @Test
    public void checkEditaDatiTuttePersoneFALSE() {
        when(utenteComponent.haUnRuolo(EDITA_TUTTI_I_DATI_PERSONALI)).thenReturn(Boolean.FALSE);

        boolean check = abilitazioniComponent.checkEditaDatiTuttePersone();

        assertThat(check).isFalse();
    }

    @Test
    public void checkEditaDomandeTRUE() {
        when(utenteComponent.haUnRuolo(EDITA_DOMANDE)).thenReturn(Boolean.TRUE);

        boolean check = abilitazioniComponent.checkEditaDomande();

        assertThat(check).isTrue();
    }

    @Test
    public void checkEditaDomandeFALSE() {
        when(utenteComponent.haUnRuolo(EDITA_DOMANDE)).thenReturn(Boolean.FALSE);

        boolean check = abilitazioniComponent.checkEditaDomande();

        assertThat(check).isFalse();
    }

    @Test
    public void checkVisualizzaIstruttoriaDomandaTRUE() {
        when(utenteComponent.haUnRuolo(VISUALIZZA_ISTRUTTORIA_DOMANDA)).thenReturn(Boolean.TRUE);

        boolean check = abilitazioniComponent.checkVisualizzaIstruttoriaDomanda();

        assertThat(check).isTrue();
    }

    @Test
    public void checkVisualizzaIstruttoriaDomandaFALSE() {
        when(utenteComponent.haUnRuolo(VISUALIZZA_ISTRUTTORIA_DOMANDA)).thenReturn(Boolean.FALSE);

        boolean check = abilitazioniComponent.checkVisualizzaIstruttoriaDomanda();

        assertThat(check).isFalse();
    }

    @Test
    public void checkEditaIstruttoriaDomandaTRUE() {
        when(utenteComponent.haUnRuolo(EDITA_ISTRUTTORIA_DOMANDA)).thenReturn(Boolean.TRUE);

        boolean check = abilitazioniComponent.checkEditaIstruttoriaDomanda();

        assertThat(check).isTrue();
    }

    @Test
    public void checkEditaIstruttoriaDomandaFALSE() {
        when(utenteComponent.haUnRuolo(EDITA_ISTRUTTORIA_DOMANDA)).thenReturn(Boolean.FALSE);

        boolean check = abilitazioniComponent.checkEditaIstruttoriaDomanda();

        assertThat(check).isFalse();
    }

}
