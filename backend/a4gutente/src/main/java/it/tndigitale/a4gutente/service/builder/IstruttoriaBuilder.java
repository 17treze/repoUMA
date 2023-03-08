package it.tndigitale.a4gutente.service.builder;

import it.tndigitale.a4gutente.dto.Istruttoria;
import it.tndigitale.a4gutente.repository.model.A4gtProfilo;
import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IstruttoriaBuilder {

    public static List<A4gtProfilo> getDisabledProfilesFrom(IstruttoriaEntita istruttoriaEntita) {
        return (istruttoriaEntita!=null)? istruttoriaEntita.getProfiliDisabilitati():null;
    }

    public static Istruttoria from(IstruttoriaEntita entita) {
        return new Istruttoria().setId(entita.getId())
                                .setMotivazioneRifiuto(entita.getMotivazioneRifiuto())
                                .setTestoComunicazione(entita.getTestoComunicazione())
                                .setVariazioneRichiesta(entita.getVariazioneRichiesta())
                                .setIdDomanda(((entita.getDomanda() != null)? entita.getDomanda().getId():null))
                                .setProfili(profiliFrom(entita))
                                .setSedi(entiFrom(entita))
                                .setVersion(entita.getVersion())
                                .setTestoMailInviata(entita.getTestoMailInviata())
                                .setNote(entita.getNote());
    }

    public static void convert(Istruttoria dto, IstruttoriaEntita entita) {
        entita.setMotivazioneRifiuto(dto.getMotivazioneRifiuto());
        entita.setTestoComunicazione(dto.getTestoComunicazione());
        entita.setVariazioneRichiesta(dto.getVariazioneRichiesta());
    }

    private static List<Long> profiliFrom(IstruttoriaEntita entita) {
        return Optional.ofNullable(entita.getProfili())
                       .orElse(Collections.emptyList())
                       .stream()
                       .map(item -> item.getId())
                       .collect(Collectors.toList());
    }

    private static List<Long> entiFrom(IstruttoriaEntita entita) {
        return Optional.ofNullable(entita.getEnti())
                       .orElse(Collections.emptyList())
                       .stream()
                       .map(item -> item.getId())
                       .collect(Collectors.toList());
    }
}
