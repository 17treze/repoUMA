package it.tndigitale.a4gutente.service.builder;

import it.tndigitale.a4gutente.dto.Profilo;
import it.tndigitale.a4gutente.dto.Responsabilita;
import it.tndigitale.a4gutente.repository.model.A4gtProfilo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.tndigitale.a4gutente.utility.ListSupport.emptyIfNull;

public class ProfiloBuilder {

    public static Profilo convert(A4gtProfilo entita) {
        return new Profilo().setId(entita.getId())
                            .setDescrizione(entita.getDescrizione())
                            .setIdentificativo(entita.getIdentificativo())
                            .setResponsabilita(entita.getResponsabilita())
                            .setHaRuoli(hasRoles(entita));
    }

    public static List<Profilo> convert(List<A4gtProfilo> a4gtProfilos) {
        return Optional.ofNullable(a4gtProfilos)
                       .orElse(Collections.emptyList())
                       .stream()
                       .map(item -> convert(item))
                       .collect(Collectors.toList());
    }

    public static Boolean isPresent(List<A4gtProfilo> profili, Responsabilita responsabilita) {
        if (profili==null) {
            return Boolean.FALSE;
        }
        return profili.stream()
                      .filter(profilo -> responsabilita.equals(profilo.getResponsabilita()))
                      .findFirst()
                      .isPresent();

    }

    public static Profilo creaProfiloDisattivo(A4gtProfilo entita) {
        return convert(entita).setDisabled(Boolean.TRUE);
    }

    public static List<Profilo> creaProfiliDisattivi(List<A4gtProfilo> profiliDisattivati) {
        return emptyIfNull(profiliDisattivati).stream()
                .map(profilo -> creaProfiloDisattivo(profilo))
                .collect(Collectors.toList());
    }

    private static Boolean hasRoles(A4gtProfilo entita) {
        List<?> roles = entita.getA4grProfiloRuolos();
        return roles!=null && !roles.isEmpty();
    }

}
