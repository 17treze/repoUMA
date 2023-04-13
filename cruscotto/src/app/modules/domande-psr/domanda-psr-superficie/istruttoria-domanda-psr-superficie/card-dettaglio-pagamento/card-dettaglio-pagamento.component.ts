import { Component, Input, OnChanges } from '@angular/core';
import { StatoDomandaPsr } from '../../../models/stato-domanda-psr';
import { ConvertCodiceOperazioneToReadableStringPipe } from '../../dettaglio-domanda-psr-superficie/card-dettaglio-sostegno/convert-codice-operazione-to-readable-string.pipe';
import { ActivatedRoute, Router } from '@angular/router';
import { DomandePsrService } from '../../../domande-psr.service';
import { IstruttoriaPsrSuperficieUtilsService } from '../../istruttoria-psr-superficie-utils.service';

@Component({
  selector: 'app-card-dettaglio-pagamento',
  templateUrl: './card-dettaglio-pagamento.component.html',
  styleUrls: ['./card-dettaglio-pagamento.component.css'],
  providers: [ConvertCodiceOperazioneToReadableStringPipe]
})
export class CardDettaglioPagamentoComponent implements OnChanges {

  @Input()
  statoDomandaPsr: StatoDomandaPsr;
  isNavigationArrowVisible = false;
  importoCalcolato: number;

  constructor(private router: Router, private route: ActivatedRoute,
              private domandePsrService: DomandePsrService,
              private istruttoriaUtilsService: IstruttoriaPsrSuperficieUtilsService) {
  }

  ngOnChanges() {
    if (this.statoDomandaPsr.stato === 'Liquidata' || this.statoDomandaPsr.stato === 'Liquidabile') {
      this.isNavigationArrowVisible = true;
    }
    if (this.statoDomandaPsr) {
      this.domandePsrService.getImportoCalcolato(this.statoDomandaPsr.idDomanda, this.statoDomandaPsr.codOperazione, this.statoDomandaPsr.tipoPagamento).subscribe((importoCalcolato) => {
        this.importoCalcolato = importoCalcolato;
      });
    }
  }

  canShowCalculatedAmount(): boolean {
    return this.istruttoriaUtilsService.canShowCalculatedAmount(this.statoDomandaPsr);
  }

  getCurrentSvg(): string {
    return 'assets/icons/svg/svg-psr-superficie/intervento' + this.statoDomandaPsr.codOperazione + '.svg';
  }

  goToDettaglioPagamento() {
    let tipoPagamento = this.statoDomandaPsr.tipoPagamento;
    if (this.statoDomandaPsr.interventoPat === 'PAT') {
      tipoPagamento = tipoPagamento === 'INTEGRAZIONE' ? 'INTEGR' : tipoPagamento;
      tipoPagamento = tipoPagamento.concat('_PAT');
    }

    this.router.navigate(
      ['./dettaglio-pagamento', tipoPagamento.toLowerCase().replace('_pat', ''),
        this.statoDomandaPsr.codOperazione], {relativeTo: this.route}
    );
  }
}
