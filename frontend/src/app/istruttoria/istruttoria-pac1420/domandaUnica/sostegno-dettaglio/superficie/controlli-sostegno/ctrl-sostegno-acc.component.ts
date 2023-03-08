import { Component, OnInit } from '@angular/core';
import { Costanti } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/Costanti';
import { MenuItem, MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { ActivatedRoute } from '@angular/router';
import { Subject, empty } from 'rxjs';
import { takeUntil, catchError } from 'rxjs/operators';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';

@Component({
  selector: 'app-ctrl-sostegno-acc',
  templateUrl: './ctrl-sostegno-acc.component.html',
  styleUrls: ['./ctrl-sostegno-acc.component.css']
})
export class CtrlSostegnoAccComponent implements OnInit {
  public cols: any[];
  public controlliSostegnoErrors: MenuItem[] = [];
  public controlliSostegnoWarnings: MenuItem[] = [];
  public controlliSostegnoInfos: MenuItem[] = [];
  public controlliSostegnoSuccesses: MenuItem[] = [];
  public isControlliSostegnoPresent: boolean = false;
  
  private idIstruttoriaCorrente: number;
  private componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    private istruttoriaDettaglioService: IstruttoriaDettaglioService,
    private messageService: MessageService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.setCols();
    this.setIdIstruttoriaCorrente();
    this.getControlliSostegno(this.idIstruttoriaCorrente.toString());
  }

  private setCols() {
    this.cols = [
      { field: 'b', header: '', width: '100%' }
    ];
  }

  private setIdIstruttoriaCorrente() {
    this.route.params.subscribe(params => {
      this.idIstruttoriaCorrente = params['idIstruttoria'];
    });
  }

  private getControlliSostegno(idIstruttoria) {
    this.istruttoriaDettaglioService.getEsitiControlloSostegno(idIstruttoria).pipe(
      takeUntil(this.componentDestroyed$),
      catchError(err => {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        return empty();
      })
    ).subscribe(controlliSostegno => {
      this.isControlliSostegnoPresent = true;
      controlliSostegno.errors.forEach(msgString => this.controlliSostegnoErrors.push({
        label: msgString,
        title: Costanti[msgString]
      }));
      controlliSostegno.infos.forEach(msgString => this.controlliSostegnoInfos.push({
        label: msgString,
        title: Costanti[msgString]
      }));
      controlliSostegno.successes.forEach(msgString => this.controlliSostegnoSuccesses.push({
        label: msgString,
        title: Costanti[msgString]
      }));
      controlliSostegno.warnings.forEach(msgString => this.controlliSostegnoWarnings.push({
        label: msgString,
        title: Costanti[msgString]
      }));
    });
  }

  checkValidDataTitle(menus: MenuItem[]) {
    return menus.some(e => !!e.title);
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }
}