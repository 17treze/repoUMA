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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.InformazioniAllevamento;
import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoMacellatoDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneDto;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ControlloCapoMacellatoTests extends ControlloCapoMacellato {

	@Autowired
	private ObjectMapper objectMapper;

	@SpyBean
	private ControlliCapoMacellatoFactory controlliCapoMacellatoFactory;

	//BR4 Verifica del periodo di detenzione del bovino da macellazione in stalla - before data riferimento
	// C1 = Dt_Com_Autorita_Ingresso – Dt_Ingresso <= limite (7 o 27)
	// C2 = Dt_Inserimento_Bdn_Ingresso – Dt_Com_Autorita_Ingresso <= 7
	// FORMULA:  C1 and C2
	@TestFactory
	Collection<DynamicTest> controlloCapoMacellato_checkPeriodoDetenzione_beforeDataRiferimento() throws Exception {

		String msg1 = "Il capo è ammesso con sanzione nonostante i giorni reali di detenzione siano maggiori o uguali a 360 giorni, ma non sono stati raggiunti i 360 giorni di detenzione amministrativi nell’allevamento.";
		String msg2 = "Il capo non è ammissibile perché non soddisfa né i giorni di detenzione amministrativi né quelli reali: entrambi sono inferiori a 360 giorni.";

		// set allevamento
		InformazioniAllevamento infoAllevamento = new InformazioniAllevamento();
		infoAllevamento.setCodiceAllevamentoBdn("123TN123");
		RichiestaAllevamDu allevamento = new RichiestaAllevamDu();
		allevamento.setCodiceIntervento("316");
		allevamento.setDatiAllevamento(objectMapper.writeValueAsString(infoAllevamento));

		return Arrays.asList(
				dynamicTest("caso ok: detenzione singola - Intervento 316 (limite 360 gg) - nato in stalla (limite 27 gg)", () -> {
					LocalDate primoGennaio = LocalDate.of(2021, 1, 1);

					// detenzione della macellazione
					DetenzioneDto detenzione = new DetenzioneDto()
							.setAllevId("123TN123") // capo macellato nell'allevamento che ha richiesto il premio 
							.setDtInizioDetenzione(primoGennaio)
							.setDtFineDetenzione(primoGennaio.plusDays(1)) // serve solo che la data di macellazione sia compresa tra dt inizio e dt fine
							.setVaccaDtIngresso(primoGennaio)  // before data riferimento
							.setVaccaDtComAutoritaIngresso(primoGennaio.plusDays(27)) // rispetta C1
							.setVaccaDtInserimentoBdnIngresso(primoGennaio.plusDays(27).plusDays(7)) // rispetta C2
							.setDtUscita(primoGennaio.plusDays(27).plusDays(7).plusDays(360)); // caso gg amministrativi ok

					CapoMacellatoDto capo = new CapoMacellatoDto().setDetenzioni(Arrays.asList(detenzione));
					capo.setDataNascita(primoGennaio);	// nato in stalla -> limite = 27 gg
					capo.setDataMacellazione(primoGennaio); // macellato durante la detenzione	

					Optional<EsitoCalcoloCapoModel> esito = checkPeriodoDetenzione.apply(capo, allevamento);
					assertTrue(esito.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, esito.get().getEsito());
					assertNull(esito.get().getMessaggio());
				}), 	
				// giorni amministrativi < 360 AND giorni reali >= 360
				dynamicTest("caso sanzioni: detenzione singola - Intervento 316 (limite 360 gg) - nato in stalla (limite 27 gg)", () -> {
					LocalDate primoGennaio = LocalDate.of(2021, 1, 1);

					// detenzione della macellazione
					DetenzioneDto detenzione = new DetenzioneDto()
							.setAllevId("123TN123") // capo macellato nell'allevamento che ha richiesto il premio 
							.setDtInizioDetenzione(primoGennaio)
							.setDtFineDetenzione(primoGennaio.plusDays(1)) // serve solo che la data di macellazione sia compresa tra dt inizio e dt fine
							.setVaccaDtIngresso(primoGennaio)  // before data riferimento
							.setVaccaDtComAutoritaIngresso(primoGennaio.plusDays(27)) // rispetta C1
							.setVaccaDtInserimentoBdnIngresso(primoGennaio.plusDays(27).plusDays(7)) // rispetta C2
							.setDtUscita(primoGennaio.plusDays(360));

					CapoMacellatoDto capo = new CapoMacellatoDto().setDetenzioni(Arrays.asList(detenzione));
					capo.setDataNascita(primoGennaio);	// nato in stalla -> limite = 27 gg
					capo.setDataMacellazione(primoGennaio); // macellato durante la detenzione	

					Optional<EsitoCalcoloCapoModel> esito = checkPeriodoDetenzione.apply(capo, allevamento);
					assertTrue(esito.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, esito.get().getEsito());
					assertEquals(msg1, esito.get().getMessaggio());
				}),
				// giorni amministrativi < 360 AND giorni reali < 360 
				dynamicTest("caso non ammissibile: detenzione singola - Intervento 316 (limite 360 gg) - nato in stalla (limite 27 gg)", () -> {
					LocalDate primoGennaio = LocalDate.of(2021, 1, 1);

					// detenzione della macellazione
					DetenzioneDto detenzione = new DetenzioneDto()
							.setAllevId("123TN123") // capo macellato nell'allevamento che ha richiesto il premio 
							.setDtInizioDetenzione(primoGennaio)
							.setDtFineDetenzione(primoGennaio.plusDays(1)) // serve solo che la data di macellazione sia compresa tra dt inizio e dt fine
							.setVaccaDtIngresso(primoGennaio)  // before data riferimento
							.setVaccaDtComAutoritaIngresso(primoGennaio.plusDays(27)) // rispetta C1
							.setVaccaDtInserimentoBdnIngresso(primoGennaio.plusDays(27).plusDays(7)) // rispetta C2
							.setDtUscita(primoGennaio.plusDays(359));

					CapoMacellatoDto capo = new CapoMacellatoDto().setDetenzioni(Arrays.asList(detenzione));
					capo.setDataNascita(primoGennaio);	// nato in stalla -> limite = 27 gg
					capo.setDataMacellazione(primoGennaio); // macellato durante la detenzione	

					Optional<EsitoCalcoloCapoModel> esito = checkPeriodoDetenzione.apply(capo, allevamento);
					assertTrue(esito.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, esito.get().getEsito());
					assertEquals(msg2, esito.get().getMessaggio());
				})
				);
	}

	//BR4 Verifica del periodo di detenzione del bovino da macellazione in stalla - before data riferimento
	// C1 = Dt_Inserimento_Bdn_Ingresso – Dt_Ingresso <= 7
	// FORMULA:  C1
	@TestFactory
	Collection<DynamicTest> controlloCapoMacellato_checkPeriodoDetenzione_afterDataRiferimento() throws Exception {
		String msg1 = "La vacca è ammissibile al sostegno con sanzione perché c’è almeno una detenzione tra quelle verificate che non rispetta la tempistica di registrazione della movimentazione.";
		String msg2 = "Il capo è ammesso con sanzione nonostante i giorni reali di detenzione siano maggiori o uguali a 180 giorni, ma non sono stati raggiunti i 180 giorni di detenzione amministrativi nell’allevamento.";
		String msg3 = "Il capo non è ammissibile perché non soddisfa né i giorni di detenzione amministrativi né quelli reali: entrambi sono inferiori a 180 giorni.";

		// set allevamento
		InformazioniAllevamento infoAllevamento = new InformazioniAllevamento();
		infoAllevamento.setCodiceAllevamentoBdn("123TN123");
		RichiestaAllevamDu allevamento = new RichiestaAllevamDu();
		allevamento.setCodiceIntervento("315");
		allevamento.setDatiAllevamento(objectMapper.writeValueAsString(infoAllevamento));

		return Arrays.asList(
				// giorni amministrativi >= 180 AND rispetto tempistiche detenzioni ok
				dynamicTest("caso ok: detenzione multipla - Intervento 315 (limite 180 gg) - non nato in stalla (limite 7 gg)", () -> {
					LocalDate primoGennaio = LocalDate.of(2022, 1, 1);

					// detenzione della macellazione
					DetenzioneDto detenzione1 = new DetenzioneDto()
							.setAllevId("123TN123") // capo macellato nell'allevamento che ha richiesto il premio 
							.setDtInizioDetenzione(primoGennaio)
							.setDtFineDetenzione(primoGennaio.plusDays(1)) // serve solo che la data di macellazione sia compresa tra dt inizio e dt fine
							.setVaccaDtIngresso(primoGennaio)  // after data riferimento
							.setVaccaDtInserimentoBdnIngresso(primoGennaio.plusDays(7)) // rispetta C1
							.setDtUscita(primoGennaio.plusDays(14)); // 14 gg -> 7 amministrativi; 14 reali;

					// altra detenzione
					DetenzioneDto detenzione2 = new DetenzioneDto()
							.setAllevId("123NA987")
							.setDtInizioDetenzione(detenzione1.getDtFineDetenzione())
							.setDtFineDetenzione(detenzione1.getDtFineDetenzione().plusDays(1))
							.setVaccaDtIngresso(primoGennaio)  // after data riferimento
							.setVaccaDtInserimentoBdnIngresso(primoGennaio.plusDays(7)) // rispetta C1
							.setDtUscita(primoGennaio.plusDays(181)); // 181 gg -> 173 amministrativi; 180 reali; ==> (CONSIDERA L'ORA LEGALE il 30 marzo)

					CapoMacellatoDto capo = new CapoMacellatoDto().setDetenzioni(Arrays.asList(detenzione1, detenzione2));
					capo.setDataNascita(primoGennaio.minusDays(100));	// non nato nell'allevamento per cui si è richiesto l'intervento -> limite = 7 gg
					capo.setDataMacellazione(primoGennaio); // macellato durante la detenzione1	

					Optional<EsitoCalcoloCapoModel> esito = checkPeriodoDetenzione.apply(capo, allevamento);
					assertTrue(esito.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, esito.get().getEsito());
					assertNull(esito.get().getMessaggio());
				}),
				// giorni amministrativi >= 180 AND rispetto tempistiche detenzioni ko
				dynamicTest("caso sanzione: detenzione multipla - Intervento 315 (limite 180 gg) - non nato in stalla (limite 7 gg)", () -> {
					LocalDate primoGennaio = LocalDate.of(2022, 1, 1);

					// detenzione della macellazione
					DetenzioneDto detenzione1 = new DetenzioneDto()
							.setAllevId("123TN123") // capo macellato nell'allevamento che ha richiesto il premio 
							.setDtInizioDetenzione(primoGennaio)
							.setDtFineDetenzione(primoGennaio.plusDays(1)) // serve solo che la data di macellazione sia compresa tra dt inizio e dt fine
							.setVaccaDtIngresso(primoGennaio)  // after data riferimento
							.setVaccaDtInserimentoBdnIngresso(primoGennaio.plusDays(7)) // rispetta C1
							.setDtUscita(primoGennaio.plusDays(14)); // 14 gg -> 7 amministrativi; 14 reali;

					// altra detenzione - non rispetta tempistiche
					DetenzioneDto detenzione2 = new DetenzioneDto()
							.setAllevId("123NA987")
							.setDtInizioDetenzione(detenzione1.getDtFineDetenzione())
							.setDtFineDetenzione(detenzione1.getDtFineDetenzione().plusDays(1))
							.setVaccaDtIngresso(primoGennaio)  // after data riferimento
							.setVaccaDtInserimentoBdnIngresso(primoGennaio.plusDays(8)) // non rispetta C1
							.setDtUscita(primoGennaio.plusDays(182)); // 182 gg -> 174 amministrativi; 181 reali; ==> (CONSIDERA L'ORA LEGALE il 30 marzo)

					CapoMacellatoDto capo = new CapoMacellatoDto().setDetenzioni(Arrays.asList(detenzione1, detenzione2));
					capo.setDataNascita(primoGennaio.minusDays(100));	// non nato nell'allevamento per cui si è richiesto l'intervento -> limite = 7 gg
					capo.setDataMacellazione(primoGennaio); // macellato durante la detenzione1	

					Optional<EsitoCalcoloCapoModel> esito = checkPeriodoDetenzione.apply(capo, allevamento);
					assertTrue(esito.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, esito.get().getEsito());
					assertEquals(msg1,esito.get().getMessaggio());
				}),
				// giorni amministrativi < 180 AND giorni reali >= 180
				dynamicTest("caso sanzione: detenzione multipla - Intervento 315 (limite 180 gg) - non nato nell'allevamento (limite 7)", () -> {
					LocalDate primoGennaio = LocalDate.of(2022, 1, 1);

					// detenzione della macellazione
					DetenzioneDto detenzione1 = new DetenzioneDto()
							.setAllevId("123TN123") // capo macellato nell'allevamento che ha richiesto il premio 
							.setDtInizioDetenzione(primoGennaio)
							.setDtFineDetenzione(primoGennaio.plusDays(1)) // serve solo che la data di macellazione sia compresa tra dt inizio e dt fine
							.setVaccaDtIngresso(primoGennaio)  // after data riferimento
							.setVaccaDtInserimentoBdnIngresso(primoGennaio.plusDays(7)) // rispetta C1
							.setDtUscita(primoGennaio.plusDays(10)); // 10 gg -> 3 amministrativi; 10 reali; 

					// altra detenzione
					DetenzioneDto detenzione2 = new DetenzioneDto()
							.setAllevId("123NA987")
							.setDtInizioDetenzione(detenzione1.getDtFineDetenzione())
							.setDtFineDetenzione(detenzione1.getDtFineDetenzione().plusDays(1))
							.setVaccaDtIngresso(primoGennaio)  // after data riferimento
							.setVaccaDtInserimentoBdnIngresso(primoGennaio.plusDays(7)) // rispetta C1
							.setDtUscita(primoGennaio.plusDays(171)); // 171 gg ->  163 amministrativi; 170 reali; ==> (CONSIDERA L'ORA LEGALE il 30 marzo)

					CapoMacellatoDto capo = new CapoMacellatoDto().setDetenzioni(Arrays.asList(detenzione1, detenzione2));
					capo.setDataNascita(primoGennaio.minusDays(100));	// non nato nell'allevamento per cui si è richiesto l'intervento -> limite = 7 gg
					capo.setDataMacellazione(primoGennaio); // macellato durante la detenzione1	

					Optional<EsitoCalcoloCapoModel> esito = checkPeriodoDetenzione.apply(capo, allevamento);
					assertTrue(esito.isPresent());
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, esito.get().getEsito());
					assertEquals(msg2,esito.get().getMessaggio());
				}),
				// giorni amministrativi < 180 AND giorni reali < 180 
				dynamicTest("caso non ammissibile: detenzione multipla - Intervento 315 (limite 180 gg) - non nato in stalla (limite 7 gg)", () -> {
					LocalDate primoGennaio = LocalDate.of(2022, 1, 1);

					// detenzione della macellazione
					DetenzioneDto detenzione1 = new DetenzioneDto()
							.setAllevId("123TN123") // capo macellato nell'allevamento che ha richiesto il premio 
							.setDtInizioDetenzione(primoGennaio)
							.setDtFineDetenzione(primoGennaio.plusDays(1)) // serve solo che la data di macellazione sia compresa tra dt inizio e dt fine
							.setVaccaDtIngresso(primoGennaio)  // after data riferimento
							.setVaccaDtInserimentoBdnIngresso(primoGennaio.plusDays(7)) // rispetta C1
							.setDtUscita(primoGennaio.plusDays(10)); // 10 gg -> 3 amministrativi; 10 reali;

					// altra detenzione
					DetenzioneDto detenzione2 = new DetenzioneDto()
							.setAllevId("123NA987")
							.setDtInizioDetenzione(detenzione1.getDtFineDetenzione())
							.setDtFineDetenzione(detenzione1.getDtFineDetenzione().plusDays(1))
							.setVaccaDtIngresso(primoGennaio)  // after data riferimento
							.setVaccaDtInserimentoBdnIngresso(primoGennaio.plusDays(7)) // rispetta C1
							.setDtUscita(primoGennaio.plusDays(170)); // 170 gg -> 162 amministrativi; 169 reali; ==> (CONSIDERA L'ORA LEGALE il 30 marzo)

					CapoMacellatoDto capo = new CapoMacellatoDto().setDetenzioni(Arrays.asList(detenzione1, detenzione2));
					capo.setDataNascita(primoGennaio.minusDays(100));	// non nato nell'allevamento per cui si è richiesto l'intervento -> limite = 7 gg
					capo.setDataMacellazione(primoGennaio); // macellato durante la detenzione1	

					Optional<EsitoCalcoloCapoModel> esito = checkPeriodoDetenzione.apply(capo, allevamento);
					assertTrue(esito.isPresent());
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, esito.get().getEsito());
					assertEquals(msg3,esito.get().getMessaggio());
				})
				);
	}
}
