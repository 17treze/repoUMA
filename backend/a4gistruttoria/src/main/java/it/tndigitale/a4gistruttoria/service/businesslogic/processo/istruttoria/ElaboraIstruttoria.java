package it.tndigitale.a4gistruttoria.service.businesslogic.processo.istruttoria;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.client.ResourceAccessException;

import it.tndigitale.a4gistruttoria.dto.DatiElaborazioneProcesso;
import it.tndigitale.a4gistruttoria.dto.istruttoria.InputProcessoIstruttorieDto;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.IstruttoriaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;
import it.tndigitale.a4gistruttoria.service.businesslogic.processo.ElaboraProcesso;

public abstract class ElaboraIstruttoria  extends ElaboraProcesso<InputProcessoIstruttorieDto, DatiElaborazioneProcesso> {

	private static final Logger logger = LoggerFactory.getLogger(ElaboraIstruttoria.class);

	@Autowired
	private IstruttoriaService istruttoriaService;

    @Override
	protected void elabora(InputProcessoIstruttorieDto datiInputProcesso, DatiElaborazioneProcesso datiProcesso, Long idProcesso) throws ElaborazioneIstruttoriaException {
		List<Long> idIstruttorie = datiInputProcesso.getIdIstruttorie();
		elaboraIstruttorie(datiProcesso, idIstruttorie, idProcesso);
    }
	
	@Override
	protected void inizializzaDatiProcesso(InputProcessoIstruttorieDto datiInputProcesso, DatiElaborazioneProcesso datiProcesso) {
		List<Long> idIstruttorie = datiInputProcesso.getIdIstruttorie();
		int numeroIstruttorie = idIstruttorie.size();

		datiProcesso.setTotale("" + numeroIstruttorie);
	}

	@Override
	protected DatiElaborazioneProcesso istanziaDatiProcesso() {
		return new DatiElaborazioneProcesso();
	}
	
	protected void elaboraIstruttorie(DatiElaborazioneProcesso datiProcesso,
			List<Long> idIstruttorie,
			Long idProcesso) {
		ElaborazioneIstruttoria managerElaborazione = getElaborazioneIstruttoriaService();
		List<String> istruttorieConProblemi = new ArrayList<>();
		List<String> istruttorieGestite = new ArrayList<>();
		Long idIstruttoria = null;
		int numeroIstruttorie = idIstruttorie.size();
		for (int i = 0; i < numeroIstruttorie; i++) {
			try {
				idIstruttoria = idIstruttorie.get(i);
				if (isIstruttoriaLavorabile(idIstruttoria)) {

					managerElaborazione.elabora(idIstruttoria);

					istruttorieGestite.add(idIstruttoria.toString());
					datiProcesso.setGestite(istruttorieGestite);
					istruttoriaService.aggiornaErrore(idIstruttoria, false, "");
				}

			} catch (ElaborazioneIstruttoriaException | ResourceAccessException  | UnexpectedRollbackException e) {
				istruttoriaInErrore(idIstruttoria, e);
				istruttorieConProblemi.add(idIstruttoria.toString());
				datiProcesso.setConProblemi(istruttorieConProblemi);
				logger.error("Elaborazione istruttoria " + idIstruttoria + " termiata con errore", e);
			}
			int avanzamento = (i * 100) / numeroIstruttorie;
			datiProcesso.setParziale(String.valueOf(i));
			avanzamentoProcesso(idProcesso, avanzamento, datiProcesso);
		}
	}
	
	protected void istruttoriaInErrore(Long idIstruttoria, Exception e) {
		istruttoriaService.aggiornaErrore(idIstruttoria, true, e.getMessage());
	}
	
	protected void processoConclusoConSuccesso(InputProcessoIstruttorieDto datiInputProcesso, DatiElaborazioneProcesso datiProcesso) {
		datiProcesso.setParziale(datiProcesso.getTotale());
		super.processoConclusoConSuccesso(datiInputProcesso, datiProcesso);
	}
	
	protected boolean isIstruttoriaLavorabile(Long idIstruttoria) {
		return !istruttoriaService.isIstruttoriaBloccata(idIstruttoria);
	}
	
	protected abstract ElaborazioneIstruttoria getElaborazioneIstruttoriaService();
}
