package it.tndigitale.a4g.uma.dto.consumi;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import it.tndigitale.a4g.uma.business.persistence.entity.MotivazioneConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburanteConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoConsuntivo;

@Schema
public class ConsuntivoDto {

	private Long id;
	private TipoConsuntivo tipo;
	private TipoCarburanteConsuntivo carburante;
	private Integer quantita;
	private MotivazioneConsuntivo motivazione;
	private List<InfoAllegatoConsuntivoDto> infoAllegati;

	public Long getId() {
		return id;
	}
	public ConsuntivoDto setId(Long id) {
		this.id = id;
		return this;
	}
	public TipoConsuntivo getTipo() {
		return tipo;
	}
	public ConsuntivoDto setTipo(TipoConsuntivo tipo) {
		this.tipo = tipo;
		return this;
	}
	public TipoCarburanteConsuntivo getCarburante() {
		return carburante;
	}
	public ConsuntivoDto setCarburante(TipoCarburanteConsuntivo carburante) {
		this.carburante = carburante;
		return this;
	}
	public Integer getQuantita() {
		return quantita;
	}
	public ConsuntivoDto setQuantita(Integer quantita) {
		this.quantita = quantita;
		return this;
	}
	public MotivazioneConsuntivo getMotivazione() {
		return motivazione;
	}
	public ConsuntivoDto setMotivazione(MotivazioneConsuntivo motivazione) {
		this.motivazione = motivazione;
		return this;
	}
	public List<InfoAllegatoConsuntivoDto> getInfoAllegati() {
		return infoAllegati;
	}
	public ConsuntivoDto setInfoAllegati(List<InfoAllegatoConsuntivoDto> infoAllegati) {
		this.infoAllegati = infoAllegati;
		return this;
	}
	public ConsuntivoDto addInfoAllegato(InfoAllegatoConsuntivoDto infoAllegato) {
		this.infoAllegati.add(infoAllegato);
		return this;
	}
}
