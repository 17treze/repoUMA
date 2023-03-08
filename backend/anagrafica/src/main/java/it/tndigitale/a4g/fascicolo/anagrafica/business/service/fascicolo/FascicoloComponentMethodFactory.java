package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaModel;
import it.tndigitale.a4g.framework.support.PersonaSelector;

@Component
public class FascicoloComponentMethodFactory implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	public FascicoloAbstractComponent from(final String codiceFiscale) {
		FascicoloAbstractComponent<? extends PersonaModel> service = null;
		if (PersonaSelector.isPersonaFisica(codiceFiscale)) {
			service = getFascicoloPersonaFisicaComponent();
		} else {
			service = getFascicoloPersonaGiuridicaComponent();
		}
		return service.setCodiceFiscaleDaCaa(codiceFiscale);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private FascicoloPersonaGiuridicaComponent getFascicoloPersonaGiuridicaComponent() {
		return applicationContext.getBean(FascicoloPersonaGiuridicaComponent.class);
	}

	private FascicoloPersonaFisicaComponent getFascicoloPersonaFisicaComponent() {
		return applicationContext.getBean(FascicoloPersonaFisicaComponent.class);
	}
}
