package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;

public class ImportaDatiStrutturaliHandlerBuilder {
	public String cuaa;
	public Date dataPresentazione;
	public JsonNode importoRichiesto;
	public BigDecimal importo;

	public ImportaDatiStrutturaliHandlerBuilder with(Consumer<ImportaDatiStrutturaliHandlerBuilder> builderFunction) {
		builderFunction.accept(this);
		return this;
	}

	public ImportaDatiStrutturaliHandler build() {
		return new ImportaDatiStrutturaliHandler(cuaa, dataPresentazione, importoRichiesto, importo);
	}

}
