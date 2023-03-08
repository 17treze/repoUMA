package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneInProprioModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.RicercaFascicoloService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.DetenzioneDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.builder.DetenzioneInProprioBuilder;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.builder.MandatoBuilder;
import it.tndigitale.a4g.framework.time.Clock;

@Service
public class DetenzioneService {
	@Autowired private RicercaFascicoloService ricercaFascicoloService;
	@Autowired private Clock clock;

	public DetenzioneDto getDetenzione(final String cuaa, final int idValidazione) throws Exception {
		var fascicoloModel = ricercaFascicoloService.getFascicoloModel(cuaa, idValidazione);
		DetenzioneDto detenzioneDto = null;
		Optional<DetenzioneModel> detenzioneCorrenteOpt = getDetenzioneCorrente(fascicoloModel);
		if (!detenzioneCorrenteOpt.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, DetenzioneNotification.DETENZIONE_MANCANTE.name());
		}
		DetenzioneModel detenzioneCorrente = detenzioneCorrenteOpt.get();
		if (detenzioneCorrente instanceof MandatoModel) {
			detenzioneDto = MandatoBuilder.from((MandatoModel)detenzioneCorrente);
		} else if (detenzioneCorrente instanceof DetenzioneInProprioModel) {
			detenzioneDto = DetenzioneInProprioBuilder.from((DetenzioneInProprioModel)detenzioneCorrente);
		} else {
			return null;
		}
		return detenzioneDto;
	}
	
	public Optional<DetenzioneModel> getDetenzioneCorrente(final FascicoloModel fascicoloModel) {
		LocalDate now = clock.today();
		for (DetenzioneModel detenzione : fascicoloModel.getDetenzioni()) {
			LocalDate dataInizio = detenzione.getDataInizio();
			LocalDate dataFine = detenzione.getDataFine();
			if ((dataInizio.isBefore(now) || dataInizio.isEqual(now)) && 
					(dataFine == null || !dataFine.isBefore(now))) {
				return Optional.of(detenzione);
			}
		}
		return Optional.empty();
	}
}
