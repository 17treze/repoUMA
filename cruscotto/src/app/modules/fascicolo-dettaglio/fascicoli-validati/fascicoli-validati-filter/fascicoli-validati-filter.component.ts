import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { DateSupport } from 'src/app/shared/utilities/date-support';
import { Localization } from 'src/app/shared/utilities/localization';

export class FascicoliValidatiFilterDto {
  dataValidazione: string;
}

@Component({
  selector: 'app-fascicoli-validati-filter',
  templateUrl: './fascicoli-validati-filter.component.html',
  styleUrls: ['./fascicoli-validati-filter.component.css']
})
export class FiltroFascicoliValidatiComponent implements OnInit {

  @Input() public resultsNumber: number;
  @Output() public search = new EventEmitter<FascicoliValidatiFilterDto>();
  public filtersFormGroup: FormGroup;
  public language = Localization.itCalendar();

  constructor() {}

  ngOnInit() {
    this.filtersFormGroup = new FormGroup({
      dataValidazione: new FormControl()
    });
  }

  onSubmit() {
    const searchFilter = new FascicoliValidatiFilterDto();
    const dataValidazione: Date = this.filtersFormGroup.get('dataValidazione').value;
    if (dataValidazione) {
      searchFilter.dataValidazione = DateSupport.convertToPatternDate(dataValidazione);
    }
    this.search.emit(searchFilter);
  }
}
