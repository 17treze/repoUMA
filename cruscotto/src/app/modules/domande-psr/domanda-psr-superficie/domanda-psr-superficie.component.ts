import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { DomandaPsr } from '../models/domanda-psr';
import { DomandePsrService } from '../domande-psr.service';
import { MenuItem } from 'primeng-lts';
import { TranslateService } from '@ngx-translate/core';
import { ActivateRouteSupport } from '../../../shared/utilities/activate-route.support';
import { Subject } from 'rxjs';
import { IFascicolo } from '../../domande-uniche/models/fascicolo.model';
import { AziendaAgricolaService } from '../../../shared/services/azienda-agricola.service';
import { CodiceMisureIntervento } from '../models/codiceMisureInterventoEnum';
import { ListaDomandePsrComponent } from '../lista-domande-psr/lista-domande-psr.component';
import { Operazione } from '../models/operazione';
import { Stack } from 'src/app/layout/back/stack';

@Component({
  selector: 'app-domanda-psr-superficie',
  templateUrl: './domanda-psr-superficie.component.html',
  styleUrls: ['./domanda-psr-superficie.component.css']
})
export class DomandaPsrSuperficieComponent implements OnInit {

  public idDomanda?: number;
  domandaPsr?: DomandaPsr;
  tabs: Array<MenuItem>;
  sezioneSelezionata = 1;
  domandaUnica: IFascicolo;
  protected componentDestroyed$: Subject<boolean> = new Subject();

  constructor(private route: ActivatedRoute,
              private aziendaAgricolaService: AziendaAgricolaService,
              private domandePsrService: DomandePsrService,
              private translateService: TranslateService,
              private router: Router) {
  }

  ngOnInit() {
    this.initTabs();
    this.idDomanda = Number(this.route.snapshot.paramMap.get('idDomanda'));
    this.domandePsrService.getDomandaPsrByNumeroDomanda(this.idDomanda).pipe().subscribe(domandaPsr => {
      this.domandaPsr = domandaPsr;
      ListaDomandePsrComponent.sortOperazioniByDescendingCode(this.domandaPsr.operazioni);
      this.customOperazioniSorting(this.domandaPsr.operazioni);
      this.mappingInterventiDaMostrare(this.domandaPsr);
      this.aziendaAgricolaService.getSelectedCuaa(domandaPsr.cuaa).subscribe(
        selected => this.domandaUnica = selected);
    });
    ActivateRouteSupport
      .findQueryParamsAndExecute((queryParams: Params) => this.getSezioneSelezionataFrom(queryParams),
        this.componentDestroyed$,
        this.route);
  }

  getDomandaYear(): number {
    return this.domandaPsr.dataPresentazione.getFullYear();
  }

  private initTabs() {
    this.tabs = new Array<MenuItem>(
      this.createMenuItem('Riepilogo Domanda', 1),
      this.createMenuItem('Dettaglio Istruttoria', 2)
    );
  }

  private createMenuItem(label: string, sezione: number): MenuItem {
    return {
      label: this.translateService.instant(label),
      command: (event?: any) => this.changeTabOfDettaglioDomandaMenu(event, sezione)
    };
  }

  private changeTabOfDettaglioDomandaMenu(event: any, sezione: number): void {
    this.router.navigate(['psr/', this.idDomanda, 'dettaglio-psr-superficie'],
      {queryParams: {[Stack.TAB_PARAM]: sezione}});
  }

  private getSezioneSelezionataFrom(queryParams: Params) {
    const sezioneSelezionataFromParams: number = queryParams.sezioneSelezionata;
    if (sezioneSelezionataFromParams != null) {
      this.sezioneSelezionata = sezioneSelezionataFromParams;
    }
  }

  private mappingInterventiDaMostrare(domanda: DomandaPsr): DomandaPsr {
    domanda.mapOperazioniDaMostrare = this.loadOperazioniMap();
    domanda.operazioni.forEach(op => {
      domanda.mapOperazioniDaMostrare.set(op.codiceMisureIntervento, true);
    });
    return domanda;
  }

  loadOperazioniMap(): Map<string, boolean> {
    const mapOperazioni = new Map<string, boolean>();
    for (const operazione in CodiceMisureIntervento) {
      mapOperazioni.set(operazione, false);
    }
    return mapOperazioni;
  }

  private customOperazioniSorting(operazioni: Array<Operazione>): Array<Operazione> {
    return operazioni.sort((n1, n2) => {
      // Prima delle operazioni inizianti con M11 e' necessario mostrare coloro che iniziano con M10
      if (n1.codiceMisureIntervento.startsWith('M10') && n2.codiceMisureIntervento.startsWith('M11')) {
        return -1;
      }
      return 0;
      }
    );
  }
}
