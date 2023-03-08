package it.tndigitale.a4gistruttoria.service.support;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import it.tndigitale.a4g.soc.client.model.ImportoLiquidato;
import it.tndigitale.a4gistruttoria.dto.EsitoControlloBuilder;
import it.tndigitale.a4gistruttoria.dto.domandaunica.DebitoBuilder;
import it.tndigitale.a4gistruttoria.dto.domandaunica.ImportiIstruttoria;
import it.tndigitale.a4gistruttoria.dto.domandaunica.ImportiIstruttoriaBuilder;
import it.tndigitale.a4gistruttoria.dto.domandaunica.Istruttoria;
import it.tndigitale.a4gistruttoria.dto.domandaunica.TransizioniHelper;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;

/**
 * Usata dai servizi mobile per recuperare tutte le informazioni da visualizzare, tipo debiti, anomalie e importi
 * @author s.caccia
 *
 */
public class ConsultazioneIstruttoriaSupport {
	
	public static Consumer<? super IstruttoriaModel> creaDtoIstruttoria(List<ImportoLiquidato> impLiquidati,List<Istruttoria> results){
		return (istruttoria) -> {
			Istruttoria istruttoriaOutput = new Istruttoria();
			istruttoriaOutput.setId(istruttoria.getId());
			istruttoriaOutput.setSostegno(istruttoria.getSostegno());
			istruttoriaOutput.setTipoIstruttoria(istruttoria.getTipologia());
			istruttoriaOutput.setStatoLavorazioneSostegno(istruttoria.getStato());

			List<TransizioneIstruttoriaModel> recuperaTransizioniSostegno = TransizioniHelper.recuperaTransizioniSostegno(istruttoria);
			istruttoriaOutput.setEsitiControlli(EsitoControlloBuilder.from(recuperaTransizioniSostegno));

			// assegna a data ultimo calcolo il valore più recente tra i sostegni trovati
			if (!recuperaTransizioniSostegno.isEmpty()) {
				Date dtUltimoCalcolo = recuperaTransizioniSostegno
						.stream()
						.filter(x -> x.getDataEsecuzione() != null)
						.max(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione))
						.orElse(recuperaTransizioniSostegno.get(0))
						.getDataEsecuzione();			
				LocalDateTime dtUltimoCalcoloLocalDate = Instant.ofEpochMilli(dtUltimoCalcolo.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
				istruttoriaOutput.setDtUltimoCalcolo(dtUltimoCalcoloLocalDate);
			}
			// recupera importi calcolati
			ImportiIstruttoria importiIstruttoria = ImportiIstruttoriaBuilder.from(recuperaTransizioniSostegno, istruttoria);
			if((impLiquidati != null) && (!impLiquidati.isEmpty())){
				impLiquidati.stream()
						.filter(imp -> !Objects.isNull(imp.getIdElencoLiquidazione()) &&
								!Objects.isNull(istruttoria.getElencoLiquidazione()) &&
								imp.getIdElencoLiquidazione().equals(istruttoria.getElencoLiquidazione().getId()))
						.findAny()
						.ifPresent(imp -> {
							importiIstruttoria.setDebitiRecuperati(DebitoBuilder.from(imp.getDebiti()));
							ImportiIstruttoriaBuilder.setDatiLiquidazione(importiIstruttoria, imp);
						});
			}
			istruttoriaOutput.setImportiIstruttoria(importiIstruttoria);
			results.add(istruttoriaOutput);
		};
	}
	
	public static Consumer<? super ImportoLiquidato> creaDtoIstruttoriaPerDisciplina(List<Istruttoria> results){
		return (imp) -> {
    		Istruttoria istruttoriaOutput = new Istruttoria();
    		ImportiIstruttoria importiIstruttoria = new ImportiIstruttoria();
    		importiIstruttoria.setImportoDisciplina(imp.getIncassatoNetto());
    		importiIstruttoria.setDebitiRecuperati(DebitoBuilder.from(imp.getDebiti()));
    		istruttoriaOutput.setImportiIstruttoria(importiIstruttoria);
    		istruttoriaOutput.setDtUltimoCalcolo(imp.getDataEsecuzionePagamento());
    		istruttoriaOutput.setEsitiControlli(new ArrayList<EsitoControllo>());
    		results.add(istruttoriaOutput);
		};
	}

}
