package it.tndigitale.a4gistruttoria.dto.domandaunica;

import it.tndigitale.a4gistruttoria.dto.EsitoControlloBuilder;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class IstruttoriaBuilder {

	private IstruttoriaBuilder() {}

	public static List<Istruttoria> from(DomandaUnicaModel domandaUnicaModel) {
		List<Istruttoria> istruttorie = new ArrayList<>();
		
		domandaUnicaModel.getA4gtLavorazioneSostegnos()
		.stream()
		.forEach(ist -> {
			Istruttoria istruttoria = new Istruttoria();
			istruttoria.setSostegno(ist.getSostegno());
			istruttoria.setTipoIstruttoria(ist.getTipologia());
			istruttoria.setStatoLavorazioneSostegno(ist.getStato());

			List<TransizioneIstruttoriaModel> recuperaTransizioniSostegno = TransizioniHelper.recuperaTransizioniSostegno(ist);
			istruttoria.setEsitiControlli(EsitoControlloBuilder.from(recuperaTransizioniSostegno));

			// assegna a data ultimo calcolo il valore più recente tra i sostegni trovati
			if (!recuperaTransizioniSostegno.isEmpty()) {
				Date dtUltimoCalcolo = recuperaTransizioniSostegno
						.stream()
						.filter(x -> x.getDataEsecuzione() != null)
						.max(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione))
						.orElse(recuperaTransizioniSostegno.get(0))
						.getDataEsecuzione();			
				LocalDateTime dtUltimoCalcoloLocalDate = Instant.ofEpochMilli(dtUltimoCalcolo.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
				istruttoria.setDtUltimoCalcolo(dtUltimoCalcoloLocalDate);
			}
			
			// recupera importi calcolati
			istruttoria.setImportiIstruttoria(ImportiIstruttoriaBuilder.from(recuperaTransizioniSostegno, ist));
			
			istruttorie.add(istruttoria);
		});
		return istruttorie;
	}
}
