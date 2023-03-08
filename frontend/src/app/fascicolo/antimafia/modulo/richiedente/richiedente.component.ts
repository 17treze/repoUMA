import { Component, OnInit, ChangeDetectorRef, Input, Output, ViewChild } from '@angular/core';
import { DichiarazioneAntimafia,  } from '../../classi/dichiarazioneAntimafia';
import { DichiarazioneAntimafiaService } from '../../dichiarazione-antimafia.service';
import { DatiDichiarazione, Richiedente } from '../../classi/datiDichiarazione';
import {EventEmitter} from '@angular/core';
import { DropdownValue } from '../../../../a4g-common/classi/dropdownValue';
import { Labels } from "src/app/app.labels";
import { StepActionComponent } from '../step-action/step-action.component';
import { AntimafiaValidationService } from '../antimafia-validation.service';
import { StatoValidazione } from '../../classi/statoValidazione';
import * as socketIo from 'socket.io';
import { Utils } from '../antimafia.utils';

@Component({
  selector: 'app-richiedente',
  templateUrl: './richiedente.component.html',
  styleUrls: ['./richiedente.component.css']
})

export class RichiedenteComponent implements OnInit {

  values: DropdownValue[];
  selectedCUAA: DropdownValue;
  richiedente: Richiedente;
  labels = Labels;
  @ViewChild(StepActionComponent, { static: true })
  private stepComponent: StepActionComponent;
  textTitle: string;

  @Output()
  select: EventEmitter<string>;

  constructor(
    private dichiarazioneAntimafiaService: DichiarazioneAntimafiaService,
    private validator: AntimafiaValidationService) {
    this.values = [ new DropdownValue('1', 'RLR')];
    this.select = new EventEmitter();
  }

  ngOnInit() {
    this.richiedente = this.getDati(this.dichiarazioneAntimafiaService.getDichiarazioneAntimafia());
    this.textTitle = Labels.datiAcquisiti;
    Utils.setAllNullValuesToND(this.richiedente);
  }

  selectItem(value){
    this.select.emit(value);
   }


  getDati(dichiarazione: DichiarazioneAntimafia): Richiedente {
    let dichiarazioneDettagli : DatiDichiarazione;
    //console.log(JSON.stringify(dichiarazione.datiDichiarazione));

    if (dichiarazione != null) {
      console.log("dichiarazioneDettagli 1 ");
      dichiarazioneDettagli = dichiarazione.datiDichiarazione; //JSON.parse(dichiarazione.datiDichiarazione);
      //console.log("dichiarazioneDettagli 2 " +JSON.stringify(dichiarazione.datiDichiarazione));
    }
    return dichiarazioneDettagli.richiedente;
  }

  conferma() {
      const validatorResponse = this.validator.validaRichiedente(this.richiedente);
      if (validatorResponse.esito !== StatoValidazione.OK) {
        alert(validatorResponse.messaggio);
        return;
      }
      console.log("richiedente next");
      this.stepComponent.goNext();
  }


}

