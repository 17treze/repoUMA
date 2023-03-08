package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the A4GT_LAVORAZIONE_SOSTEGNO database table.
 */
@Entity
@Table(name = "A4GT_ISTRUTTORIA")
@NamedQuery(name = "IstruttoriaModel.findAll", query = "SELECT a FROM IstruttoriaModel a")
public class IstruttoriaModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
    private static final long serialVersionUID = 786842538280784234L;

    @Enumerated(EnumType.STRING)
    private Sostegno sostegno;

    // bi-directional many-to-one association to A4gdStatoLavSostegno
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_STATO_LAVORAZIONE")
    private A4gdStatoLavSostegno a4gdStatoLavSostegno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOMANDA")
    private DomandaUnicaModel domandaUnicaModel;

    @Column(name = "BLOCCATA_BOOL")
    private Boolean bloccataBool = Boolean.FALSE;

    @Column(name = "ERRORE_CALCOLO")
    private Boolean erroreCalcolo = Boolean.FALSE;

    @Column(name = "DATA_ULTIMO_CALCOLO")
    private LocalDateTime dataUltimoCalcolo;

    @Enumerated(EnumType.STRING)
    private TipoIstruttoria tipologia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ELENCO_LIQUIDAZIONE")
    private ElencoLiquidazioneModel elencoLiquidazione;

    // bi-directional many-to-one association to TransizioneIstruttoriaModel
    @OneToMany(mappedBy = "istruttoria", cascade = CascadeType.REMOVE)
    @OrderBy(value = "dataEsecuzione DESC")
    private Set<TransizioneIstruttoriaModel> transizioni;

    //bi-directional many-to-one association to A4gtDatiIstruttoreDi
    @OneToOne(mappedBy = "istruttoria", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private DatiIstruttoreDisModel datiIstruttoreDisModel;

    //bi-directional many-to-one association to A4gtDatiIstruttoreDi
    @OneToOne(mappedBy = "istruttoria", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private DatiIstruttoreSuperficieModel datiIstruttoreSuperficie;

    //bi-directional many-to-one association to A4gtDatiIstruttoreDi
    @OneToOne(mappedBy = "istruttoria", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private DatiIstruttoreZootecniaModel datiIstruttoreZootecnia;

    //bi-directional many-to-one association to DatiIstruttoreDisPascoliModel
    @OneToMany(mappedBy = "istruttoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DatiIstruttoreDisPascoliModel> datiIstruttoreDisPascoli;
    
    // bi-directional many-to-one association to A4gtDatiParticellaColtura
    @OneToMany(mappedBy = "istruttoria", cascade = CascadeType.REMOVE)
    private Set<A4gtDatiParticellaColtura> datiParticellaColtura;

    public Sostegno getSostegno() {
        return sostegno;
    }

    public void setSostegno(Sostegno sostegno) {
        this.sostegno = sostegno;
    }

    @Deprecated
    public A4gdStatoLavSostegno getA4gdStatoLavSostegno() {
        return a4gdStatoLavSostegno;
    }

    @Transient
    public StatoIstruttoria getStato() {
        return StatoIstruttoria.valueOfByIdentificativo(getA4gdStatoLavSostegno().getIdentificativo());
    }

    public IstruttoriaModel setA4gdStatoLavSostegno(A4gdStatoLavSostegno a4gdStatoLavSostegno) {
        this.a4gdStatoLavSostegno = a4gdStatoLavSostegno;
        return this;
    }

    public DomandaUnicaModel getDomandaUnicaModel() {
        return domandaUnicaModel;
    }

    public IstruttoriaModel setDomandaUnicaModel(DomandaUnicaModel domandaUnicaModel) {
        this.domandaUnicaModel = domandaUnicaModel;
        return this;
    }

    public Boolean getBloccataBool() {
        return bloccataBool;
    }

    public IstruttoriaModel setBloccataBool(Boolean bloccataBool) {
        this.bloccataBool = bloccataBool;
        return this;
    }

    public Boolean getErroreCalcolo() {
        return erroreCalcolo;
    }

    public IstruttoriaModel setErroreCalcolo(Boolean erroreCalcolo) {
        this.erroreCalcolo = erroreCalcolo;
        return this;
    }

    public LocalDateTime getDataUltimoCalcolo() {
        return dataUltimoCalcolo;
    }

    public IstruttoriaModel setDataUltimoCalcolo(LocalDateTime dataUltimoCalcolo) {
        this.dataUltimoCalcolo = dataUltimoCalcolo;
        return this;
    }

    public TipoIstruttoria getTipologia() {
        return tipologia;
    }

    public IstruttoriaModel setTipologia(TipoIstruttoria tipologia) {
        this.tipologia = tipologia;
        return this;
    }

    public ElencoLiquidazioneModel getElencoLiquidazione() {
        return elencoLiquidazione;
    }

    public IstruttoriaModel setElencoLiquidazione(ElencoLiquidazioneModel elencoLiquidazione) {
        this.elencoLiquidazione = elencoLiquidazione;
        return this;
    }

    public Set<TransizioneIstruttoriaModel> getTransizioni() {
        return transizioni;
    }

    public IstruttoriaModel setTransizioni(Set<TransizioneIstruttoriaModel> transizioni) {
        this.transizioni = transizioni;
        return this;
    }

    public DatiIstruttoreDisModel getDatiIstruttoreDisModel() {
        return datiIstruttoreDisModel;
    }

    public IstruttoriaModel setDatiIstruttoreDisModel(DatiIstruttoreDisModel datiIstruttoreDisModel) {
        this.datiIstruttoreDisModel = datiIstruttoreDisModel;
        return this;
    }

    public DatiIstruttoreSuperficieModel getDatiIstruttoreSuperficie() {
        return datiIstruttoreSuperficie;
    }

    public IstruttoriaModel setDatiIstruttoreSuperficie(DatiIstruttoreSuperficieModel datiIstruttoreSuperficie) {
        this.datiIstruttoreSuperficie = datiIstruttoreSuperficie;
        return this;
    }

    public DatiIstruttoreZootecniaModel getDatiIstruttoreZootecnia() {
        return datiIstruttoreZootecnia;
    }

    public IstruttoriaModel setDatiIstruttoreZootecnia(DatiIstruttoreZootecniaModel datiIstruttoreZootecnia) {
        this.datiIstruttoreZootecnia = datiIstruttoreZootecnia;
        return this;
    }

    public Set<DatiIstruttoreDisPascoliModel> getDatiIstruttoreDisPascoli() {
        return datiIstruttoreDisPascoli;
    }

    public IstruttoriaModel setDatiIstruttoreDisPascoli(Set<DatiIstruttoreDisPascoliModel> datiIstruttoreDisPascoli) {
        this.datiIstruttoreDisPascoli = datiIstruttoreDisPascoli;
        return this;
    }

    public DatiIstruttoreDisPascoliModel addDatiIstruttoreDisPascoli(DatiIstruttoreDisPascoliModel a4gtIstruttoreDisPascoli) {
        getDatiIstruttoreDisPascoli().add(a4gtIstruttoreDisPascoli);
        a4gtIstruttoreDisPascoli.setIstruttoria(this);

        return a4gtIstruttoreDisPascoli;
    }

    public void addAllDatiIstruttoreDisPascoli(Set<DatiIstruttoreDisPascoliModel> listaPascoliModel) {
    	if (getDatiIstruttoreDisPascoli() == null ) {
    		setDatiIstruttoreDisPascoli(new HashSet<DatiIstruttoreDisPascoliModel>());
    	}
        listaPascoliModel.forEach(this::addDatiIstruttoreDisPascoli);
    }

	public Set<A4gtDatiParticellaColtura> getDatiParticellaColtura() {
		return datiParticellaColtura;
	}

	public void setDatiParticellaColtura(Set<A4gtDatiParticellaColtura> datiParticellaColtura) {
		this.datiParticellaColtura = datiParticellaColtura;
	}

}
