package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity model di domanda unica
 */
@Entity
@Table(name = "A4GT_DOMANDA")
//@NamedEntityGraph utilizzata nel caricamento EAGER delle domande con relative istruttorie
//per la generazione delle statistiche e sincronizzazioni
@NamedEntityGraph(name = "istruttorie.eager",
attributeNodes = {
		@NamedAttributeNode(value = "a4gtLavorazioneSostegnos", subgraph = "istr-subgraph")
	},
 subgraphs = {
		 @NamedSubgraph(
	      name = "istr-subgraph",
	      attributeNodes = {
	        @NamedAttributeNode("datiIstruttoreSuperficie"),
	        @NamedAttributeNode("datiIstruttoreDisModel"),
	        @NamedAttributeNode("datiIstruttoreZootecnia"),
	        @NamedAttributeNode("a4gdStatoLavSostegno")
	      }
	     )
	  }
)
public class DomandaUnicaModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
    private static final long serialVersionUID = 4215660608531666661L;

    @Column(name = "COD_ENTE_COMPILATORE")
    private BigDecimal codEnteCompilatore;

    @Column(name = "COD_MODULO_DOMANDA")
    private String codModuloDomanda;

    @Column(name = "CUAA_INTESTATARIO")
    private String cuaaIntestatario;

    @Column(name = "DESC_ENTE_COMPILATORE")
    private String descEnteCompilatore;

    @Column(name = "DESC_MODULO_DOMANDA")
    private String descModuloDomanda;

    @Temporal(TemporalType.DATE)
    @Column(name = "DT_PRESENTAZ_ORIGINARIA")
    private Date dtPresentazOriginaria;

    @Column(name = "DT_PRESENTAZIONE")
    private LocalDate dtPresentazione;

    @Temporal(TemporalType.DATE)
    @Column(name = "DT_PROTOCOLLAZ_ORIGINARIA")
    private Date dtProtocollazOriginaria;

    @Temporal(TemporalType.DATE)
    @Column(name = "DT_PROTOCOLLAZIONE")
    private Date dtProtocollazione;

    @Column(name = "NUM_DOMANDA_RETTIFICATA")
    private BigDecimal numDomandaRettificata;

    @Column(name = "NUMERO_DOMANDA")
    private BigDecimal numeroDomanda;

    @Column(name = "RAGIONE_SOCIALE")
    private String ragioneSociale;

//    @Column(name = "ANNULLO_RIDUZIONE")
//    private Boolean annulloRiduzione;

    @Column(name = "ANNO_CAMPAGNA", nullable = false)
    private Integer campagna;

    // campi aggiunti, 21 gennaio 2022, G.De Vincentiis
    @Column(name = "DT_PROTOCOLLAZ_ULT_MODIFICA", nullable = true)
    private Date dtProtocollazioneUltimaModifica;
    
    @Column(name = "NUMERO_DOMANDA_ULT_MODIFICA", nullable = true)
    private BigDecimal numeroDomandaUltimaModifica;
    
    public Date getDtProtocollazioneUltimaModifica() {
		return dtProtocollazioneUltimaModifica;
	}

	public void setDtProtocollazioneUltimaModifica(Date dtProtocollazioneUltimaModifica) {
		this.dtProtocollazioneUltimaModifica = dtProtocollazioneUltimaModifica;
	}

	public BigDecimal getNumeroDomandaUltimaModifica() {
		return numeroDomandaUltimaModifica;
	}

	public void setNumeroDomandaUltimaModifica(BigDecimal numeroDomandaUltimaModifica) {
		this.numeroDomandaUltimaModifica = numeroDomandaUltimaModifica;
	}

	// bi-directional many-to-one association to A4gtDatiLavorazione
    @OneToMany(mappedBy = "domandaUnicaModel", cascade = CascadeType.REMOVE)
    private Set<A4gtDatiLavorazione> a4gtDatiLavoraziones;

    // bi-directional many-to-one association to A4gtDatiPascolo
    @OneToMany(mappedBy = "domandaUnicaModel", cascade = CascadeType.REMOVE)
    private Set<A4gtDatiPascolo> a4gtDatiPascolos;

    // bi-directional many-to-one association to DichiarazioneDomandaUnicaModel
    @OneToMany(mappedBy = "domandaUnicaModel", cascade = CascadeType.REMOVE)
    private Set<DichiarazioneDomandaUnicaModel> dichiarazioni;

    // bi-directional many-to-one association to A4gtDatiFiltroDomanda
    @OneToMany(mappedBy = "domandaUnicaModel", cascade = CascadeType.REMOVE)
    private Set<A4gtDatiFiltroDomanda> a4gtDatiFiltroDomandas;

    @Enumerated(EnumType.STRING)
    private StatoDomanda stato;

    // bi-directional many-to-one association to A4gtEsitoControllo
    @OneToMany(mappedBy = "domandaUnicaModel", cascade = CascadeType.REMOVE)
    private Set<ErroreRicevibilitaModel> erroreRicevibilitaModels;

    // bi-directional many-to-one association to A4gtLavorazioneSostegno
//    @Transient
    @OneToMany(mappedBy = "domandaUnicaModel", cascade = CascadeType.REMOVE)
    private Set<IstruttoriaModel> a4gtLavorazioneSostegnos;

    // bi-directional many-to-one association to AllevamentoImpegnatoModel
    @OneToMany(mappedBy = "domandaUnica", cascade = CascadeType.REMOVE)
    private Set<AllevamentoImpegnatoModel> allevamentiImpegnati;

    // bi-directional many-to-one association to A4gtRichiestaSuperficie
    @OneToMany(mappedBy = "domandaUnicaModel", cascade = CascadeType.REMOVE)
    private Set<A4gtRichiestaSuperficie> a4gtRichiestaSuperficies;

    // bi-directional many-to-one association to A4gtTitoloDu
//	@OneToMany(mappedBy = "domandaUnicaModel", cascade = CascadeType.REMOVE)
//	private Set<A4gtTitoloDu> a4gtTitoloDus;

    // bi-directional many-to-one association to A4gtPascoloParticella
//    @Transient
    @OneToMany(mappedBy = "domandaUnicaModel", cascade = CascadeType.REMOVE)
    private Set<A4gtPascoloParticella> a4gtPascoloParticellas;

    //bi-directional many-to-one association to A4gtDatiErede
    @OneToMany(mappedBy = "domandaUnicaModel", cascade = CascadeType.REMOVE)
    private List<A4gtDatiErede> a4gtDatiEredes;
    
    //bi-directional many-to-one association to A4gtDatiErede
    @OneToMany(mappedBy = "domandaUnicaModel", cascade = CascadeType.REMOVE)
    private List<SostegnoModel> sostegni;

    @Column(name = "IBAN")
    private String iban;

    public BigDecimal getCodEnteCompilatore() {
        return codEnteCompilatore;
    }

    public DomandaUnicaModel setCodEnteCompilatore(BigDecimal codEnteCompilatore) {
        this.codEnteCompilatore = codEnteCompilatore;
        return this;
    }

    public String getCodModuloDomanda() {
        return codModuloDomanda;
    }

    public DomandaUnicaModel setCodModuloDomanda(String codModuloDomanda) {
        this.codModuloDomanda = codModuloDomanda;
        return this;
    }

    public String getCuaaIntestatario() {
        return cuaaIntestatario;
    }

    public DomandaUnicaModel setCuaaIntestatario(String cuaaIntestatario) {
        this.cuaaIntestatario = cuaaIntestatario;
        return this;
    }

    public String getDescEnteCompilatore() {
        return descEnteCompilatore;
    }

    public DomandaUnicaModel setDescEnteCompilatore(String descEnteCompilatore) {
        this.descEnteCompilatore = descEnteCompilatore;
        return this;
    }

    public String getDescModuloDomanda() {
        return descModuloDomanda;
    }

    public DomandaUnicaModel setDescModuloDomanda(String descModuloDomanda) {
        this.descModuloDomanda = descModuloDomanda;
        return this;
    }

    public Date getDtPresentazOriginaria() {
        return dtPresentazOriginaria;
    }

    public DomandaUnicaModel setDtPresentazOriginaria(Date dtPresentazOriginaria) {
        this.dtPresentazOriginaria = dtPresentazOriginaria;
        return this;
    }

    public LocalDate getDtPresentazione() {
        return dtPresentazione;
    }

    public DomandaUnicaModel setDtPresentazione(LocalDate dtPresentazione) {
        this.dtPresentazione = dtPresentazione;
        return this;
    }

    public Date getDtProtocollazOriginaria() {
        return dtProtocollazOriginaria;
    }

    public DomandaUnicaModel setDtProtocollazOriginaria(Date dtProtocollazOriginaria) {
        this.dtProtocollazOriginaria = dtProtocollazOriginaria;
        return this;
    }

    public Date getDtProtocollazione() {
        return dtProtocollazione;
    }

    public DomandaUnicaModel setDtProtocollazione(Date dtProtocollazione) {
        this.dtProtocollazione = dtProtocollazione;
        return this;
    }

    public BigDecimal getNumDomandaRettificata() {
        return numDomandaRettificata;
    }

    public DomandaUnicaModel setNumDomandaRettificata(BigDecimal numDomandaRettificata) {
        this.numDomandaRettificata = numDomandaRettificata;
        return this;
    }

    public BigDecimal getNumeroDomanda() {
        return numeroDomanda;
    }

    public DomandaUnicaModel setNumeroDomanda(BigDecimal numeroDomanda) {
        this.numeroDomanda = numeroDomanda;
        return this;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public DomandaUnicaModel setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
        return this;
    }

//    public Boolean getAnnulloRiduzione() {
//        return annulloRiduzione;
//    }
//
//    public DomandaUnicaModel setAnnulloRiduzione(Boolean annulloRiduzione) {
//        this.annulloRiduzione = annulloRiduzione;
//        return this;
//    }

    public Integer getCampagna() {
        return campagna;
    }

    public DomandaUnicaModel setCampagna(Integer campagna) {
        this.campagna = campagna;
        return this;
    }

    public Set<A4gtDatiLavorazione> getA4gtDatiLavoraziones() {
        return a4gtDatiLavoraziones;
    }

    public DomandaUnicaModel setA4gtDatiLavoraziones(Set<A4gtDatiLavorazione> a4gtDatiLavoraziones) {
        this.a4gtDatiLavoraziones = a4gtDatiLavoraziones;
        return this;
    }

    public Set<A4gtDatiPascolo> getA4gtDatiPascolos() {
        return a4gtDatiPascolos;
    }

    public DomandaUnicaModel setA4gtDatiPascolos(Set<A4gtDatiPascolo> a4gtDatiPascolos) {
        this.a4gtDatiPascolos = a4gtDatiPascolos;
        return this;
    }

    public Set<DichiarazioneDomandaUnicaModel> getDichiarazioni() {
        return dichiarazioni;
    }

    public DomandaUnicaModel setDichiarazioni(Set<DichiarazioneDomandaUnicaModel> dichiarazioni) {
        this.dichiarazioni = dichiarazioni;
        return this;
    }

    public Set<A4gtDatiFiltroDomanda> getA4gtDatiFiltroDomandas() {
        return a4gtDatiFiltroDomandas;
    }

    public DomandaUnicaModel setA4gtDatiFiltroDomandas(Set<A4gtDatiFiltroDomanda> a4gtDatiFiltroDomandas) {
        this.a4gtDatiFiltroDomandas = a4gtDatiFiltroDomandas;
        return this;
    }

    public StatoDomanda getStato() {
        return stato;
    }

    public DomandaUnicaModel setStato(final StatoDomanda stato) {
        this.stato = stato;
        return this;
    }

    public Set<ErroreRicevibilitaModel> getErroreRicevibilitaModels() {
        return erroreRicevibilitaModels;
    }

    public DomandaUnicaModel setErroreRicevibilitaModels(Set<ErroreRicevibilitaModel> erroreRicevibilitaModels) {
        this.erroreRicevibilitaModels = erroreRicevibilitaModels;
        return this;
    }

    public Set<IstruttoriaModel> getA4gtLavorazioneSostegnos() {
        return a4gtLavorazioneSostegnos;
    }

    public DomandaUnicaModel setA4gtLavorazioneSostegnos(Set<IstruttoriaModel> a4gtLavorazioneSostegnos) {
        this.a4gtLavorazioneSostegnos = a4gtLavorazioneSostegnos;
        return this;
    }

    public Set<AllevamentoImpegnatoModel> getAllevamentiImpegnati() {
        return allevamentiImpegnati;
    }

    public DomandaUnicaModel setAllevamentiImpegnati(Set<AllevamentoImpegnatoModel> allevamentiImpegnati) {
        this.allevamentiImpegnati = allevamentiImpegnati;
        return this;
    }

    public Set<A4gtRichiestaSuperficie> getA4gtRichiestaSuperficies() {
        return a4gtRichiestaSuperficies;
    }

    public DomandaUnicaModel setA4gtRichiestaSuperficies(Set<A4gtRichiestaSuperficie> a4gtRichiestaSuperficies) {
        this.a4gtRichiestaSuperficies = a4gtRichiestaSuperficies;
        return this;
    }

    public Set<A4gtPascoloParticella> getA4gtPascoloParticellas() {
        return a4gtPascoloParticellas;
    }

    public DomandaUnicaModel setA4gtPascoloParticellas(Set<A4gtPascoloParticella> a4gtPascoloParticellas) {
        this.a4gtPascoloParticellas = a4gtPascoloParticellas;
        return this;
    }

    public List<A4gtDatiErede> getA4gtDatiEredes() {
        return a4gtDatiEredes;
    }

    public DomandaUnicaModel setA4gtDatiEredes(List<A4gtDatiErede> a4gtDatiEredes) {
        this.a4gtDatiEredes = a4gtDatiEredes;
        return this;
    }

    public String getIban() {
        return iban;
    }

    public DomandaUnicaModel setIban(String iban) {
        this.iban = iban;
        return this;
    }

	public List<SostegnoModel> getSostegni() {
		return sostegni;
	}

	public void setSostegni(List<SostegnoModel> sostegni) {
		this.sostegni = sostegni;
	}


}
