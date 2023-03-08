import { Component, OnInit, OnDestroy } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';
import { LoaderService } from 'src/app/loader.service';
import { Costanti } from '../../Costanti';
import { IstruttoriaDomandaUnica } from '../../classi/IstruttoriaDomandaUnica';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { TipoProcesso } from 'src/app/istruttoria/istruttoria-antimafia/dto/TipoProcesso';
import { SostegnoDettaglioService } from '../sostegno-dettaglio-service.module';
import { FlowIstruttoriaDUEnum } from '../../classi/FlowIstruttoriaDUEnum';
import { CONF_PROCESSI } from '../../sostegno-shared/conf-processi';

@Component({
  selector: 'app-zootecnia',
  templateUrl: './zootecnia.component.html',
  styleUrls: ['./zootecnia.component.css']
})
export class ZootecniaComponent implements OnInit, OnDestroy {

  menu2: Array<MenuItem>;
  private istruttoriaDUCorrente: IstruttoriaDomandaUnica;
  private istruttoriaSostegno: SostegnoDu;
  public processiDaControllare: TipoProcesso[];

  constructor(
    private sostegnoService: SostegnoDettaglioService,
    private route: ActivatedRoute, 
    private loader: LoaderService
  ) { }

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
        routerLink: Costanti.dichiarazioni,
        label: 'Dichiarazioni'
      },
      {
        routerLink: Costanti.capiAmmessi,
        label: 'Capi Ammessi'
      },
      {
        routerLink: Costanti.capiRichiesti,
        label: 'Capi Richiesti'
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
        routerLink: Costanti.datiIstruttoria,
        label: 'Inserimento dati Istruttoria'
      }
    );
    if (this.istruttoriaDUCorrente.stato == StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO) {
      this.menu2.push({ routerLink: Costanti.datiLiquidazione, label: 'Dati di Liquidazione' });
    }      
    this.istruttoriaSostegno = SostegnoDu[this.istruttoriaDUCorrente.sostegno] as SostegnoDu;
  }
    
  public isEnableCalcoloDisaccoppiato() {
    return this.istruttoriaDUCorrente.sostegno === SostegnoDu.ZOOTECNIA && 
                    this.istruttoriaDUCorrente.stato === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK;
  }

  public isEnableProcessoAmmissibilita() {
    return this.istruttoriaDUCorrente.sostegno === SostegnoDu.ZOOTECNIA && 
    this.istruttoriaDUCorrente.stato === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO;
  }

  public isEnableControlliLiquidabilita() {
    return this.istruttoriaDUCorrente.sostegno === SostegnoDu.ZOOTECNIA && 
    this.istruttoriaDUCorrente.stato === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK;
  }

  public isEnableCalcoloPremio() {
    return this.istruttoriaDUCorrente.sostegno === SostegnoDu.ZOOTECNIA
      && (this.istruttoriaDUCorrente.stato === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO
        || this.istruttoriaDUCorrente.stato === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
        || this.istruttoriaDUCorrente.stato === StatoIstruttoriaEnum.INTEGRATO);
  }

  public isEnableCalcoloCapi() {
    return this.istruttoriaDUCorrente.sostegno === SostegnoDu.ZOOTECNIA
      && this.istruttoriaDUCorrente.stato === StatoIstruttoriaEnum.INTEGRATO;
  }

  public avviaProcessoCalcoloCapi() {
    this.sostegnoService.eseguiProcessoIstruttoria(
      this.istruttoriaDUCorrente.id, 
      TipoProcesso.CALCOLO_CAPI_ISTRUTTORIE,
      this.processiDaControllare,
       this.istruttoriaDUCorrente.domanda.campagna,
       this.istruttoriaSostegno);
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
      TipoProcesso.CALCOLO_ACCOPPIATO_ZOOTECNIA_ISTRUTTORIA,
      this.processiDaControllare,
      this.istruttoriaDUCorrente.domanda.campagna,
      this.istruttoriaSostegno);
  }
  
  public avviaProcessoNonAmmissibilita() {
    this.sostegnoService.eseguiProcessoIstruttoria(
      this.istruttoriaDUCorrente.id, 
      TipoProcesso.NON_AMMISSIBILITA,
      this.processiDaControllare,
      this.istruttoriaDUCorrente.domanda.campagna,
      this.istruttoriaSostegno);
  }

}
