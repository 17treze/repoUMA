package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="A4GD_LAVORAZIONE")
public class LavorazioneModel extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = 7898411418029362843L;

	@Column(name="INDICE")
	private Integer indice;

	@Column(name="NOME")
	private String nome;

	@Enumerated(EnumType.STRING)
	@Column(name="TIPOLOGIA")
	private TipologiaLavorazione tipologia;

	@Enumerated(EnumType.STRING)
	@Column(name="UNITA_DI_MISURA")
	private UnitaDiMisura unitaDiMisura;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_GRUPPO_LAVORAZIONE")
	private GruppoLavorazioneModel gruppoLavorazione;

	@OneToMany(mappedBy = "lavorazioneModel", fetch = FetchType.LAZY)
	private List<CoefficienteModel> coefficienti;


	@Transient
	public Optional<BigDecimal> getCoefficiente(Long campagna) {
		return coefficienti.stream().filter(coefficienteModel ->  {
			if (campagna < coefficienteModel.getAnnoInizio()) {return false;}
			Optional<Integer> annoFineOpt = Optional.ofNullable(coefficienteModel.getAnnoFine());
			return annoFineOpt.isPresent() && campagna > annoFineOpt.get() ? Boolean.FALSE : Boolean.TRUE;
		})
				.map(CoefficienteModel::getCoefficiente)
				.findFirst();
	} 

	public Integer getIndice() {
		return this.indice;
	}
	public LavorazioneModel setIndice(Integer indice) {
		this.indice = indice;
		return this;
	}
	public String getNome() {
		return this.nome;
	}
	public LavorazioneModel setNome(String nome) {
		this.nome = nome;
		return this;
	}
	public TipologiaLavorazione getTipologia() {
		return this.tipologia;
	}
	public LavorazioneModel setTipologia(TipologiaLavorazione tipologia) {
		this.tipologia = tipologia;
		return this;
	}
	public UnitaDiMisura getUnitaDiMisura() {
		return unitaDiMisura;
	}
	public LavorazioneModel setUnitaDiMisura(UnitaDiMisura unitaDiMisura) {
		this.unitaDiMisura = unitaDiMisura;
		return this;
	}
	public GruppoLavorazioneModel getGruppoLavorazione() {
		return gruppoLavorazione;
	}
	public LavorazioneModel setGruppoLavorazione(GruppoLavorazioneModel gruppiLavorazioneModel) {
		this.gruppoLavorazione = gruppiLavorazioneModel;
		return this;
	}
}