package it.tndigitale.a4gistruttoria.service.businesslogic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.Lists;

import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gistruttoria.dto.IstruttoriaDomandaUnicaFilter;
import it.tndigitale.a4gistruttoria.dto.istruttoria.IstruttoriaDomandaUnica;
import it.tndigitale.a4gistruttoria.dto.istruttoria.IstruttoriaDomandaUnicaCsv;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.PoliticaAgricolaComunitariaDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PoliticaAgricolaComunitariaModel;
import it.tndigitale.a4gistruttoria.repository.specification.IstruttoriaSpecificationBuilder;
import it.tndigitale.a4gistruttoria.service.builder.IstruttoriaBuilder;
import it.tndigitale.a4gistruttoria.util.CodicePac;

@Service
public class IstruttoriaService {

	private static final int QUERY_LIMIT = 999;

	@Autowired
	private IstruttoriaDao istruttoriaDao;
	@Autowired
	private Clock clock;

	@Autowired
	private PoliticaAgricolaComunitariaDao politicaAgricolaDao;

	public RisultatiPaginati<IstruttoriaDomandaUnica> ricerca(IstruttoriaDomandaUnicaFilter criteri,
			Paginazione paginazione,
			Ordinamento ordinamento) throws Exception {
		Page<IstruttoriaModel> page = istruttoriaDao.findAll(IstruttoriaSpecificationBuilder.getFilter(criteri),
				PageableBuilder.build().from(paginazione, ordinamento));
		return IstruttoriaBuilder.from(page);
	}

	public List<IstruttoriaDomandaUnica> ricerca(IstruttoriaDomandaUnicaFilter criteri) throws Exception {
		List<IstruttoriaModel> page = istruttoriaDao.findAll(IstruttoriaSpecificationBuilder.getFilter(criteri));
		return IstruttoriaBuilder.from(page);
	}

	public Long countIstruttorie(IstruttoriaDomandaUnicaFilter criteri)  {
		return istruttoriaDao.count(IstruttoriaSpecificationBuilder.getFilter(criteri));
	}

	public IstruttoriaDomandaUnica getIstruttoriaById(Long idIstruttoria)  {
		return IstruttoriaBuilder.from(istruttoriaDao.findById(idIstruttoria).orElse(null));
	}


	public boolean isIstruttoriaBloccata(Long idIstruttoria)  {
		return istruttoriaDao.findById(idIstruttoria).map(i -> Boolean.TRUE.equals(i.getBloccataBool())).orElse(false);
	}

	@Transactional
	public void aggiornaErrore(Long idIstruttoria, boolean isErroreDomanda, String messaggio) {
		IstruttoriaModel istruttoria = istruttoriaDao.getOne(idIstruttoria);
		if (isErroreDomanda) {
			istruttoria.setBloccataBool(Boolean.TRUE);			
		}
		istruttoria.setErroreCalcolo(isErroreDomanda);
		istruttoria.setDataUltimoCalcolo(LocalDateTime.now());

		istruttoriaDao.save(istruttoria);
	}

	@Transactional(readOnly = true)
	public List<Integer> getAnniCampagna(CodicePac codicePac) {
		//get dati db
		PoliticaAgricolaComunitariaModel politicaAgricolaComunitariaModel = politicaAgricolaDao.findByCodicePac(codicePac.getCodice());
		List<Integer> anniCampagna= new ArrayList<>();
		int maxYear = Math.min(clock.now().getYear(), politicaAgricolaComunitariaModel.getAnnoFine());
		for (int i = politicaAgricolaComunitariaModel.getAnnoInizio(); i <= maxYear; i++) {
			anniCampagna.add(i);
		}

		return anniCampagna;
	}
	
	
	protected BiConsumer<IstruttoriaModel, List<IstruttoriaDomandaUnicaCsv>> addToCSVTemplateList = (istruttoria, templateList) -> {
		IstruttoriaDomandaUnicaCsv csvTemplate = new IstruttoriaDomandaUnicaCsv();
		csvTemplate.setCuaa(istruttoria.getDomandaUnicaModel().getCuaaIntestatario());
		csvTemplate.setDescrizioneImpresa(istruttoria.getDomandaUnicaModel().getRagioneSociale());
		csvTemplate.setNumeroDomanda(istruttoria.getDomandaUnicaModel().getNumeroDomanda().toString());
		templateList.add(csvTemplate);
	};

	public Resource downloadCsv(List<Long> idIstruttorie) throws IOException {
		// ORA-01795: maximum number of expressions in a list is 1000
		// Partiziono la lista in sottoliste di dimensione massimo 999 (limite massimo)
		final List<List<Long>> subLists = Lists.partition(idIstruttorie, QUERY_LIMIT);
		List<IstruttoriaModel> istruttoriaList = new ArrayList<IstruttoriaModel>();
		// Per ogni sottolista reperisco le istruttorie e le aggiungo all'array che le conterrà tutte. Ogni sottolista non supererà mai il limite di record imposto da Hibernate
		subLists.forEach(subList -> istruttoriaList.addAll(istruttoriaDao.findAllById(subList)));
		List<IstruttoriaDomandaUnicaCsv> csvTemplateList = new ArrayList<IstruttoriaDomandaUnicaCsv>();
		// Per ogni istruttoria creo il relativo template CSV e lo aggiungo alla lista che conterrà i template di tutte le istruttorie.
		istruttoriaList.forEach(istruttoria -> addToCSVTemplateList.accept(istruttoria, csvTemplateList));
		
		// Creo lo schema del file CSV con la lista dei template. Tale schema verrà utilizzato per creare il file CSV.
		CsvSchema istruttorieSchema = CsvSchema.builder()
				.addColumn("CUAA", CsvSchema.ColumnType.STRING)
				.addColumn("NUMERO_DOMANDA", CsvSchema.ColumnType.STRING)
				.addColumn("DESCRIZIONE_IMPRESA", CsvSchema.ColumnType.STRING)
				.build().withColumnSeparator(';').withoutQuoteChar().withHeader();
		CsvMapper mapper = new CsvMapper();
		ObjectWriter domandeCollegateWriter = mapper
				.writerFor(IstruttoriaDomandaUnicaCsv.class)
				.with(istruttorieSchema);
		ByteArrayOutputStream csvByteArray = new ByteArrayOutputStream();
		domandeCollegateWriter.writeValues(csvByteArray).writeAll(csvTemplateList); 
		return new ByteArrayResource(csvByteArray.toByteArray());
	}
}