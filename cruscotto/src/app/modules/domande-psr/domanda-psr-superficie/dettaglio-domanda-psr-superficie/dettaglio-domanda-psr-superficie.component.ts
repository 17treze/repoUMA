import { Component, Input, OnInit } from '@angular/core';
import { DomandaPsr } from '../../models/domanda-psr';
import { DomandePsrService } from '../../domande-psr.service';
import { ImpegnoRichiestoPsr } from '../../models/impegno-richiesto-psr';
import { CodiceMisureIntervento } from '../../models/codiceMisureInterventoEnum';

@Component({
  selector: 'app-dettaglio-domanda-psr-superficie',
  templateUrl: './dettaglio-domanda-psr-superficie.component.html',
  styleUrls: ['./dettaglio-domanda-psr-superficie.component.css']
})
export class DettaglioDomandaPsrSuperficieComponent implements OnInit {

  M10_O1_4 = CodiceMisureIntervento.M10_O1_4;
  M10_O1_1 = CodiceMisureIntervento.M10_O1_1;
  M11_O1_1 = CodiceMisureIntervento.M11_O1_1;
  M11_O2_1 = CodiceMisureIntervento.M11_O2_1;
  M13_O1_1 = CodiceMisureIntervento.M13_O1_1;
  M10_O1_2 = CodiceMisureIntervento.M10_O1_2;
  M10_O1_3 = CodiceMisureIntervento.M10_O1_3;

  interventiPsr = new Map<CodiceMisureIntervento, Array<ImpegnoRichiestoPsr>>();
  cardsContainingValidSupImpegnoNetta = new Map<CodiceMisureIntervento, boolean>();

  @Input()
  domandaPsr: DomandaPsr;

  constructor(private domandePrsService: DomandePsrService) {
  }

  ngOnInit() {
    this.domandePrsService.getImpegniRichiestiPSRSuperficie(this.domandaPsr.numeroDomanda).subscribe(impegniRichiestiPsr => {
      this.filterAndValidateInterventoByCodMisuraAndCodDest(impegniRichiestiPsr, this.M10_O1_4, 'M10.1.4');
      this.filterAndValidateInterventoByCodMisuraAndCodDest(impegniRichiestiPsr, this.M10_O1_1, 'M10.1.1_base', 'M10.1.1_nat_sup_1k', 'M10.1.1_nat_inf_1k', 'M10.1.1_rds');
      this.filterAndValidateInterventoByCodMisuraAndCodDest(impegniRichiestiPsr, this.M11_O1_1, 'M11.1.1_Arboree', 'M11.1.1_Orticole', 'M11.1.1_prato', 'M11.1.1_ViteMelo');
      this.filterAndValidateInterventoByCodMisuraAndCodDest(impegniRichiestiPsr, this.M11_O2_1, 'M11.2.1_Arboree', 'M11.2.1_Orticole', 'M11.2.1_prato', 'M11.2.1_ViteMelo');
      this.filterAndValidateInterventoByCodMisuraAndCodDest(impegniRichiestiPsr, this.M13_O1_1, 'M13_sa123', 'M13_sa4', 'M13_sa5', 'M13_sa6');
      this.filterAndValidateInterventoByCodMisuraAndCodDest(impegniRichiestiPsr, this.M10_O1_2, 'M10.1.2_75', 'M10.1.2_90');
    });
  }

  private filterAndValidateInterventoByCodMisuraAndCodDest(impegniRichiestiPsr: Array<ImpegnoRichiestoPsr>, codiceMisuraintervento: CodiceMisureIntervento, ...codDestinazioni: string[]) {
    this.interventiPsr.set(codiceMisuraintervento, this.filterImpegniRichiestiByCodDestinazione(impegniRichiestiPsr, ...codDestinazioni));
    this.cardsContainingValidSupImpegnoNetta.set(codiceMisuraintervento, this.impegniRichiestiHasValidSupImpegnoNetta(codiceMisuraintervento));
  }

  private filterImpegniRichiestiByCodDestinazione(impegniRichiestiPsr: Array<ImpegnoRichiestoPsr>, ...codDestinazioni: string[]): Array<ImpegnoRichiestoPsr> {
    return impegniRichiestiPsr.filter((value: ImpegnoRichiestoPsr) => codDestinazioni.includes(value.codDestinazione));
  }

  impegniRichiestiHasValidSupImpegnoNetta(codDestinazione: CodiceMisureIntervento): boolean {
    return this.interventiPsr.get(codDestinazione).filter(item => item.supImpegnoNetta !== 0).length > 0;
  }
}
