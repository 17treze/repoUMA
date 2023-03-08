package it.tndigitale.a4g.territorio.event;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import it.tndigitale.a4g.fascicolo.territorio.business.service.ControlliFascicoloAgsCompletoEnum;
import it.tndigitale.a4g.fascicolo.territorio.business.service.FascicoloService;
import it.tndigitale.a4g.framework.component.dto.EsitoControlloDto;
import it.tndigitale.a4g.framework.event.store.EventStoreService;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;
import it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dao.ControlloCompletezzaDao;

@Service
public class ControlloCompletezzaListener {
	@Autowired
	private FascicoloService fascicoloService;
	@Autowired
	private EventStoreService eventStoreService;
	@Autowired
	private ControlloCompletezzaDao controlloCompletezzaDao;

	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@TransactionalEventListener
	public void handleEvent(StartControlloCompletezzaEvent event) {
		String cuaa = event.getData().getCuaa();
		try {
			var resultMap = fascicoloService.getControlloCompletezzaFascicolo(cuaa);
			for (Map.Entry<ControlliFascicoloAgsCompletoEnum, EsitoControlloDto> entry : resultMap.entrySet()) {
				var controlloCompletezzaModel = controlloCompletezzaDao.findByCuaaAndTipoControllo(cuaa, entry.getKey().name()).orElseThrow();
				controlloCompletezzaModel.setEsito(entry.getValue().getEsito());
				if (entry.getValue().getIdControllo() != null) {
					controlloCompletezzaModel.setIdControllo(entry.getValue().getIdControllo().intValue());
				}

				controlloCompletezzaDao.save(controlloCompletezzaModel);
			}
		} catch (Exception ex) {
			eventStoreService.triggerRetry(ex, event);
		}
	}
}
