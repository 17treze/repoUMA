/**
 * 
 */
package it.tndigitale.a4g.fascicolo.antimafia.dto;

import it.tndigitale.a4g.fascicolo.antimafia.StatoDichiarazioneEnum;

/**
 * @author B.Conetta
 * contiene il count delle domande nei vari stati
 */
public class StatoDichiarazioneCounter {
	private Long bozza;
	private Long protocollata;
	private Long controllata; // mappata in istruttoria
	private Long controlloManuale;
	private Long rifiutata;
	private Long verificaPeriodica;
	private Long positiva;
	private Long negativa;
	
	// metodo di utilità che setta gli stati in modo discreto
	public void setStatoDichirazioneCounter(String stato, Long count) {
		
		if (stato.equals(StatoDichiarazioneEnum.BOZZA.getIdentificativoStato())) {
			this.setBozza(count);
		}
		if (stato.equals(StatoDichiarazioneEnum.PROTOCOLLATA.getIdentificativoStato())) {
			this.setProtocollata(count);
		}
		if (stato.equals(StatoDichiarazioneEnum.CONTROLLATA.getIdentificativoStato())) {
			this.setControllata(count);
		}
		if (stato.equals(StatoDichiarazioneEnum.CONTROLLO_MANUALE.getIdentificativoStato())) {
			this.setControlloManuale(count);
		}
		if (stato.equals(StatoDichiarazioneEnum.POSITIVO.getIdentificativoStato())) {
			this.setPositiva(count);
		}
		if (stato.equals(StatoDichiarazioneEnum.VERIFICA_PERIODICA.getIdentificativoStato())) {
			this.setVerificaPeriodica(count);
		}
		if (stato.equals(StatoDichiarazioneEnum.RIFIUTATA.getIdentificativoStato())) {
			this.setRifiutata(count);
		}
	}
	
	public Long getBozza() {
		return bozza;
	}
	public void setBozza(Long bozza) {
		this.bozza = bozza;
	}
	
	public Long getProtocollata() {
		return protocollata;
	}
	public void setProtocollata(Long protocollata) {
		this.protocollata = protocollata;
	}
	public Long getControllata() {
		return controllata;
	}
	public void setControllata(Long controllata) {
		this.controllata = controllata;
	}
	public Long getControlloManuale() {
		return controlloManuale;
	}
	public void setControlloManuale(Long controlloManuale) {
		this.controlloManuale = controlloManuale;
	}
	public Long getRifiutata() {
		return rifiutata;
	}
	public void setRifiutata(Long rifiutata) {
		this.rifiutata = rifiutata;
	}
	public Long getVerificaPeriodica() {
		return verificaPeriodica;
	}
	public void setVerificaPeriodica(Long verificaPeriodica) {
		this.verificaPeriodica = verificaPeriodica;
	}
	public Long getPositiva() {
		return positiva;
	}
	public void setPositiva(Long positiva) {
		this.positiva = positiva;
	}
	public Long getNegativa() {
		return negativa;
	}
	public void setNegativa(Long negativa) {
		this.negativa = negativa;
	}
}
