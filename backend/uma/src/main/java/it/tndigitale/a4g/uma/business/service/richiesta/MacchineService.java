package it.tndigitale.a4g.uma.business.service.richiesta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.UtilizzoMacchinariModel;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.persistence.repository.UtilizzoMacchinariDao;
import it.tndigitale.a4g.uma.dto.richiesta.MacchinaDto;
import it.tndigitale.a4g.uma.dto.richiesta.builder.MacchinaUmaBuilder;

@Service
public class MacchineService {

	private static final Logger logger = LoggerFactory.getLogger(MacchineService.class);
	private static final String UMA_01_02_05_BR3_ERR_MSG = "Per poter procedere è necessario attivare almeno una macchina";

	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private UtilizzoMacchinariDao utilizzoMacchinariDao;

	// salva o aggiorna i macchinari dichiarati in domanda
	public List<MacchinaDto> dichiaraMacchinari(Long idRichiesta, List<MacchinaDto> macchine) {
		RichiestaCarburanteModel richiestaCarburanteModel = richiestaCarburanteDao.findById(idRichiesta).orElseThrow(() -> new EntityNotFoundException(String.format("Nessuna Richiesta con id %s trovata", idRichiesta)));

		List<UtilizzoMacchinariModel> macchinariUma = utilizzoMacchinariDao.findByRichiestaCarburante(richiestaCarburanteModel);

		// ci deve essere almeno una macchina utilizzata
		boolean noMacchineAttivate = macchine.stream().filter(macchina -> Boolean.TRUE.equals(macchina.getIsUtilizzata())).collect(Collectors.toList()).isEmpty();
		if (noMacchineAttivate) {
			logger.error(UMA_01_02_05_BR3_ERR_MSG);
			throw new IllegalArgumentException(UMA_01_02_05_BR3_ERR_MSG);
		}

		// cancella i macchinari salvati
		utilizzoMacchinariDao.deleteAll(macchinariUma);

		// salva i nuovi macchinari
		List<UtilizzoMacchinariModel> macchineDaSalvare = new ArrayList<>();
		macchine.forEach(macchina -> {
			UtilizzoMacchinariModel macchinaModel = new UtilizzoMacchinariModel()
					.setAlimentazione(macchina.getAlimentazione())
					.setClasse(macchina.getClasse())
					.setDescrizione(macchina.getDescrizione())
					.setFlagUtilizzo(macchina.getIsUtilizzata())
					.setIdentificativoAgs(macchina.getIdentificativoAgs())
					.setMarca(macchina.getMarca())
					.setPossesso(macchina.getPossesso())
					.setTarga(macchina.getTarga())
					.setRichiestaCarburante(richiestaCarburanteModel);
			macchineDaSalvare.add(macchinaModel);
		});
		List<UtilizzoMacchinariModel> utilizzoMacchinariModel = utilizzoMacchinariDao.saveAll(macchineDaSalvare);
		logger.info("[Macchine UMA] - Salvataggio macchine richiesta {}", idRichiesta);
		return new MacchinaUmaBuilder().from(utilizzoMacchinariModel);
	}

	// get macchine presenti in a4g.
	public List<MacchinaDto> getMacchinari(Long idRichiesta) {
		RichiestaCarburanteModel richiestaCarburanteModel = richiestaCarburanteDao.findById(idRichiesta).orElseThrow(() -> new EntityNotFoundException(String.format("Nessuna Richiesta con id %s trovata", idRichiesta)));
		List<UtilizzoMacchinariModel> macchinariUma = utilizzoMacchinariDao.findByRichiestaCarburante(richiestaCarburanteModel);

		if (macchinariUma.isEmpty()) { return new ArrayList<>(); } 
		return new MacchinaUmaBuilder().from(macchinariUma);
	}
}
