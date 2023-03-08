package it.tndigitale.a4g.uma.builder;

import it.tndigitale.a4g.fascicolo.territorio.client.model.CodificaColtura;
import it.tndigitale.a4g.fascicolo.territorio.client.model.ColturaDto;

public class ColturaTestBuilder {

	private ColturaDto colturaDto;

	public ColturaTestBuilder() {
		colturaDto = new ColturaDto();
	}

	public ColturaTestBuilder newDto() {
		colturaDto = new ColturaDto();
		return this;
	}

	public ColturaTestBuilder descrizione(String descrizioneUtileAlTest) {
		return this;
	}

	public ColturaTestBuilder withCodifica(String destinazioneUso, String suolo, String qualita, String uso, String varieta) {
		CodificaColtura codificaColtura = new CodificaColtura();
		codificaColtura.setCodiceDestinazioneUso(destinazioneUso);
		codificaColtura.setCodiceSuolo(suolo);
		codificaColtura.setCodiceQualita(qualita);
		codificaColtura.setCodiceUso(uso);
		codificaColtura.setCodiceVarieta(varieta);
		colturaDto.setCodifica(codificaColtura);
		return this;
	}

	public ColturaTestBuilder withSuperficie(Integer superficieAccertata) {
		colturaDto.setSuperficieAccertata(superficieAccertata);
		return this;
	}

	public ColturaDto build() {
		return colturaDto;
	}
}
