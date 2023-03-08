import { Component, OnInit, OnDestroy } from '@angular/core';
import { Costanti } from '../../Costanti';
import { LoaderService } from 'src/app/loader.service';
import { MenuItem } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';
import { IstruttoriaDomandaUnica } from '../../classi/IstruttoriaDomandaUnica';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { TipoProcesso } from 'src/app/istruttoria/istruttoria-antimafia/dto/TipoProcesso';
import { SostegnoDettaglioService } from '../sostegno-dettaglio-service.module';
import { FlowIstruttoriaDUEnum } from '../../classi/FlowIstruttoriaDUEnum';
import { CONF_PROCESSI } from '../../sostegno-shared/conf-processi';

@Component({
  selector: 'app-superficie',
  templateUrl: './superficie.component.html',
  styleUrls: ['./superficie.component.css']
})
export class SuperficieComponent implements OnInit, OnDestroy {

  menu2: Array<MenuItem>;
  private istruttoriaDUCorrente: IstruttoriaDomandaUnica;
  private istruttoriaSostegno: SostegnoDu;
  public processiDaControllare: TipoProcesso[];

  constructor(
    private sostegnoService: SostegnoDettaglioService,
    private loader: LoaderService,
    private route: ActivatedRoute
  ) {  }

  ngOnInit() {
    this.setMenu();
    this.loader.setTimeout(480000); //otto minuti
    this.processiDaControllare = this.sostegnoService.caricaProcessiDaControllare(this.istruttoriaDUCorrente);
  }

  ngOnDestroy() {
    this.loader.resetTimeout();
  }

  private setMenu() {
    this.istruttoriaDUCorrente = this.route.snapshot.data['domandaIstruttoria'];
    this.menu2 = new Array<MenuItem>(
      {
        routerLink: Costanti.superficiImpegnate,
        label: 'Superfici Impegnate'
      },
      {
        routerLink: Costanti.dichiarazioni,
        label: 'Dichiarazioni'
      },
      {
        routerLink: Costanti.controlliSostegno,
        label: 'Controlli di sostegno'
      },
      {
        routerLink: Costanti.esiticalcoli,
        label: 'Esiti Calcoli'
      },
      {
        routerLink: Costanti.esitiParticelle,
        label: 'Esiti per Particella'
      },
      {
        routerLink: Costanti.datiIstruttoria,
        label: 'Inserimento dati Istruttoria'
      }
    );
    if (this.istruttoriaDUCorrente.stato == StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO) {
      this.menu2.push({ routerLink: Costanti.datiLiquidazione, label: 'Dati di Liquidazione' });
    }
    this.istruttoriaSostegno = SostegnoDu[this.istruttoriaDUCorrente.sostegno] as SostegnoDu;
  }

  public isEnableCalcoloPremio() {
    return (this.istruttoriaDUCorrente.sostegno === SostegnoDu.SUPERFICIE) && 
                    (this.istruttoriaDUCorrente.stato === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO
                  || this.istruttoriaDUCorrente.stato === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK);
  }

  public isEnableProcessoAmmissibilita() {
    return this.istruttoriaDUCorrente.sostegno === SostegnoDu.SUPERFICIE && 
    this.istruttoriaDUCorrente.stato === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO;
  }

  public isEnableControlliLiquidabilita() {
    return this.istruttoriaDUCorrente.sostegno === SostegnoDu.SUPERFICIE && 
    this.istruttoriaDUCorrente.stato === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK;
  }

  public avviaProcessoControlloLiquidabilita() {
    this.sostegnoService.eseguiProcessoIstruttoria(
      this.istruttoriaDUCorrente.id, 
      TipoProcesso.CONTROLLO_LIQUIDABILITA_ISTRUTTORIA,
      this.processiDaControllare,
       this.istruttoriaDUCorrente.domanda.campagna,
       this.istruttoriaSostegno);
  }

  public avviaProcessoCalcoloPremio() {
    this.sostegnoService.eseguiProcessoIstruttoria(
      this.istruttoriaDUCorrente.id, 
      TipoProcesso.CALCOLO_ACCOPPIATO_SUPERFICIE_ISTRUTTORIA,
      this.processiDaControllare,
      this.istruttoriaDUCorrente.domanda.campagna,
      this.istruttoriaSostegno);
 }

  public avviaProcessoNonAmmissibilita() {
    this.processiDaControllare = [TipoProcesso.NON_AMMISSIBILITA];
    this.sostegnoService.eseguiProcessoIstruttoria(
      this.istruttoriaDUCorrente.id, 
      TipoProcesso.NON_AMMISSIBILITA,
      this.processiDaControllare,
      this.istruttoriaDUCorrente.domanda.campagna,
      this.istruttoriaSostegno);
  }

}
