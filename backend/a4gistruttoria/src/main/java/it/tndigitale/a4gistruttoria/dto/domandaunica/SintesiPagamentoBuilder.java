package it.tndigitale.a4gistruttoria.dto.domandaunica;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.CaricaPremioCalcolatoSostegno;
import it.tndigitale.a4gistruttoria.component.CaricaPremioCalcolatoSostegnoFactory;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class SintesiPagamentoBuilder {

	private static final Logger logger = LoggerFactory.getLogger(SintesiPagamentoBuilder.class);

	@Autowired
	private CaricaPremioCalcolatoSostegnoFactory premioCalcolatoSostegnoFactory;
	
	private Map<Sostegno, TipoVariabile> mappaVariabilePerSostegno = new HashMap<Sostegno, TipoVariabile>();
	
	
	
	public SintesiPagamentoBuilder() {
		super();
		mappaVariabilePerSostegno.put(Sostegno.DISACCOPPIATO, TipoVariabile.IMPCALCFINLORDO);
		mappaVariabilePerSostegno.put(Sostegno.ZOOTECNIA, TipoVariabile.ACZIMPCALCLORDOTOT);
		mappaVariabilePerSostegno.put(Sostegno.SUPERFICIE, TipoVariabile.ACSIMPCALCLORDOTOT);
	}

	public SintesiPagamento from(DomandaUnicaModel domandaUnicaModel) {

		// se il sostegno ha un pagamento autorizzato e per questo si vanno a recuperare le variabili.
		return new SintesiPagamento(calcolaImportoLordo(domandaUnicaModel), null);
	}
	
	private Double calcolaImportoLordo(DomandaUnicaModel domanda) {
		// prendo tutte le istruttorie della domanda
		Set<IstruttoriaModel> istruttorieDomande = domanda.getA4gtLavorazioneSostegnos(); 

		BigDecimal result = BigDecimal.ZERO;
		BigDecimal totSostegno = BigDecimal.ZERO;
		// per ogni sostegno
		for (Sostegno sostegno : Sostegno.values()) {
			totSostegno = istruttorieDomande.stream()
				// voglio le istruttorie PAGATE del mio sostegno 
				.filter(ist -> sostegno.equals(ist.getSostegno()))
				.filter(ist -> StatoIstruttoria.PAGAMENTO_AUTORIZZATO.equals(ist.getStato()))
				// MI INTERESSA LA PIU' RECENTE (PAGATA PER SOSTEGNO) -> di questa leggo l'importo lordo calcolato
				.max(Comparator.comparingLong(IstruttoriaModel::getId))
				.map(istruttoria -> recuperaImportoCalcolatoLordoBySostegno(istruttoria))
				.orElse(BigDecimal.ZERO);
			result = result.add(totSostegno);
		}
		return result.doubleValue();
	}
	
	private BigDecimal recuperaImportoCalcolatoLordoBySostegno(IstruttoriaModel istruttoria) {
		Sostegno sostegno = istruttoria.getSostegno();
		CaricaPremioCalcolatoSostegno premioLoader =
				premioCalcolatoSostegnoFactory.getCaricatorePremioBySostegno(CaricaPremioCalcolatoSostegno.getNomeQualificatore(sostegno));
		
		TipoVariabile variabileLordo = mappaVariabilePerSostegno.get(sostegno);
		return premioLoader.getImportoPremioCalcolato(istruttoria, variabileLordo)
			.getOrDefault(variabileLordo, BigDecimal.ZERO);
	}
}
