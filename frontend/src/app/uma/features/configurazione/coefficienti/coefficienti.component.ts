import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng-lts';
import { EMPTY, of } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { PaginatorA4G, PaginatorEvent } from 'src/app/a4g-common/interfaces/paginator.model';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { Paginazione, SortDirection } from 'src/app/a4g-common/utility/paginazione';
import { CoefficienteDto } from 'src/app/uma/core-uma/models/dto/ConfigurazioneDto';
import { LavorazioneDto } from 'src/app/uma/core-uma/models/dto/LavorazioneDto';
import { HttpClientConfigurazioneUmaService } from 'src/app/uma/core-uma/services/http-client-configurazione-uma.service';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';

@Component({
  selector: 'app-coefficienti',
  templateUrl: './coefficienti.component.html',
  styleUrls: ['./coefficienti.component.css']
})
export class CoefficientiComponent implements OnInit {

  coefficienti: PaginatorA4G<Array<CoefficienteDto>>;
  coefficiente: CoefficienteDto;
  lavorazioni: LavorazioneDto[];
  cols: any;
  displayDialog: boolean;
  newCoefficiente: boolean;
  selectedCoefficiente: CoefficienteDto;
  elementiPagina = 10;
  sortBy: string = 'coefficiente';
  sortDirection: SortDirection;

  constructor(
    private messageService: MessageService,
    private httpClientConfigurazioneUmaService: HttpClientConfigurazioneUmaService,
    private translateService: TranslateService,
    private errorService: ErrorService) { }

  ngOnInit() {
    this.setCols();
    this.coefficienti = {} as PaginatorA4G<Array<CoefficienteDto>>;
    this.coefficienti.count = 0;
    this.coefficienti.risultati = [];
    this.getLavorazioni();
  }

  private setCols() {
    this.cols = [
      { field: 'coefficiente', header: 'Coefficiente' },
      { field: 'id_lavorazione', header: 'Lavorazione' },
      { field: 'anno_inizio', header: 'Anno inizio' },
      { field: 'anno_fine', header: 'Anno fine' }
    ];
  }

  private getLavorazioni() {
    let paginazione: Paginazione = Paginazione.of(0, 1000, 'nome', SortDirection.ASC);

    this.httpClientConfigurazioneUmaService.getLavorazioni(paginazione).subscribe({
      next: value => this.lavorazioni = value.risultati,
      error: e => {
        if (e.status === 404) {
          this.messageService.add(A4gMessages.getToast('tst-lav', A4gSeverityMessage.error,
            this.translateService.instant('NO_CONTENT')));
        } else {
          this.messageService.add(A4gMessages.getToast('tst-lav', A4gSeverityMessage.error, e));
        }
      }
    });
  }

  canSave(lavorazione: CoefficienteDto) {
    return lavorazione.annoInizio && lavorazione.coefficiente && lavorazione.lavorazione;
  }

  showDialogToAdd() {
    this.newCoefficiente = true;
    this.coefficiente = {} as CoefficienteDto;
    this.displayDialog = true;
  }

  save() {
    if (this.canSave(this.coefficiente)) {
      this.httpClientConfigurazioneUmaService.postCoefficiente(this.coefficiente)
        .subscribe({
          next: resp => {
            this.messageService.add(A4gMessages.getToast('tst-coeff', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOK));
            this.changePage(null);
            this.coefficiente = null;
            this.displayDialog = false;
          },
          error: e => this.messageService.add(A4gMessages.getToast('tst-coeff', A4gSeverityMessage.error, e))
        })
    } else
      this.messageService.add(A4gMessages.getToast('tst-coeff', A4gSeverityMessage.warn, UMA_MESSAGES.mandatoryAll));
  }

  onRowSelect(event) {
    this.newCoefficiente = false;
    this.coefficiente = this.cloneCoefficiente(event.data);
    this.displayDialog = true;
  }

  cloneCoefficiente(c: CoefficienteDto): CoefficienteDto {
    var coefficiente = {} as CoefficienteDto;
    for (let prop in c) {
      coefficiente[prop] = c[prop];
    }
    return coefficiente;
  }

  changePage(event: PaginatorEvent) {
    if (event != null) {
      this.sortDirection = event.sortOrder === 1 ? SortDirection.ASC : SortDirection.DESC;
      this.sortBy = event.sortField || this.sortBy;
    }
    let paginazione: Paginazione = Paginazione.of(
      0, this.elementiPagina, this.sortBy, this.sortDirection || SortDirection.ASC
    );

    this.httpClientConfigurazioneUmaService.getCoefficienti(paginazione)
      .pipe(switchMap((res: PaginatorA4G<Array<CoefficienteDto>>) => {
        return of(res);
      }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-coeff');
          return EMPTY;
        })
      )
      .subscribe((result: PaginatorA4G<Array<CoefficienteDto>>) => {
        this.coefficienti = result;
      }, error => this.errorService.showError(error, 'tst-coeff'));
  }

}
