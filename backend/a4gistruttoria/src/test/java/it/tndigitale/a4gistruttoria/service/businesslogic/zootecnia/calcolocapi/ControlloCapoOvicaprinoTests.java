package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoOvicaprinoDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneDto;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ControlloCapoOvicaprinoTests extends ControlloCapoOvicaprino {

	@SpyBean
	private ControlliCapoOvicaprinoFactory controlliCapoOvicaprinoFactory;

	
	// caso ok: rispetta tempistiche - nato in stalla
	// caso ok: rispetta tempistiche - non nato in stalla
	// caso sanzione: non rispetta tempistica applicazione marchio - nato in stalla (Dt_Appl_Marchio - Dt_Nascita) <= 180
	// caso sanzione: non rispetta tempistica registrazione marchio - nato in stalla (Dt_inserimento_BDN_nascita - Dt_Appl_Marchio) <= 7 
	// caso sanzione: non rispetta tempistica registrazione detenzione - non nato in stalla (Dt_inserimento_bdn_ingresso – Dt_Ingresso) <= 7 
	@TestFactory
	Collection<DynamicTest> controlloCapoOvicaprino_checkIntervento320_afterDataRiferimento() throws Exception {

		String msg1 = "Il capo è ammesso con sanzione perché: (Dt_Appl_Marchio - Dt_Nascita) <= 179 (180) E (Dt_inserimento_BDN_nascita - Dt_Appl_Marchio) <= 8 (7) E ((Dt_Nascita != Dt_Ingresso) O (Dt_inserimento_bdn_ingresso – Dt_Ingresso) <= 0 (7).";
		String msg2 = "Il capo è ammesso con sanzione perché: (Dt_Appl_Marchio - Dt_Nascita) <= 1 (180) E (Dt_inserimento_BDN_nascita - Dt_Appl_Marchio) <= 0 (7) E ((Dt_Nascita != Dt_Ingresso) O (Dt_inserimento_bdn_ingresso – Dt_Ingresso) <= 8 (7).";
		// set allevamento
		RichiestaAllevamDu allevamento = new RichiestaAllevamDu();
		allevamento.setCodiceIntervento("320");

		return Arrays.asList(
				dynamicTest("caso ok: rispetta tempistiche - nato in stalla", () -> {
					LocalDate primoGennaio = LocalDate.of(2022, 1, 1);

					// detenzione della macellazione
					DetenzioneDto detenzione = new DetenzioneDto()
							.setDtInizioDetenzione(primoGennaio)
							.setDtFineDetenzione(primoGennaio.plusDays(1))
							.setVaccaDtIngresso(primoGennaio)  // after data riferimento
							.setVaccaDtInserimentoBdnIngresso(primoGennaio); // qualsiasi

					CapoOvicaprinoDto capo = new CapoOvicaprinoDto();
					capo.setDetenzioni(Arrays.asList(detenzione));
					capo.setDataNascita(primoGennaio);	// nato in stalla
					capo.setDataApplicazioneMarchio(primoGennaio.plusDays(180)); // rispetta tempisticaApplicazioneMarchio
					capo.setDataInserimentoBdnNascita(primoGennaio.plusDays(180).plusDays(7)); // tempisticaRegistrazioneNascita

					Optional<EsitoCalcoloCapoModel> esito = checkIntervento320.apply(capo, allevamento);
					assertTrue(esito.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, esito.get().getEsito());
					assertNull(esito.get().getMessaggio());
				}),
				dynamicTest("caso ok: rispetta tempistiche - non nato in stalla", () -> {
					LocalDate primoGennaio = LocalDate.of(2022, 1, 1);

					// detenzione della macellazione
					DetenzioneDto detenzione = new DetenzioneDto()
							.setDtInizioDetenzione(primoGennaio)
							.setDtFineDetenzione(primoGennaio.plusDays(1))
							.setVaccaDtIngresso(primoGennaio)  // after data riferimento
							.setVaccaDtInserimentoBdnIngresso(primoGennaio.plusDays(7)); // rispetta tempisticaDetenzione

					CapoOvicaprinoDto capo = new CapoOvicaprinoDto();
					capo.setDetenzioni(Arrays.asList(detenzione));
					capo.setDataNascita(primoGennaio.minusDays(1));	// non nato in stalla
					capo.setDataApplicazioneMarchio(primoGennaio); // qualsiasi
					capo.setDataInserimentoBdnNascita(primoGennaio); // qualsiasi

					Optional<EsitoCalcoloCapoModel> esito = checkIntervento320.apply(capo, allevamento);
					assertTrue(esito.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, esito.get().getEsito());
					assertNull(esito.get().getMessaggio());
				}),
				dynamicTest("caso sanzioni: nato in stalla - NON tempisticaRegistrazioneNascita", () -> {
					LocalDate primoGennaio = LocalDate.of(2022, 1, 1);

					// detenzione della macellazione
					DetenzioneDto detenzione = new DetenzioneDto()
							.setDtInizioDetenzione(primoGennaio)
							.setDtFineDetenzione(primoGennaio.plusDays(1))
							.setVaccaDtIngresso(primoGennaio)  // after data riferimento
							.setVaccaDtInserimentoBdnIngresso(primoGennaio); // qualsiasi

					CapoOvicaprinoDto capo = new CapoOvicaprinoDto();
					capo.setDetenzioni(Arrays.asList(detenzione));
					capo.setDataNascita(primoGennaio);	// nato in stalla
					capo.setDataApplicazioneMarchio(primoGennaio.plusDays(180)); // rispetta tempisticaApplicazioneMarchio
					capo.setDataInserimentoBdnNascita(primoGennaio.plusDays(180).plusDays(8)); // NON tempisticaRegistrazioneNascita

					Optional<EsitoCalcoloCapoModel> esito = checkIntervento320.apply(capo, allevamento);
					assertTrue(esito.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, esito.get().getEsito());
					assertEquals(msg1, esito.get().getMessaggio());
				}),
				dynamicTest("caso sanzioni: non nato in stalla - NON rispetta tempisticaDetenzione", () -> {
					LocalDate primoGennaio = LocalDate.of(2022, 1, 1);

					// detenzione della macellazione
					DetenzioneDto detenzione = new DetenzioneDto()
							.setDtInizioDetenzione(primoGennaio)
							.setDtFineDetenzione(primoGennaio.plusDays(1))
							.setVaccaDtIngresso(primoGennaio)  // after data riferimento
							.setVaccaDtInserimentoBdnIngresso(primoGennaio.plusDays(8)); // NON rispetta tempisticaDetenzione

					CapoOvicaprinoDto capo = new CapoOvicaprinoDto();
					capo.setDetenzioni(Arrays.asList(detenzione));
					capo.setDataNascita(primoGennaio.minusDays(1));	// non nato in stalla
					capo.setDataApplicazioneMarchio(primoGennaio); // qualsiasi
					capo.setDataInserimentoBdnNascita(primoGennaio); // qualsiasi

					Optional<EsitoCalcoloCapoModel> esito = checkIntervento320.apply(capo, allevamento);
					assertTrue(esito.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, esito.get().getEsito());
					assertEquals(msg2, esito.get().getMessaggio());
				}),
				dynamicTest("caso sanzione: non rispetta tempistica registrazione detenzione - non nato in stalla (Dt_inserimento_bdn_ingresso – Dt_Ingresso) > 7 ", () -> {
					LocalDate primoGennaio = LocalDate.of(2022, 1, 1);

					// detenzione della macellazione
					DetenzioneDto detenzione = new DetenzioneDto()
							.setDtInizioDetenzione(primoGennaio)
							.setDtFineDetenzione(primoGennaio.plusDays(1))
							.setVaccaDtIngresso(primoGennaio)  // after data riferimento
							.setVaccaDtInserimentoBdnIngresso(primoGennaio.plusDays(8)); // non rispetta tempisticaDetenzione

					CapoOvicaprinoDto capo = new CapoOvicaprinoDto();
					capo.setDetenzioni(Arrays.asList(detenzione));
					capo.setDataNascita(primoGennaio.minusDays(1));	// non nato in stalla
					capo.setDataApplicazioneMarchio(primoGennaio); // qualsiasi
					capo.setDataInserimentoBdnNascita(primoGennaio); // qualsiasi

					Optional<EsitoCalcoloCapoModel> esito = checkIntervento320.apply(capo, allevamento);
					
					assertTrue(esito.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, esito.get().getEsito());
					assertEquals(msg2, esito.get().getMessaggio());
				})
				);
	}
}
