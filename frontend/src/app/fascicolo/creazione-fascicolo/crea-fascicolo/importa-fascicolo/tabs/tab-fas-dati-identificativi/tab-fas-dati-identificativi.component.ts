import { Component, OnInit } from '@angular/core';
import { ImpresaDto } from 'src/app/fascicolo/creazione-fascicolo/dto/ImpresaDto';
import { ActivatedRoute } from '@angular/router';
import { LoaderService } from 'src/app/loader.service';

@Component({
  selector: 'app-tab-fas-dati-identificativi',
  templateUrl: './tab-fas-dati-identificativi.component.html',
  styleUrls: ['./tab-fas-dati-identificativi.component.css']
})
export class TabFasDatiIdentificativiComponent implements OnInit {

  anagrafica: ImpresaDto;

  constructor(
    private route: ActivatedRoute,
    private loader: LoaderService,
  ) { }

  ngOnInit() {
    this.loader.setTimeout(480000); //otto minuti
    this.anagrafica = this.route.snapshot.data['fascicolo'];
  }
  ngOnDestroy() {
    this.loader.resetTimeout();
  }

  importaDati() {
    alert('Costituisci fascicolo');
  }

}
