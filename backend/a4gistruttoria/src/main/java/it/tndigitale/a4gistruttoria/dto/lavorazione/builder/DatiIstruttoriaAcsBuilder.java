package it.tndigitale.a4gistruttoria.dto.lavorazione.builder;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaACS;
import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreSuperficieModel;

public class DatiIstruttoriaAcsBuilder {
	private DatiIstruttoriaAcsBuilder() {}

	public static DatiIstruttoriaACS from(DatiIstruttoreSuperficieModel datiIstruttoreSuperficie) {
		if (datiIstruttoreSuperficie == null ) return null;
		DatiIstruttoriaACS datiIstruttoriaACS = new DatiIstruttoriaACS();
		datiIstruttoriaACS.setControlloAntimafia(datiIstruttoreSuperficie.getControlloAntimafia());
		datiIstruttoriaACS.setControlloSigecoLoco(datiIstruttoreSuperficie.getControlloSigecoLoco());
		datiIstruttoriaACS.setId(datiIstruttoreSuperficie.getId());
		datiIstruttoriaACS.setSuperficieDeterminataFrumentoM9(datiIstruttoreSuperficie.getSupFrumentoM9());
		datiIstruttoriaACS.setSuperficieDeterminataLeguminoseM11(datiIstruttoreSuperficie.getSupLeguminoseM11());
		datiIstruttoriaACS.setSuperficieDeterminataOlivoPendenzaM16(datiIstruttoreSuperficie.getSupOlivopendenzaM16());
		datiIstruttoriaACS.setSuperficieDeterminataOlivoQualitaM17(datiIstruttoreSuperficie.getSupOlivoqualitaM17());
		datiIstruttoriaACS.setSuperficieDeterminataOlivoStandardM15(datiIstruttoreSuperficie.getSupOlivostandardM15());
		datiIstruttoriaACS.setSuperficieDeterminataPomodoroM14(datiIstruttoreSuperficie.getSupPomodoroM14());
		datiIstruttoriaACS.setSuperficieDeterminataProteaginoseM10(datiIstruttoreSuperficie.getSupProteaginoseM10());
		datiIstruttoriaACS.setSuperficieDeterminataSoiaM8(datiIstruttoreSuperficie.getSupSoiaM8());
		datiIstruttoriaACS.setAnnulloRiduzione(datiIstruttoreSuperficie.getAnnulloRiduzione());
		return datiIstruttoriaACS;
	}
	
	public static DatiIstruttoreSuperficieModel to(DatiIstruttoriaACS datiIstruttoreSuperficie) {
		if (datiIstruttoreSuperficie == null ) return null;
		DatiIstruttoreSuperficieModel datiIstruttoriaACS = new DatiIstruttoreSuperficieModel();
		datiIstruttoriaACS.setControlloAntimafia(datiIstruttoreSuperficie.getControlloAntimafia());
		datiIstruttoriaACS.setControlloSigecoLoco(datiIstruttoreSuperficie.getControlloSigecoLoco());
		datiIstruttoriaACS.setId(datiIstruttoreSuperficie.getId());
		datiIstruttoriaACS.setSupFrumentoM9(datiIstruttoreSuperficie.getSuperficieDeterminataFrumentoM9());
		datiIstruttoriaACS.setSupLeguminoseM11(datiIstruttoreSuperficie.getSuperficieDeterminataLeguminoseM11());
		datiIstruttoriaACS.setSupOlivopendenzaM16(datiIstruttoreSuperficie.getSuperficieDeterminataOlivoPendenzaM16());
		datiIstruttoriaACS.setSupOlivoqualitaM17(datiIstruttoreSuperficie.getSuperficieDeterminataOlivoQualitaM17());
		datiIstruttoriaACS.setSupOlivostandardM15(datiIstruttoreSuperficie.getSuperficieDeterminataOlivoStandardM15());
		datiIstruttoriaACS.setSupPomodoroM14(datiIstruttoreSuperficie.getSuperficieDeterminataPomodoroM14());
		datiIstruttoriaACS.setSupProteaginoseM10(datiIstruttoreSuperficie.getSuperficieDeterminataProteaginoseM10());
		datiIstruttoriaACS.setSupSoiaM8(datiIstruttoreSuperficie.getSuperficieDeterminataSoiaM8());
		datiIstruttoriaACS.setAnnulloRiduzione(datiIstruttoreSuperficie.getAnnulloRiduzione());
		return datiIstruttoriaACS;
	}	
}
