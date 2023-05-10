import { Component, OnInit } from '@angular/core';
import { MessageService, SelectItem } from 'primeng-lts';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { CoefficienteDto } from 'src/app/uma/core-uma/models/dto/ConfigurazioneDto';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';

@Component({
  selector: 'app-coefficienti',
  templateUrl: './coefficienti.component.html',
  styleUrls: ['./coefficienti.component.css']
})
export class CoefficientiComponent implements OnInit {

  coefficienti: CoefficienteDto[];

  clonedCoefficienti: { [s: string]: CoefficienteDto; } = {};

  lavorazioni: SelectItem[];

  cols: any;

  constructor(private messageService: MessageService) { }

  ngOnInit() {
    this.setCols();

    // TODO: sostituire con la chiamata in GET per popolare la lista this.coefficienti
    this.coefficienti = [
      {
        id: 1,
        versione: 0,
        coefficiente: 1,
        lavorazione: {
          id: 1,
          versione: 0,
          indice: 1,
          nome: 'lav1',
          tipologia: 'tipo1',
          unitaMisura: '1',
          gruppoLavorazione: {
            id: 1,
            versione: 0,
            indice: 1,
            nome: 'lav1',
            ambito: 'tipo1',
            annoInizio: 2022,
            annoFine: 2023
          }
        },
        annoInizio: 2020,
        annoFine: 2022
      },
      {
        id: 2,
        versione: 0,
        coefficiente: 2,
        lavorazione: {
          id: 2,
          versione: 0,
          indice: 2,
          nome: 'lav2',
          tipologia: 'tipo2',
          unitaMisura: '2',
          gruppoLavorazione: {
            id: 2,
            versione: 0,
            indice: 2,
            nome: 'lav2',
            ambito: 'tipo2',
            annoInizio: 2020,
            annoFine: 2022
          }
        },
        annoInizio: 2018,
        annoFine: 2023
      }
    ];

    // TODO: sostituire con la chiamata in GET per popolare la lista this.lavorazioni
    this.lavorazioni = [
      { label: 'Lavorazione 1', value: '1' },
      { label: 'Lavorazione 2', value: '2' },
      { label: 'Lavorazione 3', value: '3' }
    ];
  }

  private setCols() {
    this.cols = [
      { field: 'coefficiente', header: 'Coefficiente' },
      { field: 'lavorazione', header: 'Lavorazione' },
      { field: 'annoInizio', header: 'Anno inizio' },
      { field: 'annoFine', header: 'Anno fine' }
    ];
  }

  onRowEditInit(coefficiente: CoefficienteDto) {
    this.clonedCoefficienti[coefficiente.id] = { ...coefficiente };
  }

  onRowEditSave(coefficiente: CoefficienteDto) {
    if (this.canSave(coefficiente)) {
      // TODO: aggiungere chiamata per salvataggio
      delete this.clonedCoefficienti[coefficiente.id];
      this.messageService.add(A4gMessages.getToast('tst-coeff', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOK));
    }
    else {
      this.messageService.add(A4gMessages.getToast('tst-coeff', A4gSeverityMessage.warn, UMA_MESSAGES.mandatoryAll));
    }
  }

  onRowEditCancel(coefficiente: CoefficienteDto, index: number) {
    this.coefficienti[index] = this.clonedCoefficienti[coefficiente.id];
    delete this.clonedCoefficienti[coefficiente.id];
  }

  canSave(lavorazione: CoefficienteDto) {
    return lavorazione.annoFine && lavorazione.annoInizio && lavorazione.coefficiente && lavorazione.lavorazione;
  }

}
