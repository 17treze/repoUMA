import { formatDate } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs';
import * as _ from 'lodash';
import { switchMap, takeUntil } from 'rxjs/operators';
import { AttivitaAteco, FonteDatoAnagrafico, ImportanzaAttivitaEnum, IndirizzoDto, PersonaFisicaDto, PersonaGiuridicaDto, SedeDto } from 'src/app/shared/models/persona';
import { AnagraficaFascicoloService } from 'src/app/shared/services/anagrafica-fascicolo.service';
import { A4gAccordion, A4gAccordionField } from '../../a4g-accordion-tab/a4g-accordion.model';
import { KeyValue } from '../../models/KeyValue';
import { FascicoloDettaglioService } from 'src/app/shared/services/fascicolo-dettaglio.service';

@Component({
  selector: 'app-dati-anagrafe-tributaria',
  templateUrl: './dati-anagrafe-tributaria.component.html',
  styleUrls: ['./dati-anagrafe-tributaria.component.scss']
})
export class DatiAnagrafeTributariaComponent implements OnInit, OnDestroy {

  public cuaa = '';
  public personaFisica: PersonaFisicaDto = undefined;
  public personaGiuridica: PersonaGiuridicaDto = undefined;
  public templates: A4gAccordion[];
  public ateco: AttivitaAteco[];
  private componentDestroyed$: Subject<boolean> = new Subject();
  private idValidazione: number = undefined;

  constructor(
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private fascicoloDettaglioService: FascicoloDettaglioService,
    protected route: ActivatedRoute
  ) { }


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
      this.arrangeTabAndAccordionsPersonaGiuridica();
    });
  }

  private getPersonaFisica(cuaa: string, idValidazione: number) {
    this.anagraficaFascicoloService.getPersonaFisica(cuaa, idValidazione).subscribe(response => {
      this.personaFisica = response;
      this.arrangeTabAndAccordionsPersonaFisica();
    });
  }

  private buildKeyValue(key: string, value: string) {
    const keyValue = new KeyValue();
    keyValue.mkey = key;
    keyValue.mvalue = value;
    return keyValue;
  }

  private arrangeTabAndAccordionsPersonaFisica() {
    const impresaIndividuale = this.personaFisica.impresaIndividuale;
    if (impresaIndividuale) {
      this.prepareAtecoVariables(impresaIndividuale.sedeLegale);
      if (impresaIndividuale.sedeLegale &&
        impresaIndividuale.sedeLegale.iscrizioneRegistroImprese &&
        !impresaIndividuale.sedeLegale.iscrizioneRegistroImprese.cessata) {
        // CASO PERSONA FISICA CON DITTA INDIVIDUALE ISCRITTA IN CAMERA DI COMMERCIO/CON ISCRIZIONE ATTIVA
        this.anagrafeTributariaSelected();
      } else if (impresaIndividuale.sedeLegale &&
        (impresaIndividuale.sedeLegale.iscrizioneRegistroImprese == null || impresaIndividuale.sedeLegale.iscrizioneRegistroImprese.cessata)) {
        // CASO PERSONA FISICA CON DITTA INDIVIDUALE NON ISCRITTA IN CAMERA DI COMMERCIO/CON ISCRIZIONE CESSATA
        this.prepareTemplatesPersonaFisicaAnagrificaDomicilioFiscaleAT();
        this.prepareTemplatesPersonaFisicaDittaIndividualeAT();
      }
    } else {
      // CASO PERSONA FISICA SENZA DITTA INDIVIDUALE
      this.prepareTemplatesPersonaFisicaAnagrificaDomicilioFiscaleAT();
    }
  }

  private anagrafeTributariaSelected() {
    this.prepareTemplatesPersonaFisicaAnagrificaDomicilioFiscaleAT();
    this.prepareTemplatesPersonaFisicaDittaIndividualeAT();
  }

  // Popolamento accordion Anagrafica e Domicilio Fiscale della persona Fisica
  public prepareTemplatesPersonaFisicaAnagrificaDomicilioFiscaleAT() {
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

    if (this.ateco && this.ateco[0].fonteDato === FonteDatoAnagrafico.ANAGRAFE_TRIBUTARIA) {
      this.prepareTemplatesCodiciAteco(this.ateco);
    }
  }

  private arrangeTabAndAccordionsPersonaGiuridica() {
    const personaGiuridica = this.personaGiuridica;
    if (personaGiuridica) {
      this.prepareAtecoVariables(personaGiuridica.sedeLegale);
      this.prepareTemplatesPersonaGiuridicaAnagrificaRappresentanteLegaleAT();
      this.prepareTemplatesPersonaGiuridicaSedeLegaleAT();
    }
  }

  public prepareTemplatesPersonaGiuridicaAnagrificaRappresentanteLegaleAT() {
    this.templates = [];
    // DATI PERSONA GIURIDICA
    let templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'FascicoloAziendale.DATI_CAMERA_COMMERCIO.DATI_PERSONA_GIURIDICA';
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
    templateToAdd.headerTitle = 'FascicoloAziendale.DATI_CAMERA_COMMERCIO.RAPPRESENTANTE_LEGALE';
    keyValueArray = new Array<KeyValue>();

    const nominativo = this.personaGiuridica.rappresentanteLegale && this.personaGiuridica.rappresentanteLegale.nominativo || null;
    keyValueArray.push(this.buildKeyValue('Nome e Cognome:', nominativo));

    const rappresentanteCF = this.personaGiuridica.rappresentanteLegale && this.personaGiuridica.rappresentanteLegale.codiceFiscale || null;
    keyValueArray.push(this.buildKeyValue('Codice Fiscale:', rappresentanteCF));

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);
  }

  private prepareTemplatesPersonaGiuridicaSedeLegaleAT() {
    // SEDE LEGALE
    const templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'FascicoloAziendale.DATI_CAMERA_COMMERCIO.SEDE_LEGALE';
    const keyValueArray = new Array<KeyValue>();

    this.prepareKeyValueArrayIndirizzoAT(keyValueArray, this.personaGiuridica.sedeLegale ? this.personaGiuridica.sedeLegale.indirizzo : null);

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    if (this.ateco != null && this.ateco[0].fonteDato === FonteDatoAnagrafico.ANAGRAFE_TRIBUTARIA) {
      this.prepareTemplatesCodiciAteco(this.ateco);
    }
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

  buildA4gAccordionItem(kv: KeyValue, longTextValue: boolean): A4gAccordionField {
    return { ...kv, longText: longTextValue };
  }

}
