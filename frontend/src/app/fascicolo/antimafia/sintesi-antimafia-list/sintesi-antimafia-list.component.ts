import { Component, OnInit, Input, Output } from '@angular/core';
import { DichiarazioneAntimafia } from '../classi/dichiarazioneAntimafia';
import { DichiarazioneAntimafiaComponent } from '../dichiarazione-antimafia/dichiarazione-antimafia.component';
import { AntimafiaService } from '../antimafia.service';
import { ActivatedRoute, Router } from '@angular/router';
import * as FileSaver from "file-saver";
import { Configuration } from '../../../app.constants';
import { Azienda } from '../classi/azienda';
import { DichiarazioneAntimafiaService } from '../dichiarazione-antimafia.service';
import { FascicoloCorrente } from '../../fascicoloCorrente';
import { Observable } from 'rxjs';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'app-sintesi-antimafia-list',
  templateUrl: './sintesi-antimafia-list.component.html',
  styleUrls: ['./sintesi-antimafia-list.component.css']
})

export class SintesiAntimafiaListComponent implements OnInit {
  @Input()
  dichiarazioniAntimafia: Array<DichiarazioneAntimafia>;
  urlUploadFirma: string;
  urlUploaProtocolla: string;
  firmaPdf: boolean = false;
  protocollaPdf: boolean = false;
  dichiarazioneId: number;

  @Output()
  newDichiarazione: EventEmitter<string> = new EventEmitter<string>();

  constructor(private route: ActivatedRoute,
    private router: Router,
    private dichiarazioneAntimafiaService: DichiarazioneAntimafiaService,
    private antimafiaService: AntimafiaService,
    private _configuration: Configuration,
    private fascicoloCorrente: FascicoloCorrente) { }
  sub;

  ngOnInit() {
    /*this.getDichiarazioniAntimafia();
    this.urlUploadFirma = this._configuration.UrlUploadFirma;
    this.urlUploaProtocolla = this._configuration.UrlUploadProtocolla;*/
  }

  /*getDichiarazioniAntimafia(): void {
    if (this.fascicoloCorrente == null || this.fascicoloCorrente.fascicolo == null || this.fascicoloCorrente.fascicolo.cuaa == null) {
    }
    else {

      let dichiarazione = new DichiarazioneAntimafia();
      dichiarazione.azienda = new Azienda();
      dichiarazione.azienda.cuaa = this.fascicoloCorrente.fascicolo.cuaa;

      this.antimafiaService.getDichiarazioniAntimafia(dichiarazione)
        .subscribe((next) => { this.dichiarazioniAntimafia = next, this.rerouteAntimafia() });
    }
  }*/

  nuovaDichiarazione(){
    this.newDichiarazione.emit();
  } 
  /*
  scaricaPdf(idDichiarazione: number) {

    this.firmaPdf = false;
    this.protocollaPdf = false;

    this.antimafiaService.getPdf(idDichiarazione)
      .subscribe(
        (response: any) => {
          // get the response as blob, rename the file, and save  it
          const blob = response;
          const file = new Blob([blob], {});
          const filename = 'DichiarazioneAntimafia_' + idDichiarazione + "_" + Date.now() + '.pdf';
          FileSaver.saveAs(file, filename);
        },
        error => {
          alert('Errore in scarico Pdf');
        },
        () => { }
      );
  }

  caricaFirma(idDichiarazione: number) {
    this.firmaPdf = true;
    this.protocollaPdf = false;
    this.dichiarazioneId = idDichiarazione;
  }

  caricaProtocolla(idDichiarazione: number) {
    this.firmaPdf = false;
    this.protocollaPdf = true;
    this.dichiarazioneId = idDichiarazione;
  }*/
}
