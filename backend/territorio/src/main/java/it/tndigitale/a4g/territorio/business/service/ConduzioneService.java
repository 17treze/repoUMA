package it.tndigitale.a4g.territorio.business.service;

import it.tndigitale.a4g.fascicolo.territorio.dto.TitoloConduzione;
import it.tndigitale.a4g.territorio.business.persistence.entity.*;
import it.tndigitale.a4g.territorio.business.persistence.repository.*;
import it.tndigitale.a4g.territorio.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConduzioneService {

	@Autowired 
	private SottotipoDao sottotipoDao;
	@Autowired
	private TipoDocumentoConduzioneDao tipoDocumentoConduzioneDao;
	@Autowired 
	private FascicoloDao fascicoloDao;
	@Autowired 
	private ParticelleFondiarieDao particelleFondiarieDao;
	@Autowired 
	private DocumentoConduzioneDao documentoConduzioneDao;
	@Autowired 
	private ConduzioneDao conduzioneDao;

	public List<TipoConduzioneDto> elencoTipoConduzione() {
		List<TipoConduzioneDto> result = new ArrayList<>();
		for (TitoloConduzione titoloConduzione: TitoloConduzione.values()) {
			TipoConduzioneDto tipoConduzioneDto = new TipoConduzioneDto();
			tipoConduzioneDto.setAmbito(titoloConduzione);
			List<SottotipoModel> sottotipoModelList = sottotipoDao.findByAmbito(titoloConduzione.name());
			if (sottotipoModelList != null && !sottotipoModelList.isEmpty()) {
				List<SottotipoConduzioneDto> sottotipoConduzioneDtoList = new ArrayList<>();
				sottotipoModelList.forEach(sottotipoModel -> {
					SottotipoConduzioneDto sottotipoConduzioneDto = new SottotipoConduzioneDto();
					sottotipoConduzioneDto.setId(sottotipoModel.getId());
					sottotipoConduzioneDto.setDescrizione(sottotipoModel.getDescrizione());
					List<TipoDocumentoConduzioneModel> tipoDocumentoConduzioneList = tipoDocumentoConduzioneDao.findBySottotipo_Id(sottotipoModel.getId());
					if (tipoDocumentoConduzioneList != null && !tipoDocumentoConduzioneList.isEmpty()) {
						List<TipoDocumentoConduzioneDto> tipoDocumentoConduzioneDtoList = new ArrayList<>();
						tipoDocumentoConduzioneList.forEach(tipoDocumentoConduzioneModel -> {
							TipoDocumentoConduzioneDto tipoDocumentoConduzioneDto = new TipoDocumentoConduzioneDto();
							tipoDocumentoConduzioneDto.setId(tipoDocumentoConduzioneModel.getId());
							tipoDocumentoConduzioneDto.setDescrizione(tipoDocumentoConduzioneModel.getDescrizione());
							tipoDocumentoConduzioneDtoList.add(tipoDocumentoConduzioneDto);
						});
						sottotipoConduzioneDto.setDocumenti(tipoDocumentoConduzioneDtoList);
					}
					sottotipoConduzioneDtoList.add(sottotipoConduzioneDto);
				});
				tipoConduzioneDto.setSottotipo(sottotipoConduzioneDtoList);
			}
			result.add(tipoConduzioneDto);
		}

		return result;
	}
	
	@Transactional(rollbackFor = ConduzioneException.class)
	public void salvaConduzioneTerreni(String cuaa, ConduzioneTerreniDto conduzioneTerreno) throws ConduzioneException {
		try {
			// bisogna salvare il fascicolo se non c'è
			FascicoloModel fascicoloModel = null;
			Optional<FascicoloModel> fascicoloModelOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
			if (fascicoloModelOpt.isPresent()) {
				fascicoloModel = fascicoloModelOpt.get();
			} else {
				fascicoloModel = new FascicoloModel();
				fascicoloModel.setCuaa(cuaa);
				fascicoloModel = fascicoloDao.save(fascicoloModel);
			}
//		salvataggio conduzione
			ConduzioneModel conduzioneModel = new ConduzioneModel();
			conduzioneModel.setParticelle(new ArrayList<>());
			conduzioneModel.setDocumenti(new ArrayList<>());
			Optional<SottotipoModel> sottotipoModelOpt = sottotipoDao.findById(conduzioneTerreno.getIdSottotipo());
			if (sottotipoModelOpt.isPresent()) {
				conduzioneModel.setSottotipo(sottotipoModelOpt.get());
			} else {
				throw new ConduzioneException("Sottotipo conduzione non trovato: " + conduzioneTerreno.getIdSottotipo());
			}
			conduzioneModel.setFascicolo(fascicoloModel);
			conduzioneModel = conduzioneDao.save(conduzioneModel);
//		salvataggio conduzione per poi referenziarlo in documenti  e particelle
			for (ParticellaFondiariaDto part : conduzioneTerreno.getParticelleFondiarie()) {
				ParticelleFondiarieModel particelleFondiarieModel = new ParticelleFondiarieModel();
				particelleFondiarieModel.setParticella(part.getParticella());
				particelleFondiarieModel.setComune(part.getComune());
				particelleFondiarieModel.setSub(part.getSub());
				particelleFondiarieModel.setFoglio(part.getFoglio());
				particelleFondiarieModel.setSuperficieCondotta(part.getSuperficieCondotta());
				particelleFondiarieModel.setConduzione(conduzioneModel);
				conduzioneModel.getParticelle().add(particelleFondiarieDao.save(particelleFondiarieModel));
			}
			for (DocumentoConduzioneDto doc : conduzioneTerreno.getDocumentiConduzione()) {
				DocumentoConduzioneModel documentoConduzioneModel = new DocumentoConduzioneModel();
				Optional<TipoDocumentoConduzioneModel> tipoDocumentoConduzioneOpt = tipoDocumentoConduzioneDao.findById(doc.getIdTipoDocumento());
				if (tipoDocumentoConduzioneOpt.isPresent()) {
					TipoDocumentoConduzioneModel tipoDocumentoConduzioneModel = tipoDocumentoConduzioneOpt.get();
					documentoConduzioneModel.setTipoDocumentoConduzione(tipoDocumentoConduzioneModel);
					documentoConduzioneModel.setDataInserimento(LocalDate.now());
					documentoConduzioneModel.setContratto(doc.getContratto());
					documentoConduzioneModel.setDataInizioValidita(doc.getDataInizioValidita());
					documentoConduzioneModel.setDataFineValidita(doc.getDataFineValidita());
					documentoConduzioneModel.setConduzione(conduzioneModel);
					conduzioneModel.getDocumenti().add(documentoConduzioneDao.save(documentoConduzioneModel));
				} else {
					throw new ConduzioneException("Tipo documento non trovato: " + doc.getIdTipoDocumento());
				}
			}
		} catch (Exception e) {
			throw new ConduzioneException(e.getMessage());
		}
	}
}
