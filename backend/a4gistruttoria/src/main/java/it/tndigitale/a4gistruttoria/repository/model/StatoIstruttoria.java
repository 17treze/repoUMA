package it.tndigitale.a4gistruttoria.repository.model;

import java.util.Arrays;
import java.util.List;

public enum StatoIstruttoria {
	RICHIESTO("RICHIESTO"),
	INTEGRATO("INTEGRATO"),
	CONTROLLI_CALCOLO_KO("CONTROLLI_CALCOLO_KO"), //CONTROLLI NON SUPERATI
	CONTROLLI_CALCOLO_OK("CONTROLLI_CALCOLO_OK"), //CONTROLLI SUPERATI
	NON_AMMISSIBILE("NON_AMMISSIBILE"),
	LIQUIDABILE("LIQUIDABILE"),
	CONTROLLI_LIQUIDABILE_KO("CONTROLLI_LIQUIDABILE_KO"),
	DEBITI("DEBITI"),
	NON_LIQUIDABILE("NON_LIQUIDABILE"),
	PAGAMENTO_AUTORIZZATO("PAGAMENTO_AUTORIZZATO"),
	PAGAMENTO_NON_AUTORIZZATO("PAGAMENTO_NON_AUTORIZZATO"),
	CONTROLLI_INTERSOSTEGNO_OK("CONTROLLI_INTERSOSTEGNO_OK"),
	NON_RICHIESTO("NON_RICHIESTO");

	private String statoIstruttoria;

	public String getStatoIstruttoria() {
		return statoIstruttoria;
	}

	private StatoIstruttoria(String statoLavorazioneSostegno) {
		this.statoIstruttoria = statoLavorazioneSostegno;
	}

	public static StatoIstruttoria valueOfByIdentificativo(String identificativo) {
		for (StatoIstruttoria s : values()) {
			if (s.statoIstruttoria.equals(identificativo)) {
				return s;
			}
		}
		return null;
	}
	
	public static boolean isStatoCalcoloValido(StatoIstruttoria stato) {
		List<StatoIstruttoria> statiValidi =
				Arrays.asList(LIQUIDABILE, CONTROLLI_LIQUIDABILE_KO, NON_LIQUIDABILE, PAGAMENTO_AUTORIZZATO, PAGAMENTO_NON_AUTORIZZATO,
						NON_AMMISSIBILE, CONTROLLI_INTERSOSTEGNO_OK);
		return statiValidi.contains(stato);
	}
	
	public static boolean isStatoCalcoloValidoConDebiti(StatoIstruttoria stato) {
		List<StatoIstruttoria> statiValidi =
				Arrays.asList(LIQUIDABILE, CONTROLLI_LIQUIDABILE_KO, NON_LIQUIDABILE, PAGAMENTO_AUTORIZZATO, PAGAMENTO_NON_AUTORIZZATO,
						NON_AMMISSIBILE, CONTROLLI_INTERSOSTEGNO_OK, DEBITI);
		return statiValidi.contains(stato);
	}
	
	public static boolean isStatoValidoPerResetIstruttoria(StatoIstruttoria stato) {
		List<StatoIstruttoria> statiValidi =
				Arrays.asList(NON_AMMISSIBILE, LIQUIDABILE, CONTROLLI_LIQUIDABILE_KO, NON_LIQUIDABILE, PAGAMENTO_NON_AUTORIZZATO);
		return statiValidi.contains(stato);
	}

}
