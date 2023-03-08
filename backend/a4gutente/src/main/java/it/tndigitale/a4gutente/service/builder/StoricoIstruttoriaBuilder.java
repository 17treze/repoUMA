package it.tndigitale.a4gutente.service.builder;

import it.tndigitale.a4gutente.dto.StoricoIstruttorie;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;

import java.util.List;

public class StoricoIstruttoriaBuilder {

    public static StoricoIstruttorie from(A4gtUtente utente,
                                          List<IstruttoriaEntita> istruttorie) {
        return new StoricoIstruttorie();
    }

}
