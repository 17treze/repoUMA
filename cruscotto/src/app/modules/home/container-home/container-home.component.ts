import { TranslateService } from '@ngx-translate/core';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { Router, ActivatedRoute } from '@angular/router';
import { Utente } from '../../register/utente.model';
import { IFascicolo } from '../../domande-uniche/models/fascicolo.model';
import { AziendaAgricolaService } from 'src/app/shared/services/azienda-agricola.service';
import { environment } from 'src/environments/environment';
import { MenuItem } from 'primeng-lts';

@Component({
  selector: 'app-container-home',
  templateUrl: './container-home.component.html',
  styleUrls: ['./container-home.component.css']
})
export class ContainerHomeComponent implements OnInit, OnDestroy {

  public tabs: MenuItem[];
  protected componentDestroyed$: Subject<boolean> = new Subject();
  public domandaUnica: IFascicolo;
  utente: Utente;
  urlReindirizzamento = '/';
  public disablePSRSuperficie: boolean = environment.disablePSRSuperficie;
  public disablePSRStrutturale: boolean = environment.disablePSRStrutturale;
  public disableUMA: boolean = environment.disableUMA;
  public disableDUV_DPV: boolean = environment.disableDUV_DPV;
  public disableANTIMAFIA: boolean = environment.disableANTIMAFIA;
  public disableLinkFascicolo: boolean = environment.disableLinkFascicolo;
  public disableDichiarazioniAssociative: boolean = environment.disableDichiarazioniAssociative;
  public disableCreaFascicolo: boolean = environment.disableCreaFascicolo;
  public cards: MenuItem[];

  constructor(
    private aziendaAgricolaService: AziendaAgricolaService,
    protected route: ActivatedRoute,
    private router: Router,
    protected translateService: TranslateService
  ) { }

  ngOnInit() {
    this.caricaUtente();
    this.setCards();
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  setCards() {
    this.cards = [
      {
        label: 'HOME_CARDS.DOMANDA_UNICA',
        command: (event: any) => this.vaiALista(),
        visible: true,
        disabled: false
      },
      {
        label: 'HOME_CARDS.DOMANDA_PSR_SUPERFICIE',
        command: (event: any) => this.vaiAPsr(),
        visible: !this.disablePSRSuperficie,
        disabled: false
      },
      {
        label: 'HOME_CARDS.DOMANDA_PSR_STRUTTURALI',
        command: (event: any) => this.vaiAPsrStrutturali(),
        visible: !this.disablePSRStrutturale,
        disabled: false
      },
      {
        label: 'HOME_CARDS.DOMANDA_UMA',
        command: (event: any) => this.vaiADomandeUma(),
        visible: !this.disableUMA,
        disabled: false
      },
      {
        label: 'HOME_CARDS.DOMANDA_DUV_DPV',
        command: (event: any) => this.vaiALista(),
        visible: !this.disableDUV_DPV,
        disabled: true
      },
      {
        label: 'HOME_CARDS.DOMANDA_ANTIMAFIA',
        command: (event: any) => this.vaiAntimafia(),
        visible: !this.disableANTIMAFIA,
        disabled: false
      },
      {
        label: 'HOME_CARDS.DICHIARAZIONI_ASSOCIATIVE',
        command: (event: any) => this.vaiADichiarazioniAssociative(),
        visible: !this.disableDichiarazioniAssociative,
        disabled: false
      },
      {
        label: 'HOME_CARDS.CREA_FASCICOLO',
        command: (event: any) => this.vaiACreaFascicolo(),
        visible: !this.disableCreaFascicolo,
        disabled: false
      }
    ];
  }

  caricaUtente() {
    this.aziendaAgricolaService.getSelectedCuaa().pipe(
      takeUntil(this.componentDestroyed$)).subscribe(
        selected => this.domandaUnica = selected);
  }

  getLinkToFascicolo() {
    this.router.navigate(
      [`fascicolo-dettaglio/${this.domandaUnica.cuaa}`],
      { relativeTo: this.route }
    );
  }

  vaiALista(): any {
    this.router.navigate(
      [`domande-uniche/${this.domandaUnica.cuaa}/lista`],
      { relativeTo: this.route }
    );
  }

  vaiAntimafia(): any {
    this.router.navigate(
      [`antimafia/${this.domandaUnica.cuaa}`],
      { relativeTo: this.route }
    );
  }

  vaiAPsr(): any {
    this.router.navigate(
      [`psr/${this.domandaUnica.cuaa}/lista`],
      { relativeTo: this.route }
    );
  }

  vaiAPsrStrutturali(): any {
    this.router.navigate(
      [`psr-strutturali/${this.domandaUnica.cuaa}/lista`],
      { relativeTo: this.route }
    );
  }

  vaiADomandeUma(): any {
    this.router.navigate(
      [`domande-uma/${this.domandaUnica.cuaa}`],
      { relativeTo: this.route }
    );
  }

  vaiADichiarazioniAssociative(): any {
    this.router.navigate(
      [`dichiarazioni-associative/${this.domandaUnica.cuaa}`],
      { relativeTo: this.route }
    );
  }

  vaiACreaFascicolo(): any {
    this.router.navigate(
      ['crea-fascicolo'],
      { relativeTo: this.route }
    );
  }
}
