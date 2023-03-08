package it.tndigitale.a4gistruttoria.dto.lavorazione.builder;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaACZ;
import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreZootecniaModel;

public class DatiIstruttoriaAczBuilder {
	
	private DatiIstruttoriaAczBuilder() {}

	public static DatiIstruttoriaACZ from(DatiIstruttoreZootecniaModel datiIstruttoreZootecnia) {
		if (datiIstruttoreZootecnia == null ) return null;
		DatiIstruttoriaACZ datiIstruttoriaACZ=new DatiIstruttoriaACZ();
		datiIstruttoriaACZ.setControlloAntimafia(datiIstruttoreZootecnia.getControlloAntimafia());
		datiIstruttoriaACZ.setControlloSigecoLoco(datiIstruttoreZootecnia.getControlloSigecoLoco());
		datiIstruttoriaACZ.setCuaaSubentrante(datiIstruttoreZootecnia.getCuaaSubentrante());
		datiIstruttoriaACZ.setId(datiIstruttoreZootecnia.getId());
		datiIstruttoriaACZ.setAnnulloRiduzione(datiIstruttoreZootecnia.getAnnulloRiduzione());
		return datiIstruttoriaACZ;
	}
	
	public static DatiIstruttoreZootecniaModel to(DatiIstruttoriaACZ datiIstruttoreZootecnia) {
		if (datiIstruttoreZootecnia == null ) return null;
		DatiIstruttoreZootecniaModel datiIstruttoriaACZ=new DatiIstruttoreZootecniaModel();
		datiIstruttoriaACZ.setControlloAntimafia(datiIstruttoreZootecnia.getControlloAntimafia());
		datiIstruttoriaACZ.setControlloSigecoLoco(datiIstruttoreZootecnia.getControlloSigecoLoco());
		datiIstruttoriaACZ.setCuaaSubentrante(datiIstruttoreZootecnia.getCuaaSubentrante());
		datiIstruttoriaACZ.setId(datiIstruttoreZootecnia.getId());
		datiIstruttoriaACZ.setAnnulloRiduzione(datiIstruttoreZootecnia.getAnnulloRiduzione());
		return datiIstruttoriaACZ;
	}


}
