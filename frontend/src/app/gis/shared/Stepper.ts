import { Injectable } from '@angular/core';

@Injectable()
export class Stepper {
   currentStepLavorazione: number;
   closeLavorazioneStep3: boolean;
   resetStep: boolean;
   goToStep: any;
   refreshStep: boolean;
}

