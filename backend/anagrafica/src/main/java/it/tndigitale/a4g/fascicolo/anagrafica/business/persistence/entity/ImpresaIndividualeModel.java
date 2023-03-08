package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class ImpresaIndividualeModel {

	@Column(name = "DENOMINAZIONE", length = 200)
	private String denominazione;

	@Column(name = "PARTITA_IVA", length = 11)
	private String partitaIVA;

	@Column(name = "FORMA_GIURIDICA", length = 100)
	private String formaGiuridica;

	@Embedded
	private SedeModel sedeLegale;

	public String getDenominazione() {
		return denominazione;
	}

	public ImpresaIndividualeModel setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}

	public String getPartitaIVA() {
		return partitaIVA;
	}

	public ImpresaIndividualeModel setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
		return this;
	}

	public String getFormaGiuridica() {
		return formaGiuridica;
	}

	public ImpresaIndividualeModel setFormaGiuridica(String formaGiuridica) {
		this.formaGiuridica = formaGiuridica;
		return this;
	}

	public SedeModel getSedeLegale() {
		return sedeLegale;
	}

	public ImpresaIndividualeModel setSedeLegale(SedeModel sedeLegale) {
		this.sedeLegale = sedeLegale;
		return this;
	}
	
	public boolean isEmpty() {
		return denominazione == null
				&& partitaIVA == null
				&& formaGiuridica == null
				&& (sedeLegale == null || sedeLegale.isEmpty());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImpresaIndividualeModel that = (ImpresaIndividualeModel) o;
		return Objects.equals(denominazione, that.denominazione) &&
				Objects.equals(partitaIVA, that.partitaIVA) &&
				Objects.equals(formaGiuridica, that.formaGiuridica) &&
				Objects.equals(sedeLegale, that.sedeLegale);
	}

	@Override
	public int hashCode() {
		return Objects.hash(denominazione, partitaIVA, formaGiuridica, sedeLegale);
	}
}
