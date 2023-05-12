import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng-lts';
import { EMPTY, of } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { PaginatorA4G } from 'src/app/a4g-common/interfaces/paginator.model';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { PaginatorEvent, Paginazione, SortDirection } from 'src/app/a4g-common/utility/paginazione';
import { GruppoLavorazioneDto, LavorazioneDto } from 'src/app/uma/core-uma/models/dto/ConfigurazioneDto';
import { TipologiaLavorazioneEnum } from 'src/app/uma/core-uma/models/enums/TipologiaLavorazione.enum';
import { UnitaMisura } from 'src/app/uma/core-uma/models/enums/UnitaMisura.enum';
import { HttpClientConfigurazioneUmaService } from 'src/app/uma/core-uma/services/http-client-configurazione-uma.service';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';

@Component({
  selector: 'app-lavorazioni',
  templateUrl: './lavorazioni.component.html',
  styleUrls: ['./lavorazioni.component.css']
})
export class LavorazioniComponent implements OnInit {

  lavorazioni: PaginatorA4G<Array<LavorazioneDto>>;;
  lavorazione: LavorazioneDto;
  gruppiLavorazione: GruppoLavorazioneDto[];
  cols: any;
  displayDialog: boolean;
  newLavorazione: boolean;
  selectedLavorazione: LavorazioneDto;
  elementiPagina = 10;
  sortBy: string = 'nome';
  sortDirection: SortDirection;
  unitaMisuraEnum = Object.keys(UnitaMisura).map(key => ({ label: UnitaMisura[key], value: key }));
  selectedUnitaMisura: any;
  tipologiaLavorazioneEnum = Object.keys(TipologiaLavorazioneEnum).map(key => ({ label: TipologiaLavorazioneEnum[key], value: key }));
  selectedTipologiaLavorazione: any;

  constructor(
    private messageService: MessageService,
    private httpClientConfigurazioneUmaService: HttpClientConfigurazioneUmaService,
    private translateService: TranslateService,
    private errorService: ErrorService
  ) { }

  ngOnInit() {
    this.setCols();
    this.lavorazioni = {} as PaginatorA4G<Array<LavorazioneDto>>;
    this.lavorazioni.count = 0;
    this.lavorazioni.risultati = [];
    this.getGruppiLavorazione();
  }

  private setCols() {
    this.cols = [
      { field: 'indice', header: 'Indice' },
      { field: 'nome', header: 'Nome' },
      { field: 'tipologia', header: 'Tipologia' },
      { field: 'unitaDiMisura', header: 'Unità di misura' },
      { field: 'gruppoLavorazione', header: 'Gruppo lavorazione' }
    ];
  }

  private getGruppiLavorazione() {
    let paginazione: Paginazione = Paginazione.of(0, 1000, 'nome', SortDirection.ASC);

    this.httpClientConfigurazioneUmaService.getGruppiLavorazioni(paginazione).subscribe({
      next: value => this.gruppiLavorazione = value.risultati,
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

  canSave(lavorazione: LavorazioneDto) {
    return lavorazione.gruppoLavorazione && lavorazione.indice && lavorazione.nome && lavorazione.tipologia && lavorazione.unitaDiMisura;
  }

  showDialogToAdd() {
    this.selectedUnitaMisura = null;
    this.selectedTipologiaLavorazione = null;
    this.newLavorazione = true;
    this.lavorazione = {} as LavorazioneDto;
    this.lavorazione.id = null;
    this.displayDialog = true;
  }

  save() {
    this.lavorazione.unitaDiMisura = (this.selectedUnitaMisura && this.selectedUnitaMisura.value) ? this.selectedUnitaMisura.value : null;
    this.lavorazione.tipologia = (this.selectedTipologiaLavorazione && this.selectedTipologiaLavorazione.value) ? this.selectedTipologiaLavorazione.value : null;
    if (this.canSave(this.lavorazione)) {
      this.httpClientConfigurazioneUmaService.postLavorazione(this.lavorazione)
        .subscribe({
          next: resp => {
            this.messageService.add(A4gMessages.getToast('tst-lav', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOK));
            this.changePage(null);
            this.lavorazione = null;
            this.displayDialog = false;
          },
          error: e => this.messageService.add(A4gMessages.getToast('tst-lav', A4gSeverityMessage.error, e))
        })
    } else
      this.messageService.add(A4gMessages.getToast('tst-lav', A4gSeverityMessage.warn, UMA_MESSAGES.mandatoryAll));
  }

  onRowSelect(event) {
    this.newLavorazione = false;
    this.lavorazione = this.cloneLavorazione(event.data);
    this.displayDialog = true;
  }

  cloneLavorazione(c: LavorazioneDto): LavorazioneDto {
    var lavorazione = {} as LavorazioneDto;
    for (let prop in c) {
      lavorazione[prop] = c[prop];
    }
    this.selectedUnitaMisura = this.unitaMisuraEnum.find(x => x.value === lavorazione.unitaDiMisura);
    this.selectedTipologiaLavorazione = this.tipologiaLavorazioneEnum.find(x => x.value === lavorazione.tipologia);
    return lavorazione;
  }

  changePage(event: PaginatorEvent) {
    if (event != null) {
      this.sortDirection = event.sortOrder === 1 ? SortDirection.ASC : SortDirection.DESC;
      this.sortBy = event.sortField || this.sortBy;
    }
    let paginazione: Paginazione = Paginazione.of(
      0, this.elementiPagina, this.sortBy, this.sortDirection || SortDirection.ASC
    );

    this.httpClientConfigurazioneUmaService.getLavorazioni(paginazione)
      .pipe(switchMap((res: PaginatorA4G<Array<LavorazioneDto>>) => {
        return of(res);
      }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-gruppi-lav');
          return EMPTY;
        })
      )
      .subscribe((result: PaginatorA4G<Array<LavorazioneDto>>) => {
        this.lavorazioni = result;
      }, error => this.errorService.showError(error, 'tst-gruppi-lav'));
  }

}
