package it.tndigitale.a4gistruttoria.dto;

import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;

public class IstruttoriaAntimafiaChainHandlerBuilder {

	public JsonNode datiDichiarazione;
	public String cuaa;
	public JsonNode dettaglioImpresa;
	public JsonNode soggettiParix;
	public IstruttoriaAntimafiaEsito istruttoriaAntimafiaEsito;
	public Boolean isParixOK;
	public Boolean isSiapOK;
	public Boolean areSoggettiOK;

	public IstruttoriaAntimafiaChainHandlerBuilder with(Consumer<IstruttoriaAntimafiaChainHandlerBuilder> builderFunction) {
		builderFunction.accept(this);
		return this;
	}

	public IstruttoriaAntimafiaChainHandler build() {
		return new IstruttoriaAntimafiaChainHandler(datiDichiarazione, cuaa, dettaglioImpresa, soggettiParix, istruttoriaAntimafiaEsito, isParixOK, isSiapOK, areSoggettiOK);
	}
}