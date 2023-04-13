import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng-lts';
import { EMPTY, of, Subject } from 'rxjs';
import { catchError, takeUntil } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { AnagraficaFascicoloService } from 'src/app/shared/services/anagrafica-fascicolo.service';
import { EsitoControlloDto } from '../models/EsitoControlloDto';

@Component({
  selector: 'app-popup-controllo-completezza',
  templateUrl: './popup-controllo-completezza.component.html',
  styleUrls: ['./popup-controllo-completezza.component.scss']
})
export class PopupControlloCompletezzaComponent implements OnInit, OnDestroy {
  @Input() public cuaa: string;
  @Input() public isPopupVisible: boolean = false;
  @Output() public chiudiPopup = new EventEmitter();

  public checkList: Map<string, EsitoControlloDto>;
  public keys = [];
  private componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    private messageService: MessageService,
    protected translateService: TranslateService,
    private fascicoloService: AnagraficaFascicoloService) {
  }

  ngOnInit() {}

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  public close() {
    this.isPopupVisible = false;
    this.chiudiPopup.emit();
  }

  private handleMapResult(res: Map<string, EsitoControlloDto>, outMap: Map<string, EsitoControlloDto>): Map<string, EsitoControlloDto> {
    for (let k in res) {
      //if (outMap.has(k)) {
      if (outMap[k] != undefined) {
        outMap[k] = outMap[k] && res[k];
      } else {
        this.keys.push(k);
        outMap[k] = res[k];
      }
    }
    return outMap;
  }

  private handleNoContent = (error: HttpErrorResponse) => {
    if (error.status === 204) {
      return of(new Map<string, EsitoControlloDto>());
    } else if (error.status === 403) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('HttpErrorMessages.403')));
      this.close();
      return EMPTY;
    } else {
      console.error("Errore verifica completezza: " + error.message);
      this.messageService.add(A4gMessages.getToast(
        'tst', A4gSeverityMessage.error, error.message));
      this.close();
      return EMPTY;
    }
  }

  public open() {
    this.fascicoloService.getControlloCompletezza(this.cuaa).pipe(
        takeUntil(this.componentDestroyed$),
        catchError(this.handleNoContent)
      ).subscribe(res => {
      this.checkList = this.handleMapResult(res, new Map<string, EsitoControlloDto>());
    });
  }
}
