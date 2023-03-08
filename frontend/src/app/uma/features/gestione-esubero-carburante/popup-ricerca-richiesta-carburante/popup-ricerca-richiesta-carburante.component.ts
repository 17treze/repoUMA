import { FormService } from '../../../../a4g-common/services/form.service';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { Component, EventEmitter, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { Table } from 'primeng/table';
import { Dialog } from 'primeng/dialog';
import { Subscription } from 'rxjs';
import { PaginatorEvent } from 'src/app/a4g-common/interfaces/paginator.model';
import { FormControl, FormGroup } from '@angular/forms';
import { HttpClientTrasferimentiCarburanteService } from 'src/app/uma/core-uma/services/http-client-trasferimenti-carburante.service';

import { AziendaDto } from 'src/app/uma/core-uma/models/dto/AziendaDto';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { MessageService } from 'primeng-lts';
import { errorMonitor } from 'events';

@Component({
  selector: 'app-popup-ricerca-richiesta-carburante',
  templateUrl: './popup-ricerca-richiesta-carburante.component.html',
  styleUrls: ['./popup-ricerca-richiesta-carburante.component.scss']
})
export class PopupRicercaRichiestaCarburanteComponent implements OnInit, OnDestroy {
  datasource: Array<AziendaDto>;
  display: boolean;
  idRichiestaMittente: string;
  totalElements: number;
  searchForm: FormGroup;

  @ViewChild('dialog', { static: true })
  dialogElement: Dialog;
  @ViewChild('table') table: Table;
  @Output() chiudiPopup = new EventEmitter<any>();

  // Subscriptions
  validaSubscription: Subscription;

  constructor(
    private trasferimentiService: HttpClientTrasferimentiCarburanteService,
    private errorService: ErrorService,
    private formService: FormService,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.searchForm = new FormGroup({
      idRichiestaDestinatario: new FormControl(null)
    });
  }

  ngOnDestroy() {
    if (this.validaSubscription) {
      this.validaSubscription.unsubscribe();
    }
  }

  open(idRichiestaMittente: string) {
    this.display = true;
    this.searchForm.get('idRichiestaDestinatario').setValue(null);
    this.datasource = null;
    this.idRichiestaMittente = idRichiestaMittente;
  }

  onHideDialog() {
  }

  chiudiDialog($event?: Event) {
    this.display = false;
    this.datasource = null;
    this.dialogElement._style.width = 'unset'; // reset width dialog
  }

  onClickButton(tipo: String, cuaa: string, denominazione: string) {
    switch (tipo) {
      case "CERCA": {
        this.cercaRichiesta(this.searchForm.get('idRichiestaDestinatario').value);
        break;
      }
      case "AGGIUNGI": {
        this.addTrasferimento(this.searchForm.get('idRichiestaDestinatario').value, cuaa, denominazione);
        break;
      }
      default: {
        break;
      }
    }
  }

  cercaRichiesta(idRichiestaDestinatario: string) {
    this.formService.validateForm(this.searchForm);
    if (!this.searchForm.valid) {
      this.messageService.add(A4gMessages.getToast('tst-ricerca-richiesta-carburante', A4gSeverityMessage.error, UMA_MESSAGES.datiNotCorretti));
      return;
    }
    this.validaSubscription = this.trasferimentiService.validaTrasferimento(this.idRichiestaMittente, idRichiestaDestinatario)
      .subscribe(
        (aziendaDto: AziendaDto) => {
          this.dialogElement._style.width = '80vw'; // set 80% screen width dialog
          this.datasource = [];
          this.datasource.push(aziendaDto);
          this.totalElements = 1;
        },
        (error) => {
          if (error && error.error && error.error.message && error.error.message.startsWith('Non è possibile modificare')) { //TODO: cambiare il messaggio A BE
            this.errorService.showErrorWithMessage(UMA_MESSAGES.aziendaDestinatariaTrasferimentoKO, 'tst-ricerca-richiesta-carburante');
          } else {
            this.errorService.showError(error, 'tst-ricerca-richiesta-carburante');
          }
        }
      );
  }

  addTrasferimento(idDestinatario: string, cuaa: string, denominazione: string) {
    this.chiudiDialog();
    this.chiudiPopup.emit({ idDestinatario: idDestinatario, cuaa: cuaa, denominazione: denominazione, modalitaCrea: true });
  }

  public changePage(event: PaginatorEvent) {
    // this.cercaRichiesta();
  }

  reset() {
    if (this.table) {
      this.table.reset();
    }
  }
}
