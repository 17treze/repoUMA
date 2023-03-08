package it.tndigitale.a4g.richiestamodificasuolo.utente;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.SportelloFascicoloDto;
import it.tndigitale.a4g.fascicolo.client.model.Fascicolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.client.AnagraficaFascicoloClient;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.client.FascicoloClient;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.RichiestaModificaSuoloFilter;

@Component
public class FascicoloAbilitatiComponent {
	
	@Autowired
	private AnagraficaFascicoloClient anagraficaClient;

	public void cuaaAbilitati(RichiestaModificaSuoloFilter criteri) throws Exception {
		List<String> mandatiCuaa = new ArrayList<>();
		
		List<SportelloFascicoloDto> listSportelli = anagraficaClient.ricercaFascicoli();
		if (listSportelli != null) {
			listSportelli.forEach( s -> 
				mandatiCuaa.addAll(s.getCuaaList())
			);
		}
		criteri.setListCuaaMandatoCaa(mandatiCuaa);
	}

	public boolean isCuaaAbilitato(String cuaa) {
		List<String> mandatiCuaa = new ArrayList<>();
		
		try {
			List<SportelloFascicoloDto> listSportelli = anagraficaClient.ricercaFascicoli();
			if (listSportelli != null) {
				listSportelli.forEach( s -> 
					mandatiCuaa.addAll(s.getCuaaList())
				);
			}
			return mandatiCuaa.contains(cuaa);
		} catch (Exception e) {
			return false;
		}

	}
}
