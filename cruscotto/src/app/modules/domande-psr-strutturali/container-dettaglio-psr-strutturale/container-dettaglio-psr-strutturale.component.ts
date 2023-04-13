import {Component, OnInit} from '@angular/core';
import {DomandePsrStrutturaliService} from '../domande-psr-strutturali.service';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {Subject} from 'rxjs';
import {InfoGeneraliPSRStrutturale} from '../models/info-generali-domanda-psr-strutturale';
import {MenuItem} from "primeng-lts";
import {TranslateService} from "@ngx-translate/core";
import {DateService} from "../../../shared/utilities/date.service";
import {ActivateRouteSupport} from "../../../shared/utilities/activate-route.support";
import { Stack } from 'src/app/layout/back/stack';

@Component({
  selector: 'app-dettaglio-psr-strutturale',
  templateUrl: './container-dettaglio-psr-strutturale.component.html',
  styleUrls: ['./container-dettaglio-psr-strutturale.component.scss']
})
export class ContainerDettaglioDomandaPsrStruttComponent implements OnInit {

  public idDomanda: number;
  public domandaPsrStrutturale: InfoGeneraliPSRStrutturale[];
  protected componentDestroyed$: Subject<boolean> = new Subject();
  public tabs: Array<MenuItem>;
  public datiProtocollo: any;
  public sezioneSelezionata: number = 1;
  public dataProtocollazione: Date;

  constructor(private domandePsrStrutturaliService: DomandePsrStrutturaliService,
              private route: ActivatedRoute,
              private router: Router,
              private translateService: TranslateService,
              private dateService: DateService) {
  }

  ngOnInit() {
    this.initTabs();
    ActivateRouteSupport
      .findParamsAndExecute((params: Params) => this.callServiceDettaglioDomandaFrom(params),
                            this.componentDestroyed$,
                            this.route);
    ActivateRouteSupport
      .findQueryParamsAndExecute((queryParams: Params) => this.getSezioneSelezionataFrom(queryParams),
                                 this.componentDestroyed$,
                                 this.route);
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  private getSezioneSelezionataFrom(queryParams: Params) {
      let sezioneSelezionataFromParams: number = queryParams["sezioneSelezionata"];
      if (sezioneSelezionataFromParams != null) {
        this.sezioneSelezionata = sezioneSelezionataFromParams;
      }
  }

  private callServiceDettaglioDomandaFrom(queryParams: Params): void {
    this.idDomanda = queryParams['idDomanda'];
    // this.idDomanda = 15079;
    // this.idDomanda = 10366;
    if (this.idDomanda) {
        this.domandePsrStrutturaliService
          .getAziendaDaDomandaPSRStrutturale(this.idDomanda)
          .subscribe(
            domanda => {
              this.domandaPsrStrutturale = domanda;
              if (this.domandaPsrStrutturale) {
                this.dataProtocollazione = this.dateService.fromIso(this.domandaPsrStrutturale[0].dataProgetto);
                this.datiProtocollo = {
                  'numero': this.domandaPsrStrutturale[0].protocolloProgetto,
                  'data': this.dateService.toIsoDate(this.dataProtocollazione)
                };
              }
            }
          );
    }
  }

  private initTabs() {
    this.tabs = new Array<MenuItem>(
      this.createMenuItem('PSR-Strutturali.riepilogoDomanda', 1),
      this.createMenuItem('PSR-Strutturali.dettaglioIstruttoria', 2)
    )
  }

  private changeTabOfDettaglioDomandaMenu(event: any, sezione: number): void {
    this.router.navigate(['./psr-strutturali/' + this.idDomanda + "/dettaglioPsrStrutturali"],
      {queryParams: { [Stack.TAB_PARAM]: sezione}});
  }

  private createMenuItem(label: string, sezione: number): MenuItem {
    return {
        label: this.translateService.instant(label),
        command: (event?: any) => this.changeTabOfDettaglioDomandaMenu(event, sezione)
    };
  }

}
