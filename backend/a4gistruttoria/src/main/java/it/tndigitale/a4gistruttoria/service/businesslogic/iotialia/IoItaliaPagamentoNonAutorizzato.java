package it.tndigitale.a4gistruttoria.service.businesslogic.iotialia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.CertificazioneAntimafiaFilter;
import it.tndigitale.a4gistruttoria.dto.IoItaliaMessage;
import it.tndigitale.a4gistruttoria.dto.PageResultWrapper;
import it.tndigitale.a4gistruttoria.dto.antimafia.DichiarazioneAntimafia;
import it.tndigitale.a4gistruttoria.dto.domandaunica.TransizioniHelper;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.repository.dao.PassoTransizioneDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi;
import it.tndigitale.a4gistruttoria.util.CustomCollectors;
import it.tndigitale.a4gistruttoria.util.CustomConverters;
import it.tndigitale.a4gistruttoria.util.StatoDichiarazioneEnum;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
@Component(IoItaliaPagamentoNonAutorizzato.NOME_QUALIFICATORE)
public class IoItaliaPagamentoNonAutorizzato extends IoItaliaMessaggioByStato {
	private static final String PROP_MESS_PREFIX="pagamento-non-autorizzato";
	public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "PAGAMENTO_NON_AUTORIZZATO";

	private static final List<StatoDichiarazioneEnum> STATI_VALIDI_DICHIARAZIONE_AMF = Arrays.asList(StatoDichiarazioneEnum.CONTROLLATA, StatoDichiarazioneEnum.PROTOCOLLATA);

	@Autowired
	private ConsumeExternalRestApi consumeExternalRestApi;
	@Autowired
	private PassoTransizioneDao passoTransizioneDao;

	@Override
	protected void buildMessage(IoItaliaMessage ioItaliaMessage, IstruttoriaModel istruttoriaModel) {

		ioItaliaMessage.setMessaggio(
				String.format(ioItaliaMessage.getMessaggio(), 
						LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),//Il giorno <gg/mm/aaaa> 
						istruttoriaModel.getSostegno().name(),// <DESCRIZIONE SOSTEGNO>
						istruttoriaModel.getDomandaUnicaModel().getCampagna(),//<CAMPAGNA DI RIFERIMENTO> 
						istruttoriaModel.getDomandaUnicaModel().getCuaaIntestatario(),//<CUAA>
						istruttoriaModel.getDomandaUnicaModel().getRagioneSociale(),//<DESCRIZIONE IMPRESA>
						istruttoriaModel.getDomandaUnicaModel().getNumeroDomanda()//<NUMERO DOMANDA>
						)
				);
	}

	@Override
	protected String getPropPrefix() {
		return PROP_MESS_PREFIX;
	}

	@Override
	protected Boolean checkInvio(IstruttoriaModel istruttoriaModel) {
		return Stream.of(
				checkImportoMinimoAntimafiaRaggiunto,
				checkDichiarazioneAntimafiaValida
				)
				.map(f -> f.apply(istruttoriaModel))
				.filter(Boolean.FALSE::equals)
				.collect(Collectors.toList()).isEmpty();
	}


	// IOIT-DU-01-04: verifica la validità della certificazione antimafia che è data dallo stato in cui si trova la domanda di certificazione antimafia per il CUAA - protocollata - in istruttoria)
	private Function<IstruttoriaModel, Boolean> checkDichiarazioneAntimafiaValida = istruttoriaModel-> {
		String cuaa = istruttoriaModel.getDomandaUnicaModel().getCuaaIntestatario();

		// chiamata a a4gfascicolo: tutte le dichiarazioni negli stati validi (CONTROLLATA - PROTOCOLLATA) aventi il cuaa dell'istruttoria
		CertificazioneAntimafiaFilter filter = new CertificazioneAntimafiaFilter();
		List<DichiarazioneAntimafia> dichiarazioniTrovate = new ArrayList<>();
		try {
			filter.setFiltroGenerico(cuaa);
			filter.setStato(STATI_VALIDI_DICHIARAZIONE_AMF.get(0));
			PageResultWrapper<DichiarazioneAntimafia> dichiarazioniAntimafiaControllate = consumeExternalRestApi.getDichiarazioniAntimafiaPage(filter, null, null);
			filter.setFiltroGenerico(cuaa);
			filter.setStato(STATI_VALIDI_DICHIARAZIONE_AMF.get(1));
			PageResultWrapper<DichiarazioneAntimafia> dichiarazioniAntimafiaProtocollate = consumeExternalRestApi.getDichiarazioniAntimafiaPage(filter, null, null);
			dichiarazioniTrovate.addAll(dichiarazioniAntimafiaProtocollate.getResults());
			dichiarazioniTrovate.addAll(dichiarazioniAntimafiaControllate.getResults());
		} catch (Exception e) {
			return Boolean.FALSE;
		}
		return dichiarazioniTrovate.size() == 1 ? Boolean.TRUE : Boolean.FALSE;
	};

	// IOIT-DU-01-04: BRIDUSDS049 vera -  importo antiamfia raggiunto
	private Function<IstruttoriaModel, Boolean> checkImportoMinimoAntimafiaRaggiunto = istruttoriaModel-> {
		List<TransizioneIstruttoriaModel> transizioniDiOgniStato = TransizioniHelper.recuperaTransizioniSostegno(istruttoriaModel);
		Optional<TransizioneIstruttoriaModel> tranzioneOpt = TransizioniHelper.recuperaTransizioneSostegnoByStato(transizioniDiOgniStato, istruttoriaModel.getStato());

		if (tranzioneOpt.isPresent()) {
			List<PassoTransizioneModel> passi = passoTransizioneDao.findByTransizioneIstruttoria(tranzioneOpt.get());
			List<EsitoControllo> esitiControlliImportiAntimafia = passi
					.stream().filter(passo -> TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA.equals(passo.getCodicePasso()))
					.map(item -> CustomConverters.jsonConvert(item.getDatiSintesiLavorazione(), DatiSintesi.class))
					.flatMap(item -> item.getEsitiControlli().stream())
					.collect(Collectors.toList());

			EsitoControllo esitoControllo = esitiControlliImportiAntimafia
					.stream()
					.filter(esito -> TipoControllo.BRIDUSDS049_importoMinimoAntimafia.equals(esito.getTipoControllo()))
					.collect(CustomCollectors.toSingleton());

			// esito false = importo raggiunto
			return !esitoControllo.getEsito();
		}
		return Boolean.FALSE;
	};
}
