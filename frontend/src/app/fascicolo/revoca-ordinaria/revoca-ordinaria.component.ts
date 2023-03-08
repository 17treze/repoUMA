import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormGroup, NgForm, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { MessageService } from 'primeng/api';

import { AnagraficaFascicoloService } from '../creazione-fascicolo/anagrafica-fascicolo.service';
import { CreazioneFascicoloService } from '../creazione-fascicolo/shared/crea-fascicolo.service';
import { filter } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { TranslateService } from '@ngx-translate/core';
import { DatiAperturaFascicoloDto } from '../creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { Labels } from 'src/app/app.labels';
import { MandatoService } from '../mandato.service';

@Component({
  selector: 'app-revoca-ordinaria',
  templateUrl: './revoca-ordinaria.component.html',
  styleUrls: ['./revoca-ordinaria.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class RevocaOrdinariaComponent implements OnInit {

  public labels = Labels;
  revocaForm: FormGroup;
  previousUrl: string;

  constructor(
    private messageService: MessageService,
    private route: ActivatedRoute,
    private router: Router,
    private mandatoService: MandatoService,
    private translateService: TranslateService
  ) { }

  ngOnInit() {
    this.determinePreviousPath();
    this.revocaForm = new FormGroup({
      cuaa: new FormControl(null, [Validators.required])
    });
  }

  private determinePreviousPath() {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        console.log('prev:', event.url);
        this.previousUrl = event.url;
      });
  }

  onSubmit($event: Event) {
    /*     this.anagraficaFascicoloService.getFascicoloByCuaa(f.value.cuaa).subscribe(fascicolo => {
          if (fascicolo) {
            // router link alla nuova comp
            this.router.navigate(['/funzioniPat/revocaOrdinaria/' + f.value.cuaa], { replaceUrl: true });
          } else {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.noFascicolo));
          }
        }); */
    // In assenza del metodo per il reperimento del fascicolo da A4G temporaneamente accediamo direttamente alla pagina di revoca
    // e recuperiamo i dati allo stesso modo della creazione del fascicolo
    this.mandatoService.verificaMandato(this.revocaForm.get('cuaa').value).subscribe((res: DatiAperturaFascicoloDto) => {
      console.log('DATIAPERTURAFASCICOLO', res);
      this.router.navigate(['./revocaOrdinaria/' + this.revocaForm.get('cuaa').value], { relativeTo: this.route.parent.parent });
    }, (error) => {
      this.messageService.add(
        A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('EXC_REVOCA_MANDATO.' + error.error.message)));
    });
  }
}
