import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { Paginazione, SortDirection } from 'src/app/a4g-common/utility/paginazione';
import { AutocompleteElencoDomandeParams } from '../../domain/autocomplete-elenco-domande';
import { IstruttoriaFiltroRicercaDomande, YesNoEnum } from '../../istruttoria-shared-filter/istruttoria-filtro-ricerca-domande';
import { ElencoDomandeService } from '../../services/elenco-domande.service';
import { MenuItem, SelectItem } from 'primeng/api';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { TabAzCommonComponent } from '../TabAzCommonComponent';

@Component({
  selector: 'app-filtri-zootecnia',
  templateUrl: './filtri-zootecnia.component.html',
  styleUrls: ['./filtri-zootecnia.component.css']
})
export class FiltriZootecniaComponent implements OnInit {

  public filtersFormGroup: FormGroup;

  @Input() autoCompleteParams: AutocompleteElencoDomandeParams;
  @Input() resultsNumber: number;
  @Output() selectedFiltersSubmit: EventEmitter<IstruttoriaFiltroRicercaDomande> = new EventEmitter();
  cuaaS: string[];
  denominazioneS: string[];
  sostegno: string = 'ACC_SUPERFICI';
  
  // Id: Codice Agea
  public interventi: MenuItem[] = [
    { id: '310', label: 'M1', title: 'M1 - Vacche da latte (310)' },
    { id: '311', label: 'M2', title: 'M2 - Vacche da latte in zone di montagna (311)' },
    { id: '313', label: 'M4', title: 'M4 - Vacche nutrici (313)' },
    { id: '315', label: 'M5', title: 'M5- Bovini macellati (315)' },
    { id: '320', label: 'M6', title: 'M6 - Agnelle (320)' },
    { id: '321', label: 'M7', title: 'M7 - Ovicaprini macellati (321)' },
    { id: '316', label: 'M19', title: 'M19 - Bovini macellati - Allevati 12 mesi (316)' },
    { id: '318', label: 'M19', title: 'M19 - Bovini macellati - Etichettatura (318)' },
    { id: '322', label: 'M20', title: 'M20 - Vacche nutrici non iscritte (322)' } 
  ];

  public yesNo: SelectItem[] = [
    { label: this.translateService.instant('DOMANDA_UNICA_FILTRI.all'), value: null },
    { label: this.translateService.instant('DOMANDA_UNICA_FILTRI.yes'), value: YesNoEnum.YES },
    { label: this.translateService.instant('DOMANDA_UNICA_FILTRI.no'), value: YesNoEnum.NO }
  ];
  

  constructor(
    private elencoDomandeService: ElencoDomandeService,
    private translateService: TranslateService
    ) {
      let filtersFormControls = {
        'cuaaFormControl': new FormControl(),
        'denominazioneFormControl': new FormControl(),
        'numeroDomandaFormControl': new FormControl(),
        'interventiFormControl': new FormControl(this.interventi),
        'selectedBloccataBoolFormControl': new FormControl(null),
        'selectedErroreCalcoloFormControl': new FormControl(null)
      };
      this.filtersFormGroup = new FormGroup(filtersFormControls);
  }
  
  ngOnInit() {
  }
  
  public isShowErroriCalcolo() {
    let state: StatoIstruttoriaEnum = StatoIstruttoriaEnum[this.autoCompleteParams.statoSostegno];
    return TabAzCommonComponent.isShowErroriByState(state);
  }

  public isShowBloccato() {
    let state: StatoIstruttoriaEnum = StatoIstruttoriaEnum[this.autoCompleteParams.statoSostegno];
    return TabAzCommonComponent.isShowBloccatoByState(state);
  }

  public inputFilterRagioneSocialeUpdate(event: any): void {
    let paginazione: Paginazione = Paginazione.of(
      0, 50, 'id', SortDirection.ASC);
    this.elencoDomandeService.getRagioneSocialeSuggestionsAutocomplete(
      this.autoCompleteParams.statoSostegno,
      this.autoCompleteParams.sostegno,
      this.autoCompleteParams.annoCampagna,
      event.query,
      this.autoCompleteParams.tipo,
      paginazione).subscribe(res => this.denominazioneS = res.risultati);
  }

  public inputFilterCuaaUpdate(event: any) {
    if (event && event.query && event.query.length <= 2) {
      return;
    }
    let paginazione: Paginazione = Paginazione.of(
      0, 50, 'id', SortDirection.ASC);
    this.elencoDomandeService.getCuaaSuggestionsAutocomplete(
      this.autoCompleteParams.statoSostegno,
      this.autoCompleteParams.sostegno,
      this.autoCompleteParams.annoCampagna,
      event.query,
      this.autoCompleteParams.tipo,
      paginazione).subscribe(res => this.cuaaS = res.risultati);
  }

  public onSubmit() {
    let istruttoriaFiltroRicercaDomande: IstruttoriaFiltroRicercaDomande = new IstruttoriaFiltroRicercaDomande();
    istruttoriaFiltroRicercaDomande.cuaa = this.filtersFormGroup.controls.cuaaFormControl.value;
    let numeroDomandaFilter = this.filtersFormGroup.controls.numeroDomandaFormControl.value;
    if (!numeroDomandaFilter) {
      istruttoriaFiltroRicercaDomande.numero_domanda = null;
    } else {
      istruttoriaFiltroRicercaDomande.numero_domanda = Number.parseInt(numeroDomandaFilter);
    }
    istruttoriaFiltroRicercaDomande.denominazione = this.filtersFormGroup.controls.denominazioneFormControl.value;
    istruttoriaFiltroRicercaDomande.interventi = this.filtersFormGroup.controls.interventiFormControl.value.map(selected => selected.id);
    istruttoriaFiltroRicercaDomande.bloccataBool = this.filtersFormGroup.controls.selectedBloccataBoolFormControl.value;
    istruttoriaFiltroRicercaDomande.erroreCalcolo = this.filtersFormGroup.controls.selectedErroreCalcoloFormControl.value;
    this.selectedFiltersSubmit.emit(istruttoriaFiltroRicercaDomande);
  }

}
