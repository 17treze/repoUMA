package it.tndigitale.a4gistruttoria.service.businesslogic;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import it.tndigitale.a4gistruttoria.repository.dao.PassoTransizioneDao;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiPassoLavorazione;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.repository.dao.AnomDomandaSostegnoDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtAnomDomandaSostegno;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipoControllo;

public abstract class ElaboraPassoIstruttoria {

	@Autowired
	private PassoTransizioneDao passiLavorazioneSostegnoDao;
	@Autowired
	private TransizioneIstruttoriaDao transizioneIstruttoriaDao;
	@Autowired
	private AnomDomandaSostegnoDao daoAnomDomandaSostegno;
	@Autowired
	private ObjectMapper mapper;
	
	public abstract TipologiaPassoTransizione getPasso();
	
	/**
	 * Salva in banca dati il risultato del passaggio di lavorazione
	 *
	 * @param datiPassoLavorazioneIstruttoria
	 * @return
	 */
	protected PassoTransizioneModel salvaPassoLavorazioneSostegno(DatiPassoLavorazione datiPassoLavorazioneIstruttoria) throws Exception {
		PassoTransizioneModel passiLavSostegnoModel = new PassoTransizioneModel();
		passiLavSostegnoModel.setCodiceEsito(datiPassoLavorazioneIstruttoria.getCodiceEsito());
		passiLavSostegnoModel.setCodicePasso(getPasso());
		passiLavSostegnoModel.setEsito(datiPassoLavorazioneIstruttoria.getEsito() ? "OK" : "KO");

		// Rimozione di eventuali variabili di Input con valore a null
		datiPassoLavorazioneIstruttoria.getDatiInput().setVariabiliCalcolo(datiPassoLavorazioneIstruttoria.getDatiInput().getVariabiliCalcolo().stream()
				.filter(v -> (v.getValString() != null || v.getValBoolean() != null || v.getValList() != null || v.getValNumber() != null)).collect(Collectors.toList()));

		// Rimozione di eventuali variabili di Output con valore a null
		datiPassoLavorazioneIstruttoria.getDatiOutput().setVariabiliCalcolo(datiPassoLavorazioneIstruttoria.getDatiOutput().getVariabiliCalcolo().stream()
				.filter(v -> (v.getValString() != null || v.getValBoolean() != null || v.getValList() != null || v.getValNumber() != null)).collect(Collectors.toList()));

		passiLavSostegnoModel.setDatiInput(mapper.writeValueAsString(datiPassoLavorazioneIstruttoria.getDatiInput()));
		passiLavSostegnoModel.setDatiOutput(mapper.writeValueAsString(datiPassoLavorazioneIstruttoria.getDatiOutput()));

		DatiSintesi datiSintesi = datiPassoLavorazioneIstruttoria.getDatiSintesi();
		List<EsitoControllo> esitiControlliout = datiSintesi.getEsitiControlli();

		passiLavSostegnoModel.setDatiSintesiLavorazione(mapper.writeValueAsString(datiPassoLavorazioneIstruttoria.getDatiSintesi()));

		// salvataggio anomalie per domanda
		Optional<TransizioneIstruttoriaModel> transizioneSostegnoModel = transizioneIstruttoriaDao.findById(datiPassoLavorazioneIstruttoria.getIdTransizione());
		if (transizioneSostegnoModel.isPresent()) {
			passiLavSostegnoModel.setTransizioneIstruttoria(transizioneSostegnoModel.get());
		}

		passiLavSostegnoModel = passiLavorazioneSostegnoDao.save(passiLavSostegnoModel);

		for (EsitoControllo esito : esitiControlliout) {
			if (esito.getEsito() != null) {
				A4gtAnomDomandaSostegno anom = new A4gtAnomDomandaSostegno();
				if (esito.getTipoControllo().equals(TipoControllo.BRIDUSDC021_idDomandaSanzioni) && !esito.getValString().equals("false")) {
					anom.setCodiceAnomalia(esito.getTipoControllo().getCodice().concat("_").concat(esito.getValString().toUpperCase()));
				} else if (esito.getTipoControllo().equals(TipoControllo.BRIDUSDC025_impegniGreening)) {
					anom.setCodiceAnomalia(esito.getTipoControllo().getCodice().concat("_").concat(esito.getValString().toUpperCase()));
				} else {
					anom.setCodiceAnomalia(esito.getTipoControllo().getCodice());
				}
				anom.setLivelloAnomalia(esito.getLivelloControllo().name());
				anom.setEsito(esito.getEsito() ? "SI" : "NO");
				anom.setPassoLavorazione(passiLavSostegnoModel);
				daoAnomDomandaSostegno.save(anom);
			}
		}
		return passiLavSostegnoModel;
	}
}
