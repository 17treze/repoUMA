import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng-lts';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { GruppoLavorazioneDto } from 'src/app/uma/core-uma/models/dto/ConfigurazioneDto';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { EMPTY, of } from 'rxjs';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { HttpClientConfigurazioneUmaService } from 'src/app/uma/core-uma/services/http-client-configurazione-uma.service';
import { PaginatorA4G, PaginatorEvent } from 'src/app/a4g-common/interfaces/paginator.model';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { Paginazione, SortDirection } from 'src/app/a4g-common/utility/paginazione';
import { catchError, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-gruppi-lavorazione',
  templateUrl: './gruppi-lavorazione.component.html',
  styleUrls: ['./gruppi-lavorazione.component.css']
})
export class GruppiLavorazioneComponent implements OnInit {

  gruppiLavorazione: PaginatorA4G<Array<GruppoLavorazioneDto>>;
  gruppoLavorazione: GruppoLavorazioneDto;
  display: boolean;
  cols: any;
  displayDialog: boolean;
  newGruppoLavorazione: boolean;
  selectedGruppoLavorazione: GruppoLavorazioneDto;
  elementiPagina = 10;
  sortBy: string = 'nome';
  sortDirection: SortDirection;

  constructor(private messageService: MessageService,
    private errorService: ErrorService,
    private httpClientConfigurazioneUmaService: HttpClientConfigurazioneUmaService
  ) { }

  ngOnInit() {
    this.setCols();
    this.display = true;
    this.gruppiLavorazione = {} as PaginatorA4G<Array<GruppoLavorazioneDto>>;
    this.gruppiLavorazione.count = 0;
    this.gruppiLavorazione.risultati = [];
  }

  private setCols() {
    this.cols = [
      { field: 'indice', header: 'Indice' },
      { field: 'nome', header: 'Nome' },
      { field: 'ambito', header: 'Ambito' },
      { field: 'anno_inizio', header: 'Anno inizio' },
      { field: 'anno_fine', header: 'Anno fine' }
    ];
  }

  canSave(lavorazione: GruppoLavorazioneDto) {
    return lavorazione.ambitoLavorazione && lavorazione.annoFine && lavorazione.annoInizio && lavorazione.indice && lavorazione.nome;
  }

  showDialogToAdd() {
    this.newGruppoLavorazione = true;
    this.gruppoLavorazione = {} as GruppoLavorazioneDto;
    this.displayDialog = true;
  }

  save() {
    // TODO: aggiungere chiamata per salvataggio nuovo record o aggiornmento record esistente
    if (this.canSave(this.gruppoLavorazione)) {
      let coefficienti = [...this.gruppiLavorazione.risultati];
      if (this.newGruppoLavorazione)
        coefficienti.push(this.gruppoLavorazione);
      else
        coefficienti[this.gruppiLavorazione.risultati.indexOf(this.selectedGruppoLavorazione)] = this.gruppoLavorazione;
      this.messageService.add(A4gMessages.getToast('tst-gruppi-lav', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOK));
      this.gruppiLavorazione.risultati = coefficienti;
      this.gruppoLavorazione = null;
      this.displayDialog = false;
    } else
      this.messageService.add(A4gMessages.getToast('tst-gruppi-lav', A4gSeverityMessage.warn, UMA_MESSAGES.mandatoryAll));
  }

  onRowSelect(event) {
    this.newGruppoLavorazione = false;
    this.gruppoLavorazione = this.cloneCoefficiente(event.data);
    this.displayDialog = true;
  }

  cloneCoefficiente(c: GruppoLavorazioneDto): GruppoLavorazioneDto {
    var coefficiente = {} as GruppoLavorazioneDto;
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
      event.first / this.elementiPagina, this.elementiPagina, this.sortBy, this.sortDirection || SortDirection.ASC
    );

    this.httpClientConfigurazioneUmaService.getGruppiLavorazioni(paginazione)
      .pipe(switchMap((res: PaginatorA4G<Array<GruppoLavorazioneDto>>) => {
        return of(res);
      }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-gruppi-lav');
          return EMPTY;
        })
      )
      .subscribe((result: PaginatorA4G<Array<GruppoLavorazioneDto>>) => {
        this.gruppiLavorazione = result;
      }, error => this.errorService.showError(error, 'tst-gruppi-lav'));
  }

}
