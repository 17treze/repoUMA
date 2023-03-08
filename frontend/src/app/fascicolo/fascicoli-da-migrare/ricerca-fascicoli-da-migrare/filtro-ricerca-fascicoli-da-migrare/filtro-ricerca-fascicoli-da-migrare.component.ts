import { FascicoloService } from 'src/app/fascicolo/fascicolo.service';
import { Component, EventEmitter, OnInit, Output, ViewEncapsulation } from '@angular/core';
import { FormControl, FormGroup, NgForm } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { Fascicolo } from 'src/app/a4g-common/classi/Fascicolo';
import { InputFascicolo } from 'src/app/a4g-common/classi/InputFascicolo';

@Component({
  selector: 'app-filtro-ricerca-fascicoli-da-migrare',
  templateUrl: './filtro-ricerca-fascicoli-da-migrare.component.html',
  styleUrls: ['./filtro-ricerca-fascicoli-da-migrare.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class FiltroRicercaFascicoliDaMigrareComponent implements OnInit {

  @Output() search = new EventEmitter<Array<Fascicolo>>();

  public filtersFormGroup: FormGroup;

  constructor(
    private fascicoloService: FascicoloService,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.filtersFormGroup = new FormGroup({
      cuaa: new FormControl(null),
      denominazione: new FormControl(null),
    });
  }

  onSubmit(f: FormGroup) {
    if (!f.controls.cuaa.value && !f.controls.denominazione.value) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_DATI));
      return;
    }
    const searchFilter = new InputFascicolo();
    if (this.filtersFormGroup.get('cuaa').value) {
      searchFilter.cuaa = this.filtersFormGroup.get('cuaa').value;
    }
    if (this.filtersFormGroup.get('denominazione').value) {
      searchFilter.denominazione = this.filtersFormGroup.get('denominazione').value;
    }

    this.fascicoloService.ricercaFascicoli(searchFilter)
      .subscribe(
        (res: Array<Fascicolo>) => {
          this.search.emit(res);
        }, error => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_RICERCA_FASCICOLO));
        });
    searchFilter.clean();
  }
}
