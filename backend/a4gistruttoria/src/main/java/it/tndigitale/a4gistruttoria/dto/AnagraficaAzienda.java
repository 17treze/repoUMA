package it.tndigitale.a4gistruttoria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class AnagraficaAzienda {

    public static final String TIPO_AZIENDA_PERSONA_FISICA = "PF";
    public static final String TIPO_AZIENDA_PERSONA_GIURIDICA = "PG";
    public static final String TIPO_AZIENDA_DITTA_INDIVIDUALE = "DI";

    // Codice fiscale azienda agricola. 11 o 16 caratteri
    @JsonProperty("CUAA")
    private String cuaa;

    // Valori possibili: PF=Persona fisica, PG=Persona Giuridica
    @JsonProperty("tipoAzienda")
    private String tipoAzienda;

    // Denominazione per persone giuridiche, Cognome per persone fisiche, 150 caratteri
    @JsonProperty("denominazione")
    private String denominazione;

    // Nome se azienda è ditta individuale
    @JsonProperty("nomePF")
    private String nomePf;

    // Sesso se azienda è ditta individuale, M=Maschi, F=Femmina
    @JsonProperty("sessoPF")
    private String sessoPf;

    // Data nascita soggetto se l'azienda è una ditta individuale
    @JsonProperty("dataNascitaPF")
    private LocalDateTime dataNascitaPf;

    // Codice Belfiore comune o stato estero di nascita per le ditte individuali
    @JsonProperty("comuneNascitaPF")
    private String comuneNascitaPf;

    //<summary>
    // Codice tipo documento di riconoscimento 1 = Carta di identita, 2 = Patente auto, 3 = Tessera delle Ferrovie dello Stato,
    // 4 = Passaporto, 5 = Tessera postale, 6 = Altro documento, 7 = Libretto postale
    // </summary>
    @JsonProperty("TipoDocumento")
    private String tipoDocumento;

    // Numero documento di riconoscimento
    @JsonProperty("NumeroDocumento")
    private String numeroDocumento;

    // Data rilascio documento di identita
    @JsonProperty("DataDocumento")
    private LocalDateTime dataDocumento;

    // Data scadenza documento identita
    @JsonProperty("DataScadDocumento")
    private LocalDateTime dataScadDocumento;

    // Sigla provincia residenza per persone fisiche o sede legale per persone giuridiche
    @JsonProperty("provinciaResidenza")
    private String provinciaResidenza;

    // Denominazione comune residenza per persone fisiche o sede legale per persone giuridiche
    @JsonProperty("comuneResidenza")
    private String comuneResidenza;

    // Indirizzo residenza per persone fisiche o sede legale per persone giuridiche
    @JsonProperty("indirizzoResidenza")
    private String indirizzoResidenza;

    // Cap residenza per persone fisiche o sede legale per persone giuridiche
    @JsonProperty("capResidenza")
    private String capResidenza;

    // Codice stato estero residenza per persone fisiche o sede legale per persone giuridiche
    @JsonProperty("codiceStatoEsteroResidenza")
    private String codiceStatoEsteroResidenza;

    // Sigla provincia recapito, presente solo se diverso da residenza/sede legale
    @JsonProperty("provinciaRecapito")
    private String provinciaRecapito;

    // Denominazione comune recapito, presente solo se diverso da residenza/sede legale
    @JsonProperty("comuneRecapito")
    private String comuneRecapito;

    // Indirizzo recapito, presente solo se diverso da residenza/sede legale
    @JsonProperty("indirizzoRecapito")
    private String indirizzoRecapito;

    // CAP recapito, presente solo se diverso da residenza/sede legale
    @JsonProperty("capRecapito")
    private String capRecapito;

    // Codice stato estero recapito, presente solo se diverso da residenza/sede legale
    @JsonProperty("codiceStatoEsteroRecapito")
    private String codiceStatoEsteroRecapito;

    // Partita IVA Azienda
    @JsonProperty("partitaIVA")
    private String partitaIva;

    // Codice CCIAA di iscrizione al REA
    @JsonProperty("iscrizioneRea")
    private String iscrizioneRea;

    // Cod. CCIAA di iscr. al Registro Imprese
    @JsonProperty("iscrizioneRegistroImprese")
    private String iscrizioneRegistroImprese;

    // Codice I.N.P.S. azienda agricola, 15 caratteri
    @JsonProperty("codiceInps")
    private String codiceInps;

    // Codice di identificazione dell’OP competente (FEOGA)
    @JsonProperty("organismoPagatore")
    private String organismoPagatore;

    // Data prima costituzione fascicolo presso il CAA
    @JsonProperty("dataAperturaFascicolo")
    private LocalDateTime dataAperturaFascicolo;

    // Data chiusura/revoca fascicolo presso il CAA
    @JsonProperty("dataChiusuraFascicolo")
    private LocalDateTime dataChiusuraFascicolo;

    // Codice di riconoscimento della tipologia del detentore, 3 caratteri
    @JsonProperty("tipoDetentore")
    private String tipoDetentore;

    // Codice di identificazione dell’ufficio presso cui è custodito il fascicolo cartaceo
    @JsonProperty("detentore")
    private String detentore;

    // pec aziendale
    @JsonProperty("pec")
    private String pec;
    
    // attivita prevalente parix
    @JsonProperty("attivita")
    private String attivita;

    // Data validazione fascicolo
    @JsonProperty("dataValidazFascicolo")
    private LocalDateTime dataValidazFascicolo;

    // Numero della scheda di validazione

    @JsonProperty("schedaValidazione")
    private String schedaValidazione;

    // Data della scheda di validazione fascicolo
    @JsonProperty("dataSchedaValidazione")
    private LocalDateTime dataSchedaValidazione;

    // Data sottoscrizione mandato al CAA
    @JsonProperty("dataSottMandato")
    private LocalDateTime dataSottMandato;

    // Data validità controlli sul fascicolo
    @JsonProperty("dataElaborazione")
    private LocalDateTime dataElaborazione;

    public void setTipoAzienda(String tipoAzienda) {
        this.tipoAzienda = tipoAzienda;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public void setNomePf(String nomePf) {
        this.nomePf = nomePf;
    }

    public void setSessoPf(String sessoPf) {
        this.sessoPf = sessoPf;
    }

    public void setDataNascitaPf(LocalDateTime dataNascitaPf) {
        this.dataNascitaPf = dataNascitaPf;
    }

    public void setComuneNascitaPf(String comuneNascitaPf) {
        this.comuneNascitaPf = comuneNascitaPf;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public void setDataDocumento(LocalDateTime dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    public void setDataScadDocumento(LocalDateTime dataScadDocumento) {
        this.dataScadDocumento = dataScadDocumento;
    }

    public void setProvinciaResidenza(String provinciaResidenza) {
        this.provinciaResidenza = provinciaResidenza;
    }

    public void setComuneResidenza(String comuneResidenza) {
        this.comuneResidenza = comuneResidenza;
    }

    public void setIndirizzoResidenza(String indirizzoResidenza) {
        this.indirizzoResidenza = indirizzoResidenza;
    }

    public void setCapResidenza(String capResidenza) {
        this.capResidenza = capResidenza;
    }

    public void setCodiceStatoEsteroResidenza(String codiceStatoEsteroResidenza) {
        this.codiceStatoEsteroResidenza = codiceStatoEsteroResidenza;
    }

    public void setProvinciaRecapito(String provinciaRecapito) {
        this.provinciaRecapito = provinciaRecapito;
    }

    public void setComuneRecapito(String comuneRecapito) {
        this.comuneRecapito = comuneRecapito;
    }

    public void setIndirizzoRecapito(String indirizzoRecapito) {
        this.indirizzoRecapito = indirizzoRecapito;
    }

    public void setCapRecapito(String capRecapito) {
        this.capRecapito = capRecapito;
    }

    public void setCodiceStatoEsteroRecapito(String codiceStatoEsteroRecapito) {
        this.codiceStatoEsteroRecapito = codiceStatoEsteroRecapito;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public void setIscrizioneRea(String iscrizioneRea) {
        this.iscrizioneRea = iscrizioneRea;
    }

    public void setIscrizioneRegistroImprese(String iscrizioneRegistroImprese) {
        this.iscrizioneRegistroImprese = iscrizioneRegistroImprese;
    }

    public void setCodiceInps(String codiceInps) {
        this.codiceInps = codiceInps;
    }

    public void setOrganismoPagatore(String organismoPagatore) {
        this.organismoPagatore = organismoPagatore;
    }

    public void setDataAperturaFascicolo(LocalDateTime dataAperturaFascicolo) {
        this.dataAperturaFascicolo = dataAperturaFascicolo;
    }

    public void setDataChiusuraFascicolo(LocalDateTime dataChiusuraFascicolo) {
        this.dataChiusuraFascicolo = dataChiusuraFascicolo;
    }

    public void setTipoDetentore(String tipoDetentore) {
        this.tipoDetentore = tipoDetentore;
    }

    public void setDetentore(String detentore) {
        this.detentore = detentore;
    }

    public void setPec(String pec) {
        this.pec = pec;
    }

    public void setAttivita(String attivita) {
		this.attivita = attivita;
	}

	public void setDataValidazFascicolo(LocalDateTime dataValidazFascicolo) {
        this.dataValidazFascicolo = dataValidazFascicolo;
    }

    public void setSchedaValidazione(String schedaValidazione) {
        this.schedaValidazione = schedaValidazione;
    }

    public void setDataSchedaValidazione(LocalDateTime dataSchedaValidazione) {
        this.dataSchedaValidazione = dataSchedaValidazione;
    }

    public void setDataSottMandato(LocalDateTime dataSottMandato) {
        this.dataSottMandato = dataSottMandato;
    }

    public void setDataElaborazione(LocalDateTime dataElaborazione) {
        this.dataElaborazione = dataElaborazione;
    }

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getTipoAzienda() {
		return tipoAzienda;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public String getNomePf() {
		return nomePf;
	}

	public String getSessoPf() {
		return sessoPf;
	}

	public LocalDateTime getDataNascitaPf() {
		return dataNascitaPf;
	}

	public String getComuneNascitaPf() {
		return comuneNascitaPf;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public LocalDateTime getDataDocumento() {
		return dataDocumento;
	}

	public LocalDateTime getDataScadDocumento() {
		return dataScadDocumento;
	}

	public String getProvinciaResidenza() {
		return provinciaResidenza;
	}

	public String getComuneResidenza() {
		return comuneResidenza;
	}

	public String getIndirizzoResidenza() {
		return indirizzoResidenza;
	}

	public String getCapResidenza() {
		return capResidenza;
	}

	public String getCodiceStatoEsteroResidenza() {
		return codiceStatoEsteroResidenza;
	}

	public String getProvinciaRecapito() {
		return provinciaRecapito;
	}

	public String getComuneRecapito() {
		return comuneRecapito;
	}

	public String getIndirizzoRecapito() {
		return indirizzoRecapito;
	}

	public String getCapRecapito() {
		return capRecapito;
	}

	public String getCodiceStatoEsteroRecapito() {
		return codiceStatoEsteroRecapito;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public String getIscrizioneRea() {
		return iscrizioneRea;
	}

	public String getIscrizioneRegistroImprese() {
		return iscrizioneRegistroImprese;
	}

	public String getCodiceInps() {
		return codiceInps;
	}

	public String getOrganismoPagatore() {
		return organismoPagatore;
	}

	public LocalDateTime getDataAperturaFascicolo() {
		return dataAperturaFascicolo;
	}

	public LocalDateTime getDataChiusuraFascicolo() {
		return dataChiusuraFascicolo;
	}

	public String getTipoDetentore() {
		return tipoDetentore;
	}

	public String getDetentore() {
		return detentore;
	}

	public String getPec() {
		return pec;
	}

	public String getAttivita() {
		return attivita;
	}

	public LocalDateTime getDataValidazFascicolo() {
		return dataValidazFascicolo;
	}

	public String getSchedaValidazione() {
		return schedaValidazione;
	}

	public LocalDateTime getDataSchedaValidazione() {
		return dataSchedaValidazione;
	}

	public LocalDateTime getDataSottMandato() {
		return dataSottMandato;
	}

	public LocalDateTime getDataElaborazione() {
		return dataElaborazione;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AnagraficaAzienda [cuaa=");
		builder.append(cuaa);
		builder.append(", tipoAzienda=");
		builder.append(tipoAzienda);
		builder.append(", denominazione=");
		builder.append(denominazione);
		builder.append(", nomePf=");
		builder.append(nomePf);
		builder.append(", sessoPf=");
		builder.append(sessoPf);
		builder.append(", dataNascitaPf=");
		builder.append(dataNascitaPf);
		builder.append(", comuneNascitaPf=");
		builder.append(comuneNascitaPf);
		builder.append(", tipoDocumento=");
		builder.append(tipoDocumento);
		builder.append(", numeroDocumento=");
		builder.append(numeroDocumento);
		builder.append(", dataDocumento=");
		builder.append(dataDocumento);
		builder.append(", dataScadDocumento=");
		builder.append(dataScadDocumento);
		builder.append(", provinciaResidenza=");
		builder.append(provinciaResidenza);
		builder.append(", comuneResidenza=");
		builder.append(comuneResidenza);
		builder.append(", indirizzoResidenza=");
		builder.append(indirizzoResidenza);
		builder.append(", capResidenza=");
		builder.append(capResidenza);
		builder.append(", codiceStatoEsteroResidenza=");
		builder.append(codiceStatoEsteroResidenza);
		builder.append(", provinciaRecapito=");
		builder.append(provinciaRecapito);
		builder.append(", comuneRecapito=");
		builder.append(comuneRecapito);
		builder.append(", indirizzoRecapito=");
		builder.append(indirizzoRecapito);
		builder.append(", capRecapito=");
		builder.append(capRecapito);
		builder.append(", codiceStatoEsteroRecapito=");
		builder.append(codiceStatoEsteroRecapito);
		builder.append(", partitaIva=");
		builder.append(partitaIva);
		builder.append(", iscrizioneRea=");
		builder.append(iscrizioneRea);
		builder.append(", iscrizioneRegistroImprese=");
		builder.append(iscrizioneRegistroImprese);
		builder.append(", codiceInps=");
		builder.append(codiceInps);
		builder.append(", organismoPagatore=");
		builder.append(organismoPagatore);
		builder.append(", dataAperturaFascicolo=");
		builder.append(dataAperturaFascicolo);
		builder.append(", dataChiusuraFascicolo=");
		builder.append(dataChiusuraFascicolo);
		builder.append(", tipoDetentore=");
		builder.append(tipoDetentore);
		builder.append(", detentore=");
		builder.append(detentore);
		builder.append(", pec=");
		builder.append(pec);
		builder.append(", attivita=");
		builder.append(attivita);
		builder.append(", dataValidazFascicolo=");
		builder.append(dataValidazFascicolo);
		builder.append(", schedaValidazione=");
		builder.append(schedaValidazione);
		builder.append(", dataSchedaValidazione=");
		builder.append(dataSchedaValidazione);
		builder.append(", dataSottMandato=");
		builder.append(dataSottMandato);
		builder.append(", dataElaborazione=");
		builder.append(dataElaborazione);
		builder.append("]");
		return builder.toString();
	}

}
