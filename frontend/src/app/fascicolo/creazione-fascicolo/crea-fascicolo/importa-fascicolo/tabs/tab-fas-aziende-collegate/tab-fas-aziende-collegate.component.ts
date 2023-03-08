import { ImpresaDto } from 'src/app/fascicolo/creazione-fascicolo/dto/ImpresaDto';
import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-tab-fas-aziende-collegate',
  templateUrl: './tab-fas-aziende-collegate.component.html',
  styleUrls: ['./tab-fas-aziende-collegate.component.css']
})
export class TabFasAziendeCollegateComponent implements OnInit {

  anagrafica: ImpresaDto;

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.anagrafica = this.route.snapshot.data['fascicolo'];
  }

  getIndirizzo(indirizzo) {
    if (!indirizzo) { return; }
    if (indirizzo.toponimo && indirizzo.via) {
      return  indirizzo.toponimo.concat(" ") +
              indirizzo.via
    }
  }

  getIndirizzoCompleto(indirizzo) {
    if (!indirizzo) { return; }
    const comune = indirizzo.comune ? indirizzo.comune+',' : '';
    const cap = indirizzo.cap ? indirizzo.cap+',': '';
    const numeroCivico = indirizzo.numeroCivico ? indirizzo.numeroCivico+',' : '';
    const provincia = indirizzo.provincia ? indirizzo.provincia : '';
    return  this.getIndirizzo(indirizzo).concat(`,${numeroCivico} ${cap} ${comune} ${provincia}`);
  }

}
