package it.tndigitale.a4g.uma.business.service.client;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.territorio.client.api.PianoColturaleControllerApi;
import it.tndigitale.a4g.fascicolo.territorio.client.model.ColturaDto;
import it.tndigitale.a4g.fascicolo.territorio.client.model.CriterioMantenimento;
import it.tndigitale.a4g.fascicolo.territorio.client.model.ParticellaDto;
import it.tndigitale.a4g.fascicolo.territorio.client.model.TitoloConduzione;

@Component
public class UmaTerritorioClient extends AbstractClient {

	@Value("${it.tndigit.a4g.uma.fascicolo.territorio.url}")
	private String urlTerritorio;

	// reperisce le colture solo in provincia di TN e BZ
	// filtra solo le particelle opportune - Refactor: fai in modo che le restituisca già pronte interrogando solo il servizio rest
	public List<ParticellaDto> getColture(String cuaa, LocalDateTime data) {
		List<ParticellaDto> pianoColturale = getPianoColturaleControllerApi().getPianoColturale(cuaa, data, null, null, null);
		// UMA-01-02-06-A-RC-1: Scarta le particelle che hanno conduzione Altro e Atto 314 e TASK-UMA-37: scarta conduzione Altro e Atto 394
		if (CollectionUtils.isEmpty(pianoColturale)) {return new ArrayList<>();}
		return pianoColturale.stream()
				.filter(pc -> !(314 == pc.getConduzioneDto().getCodiceAtto() && TitoloConduzione.ALTRO.name().equals(pc.getConduzioneDto().getTitolo().name())))
				.filter(pc -> !(394 == pc.getConduzioneDto().getCodiceAtto() && TitoloConduzione.ALTRO.name().equals(pc.getConduzioneDto().getTitolo().name())))
				// escludi in ciascuna particella le colture che hanno criterio di mantenimento "NESSUNA_PRATICA"
				.map(pc -> {
					List<ColturaDto> coltureFiltrate = pc.getColture().stream().filter(c -> !CriterioMantenimento.NESSUNA_PRATICA.equals(c.getCriterioMantenimento())).collect(Collectors.toList());
					return pc.setColture(coltureFiltrate);
				})
				.collect(Collectors.toList());
	}

	private PianoColturaleControllerApi getPianoColturaleControllerApi() {
		return restClientProxy(PianoColturaleControllerApi.class, urlTerritorio);
	}
}
