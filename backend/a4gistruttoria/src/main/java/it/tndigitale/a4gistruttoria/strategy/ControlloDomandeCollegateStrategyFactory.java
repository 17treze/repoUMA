package it.tndigitale.a4gistruttoria.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.util.TipoDomandaCollegata;

@Component
public class ControlloDomandeCollegateStrategyFactory {

	@Autowired
	private ControlloDatiDomandaUnicaStrategy controlloDatiDomandaUnicaStrategy;
	@Autowired
	private ControlloDatiStrutturaliStrategy controlloDatiStrutturaliStrategy;
	@Autowired
	private ControlloDatiSuperficieStrategy controlloDatiSuperficieStrategy;

	public ControlloDomandeCollegateStrategy getControlloDomandeCollegateStrategy(TipoDomandaCollegata type) {
		switch (type) {
		case PSR_STRUTTURALI_EU:
			return controlloDatiStrutturaliStrategy;
		case PSR_SUPERFICIE_EU:
			return controlloDatiSuperficieStrategy;
		case DOMANDA_UNICA:
			return controlloDatiDomandaUnicaStrategy;
		default:
			return null;
		}
	}
}
