package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

public final class CAANativeQueryString {

	public static final String LETTURA_SPORTELLI_CAA = 
			"select s.cod_ente, s.des_ente, s.indirizzo, s.cap, s.provincia, s.comune, " + 
			"caa.cod_ente as caa_cod_ente, caa.des_ente as caa_des_ente, caa.indirizzo as caa_indirizzo, caa.cap as caa_cap, caa.provincia caa_provincia, caa.comune as caa_comune, caa.cod_fiscale as caa_cod_fiscale " + 
			"from siti.sitiente s " + 
			"left outer join siti.sitiente caa on caa.cod_ente = s.cod_ente_sup " + 
			"where sysdate BETWEEN s.data_inizio and s.data_fine " +
			"and s.cod_ente in (:codiciSportello)";

	private CAANativeQueryString() {
		super();
	}

	
}
