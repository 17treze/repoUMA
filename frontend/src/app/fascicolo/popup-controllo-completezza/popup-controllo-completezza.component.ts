import { TipoSegnalazioneEnum } from './../creazione-fascicolo/dto/EsitoControlloDto';
import { StatoFascicoloEnum } from '../creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng-lts';
import { EMPTY, of, Subject } from 'rxjs';
import { catchError, switchMap, takeUntil } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { EsitoControlloDto } from '../creazione-fascicolo/dto/EsitoControlloDto';
import { MediatorService } from '../mediator.service';

@Component({
  selector: 'app-popup-controllo-completezza',
  templateUrl: './popup-controllo-completezza.component.html',
  styleUrls: ['./popup-controllo-completezza.component.scss']
})
export class PopupControlloCompletezzaComponent implements OnInit {

  @Input() public cuaa: string;
  @Input() public display = false;
  @Input() public statoFascicolo: StatoFascicoloEnum;
  @Input() public isFascicoloStatoIdoneoPerDownloadScheda: boolean;
  @Output() public chiudiPopup = new EventEmitter();
  @Output() public updateStatoFascicolo = new EventEmitter();
  @Output() public schedaValidazione = new EventEmitter();

  public checkList: Map<string, EsitoControlloDto>;
  public keys = [];
  public erroriList: string[];
  public avvertenzeList: string[];
  private componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    private messageService: MessageService,
    private mediatorService: MediatorService,
    private translateService: TranslateService
  ) { }

  ngOnInit() {
    this.getEsitiControlloCompletezza();
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  public close() {
    this.display = false;
    this.chiudiPopup.emit();
  }

  public startControlloCompletezza() {
    this.mediatorService.startControlloCompletezza(this.cuaa)
      .pipe(
        takeUntil(this.componentDestroyed$),
        catchError(err => {
          if (err.status === 400) {
            this.messageService.add(A4gMessages.getToast(
                'tst', A4gSeverityMessage.error,
                this.translateService.instant('FAS_ANA.CONTROLLI_COMPLETEZZA_KO.' + err.error.message)));
          } else {
            this.messageService.add(A4gMessages.getToast(
                'fdc-toast', A4gSeverityMessage.error, this.translateService.instant('FAS_ANA.RUN_CONTROLLI_COMPLETEZZA_ERROR')));
          }
          return EMPTY;
        }),
        switchMap(res => {
          this.messageService.add(A4gMessages.getToast(
            'fdc-toast', A4gSeverityMessage.success, this.translateService.instant('FAS_ANA.RUN_CONTROLLI_COMPLETEZZA')
          ));
          return of(this.getEsitiControlloCompletezza());
        })
      ).subscribe();
  }

  public getEsitiControlloCompletezza() {
    this.mediatorService.getEsitiControlloCompletezza(this.cuaa).pipe(
      takeUntil(this.componentDestroyed$),
      catchError(this.handleNoContentOrError)
    ).subscribe(res => {
      this.updateStatoFascicolo.emit();
      this.keys = [];
      this.checkList = this.handleMapResult(res, new Map<string, EsitoControlloDto>());
      this.setErroriEAvvertenze(this.checkList);
    });
  }

  public isStatoControlliInCorso() {
    return this.statoFascicolo === StatoFascicoloEnum.CONTROLLI_IN_CORSO;
  }

  public isStatoInAggiornamento() {
    return this.statoFascicolo === StatoFascicoloEnum.IN_AGGIORNAMENTO;
  }

  private handleNoContentOrError = (err: HttpErrorResponse) => {
    if (err.status === 204) {
      return of(new Map<string, EsitoControlloDto>());
    } else {
      this.messageService.add(A4gMessages.getToast(
        'fdc-toast', A4gSeverityMessage.error, this.translateService.instant('FAS_ANA.GET_ESITI_CONTROLLI_COMPLETEZZA_ERROR')));
      return EMPTY;
    }
  }

  private handleMapResult(res: Map<string, EsitoControlloDto>, outMap: Map<string, EsitoControlloDto>): Map<string, EsitoControlloDto> {
    for (const k in res) {
      if (res[k].esito != null) {
        if (outMap[k] !== undefined) {
          outMap[k] = outMap[k] && res[k];
        } else {
          this.keys.push(k);
          outMap[k] = res[k];
        }
      }
    }
    return outMap;
  }

  private setErroriEAvvertenze(checkList: Map<string, EsitoControlloDto>) {
    this.avvertenzeList = [];
    this.erroriList = [];
    this.keys.forEach(res => {
      if (checkList[res].segnalazioni && checkList[res].segnalazioni.length > 0) {
        checkList[res].segnalazioni.forEach(
          segnalazione => {
            if (segnalazione.tipo === TipoSegnalazioneEnum.AVVERTENZA) {
              this.avvertenzeList.push(segnalazione.descrizione);
            } else {
              this.erroriList.push(segnalazione.descrizione);
            }
          }
        );
      }
    });
  }

  public getSchedaValidazione() {
    this.schedaValidazione.emit();
    this.updateStatoFascicolo.emit();
  }

}
