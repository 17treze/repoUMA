/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

import java.util.List;
import java.util.Map;

import it.tndigitale.a4gistruttoria.dto.domandaunica.DichiarazioneDu;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiDomandaAccoppiato;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaACZ;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

public class AccoppiatoZootecnia {
	
	private Long idDomanda;
	private StatoIstruttoria statoLavorazioneSostegno;
	private DatiIstruttoriaACZ datiIstruttoriaACZ;
	private List<DichiarazioneDu> dichiarazioni;
	private ControlliSostegno controlliSostegno;
	private DatiDomandaAccoppiato datiDomandaACZ;
	private Map<String, String> datiDisciplinaFinanziaria;
	
	public AccoppiatoZootecnia(Long idDomanda, StatoIstruttoria statoLavorazioneSostegno) {
		setIdDomanda(idDomanda);
		setStatoLavorazioneSostegno(statoLavorazioneSostegno);
	}

	public Long getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}
	
	public StatoIstruttoria getStatoLavorazioneSostegno() {
		return statoLavorazioneSostegno;
	}

	public void setStatoLavorazioneSostegno(StatoIstruttoria statoLavorazioneSostegno) {
		this.statoLavorazioneSostegno = statoLavorazioneSostegno;
	}

	public DatiIstruttoriaACZ getDatiIstruttoriaACZ() {
		return datiIstruttoriaACZ;
	}

	public void setDatiIstruttoriaACZ(DatiIstruttoriaACZ datiIstruttoriaACZ) {
		this.datiIstruttoriaACZ = datiIstruttoriaACZ;
	}

	public List<DichiarazioneDu> getDichiarazioni() {
		return dichiarazioni;
	}

	public void setDichiarazioni(List<DichiarazioneDu> dichiarazioni) {
		this.dichiarazioni = dichiarazioni;
	}

	public ControlliSostegno getControlliSostegno() {
		return controlliSostegno;
	}

	public void setControlliSostegno(ControlliSostegno controlliSostegno) {
		this.controlliSostegno = controlliSostegno;
	}

	public DatiDomandaAccoppiato getDatiDomandaACZ() {
		return datiDomandaACZ;
	}

	public void setDatiDomandaACZ(DatiDomandaAccoppiato datiDomandaACZ) {
		this.datiDomandaACZ = datiDomandaACZ;
	}

	public Map<String, String> getDatiDisciplinaFinanziaria() {
		return datiDisciplinaFinanziaria;
	}

	public void setDatiDisciplinaFinanziaria(Map<String, String> datiDisciplinaFinanziaria) {
		this.datiDisciplinaFinanziaria = datiDisciplinaFinanziaria;
	}
}
