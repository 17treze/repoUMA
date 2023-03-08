import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { MenuItem, SelectItem } from 'primeng/api';
import { InputFascicolo } from 'src/app/a4g-common/classi/InputFascicolo';
import { NgForm, FormGroup, FormControl } from '@angular/forms';
import { Costanti } from '../../Costanti';
import { FiltroAccoppiatiService } from '../../filtro-accoppiati.service';
import { AutocompleteElencoDomandeParams } from '../../domain/autocomplete-elenco-domande';
import { FiltroRicercaDomande } from '../../domain/filtroRicercaDomande';
import { Paginazione, SortDirection } from 'src/app/a4g-common/utility/paginazione';
import { ElencoDomandeService } from '../../services/elenco-domande.service';
import { TranslateService } from '@ngx-translate/core';
import { IstruttoriaFiltroRicercaDomande, YesNoEnum } from '../../istruttoria-shared-filter/istruttoria-filtro-ricerca-domande';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { TabSuperficiCommonComponent } from '../TabSuperficiCommonComponent';

@Component({
  selector: 'app-filtri-superfici',
  templateUrl: './filtri-superfici.component.html',
  styleUrls: ['./filtri-superfici.component.css']
})
export class FiltriSuperficiComponent implements OnInit {
  @Input() autoCompleteParams: AutocompleteElencoDomandeParams;
  @Input() resultsNumber: number;
  @Output() selectedFiltersSubmit: EventEmitter<IstruttoriaFiltroRicercaDomande> = new EventEmitter();
  public filtersFormGroup: FormGroup;
  public cuaaS: string[];
  public denominazioneS: string[];
  
  // Id: Codice Agea
  public interventi: MenuItem[] = [
    { id: '122', label: 'M8', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.soia") },
    { id: '124', label: 'M9', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.frumento")},
    { id: '123', label: 'M10', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.proteaginose")},
    { id: '125', label: 'M11', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.leguminose")},
    { id: '128', label: 'M14', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.pomodoro")},
    { id: '129', label: 'M15', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.oliviStandard")},
    { id: '132', label: 'M16', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.olivi75")},
    { id: '138', label: 'M17', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.oliviQualita")}
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

  public isShowBloccato() {
    let state: StatoIstruttoriaEnum = StatoIstruttoriaEnum[this.autoCompleteParams.statoSostegno];
    return TabSuperficiCommonComponent.isShowBloccatoByState(state);
  }
  
  public isShowErroriCalcolo() {
    let state: StatoIstruttoriaEnum = StatoIstruttoriaEnum[this.autoCompleteParams.statoSostegno];
    return TabSuperficiCommonComponent.isShowErroriByState(state);
  }

  public yesNo: SelectItem[] = [
    { label: this.translateService.instant('DOMANDA_UNICA_FILTRI.all'), value: null },
    { label: this.translateService.instant('DOMANDA_UNICA_FILTRI.yes'), value: YesNoEnum.YES },
    { label: this.translateService.instant('DOMANDA_UNICA_FILTRI.no'), value: YesNoEnum.NO }
  ];

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