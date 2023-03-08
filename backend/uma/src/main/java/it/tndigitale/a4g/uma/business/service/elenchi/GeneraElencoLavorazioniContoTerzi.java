package it.tndigitale.a4g.uma.business.service.elenchi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import it.tndigitale.a4g.framework.support.CustomCollectors;
import it.tndigitale.a4g.framework.support.ListSupport;
import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.service.clienti.ClientiService;
import it.tndigitale.a4g.uma.business.service.lavorazioni.LavorazioniService;
import it.tndigitale.a4g.uma.dto.richiesta.DichiarazioneDto;
import it.tndigitale.a4g.uma.dto.richiesta.FabbisognoDto;
import it.tndigitale.a4g.uma.dto.richiesta.LavorazioneDto;
import it.tndigitale.a4g.uma.dto.richiesta.LavorazioneFilter.Lavorazioni;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;

@Component(ElenchiTemplate.PREFISSO + "LAVORAZIONI_CLIENTI_CONTO_TERZI")
public class GeneraElencoLavorazioniContoTerzi extends ElenchiTemplate  {

	private static final String ENTE_PRESENTATORE_CLIENTE = "ENTE_PRESENTATORE_CLIENTE";
	private static final String CUAA_CLIENTE = "CUAA_CLIENTE";
	private static final String DENOMINAZIONE_CLIENTE = "DENOMINAZIONE_CLIENTE";
	private static final String ID_DOMANDA_CLIENTE = "ID_DOMANDA_CLIENTE";

	private static final String ENTE_PRESENTATORE_CT = "ENTE_PRESENTATORE_CT";
	private static final String CUAA_CT = "CUAA_CT";
	private static final String DENOMINAZIONE_CT = "DENOMINAZIONE_CT";
	private static final String ID_DICHIARAZIONE_CONSUMI_CT = "ID_DICHIARAZIONE_CONSUMI_CT";

	private static final String COLTURA = "COLTURA";
	private static final String LAVORAZIONE = "LAVORAZIONE";

	private static final String SUPERFICIE_MASSIMA_CLIENTE = "SUPERFICIE_MASSIMA_CLIENTE";
	private static final String SUPERFICIE_GASOLIO_RICHIESTA_CLIENTE = "SUPERFICIE_GASOLIO_RICHIESTA_CLIENTE";
	private static final String SUPERFICIE_GASOLIO_RICHIESTA_CT = "SUPERFICIE_GASOLIO_RICHIESTA_CT";

	@Autowired
	private ClientiService clientiService;
	@Autowired
	private LavorazioniService lavorazioniService;
	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;

	@Override
	protected Class<? extends ElencoBaseTemplate> getPojo() {
		return ElencoLavorazioniContoTerzi.class;
	}

	@Override
	protected CsvSchema getSchema() {
		return CsvSchema.builder()
				.addColumn(ENTE_PRESENTATORE_CLIENTE, CsvSchema.ColumnType.STRING)
				.addColumn(CUAA_CLIENTE, CsvSchema.ColumnType.STRING)
				.addColumn(DENOMINAZIONE_CLIENTE, CsvSchema.ColumnType.STRING)
				.addColumn(ID_DOMANDA_CLIENTE, CsvSchema.ColumnType.NUMBER)
				.addColumn(CAMPAGNA, CsvSchema.ColumnType.NUMBER)
				.addColumn(ENTE_PRESENTATORE_CT, CsvSchema.ColumnType.STRING)
				.addColumn(CUAA_CT, CsvSchema.ColumnType.STRING)
				.addColumn(DENOMINAZIONE_CT, CsvSchema.ColumnType.STRING)
				.addColumn(ID_DICHIARAZIONE_CONSUMI_CT, CsvSchema.ColumnType.NUMBER)
				.addColumn(COLTURA, CsvSchema.ColumnType.STRING)
				.addColumn(LAVORAZIONE, CsvSchema.ColumnType.STRING)
				.addColumn(SUPERFICIE_GASOLIO_RICHIESTA_CLIENTE, CsvSchema.ColumnType.NUMBER)
				.addColumn(SUPERFICIE_GASOLIO_RICHIESTA_CT, CsvSchema.ColumnType.NUMBER)
				.addColumn(SUPERFICIE_MASSIMA_CLIENTE, CsvSchema.ColumnType.NUMBER)
				.build().withColumnSeparator(';').withHeader();
	}

	@Override
	protected List<ElencoBaseTemplate> getDati(Long campagna) {

		ArrayList<ElencoBaseTemplate> results = new ArrayList<>();
		// ciclare sulle dichiarazioni consumi di quell'anno di campagna protocollate
		dichiarazioneConsumiDao.findByRichiestaCarburante_campagnaAndStato(campagna, StatoDichiarazioneConsumi.PROTOCOLLATA).stream()
		.forEach(consumi -> consumi.getClienti().stream().forEach(cliente -> {
			// per ogni cliente -> get cuaa, campagna -> find richiesta by stato, cuaa e campagna
			Optional<RichiestaCarburanteModel> richiestaOpt = richiestaCarburanteDao.findByCuaaAndCampagnaAndStato(cliente.getCuaa(), campagna, StatoRichiestaCarburante.AUTORIZZATA);

			if (richiestaOpt.isPresent()) {
				// per ogni cliente -> get consumi clienti e fabbisogni e sup massima!
				List<RaggruppamentoLavorazioniDto> lavorazioniCt = clientiService.getLavorazioniSuperficie(cliente.getId()); // reperimento superfici massime relative ai gruppi
				List<DichiarazioneDto> fabbisogniCt = clientiService.getFabbisogniSuperficie(cliente.getId()); // reperimento fabbisogni espressi dal contoterzista

				// se trova la richiesta, estrarre i fabbisogni e costruirli con il builder 
				List<RaggruppamentoLavorazioniDto> lavorazioniCliente = lavorazioniService.getCategorieLavorazioni(richiestaOpt.get().getId(), AmbitoLavorazione.SUPERFICIE);
				List<DichiarazioneDto> fabbisogniCliente = lavorazioniService.getFabbisogni(richiestaOpt.get().getId(), Lavorazioni.SUPERFICIE);

				var fab1 = fabbisogniCt.stream().map(DichiarazioneDto::getLavorazioneId).collect(Collectors.toList());
				var fab2 = fabbisogniCliente.stream().map(DichiarazioneDto::getLavorazioneId).collect(Collectors.toList());

				// id delle lavorazioni in comune
				List<Long> idLavorazioniFabbisogniInComune = ListSupport.intersect(fab1, fab2);


				idLavorazioniFabbisogniInComune.stream().forEach(idLavorazioneComune -> {
					// ottenere nome gruppo				-> lavorazioniCliente
					// ottenere nome lavorazione 		-> lavorazioniCliente 
					// superficie gasolio cliente		-> fabbisogniCliente
					// sup gasolio conto terzista		-> fabbisogniCt
					// sup massima cliente 				-> lavorazioniCt

					// indice delle lavorazioni che mi interessano
					// ho bisogno di una mappa di indici! 

					Optional<BigDecimal> superficieGasolioClienteOpt = fabbisogniCliente.stream()
							.filter(f -> idLavorazioneComune.equals(f.getLavorazioneId()))
							.flatMap(x -> x.getFabbisogni().stream())
							.filter(f -> TipoCarburante.GASOLIO.equals(f.getCarburante()))
							.map(FabbisognoDto::getQuantita)
							.collect(CustomCollectors.collectOne());

					Optional<BigDecimal> superficieGasolioCtOpt = fabbisogniCt.stream()
							.filter(f -> idLavorazioneComune.equals(f.getLavorazioneId()))
							.flatMap(x -> x.getFabbisogni().stream())
							.filter(f -> TipoCarburante.GASOLIO.equals(f.getCarburante()))
							.map(FabbisognoDto::getQuantita)
							.collect(CustomCollectors.collectOne());

					// la superficie massima viene ricalcolata
					Long supMaxCliente = lavorazioniCt.stream().filter(gruppo -> gruppo.getLavorazioni().stream().anyMatch(lav -> idLavorazioneComune.equals(lav.getId())))
							.map(RaggruppamentoLavorazioniDto::getSuperficieMassima)
							.collect(CustomCollectors.toSingleton());

					RaggruppamentoLavorazioniDto gruppoLavorazioneCliente = lavorazioniCliente.stream().filter(gruppo -> gruppo.getLavorazioni().stream().anyMatch(lav -> idLavorazioneComune.equals(lav.getId())))
							.collect(CustomCollectors.toSingleton());

					LavorazioneDto lavorazioneCliente = gruppoLavorazioneCliente.getLavorazioni().stream().filter(lav -> idLavorazioneComune.equals(lav.getId())).collect(CustomCollectors.toSingleton());

					if (superficieGasolioClienteOpt.isPresent() && superficieGasolioCtOpt.isPresent()) {

						results.add(new ElencoLavorazioniContoTerzi()
								.setEnte(richiestaOpt.get().getEntePresentatore())
								.setCuaa(richiestaOpt.get().getCuaa())
								.setDenominazione(richiestaOpt.get().getDenominazione())
								.setIdDomanda(richiestaOpt.get().getId())
								.setEnteCt(consumi.getEntePresentatore())
								.setCuaaCt(consumi.getRichiestaCarburante().getCuaa())
								.setDenominazioneCt(consumi.getRichiestaCarburante().getDenominazione())
								.setIdDomandaCt(consumi.getId())
								.setGruppoLavorazione(gruppoLavorazioneCliente.getNome())
								.setLavorazione(lavorazioneCliente.getNome())
								.setSupMaxCliente(supMaxCliente.intValue())
								.setGasolioCliente(superficieGasolioClienteOpt.get().intValue())
								.setGasolioCt(superficieGasolioCtOpt.get().intValue())
								.setCampagna(campagna)
								.setCuaa(cliente.getCuaa()));
					}
				});
			}
		}));
		return results;
	}

	@Override
	protected String getFileName(Long campagna) {
		return "ElencoLavorazioniClientiContoTerzi_" + campagna.toString();
	}

	private class ElencoLavorazioniContoTerzi extends ElencoBaseTemplate {

		private String enteCt;
		private String cuaaCt;
		private String denominazioneCt;
		private Long idDomandaCt;

		private String gruppoLavorazione;
		private String lavorazione;

		private Integer supMaxCliente;

		private Integer gasolioCliente;
		private Integer gasolioCt;

		// getter e setter from parent


		@Override
		@JsonProperty(ENTE_PRESENTATORE_CLIENTE)
		public String getEnte() {
			return super.getEnte();
		}
		@Override
		public ElencoLavorazioniContoTerzi setEnte(String ente) {
			super.setEnte(ente);
			return this;
		}

		@Override
		@JsonProperty(CUAA_CLIENTE)
		public String getCuaa() {
			return super.getCuaa();
		}
		@Override
		public ElencoLavorazioniContoTerzi setCuaa(String cuaa) {
			super.setCuaa(cuaa);
			return this;
		}

		@Override
		@JsonProperty(DENOMINAZIONE_CLIENTE)
		public String getDenominazione() {
			return super.getDenominazione();
		}
		@Override
		public ElencoLavorazioniContoTerzi setDenominazione(String denominazione) {
			super.setDenominazione(denominazione);
			return this;
		}

		@Override
		@JsonProperty(ID_DOMANDA_CLIENTE)
		public Long getIdDomanda() {
			return super.getIdDomanda();
		}
		@Override
		public ElencoLavorazioniContoTerzi setIdDomanda(Long idDomanda) {
			super.setIdDomanda(idDomanda);
			return this;
		}

		@Override
		@JsonProperty(CAMPAGNA)
		public Long getCampagna() {
			return super.getCampagna();
		}
		@Override
		public ElencoLavorazioniContoTerzi setCampagna(Long campagna) {
			super.setCampagna(campagna);
			return this;
		}

		// dati specifici della classe
		@JsonProperty(ENTE_PRESENTATORE_CT)
		public String getEnteCt() {
			return enteCt;
		}
		public ElencoLavorazioniContoTerzi setEnteCt(String enteCt) {
			this.enteCt = enteCt;
			return this;
		}

		@JsonProperty(CUAA_CT)
		public String getCuaaCt() {
			return cuaaCt;
		}
		public ElencoLavorazioniContoTerzi setCuaaCt(String cuaaCt) {
			this.cuaaCt = cuaaCt;
			return this;
		}

		@JsonProperty(DENOMINAZIONE_CT)
		public String getDenominazioneCt() {
			return denominazioneCt;
		}
		public ElencoLavorazioniContoTerzi setDenominazioneCt(String denominazioneCt) {
			this.denominazioneCt = denominazioneCt;
			return this;
		}

		@JsonProperty(ID_DICHIARAZIONE_CONSUMI_CT)
		public Long getIdDomandaCt() {
			return idDomandaCt;
		}
		public ElencoLavorazioniContoTerzi setIdDomandaCt(Long idDomandaCt) {
			this.idDomandaCt = idDomandaCt;
			return this;
		}

		@JsonProperty(COLTURA)
		public String getGruppoLavorazione() {
			return gruppoLavorazione;
		}
		public ElencoLavorazioniContoTerzi setGruppoLavorazione(String gruppoLavorazione) {
			this.gruppoLavorazione = gruppoLavorazione;
			return this;
		}

		@JsonProperty(LAVORAZIONE)
		public String getLavorazione() {
			return lavorazione;
		}
		public ElencoLavorazioniContoTerzi setLavorazione(String lavorazione) {
			this.lavorazione = lavorazione;
			return this;
		}

		@JsonProperty(SUPERFICIE_MASSIMA_CLIENTE)
		public Integer getSupMaxCliente() {
			return supMaxCliente;
		}
		public ElencoLavorazioniContoTerzi setSupMaxCliente(Integer supMaxCliente) {
			this.supMaxCliente = supMaxCliente;
			return this;
		}

		@JsonProperty(SUPERFICIE_GASOLIO_RICHIESTA_CLIENTE)
		public Integer getGasolioCliente() {
			return gasolioCliente;
		}
		public ElencoLavorazioniContoTerzi setGasolioCliente(Integer gasolioCliente) {
			this.gasolioCliente = gasolioCliente;
			return this;
		}

		@JsonProperty(SUPERFICIE_GASOLIO_RICHIESTA_CT)
		public Integer getGasolioCt() {
			return gasolioCt;
		}
		public ElencoLavorazioniContoTerzi setGasolioCt(Integer gasolioCt) {
			this.gasolioCt = gasolioCt;
			return this;
		}
	}
}
