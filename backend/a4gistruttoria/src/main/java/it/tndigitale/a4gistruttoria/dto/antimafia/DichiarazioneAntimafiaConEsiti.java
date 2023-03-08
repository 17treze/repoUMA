package it.tndigitale.a4gistruttoria.dto.antimafia;

import java.util.Date;
import java.util.List;

public class DichiarazioneAntimafiaConEsiti extends DichiarazioneAntimafia {
	private List<Long> idsDomandaSuperficie;
	private List<Long> idsDomandaStrutturale;
	private List<Long> idsDomandaDu;
	private Date dtBdna;
	private String protocolloBdna;

	public Date getDtBdna() {
		return dtBdna;
	}

	public String getProtocolloBdna() {
		return protocolloBdna;
	}

	public void setDtBdna(Date dtBdna) {
		this.dtBdna = dtBdna;
	}

	public void setProtocolloBdna(String protocolloBdna) {
		this.protocolloBdna = protocolloBdna;
	}

	public List<Long> getIdsDomandaSuperficie() {
		return idsDomandaSuperficie;
	}

	public void setIdsDomandaSuperficie(List<Long> idsDomandaSuperficie) {
		this.idsDomandaSuperficie = idsDomandaSuperficie;
	}

	public List<Long> getIdsDomandaStrutturale() {
		return idsDomandaStrutturale;
	}

	public void setIdsDomandaStrutturale(List<Long> idsDomandaStrutturale) {
		this.idsDomandaStrutturale = idsDomandaStrutturale;
	}

	public List<Long> getIdsDomandaDu() {
		return idsDomandaDu;
	}

	public void setIdsDomandaDu(List<Long> idsDomandaDu) {
		this.idsDomandaDu = idsDomandaDu;
	}

}
