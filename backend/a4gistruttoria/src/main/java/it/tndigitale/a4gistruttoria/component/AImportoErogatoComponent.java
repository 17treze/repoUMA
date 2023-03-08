package it.tndigitale.a4gistruttoria.component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

public abstract class AImportoErogatoComponent {

	private static final Logger logger = LoggerFactory.getLogger(AImportoErogatoComponent.class);

	@Autowired
	DomandaUnicaDao daoDomanda;

	@Autowired
	ObjectMapper objectMapper;

	public AImportoErogatoComponent() {
		super();
	}

	public Map<TipoVariabile, BigDecimal> calcolaSommePerIstruttoriePagate(DomandaUnicaModel domanda, TipoVariabile... variabili) throws Exception {
		logger.debug("calcolaSommePerIstruttoriePagate: inizio per la domanda {}", domanda.getNumeroDomanda());
		StatoIstruttoria statoIstruttoria = getStatoIstruttoria();
		logger.debug("calcolaSommePerIstruttoriePagate: inizio per la domanda {} e stato {}", domanda.getNumeroDomanda(), statoIstruttoria);
		Sostegno sostegno = getSostegno();
		logger.debug("calcolaSommePerIstruttoriePagate: inizio per la domanda {} e sostegno {}", domanda.getNumeroDomanda(), sostegno);
		List<IstruttoriaModel> istruttorieDomanda = domanda.getA4gtLavorazioneSostegnos()
			.stream()
			.filter(i -> sostegno.equals(i.getSostegno()))
			.filter(i -> statoIstruttoria.equals(i.getStato()))
			.collect(Collectors.toList());
		Map<TipoVariabile, BigDecimal> mappaImporti = new HashMap<TipoVariabile, BigDecimal>();
		PassoCalcoloComponent passoComp = getPassoCalcolo();
		for (IstruttoriaModel istruttoria : istruttorieDomanda) {
			logger.debug("calcolaSommePerIstruttoriePagate: per la domanda {} elaboro istruttoria {}", domanda.getNumeroDomanda(), istruttoria.getId());
			PassoTransizioneModel passoCalcolo =
					passoComp.caricaPassoCalcoloValido(istruttoria);			
			logger.debug("calcolaSommePerIstruttoriePagate: per la domanda {} leggo i dati del passo {}", domanda.getNumeroDomanda(), passoCalcolo.getId());
			DatiOutput output = objectMapper.readValue(passoCalcolo.getDatiOutput(), DatiOutput.class);
			for (TipoVariabile variabile : variabili) {
				addImportoErogato(mappaImporti, variabile, output.getVariabiliCalcolo().stream()
						.filter(p -> p.getTipoVariabile().equals(variabile))
						.findFirst()
						.map(v -> v.getValNumber()).orElse(null));
			}
			
		}
		return mappaImporti;
	}

	protected abstract Sostegno getSostegno();
	
	protected abstract PassoCalcoloComponent getPassoCalcolo();

	protected StatoIstruttoria getStatoIstruttoria() {
		return StatoIstruttoria.PAGAMENTO_AUTORIZZATO;
	}

	protected void addImportoErogato(Map<TipoVariabile, BigDecimal> mappaImporti, TipoVariabile variabile, BigDecimal importo) {
		logger.debug("addImportoErogato: variabile {} importo {}", variabile, importo);
		if (importo != null) {
			BigDecimal totale = mappaImporti.getOrDefault(variabile, BigDecimal.ZERO);
			mappaImporti.put(variabile, totale.add(importo));
		}
	}

}