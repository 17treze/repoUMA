package it.tndigitale.a4g.uma.business.service.elenchi;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.framework.model.Attachment;
import it.tndigitale.a4g.uma.GeneralFactory;

@Service
public class ElenchiService {

	@Autowired
	private GeneralFactory factory;

	public Attachment stampaElenco(TipoElenco tipo, Long campagna) throws IOException {
		ElenchiTemplate elenchiTemplate = factory.getElenchiTemplate(ElenchiTemplate.PREFISSO + tipo.name());
		return elenchiTemplate.getCsv(campagna);
	}

}
