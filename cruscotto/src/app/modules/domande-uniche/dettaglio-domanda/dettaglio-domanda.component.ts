import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { DomandaUnica } from "../models/domanda-unica";
import { SostegnoSuperfici } from "../models/sostegno-superfici";
import { ElementOfSostegnoSuperfici } from "../models/element-of-sostegno-superfici";
import { DomandaIntegrativa } from '../models/domanda-integrativa';
import { DomandeUnicheService } from '../domande-uniche.service';
import { Importi } from '../models/importi';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-dettaglio-domanda',
  encapsulation: ViewEncapsulation.None,
  templateUrl: './dettaglio-domanda.component.html',
  styleUrls: ['./dettaglio-domanda.component.scss']
})
export class DettaglioDomandaComponent implements OnInit {
  public interventi: Array<string> = [];
  public domandaIntegrativa: DomandaIntegrativa;
  public numeroDomandaIntegrativa: string;
  public dataDomandaIntegrativa: string;
  public importoCalcolato: number;
  public importoLiquidato: number;

  public interventoDescrizione = {
    "LATTE": "domandaUnica.sostegni.accoppiato_zootecnia.interventiACZ.int310",
    "LATTE_BMONT": "domandaUnica.sostegni.accoppiato_zootecnia.interventiACZ.int311",
    "LATTE_BUFAL": "domandaUnica.sostegni.accoppiato_zootecnia.interventiACZ.int312",
    "BOVINI_VAC": "domandaUnica.sostegni.accoppiato_zootecnia.interventiACZ.int313",
    "BOVINI_VAC_SEL": "domandaUnica.sostegni.accoppiato_zootecnia.interventiACZ.int314",
    "BOVINI_MAC": "domandaUnica.sostegni.accoppiato_zootecnia.interventiACZ.int315",
    "BOVINI_MAC_12M": "domandaUnica.sostegni.accoppiato_zootecnia.interventiACZ.int316",
    "BOVINI_MAC_ETIC": "domandaUnica.sostegni.accoppiato_zootecnia.interventiACZ.int318",
    "OVICAP_AGN": "domandaUnica.sostegni.accoppiato_zootecnia.interventiACZ.int320",
    "OVICAP_MAC": "domandaUnica.sostegni.accoppiato_zootecnia.interventiACZ.int321",
    "BOVINI_VAC_NO_ISCR": "domandaUnica.sostegni.accoppiato_zootecnia.interventiACZ.int322"
  };

  @Input() public domandaUnica: DomandaUnica;
  public elementiSostegno: Array<ElementOfSostegnoSuperfici>;
  private importi: Importi;

  constructor(private domandeUnicheService: DomandeUnicheService,
              private translate: TranslateService) { }

  ngOnInit(): void {
    this.elementiSostegno = SostegnoSuperfici.calcolaElementiSostegnoSuperficiFrom(this.domandaUnica);

    let numeroDomanda: string = this.domandaUnica.infoGeneraliDomanda.numeroDomanda;
    this.caricaImporti(numeroDomanda);

    this.domandaUnica.richieste.sostegniAllevamento.sort((a,b) => {
      if (a.codIdAllevamento === b.codIdAllevamento)
        return this.translate.instant(this.interventoDescrizione[a.codIntervento]).localeCompare(this.translate.instant(this.interventoDescrizione[b.codIntervento]));
      return a.codIdAllevamento.localeCompare(b.codIdAllevamento)
    });

    console.log(this.domandaUnica);
  }

  private caricaImporti(numeroDomanda: string): any {
    this.importi = new Importi;
    this.domandeUnicheService.getImporti(numeroDomanda).subscribe(
      (importi: Importi) => {
        this.importi = importi;
        if (this.importi != null) {
          if (this.importi.importoCalcolato != null) {
            this.importoCalcolato = this.importi.importoCalcolato
          }
          if (this.importi.importoLiquidato != null) {
            this.importoLiquidato = this.importi.importoLiquidato
          }
        }
      }
    ), err => {
      console.error(err);
    }
  }

  public recuperaIconZootecnia(codiceInt: string): string {
    const interventoIcon = {
      "LATTE": "assets/icons/svg/icon-latte.svg",
      "LATTE_BMONT": "assets/icons/svg/icon-montane.svg",
      "LATTE_BUFAL": "assets/icons/svg/icon-bufale.svg",
      "BOVINI_VAC": "assets/icons/svg/icon-nutrici.svg",
      "BOVINI_VAC_SEL": "assets/icons/svg/icon-nutricirazza.svg",
      "BOVINI_MAC": "assets/icons/svg/icon-bovinimacellati.svg",
      "BOVINI_MAC_12M": "assets/icons/svg/icon-bovinimacellati-12.svg",
      "BOVINI_MAC_ETIC": "assets/icons/svg/icon-bovinimacellati-etichetta.svg",
      "OVICAP_AGN": "assets/icons/svg/icon-agnelle.svg",
      "OVICAP_MAC": "assets/icons/svg/icon-ovicaprini.svg",
      "BOVINI_VAC_NO_ISCR": "assets/icons/svg/icon-nutricinoniscritte.svg"
    };

    return interventoIcon[codiceInt];
  }

  public recuperaIconSuperficie(label: string): string {
    
    const interventoIcon = {
      "soia": "assets/icons/svg/icon-soia.svg",
      "frumentoDuro": "assets/icons/svg/icon-frumentoduro.svg",
      "frumentoLeguminose": "assets/icons/svg/icon-leguminose.svg",
      "frumentoProteaginose": "assets/icons/svg/icon-proteaginose.svg",
      "pomodoro": "assets/icons/svg/icon-pomodoro.svg",
      "olivo75": "assets/icons/svg/icon-olivo-pendenza.svg",
      "olivoQualita": "assets/icons/svg/icon-olivoqualita.svg",
      "olivoNazionale": "assets/icons/svg/icon-olivonazionale.svg"
    }
    
    return interventoIcon[label];
  }

  /*
  private creaElencoInterventiACZ(domandaIntegrativa: DomandaIntegrativa): Set<string>  {
    let interventi_appo = new Set("");
    for (let numeroAllevamenti in domandaIntegrativa.allevamenti){
      for (let numeroCapi in domandaIntegrativa.allevamenti[numeroAllevamenti].capi){
        for (let intervento of domandaIntegrativa.allevamenti[numeroAllevamenti].capi[numeroCapi].interventi){
          interventi_appo.add(intervento.codice);
        }
      }
    }
    return interventi_appo;
  }
  */
}
