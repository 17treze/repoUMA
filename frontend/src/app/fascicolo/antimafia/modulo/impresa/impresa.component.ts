import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { DichiarazioneAntimafiaService } from '../../dichiarazione-antimafia.service';
import { DichiarazioneAntimafia } from '../../classi/dichiarazioneAntimafia';
import { DatiDichiarazione, DettaglioImpresa } from '../../classi/datiDichiarazione';
import { Labels } from "src/app/app.labels";
import { StepActionComponent } from '../step-action/step-action.component';
import { AntimafiaValidationService } from '../antimafia-validation.service';
import { StatoValidazione } from '../../classi/statoValidazione';
import { Utils } from '../antimafia.utils';

@Component({
  selector: 'app-impresa',
  templateUrl: './impresa.component.html',
  styleUrls: ['./impresa.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ImpresaComponent implements OnInit {
  dettaglioImpresa: DettaglioImpresa;
  labels = Labels;
  @ViewChild(StepActionComponent, { static: true })
  private stepComponent: StepActionComponent;
  textTitle: string;

  constructor(private dichiarazioneAntimafiaService: DichiarazioneAntimafiaService, private validator: AntimafiaValidationService) { }

  ngOnInit() {
    this.dettaglioImpresa = this.getAnagraficaImpresa(this.dichiarazioneAntimafiaService.getDichiarazioneAntimafia());
    this.textTitle = Labels.datiAcquisiti;
  }


  getAnagraficaImpresa(dichiarazione: DichiarazioneAntimafia): any {
    let dichiarazioneDettagli: DatiDichiarazione;

    if (dichiarazione != null) {
      dichiarazioneDettagli = dichiarazioneDettagli = dichiarazione.datiDichiarazione; //JSON.parse(dichiarazione.datiDichiarazione);
      return dichiarazioneDettagli.dettaglioImpresa;
    }

    return null;
  }

  conferma() {
    const validatorResponse = this.validator.validaImpresa(this.dettaglioImpresa);
    if (validatorResponse.esito !== StatoValidazione.OK) {
      alert(validatorResponse.messaggio);
      return;
    }
    console.log("impresa next");
    this.stepComponent.goNext();
  }

}
