package it.tndigitale.a4gistruttoria.service.businesslogic.processo.domanda;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.tndigitale.a4gistruttoria.dto.DatiElaborazioneProcesso;
import it.tndigitale.a4gistruttoria.dto.ProcessoAnnoCampagnaDomandaDto;
import it.tndigitale.a4gistruttoria.service.businesslogic.domanda.ElaborazioneDomanda;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneDomandaException;
import it.tndigitale.a4gistruttoria.service.businesslogic.processo.ElaboraProcesso;

public abstract class ElaboraDomanda extends ElaboraProcesso<ProcessoAnnoCampagnaDomandaDto, DatiElaborazioneProcesso> {
    private static final Logger logger = LoggerFactory.getLogger(ElaboraDomanda.class);

	protected void processoConclusoConSuccesso(ProcessoAnnoCampagnaDomandaDto datiInputProcesso, DatiElaborazioneProcesso datiProcesso) {
		datiProcesso.setParziale(datiProcesso.getTotale());
		super.processoConclusoConSuccesso(datiInputProcesso, datiProcesso);
	}
    
    @Override
	protected void elabora(ProcessoAnnoCampagnaDomandaDto datiInputProcesso, DatiElaborazioneProcesso datiProcesso, Long idProcesso) throws ElaborazioneDomandaException {
        List<Long> idDomandeProtocollate = getIdDomandeDaElaborare(datiInputProcesso.getCampagna());
        logger.debug("Lista domande recuperate");

        int numeroDomande = 0;
        if (idDomandeProtocollate != null) {
            numeroDomande = idDomandeProtocollate.size();
        }
        logger.debug("trovate {} da elaborare ", numeroDomande);
        datiProcesso.setTotale(String.valueOf(numeroDomande));

        ArrayList<String> domandeGestite = new ArrayList<>();
        ArrayList<String> domandeConProblemi = new ArrayList<>();

        avanzamentoProcesso(datiInputProcesso.getIdProcesso(), 0, datiProcesso);
        ElaborazioneDomanda managerElaborazione = getElaborazioneDomandaService();

        for (int i = 0; i < numeroDomande; i++) {
        	Long idDomanda = null;
            try {
                idDomanda = idDomandeProtocollate.get(i);
                logger.debug("Elaboro domanda {} ", idDomanda);
                managerElaborazione.elabora(idDomanda);

                domandeGestite.add(idDomanda.toString());
                datiProcesso.setGestite(domandeGestite);
            } catch (Exception e) {
                domandeConProblemi.add(String.valueOf(idDomanda));
                datiProcesso.setConProblemi(domandeConProblemi);
                logger.error("Elaborazione domanda " + idDomanda + " termiata con errore", e);
            }
			int avanzamento = (i * 100) / numeroDomande;
			datiProcesso.setParziale(String.valueOf(i));
			avanzamentoProcesso(idProcesso, avanzamento, datiProcesso);
        }
	}

	@Override
	protected void inizializzaDatiProcesso(ProcessoAnnoCampagnaDomandaDto datiInputProcesso, DatiElaborazioneProcesso datiProcesso) {
	}

	@Override
	protected DatiElaborazioneProcesso istanziaDatiProcesso() {
		return new DatiElaborazioneProcesso();
	}

	public List<Long> getIdDomandeDaElaborare(Integer campagna) throws ElaborazioneDomandaException {
    	ElaborazioneDomanda managerElaborazione = getElaborazioneDomandaService();
    	return managerElaborazione.caricaIdDaElaborare(campagna);
    }

	protected abstract ElaborazioneDomanda getElaborazioneDomandaService();
}
