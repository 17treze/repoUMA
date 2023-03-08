import { DateSupport } from './../../a4g-common/utility/date-support';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Subject } from 'rxjs';
import { ConduzioneDto } from '../creazione-fascicolo/dto/ConduzioneDto';

@Component({
  selector: 'app-popup-completa-trasferimento-in-appag',
  templateUrl: './popup-completa-trasferimento-in-appag.component.html',
  styleUrls: ['./popup-completa-trasferimento-in-appag.component.css']
})
export class PopupCompletaTrasferimentoInAppagComponent implements OnInit {

  @Input() public popupVisible = true;
  @Input() public cuaa: string;
  @Input() public conduzioniList: ConduzioneDto[];
  @Output() public chiudiPopupEmitter = new EventEmitter<string>();
  @Output() public salvaConduzioneTerreniEmitter = new EventEmitter();

  public cols: any[];
  public dateSupport = DateSupport;
  protected componentDestroyed$: Subject<boolean> = new Subject();

  constructor() { }

  ngOnInit() {
    this.cols = this.setCols();
  }

  private setCols() {
    return [
      { field: 'datiParticella.provincia', header: 'Provincia' },
      { field: 'datiParticella.comune', header: 'Comune' },
      { field: 'datiParticella.sezione', header: 'Sezione' },
      { field: 'datiParticella.foglio', header: 'Foglio' },
      { field: 'datiParticella.particella', header: 'Particella' },
      { field: 'datiParticella.subalterno', header: 'Subalterno' },
      { field: 'descrizioneTipoConduzione', header: 'Tipo conduzione' },
      { field: 'codiceFiscaleProprietarioList[0]', header: 'Proprietario' },
      { field: 'superficieCondotta', header: 'Sup. condotta' }
    ];
  }

  public closePopup() {
    this.popupVisible = false;
    this.chiudiPopupEmitter.emit();
  }

  public onSave() {
    this.salvaConduzioneTerreniEmitter.emit();
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

}
