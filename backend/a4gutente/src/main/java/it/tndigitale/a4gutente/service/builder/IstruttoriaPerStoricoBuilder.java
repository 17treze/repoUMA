package it.tndigitale.a4gutente.service.builder;

import it.tndigitale.a4gutente.dto.Distributore;
import it.tndigitale.a4gutente.dto.IstruttoriaPerStorico;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste;
import it.tndigitale.a4gutente.dto.Utente;
import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;
import it.tndigitale.a4gutente.service.support.IstruttoriaSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.tndigitale.a4gutente.dto.Responsabilita.TITOLARE_AZIENDA_AGRICOLA;
import static it.tndigitale.a4gutente.service.builder.ProfiloBuilder.isPresent;
import static it.tndigitale.a4gutente.service.support.IstruttoriaSupport.estraiAziendeDaDomandaIstruttoria;
import static it.tndigitale.a4gutente.utility.ListSupport.convertToSet;

public class IstruttoriaPerStoricoBuilder {

    public static IstruttoriaPerStorico from(IstruttoriaEntita istruttoria,
                                             Utente operatore,
                                             IstruttoriaSupport istruttoriaSupport) {

        IstruttoriaPerStorico istruttoriaPerStorico = new IstruttoriaPerStorico()
                .setDataTermineIstruttoria(istruttoria.getDataTermineIstruttoria())
                .setIdDomanda((istruttoria.getDomanda() != null) ? istruttoria.getDomanda().getId():null)
                .setId(istruttoria.getId())
                .setProfili(ProfiloBuilder.convert(istruttoria.getProfili()))
                .setIstruttore(operatore)
                .setMotivazioneDisattivazione(istruttoria.getMotivazioneDisattivazione())
                .setMotivazioneRifiuto(istruttoria.getMotivazioneRifiuto())
                .setVariazioneRichiesta(istruttoria.getVariazioneRichiesta())
                .setNote(istruttoria.getNote())
                .setTestoComunicazione(istruttoria.getTestoComunicazione())
                .setTestoMailInviata(istruttoria.getTestoMailInviata())
                .setSedi(EnteBuilder.from(convertToSet(istruttoria.getEnti())))
                .setDistributori(getDistributoriFromRuoliDistributore(istruttoriaSupport, istruttoria))
                .addProfiliDisattivi(istruttoria.getProfiliDisabilitati());

        if (isPresent(istruttoria.getProfili(), TITOLARE_AZIENDA_AGRICOLA)) {
            istruttoriaPerStorico.setAziende(estraiAziendeDaDomandaIstruttoria(istruttoria.getDomanda()));
        }

        return istruttoriaPerStorico;
    }

    private static List<Distributore> getDistributoriFromRuoliDistributore(IstruttoriaSupport istruttoriaSupport, IstruttoriaEntita istruttoria) {
        try {
            List<ResponsabilitaRichieste.RuoloDistributore> ruoli = istruttoriaSupport.estraiDistributoriDaDomandaIstruttoria(istruttoria.getDomanda().getId());
            return ruoli.stream().flatMap(r -> r.getDistributori().stream()).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
