package it.tndigitale.a4gistruttoria.builder;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

public class DomandaUnicaModelBuilder {
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	// transizione sostegno
	public static TransizioneIstruttoriaModel buildTransizioneSostegno(
			IstruttoriaModel istruttoria, 
			StatoIstruttoria statoLavorazioneSostegno,
			Set<PassoTransizioneModel> passi) {
		TransizioneIstruttoriaModel transizioneIstruttoria = new TransizioneIstruttoriaModel();
		transizioneIstruttoria.setIstruttoria(istruttoria);
		transizioneIstruttoria.setDataEsecuzione(new Date());
		transizioneIstruttoria.setA4gdStatoLavSostegno1(buildStatoLavorazioneSostegno(statoLavorazioneSostegno));
		transizioneIstruttoria.setPassiTransizione(passi);
		return transizioneIstruttoria;
	}

	// passi lavorazione sostegno 
	public static PassoTransizioneModel buildPassoLavorazione(
            TipologiaPassoTransizione tipologiaPasso, String datiSintesi, String datiOutput) {

		PassoTransizioneModel passoTransizione = new PassoTransizioneModel();
		passoTransizione.setCodicePasso(tipologiaPasso);
		passoTransizione.setDatiSintesiLavorazione(datiSintesi);
		passoTransizione.setDatiOutput(datiOutput);
		return passoTransizione;
	}

	// lavorazione sostegno 
	public static IstruttoriaModel buildLavorazioneSostegno(
			DomandaUnicaModel domandaUnicaModel, Sostegno sostegno,
			StatoIstruttoria statoLavorazioneSostegno, TipoIstruttoria tipoIstruttoria) {
		IstruttoriaModel istruttoriaModel = new IstruttoriaModel();
		istruttoriaModel.setDomandaUnicaModel(domandaUnicaModel);
		istruttoriaModel.setSostegno(sostegno);
		istruttoriaModel.setTipologia(tipoIstruttoria);
		istruttoriaModel.setA4gdStatoLavSostegno(buildStatoLavorazioneSostegno(statoLavorazioneSostegno));
		return istruttoriaModel;
	}


	// stato lavorazione sostegno 
	public static A4gdStatoLavSostegno buildStatoLavorazioneSostegno(StatoIstruttoria statoLavorazioneSostegno) {
		A4gdStatoLavSostegno a4gdStatoLavSostegno = new A4gdStatoLavSostegno();
		a4gdStatoLavSostegno.setIdentificativo(statoLavorazioneSostegno.getStatoIstruttoria());
		return a4gdStatoLavSostegno;
	}
	
	// dati Sintesi
	public static String buildDatiSintesi(List<VariabileCalcolo> variabiliCalcolo, List<EsitoControllo> esitiControlli) throws JsonProcessingException {
		DatiSintesi datiSintesi = new DatiSintesi();
		datiSintesi.setVariabiliCalcolo(variabiliCalcolo);
		datiSintesi.setEsitiControlli(esitiControlli);
		datiSintesi.setVariabiliParticellaColtura(null);
		return objectMapper.writeValueAsString(datiSintesi);
		
	}
	
	// dati output
	public static String buildDatiOutput(List<VariabileCalcolo> variabiliCalcolo) throws JsonProcessingException {
		DatiOutput datiOutput = new DatiOutput();
		datiOutput.setVariabiliCalcolo(variabiliCalcolo);
		return objectMapper.writeValueAsString(datiOutput);
	}
}
