package it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.A4gistruttoriaConfigurazione;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.lavorazione.Cuaa;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.repository.dao.DatiEredeDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDatiErede;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElencoLiquidazioneException;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.DatiLiquidazioneIstruttoria.Beneficiario;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.DatiLiquidazioneIstruttoria.Beneficiario.Sesso;
import it.tndigitale.a4gistruttoria.util.LocalDateConverter;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@Service
public class BeneficiarioLiquidazioneIstruttoriaService {


	private static final Logger logger = LoggerFactory.getLogger(BeneficiarioLiquidazioneIstruttoriaService.class);
	
	@Autowired
	protected TransizioneIstruttoriaDao daoTransizionesostegno;
	@Autowired
	protected ObjectMapper mapper;
	@Autowired
	private DatiEredeDao datiEredeDao;
	@Autowired
	private A4gistruttoriaConfigurazione configurazione;
	@Autowired
	private RestTemplate restTemplate;
	

	public Beneficiario creaDatiBeneficiario(IstruttoriaModel istruttoria) throws ElencoLiquidazioneException {
		if (esisteErede(istruttoria)) {
			return creaDatiBeneficiarioDaErede(istruttoria);
		}
		return caricaDatiTitolare(istruttoria);
	}
	
	protected Beneficiario caricaDatiTitolare(IstruttoriaModel istruttoria) throws ElencoLiquidazioneException {
		String cuaa = null;
		try {
			DomandaUnicaModel domandaUnicaModel = istruttoria.getDomandaUnicaModel();
			cuaa = domandaUnicaModel.getCuaaIntestatario();
			// Recupero informazioni Anagrafiche CUAA da AGS
			String resource = configurazione.getUriAgs().concat(ApiUrls.AGS_CUAA).concat("?cuaa=").concat(cuaa);
			Cuaa infoCuaa = restTemplate.getForObject(new URI(resource), Cuaa.class);
			Beneficiario beneficiario = new Beneficiario();
			beneficiario.setRagioneSociale(domandaUnicaModel.getRagioneSociale());
			beneficiario.setIban(domandaUnicaModel.getIban());
			beneficiario.setCodiceFiscale(infoCuaa.getCodiceFiscale());
			beneficiario.setCognome(infoCuaa.getCognome());
			beneficiario.setNome(infoCuaa.getNome());
			beneficiario.setSesso(Sesso.valueOfBySigla(infoCuaa.getSesso()));
			beneficiario.setDataNascita(LocalDateConverter.fromDate(infoCuaa.getDataNascita()));
			beneficiario.setIstatComuneNascita(infoCuaa.getCodiceIstatNascita());
			beneficiario.setSiglaProvinciaNascita(infoCuaa.getSiglaProvNacita());
			beneficiario.setIndirizzo(infoCuaa.getIndirizzoRecapito());
			beneficiario.setIstatComuneRecapito((infoCuaa.getCodiceIstatRecapito() != null) ? infoCuaa.getCodiceIstatRecapito().toString() : null);
			beneficiario.setSiglaProvinciaRecapito(infoCuaa.getSiglaProvRecapito());
			beneficiario.setCap(infoCuaa.getCap() != null ? infoCuaa.getCap().toString() : null);
			return beneficiario;
		} catch (RestClientException | URISyntaxException e) {
			logger.error("Errore caricando i dati del beneficiario dell'azienda {}", cuaa, e);
			throw new ElencoLiquidazioneException("Errore caricando i dati del beneficiario dell'azienda " + cuaa);
		}
		
	}
	
	protected Beneficiario creaDatiBeneficiarioDaErede(IstruttoriaModel istruttoria) throws ElencoLiquidazioneException {
		A4gtDatiErede datiErede = caricaDatiErede(istruttoria);
		Beneficiario beneficiario = null;
		
		if (datiErede != null) {
			beneficiario = new Beneficiario();
			beneficiario.setRagioneSociale(datiErede.getCognome() + " " + datiErede.getNome());
			beneficiario.setIban(datiErede.getIban());
			beneficiario.setCodiceFiscale(datiErede.getCodiceFiscale());
			beneficiario.setCognome(datiErede.getCognome());
			beneficiario.setNome(datiErede.getNome());
			beneficiario.setSesso(Sesso.valueOfBySigla(datiErede.getSesso()));
			beneficiario.setDataNascita(LocalDateConverter.fromDate(datiErede.getDtNascita()));
			beneficiario.setIstatComuneNascita(datiErede.getCodIstatNascita());
			beneficiario.setSiglaProvinciaNascita(datiErede.getProvinciaNascita());
			beneficiario.setIndirizzo(datiErede.getIndirizzo());
			beneficiario.setIstatComuneRecapito(datiErede.getCodiceIstat());
			beneficiario.setSiglaProvinciaRecapito(datiErede.getProvincia());
			beneficiario.setCap(datiErede.getCap());	
		}

		return beneficiario;
	}
	
	protected A4gtDatiErede caricaDatiErede(IstruttoriaModel istruttoria) throws ElencoLiquidazioneException {
		return datiEredeDao.findByDomandaUnicaModel(istruttoria.getDomandaUnicaModel());
	}

	protected boolean esisteErede(IstruttoriaModel istruttoria) throws ElencoLiquidazioneException {
		TransizioneIstruttoriaModel transizione = null;
		List<TransizioneIstruttoriaModel> transizioniLiquidabilita = 
				daoTransizionesostegno.findTransizioneLiquidabilita(istruttoria);
		if (transizioniLiquidabilita.isEmpty()) {
			StringBuilder messaggioErrore = new StringBuilder("Impossibile recuperare la Transizione Liquidabilita ");
			messaggioErrore.append("per l'istruttoria ");
			messaggioErrore.append(istruttoria.getId().toString());
			logger.error("Impossibile recuperare la transizione di liquidabilita per l'istruttoria {}", istruttoria.getId());
			throw new ElencoLiquidazioneException(messaggioErrore.toString());
		} else {
			transizione = transizioniLiquidabilita.stream()
					.sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed())
					.findFirst().get();
		}
		PassoTransizioneModel passo = transizione.getPassiTransizione()
					.stream()
					.filter(p -> TipologiaPassoTransizione.LIQUIDABILITA.equals(p.getCodicePasso()))
					.findFirst().get();
		DatiSintesi sintesi = null;
		try {
			sintesi = mapper.readValue(passo.getDatiSintesiLavorazione(), DatiSintesi.class);
		} catch (IOException e) {
			logger.error("Errore leggendo i dati di sintesi del pass {}", passo.getId(), e);
		}
		Optional<EsitoControllo> esitoErede = sintesi.getEsitiControlli()
				.stream()
				.filter(esito -> esito.getTipoControllo().equals(TipoControllo.BRIDUNVL129_erede))
				.findFirst()
				.filter(esito -> Boolean.TRUE.equals(esito.getEsito()));
		return esitoErede.isPresent();
	}
}
