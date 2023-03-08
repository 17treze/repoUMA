package it.tndigitale.a4gistruttoria.action.cup;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import it.tndigitale.a4gistruttoria.cup.dto.CODICETIPOLOGIACOPFINANZ;
import it.tndigitale.a4gistruttoria.cup.dto.CUPGENERAZIONE;
import it.tndigitale.a4gistruttoria.cup.dto.FINANZIAMENTO;
import it.tndigitale.a4gistruttoria.cup.dto.ObjectFactory;
import it.tndigitale.a4gistruttoria.dto.cup.DatiCUP;

@Component
public class PopolaFinanziamentoConsumer implements BiConsumer<CupHandler, CUPGENERAZIONE> {

	private static final Logger logger = LoggerFactory.getLogger(PopolaFinanziamentoConsumer.class);	

	private final Map<String, String>  mapCodiciTipologiaCopertura = new HashMap<String, String>();
	
	
	private PopolaFinanziamentoConsumer() {
		super();
		mapCodiciTipologiaCopertura.put("STATALE", "001");
		mapCodiciTipologiaCopertura.put("REGIONALE", "002");
		mapCodiciTipologiaCopertura.put("PROVINCIALE", "003");
		mapCodiciTipologiaCopertura.put("COMUNALE", "004");
		mapCodiciTipologiaCopertura.put("COMUNITARIA", "006");
		mapCodiciTipologiaCopertura.put("PRIVATA", "007");
	}

	@Override
	public void accept(CupHandler handler, CUPGENERAZIONE generazione) {
		DatiCUP datiCupInput = handler.getDatiCUP();
		ObjectFactory of = new ObjectFactory();
		FINANZIAMENTO f = of.createFINANZIAMENTO();
		double importo = Double.parseDouble(formatImporto(datiCupInput.getTotaleImportoRichiesto()));
		f.setCosto(new BigDecimal("" + Math.round(importo)).divide(new BigDecimal("1000")).setScale(3).toString());
		importo = Double.parseDouble(formatImporto(datiCupInput.getContributoRichiesto()));
		f.setFinanziamento(new BigDecimal("" + Math.round(importo)).divide(new BigDecimal("1000")).setScale(3).toString());
		String tipologiaCopertura = datiCupInput.getTipologiaCopertura();
		if (!StringUtils.isEmpty(tipologiaCopertura)) {
			List<String> coperture =
					Collections.list(new StringTokenizer(tipologiaCopertura, ";"))
							.stream()
							.map(token -> (String) token)
							.map(String::toUpperCase)
							.map(codice -> mapCodiciTipologiaCopertura.get(codice))
							.collect(Collectors.toList());

			for (String codiceTip : coperture) {
				CODICETIPOLOGIACOPFINANZ codice = of.createCODICETIPOLOGIACOPFINANZ();
				codice.setvalue(codiceTip);
				f.getCODICETIPOLOGIACOPFINANZ().add(codice);
			}
		}
		
		generazione.setFINANZIAMENTO(f);
	} 
	
	protected String formatImporto(String importo) {
		logger.debug("importo da formattare: " + importo);
		return importo.replaceAll("\\.", "").replaceAll(",", ".");
	}
}
