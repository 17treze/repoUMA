import { Component, OnInit, Input } from '@angular/core';
import { RiepilogoStatoDomande } from '../../../../../dettaglio-istruttoria/RiepilogoStatoDomande';
import { RicevibilitaComponent } from '../ricevibilita.component';
import { Router, ActivatedRoute } from '@angular/router';
import { Costanti } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/Costanti';
import { StatoDomandaEnum } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/dettaglio-istruttoria/statoDomanda';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-stato-domande-ricevibilita',
  templateUrl: './stato-domande-ricevibilita.component.html',
  styleUrls: ['./stato-domande-ricevibilita.component.css']
})
export class StatoDomandeRicevibilitaComponent implements OnInit {

  @Input() riepilogoStatoDomande: RiepilogoStatoDomande;
  @Input() dettaglioIstruttoria: RicevibilitaComponent;

  public annoCampagna: number

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private istruttoriaService: IstruttoriaService,
    private messageService: MessageService
  ) { }

  displayInIstruttoriaDialog = false;

  ngOnInit() {
    this.annoCampagna = this.route.snapshot.params.annoCampagna;
    this.riepilogoStatoDomande;
    this.dettaglioIstruttoria;
  }

  onIstruttoriaDialogClose(event) {
    this.displayInIstruttoriaDialog = event;
  }

  displayDialog() {
    this.displayInIstruttoriaDialog = true;
  }

  goListaDomande(stato: String): void {
    if (stato === StatoDomandaEnum.RICEVIBILE)
      this.router.navigate(['./' + Costanti.domandeRicevibli], { relativeTo: this.route });
    else if (stato === StatoDomandaEnum.NON_RICEVIBILE)
      this.router.navigate(['./' + Costanti.domandeNonRicevibili], { relativeTo: this.route });
    else if (stato === StatoDomandaEnum.IN_ISTRUTTORIA)
      this.router.navigate(['./' + Costanti.domandeInIstruttoria], { relativeTo: this.route });
  }

  avviaRicevibilita() {
    this.istruttoriaService.avviaProcessoRicevibilitaDU(this.annoCampagna)
      .subscribe(
        ok => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.processoRicevibilitaOk));
        },
        err => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.BRIAMPRT003));
        });
  }

  avviaProcessoIstruttoria() {
    this.istruttoriaService.avviaProcessoIstruttoriaDU(this.annoCampagna)
      .subscribe(
        ok => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.processoAvvioIstruttoriaOk));
        },
        err => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.BRIAMPRT003));
        });
  }

}
