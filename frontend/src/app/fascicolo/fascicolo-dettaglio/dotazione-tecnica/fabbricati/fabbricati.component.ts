import { ErrorService } from './../../../../a4g-common/services/error.service';
import { MessageService } from 'primeng/api';
import { DotazioneTecnicaService } from './../dotazione-tecnica.service';
import { PopupNuovoFabbricatoComponent } from './popup-nuovo-fabbricato/popup-nuovo-fabbricato.component';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FascicoloDettaglioService } from 'src/app/fascicolo/fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import { FascicoloDettaglio } from 'src/app/fascicolo/shared/fascicolo.model';
import { Subscription } from 'rxjs';
import { FabbricatoDto } from 'src/app/a4g-common/classi/dto/dotazione-tecnica/FabbricatoDto';
import { ActivatedRoute } from '@angular/router';
import { StatoFascicoloEnum } from 'src/app/fascicolo/creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { DotazioneTecnicaBuilderService } from 'src/app/a4g-common/services/builders/dotazione-tecnica-builder.service';
import { DettaglioFabbricatoDto } from 'src/app/a4g-common/classi/dto/dotazione-tecnica/DettaglioFabbricatoDto';

@Component({
  selector: 'app-fabbricati',
  templateUrl: './fabbricati.component.html',
  styleUrls: ['./fabbricati.component.scss']
})
export class FabbricatiComponent implements OnInit, OnDestroy {
  @ViewChild('popupNuovoFabbricato')
  popupNuovoFabbricato: PopupNuovoFabbricatoComponent;

  public cols: any[];
  public listaFabbricati: Array<FabbricatoDto>;
  public readonly: boolean;

  private idValidazione: string;
  private fabbricatiSubscription: Subscription;
  private deleteFabbricatoByIdSubscription: Subscription;
  private getFabbricatiSubscription: Subscription;
  private getFabbricatoByIdSubscription: Subscription;
  private getFabbricatoByCuaaSubscription: Subscription;

  constructor(
    private fascicoloDettaglioService: FascicoloDettaglioService,
    private route: ActivatedRoute,
    private dotazioneTecnicaService: DotazioneTecnicaService,
    private dotazioneTecnicaBuiler: DotazioneTecnicaBuilderService,
    private messageService: MessageService,
    private errorService: ErrorService
  ) { }

  ngOnInit() {
    this.idValidazione = this.route.snapshot.queryParams['id-validazione'];
    const statoFascicolo = this.fascicoloDettaglioService.fascicoloCorrente.value.stato;
    this.readonly = (statoFascicolo === StatoFascicoloEnum.VALIDATO || statoFascicolo === StatoFascicoloEnum.IN_AGGIORNAMENTO) &&
      (this.idValidazione == null) ? false : true;
    this.listaFabbricati = [];
    this.setCols();
    this.getFabbricati();
  }

  ngOnDestroy() {
    if (this.fabbricatiSubscription) {
      this.fabbricatiSubscription.unsubscribe();
    }
    if (this.deleteFabbricatoByIdSubscription) {
      this.deleteFabbricatoByIdSubscription.unsubscribe();
    }
    if (this.getFabbricatiSubscription) {
      this.getFabbricatiSubscription.unsubscribe();
    }
    if (this.getFabbricatoByIdSubscription) {
      this.getFabbricatoByIdSubscription.unsubscribe();
    }
    if (this.getFabbricatoByCuaaSubscription) {
      this.getFabbricatoByCuaaSubscription.unsubscribe();
    }
  }

  public aggiungiFabbricato() {
    this.popupNuovoFabbricato.open(false, this.getCuaa());
  }

  public goBack() {
    this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.DATI);
  }

  public deleteFabbricato(id: number) {
    this.deleteFabbricatoByIdSubscription = this.dotazioneTecnicaService.deleteFabbricato(this.getCuaa(), id.toString()).subscribe(res => {
      this.messageService.add(A4gMessages.getToast('tst-fabbricati', A4gSeverityMessage.success, "Il fabbricato è stato eliminato dal sistema!"));
      this.getFabbricati();
    }, error => this.errorService.showError(error, 'tst-fabbricati'));
  }

  public dettaglioFabbricato(idFabbricato: number) {
    console.log('Dettaglio fabricato Id:' + idFabbricato);
    this.getFabbricatoByIdSubscription = this.dotazioneTecnicaService.getFabbricatoByIdAndIdValidazione(this.getCuaa(), idFabbricato.toString(), this.idValidazione).subscribe((fabbricato: DettaglioFabbricatoDto) => {
      this.popupNuovoFabbricato.open(this.readonly, this.getCuaa(), this.dotazioneTecnicaBuiler.toDettaglioFabbricatoVM(fabbricato, fabbricato.type as any), fabbricato.datiCatastali, this.idValidazione != null ? this.idValidazione : null);
    }, error => this.errorService.showError(error, 'tst-fabbricati'));
  }

  public onCloseDialog($event?: string) {
    // dopo il salvataggio refresh della lista
    if ($event) {
      this.getFabbricati();
    }
  }

  private setCols() {
    this.cols = [
      { field: 'tipologia', header: 'Tipologia' },
      { field: 'comune', header: 'Comune' },
      { field: 'volume', header: 'Volume' },
      { field: 'superficie', header: 'Superficie' }
    ];
  }

  private getFabbricati() {
    this.getFabbricatoByCuaaSubscription = this.dotazioneTecnicaService.getFabbricatiByCuaaAndIdValidazione(this.getCuaa(), this.idValidazione)
      .subscribe((fabbricati: FabbricatoDto[]) => {
        this.listaFabbricati = [];
        if (fabbricati && fabbricati.length) {
          this.listaFabbricati = fabbricati;
          this.listaFabbricati.forEach((fabbricato: FabbricatoDto) => fabbricato.tipologia = fabbricato.tipologia.replace(/[_]/g, " "));
        }
      }, err => this.errorService.showError(err, 'tst-fabbricati'));
  }

  private getCuaa() {
    return this.fascicoloDettaglioService.fascicoloCorrente?.value?.cuaa;
  }

  public isFascicoloAttuale(): boolean {
    return this.fascicoloDettaglioService.fascicoloCorrente && this.idValidazione == null;
  }

}
