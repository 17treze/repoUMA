import { Component, OnInit } from '@angular/core';
import { ImpresaDto } from 'src/app/fascicolo/creazione-fascicolo/dto/ImpresaDto';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-tab-fas-attivita-iva',
  templateUrl: './tab-fas-attivita-iva.component.html',
  styleUrls: ['./tab-fas-attivita-iva.component.css']
})
export class TabFasAttivitaIvaComponent implements OnInit {
  
  anagrafica: ImpresaDto;

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.anagrafica = this.route.snapshot.data['fascicolo'];
  }

}
