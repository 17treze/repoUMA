package it.tndigitale.a4gistruttoria.strategy;

import it.tndigitale.a4gistruttoria.dto.domandaunica.DichiarazioneDu;
import it.tndigitale.a4gistruttoria.repository.model.DichiarazioneDomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class DatiDichiarazioniAbstract extends DatiDettaglioAbstract {
	
	private static final String NO = "NO";
	private static final String SI = "SI";
	private static final String DATE_PATTERN = "dd/MM/yyyy";
	
	protected List<DichiarazioneDu> recuperaDichiarazioni(DomandaUnicaModel domanda) {
		List<DichiarazioneDu> dichiarazioni =
				domanda.getDichiarazioni().stream().filter(d -> getCodiciDichiarazioni().contains(d.getCodice())).map(d -> {
					DichiarazioneDu dichiarazione = new DichiarazioneDu();
					BeanUtils.copyProperties(d, dichiarazione);

					dichiarazione.setValore(getValoreDichiarazioneDu(d));

					// dichiarazione.setIdTipoDichiarazioneDu(d.getA4gdTipoDichiarazioneDu().getIdentificativo());
					// dichiarazione.setDescrizioneTipoDichiarazioneDu(d.getA4gdTipoDichiarazioneDu().getDescrizione());
					dichiarazione.setQuadro(d.getQuadro());
					
					return dichiarazione;
				}).collect(Collectors.toList());
		Collections.sort(dichiarazioni);
		return dichiarazioni;
	}
	
	protected abstract List<String> getCodiciDichiarazioni(); 

	private String getValoreDichiarazioneDu(DichiarazioneDomandaUnicaModel d) {
		String val = "";
		if (d.getValoreBool() != null) {
			val = d.getValoreBool().equals(BigDecimal.ZERO) ? NO : SI;
		} else if (d.getValoreData() != null) {
			DateFormat df = new SimpleDateFormat(DATE_PATTERN);
			val = df.format(d.getValoreData());
		} else if (d.getValoreStringa() != null && !d.getValoreStringa().isEmpty()) {
			val = d.getValoreStringa();
		} else if (d.getValoreNumero() != null) {
			val = d.getValoreNumero().toString();
		}
		return val;
	}
	
	@Override
	protected StatoIstruttoria adjustStatoLavorazioneSostegno(StatoIstruttoria stato) {
		return stato; // StatoLavorazioneSostegno ininfluente per il dettaglio dati dichiarazioni
	}
}
