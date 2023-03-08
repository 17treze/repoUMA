import {Component, OnDestroy, OnInit} from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng-lts';
import { Subject } from 'rxjs';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { AnagraficaFascicoloService } from '../../creazione-fascicolo/anagrafica-fascicolo.service';
import { CreazioneFascicoloService } from '../../creazione-fascicolo/shared/crea-fascicolo.service';

@Component({
  selector: 'app-ricerca-fascicolo-da-riaprire',
  templateUrl: './ricerca-fascicolo-da-riaprire.component.html',
  styleUrls: ['./ricerca-fascicolo-da-riaprire.component.css']
})
export class RicercaFascicoloDaRiaprireComponent implements OnInit, OnDestroy {

  cuaa: string;
  private componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    private messageService: MessageService,
    private route: ActivatedRoute,
    private router: Router,
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private creazioneFascicoloService: CreazioneFascicoloService,
    private translateService: TranslateService
  ) { }

  ngOnInit() {
  }

  onSubmit(f: NgForm) {
    this.anagraficaFascicoloService.getVerificaRiaperturaFascicolo(f.value.cuaa)
      .subscribe(response => {
        this.creazioneFascicoloService.anagraficaTributaria = response;
        this.router.navigate(['cuaa/' + f.value.cuaa + '/riapri'], { relativeTo: this.route });
      }, error => {
        if (error.error.message &&
          this.translateService.instant('EXC_APRI_FASCICOLO.' + error.error.message) !== 'EXC_APRI_FASCICOLO.' + error.error.message) {
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

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

}
