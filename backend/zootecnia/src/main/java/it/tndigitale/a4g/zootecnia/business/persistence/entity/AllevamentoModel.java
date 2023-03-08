package it.tndigitale.a4g.zootecnia.business.persistence.entity;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;

@Entity
@Table(name = "A4GT_ALLEVAMENTO")
public class AllevamentoModel extends EntitaDominioFascicolo {
	private static final long serialVersionUID = -9150787703173740707L;

	@Column(name = "IDENTIFICATIVO", length = 20)
	private String identificativo;
	
	@Column(name = "IDENTIFICATIVO_FISCALE", length = 16)
	private String identificativoFiscale;

	@Column(name = "TIPOLOGIA_ALLEVAMENTO", length = 100)
	private String tipologiaAllevamento;
	
	@Column(name = "CF_PROPRIETARIO", length = 16)
	private String cfProprietario;
	
	@Column(name = "DENOMINAZIONE_PROPRIETARIO", length = 200)
	private String denominazioneProprietario;
	
	@Column(name = "CF_DETENTORE", length = 16)
	private String cfDetentore;
	
	@Column(name = "DENOMINAZIONE_DETENTORE", length = 200)
	private String denominazioneDetentore;
	
	@Column(name = "DT_INIZIO_DETENZIONE")
	private LocalDate dtInizioDetenzione;
	
	@Column(name = "DT_FINE_DETENZIONE")
	private LocalDate dtFineDetenzione;
	
	@Column(name = "SOCCIDA", length = 20)
	private String soccida;
	
	@Column(name = "SPECIE", length = 100)
	private String specie;
	
	@Column(name = "TIPOLOGIA_PRODUZIONE", length = 100)
	private String tipologiaProduzione;
	
	@Column(name = "ORIENTAMENTO_PRODUTTIVO", length = 100)
	private String orientamentoProduttivo;
	
	@Column(name = "DT_APERTURA_ALLEVAMENTO")
	private LocalDate dtAperturaAllevamento;
	
	@Column(name = "DT_CHIUSURA_ALLEVAMENTO")
	private LocalDate dtChiusuraAllevamento;
	
	@Column(name = "AUTORIZZAZIONE_SANITARIA_LATTE", length = 20)
	private String autorizzazioneSanitariaLatte;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_STRUTTURA", referencedColumnName = "ID")
    @JoinColumn(name="STRUTTURA_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private StrutturaAllevamentoModel strutturaAllevamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FASCICOLO_ID", referencedColumnName = "ID")
    @JoinColumn(name="FASCICOLO_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
    private FascicoloModel fascicolo;
	
	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public String getIdentificativoFiscale() {
		return identificativoFiscale;
	}

	public void setIdentificativoFiscale(String identificativoFiscale) {
		this.identificativoFiscale = identificativoFiscale;
	}

	public String getTipologiaAllevamento() {
		return tipologiaAllevamento;
	}

	public void setTipologiaAllevamento(String tipologiaAllevamento) {
		this.tipologiaAllevamento = tipologiaAllevamento;
	}

	public String getCfProprietario() {
		return cfProprietario;
	}

	public void setCfProprietario(String cfProprietario) {
		this.cfProprietario = cfProprietario;
	}

	public String getDenominazioneProprietario() {
		return denominazioneProprietario;
	}

	public void setDenominazioneProprietario(String denominazioneProprietario) {
		this.denominazioneProprietario = denominazioneProprietario;
	}

	public String getCfDetentore() {
		return cfDetentore;
	}

	public void setCfDetentore(String cfDetentore) {
		this.cfDetentore = cfDetentore;
	}

	public String getDenominazioneDetentore() {
		return denominazioneDetentore;
	}

	public void setDenominazioneDetentore(String denominazioneDetentore) {
		this.denominazioneDetentore = denominazioneDetentore;
	}

	public LocalDate getDtInizioDetenzione() {
		return dtInizioDetenzione;
	}

	public void setDtInizioDetenzione(LocalDate dtInizioDetenzione) {
		this.dtInizioDetenzione = dtInizioDetenzione;
	}

	public LocalDate getDtFineDetenzione() {
		return dtFineDetenzione;
	}

	public void setDtFineDetenzione(LocalDate dtFineDetenzione) {
		this.dtFineDetenzione = dtFineDetenzione;
	}

	public String getSoccida() {
		return soccida;
	}

	public void setSoccida(String soccida) {
		this.soccida = soccida;
	}

	public String getSpecie() {
		return specie;
	}

	public void setSpecie(String specie) {
		this.specie = specie;
	}

	public String getTipologiaProduzione() {
		return tipologiaProduzione;
	}

	public void setTipologiaProduzione(String tipologiaProduzione) {
		this.tipologiaProduzione = tipologiaProduzione;
	}

	public String getOrientamentoProduttivo() {
		return orientamentoProduttivo;
	}

	public void setOrientamentoProduttivo(String orientamentoProduttivo) {
		this.orientamentoProduttivo = orientamentoProduttivo;
	}

	public LocalDate getDtAperturaAllevamento() {
		return dtAperturaAllevamento;
	}

	public void setDtAperturaAllevamento(LocalDate dtAperturaAllevamento) {
		this.dtAperturaAllevamento = dtAperturaAllevamento;
	}

	public LocalDate getDtChiusuraAllevamento() {
		return dtChiusuraAllevamento;
	}

	public void setDtChiusuraAllevamento(LocalDate dtChiusuraAllevamento) {
		this.dtChiusuraAllevamento = dtChiusuraAllevamento;
	}

	public String getAutorizzazioneSanitariaLatte() {
		return autorizzazioneSanitariaLatte;
	}

	public void setAutorizzazioneSanitariaLatte(String autorizzazioneSanitariaLatte) {
		this.autorizzazioneSanitariaLatte = autorizzazioneSanitariaLatte;
	}

	public StrutturaAllevamentoModel getStrutturaAllevamento() {
		return strutturaAllevamento;
	}

	public void setStrutturaAllevamento(StrutturaAllevamentoModel strutturaAllevamento) {
		this.strutturaAllevamento = strutturaAllevamento;
	}

	public FascicoloModel getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(FascicoloModel fascicolo) {
		this.fascicolo = fascicolo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(autorizzazioneSanitariaLatte, cfDetentore, cfProprietario, denominazioneDetentore,
				denominazioneProprietario, dtAperturaAllevamento, dtChiusuraAllevamento, dtFineDetenzione,
				dtInizioDetenzione, fascicolo, identificativo, identificativoFiscale, orientamentoProduttivo,
				soccida, specie, strutturaAllevamento, tipologiaAllevamento, tipologiaProduzione);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AllevamentoModel other = (AllevamentoModel) obj;
		return Objects.equals(autorizzazioneSanitariaLatte, other.autorizzazioneSanitariaLatte)
				&& Objects.equals(cfDetentore, other.cfDetentore)
				&& Objects.equals(cfProprietario, other.cfProprietario)
				&& Objects.equals(denominazioneDetentore, other.denominazioneDetentore)
				&& Objects.equals(denominazioneProprietario, other.denominazioneProprietario)
				&& Objects.equals(dtAperturaAllevamento, other.dtAperturaAllevamento)
				&& Objects.equals(dtChiusuraAllevamento, other.dtChiusuraAllevamento)
				&& Objects.equals(dtFineDetenzione, other.dtFineDetenzione)
				&& Objects.equals(dtInizioDetenzione, other.dtInizioDetenzione)
				&& Objects.equals(fascicolo, other.fascicolo) && Objects.equals(identificativo, other.identificativo)
				&& Objects.equals(identificativoFiscale, other.identificativoFiscale)
				&& Objects.equals(orientamentoProduttivo, other.orientamentoProduttivo)
				&& Objects.equals(soccida, other.soccida)
				&& Objects.equals(specie, other.specie)
				&& Objects.equals(strutturaAllevamento, other.strutturaAllevamento)
				&& Objects.equals(tipologiaAllevamento, other.tipologiaAllevamento)
				&& Objects.equals(tipologiaProduzione, other.tipologiaProduzione);
	}
}
