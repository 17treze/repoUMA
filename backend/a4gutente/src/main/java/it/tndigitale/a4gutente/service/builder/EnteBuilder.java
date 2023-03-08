package it.tndigitale.a4gutente.service.builder;

import it.tndigitale.a4gutente.dto.EnteCAA;
import it.tndigitale.a4gutente.dto.EnteSede;
import it.tndigitale.a4gutente.repository.model.A4gtEnte;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

public class EnteBuilder {

    public static List<EnteSede> from(Set<A4gtEnte> a4gtEntes) {
        return Optional.ofNullable(a4gtEntes)
                       .orElse(new HashSet<>())
                       .stream()
                       .map(entita -> convert(entita))
                       .collect(Collectors.toList());
    }

    public static EnteSede convert(A4gtEnte entita) {
        return new EnteSede().setId(entita.getId())
                             .setCaa(entita.getCAA())
                             .setIdentificativo(entita.getIdentificativo())
                             .setDescrizione(entita.getDescrizione());
    }

    public static List<EnteCAA> from(List<A4gtEnte> a4gEnti) {
        List<EnteCAA> ufficiCaa = new ArrayList<>();
        EnteCAA ufficioCaa;
        for (A4gtEnte a4gEnte : a4gEnti) {
            EnteSede enteOut = new EnteSede();
            copyProperties(a4gEnte, enteOut);
            Optional<EnteCAA> ufficioCaaP = getUffico(ufficiCaa, a4gEnte);
            if (!ufficioCaaP.isPresent()) {
                ufficioCaa = from(a4gEnte);
                ufficiCaa.add(ufficioCaa);
            } else {
                ufficioCaa = ufficioCaaP.get();
            }
            ufficioCaa.getSedi().add(enteOut);
        }
        return ufficiCaa;
    }

    private static EnteCAA from(A4gtEnte a4gEnte) {
        EnteCAA ufficioCaa = new EnteCAA();
        ufficioCaa.setCodice(new Long(a4gEnte.getCAA().getCodice()));
        ufficioCaa.setDescrizione(a4gEnte.getCAA().getDescrizione());
        ufficioCaa.setSedi(new ArrayList<>());
        return ufficioCaa;
    }

    private static Optional<EnteCAA> getUffico(List<EnteCAA> ufficiCaa, A4gtEnte a4gEnte) {
        return ufficiCaa.stream()
                        .filter(x -> x.getCodice().equals(new Long(a4gEnte.getCAA().getCodice())))
                        .findAny();
    }
}
