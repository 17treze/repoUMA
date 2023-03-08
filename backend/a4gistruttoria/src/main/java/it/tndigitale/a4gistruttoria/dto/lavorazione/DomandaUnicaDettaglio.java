package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.List;

import it.tndigitale.a4gistruttoria.dto.DomandaUnica;
import it.tndigitale.a4gistruttoria.dto.InfoGeneraliDomanda;
import it.tndigitale.a4gistruttoria.dto.KeyValueStringString;
import it.tndigitale.a4gistruttoria.dto.domandaunica.DichiarazioneDu;

public class DomandaUnicaDettaglio extends DomandaUnica {
	private Long id;

	private InfoGeneraliDomanda infoGeneraliDomanda;
	private String statoSostegno;
	private Double supImpegnataLorda;
	private Double supImpegnataNetta;

	// TODO: manca categoria
	private List<DichiarazioneDu> dichiarazioni;
	private List<KeyValueStringString> informazioniDomanda;
	private List<ControlloFrontend> controlliSostegno;
	private List<ControlloFrontend> datiDomanda;

	// datiDomanda
	// datiParticella
	private DatiIstruttoria datiIstruttoria;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public InfoGeneraliDomanda getInfoGeneraliDomanda() {
		return infoGeneraliDomanda;
	}

	@Override
	public void setInfoGeneraliDomanda(InfoGeneraliDomanda infoGeneraliDomanda) {
		this.infoGeneraliDomanda = infoGeneraliDomanda;
	}

	public String getStatoSostegno() {
		return statoSostegno;
	}

	public void setStatoSostegno(String statoSostegno) {
		this.statoSostegno = statoSostegno;
	}

	public Double getSupImpegnataLorda() {
		return supImpegnataLorda;
	}

	public void setSupImpegnataLorda(Double supImpegnataLorda) {
		this.supImpegnataLorda = supImpegnataLorda;
	}

	public Double getSupImpegnataNetta() {
		return supImpegnataNetta;
	}

	public void setSupImpegnataNetta(Double supImpegnataNetta) {
		this.supImpegnataNetta = supImpegnataNetta;
	}

	public List<DichiarazioneDu> getDichiarazioni() {
		return dichiarazioni;
	}

	public void setDichiarazioni(List<DichiarazioneDu> dichiarazioni) {
		this.dichiarazioni = dichiarazioni;
	}

	public List<KeyValueStringString> getInformazioniDomanda() {
		return informazioniDomanda;
	}

	public void setInformazioniDomanda(List<KeyValueStringString> informazioniDomanda) {
		this.informazioniDomanda = informazioniDomanda;
	}

	public DatiIstruttoria getDatiIstruttoria() {
		return datiIstruttoria;
	}

	public void setDatiIstruttoria(DatiIstruttoria datiIstruttoria) {
		this.datiIstruttoria = datiIstruttoria;
	}

	public List<ControlloFrontend> getControlliSostegno() {
		return controlliSostegno;
	}

	public void setControlliSostegno(List<ControlloFrontend> controlliSostegno) {
		this.controlliSostegno = controlliSostegno;
	}

	public List<ControlloFrontend> getDatiDomanda() {
		return datiDomanda;
	}

	public void setDatiDomanda(List<ControlloFrontend> datiDomanda) {
		this.datiDomanda = datiDomanda;
	}
}
