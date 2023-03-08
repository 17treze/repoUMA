package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 
 * Oggetto creato per mettere in relazione il DTO CapoBdn con i suoi relativi dati sorgenti contenuti in una List<JsonNode> 
 * @author s.caccia
 *
 */
public class CapoBdnWrapper {

	private List<JsonNode> listaVacche;
	private CapoDto capoDto;

	public List<JsonNode> getListaVacche() {
		return listaVacche;
	}
	public CapoBdnWrapper setListaVacche(List<JsonNode> listaVacche) {
		this.listaVacche = listaVacche;
		return this;
	}
	public CapoDto getCapo() {
		return capoDto;
	}
	public CapoBdnWrapper setCapo(CapoDto capoDto) {
		this.capoDto = capoDto;
		return this;
	}
}
