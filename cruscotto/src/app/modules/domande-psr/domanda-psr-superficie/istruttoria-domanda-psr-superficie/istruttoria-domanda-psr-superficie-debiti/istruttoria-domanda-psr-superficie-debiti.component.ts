import { Component, Input } from '@angular/core';
import { DebitiImportoLiquidatoDomandaPsr } from '../../../models/importo-liquidato-domanda-psr';

@Component({
  selector: 'app-istruttoria-domanda-psr-superficie-debiti',
  templateUrl: './istruttoria-domanda-psr-superficie-debiti.component.html',
  styleUrls: ['./istruttoria-domanda-psr-superficie-debiti.component.css']
})
export class IstruttoriaDomandaPsrSuperficieDebitiComponent {

  @Input()
  debiti: DebitiImportoLiquidatoDomandaPsr[];

  getTotaleDebiti(): number {
    return this.debiti.reduce((totale, debito) => {
      return totale + debito.importo;
    }, 0);
  }
}
