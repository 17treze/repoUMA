package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiPassoLavorazione;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.OpzioniImportoMinimo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaboraPassoIstruttoria;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Service
public class PassoImportoMinimoService extends ElaboraPassoIstruttoria {

	@Autowired
	private VerificaImportoMinimo verificaImporto;

	protected Function<EsitoControllo, OpzioniImportoMinimo> esitoImportoMinimo2statoSostegno = 
			controllo -> OpzioniImportoMinimo.valueOf(controllo.getValString());
	
	public OpzioniImportoMinimo elaboraPasso(List<VariabileCalcolo> variabiliInput, 
			DatiLavorazioneControlloIntersostegno dati) throws Exception {
		List<VariabileCalcolo> importoMinimoVariabiliInput = 
				new ArrayList<VariabileCalcolo>(variabiliInput);
		importoMinimoVariabiliInput.add(new VariabileCalcolo(TipoVariabile.IMPMINIMOLIQ, verificaImporto.getSoglia()));
		CodiceEsito esito = verificaImporto.apply(dati);
		EsitoControllo controllo = verificaImporto.trasformaEsitoInRisultatoEsito(esito);
		
		DatiPassoLavorazione passo = new DatiPassoLavorazione();
		passo.setIdTransizione(dati.getTransizione().getId());
		passo.setPasso(getPasso());
		passo.getDatiInput().setVariabiliCalcolo(importoMinimoVariabiliInput);
		passo.getDatiSintesi().getEsitiControlli().add(controllo);
		passo.setCodiceEsito(esito.getCodiceEsito());
		passo.setEsito(controllo.getEsito());
		salvaPassoLavorazioneSostegno(passo);
		
		return esitoImportoMinimo2statoSostegno.apply(controllo);
	}

	@Override
	public TipologiaPassoTransizione getPasso() {
		return TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO;
	}
	
}
