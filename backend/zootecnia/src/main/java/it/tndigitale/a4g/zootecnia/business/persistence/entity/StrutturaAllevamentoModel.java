package it.tndigitale.a4g.zootecnia.business.persistence.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;

@Entity
@Table(name = "A4GT_STRUTTURA")
public class StrutturaAllevamentoModel extends EntitaDominioFascicolo {
	private static final long serialVersionUID = 2513890223601638482L;

	@Column(name = "IDENTIFICATIVO", length = 100)
	private String identificativo;
	
	@Column(name = "INDIRIZZO", length = 200)
	private String indirizzo;
	
	@Column(name = "CAP", length = 100)
	private String cap;
	
	@Column(name = "LOCALITA", length = 200)
	private String localita;
	
	@Column(name = "COMUNE", length = 200)
	private String comune;
	
	@Column(name = "LATITUDINE", length = 100)
	private String latitudine;
	
	@Column(name = "LONGITUDINE", length = 100)
	private String longitudine;
	
	@Column(name = "FOGLIO", length = 100)
	private String foglio;
	
	@Column(name = "SEZIONE", length = 100)
	private String sezione;
	
	@Column(name = "PARTICELLA", length = 100)
	private String particella;
	
	@Column(name = "SUBALTERNO", length = 100)
	private String subalterno;
	
	@Column(name = "CUAA", length = 16)
	private String cuaa;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "strutturaAllevamento")
	private List<AllevamentoModel> allevamenti;

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(String latitudine) {
		this.latitudine = latitudine;
	}

	public String getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(String longitudine) {
		this.longitudine = longitudine;
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getSubalterno() {
		return subalterno;
	}

	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public List<AllevamentoModel> getAllevamenti() {
		return allevamenti;
	}

	public void setAllevamenti(List<AllevamentoModel> allevamenti) {
		this.allevamenti = allevamenti;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cap, comune, cuaa, foglio, identificativo, indirizzo, latitudine, localita,
				longitudine, particella, sezione, subalterno);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StrutturaAllevamentoModel other = (StrutturaAllevamentoModel) obj;
		return Objects.equals(cap, other.cap)
				&& Objects.equals(comune, other.comune) && Objects.equals(cuaa, other.cuaa)
				&& Objects.equals(foglio, other.foglio) && Objects.equals(identificativo, other.identificativo)
				&& Objects.equals(indirizzo, other.indirizzo) && Objects.equals(latitudine, other.latitudine)
				&& Objects.equals(localita, other.localita) && Objects.equals(longitudine, other.longitudine)
				&& Objects.equals(particella, other.particella) && Objects.equals(sezione, other.sezione)
				&& Objects.equals(subalterno, other.subalterno);
	}
}
