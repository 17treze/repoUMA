import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FascicoloDaCuaa } from 'src/app/a4g-common/classi/FascicoloCuaa';
import { AuthService } from 'src/app/auth/auth.service';
import { Utente } from 'src/app/auth/user';
import { StatoFascicoloEnum } from '../creazione-fascicolo/dto/DatiAperturaFascicoloDto';

@Component({
  selector: 'app-popup-conferma-in-aggiornamento',
  templateUrl: './popup-conferma-in-aggiornamento.component.html',
  styleUrls: ['./popup-conferma-in-aggiornamento.component.css']
})
export class PopupConfermaInAggiornamentoComponent {
  @Input() public isPopupVisible = false;
  @Input() public fascicoloCorrente: FascicoloDaCuaa;
  @Output() public chiudiPopup = new EventEmitter();

  constructor(
    private authService: AuthService,
  ) {}

  public changeStatusInAggiornamentoFascicolo() {
    this.chiudiPopup.emit('SI');
    this.isPopupVisible = false;
  }

  public close() {
    this.chiudiPopup.emit('NO');
    this.isPopupVisible = false;
  }

  public isFascicoloAggiornabile(): boolean {
    const utente: Utente = this.authService.getUser(false);
    return this.fascicoloCorrente &&
      (this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_CAA
        || this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_AZIENDA
        || this.fascicoloCorrente.stato === StatoFascicoloEnum.FIRMATO_CAA)
        && utente.profili.some(e => e.identificativo === 'caa');
  }

}
