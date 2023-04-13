import { Component, Input, OnInit } from '@angular/core';
import { DomandaPsr } from '../../models/domanda-psr';
import { DomandePsrService } from '../../domande-psr.service';
import { StatoDomandaPsr } from '../../models/stato-domanda-psr';
import { forkJoin } from 'rxjs';
import {
  DebitiImportoLiquidatoDomandaPsr,
  ImportoLiquidatoDomandaPsr
} from '../../models/importo-liquidato-domanda-psr';
import { IstruttoriaPsrSuperficieUtilsService } from '../istruttoria-psr-superficie-utils.service';

@Component({
  selector: 'app-istruttoria-domanda-psr-superficie',
  templateUrl: './istruttoria-domanda-psr-superficie.component.html',
  styleUrls: ['./istruttoria-domanda-psr-superficie.component.css']
})
export class IstruttoriaDomandaPsrSuperficieComponent implements OnInit {

  @Input()
  domandaPsr: DomandaPsr;

  statiDomandaPsr: StatoDomandaPsr[] = [];
  importiLiquidatiDomandaPsr: ImportoLiquidatoDomandaPsr[] = [];
  debitiForStatoDomanda: Map<StatoDomandaPsr, DebitiImportoLiquidatoDomandaPsr[]> = new Map();

  constructor(private domandePsrService: DomandePsrService,
              private istruttoriaUtilsService:IstruttoriaPsrSuperficieUtilsService) {
  }

  ngOnInit() {
    if (this.domandaPsr) {
      forkJoin([
        this.domandePsrService.getStatoDomandaPsrByIdDomanda(this.domandaPsr.numeroDomanda),
        this.domandePsrService.getPSRSuperficieLiquidazione(this.domandaPsr.cuaa, String(this.domandaPsr.numeroDomanda),
          this.domandaPsr.dataPresentazione.getFullYear())
      ]).subscribe(([statiDomandaPsr, importiLiquidatiDomandaPsr]) => {
        this.statiDomandaPsr = statiDomandaPsr;
        this.importiLiquidatiDomandaPsr = importiLiquidatiDomandaPsr;

        this.statiDomandaPsr.forEach((statoDomanda) => {
          if (statoDomanda.stato.toLowerCase() !== 'liquidata') return;
          let importiStato = importiLiquidatiDomandaPsr
            .filter(this.istruttoriaUtilsService.filterForStatoDomanda(statoDomanda));
          
          if (statoDomanda.codOperazione.startsWith('13')) {
            importiStato = this.importiLiquidatiDomandaPsr
              .filter(i => !i.codiceProdotto.startsWith('10') && !i.codiceProdotto.startsWith('11') && !i.codiceProdotto.substring(1).startsWith('11')
                && i.tipoPagamento.toLowerCase() === statoDomanda.tipoPagamento.toLowerCase());
            if (!importiStato) return;
          }

          statoDomanda.importoLiquidato = importiStato.reduce((totale, importoLiquidato) => {
            return totale + importoLiquidato.incassatoNetto;
          }, 0);
        });
        statiDomandaPsr.forEach((statoDomanda) => {
          this.debitiForStatoDomanda.set(statoDomanda, this.getDebitiForDomanda(statoDomanda));
        });
      });
    }
  }

  getDebitiForDomanda(statoDomanda: StatoDomandaPsr): DebitiImportoLiquidatoDomandaPsr[] {
     let debitiImportoLiquidatoDomandaPsrs = [];
     const filteredImporti = this.importiLiquidatiDomandaPsr
      .filter(this.istruttoriaUtilsService.filterForStatoDomanda(statoDomanda))[0];
     if (filteredImporti) {
      debitiImportoLiquidatoDomandaPsrs = filteredImporti.debiti;
     }
     return debitiImportoLiquidatoDomandaPsrs.length > 0 ? debitiImportoLiquidatoDomandaPsrs : undefined;
  }

  canShowCalculatedAmount(statoDomandaPsr: StatoDomandaPsr): boolean {
    return this.istruttoriaUtilsService.canShowCalculatedAmount(statoDomandaPsr);
  }
}
