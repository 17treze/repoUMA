package it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione;

import java.time.LocalDate;

class DatiLiquidazioneIstruttoria {
	
	static enum TipoPagamento {
		// Pagamento Unico
		PAGAMENTO_UNICO("01"), 
		// Anticipo
		ANTICIPO("02"), 
		// Stato avanzamento
		STATO_AVANZAMENTO("03"), 
		// Saldo
		SALDO("04"),
		
		// Integrazione
		INTEGRAZIONE("05");

		private String codice;

		private TipoPagamento(String codice) {
			this.codice = codice;
		}

		String getCodice() {
			return codice;
		}
		
	}

	private Integer progressivo;
	private Integer annoCampagna;
	private TipoPagamento tipoPagamento;
	private LocalDate dataProtocollazione;
	private Long numeroDomanda;
	private Beneficiario beneficiario;
	private Double importoTotale;
	private VoceSpesa voce1;
	private VoceSpesa voce2;
	private VoceSpesa voce3;
	private VoceSpesa voce4;
	private VoceSpesa voce5;
	private VoceSpesa voce6;
	private VoceSpesa voce7;
	private VoceSpesa voce8;
	private VoceSpesa voce9;
	private VoceSpesa voce10;
	private VoceSpesa voce11;
	private VoceSpesa voce12;
	private VoceSpesa voce13;
	private VoceSpesa voce14;
	private Long identificativoElenco;
	
	static class Beneficiario {
		static enum Sesso {
			MASCHIO, FEMMINA;
			
			static Sesso valueOfBySigla(String sigla) {
				if ("M".equals(sigla)) return MASCHIO;
				if ("F".equals(sigla)) return FEMMINA;
				return null;
			}
		}
		private String iban;
		private String ragioneSociale;
		private String cognome;
		private String nome;
		private Sesso sesso;
		private LocalDate dataNascita;
		private String istatComuneNascita;
		private String siglaProvinciaNascita;
		private String codiceFiscale;
		private String indirizzo;
		private String istatComuneRecapito;
		private String siglaProvinciaRecapito;
		private String cap;
		

		String getIban() {
			return iban;
		}

		void setIban(String iban) {
			this.iban = iban;
		}
		String getRagioneSociale() {
			return ragioneSociale;
		}
		String getCognome() {
			return cognome;
		}
		String getNome() {
			return nome;
		}
		Sesso getSesso() {
			return sesso;
		}
		LocalDate getDataNascita() {
			return dataNascita;
		}
		String getIstatComuneNascita() {
			return istatComuneNascita;
		}
		String getSiglaProvinciaNascita() {
			return siglaProvinciaNascita;
		}
		String getCodiceFiscale() {
			return codiceFiscale;
		}
		String getIndirizzo() {
			return indirizzo;
		}
		String getIstatComuneRecapito() {
			return istatComuneRecapito;
		}
		String getSiglaProvinciaRecapito() {
			return siglaProvinciaRecapito;
		}
		String getCap() {
			return cap;
		}
		void setRagioneSociale(String ragioneSociale) {
			this.ragioneSociale = ragioneSociale;
		}
		void setCognome(String cognome) {
			this.cognome = cognome;
		}
		void setNome(String nome) {
			this.nome = nome;
		}
		void setSesso(Sesso sesso) {
			this.sesso = sesso;
		}
		void setDataNascita(LocalDate dataNascita) {
			this.dataNascita = dataNascita;
		}
		void setIstatComuneNascita(String istatComuneNascita) {
			this.istatComuneNascita = istatComuneNascita;
		}
		void setSiglaProvinciaNascita(String siglaProvinciaNascita) {
			this.siglaProvinciaNascita = siglaProvinciaNascita;
		}
		void setCodiceFiscale(String codiceFiscale) {
			this.codiceFiscale = codiceFiscale;
		}
		void setIndirizzo(String indirizzo) {
			this.indirizzo = indirizzo;
		}
		void setIstatComuneRecapito(String istatComuneRecapito) {
			this.istatComuneRecapito = istatComuneRecapito;
		}
		void setSiglaProvinciaRecapito(String siglaProvinciaRecapito) {
			this.siglaProvinciaRecapito = siglaProvinciaRecapito;
		}
		void setCap(String cap) {
			this.cap = cap;
		}

	}
	
	static class VoceSpesa {
		private String capitolo;
	    private Double codiceProdotto;
	    private Double quantita;
	    private Double importo;
		
	    String getCapitolo() {
			return capitolo;
		}
		Double getCodiceProdotto() {
			return codiceProdotto;
		}
		Double getQuantita() {
			return quantita;
		}
		Double getImporto() {
			return importo;
		}
		void setCapitolo(String capitolo) {
			this.capitolo = capitolo;
		}
		void setCodiceProdotto(Double codiceProdotto) {
			this.codiceProdotto = codiceProdotto;
		}
		void setQuantita(Double quantita) {
			this.quantita = quantita;
		}
		void setImporto(Double importo) {
			this.importo = importo;
		}
	}

	Integer getProgressivo() {
		return progressivo;
	}

	Integer getAnnoCampagna() {
		return annoCampagna;
	}

	TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	LocalDate getDataProtocollazione() {
		return dataProtocollazione;
	}

	Long getNumeroDomanda() {
		return numeroDomanda;
	}

	Beneficiario getBeneficiario() {
		return beneficiario;
	}

	Double getImportoTotale() {
		return importoTotale;
	}

	VoceSpesa getVoce1() {
		return voce1;
	}

	VoceSpesa getVoce2() {
		return voce2;
	}

	VoceSpesa getVoce3() {
		return voce3;
	}

	VoceSpesa getVoce4() {
		return voce4;
	}

	VoceSpesa getVoce5() {
		return voce5;
	}

	VoceSpesa getVoce6() {
		return voce6;
	}

	VoceSpesa getVoce7() {
		return voce7;
	}

	VoceSpesa getVoce8() {
		return voce8;
	}

	VoceSpesa getVoce9() {
		return voce9;
	}

	VoceSpesa getVoce10() {
		return voce10;
	}

	VoceSpesa getVoce11() {
		return voce11;
	}

	VoceSpesa getVoce12() {
		return voce12;
	}

	VoceSpesa getVoce13() {
		return voce13;
	}

	VoceSpesa getVoce14() {
		return voce14;
	}

	Long getIdentificativoElenco() {
		return identificativoElenco;
	}

	void setProgressivo(Integer progressivo) {
		this.progressivo = progressivo;
	}

	void setAnnoCampagna(Integer annoCampagna) {
		this.annoCampagna = annoCampagna;
	}

	void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	void setDataProtocollazione(LocalDate dataProtocollazione) {
		this.dataProtocollazione = dataProtocollazione;
	}

	void setNumeroDomanda(Long numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}

	void setImportoTotale(Double importoTotale) {
		this.importoTotale = importoTotale;
	}

	void setVoce1(VoceSpesa voce1) {
		this.voce1 = voce1;
	}

	void setVoce2(VoceSpesa voce2) {
		this.voce2 = voce2;
	}

	void setVoce3(VoceSpesa voce3) {
		this.voce3 = voce3;
	}

	void setVoce4(VoceSpesa voce4) {
		this.voce4 = voce4;
	}

	void setVoce5(VoceSpesa voce5) {
		this.voce5 = voce5;
	}

	void setVoce6(VoceSpesa voce6) {
		this.voce6 = voce6;
	}

	void setVoce7(VoceSpesa voce7) {
		this.voce7 = voce7;
	}

	void setVoce8(VoceSpesa voce8) {
		this.voce8 = voce8;
	}

	void setVoce9(VoceSpesa voce9) {
		this.voce9 = voce9;
	}

	void setVoce10(VoceSpesa voce10) {
		this.voce10 = voce10;
	}

	void setVoce11(VoceSpesa voce11) {
		this.voce11 = voce11;
	}

	void setVoce12(VoceSpesa voce12) {
		this.voce12 = voce12;
	}

	void setVoce13(VoceSpesa voce13) {
		this.voce13 = voce13;
	}

	void setVoce14(VoceSpesa voce14) {
		this.voce14 = voce14;
	}

	void setIdentificativoElenco(Long identificativoElenco) {
		this.identificativoElenco = identificativoElenco;
	}

}
