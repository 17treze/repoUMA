import { DatiPagamento } from '../models/dettaglio-istruttoria-srt';
import { Component, Input, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, takeUntil } from 'rxjs/operators';
import { DomandePsrStrutturaliService } from '../domande-psr-strutturali.service';
import { DomandaPsrStrutturale } from '../models/domanda-psr-strutturale';
import { EMPTY, forkJoin, Subject } from 'rxjs';
import { MessageService } from 'primeng-lts';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { TotaliImportiSRStrutturale } from '../models/importi-totali';
import { getIconForDatiPagamentoByTipologia } from '../domande-psr-strutturali-utils';

@Component({
  selector: 'app-dettaglio-istruttoria-psr-strutt',
  templateUrl: './dettaglio-istruttoria-psr-strutturale.component.html',
  styleUrls: ['./dettaglio-istruttoria-psr-strutturale.component.scss'],
  encapsulation: ViewEncapsulation.None
})

export class DettaglioIstruttoriaPsrStrutturaleComponent implements OnInit, OnDestroy {

  @Input() public domandaPsrStrutturale: DomandaPsrStrutturale;
  @Input() public dataProtocollazione;

  public statoProgettoIcon = {
    FINANZIABILE: 'icon-tractor.svg',
    ANTICIPO: 'icon-land.svg',
    SALDO: 'icon-w.svg',
    ACCONTO: 'icon-w.svg',
    CONTROLLI_IN_LOCO: 'icon-w.svg',
  };
  public severityClass = {
    0: 'alert-danger',
    1: 'alert-warning',
    2: 'alert-success'
  };
  public severityColorClass = {
    0: 'text-warning',
    1: 'text-warning',
    2: 'text-danger'
  };

  public statiDettaglio: Array<string> = ['ANTICIPO', 'ACCONTO', 'SALDO', 'CONTROLLI_IN_LOCO', 'FINANZIABILE'];
  public dettaglioCard: DatiPagamento[] = [];
  public statoProgetto: string;
  public isFinanziatoPat: boolean;
  private componentDestroyed$: Subject<boolean> = new Subject();
  private totaliImportiSRStrutturale: TotaliImportiSRStrutturale[] = [];

  constructor(
    private route: ActivatedRoute,
    private service: DomandePsrStrutturaliService,
    private messageService: MessageService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getDettaglioIstruttoria();
  }

  private getDettaglioIstruttoria() {
    const numeroDomanda = this.route.snapshot.params.idDomanda;
    forkJoin(
      this.service.getPSRStrutturaliTotali(numeroDomanda).pipe(
        catchError(e => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDettaglioPSRStr));
          return EMPTY;
        })),
      this.service.getPSRStrutturaliDettaglioIstruttoria(numeroDomanda).pipe(
        catchError(e => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDettaglioPSRStr));
          return EMPTY;
        })),
      this.service.getVariantiByIdProgetto(numeroDomanda).pipe(
        catchError(e => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDettaglioPSRStr));
          return EMPTY;
        }))
    ).pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(result => {
        if (result) {
          this.totaliImportiSRStrutturale = result[0];

          const dettaglioIstruttoria = result[1];

          this.statoProgetto = dettaglioIstruttoria.statoProgetto;
          this.isFinanziatoPat = dettaglioIstruttoria.isFinanziatoPat;

          this.setObject(dettaglioIstruttoria);
          if (!dettaglioIstruttoria.isFinanziatoPat) {
            this.getPSRStrutturaliLiquidazione(dettaglioIstruttoria.idProgetto);
          }
          if (result.length > 2 && result[2]) {
            result[2].forEach(variante => {
              this.setObject(variante);
            });
          }
        } else {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreDettaglioVuotoPSRStr));
        }
      },
      error => {
        console.log(error);
      });
  }

  private setObject(result: any) {
    const arrayPro = ['acconti', 'anticipo', 'saldo', 'finanziabilita', 'idVariante'];
    Object.keys(result).forEach(key => {
      if (arrayPro.includes(key) && result[key]) {
        if (key === 'acconti') {
          result[key].forEach(element => {
            this.dettaglioCard.push(
              {
                tipologia: 'acconto',
                contributoLiquidabile: element.contributoLiquidabile,
                contributoRichiesto: element.contributoRichiesto,
                costoInvestimentoRichiesto: element.costoInvestimentoRichiesto,
                data: element.data,
                idDomandaPagamento: element.idDomandaPagamento,
                saldoContributoLiquidato: element.saldoContributoLiquidato,
                socNumeroDomanda: element.socNumeroDomanda,
                socIncassatoNetto: element.socIncassatoNetto,
                sanzioni: element.sanzioni
              });
          });
        }
        if (key === 'saldo') {
          this.dettaglioCard.push(
            {
              tipologia: key,
              contributoLiquidabile: result[key].contributoLiquidabile,
              contributoRichiesto: result[key].contributoRichiesto,
              costoInvestimentoRichiesto: result[key].costoInvestimentoRichiesto,
              data: result[key].data,
              saldoContributoLiquidato: result[key].saldoContributoLiquidato,
              socNumeroDomanda: result[key].socNumeroDomanda,
              socIncassatoNetto: result[key].socIncassatoNetto,
              sanzioni: result[key].sanzioni
            });
        }
        if (key === 'anticipo') {
          this.dettaglioCard.push(
            {
              tipologia: key,
              anticipoLiquidabile: result[key].anticipoLiquidabile,
              anticipoRichiesto: result[key].anticipoRichiesto,
              contributoAmmesso: this.totaliImportiSRStrutturale[0].contributoTotale,
              costoAmmesso: this.totaliImportiSRStrutturale[0].costoTotale,
              data: result[key].data,
              socNumeroDomanda: result[key].socNumeroDomanda,
              socIncassatoNetto: result[key].socIncassatoNetto
            });
        }
        if (key === 'finanziabilita') {
          this.dettaglioCard.push(
            {
              tipologia: key,
              contributoAmmesso: result[key].contributoAmmesso,
              contributoRichiesto: result[key].contributoRichiesto,
              costoRichiesto: result[key].costoRichiesto,
              data: result[key].data
            });
        }
        if (key === 'idVariante' && result.dataApprovazione) {
          this.dettaglioCard.push({
            tipologia: key,
            data: result.dataApprovazione
          });
        }
      }
    });
    this.dettaglioCard.sort((a, b) => a.data < b.data ? 1 : -1);
  }

  private getPSRStrutturaliLiquidazione(idProgetto) {
    this.service.getPSRStrutturaliLiquidazione(this.domandaPsrStrutturale[0].cuaa, idProgetto)
      .subscribe(result => {
          if (result) {
            result.forEach(element => {
              this.dettaglioCard.forEach(item => {
                if (item.socNumeroDomanda && item.socNumeroDomanda === element.numeroDomanda) {
                  item.socIncassatoNetto = element.incassatoNetto;
                  item.debiti = element.debiti;
                  item.totaleRecuperato  = element.totaleRecuperato;
                }
              });
            });
          }
        },
        err => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoTotaliPSRStr));
        }
      );
  }

  public getTimelineIcon(datiPagamento: DatiPagamento) {
    return getIconForDatiPagamentoByTipologia(datiPagamento.tipologia);
  }

  public getLabelCard(statoProgetto: string) {
    if (statoProgetto && !this.statiDettaglio.includes(statoProgetto.toUpperCase())) {
      return statoProgetto.toUpperCase();
    } else {
      return 'FINANZIABILE';
    }
  }

  public getIncassatoNetto(statoCard: string, item: DatiPagamento) {
    if (statoCard === 'Anticipo') {
      if (this.isFinanziatoPat) {
        return item.anticipoLiquidabile;
      } else {
        return item.socIncassatoNetto;
      }
    }
    if (statoCard === 'Saldo') {
      if (this.isFinanziatoPat) {
        return item.saldoContributoLiquidato;
      } else {
        return item.socIncassatoNetto;
      }
    }
    if (statoCard === 'Acconto') {
      if (this.isFinanziatoPat) {
        return item.contributoLiquidabile;
      } else {
        return item.socIncassatoNetto;
      }
    }
  }

  public showValue(value: number) {
    return value != null && value >= 0;
  }

  public showRow(value: number) {
    return value != null && value > 0;
  }

  public goToEsitiIstruttoriaVisible(statoProgetto, tipologia: string): boolean {
    if (statoProgetto && tipologia !== 'anticipo') {
      return this.statiDettaglio.includes(statoProgetto.toUpperCase());
    } else {
      return false;
    }
  }

  public goToEsitiIstruttoria(statoProgetto, tipologia: string, idDomandaPagamento?: number) {
    if (this.statiDettaglio.includes(statoProgetto.toUpperCase())) {
      tipologia = tipologia ? tipologia.replace('_pat', '').replace('_PAT', '') : tipologia;
      const navigateTo = ['./dettaglio', tipologia];
      if (idDomandaPagamento) {
        navigateTo.push(String(idDomandaPagamento));
      }
      this.router.navigate(navigateTo, {relativeTo: this.route});
    } else {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.operazioneNegataPerStato(statoProgetto)));
    }
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

}
