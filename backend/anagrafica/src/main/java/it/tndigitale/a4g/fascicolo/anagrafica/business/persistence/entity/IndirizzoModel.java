package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IndirizzoModel implements Serializable {
    private static final long serialVersionUID = 7895070791059738259L;

    @Column(name = "DESCRIZIONE_ESTESA")
    private String descrizioneEstesa;

    private String toponimo;

    private String via;

    @Column(name = "NUMERO_CIVICO")
    private String numeroCivico;

    private String cap;

    @Column(name = "CODICE_ISTAT")
    private String codiceIstat;

    private String frazione;

    private String provincia;
    
    @Column(name = "COMUNE")
	private String comune;

    public String getDescrizioneEstesa() {
        return descrizioneEstesa;
    }

    public IndirizzoModel setDescrizioneEstesa(String descrizioneEstesa) {
        this.descrizioneEstesa = descrizioneEstesa;
        return this;
    }

    public String getToponimo() {
        return toponimo;
    }

    public IndirizzoModel setToponimo(String toponimo) {
        this.toponimo = toponimo;
        return this;
    }

    public String getVia() {
        return via;
    }

    public IndirizzoModel setVia(String via) {
        this.via = via;
        return this;
    }

    public String getNumeroCivico() {
        return numeroCivico;
    }

    public IndirizzoModel setNumeroCivico(String numeroCivico) {
        this.numeroCivico = numeroCivico;
        return this;
    }

    public String getCap() {
        return cap;
    }

    public IndirizzoModel setCap(String cap) {
        this.cap = cap;
        return this;
    }

    public String getCodiceIstat() {
        return codiceIstat;
    }

    public IndirizzoModel setCodiceIstat(String codiceIstat) {
        this.codiceIstat = codiceIstat;
        return this;
    }

    public String getFrazione() {
        return frazione;
    }

    public IndirizzoModel setFrazione(String frazione) {
        this.frazione = frazione;
        return this;
    }

    public String getProvincia() {
        return provincia;
    }

    public IndirizzoModel setProvincia(String provincia) {
        this.provincia = provincia;
        return this;
    }

    public String getComune() {
		return comune;
	}

	public IndirizzoModel setComune(String comune) {
		this.comune = comune;
		return this;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndirizzoModel that = (IndirizzoModel) o;
        return Objects.equals(descrizioneEstesa, that.descrizioneEstesa) &&
                Objects.equals(toponimo, that.toponimo) &&
                Objects.equals(via, that.via) &&
                Objects.equals(numeroCivico, that.numeroCivico) &&
                Objects.equals(cap, that.cap) &&
                Objects.equals(codiceIstat, that.codiceIstat) &&
                Objects.equals(frazione, that.frazione) &&
                Objects.equals(provincia, that.provincia) &&
                Objects.equals(comune, that.comune);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descrizioneEstesa, toponimo, via, numeroCivico, cap, codiceIstat, frazione, provincia, comune);
    }
}
