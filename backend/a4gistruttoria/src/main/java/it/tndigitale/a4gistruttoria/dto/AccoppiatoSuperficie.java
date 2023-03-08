/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

import java.util.List;
import java.util.Map;

import it.tndigitale.a4gistruttoria.dto.domandaunica.DichiarazioneDu;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiDomandaAccoppiato;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaACS;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiParticellaACS;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DettaglioSuperficiImpegnateACS;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

/**
 * @author S.DeLuca
 *
 */
public class AccoppiatoSuperficie {

	private Long idDomanda;
	private StatoIstruttoria statoLavorazioneSostegno;
	private ControlliSostegno controlliSostegno;
	private DatiIstruttoriaACS datiIstruttoriaACS;
	private DatiParticellaACS datiParticellaACS;
	private DatiDomandaAccoppiato datiDomandaACS;
	private DettaglioSuperficiImpegnateACS datiSuperficiImpegnate;
	private List<DichiarazioneDu> dichiarazioni;
	private Map<String, String> datiDisciplinaFinanziaria;
	
	public AccoppiatoSuperficie(Long idDomanda, StatoIstruttoria statoLavorazioneSostegno) {
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


	public ControlliSostegno getControlliSostegno() {
		return controlliSostegno;
	}

	public void setControlliSostegno(ControlliSostegno controlliSostegno) {
		this.controlliSostegno = controlliSostegno;
	}

	public DatiIstruttoriaACS getDatiIstruttoriaACS() {
		return datiIstruttoriaACS;
	}

	public void setDatiIstruttoriaACS(DatiIstruttoriaACS datiIstruttoriaACS) {
		this.datiIstruttoriaACS = datiIstruttoriaACS;
	}

	public DatiParticellaACS getDatiParticellaACS() {
		return datiParticellaACS;
	}

	public void setDatiParticellaACS(DatiParticellaACS datiParticellaACS) {
		this.datiParticellaACS = datiParticellaACS;
	}

	public DatiDomandaAccoppiato getDatiDomandaACS() {
		return datiDomandaACS;
	}

	public void setDatiDomandaACS(DatiDomandaAccoppiato datiDomandaACS) {
		this.datiDomandaACS = datiDomandaACS;
	}

	public DettaglioSuperficiImpegnateACS getDatiSuperficiImpegnate() {
		return datiSuperficiImpegnate;
	}

	public void setDatiSuperficiImpegnate(DettaglioSuperficiImpegnateACS datiSuperficiImpegnate) {
		this.datiSuperficiImpegnate = datiSuperficiImpegnate;
	}

	public List<DichiarazioneDu> getDichiarazioni() {
		return dichiarazioni;
	}

	public void setDichiarazioni(List<DichiarazioneDu> dichiarazioni) {
		this.dichiarazioni = dichiarazioni;
	}

	public Map<String, String> getDatiDisciplinaFinanziaria() {
		return datiDisciplinaFinanziaria;
	}

	public void setDatiDisciplinaFinanziaria(Map<String, String> datiDisciplinaFinanziaria) {
		this.datiDisciplinaFinanziaria = datiDisciplinaFinanziaria;
	}
}
