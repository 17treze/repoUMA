package it.tndigitale.a4gistruttoria.service;


import java.util.Calendar;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.dto.RicevutaDomandaIntegrativaZootecnia;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.RicevutaDomandaIntegrativaZootecniaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtRicevutaDomIntZoot;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;

@Service
public class DomandaIntegrativaServiceImpl implements DomandaIntegrativaService {

	private static Logger logger = LoggerFactory.getLogger(DomandaIntegrativaServiceImpl.class);
	
	
	@Value("${zootecnia.interventi.vacchemontagna}")
	private String[] interventiCodiciVaccheLatteMontagna;
	@Value("${zootecnia.interventi.vacchenutricielatte}")
	private String[] interventiCodiciVaccheLatte;
	
	@Autowired
	private DomandaUnicaDao domandaUnicaDao;
	@Autowired
	private RicevutaDomandaIntegrativaZootecniaDao ricevutaDomandaDao;

	
	@Transactional
	@Override
	public RicevutaDomandaIntegrativaZootecnia salva(RicevutaDomandaIntegrativaZootecnia ricevutaDI) throws Exception {
		Long idDomanda = ricevutaDI.getIdDomanda();
		logger.debug("salva: ricevuta domanda integrativa per la domanda {}", idDomanda);
		DomandaUnicaModel domanda = domandaUnicaDao.findById(ricevutaDI.getIdDomanda()).orElseThrow(() -> new EntityNotFoundException(String.format("Nessuna domanda trovata per id: %d", idDomanda)));
		A4gtRicevutaDomIntZoot ricevutaDaAggiornare = ricevutaDomandaDao.findByDomandaUnicaModel(domanda);
		if (ricevutaDaAggiornare == null) {
			ricevutaDaAggiornare = new A4gtRicevutaDomIntZoot();
			ricevutaDaAggiornare.setDomandaUnicaModel(domanda);
		}
		logger.debug("salva: salvo la ricevuta {}", ricevutaDaAggiornare.getId());
		ricevutaDaAggiornare.setRicevuta(ricevutaDI.getRicevuta());
		ricevutaDaAggiornare.setDtUltimoAggiornamento(Calendar.getInstance().getTime());
		ricevutaDomandaDao.save(ricevutaDaAggiornare);
		return ricevutaDI;
	}

	@Transactional
	@Override
	public RicevutaDomandaIntegrativaZootecnia cancella(RicevutaDomandaIntegrativaZootecnia ricevutaDI) {
		Long idDomanda = ricevutaDI.getIdDomanda();
		logger.debug("cancella: ricevuta domanda integrativa per la domanda {}", idDomanda);
		DomandaUnicaModel domanda = domandaUnicaDao.findById(ricevutaDI.getIdDomanda()).orElseThrow(() -> new EntityNotFoundException(String.format("Nessuna domanda trovata per id: %d", idDomanda)));
		A4gtRicevutaDomIntZoot ricevutaDaCancellare = ricevutaDomandaDao.findByDomandaUnicaModel(domanda);
		if (ricevutaDaCancellare == null) {
			return null;
		}
		logger.debug("cancella: cancella la ricevuta {}", ricevutaDaCancellare.getId());
		ricevutaDomandaDao.deleteById(ricevutaDaCancellare.getId());
		return ricevutaDI;
	}
	
	@Override
	public byte[] getRicevutaDomandaIntegrativa(Long idDomanda) {

		A4gtRicevutaDomIntZoot ricevuta = ricevutaDomandaDao.findByDomandaUnicaModel_Id(idDomanda);
		if (ricevuta != null) {
			return ricevuta.getRicevuta();
		}
		throw new NoResultException("Nessuna ricevuta di domanda integrativa trovata per la domanda con id " + idDomanda);
	}

}
