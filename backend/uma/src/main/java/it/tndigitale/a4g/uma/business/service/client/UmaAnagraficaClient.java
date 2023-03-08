package it.tndigitale.a4g.uma.business.service.client;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.client.api.CaaAgsControllerApi;
import it.tndigitale.a4g.fascicolo.anagrafica.client.api.FascicoloAgsControllerApi;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.CaricaAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.MovimentoValidazioneFascicoloAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.SportelloFascicoloDto;

@Component
public class UmaAnagraficaClient extends AbstractClient {

	@Value("${it.tndigit.a4g.uma.fascicolo.anagrafica.url}")
	private String urlAnagrafica;

	// Methods from Controllers
	public List<CaricaAgsDto> getTitolariRappresentantiLegali(String cuaa) {
		return this.getFascicoloAgsControllerApi().getPersoneConCaricaUsingGET(cuaa);
	}

	public List<CaricaAgsDto> getEredi(String cuaa) {
		return this.getFascicoloAgsControllerApi().getErediUsingGET(cuaa);
	}

	public FascicoloAgsDto getFascicolo(String cuaa) {
		return this.getFascicoloAgsControllerApi().getFascicoloUsingGET(cuaa, null);
	}

	public FascicoloAgsDto getFascicolo(String cuaa, LocalDateTime data) {
		return this.getFascicoloAgsControllerApi().getFascicoloUsingGET(cuaa, data);
	}

	public MovimentoValidazioneFascicoloAgsDto getMovimentazioniValidazioneFascicolo(Long id, Long campagna) {
		return this.getFascicoloAgsControllerApi().getMovimentiValidazioneFascicoloUsingGET(campagna, id);
	}

	public List<SportelloFascicoloDto> getSportelliFascicoli() {
		return this.getCaaAgsControllerApi().getFascicoliEnteUtenteConnessoUsingGET(null);
	}

	// Get Controller 
	private FascicoloAgsControllerApi getFascicoloAgsControllerApi() {
		return restClientProxy(FascicoloAgsControllerApi.class, urlAnagrafica);
	}

	private CaaAgsControllerApi getCaaAgsControllerApi() {
		return restClientProxy(CaaAgsControllerApi.class, urlAnagrafica);
	}
}
