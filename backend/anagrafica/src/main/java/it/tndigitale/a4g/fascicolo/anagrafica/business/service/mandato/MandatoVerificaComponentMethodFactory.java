package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaModel;
import it.tndigitale.a4g.framework.support.PersonaSelector;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class MandatoVerificaComponentMethodFactory implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	public MandatoAbstractComponent from(final String codiceFiscale) {
		MandatoAbstractComponent<? extends PersonaModel> service = null;
		if (PersonaSelector.isPersonaFisica(codiceFiscale)) {
			service = getMandatoPersonaFisicaComponent();
		} else {
			service = getMandatoPersonaGiuridicaComponent();
		}
		return service.setCodiceFiscaleDaCaa(codiceFiscale);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private MandatoPersonaGiuridicaComponent getMandatoPersonaGiuridicaComponent() {
		return applicationContext.getBean(MandatoPersonaGiuridicaComponent.class);
	}

	private MandatoPersonaFisicaComponent getMandatoPersonaFisicaComponent() {
		return applicationContext.getBean(MandatoPersonaFisicaComponent.class);
	}
}
