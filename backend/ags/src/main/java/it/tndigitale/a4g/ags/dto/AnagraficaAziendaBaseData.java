package it.tndigitale.a4g.ags.dto;

import java.util.Date;

public class AnagraficaAziendaBaseData {

    private final String naturaGiuridica;
    private final String nome;
    private final String cognome;
    private final String sessoPf;
    private final Date dataNascita;
    private final String comuneNascita;
    private final String denominazione;
    private final String comuneResidenza;
    private final String provinciaResidenza;
    private final String capResidenza;
    private final String indirizzoResidenza;
    private final String comuneRecapito;
    private final String provinciaRecapito;
    private final String capRecapito;
    private final String indirizzoRecapito;
    private final String partitaIva;
    private final String iscrizioneRea;
    private final String organismoPagatore;
    private final Date dataAperturaFascicolo;
    private final Date dataChiusuraFascicolo;
    private final String pec;
    private final String attivita;

    public AnagraficaAziendaBaseData(String naturaGiuridica, String nome, String cognome, String sessoPf,
                                     Date dataNascita, String comuneNascita, String denominazione,
                                     String comuneResidenza, String provinciaResidenza, String capResidenza,
                                     String indirizzoResidenza, String comuneRecapito, String provinciaRecapito,
                                     String capRecapito, String indirizzoRecapito, String partitaIva,
                                     String iscrizioneRea, String organismoPagatore, Date dataAperturaFascicolo,
                                     Date dataChiusuraFascicolo, String pec, String attivita) {
        this.naturaGiuridica = naturaGiuridica;
        this.nome = nome;
        this.cognome = cognome;
        this.sessoPf = sessoPf;
        this.dataNascita = dataNascita;
        this.comuneNascita = comuneNascita;
        this.denominazione = denominazione;
        this.comuneResidenza = comuneResidenza;
        this.provinciaResidenza = provinciaResidenza;
        this.capResidenza = capResidenza;
        this.indirizzoResidenza = indirizzoResidenza;
        this.comuneRecapito = comuneRecapito;
        this.provinciaRecapito = provinciaRecapito;
        this.capRecapito = capRecapito;
        this.indirizzoRecapito = indirizzoRecapito;
        this.partitaIva = partitaIva;
        this.iscrizioneRea = iscrizioneRea;
        this.organismoPagatore = organismoPagatore;
        this.dataAperturaFascicolo = dataAperturaFascicolo;
        this.dataChiusuraFascicolo = dataChiusuraFascicolo;
        this.pec = pec;
        this.attivita = attivita;
    }

    public String getNaturaGiuridica() {
        return naturaGiuridica;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getSessoPf() {
        return sessoPf;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public String getComuneNascita() {
        return comuneNascita;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public String getComuneResidenza() {
        return comuneResidenza;
    }

    public String getProvinciaResidenza() {
        return provinciaResidenza;
    }

    public String getCapResidenza() {
        return capResidenza;
    }

    public String getIndirizzoResidenza() {
        return indirizzoResidenza;
    }

    public String getComuneRecapito() {
        return comuneRecapito;
    }

    public String getProvinciaRecapito() {
        return provinciaRecapito;
    }

    public String getCapRecapito() {
        return capRecapito;
    }

    public String getIndirizzoRecapito() {
        return indirizzoRecapito;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public String getIscrizioneRea() {
        return iscrizioneRea;
    }

    public String getOrganismoPagatore() {
        return organismoPagatore;
    }

    public Date getDataAperturaFascicolo() {
        return dataAperturaFascicolo;
    }

    public Date getDataChiusuraFascicolo() {
        return dataChiusuraFascicolo;
    }

    public String getPec() {
        return pec;
    }

	public String getAttivita() {
		return attivita;
	}
}
