import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DomandeUmaService } from './domande-uma.service';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng-lts';
import { DomandaUma } from './models/domanda-uma';
import { catchError } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { StatoRichiesta } from './models/stato-richiesta';
import { AziendaAgricolaService } from 'src/app/shared/services/azienda-agricola.service';
import { DateService } from 'src/app/shared/utilities/date.service';
import { DomandeUmaPaginate } from './models/domande-uma-paginate';
import { environment } from 'src/environments/environment';
import { DateUtilService } from 'src/app/shared/services/date-util.service';

@Component({
  selector: 'app-domande-uma',
  templateUrl: './domande-uma.component.html',
  styleUrls: ['./domande-uma.component.css'],
})
export class DomandeUmaComponent implements OnInit {
  constructor(
    private umaService: DomandeUmaService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private messageService: MessageService,
    private aziendaAgricolaService: AziendaAgricolaService,
    private dateService: DateService,
    private dateUtilService: DateUtilService
  ) {}

  cuaa = '';
  companyName?: string;
  domande?: DomandaUma[];
  swiping = false;

  ngOnInit() {
    this.cuaa = this.activatedRoute.snapshot.paramMap.get('cuaa');
    this.fetchDomandeUma();
  }

  fetchCompanyName() {
    this.aziendaAgricolaService.getSelectedCuaa(this.cuaa).subscribe(company => {
      this.companyName = company ? company.denominazione : '';
    });
  }

  fetchDomandeUma() {
    const campagne = this.dateUtilService.getListYearsFrom(environment.annoInizioCampagnaUma);
    this.umaService
      .getDomandeUma(this.cuaa, [StatoRichiesta.AUTORIZZATA], campagne)
      .pipe(catchError(async (error) => {
        this.showError();
        throw error.status;
      }))
      .subscribe((domandePaginate: DomandeUmaPaginate) => {
        const domande: DomandaUma[] = domandePaginate.risultati;
        if (!domande || !domande.length) {
          this.showError();
          return;
        }

        this.domande = this.sortUmaApplicationsByPresentationDate(domande);
        if (domande && domande.length) {
          this.companyName = domande[0].denominazione;
        }

        if (!this.companyName) {
          this.fetchCompanyName();
        }
      });
  }

  showError() {
    this.messageService.add(
      A4gMessages.getToast(
        'toast',
        A4gSeverityMessage.error,
        A4gMessages.erroreRecuperoDomandeUma
      )
    );
  }

  sortUmaApplicationsByPresentationDate(applications: DomandaUma[]): DomandaUma[] {
    return applications.sort((a, b) => {
      return (
        this.dateService.getDateTimeFromString(b.dataPresentazione) -
        this.dateService.getDateTimeFromString(a.dataPresentazione)
      );
    });
  }

  gotoDetails(domanda: DomandaUma) {
    if (this.swiping) return;
    this.router.navigate(['./domande-uma/' + domanda.id + '/dettaglio']);
  }
  
  onSwipeMove(event: any) {
    this.swiping = true;
  }
  onSwipeEnd(event: any) {
    this.swiping = false;
  }
}
