import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { DomandaPsrStrutturale } from '../models/domanda-psr-strutturale';
import { DomandePsrStrutturaliService } from '../domande-psr-strutturali.service';
import { DettaglioPSRStrutturale } from '../models/dettaglio-domanda-psr-strutturale';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { MessageService } from 'primeng-lts';
import { TotaliImportiSRStrutturale } from '../models/importi-totali';
import { ImportiLiquidazione } from '../models/importi-liquidazione';
import { DettaglioIstruttoriaSrt } from '../models/dettaglio-istruttoria-srt';
import { forkJoin, Observable } from 'rxjs';
import { Variante } from '../models/variante';
import { Investimento } from '../models/investimento';
import { map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-dettaglio-domanda-psr-strutt',
  encapsulation: ViewEncapsulation.None,
  templateUrl: './dettaglio-domanda-psr-strutturale.component.html',
  styleUrls: ['./dettaglio-domanda-psr-strutturale.component.scss']
})
export class DettaglioDomandaPsrStrutturaleComponent implements OnInit {
  public interventi: Array<string> = [];
  public dataDomandaIntegrativa: string;
  public importoCalcolato: number;
  public importoLiquidato: number;
  public dettaglioPSRStrutturale: DettaglioPSRStrutturale[] = [];
  public investimenti: Investimento[] = [];

  @Input() public domandaPsrStrutturale: DomandaPsrStrutturale[];
  public totaleContributiRichiesto = 0;
  public totaleCostiInvestimento = 0;
  public speseTecniche = 0;
  public totaleContributiAmmessi = 0;
  public totaleCostiContributi = 0;
  public totaleLiquidato = 0;
  private statoDomanda: string;
  private totaliImportiSRStrutturale: TotaliImportiSRStrutturale[];
  private importiLiquidazione: ImportiLiquidazione[];
  public contributoRimanente = 0;
  public contributoTotaleAmmesso = 0;
  public costoTotaleAmmesso = 0;
  public dettaglioIstruttoria: DettaglioIstruttoriaSrt;
  public varianti: Variante[] = [];
  public showInvestimentiVarianti = false;

  constructor(private domandePsrStrutturaliService: DomandePsrStrutturaliService,
              private messageService: MessageService) {
  }

  ngOnInit(): void {
    const numeroDomanda: string = this.domandaPsrStrutturale[0].idProgetto.toString();
    this.statoDomanda = this.domandaPsrStrutturale[0].codiceStatoProgetto;

    this.caricaVariantiByIdProgetto(numeroDomanda).pipe(
      switchMap((varianti) => {
        this.varianti = varianti || [];
        this.mostraInvestimentiVarianti(numeroDomanda);
        return this.domandePsrStrutturaliService.getPSRStrutturaliDettaglio(numeroDomanda)
        .pipe(map(this.calcolaTotaliIfThereAreDettagliStrutturali(numeroDomanda)));
      })
    ).subscribe();
    if (this.statoDomandaRichiesto()) {
      this.caricaTotaliImporti(numeroDomanda);
      this.caricaLiquidazioneImporti(numeroDomanda);
    } else {
      this.contributoTotaleAmmesso = -1;
      this.totaleLiquidato = -1;
      this.totaleContributiAmmessi = -1;
    }
  }


  private mostraInvestimentiVarianti(idProgetto): void {
      const ultimaVarianteApprovata = this.trovaUltimaVarianteApprovata(this.varianti, 0);
      if (ultimaVarianteApprovata != null) {
        this.domandePsrStrutturaliService.getInvestimentiByIdVariante(ultimaVarianteApprovata.idVariante).subscribe((investimenti) => {
          this.investimenti = investimenti;
          this.showInvestimentiVarianti = true;
        });
      }
  }

  private calcolaTotaliIfThereAreDettagliStrutturali(numeroDomanda: string) {
    return (dettaglioPSRStrutturale: DettaglioPSRStrutturale[]) => {
      this.dettaglioPSRStrutturale = dettaglioPSRStrutturale;
      if (this.dettaglioPSRStrutturale != null && this.dettaglioPSRStrutturale.length !== 0) {
          this.calcolaTotali(numeroDomanda);
        } else {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDettaglioPSRStr));
        }
    };
  }

  private trovaUltimaVarianteApprovata(varianti: Variante[], index: number): Variante {
    if (varianti.length === 0 || index === varianti.length) {
      return null;
    }
    const ultimaVariante = this.varianti[index];
    if (ultimaVariante.approvata) {
      return ultimaVariante;
    }
    this.trovaUltimaVarianteApprovata(varianti, index + 1);
  }

  private caricaTotaliImporti(numeroDomanda: string): any {
    this.domandePsrStrutturaliService.getPSRStrutturaliTotali(numeroDomanda).subscribe(
      (totaliImportiSRStrutturale: TotaliImportiSRStrutturale[]) => {
        this.totaliImportiSRStrutturale = totaliImportiSRStrutturale;
        if (this.totaliImportiSRStrutturale != null) {
          if (this.totaliImportiSRStrutturale.length == 0) {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreTotaliVuotiPSRStr));
          } else {
            this.scriviTotali();
          }
        } else {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoTotaliPSRStr));
        }
      }
    ), err => {
      console.error(err);
    };
  }

  private caricaLiquidazioneImporti(numeroDomanda: string): any {
    const dataProgetto = new Date(this.domandaPsrStrutturale[0].dataProgetto);
    const anno: string = dataProgetto.getFullYear().toString();
    const cuaa: string = this.domandaPsrStrutturale[0].cuaa;

    forkJoin([
      this.domandePsrStrutturaliService.getPSRStrutturaliLiquidazione(cuaa, numeroDomanda),
      this.domandePsrStrutturaliService.getPSRStrutturaliDettaglioIstruttoria(numeroDomanda)
    ]).subscribe(
      ([totaliImportiSRStrutturale, dettaglioIstruttoria]) => {
        this.dettaglioIstruttoria = dettaglioIstruttoria;
        this.importiLiquidazione = totaliImportiSRStrutturale;

        if (this.dettaglioIstruttoria.isFinanziatoPat) {
          this.calcolaLiquidazioneTotaleConFondiPat();
        } else if (this.importiLiquidazione && this.importiLiquidazione.length > 0) {
          this.calcolaLiquidazioneTotaleSenzaFondiPat();
        } else {
          // this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoTotaliPSRStr));
        }
      }
    ), err => {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoTotaliPSRStr));
      console.error(err);
    };
  }

  private statoDomandaRichiesto(): boolean {
    let controllo = false;

    // BR3: Se la domanda è in uno dei seguenti stati ‘Finanziabile’, ‘Controllo in loco’, ‘Anticipo’, ‘Acconto’, ‘Saldo’
    const statiValidi: string[] = ['F', 'H', 'V', 'S', 'O', 'T'];
    statiValidi.forEach(
      (valido) => {
        if (this.statoDomanda === valido) {
          controllo = true;
        }
      });

    return controllo;
  }

  private calcolaTotali(idProgetto: string): any {
      const variantiApprovate = this.varianti.filter(value => value.approvata === true);
      if (variantiApprovate.length > 0) {
        const lastVariante = variantiApprovate[0];
        this.domandePsrStrutturaliService.getTotaliVariante(idProgetto, lastVariante.idVariante).subscribe(value => {
          if (value.contributoRichiesto !== undefined && value.spesaRichiesta) {
            this.totaleContributiRichiesto = this.contributoTotaleAmmesso = value.contributoRichiesto;
            this.totaleCostiInvestimento = this.costoTotaleAmmesso = value.spesaRichiesta;
          } else {
            this.calcolaTotaliWithNoVariante();
          }
        });
      } else {
        this.calcolaTotaliWithNoVariante();
      }
  }

  private calcolaTotaliWithNoVariante() {
    this.dettaglioPSRStrutturale.forEach(domanda => {
      this.speseTecniche = this.speseTecniche + domanda.speseTecniche;
      this.totaleCostiInvestimento = this.totaleCostiInvestimento + domanda.costoInvestimento + domanda.speseTecniche;
      if (this.domandaPsrStrutturale[0].codiceOperazione === '6.1.1') {
          this.totaleContributiRichiesto = 40000;
      } else {
          this.totaleContributiRichiesto = this.totaleContributiRichiesto + domanda.contributoRichiesto;
      }
    });
  }

  private scriviTotali(): any {
    this.contributoRimanente = this.totaliImportiSRStrutturale[0].contributoRimanente;
    this.contributoTotaleAmmesso = this.totaliImportiSRStrutturale[0].contributoTotale;
    this.costoTotaleAmmesso = this.totaliImportiSRStrutturale[0].costoTotale;
  }

  private calcolaLiquidazioneTotaleSenzaFondiPat(): any {
    this.totaleLiquidato = 0;
    this.importiLiquidazione.forEach(
      (importo) => {
        this.totaleLiquidato = this.totaleLiquidato + importo.incassatoNetto;
      });
  }

  private calcolaLiquidazioneTotaleConFondiPat(): any {
    this.totaleLiquidato = 0;
    if (this.dettaglioIstruttoria.saldo) {
      this.totaleLiquidato += this.dettaglioIstruttoria.saldo.contributoLiquidabile;
    }

    if (this.dettaglioIstruttoria.anticipo) {
      this.totaleLiquidato += this.dettaglioIstruttoria.anticipo.anticipoLiquidabile;
    }

    if (this.dettaglioIstruttoria.acconti) {
      this.dettaglioIstruttoria.acconti.forEach(acconto => {
        this.totaleLiquidato += acconto.contributoLiquidabile;
      });
    }
  }

  private caricaVariantiByIdProgetto(idProgetto: string): Observable<Variante[]> {
    return this.domandePsrStrutturaliService.getVariantiByIdProgetto(idProgetto);
  }

}
