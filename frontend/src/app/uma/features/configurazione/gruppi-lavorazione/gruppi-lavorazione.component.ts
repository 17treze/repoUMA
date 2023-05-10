import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng-lts';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { GruppoLavorazioneDto } from 'src/app/uma/core-uma/models/dto/ConfigurazioneDto';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';

@Component({
  selector: 'app-gruppi-lavorazione',
  templateUrl: './gruppi-lavorazione.component.html',
  styleUrls: ['./gruppi-lavorazione.component.css']
})
export class GruppiLavorazioneComponent implements OnInit {

  gruppiLavorazione: GruppoLavorazioneDto[];
  gruppoLavorazione: GruppoLavorazioneDto;
  cols: any;
  displayDialog: boolean;
  newGruppoLavorazione: boolean;
  selectedGruppoLavorazione: GruppoLavorazioneDto;

  constructor(private messageService: MessageService) { }

  ngOnInit() {
    this.setCols();

    // TODO: sostituire con la chiamata in GET per popolare la lista this.gruppiLavorazione
    this.gruppiLavorazione = [
      {
        id: 1,
        versione: 0,
        indice: 1,
        nome: 'lav1',
        ambito: 'tipo1',
        annoInizio: 2022,
        annoFine: 2023
      }
    ];
  }

  private setCols() {
    this.cols = [
      { field: 'indice', header: 'Indice' },
      { field: 'nome', header: 'Nome' },
      { field: 'ambito', header: 'Ambito' },
      { field: 'annoInizio', header: 'Anno inizio' },
      { field: 'annoFine', header: 'Anno fine' }
    ];
  }

  canSave(lavorazione: GruppoLavorazioneDto) {
    return lavorazione.ambito && lavorazione.annoFine && lavorazione.annoInizio && lavorazione.indice && lavorazione.nome;
  }

  showDialogToAdd() {
    this.newGruppoLavorazione = true;
    this.gruppoLavorazione = {} as GruppoLavorazioneDto;
    this.displayDialog = true;
  }

  save() {
    // TODO: aggiungere chiamata per salvataggio nuovo record o aggiornmento record esistente
    if (this.canSave(this.gruppoLavorazione)) {
      let coefficienti = [...this.gruppiLavorazione];
      if (this.newGruppoLavorazione)
        coefficienti.push(this.gruppoLavorazione);
      else
        coefficienti[this.gruppiLavorazione.indexOf(this.selectedGruppoLavorazione)] = this.gruppoLavorazione;
      this.messageService.add(A4gMessages.getToast('tst-gruppi-lav', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOK));
      this.gruppiLavorazione = coefficienti;
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

}
