import { MessageService } from 'primeng/api';
import { TipoProcesso } from 'src/app/istruttoria/istruttoria-antimafia/dto/TipoProcesso';
import { IstruttoriaService } from '../istruttoria.service';
import { SostegnoDu } from '../classi/SostegnoDu';
import { tap, takeUntil, switchMap } from 'rxjs/operators';
import { ProcessRepeatingServiceCall } from '../sostegno-shared/repeating-service-call';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { defer, Subject, iif } from 'rxjs';
import { LoaderService } from 'src/app/loader.service';
import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CONF_PROCESSI } from '../sostegno-shared/conf-processi';
import { FlowIstruttoriaDUEnum } from '../classi/FlowIstruttoriaDUEnum';
import { IstruttoriaDomandaUnica } from '../classi/IstruttoriaDomandaUnica';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class SostegnoDettaglioService {
    protected componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    private istruttoriaService: IstruttoriaService,
    protected repeatingServiceCall: ProcessRepeatingServiceCall,
    protected messageService: MessageService,
    protected loaderService: LoaderService,
  ) { }

  public caricaProcessiDaControllare(istruttoria: IstruttoriaDomandaUnica): TipoProcesso[] {
    //carico i processi da controllare per TUTTI i valori di FlowIstruttoriaDUEnum
    let processiDaControllare: TipoProcesso[] = [];
    processiDaControllare = this.getProcessiDaControllare(istruttoria, FlowIstruttoriaDUEnum.CALCOLO_PREMIO);
    processiDaControllare.concat(this.getProcessiDaControllare(istruttoria, FlowIstruttoriaDUEnum.CALCOLO_PREMIO));
    processiDaControllare.concat(this.getProcessiDaControllare(istruttoria, FlowIstruttoriaDUEnum.CALCOLO_PREMIO));
    return processiDaControllare;
  }

  public getProcessiDaControllare(istruttoria: IstruttoriaDomandaUnica, flowIstruttoria: FlowIstruttoriaDUEnum): TipoProcesso[] {
    try {
      let retVal = CONF_PROCESSI
      [istruttoria.sostegno]
      [flowIstruttoria]
      [istruttoria.stato];
      if (retVal.length === 0) {
        return [];
      }
      return retVal;
    } catch (error) {
      return [];
    }
  }

  public eseguiProcessoIstruttoria(
    idIstruttoria: number,
    tipoProcesso: TipoProcesso,
    checkTipiProcessoInEsecuzione: TipoProcesso[],
    annoCampagna?: number, sostegno?: SostegnoDu) {
      const avvia$ = this.istruttoriaService.avviaProcessoIstruttoriaDUByID([idIstruttoria], tipoProcesso, annoCampagna, sostegno).pipe(
      tap(val => {
        this.repeatingServiceCall.start();
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.info, A4gMessages.processoAvviatoOK));
      })
    );
    const showMessage$ = defer(() => this.messageService.add(A4gMessages.getToast(
      'tst', A4gSeverityMessage.warn, A4gMessages.processoInCorso)));
    this.istruttoriaService.checkProcessiInEsecuzione(checkTipiProcessoInEsecuzione).pipe(
      takeUntil(this.componentDestroyed$),
      switchMap(inEsecuzione => iif(
        () => inEsecuzione, showMessage$, avvia$))).subscribe();
  }
  
  ngOnDestroy() {
    this.loaderService.resetTimeout();
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }
}
