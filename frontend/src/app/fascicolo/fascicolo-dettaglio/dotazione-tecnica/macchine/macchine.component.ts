import { DettaglioMacchinaDto } from './../../../../a4g-common/classi/dto/dotazione-tecnica/DettaglioMacchinaDto';
import { PopupNuovaMacchinaComponent } from './popup-nuova-macchina/popup-nuova-macchina.component';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FascicoloDettaglioService } from 'src/app/fascicolo/fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import { FascicoloDettaglio } from 'src/app/fascicolo/shared/fascicolo.model';
import { EMPTY, Subscription } from 'rxjs';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { StatoFascicoloEnum } from 'src/app/fascicolo/creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { HttpClientMacchineService } from 'src/app/a4g-common/services/http-client-macchine.service';
import { catchError, switchMap } from 'rxjs/operators';
import { MacchinaDto } from 'src/app/a4g-common/classi/dto/dotazione-tecnica/MacchinaDto';
import { DotazioneTecnicaBuilderService } from 'src/app/a4g-common/services/builders/dotazione-tecnica-builder.service';
import { MessageService } from 'primeng-lts';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { MediatorService } from 'src/app/fascicolo/mediator.service';
import { FascicoloDaCuaa } from 'src/app/a4g-common/classi/FascicoloCuaa';
import { DettaglioMacchinaVM } from 'src/app/a4g-common/classi/viewModels/MacchinarioVM';

@Component({
  selector: 'app-macchine',
  templateUrl: './macchine.component.html',
  styleUrls: ['./macchine.component.scss']
})
export class MacchineComponent implements OnInit, OnDestroy {
  @ViewChild("popupNuovaMacchina")
  popupNuovaMacchina: PopupNuovaMacchinaComponent;

  cols: any[];
  listaMacchine: Array<MacchinaDto>;

  readonly: boolean;
  idValidazione: string;

  // Subscriptions
  macchineSubscription: Subscription;
  deleteMacchinaByIdSubscription: Subscription;
  getMacchineSubscription: Subscription;
  getMacchinaByIdSubscription: Subscription;

  constructor(
    private fascicoloDettaglioService: FascicoloDettaglioService,
    private errorService: ErrorService,
    private httpClientMacchineService: HttpClientMacchineService,
    private dotazioneTecnicaBuilderService: DotazioneTecnicaBuilderService,
    private httpClientMediatorService: MediatorService,
    private messageService: MessageService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.idValidazione = this.route.snapshot.queryParams['id-validazione'];
    const statoFascicolo = this.fascicoloDettaglioService.fascicoloCorrente.value.stato;
    this.readonly = (statoFascicolo == StatoFascicoloEnum.VALIDATO || statoFascicolo == StatoFascicoloEnum.IN_AGGIORNAMENTO) && (this.idValidazione == null) ? false : true;
    this.listaMacchine = [];
    this.setCols();
    this.getMacchine(this.idValidazione ? this.idValidazione : null);
  }

  ngOnDestroy() {
    if (this.macchineSubscription) {
      this.macchineSubscription.unsubscribe();
    }
    if (this.deleteMacchinaByIdSubscription) {
      this.deleteMacchinaByIdSubscription.unsubscribe();
    }
    if (this.getMacchineSubscription) {
      this.getMacchineSubscription.unsubscribe();
    }
    if (this.getMacchinaByIdSubscription) {
      this.getMacchinaByIdSubscription.unsubscribe();
    }
  }

  aggiungiMacchina() {
    this.popupNuovaMacchina.open(false, this.getCuaa());
  }

  goBack() {
    this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.DATI);
  }

  deleteMacchina(id: number) {
    console.log("Cancello macchina id: " + id.toString());
    this.deleteMacchinaByIdSubscription = this.httpClientMediatorService.deleteMacchinaById(this.getCuaa(), id.toString())
      .pipe(
        switchMap((res: any) => {
          const p: FascicoloDaCuaa = this.fascicoloDettaglioService.fascicoloCorrente.value;
          if (p.stato != StatoFascicoloEnum.IN_AGGIORNAMENTO) {
            p.stato = StatoFascicoloEnum.IN_AGGIORNAMENTO;
            this.fascicoloDettaglioService.fascicoloCorrente.next(p);
          }
          this.messageService.add(A4gMessages.getToast('tst-macchine', A4gSeverityMessage.success, "La macchina è stata eliminata dal sistema!"));
          return this.httpClientMacchineService.getMacchine(this.getCuaa());
        }),
        catchError((err) => {
          this.errorService.showError(err, 'tst-macchine');
          return EMPTY;
        }))
      .subscribe((macchine: Array<MacchinaDto>) => {
        if (macchine && macchine.length) {
          this.listaMacchine = macchine;
        } else {
          this.listaMacchine = [];
        }
      }, error => this.errorService.showError(error, 'tst-macchine'));
  }

  onCloseDialog($event?: string) {
    // dopo il salvataggio refresh della lista
    if ($event) {
      this.getMacchine();
    }
  }

  public isFascicoloAttuale(): boolean {
    return this.fascicoloDettaglioService.fascicoloCorrente && this.idValidazione == null;
  }

  dettaglioMacchina(idMacchina: number) {
    this.getMacchinaByIdSubscription = this.httpClientMacchineService.getMacchinaById(this.getCuaa(), idMacchina.toString(), this.idValidazione ? this.idValidazione : null).subscribe((dettaglioMacchina: DettaglioMacchinaDto) => {
      const dettaglioMacchinaVM: DettaglioMacchinaVM = this.dotazioneTecnicaBuilderService.toDettaglioMacchinaVM(dettaglioMacchina);
      this.popupNuovaMacchina.open(this.readonly, this.getCuaa(), dettaglioMacchinaVM, this.idValidazione ? this.idValidazione : null);
    }, error => this.errorService.showError(error, 'tst-macchine'));
    // this.router.navigate([id.toString() + "/dettaglio-macchina"], { relativeTo: this.route }); -> vecchia pagina
  }

  private setCols() {
    this.cols = [
      { field: 'tipologia', header: 'Classe ai fini del carburante agevolato per uso agricolo' },
      { field: 'modello', header: 'Modello' },
      { field: 'targa', header: 'Targa' },
      { field: 'alimentazione', header: 'Alimentazione' }
    ];
  }

  private getMacchine(idValidazione?: string) {
    this.getMacchineSubscription = this.httpClientMacchineService.getMacchine(this.getCuaa(), idValidazione)
      .subscribe((macchine: Array<MacchinaDto>) => {
        if (macchine && macchine.length) {
          this.listaMacchine = [];
          macchine.forEach(macchina => {
            macchina.tipologia = macchina.tipologia;
            this.listaMacchine.push(macchina);
          })
        } else {
          this.listaMacchine = [];
        }
      }, error => this.errorService.showError(error, 'tst-macchine'));
  }

  private getCuaa() {
    return this.fascicoloDettaglioService.fascicoloCorrente?.value?.cuaa;
  }

}