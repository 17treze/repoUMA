package it.tndigitale.a4gutente.repository.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import it.tndigitale.a4gutente.codici.TipoInformativa;

@Entity
@DiscriminatorValue(TipoInformativa.Valori.GENERALE)
public class AccettazioneInformativaGeneraleEntita extends AccettazioneInformativaEntita {

}
