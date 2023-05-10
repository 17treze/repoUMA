import { Component, OnInit } from '@angular/core';
import { SelectItem, MessageService } from 'primeng-lts';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { LavorazioneDto } from 'src/app/uma/core-uma/models/dto/ConfigurazioneDto';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';

@Component({
  selector: 'app-lavorazioni',
  templateUrl: './lavorazioni.component.html',
  styleUrls: ['./lavorazioni.component.css']
})
export class LavorazioniComponent implements OnInit {

  lavorazioni: LavorazioneDto[];
  lavorazione: LavorazioneDto;
  gruppiLavorazione: SelectItem[];
  cols: any;
  displayDialog: boolean;
  newLavorazione: boolean;
  selectedLavorazione: LavorazioneDto;

  constructor(private messageService: MessageService) { }

  ngOnInit() {
    this.setCols();

    // TODO: sostituire con la chiamata in GET per popolare la lista this.lavorazioni
    this.lavorazioni = [
      {
        id: 1,
        versione: 0,
        indice: 1,
        nome: 'lav1',
        tipologia: 'tipo1',
        unitaMisura: '1',
        gruppoLavorazione: 1
      }
    ];

    // TODO: sostituire con la chiamata in GET per popolare la lista this.gruppiLavorazione
    this.gruppiLavorazione = [
      { label: 'Gruppo lavorazione 1', value: '1' },
      { label: 'Gruppo lavorazione 2', value: '2' },
      { label: 'Gruppo lavorazione 3', value: '3' }
    ];
  }

  private setCols() {
    this.cols = [
      { field: 'indice', header: 'Indice' },
      { field: 'nome', header: 'Nome' },
      { field: 'tipologia', header: 'Tipologia' },
      { field: 'unitaMisura', header: 'Unità di misura' },
      { field: 'gruppoLavorazione', header: 'Gruppo lavorazione' }
    ];
  }

  canSave(lavorazione: LavorazioneDto) {
    return lavorazione.gruppoLavorazione && lavorazione.indice && lavorazione.nome && lavorazione.tipologia && lavorazione.unitaMisura;
  }

  showDialogToAdd() {
    this.newLavorazione = true;
    this.lavorazione = {} as LavorazioneDto;
    this.displayDialog = true;
  }

  save() {
    // TODO: aggiungere chiamata per salvataggio nuovo record o aggiornmento record esistente
    if (this.canSave(this.lavorazione)) {
      let lavorazioni = [...this.lavorazioni];
      if (this.newLavorazione)
        lavorazioni.push(this.lavorazione);
      else
        lavorazioni[this.lavorazioni.indexOf(this.selectedLavorazione)] = this.lavorazione;
      this.messageService.add(A4gMessages.getToast('tst-lav', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOK));
      this.lavorazioni = lavorazioni;
      this.lavorazione = null;
      this.displayDialog = false;
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
    return lavorazione;
  }

}
