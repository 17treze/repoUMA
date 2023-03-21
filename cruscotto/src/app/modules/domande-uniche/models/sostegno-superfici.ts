import {DomandaUnica} from "./domanda-unica";
import {ElementOfSostegnoSuperfici} from "./element-of-sostegno-superfici";

export class SostegnoSuperfici {

  superficieRichiestaSoia: number;
  superficieRichiestaFrumentoDuro: number;
  superficieRichiestaFrumentoProteaginose: number;
  superficieRichiestaFrumentoLeguminose: number;
  superficieRichiestaPomodoro: number;
  superficieRichiestaOlivoNazionale: number;
  superficieRichiestaOlivo75: number;
  superficieRichiestaOlivoQualita: number;
  superficieTotaleRichiesta: number;

  sostegnoSoiaRichiesto: boolean;
  sostegnoFrumentoDuroRichiesto: boolean;
  sostegnoFrumentoProteaginoseRichiesto: boolean;
  sostegnoFrumentoLeguminoseRichiesto: boolean;
  sostegnoPomorodoRichiesto: boolean;
  sostegnoOlivoNazionareRichiesto: boolean;
  sostegnoOlivo75Richiesto: boolean;
  sostegnoOlivoQualitaRichiesto: boolean;

  public static calcolaElementiSostegnoSuperficiFrom(domanda: DomandaUnica): Array<ElementOfSostegnoSuperfici> {
    let elementiSost: Array<ElementOfSostegnoSuperfici> = [];
    if (domanda && domanda.sostegnoSuperfici) {
      let sostegno: SostegnoSuperfici = domanda.sostegnoSuperfici;
      SostegnoSuperfici.pushElementIfPresent(elementiSost, sostegno.superficieRichiestaSoia, sostegno.sostegnoSoiaRichiesto, "soia");
      SostegnoSuperfici.pushElementIfPresent(elementiSost, sostegno.superficieRichiestaFrumentoDuro, sostegno.sostegnoFrumentoDuroRichiesto, "frumentoDuro");
      SostegnoSuperfici.pushElementIfPresent(elementiSost, sostegno.superficieRichiestaFrumentoLeguminose, sostegno.sostegnoFrumentoLeguminoseRichiesto, "frumentoLeguminose");
      SostegnoSuperfici.pushElementIfPresent(elementiSost, sostegno.superficieRichiestaFrumentoProteaginose, sostegno.sostegnoFrumentoProteaginoseRichiesto, "frumentoProteaginose");
      SostegnoSuperfici.pushElementIfPresent(elementiSost, sostegno.superficieRichiestaPomodoro, sostegno.sostegnoPomorodoRichiesto,"pomodoro");
      SostegnoSuperfici.pushElementIfPresent(elementiSost, sostegno.superficieRichiestaOlivo75, sostegno.sostegnoOlivo75Richiesto, "olivo75");
      SostegnoSuperfici.pushElementIfPresent(elementiSost, sostegno.superficieRichiestaOlivoQualita, sostegno.sostegnoOlivoQualitaRichiesto, "olivoQualita");
      SostegnoSuperfici.pushElementIfPresent(elementiSost, sostegno.superficieRichiestaOlivoNazionale, sostegno.sostegnoOlivoNazionareRichiesto, "olivoNazionale");
      SostegnoSuperfici.pushElementIfPresent(elementiSost, sostegno.superficieTotaleRichiesta, true, "totale");
    }
    return elementiSost;
  }

  private static pushElementIfPresent(elementiSost: Array<ElementOfSostegnoSuperfici>,
                                      superficie: number,
                                      isSostegnoRichiesto: boolean,
                                      label: string): void {
      if (isSostegnoRichiesto) {
        elementiSost.push(ElementOfSostegnoSuperfici.of(label, superficie, isSostegnoRichiesto));
      }
  }

}


