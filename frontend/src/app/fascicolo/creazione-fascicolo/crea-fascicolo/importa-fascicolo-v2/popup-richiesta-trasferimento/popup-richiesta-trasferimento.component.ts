import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-popup-richiesta-trasferimento',
  templateUrl: './popup-richiesta-trasferimento.component.html',
  styleUrls: ['./popup-richiesta-trasferimento.component.css']
})
export class PopupRichiestaTrasferimentoComponent {

  @Input() public popupRichiestaTrasferimentoOpen: boolean;

  @Output() public chiudiPopup = new EventEmitter();
  @Output() public getReportTrasferimentoOp = new EventEmitter();

  protected componentDestroyed$: Subject<boolean> = new Subject();

  constructor() { }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  public closePopup() {
    this.chiudiPopup.emit(false);
  }

  public downloadModuloTrasferimento() {
    this.getReportTrasferimentoOp.emit(true);
  }

}
