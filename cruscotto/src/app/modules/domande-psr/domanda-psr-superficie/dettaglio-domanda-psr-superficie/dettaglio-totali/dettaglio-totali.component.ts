import { Component, Input, OnChanges } from '@angular/core';
import { DomandePsrService } from '../../../domande-psr.service';
import { DomandaPsr } from '../../../models/domanda-psr';
import { ImportiDomandaPsr } from '../../../models/importi-domanda-psr';
import { forkJoin } from 'rxjs';
import { IstruttoriaPsrSuperficieUtilsService } from '../../istruttoria-psr-superficie-utils.service';

@Component({
  selector: 'app-dettaglio-totali',
  templateUrl: './dettaglio-totali.component.html',
  styleUrls: ['./dettaglio-totali.component.scss']
})
export class DettaglioTotaliComponent implements OnChanges {

  @Input()
  domandaPsr: DomandaPsr;

  importiDomandaPsr: ImportiDomandaPsr;
  importoTotaleLiquidato: number = 0;
  totaleImportoCalcolato: number = 0;

  constructor(private domandePsrService: DomandePsrService,
              private istruttoriaUtilsService: IstruttoriaPsrSuperficieUtilsService) {
  }

  ngOnChanges() {
    if (this.domandaPsr) {
      forkJoin([
        this.domandePsrService.getImportiDomandaPsr(this.domandaPsr.cuaa, this.domandaPsr.dataPresentazione.getFullYear()),
        this.domandePsrService.getStatoDomandaPsrByIdDomanda(this.domandaPsr.numeroDomanda),
        this.domandePsrService.getPSRSuperficieLiquidazione(this.domandaPsr.cuaa, String(this.domandaPsr.numeroDomanda),
          this.domandaPsr.dataPresentazione.getFullYear()),
        this.domandePsrService.getTotaleImportoCalcolatoByIdDomanda(this.domandaPsr.numeroDomanda)
      ]).subscribe(([importiDomandaPsr, statiDomandaPsr, importiLiquidatiDomandaPsr, totaleImportoCalcolato]) => {
        this.importiDomandaPsr = importiDomandaPsr;

        statiDomandaPsr.forEach((statoDomanda) => {
          if (statoDomanda.stato.toLowerCase() !== 'liquidata') return;
          
          let importiStato = importiLiquidatiDomandaPsr
            .filter(this.istruttoriaUtilsService.filterForStatoDomanda(statoDomanda));
          if (statoDomanda.codOperazione.startsWith('13')) {
              importiStato = importiLiquidatiDomandaPsr
                .filter(i => !i.codiceProdotto.startsWith('10') && !i.codiceProdotto.startsWith('11'));
            
            if (!importiStato) return;
          }

          const totaleForStatoDomanda = importiStato.reduce((totale, importoLiquidato) => {
            return totale + importoLiquidato.incassatoNetto;
          }, 0);
          this.importoTotaleLiquidato = this.importoTotaleLiquidato + totaleForStatoDomanda;
        });
        this.totaleImportoCalcolato = totaleImportoCalcolato;
      });
    }
  }
}
