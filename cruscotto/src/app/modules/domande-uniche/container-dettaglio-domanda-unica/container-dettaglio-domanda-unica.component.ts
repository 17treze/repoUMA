import {Component, OnInit} from '@angular/core';
import {DomandeUnicheService} from '../domande-uniche.service';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {Subject} from 'rxjs';
import {DomandaUnica} from '../models/domanda-unica';
import {MenuItem} from "primeng-lts";
import {TranslateService} from "@ngx-translate/core";
import {DateService} from "../../../shared/utilities/date.service";
import {ActivateRouteSupport} from "../../../shared/utilities/activate-route.support";
import { Stack } from 'src/app/layout/back/stack';

@Component({
  selector: 'app-dettaglio-domanda-unica',
  templateUrl: './container-dettaglio-domanda-unica.component.html',
  styleUrls: ['./container-dettaglio-domanda-unica.component.scss']
})
export class ContainerDettaglioDomandaUnicaComponent implements OnInit {

  private idDomanda: number;
  public domandaUnica: DomandaUnica;
  protected componentDestroyed$: Subject<boolean> = new Subject();
  public tabs: Array<MenuItem>;
  public datiProtocollo: any;
  public sezioneSelezionata: number = 1;
  public dataProtocollazione: Date;

  constructor(private domandeUnicheService: DomandeUnicheService,
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
    if (this.idDomanda) {
        this.domandeUnicheService
          .dettaglioDomandaUnica(this.idDomanda, "sostegniAllevamento")
          .subscribe(
            domanda => {
              this.domandaUnica = domanda;
              if (this.domandaUnica && this.domandaUnica.infoGeneraliDomanda) {
                this.dataProtocollazione = this.dateService.fromIso(this.domandaUnica.infoGeneraliDomanda.dataProtocollazione);
                this.datiProtocollo = {
                  'numero': this.domandaUnica.infoGeneraliDomanda.numeroDomanda,
                  'data': this.dateService.toIsoDate(this.dataProtocollazione)
                };
              }
            }
          );
    }
  }

  private initTabs() {
    this.tabs = new Array<MenuItem>(
      this.createMenuItem('domandaUnica.riepilogoDomanda', 1),
      this.createMenuItem('domandaUnica.dettaglioIstruttoria', 2)
    )
  }

  private changeTabOfDettaglioDomandaMenu(event: any, sezione: number): void {
    this.router.navigate(['./domande-uniche/' + this.idDomanda + "/dettaglioDomandaUnica"],
      {queryParams: { [Stack.TAB_PARAM]: sezione}});
  }

  private createMenuItem(label: string, sezione: number): MenuItem {
    return {
        label: this.translateService.instant(label),
        command: (event?: any) => this.changeTabOfDettaglioDomandaMenu(event, sezione)
    };
  }

}
