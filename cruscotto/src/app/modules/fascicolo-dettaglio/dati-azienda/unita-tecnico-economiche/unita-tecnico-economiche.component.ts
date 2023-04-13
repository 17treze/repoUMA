import { A4gAccordionExt, A4gAccordionFieldExt } from './../../a4g-accordion-tab/a4g-accordion.model';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng-lts';
import { Subject } from 'rxjs';
import { switchMap, takeUntil } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { UnitaTecnicoEconomicheDto } from 'src/app/shared/models/UnitaTecnicoEconomicheDto';
import { AnagraficaFascicoloService } from 'src/app/shared/services/anagrafica-fascicolo.service';
import { A4gAccordion, A4gAccordionField } from '../../a4g-accordion-tab/a4g-accordion.model';
import { KeyValue, KeyValueExt } from '../../models/KeyValue';
import { DatePipe } from '@angular/common';
import { FascicoloDettaglioService } from 'src/app/shared/services/fascicolo-dettaglio.service';

@Component({
  selector: 'app-unita-tecnico-economiche',
  templateUrl: './unita-tecnico-economiche.component.html',
  styleUrls: ['./unita-tecnico-economiche.component.scss'],
  providers:[DatePipe]
})
export class UnitaTecnicoEconomicheComponent implements OnInit, OnDestroy {

  private componentDestroyed$: Subject<boolean> = new Subject();
  private idValidazione: number = undefined;
  private cuaa: string = undefined;
  public unitaTecnicoEconomiche: UnitaTecnicoEconomicheDto[];
  public templates: A4gAccordionExt[];

  constructor(
    private messageService: MessageService,
    private fascicoloDettaglioService: FascicoloDettaglioService,
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    protected route: ActivatedRoute,
    protected translateService: TranslateService,
    public datepipe: DatePipe) { }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  ngOnInit(): void {
    this.getIdValidazione();
    this.fascicoloDettaglioService.fascicoloCorrente.pipe(
      switchMap(fascicoloCorrente => {
        this.cuaa = fascicoloCorrente.cuaa;
        return this.anagraficaFascicoloService.getUnitaTecnicoEconomiche(this.cuaa, this.idValidazione);
      }),
      takeUntil(this.componentDestroyed$)
    ).subscribe(resp => {
      if (resp) {
        this.unitaTecnicoEconomiche = resp.sort((a, b) =>
        (parseInt(a.identificativoUte, 10) > parseInt(b.identificativoUte, 10)) ? 1
        : ((parseInt(b.identificativoUte, 10) > parseInt(a.identificativoUte, 10)) ? -1 : 0));
        this.arrangeTabAndAccordionsUnitaTecnicoEconomiche();
      }
    }, err => {
      if (err.status === 403) {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('HttpErrorMessages.403')));
      } else {
        this.messageService.add(A4gMessages.getToast(
          'tst',
          A4gSeverityMessage.error,
          this.translateService.instant('FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.ERRORE_GET_UNITA_TECNICO_ECONOMICHE')
        ));
      }
    });
  }

  private getIdValidazione() {
    this.route.queryParams.subscribe(queryParams => this.idValidazione = queryParams['id-validazione']);
  }

  // Popolamento accordion Unita Tecnico Locali
  public arrangeTabAndAccordionsUnitaTecnicoEconomiche() {
    this.templates = [];
    if (Array.isArray(this.unitaTecnicoEconomiche) && this.unitaTecnicoEconomiche.length) {
      for (let entry of this.unitaTecnicoEconomiche) {
        let templateToAdd = {} as A4gAccordionExt;
        templateToAdd.headerTitle = 'Unita Tecnico Locale ' + entry.identificativoUte + ' : ' + (entry.attivita == null ? '' : entry.attivita);

        let keyValueArrayExt = new Array<KeyValueExt>();
        let dataAperturaKeyValueArray = new Array<KeyValue>();
        // Data apertura
        let dataApertura = entry.dataApertura || null;
        if (dataApertura) {
          dataAperturaKeyValueArray.push(this.buildKeyValue(this.datepipe.transform(dataApertura, 'dd-MM-yyyy'), ""));
        }
        keyValueArrayExt.push(this.buildA4gAccordionItemExt(this.buildKeyValueExt('FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.DT_APERTURA', dataAperturaKeyValueArray), false, true));
        // Dati di cessazione
        keyValueArrayExt.push(this.buildA4gAccordionItemExt(this.buildKeyValueExt('FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.DATI_CESSAZIONE', this.buildDatiCessazioneStruttura(entry)), false, true));
        // Commercio al dettaglio
        keyValueArrayExt.push(this.buildA4gAccordionItemExt(this.buildKeyValueExt('FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.COMMERCIO_DETTAGLIO', this.buildCommercioAlDettaglioStruttura(entry)), false, true));
        // Indirizzo dell'unita
        keyValueArrayExt.push(this.buildA4gAccordionItemExt(this.buildKeyValueExt('FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.INDIRIZZO_UNITA', this.buildIndirizzoStruttura(entry)), false, true));
        // Lista di destinazioni d'uso dell'unita
        keyValueArrayExt.push(this.buildA4gAccordionItemExt(this.buildKeyValueExt('FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.LISTA_DEST_USO', this.buildDestinazioneUsoStruttura(entry)), false, true));
        // Lista attivita ATECO
        keyValueArrayExt.push(this.buildA4gAccordionItemExt(this.buildKeyValueExt('FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.LISTA_ATTIVITA_ATECO', this.buildAttivitaAtecoStruttura(entry)), false, true));

        templateToAdd.fields = keyValueArrayExt;
        this.templates.push(templateToAdd);
      }
    }
  }

  private buildDestinazioneUsoStruttura(entry: UnitaTecnicoEconomicheDto){
    let destinazioniUsoKeyValueArray = new Array<KeyValue>();
    if (Array.isArray(entry.destinazioniUso) && entry.destinazioniUso.length) {
      for (let destinazioneUso of entry.destinazioniUso) {
        destinazioniUsoKeyValueArray.push(this.buildKeyValue("", destinazioneUso.descrizione));
      }
    }
    return destinazioniUsoKeyValueArray;
  }
  private buildAttivitaAtecoStruttura(entry: UnitaTecnicoEconomicheDto){
    let attivitaAtecoKeyValueArray = new Array<KeyValue>();
    if (Array.isArray(entry.attivitaAteco) && entry.attivitaAteco.length) {
      for (let attivita of entry.attivitaAteco) {
        attivitaAtecoKeyValueArray.push(this.buildKeyValue(attivita.codice, attivita.descrizione));
      }
    }
    return attivitaAtecoKeyValueArray;
  }

  private buildCommercioAlDettaglioStruttura(entry: UnitaTecnicoEconomicheDto){
    let commercioAlDettaglioKeyValueArray = new Array<KeyValue>();
    let dataInizioDettaglio = entry.dataDenunciaInizioAttivita || null;
    let settoreMerceologico = entry.settoreMerceologico || null;
    let recapitoTelefonico = entry.telefono || null;
    let indirizzoPec = entry.indirizzoPec || null;
    let dataInizioDettaglioParsed = null;
    if (dataInizioDettaglio) {
      dataInizioDettaglioParsed = this.datepipe.transform(dataInizioDettaglio, 'dd-MM-yyyy');
    }
    commercioAlDettaglioKeyValueArray.push(this.buildKeyValue("FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.COMMERCIO_DETTAGLIO_STRUCT.DT_INIZIO_ATT_COMM_DETT", dataInizioDettaglioParsed));
    commercioAlDettaglioKeyValueArray.push(this.buildKeyValue("FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.COMMERCIO_DETTAGLIO_STRUCT.SETTORE_MERCEOLOGICO", settoreMerceologico));
    commercioAlDettaglioKeyValueArray.push(this.buildKeyValue("FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.COMMERCIO_DETTAGLIO_STRUCT.RECAPITO_TEL", recapitoTelefonico));
    commercioAlDettaglioKeyValueArray.push(this.buildKeyValue("FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.COMMERCIO_DETTAGLIO_STRUCT.PEC", indirizzoPec));
    return commercioAlDettaglioKeyValueArray;
  }

  private buildDatiCessazioneStruttura(entry: UnitaTecnicoEconomicheDto){
    let datiCessazioneKeyValueArray = new Array<KeyValue>();
    let dataCessazione = entry.dataCessazione || null;
    let dataDenunciaCessazione = entry.dataDenunciaCessazione || null;
    let motivoCessazione = entry.causaleCessazione || null;
    let dataCessazioneParsed = null;
    let dataDenunciaCessazioneParsed = null;
    if (dataCessazione) {
      dataCessazioneParsed = this.datepipe.transform(dataCessazione, 'dd-MM-yyyy');
    }
    datiCessazioneKeyValueArray.push(this.buildKeyValue("FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.DATI_CESSAZIONE_STRUCT.DT_CESSAZIONE", dataCessazioneParsed));

    if (dataDenunciaCessazione) {
      dataDenunciaCessazioneParsed = this.datepipe.transform(dataDenunciaCessazione, 'dd-MM-yyyy');
    }
    datiCessazioneKeyValueArray.push(this.buildKeyValue("FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.DATI_CESSAZIONE_STRUCT.DT_DENUNCIA_CESSAZIONE", dataDenunciaCessazioneParsed));
    datiCessazioneKeyValueArray.push(this.buildKeyValue("FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.DATI_CESSAZIONE_STRUCT.MOTIVO_CESSAZIONE", motivoCessazione));
    return datiCessazioneKeyValueArray;
  }
  private buildIndirizzoStruttura(entry: UnitaTecnicoEconomicheDto){
    let indirizzoKeyValueArray = new Array<KeyValue>();
    let toponimoUnita = entry.toponimo || null;
    let viaUnita = entry.via || null;
    let numeroCivico = entry.numeroCivico || null;
    let comune = entry.comune || null;
    let cap = entry.cap || null;
    let codiceIstatComune = entry.codiceIstatComune || null;
    indirizzoKeyValueArray.push(this.buildKeyValue('FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.INDIRIZZO_UNITA_STRUCT.TOPONIMO', toponimoUnita));
    indirizzoKeyValueArray.push(this.buildKeyValue('FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.INDIRIZZO_UNITA_STRUCT.VIA', viaUnita));
    indirizzoKeyValueArray.push(this.buildKeyValue('FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.INDIRIZZO_UNITA_STRUCT.NUMERO_CIVICO', numeroCivico));
    indirizzoKeyValueArray.push(this.buildKeyValue('FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.INDIRIZZO_UNITA_STRUCT.COMUNE', comune));
    indirizzoKeyValueArray.push(this.buildKeyValue('FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.INDIRIZZO_UNITA_STRUCT.CAP', cap));
    indirizzoKeyValueArray.push(this.buildKeyValue('FascicoloAziendale.UNITA_TECNICO_ECONOMICHE.INDIRIZZO_UNITA_STRUCT.CODICE_ISTAT_COMUNE', codiceIstatComune));
    return indirizzoKeyValueArray;
  }


  buildKeyValueExt(key: string, value: Array<KeyValue>) {
    const keyValueExt = new KeyValueExt();
    keyValueExt.mkey = key;
    keyValueExt.mvalue = value;
    return keyValueExt;
  }

  buildA4gAccordionItemExt(kv: KeyValueExt, longTextValue: boolean, noBorder: boolean): A4gAccordionFieldExt {
    return { ...kv, longText: longTextValue, noBorder };
  }

  buildKeyValue(key: string, value: string) {
    const keyValue = new KeyValue();
    keyValue.mkey = key;
    keyValue.mvalue = value;
    return keyValue;
  }

  buildA4gAccordionItem(kv: KeyValue, longTextValue: boolean): A4gAccordionField {
    return { ...kv, longText: longTextValue };
  }

}
