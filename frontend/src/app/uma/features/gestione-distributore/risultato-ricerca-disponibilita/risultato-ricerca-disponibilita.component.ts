import { FormatConverterService } from './../../../shared-uma/services/format-converter.service';
import { StatoDichiarazioneConsumiEnum } from './../../../../a4g-common/classi/enums/uma/StatoDichiarazioneConsumi.enum';
import { HttpClientDichiarazioneConsumiUmaService } from './../../../core-uma/services/http-client-dichiarazione-consumi-uma.service';
import { Component, Input, OnInit, OnChanges, SimpleChanges, OnDestroy, Output, EventEmitter } from '@angular/core';
import { DichiarazioneConsumiDto } from 'src/app/uma/core-uma/models/dto/DichiarazioneConsumiDto';
import { catchError, switchMap } from 'rxjs/operators';
import { HttpClientTrasferimentiCarburanteService } from 'src/app/uma/core-uma/services/http-client-trasferimenti-carburante.service';
import { HttpClientDomandaUmaService } from 'src/app/uma/core-uma/services/http-client-domanda-uma.service';
import { forkJoin, EMPTY, Subscription } from 'rxjs';
import { CarburanteDto } from 'src/app/uma/core-uma/models/dto/CarburanteDto';
import { PrelievoDto } from 'src/app/uma/core-uma/models/dto/PrelievoDto';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { PaginatorA4G } from 'src/app/a4g-common/interfaces/paginator.model';
import { CarburanteTotale } from 'src/app/uma/core-uma/models/dto/CarburanteTotale';
import { TrasferimentoDto } from 'src/app/uma/core-uma/models/dto/CarburanteRicevutoDto';
import * as _ from 'lodash';
@Component({
  selector: 'app-risultato-ricerca-disponibilita',
  templateUrl: './risultato-ricerca-disponibilita.component.html',
  styleUrls: ['./risultato-ricerca-disponibilita.component.scss']
})
export class RisultatoRicercaDisponibilitaComponent implements OnInit, OnChanges, OnDestroy {
  @Input() querySearch: { cuaa: string, idDomanda: string };
  @Output() updatePrelevabile = new EventEmitter<CarburanteDto>();

  cols: any[];
  rows: any[];
  dichiarazioneConsumiDto: DichiarazioneConsumiDto;
  richiestaCarburanteDto: RichiestaCarburanteDto;
  denominazioneAzienda: string;

  // Subscriptions
  getDomandaSubscription: Subscription;

  constructor(
    private httpClientTrasferimentiCarburanteService: HttpClientTrasferimentiCarburanteService,
    private httpClientDomandaUmaService: HttpClientDomandaUmaService,
    private httpClientDichiarazioneConsumiUmaService: HttpClientDichiarazioneConsumiUmaService,
    private errorService: ErrorService,
    private formatConverterService: FormatConverterService
  ) { }

  ngOnInit() {
    this.cols = [
      { header: 'Disponibilità carburante', width: '70%', align: "left" },
      { header: 'Gasolio', width: '10%', align: "center" },
      { header: 'Benzina', width: '10%', align: "center" },
      { header: 'Gasolio serre', width: '10%', align: "center" }
    ];
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes && changes.querySearch && changes.querySearch.currentValue && changes.querySearch.currentValue.idDomanda != null) {
      this.getValori(changes.querySearch.currentValue.idDomanda);
    }
  }

  ngOnDestroy(): void {
    if (this.getDomandaSubscription) {
      this.getDomandaSubscription.unsubscribe();
    }
  }

  getValori(idDomanda: string) {
    this.getDomandaSubscription = this.httpClientDomandaUmaService.getDomandaById(idDomanda)
      .pipe(
        catchError((err) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap((richiestaCarburante: RichiestaCarburanteDto) => {
          this.richiestaCarburanteDto = this.toSumGasolioAndGasolioTerzi(richiestaCarburante);
          this.denominazioneAzienda = richiestaCarburante.denominazione;
          const getCarburanteRicevuto$ = this.httpClientTrasferimentiCarburanteService.getCarburanteRicevuto(this.richiestaCarburanteDto.cuaa, this.richiestaCarburanteDto.campagna.toString());
          const getCarburantePrelevato$ = this.httpClientDomandaUmaService.getPrelieviByCuaaAndCampagna(richiestaCarburante.cuaa, richiestaCarburante.campagna.toString());
          const getResiduoInzioAnno$ = this.httpClientDichiarazioneConsumiUmaService.getResiduoAnnoPrecedente(this.richiestaCarburanteDto.cuaa, this.richiestaCarburanteDto.campagna - 1, [StatoDichiarazioneConsumiEnum.PROTOCOLLATA]);
          return forkJoin([getCarburanteRicevuto$, getCarburantePrelevato$, getResiduoInzioAnno$]);
        }),
        catchError((err) => {
          this.errorService.showError(err);
          return EMPTY;
        }))
      .subscribe(([ricevuti, prelevati, consumiAnnoPrecedente]:
        [CarburanteTotale<TrasferimentoDto>, CarburanteTotale<PrelievoDto>, Array<DichiarazioneConsumiDto>]) => {
        const residuo: CarburanteDto = (consumiAnnoPrecedente && consumiAnnoPrecedente.length && consumiAnnoPrecedente[0].rimanenza) ? consumiAnnoPrecedente[0].rimanenza : this.initialiazeCarburanteDto();
        // Gasolio = Gasolio + Gasolio Terzi
        residuo.gasolio = this.formatConverterService.toNumber(residuo.gasolio) + this.formatConverterService.toNumber(residuo.gasolioTerzi);
        const assegnatoNettoResiduo = this.setAssegnatoNettoAlResiduo(residuo);
        const ricevuto = this.setCarburanteRicevuto(ricevuti.totale);
        const prelevato = this.setCarburantePrelevato(prelevati.totale);
        const prelevabile = this.setDisponibile(assegnatoNettoResiduo, prelevato, ricevuto);
        this.updatePrelevabile.emit(prelevabile);
        this.setRows(assegnatoNettoResiduo, ricevuto, prelevato, prelevabile);
      }, error => this.errorService.showError(error));
  }

  setRows(assegnatoNettoResiduo: CarburanteDto, ricevuto: CarburanteDto, prelevato: CarburanteDto, prelevabile: CarburanteDto) {
    this.rows = [
      { header: 'Assegnato al netto del residuo', gasolio: assegnatoNettoResiduo.gasolio, benzina: assegnatoNettoResiduo.benzina, gasolioSerre: assegnatoNettoResiduo.gasolioSerre },
      { header: 'Prelevato', gasolio: prelevato.gasolio, benzina: prelevato.benzina, gasolioSerre: prelevato.gasolioSerre },
      { header: 'Ricevuto', gasolio: ricevuto.gasolio, benzina: ricevuto.benzina, gasolioSerre: ricevuto.gasolioSerre },
      { header: 'Prelevabile', gasolio: prelevabile.gasolio, benzina: prelevabile.benzina, gasolioSerre: prelevabile.gasolioSerre }
    ];
  }

  private toSumGasolioAndGasolioTerzi(richiesta: RichiestaCarburanteDto): RichiestaCarburanteDto {
    let _richiesta: RichiestaCarburanteDto = _.cloneDeep(richiesta);
    _richiesta.carburanteRichiesto.gasolio = this.formatConverterService.toNumber(_richiesta.carburanteRichiesto.gasolio) + this.formatConverterService.toNumber(_richiesta.carburanteRichiesto.gasolioTerzi);
    return _richiesta;
  }

  private setAssegnatoNettoAlResiduo(residuo: CarburanteDto): CarburanteDto {
    if (!this.richiestaCarburanteDto || !this.richiestaCarburanteDto.carburanteRichiesto) {
      return this.initialiazeCarburanteDto();
    }
    const richiesto = this.richiestaCarburanteDto.carburanteRichiesto;
    const assegnatoNettoResiduo = new CarburanteDto();
    assegnatoNettoResiduo.gasolio = richiesto.gasolio - residuo.gasolio;
    assegnatoNettoResiduo.benzina = richiesto.benzina - residuo.benzina;
    assegnatoNettoResiduo.gasolioSerre = richiesto.gasolioSerre - residuo.gasolioSerre;

    return assegnatoNettoResiduo;
  }

  private setCarburanteRicevuto(ricevuto: CarburanteDto): CarburanteDto {
    if (!ricevuto) {
      return this.initialiazeCarburanteDto();
    }
    const sommaRicevuto = new CarburanteDto();
    sommaRicevuto.gasolio = ricevuto.gasolio;
    sommaRicevuto.benzina = ricevuto.benzina;
    sommaRicevuto.gasolioSerre = ricevuto.gasolioSerre;
    return sommaRicevuto;
  }

  private setCarburantePrelevato(prelevato: CarburanteDto): CarburanteDto {
    if (!prelevato) {
      return this.initialiazeCarburanteDto();
    }

    const sommaPrelevato = new CarburanteDto();
    sommaPrelevato.gasolio = prelevato.gasolio;
    sommaPrelevato.benzina = prelevato.benzina;
    sommaPrelevato.gasolioSerre = prelevato.gasolioSerre;

    return sommaPrelevato;
  }

  private setDisponibile(assegnato: CarburanteDto, prelevato: CarburanteDto, ricevuto: CarburanteDto): CarburanteDto {
    const disponibile = new CarburanteDto();
    disponibile.gasolio = assegnato.gasolio - prelevato.gasolio - ricevuto.gasolio;
    disponibile.benzina = assegnato.benzina - prelevato.benzina - ricevuto.benzina;
    disponibile.gasolioSerre = assegnato.gasolioSerre - prelevato.gasolioSerre - ricevuto.gasolioSerre;
    return disponibile;
  }

  private initialiazeCarburanteDto(): CarburanteDto {
    return { gasolio: 0, benzina: 0, gasolioSerre: 0, gasolioTerzi: 0 };
  }

}
