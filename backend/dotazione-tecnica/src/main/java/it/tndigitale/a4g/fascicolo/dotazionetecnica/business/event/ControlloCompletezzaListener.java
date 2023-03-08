package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.event;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.ControlloCompletezzaDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.ControlliFascicoloDotazioneTecnicaCompletoEnum;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.FascicoloService;
import it.tndigitale.a4g.framework.component.dto.EsitoControlloDto;
import it.tndigitale.a4g.framework.event.store.EventStoreService;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;

@Service
public class ControlloCompletezzaListener {
	@Autowired
	private ControlloCompletezzaDao controlloCompletezzaDao;
	@Autowired
	private FascicoloService fascicoloService;
	@Autowired
	private EventStoreService eventStoreService;

	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@TransactionalEventListener
	public void handleEvent(StartControlloCompletezzaEvent event) {
		String cuaa = event.getData().getCuaa();
		try {
			var resultMap = fascicoloService.getControlloCompletezzaFascicolo(cuaa);
			for (Map.Entry<ControlliFascicoloDotazioneTecnicaCompletoEnum, EsitoControlloDto> entry : resultMap.entrySet()) {
				//				TODO creare indice cuaa, tipoControllo?
				var controlloCompletezzaModel = controlloCompletezzaDao.findByCuaaAndTipoControllo(cuaa, entry.getKey().name()).orElseThrow();
				controlloCompletezzaModel.setEsito(entry.getValue().getEsito());
				controlloCompletezzaDao.save(controlloCompletezzaModel);
			}
		} catch (Exception ex) {
			eventStoreService.triggerRetry(ex, event);
		}
	}
}
