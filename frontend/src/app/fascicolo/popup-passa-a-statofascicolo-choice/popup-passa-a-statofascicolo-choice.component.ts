import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { FascicoloDaCuaa } from 'src/app/a4g-common/classi/FascicoloCuaa';
import { AuthService } from 'src/app/auth/auth.service';
import { Utente } from 'src/app/auth/user';
import { StatoFascicoloEnum } from '../creazione-fascicolo/dto/DatiAperturaFascicoloDto';

@Component({
  selector: 'app-popup-passa-a-statofascicolo-choice',
  templateUrl: './popup-passa-a-statofascicolo-choice.component.html'
})
export class PopupPassaAStatofascicoloChoiceComponent {
  @Input() public isPopupVisible: boolean = false;
  @Input() public fascicoloCorrente: FascicoloDaCuaa;
  @Output() public chiudiPopup = new EventEmitter();

  constructor(
    private authService: AuthService,
  ) {}

  public openPopupSchedaValidazioneFirmaCaaAzienda() {
    this.chiudiPopup.emit(StatoFascicoloEnum.VALIDATO);
    this.isPopupVisible = false;
  }

  public changeStatusInAggiornamentoFascicolo() {
    this.chiudiPopup.emit(StatoFascicoloEnum.IN_AGGIORNAMENTO);
    this.isPopupVisible = false;
  }

  public openPopupSchedaValidazione() {
    this.chiudiPopup.emit(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA);
    this.isPopupVisible = false;
  }

  public close() {
    this.chiudiPopup.emit("0");
    this.isPopupVisible = false;
  }

  public isUtenteCaaAndFascicoloInAggiornamento(): boolean {
    let utente: Utente = this.authService.getUser(false);
    return this.fascicoloCorrente &&
      (this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_AGGIORNAMENTO
        || this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_CAA)
        && utente.profili.some(e => e.identificativo === 'caa');
  }

  public isFascicoloAllaFirmaAziendaOrAllaFirmaCaa(): boolean {
    return this.fascicoloCorrente &&
      (this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_CAA
        || this.fascicoloCorrente.stato === StatoFascicoloEnum.FIRMATO_CAA);
  }

  public isFascicoloInAggiornamentoOrAllaFirmaCaa(): boolean {
    return this.fascicoloCorrente &&
      (this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_AGGIORNAMENTO ||
        this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_CAA);
  }
}
