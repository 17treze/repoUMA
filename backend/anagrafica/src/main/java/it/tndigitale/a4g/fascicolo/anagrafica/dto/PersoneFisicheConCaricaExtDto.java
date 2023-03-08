package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersoneFisicheConCaricaExtDto {

    private Long idFascicolo;
    private Set<Long> idPersonaFisicaConCaricaSet;
}