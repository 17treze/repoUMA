import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {MessageService} from "primeng-lts";
import {ActivatedRoute, Router} from "@angular/router";
import {CreazioneFascicoloService} from "../../creazione-fascicolo/shared/crea-fascicolo.service";
import {TranslateService} from "@ngx-translate/core";
import {NgForm} from "@angular/forms";
import {A4gMessages, A4gSeverityMessage} from "../../../a4g-common/a4g-messages";
import {MandatoService} from "../../mandato.service";

@Component({
  selector: 'app-ricerca-fascicolo-per-acquisizione-mandato',
  templateUrl: './ricerca-fascicolo-per-acquisizione-mandato.component.html',
  styleUrls: ['./ricerca-fascicolo-per-acquisizione-mandato.component.css']
})
export class RicercaFascicoloPerAcquisizioneMandatoComponent implements OnInit, OnDestroy {
  cuaa: string;
  private componentDestroyed$: Subject<boolean> = new Subject();

  constructor(private messageService: MessageService,
              private route: ActivatedRoute,
              private router: Router,
              private mandatoService: MandatoService,
              private creazioneFascicoloService: CreazioneFascicoloService,
              private translateService: TranslateService) { }

  ngOnInit() {
  }

  onSubmit(f: NgForm) {
    this.mandatoService.getVerificaAcquisizioneMandato(f.value.cuaa)
        .subscribe(response => {
        this.creazioneFascicoloService.anagraficaTributaria = response;
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.info, 'Per il cuaa:' + f.value.cuaa + ' e\' possibile acquisire il mandato'  ))
          this.router.navigate(['cuaa/' + f.value.cuaa + '/acquisizione-mandato'], { relativeTo: this.route });
        }, error => {
          if (error.error.message &&
              this.translateService.instant('EXC_MANDATO_VERIFICA_ACQUISIZIONE.' + error.error.message) !== 'EXC_MANDATO_VERIFICA_ACQUISIZIONE.' + error.error.message) {
                this.messageService.add(A4gMessages.getToast(
                    'tst',
                    A4gSeverityMessage.error,
                    this.translateService.instant('EXC_MANDATO_VERIFICA_ACQUISIZIONE.' + error.error.message)));
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
