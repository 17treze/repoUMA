import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-popup-run-migrazione',
  templateUrl: './popup-run-migrazione.component.html',
  styleUrls: ['./popup-run-migrazione.component.css']
})
export class PopupRunMigrazioneComponent {

  @Input() public popupRunMigrazioneOpen: boolean;

  @Output() public chiudiPopup = new EventEmitter();

  protected componentDestroyed$: Subject<boolean> = new Subject();

  constructor() { }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  public closePopup() {
    this.chiudiPopup.emit(false);
  }

}
