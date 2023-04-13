import {Component, OnDestroy, OnInit} from '@angular/core';
import { InfoGeneraliPSRStrutturale } from '../../models/info-generali-domanda-psr-strutturale';
import { DomandePsrStrutturaliService } from '../../domande-psr-strutturali.service';
import { ActivatedRoute } from '@angular/router';
import { ActivateRouteSupport } from '../../../../shared/utilities/activate-route.support';
import { forkJoin, Subject } from 'rxjs';
import { PsrFattura } from '../../models/dettaglio-istruttoria-srt';

@Component({
  selector: 'app-dettaglio-intervento-psr-strutturale',
  templateUrl: './dettaglio-intervento-psr-strutturale.component.html',
  styleUrls: ['./dettaglio-intervento-psr-strutturale.component.css']
})
export class DettaglioInterventoPsrStrutturaleComponent implements OnInit, OnDestroy {

  idDomanda: number;
  idDomandaPagamento: number;
  idIntervento: number;
  tipologia: 'ACCONTO' | 'SALDO';

  fatture: Array<PsrFattura> = [];

  domandaPsrStrutturale: InfoGeneraliPSRStrutturale;

  protected componentDestroyed$: Subject<boolean> = new Subject();

  constructor(private domandePsrStrutturaliService: DomandePsrStrutturaliService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    ActivateRouteSupport.observeParams(this.componentDestroyed$, this.route)
      .subscribe(({ path, query}) => {
        this.retrieveDomandaAndFatture(+path.idDomanda, path.tipologia.toUpperCase(), +path.idIntervento,
          path.idDomandaPagamento);
      });
  }

  private retrieveDomandaAndFatture(idDomanda: number, tipologia: 'ACCONTO' | 'SALDO', idIntervento: number,
                                    idDomandaPagamento?: number): void {
    this.idDomanda = idDomanda;
    this.idIntervento = idIntervento;
    this.tipologia = tipologia;
    this.idDomandaPagamento = idDomandaPagamento;

    if (this.idDomanda) {
      forkJoin([
        this.domandePsrStrutturaliService.getAziendaDaDomandaPSRStrutturale(this.idDomanda),
        this.domandePsrStrutturaliService.getFattureByProgettoAndIntervento(this.idDomanda, this.tipologia,
          this.idIntervento, this.idDomandaPagamento)
      ]).subscribe(([domanda, fatture]) => {
        this.domandaPsrStrutturale = domanda[0];
        this.fatture = fatture;
      });
    }
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }
}
