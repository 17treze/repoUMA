package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy;

import java.util.HashMap;
import java.util.Map;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.Carica;

public class CaricheLegacyMap {

	private CaricheLegacyMap() {}

	protected static final Map<String, Carica> RUOLI_RAPPRESENTANTI_LOCALI = populateMap();
	protected static final Map<String, Carica> RUOLI_EREDI = erediMap();

	private static HashMap<String, Carica> populateMap() {
		HashMap<String, Carica> map = new HashMap<>();
		map.put("000001", Carica.TITOLARE);
		map.put("000003", Carica.SOCIO_AMMINISTRATORE);
		map.put("000004", Carica.CURATORE_FALLIMENTARE);
		map.put("000005", Carica.LIQUIDATORE);
		map.put("000006", Carica.COMMISSARIO_GIUDIZIALE);
		map.put("000009", Carica.SOCIO);
		map.put("000044", Carica.PRESIDENTE_CONSIGLIO_AMMINISTRAZIONE);
		map.put("000049", Carica.CONSIGLIERE_DELEGATO);
		map.put("000050", Carica.AMMINISTRATORE_GIUDIZIARIO);
		map.put("000052", Carica.SOCIO_ACCOMANDATARIO);
		map.put("000071", Carica.SOCIO_UNICO);
		map.put("000074", Carica.LEGALE_RAPPRESENTANTE);
		map.put("000075", Carica.TUTORE);
		map.put("000081", Carica.INSTITORE);
		map.put("000082", Carica.SOCIO_RAPPRESENTANTE);
		map.put("000086", Carica.TITOLARE_DELL_IMPRESA_ARTIGIANA);
		map.put("000087", Carica.PRESIDENTE_COMITATO_ESECUTIVO);
		map.put("000093", Carica.PRESIDENTE_CONSORZIO);
		map.put("000094", Carica.PRESIDENTE_EFFETTIVO_CONSIGLIO_DIRETTIVO);
		map.put("000096", Carica.COMMISSARIO_LIQUIDATORE);
		map.put("000097", Carica.COMMISSARIO_GIUDIZIARIO);
		map.put("000098", Carica.LEGALE_RAPPRESENTANTE_ART_2_L_25_8_91_N_287);
		map.put("000099", Carica.LIQUIDATORE_GIUDIZIARIO);
		map.put("000103", Carica.LEGALE_RAPPRESENTANTE_DI_SOCIETA);
		map.put("000104", Carica.SOCIO_CONTITOLARE);
		map.put("000106", Carica.COMMISSARIO_STRAORDINARIO);
		map.put("000112", Carica.PRESIDENTE_CONSIGLIO_DIRETTIVO);
		map.put("000115", Carica.AMMINISTRATORE_STRAORDINARIO);
		map.put("000119", Carica.PRESIDENTE_DEL_CONSIGLIO_DI_GESTIONE);
		map.put("000122", Carica.PRESIDENTE_COMITATO_DIRETTIVO);
		map.put("000126", Carica.SOCIO_ACCOMANDATARIO_E_RAPPRESENTANTE_LEGALE);
		map.put("000133", Carica.SOCIO_DI_SOCIETA_DI_PERSONE_RAPPRES);
		map.put("000138", Carica.SOCIO_ACCOMANDATARIO_D_OPERA);
		map.put("000140", Carica.PREPOSTO_DELLA_SEDE_SECONDARIA);
		map.put("000141", Carica.CURATORE);
		map.put("000155", Carica.MADRE_ESERCENTE_LA_PATRIA_POTESTA);
		map.put("000157", Carica.AMMINISTRATORE_PROVVISORIO);
		map.put("000158", Carica.ACCOMANDATARIO_DI_SAPA);
		map.put("000002", Carica.LEGALE_RAPPRESENTANTE);
		map.put("000095", Carica.PROCURATORE_SPECIALE);

		return map;
	}

	private static HashMap<String, Carica> erediMap() {
		HashMap<String, Carica> map = new HashMap<>();
		map.put("001000", Carica.EREDE);
		return map;
	}
}
