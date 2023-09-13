package it.tndigitale.a4g.uma.business.service.clienti;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// import it.tndigitale.a4g.fascicolo.anagrafica.client.model.MovimentoValidazioneFascicoloAgsDto;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;

@Component
public class ClientiValidator {

	private static final String BR2_UMA_03_04 = "Il CUAA selezionato non ha un fascicolo aziendale valido nell’anno precedente";
	private static final String UMA_03_04_ESISTE_CLIENTE = "Il CUAA selezionato è già stato inserito!";
	private static final String UMA_03_04_STESSO_CLIENTE = "Non è possibile importare la propria azienda";

	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;

//	@Autowired
//	private UmaAnagraficaClient anagraficaClient;

//	MovimentoValidazioneFascicoloAgsDto validaFascicoloCliente(Long id, Integer campagna) {
//		MovimentoValidazioneFascicoloAgsDto movimentoValidazioneFascicolo = anagraficaClient.getMovimentazioniValidazioneFascicolo(id, campagna.longValue()); 
//		if (movimentoValidazioneFascicolo != null) {
//			fascicoloValidoValidatoAnnoPrecedente
//			.accept(movimentoValidazioneFascicolo);
//		} else {
//			throw new IllegalArgumentException("Non è stato possibile reperire i dati del fascicolo con id: " + id.toString());
//		}
//		return null; // movimentoValidazioneFascicolo;
//	}
//
//	DichiarazioneConsumiModel validaDichiarazioneConsumiCliente(Long id, MovimentoValidazioneFascicoloAgsDto movFas) {
//		Optional<DichiarazioneConsumiModel> dichiarazioneConsumiOpt = dichiarazioneConsumiDao.findById(id);
//		DichiarazioneConsumiModel dichiarazioneConsumi = null;
//		if (dichiarazioneConsumiOpt.isPresent()) {
//			dichiarazioneConsumi = dichiarazioneConsumiOpt.get();
//			stessoCliente
//			.andThen(esisteCliente)
//			.accept(dichiarazioneConsumi, movFas);
//		} else {
//			throw new IllegalArgumentException("Non è stato possibile reperire la dichiarazione consumi con id: " + id.toString());
//		}
//
//		return dichiarazioneConsumi;
//	}
//
//	private BiConsumer<DichiarazioneConsumiModel, MovimentoValidazioneFascicoloAgsDto> stessoCliente = (dichiarazione, movimentoValidazioneFascicolo) -> {
//		// Verifico che il cliente da importare non sia lo stesso che detenie la dichiarazione consumi
//		if (dichiarazione.getRichiestaCarburante().getCuaa().equals(movimentoValidazioneFascicolo.getCuaa())) {
//			throw new IllegalArgumentException(UMA_03_04_STESSO_CLIENTE);
//		}
//	};
//
//	private BiConsumer<DichiarazioneConsumiModel, MovimentoValidazioneFascicoloAgsDto> esisteCliente = (dichiarazione, movimentoValidazioneFascicolo) -> dichiarazione.getClienti().forEach(cliente -> {
//		if (cliente.getCuaa().equals(movimentoValidazioneFascicolo.getCuaa())) {
//			throw new IllegalArgumentException(UMA_03_04_ESISTE_CLIENTE);
//		}
//	});
//
//	private Consumer<MovimentoValidazioneFascicoloAgsDto> fascicoloValidoValidatoAnnoPrecedente = movimentoValidazioneFascicolo ->  {
//		if (movimentoValidazioneFascicolo.getDataUltimaValidazionePositiva() == null) {
//			throw new IllegalArgumentException(BR2_UMA_03_04);
//		}
//	};

}
