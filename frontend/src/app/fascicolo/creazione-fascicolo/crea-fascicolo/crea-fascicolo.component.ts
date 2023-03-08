import { Router, ActivatedRoute } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Labels } from 'src/app/app.labels';
import { MessageService } from 'primeng/api';
import { NgForm } from '@angular/forms';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { AnagraficaFascicoloService } from '../anagrafica-fascicolo.service';
import { CreazioneFascicoloService } from '../shared/crea-fascicolo.service';
import { TranslateService } from '@ngx-translate/core';
import { Subject } from 'rxjs';
import { IMPORTA_FASCICOLO_STATO } from './importa-fascicolo-v2/importa-fascicolo-v2.component';

@Component({
  selector: 'app-crea-fascicolo',
  templateUrl: './crea-fascicolo.component.html',
  styleUrls: ['./crea-fascicolo.component.css']
})
export class CreaFascicoloComponent implements OnInit, OnDestroy {
  labels = Labels;
  cuaa: string;
  ctx: IMPORTA_FASCICOLO_STATO.APRI | IMPORTA_FASCICOLO_STATO.APRI_TRASFERISCI  | IMPORTA_FASCICOLO_STATO.TRASFERISCI;
  public title: string;
  public titleBtn: string;
  private componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
      private messageService: MessageService,
      private route: ActivatedRoute,
      private router: Router,
      private anagraficaFascicoloService: AnagraficaFascicoloService,
      private creazioneFascicoloService: CreazioneFascicoloService,
      private translateService: TranslateService) {
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }
  
  ngOnInit() {
    if (this.router.url.indexOf('/crea') > - 1) {
      this.ctx = IMPORTA_FASCICOLO_STATO.APRI;
    } else if (this.router.url.indexOf('/costituisci-trasferisci') > - 1) {
      this.ctx = IMPORTA_FASCICOLO_STATO.APRI_TRASFERISCI;
    } else if (this.router.url.indexOf('/trasferisci') > - 1) {
      this.ctx = IMPORTA_FASCICOLO_STATO.TRASFERISCI;
    } else {
      this.router.navigate(['crea'], { relativeTo: this.route });
    }
    switch (this.ctx) {
      case IMPORTA_FASCICOLO_STATO.APRI_TRASFERISCI:
        this.title = Labels.costituisciTrasferisciFascicolo.toUpperCase()
        this.titleBtn = Labels.btnCostituisciTrasferisciFascicolo;
        break;
      case IMPORTA_FASCICOLO_STATO.TRASFERISCI:
          this.title = Labels.trasferisciFascicolo.toUpperCase();
          this.titleBtn = Labels.trasferisciFascicolo;
          break;
      default:
        this.title = Labels.creazioneNuovoFascicolo.toUpperCase();
        this.titleBtn = Labels.creazioneNuovoFascicolo;
    }
  }

  onSubmit(f: NgForm) {
    let callVerifica;
    if (this.ctx === IMPORTA_FASCICOLO_STATO.APRI) {
      callVerifica = this.anagraficaFascicoloService.getVerificaAperturaFascicolo(f.value.cuaa);
    } else if (this.ctx === IMPORTA_FASCICOLO_STATO.APRI_TRASFERISCI) {
      callVerifica = this.anagraficaFascicoloService.getVerificaAperturaTrasferimentoFascicolo(f.value.cuaa);
    } else { //TRASFERIMENTO FASCICOLO
      callVerifica = this.anagraficaFascicoloService.getVerificaTrasferimentoFascicolo(f.value.cuaa);
    }
    callVerifica.subscribe(response => {
      this.creazioneFascicoloService.anagraficaTributaria = response;
      this.router.navigate([f.value.cuaa], { relativeTo: this.route });
    }, error => {
      if (error.error.message &&
        this.translateService.instant('EXC_APRI_FASCICOLO.' + error.error.message) != 'EXC_APRI_FASCICOLO.' + error.error.message) {
          this.messageService.add(A4gMessages.getToast(
            'tst',
            A4gSeverityMessage.error,
            this.translateService.instant('EXC_APRI_FASCICOLO.' + error.error.message)));
      } else {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
      }
    });
  }
}