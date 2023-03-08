package it.tndigitale.a4gistruttoria.strategy;

import java.util.Optional;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.DomandaCollegata;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDomandeCollegate;
import it.tndigitale.a4gistruttoria.util.StatoDomandaCollegata;
import it.tndigitale.a4gistruttoria.util.TipoDomandaCollegata;

@Component
public class ControlloDatiStrutturaliStrategy implements ControlloDomandeCollegateStrategy {

	@Override
	public A4gtDomandeCollegate controlloDomandeCollegate(Optional<A4gtDomandeCollegate> domanda, DomandaCollegata domandaCollegataInput) {
		A4gtDomandeCollegate domandaCollegataOutput;
		if (!domanda.isPresent()) {
			domandaCollegataOutput = new A4gtDomandeCollegate();
			domandaCollegataOutput.setTipoDomanda(TipoDomandaCollegata.PSR_STRUTTURALI_EU);
			domandaCollegataOutput.setCuaa(domandaCollegataInput.getCuaa());
			domandaCollegataOutput.setIdDomanda(domandaCollegataInput.getIdDomanda());
			domandaCollegataOutput.setDtDomanda(domandaCollegataInput.getDtDomanda());
			domandaCollegataOutput.setImportoRichiesto(domandaCollegataInput.getImportoRichiesto());
			domandaCollegataOutput.setStatoBdna(StatoDomandaCollegata.NON_CARICATO);
			domandaCollegataOutput.setMisurePsr(domandaCollegataInput.getMisurePsr());
		} else {
			// BRIAMCNT009
			domandaCollegataOutput = domanda.get();
			if (domandaCollegataOutput.getImportoRichiesto().compareTo(domandaCollegataInput.getImportoRichiesto()) != 0) {
				domandaCollegataOutput.setImportoRichiesto(domandaCollegataInput.getImportoRichiesto());

				domandaCollegataOutput.setDtDomanda(domandaCollegataInput.getDtDomanda());

				domandaCollegataOutput.setStatoBdna(StatoDomandaCollegata.NON_CARICATO);
				domandaCollegataOutput.setDtBdnaOp(null);
				domandaCollegataOutput.setDtBdna(null);
				domandaCollegataOutput.setDtFineEsitoNegativo(null);
				domandaCollegataOutput.setDtInizioEsitoNegativo(null);
				domandaCollegataOutput.setDtFineSilenzioAssenso(null);
				domandaCollegataOutput.setDtInizioSilenzioAssenso(null);
			} else {
				return null;
			}
		}
		return domandaCollegataOutput;
	}

}
