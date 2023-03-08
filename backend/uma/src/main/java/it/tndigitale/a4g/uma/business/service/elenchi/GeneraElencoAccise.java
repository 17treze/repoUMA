package it.tndigitale.a4g.uma.business.service.elenchi;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.service.consumi.calcoli.CarburanteHelper;
import it.tndigitale.a4g.uma.business.service.richiesta.RichiestaCarburanteService;

@Component(ElenchiTemplate.PREFISSO + "ACCISE")
public class GeneraElencoAccise extends ElenchiTemplate {

	private static final String DATA_DICHIARAZIONE = "DATA_DICHIARAZIONE";
	private static final String ID_DICHIARAZIONE = "ID_DICHIARAZIONE";

	private static final String GASOLIO_TOT_RESIDUO = "GASOLIO_TOTALE_RESIDUO";
	private static final String GASOLIO_TOT_AMMISSIBILE = "GASOLIO_TOTALE_AMMISSIBILE";
	private static final String GASOLIO_TOT_RICHIESTO = "GASOLIO_TOTALE_RICHIESTO";
	private static final String GASOLIO_TOT_AMMESSO = "GASOLIO_TOTALE_AMMESSO";
	private static final String GASOLIO_PRELEVATO = "GASOLIO_PRELEVATO";
	private static final String GASOLIO_RICEVUTO = "GASOLIO_RICEVUTO";
	private static final String GASOLIO_TRASFERITO = "GASOLIO_TRASFERITO";
	private static final String GASOLIO_TOT_CONSUMATO = "GASOLIO_TOTALE_CONSUMATO";
	private static final String GASOLIO_TOT_RIMANENZA = "GASOLIO_TOTALE_RIMANENZA";
	private static final String GASOLIO_TOT_RECUPERO = "GASOLIO_TOTALE_RECUPERO";
	private static final String GASOLIO_ACCISA = "GASOLIO_ACCISA";

	private static final String BENZINA_RESIDUA = "BENZINA_RESIDUA";
	private static final String BENZINA_AMMISSIBILE = "BENZINA_AMMISSIBILE";
	private static final String BENZINA_RICHIESTA = "BENZINA_RICHIESTA";
	private static final String BENZINA_AMMESSA = "BENZINA_AMMESSA";
	private static final String BENZINA_PRELEVATA = "BENZINA_PRELEVATA";
	private static final String BENZINA_RICEVUTA = "BENZINA_RICEVUTA";
	private static final String BENZINA_TRASFERITA = "BENZINA_TRASFERITA";
	private static final String BENZINA_CONSUMATA = "BENZINA_CONSUMATA";
	private static final String BENZINA_RIMANENZA = "BENZINA_RIMANENZA";
	private static final String BENZINA_RECUPERO = "BENZINA_RECUPERO";
	private static final String BENZINA_ACCISA = "BENZINA_ACCISA";

	private static final String GASOLIO_SERRE_RESIDUO = "GASOLIO_SERRE_RESIDUO";
	private static final String GASOLIO_SERRE_AMMISSIBILE = "GASOLIO_SERRE_AMMISSIBILE";
	private static final String GASOLIO_SERRE_RICHIESTO = "GASOLIO_SERRE_RICHIESTO";
	private static final String GASOLIO_SERRE_AMMESSO = "GASOLIO_SERRE_AMMESSO";
	private static final String GASOLIO_SERRE_PRELEVATO = "GASOLIO_SERRE_PRELEVATO";
	private static final String GASOLIO_SERRE_RICEVUTO = "GASOLIO_SERRE_RICEVUTO";
	private static final String GASOLIO_SERRE_TRASFERITO = "GASOLIO_SERRE_TRASFERITO";
	private static final String GASOLIO_SERRE_CONSUMATO = "GASOLIO_SERRE_CONSUMATO";
	private static final String GASOLIO_SERRE_RIMANENZA = "GASOLIO_SERRE_RIMANENZA";
	private static final String GASOLIO_SERRE_RECUPERO = "GASOLIO_SERRE_RECUPERO";
	private static final String GASOLIO_SERRE_ACCISA = "GASOLIO_SERRE_ACCISA";

	private static final String MOTIVAZIONE_ACCISA = "MOTIVAZIONE_ACCISA";

	@Autowired
	private RichiestaCarburanteService richiestaCarburanteService;
	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;
	@Autowired
	private CarburanteHelper carburanteHelper;


	@Override
	protected Class<? extends ElencoBaseTemplate> getPojo() {
		return ElencoAccisa.class;
	}

	@Override
	protected CsvSchema getSchema() {
		return CsvSchema.builder()
				.addColumn(ENTE_PRESENTATORE, CsvSchema.ColumnType.STRING)
				.addColumn(CUAA, CsvSchema.ColumnType.STRING)
				.addColumn(DENOMINAZIONE, CsvSchema.ColumnType.STRING)
				.addColumn(ID_DICHIARAZIONE, CsvSchema.ColumnType.NUMBER)
				.addColumn(DATA_DICHIARAZIONE, CsvSchema.ColumnType.STRING)
				.addColumn(STATO_DOMANDA, CsvSchema.ColumnType.STRING)
				.addColumn(CAMPAGNA, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_TOT_RESIDUO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_TOT_AMMISSIBILE, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_TOT_RICHIESTO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_TOT_AMMESSO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_PRELEVATO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_RICEVUTO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_TRASFERITO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_TOT_CONSUMATO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_TOT_RIMANENZA, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_TOT_RECUPERO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_ACCISA, CsvSchema.ColumnType.NUMBER)
				.addColumn(BENZINA_RESIDUA, CsvSchema.ColumnType.NUMBER)
				.addColumn(BENZINA_AMMISSIBILE, CsvSchema.ColumnType.NUMBER)
				.addColumn(BENZINA_RICHIESTA, CsvSchema.ColumnType.NUMBER)
				.addColumn(BENZINA_AMMESSA, CsvSchema.ColumnType.NUMBER)
				.addColumn(BENZINA_PRELEVATA, CsvSchema.ColumnType.NUMBER)
				.addColumn(BENZINA_RICEVUTA, CsvSchema.ColumnType.NUMBER)
				.addColumn(BENZINA_TRASFERITA, CsvSchema.ColumnType.NUMBER)
				.addColumn(BENZINA_CONSUMATA, CsvSchema.ColumnType.NUMBER)
				.addColumn(BENZINA_RIMANENZA, CsvSchema.ColumnType.NUMBER)
				.addColumn(BENZINA_RECUPERO, CsvSchema.ColumnType.NUMBER)
				.addColumn(BENZINA_ACCISA, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_SERRE_RESIDUO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_SERRE_AMMISSIBILE, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_SERRE_RICHIESTO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_SERRE_AMMESSO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_SERRE_PRELEVATO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_SERRE_RICEVUTO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_SERRE_TRASFERITO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_SERRE_CONSUMATO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_SERRE_RIMANENZA, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_SERRE_RECUPERO, CsvSchema.ColumnType.NUMBER)
				.addColumn(GASOLIO_SERRE_ACCISA, CsvSchema.ColumnType.NUMBER)
				.addColumn(MOTIVAZIONE_ACCISA, CsvSchema.ColumnType.STRING)
				.build().withColumnSeparator(';').withHeader();
	}

	@Override
	protected List<ElencoBaseTemplate> getDati(Long campagna) {
		List<DichiarazioneConsumiModel> dichiarazioni = dichiarazioneConsumiDao.findByRichiestaCarburante_campagnaAndStato(campagna, StatoDichiarazioneConsumi.PROTOCOLLATA)
				.stream()
				.filter(c -> c.getMotivazioneAccisa() != null)
				.collect(Collectors.toList());

		return dichiarazioni.stream()
				.map(dichiarazione -> {
					RichiestaCarburanteModel richiesta = dichiarazione.getRichiestaCarburante();
					var accisa = carburanteHelper.calcolaAccisa(dichiarazione);
					var ammissibile = richiestaCarburanteService.calcolaCarburanteAmmissibile(richiesta.getId());
					var ammesso = carburanteHelper.getAmmissibileCompleto(dichiarazione);
					var residuo = carburanteHelper.getResiduoAnnoPrecedente(richiesta.getCuaa(), campagna);
					var ricevuto = carburanteHelper.getTotaleCarburanteRicevuto(richiesta.getCuaa(), campagna);
					var trasferito = carburanteHelper.getTotaleCarburanteTrasferito(richiesta.getCuaa(), campagna);
					var prelevato = carburanteHelper.getTotaleCarburantePrelevato(richiesta.getCuaa(), campagna);
					var consumato = carburanteHelper.getConsuntivo(Optional.of(dichiarazione), TipoConsuntivo.CONSUMATO);
					var recupero = carburanteHelper.getConsuntivo(Optional.of(dichiarazione), TipoConsuntivo.RECUPERO);
					var rimanenza = carburanteHelper.getConsuntivo(Optional.of(dichiarazione), TipoConsuntivo.RIMANENZA);
					return new ElencoAccisa()
							.setMotivazioneAccisa(dichiarazione.getMotivazioneAccisa())
							.setDataDichiarazione(dichiarazione.getDataProtocollazione() != null ? dichiarazione.getDataProtocollazione().format(formatter) : null)
							.setStatoDichiarazione(dichiarazione.getStato().name())
							.setIdDomanda(dichiarazione.getId())
							.setCampagna(campagna)
							.setCuaa(richiesta.getCuaa())
							.setEnte(richiesta.getEntePresentatore())
							.setDenominazione(richiesta.getDenominazione())
							.setGasolioTotAmmesso(ammesso.getGasolio().longValue() + ammesso.getGasolioTerzi().longValue())
							.setGasolioTotAmmissibile(ammissibile.getGasolio().longValue())
							.setGasolioTotConsumato(consumato.getGasolio().longValue() + consumato.getGasolioTerzi().longValue())
							.setGasolioTotPrelevato(prelevato.getGasolio().longValue())
							.setGasolioTotRecupero(recupero.getGasolio().longValue() + recupero.getGasolioTerzi().longValue())
							.setGasolioTotResiduo(residuo.getGasolio().longValue() + residuo.getGasolioTerzi().longValue())
							.setGasolioTotRicevuto(ricevuto.getGasolio().longValue())
							.setGasolioTotRichiesto(richiesta.getGasolio().longValue() + richiesta.getGasolioTerzi().longValue())
							.setGasolioTotRimanenza(rimanenza.getGasolio().longValue() + rimanenza.getGasolioTerzi().longValue())
							.setGasolioTotTrasferito(trasferito.getGasolio().longValue())
							.setGasolioAccisa(accisa.getGasolio().longValue())
							.setBenzinaAmmessa(ammesso.getBenzina().longValue())
							.setBenzinaAmmissibile(ammissibile.getBenzina().longValue())
							.setBenzinaConsumata(consumato.getBenzina().longValue())
							.setBenzinaPrelevata(prelevato.getBenzina().longValue())
							.setBenzinaRecupero(recupero.getBenzina().longValue())
							.setBenzinaResidua(residuo.getBenzina().longValue())
							.setBenzinaRicevuta(ricevuto.getBenzina().longValue())
							.setBenzinaRichiesta(richiesta.getBenzina().longValue())
							.setBenzinaRimanenza(rimanenza.getBenzina().longValue())
							.setBenzinaTrasferita(trasferito.getBenzina().longValue())
							.setBenzinaAccisa(accisa.getBenzina().longValue())
							.setGasolioSerreAmmesso(ammesso.getGasolioSerre().longValue())
							.setGasolioSerreAmmissibile(ammissibile.getGasolioSerre().longValue())
							.setGasolioSerreConsumato(consumato.getGasolioSerre().longValue())
							.setGasolioSerrePrelevato(prelevato.getGasolioSerre().longValue())
							.setGasolioSerreRecupero(recupero.getGasolioSerre().longValue())
							.setGasolioSerreResiduo(residuo.getGasolioSerre().longValue())
							.setGasolioSerreRicevuto(ricevuto.getGasolioSerre().longValue())
							.setGasolioSerreRichiesto(richiesta.getGasolioSerre().longValue())
							.setGasolioSerreRimanenza(rimanenza.getGasolioSerre().longValue())
							.setGasolioSerreTrasferito(trasferito.getGasolioSerre().longValue())
							.setGasolioSerreAccisa(accisa.getGasolioSerre().longValue());
				})
				.sorted(dateComparator)
				.collect(Collectors.toList());
	}

	Comparator<ElencoAccisa> dateComparator = (x,y) -> {
		if (x.getDataDichiarazione() == null) return 1;
		if (y.getDataDichiarazione() == null) return -1;

		LocalDate data1 = LocalDate.parse(x.getDataDichiarazione(), formatter);
		LocalDate data2 = LocalDate.parse(y.getDataDichiarazione(), formatter);

		return data1.compareTo(data2);
	};

	@Override
	protected String getFileName(Long campagna) {
		return "ElencoAccise_" + campagna.toString();
	}

	private class ElencoAccisa extends ElencoBaseTemplate {

		private String dataDichiarazione;
		private String statoDichiarazione;

		private Long gasolioTotResiduo;
		private Long gasolioTotAmmissibile;
		private Long gasolioTotRichiesto;
		private Long gasolioTotAmmesso;
		private Long gasolioTotPrelevato;
		private Long gasolioTotRicevuto;
		private Long gasolioTotTrasferito;
		private Long gasolioTotConsumato;
		private Long gasolioTotRimanenza;
		private Long gasolioTotRecupero;
		private Long gasolioAccisa;

		private Long benzinaResidua;
		private Long benzinaAmmissibile;
		private Long benzinaRichiesta;
		private Long benzinaAmmessa;
		private Long benzinaPrelevata;
		private Long benzinaRicevuta;
		private Long benzinaTrasferita;
		private Long benzinaConsumata;
		private Long benzinaRimanenza;
		private Long benzinaRecupero;
		private Long benzinaAccisa;

		private Long gasolioSerreResiduo;
		private Long gasolioSerreAmmissibile;
		private Long gasolioSerreRichiesto;
		private Long gasolioSerreAmmesso;
		private Long gasolioSerrePrelevato;
		private Long gasolioSerreRicevuto;
		private Long gasolioSerreTrasferito;
		private Long gasolioSerreConsumato;
		private Long gasolioSerreRimanenza;
		private Long gasolioSerreRecupero;
		private Long gasolioSerreAccisa;

		private String motivazioneAccisa;

		@Override
		@JsonProperty(ID_DICHIARAZIONE)
		public Long getIdDomanda() {
			return super.getIdDomanda();
		}

		@Override
		public ElencoAccisa setEnte(String ente) {
			super.setEnte(ente);
			return this;
		}

		@Override
		public ElencoAccisa setCuaa(String cuaa) {
			super.setCuaa(cuaa);
			return this;
		}

		@Override
		public ElencoAccisa setDenominazione(String denominazione) {
			super.setDenominazione(denominazione);
			return this;
		}

		@Override
		public ElencoAccisa setIdDomanda(Long idDomanda) {
			super.setIdDomanda(idDomanda);
			return this;
		}

		@Override
		public ElencoAccisa setCampagna(Long campagna) {
			super.setCampagna(campagna);
			return this;
		}

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
		@JsonProperty(CAMPAGNA)
		public Long getCampagna() {
			return super.getCampagna();
		}

		@JsonProperty(DATA_DICHIARAZIONE)
		public String getDataDichiarazione() {
			return dataDichiarazione;
		}

		public ElencoAccisa setDataDichiarazione(String dataDichiarazione) {
			this.dataDichiarazione = dataDichiarazione;
			return this;
		}

		@JsonProperty(STATO_DOMANDA)
		public String getStatoDichiarazione() {
			return statoDichiarazione;
		}

		public ElencoAccisa setStatoDichiarazione(String statoDichiarazione) {
			this.statoDichiarazione = statoDichiarazione;
			return this;
		}

		@JsonProperty(GASOLIO_TOT_RESIDUO)
		public Long getGasolioTotResiduo() {
			return gasolioTotResiduo;
		}

		public ElencoAccisa setGasolioTotResiduo(Long gasolioTotResiduo) {
			this.gasolioTotResiduo = gasolioTotResiduo;
			return this;
		}

		@JsonProperty(GASOLIO_TOT_AMMISSIBILE)
		public Long getGasolioTotAmmissibile() {
			return gasolioTotAmmissibile;
		}

		public ElencoAccisa setGasolioTotAmmissibile(Long gasolioTotAmmissibile) {
			this.gasolioTotAmmissibile = gasolioTotAmmissibile;
			return this;
		}

		@JsonProperty(GASOLIO_TOT_RICHIESTO)
		public Long getGasolioTotRichiesto() {
			return gasolioTotRichiesto;
		}

		public ElencoAccisa setGasolioTotRichiesto(Long gasolioTotRichiesto) {
			this.gasolioTotRichiesto = gasolioTotRichiesto;
			return this;
		}

		@JsonProperty(GASOLIO_TOT_AMMESSO)
		public Long getGasolioTotAmmesso() {
			return gasolioTotAmmesso;
		}

		public ElencoAccisa setGasolioTotAmmesso(Long gasolioTotAmmesso) {
			this.gasolioTotAmmesso = gasolioTotAmmesso;
			return this;
		}

		@JsonProperty(GASOLIO_PRELEVATO)
		public Long getGasolioTotPrelevato() {
			return gasolioTotPrelevato;
		}

		public ElencoAccisa setGasolioTotPrelevato(Long gasolioTotPrelevato) {
			this.gasolioTotPrelevato = gasolioTotPrelevato;
			return this;
		}

		@JsonProperty(GASOLIO_RICEVUTO)
		public Long getGasolioTotRicevuto() {
			return gasolioTotRicevuto;
		}

		public ElencoAccisa setGasolioTotRicevuto(Long gasolioTotRicevuto) {
			this.gasolioTotRicevuto = gasolioTotRicevuto;
			return this;
		}

		@JsonProperty(GASOLIO_TRASFERITO)
		public Long getGasolioTotTrasferito() {
			return gasolioTotTrasferito;
		}

		public ElencoAccisa setGasolioTotTrasferito(Long gasolioTotTrasferito) {
			this.gasolioTotTrasferito = gasolioTotTrasferito;
			return this;
		}

		@JsonProperty(GASOLIO_TOT_CONSUMATO)
		public Long getGasolioTotConsumato() {
			return gasolioTotConsumato;
		}

		public ElencoAccisa setGasolioTotConsumato(Long gasolioTotConsumato) {
			this.gasolioTotConsumato = gasolioTotConsumato;
			return this;
		}

		@JsonProperty(GASOLIO_TOT_RIMANENZA)
		public Long getGasolioTotRimanenza() {
			return gasolioTotRimanenza;
		}

		public ElencoAccisa setGasolioTotRimanenza(Long gasolioTotRimanenza) {
			this.gasolioTotRimanenza = gasolioTotRimanenza;
			return this;
		}

		@JsonProperty(GASOLIO_TOT_RECUPERO)
		public Long getGasolioTotRecupero() {
			return gasolioTotRecupero;
		}

		public ElencoAccisa setGasolioTotRecupero(Long gasolioTotRecupero) {
			this.gasolioTotRecupero = gasolioTotRecupero;
			return this;
		}

		@JsonProperty(GASOLIO_ACCISA)
		public Long getGasolioAccisa() {
			return gasolioAccisa;
		}

		public ElencoAccisa setGasolioAccisa(Long gasolioAccisa) {
			this.gasolioAccisa = gasolioAccisa;
			return this;
		}

		@JsonProperty(BENZINA_RESIDUA)
		public Long getBenzinaResidua() {
			return benzinaResidua;
		}

		public ElencoAccisa setBenzinaResidua(Long benzinaResidua) {
			this.benzinaResidua = benzinaResidua;
			return this;
		}

		@JsonProperty(BENZINA_AMMISSIBILE)
		public Long getBenzinaAmmissibile() {
			return benzinaAmmissibile;
		}

		public ElencoAccisa setBenzinaAmmissibile(Long benzinaAmmissibile) {
			this.benzinaAmmissibile = benzinaAmmissibile;
			return this;
		}

		@JsonProperty(BENZINA_RICHIESTA)
		public Long getBenzinaRichiesta() {
			return benzinaRichiesta;
		}

		public ElencoAccisa setBenzinaRichiesta(Long benzinaRichiesta) {
			this.benzinaRichiesta = benzinaRichiesta;
			return this;
		}

		@JsonProperty(BENZINA_AMMESSA)
		public Long getBenzinaAmmessa() {
			return benzinaAmmessa;
		}

		public ElencoAccisa setBenzinaAmmessa(Long benzinaAmmessa) {
			this.benzinaAmmessa = benzinaAmmessa;
			return this;
		}

		@JsonProperty(BENZINA_PRELEVATA)
		public Long getBenzinaPrelevata() {
			return benzinaPrelevata;
		}

		public ElencoAccisa setBenzinaPrelevata(Long benzinaPrelevata) {
			this.benzinaPrelevata = benzinaPrelevata;
			return this;
		}

		@JsonProperty(BENZINA_RICEVUTA)
		public Long getBenzinaRicevuta() {
			return benzinaRicevuta;
		}

		public ElencoAccisa setBenzinaRicevuta(Long benzinaRicevuta) {
			this.benzinaRicevuta = benzinaRicevuta;
			return this;
		}

		@JsonProperty(BENZINA_TRASFERITA)
		public Long getBenzinaTrasferita() {
			return benzinaTrasferita;
		}

		public ElencoAccisa setBenzinaTrasferita(Long benzinaTrasferita) {
			this.benzinaTrasferita = benzinaTrasferita;
			return this;
		}

		@JsonProperty(BENZINA_CONSUMATA)
		public Long getBenzinaConsumata() {
			return benzinaConsumata;
		}

		public ElencoAccisa setBenzinaConsumata(Long benzinaConsumata) {
			this.benzinaConsumata = benzinaConsumata;
			return this;
		}

		@JsonProperty(BENZINA_RIMANENZA)
		public Long getBenzinaRimanenza() {
			return benzinaRimanenza;
		}

		public ElencoAccisa setBenzinaRimanenza(Long benzinaRimanenza) {
			this.benzinaRimanenza = benzinaRimanenza;
			return this;
		}

		@JsonProperty(BENZINA_RECUPERO)
		public Long getBenzinaRecupero() {
			return benzinaRecupero;
		}

		public ElencoAccisa setBenzinaRecupero(Long benzinaRecupero) {
			this.benzinaRecupero = benzinaRecupero;
			return this;
		}

		@JsonProperty(BENZINA_ACCISA)
		public Long getBenzinaAccisa() {
			return benzinaAccisa;
		}

		public ElencoAccisa setBenzinaAccisa(Long benzinaAccisa) {
			this.benzinaAccisa = benzinaAccisa;
			return this;
		}

		@JsonProperty(GASOLIO_SERRE_RESIDUO)
		public Long getGasolioSerreResiduo() {
			return gasolioSerreResiduo;
		}

		public ElencoAccisa setGasolioSerreResiduo(Long gasolioSerreResiduo) {
			this.gasolioSerreResiduo = gasolioSerreResiduo;
			return this;
		}

		@JsonProperty(GASOLIO_SERRE_AMMISSIBILE)
		public Long getGasolioSerreAmmissibile() {
			return gasolioSerreAmmissibile;
		}

		public ElencoAccisa setGasolioSerreAmmissibile(Long gasolioSerreAmmissibile) {
			this.gasolioSerreAmmissibile = gasolioSerreAmmissibile;
			return this;
		}

		@JsonProperty(GASOLIO_SERRE_RICHIESTO)
		public Long getGasolioSerreRichiesto() {
			return gasolioSerreRichiesto;
		}

		public ElencoAccisa setGasolioSerreRichiesto(Long gasolioSerreRichiesto) {
			this.gasolioSerreRichiesto = gasolioSerreRichiesto;
			return this;
		}

		@JsonProperty(GASOLIO_SERRE_AMMESSO)
		public Long getGasolioSerreAmmesso() {
			return gasolioSerreAmmesso;
		}

		public ElencoAccisa setGasolioSerreAmmesso(Long gasolioSerreAmmesso) {
			this.gasolioSerreAmmesso = gasolioSerreAmmesso;
			return this;
		}

		@JsonProperty(GASOLIO_SERRE_PRELEVATO)
		public Long getGasolioSerrePrelevato() {
			return gasolioSerrePrelevato;
		}

		public ElencoAccisa setGasolioSerrePrelevato(Long gasolioSerrePrelevato) {
			this.gasolioSerrePrelevato = gasolioSerrePrelevato;
			return this;
		}

		@JsonProperty(GASOLIO_SERRE_RICEVUTO)
		public Long getGasolioSerreRicevuto() {
			return gasolioSerreRicevuto;
		}

		public ElencoAccisa setGasolioSerreRicevuto(Long gasolioSerreRicevuto) {
			this.gasolioSerreRicevuto = gasolioSerreRicevuto;
			return this;
		}

		@JsonProperty(GASOLIO_SERRE_TRASFERITO)
		public Long getGasolioSerreTrasferito() {
			return gasolioSerreTrasferito;
		}

		public ElencoAccisa setGasolioSerreTrasferito(Long gasolioSerreTrasferito) {
			this.gasolioSerreTrasferito = gasolioSerreTrasferito;
			return this;
		}

		@JsonProperty(GASOLIO_SERRE_CONSUMATO)
		public Long getGasolioSerreConsumato() {
			return gasolioSerreConsumato;
		}

		public ElencoAccisa setGasolioSerreConsumato(Long gasolioSerreConsumato) {
			this.gasolioSerreConsumato = gasolioSerreConsumato;
			return this;
		}

		@JsonProperty(GASOLIO_SERRE_RIMANENZA)
		public Long getGasolioSerreRimanenza() {
			return gasolioSerreRimanenza;
		}

		public ElencoAccisa setGasolioSerreRimanenza(Long gasolioSerreRimanenza) {
			this.gasolioSerreRimanenza = gasolioSerreRimanenza;
			return this;
		}

		@JsonProperty(GASOLIO_SERRE_RECUPERO)
		public Long getGasolioSerreRecupero() {
			return gasolioSerreRecupero;
		}

		public ElencoAccisa setGasolioSerreRecupero(Long gasolioSerreRecupero) {
			this.gasolioSerreRecupero = gasolioSerreRecupero;
			return this;
		}

		@JsonProperty(GASOLIO_SERRE_ACCISA)
		public Long getGasolioSerreAccisa() {
			return gasolioSerreAccisa;
		}

		public ElencoAccisa setGasolioSerreAccisa(Long gasolioSerreAccisa) {
			this.gasolioSerreAccisa = gasolioSerreAccisa;
			return this;
		}

		@JsonProperty(MOTIVAZIONE_ACCISA)
		public String getMotivazioneAccisa() {
			return motivazioneAccisa;
		}

		public ElencoAccisa setMotivazioneAccisa(String motivazioneAccisa) {
			this.motivazioneAccisa = motivazioneAccisa;
			return this;
		}
	}
}

