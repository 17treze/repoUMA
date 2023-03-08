package it.tndigitale.a4gistruttoria.strategy;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.antimafia.DichiarazioneAntimafiaConEsiti;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDomandeCollegate;
import it.tndigitale.a4gistruttoria.util.StatoDichiarazioneEnum;
import it.tndigitale.a4gistruttoria.util.TipoDomandaCollegata;

@Component
public class DomandeCollegateFiltrateStrategyImpl implements DomandeCollegateFiltrateStrategy {

	@Override
	public DichiarazioneAntimafiaConEsiti getDomanda(List<A4gtDomandeCollegate> a4gtDomandeCollegate, StatoDichiarazioneEnum stato) {
		DichiarazioneAntimafiaConEsiti dac = new DichiarazioneAntimafiaConEsiti();

		try {
			List<Long> idsDomandaUnica = a4gtDomandeCollegate.stream()
					.filter(a -> TipoDomandaCollegata.DOMANDA_UNICA.equals(a.getTipoDomanda()))
					.map(A4gtDomandeCollegate::getId)
					.collect(Collectors.toList());
			List<Long> idsDomandaStrutturale = a4gtDomandeCollegate.stream()
					.filter(a -> TipoDomandaCollegata.PSR_STRUTTURALI_EU.equals(a.getTipoDomanda()))
					.map(A4gtDomandeCollegate::getId)
					.collect(Collectors.toList());
			List<Long> idsDomandaSuperficie = a4gtDomandeCollegate.stream()
					.filter(a -> TipoDomandaCollegata.PSR_SUPERFICIE_EU.equals(a.getTipoDomanda()))
					.map(A4gtDomandeCollegate::getId)
					.collect(Collectors.toList());
	
			dac.setIdsDomandaDu(idsDomandaUnica);
			dac.setIdsDomandaStrutturale(idsDomandaStrutturale);
			dac.setIdsDomandaSuperficie(idsDomandaSuperficie);

			if (StatoDichiarazioneEnum.POSITIVO.equals(stato)) {
				// esiste almeno una domanda collegata alla dichiarazione
				Optional<A4gtDomandeCollegate> domanda = 
						a4gtDomandeCollegate.stream()
						.filter(dom -> TipoDomandaCollegata.DOMANDA_UNICA.equals(dom.getTipoDomanda())
						|| TipoDomandaCollegata.PSR_STRUTTURALI_EU.equals(dom.getTipoDomanda()) 
						|| TipoDomandaCollegata.PSR_SUPERFICIE_EU.equals(dom.getTipoDomanda()))
						.findFirst();
				if (domanda.isPresent()) {
					A4gtDomandeCollegate domandaCollegata = domanda.get();
					dac.setDtBdna(domandaCollegata.getDtBdna());
					dac.setProtocolloBdna(domandaCollegata.getProtocollo());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return dac;
	}

}
