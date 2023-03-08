package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.GeneratedValue;

@Entity
@Table(name="A4GD_MAPPING_AGS")
public class MappingAgsModel implements Serializable {

	private static final long serialVersionUID = -6489267374450280098L;

	@Id
	@Column(name="ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SOTTOTIPO")
	private SottotipoModel sottotipo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_TIPOLOGIA")
	private TipologiaModel tipologia ;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_CLASSE_FUNZIONALE")
	private ClasseFunzionaleModel classeFunzionale;
		
	@Column(name="DE_CLASSE", length = 26)
	private String descrizioneClasse;
	
	@Column(name="DE_MACCHINA")
	private String descrizioneMacchina;
	
	@Column(name="DE_SOTTO_CLASSE")
	private String descrizioneSottoClasse;
	
	@Column(name="ID_MACCHINA")
	private Long idMacchina;
	
	@Column(name="ID_TIPO_MACCHINA")
	private Long idTipoMacchina;	

	@Column(name="SCO_CLASSE")
	private String codiceAgs;
	
	@Column(name="SCO_SOTTO_CLASSE")
	private String sottoCodiceAgs;

	@Column(name="TIPOLOGIA")
	private String descrizioneTipologia;
	
	@Column(name="CLASSE_FUNZIONALE")
	private String descrizioneClasseFunzionale;
	
	@Column(name="SOTTOTIPO")
	private String descrizioneSottotipo;
	
	

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public SottotipoModel getSottotipo() {
		return sottotipo;
	}



	public void setSottotipo(SottotipoModel sottotipo) {
		this.sottotipo = sottotipo;
	}



	public TipologiaModel getTipologia() {
		return tipologia;
	}



	public void setTipologia(TipologiaModel tipologia) {
		this.tipologia = tipologia;
	}



	public ClasseFunzionaleModel getClasseFunzionale() {
		return classeFunzionale;
	}



	public void setClasseFunzionale(ClasseFunzionaleModel classeFunzionale) {
		this.classeFunzionale = classeFunzionale;
	}



	public String getDescrizioneClasse() {
		return descrizioneClasse;
	}



	public void setDescrizioneClasse(String descrizioneClasse) {
		this.descrizioneClasse = descrizioneClasse;
	}



	public String getDescrizioneMacchina() {
		return descrizioneMacchina;
	}



	public void setDescrizioneMacchina(String descrizioneMacchina) {
		this.descrizioneMacchina = descrizioneMacchina;
	}



	public String getDescrizioneSottoClasse() {
		return descrizioneSottoClasse;
	}



	public void setDescrizioneSottoClasse(String descrizioneSottoClasse) {
		this.descrizioneSottoClasse = descrizioneSottoClasse;
	}



	public Long getIdMacchina() {
		return idMacchina;
	}



	public void setIdMacchina(Long idMacchina) {
		this.idMacchina = idMacchina;
	}



	public Long getIdTipoMacchina() {
		return idTipoMacchina;
	}



	public void setIdTipoMacchina(Long idTipoMacchina) {
		this.idTipoMacchina = idTipoMacchina;
	}



	public String getCodiceAgs() {
		return codiceAgs;
	}



	public void setCodiceAgs(String codiceAgs) {
		this.codiceAgs = codiceAgs;
	}



	public String getSottoCodiceAgs() {
		return sottoCodiceAgs;
	}



	public void setSottoCodiceAgs(String sottoCodiceAgs) {
		this.sottoCodiceAgs = sottoCodiceAgs;
	}



	public String getDescrizioneTipologia() {
		return descrizioneTipologia;
	}



	public void setDescrizioneTipologia(String descrizioneTipologia) {
		this.descrizioneTipologia = descrizioneTipologia;
	}



	public String getDescrizioneClasseFunzionale() {
		return descrizioneClasseFunzionale;
	}



	public void setDescrizioneClasseFunzionale(String descrizioneClasseFunzionale) {
		this.descrizioneClasseFunzionale = descrizioneClasseFunzionale;
	}



	public String getDescrizioneSottotipo() {
		return descrizioneSottotipo;
	}



	public void setDescrizioneSottotipo(String descrizioneSottotipo) {
		this.descrizioneSottotipo = descrizioneSottotipo;
	}

	@Override
	public String toString() {
		Long idSottotipo = null;

		if (this.sottotipo != null)
			idSottotipo = this.sottotipo.getId();


		return "id[" + this.id + "] idSottotipo[" + idSottotipo + "] descrizioneSottotipo[" + this.descrizioneSottotipo + "";
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	 
	
	 
}
