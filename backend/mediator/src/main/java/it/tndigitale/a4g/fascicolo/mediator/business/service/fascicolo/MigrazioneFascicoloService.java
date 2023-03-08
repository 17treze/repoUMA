package it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import it.tndigitale.a4g.fascicolo.mediator.business.service.event.MigrazioneFascicoloEvent;
import it.tndigitale.a4g.fascicolo.mediator.dto.MigrazioneFascicoloDto;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;

@Service
public class MigrazioneFascicoloService {

	@Autowired UtenteComponent utenteComponent;
	@Autowired private EventBus eventBus;

	@Transactional
	public void invioEventoMigrazione(final String cuaa, final String codiceFiscaleRappresentante, final Long identificativoSportello, final byte[] contratto, final List<MultipartFile> allegati, final Boolean migraModoPagamento, final Boolean migraMacchinari, final Boolean migraFabbricati) 
			throws IOException {
		MigrazioneFascicoloDto migrazioneFascicoloDto = new MigrazioneFascicoloDto();
		migrazioneFascicoloDto.setCuaa(cuaa);
		migrazioneFascicoloDto.setCodiceFiscaleRappresentante(codiceFiscaleRappresentante);
		migrazioneFascicoloDto.setIdentificativoSportello(identificativoSportello);
		migrazioneFascicoloDto.setMigraModoPagamento(migraModoPagamento);
		migrazioneFascicoloDto.setMigraMacchinari(migraMacchinari);
		migrazioneFascicoloDto.setMigraFabbricati(migraFabbricati);
		migrazioneFascicoloDto.setContratto(new ByteArrayResource(contratto));
		migrazioneFascicoloDto.setUtenteConnesso(utenteComponent.utenza());
		if (!CollectionUtils.isEmpty(allegati)) {
			List<ByteArrayResource> attachments = new ArrayList<>();
			for (MultipartFile allegato : allegati) {
				attachments.add(new ByteArrayResource(allegato.getBytes()));
			}
			migrazioneFascicoloDto.setAllegati(attachments);
		}
		eventBus.publishEvent(new MigrazioneFascicoloEvent(migrazioneFascicoloDto));
	}
}
