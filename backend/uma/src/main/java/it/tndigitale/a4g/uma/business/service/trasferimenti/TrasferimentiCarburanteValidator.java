package it.tndigitale.a4g.uma.business.service.trasferimenti;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.TrasferimentoCarburanteDao;
import it.tndigitale.a4g.uma.business.service.consumi.calcoli.CarburanteHelper;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;

@Component
public class TrasferimentiCarburanteValidator {


	// ATTENZIONE: Non è previsto nessun controllo sul destinatario del carburante. E' delegato al CAA operare correttamente
	private static final String UMA_07_02_BR1 = "L’azienda agricola selezionata non può ricevere carburante!";
	private static final String UMA_07_02_BR2 = "La quantità digitata eccede i litri di carburante che è possibile trasferire!";
	private static final String UMA_07_03_BR1 = "Attenzione! Non è possibile effettuare più di un trasferimento di carburante";
	private static final String UMA_07_03_BR2 = "Non è possibile modificare questo trasferimento di carburante";

	@Autowired
	private TrasferimentoCarburanteDao trasferimentoCarburanteDao;
	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;
	@Autowired
	private CarburanteHelper carburanteHelper;

	// verifica consistenza dei dati
	void valida(RichiestaCarburanteModel richiestaMittente, RichiestaCarburanteModel richiestaDestinatario) {

		//non posso trasferire se il mittente ha già la dichiarazione consumi autorizzata per l'anno di campagna
		Optional<DichiarazioneConsumiModel> dichiarazioneMittente = dichiarazioneConsumiDao.findByRichiestaCarburante_cuaaAndRichiestaCarburante_campagna(richiestaMittente.getCuaa(), richiestaMittente.getCampagna());
		Assert.isTrue(dichiarazioneMittente.isPresent() ? !StatoDichiarazioneConsumi.PROTOCOLLATA.equals(dichiarazioneMittente.get().getStato()) : Boolean.TRUE, UMA_07_03_BR2); 

		//non posso trasferire se il destinatario ha già la dichiarazione consumi autorizzata per l'anno di campagna
		Optional<DichiarazioneConsumiModel> dichiarazioneDestinatario = dichiarazioneConsumiDao.findByRichiestaCarburante_cuaaAndRichiestaCarburante_campagna(richiestaDestinatario.getCuaa(), richiestaDestinatario.getCampagna());
		Assert.isTrue(dichiarazioneDestinatario.isPresent() ? !StatoDichiarazioneConsumi.PROTOCOLLATA.equals(dichiarazioneDestinatario.get().getStato()) : Boolean.TRUE, UMA_07_03_BR2); 

		// non posso trasferire se il destinatario non ha una richiesta autorizzata
		Assert.isTrue(StatoRichiestaCarburante.AUTORIZZATA.equals(richiestaDestinatario.getStato()), UMA_07_02_BR1);

		// non posso trasferire a me stesso 
		Assert.isTrue(!richiestaDestinatario.getCuaa().equals(richiestaMittente.getCuaa()), UMA_07_02_BR1);

		// non posso trasferire se ho già fatto un trasferimento durante l'anno di campagna
		Assert.isTrue(!trasferimentoCarburanteDao.existsByRichiestaCarburante(richiestaMittente), UMA_07_03_BR1);

		// il mittente e il destinatario hanno richieste relativamente allo stesso anno di campagna
		Assert.isTrue(richiestaMittente.getCampagna().equals(richiestaDestinatario.getCampagna()), UMA_07_02_BR1);

	}

	// il quantitativo trasferito non deve superare il quantitativo trasferibile (= residuo + prelevato)
	// verifica se l'azienda può trasferire il quantitativo indicato
	void validaTrasferimento(RichiestaCarburanteModel richiestaMittente, CarburanteDto carburanteTrasferito) {
		CarburanteDto trasferibile = carburanteHelper.calcolaCarburanteTrasferibile(richiestaMittente.getCuaa(), richiestaMittente.getCampagna());
		if (carburanteTrasferito.getGasolio() > trasferibile.getGasolio() 
				|| carburanteTrasferito.getBenzina() > trasferibile.getBenzina() 
				|| carburanteTrasferito.getGasolioSerre() > trasferibile.getGasolioSerre()) {
			throw new IllegalArgumentException(UMA_07_02_BR2);
		}
	}



}
