import { Component, OnInit } from "@angular/core";
import { AntimafiaService } from "src/app/fascicolo/antimafia/antimafia.service";
import { StatoDichiarazioneFilter } from "src/app/fascicolo/antimafia/classi/statoDichiarazioneFilter";
import { StatoDichiarazione } from "src/app/fascicolo/antimafia/classi/statoDichiarazioneEnum";
import { StatoDichiarazioneCount } from "src/app/fascicolo/antimafia/classi/statoDichiarazioneCount";
import { Labels } from 'src/app/app.labels';
import { IstruttoriaAntimafiaService } from '../istruttoria-antimafia.service';
import { TipoProcesso } from "../dto/TipoProcesso";
import { StatoProcesso } from "../dto/StatoProcesso";
import { A4gMessages } from "src/app/a4g-common/a4g-messages";
import { timer, Subscription } from "rxjs";
import { Configuration } from "src/app/app.constants";
import { HttpClient } from "@angular/common/http";
import { switchMap } from "rxjs/operators";
import { ProcessiDiControllo } from "../../istruttoria-pac1420/domandaUnica/sostegno-shared/dto/processi-di-controllo";
import { TranslateService } from "@ngx-translate/core";

@Component({
  selector: "app-certificazioni-antimafia",
  templateUrl: "./certificazioni-antimafia.component.html",
  styleUrls: ["./certificazioni-antimafia.component.css"]
})
export class CertificazioniAntimafiaComponent implements OnInit {
  constructor(private antimafiaService: AntimafiaService,
    private istruttoriaAntimafiaService: IstruttoriaAntimafiaService,
    private http: HttpClient,
    private _configuration: Configuration,
    private translateService: TranslateService) { }
  index: number;
  itemBadges: StatoDichiarazioneCount;
  viewState: StatoDichiarazione;
  labels = Labels;
  processiInCorso: string = Labels.NESSUN_PROCESSO_DI_CONTROLLO;
  refresh: boolean = false;
  controlloAntimafia = TipoProcesso.CONTROLLO_ANTIMAFIA;
  processoImportaDomandePsr = TipoProcesso.IMPORTA_DOMANDE_PSR_SUPERFICIE;

  timer: Subscription;
  timeout: number = 30000;

  ngOnInit() {
    this.loadCounters();
    this.index = this.istruttoriaAntimafiaService.getTabIndex();
    this.newCheckStatoAvanzamento();
  }
  //todo:  NG ON DESTROY UNSUBSCRIBE TUTTO
  ngOnDestroy(): void {
    if (!A4gMessages.isUndefinedOrNull(this.timer)) {
      this.timer.unsubscribe();
    }
    this.refresh = false;
  }

  aggiornaCounters(event: Event) {
    this.loadCounters();
  }

  private loadCounters() {
    this.itemBadges = new StatoDichiarazioneCount();
    const statiDichiarazioneFilter: StatoDichiarazioneFilter = {
      statiDichiarazione: [
        StatoDichiarazione.BOZZA,
        StatoDichiarazione.PROTOCOLLATA,
        StatoDichiarazione.CONTROLLATA,
        StatoDichiarazione.CONTROLLO_MANUALE,
        StatoDichiarazione.RIFIUTATA,
        StatoDichiarazione.POSITIVO,
        StatoDichiarazione.VERIFICA_PERIODICA
      ]
    };
    this.antimafiaService
      .getStatoDichiarazioniCount(statiDichiarazioneFilter)
      .subscribe(next => {
        console.log("NEXT = " + JSON.stringify(next));
        this.itemBadges = next;
      });
  }

  private newCheckStatoAvanzamento() {
      this.istruttoriaAntimafiaService.getProcessiDiControllo(TipoProcesso.CONTROLLO_ANTIMAFIA).pipe(
        switchMap((ctrlAmf: ProcessiDiControllo[]) => {
          if (ctrlAmf) {
            this.runStatoAvanzamentoProcesso(Number(ctrlAmf[0].datiElaborazioneProcesso.daElaborare), TipoProcesso.CONTROLLO_ANTIMAFIA);
            return null;
          }
          return this.istruttoriaAntimafiaService.getProcessiDiControllo(TipoProcesso.IMPORTA_DOMANDE_PSR_SUPERFICIE);
        })).subscribe((impPsr: ProcessiDiControllo[]) => {
          if (impPsr) {
            this.runStatoAvanzamentoProcesso(Number(impPsr[0].datiElaborazioneProcesso.daElaborare), TipoProcesso.IMPORTA_DOMANDE_PSR_SUPERFICIE);
          }
          return null;
        });
  }

  runStatoAvanzamentoProcesso(event: number, tipoProcesso: TipoProcesso) {
    let domandeParziali: number = 0;
    let domandeTotali: number = event;

    this.processiInCorso = A4gMessages.procesioDiControlloIstruttoria(String(this.translateService.instant("PROCESSI." + tipoProcesso)), String(domandeParziali), String(domandeTotali));
    let params = encodeURIComponent(JSON.stringify({ tipoProcesso: tipoProcesso, statoProcesso: StatoProcesso.RUN }));
    this.timer = timer(0, this.timeout).subscribe((numRipetizioni) => {
      this.http.get(this._configuration.urlControlloAntimafiaStatoAvanzamento + params).subscribe(
        (statoAvanzamento: any) => {
          if (numRipetizioni > 1 && (String(statoAvanzamento.esito) === Labels.NESSUN_PROCESSO_DI_CONTROLLO)) {
            this.timer.unsubscribe();
            this.processiInCorso = Labels.NESSUN_PROCESSO_DI_CONTROLLO;
            this.refresh = true;
            this.loadCounters();
            return;
          }
          if (!A4gMessages.isUndefinedOrNull(statoAvanzamento.totale)) {
            domandeParziali = statoAvanzamento.totale;
            this.processiInCorso = A4gMessages.procesioDiControlloIstruttoria(String(this.translateService.instant("PROCESSI." + tipoProcesso)), String(domandeParziali), String(domandeTotali));
          }
          if (domandeParziali >= domandeTotali) {
            this.timer.unsubscribe();
            this.processiInCorso = Labels.NESSUN_PROCESSO_DI_CONTROLLO;
            this.refresh = true;
            this.loadCounters();
          }
        }, err => {
          this.timer.unsubscribe();
          this.processiInCorso = Labels.NESSUN_PROCESSO_DI_CONTROLLO;
        }
      );

    });
  }
}
