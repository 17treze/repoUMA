import { Component, OnInit, Input } from '@angular/core';
import { A4gMessages } from 'src/app/a4g-common/a4g-messages';
import { Labels } from 'src/app/app.labels';
import { ProcessiDiControllo } from '../dto/processi-di-controllo';
import { IstruttoriaService } from '../../istruttoria.service';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { ProcessRepeatingServiceCall } from '../repeating-service-call';

@Component({
  selector: 'app-processi-di-controllo',
  templateUrl: './processi-di-controllo.component.html',
  styleUrls: ['./processi-di-controllo.component.css']
})
export class ProcessiDiControlloComponent implements OnInit {

  @Input() processiDaControllare: string[];

  public interval: any;
  public label = Labels.NESSUN_PROCESSO_DI_CONTROLLO;
  public processi: Array<ProcessiDiControllo> = [];
  private repeatingServiceCallSubscription: Subscription;

  constructor(
    private istruttoriaService: IstruttoriaService, 
    private translateService: TranslateService,
    private repeatingServiceCall: ProcessRepeatingServiceCall) {}

  ngOnInit() {
    this.repeatingServiceCallSubscription = this.repeatingServiceCall.observable$
      .subscribe(val => this.getProcessiDiControllo(val));
  }

  ngOnDestroy() {
    clearInterval(this.interval);
    this.repeatingServiceCall.stop();
    this.repeatingServiceCallSubscription.unsubscribe();
  }

  private getProcessiDiControllo(timerIteration: number) {
    this.istruttoriaService.getProcessiDiControllo(null).subscribe(processiRun => {
      if (processiRun) {
        this.processi = [];
        processiRun.forEach(element => {
          if (this.processiDaControllare.includes(element.tipoProcesso)) {
            this.processi.push(element);
          }
        });
      }
      if (!processiRun || this.processi.length === 0) {
        console.log(timerIteration);
        //l'iterazione ZERO è la prima iterazione di quando entra nella pagina. 
        //In questo caso non è necessario fare il refresh della pagine con relativi contatori
        if (timerIteration > 0) {
          this.repeatingServiceCall.stopAndRefresh();
        } else {
          this.repeatingServiceCall.stop();
        }
      }
    }, error => {
      console.log(error);
    });
  };

  public getCountAvanzamentoProcessi(processo: ProcessiDiControllo) {
    const parzialeIstruttorieGestite = processo.datiElaborazioneProcesso.parziale || '0';
    return A4gMessages.procesioDiControlloIstruttoria(String(this.translateService.instant("PROCESSI." + processo.tipoProcesso)), String(parzialeIstruttorieGestite), String(processo.datiElaborazioneProcesso.totale  || '0'));
  }

}
 