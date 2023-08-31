import { IndiceUmaService } from './../../core-uma/services/indice-uma.service';
import { BehaviorSubject } from 'rxjs';
import { Injectable } from '@angular/core';
import { FabbisognoDichiarato } from '../../core-uma/models/viewModels/FabbisognoDichiaratoViewModel';
import { MenuItem } from 'primeng/api';
import { RichiestaCarburanteDto } from '../../core-uma/models/dto/RichiestaCarburanteDto';
import { StatoDomandaUma } from 'src/app/a4g-common/classi/enums/uma/StatoDomandaUma.enum';
import { AuthService } from 'src/app/auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class GestioneDomandaUmaService {
  private _readonlyDomanda$ = new BehaviorSubject<boolean>(false);
  private _fabbisognoDichiarato$ = new BehaviorSubject<FabbisognoDichiarato>(null);

  constructor(
    private indiceUmaService: IndiceUmaService,
    private authService: AuthService
  ) { }

  // aggiornamento stato readonly alla pagina sezioni-domanda
  updatedDomandaUmaStatus(data: boolean) {
    this._readonlyDomanda$.next(data);
  }

  // aggiornamento dichiarazione Fabbisogno alla pagina sezioni-domanda
  updateDichiarazioneFabbisogno(data: FabbisognoDichiarato) {
    this._fabbisognoDichiarato$.next(data);
  }

  // Le richieste nello stato in compilazioni sono modificabili, le altre no 
  isInCompilazione(richiesta: RichiestaCarburanteDto, items?: Array<MenuItem>): { inCompilazione: boolean, mostraScarica: boolean } {
    let inCompilazione: boolean = false;
    let mostraScarica: boolean = false;
    if (richiesta.stato === StatoDomandaUma.IN_COMPILAZIONE &&
      this.authService.userSelectedRole !== AuthService.roleIstruttoreUMA &&
      this.authService.userSelectedRole !== AuthService.roleAdmin &&
      this.authService.userSelectedRole !== AuthService.rolePrivate &&
      this.authService.userSelectedRole !== AuthService.roleCaa) {
      this.indiceUmaService.READONLY_MODE = false;
      inCompilazione = !this.indiceUmaService.READONLY_MODE;
      mostraScarica = false;
      if (items && items.length) {
        items.push({ label: 'Protocollazione' });
      }
    } else { // se il ruolo è istruttore UMA oppure la domanda non è in compilazione, visualizzo in sola lettura
      this.indiceUmaService.READONLY_MODE = true;
      inCompilazione = !this.indiceUmaService.READONLY_MODE;
      mostraScarica = richiesta.stato === StatoDomandaUma.IN_COMPILAZIONE ? false : true;
    }
    localStorage.setItem("UMA_RO", this.indiceUmaService.READONLY_MODE.toString());
    return { inCompilazione: inCompilazione, mostraScarica: mostraScarica };
  }

  /**  In base al tipo fabbisogno dichiarato (gasolio e/o benzina) abilito/disabilito la colonna dei fabbisogni relativi
  nelle varie sezioni relative al carburante */
  getTipoCarburanteDichiarato(richiesta: RichiestaCarburanteDto): FabbisognoDichiarato {
    const fabbisognoDichiarato: FabbisognoDichiarato = { gasolio: richiesta.haMacchineGasolio, benzina: richiesta.haMacchineBenzina };
    return fabbisognoDichiarato;
  }

  get readonlyDomanda$(): BehaviorSubject<boolean> {
    return this._readonlyDomanda$;
  }
  get fabbisognoDichiarato$(): BehaviorSubject<FabbisognoDichiarato> {
    return this._fabbisognoDichiarato$;
  }
}
