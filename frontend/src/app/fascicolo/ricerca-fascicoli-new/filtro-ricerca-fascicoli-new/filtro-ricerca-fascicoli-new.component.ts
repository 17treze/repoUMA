import { Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { PaginatorA4G } from 'src/app/a4g-common/interfaces/paginator.model';
import { PaginatorService } from 'src/app/a4g-common/services/paginator.service';
import { Paginazione } from 'src/app/a4g-common/utility/paginazione';
import { AnagraficaFascicoloService } from '../../creazione-fascicolo/anagrafica-fascicolo.service';
import { FascicoloDTO } from '../../shared/fascicolo.model';
import { FiltroRicercaFascicoli } from '../ricerca-fascicoli-new.model';

@Component({
  selector: 'app-filtro-ricerca-fascicoli-new',
  templateUrl: './filtro-ricerca-fascicoli-new.component.html',
  styleUrls: ['./filtro-ricerca-fascicoli-new.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class FiltroRicercaFascicoliNewComponent implements OnInit {

  @Input() resultsNumber: number;
  @Output() search = new EventEmitter<PaginatorA4G<Array<FascicoloDTO>>>();

  cuaList: Array<string>;
  descrizioneImpresaList: Array<string>;
  filtersFormGroup: FormGroup;

  constructor(
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private messageService: MessageService,
    private paginatorService: PaginatorService
  ) { }

  ngOnInit() {
    this.cuaList = [];
    this.descrizioneImpresaList = [];
    this.resultsNumber = 0;
    this.filtersFormGroup = new FormGroup({
      cuaa: new FormControl(),
      ragioneSociale: new FormControl(),
    });
  }

  public querySearch(event: any, origin: 'CUAA' | 'RAGIONE_SOCIALE'): void {
    let searchFilter = new FiltroRicercaFascicoli()
    if (origin === 'CUAA') {
      searchFilter.cuaa = this.filtersFormGroup.get('cuaa').value
    } else {
      searchFilter.ragioneSociale = this.filtersFormGroup.get('ragioneSociale').value
    }
    const paginazione: Paginazione = this.paginatorService.getDefaultPagination(50, 'id');
    
    this.anagraficaFascicoloService.getAnagraficaFascicolo(
      searchFilter,
      paginazione).subscribe((res: PaginatorA4G<Array<FascicoloDTO>>) => {
        if (origin === 'CUAA') {
          this.cuaList = res.risultati.map((elem: FascicoloDTO) => elem.codiCuaa);
        } else { // RAGIONE_SOCIALE
          this.descrizioneImpresaList = res.risultati.map((elem: FascicoloDTO) => elem.descDeno);
        }
      });
  }

  onSubmit() {
    let searchFilter = new FiltroRicercaFascicoli()
    if (this.filtersFormGroup.get('cuaa').value) {
      searchFilter.cuaa = this.filtersFormGroup.get('cuaa').value
    }
    if (this.filtersFormGroup.get('ragioneSociale').value) {
      searchFilter.ragioneSociale = this.filtersFormGroup.get('ragioneSociale').value
    }
    
    const paginazione: Paginazione = this.paginatorService.getDefaultPagination(50, 'id');
    this.anagraficaFascicoloService.getAnagraficaFascicolo(
      searchFilter,
      paginazione).subscribe((res: PaginatorA4G<Array<FascicoloDTO>>) => {
        if (res && res.risultati) {
          this.search.emit(res);
        }
        // elementi totali
        this.resultsNumber = res.count;
      }, error => {
        console.error(error);
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_RICERCA_FASCICOLO));
      });
      searchFilter.clean()
  }
}
