package it.tndigitale.a4gistruttoria.component.acz;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaAccoppiati;
import it.tndigitale.a4gistruttoria.service.DatiIstruttoreService;

@Component
public class ControlloDatiIstruttoriaControlliInLoco {
	
	private static final Logger logger = LoggerFactory.getLogger(ControlloDatiIstruttoriaControlliInLoco.class);

	public Boolean checkControlliInLocoOf(IstruttoriaModel istruttoria) {
		return istruttoria != null &&
			   istruttoria.getDatiIstruttoreZootecnia() != null &&
			   istruttoria.getDatiIstruttoreZootecnia().getControlloSigecoLoco();
	}

}
