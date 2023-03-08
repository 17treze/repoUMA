package it.tndigitale.a4g.zootecnia.business.event;

import it.tndigitale.a4g.framework.event.store.EventStoreService;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;
import it.tndigitale.a4g.zootecnia.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.zootecnia.business.persistence.repository.ControlloCompletezzaDao;
import it.tndigitale.a4g.zootecnia.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.zootecnia.business.service.ControlliFascicoloZootecniaCompletoEnum;
import it.tndigitale.a4g.zootecnia.business.service.ZootecniaService;
import it.tndigitale.a4g.zootecnia.dto.EsitoControlloDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.Optional;

@Service
public class ControlloCompletezzaListener {
	@Autowired
	private ControlloCompletezzaDao controlloCompletezzaDao;
	@Autowired
	private ZootecniaService zootecniaService;
	@Autowired
	private EventStoreService eventStoreService;

	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@TransactionalEventListener
	public void handleEvent(StartControlloCompletezzaEvent event) {
		String cuaa = event.getData().getCuaa();
		Integer idValidazione = event.getData().getIdValidazione();
		try {
			var resultMap = zootecniaService.getControlloCompletezzaFascicolo(cuaa);
			for (Map.Entry<ControlliFascicoloZootecniaCompletoEnum, EsitoControlloDto> entry : resultMap.entrySet()) {
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
