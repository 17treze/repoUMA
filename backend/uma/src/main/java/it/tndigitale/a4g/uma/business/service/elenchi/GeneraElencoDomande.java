package it.tndigitale.a4g.uma.business.service.elenchi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.uma.Ruoli;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.service.consumi.RicercaDichiarazioneConsumiService;
import it.tndigitale.a4g.uma.business.service.logging.LoggingService;
import it.tndigitale.a4g.uma.business.service.richiesta.RicercaRichiestaCarburanteService;
import it.tndigitale.a4g.uma.dto.DomandaUmaDto;
import it.tndigitale.a4g.uma.dto.protocollo.TipoDocumentoUma;

@Component(ElenchiTemplate.PREFISSO + "DOMANDE")
public class GeneraElencoDomande extends ElenchiTemplate {

	private static final Logger logger = LoggerFactory.getLogger(GeneraElencoDomande.class);

	private static final String DATA_DOMANDA = "DATA_DOMANDA";
	private static final String TIPO_DOMANDA = "TIPO_DOMANDA";
	private static final String OPERATORE = "OPERATORE";

	@Autowired
	private LoggingService loggingService;
	@Autowired
	private RicercaRichiestaCarburanteService ricercaRichiestaCarburanteService;
	@Autowired
	private RicercaDichiarazioneConsumiService ricercaDichiarazioneConsumiService;
	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;
	@Autowired 
	private UtenteComponent utenteComponent;

	@Override
	protected Class<? extends ElencoBaseTemplate> getPojo() {
		return ElencoDomande.class;
	}

	@Override
	protected List<ElencoBaseTemplate> getDati(Long campagna) {
		Stream<DomandaUmaDto> stream = Stream.empty();

		// ricerco tutte le richieste a prescendere dall'utenza perchè servono per valutare eventuali rettifiche

		if (utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)) { // OPERATORE CAA
			// per operatori CAA - Utilizzo il service che è ottimizzato per le ricerca dei CAA
			List<DomandaUmaDto> richiesteCaa = ricercaRichiestaCarburanteService.getRichieste(campagna);
			List<DomandaUmaDto> consumiCaa = ricercaDichiarazioneConsumiService.getDichiarazioniConsumi(campagna);

			stream = Stream.concat(richiesteCaa.stream(), consumiCaa.stream());
		} else if (utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)) { // ISTRUTTORE UMA
			final List<DomandaUmaDto> richieste = richiestaCarburanteDao.findDomandaByCampagna(campagna);
			var richiesteAll = richieste;
			richiesteAll = richiesteAll.stream()
					.map(documento -> documento.setTipo(ricercaRichiestaCarburanteService.isRichiestaRettificante(richieste, documento).booleanValue() ? TipoDocumentoUma.RETTIFICA : TipoDocumentoUma.RICHIESTA))
					.collect(Collectors.toList());

			List<DomandaUmaDto> consumiAll = dichiarazioneConsumiDao.findDomandaByCampagna(campagna);

			consumiAll = consumiAll.stream()
					.map(documento -> documento.setTipo(TipoDocumentoUma.DICHIARAZIONE_CONSUMI))
					.collect(Collectors.toList());

			stream = Stream.concat(richiesteAll.stream(), consumiAll.stream());
		}

		return stream.map(domanda -> {
			boolean isInCompilazione = TipoDocumentoUma.DICHIARAZIONE_CONSUMI.equals(domanda.getTipo()) ? 
					StatoDichiarazioneConsumi.valueOf(domanda.getStato()).equals(StatoDichiarazioneConsumi.IN_COMPILAZIONE) :
						StatoRichiestaCarburante.valueOf(domanda.getStato()).equals(StatoRichiestaCarburante.IN_COMPILAZIONE);

			var clazz = TipoDocumentoUma.DICHIARAZIONE_CONSUMI.equals(domanda.getTipo()) ? DichiarazioneConsumiModel.class : RichiestaCarburanteModel.class;

			String operatore = isInCompilazione ? loggingService.getOperatoreCreazione(clazz, domanda.getId()) : loggingService.getOperatoreUltimoAggiornamento(clazz, domanda.getId());
			LocalDateTime dataDomanda;
			try {
				dataDomanda = isInCompilazione ? domanda.getDataPresentazione(): domanda.getDataProtocollazione();
			} catch(Exception e) {
				logger.error("[GeneraElencoDomande] - ERRORE nel reperimento date in fase di creazione del csv. ID: {} - CUAA: {} - DOCUMENTO: {}" , domanda.getId(), domanda.getCuaa(), domanda.getTipo());
				dataDomanda = null;
			}
			return new ElencoDomande()
					.setDataDomanda(dataDomanda != null ? dataDomanda.format(formatter): null)
					.setTipoDomanda(domanda.getTipo())
					.setOperatore(operatore)
					.setStatoDomanda(domanda.getStato())
					.setCampagna(campagna)
					.setCuaa(domanda.getCuaa())
					.setDenominazione(domanda.getDenominazione())
					.setEnte(domanda.getEnte())
					.setIdDomanda(domanda.getId());
		})
				.sorted(dateComparator)
				.collect(Collectors.toList());
	}

	@Override
	protected CsvSchema getSchema() {
		return CsvSchema.builder()
				.addColumn(ENTE_PRESENTATORE, CsvSchema.ColumnType.STRING)
				.addColumn(CUAA, CsvSchema.ColumnType.STRING)
				.addColumn(DENOMINAZIONE, CsvSchema.ColumnType.STRING)
				.addColumn(ID_DOMANDA, CsvSchema.ColumnType.NUMBER)
				.addColumn(DATA_DOMANDA, CsvSchema.ColumnType.STRING)
				.addColumn(TIPO_DOMANDA, CsvSchema.ColumnType.STRING)
				.addColumn(STATO_DOMANDA, CsvSchema.ColumnType.STRING)
				.addColumn(CAMPAGNA, CsvSchema.ColumnType.NUMBER)
				.addColumn(OPERATORE, CsvSchema.ColumnType.STRING)
				.build().withColumnSeparator(';').withHeader();
	}

	@Override
	protected String getFileName(Long campagna) {
		return "ElencoDomandeUMA_" + campagna.toString();
	}

	Comparator<ElencoDomande> dateComparator = (x,y) -> {
		if (x.getDataDomanda() == null) return 1;
		if (y.getDataDomanda() == null) return -1;

		LocalDate data1 = LocalDate.parse(x.getDataDomanda(), formatter);
		LocalDate data2 = LocalDate.parse(y.getDataDomanda(), formatter);

		return data1.compareTo(data2);
	};

	protected class ElencoDomande extends ElencoBaseTemplate {


		private String dataDomanda;
		private TipoDocumentoUma tipoDomanda;
		private String statoDomanda;
		private String operatore;


		@Override
		@JsonProperty(ENTE_PRESENTATORE)
		public String getEnte() {
			return super.getEnte();
		}
		@Override
		@JsonProperty(CUAA)
		public String getCuaa() {
			return super.getCuaa();
		}
		@Override
		@JsonProperty(DENOMINAZIONE)
		public String getDenominazione() {
			return super.getDenominazione();
		}
		@Override
		@JsonProperty(ID_DOMANDA)
		public Long getIdDomanda() {
			return super.getIdDomanda();
		}
		@Override
		@JsonProperty(CAMPAGNA)
		public Long getCampagna() {
			return super.getCampagna();
		}
		@JsonProperty(DATA_DOMANDA)
		public String getDataDomanda() {
			return dataDomanda;
		}

		public ElencoDomande setDataDomanda(String dataDomanda) {
			this.dataDomanda = dataDomanda;
			return this;
		}

		@JsonProperty(TIPO_DOMANDA)
		public TipoDocumentoUma getTipoDomanda() {
			return tipoDomanda;
		}

		public ElencoDomande setTipoDomanda(TipoDocumentoUma tipoDomanda) {
			this.tipoDomanda = tipoDomanda;
			return this;
		}

		@JsonProperty(STATO_DOMANDA)
		public String getStatoDomanda() {
			return statoDomanda;
		}

		public ElencoDomande setStatoDomanda(String statoDomanda) {
			this.statoDomanda = statoDomanda;
			return this;
		}

		@JsonProperty(OPERATORE)
		public String getOperatore() {
			return operatore;
		}

		public ElencoDomande setOperatore(String operatore) {
			this.operatore = operatore;
			return this;
		}
		// setter classe padre

		@Override
		public ElencoDomande setEnte(String ente) {
			super.setEnte(ente);
			return this;
		}

		@Override
		public ElencoDomande setCuaa(String cuaa) {
			super.setCuaa(cuaa);
			return this;
		}

		@Override
		public ElencoDomande setDenominazione(String denominazione) {
			super.setDenominazione(denominazione);
			return this;
		}

		@Override
		public ElencoDomande setIdDomanda(Long idDomanda) {
			super.setIdDomanda(idDomanda);
			return this;
		}

		@Override
		public ElencoDomande setCampagna(Long campagna) {
			super.setCampagna(campagna);
			return this;
		}

	}
}
