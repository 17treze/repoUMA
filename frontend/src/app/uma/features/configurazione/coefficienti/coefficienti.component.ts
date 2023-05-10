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
  coefficiente: CoefficienteDto;
  lavorazioni: SelectItem[];
  cols: any;
  displayDialog: boolean;
  newCoefficiente: boolean;
  selectedCoefficiente: CoefficienteDto;

  constructor(private messageService: MessageService) { }

  ngOnInit() {
    this.setCols();

    // TODO: sostituire con la chiamata in GET per popolare la lista this.coefficienti
    this.coefficienti = [
      {
        id: 1,
        versione: 0,
        coefficiente: 1,
        lavorazione: 1,
        annoInizio: 2020,
        annoFine: 2022
      },
      {
        id: 2,
        versione: 0,
        coefficiente: 2,
        lavorazione: 2,
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

  canSave(lavorazione: CoefficienteDto) {
    return lavorazione.annoFine && lavorazione.annoInizio && lavorazione.coefficiente && lavorazione.lavorazione;
  }

  showDialogToAdd() {
    this.newCoefficiente = true;
    this.coefficiente = {} as CoefficienteDto;
    this.displayDialog = true;
  }

  save() {
    // TODO: aggiungere chiamata per salvataggio nuovo record o aggiornmento record esistente
    if (this.canSave(this.coefficiente)) {
      let coefficienti = [...this.coefficienti];
      if (this.newCoefficiente)
        coefficienti.push(this.coefficiente);
      else
        coefficienti[this.coefficienti.indexOf(this.selectedCoefficiente)] = this.coefficiente;
      this.messageService.add(A4gMessages.getToast('tst-coeff', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOK));
      this.coefficienti = coefficienti;
      this.coefficiente = null;
      this.displayDialog = false;
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

}
