import { Component, OnInit } from '@angular/core';
import { IChildInteraction } from '../../classi/IChildInteaction';
import { UtentiService } from '../../utenti.service';
import { CaricaDto } from 'src/app/amministrazione/gestione-utenze/model/facicoloImprese.model';
import { ResponsabilitaTitolare } from '../../classi/responsabilita-titolare';

@Component({
  selector: 'app-legale',
  templateUrl: './legale.component.html',
  styleUrls: ['./legale.component.css']
})
export class LegaleComponent implements OnInit, IChildInteraction {

  constructor(private utentiService: UtentiService) {
    this.isValid = false;
    this.submitted = false;
  }

  index: number;
  selfRef: any;
  compInteraction: any;
  isValid: boolean;
  title: string;
  codeResponsabilita: string;

  disabled: boolean;
  submitted: boolean;
  cfPersona: string;
  elencoCariche: CaricaDto[];

  ngOnInit() {
    console.log("LegaleComponent cfPersona " + this.cfPersona);
    //nel caso in cui non ci sono responsabilità allora esegui la richiesta al backend
    //il caso in cui, invece, ci sono responsabilità significa che sono state già recuperate dalla domanda
    if (!this.elencoCariche) {
      this.utentiService.getListaCariche(this.cfPersona)
        .subscribe(r => {
          this.elencoCariche = r;
          if (this.elencoCariche) {
            this.isValid = true;
          }
        });
    }
  }

  removeMe() {
    this.compInteraction.remove(this.index);
  }

  setDisabled(input: boolean) {
    this.disabled = input;
  }

  addElencoResponsabilita(responsabilita: ResponsabilitaTitolare) {
    if (!this.elencoCariche) {
      this.elencoCariche = [];
    }
    const fascicoloDto: CaricaDto = {
      codiceFiscale: null,
      cuaa: responsabilita.cuaa,
      denominazione: responsabilita.denominazione,
      carica: null
    };
    this.elencoCariche.push(fascicoloDto);
  }


}
