import { DatiSospensioneFascicolo, FascicoloDettaglio } from './../shared/fascicolo.model';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng-lts';
import { Subject, EMPTY } from 'rxjs';
import { takeUntil, catchError } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { FascicoloService } from '../fascicolo.service';
import { FascicoloDettaglioService } from '../fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-dati-sospensione',
  templateUrl: './dati-sospensione.component.html',
  styleUrls: ['./dati-sospensione.component.css']
})
export class DatiSospensioneComponent implements OnInit, OnDestroy {

  public sospensioniList: DatiSospensioneFascicolo[] = [];
  public cols: any[];
  private cuaa = '';
  protected componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    protected route: ActivatedRoute,
    protected fascicoloService: FascicoloService,
    protected fascicoloDettaglioService: FascicoloDettaglioService,
    protected messageService: MessageService,
    protected router: Router,
    protected translateService: TranslateService
  ) { }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  ngOnInit() {
    this.cuaa = this.route.snapshot.paramMap.get('cuaa');
    // this.getDatiSospensione(this.cuaa);
    this.setCols();
  }

  private setCols() {
    this.cols = [
      { field: 'dataInizio', header: this.translateService.instant('SOSPENSIONE.DATA_INIZIO'), date: true },
      { field: 'motivazioneInizio', header: this.translateService.instant('SOSPENSIONE.MOTIVAZIONE_INIZIO') },
      { field: 'dataFine', header: this.translateService.instant('SOSPENSIONE.DATA_FINE'), date: true },
      { field: 'motivazioneFine', header: this.translateService.instant('SOSPENSIONE.MOTIVAZIONE_FINE') },
      { field: 'utente', header: 'Utente' }
    ];
  }
  /*
  private getDatiSospensione(cuaa: string) {
    this.fascicoloService.getDatiSospensioneFascicolo(cuaa).pipe(
      takeUntil(this.componentDestroyed$),
      catchError(err => {
        this.sospensioniList = [];
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        return EMPTY;
      })
    ).subscribe((res) => {
      this.sospensioniList = res;
      this.sospensioniList.sort((a, b) => (a.dataInizio < b.dataInizio) ? 1 : -1);
    });
  }
  */
  public handleClick() {
    this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.DATI);
  }

}
