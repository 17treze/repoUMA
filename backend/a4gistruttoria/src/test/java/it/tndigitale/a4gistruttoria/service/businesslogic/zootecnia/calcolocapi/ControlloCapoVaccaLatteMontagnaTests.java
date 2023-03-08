package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.InformazioniAllevamento;
import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoLatteDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.VitelloDto;
import it.tndigitale.a4gistruttoria.repository.dao.AllevMontagnaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtAllevMontagna;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "utente")
class ControlloCapoVaccaLatteMontagnaTests extends ControlloCapoVaccaLatteMontagna {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AllevMontagnaDao allevMontagnaDao;

	//BR2 Individuazione del primo parto per il quale si richiede l’intervento 311
	@TestFactory
	Collection<DynamicTest> checkPrimoPartoTest() throws Exception {

		String msg1 = "I vitelli del primo parto non sono nati nell’allevamento per cui è richiesto l’intervento";

		InformazioniAllevamento infoAllevamento = new InformazioniAllevamento();
		RichiestaAllevamDu allevamento = new RichiestaAllevamDu();
		VitelloDto vitello = new VitelloDto();
		DetenzioneDto detenzione = new DetenzioneDto().setAziendaCodice("TN987BZ");
		CapoLatteDto capo = new CapoLatteDto().setVitelli(Arrays.asList(vitello)).setDetenzioni(Arrays.asList(detenzione));


		return Arrays.asList(
				dynamicTest("Vitello nato in altro allevamento", () -> {

					infoAllevamento.setCodiceAllevamento("TN123AZ");
					allevamento.setDatiAllevamento(objectMapper.writeValueAsString(infoAllevamento));

					vitello.setDataNascita(LocalDate.of(2022, 1, 1));

					detenzione
					.setDtInizioDetenzione(vitello.getDataNascita())
					.setDtFineDetenzione(vitello.getDataNascita());


					Optional<EsitoCalcoloCapoModel> response = checkPrimoParto().apply(capo, allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.DA_SCARTARE, response.get().getEsito());
					assertEquals(msg1, response.get().getMessaggio());

				}),
				dynamicTest("Vitello nato fuori detenzione", () -> {

					infoAllevamento.setCodiceAllevamento("TN123AZ");
					allevamento.setDatiAllevamento(objectMapper.writeValueAsString(infoAllevamento));

					vitello.setDataNascita(LocalDate.of(2022, 1, 1));

					detenzione
					.setDtInizioDetenzione(vitello.getDataNascita().plusDays(1))
					.setDtFineDetenzione(vitello.getDataNascita().plusDays(1));


					Optional<EsitoCalcoloCapoModel> response = checkPrimoParto().apply(capo, allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.DA_SCARTARE, response.get().getEsito());
					assertEquals(msg1, response.get().getMessaggio());
				}),
				dynamicTest("corrispondenza allevamento e detenzione primo parto - caso ok", () -> {

					infoAllevamento.setCodiceAllevamento("TN987BZ");
					allevamento.setDatiAllevamento(objectMapper.writeValueAsString(infoAllevamento));

					vitello.setDataNascita(LocalDate.of(2022, 1, 1));

					detenzione
					.setDtInizioDetenzione(vitello.getDataNascita())
					.setDtFineDetenzione(vitello.getDataNascita());

					Optional<EsitoCalcoloCapoModel> response = checkPrimoParto().apply(capo, allevamento);
					assertTrue(response.isEmpty());
				}),
				// caso mai verificato
				dynamicTest("Assenza primo parto - caso ok", () -> {

					infoAllevamento.setCodiceAllevamento("TN987BZ");
					allevamento.setDatiAllevamento(objectMapper.writeValueAsString(infoAllevamento));
					capo.setVitelli(new ArrayList<>());

					Optional<EsitoCalcoloCapoModel> response = checkPrimoParto().apply(capo, allevamento);
					assertTrue(response.isEmpty());
				})
				);
	}

	// BR6 - Verifica che l’allevamento è di montagna.
	@TestFactory
	@Transactional
	Collection<DynamicTest> checkAllevamentoMontagnaTest() throws Exception {

		Function<String, String> msg = codiceAllevamento -> MessageFormat.format("Il capo non è ammissibile perché l’allevamento {0} per cui è stato richiesto il sostegno non è di montagna.", codiceAllevamento);

		InformazioniAllevamento infoAllevamento = new InformazioniAllevamento();
		RichiestaAllevamDu allevamento = new RichiestaAllevamDu();

		A4gtAllevMontagna allevamentoMontagnaMock = new A4gtAllevMontagna();

		return Arrays.asList(
				dynamicTest("allevamento montagna certificato in provincia", () -> {

					allevamentoMontagnaMock.setCodiceAllevamento("123TN987");
					allevamentoMontagnaMock.setFlagMotagna(Boolean.TRUE);
					allevMontagnaDao.save(allevamentoMontagnaMock);

					infoAllevamento.setCodiceAllevamento("123TN987");
					allevamento.setDatiAllevamento(objectMapper.writeValueAsString(infoAllevamento));

					Optional<EsitoCalcoloCapoModel> response = checkAllevamentoMontagna().apply(null, allevamento);
					assertTrue(response.isEmpty());
				}),
				// allevamento presente, valido, ma fuori provincia
				dynamicTest("allevamento montagna certificato fuori provincia", () -> {

					allevamentoMontagnaMock.setCodiceAllevamento("123NA987");
					allevamentoMontagnaMock.setFlagMotagna(Boolean.TRUE);
					allevMontagnaDao.save(allevamentoMontagnaMock);

					infoAllevamento.setCodiceAllevamento("123NA987");
					allevamento.setDatiAllevamento(objectMapper.writeValueAsString(infoAllevamento));

					Optional<EsitoCalcoloCapoModel> response = checkAllevamentoMontagna().apply(null, allevamento);
					assertTrue(response.isEmpty());
				}),
				dynamicTest("allevamento montagna non certificato", () -> {

					String codiceAllevamento = "123NA999";
					allevamentoMontagnaMock.setCodiceAllevamento(codiceAllevamento);
					allevamentoMontagnaMock.setFlagMotagna(Boolean.FALSE);
					allevMontagnaDao.save(allevamentoMontagnaMock);

					infoAllevamento.setCodiceAllevamento(codiceAllevamento);
					allevamento.setDatiAllevamento(objectMapper.writeValueAsString(infoAllevamento));

					Optional<EsitoCalcoloCapoModel> response = checkAllevamentoMontagna().apply(null, allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, response.get().getEsito());
					assertEquals(msg.apply(codiceAllevamento), response.get().getMessaggio());
				}),
				// allevamento non presente sul db 
				dynamicTest("allevamento montagna non presente", () -> {

					String codiceAllevamento = "321NA123";
					infoAllevamento.setCodiceAllevamento(codiceAllevamento);
					allevamento.setDatiAllevamento(objectMapper.writeValueAsString(infoAllevamento));

					Optional<EsitoCalcoloCapoModel> response = checkAllevamentoMontagna().apply(null, allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, response.get().getEsito());
					assertEquals(msg.apply(codiceAllevamento), response.get().getMessaggio());
				})
				);
	}

	//BR5 Verifica del rispetto dei sei mesi di detenzione in allevamenti in zona montana
	@TestFactory
	Collection<DynamicTest> checkMovimentazione_beforeDataRiferimento() throws Exception {

		String msg1 = "Il capo non è ammissibile perché il periodo di detenzione in allevamenti di montagna calcolato (179 giorni)  sommando i giorni di detenzione nel periodo contenente il primo parto è inferiore ai giorni richiesti (180 giorni).";
		String msg2 = "Il capo è ammesso con sanzioni perché non sono stati rispettati i tempi di registrazione della movimentazione in almeno una delle detenzioni che hanno concorso a raggiungere i 6 mesi.";

		String codiceAllevamento = "123BZ987";

		// save allevamento di montagna 
		A4gtAllevMontagna allevamentoMontagnaMock = new A4gtAllevMontagna();
		allevamentoMontagnaMock.setCodiceAllevamento(codiceAllevamento);
		allevamentoMontagnaMock.setFlagMotagna(Boolean.TRUE);
		allevMontagnaDao.save(allevamentoMontagnaMock);

		// prendo un vitello, e lo lego ad una delle detenzioni create 
		VitelloDto vitello = new VitelloDto();

		// assegno detenzioni al capo 
		CapoLatteDto capo = new CapoLatteDto()
				.setVitelli(Arrays.asList(vitello));

		Integer annoCampagna = 2020;
		RichiestaAllevamDu allevamento = new RichiestaAllevamDu();
		allevamento.setCampagna(annoCampagna);

		return Arrays.asList(
				dynamicTest("somma detenzioni contigue >= 180 - rispetta tempistica", () -> {
					List<DetenzioneDto> detenzioniContigue = getMockDetenzioniContigue(codiceAllevamento, 190, annoCampagna);
					capo.setDetenzioni(detenzioniContigue);
					vitello.setDataNascita(detenzioniContigue.get(2).getDtInizioDetenzione());

					Optional<EsitoCalcoloCapoModel> response = checkMovimentazione().apply(capo,allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, response.get().getEsito());
					assertEquals("",response.get().getMessaggio());
				}),
				dynamicTest("somma detenzioni contigue < 180", () -> {

					List<DetenzioneDto> detenzioniContigue = getMockDetenzioniContigue(codiceAllevamento, 181, annoCampagna);
					capo.setDetenzioni(detenzioniContigue);
					vitello.setDataNascita(detenzioniContigue.get(2).getDtInizioDetenzione());

					Optional<EsitoCalcoloCapoModel> response = checkMovimentazione().apply(capo,allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, response.get().getEsito());
					assertEquals(msg1,response.get().getMessaggio());
				}),
				dynamicTest("somma detenzioni contigue >= 180 - non rispetta tempistiche", () -> {

					List<DetenzioneDto> detenzioniContigue = getMockDetenzioniContigue(codiceAllevamento, 200, annoCampagna);

					detenzioniContigue.add(new DetenzioneDto()
							.setDtInizioDetenzione(detenzioniContigue.get(detenzioniContigue.size()-1).getDtFineDetenzione())
							.setDtFineDetenzione(detenzioniContigue.get(detenzioniContigue.size()-1).getDtFineDetenzione().plusDays(10))
							.setAziendaCodice(codiceAllevamento)
							.setVaccaDtIngresso(detenzioniContigue.get(detenzioniContigue.size()-1).getDtFineDetenzione()) // before
							.setVaccaDtComAutoritaIngresso(detenzioniContigue.get(detenzioniContigue.size()-1).getDtFineDetenzione().plusDays(8))
							.setVaccaDtInserimentoBdnIngresso(detenzioniContigue.get(detenzioniContigue.size()-1).getDtFineDetenzione().plusDays(16))
							);
					capo.setDetenzioni(detenzioniContigue);
					vitello.setDataNascita(detenzioniContigue.get(2).getDtInizioDetenzione());

					Optional<EsitoCalcoloCapoModel> response = checkMovimentazione().apply(capo,allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, response.get().getEsito());
					assertEquals(msg2,response.get().getMessaggio());
				})
				);
	}

	//BR5 Verifica del rispetto dei sei mesi di detenzione in allevamenti in zona montana
	@TestFactory
	Collection<DynamicTest> checkMovimentazione_afterDataRiferimento() throws Exception {

		String msg1 = "Il capo non è ammissibile perché il periodo di detenzione in allevamenti di montagna calcolato (179 giorni)  sommando i giorni di detenzione nel periodo contenente il primo parto è inferiore ai giorni richiesti (180 giorni).";
		String msg2 = "Il capo è ammesso con sanzioni perché non sono stati rispettati i tempi di registrazione della movimentazione in almeno una delle detenzioni che hanno concorso a raggiungere i 6 mesi.";

		String codiceAllevamento = "123BZ987";

		// save allevamento di montagna 
		A4gtAllevMontagna allevamentoMontagnaMock = new A4gtAllevMontagna();
		allevamentoMontagnaMock.setCodiceAllevamento(codiceAllevamento);
		allevamentoMontagnaMock.setFlagMotagna(Boolean.TRUE);
		allevMontagnaDao.save(allevamentoMontagnaMock);

		// prendo un vitello, e lo lego ad una delle detenzioni create 
		VitelloDto vitello = new VitelloDto();

		// assegno detenzioni al capo 
		CapoLatteDto capo = new CapoLatteDto()
				.setVitelli(Arrays.asList(vitello));

		Integer annoCampagna = 2022;
		RichiestaAllevamDu allevamento = new RichiestaAllevamDu();
		allevamento.setCampagna(annoCampagna);

		return Arrays.asList(
				dynamicTest("somma detenzioni contigue >= 180 - rispetta tempistica", () -> {
					List<DetenzioneDto> detenzioniContigue = getMockDetenzioniContigue(codiceAllevamento, 190, annoCampagna);
					capo.setDetenzioni(detenzioniContigue);
					vitello.setDataNascita(detenzioniContigue.get(2).getDtInizioDetenzione());

					Optional<EsitoCalcoloCapoModel> response = checkMovimentazione().apply(capo,allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, response.get().getEsito());
					assertEquals("",response.get().getMessaggio());
				}),
				dynamicTest("somma detenzioni contigue < 180", () -> {

					List<DetenzioneDto> detenzioniContigue = getMockDetenzioniContigue(codiceAllevamento, 181, annoCampagna);
					capo.setDetenzioni(detenzioniContigue);
					vitello.setDataNascita(detenzioniContigue.get(2).getDtInizioDetenzione());

					Optional<EsitoCalcoloCapoModel> response = checkMovimentazione().apply(capo,allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, response.get().getEsito());
					assertEquals(msg1,response.get().getMessaggio());
				}),
				dynamicTest("somma detenzioni contigue >= 180 - non rispetta tempistiche", () -> {

					List<DetenzioneDto> detenzioniContigue = getMockDetenzioniContigue(codiceAllevamento, 200, annoCampagna);

					detenzioniContigue.add(new DetenzioneDto()
							.setDtInizioDetenzione(detenzioniContigue.get(detenzioniContigue.size()-1).getDtFineDetenzione())
							.setDtFineDetenzione(detenzioniContigue.get(detenzioniContigue.size()-1).getDtFineDetenzione().plusDays(10))
							.setAziendaCodice(codiceAllevamento)
							.setVaccaDtIngresso(detenzioniContigue.get(detenzioniContigue.size()-1).getDtFineDetenzione()) // before
							.setVaccaDtComAutoritaIngresso(detenzioniContigue.get(detenzioniContigue.size()-1).getDtFineDetenzione().plusDays(8))
							.setVaccaDtInserimentoBdnIngresso(detenzioniContigue.get(detenzioniContigue.size()-1).getDtFineDetenzione().plusDays(16))
							);
					capo.setDetenzioni(detenzioniContigue);
					vitello.setDataNascita(detenzioniContigue.get(2).getDtInizioDetenzione());

					Optional<EsitoCalcoloCapoModel> response = checkMovimentazione().apply(capo,allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, response.get().getEsito());
					assertEquals(msg2,response.get().getMessaggio());
				})
				);
	}

	private List<DetenzioneDto> getMockDetenzioniContigue(String codiceAllevamento, Integer days, Integer campagna) {
		LocalDate start = LocalDate.of(campagna, 1, 1);
		List<LocalDate> dateDetenzioniContigue = start.datesUntil(start.plusDays(days), Period.ofDays(5)).collect(Collectors.toList());
		List<DetenzioneDto> detenzioniContigue = new ArrayList<>();

		for (int i = 0;  i < dateDetenzioniContigue.size() - 1 ; i++) {

			detenzioniContigue.add(new DetenzioneDto()
					.setDtInizioDetenzione(dateDetenzioniContigue.get(i))
					.setDtFineDetenzione(dateDetenzioniContigue.get(i+1))
					.setAziendaCodice(codiceAllevamento)
					.setVaccaDtIngresso(dateDetenzioniContigue.get(i)) // before
					.setVaccaDtComAutoritaIngresso(dateDetenzioniContigue.get(i)) // ok
					.setVaccaDtInserimentoBdnIngresso(dateDetenzioniContigue.get(i)) // ok
					);	
		}
		return detenzioniContigue;
	}

}