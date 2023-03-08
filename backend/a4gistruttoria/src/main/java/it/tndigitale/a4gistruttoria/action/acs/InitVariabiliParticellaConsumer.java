package it.tndigitale.a4gistruttoria.action.acs;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.dto.InformazioniColtivazione;
import it.tndigitale.a4gistruttoria.dto.Particella;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ParticellaColtura;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.RichiestaSuperficieDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtRichiestaSuperficie;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@Component
public class InitVariabiliParticellaConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {


	@Autowired
	private RichiestaSuperficieDao daoRichiestaSuperficie;

	@Autowired
	private ObjectMapper objectMapper;

	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliParticellaConsumer.class);

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("Carico le superfici impegnate {}", domanda.getId());
		MapVariabili inputListaVariabiliCalcolo = handler.getVariabiliInput();
		inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.PFSUPIMP_M8, new ArrayList<ParticellaColtura>(
				getParticelleSuperficieImpegnataByMisura(domanda, CodiceInterventoAgs.SOIA))));
		inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.PFSUPIMP_M9, new ArrayList<ParticellaColtura>(
				getParticelleSuperficieImpegnataByMisura(domanda, CodiceInterventoAgs.GDURO))));
		inputListaVariabiliCalcolo
				.add(new VariabileCalcolo(TipoVariabile.PFSUPIMP_M10, new ArrayList<ParticellaColtura>(
						getParticelleSuperficieImpegnataByMisura(domanda, CodiceInterventoAgs.CPROT))));
		inputListaVariabiliCalcolo
				.add(new VariabileCalcolo(TipoVariabile.PFSUPIMP_M11, new ArrayList<ParticellaColtura>(
						getParticelleSuperficieImpegnataByMisura(domanda, CodiceInterventoAgs.LEGUMIN))));
		inputListaVariabiliCalcolo
				.add(new VariabileCalcolo(TipoVariabile.PFSUPIMP_M14, new ArrayList<ParticellaColtura>(
						getParticelleSuperficieImpegnataByMisura(domanda, CodiceInterventoAgs.POMOD))));
		inputListaVariabiliCalcolo
				.add(new VariabileCalcolo(TipoVariabile.PFSUPIMP_M15, new ArrayList<ParticellaColtura>(
						getParticelleSuperficieImpegnataByMisura(domanda, CodiceInterventoAgs.OLIO))));
		inputListaVariabiliCalcolo
				.add(new VariabileCalcolo(TipoVariabile.PFSUPIMP_M16, new ArrayList<ParticellaColtura>(
						getParticelleSuperficieImpegnataByMisura(domanda, CodiceInterventoAgs.OLIVE_PEND75))));
		inputListaVariabiliCalcolo
				.add(new VariabileCalcolo(TipoVariabile.PFSUPIMP_M17, new ArrayList<ParticellaColtura>(
						getParticelleSuperficieImpegnataByMisura(domanda, CodiceInterventoAgs.OLIVE_DISC))));
	}

	protected List<ParticellaColtura> getParticelleSuperficieImpegnataByMisura(DomandaUnicaModel domanda,
                                                                               CodiceInterventoAgs codiceInterventoAgs) {
		List<ParticellaColtura> toReturn = new ArrayList<>();
		List<A4gtRichiestaSuperficie> result = daoRichiestaSuperficie.findByDomandaIntervento(domanda.getId(),
				codiceInterventoAgs);
		for (A4gtRichiestaSuperficie item : result) {
			ParticellaColtura pc = new ParticellaColtura();
			try {
				pc.setParticella(objectMapper.readValue(item.getInfoCatastali(), Particella.class));
			} catch (IOException e) {
				String errorMessage = "Errore nel recupero delle informazioni catastali di input per la domanda "
						+ domanda.getNumeroDomanda();
				logger.error(errorMessage, e);
			}
			pc.setColtura(item.getCodiceColtura3());

			if (toReturn.contains(pc)) {
				pc = toReturn.get(toReturn.indexOf(pc));
			} else {
				// Recupero Info coltivazione particella
				InformazioniColtivazione infoColt = new InformazioniColtivazione();
				try {
					infoColt = objectMapper.readValue(item.getInfoColtivazione(), InformazioniColtivazione.class);
					pc.setDescColtura(infoColt.getDescrizioneColtura());
					pc.setLivello(infoColt.getCodLivello());
				} catch (IOException e) {
					String errorMessage = "Errore nel recupero delle informazioni di coltivazione di input per la domanda "
							+ domanda.getNumeroDomanda();
					logger.error(errorMessage, e);
				}
				pc.setValNum(0F);
				toReturn.add(pc);
			}

			// Prendo la superficie richiesta *NETTA*
			pc.setValNum((item.getSupRichiestaNetta().add(new BigDecimal(Float.toString(pc.getValNum()))).floatValue()));
		}
		return toReturn;
	}
}
