import { Component, OnInit } from '@angular/core';
import { ActivateRouteSupport } from '../../../../../shared/utilities/activate-route.support';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs';
import { DomandePsrService } from '../../../domande-psr.service';
import { DomandaPsr } from '../../../models/domanda-psr';
import {
  DettaglioPagamentoM10O11, DettaglioPagamentoM10O12,
  DettaglioPagamentoM10O13,
  DettaglioPagamentoM10O14, DettaglioPagamentoM1311
} from '../../../models/dettaglio-pagamento-psr';
import { map, switchMap, tap } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from '../../../../../shared/a4g-messages';
import { MessageService } from 'primeng-lts';

@Component({
  selector: 'app-dettaglio-pagamento-psr-superficie',
  templateUrl: './dettaglio-pagamento-psr-superficie.component.html',
  styleUrls: ['./dettaglio-pagamento-psr-superficie.component.scss']
})
export class DettaglioPagamentoPsrSuperficieComponent implements OnInit {

  protected componentDestroyed$: Subject<boolean> = new Subject();
  domandaPsr: DomandaPsr;

  dettaglioPagamentoPsr: DettaglioPagamentoM10O11 | DettaglioPagamentoM10O12 | DettaglioPagamentoM10O13 | DettaglioPagamentoM10O14 | DettaglioPagamentoM1311;
  codiceOperazione: string;

  constructor(
    private domandePsrService: DomandePsrService,
    private messageService: MessageService,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    ActivateRouteSupport.observeParams(this.componentDestroyed$, this.route).pipe(
      switchMap(({ path, query }) => {
        return this.domandePsrService.getDomandaPsrByNumeroDomanda(+path.idDomanda).pipe(
          tap((domandaPsr) => this.domandaPsr = domandaPsr),
          map((domandaPsr) => {
            return { path, domandaPsr };
          })
        );
      }),
      switchMap(({ domandaPsr, path }) => {
        this.codiceOperazione = path.codiceOperazione;
        return this.domandePsrService.getDettaglioPagamentoPsr(domandaPsr.numeroDomanda, domandaPsr.dataPresentazione.getFullYear(), path.codiceOperazione, path.tipologia.toUpperCase());
      })
    ).subscribe((dettagliPagamento) => {
      if (dettagliPagamento.status === 204) {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, 'Errore nel recupero del dettaglio pagamento'));
        return;
      }
      this.dettaglioPagamentoPsr = dettagliPagamento.body;
    });
  }
}
