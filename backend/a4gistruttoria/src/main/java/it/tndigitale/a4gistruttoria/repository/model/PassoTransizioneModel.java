package it.tndigitale.a4gistruttoria.repository.model;

import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;


@Entity
@Table(name="A4GT_PASSO_TRANSIZIONE")
@NamedQuery(name="PassoTransizioneModel.findAll", query="SELECT a FROM PassoTransizioneModel a")
public class PassoTransizioneModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
    private static final long serialVersionUID = 2645687028342313786L;

    @Column(name="CODICE_ESITO")
	private String codiceEsito;

	@Column(name="CODICE_PASSO")
	@Enumerated(EnumType.STRING)
	private TipologiaPassoTransizione codicePasso;

	@Lob
	@Column(name="DATI_INPUT")
	private String datiInput;

	@Lob
	@Column(name="DATI_OUTPUT")
	private String datiOutput;

	@Lob
	@Column(name="DATI_SINTESI_LAVORAZIONE")
	private String datiSintesiLavorazione;

	private String esito;

	//bi-directional many-to-one association to TransizioneIstruttoriaModel
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_TRANSIZ_SOSTEGNO")
	private TransizioneIstruttoriaModel transizioneIstruttoria;
	
	//bi-directional many-to-one association to PassoTransizioneModel
	@OneToMany(mappedBy="passoLavorazione", cascade = CascadeType.REMOVE)
	private Set<A4gtAnomDomandaSostegno> anomalie;
	

	public PassoTransizioneModel() {
	}

	public String getCodiceEsito() {
		return this.codiceEsito;
	}

	public void setCodiceEsito(String codiceEsito) {
		this.codiceEsito = codiceEsito;
	}

	public TipologiaPassoTransizione getCodicePasso() {
		return this.codicePasso;
	}

	public void setCodicePasso(TipologiaPassoTransizione codicePasso) {
		this.codicePasso = codicePasso;
	}

	public String getDatiInput() {
		return this.datiInput;
	}

	public void setDatiInput(String datiInput) {
		this.datiInput = datiInput;
	}

	public String getDatiOutput() {
		return this.datiOutput;
	}

	public void setDatiOutput(String datiOutput) {
		this.datiOutput = datiOutput;
	}

	public String getDatiSintesiLavorazione() {
		return this.datiSintesiLavorazione;
	}

	public void setDatiSintesiLavorazione(String datiSintesiLavorazione) {
		this.datiSintesiLavorazione = datiSintesiLavorazione;
	}

	public String getEsito() {
		return this.esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public TransizioneIstruttoriaModel getTransizioneIstruttoria() {
		return this.transizioneIstruttoria;
	}

	public void setTransizioneIstruttoria(TransizioneIstruttoriaModel transizioneIstruttoria) {
		this.transizioneIstruttoria = transizioneIstruttoria;
	}

	public Set<A4gtAnomDomandaSostegno> getAnomalie() {
		return anomalie;
	}

	public PassoTransizioneModel setAnomalie(Set<A4gtAnomDomandaSostegno> anomalie) {
		this.anomalie = anomalie;
		return this;
	}
}