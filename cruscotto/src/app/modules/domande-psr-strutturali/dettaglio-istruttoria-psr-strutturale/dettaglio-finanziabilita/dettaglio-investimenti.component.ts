import { Component, OnDestroy, OnInit } from '@angular/core';
import { PsrInterventoDettaglioDto } from '../../models/dettaglio-istruttoria-srt';
import { ActivateRouteSupport } from '../../../../shared/utilities/activate-route.support';
import { ActivatedRoute } from '@angular/router';
import { forkJoin, Subject } from 'rxjs';
import { DomandePsrStrutturaliService } from '../../domande-psr-strutturali.service';

import { InfoGeneraliPSRStrutturale } from '../../models/info-generali-domanda-psr-strutturale';

@Component({
  selector: 'app-dettaglio-investimenti',
  templateUrl: './dettaglio-investimenti.component.html',
  styleUrls: ['./dettaglio-investimenti.component.css']
})
export class DettaglioInvestimentiComponent implements OnInit, OnDestroy {

  dettagliFinanziabilita: Array<PsrInterventoDettaglioDto> = [];

  protected componentDestroyed$: Subject<boolean> = new Subject();

  idDomanda: number;
  tipologia: 'ACCONTO' | 'SALDO' | 'FINANZIABILITA';
  idDomandaPagamento?: number;

  domandaPsrStrutturale: InfoGeneraliPSRStrutturale;


  constructor(private domandePsrStrutturaliService: DomandePsrStrutturaliService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    ActivateRouteSupport.observeParams(this.componentDestroyed$, this.route)
      .subscribe(({ path, query}) => {
        this.callServiceDettaglioDomandaFrom(+path.idDomanda, path.tipologia.toUpperCase(), path.idDomandaPagamento);
      });
  }

  private callServiceDettaglioDomandaFrom(idDomanda: number, tipologia: 'ACCONTO' | 'SALDO' | 'FINANZIABILITA', idDomandaPagamento?: number): void {
    this.idDomanda = idDomanda;
    this.tipologia = tipologia;
    this.idDomandaPagamento = idDomandaPagamento;

    if (this.idDomanda) {
      forkJoin([
        this.domandePsrStrutturaliService.getAziendaDaDomandaPSRStrutturale(this.idDomanda),
        this.domandePsrStrutturaliService.getDettaglioIstruttoriaByProgettoAndTipo(this.idDomanda, this.tipologia, this.idDomandaPagamento)
      ]).subscribe(([domanda, dettaglioFinanziabilita]) => {
        this.domandaPsrStrutturale = domanda[0];
        this.dettagliFinanziabilita = dettaglioFinanziabilita;
      });
    }
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }
}
