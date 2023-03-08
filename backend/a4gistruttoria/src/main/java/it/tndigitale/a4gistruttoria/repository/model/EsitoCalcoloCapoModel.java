package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * The persistent class for the A4GT_ESITO_CALCOLO_CAPO database table.
 * 
 */
@Entity
@Table(name = "A4GT_ESITO_CALCOLO_CAPO")
@NamedQuery(name = "EsitoCalcoloCapoModel.findAll", query = "SELECT a FROM EsitoCalcoloCapoModel a")
public class EsitoCalcoloCapoModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1002842272637347080L;

	@Column(name = "CAPO_ID")
	private Long capoId;

	@Column(name = "CODICE_CAPO")
	private String codiceCapo;

	@Column(name = "ESITO")
	@Enumerated(EnumType.STRING)
	private EsitoCalcoloCapo esito;

	@Column(name = "MESSAGGIO")
	private String messaggio;
	
	@Column(name = "DUPLICATO")
	private Boolean duplicato;
	
	@Column(name = "CONTROLLO_NON_SUPERATO")
	private Boolean controlloNonSuperato;
	
	@Column(name = "RICHIESTO")
	private Boolean richiesto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ALLEVAM_DU")
	private AllevamentoImpegnatoModel allevamentoImpegnato;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "ID_TRANSIZIONE")
	private TransizioneIstruttoriaModel transizione;

	
	@OneToMany(mappedBy="esitoCalcoloCapo", fetch=FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<A4gtCapoTracking> a4gtCapoTrackings;

	public EsitoCalcoloCapoModel() {
		super();
	}

	public EsitoCalcoloCapoModel(EsitoCalcoloCapo esito, String messaggio) {
		super();
		this.esito = esito;
		this.messaggio = messaggio;
	}

	public Long getCapoId() {
		return this.capoId;
	}

	public void setCapoId(Long capoId) {
		this.capoId = capoId;
	}

	public String getCodiceCapo() {
		return this.codiceCapo;
	}

	public void setCodiceCapo(String codiceCapo) {
		this.codiceCapo = codiceCapo;
	}

	public EsitoCalcoloCapo getEsito() {
		return this.esito;
	}

	public void setEsito(EsitoCalcoloCapo esito) {
		this.esito = esito;
	}

	public String getMessaggio() {
		return this.messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
	
	public Boolean getDuplicato() {
		return this.duplicato;
	}

	public void setDuplicato(Boolean duplicato) {
		this.duplicato = duplicato;
	}
	
	public Boolean getControlloNonSuperato() {
		return this.controlloNonSuperato;
	}

	public void setControlloNonSuperato(Boolean controlloNonSuperato) {
		this.controlloNonSuperato = controlloNonSuperato;
	}
	
	public Boolean getRichiesto() {
		return this.richiesto;
	}

	public void setRichiesto(Boolean richiesto) {
		this.richiesto = richiesto;
	}

	public AllevamentoImpegnatoModel getAllevamentoImpegnato() {
		return this.allevamentoImpegnato;
	}

	public void setAllevamentoImpegnato(AllevamentoImpegnatoModel allevamentoImpegnato) {
		this.allevamentoImpegnato = allevamentoImpegnato;
	}

	public TransizioneIstruttoriaModel getTransizione() {
		return transizione;
	}

	public void setTransizione(TransizioneIstruttoriaModel transizione) {
		this.transizione = transizione;
	}

	public List<A4gtCapoTracking> getA4gtCapoTrackings() {
		return a4gtCapoTrackings;
	}

	public void setA4gtCapoTrackings(List<A4gtCapoTracking> a4gtCapoTrackings) {
		this.a4gtCapoTrackings = a4gtCapoTrackings;
	}

}