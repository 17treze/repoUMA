package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.HashMap;
import java.util.Map;

import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

public class MappaEsitiFoglieAmmissibilitaAccoppiatoSuperficie {
	
	private MappaEsitiFoglieAmmissibilitaAccoppiatoSuperficie() {
	}

	private static Map<EsitoAmmissibilitaAccoppiatoSuperficie, FoglieAmmissibilitaAccoppiatoSuperficie> mappaEsiti = new HashMap<>();
	
	static {
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(true)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_001_F_OK);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(null)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_001_F_OK);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(false)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_001_F_KO);

		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(true)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_002_F);

		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(null)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_002_F);
		

		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(false)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_002_F);

		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(false)
				.withSigeco(null)
				.withOlivo75(true)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_005_F_OK);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(false)
				.withSigeco(null)
				.withOlivo75(null)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_005_F_OK);

		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(false)
				.withSigeco(null)
				.withOlivo75(false)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_005_F_KO);

		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(false)
				.withSigeco(null)
				.withOlivo75(true)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_006_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(false)
				.withSigeco(null)
				.withOlivo75(null)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_006_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(false)
				.withSigeco(null)
				.withOlivo75(false)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_006_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(true)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_009_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(null)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_009_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(false)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_009_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(true)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_010_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(null)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_010_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(false)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_010_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(false)
				.withSigeco(null)
				.withOlivo75(true)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_013_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(false)
				.withSigeco(null)
				.withOlivo75(null)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_013_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(false)
				.withSigeco(null)
				.withOlivo75(false)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_013_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(false)
				.withSigeco(null)
				.withOlivo75(true)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_014_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(false)
				.withSigeco(null)
				.withOlivo75(null)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_014_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(false)
				.withSigeco(null)
				.withOlivo75(false)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_014_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(true)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_017_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(null)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_017_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(false)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_017_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(true)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_018_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(null)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_018_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(true)
				.withOlivo75(false)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_018_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(false)
				.withOlivo75(true)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_021_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(false)
				.withOlivo75(null)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_021_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(false)
				.withOlivo75(false)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_021_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(false)
				.withOlivo75(true)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_022_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(false)
				.withOlivo75(null)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_022_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(false)
				.withOlivo75(false)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_022_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(true)
				.withOlivo75(true)
				.withSigeco(false)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_025_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(true)
				.withOlivo75(true)
				.withSigeco(false)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_025_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(true)
				.withOlivo75(null)
				.withSigeco(false)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_025_F);

		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(true)
				.withOlivo75(null)
				.withSigeco(false)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_025_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(true)
				.withOlivo75(false)
				.withSigeco(false)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_025_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(true)
				.withCampione(true)
				.withOlivo75(false)
				.withSigeco(false)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_025_F);

		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(true)
				.withOlivo75(true)
				.withSigeco(false)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_026_F);

		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(true)
				.withOlivo75(true)
				.withSigeco(false)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_026_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(true)
				.withOlivo75(null)
				.withSigeco(false)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_026_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(true)
				.withOlivo75(null)
				.withSigeco(false)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_026_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(true)
				.withOlivo75(false)
				.withSigeco(false)
				.withSuperficieMinima(false),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_026_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(true)
				.withAgricoltoreAttivo(false)
				.withCampione(true)
				.withOlivo75(false)
				.withSigeco(false)
				.withSuperficieMinima(true),
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_026_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(false)
				.withOlivo75(true)
				.withSuperficieMinima(false),				
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_027_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(false)
				.withOlivo75(true)
				.withSuperficieMinima(true),				
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_027_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(false)
				.withOlivo75(null)
				.withSuperficieMinima(false),				
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_027_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(false)
				.withOlivo75(null)
				.withSuperficieMinima(true),				
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_027_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(false)
				.withOlivo75(false)
				.withSuperficieMinima(false),				
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_027_F);
		
		mappaEsiti.put(new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(false)
				.withCampione(true)
				.withSigeco(false)
				.withOlivo75(false)
				.withSuperficieMinima(true),				
				FoglieAmmissibilitaAccoppiatoSuperficie.DUF_027_F);
	}
	
	public static FoglieAmmissibilitaAccoppiatoSuperficie getFoglia(EsitoAmmissibilitaAccoppiatoSuperficie esitoControlli) {
		return mappaEsiti.get(esitoControlli);
	}

	public enum FoglieAmmissibilitaAccoppiatoSuperficie implements FoglieAlgoritmoWorkflow {
		
		DUF_001_F_OK("DUF_001_F", true, StatoIstruttoria.CONTROLLI_CALCOLO_OK),
		DUF_001_F_KO("DUF_001_F", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_002_F("DUF_002_F", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_005_F_OK("DUF_005_F", true, StatoIstruttoria.CONTROLLI_CALCOLO_OK),
		DUF_005_F_KO("DUF_005_F", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_006_F("DUF_006_F", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_009_F("DUF_009_F", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_010_F("DUF_010_F", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_013_F("DUF_013_F", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_014_F("DUF_014_F", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_017_F("DUF_017_F", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_018_F("DUF_018_F", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_021_F("DUF_021_F", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_022_F("DUF_022_F", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_025_F("DUF_025_F", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_026_F("DUF_026_F", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_027_F("DUF_027_F", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO);

		private String codiceEsito;
		private boolean esitoOK;
		private StatoIstruttoria statoWF;

		private FoglieAmmissibilitaAccoppiatoSuperficie(String codiceEsito, boolean esitoOK, StatoIstruttoria statoWF) {
			this.codiceEsito = codiceEsito;
			this.esitoOK = esitoOK;
			this.statoWF = statoWF;
		}

		public String getCodiceEsito() {
			return codiceEsito;
		}
		
		public boolean isEsitoOK() {
			return esitoOK;
		}
		
		public StatoIstruttoria getStatoWF() {
			return statoWF;
		}
	}

}
