package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoLatteDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.VitelloDto;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ControlloCapoVaccaLatteTests extends ControlloCapoVaccaLatte {

	private static final LocalDate DATA_RIFERIMENTO = LocalDate.of(2021, 4, 20);

	@SpyBean
	private ControlliCapiLatteFactory controlliCapiLatteFactory;

	//IDU-ACZ-00 Ammissibilità interventi 314, 317 e 319
	@TestFactory
	Collection<DynamicTest> controlloCapoVaccaLatte_checkInterventoDaScartare() throws Exception {

		RichiestaAllevamDu allevamento = new RichiestaAllevamDu();
		String msg1 = "Capo appartenente a intervento non ammissibile (314)";
		return Arrays.asList(
				dynamicTest("Intervento 314 - da scartare", () -> {
					allevamento.setCodiceIntervento("314");
					Optional<EsitoCalcoloCapoModel> response = checkInterventoDaScartare().apply(null, allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, response.get().getEsito());
					assertEquals(msg1 ,response.get().getMessaggio());

				}), 	dynamicTest("Intervento 317 - da considerare", () -> {
					allevamento.setCodiceIntervento("317");
					Optional<EsitoCalcoloCapoModel> response = checkInterventoDaScartare().apply(null, allevamento);
					assertTrue(response.isEmpty());
				}));
	}

	// la vacca deve partorire tra i 20 mesi ed i 18 anni di età - finestra temporale di fertilità
	@TestFactory
	Collection<DynamicTest> controlloCapoVaccaLatte_checkLimiteEta() throws Exception {

		String msg1 = "Il capo non è ammesso in quanto al momento del parto (24/08/2023) la vacca nata il 01/01/2022, aveva 1 anni di età.";
		String msg2 = "Il capo non è ammesso in quanto al momento del parto (28/12/2039) la vacca nata il 01/01/2022, aveva 18 anni di età.";

		CapoLatteDto CapoLatteDto = new CapoLatteDto().setDataNascita(LocalDate.of(2022, 1, 1));

		// < 20 mesi	-> 20 * 30 gg -> 600
		VitelloDto vitello20Mesi = new VitelloDto().setDataNascita(LocalDate.of(2022, 1, 1).plusDays(600));

		// > 18 anni	-> 18 * 365 gg -> 6570
		VitelloDto vitello18Anni = new VitelloDto().setDataNascita(LocalDate.of(2022, 1, 1).plusDays(6570));

		// caso ok
		VitelloDto vitelloOk = new VitelloDto().setDataNascita(LocalDate.of(2022, 1, 1).plusDays(2985));

		return Arrays.asList(
				dynamicTest("eta < 20 mesi", () -> {
					Optional<EsitoCalcoloCapoModel> response = checkLimiteEta().apply(CapoLatteDto.setVitelli(Arrays.asList(vitello20Mesi)), null);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, response.get().getEsito());
					assertEquals(msg1 ,response.get().getMessaggio());

				}),		dynamicTest("eta > 18 anni", () -> {

					Optional<EsitoCalcoloCapoModel> response = checkLimiteEta().apply(CapoLatteDto.setVitelli(Arrays.asList(vitello18Anni)), null);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, response.get().getEsito());
					assertEquals(msg2 ,response.get().getMessaggio());

				}), 	dynamicTest("caso ok", () -> {

					Optional<EsitoCalcoloCapoModel> response = checkLimiteEta().apply(CapoLatteDto.setVitelli(Arrays.asList(vitelloOk)), null);
					assertTrue(response.isEmpty());
				}));	
	}

	//BR4 - obblighi di identificazione e registrazione
	@TestFactory
	Collection<DynamicTest> controlloCapoVaccaLatte_checkIdentificazioneRegistrazione_partoGemellare() throws Exception {

		CapoLatteDto CapoLatteDto = new CapoLatteDto().setDataNascita(LocalDate.of(2022, 1, 1));
		LocalDate dtNascitaVitello = LocalDate.of(2022, 3, 7);
		VitelloDto vitello1 = new VitelloDto().setDataNascita(dtNascitaVitello);
		VitelloDto vitello2 = new VitelloDto().setDataNascita(dtNascitaVitello);

		return Arrays.asList(
				// considera entrambi i vitelli del primo parto
				dynamicTest("primo parto gemellare", () -> {

					vitello1
					.setTipoOrigine("W")
					.setDtApplicazioneMarchio(LocalDate.of(2022, 3, 7))
					.setDtInserimentoBdnNascita(LocalDate.of(2022, 3, 7))
					.setFlagProrogaMarcatura("S");

					vitello2
					.setTipoOrigine("W")
					.setDtApplicazioneMarchio(LocalDate.of(2022, 3, 7))
					.setDtInserimentoBdnNascita(LocalDate.of(2022, 3, 7))
					.setFlagProrogaMarcatura("S");

					CapoLatteDto.setVitelli(Arrays.asList(vitello1,vitello2));
					checkIdentificazioneRegistrazione().apply(CapoLatteDto,null);
					Mockito.verify(controlliCapiLatteFactory, times(2)).from(dtNascitaVitello);
				}),
				// considera soltanto il vitello del primo parto
				dynamicTest("primo parto non gemellare", () -> {

					vitello1
					.setTipoOrigine("ALTRO")
					.setDtApplicazioneMarchio(LocalDate.of(2022, 3, 7))
					.setDtInserimentoBdnNascita(LocalDate.of(2022, 3, 7))
					.setFlagProrogaMarcatura("S");

					vitello2
					.setTipoOrigine("ALTRO")
					.setDtApplicazioneMarchio(LocalDate.of(2022, 3, 7))
					.setDtInserimentoBdnNascita(LocalDate.of(2022, 3, 7))
					.setFlagProrogaMarcatura("S");

					CapoLatteDto.setVitelli(Arrays.asList(vitello1));
					checkIdentificazioneRegistrazione().apply(CapoLatteDto,null);
					Mockito.verify(controlliCapiLatteFactory, times(2+1)).from(dtNascitaVitello); // 2 del test precedente + 1 attuale
				})
				);
	}

	//BR4 - obblighi di identificazione e registrazione - vitelli nati prima della data riferimento 20/04/2021 (inclusa)
	@TestFactory
	Collection<DynamicTest> controlloCapoVaccaLatte_checkIdentificazioneRegistrazione_beforeDataRiferimento_casiKO() throws Exception {

		// vitello 1 ok e 2 ko
		String msg1 = "Il capo non è ammissibile perché non sono stati rispettati i tempi di identificazione e registrazione del vitello in quanto è stato superato il limite 187 tra la data di registrazione della nascita 25/10/2021 e la data di nascita 20/04/2021 che è pari a 188 ( delegata la registrazione nascita; allevamento  autorizzato a proroga marcatura)";
		String msg2 = "Il capo non è ammissibile perché non sono stati rispettati i tempi di identificazione e registrazione del vitello in quanto è stato superato il limite 180 tra la data di registrazione della nascita 18/10/2021 e la data di nascita 20/04/2021 che è pari a 181 (non delegata la registrazione nascita; allevamento  autorizzato a proroga marcatura)"; 
		String msg3 = "Il capo non è ammissibile perché non sono stati rispettati i tempi di identificazione e registrazione del vitello in quanto è stato superato il limite 34 tra la data di registrazione della nascita 25/05/2021 e la data di nascita 20/04/2021 che è pari a 35 ( delegata la registrazione nascita; allevamento non autorizzato a proroga marcatura)"; 
		String msg4 = "Il capo non è ammissibile perché non sono stati rispettati i tempi di identificazione e registrazione del vitello in quanto è stato superato il limite 27 tra la data di registrazione della nascita 18/05/2021 e la data di nascita 20/04/2021 che è pari a 28 (non delegata la registrazione nascita; allevamento non autorizzato a proroga marcatura)"; 

		CapoLatteDto CapoLatteDto = new CapoLatteDto().setDataNascita(LocalDate.of(2020, 1, 1));
		LocalDate dtNascitaVitello = DATA_RIFERIMENTO;
		VitelloDto vitello1 = new VitelloDto().setDataNascita(dtNascitaVitello).setTipoOrigine("W");
		VitelloDto vitello2 = new VitelloDto().setDataNascita(dtNascitaVitello).setTipoOrigine("W");
		CapoLatteDto.setVitelli(Arrays.asList(vitello1,vitello2));

		return Arrays.asList(
				// caso flagDelegatoNascitaVitello S AND flagProrogaMarcatura S
				dynamicTest("delega nascita e proroga marcatura - 187 gg", () -> {

					vitello1
					.setFlagDelegatoNascitaVitello("S")
					.setFlagProrogaMarcatura("S")
					.setDtInserimentoBdnNascita(dtNascitaVitello.plusDays(187L)); // ha fatto in tempo - controllo ok

					vitello2
					.setFlagDelegatoNascitaVitello("S")
					.setFlagProrogaMarcatura("S")
					.setDtInserimentoBdnNascita(dtNascitaVitello.plusDays(188L)); // non ha fatto in tempo - controllo ko

					Optional<EsitoCalcoloCapoModel> response = checkIdentificazioneRegistrazione().apply(CapoLatteDto,null);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, response.get().getEsito());
					assertEquals(msg1 ,response.get().getMessaggio());
				}),
				// caso flagDelegatoNascitaVitello N AND flagProrogaMarcatura S
				dynamicTest("non delega nascita e proroga marcatura - 180 gg", () -> {

					vitello1
					.setFlagDelegatoNascitaVitello("N")
					.setFlagProrogaMarcatura("S")
					.setDtInserimentoBdnNascita(dtNascitaVitello.plusDays(180L)); // ha fatto in tempo - controllo ok

					vitello2
					.setFlagDelegatoNascitaVitello("N")
					.setFlagProrogaMarcatura("S")
					.setDtInserimentoBdnNascita(dtNascitaVitello.plusDays(181L)); // non ha fatto in tempo - controllo ko

					Optional<EsitoCalcoloCapoModel> response = checkIdentificazioneRegistrazione().apply(CapoLatteDto,null);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, response.get().getEsito());
					assertEquals(msg2,response.get().getMessaggio());
				}),
				// caso flagDelegatoNascitaVitello S AND flagProrogaMarcatura N
				dynamicTest("delega nascita e non proroga marcatura - 34 gg", () -> {

					vitello1
					.setFlagDelegatoNascitaVitello("S")
					.setFlagProrogaMarcatura("N")
					.setDtInserimentoBdnNascita(dtNascitaVitello.plusDays(34L)); // ha fatto in tempo - controllo ok

					vitello2
					.setFlagDelegatoNascitaVitello("S")
					.setFlagProrogaMarcatura("N")
					.setDtInserimentoBdnNascita(dtNascitaVitello.plusDays(35L)); // non ha fatto in tempo - controllo ko

					Optional<EsitoCalcoloCapoModel> response = checkIdentificazioneRegistrazione().apply(CapoLatteDto,null);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, response.get().getEsito());
					assertEquals(msg3,response.get().getMessaggio());
				}),
				// caso flagDelegatoNascitaVitello N AND flagProrogaMarcatura N
				dynamicTest("non delega nascita e non proroga marcatura - 27 gg", () -> {

					vitello1
					.setFlagDelegatoNascitaVitello("N")
					.setFlagProrogaMarcatura("N")
					.setDtInserimentoBdnNascita(dtNascitaVitello.plusDays(27L)); // ha fatto in tempo - controllo ok

					vitello2
					.setFlagDelegatoNascitaVitello("N")
					.setFlagProrogaMarcatura("N")
					.setDtInserimentoBdnNascita(dtNascitaVitello.plusDays(28L)); // non ha fatto in tempo - controllo ko

					Optional<EsitoCalcoloCapoModel> response = checkIdentificazioneRegistrazione().apply(CapoLatteDto,null);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, response.get().getEsito());
					assertEquals(msg4,response.get().getMessaggio());
				})
				);
	}

	//BR4 - obblighi di identificazione e registrazione - vitelli nati prima della data riferimento 20/04/2021 (inclusa)
	@Test
	void controlloCapoVaccaLatte_checkIdentificazioneRegistrazione_beforeDataRiferimento_casoOK() throws Exception {

		// vitello 1 e 2 ok
		CapoLatteDto CapoLatteDto = new CapoLatteDto().setDataNascita(LocalDate.of(2020, 1, 1));
		LocalDate dtNascitaVitello = DATA_RIFERIMENTO;
		VitelloDto vitello1 = new VitelloDto().setDataNascita(dtNascitaVitello).setTipoOrigine("W");
		VitelloDto vitello2 = new VitelloDto().setDataNascita(dtNascitaVitello).setTipoOrigine("W");
		CapoLatteDto.setVitelli(Arrays.asList(vitello1,vitello2));

		vitello1
		.setFlagDelegatoNascitaVitello("N")
		.setFlagProrogaMarcatura("N")
		.setDtInserimentoBdnNascita(dtNascitaVitello.plusDays(27L));

		vitello2
		.setFlagDelegatoNascitaVitello("N")
		.setFlagProrogaMarcatura("N")
		.setDtInserimentoBdnNascita(dtNascitaVitello);

		Optional<EsitoCalcoloCapoModel> response = checkIdentificazioneRegistrazione().apply(CapoLatteDto,null);
		assertTrue(response.isEmpty());
	}


	//BR4 - obblighi di identificazione e registrazione - vitelli nati dopo la data riferimento 20/04/2021 (esclusa)
	//	SE FLAG_PROROGA_MARCATURA != S THEN 
	//
	//			Vitello_Dt_Appl_Marchio - Dt_Nascita_Vitello <= 20
	//			AND
	//			Vitello_Dt_Inserimento_Bdn_Nascita - Vitello_Dt_Appl_Marchio <= 7
	//			
	//		ELSE
	//
	//			Vitello_Dt_Appl_Marchio - Dt_Nascita_Vitello <= 180
	@TestFactory
	Collection<DynamicTest> controlloCapoVaccaLatte_checkIdentificazioneRegistrazione_afterDataRiferimento() throws Exception {

		String msg1 = "Il capo non è ammissibile perché non sono stati rispettati i tempi di identificazione e registrazione del vitello: (Vitello_Dt_Appl_Marchio - Dt_Nascita_Vitello) <= 181 (180).";
		String msg2 = "Il capo non è ammissibile perché non sono stati rispettati i tempi di identificazione e registrazione del vitello: (Vitello_Dt_Appl_Marchio - Dt_Nascita_Vitello) <= 21 (20) E (Vitello_Dt_Inserimento_Bdn_Nascita - Vitello_Dt_Appl_Marchio) <= 7 (7).";
		String msg3 = "Il capo non è ammissibile perché non sono stati rispettati i tempi di identificazione e registrazione del vitello: (Vitello_Dt_Appl_Marchio - Dt_Nascita_Vitello) <= 20 (20) E (Vitello_Dt_Inserimento_Bdn_Nascita - Vitello_Dt_Appl_Marchio) <= 8 (7).";

		CapoLatteDto CapoLatteDto = new CapoLatteDto().setDataNascita(LocalDate.of(2022, 1, 1));
		LocalDate dtNascitaVitello = DATA_RIFERIMENTO.plusDays(1);
		VitelloDto vitello = new VitelloDto().setDataNascita(dtNascitaVitello).setTipoOrigine("N");
		CapoLatteDto.setVitelli(Arrays.asList(vitello));

		return Arrays.asList(
				dynamicTest("proroga marcatura e non rispetto tempistiche marchio", () -> {

					vitello
					.setFlagProrogaMarcatura("S")
					.setDtApplicazioneMarchio(dtNascitaVitello.plusDays(181)) // fuori dalla tempistica
					.setDtInserimentoBdnNascita(dtNascitaVitello.plusDays(100)); // valore qualsiasi
					Optional<EsitoCalcoloCapoModel> response = checkIdentificazioneRegistrazione().apply(CapoLatteDto,null);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, response.get().getEsito());
					assertEquals(msg1 ,response.get().getMessaggio());
				}),
				dynamicTest("senza proroga marcatura e non rispetto tempistiche marchio", () -> {

					vitello
					.setFlagProrogaMarcatura("ALTRO")
					.setDtApplicazioneMarchio(dtNascitaVitello.plusDays(21)) // non rispetta tempistiche Marchio
					.setDtInserimentoBdnNascita(vitello.getDtApplicazioneMarchio().plusDays(7)); //rispetta tempistiche bdn

					Optional<EsitoCalcoloCapoModel> response = checkIdentificazioneRegistrazione().apply(CapoLatteDto,null);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, response.get().getEsito());
					assertEquals(msg2 ,response.get().getMessaggio());
				}),
				dynamicTest("senza proroga marcatura e non rispetto tempistiche bdn", () -> {

					vitello
					.setFlagProrogaMarcatura("ALTRO")
					.setDtApplicazioneMarchio(dtNascitaVitello.plusDays(20)) // rispetta tempistiche Marchio
					.setDtInserimentoBdnNascita(vitello.getDtApplicazioneMarchio().plusDays(8)); //non rispetta tempistiche bdn

					Optional<EsitoCalcoloCapoModel> response = checkIdentificazioneRegistrazione().apply(CapoLatteDto,null);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, response.get().getEsito());
					assertEquals(msg3 ,response.get().getMessaggio());
				}),
				dynamicTest("proroga marcatura caso ok", () -> {

					vitello
					.setFlagProrogaMarcatura("S")
					.setDtApplicazioneMarchio(dtNascitaVitello.plusDays(20)) // rispetta tempistiche Marchio
					.setDtInserimentoBdnNascita(vitello.getDtApplicazioneMarchio().plusDays(7)); // rispetta tempistiche bdn

					Optional<EsitoCalcoloCapoModel> response = checkIdentificazioneRegistrazione().apply(CapoLatteDto,null);
					assertTrue(response.isEmpty());
				}),
				dynamicTest("senza proroga marcatura caso ok", () -> {

					vitello
					.setFlagProrogaMarcatura("S")
					.setDtApplicazioneMarchio(dtNascitaVitello.plusDays(180)); // rispetta tempistiche Marchio

					Optional<EsitoCalcoloCapoModel> response = checkIdentificazioneRegistrazione().apply(CapoLatteDto,null);
					assertTrue(response.isEmpty());
				})
				);
	}


	//BR5 - Verifica del rispetto della tempistica di registrazione delle movimentazioni - Vacche entrate in allevamento prima del 20/04/2021 (inclusa)
	// C1 = (Vacca_Dt_Inserimento_Bdn_Ingresso < 01/01/Anno campagna)
	// C2 = (Vacca_Dt_Com_Autorita_Ingresso – Vacca_Dt_Ingresso) <= 7
	// C3 = (Vacca_Dt_Inserimento_Bdn_Ingresso – Vacca_Dt_Com_Autorita_Ingresso) <= 7
	// FORMULA:  C1 or (C2 and C3)
	//	casi ok
	//
	//	T
	//	F or (T and T)
	//
	//	casi ko
	//
	//	F or (F and T)
	//	F or (T and F)
	//	F or (F and F)
	@TestFactory
	Collection<DynamicTest> controlloCapoVaccaLatte_checkMovimentazione_beforeDataRiferimento_casiKO() throws Exception {

		String msg1 = "il capo è ammesso con sanzioni perché non sono stati rispettati i tempi di registrazione della movimentazione in quanto non sono stati rispettati tutti i tempi di registrazione (entrambi devono essere inferiori agli 8 giorni): Vacca_Dt_Com_Autorita_Ingresso=25/12/2020 – Vacca_Dt_Ingresso=17/12/2020 è pari a 8; Vacca_Dt_Inserimento_Bdn_Ingresso=01/01/2021 – Vacca_Dt_Com_Autorita_Ingresso=25/12/2020 è pari a 7";
		String msg2 = "il capo è ammesso con sanzioni perché non sono stati rispettati i tempi di registrazione della movimentazione in quanto non sono stati rispettati tutti i tempi di registrazione (entrambi devono essere inferiori agli 8 giorni): Vacca_Dt_Com_Autorita_Ingresso=24/12/2020 – Vacca_Dt_Ingresso=17/12/2020 è pari a 7; Vacca_Dt_Inserimento_Bdn_Ingresso=01/01/2021 – Vacca_Dt_Com_Autorita_Ingresso=24/12/2020 è pari a 8";
		Integer annoCampagna = 2021;
		RichiestaAllevamDu allevamento = new RichiestaAllevamDu();
		allevamento.setCampagna(annoCampagna);
		CapoLatteDto CapoLatteDto = new CapoLatteDto().setDataNascita(LocalDate.of(2021, 1, 1));

		// detenzione 1 ok, detenzione 2 ko
		DetenzioneDto detenzione1 = new DetenzioneDto().setVaccaDtIngresso(LocalDate.of(annoCampagna, 1, 1)); // before
		DetenzioneDto detenzione2 = new DetenzioneDto();
		CapoLatteDto.setDetenzioni(Arrays.asList(detenzione1, detenzione2));

		return Arrays.asList(
				dynamicTest("F or (F and T)", () -> {
					detenzione1
					.setVaccaDtComAutoritaIngresso(detenzione1.getVaccaDtIngresso().plusDays(7))	// rispetta C2
					.setVaccaDtInserimentoBdnIngresso(detenzione1.getVaccaDtComAutoritaIngresso().plusDays(7)); // rispetta C1 e C3

					detenzione2
					.setVaccaDtIngresso(LocalDate.of(annoCampagna, 1, 1).minusDays(8+7)) // non rispetta C1
					.setVaccaDtComAutoritaIngresso(detenzione2.getVaccaDtIngresso().plusDays(8))	// non rispetta rispetta C2
					.setVaccaDtInserimentoBdnIngresso(detenzione2.getVaccaDtComAutoritaIngresso().plusDays(7)); // rispetta C3

					Optional<EsitoCalcoloCapoModel> response = checkMovimentazione().apply(CapoLatteDto,allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, response.get().getEsito());
					assertEquals(msg1, response.get().getMessaggio());
				}),
				dynamicTest("F or (T and F)", () -> {
					detenzione1
					.setVaccaDtComAutoritaIngresso(detenzione1.getVaccaDtIngresso().plusDays(7))	// rispetta C2
					.setVaccaDtInserimentoBdnIngresso(detenzione1.getVaccaDtComAutoritaIngresso().plusDays(7)); // rispetta C1 e C3

					detenzione2
					.setVaccaDtIngresso(LocalDate.of(annoCampagna, 1, 1).minusDays(7+8)) // non rispetta C1
					.setVaccaDtComAutoritaIngresso(detenzione2.getVaccaDtIngresso().plusDays(7))	// rispetta C2
					.setVaccaDtInserimentoBdnIngresso(detenzione2.getVaccaDtComAutoritaIngresso().plusDays(8)); // non rispetta C3

					Optional<EsitoCalcoloCapoModel> response = checkMovimentazione().apply(CapoLatteDto,allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, response.get().getEsito());
					assertEquals(msg2, response.get().getMessaggio());
				})
				);
	}

	@TestFactory
	Collection<DynamicTest> controlloCapoVaccaLatte_checkMovimentazione_beforeDataRiferimento_casiOK() throws Exception {

		Integer annoCampagna = 2021;
		RichiestaAllevamDu allevamento = new RichiestaAllevamDu();
		allevamento.setCampagna(annoCampagna);
		CapoLatteDto CapoLatteDto = new CapoLatteDto().setDataNascita(LocalDate.of(2021, 1, 1));

		// detenzione
		DetenzioneDto detenzione = new DetenzioneDto(); // before
		CapoLatteDto.setDetenzioni(Arrays.asList(detenzione));

		return Arrays.asList(
				dynamicTest("T", () -> {
					detenzione
					.setVaccaDtIngresso(LocalDate.of(annoCampagna, 1, 1).minusDays(30)) // rispetta C1
					.setVaccaDtComAutoritaIngresso(detenzione.getVaccaDtIngresso().plusDays(10))	// non rispetta C2
					.setVaccaDtInserimentoBdnIngresso(detenzione.getVaccaDtComAutoritaIngresso().plusDays(10)); // non rispetta C3

					Optional<EsitoCalcoloCapoModel> response = checkMovimentazione().apply(CapoLatteDto,allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, response.get().getEsito());
					assertNull(response.get().getMessaggio());
				}),
				dynamicTest("F or (T and T)", () -> {
					detenzione
					.setVaccaDtIngresso(LocalDate.of(annoCampagna, 1, 1).minusDays(14)) // non rispetta C1
					.setVaccaDtComAutoritaIngresso(detenzione.getVaccaDtIngresso().plusDays(7))	// rispetta C2
					.setVaccaDtInserimentoBdnIngresso(detenzione.getVaccaDtComAutoritaIngresso().plusDays(7)); // rispetta C3

					Optional<EsitoCalcoloCapoModel> response = checkMovimentazione().apply(CapoLatteDto,allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, response.get().getEsito());
					assertNull(response.get().getMessaggio());
				})
				);
	}


	// BR5
	//	Verifica del rispetto della tempistica di registrazione delle movimentazioni - Vacche entrate in allevamento dopo il 20/04/2021 (escluso)
	// C1 = (Vacca_Dt_Inserimento_Bdn_Ingresso < 01/01/<Anno campagna>)
	// C2 = (Vacca_Dt_Inserimento_Bdn_Ingresso – Vacca_Dt_Ingresso) <= 7
	// FORMULA:  C1 or C2
	//	casi ok
	//
	//	T or F
	//	F or T
	//
	//	casi ko
	//
	// 	F or F
	@Test
	void controlloCapoVaccaLatte_checkMovimentazione_afterDataRiferimento_casiKO() throws Exception {

		String msg1 = "il capo è ammesso con sanzioni perché non sono stati rispettati i tempi di registrazione della movimentazione: (Vacca_Dt_Inserimento_Bdn_Ingresso < 09/01/2022 (01/01/2022)) O (Vacca_Dt_Inserimento_Bdn_Ingresso – Vacca_Dt_Ingresso) <= 8 (7).";

		Integer annoCampagna = 2022;
		RichiestaAllevamDu allevamento = new RichiestaAllevamDu();
		allevamento.setCampagna(annoCampagna);
		CapoLatteDto CapoLatteDto = new CapoLatteDto().setDataNascita(LocalDate.of(2021, 1, 1));

		// detenzione 1 ok
		DetenzioneDto detenzione1 = new DetenzioneDto();
		detenzione1
		.setVaccaDtIngresso(DATA_RIFERIMENTO.plusDays(1)) // after - verifica C1
		.setVaccaDtInserimentoBdnIngresso(detenzione1.getVaccaDtIngresso().plusDays(7)); // verifica C2

		// detenzione 2 ko
		DetenzioneDto detenzione2 = new DetenzioneDto();
		detenzione2
		.setVaccaDtIngresso(LocalDate.of(2022, 1, 1)) // after - non verifica C1
		.setVaccaDtInserimentoBdnIngresso(detenzione2.getVaccaDtIngresso().plusDays(8)); // non verifica C2

		CapoLatteDto.setDetenzioni(Arrays.asList(detenzione1,detenzione2));

		Optional<EsitoCalcoloCapoModel> response = checkMovimentazione().apply(CapoLatteDto,allevamento);
		assertTrue(response.isPresent());
		assertEquals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, response.get().getEsito());
		assertEquals(msg1, response.get().getMessaggio());
	}


	@TestFactory
	Collection<DynamicTest> controlloCapoVaccaLatte_checkMovimentazione_afterDataRiferimento_casiOK() throws Exception {

		Integer annoCampagna = 2022;
		RichiestaAllevamDu allevamento = new RichiestaAllevamDu();
		allevamento.setCampagna(annoCampagna);
		CapoLatteDto CapoLatteDto = new CapoLatteDto().setDataNascita(LocalDate.of(2021, 1, 1));

		// detenzione
		DetenzioneDto detenzione = new DetenzioneDto(); // before
		CapoLatteDto.setDetenzioni(Arrays.asList(detenzione));

		return Arrays.asList(
				dynamicTest("T or F", () -> {
					detenzione
					.setVaccaDtIngresso(DATA_RIFERIMENTO.plusDays(1)) //after - rispetta C1
					.setVaccaDtInserimentoBdnIngresso(detenzione.getVaccaDtIngresso().plusDays(8)); // non rispetta C2

					Optional<EsitoCalcoloCapoModel> response = checkMovimentazione().apply(CapoLatteDto,allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, response.get().getEsito());
					assertNull(response.get().getMessaggio());
				}),
				dynamicTest("F or T", () -> {
					detenzione
					.setVaccaDtIngresso(LocalDate.of(2022, 1, 1)) //after - non rispetta C1
					.setVaccaDtInserimentoBdnIngresso(LocalDate.of(2022, 1, 1)); // rispetta C2

					Optional<EsitoCalcoloCapoModel> response = checkMovimentazione().apply(CapoLatteDto,allevamento);
					assertTrue(response.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, response.get().getEsito());
					assertNull(response.get().getMessaggio());
				})
				);
	}

}
