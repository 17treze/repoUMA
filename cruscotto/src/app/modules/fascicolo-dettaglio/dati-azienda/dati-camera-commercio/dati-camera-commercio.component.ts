import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Subject } from 'rxjs';
import { switchMap, takeUntil } from 'rxjs/operators';
import { AttivitaAteco, FonteDatoAnagrafico, ImportanzaAttivitaEnum, IndirizzoDto, IscrizioneRepertorioEconomicoDto, PersonaDto, PersonaFisicaDto, PersonaGiuridicaDto, SedeDto } from 'src/app/shared/models/persona';
import { AnagraficaFascicoloService } from 'src/app/shared/services/anagrafica-fascicolo.service';
import { A4gAccordion, A4gAccordionField } from '../../a4g-accordion-tab/a4g-accordion.model';
import * as _ from 'lodash';
import { KeyValue } from '../../models/KeyValue';
import { formatDate } from '@angular/common';
import { FascicoloDettaglioService } from 'src/app/shared/services/fascicolo-dettaglio.service';

@Component({
  selector: 'app-dati-camera-commercio',
  templateUrl: './dati-camera-commercio.component.html',
  styleUrls: ['./dati-camera-commercio.component.scss']
})
export class DatiCameraCommercioComponent implements OnInit, OnDestroy {

  private componentDestroyed$: Subject<boolean> = new Subject();
  private idValidazione: number = undefined;
  private cuaa: string = undefined;
  public sezioniImpresa: PersonaDto;
  public ateco: AttivitaAteco[];
  public templates: A4gAccordion[];
  public personaFisica: PersonaFisicaDto;
  public personaGiuridica: PersonaGiuridicaDto;

  constructor(
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private fascicoloDettaglioService: FascicoloDettaglioService,
    private translateService: TranslateService,
    protected route: ActivatedRoute) { }


  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  ngOnInit() {
    this.fascicoloDettaglioService.fascicoloCorrente.pipe(
      switchMap(fascicoloCorrente => {
        this.cuaa = fascicoloCorrente.cuaa;
        return this.route.queryParams;
      }),
      takeUntil(this.componentDestroyed$)
    ).subscribe(queryParams => {
      const paramIdVal: string = queryParams['id-validazione'];
      if (paramIdVal) {
        this.idValidazione = Number.parseInt(paramIdVal, 10);
      } else {
        this.idValidazione = 0;
      }
      if (this.cuaa.length === 16) {
        this.getPersonaFisica(this.cuaa, this.idValidazione);
      } else if (this.cuaa.length === 11) {
        this.getPersonaGiuridica(this.cuaa, this.idValidazione);
      }
    });
  }

  private getPersonaGiuridica(cuaa: string, idValidazione: number) {
    this.anagraficaFascicoloService.getPersonaGiuridica(cuaa, idValidazione).subscribe(response => {
      this.personaGiuridica = response;
      this.sezioniImpresa = response;
      this.arrangeTabAndAccordionsPersonaGiuridica();
    });
  }

  private getPersonaFisica(cuaa: string, idValidazione: number) {
    this.anagraficaFascicoloService.getPersonaFisica(cuaa, idValidazione).subscribe(response => {
      this.personaFisica = response;
      this.arrangeTabAndAccordionsPersonaFisica();
    });
  }

  public prepareTemplatesPersonaGiuridicaAnagraficaRappresentanteLegaleAT() {
    this.templates = [];
    // DATI PERSONA GIURIDICA
    let templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'DATI PERSONA GIURIDICA';
    let keyValueArray = new Array<KeyValue>();

    const denominazione = this.personaGiuridica.denominazione || null;
    keyValueArray.push(this.buildKeyValue('Denominazione:', denominazione));

    const formaGiuridica = this.personaGiuridica.formaGiuridica || null;
    keyValueArray.push(this.buildKeyValue('Forma giuridica:', formaGiuridica));

    const codiceFiscale = this.personaGiuridica.codiceFiscale || null;
    keyValueArray.push(this.buildKeyValue('Codice Fiscale:', codiceFiscale));

    const partitaIVA = this.personaGiuridica.partitaIva || null;
    keyValueArray.push(this.buildKeyValue('Partita IVA:', partitaIVA));

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    // RAPPRESENTANTE LEGALE
    templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'RAPPRESENTANTE LEGALE';
    keyValueArray = new Array<KeyValue>();

    const nominativo = this.personaGiuridica.rappresentanteLegale && this.personaGiuridica.rappresentanteLegale.nominativo || null;
    keyValueArray.push(this.buildKeyValue('Nome e Cognome:', nominativo));

    const rappresentanteCF = this.personaGiuridica.rappresentanteLegale && this.personaGiuridica.rappresentanteLegale.codiceFiscale || null;
    keyValueArray.push(this.buildKeyValue('Codice Fiscale:', rappresentanteCF));

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);
  }

  private arrangeTabAndAccordionsPersonaGiuridica() {
    const personaGiuridica = this.personaGiuridica;
    if (personaGiuridica) {
      this.prepareAtecoVariables(personaGiuridica.sedeLegale);
      if (personaGiuridica.sedeLegale &&
        personaGiuridica.sedeLegale.iscrizioneRegistroImprese &&
        !personaGiuridica.sedeLegale.iscrizioneRegistroImprese.cessata) {
        this.prepareTemplatesPersonaGiuridicaDatiCameraDiCommercio();
        this.prepareTemplatesPersonaGiuridicaDatiSedeLegaleCameraDiCommercio();
      } else if (personaGiuridica.sedeLegale &&
        (personaGiuridica.sedeLegale.iscrizioneRegistroImprese == null || personaGiuridica.sedeLegale.iscrizioneRegistroImprese.cessata)) {
        this.prepareTemplatesPersonaGiuridicaAnagraficaRappresentanteLegaleAT()
        this.prepareTemplatesPersonaGiuridicaSedeLegaleAT();
      }
    }
  }

  // Popolamento accordion Dati Sede Legale della Persona Giuridica (Camera di Commercio)
  public prepareTemplatesPersonaGiuridicaDatiSedeLegaleCameraDiCommercio() {
    // SEDE LEGALE
    const templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'SEDE LEGALE';
    const keyValueArray = new Array<KeyValue>();

    this.prepareKeyValueArrayIndirizzoCameraDiCommercio(keyValueArray, this.personaGiuridica.sedeLegale ? this.personaGiuridica.sedeLegale : null);

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    if (this.ateco != null && this.ateco[0].fonteDato === FonteDatoAnagrafico.CAMERA_COMMERCIO) {
      this.prepareTemplatesCodiciAteco(this.ateco);
    }
  }

  private costruisciIndirizzoSedeLegale(sedeLegale: SedeDto): string {
    let indirizzo = '';
    const frazione = sedeLegale && sedeLegale.indirizzoCameraCommercio && sedeLegale.indirizzoCameraCommercio.frazione || null;
    const toponimo = sedeLegale && sedeLegale.indirizzoCameraCommercio && sedeLegale.indirizzoCameraCommercio.toponimo || null;
    const via = sedeLegale && sedeLegale.indirizzoCameraCommercio && sedeLegale.indirizzoCameraCommercio.via || null;
    const civico = sedeLegale && sedeLegale.indirizzoCameraCommercio && sedeLegale.indirizzoCameraCommercio.civico || null;
    if (frazione) { indirizzo = indirizzo + frazione + ' '; }
    if (toponimo) { indirizzo = indirizzo + toponimo + ' '; }
    if (via) { indirizzo = indirizzo + via + ', '; }
    if (civico) { indirizzo = indirizzo + civico; }
    return indirizzo;
  }

  private prepareKeyValueArrayIndirizzoCameraDiCommercio(keyValueArray: Array<KeyValue>, sedeLegale: SedeDto) {
    // Concateno i valori dell'indirizzo proviente da Parix
    const indirizzoSedeLegale = this.costruisciIndirizzoSedeLegale(sedeLegale);
    // Creo una variabile che contenga gli stessi valori dell'indirizzo Parix e setto l'indirizzo costruito in toponimo, così da poter
    // risfruttare prepareKeyValueArrayIndirizzoAT per le logiche comuni
    const indirizzoToSend = Object.assign({}, sedeLegale.indirizzoCameraCommercio);
    indirizzoToSend.toponimo = indirizzoSedeLegale;

    this.prepareKeyValueArrayIndirizzoAT(keyValueArray, indirizzoToSend, true);

    const codiceIstat = indirizzoToSend ? indirizzoToSend.codiceIstat : null;
    keyValueArray.push(this.buildKeyValue('Cod. ISTAT comune:', codiceIstat));

    const telefono = sedeLegale ? sedeLegale.telefono : null;
    keyValueArray.push(this.buildKeyValue('Telefono:', telefono));

    const indirizzoPec = sedeLegale && sedeLegale.indirizzoPec || null;
    keyValueArray.push(this.buildKeyValue('PEC:', indirizzoPec));
  }

  private prepareTemplatesPersonaGiuridicaSedeLegaleAT() {
    // SEDE LEGALE
    const templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'SEDE LEGALE';
    const keyValueArray = new Array<KeyValue>();

    this.prepareKeyValueArrayIndirizzoAT(keyValueArray, this.personaGiuridica.sedeLegale ? this.personaGiuridica.sedeLegale.indirizzo : null);

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    if (this.ateco != null && this.ateco[0].fonteDato === FonteDatoAnagrafico.ANAGRAFE_TRIBUTARIA) {
      this.prepareTemplatesCodiciAteco(this.ateco);
    }
  }

  buildA4gAccordionItem(kv: KeyValue, longTextValue: boolean): A4gAccordionField {
    return { ...kv, longText: longTextValue };
  }

  // Popolamento accordion Dati Persona Giuridica e Iscrizione alla Camera di Commercio della Persona Giuridica (Camera di Commercio)
  public prepareTemplatesPersonaGiuridicaDatiCameraDiCommercio() {
    this.templates = [];
    // DATI PERSONA GIURIDICA
    let templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'DATI PERSONA GIURIDICA';
    let keyValueArray = new Array<KeyValue>();

    const oggettoSociale = this.personaGiuridica.oggettoSociale || null;
    keyValueArray.push(this.buildA4gAccordionItem(this.buildKeyValue('Oggetto sociale:', oggettoSociale), true));

    const dataCostituzione = this.personaGiuridica.dataCostituzione || null;
    let dataCostituzioneFrmt = null;
    if (dataCostituzione) {
      dataCostituzioneFrmt = formatDate(dataCostituzione, 'dd/MM/yyyy', 'it-IT');
    }
    keyValueArray.push(this.buildKeyValue('Data costituzione:', dataCostituzioneFrmt));

    const dataTermine = this.personaGiuridica.dataTermine || null;
    let dataTermineFrmt = null;
    if (dataTermine) {
      dataTermineFrmt = formatDate(dataTermine, 'dd/MM/yyyy', 'it-IT');
    }
    keyValueArray.push(this.buildKeyValue('Data termine:', dataTermineFrmt));

    const capitaleSociale = this.personaGiuridica.capitaleSocialeDeliberato || null;
    let capitaleSocialeStr = null;
    if (capitaleSociale) {
      capitaleSocialeStr = capitaleSociale.toLocaleString() + ' euro';
    }
    keyValueArray.push(this.buildKeyValue('Capitale soc. deliberato:', capitaleSocialeStr));

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    // REPERTORIO ECONOMICO AMMINISTRATIVO (REA)
    templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'FascicoloAziendale.DATI_CAMERA_COMMERCIO.REPERTORIO_ECONOMICO_AMMINISTRATIVO';
    keyValueArray = new Array<KeyValue>();

    this.prepareKeyValueIRE(keyValueArray, this.personaGiuridica.sedeLegale ? this.personaGiuridica.sedeLegale.iscrizioneRegistroImprese : null);

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    // REGISTRO IMPRESA (// FAS-ANA-09)
    if (this.sezioniImpresa && this.sezioniImpresa.iscrizioniSezione.length > 0) {
      this.prepareKeyValueRegistroImp(this.sezioniImpresa ? this.sezioniImpresa : null);
    }
  }

  private prepareKeyValueRegistroImp(sezioniImpresa: PersonaDto) {
    // FAS-ANA-09 - BR1
    // Per ogni iscrizione, il sistema registra le seguenti informazioni:
    // descrizione, data di iscrizione
    // Per l’iscrizione alla sezione speciale: qualifica; se è un coltivatore diretto (vale solo per i Piccoli imprenditori)

    const templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'REGISTRO IMPRESA';
    const keyValueArray = new Array<A4gAccordionField>();
    let keyValue = new A4gAccordionField();
    let mvalue: string;

    sezioniImpresa.iscrizioniSezione.forEach(sezione => {
      keyValue.mkey = this.translateService.instant(sezione.sezione);
      mvalue = formatDate(sezione.dataIscrizione, 'dd/MM/yyyy', 'it-IT');
      if (sezione.qualifica) {
        mvalue = mvalue + ', ' + sezione.qualifica;
      }
      if (sezione.coltivatoreDiretto) {
        mvalue = mvalue + ', Coltivatore Diretto';
      }
      keyValue.mvalue = mvalue || null;

      keyValueArray.push(keyValue);
      keyValue = new A4gAccordionField();
    });

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);
  }

  private prepareKeyValueIRE(keyValueArray: Array<KeyValue>, iscrizioneRegistroImprese: IscrizioneRepertorioEconomicoDto) {
    const dataIscrizione = iscrizioneRegistroImprese && iscrizioneRegistroImprese.dataIscrizione || null;
    let dataIscrizioneFrmt = null;
    if (dataIscrizione) {
      dataIscrizioneFrmt = formatDate(dataIscrizione, 'dd/MM/yyyy', 'it-IT');
    }
    keyValueArray.push(this.buildKeyValue('Data costituzione:', dataIscrizioneFrmt));

    const codiceRea = iscrizioneRegistroImprese && iscrizioneRegistroImprese.codiceRea || null;
    keyValueArray.push(this.buildKeyValue('Codice REA:', codiceRea));

    const provinciaRea = iscrizioneRegistroImprese && iscrizioneRegistroImprese.provinciaRea || null;
    keyValueArray.push(this.buildKeyValue('Provincia REA:', provinciaRea));
  }

  private arrangeTabAndAccordionsPersonaFisica() {
    const impresaIndividuale = this.personaFisica.impresaIndividuale;
    if (impresaIndividuale) {
      this.prepareAtecoVariables(impresaIndividuale.sedeLegale);
      if (impresaIndividuale.sedeLegale &&
        impresaIndividuale.sedeLegale.iscrizioneRegistroImprese &&
        !impresaIndividuale.sedeLegale.iscrizioneRegistroImprese.cessata) {
        // CASO PERSONA FISICA CON DITTA INDIVIDUALE ISCRITTA IN CAMERA DI COMMERCIO/CON ISCRIZIONE ATTIVA
        this.cameraCommercioSelected();
      }
      /* Inibisco la visualizzazione dei dati inerenti la camera di commercio
      else if (impresaIndividuale.sedeLegale &&
        (impresaIndividuale.sedeLegale.iscrizioneRegistroImprese == null || impresaIndividuale.sedeLegale.iscrizioneRegistroImprese.cessata)) {
        // CASO PERSONA FISICA CON DITTA INDIVIDUALE NON ISCRITTA IN CAMERA DI COMMERCIO/CON ISCRIZIONE CESSATA
        this.prepareTemplatesPersonaFisicaAnagraficaDomicilioFiscaleAT();
        this.prepareTemplatesPersonaFisicaDittaIndividualeAT();
      }
      */
    } else {
      // CASO PERSONA FISICA SENZA DITTA INDIVIDUALE
      this.prepareTemplatesPersonaFisicaAnagraficaDomicilioFiscaleAT();
    }
  }

  private cameraCommercioSelected() {
    this.prepareTemplatesPersonaFisicaDatiCameraCommercio();
    this.prepareTemplatesPersonaFisicaSedeLegaleCameraDiCommercio();
  }

  public prepareTemplatesPersonaFisicaDatiCameraCommercio() {
    this.templates = [];
    // DATI DITTA INDIVIDUALE
    let templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'DATI DITTA INDIVIDUALE';
    let keyValueArray = new Array<KeyValue>();

    const formaGiuridica = this.personaFisica.impresaIndividuale.formaGiuridica;
    keyValueArray.push(this.buildKeyValue('Forma giuridica:', formaGiuridica));

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    // ISCRIZIONE CAMERA DI COMMERCIO
    templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'ISCRIZIONE CAMERA DI COMMERCIO';
    keyValueArray = new Array<KeyValue>();

    this.prepareKeyValueIRE(keyValueArray, (this.personaFisica.impresaIndividuale && this.personaFisica.impresaIndividuale.sedeLegale) ? this.personaFisica.impresaIndividuale.sedeLegale.iscrizioneRegistroImprese : null);

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    // REGISTRO IMPRESA (// FAS-ANA-09)
    if (this.personaFisica.iscrizioniSezione.length > 0) {
      this.prepareKeyValueRegistroImp(this.personaFisica ? this.personaFisica : null);
    }
  }

  public prepareTemplatesPersonaFisicaSedeLegaleCameraDiCommercio() {
    // SEDE LEGALE
    const templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'SEDE LEGALE';
    const keyValueArray = new Array<KeyValue>();

    this.prepareKeyValueArrayIndirizzoCameraDiCommercio(keyValueArray, (this.personaFisica.impresaIndividuale && this.personaFisica.impresaIndividuale.sedeLegale) ? this.personaFisica.impresaIndividuale.sedeLegale : null);

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    if (this.ateco[0].fonteDato === FonteDatoAnagrafico.CAMERA_COMMERCIO) {
      this.prepareTemplatesCodiciAteco(this.ateco);
    }
  }

  // Popolamento accordion Anagrafica e Domicilio Fiscale della persona Fisica
  public prepareTemplatesPersonaFisicaAnagraficaDomicilioFiscaleAT() {
    this.templates = [];
    // DATI PERSONA FISICA
    let templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'DATI PERSONA FISICA';
    let keyValueArray = new Array<KeyValue>();

    const nome = this.personaFisica.anagrafica && this.personaFisica.anagrafica.nome || null;
    keyValueArray.push(this.buildKeyValue('Nome:', nome));

    const cognome = this.personaFisica.anagrafica && this.personaFisica.anagrafica.cognome || null;
    keyValueArray.push(this.buildKeyValue('Cognome:', cognome));

    const codiceFiscale = this.personaFisica.codiceFiscale || null;
    keyValueArray.push(this.buildKeyValue('Codice Fiscale:', codiceFiscale));

    const sesso = this.personaFisica.anagrafica && this.personaFisica.anagrafica.sesso || null;
    keyValueArray.push(this.buildKeyValue('Sesso:', sesso));

    const luogoNascita = this.personaFisica.anagrafica && this.personaFisica.anagrafica.comuneNascita || null;
    keyValueArray.push(this.buildKeyValue('Nato a:', luogoNascita));

    let dataNascita = this.personaFisica.anagrafica && this.personaFisica.anagrafica.dataNascita || null;
    if (dataNascita) {
      dataNascita = formatDate(dataNascita, 'dd/MM/yyyy', 'it-IT') || null;
    }
    keyValueArray.push(this.buildKeyValue('In Data:', dataNascita));

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    // DOMICILIO FISCALE
    templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'DOMICILIO FISCALE';
    keyValueArray = new Array<KeyValue>();

    this.prepareKeyValueArrayIndirizzoAT(keyValueArray, this.personaFisica.domicilioFiscale);

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);
  }

  buildKeyValue(key: string, value: string) {
    const keyValue = new KeyValue();
    keyValue.mkey = key;
    keyValue.mvalue = value;
    return keyValue;
  }

  private prepareKeyValueArrayIndirizzoAT(keyValueArray: Array<KeyValue>, indirizzoDto: IndirizzoDto, indirizzoSedeLegale?: boolean) {
    const indirizzo = indirizzoDto && indirizzoDto.toponimo || null;
    if (indirizzoSedeLegale) {
      keyValueArray.push(this.buildKeyValue('Indirizzo sede legale:', indirizzo));
    } else {
      keyValueArray.push(this.buildKeyValue('Indirizzo:', indirizzo));
    }

    const comune = indirizzoDto ? indirizzoDto.comune : null;
    keyValueArray.push(this.buildKeyValue('Comune:', comune));

    const cap = indirizzoDto ? indirizzoDto.cap : null;
    keyValueArray.push(this.buildKeyValue('CAP:', cap));

    const provincia = indirizzoDto ? indirizzoDto.provincia : null;
    keyValueArray.push(this.buildKeyValue('Provincia:', provincia));
  }

  private prepareAtecoVariables(sede: SedeDto) {
    if (sede != null && sede.attivitaAteco != null &&
      sede.attivitaAteco.length > 0) {
      this.ateco = [];
      sede.attivitaAteco.forEach(attivita => {
        attivita.peso = this.setPeso(attivita);
        this.ateco.push(attivita);
      });
      this.ateco = _.orderBy(this.ateco, ['peso', 'codice'], ['asc', 'asc']);
    }
  }

  setPeso(attivita: AttivitaAteco): number {
    if (attivita.importanza === ImportanzaAttivitaEnum[ImportanzaAttivitaEnum.PRIMARIO_IMPRESA]) {
      return 0;
    } else if (attivita.importanza === ImportanzaAttivitaEnum[ImportanzaAttivitaEnum.PRIMARIO]) {
      return 1;
    } else if (attivita.importanza === ImportanzaAttivitaEnum[ImportanzaAttivitaEnum.SECONDARIO]) {
      return 2;
    }
    return 3;
  }

  // Popolamento accordion Dati Ditta Individuale della Persona Fisica e le eventuali Attività Ateco (Anagrafe Tributaria)
  public prepareTemplatesPersonaFisicaDittaIndividualeAT() {
    // DATI DITTA INDIVIDUALE
    const templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'DATI DITTA INDIVIDUALE';
    const keyValueArray = new Array<KeyValue>();

    const denominazione = this.personaFisica.impresaIndividuale && this.personaFisica.impresaIndividuale.denominazione || null;
    keyValueArray.push(this.buildKeyValue('Denominazione:', denominazione));

    const pIva = this.personaFisica.impresaIndividuale && this.personaFisica.impresaIndividuale.partitaIva || null;
    keyValueArray.push(this.buildKeyValue('Partita IVA:', pIva));

    this.prepareKeyValueArrayIndirizzoAT(keyValueArray, (this.personaFisica.impresaIndividuale && this.personaFisica.impresaIndividuale.sedeLegale) ? this.personaFisica.impresaIndividuale.sedeLegale.indirizzo : null, true);

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    if (this.ateco[0].fonteDato === FonteDatoAnagrafico.ANAGRAFE_TRIBUTARIA) {
      this.prepareTemplatesCodiciAteco(this.ateco);
    }
  }

  // Metodo generico per popolare l'accordion delle Attività Ateco
  private prepareTemplatesCodiciAteco(attivita: AttivitaAteco[]) {
    // CODICI ATECO
    const templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'CODICI ATECO';
    const keyValueArray = new Array<A4gAccordionField>();
    let keyValue = new A4gAccordionField();

    attivita.forEach(ateco => {
      keyValue.mkey = ateco.codice || null;
      keyValue.mvalue = ateco.descrizione || null;
      keyValue.showButton = ateco.importanza;
      keyValue.showButton = (ateco.importanza === ImportanzaAttivitaEnum[ImportanzaAttivitaEnum.PRIMARIO_IMPRESA]) ? "PRIMARIO D'IMPRESA" : ateco.importanza;
      keyValue.colorButton = (ateco.importanza === ImportanzaAttivitaEnum[ImportanzaAttivitaEnum.SECONDARIO]) ? 'grey' : 'green';

      keyValueArray.push(keyValue);
      keyValue = new A4gAccordionField();
    });

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);
  }
}
