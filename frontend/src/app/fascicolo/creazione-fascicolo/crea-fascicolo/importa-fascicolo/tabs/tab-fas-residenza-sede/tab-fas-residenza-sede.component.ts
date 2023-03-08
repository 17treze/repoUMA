import { Component, OnInit } from '@angular/core';
import { ImpresaDto } from 'src/app/fascicolo/creazione-fascicolo/dto/ImpresaDto';
import { ActivatedRoute } from '@angular/router';
import { Labels } from 'src/app/app.labels';

@Component({
  selector: 'app-tab-fas-residenza-sede',
  templateUrl: './tab-fas-residenza-sede.component.html',
  styleUrls: ['./tab-fas-residenza-sede.component.css']
})
export class TabFasResidenzaSedeComponent implements OnInit {

  anagrafica: ImpresaDto;
  indirizzo: string;
  constructor(
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.anagrafica = this.route.snapshot.data['fascicolo'];

    if (this.anagrafica.sedeLegale && this.anagrafica.sedeLegale.indirizzo.toponimo && this.anagrafica.sedeLegale.indirizzo.via) {
      this.indirizzo =
        this.anagrafica.sedeLegale.indirizzo.toponimo.concat(" ") +
        this.anagrafica.sedeLegale.indirizzo.via.concat(" ") +
        this.anagrafica.sedeLegale.indirizzo.numeroCivico;
    }
  }

}
