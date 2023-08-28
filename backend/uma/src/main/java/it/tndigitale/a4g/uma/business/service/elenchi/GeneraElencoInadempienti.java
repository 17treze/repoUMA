package it.tndigitale.a4g.uma.business.service.elenchi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.ColumnType;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.SportelloFascicoloDto;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.uma.Ruoli;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.dto.DomandaUmaDto;

@Component(ElenchiTemplate.PREFISSO + "INADEMPIENTI")
public class GeneraElencoInadempienti extends ElenchiTemplate {

	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired 
	private UtenteComponent utenteComponent;
	@Autowired
	private UmaAnagraficaClient anagraficaClient;

	@Override
	protected Class<? extends ElencoBaseTemplate> getPojo() {
		return ElencoInadempienti.class;
	}

	@Override
	protected CsvSchema getSchema() {
		return CsvSchema.builder()
				.addColumn(ENTE_PRESENTATORE, CsvSchema.ColumnType.STRING)
				.addColumn(CUAA, CsvSchema.ColumnType.STRING).setColumnType(1, ColumnType.STRING)
				.addColumn(DENOMINAZIONE, CsvSchema.ColumnType.STRING)
				.addColumn(ID_DOMANDA, CsvSchema.ColumnType.NUMBER)
				.addColumn(STATO_DOMANDA, CsvSchema.ColumnType.STRING)
				.addColumn(CAMPAGNA, CsvSchema.ColumnType.NUMBER)
				.build().withColumnSeparator(';').withHeader();
	}

	@Override
	protected List<ElencoBaseTemplate> getDati(Long campagna) {
		List<String> cuaaList = new ArrayList<>();
		List<DomandaUmaDto> richiesteInadempienti = richiestaCarburanteDao.findRichiesteInadempienti(campagna);

		if (CollectionUtils.isEmpty(richiesteInadempienti)) {
			return new ArrayList<>();
		}

//		if (utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)) { // OPERATORE CAA: effettua filtro
//			cuaaList = anagraficaClient.getSportelliFascicoli().stream()
//					.map(SportelloFascicoloDto::getCuaaList)
//					.flatMap(List::stream)
//					.collect(Collectors.toList());
//		}

		if (!CollectionUtils.isEmpty(cuaaList)) {
			final var list = cuaaList;
			richiesteInadempienti = richiesteInadempienti.stream().parallel()
					.filter(richiesta -> list.contains(richiesta.getCuaa()))
					.collect(Collectors.toList());
		}

		return richiesteInadempienti.stream().map(richiesta -> new ElencoInadempienti()
				.setStato(StatoRichiestaCarburante.AUTORIZZATA.name())
				.setEnte(richiesta.getEnte())
				.setCuaa(richiesta.getCuaa())
				.setDenominazione(richiesta.getDenominazione())
				.setIdDomanda(richiesta.getId())
				.setCampagna(campagna))
				.collect(Collectors.toList());
	}

	@Override
	protected String getFileName(Long campagna) {
		return "ElencoInadempienti_" + campagna.toString();
	}

	@JsonPropertyOrder({ENTE_PRESENTATORE, CUAA, DENOMINAZIONE, ID_DOMANDA, STATO_DOMANDA, CAMPAGNA})
	private class ElencoInadempienti extends ElencoBaseTemplate {

		private String stato;

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

		@JsonProperty(STATO_DOMANDA)
		public String getStato() {
			return stato;
		}
		public ElencoBaseTemplate setStato(String stato) {
			this.stato = stato;
			return this;
		}
	}
}
