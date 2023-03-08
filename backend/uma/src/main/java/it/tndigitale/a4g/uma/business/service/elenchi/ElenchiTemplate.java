package it.tndigitale.a4g.uma.business.service.elenchi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import it.tndigitale.a4g.framework.model.Attachment;

public abstract class ElenchiTemplate {

	protected static final String PREFISSO = "ELENCO_";
	protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	protected static final String ENTE_PRESENTATORE = "ENTE_PRESENTATORE";
	protected static final String CUAA = "CUAA";
	protected static final String DENOMINAZIONE = "DENOMINAZIONE";
	protected static final String ID_DOMANDA = "ID_DOMANDA";
	protected static final String STATO_DOMANDA = "STATO_DOMANDA";
	protected static final String CAMPAGNA = "CAMPAGNA";

	public Attachment getCsv(Long campagna) throws IOException {
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

		new CsvMapper().writerFor(getPojo()).with(getSchema())
		.writeValues(byteArray)
		.writeAll(getDati(campagna));

		return new Attachment().setFile(byteArray.toByteArray()).setFileName(getFileName(campagna));
	}

	protected abstract Class<? extends ElencoBaseTemplate> getPojo();
	protected abstract CsvSchema getSchema();
	protected abstract List<ElencoBaseTemplate> getDati(Long campagna);
	protected abstract String getFileName(Long campagna);

}
