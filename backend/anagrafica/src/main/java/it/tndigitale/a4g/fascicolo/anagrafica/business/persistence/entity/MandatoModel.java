package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "A4GT_MANDATO")
public class MandatoModel extends DetenzioneModel {
	private static final long serialVersionUID = -2164403113700316255L;
	
	@Lob
	@Column(name = "CONTRATTO", nullable = false)
	private byte[] contratto;

	@Column(name = "DATA_SOTTOSCRIZIONE", nullable = false)
	private LocalDate dataSottoscrizione;
	
	@OneToMany(mappedBy = "mandato", fetch = FetchType.LAZY)
	private List<RevocaImmediataModel> revocheImmediate;

	public byte[] getContratto() {
		return contratto;
	}

	public MandatoModel setContratto(byte[] contratto) {
		this.contratto = contratto;
		return this;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDENTIFICATIVO_SPORTELLO", referencedColumnName = "IDENTIFICATIVO")
	private SportelloModel sportello;

	public SportelloModel getSportello() {
		return sportello;
	}

	public MandatoModel setSportello(SportelloModel sportello) {
		this.sportello = sportello;
		return this;
	}

	public LocalDate getDataSottoscrizione() {
		return dataSottoscrizione;
	}
	public MandatoModel setDataSottoscrizione(LocalDate dataSottoscrizione) {
		this.dataSottoscrizione = dataSottoscrizione;
		return this;
	}

	public List<RevocaImmediataModel> getRevocheImmediate() {
		return revocheImmediate;
	}

	public MandatoModel setRevocheImmediate(List<RevocaImmediataModel> revocheImmediate) {
		this.revocheImmediate = revocheImmediate;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(contratto);
		result = prime * result + Objects.hash(dataSottoscrizione, sportello, revocheImmediate);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MandatoModel other = (MandatoModel) obj;
		return Arrays.equals(contratto, other.contratto) && Objects.equals(dataSottoscrizione, other.dataSottoscrizione)
				&& Objects.equals(sportello, other.sportello)
				&& Objects.equals(revocheImmediate, other.revocheImmediate);
	}
}
