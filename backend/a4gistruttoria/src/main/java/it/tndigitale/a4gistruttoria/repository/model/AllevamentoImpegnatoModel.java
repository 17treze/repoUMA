package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name="A4GT_ALLEVAMENTO_IMPEGNATO")
@NamedQuery(name="AllevamentoImpegnatoModel.findAll", query="SELECT a FROM AllevamentoImpegnatoModel a")
public class AllevamentoImpegnatoModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 797188138849366065L;

	@Column(name="CODICE_SPECIE")
	private String codiceSpecie;

	@Lob
	@Column(name="DATI_ALLEVAMENTO")
	private String datiAllevamento;

	@Lob
	@Column(name="DATI_DETENTORE")
	private String datiDetentore;

	@Lob
	@Column(name="DATI_PROPRIETARIO")
	private String datiProprietario;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_INTERVENTO")
	private InterventoModel intervento;

	//bi-directional many-to-one association to DomandaUnicaModel
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_DOMANDA")
	private DomandaUnicaModel domandaUnica;
	
	//bi-directional many-to-one association to EsitoCalcoloCapoModel
	@OneToMany(mappedBy="allevamentoImpegnato", cascade = CascadeType.REMOVE)
	private Set<EsitoCalcoloCapoModel> esitiCalcoloCapi;
	
	public String getCodiceSpecie() {
		return this.codiceSpecie;
	}

	public void setCodiceSpecie(String codiceSpecie) {
		this.codiceSpecie = codiceSpecie;
	}

	public String getDatiAllevamento() {
		return this.datiAllevamento;
	}

	public void setDatiAllevamento(String datiAllevamento) {
		this.datiAllevamento = datiAllevamento;
	}

	public String getDatiDetentore() {
		return this.datiDetentore;
	}

	public void setDatiDetentore(String datiDetentore) {
		this.datiDetentore = datiDetentore;
	}

	public String getDatiProprietario() {
		return this.datiProprietario;
	}

	public void setDatiProprietario(String datiProprietario) {
		this.datiProprietario = datiProprietario;
	}

	public InterventoModel getIntervento() {
		return this.intervento;
	}

	public void setIntervento(InterventoModel intervento) {
		this.intervento = intervento;
	}

	public DomandaUnicaModel getDomandaUnica() {
		return this.domandaUnica;
	}

	public void setDomandaUnica(DomandaUnicaModel domandaUnica) {
		this.domandaUnica = domandaUnica;
	}
	
	public Set<EsitoCalcoloCapoModel> getEsitiCalcoloCapi() {
		return this.esitiCalcoloCapi;
	}

	public void setEsitiCalcoloCapi(Set<EsitoCalcoloCapoModel> esitiCalcoloCapi) {
		this.esitiCalcoloCapi = esitiCalcoloCapi;
	}

	public EsitoCalcoloCapoModel addEsitoCalcoloCapo(EsitoCalcoloCapoModel esitoCalcoloCapo) {
		getEsitiCalcoloCapi().add(esitoCalcoloCapo);
		esitoCalcoloCapo.setAllevamentoImpegnato(this);

		return esitoCalcoloCapo;
	}
}