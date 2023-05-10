package it.tndigitale.a4g.uma.business.service.configurazione;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.ColturaGruppiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.ConfigurazioneModel;
import it.tndigitale.a4g.uma.business.persistence.repository.ColturaGruppiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.ConfigurazioneDao;
import it.tndigitale.a4g.uma.dto.ColturaGruppiDto;

@Service
public class ConfigurazioneService {
	
	@Autowired
	private Clock clock;
	@Autowired
	private ConfigurazioneDao configurazioneDao;
	@Autowired
	private ColturaGruppiDao colturaGruppiDao;
	
	public LocalDateTime getDataLimitePrelievi(int annoCampagna) {
		var conf = configurazioneDao.findByCampagna(annoCampagna);
		// Se esiste una configurazione restituiamo la data aggiornata, altrimenti quella di default (1 Novembre)
		if (conf.isPresent()) {
			return conf.get().getDataPrelievi();
		}
		else {
			return LocalDateTime.of(annoCampagna, 11, 1, 0, 0);
		}
	}
	
	public Long modificaDataLimitePrelievi(LocalDateTime dataLimitePrelievi) {
		int campagnaAttuale = clock.now().getYear();
		
		// Controllo se la data in input appartenga all'anno di campagna attuale
		if (dataLimitePrelievi.getYear() != campagnaAttuale) {
			throw new IllegalArgumentException(
					"La data limite per i prelievi deve essere riferita all'anno di campagna attuale");
		}
		
		// Cerco la possibile configurazione già inserita per l'anno di campagna attuale
		var existingConf = configurazioneDao.findByCampagna(campagnaAttuale);
		var toSave = new ConfigurazioneModel();
		
		// Se è già presente una configurazione salvata la modifichiamo, altrimenti ne creiamo una nuova
		if (existingConf.isPresent()) {
			toSave = existingConf.get();
		}
		else {
			toSave.setCampagna(campagnaAttuale);
		}
		toSave.setDataPrelievi(dataLimitePrelievi);
		
		return configurazioneDao.save(toSave).getId();
	}
	
	public RisultatiPaginati<ColturaGruppiDto> getGruppiColturali(Paginazione paginazione, Ordinamento ordinamento) {
		List<ColturaGruppiDto> listColturaGruppiDto = new ArrayList<ColturaGruppiDto>();
		Page<ColturaGruppiModel> pageColturaGruppiModel = colturaGruppiDao
				.findAllValid(PageableBuilder.build().from(paginazione, ordinamento));
		for (ColturaGruppiModel colturaGruppiModel : pageColturaGruppiModel) {
			ColturaGruppiDto colturaGruppiDto = new ColturaGruppiDto();
			colturaGruppiDto.setAnnoInizio(colturaGruppiModel.getAnnoInizio())
					.setCodiceDestUso(colturaGruppiModel.getCodiceDestUso())
					.setCodiceQualita(colturaGruppiModel.getCodiceQualita())
					.setCodiceSuolo(colturaGruppiModel.getCodiceSuolo()).setCodiceUso(colturaGruppiModel.getCodiceUso())
					.setCodiceVarieta(colturaGruppiModel.getCodiceVarieta())
					.setGruppoLavorazione(colturaGruppiModel.getGruppoLavorazione().getId());
			listColturaGruppiDto.add(colturaGruppiDto);
		}
		return RisultatiPaginati.of(listColturaGruppiDto, pageColturaGruppiModel.getTotalElements());
	}
	
}
