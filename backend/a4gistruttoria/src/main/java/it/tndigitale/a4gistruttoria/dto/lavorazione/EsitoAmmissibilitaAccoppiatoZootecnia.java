package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.Objects;
import java.util.Optional;

public class EsitoAmmissibilitaAccoppiatoZootecnia {

	private Optional<Boolean> infoAgricoltoreAttivo;
	private Optional<Boolean> agricoltoreAttivo;
	private Optional<Boolean> campione;
	private Optional<Boolean> esitoControlliInLoco;
	private Optional<Boolean> riduzioni;
	private Optional<Boolean> ubaMinime;
	private Optional<InterventiSanzionati> interventiSanzionati;
	private Optional<Boolean> domandaIntegrativa;
	
	private EsitoAmmissibilitaAccoppiatoZootecnia() {
		// Private empty constructor to prevent initialization without builder
	}
	
	public static EsitoAmmissibilitaAccoppiatoZootecnia empty() {
		return new EsitoAmmissibilitaAccoppiatoZootecnia()
				.withAgricoltoreAttivo(Optional.empty())
				.withInfoAgricoltoreAttivo(Optional.empty())
				.withDomandaIntegrativa(Optional.empty())
				.withCampione(Optional.empty())
				.withInterventiSanzionati(Optional.empty())
				.withUbaMinime(Optional.empty())
				.withEsitoControlliInLoco(Optional.empty())
				.withRiduzioni(Optional.empty());
	}

	public Optional<Boolean> isInfoAgricoltoreAttivo() {
		return infoAgricoltoreAttivo;
	}

	public void setInfoAgricoltoreAttivo(Optional<Boolean> infoAgricoltoreAttivo) {
		this.infoAgricoltoreAttivo = infoAgricoltoreAttivo;
	}

	public Optional<Boolean> isAgricoltoreAttivo() {
		return agricoltoreAttivo;
	}

	public void setAgricoltoreAttivo(Optional<Boolean> agricoltoreAttivo) {
		this.agricoltoreAttivo = agricoltoreAttivo;
	}

	public Optional<Boolean> isCampione() {
		return campione;
	}

	public void setCampione(Optional<Boolean> campione) {
		this.campione = campione;
	}

	public Optional<Boolean> isEsitoControlliInLoco() {
		return esitoControlliInLoco;
	}

	public void setEsitoControlliInLoco(Optional<Boolean> esitoControlliInLoco) {
		this.esitoControlliInLoco = esitoControlliInLoco;
	}

	public Optional<Boolean> isRiduzioni() {
		return riduzioni;
	}

	public void setRiduzioni(Optional<Boolean> riduzioni) {
		this.riduzioni = riduzioni;
	}

	public Optional<Boolean> isUbaMinime() {
		return ubaMinime;
	}

	public void setUbaMinime(Optional<Boolean> ubaMinime) {
		this.ubaMinime = ubaMinime;
	}

	public Optional<InterventiSanzionati> getInterventiSanzionati() {
		return interventiSanzionati;
	}

	public void setInterventiSanzionati(Optional<InterventiSanzionati> interventiSanzionati) {
		this.interventiSanzionati = interventiSanzionati;
	}

	public Optional<Boolean> isDomandaIntegrativa() {
		return domandaIntegrativa;
	}

	public void setDomandaIntegrativa(Optional<Boolean> domandaIntegrativa) {
		this.domandaIntegrativa = domandaIntegrativa;
	}

	public EsitoAmmissibilitaAccoppiatoZootecnia withInfoAgricoltoreAttivo(Optional<Boolean> infoAgricoltoreAttivo) {
		this.infoAgricoltoreAttivo = infoAgricoltoreAttivo;
		return this;
	}

	public EsitoAmmissibilitaAccoppiatoZootecnia withAgricoltoreAttivo(Optional<Boolean> agricoltoreAttivo) {
		this.agricoltoreAttivo = agricoltoreAttivo;
		return this;
	}

	public EsitoAmmissibilitaAccoppiatoZootecnia withCampione(Optional<Boolean> campione) {
		this.campione = campione;
		return this;
	}

	public EsitoAmmissibilitaAccoppiatoZootecnia withEsitoControlliInLoco(Optional<Boolean> esitoControlliInLoco) {
		this.esitoControlliInLoco = esitoControlliInLoco;
		return this;
	}

	public EsitoAmmissibilitaAccoppiatoZootecnia withRiduzioni(Optional<Boolean> riduzioni) {
		this.riduzioni = riduzioni;
		return this;
	}
	
	public EsitoAmmissibilitaAccoppiatoZootecnia withRiduzioniPresent() {
		this.riduzioni = Optional.of(true);
		return this;
	}

	public EsitoAmmissibilitaAccoppiatoZootecnia withUbaMinime(Optional<Boolean> ubaMinime) {
		this.ubaMinime = ubaMinime;
		return this;
	}

	public EsitoAmmissibilitaAccoppiatoZootecnia withInterventiSanzionati(Optional<InterventiSanzionati> interventiSanzionati) {
		this.interventiSanzionati = interventiSanzionati;
		return this;
	}

	public EsitoAmmissibilitaAccoppiatoZootecnia withDomandaIntegrativa(Optional<Boolean> domandaIntegrativa) {
		this.domandaIntegrativa = domandaIntegrativa;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(agricoltoreAttivo, campione, domandaIntegrativa, 
				esitoControlliInLoco, infoAgricoltoreAttivo, interventiSanzionati, 
				// riduzioni, --> Rimosso per controlli di uguaglianza e calcolo foglia
				ubaMinime);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EsitoAmmissibilitaAccoppiatoZootecnia other = (EsitoAmmissibilitaAccoppiatoZootecnia) obj;
		
		return Objects.equals(agricoltoreAttivo, other.agricoltoreAttivo) 
				&& Objects.equals(campione, other.campione)
				&& Objects.equals(domandaIntegrativa, other.domandaIntegrativa)
				&& Objects.equals(esitoControlliInLoco, other.esitoControlliInLoco)
				&& Objects.equals(infoAgricoltoreAttivo, other.infoAgricoltoreAttivo)
				&& Objects.equals(interventiSanzionati, other.interventiSanzionati)
				// && Objects.equals(riduzioni, other.riduzioni) --> Rimosso per controlli di uguaglianza e calcolo foglia
				&& Objects.equals(riduzioni.isPresent(), other.riduzioni.isPresent())
				&& Objects.equals(ubaMinime, other.ubaMinime);
	}

	@Override
	public String toString() {
		return "EsitoAmmissibilitaAccoppiatoZootecnia [infoAgricoltoreAttivo=" + infoAgricoltoreAttivo
				+ ", agricoltoreAttivo=" + agricoltoreAttivo + ", campione=" + campione + ", esitoControlliInLoco="
				+ esitoControlliInLoco + ", riduzioni=" + riduzioni + ", ubaMinime=" + ubaMinime
				+ ", interventiSanzionati=" + interventiSanzionati + ", domandaIntegrativa=" + domandaIntegrativa + "]";
	}
}