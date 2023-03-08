import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Costanti } from '../../Costanti';
import { ActivatedRoute } from '@angular/router';
import { IstruttoriaDomandaUnica } from '../../classi/IstruttoriaDomandaUnica';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { TipoProcesso } from 'src/app/istruttoria/istruttoria-antimafia/dto/TipoProcesso';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { SostegnoDettaglioService } from '../sostegno-dettaglio-service.module';
import { TipoIstruttoriaEnum } from '../../classi/TipoIstruttoriaEnum';
import { TabCommonComponent } from '../../sostegno-shared/TabCommonComponent';

@Component({
  selector: 'app-disaccoppiato',
  templateUrl: './disaccoppiato.component.html',
  styleUrls: ['./disaccoppiato.component.css']
})
export class DisaccoppiatoComponent implements OnInit {
  menu2: Array<MenuItem>;
  private istruttoriaDUCorrente: IstruttoriaDomandaUnica;
  private istruttoriaSostegno: SostegnoDu;
  public processiDaControllare: TipoProcesso[];
  private keyIstruttoria: keyof typeof TipoIstruttoriaEnum;
  private tipoIstruttoria: TipoIstruttoriaEnum;

  constructor(
    private sostegnoService: SostegnoDettaglioService,
    private route: ActivatedRoute
  ) {  }

  ngOnInit() {
    this.istruttoriaDUCorrente = this.route.snapshot.data['domandaIstruttoria'];
    this.menu2 = new Array<MenuItem>(
      { routerLink: Costanti.superficiImpegnate, label: 'Superfici Impegnate' },
      { routerLink: Costanti.dichiarazioni, label: 'Dichiarazioni' },
      { routerLink: Costanti.informazioniDomanda, label: 'Informazioni di domanda' },
      { routerLink: Costanti.controlliSostegno, label: 'Controlli di sostegno' },
      { routerLink: Costanti.datiDomanda, label: 'Esiti di domanda' },
      { routerLink: Costanti.datiPerPascolo, label: 'Esiti per pascolo' },
      { routerLink: Costanti.esitiParticelle, label: 'Esiti su particelle' },
      { routerLink: Costanti.datiIstruttoria, label: 'Inserimento dati Istruttoria' }
    );
    if (this.istruttoriaDUCorrente.stato == StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO) {
      this.menu2.push({ routerLink: Costanti.datiLiquidazione, label: 'Dati di Liquidazione' });
    }
    this.istruttoriaSostegno = SostegnoDu[this.istruttoriaDUCorrente.sostegno] as SostegnoDu;
    this.processiDaControllare = this.sostegnoService.caricaProcessiDaControllare(this.istruttoriaDUCorrente);
    this.keyIstruttoria = this.istruttoriaDUCorrente.tipo as keyof typeof TipoIstruttoriaEnum;
    this.tipoIstruttoria = TipoIstruttoriaEnum[this.keyIstruttoria];
  }

  public isEnableCalcoloDisaccoppiato() {
    return TabCommonComponent.anticipiAttivi(this.tipoIstruttoria, this.istruttoriaDUCorrente.domanda.campagna)
    && (this.istruttoriaDUCorrente.sostegno === SostegnoDu.DISACCOPPIATO) && 
                    (this.istruttoriaDUCorrente.stato === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO
                  || this.istruttoriaDUCorrente.stato === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK);
  }

  public isEnableProcessoAmmissibilita() {
    return TabCommonComponent.anticipiAttivi(this.tipoIstruttoria, this.istruttoriaDUCorrente.domanda.campagna)
    && this.istruttoriaDUCorrente.sostegno === SostegnoDu.DISACCOPPIATO && 
    this.istruttoriaDUCorrente.stato === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO;
  }

  public isEnableControlliLiquidabilita() {
    return TabCommonComponent.anticipiAttivi(this.tipoIstruttoria, this.istruttoriaDUCorrente.domanda.campagna)
    && this.istruttoriaDUCorrente.sostegno === SostegnoDu.DISACCOPPIATO && 
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

  public avviaProcessoCalcoloDisaccoppiato() {
    this.sostegnoService.eseguiProcessoIstruttoria(
      this.istruttoriaDUCorrente.id, 
      TipoProcesso.CALCOLO_DISACCOPPIATO_ISTRUTTORIA,
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
