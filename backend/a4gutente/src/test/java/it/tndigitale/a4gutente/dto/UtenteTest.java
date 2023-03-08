package it.tndigitale.a4gutente.dto;

import it.tndigitale.a4gutente.repository.model.A4gtProfilo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class UtenteTest {

    @Test
    public void forFlagDisabledIfProfileDisabledNullThenUserHasAllProfileEnabled() {
        Utente utente = new Utente().setProfili(moreProfileEnabled());

        utente.disableProfiles(null);

        assertThat(utente.getProfili()).extracting("disabled").contains(Boolean.FALSE, Boolean.FALSE);
    }

    @Test
    public void forFlagDisabledIfProfileDisabledEmptyThenUserHasAllProfileEnabled() {
        Utente utente = new Utente().setProfili(moreProfileEnabled());

        utente.disableProfiles(Collections.emptyList());

        assertThat(utente.getProfili()).extracting("disabled").contains(Boolean.FALSE, Boolean.FALSE);
    }

    @Test
    public void forFlagDisabledIfProfileDisabledNotEmptyAndOneMatchThenUserHasOneProfileDisabled() {
        Utente utente = new Utente().setProfili(moreProfileEnabled());

        utente.disableProfiles(Arrays.asList(profiloDisabled()));

        assertThat(utente.getProfili()).extracting("disabled").contains(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
    }



    private List<Profilo> moreProfileEnabled() {
        List<Profilo> profili = new ArrayList<>();
        profili.add(new Profilo().setId(1L));
        profili.add(new Profilo().setId(2L));
        return profili;
    }

    private A4gtProfilo profiloDisabled() {
        A4gtProfilo profilo = new A4gtProfilo();
        profilo.setId(2L);
        return profilo;
    }

}
