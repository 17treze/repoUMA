import { Labels } from 'src/app/app.labels';
import { MenuItem } from 'primeng/api';
import { Component, OnInit } from '@angular/core';
import { ImpresaDto } from '../../dto/ImpresaDto';
import { ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-importa-fascicolo',
  templateUrl: './importa-fascicolo.component.html',
  styleUrls: ['./importa-fascicolo.component.css']
})
export class ImportaFascicoloComponent implements OnInit {
  tabs: Array<MenuItem>;
  dati;
  title = Labels.creazioneNuovoFascicolo.toUpperCase();


  constructor(
    private route: ActivatedRoute,
    private translate: TranslateService
  ) { }

  ngOnInit() {
    let anagrafica: ImpresaDto = this.route.snapshot.data['fascicolo'];
    this.dati = [
      { chiave: Labels.cuaa, valore: anagrafica.codiceFiscale },
      { chiave: Labels.denominazioneFascicolo, valore: anagrafica.denominazione }
    ];
    this.tabs = new Array<MenuItem>(
      {
        routerLink: 'datiIdentificativi',
        label: Labels.datiIdentificativi
      },
      {
        routerLink: 'attivitaIva',
        label: Labels.attivitaIVA
      },
      {
        routerLink: 'soggettiCarica',
        label: Labels.soggettiCarica
      },
      {
        routerLink: 'aziendeCollegate',
        label: Labels.aziendeCollegate
      },
    )
  }

}
