package it.tndigitale.a4g.fascicolo.antimafia.action;

import java.util.List;
import java.util.function.Function;

import it.tndigitale.a4g.fascicolo.antimafia.dto.Dichiarazione;

@FunctionalInterface
public interface FunctionDichiarazioneAntimafiaDecorator extends Function<List<Dichiarazione>, List<Dichiarazione>> {
}
