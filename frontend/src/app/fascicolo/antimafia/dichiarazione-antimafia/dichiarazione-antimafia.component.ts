import { Component, OnInit, ViewEncapsulation} from '@angular/core';
import { ActivatedRoute  } from '@angular/router';
import { DichiarazioneAntimafiaService } from '../dichiarazione-antimafia.service';
import { Labels } from '../../../app.labels';
import {MenuItem} from 'primeng/api';
import { StepEvent } from '../classi/stepEvent';

@Component({
  selector: 'app-dichiarazione-antimafia',
  templateUrl: './dichiarazione-antimafia.component.html',
  styleUrls: ['./dichiarazione-antimafia.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DichiarazioneAntimafiaComponent  implements OnInit  {
  textTitle: string;
  isCompleted: boolean;
  stepsAntimafia: MenuItem[];


  constructor(private dichiarazioneAntimafiaService: DichiarazioneAntimafiaService,
    private route: ActivatedRoute,
    public stepEvent: StepEvent
  ) {

  }

  ngOnInit() {
    const dichiarazione = this.route.snapshot.data['dichiarazione'];
    this.dichiarazioneAntimafiaService.setDichiarazioneAntimafia(dichiarazione);
    this.stepsAntimafia = [
      {label: Labels.procedimenti},
      {label: Labels.anagraficaRichiedente},
      {label: Labels.anagraficaImpresa},
      {label: Labels.soggettiCarica},
      {label: Labels.aziendeCollegate},
      {label: Labels.familiari},
      {label: Labels.verifica}
    ];
    this.stepEvent.stepIndex = 0;
    this.textTitle = Labels.richiestaCertificazione;
  }
}
