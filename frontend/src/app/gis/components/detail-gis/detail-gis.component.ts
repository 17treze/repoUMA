import { ToastGisComponent } from './../toast-gis/toast-gis.component';
import { GisCostants } from './../../shared/gis.constants';
import { Component, Input, OnChanges, OnInit, ViewEncapsulation } from '@angular/core';
import { SearchResults } from '../../models/searchgis/search-results.model';
import { Detail } from './../../models/searchgis/detail';
import { RichiestaModificaSuoloService } from '../../services/richiesta-modifica-suolo.service';
import { TipoInterventoColturale } from '../../models/detailgis/tipoInterventoColturale.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Utente } from '../../../auth/user';
import { MapService } from '../mappa/map.service';
import { StatiRichiesta, StatiRichiestaoDecode } from '../../shared/StatiRichiesta.enum';
import { DatePipe } from '@angular/common';
import { PanelEvent } from '../../shared/PanelEvent';
import { AllegatiEvent } from '../../shared/AllegatiEvent';
import { DataService } from 'src/app/a4g-common/services/data.service';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'detail-gis',
  templateUrl: './detail-gis.component.html',
  styleUrls: ['./detail-gis.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DetailGisComponent implements OnInit, OnChanges {
  @Input() searchResults: SearchResults[];
  @Input() detailResults: Detail;
  @Input() showDetail: boolean;
  @Input() detailIndex: any;
  @Input() datiAggiuntivi: any;
  @Input() allegatiCount: any;
  @Input() profiloUtente: any;
  @Input() showResults: any;
  @Input() display: any;

  utenteLoggato: Utente = JSON.parse(sessionStorage.getItem('user'));
  codiceFiscale: string = this.utenteLoggato.codiceFiscale;
  dismissible: false;
  blockScroll: true;
  detailForm: FormGroup;
  submitted = false;
  detailData: any;
  selectTipoInterventoColturale: TipoInterventoColturale;
  visibileOrtofoto: boolean;
  tipoInterventoColturale: any;
  periodoInterventoDataInizioMapped: any;
  periodoInterventoDataFineMapped: any;
  statoLavorabile: boolean;
  sezioniCatastali: any;
  response: SearchResults[];
  allegati: any;
  initFile: boolean = true;
  statiRichiesta = StatiRichiesta;
  listaComuni = JSON.parse(sessionStorage.getItem('listaComuni_TN'));
  listStatiRichiestaAggiornamentoAbilitato = [StatiRichiesta.LAVORABILE, StatiRichiesta.CONCLUSA];

  constructor(private formBuilder: FormBuilder, private richiestaModificaSuoloService: RichiestaModificaSuoloService,
    public gisCostants: GisCostants, private toastComponent: ToastGisComponent, private mapService: MapService,
    private datePipe: DatePipe, public panelEvent: PanelEvent, public allegatiEvent: AllegatiEvent,
    private dataService: DataService) {
  }

  ngOnInit() {
    this.detailForm = this.formBuilder.group({
      tipoInterventoColturale: ['', [Validators.nullValidator]],
      visibileOrtofoto: ['', [Validators.nullValidator]],
      statoLavorabile: ['', [Validators.nullValidator]],
      periodoInterventoDataInizioMapped: ['', [Validators.nullValidator]],
      periodoInterventoDataFineMapped: ['', [Validators.nullValidator]]
    });

    this.dataService.richiesta.allegato.uploaded.subscribe(value => this.loadAllegatiCount());
    this.dataService.richiesta.allegato.deleted.subscribe(value => this.loadAllegatiCount());
  }

  ngOnChanges() {
    this.mappedStatoRichiesta();
    this.codiceFiscale = this.codiceFiscale.split('@')[0];
    setTimeout(() => {

      if (this.detailResults && this.detailResults.datiAggiuntivi) {
        // Decodificare il comune
        this.statoLavorabile = this.detailResults.stato === StatiRichiesta.APERTA ? false : true;

        let x = this.detailResults['comuniMapped'].cod;

        for (let i = 0; i < this.detailResults['comuniMapped'].length; i++) {
          const decodificaCodiceComune = this.listaComuni.risultati.filter(x => x.codice === this.detailResults['comuniMapped'][i].cod);
          if (decodificaCodiceComune && decodificaCodiceComune[0]) {
            this.detailResults['comuniMapped'][i].denominazione = decodificaCodiceComune[0].denominazione;
          }
        }
        if (this.detailResults.stato === StatiRichiesta.APERTA && this.profiloUtente.profilo === 'caa') {
          this.detailForm.controls['statoLavorabile'].enable();
        } else {
          this.detailForm.controls['statoLavorabile'].disable();
        }
      }
    }, 500);
  }

  mappedStatoRichiesta() {
    if (this.detailResults && this.detailResults.stato) {
      this.detailResults['statoRichiestaMappato'] = StatiRichiestaoDecode.decode(this.detailResults.stato);
    }
  }

  setInitAllegato(val) {
    this.initFile = val;
    return val;
  }

  showDialog() {
    this.allegatiEvent.params = {
      'numeroElementiPagina': 5,
      'pagina': 0,
      'triggerFromDetail': true
    };
    this.allegatiEvent.displayAllegati = true;
  }

  get f() { return this.detailForm.controls; }
  submitData(): void {
    if (!this.listStatiRichiestaAggiornamentoAbilitato.includes(this.detailResults.stato) && this.profiloUtente.profilo === 'caa') {

      const detailObj = Object.assign([], this.detailResults);

      // date format YYYY-MM-DD
      if (this.detailResults.datiAggiuntivi.periodoIntervento.dataInizio) {

        let dataInizio = new Date(this.detailResults.datiAggiuntivi.periodoIntervento.dataInizio);
        let diff = dataInizio.getTimezoneOffset() / 60;
        dataInizio.setHours(0 - diff);
        detailObj.datiAggiuntivi.periodoIntervento.dataInizio = dataInizio;

      }

      if (this.detailResults.datiAggiuntivi.periodoIntervento.dataFine) {
        let dataFine = new Date(this.detailResults.datiAggiuntivi.periodoIntervento.dataFine);
        let diff = dataFine.getTimezoneOffset() / 60;
        dataFine.setHours(0 - diff);
        detailObj.datiAggiuntivi.periodoIntervento.dataFine = dataFine;

      }

      const putParams: Record<string, Detail> = {
        1: {
          id: detailObj.id,
          utente: this.codiceFiscale,
          data: detailObj.data,
          tipo: detailObj.tipo,
          stato: this.statoLavorabile === true ? StatiRichiesta.LAVORABILE : detailObj.stato,
          esito: detailObj.esito,
          aziendaAgricola: detailObj.aziendaAgricola,
          campagna: detailObj.campagna,
          datiAggiuntivi: detailObj.datiAggiuntivi
        }
      };
      this.richiestaModificaSuoloService.putRichiestaModificaSuolo(putParams[1]).subscribe((respone: any) => {
        this.response = respone;
        if (this.response['status'] === 200) {
          this.toastComponent.showSuccess();
          if (this.statoLavorabile === true) {
            detailObj.stato = StatiRichiesta.LAVORABILE;
            this.detailResults.stato = StatiRichiesta.LAVORABILE;
            this.detailResults['statoRichiestaMappato'] = StatiRichiestaoDecode.decode(this.detailResults.stato);
          }
          this.searchResults[this.detailIndex].stato = this.detailResults.stato;
          this.searchResults[this.detailIndex].datiAggiuntivi = this.detailResults.datiAggiuntivi;
          this.searchResults[this.detailIndex]['statoRichiestaMappato'] = StatiRichiestaoDecode.decode(this.searchResults[this.detailIndex].stato);
        } else {
          this.toastComponent.showError();
        }
      },
        (err) => {
          this.toastComponent.showErrorGenerico(err.error);
        });
    } else {
      this.toastComponent.showErrorEditRichiestaModificaSuolo(this.detailResults.stato);

    }
  }

  loadAllegatiCount() {
    this.richiestaModificaSuoloService
      .getAllegati(this.detailResults.id, this.allegatiEvent.params)
      .subscribe((response: any) => {
        this.allegatiEvent.allegatiCount = response.count;
      });
  }

  loadAllegatiDichiarato(idDichiarato) {
    this.allegatiEvent.params = {
      'numeroElementiPagina': 5,
      'pagina': 0,
      'triggerFromDetail': true
    };
    this.richiestaModificaSuoloService.getAllegatiDichiarato(idDichiarato, this.allegatiEvent.params).subscribe((response: any) => {
      this.allegatiEvent.allegati = response;
    });
  }

  centerMap(extent) {
    this.mapService.centerMap(extent);
  }
}