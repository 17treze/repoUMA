package it.tndigitale.a4g.richiestamodificasuolo.dto.filter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoRichiestaModificaSuolo;

@ApiModel(description = "Rappresenta il modello dei filtri di ricerca delle richieste di modifica suolo")
public class RichiestaModificaSuoloFilter implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiParam(value = "id richiesta modifica suolo da ricercare", required = true)
	private Long idRichiesta;

	@ApiParam(value = "CUAA richiesta modifica suolo da ricercare per il BACK_OFFICE ", required = true)
	private String cuaa;
	@ApiParam(value = "comuneCatastale richiesta modifica suolo da ricercare", required = true)
	private String comuneCatastale;

	@ApiParam(value = "Anno di campagna modifica suolo da ricercare", required = true)
	private Long campagna;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime data;

	private StatoRichiestaModificaSuolo stato;
	private TipoRichiestaModificaSuolo tipo;

	@ApiParam(value = "CUAA richiesta modifica suolo da ricercare per i CAA ", required = false, hidden = true)
	private List<String> listCuaaMandatoCaa;

	public Long getIdRichiesta() {
		return idRichiesta;
	}

	public void setIdRichiesta(Long idRichiesta) {
		this.idRichiesta = idRichiesta;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getComuneCatastale() {
		return comuneCatastale;
	}

	public void setComuneCatastale(String comuneCatastale) {
		this.comuneCatastale = comuneCatastale;
	}

	public Long getCampagna() {
		return campagna;
	}

	public void setCampagna(Long campagna) {
		this.campagna = campagna;
	}

	public StatoRichiestaModificaSuolo getStato() {
		return stato;
	}

	public void setStato(StatoRichiestaModificaSuolo stato) {
		this.stato = stato;
	}

	public TipoRichiestaModificaSuolo getTipo() {
		return tipo;
	}

	public void setTipo(TipoRichiestaModificaSuolo tipo) {
		this.tipo = tipo;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public List<String> getListCuaaMandatoCaa() {
		return listCuaaMandatoCaa;
	}

	public void setListCuaaMandatoCaa(List<String> listCuaaMandatoCaa) {
		this.listCuaaMandatoCaa = listCuaaMandatoCaa;
	}

	@Override
	public String toString() {
		return String.format("RichiestaModificaSuoloFilter [idRichiesta=%s, cuaa=%s, comuneCatastale=%s, campagna=%s, stato=%s,tipo=%s,listCuaaMandatoCaa=%s]", idRichiesta, cuaa, comuneCatastale,
				campagna, stato, tipo, listCuaaMandatoCaa);
	}

}
