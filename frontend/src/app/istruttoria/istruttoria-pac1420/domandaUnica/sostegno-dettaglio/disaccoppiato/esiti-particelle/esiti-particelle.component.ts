import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';
import { MessageService } from 'primeng/api';
import { Paginazione } from 'src/app/a4g-common/utility/paginazione';
import { PaginaDettaglioParticella, DettaglioParticella } from '../../../domain/dettaglioParticella';
import { PaginaDettaglioCalcoloParticella } from '../../../domain/dettaglioCalcoloParticella';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { forkJoin, Observable, of, Subject } from 'rxjs';
import { takeUntil, map } from 'rxjs/operators';
import { IstruttoriaDomandaUnica } from '../../../classi/IstruttoriaDomandaUnica';
import { Configuration } from 'src/app/app.constants';
import * as FileSaver from "file-saver";
import { DateSupport } from 'src/app/a4g-common/utility/date-support';


class ParcelleLinkValue {
  link: string;
  value: number;
}

@Component({
  selector: 'app-esiti-particelle',
  templateUrl: './esiti-particelle.component.html',
  styleUrls: ['./esiti-particelle.component.css']
})
export class EsitiParticelleComponent implements OnInit, OnDestroy {
  cols: any[];
  colsGreening: any[];
  colsMantenimento: any[];
  idIstruttoria: number;
  numeroPagina = 1;
  elementiPerPagina = 10;

  listaDettaglioParticelleTable: PaginaDettaglioParticella = new PaginaDettaglioParticella();
  listaDettaglioParticelleTableGreening: PaginaDettaglioParticella = new PaginaDettaglioParticella();
  listaDettaglioParticelleTableMantenimento: PaginaDettaglioParticella = new PaginaDettaglioParticella();
  numeroPaginaGreen = 1;
  first = 0;
  paginazione: Paginazione

  @ViewChild('tableEle') tableEle;
  @ViewChild('tableGreening') tableGreening;
  @ViewChild('tableMan') tableMan;
  protected componentDestroyed$: Subject<boolean> = new Subject();
  protected istruttoriaDUCorrente: IstruttoriaDomandaUnica;


  constructor(
    private route: ActivatedRoute,
    private istruttoriaDettaglioService: IstruttoriaDettaglioService,
    private messageService: MessageService,
    private configuration: Configuration
  ) {}

  ngOnInit() {
    this.setCols();
    this.getIdIstruttoria();
    this.getDettaglio();
    this.istruttoriaDUCorrente = this.route.snapshot.data['domandaIstruttoria'];
  };

  private setCols() {
    this.cols = [
      { field: 'codNazionale', header: 'Cod. Comune' },
      { field: 'foglio', header: 'Foglio' },
      { field: 'particella', header: 'Particella' },
      { field: 'sub', header: 'Sub' },
      { field: 'codiceColtura3', header: 'Cod. Coltura' },
      { field: 'descrizioneColtura', header: 'Descrizione Coltura', sortable: false },
      { field: 'superficieImpegnata', header: 'Sup Imp.' },
      { field: 'superficieEleggibile', header: 'Sup Elegg.' },
      { field: 'superficieSigeco', header: 'Sup Controllo in loco' },
      { field: 'anomalieMantenimento', header: 'Anomalie Man' },
      { field: 'anomalieCoordinamento', header: 'Anomalie Coor' },
      { field: 'supAnCoord', header: 'Sup An. Coord.' },
      { field: 'superficieDeterminata', header: 'Sup Determinata' },
      { field: 'superficieScostamento', header: 'Sup Scostamento' }
    ];

    this.colsGreening = [
      { field: 'codNazionale', header: 'Cod. Comune' },
      { field: 'foglio', header: 'Foglio' },
      { field: 'particella', header: 'Particella' },
      { field: 'sub', header: 'Sub' },
      { field: 'codiceColtura3', header: 'Cod. Coltura' },
      { field: 'descrizioneColtura', header: 'Descrizione Coltura', sortable: false },
      { field: 'superficieDeterminata', header: 'Sup Determinata' },
      { field: 'tipoColtura', header: 'Tipo di Coltura' },
      { field: 'tipoSeminativo', header: 'Tipo di Seminativo' },
      { field: 'colturaPrincipale', header: 'Coltura Principale' },
      { field: 'secondaColtura', header: 'Seconda Coltura' },
      { field: 'azotoFissatrice', header: 'Azoto Fissatrice' }
    ];

    this.colsMantenimento = [
      { field: 'codNazionale', header: 'Cod. Comune' },
      { field: 'foglio', header: 'Foglio' },
      { field: 'particella', header: 'Particella' },
      { field: 'sub', header: 'Sub' },
      { field: 'codiceColtura3', header: 'Cod. Coltura' },
      { field: 'descrizioneColtura', header: 'Descrizione Coltura', sortable: false },
      { field: 'pascolo', header: 'Pascolo' },
      { field: 'superficieImpegnata', header: 'Sup. imp. netta' },
      { field: 'superficieEleggibile', header: 'Sup. eleggibile' },
      { field: 'superficieSigeco', header: 'Sup. controllo in loco' },
      { field: 'anomalieMantenimento', header: 'Anomalie MAN' }
    ];

  }

  private getIdIstruttoria() {
    this.idIstruttoria = this.route.snapshot.data['domandaIstruttoria'].id;
  }

  private getDettaglio() {
    this.changePage1(this.tableEle, 0);
    this.first = this.first + 1;
  }

  private changePage1(event, update: number) {
    let first = 1;
    if (event) {
      first = event.first;
    }
    this.numeroPagina = Math.floor(first / this.elementiPerPagina);
    if (event != null && event.sortField != null) {
      this.paginazione = Paginazione.of(
        this.numeroPagina, this.elementiPerPagina, event.sortField, this.istruttoriaDettaglioService.getOrdine(event.sortOrder));
    } else {
      this.paginazione = Paginazione.of(
        this.numeroPagina, this.elementiPerPagina, 'id', 'ASC');
    }
    switch (update) {
      case 0: { //primo accesso alla pagina
        this.istruttoriaDettaglioService.getDatiParticellaIsNotPascoloDU(this.idIstruttoria.toString(), this.paginazione)
        .pipe(takeUntil(this.componentDestroyed$))
        .subscribe(
          dati => {
            if (dati != null) {
              console.log('Dettaglio Particelle trovate');
              const particelle = this.caricaParticelle(dati);
              this.listaDettaglioParticelleTable.elementiTotali = dati.elementiTotali;
              this.listaDettaglioParticelleTable.risultati = particelle;
              this.listaDettaglioParticelleTableGreening.elementiTotali = dati.elementiTotali;
              this.listaDettaglioParticelleTableGreening.risultati = particelle;
            }
          }, err => {
            this.messageService.add(A4gMessages.getToast(
              'tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
          });
        break;
      }
      case 1: { //aggiorna tabella Eleggibilità
        this.istruttoriaDettaglioService.getDatiParticellaIsNotPascoloDU(this.idIstruttoria.toString(), this.paginazione)
        .pipe(takeUntil(this.componentDestroyed$))
        .subscribe(
          dati => {
            if (dati != null) {
              console.log('Dettaglio Particelle trovate');
              const particelle = this.caricaParticelle(dati);
              this.listaDettaglioParticelleTable.elementiTotali = dati.elementiTotali;
              this.listaDettaglioParticelleTable.risultati = particelle;
            }
          }, err => {
            this.messageService.add(A4gMessages.getToast(
              'tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDatiEleggibilita));
          });
        break;
      }
      case 2: { //aggiorna tabella Greening
        this.istruttoriaDettaglioService.getDatiParticellaIsNotPascoloDU(this.idIstruttoria.toString(), this.paginazione)
        .pipe(takeUntil(this.componentDestroyed$))
        .subscribe(
          dati => {
            if (dati != null) {
              console.log('Dettaglio Particelle trovate');
              const particelle = this.caricaParticelle(dati);
              this.listaDettaglioParticelleTableGreening.elementiTotali = dati.elementiTotali;
              this.listaDettaglioParticelleTableGreening.risultati = particelle;
            }
          }, err => {
            this.messageService.add(A4gMessages.getToast(
              'tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDatiGreening));
          });
        break;
      }
      case 3: { //aggiorna tabella Mantenimento
        this.istruttoriaDettaglioService.getDatiParticellaIsPascoloDU(this.idIstruttoria.toString(), this.paginazione, true)
        .pipe(takeUntil(this.componentDestroyed$))
        .subscribe(
          dati => {
            if (dati != null) {
              console.log('Dettaglio Particelle trovate');
              const particelle = this.caricaParticelle(dati);
              this.listaDettaglioParticelleTableMantenimento.elementiTotali = dati.elementiTotali;
              this.listaDettaglioParticelleTableMantenimento.risultati = particelle;
            }
          }, err => {
            this.messageService.add(A4gMessages.getToast(
              'tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDatiMantenimento));
          });
        break;
      }
    }
  }

  private changePage(event, update: number) {
    if (this.idIstruttoria != null) {
      if (this.first === 3) {
        this.changePage1(event, update);
      } else {
        this.first = this.first + 1;
      }
    } else {
      this.first = this.first + 1;
    }
  }

  public changePageEleggibilita(event) {
    console.log('Recupero dettaglio particella changePageEleggibilita');
    this.changePage(event, 1);
  }

  public changePageGreening(event) {
    console.log('Recupero dettaglio particella changePageGreening');
    this.changePage(event, 2);
  }

  public changePageMantenimento(event) {
    console.log('Recupero dettaglio particella changePageMantenimento');
    this.changePage(event, 3);
  }

  private caricaParticelle(res: PaginaDettaglioCalcoloParticella): Array<DettaglioParticella> {
    const particelle = new Array<DettaglioParticella>();
    res.risultati.forEach(element => {
      // tslint:disable-next-line:prefer-const
      let particella = new DettaglioParticella();
      particella.id = element.infoCatastali.idParticella;
      particella.comuneCatastale = element.infoCatastali.comune;
      particella.codNazionale = element.infoCatastali.codNazionale;
      particella.foglio = element.infoCatastali.foglio;
      particella.particella = element.infoCatastali.particella;
      particella.sub = element.infoCatastali.sub;
      particella.codColtura = element.codiceColtura3;
      particella.descrizioneColtura = element.descrizioneColtura;
      particella.supImpegnata = element.variabiliCalcoloParticella.superficieImpegnata;
      particella.supEleggibile = element.variabiliCalcoloParticella.superficieEleggibile;
      particella.supSigeco = element.variabiliCalcoloParticella.superficieSigeco;
      particella.supDeterminata = element.variabiliCalcoloParticella.superficieDeterminata;
      particella.anomalieMantenimento = this.boolToString(element.variabiliCalcoloParticella.anomalieMantenimento);
      particella.anomalieCoordinamento = this.boolToString(element.variabiliCalcoloParticella.anomalieCoordinamento);
      particella.tipoColtura = element.variabiliCalcoloParticella.tipoColtura;
      particella.tipoSeminativo = element.variabiliCalcoloParticella.tipoSeminativo;
      particella.colturaPrincipale = this.boolToString(element.variabiliCalcoloParticella.colturaPrincipale);
      particella.secondaColtura = this.boolToString(element.variabiliCalcoloParticella.secondaColtura);
      particella.azotoFissatrice = this.boolToString(element.variabiliCalcoloParticella.azotoFissatrice);
      particella.pascolo = element.variabiliCalcoloParticella.pascolo;
      particella.supScostamento = element.variabiliCalcoloParticella.superficieScostamento;
//      particella.supScostamento = particella.supImpegnata - particella.supDeterminata;
      particella.superficieAnomalieCoordinamento = element.variabiliCalcoloParticella.superficieAnomalieCoordinamento;
      particelle.push(particella);
    });
    console.log(particelle);
    return particelle;
  }

  boolToString(val: boolean): string {
    if (val == null) {
      return '';
    }
    if (val === true) {
      return 'SI';
    }
    if (val === false) {
      return 'NO';
    }
  }

  public onRowExpanded(isExpanded: boolean, dettaglioParticellaId: number): void {
    console.log(`isExpanded: ${isExpanded}, dettaglioParticellaId: ${dettaglioParticellaId}`);
  }

  public parcelleResults = [];
  
  public getParcelleValues(dettaglioParticella: DettaglioParticella): Observable<[ParcelleLinkValue]> {
      const numeroDomanda = this.istruttoriaDUCorrente.domanda.numeroDomanda;
      const idDomanda = this.istruttoriaDUCorrente.domanda.id;
      const idParticella = dettaglioParticella.id;
      const codCultura = dettaglioParticella.codColtura;
      if (this.parcelleResults[idParticella] && this.parcelleResults[idParticella][codCultura]) {
        return of(this.parcelleResults[idParticella][codCultura]);
      } else {
        let fJoin = forkJoin([
          this.istruttoriaDettaglioService.getIstruttoriaGraficaDomandaUnica(numeroDomanda, this.istruttoriaDUCorrente.domanda.cuaaIntestatario),
          this.istruttoriaDettaglioService.findIdParcelleByDomandaParticellaIntervento(idDomanda, idParticella, codCultura)
        ]).pipe(
          map(ress => {
            const istGfx = ress[0];
            const idPrc = ress[1];
            let retVals: number[] = [];
            for (const el of idPrc) {
              retVals = retVals.concat(el.idParcelle.filter(el2 => !retVals.includes(el2)));
            }
            retVals.forEach(numeroParcella => {
              const pclLV: ParcelleLinkValue = new ParcelleLinkValue();
              if (istGfx) {
                const dataRiferimento: string = DateSupport.convertToPatternDate(istGfx.dataRiferimento, 'DD/MM/YYYY');
                const dataIstruttoria: string = DateSupport.convertToPatternDate(istGfx.dataIstruttoriaGrafica, 'DD/MM/YYYY');
                pclLV.link = this.configuration.siap_basepath
                  +`?azione=domandaGrafica&cuaa=${istGfx.cuaa}&abilitaEditing=NO&atto=${numeroDomanda}`
                  +`&decoTipoDocu=1950&op=IT25&anno=${istGfx.anno}`
                  +`&flagAcc=0&flagPartCata=0&flagParc=1&ufficio=6&dataRife=${dataRiferimento}`
                  +`&tipoVisu=3&dataIstr=${dataIstruttoria}&idParcAgri=${numeroParcella}`;
              } else {
                pclLV.link = null;
              }
              pclLV.value = numeroParcella;
              if (!this.parcelleResults[idParticella]) {
                this.parcelleResults[idParticella] = [];
              }
              if (!this.parcelleResults[idParticella][codCultura]) {
                this.parcelleResults[idParticella][codCultura] = [];
              }
              this.parcelleResults[idParticella][codCultura].push(pclLV);
            });
            return this.parcelleResults[idParticella][codCultura];
          })
        );
        return fJoin;
      }
  }

  public downloadCsv(tipoDati: number) {
    let TipoDatiStr: string = "";
    if (tipoDati== 1) {TipoDatiStr = "_DATI_ELEGGIBILITA";}
    if (tipoDati== 2) {TipoDatiStr = "_DATI_GREENING";}
    if (tipoDati== 3) {TipoDatiStr = "_DATI_MANTENIMENTO";}

    let data = new Date();
    let month = (data.getMonth() < 10) ? ("0" + (data.getMonth() + 1).toString()) : (data.getMonth()).toString();
    let day = (data.getDate() < 10) ? ("0" + data.getDate().toString()) : data.getDate().toString();
    let hours = (data.getHours() < 10) ? ("0" + data.getHours().toString()) : data.getHours().toString();
    let mins = (data.getMinutes() < 10) ? ("0" + data.getMinutes().toString()) : data.getMinutes().toString();
    let sec = (data.getSeconds() < 10) ? ("0" + data.getSeconds().toString()) : data.getSeconds().toString();
    // IDU-ANT-13-02 - il file avrà il seguente nome $CUAA_SOSTEGNO_$SOSTEGNO_$CAMPAGNA_$TIPO_PAGAMENTO_DATI_ELEGGIBILITA_YYYYMMDD_HH24miss.csv
    const fileName = this.istruttoriaDUCorrente.domanda.cuaaIntestatario + "_"
      + this.istruttoriaDUCorrente.sostegno + "_" 
      + this.istruttoriaDUCorrente.domanda.campagna + "_"
      + this.istruttoriaDUCorrente.tipo + TipoDatiStr + "_"
      + data.getFullYear().toString() 
      + month
      + day + "_" 
      + hours
      + mins
      + sec
      + ".csv";
    if (tipoDati >= 1 && tipoDati <= 3) {
      this.istruttoriaDettaglioService.getDCSVDatiParticelleDati(this.idIstruttoria, tipoDati)
      .subscribe(csv => {
        let csvBlob: Blob = new Blob([csv], { type: "text/plain;charset=utf-8" });
        FileSaver.saveAs(csvBlob, fileName);
      }, err => {
        this.messageService.add(A4gMessages.getToast("tst", A4gSeverityMessage.error, err.message));
      });
    }
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }
}
