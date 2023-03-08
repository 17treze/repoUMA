/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gistruttoria.util.TipologiaStatistica;

/**
 * Individua la statistica richiesta da parte del utente per un certo anno di 
 * campagna
 * @author Lorenzo Martinelli
 */
@ApiModel(description = "Rappresenta le possibili statistiche erogate dal sistema")
public class StatisticaFilter implements Serializable {
	private static final long serialVersionUID = 5325342631265333545L;

	@ApiParam(
		value = "Lista delle possibili statistiche da richiedere al sistema", 
		required = true)
	private TipologiaStatistica tipologiaStatistica;
	
	@ApiParam(
		value = "Anno di campagna", 
		required = true)
	private Integer campagna;
	
	public TipologiaStatistica getTipologiaStatistica() {
		return tipologiaStatistica;
	}
	
	public void setTipologiaStatistica(TipologiaStatistica tipologiaStatistica) {
		this.tipologiaStatistica = tipologiaStatistica;
	}
	
	public Integer getCampagna() {
		return campagna;
	}
	
	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}
}
