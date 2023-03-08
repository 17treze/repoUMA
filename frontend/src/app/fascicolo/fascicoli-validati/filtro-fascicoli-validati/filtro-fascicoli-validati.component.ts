import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Localization } from 'src/app/a4g-common/utility/localization';
import { FascicoloDettaglioService } from '../../fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import { FascicoloDettaglio } from '../../shared/fascicolo.model';

export class FascicoliValidatiFilterDto {
  annoValidazione: number;
}

@Component({
  selector: 'app-filtro-fascicoli-validati',
  templateUrl: './filtro-fascicoli-validati.component.html',
  styleUrls: ['./filtro-fascicoli-validati.component.scss']
})
export class FiltroFascicoliValidatiComponent implements OnInit {

  @Input() public resultsNumber: number;
  @Output() public search = new EventEmitter<FascicoliValidatiFilterDto>();
  public filtersFormGroup: FormGroup;
  public language = Localization.itCalendar();

  constructor(
    private fascicoloDettaglioService: FascicoloDettaglioService
  ) { }

  ngOnInit() {
    this.filtersFormGroup = new FormGroup({
      annoValidazione: new FormControl()
    });
  }

  onSubmit() {
    const searchFilter = new FascicoliValidatiFilterDto();
    const annoValidazione = this.filtersFormGroup.get('annoValidazione').value;
    if (annoValidazione) {
      searchFilter.annoValidazione = annoValidazione;
    }
    this.search.emit(searchFilter);
  }

  public handleClick() {
    this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.DATI);
  }
}
