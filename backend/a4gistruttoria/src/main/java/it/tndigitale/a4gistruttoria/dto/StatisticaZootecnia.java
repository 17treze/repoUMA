/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

/**
 * @author B.Conetta
 *
 */
public class StatisticaZootecnia {

	private String codiceAgea;
	private String descrizioneBreve ;
	private Integer totali ;
	private Integer ammissibili ;
	private Integer nonAmmissibili ;
	private Integer ammissibiliConSanzione;
	
	public String getCodiceAgea() {
		return codiceAgea;
	}
	public void setCodiceAgea(String codiceAgea) {
		this.codiceAgea = codiceAgea;
	}
	public String getDescrizioneBreve() {
		return descrizioneBreve;
	}
	public void setDescrizioneBreve(String descrizioneBreve) {
		this.descrizioneBreve = descrizioneBreve;
	}
	public Integer getTotali() {
		return totali;
	}
	public void setTotali(Integer totali) {
		this.totali = totali;
	}
	public Integer getAmmissibili() {
		return ammissibili;
	}
	public void setAmmissibili(Integer ammissibili) {
		this.ammissibili = ammissibili;
	}
	public Integer getNonAmmissibili() {
		return nonAmmissibili;
	}
	public void setNonAmmissibili(Integer nonAmmissibili) {
		this.nonAmmissibili = nonAmmissibili;
	}
	public Integer getAmmissibiliConSanzione() {
		return ammissibiliConSanzione;
	}
	public void setAmmissibiliConSanzione(Integer ammissibiliConSanzione) {
		this.ammissibiliConSanzione = ammissibiliConSanzione;
	}
}
