package it.tndigitale.a4gistruttoria.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import it.tndigitale.a4gistruttoria.action.cup.CupConverterFunction;
import it.tndigitale.a4gistruttoria.cup.dto.CUP;
import it.tndigitale.a4gistruttoria.cup.dto.CUPGENERAZIONE;
import it.tndigitale.a4gistruttoria.dto.cup.DatiCUP;

@Service
public class CUPService {

	@Autowired
	private CupConverterFunction converter;
	
	private static final Logger logger = LoggerFactory.getLogger(CUPService.class);	
	
	public byte[] generaXMLCup(byte[] csv) throws Exception {
		List<CUPGENERAZIONE> cup = leggiInput(csv);
		CUP cupXML = new CUP();		
		cup.stream().forEach(row -> cupXML.getCUPGENERAZIONEOrCUPCHIUSURAREVOCA().add(row));		
		return convert(cupXML);
	}
	
	protected List<CUPGENERAZIONE> leggiInput(byte[] csv) throws Exception {
        CsvMapper mapper = new CsvMapper();
        CsvSchema bootstrapSchema = CsvSchema.builder()
        		.addColumn("naturaCup", CsvSchema.ColumnType.STRING)
        		.addColumn("tipologiaCup", CsvSchema.ColumnType.STRING)
        		.addColumn("settoreCup", CsvSchema.ColumnType.STRING)
        		.addColumn("sottosettoreCup", CsvSchema.ColumnType.STRING)
        		.addColumn("categoriaCup", CsvSchema.ColumnType.STRING)
        		.addColumn("descrizioneCodice", CsvSchema.ColumnType.STRING)
        		.addColumn("idProgetto", CsvSchema.ColumnType.STRING)
        		.addColumn("cuaa", CsvSchema.ColumnType.STRING)
        		.addColumn("totaleImportoRichiesto", CsvSchema.ColumnType.STRING)
        		.addColumn("tipologiaCopertura", CsvSchema.ColumnType.STRING)
        		.addColumn("contributoRichiesto", CsvSchema.ColumnType.STRING)
        		.build()
        		.withSkipFirstDataRow(true)
        		.withColumnSeparator(';');
        mapper.enable(CsvParser.Feature.TRIM_SPACES);
		MappingIterator<DatiCUP> cupIterator = mapper.readerWithTypedSchemaFor(DatiCUP.class).with(bootstrapSchema).readValues(csv);
		return cupIterator.readAll().stream().map(converter).collect(Collectors.toList());
	}

	protected byte[] convert(CUP cupXML) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			JAXBContext jc = JAXBContext.newInstance(CUP.class.getPackage().getName());
			Marshaller m  = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(cupXML, outputStream);
			outputStream.flush();
			return outputStream.toByteArray();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				logger.error("marshall: errore eseguendo la close");
			}
		}
		
	}

}
