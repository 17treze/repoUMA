package it.tndigitale.a4g.territorio.business.persistence.entity;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "A4GT_DOCUMENTO_CONDUZIONE")
public class DocumentoConduzioneModel extends EntitaDominioFascicolo {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CONDUZIONE_ID", referencedColumnName = "ID")
	@JoinColumn(name="CONDUZIONE_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private ConduzioneModel conduzione;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="TIPO_DOCUMENTO_ID", referencedColumnName = "ID")
	private TipoDocumentoConduzioneModel tipoDocumentoConduzione;

	@Column(name = "DATA_INSERIMENTO", nullable = false)
	private LocalDate dataInserimento;

	@Lob
	@Column(name = "DOCUMENTO", nullable = false)
	private byte[] contratto;

	@Column(name = "DATA_INIZIO_VALIDITA")
	private LocalDate dataInizioValidita;

	@Column(name = "DATA_FINE_VALIDITA")
	private LocalDate dataFineValidita;

	public ConduzioneModel getConduzione() {
		return conduzione;
	}

	public DocumentoConduzioneModel setConduzione(ConduzioneModel conduzione) {
		this.conduzione = conduzione;
		return this;
	}

	public TipoDocumentoConduzioneModel getTipoDocumentoConduzione() {
		return tipoDocumentoConduzione;
	}

	public DocumentoConduzioneModel setTipoDocumentoConduzione(TipoDocumentoConduzioneModel tipoDocumentoConduzione) {
		this.tipoDocumentoConduzione = tipoDocumentoConduzione;
		return this;
	}

	public LocalDate getDataInserimento() {
		return dataInserimento;
	}

	public DocumentoConduzioneModel setDataInserimento(LocalDate dataInserimento) {
		this.dataInserimento = dataInserimento;
		return this;
	}

	public byte[] getContratto() {
		return contratto;
	}

	public DocumentoConduzioneModel setContratto(byte[] contratto) {
		this.contratto = contratto;
		return this;
	}

	public LocalDate getDataInizioValidita() {
		return dataInizioValidita;
	}

	public DocumentoConduzioneModel setDataInizioValidita(LocalDate dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
		return this;
	}

	public LocalDate getDataFineValidita() {
		return dataFineValidita;
	}

	public DocumentoConduzioneModel setDataFineValidita(LocalDate dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DocumentoConduzioneModel)) return false;
		DocumentoConduzioneModel that = (DocumentoConduzioneModel) o;
		return Objects.equals(dataInserimento, that.dataInserimento) && Arrays.equals(contratto, that.contratto) && Objects.equals(dataInizioValidita, that.dataInizioValidita) && Objects.equals(dataFineValidita, that.dataFineValidita);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(dataInserimento, dataInizioValidita, dataFineValidita);
		result = 31 * result + Arrays.hashCode(contratto);
		return result;
	}
}
