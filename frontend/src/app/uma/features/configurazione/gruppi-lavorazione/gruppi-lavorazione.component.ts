import { Component, OnInit } from '@angular/core';
import { SelectItem, MessageService } from 'primeng-lts';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { GruppoLavorazioneDto, LavorazioneDto } from 'src/app/uma/core-uma/models/dto/ConfigurazioneDto';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';

@Component({
  selector: 'app-gruppi-lavorazione',
  templateUrl: './gruppi-lavorazione.component.html',
  styleUrls: ['./gruppi-lavorazione.component.css']
})
export class GruppiLavorazioneComponent implements OnInit {

  gruppiLavorazione: GruppoLavorazioneDto[];

  clonedGruppiLavorazione: { [s: string]: GruppoLavorazioneDto; } = {};

  cols: any;

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

  onRowEditInit(gruppoLavorazione: GruppoLavorazioneDto) {
    this.clonedGruppiLavorazione[gruppoLavorazione.id] = { ...gruppoLavorazione };
  }

  onRowEditSave(gruppoLavorazione: GruppoLavorazioneDto) {
    if (this.canSave(gruppoLavorazione)) {
      // TODO: aggiungere chiamata per salvataggio
      delete this.clonedGruppiLavorazione[gruppoLavorazione.id];
      this.messageService.add(A4gMessages.getToast('tst-gruppi-lav', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOK));
    }
    else {
      this.messageService.add(A4gMessages.getToast('tst-gruppi-lav', A4gSeverityMessage.warn, UMA_MESSAGES.mandatoryAll));
    }
  }

  onRowEditCancel(gruppoLavorazione: GruppoLavorazioneDto, index: number) {
    this.gruppiLavorazione[index] = this.clonedGruppiLavorazione[gruppoLavorazione.id];
    delete this.clonedGruppiLavorazione[gruppoLavorazione.id];
  }

  canSave(lavorazione: GruppoLavorazioneDto) {
    return lavorazione.ambito && lavorazione.annoFine && lavorazione.annoInizio && lavorazione.indice && lavorazione.nome;
  }

}
