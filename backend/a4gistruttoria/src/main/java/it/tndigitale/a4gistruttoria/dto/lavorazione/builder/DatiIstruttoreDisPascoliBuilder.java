package it.tndigitale.a4gistruttoria.dto.lavorazione.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaPascoli;
import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreDisPascoliModel;

public class DatiIstruttoreDisPascoliBuilder {
	
	public static DatiIstruttoriaPascoli from(DatiIstruttoreDisPascoliModel datiIstruttoreDisPascoli) {
		DatiIstruttoriaPascoli datiIstruttoriaPascoli = new DatiIstruttoriaPascoli();
		datiIstruttoriaPascoli.setCuaaResponsabile(datiIstruttoreDisPascoli.getCuaaResponsabile());
		datiIstruttoriaPascoli.setDescrizionePascolo(datiIstruttoreDisPascoli.getDescrizionePascolo());
		datiIstruttoriaPascoli.setEsitoControlloMantenimento(datiIstruttoreDisPascoli.getEsitoControlloMantenimento());
		datiIstruttoriaPascoli.setSuperficieDeterminata(datiIstruttoreDisPascoli.getSuperficieDeterminata());
		datiIstruttoriaPascoli.setVerificaDocumentazione(datiIstruttoreDisPascoli.getVerificaDocumentazione());
		datiIstruttoriaPascoli.setId(datiIstruttoreDisPascoli.getId());
		datiIstruttoriaPascoli.setVersion(datiIstruttoreDisPascoli.getVersion());
		return datiIstruttoriaPascoli;
	}
	
	public static List<DatiIstruttoriaPascoli> from(Set<DatiIstruttoreDisPascoliModel> listaPascoliModel) {
		return 
				Optional.ofNullable(listaPascoliModel)
					.map(listaPascoli -> 
							listaPascoliModel.stream()
								.map(DatiIstruttoreDisPascoliBuilder::from)
								.collect(Collectors.toList())
					)
					.orElse(new ArrayList<DatiIstruttoriaPascoli>());
	}
	
	public static DatiIstruttoreDisPascoliModel to(DatiIstruttoriaPascoli datiIstruttoreDisPascoli) {
		DatiIstruttoreDisPascoliModel datiIstruttoriaPascoliModel = new DatiIstruttoreDisPascoliModel();
		datiIstruttoriaPascoliModel.setCuaaResponsabile(datiIstruttoreDisPascoli.getCuaaResponsabile());
		datiIstruttoriaPascoliModel.setDescrizionePascolo(datiIstruttoreDisPascoli.getDescrizionePascolo());
		datiIstruttoriaPascoliModel.setEsitoControlloMantenimento(datiIstruttoreDisPascoli.getEsitoControlloMantenimento());
		datiIstruttoriaPascoliModel.setId(datiIstruttoreDisPascoli.getId());
		datiIstruttoriaPascoliModel.setSuperficieDeterminata(datiIstruttoreDisPascoli.getSuperficieDeterminata());
		datiIstruttoriaPascoliModel.setVerificaDocumentazione(datiIstruttoreDisPascoli.getVerificaDocumentazione());
		datiIstruttoriaPascoliModel.setVersion(datiIstruttoreDisPascoli.getVersion());
		return datiIstruttoriaPascoliModel;
	}
	
	public static Set<DatiIstruttoreDisPascoliModel> to(List<DatiIstruttoriaPascoli> datiIstruttoriaPascoli) {
		return datiIstruttoriaPascoli.stream()
				.map(DatiIstruttoreDisPascoliBuilder::to)
				.collect(Collectors.toSet()); 
	}

}
