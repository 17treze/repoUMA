import { Component, OnInit } from '@angular/core';
import { MessageService, SelectItem } from 'primeng-lts';
import { GruppoFabbricatoDto } from 'src/app/uma/core-uma/models/dto/ConfigurazioneDto';

@Component({
  selector: 'app-gruppi-fabbricato',
  templateUrl: './gruppi-fabbricato.component.html',
  styleUrls: ['./gruppi-fabbricato.component.css']
})
export class GruppiFabbricatoComponent implements OnInit {

  gruppiFabbricato: GruppoFabbricatoDto[];
  cols: any;
  gruppiLavorazione: SelectItem[];

  constructor(private messageService: MessageService) { }

  ngOnInit() {
    this.setCols();

    // TODO: sostituire con la chiamata in GET per popolare la lista this.gruppiFabbricato
    this.gruppiFabbricato = [
      {
        id: 1,
        versione: 0,
        codiceFabbricato: 'fabbricato1',
        tipoFabbricato: 'tipo1',
        gruppoLavorazione: 1,
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
      { field: 'codiceFabbricato', header: 'Codice fabbricato' },
      { field: 'tipoFabbricato', header: 'Tipo Fabbricato' },
      { field: 'gruppoLavorazione', header: 'Gruppo lavorazione' }
    ];
  }

}
