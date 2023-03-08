package it.tndigitale.a4g.uma.business.service.protocollo;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.uma.GeneralFactory;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.service.consumi.DichiarazioneConsumiValidator;
import it.tndigitale.a4g.uma.business.service.richiesta.RicercaRichiestaCarburanteService;
import it.tndigitale.a4g.uma.business.service.richiesta.RichiestaCarburanteValidator;
import it.tndigitale.a4g.uma.dto.protocollo.TipoDocumentoUma;

@Service
public class ProtocolloService {

	private static final Logger logger = LoggerFactory.getLogger(ProtocolloService.class);

	@Autowired
	private GeneralFactory protocollazioneFactory;
	@Autowired
	private RicercaRichiestaCarburanteService ricercaRichiestaCarburanteService;
	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;
	@Autowired
	private RichiestaCarburanteValidator richiestaCarburanteValidator;
	@Autowired
	private DichiarazioneConsumiValidator dichiarazioneConsumiValidator;

	@Transactional
	public void protocollaRichiesta(Long id, ByteArrayResource documento, boolean haFirma) {
		RichiestaCarburanteModel richiesta = richiestaCarburanteDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Richiesta con id: ".concat(String.valueOf(id)).concat("non trovata")));
		richiestaCarburanteValidator.validaProtocollazione(richiesta);
		Optional<Long> idRettificata = ricercaRichiestaCarburanteService.getIdRettificata(richiesta.getCuaa(), richiesta.getCampagna(), richiesta.getDataPresentazione());
		TipoDocumentoUma tipoDocumento = idRettificata.isPresent() ? TipoDocumentoUma.RETTIFICA : TipoDocumentoUma.RICHIESTA;
		logger.info("Avvio iter protocollazione domanda {} - {}" , id, tipoDocumento);
		ProtocollazioneStrategy protocollazioneStrategy = protocollazioneFactory.getProtocollazioneStrategy("PROTOCOLLA_" + tipoDocumento.name());
		protocollazioneStrategy.avviaProtocollo(id, documento, haFirma);
	}

	@Transactional
	public void protocollaDichiarazione(Long id, ByteArrayResource documento, boolean haFirma) {
		DichiarazioneConsumiModel dichiarazione = dichiarazioneConsumiDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Dichiarazione con id: ".concat(String.valueOf(id)).concat("non trovata")));
		dichiarazioneConsumiValidator.validaProtocollazione(dichiarazione);
		logger.info("Avvio iter protocollazione domanda {} - {}" , id, TipoDocumentoUma.DICHIARAZIONE_CONSUMI);
		ProtocollazioneStrategy protocollazioneStrategy = protocollazioneFactory.getProtocollazioneStrategy("PROTOCOLLA_" + TipoDocumentoUma.DICHIARAZIONE_CONSUMI.name());
		protocollazioneStrategy.avviaProtocollo(id, documento, haFirma);
	}
}
