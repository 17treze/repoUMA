package it.tndigitale.a4gistruttoria.component;

import java.math.BigDecimal;
import java.util.Map;

import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.TransizioneIstruttoriaService;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

public abstract class PassoCalcoloComponent {

	@Autowired
    DomandaUnicaDao daoDomanda;
	@Autowired
	TransizioneIstruttoriaService transizioneService;
	@Autowired
	ObjectMapper objectMapper;
	
	// Recupero l'importo
	public PassoTransizioneModel caricaPassoCalcoloValido(IstruttoriaModel istruttoria) throws Exception {
		TipologiaPassoTransizione passo = getPassoCalcolo();
		
		TransizioneIstruttoriaModel ultimaTransizioneUtile = transizioneService.caricaUltimaTransizione(istruttoria, StatoIstruttoria.CONTROLLI_CALCOLO_OK);
		return ultimaTransizioneUtile.getPassiTransizione().stream()
				.filter(p -> p.getCodicePasso().equals(passo))
				.findFirst()
				.orElse(null);
	}
	
	protected abstract TipologiaPassoTransizione getPassoCalcolo();
	
	protected void addImportoErogato(Map<TipoVariabile, BigDecimal> mappaImporti, TipoVariabile variabile, BigDecimal importo) {
		if (importo != null) {
			BigDecimal totale = mappaImporti.getOrDefault(variabile, BigDecimal.ZERO);
			mappaImporti.put(variabile, totale.add(importo));
		}
	}
}
