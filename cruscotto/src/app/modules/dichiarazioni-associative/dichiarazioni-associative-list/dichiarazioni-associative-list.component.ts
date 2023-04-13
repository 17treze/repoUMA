import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { AnagraficaFascicoloService } from '../../../../app/shared/services/anagrafica-fascicolo.service';
import { TranslateService } from '@ngx-translate/core';
import { DichiarazioneAssociativaDto, OrganizzazioneDto } from '../models/organizzazione';
import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng-lts';
import { A4gMessages, A4gSeverityMessage } from '../../../../app/shared/a4g-messages';
import { catchError, takeUntil } from 'rxjs/operators';
import { EMPTY, Subject } from 'rxjs';

@Component({
  selector: 'app-dichiarazioni-associative-list',
  templateUrl: './dichiarazioni-associative-list.component.html'
})
export class DichiarazioniAssociativeListComponent implements OnInit {

  public dichiarazioni: DichiarazioneAssociativaDto[];
  public dichiarazione: DichiarazioneAssociativaDto = new DichiarazioneAssociativaDto();
  public selectedDichiarazione: DichiarazioneAssociativaDto;
  public cols: any[];
  public isNewDichiarazione: boolean;
  public displayDialog: boolean;
  public organizzazioni: OrganizzazioneDto[];
  private cuaaFascicolo: string;
  protected componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    protected messageService: MessageService,
    protected translateService: TranslateService,
    protected anagraficaFascicoloService: AnagraficaFascicoloService,
    protected route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.cuaaFascicolo = this.route.parent.snapshot.paramMap.get('cuaa');
    this.setCols();
    this.setDichiarazioni();
  }

  private setCols() {
    this.cols = [
      { field: 'organizzazione', subfield: 'denominazione', header: this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.PROPERTY.ORGANIZZAZIONE') },
      { field: 'dataInizioAssociazione', type: 'date', header: this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.PROPERTY.DT_INIZIO') },
      { field: 'dataFineAssociazione', type: 'date', header: this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.PROPERTY.DT_FINE') },
      { field: 'dataCancellazioneAssociazione', header: this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.PROPERTY.STATO') }
    ];
  }

  private setDichiarazioni() {
    this.anagraficaFascicoloService.getDichiarazioniAssociative(this.cuaaFascicolo)
      .pipe(
        takeUntil(this.componentDestroyed$),
        catchError(
          err => {
            if (err.status === 403) {
              this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('HttpErrorMessages.403')));
            } else {
              this.messageService.add(A4gMessages.getToast(
                'tst',
                A4gSeverityMessage.error,
                this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.MSG_TOAST.GET_ELENCO_DICHIARAZIONI_ERROR')
              ));
            }
            return EMPTY;
          }
        )
      ).subscribe(res => {
        this.dichiarazioni = res.sort((a, b) => a.dataInserimentoAssociazione < b.dataInserimentoAssociazione ? 1 : -1);
      });
  }

  public onRowSelect(event) {
    if (event.data.dataFineAssociazione || event.data.dataCancellazioneAssociazione) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.MSG_TOAST.CLOSE_WARNING')));
    } else {
      this.isNewDichiarazione = false;
      this.dichiarazione = this.cloneDichiarazione(event.data);
      this.displayDialog = true;
    }
  }

  private cloneDichiarazione(c: DichiarazioneAssociativaDto): DichiarazioneAssociativaDto {
    const dichiarazione = new DichiarazioneAssociativaDto();
    for (let prop in c) {
      dichiarazione[prop] = c[prop];
    }
    return dichiarazione;
  }

  public showDialogToAdd() {
    this.anagraficaFascicoloService.getAllOrganizzazioni()
      .pipe(
        takeUntil(this.componentDestroyed$),
        catchError(
          err => {
            this.messageService.add(A4gMessages.getToast(
              'tst',
              A4gSeverityMessage.error,
              this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.MSG_TOAST.GET_ELENCO_ORGANIZZAZIONI_ERROR')
            ));
            return EMPTY;
          }
        )
      ).subscribe(res => {
        this.organizzazioni = res;
        this.isNewDichiarazione = true;
        this.dichiarazione = new DichiarazioneAssociativaDto();
        this.displayDialog = true;
      });
  }

  public save(event) {
    if (event.isNewDichiarazione) {
      // Inserimento nuova dichiarazione
      this.anagraficaFascicoloService.putRlAggiungiDichiarazioneAssociativa(event.dichiarazione, this.cuaaFascicolo)
        .pipe(
          takeUntil(this.componentDestroyed$),
          catchError(
            (err: HttpErrorResponse) => {
              if (err.status === 403) {
                this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('HttpErrorMessages.403')));
              } else if (err.status === 302) {
                this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, err.error.message));
              } else {
                this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.MSG_TOAST.INSERT_ERROR')));
              }
              return EMPTY;
            }
          )
        ).subscribe(res => {
          this.messageService.add(A4gMessages.getToast(
            'tst',
            A4gSeverityMessage.success,
            this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.MSG_TOAST.INSERT_SUCCESS')
          ));
          this.closeDialog(false);
          this.setDichiarazioni();
        });
    } else {
      // Chiusura dichiarazione esistente
      this.anagraficaFascicoloService.putRlChiudiDichiarazioneAssociativa(event.dichiarazione, this.cuaaFascicolo)
        .pipe(
          takeUntil(this.componentDestroyed$),
          catchError(
            err => {
              if (err.status === 403) {
                this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('HttpErrorMessages.403')));
              } else {
                this.messageService.add(A4gMessages.getToast(
                  'tst',
                  A4gSeverityMessage.error,
                  this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.MSG_TOAST.CLOSE_ERROR')
                ));
              }
              return EMPTY;
            }
          )
        ).subscribe(res => {
          this.messageService.add(A4gMessages.getToast(
            'tst',
            A4gSeverityMessage.success,
            this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.MSG_TOAST.INSERT_SUCCESS')
          ));
          this.closeDialog(false);
          this.setDichiarazioni();
        });
    }
  }

  public closeDialog(event: boolean) {
    this.displayDialog = event;
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

}
