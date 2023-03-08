package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import it.tndigitale.a4gistruttoria.component.CaricaPremioSostegno;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

public abstract class ControlloImporto implements Function<DatiLavorazioneControlloIntersostegno, CodiceEsito> {

	protected final Predicate<Double> sogliaMinima = importo -> {
		Double soglia = getSoglia();
		return Objects.nonNull(importo) && Objects.nonNull(soglia) ? importo.compareTo(soglia) >= 0 : Boolean.FALSE;
	};
	
	protected final Predicate<Double> isNotZero = importo -> Objects.nonNull(importo) ? importo.compareTo(BigDecimal.ZERO.doubleValue()) > 0 : Boolean.FALSE;

	protected final BiFunction<Double, Double, Optional<Double>> sum = (a, b) -> Optional.ofNullable(a).filter(a1 -> Objects.nonNull(b)).map(a1 -> Double.sum(a1, b));

	protected Predicate<DatiLavorazioneControlloIntersostegno> notHasSostegniAggiuntivi = importoMinimoHandler -> !importoMinimoHandler.getHasSostegno2() && !importoMinimoHandler.getHasSostegno3();

	protected BiPredicate<Boolean, StatoIstruttoria> bridu_044 = (has, stato) -> !Boolean.TRUE.equals(has) || !CaricaPremioSostegno.isStatoInAttesa(stato);

	protected BiPredicate<Boolean, StatoIstruttoria> bridu_045 = (has, stato) -> Boolean.TRUE.equals(has) && !CaricaPremioSostegno.isStatoInAttesa(stato);
	
	protected final Function<DatiLavorazioneControlloIntersostegno, Double> importoSostegno2 = (importoMinimoHandler) -> (!Boolean.TRUE.equals(importoMinimoHandler.getHasSostegno2()) || CaricaPremioSostegno.isStatoAnnullaImporto(importoMinimoHandler.getStatoLavorazioneSostegno2()))
			? (Double)BigDecimal.ZERO.doubleValue() : importoMinimoHandler.getImportoSostegno2();

	protected final Function<DatiLavorazioneControlloIntersostegno, Double> importoSostegno3 = (datiIntersostegno) -> (!Boolean.TRUE.equals(datiIntersostegno.getHasSostegno3()) || CaricaPremioSostegno.isStatoAnnullaImporto(datiIntersostegno.getStatoLavorazioneSostegno3()))
			? (Double)BigDecimal.ZERO.doubleValue() : datiIntersostegno.getImportoSostegno3();

	public ControlloImporto() {
		super();
	}
	
	protected abstract Double getSoglia();

}