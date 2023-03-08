import { EredeDto } from './../creazione-fascicolo/dto/EredeDto';
import { Component, EventEmitter, Input, OnInit, Output, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subject } from 'rxjs';
import { AnagraficaFascicoloService } from '../creazione-fascicolo/anagrafica-fascicolo.service';

@Component({
  selector: 'app-popup-erede',
  templateUrl: './popup-erede.component.html',
  styleUrls: ['./popup-erede.component.css']
})
export class PopupEredeComponent implements OnInit, OnDestroy {

  @Input() public popupEredeOpen: boolean;
  @Input() public cuaa: string;
  @Input() public idValidazione: number;
  @Input() public erede: EredeDto;
  @Output() public chiudiPopup = new EventEmitter();
  @Output() public saveErede = new EventEmitter();

  public eredeFormGroup: FormGroup;
  protected componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    protected anagraficaFascicoloService: AnagraficaFascicoloService
  ) { }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  ngOnInit() {
    this.setEredeFormGroup();
  }

  private setEredeFormGroup() {
    this.eredeFormGroup = new FormGroup({
      cfErede: new FormControl(
        this.erede ? this.erede.cfErede : '',
        [Validators.required]
      ),
    });
  }

  public closePopup() {
    this.chiudiPopup.emit(false);
  }

  public onSaveErede() {
    this.saveErede.emit(this.eredeFormGroup.value);
  }

}
