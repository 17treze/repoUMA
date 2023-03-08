package it.tndigitale.a4g.fascicolo.anagrafica.business.event;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.ControlloCompletezzaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.ControlliFascicoloAnagraficaCompletoEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.ValidazioneFascicoloService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.EsitoControlloDto;
import it.tndigitale.a4g.framework.event.store.EventStoreService;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Service
public class ControlloCompletezzaListener {
	@Autowired
	private ControlloCompletezzaDao controlloCompletezzaDao;
	@Autowired
	private ValidazioneFascicoloService validazioneFascicoloService;
	@Autowired
	private EventStoreService eventStoreService;

	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@TransactionalEventListener
	public void handleEvent(StartControlloCompletezzaEvent event) {
		String cuaa = event.getData().getCuaa();
		Integer idValidazione = event.getData().getIdValidazione();
		try {
			var resultMap = validazioneFascicoloService.getControlloCompletezzaFascicolo(cuaa);
			for (Map.Entry<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto> entry : resultMap.entrySet()) {
//				TODO creare indice cuaa, tipoControllo?
				var controlloCompletezzaModel = controlloCompletezzaDao.findByFascicolo_CuaaAndFascicolo_IdValidazioneAndTipoControllo(cuaa, 0, entry.getKey().name()).orElseThrow();
				controlloCompletezzaModel.setEsito(entry.getValue().getEsito());
				controlloCompletezzaDao.save(controlloCompletezzaModel);
			}
		} catch (Exception ex) {
			eventStoreService.triggerRetry(ex, event);
		}
	}
}
