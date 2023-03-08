import { Injectable } from '@angular/core';
import { DichiarazioneAntimafia } from './classi/dichiarazioneAntimafia';
import { Azienda } from './classi/azienda';

@Injectable({
  providedIn: 'root'
})
export class DichiarazioneAntimafiaService {
  private dichiarazioneAntimafia: DichiarazioneAntimafia = new DichiarazioneAntimafia();
  private enabled: boolean;
  private textTitle: string;

  constructor() {
  }

  setDichiarazioneAntimafia(dichiarazioneAntimafia: DichiarazioneAntimafia) {
    this.dichiarazioneAntimafia = dichiarazioneAntimafia;
  }

  setAzienda(aziendaAgricola: Azienda) {
    this.dichiarazioneAntimafia.azienda = aziendaAgricola;
  }

  getDichiarazioneAntimafia(): DichiarazioneAntimafia {
      return this.dichiarazioneAntimafia;
  }

  getEnabled(){
    return this.enabled;
  }

  getTitle(){
    return this.textTitle;
  }

  /*setEdit() {
    if (this.dichiarazioneAntimafia) {
      switch (this.dichiarazioneAntimafia.stato.identificativo) {
        case 'BOZZA':
          this.enabled = true;
          this.textTitle = "Compila dichiarazione antimafia";
          break;

        case 'FIRMATA':
          this.enabled = false;
          this.textTitle = "Consulta dichiarazione antimafia";
          break;

        case 'PROTOCOLLATO':
          this.enabled = false;
          this.textTitle = "Consulta dichiarazione antimafia";
          break;

        default:
          this.enabled = false;
          this.textTitle = "Consulta dichiarazione antimafia";
          break;
      }
    }
  }

  getAnagraficaSoggetto(): AnagraficaSoggetto {
    let anagraficaSoggetto = new AnagraficaSoggetto();
    if(this.dichiarazioneAntimafia && this.dichiarazioneAntimafia.datiDichiarazione && this.dichiarazioneAntimafia.datiDichiarazione.length > 0){
      anagraficaSoggetto = JSON.parse(this.dichiarazioneAntimafia.datiDichiarazione);
    }
    return anagraficaSoggetto; 
  }*/
}
