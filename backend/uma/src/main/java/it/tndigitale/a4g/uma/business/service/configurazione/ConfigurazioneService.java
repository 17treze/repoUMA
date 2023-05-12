package it.tndigitale.a4g.uma.business.service.configurazione;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.CoefficienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.ColturaGruppiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.ConfigurazioneModel;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbricatoGruppiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.GruppoLavorazioneModel;
import it.tndigitale.a4g.uma.business.persistence.entity.LavorazioneModel;
import it.tndigitale.a4g.uma.business.persistence.repository.CoefficienteDao;
import it.tndigitale.a4g.uma.business.persistence.repository.ColturaGruppiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.ConfigurazioneDao;
import it.tndigitale.a4g.uma.business.persistence.repository.FabbricatoGruppiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.GruppiLavorazioneDao;
import it.tndigitale.a4g.uma.business.persistence.repository.LavorazioneDao;
import it.tndigitale.a4g.uma.dto.CoefficienteDto;
import it.tndigitale.a4g.uma.dto.ColturaGruppiDto;
import it.tndigitale.a4g.uma.dto.FabbricatoGruppiDto;
import it.tndigitale.a4g.uma.dto.GruppoLavorazioneDto;
import it.tndigitale.a4g.uma.dto.LavorazioneDto;

@Service
public class ConfigurazioneService {
	
	@Autowired
	private Clock clock;
	@Autowired
	private ConfigurazioneDao configurazioneDao;
	@Autowired
	private ColturaGruppiDao colturaGruppiDao;
	@Autowired
	private GruppiLavorazioneDao gruppiLavorazioneDao;
	@Autowired
	private FabbricatoGruppiDao fabbricatoGruppiDao;
	@Autowired
	private CoefficienteDao coefficienteDao;
	@Autowired
	private LavorazioneDao lavorazioneDao;
	
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
			colturaGruppiDto.setId(colturaGruppiModel.getId()).setAnnoInizio(colturaGruppiModel.getAnnoInizio())
					.setCodiceDestUso(colturaGruppiModel.getCodiceDestUso())
					.setCodiceQualita(colturaGruppiModel.getCodiceQualita())
					.setCodiceSuolo(colturaGruppiModel.getCodiceSuolo()).setCodiceUso(colturaGruppiModel.getCodiceUso())
					.setCodiceVarieta(colturaGruppiModel.getCodiceVarieta())
					.setGruppoLavorazione(colturaGruppiModel.getGruppoLavorazione().getId());
			listColturaGruppiDto.add(colturaGruppiDto);
		}
		return RisultatiPaginati.of(listColturaGruppiDto, pageColturaGruppiModel.getTotalElements());
	}
	
	public RisultatiPaginati<GruppoLavorazioneDto> getGruppiLavorazione(Paginazione paginazione,
			Ordinamento ordinamento) {
		List<GruppoLavorazioneDto> listGruppoLavorazioneDto = new ArrayList<GruppoLavorazioneDto>();
		Page<GruppoLavorazioneModel> pageGruppoLavorazioneModel = gruppiLavorazioneDao
				.findAllValid(PageableBuilder.build().from(paginazione, ordinamento));
		for (GruppoLavorazioneModel gruppoLavorazioneModel : pageGruppoLavorazioneModel) {
			GruppoLavorazioneDto gruppoLavorazioneDto = new GruppoLavorazioneDto();
			gruppoLavorazioneDto.setId(gruppoLavorazioneModel.getId())
					.setAnnoInizio(gruppoLavorazioneModel.getAnnoInizio()).setNome(gruppoLavorazioneModel.getNome())
					.setIndice(gruppoLavorazioneModel.getIndice())
					.setAmbitoLavorazione(gruppoLavorazioneModel.getAmbitoLavorazione());
			listGruppoLavorazioneDto.add(gruppoLavorazioneDto);
		}
		return RisultatiPaginati.of(listGruppoLavorazioneDto, pageGruppoLavorazioneModel.getTotalElements());
	}
	
	public Long saveGruppoLavorazione(GruppoLavorazioneDto gruppoLavorazioneDto) {
		
		GruppoLavorazioneModel gruppoLavorazioneModel = new GruppoLavorazioneModel();
		Optional<GruppoLavorazioneModel> gruppoLavorazioneModelOpt = gruppiLavorazioneDao
				.findById(gruppoLavorazioneDto.getId());
		if (gruppoLavorazioneModelOpt.isPresent()) {
			gruppoLavorazioneModel = gruppoLavorazioneModelOpt.get();
		}
		else {
			gruppoLavorazioneModel.setId(gruppoLavorazioneDto.getId());
		}
		gruppoLavorazioneModel.setNome(gruppoLavorazioneDto.getNome());
		gruppoLavorazioneModel.setIndice(gruppoLavorazioneDto.getIndice());
		gruppoLavorazioneModel.setAmbitoLavorazione(gruppoLavorazioneDto.getAmbitoLavorazione());
		gruppoLavorazioneModel.setAnnoInizio(gruppoLavorazioneDto.getAnnoInizio());
		gruppoLavorazioneModel.setAnnoFine(gruppoLavorazioneDto.getAnnoInizio());
		return gruppiLavorazioneDao.save(gruppoLavorazioneModel).getId();
	}
	
	public RisultatiPaginati<FabbricatoGruppiDto> getGruppiFabbricato(Paginazione paginazione,
			Ordinamento ordinamento) {
		List<FabbricatoGruppiDto> listFabbricatoGruppiDto = new ArrayList<FabbricatoGruppiDto>();
		Page<FabbricatoGruppiModel> pageFabbricatoGruppiModel = fabbricatoGruppiDao
				.findAll(PageableBuilder.build().from(paginazione, ordinamento));
		for (FabbricatoGruppiModel fabbricatoGruppiModel : pageFabbricatoGruppiModel) {
			FabbricatoGruppiDto fabbricatoGruppiDto = new FabbricatoGruppiDto();
			fabbricatoGruppiDto.setId(fabbricatoGruppiModel.getId())
					.setCodiceFabbricato(fabbricatoGruppiModel.getCodiceFabbricato())
					.setTipoFabbricato(fabbricatoGruppiModel.getTipoFabbricato())
					.setGruppoLavorazione(fabbricatoGruppiModel.getGruppoLavorazione().getId());
			listFabbricatoGruppiDto.add(fabbricatoGruppiDto);
		}
		return RisultatiPaginati.of(listFabbricatoGruppiDto, pageFabbricatoGruppiModel.getTotalElements());
	}
	
	public RisultatiPaginati<CoefficienteDto> getCoefficienti(Paginazione paginazione, Ordinamento ordinamento) {
		List<CoefficienteDto> listCoefficienteDto = new ArrayList<CoefficienteDto>();
		Page<CoefficienteModel> pageCoefficienteModel = coefficienteDao
				.findAllValid(PageableBuilder.build().from(paginazione, ordinamento));
		for (CoefficienteModel coefficienteModel : pageCoefficienteModel) {
			CoefficienteDto coefficienteDto = new CoefficienteDto();
			coefficienteDto.setId(coefficienteModel.getId()).setAnnoInizio(coefficienteModel.getAnnoInizio())
					.setCoefficiente(coefficienteModel.getCoefficiente())
					.setIdLavorazione(coefficienteModel.getLavorazioneModel().getId());
			listCoefficienteDto.add(coefficienteDto);
		}
		return RisultatiPaginati.of(listCoefficienteDto, pageCoefficienteModel.getTotalElements());
	}
	
	public Long saveCoefficiente(CoefficienteDto coefficienteDto) {
		
		CoefficienteModel coefficienteModel = new CoefficienteModel();
		Optional<CoefficienteModel> coefficienteModelOpt = coefficienteDao.findById(coefficienteDto.getId());
		if (coefficienteModelOpt.isPresent()) {
			coefficienteModel = coefficienteModelOpt.get();
		}
		else {
			coefficienteModel.setId(coefficienteDto.getId());
		}
		coefficienteModel.setCoefficiente(coefficienteDto.getCoefficiente());
		Optional<LavorazioneModel> lavorazioneModelOpt = lavorazioneDao.findById(coefficienteDto.getIdLavorazione());
		if (lavorazioneModelOpt.isPresent()) {
			coefficienteModel.setLavorazioneModel(lavorazioneModelOpt.get());
		}
		else {
			throw new IllegalArgumentException("Identificativo lavorazione mancante o non esistente");
		}
		coefficienteModel.setAnnoInizio(coefficienteDto.getAnnoInizio());
		coefficienteModel.setAnnoFine(coefficienteDto.getAnnoInizio());
		return coefficienteDao.save(coefficienteModel).getId();
	}
	
	public RisultatiPaginati<LavorazioneDto> getLavorazioni(Paginazione paginazione, Ordinamento ordinamento) {
		List<LavorazioneDto> listLavorazioneDto = new ArrayList<LavorazioneDto>();
		Page<LavorazioneModel> pageLavorazioneModel = lavorazioneDao
				.findAll(PageableBuilder.build().from(paginazione, ordinamento));
		for (LavorazioneModel lavorazioneModel : pageLavorazioneModel) {
			LavorazioneDto lavorazioneDto = new LavorazioneDto();
			GruppoLavorazioneDto gruppoLavorazioneDto = new GruppoLavorazioneDto();
			gruppoLavorazioneDto.setId(lavorazioneModel.getGruppoLavorazione().getId())
					.setAnnoInizio(lavorazioneModel.getGruppoLavorazione().getAnnoInizio())
					.setNome(lavorazioneModel.getGruppoLavorazione().getNome())
					.setIndice(lavorazioneModel.getGruppoLavorazione().getIndice())
					.setAmbitoLavorazione(lavorazioneModel.getGruppoLavorazione().getAmbitoLavorazione());
			lavorazioneDto.setId(lavorazioneModel.getId()).setIndice(lavorazioneModel.getIndice())
					.setNome(lavorazioneModel.getNome()).setTipologia(lavorazioneModel.getTipologia())
					.setUnitaDiMisura(lavorazioneModel.getUnitaDiMisura()).setGruppoLavorazione(gruppoLavorazioneDto);
			listLavorazioneDto.add(lavorazioneDto);
		}
		return RisultatiPaginati.of(listLavorazioneDto, pageLavorazioneModel.getTotalElements());
	}
	
	public Long saveLavorazione(LavorazioneDto lavorazioneDto) {
		
		LavorazioneModel lavorazioneModel = new LavorazioneModel();
		Optional<LavorazioneModel> lavorazioneModelOpt = lavorazioneDao.findById(lavorazioneDto.getId());
		if (lavorazioneModelOpt.isPresent()) {
			lavorazioneModel = lavorazioneModelOpt.get();
		}
		else {
			lavorazioneModel.setId(lavorazioneDto.getId());
		}
		Optional<GruppoLavorazioneModel> gruppoLavorazioneModelOpt = gruppiLavorazioneDao
				.findById(lavorazioneDto.getGruppoLavorazione().getId());
		if (gruppoLavorazioneModelOpt.isPresent()) {
			lavorazioneModel.setGruppoLavorazione(gruppoLavorazioneModelOpt.get());
		}
		else {
			throw new IllegalArgumentException("Identificativo gruppo di lavorazione mancante o non esistente");
		}
		lavorazioneModel.setIndice(lavorazioneDto.getIndice());
		lavorazioneModel.setNome(lavorazioneDto.getNome());
		lavorazioneModel.setTipologia(lavorazioneDto.getTipologia());
		lavorazioneModel.setUnitaDiMisura(lavorazioneDto.getUnitaDiMisura());
		return lavorazioneDao.save(lavorazioneModel).getId();
	}
}
