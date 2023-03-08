import { Component, OnInit } from '@angular/core';
import { ImpresaDto } from 'src/app/fascicolo/creazione-fascicolo/dto/ImpresaDto';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-tab-fas-soggetti-carica',
  templateUrl: './tab-fas-soggetti-carica.component.html',
  styleUrls: ['./tab-fas-soggetti-carica.component.css']
})
export class TabFasSoggettiCaricaComponent implements OnInit {

  anagrafica: ImpresaDto;

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.anagrafica = this.route.snapshot.data['fascicolo'];
    if (this.anagrafica.soggettiConCarica) {
      this.anagrafica.soggettiConCarica.sort((a, b) => a.codiceFiscale.localeCompare(b.codiceFiscale));
    }
  }

  getIndirizzoCompleto(indirizzo) {
    if (!indirizzo) { return ""; }
    const toponimo = indirizzo.toponimo ? indirizzo.toponimo : "";
    const via = indirizzo.via ? indirizzo.via : "";
    const comune = indirizzo.comune ? indirizzo.comune : "";
    const cap = indirizzo.cap ? indirizzo.cap : "";
    const provincia = indirizzo.provincia ? indirizzo.provincia : "";
    return toponimo.concat(` ${via} ${comune} ${cap} ${provincia}`);
  }

}
