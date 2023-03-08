package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DomandaUnicheCsvImport {

	private String cuaa;
	private Long idDomanda;
	private BigDecimal importoRichiesto;
	private Date dataPresentazione;
	private Integer campagna;

	public DomandaUnicheCsvImport(String linea) {
		if(linea == null || linea.trim().equals("")) {
			return;
		}
		String[] splitted = linea.split(";");
		try {
			this.setCuaa(splitted[0]);
		} catch (Exception e) {
			return;
		}
		try {
			this.setIdDomanda(new Long(splitted[1]));
		} catch (Exception e) {
			return;
		}
		try {
			this.setDataPresentazione(new SimpleDateFormat("dd/MM/yyyy").parse(splitted[2]));
		} catch (Exception e) {
			return;
		}
		try {
			this.setImportoRichiesto(new BigDecimal(splitted[3].replaceAll(",",".")));
		} catch (Exception e) {
			return;
		}
		try {
			this.setCampagna(new Integer(splitted[4]));
		} catch (Exception e) {
			return;
		}
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public Long getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}

	public BigDecimal getImportoRichiesto() {
		return importoRichiesto;
	}

	public void setImportoRichiesto(BigDecimal importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
	}

	public Date getDataPresentazione() {
		return dataPresentazione;
	}

	public void setDataPresentazione(Date dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}
	
	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}
	
	public Boolean validate() {
		if (this.cuaa == null || this.cuaa.isEmpty()) {
			return Boolean.FALSE;
		}
		if (this.dataPresentazione == null) {
			return Boolean.FALSE;
		}
		if (this.idDomanda == null) {
			return Boolean.FALSE;
		}
		if (this.importoRichiesto == null) {
			return Boolean.FALSE;
		}
		if (this.campagna == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

}
