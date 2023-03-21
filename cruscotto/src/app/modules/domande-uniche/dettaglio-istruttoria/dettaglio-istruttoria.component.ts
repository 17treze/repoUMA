import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng-lts';
import { map, switchMap } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { DomandeUnicheService } from '../domande-uniche.service';
import { Istruttorie } from '../models/dettaglio-istruttoria';
import { SostegnoType } from '../models/enumeration/sostegno-type.model';
import { CreaRegole } from './crea-regole';

@Component({
  selector: 'app-dettaglio-istruttoria',
  templateUrl: './dettaglio-istruttoria.component.html',
  styleUrls: ['./dettaglio-istruttoria.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DettaglioIstruttoriaComponent implements OnInit {

  public totaleDebitoRecuperato: number[] = [];
  public totaleImportoLiquidato: number[] = [];
  public totaleGeneraleImportoCalcolato: number = 0;
  public totaleGeneraleImportoLiquidato: number = 0;

  @Input() dataProtocollazione;

  istruttorie: Istruttorie[];
  severityClass = {
    0: "alert-danger",
    1: "alert-warning",
    2: "alert-success"
  };

  severityColorClass = {
    0: "text-warning",
    1: "text-warning",
    2: "text-danger"
  };

  sostegnoIcon = {
    "DISACCOPPIATO": "icon-tractor.svg",
    "SUPERFICIE": "icon-land.svg",
    "ZOOTECNIA": "icon-w.svg"
  };

  statiDettaglio: Array<string> = ['LIQUIDABILE', 'CONTROLLI_LIQUIDABILE_KO', 'NON_LIQUIDABILE', 'PAGAMENTO_AUTORIZZATO', 'PAGAMENTO_NON_AUTORIZZATO', 'CONTROLLI_INTERSOSTEGNO_OK','DEBITI'];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: DomandeUnicheService,
    private creaRegole: CreaRegole,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.route.paramMap.pipe(
      switchMap(params => {
        let numeroDomanda: string = params.get("idDomanda");
        return this.service.getIstruttorie(numeroDomanda);
      }),
      map(istruttorie => {
        if (istruttorie) {
          istruttorie.forEach(istruttoria => {
            // filtro utilizzato per togliere i passi che non hanno un responso tramite il valore esito
            istruttoria.esitiControlli = istruttoria.esitiControlli.filter(x => (x.esito != null));
            switch (istruttoria.sostegno) {
              case SostegnoType.DISACCOPPIATO: this.creaRegole.perDisaccoppiato(istruttoria);
                break;
              case SostegnoType.ZOOTECNIA: this.creaRegole.perZootecnia(istruttoria);
                break;
              case SostegnoType.SUPERFICIE: this.creaRegole.perSuperfici(istruttoria);
                break;
            }
          });
        }
        return istruttorie;
      })
    ).subscribe(istruttorie => {
      if (istruttorie) {
        this.istruttorie = istruttorie.sort((a, b) => a.dtUltimoCalcolo < b.dtUltimoCalcolo ? 1 : -1);
        this.calcolaTotaleDebiti();
        this.calcolaImportoLiquidato();
        this.calcolaTotaleGeneraleImporti();
      }
    });
  }

  private calcolaTotaleDebiti(): any {
    for (let numeroIstruttorie in this.istruttorie) {
      this.totaleDebitoRecuperato[numeroIstruttorie] = 0;
      if (this.istruttorie[numeroIstruttorie].importiIstruttoria && this.istruttorie[numeroIstruttorie].importiIstruttoria.debitiRecuperati != null) {
        for (let debitoRecuperato of this.istruttorie[numeroIstruttorie].importiIstruttoria.debitiRecuperati) {
          this.totaleDebitoRecuperato[numeroIstruttorie] = (this.totaleDebitoRecuperato[numeroIstruttorie] * 100 + debitoRecuperato.importo * 100) / 100;
        }
      }
    }
  }

  private calcolaImportoLiquidato(): any {
    for (let numeroIstruttorie in this.istruttorie) {
      this.totaleImportoLiquidato[numeroIstruttorie] = 0;
      if (this.istruttorie[numeroIstruttorie].importiIstruttoria && this.istruttorie[numeroIstruttorie].importiIstruttoria.importoAutorizzato != null) {
        this.totaleImportoLiquidato[numeroIstruttorie] = (this.istruttorie[numeroIstruttorie].importiIstruttoria.importoAutorizzato * 100 - this.totaleDebitoRecuperato[numeroIstruttorie] * 100) / 100;
      }
    }
  }

  private calcolaTotaleGeneraleImporti(): any {
    this.totaleGeneraleImportoCalcolato = 0;
    this.totaleGeneraleImportoLiquidato = 0;
    for (let numeroIstruttorie in this.istruttorie) {
      if (this.totaleImportoLiquidato[numeroIstruttorie] != null) {
        this.totaleGeneraleImportoLiquidato = this.totaleGeneraleImportoLiquidato + this.totaleImportoLiquidato[numeroIstruttorie];
      }
      if (this.istruttorie[numeroIstruttorie].importiIstruttoria && this.istruttorie[numeroIstruttorie].importiIstruttoria.importoCalcolato != null) {
        this.totaleGeneraleImportoCalcolato = (this.totaleGeneraleImportoCalcolato * 100 + this.istruttorie[numeroIstruttorie].importiIstruttoria.importoCalcolato * 100) / 100;
      }
    }
  }

  public goToEsitiIstruttoriaVisible(istruttoria: Istruttorie): boolean {
    return this.statiDettaglio.includes(istruttoria.statoLavorazioneSostegno) ? true : false;
  }

  public goToEsitiIstruttoria(istruttoria: Istruttorie) {
    console.log(istruttoria)
    if (this.statiDettaglio.includes(istruttoria.statoLavorazioneSostegno))
      switch (istruttoria.sostegno) {
        case SostegnoType.DISACCOPPIATO: {
          this.router.navigate(['./istruttoria/' + istruttoria.id + '/esitiIstruttoria'], { relativeTo: this.route });
          break;
        }
        case SostegnoType.SUPERFICIE: {
          this.router.navigate(['./istruttoria/' + istruttoria.id + '/esitiIstruttoriaSuperficie'], { relativeTo: this.route });
          break;
        }
        case SostegnoType.ZOOTECNIA: {
          this.router.navigate(['./istruttoria/' + istruttoria.id + '/esitiIstruttoriaZootecnia'], { relativeTo: this.route });
          break;
        }
        default: {
          break;
        }
      }
    else
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.operazioneNegataPerStato(istruttoria.statoLavorazioneSostegno)));
  }

  public getTimelineIcon(istruttoria) {
    // Gestione icona disciplina
    if (istruttoria.sostegno) {
      return `assets/icons/svg/${this.sostegnoIcon[istruttoria.sostegno]}`;
    } else {
      return `assets/icons/svg/icon-disciplina-finaziaria.svg`;
    }
  }
}
