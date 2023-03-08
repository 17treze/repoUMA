package it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione;

import it.tndigitale.a4gistruttoria.util.TipoCampoElencoLiquidazione;

public enum CampoElencoLiquidazione {
	TIPO_RECORD(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 2, 0, 1, "D1"),
	PROGRESSIVO_RECORD(TipoCampoElencoLiquidazione.NUMERICO, false, 6, 2, 7),
	SETTORE(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 3, 8, 10, "009"),
	ANNO_CAMPAGNA(TipoCampoElencoLiquidazione.NUMERICO, true, 4, 11, 14),
	TIPO_PAGAMENTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 2, 15, 16),
	DATA_PROTOCOLLO(TipoCampoElencoLiquidazione.DATA, true, 8, 17, 24),
	NUMERO_DOMANDA(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 15, 25, 39),
	RAGIONE_SOCIALE(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 70, 40, 109),
	COGNOME_BENEFICIARIO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 50, 110, 159),
	NOME_BENEFICIARIO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 50, 160, 209),
	SESSO(TipoCampoElencoLiquidazione.ALFANUMERICO, false, 1, 210, 210),
	DATA_NASCITA(TipoCampoElencoLiquidazione.DATA, false, 8, 211, 218),
	ISTAT_COMUNE_NASCITA(TipoCampoElencoLiquidazione.ALFANUMERICO, false, 6, 219, 224),
	SIGLA_PROVINCIA_NASCITA(TipoCampoElencoLiquidazione.ALFANUMERICO, false, 3, 225, 227),
	CODICE_FISCALE(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 16, 228, 243),
	INDIRIZZO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 45, 244, 288),
	ISTAT_COMUNE_RECAPITO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 6, 289, 294),
	SIGLA_PROVINCIA_RECAPITO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 3, 295, 297),
	CAP(TipoCampoElencoLiquidazione.NUMERICO, true, 5, 298, 302),
	MODALITA_PAGAMENTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 2, 303, 304, "01"),
	CODICE_ABI(TipoCampoElencoLiquidazione.NUMERICO, false, 5, 305, 309),
	CODICE_CAB(TipoCampoElencoLiquidazione.NUMERICO, false, 5, 310, 314),
	NUMERO_CONTO(TipoCampoElencoLiquidazione.ALFANUMERICO, false, 17, 315, 331),
	IMPORTO_TOTALE(TipoCampoElencoLiquidazione.NUMERICO, true, 12, 332, 343),
	// Voce Spesa 1
	VS1_CODICE_PRODOTTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 6, 344, 349),
	VS1_CAPITOLO_SPESA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 350, 359),
	VS1_QUANTITA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 360, 369),
	VS1_IMPORTO(TipoCampoElencoLiquidazione.NUMERICO, true, 12, 370, 381),
	// Voce Spesa 2
	VS2_CODICE_PRODOTTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 6, 382, 387),
	VS2_CAPITOLO_SPESA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 388, 397),
	VS2_QUANTITA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 398, 407),
	VS2_IMPORTO(TipoCampoElencoLiquidazione.NUMERICO, true, 12, 408, 419),
	// Voce Spesa 3
	VS3_CODICE_PRODOTTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 6, 420, 425),
	VS3_CAPITOLO_SPESA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 426, 435),
	VS3_QUANTITA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 436, 445),
	VS3_IMPORTO(TipoCampoElencoLiquidazione.NUMERICO, true, 12, 446, 457),
	// Voce Spesa 4
	VS4_CODICE_PRODOTTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 6, 458, 463),
	VS4_CAPITOLO_SPESA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 464, 473),
	VS4_QUANTITA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 474, 483),
	VS4_IMPORTO(TipoCampoElencoLiquidazione.NUMERICO, true, 12, 484, 495),
	// Voce Spesa 5
	VS5_CODICE_PRODOTTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 6, 496, 501),
	VS5_CAPITOLO_SPESA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 502, 511),
	VS5_QUANTITA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 512, 521),
	VS5_IMPORTO(TipoCampoElencoLiquidazione.NUMERICO, true, 12, 522, 533),
	// Voce Spesa 6
	VS6_CODICE_PRODOTTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 6, 534, 539),
	VS6_CAPITOLO_SPESA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 540, 549),
	VS6_QUANTITA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 550, 559),
	VS6_IMPORTO(TipoCampoElencoLiquidazione.NUMERICO, true, 12, 560, 571),
	// Voce Spesa 7
	VS7_CODICE_PRODOTTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 6, 572, 577),
	VS7_CAPITOLO_SPESA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 578, 587),
	VS7_QUANTITA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 588, 597),
	VS7_IMPORTO(TipoCampoElencoLiquidazione.NUMERICO, true, 12, 598, 609),
	// Voce Spesa 8
	VS8_CODICE_PRODOTTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 6, 610, 615),
	VS8_CAPITOLO_SPESA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 616, 625),
	VS8_QUANTITA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 626, 635),
	VS8_IMPORTO(TipoCampoElencoLiquidazione.NUMERICO, true, 12, 636, 647),
	// Voce Spesa 9
	VS9_CODICE_PRODOTTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 6, 648, 653),
	VS9_CAPITOLO_SPESA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 654, 663),
	VS9_QUANTITA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 664, 673),
	VS9_IMPORTO(TipoCampoElencoLiquidazione.NUMERICO, true, 12, 674, 685),
	// Voce Spesa 10
	VS10_CODICE_PRODOTTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 6, 686, 691),
	VS10_CAPITOLO_SPESA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 692, 701),
	VS10_QUANTITA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 702, 711),
	VS10_IMPORTO(TipoCampoElencoLiquidazione.NUMERICO, true, 12, 712, 723),
	// Voce Spesa 11
	VS11_CODICE_PRODOTTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 6, 724, 729),
	VS11_CAPITOLO_SPESA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 730, 739),
	VS11_QUANTITA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 740, 749),
	VS11_IMPORTO(TipoCampoElencoLiquidazione.NUMERICO, true, 12, 750, 761),
	// Voce Spesa 12
	VS12_CODICE_PRODOTTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 6, 762, 767, "      "),
	VS12_CAPITOLO_SPESA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 768, 777, "0000000000"),
	VS12_QUANTITA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 778, 787, "0000000000"),
	VS12_IMPORTO(TipoCampoElencoLiquidazione.NUMERICO, true, 12, 788, 799, "0000000000"),
	// Voce Spesa 13
	VS13_CODICE_PRODOTTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 6, 800, 805, "      "),
	VS13_CAPITOLO_SPESA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 806, 815, "0000000000"),
	VS13_QUANTITA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 816, 825, "0000000000"),
	VS13_IMPORTO(TipoCampoElencoLiquidazione.NUMERICO, true, 12, 826, 837, "0000000000"),
	// Voce Spesa 14
	VS14_CODICE_PRODOTTO(TipoCampoElencoLiquidazione.ALFANUMERICO, true, 6, 838, 843, "      "),
	VS14_CAPITOLO_SPESA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 844, 853, "0000000000"),
	VS14_QUANTITA(TipoCampoElencoLiquidazione.NUMERICO, true, 10, 854, 863, "0000000000"),
	VS14_IMPORTO(TipoCampoElencoLiquidazione.NUMERICO, true, 12, 864, 875, "0000000000"),
	NOTE_BENEFICIARIO(TipoCampoElencoLiquidazione.ALFANUMERICO, false, 160, 876, 1035),
	SETTORE_EFFETTIVO(TipoCampoElencoLiquidazione.ALFANUMERICO, false, 6, 1036, 1041),
	IDENTIFICATIVO_ELENCO(TipoCampoElencoLiquidazione.NUMERICO, false, 10, 1042, 1051),
	CIG(TipoCampoElencoLiquidazione.ALFANUMERICO, false, 10, 1052, 1061),
	CUP(TipoCampoElencoLiquidazione.ALFANUMERICO, false, 15, 1062, 1076),
	FILLER(TipoCampoElencoLiquidazione.ALFANUMERICO, false, 158, 1077, 1234);

	private TipoCampoElencoLiquidazione tipoCampo;
	private boolean obbligatorio;
	private int dimensione;
	private int start;
	private int end;
	private String costante;

	private CampoElencoLiquidazione(TipoCampoElencoLiquidazione tipoCampo, boolean obbligatorio, int dimensione, int start, int end) {
		this.tipoCampo = tipoCampo;
		this.obbligatorio = obbligatorio;
		this.dimensione = dimensione;
		this.start = start;
		this.end = end;
	}

	private CampoElencoLiquidazione(TipoCampoElencoLiquidazione tipoCampo, boolean obbligatorio, int dimensione, int start, int end, String costante) {
		this.tipoCampo = tipoCampo;
		this.obbligatorio = obbligatorio;
		this.dimensione = dimensione;
		this.start = start;
		this.end = end;
		this.costante = costante;
	}

	public TipoCampoElencoLiquidazione getTipoCampo() {
		return tipoCampo;
	}

	public void setTipoCampo(TipoCampoElencoLiquidazione tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	public boolean isObbligatorio() {
		return obbligatorio;
	}

	public void setObbligatorio(boolean obbligatorio) {
		this.obbligatorio = obbligatorio;
	}

	public int getDimensione() {
		return dimensione;
	}

	public void setDimensione(int dimensione) {
		this.dimensione = dimensione;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getCostante() {
		return costante;
	}

	public void setCostante(String costante) {
		this.costante = costante;
	}
}
