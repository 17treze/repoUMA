package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="TAB_AGRI_UMAL_ALLEGATI_CONSUNTIVI")
public class AllegatoConsuntivoModel extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = 3860626060681709952L;

	@Column(name="NOME_FILE", nullable = false, length = 50)
	private String nomeFile;

	@Column(name="DESCRIZIONE_FILE", nullable = true, length = 50)
	private String descrizione;

	@Column(name="TIPO_DOCUMENTO", nullable = true, length = 50)
	@Enumerated(EnumType.STRING)
	private TipoAllegatoConsuntivo tipoAllegato;

	@Lob
	@Column(name="DOCUMENTO")
	private byte[] documento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CONSUNTIVO")
	private ConsuntivoConsumiModel consuntivoModel;

	public String getNomeFile() {
		return nomeFile;
	}
	public AllegatoConsuntivoModel setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
		return this;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public AllegatoConsuntivoModel setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}
	public TipoAllegatoConsuntivo getTipoAllegato() {
		return tipoAllegato;
	}
	public AllegatoConsuntivoModel setTipoAllegato(TipoAllegatoConsuntivo tipoAllegato) {
		this.tipoAllegato = tipoAllegato;
		return this;
	}
	public byte[] getDocumento() {
		return documento;
	}
	public AllegatoConsuntivoModel setDocumento(byte[] documento) {
		this.documento = documento;
		return this;
	}
	public ConsuntivoConsumiModel getConsuntivoModel() {
		return consuntivoModel;
	}
	public AllegatoConsuntivoModel setConsuntivoModel(ConsuntivoConsumiModel consuntivoModel) {
		this.consuntivoModel = consuntivoModel;
		return this;
	}
}
