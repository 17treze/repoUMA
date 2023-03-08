import {Component, OnInit} from '@angular/core';
import {IstruttoriaDettaglioService} from "../../shared/istruttoria-dettaglio.service";
import {PascoloDao} from "../../../domain/pascolo-dao";
import {ActivatedRoute} from "@angular/router";
import {IstruttoriaDomandaUnica} from "../../../classi/IstruttoriaDomandaUnica";
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { IstruttoriaService } from '../../../istruttoria.service';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { MessageService } from 'primeng/api';

@Component({
    selector: 'app-dati-pascolo',
    templateUrl: './dati-pascolo.component.html',
    styleUrls: ['./dati-pascolo.component.scss']
})
export class DatiPascoloComponent implements OnInit {

    private componentDestroyed$: Subject<boolean> = new Subject();
    
    public cols: any[] = [{ header: 'Dati in entrata', width: '70%' }];
    public cols2: any[] = [{ header: 'Dati in uscita', width: '70%' }];
    public cols3: any[] = [{header: 'Anomalie' , width: '70%'}];
    public arrayPascoli: Array<PascoloDao> = null;
    public dati: Array<any> = [{}];
    istruttoriaDUCorrente: IstruttoriaDomandaUnica;

    constructor(private istruttoriaDettaglioService: IstruttoriaDettaglioService,
        private istruttoriaService: IstruttoriaService,
        private messageService: MessageService,
        private route: ActivatedRoute) { }

    ngOnDestroy() {
        this.componentDestroyed$.next(true);
        this.componentDestroyed$.complete();
    }

    ngOnInit() {
        this.istruttoriaDUCorrente = this.route.snapshot.data['domandaIstruttoria'];
        let idIstruttoria: number = this.istruttoriaDUCorrente.id;
        if (idIstruttoria) {
            this.istruttoriaDettaglioService
                .getDatiperPascoloByIdIstruttoriaConEsitoMantenimento(idIstruttoria)
                .pipe(takeUntil(this.componentDestroyed$))
                .subscribe(dati => {
                    
                    if (dati) {
                        dati.sort((one, two) => (one.descPascolo < two.descPascolo ? -1 : 1));
                    }
                    for (let pascolo of dati) {
                        if (pascolo.listaEsitiPascolo) {
                            pascolo.listaEsitiPascolo
                                .sort((a, b) => (a.ordineControllo < b.ordineControllo) ? -1 : 1)
                                .sort((a, b) => (a.ordine1 < b.ordine1) ? -1 : 1);
                        }
                    }
                    this.arrayPascoli = dati;
                });
        }
    }

    aggiornaDatiBDN() {
      if (!this.istruttoriaDUCorrente || !this.istruttoriaDUCorrente.domanda || !this.istruttoriaDUCorrente.domanda.cuaaIntestatario) {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, 'CUAA non presente'));
        return;
      }
        this.istruttoriaService.aggiornaDatiBDN(this.istruttoriaDUCorrente.domanda.campagna, this.istruttoriaDUCorrente.domanda.cuaaIntestatario).subscribe(next => {
          let messaggioAggiornamento: string = "";
          if (next) {
            messaggioAggiornamento = A4gMessages.aggiornamentoDatiBDN_OK;
          } else {
            messaggioAggiornamento = A4gMessages.aggiornamentoDatiBDN_KO;
          }
          this.messageService.add(
            A4gMessages.getToast(
              'tst',
              A4gSeverityMessage.success,
              messaggioAggiornamento
            ));
          //this.runStatoAvanzamentoProcesso(domande.length);
        }, err => {
            console.log(err.error.message);
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.aggiornamentoDatiBDN_Errore));
        });
      }
        
}
