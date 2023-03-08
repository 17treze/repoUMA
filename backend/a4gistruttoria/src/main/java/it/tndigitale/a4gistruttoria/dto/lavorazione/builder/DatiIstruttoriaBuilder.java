package it.tndigitale.a4gistruttoria.dto.lavorazione.builder;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreDisModel;

public class DatiIstruttoriaBuilder {
	
	private DatiIstruttoriaBuilder() {}
	
	public static DatiIstruttoria from(final DatiIstruttoreDisModel dModel) {
		if (dModel == null ) return null;
		final DatiIstruttoria datiIstruttoria = new DatiIstruttoria();
		datiIstruttoria.setId(dModel.getId());
		datiIstruttoria.setBpsImportoSanzioniAnnoPrecedente(dModel.getBpsImpSan());
		datiIstruttoria.setBpsSanzioniAnnoPrecedente(dModel.getBpsSanzAnnoPrec());
		datiIstruttoria.setbPSSuperficie(dModel.getBpsSuperficie());
		datiIstruttoria.setControlloAntimafia(dModel.getControlloAntimafia());
		datiIstruttoria.setDomAnnoPrecNonLiquidabile(dModel.getDomAnnoPrecNonLiq());
		datiIstruttoria.setGioImportoSanzioniAnnoPrecedente(dModel.getGioImpSan());
		datiIstruttoria.setGioSanzioniAnnoPrecedente(dModel.getGioSanzAnnoPrec());
		datiIstruttoria.setGreeningSuperficie(dModel.getGreeningSuperficie());
		datiIstruttoria.setImportoSalari(dModel.getImportoSalari());
		datiIstruttoria.setNoteIstruttore(dModel.getNoteIstruttore());
		datiIstruttoria.setAnnulloRiduzione(dModel.getAnnulloRiduzione());
		return datiIstruttoria;
	}
	
	public static DatiIstruttoreDisModel from(final DatiIstruttoria dModel) {
		if (dModel == null ) return null;
		final DatiIstruttoreDisModel datiIstruttoreDisModel = new DatiIstruttoreDisModel();
		datiIstruttoreDisModel.setBpsImpSan(dModel.getBpsImportoSanzioniAnnoPrecedente());
		datiIstruttoreDisModel.setBpsSanzAnnoPrec(dModel.getBpsSanzioniAnnoPrecedente());
		datiIstruttoreDisModel.setBpsSuperficie(dModel.getbPSSuperficie());
		datiIstruttoreDisModel.setControlloAntimafia(dModel.getControlloAntimafia());
		datiIstruttoreDisModel.setDomAnnoPrecNonLiq(dModel.getDomAnnoPrecNonLiquidabile());
		datiIstruttoreDisModel.setGioImpSan(dModel.getGioImportoSanzioniAnnoPrecedente());
		datiIstruttoreDisModel.setGioSanzAnnoPrec(dModel.getGioSanzioniAnnoPrecedente());
		datiIstruttoreDisModel.setGreeningSuperficie(dModel.getGreeningSuperficie());
		datiIstruttoreDisModel.setId(dModel.getId());
		datiIstruttoreDisModel.setImportoSalari(dModel.getImportoSalari());
		datiIstruttoreDisModel.setNoteIstruttore(dModel.getNoteIstruttore());
		datiIstruttoreDisModel.setAnnulloRiduzione(dModel.getAnnulloRiduzione());
		return datiIstruttoreDisModel;
	}	

}
