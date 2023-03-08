package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

public class MappaEsitiFoglieAmmissibilitaAccoppiatoZootecnia {
	
	private MappaEsitiFoglieAmmissibilitaAccoppiatoZootecnia() {
	}

	private static Map<EsitoAmmissibilitaAccoppiatoZootecnia, FoglieAmmissibilitaAccoppiatoZootecnia> mappaEsiti = new HashMap<>();
	
	static {
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withDomandaIntegrativa(Optional.of(false)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_000);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(true))
				.withCampione(Optional.of(true))
				.withEsitoControlliInLoco(Optional.of(false)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_001);

		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(true))
				.withCampione(Optional.of(true))
				.withEsitoControlliInLoco(Optional.of(true))
				.withUbaMinime(Optional.of(false)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_002);

		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(true))
				.withCampione(Optional.of(true))
				.withEsitoControlliInLoco(Optional.of(true))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.NESSUNA_SANZIONE)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_003);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(true))
				.withCampione(Optional.of(true))
				.withEsitoControlliInLoco(Optional.of(true))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.CON_SANZIONI)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_004);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(true))
				.withCampione(Optional.of(true))
				.withEsitoControlliInLoco(Optional.of(true))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.TUTTI_SANZIONATI)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_005);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(true))
				.withCampione(Optional.of(false))
				.withUbaMinime(Optional.of(false)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_009);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(true))
				.withCampione(Optional.of(false))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.NESSUNA_SANZIONE)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_010);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(true))
				.withCampione(Optional.of(false))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.CON_SANZIONI)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_011);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(true))
				.withCampione(Optional.of(false))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.TUTTI_SANZIONATI)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_012);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(true))
				.withEsitoControlliInLoco(Optional.of(false)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_016);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(true))
				.withEsitoControlliInLoco(Optional.of(true))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.NESSUNA_SANZIONE)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_017);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(true))
				.withEsitoControlliInLoco(Optional.of(true))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.CON_SANZIONI)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_018);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(true))
				.withEsitoControlliInLoco(Optional.of(true))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.TUTTI_SANZIONATI)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_019);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(true))
				.withEsitoControlliInLoco(Optional.of(true))
				.withUbaMinime(Optional.of(false)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_023);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(false))
				.withUbaMinime(Optional.of(false)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_024);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(false))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.NESSUNA_SANZIONE)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_025);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(false))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.CON_SANZIONI)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_026);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(true))
				.withAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(false))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.TUTTI_SANZIONATI)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_027);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(true))
				.withEsitoControlliInLoco(Optional.of(false)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_031);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(true))
				.withEsitoControlliInLoco(Optional.of(true))
				.withUbaMinime(Optional.of(false)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_032);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(true))
				.withEsitoControlliInLoco(Optional.of(true))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.NESSUNA_SANZIONE)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_036);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(true))
				.withEsitoControlliInLoco(Optional.of(true))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.CON_SANZIONI)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_037);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(true))
				.withEsitoControlliInLoco(Optional.of(true))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.TUTTI_SANZIONATI)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_038);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(false))
				.withUbaMinime(Optional.of(false)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_039);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(false))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.NESSUNA_SANZIONE)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_040);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(false))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.CON_SANZIONI)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_041);
		
		mappaEsiti.put(EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withRiduzioniPresent()
				.withInfoAgricoltoreAttivo(Optional.of(false))
				.withCampione(Optional.of(false))
				.withUbaMinime(Optional.of(true))
				.withInterventiSanzionati(Optional.of(InterventiSanzionati.TUTTI_SANZIONATI)),
				FoglieAmmissibilitaAccoppiatoZootecnia.DUF_042);
	}
	
	public static FoglieAlgoritmoWorkflow getFoglia(EsitoAmmissibilitaAccoppiatoZootecnia esitoControlli) {
		return mappaEsiti.get(esitoControlli);
	}

	public enum FoglieAmmissibilitaAccoppiatoZootecnia implements FoglieAlgoritmoWorkflow {
		
		DUF_000("DUF_000", false, StatoIstruttoria.NON_AMMISSIBILE),
		DUF_001("DUF_001", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_002("DUF_002", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_003("DUF_003", true, StatoIstruttoria.CONTROLLI_CALCOLO_OK),
		DUF_004("DUF_004", true, StatoIstruttoria.CONTROLLI_CALCOLO_OK),
		DUF_005("DUF_005", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_009("DUF_009", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_010("DUF_010", true, StatoIstruttoria.CONTROLLI_CALCOLO_OK),
		DUF_011("DUF_011", true, StatoIstruttoria.CONTROLLI_CALCOLO_OK),
		DUF_012("DUF_012", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_016("DUF_016", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_017("DUF_017", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_018("DUF_018", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_019("DUF_019", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_023("DUF_023", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_024("DUF_024", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_025("DUF_025", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_026("DUF_026", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_027("DUF_027", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_031("DUF_031", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_032("DUF_032", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_036("DUF_036", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_037("DUF_037", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_038("DUF_038", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_039("DUF_039", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_040("DUF_040", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_041("DUF_041", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO),
		DUF_042("DUF_042", false, StatoIstruttoria.CONTROLLI_CALCOLO_KO);
		
		private String codiceEsito;
		private boolean esitoOK;
		private StatoIstruttoria statoWF;

		private FoglieAmmissibilitaAccoppiatoZootecnia(String codiceEsito, boolean esitoOK, StatoIstruttoria statoWF) {
			this.codiceEsito = codiceEsito;
			this.esitoOK = esitoOK;
			this.statoWF = statoWF;
		}

		/* (non-Javadoc)
		 * @see it.tndigitale.a4gistruttoria.dto.lavorazione.FoglieAlgoritmoWorkflow#getCodiceEsito()
		 */
		@Override
		public String getCodiceEsito() {
			return codiceEsito;
		}
		
		/* (non-Javadoc)
		 * @see it.tndigitale.a4gistruttoria.dto.lavorazione.FoglieAlgoritmoWorkflow#isEsitoOK()
		 */
		@Override
		public boolean isEsitoOK() {
			return esitoOK;
		}
		
		/* (non-Javadoc)
		 * @see it.tndigitale.a4gistruttoria.dto.lavorazione.FoglieAlgoritmoWorkflow#getStatoWF()
		 */
		@Override
		public StatoIstruttoria getStatoWF() {
			return statoWF;
		}
	}

}
