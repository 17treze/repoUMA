package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="A4GT_CONSUNTIVI_CONSUMI" , uniqueConstraints = @UniqueConstraint(columnNames = {"ID_CONSUMI" , "TIPO" , "CARBURANTE"}, name = "A4GT_CONSUNTIVI_CONSUMI_UIDX"))
public class ConsuntivoConsumiModel extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = -5159732199786160498L;

	@Column(name="TIPO", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private TipoConsuntivo tipoConsuntivo;

	@Column(name="CARBURANTE", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private TipoCarburanteConsuntivo tipoCarburante;

	@Column(name="QUANTITA", nullable = false)
	private BigDecimal quantita;

	@Column(name="MOTIVAZIONE", nullable = true)
	@Enumerated(EnumType.STRING)
	private MotivazioneConsuntivo motivazione;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CONSUMI")
	private DichiarazioneConsumiModel dichiarazioneConsumi;

	@OneToMany(mappedBy = "consuntivoModel", fetch = FetchType.LAZY)
	private List<AllegatoConsuntivoModel> allegati;

	public TipoConsuntivo getTipoConsuntivo() {
		return tipoConsuntivo;
	}
	public ConsuntivoConsumiModel setTipoConsuntivo(TipoConsuntivo tipoConsuntivo) {
		this.tipoConsuntivo = tipoConsuntivo;
		return this;
	}
	public TipoCarburanteConsuntivo getTipoCarburante() {
		return tipoCarburante;
	}
	public ConsuntivoConsumiModel setTipoCarburante(TipoCarburanteConsuntivo tipoCarburante) {
		this.tipoCarburante = tipoCarburante;
		return this;
	}
	public BigDecimal getQuantita() {
		return quantita;
	}
	public ConsuntivoConsumiModel setQuantita(BigDecimal quantita) {
		this.quantita = quantita;
		return this;
	}
	public DichiarazioneConsumiModel getDichiarazioneConsumi() {
		return dichiarazioneConsumi;
	}
	public ConsuntivoConsumiModel setDichiarazioneConsumi(DichiarazioneConsumiModel dichiarazioneConsumi) {
		this.dichiarazioneConsumi = dichiarazioneConsumi;
		return this;
	}
	public MotivazioneConsuntivo getMotivazione() {
		return motivazione;
	}
	public ConsuntivoConsumiModel setMotivazione(MotivazioneConsuntivo motivazione) {
		this.motivazione = motivazione;
		return this;
	}
	public List<AllegatoConsuntivoModel> getAllegati() {
		return allegati;
	}
	public ConsuntivoConsumiModel setAllegati(List<AllegatoConsuntivoModel> allegati) {
		this.allegati = allegati;
		return this;
	}
}
