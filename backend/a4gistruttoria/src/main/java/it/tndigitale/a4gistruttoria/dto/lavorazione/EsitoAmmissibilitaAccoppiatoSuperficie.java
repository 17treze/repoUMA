package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.Objects;

public class EsitoAmmissibilitaAccoppiatoSuperficie {

	private Boolean infoAgricoltoreAttivo;
	private Boolean agricoltoreAttivo;
	private Boolean campione;
	private Boolean sigeco;
	private Boolean olivo75;
	private Boolean olivoNazionale;
	private Boolean olivoQualita;
	private Boolean superficieMinima;

	public Boolean isInfoAgricoltoreAttivo() {
		return infoAgricoltoreAttivo;
	}

	public void setInfoAgricoltoreAttivo(Boolean infoAgricoltoreAttivo) {
		this.infoAgricoltoreAttivo = infoAgricoltoreAttivo;
	}

	public EsitoAmmissibilitaAccoppiatoSuperficie withInfoAgricoltoreAttivo(Boolean infoAgricoltoreAttivo) {
		this.infoAgricoltoreAttivo = infoAgricoltoreAttivo;
		return this;
	}

	public Boolean isAgricoltoreAttivo() {
		return agricoltoreAttivo;
	}

	public void setAgricoltoreAttivo(Boolean agricoltoreAttivo) {
		this.agricoltoreAttivo = agricoltoreAttivo;
	}

	public EsitoAmmissibilitaAccoppiatoSuperficie withAgricoltoreAttivo(Boolean agricoltoreAttivo) {
		this.agricoltoreAttivo = agricoltoreAttivo;
		return this;
	}

	public Boolean isCampione() {
		return campione;
	}

	public void setCampione(Boolean campione) {
		this.campione = campione;
	}

	public EsitoAmmissibilitaAccoppiatoSuperficie withCampione(Boolean campione) {
		this.campione = campione;
		return this;
	}

	public Boolean isSigeco() {
		return sigeco;
	}

	public void setSigeco(Boolean sigeco) {
		this.sigeco = sigeco;
	}

	public EsitoAmmissibilitaAccoppiatoSuperficie withSigeco(Boolean sigeco) {
		this.sigeco = sigeco;
		return this;
	}

	public Boolean isOlivo75() {
		return olivo75;
	}

	public void setOlivo75(Boolean olivo75) {
		this.olivo75 = olivo75;
	}

	public EsitoAmmissibilitaAccoppiatoSuperficie withOlivo75(Boolean olivo75) {
		this.olivo75 = olivo75;
		return this;
	}

	public Boolean isOlivoNazionale() {
		return olivoNazionale;
	}

	public void setOlivoNazionale(Boolean olivoNazionale) {
		this.olivoNazionale = olivoNazionale;
	}

	public EsitoAmmissibilitaAccoppiatoSuperficie withOlivoNazionale(Boolean olivoNazionale) {
		this.olivoNazionale = olivoNazionale;
		return this;
	}

	public Boolean isOlivoQualita() {
		return olivoQualita;
	}

	public void setOlivoQualita(Boolean olivoQualita) {
		this.olivoQualita = olivoQualita;
	}

	public EsitoAmmissibilitaAccoppiatoSuperficie withOlivoQualita(Boolean olivoQualita) {
		this.olivoQualita = olivoQualita;
		return this;
	}

	public Boolean isSuperficieMinima() {
		return superficieMinima;
	}

	public void setSuperficieMinima(Boolean superficieMinima) {
		this.superficieMinima = superficieMinima;
	}

	public EsitoAmmissibilitaAccoppiatoSuperficie withSuperficieMinima(Boolean superficieMinima) {
		this.superficieMinima = superficieMinima;
		return this;
	}

	@Override
	public int hashCode() {
		// Nota: olivoNazionale e olivoQualita non influiscono ai fini del calcolo
		// della foglia, quindi sono esclusi dal calcolo dell'hash
		return Objects.hash(
				agricoltoreAttivo, 
				campione, 
				infoAgricoltoreAttivo, 
				sigeco, 
				olivo75,
				superficieMinima);
	}

	@Override
	public boolean equals(Object obj) {
		// Nota: olivoNazionale e olivoQualita non influiscono ai fini del calcolo
		// della foglia, quindi sono esclusi dall'equals
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EsitoAmmissibilitaAccoppiatoSuperficie other = (EsitoAmmissibilitaAccoppiatoSuperficie) obj;
		return Objects.equals(agricoltoreAttivo, other.agricoltoreAttivo) 
				&& Objects.equals(campione, other.campione)
				&& Objects.equals(infoAgricoltoreAttivo, other.infoAgricoltoreAttivo)
				&& Objects.equals(sigeco, other.sigeco)
				&& Objects.equals(olivo75, other.olivo75)
				&& Objects.equals(superficieMinima, other.superficieMinima);
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EsitoAmmissibilitaAccoppiatoSuperficie [infoAgricoltoreAttivo=");
		builder.append(infoAgricoltoreAttivo);
		builder.append(", agricoltoreAttivo=");
		builder.append(agricoltoreAttivo);
		builder.append(", campione=");
		builder.append(campione);
		builder.append(", sigeco=");
		builder.append(sigeco);
		builder.append(", olivo75=");
		builder.append(olivo75);
		builder.append(", olivoNazionale=");
		builder.append(olivoNazionale);
		builder.append(", olivoQualita=");
		builder.append(olivoQualita);
		builder.append(", superficieMinima=");
		builder.append(superficieMinima);
		builder.append("]");
		return builder.toString();
	}
}