package it.tndigitale.a4g.fascicolo.mediator.business.service.event;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.mediator.business.service.client.DotazioneTecnicaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.ZootecniaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.dto.MigrazioneFascicoloDto;
import it.tndigitale.a4g.framework.client.custom.AnagraficaClient;
import it.tndigitale.a4g.framework.event.store.EventStoreService;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;

@Component
public class MigrazioneFascicoloListener {
	private static final Logger log = LoggerFactory.getLogger(MigrazioneFascicoloListener.class);

	@Autowired private AnagraficaClient anagraficaClient;
	@Autowired private EventStoreService eventStoreService;
	@Autowired private ZootecniaPrivateClient zootecniaPrivateClient;
	@Autowired private DotazioneTecnicaPrivateClient dotazioneTecnicaPrivateClient;

	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@TransactionalEventListener
	public void handleEvent(MigrazioneFascicoloEvent event) {

		MigrazioneFascicoloDto data = event.getData();
		List<ByteArrayResource> allegati = data.getAllegati();

		try {
			List<File> attachments = null;
			File contratto = null;

			contratto = File.createTempFile("mandato", ".pdf");
			FileUtils.writeByteArrayToFile(contratto, data.getContratto().getByteArray());
			if (!CollectionUtils.isEmpty(allegati)) {
				attachments = new ArrayList<>();
				for (ByteArrayResource allegato : allegati) {
					File tmpAllegato = File.createTempFile(allegato.getDescription(), "");
					FileUtils.writeByteArrayToFile(tmpAllegato, allegato.getByteArray());
					attachments.add(tmpAllegato);
				}
			}
			anagraficaClient.migraUsingPOST(attachments, data.getCodiceFiscaleRappresentante(), contratto, data.getCuaa(),
					data.getIdentificativoSportello(), data.getMigraModoPagamento(), data.getUtenteConnesso());

			try {
				zootecniaPrivateClient.aggiornaAllevamenti(data.getCuaa(), LocalDate.now());
			} catch (Throwable e) {
				log.warn("Non ho migrato la zootecnia : procedo {}", data.getCuaa());
			}

			if (data.getMigraMacchinari()) {
				dotazioneTecnicaPrivateClient.migraMacchinari(data.getCuaa());
			}
			if (data.getMigraFabbricati()) {
				dotazioneTecnicaPrivateClient.migraFabbricati(data.getCuaa());
			}
		} catch (Exception ex) {
			eventStoreService.triggerRetry(ex, event);
		}
	}
}
