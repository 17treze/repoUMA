import { StatoFascicoloEnum } from './../../../creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MenuItem, MessageService } from 'primeng/api';
import { A4gAccordion, A4gAccordionField } from 'src/app/a4g-common/a4g-accordion-tab/a4g-accordion.model';
import { KeyValue } from 'src/app/a4g-common/classi/KeyValue';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { AnagraficaFascicoloService } from 'src/app/fascicolo/creazione-fascicolo/anagrafica-fascicolo.service';
import { FascicoloDettaglioService } from 'src/app/fascicolo/fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import { AttivitaAteco, FonteDatoAnagrafico, ImportanzaAttivitaEnum, IndirizzoDto, IscrizioneRepertorioEconomicoDto, PersonaDto, PersonaFisicaConCaricaDto, PersonaFisicaDto, PersonaGiuridicaDto, SedeDto } from 'src/app/fascicolo/creazione-fascicolo/dto/PersonaDto';
import { formatDate } from '@angular/common';
import * as _ from 'lodash';
import { FascicoloDettaglio } from 'src/app/fascicolo/shared/fascicolo.model';
import { TranslateService } from '@ngx-translate/core';
import { FascicoloDaCuaa } from 'src/app/a4g-common/classi/FascicoloCuaa';
import { EMPTY, of, Subject } from 'rxjs';
import { catchError, switchMap, takeUntil, tap } from 'rxjs/operators';
import { MediatorService } from "../../../mediator.service";

@Component({
  selector: 'app-dati-azienda',
  templateUrl: './dati-azienda.component.html',
  styleUrls: ['./dati-azienda.component.css']
})
export class DatiAziendaComponent implements OnInit, OnDestroy {
  public personaFisica: PersonaFisicaDto;
  public personaGiuridica: PersonaGiuridicaDto;
  public sezioniImpresa: PersonaDto;
  public nome: string;
  public cognome: string;
  public templates: A4gAccordion[];
  public ateco: AttivitaAteco[];
  public tabPersoneConCarica: boolean = false;
  public tabRegistroImpresa: boolean = false;
  public tabUnitaTecnicoEconomiche: boolean = false;
  public tabs: MenuItem[] = [];
  private fascicoloCorrente: FascicoloDaCuaa;
  protected componentDestroyed$: Subject<boolean> = new Subject();
  private idValidazione: number = undefined;
  public updated: Subject<void> = new Subject();
  public updateError: string;

  constructor(
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private route: ActivatedRoute,
    private router: Router,
    private fascicoloDettaglioService: FascicoloDettaglioService,
    private translateService: TranslateService,
    private messageService: MessageService,
    private mediatorService: MediatorService) {
  }

  private subscribeStatoFascicolo() {
    this.fascicoloDettaglioService.fascicoloCorrente.pipe(
      takeUntil(this.componentDestroyed$))
      .subscribe((fascicoloCorrente: FascicoloDaCuaa) => {
        if (fascicoloCorrente) {
          this.fascicoloCorrente = fascicoloCorrente;
        }
      });
  }

  ngOnInit() {
    let cuaa: string = undefined;
    this.subscribeStatoFascicolo();
    this.route.params.pipe(
      takeUntil(this.componentDestroyed$),
      switchMap(urlParams => {
        cuaa = urlParams['cuaa'];
        return this.route.queryParams;
      }),
      switchMap(queryParams => {
        const paramIdVal: string = queryParams['id-validazione'];
        if (paramIdVal) {
          this.idValidazione = Number.parseInt(paramIdVal);
        } else {
          this.idValidazione = 0;
        }
        if (cuaa.length === 16) {
          return this.getPersonaFisica(cuaa);
        } else if (cuaa.length === 11) {
          return this.getPersonaGiuridica(cuaa);
        }
        return EMPTY;
      })
    ).subscribe();
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  private getPersonaGiuridica(cuaa: string) {
    return this.anagraficaFascicoloService.getPersonaGiuridica(cuaa, this.idValidazione).pipe(
      takeUntil(this.componentDestroyed$),
      tap(response => {
        this.personaGiuridica = response;
        this.arrangeTabAndAccordionsPersonaGiuridica();
      })
    );
  }

  private getPersonaFisica(cuaa: string) {
    return this.anagraficaFascicoloService.getPersonaFisica(cuaa, this.idValidazione).pipe(
      takeUntil(this.componentDestroyed$),
      tap(response => {
        this.personaFisica = response;
        this.arrangeTabAndAccordionsPersonaFisica();
      })
    );
  }

  private anagrafeTributariaSelected() {
    this.prepareTemplatesPersonaFisicaAnagrificaDomicilioFiscaleAT();
    this.prepareTemplatesPersonaFisicaDittaIndividualeAT();
  }

  private cameraCommercioSelected() {
    this.prepareTemplatesPersonaFisicaDatiCameraCommercio();
    this.prepareTemplatesPersonaFisicaSedeLegaleCameraDiCommercio();
  }

  public fascicoloEditable(): boolean {
    return this.fascicoloCorrente && this.idValidazione === 0 && (
      this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_AGGIORNAMENTO
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.VALIDATO
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.CONTROLLATO_OK
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_CHIUSURA);
  }

  private arrangeTabAndAccordionsPersonaFisica() {
    let impresaIndividuale = this.personaFisica.impresaIndividuale;
    if (impresaIndividuale) {
      this.prepareAtecoVariables(impresaIndividuale.sedeLegale);
      if (impresaIndividuale.sedeLegale &&
        impresaIndividuale.sedeLegale.iscrizioneRegistroImprese &&
        !impresaIndividuale.sedeLegale.iscrizioneRegistroImprese.cessata) {
        // CASO PERSONA FISICA CON DITTA INDIVIDUALE ISCRITTA IN CAMERA DI COMMERCIO/CON ISCRIZIONE ATTIVA
        this.anagrafeTributariaSelected();
        this.tabs = new Array<MenuItem>(
          {
            label: 'Dati Anagrafe Tributaria', command: (event) => {
              this.anagrafeTributariaSelected();
            }
          },
          {
            label: 'Dati Camera Di Commercio', command: (event) => {
              this.cameraCommercioSelected();
            }
          },
          {
            label: 'Persone con cariche', command: (event) => {
              this.setTabDefault();
              this.tabPersoneConCarica = true;
            }
          }
        );
      } else if (impresaIndividuale.sedeLegale &&
        (impresaIndividuale.sedeLegale.iscrizioneRegistroImprese == null || impresaIndividuale.sedeLegale.iscrizioneRegistroImprese.cessata)) {
        // CASO PERSONA FISICA CON DITTA INDIVIDUALE NON ISCRITTA IN CAMERA DI COMMERCIO/CON ISCRIZIONE CESSATA
        this.prepareTemplatesPersonaFisicaAnagrificaDomicilioFiscaleAT();
        this.prepareTemplatesPersonaFisicaDittaIndividualeAT();
        this.tabs = new Array<MenuItem>(
          {
            label: 'Dati Anagrafe Tributaria', command: (event) => {
              this.prepareTemplatesPersonaFisicaAnagrificaDomicilioFiscaleAT();
              this.prepareTemplatesPersonaFisicaDittaIndividualeAT();
            }
          }
        );
      }
    } else {
      // CASO PERSONA FISICA SENZA DITTA INDIVIDUALE
      this.prepareTemplatesPersonaFisicaAnagrificaDomicilioFiscaleAT();
      this.tabs = new Array<MenuItem>(
        {
          label: 'Dati Anagrafe Tributaria', command: (event) => {
            this.prepareTemplatesPersonaFisicaAnagrificaDomicilioFiscaleAT();
          }
        }
      );
    }
  }

  private getSezioni(cuaa: string) {
    this.anagraficaFascicoloService.getSezioniImpresa(cuaa).subscribe(response => {
      this.sezioniImpresa = response;
    },
      err => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, A4gMessages.errorGetUnitaTecnicoEconomiche));
      });
  }

  private arrangeTabAndAccordionsPersonaGiuridica() {
    let personaGiuridica = this.personaGiuridica;
    if (personaGiuridica) {
      let cuaa = this.route.snapshot.paramMap.get('cuaa');
      this.getSezioni(cuaa);

      this.prepareAtecoVariables(personaGiuridica.sedeLegale);
      if (personaGiuridica.sedeLegale &&
        personaGiuridica.sedeLegale.iscrizioneRegistroImprese &&
        !personaGiuridica.sedeLegale.iscrizioneRegistroImprese.cessata) {
        this.prepareTemplatesPersonaGiuridicaAnagrificaRappresentanteLegaleAT()
        this.prepareTemplatesPersonaGiuridicaSedeLegaleAT();
        // CASO PERSONA GIURIDICA CON IMPRESA ATTIVA
        this.tabs = new Array<MenuItem>(
          {
            label: 'Dati Anagrafe Tributaria', command: (event) => {
              this.setTabDefault();
              this.prepareTemplatesPersonaGiuridicaAnagrificaRappresentanteLegaleAT()
              this.prepareTemplatesPersonaGiuridicaSedeLegaleAT();
            }
          },
          {
            label: 'Dati Camera Di Commercio', command: (event) => {
              this.setTabDefault();
              this.prepareTemplatesPersonaGiuridicaDatiCameraDiCommercio();
              this.prepareTemplatesPersonaGiuridicaDatiSedeLegaleCameraDiCommercio();
            }
          },
          {
            label: 'Persone con cariche', command: (event) => {
              this.setTabDefault();
              this.tabPersoneConCarica = true;
            }
          },
          {
            label: 'Unità locale', command: (event) => {
              this.setTabDefault();
              this.tabUnitaTecnicoEconomiche = true;
            }
          }
        );
      } else if (personaGiuridica.sedeLegale &&
        (personaGiuridica.sedeLegale.iscrizioneRegistroImprese == null || personaGiuridica.sedeLegale.iscrizioneRegistroImprese.cessata)) {
        this.prepareTemplatesPersonaGiuridicaAnagrificaRappresentanteLegaleAT()
        this.prepareTemplatesPersonaGiuridicaSedeLegaleAT();
        // CASO PERSONA GIURIDICA CON IMPRESA CESSATA
        this.tabs = new Array<MenuItem>(
          {
            label: 'Dati Anagrafe Tributaria', command: (event) => {
              this.prepareTemplatesPersonaGiuridicaAnagrificaRappresentanteLegaleAT()
              this.prepareTemplatesPersonaGiuridicaSedeLegaleAT();
            }
          }
        );
      }
    }
  }

  // Popolamento accordion Anagrafica e Domicilio Fiscale della persona Fisica
  public prepareTemplatesPersonaFisicaAnagrificaDomicilioFiscaleAT() {
    this.templates = [];
    // DATI PERSONA FISICA
    let templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'DATI PERSONA FISICA';
    let keyValueArray = new Array<KeyValue>();

    let nome = this.personaFisica.anagrafica && this.personaFisica.anagrafica.nome || null;
    keyValueArray.push(this.buildKeyValue('Nome:', nome));

    let cognome = this.personaFisica.anagrafica && this.personaFisica.anagrafica.cognome || null;
    keyValueArray.push(this.buildKeyValue('Cognome:', cognome));

    let codiceFiscale = this.personaFisica.codiceFiscale || null;
    keyValueArray.push(this.buildKeyValue('Codice Fiscale:', codiceFiscale));

    let sesso = this.personaFisica.anagrafica && this.personaFisica.anagrafica.sesso || null;
    keyValueArray.push(this.buildKeyValue('Sesso:', sesso));

    let luogoNascita = this.personaFisica.anagrafica && this.personaFisica.anagrafica.comuneNascita || null;
    keyValueArray.push(this.buildKeyValue('Nato a:', luogoNascita));

    let dataNascita = this.personaFisica.anagrafica && this.personaFisica.anagrafica.dataNascita || null;
    if (dataNascita) {
      dataNascita = formatDate(dataNascita, 'dd/MM/yyyy', 'it-IT') || null;
    }
    keyValueArray.push(this.buildKeyValue('Nato il:', dataNascita));

    if (this.personaFisica.anagrafica && this.personaFisica.anagrafica.deceduto) {
      let dataMorte = this.personaFisica.anagrafica && this.personaFisica.anagrafica.dataMorte || null;
      if (dataMorte) {
        dataMorte = formatDate(dataMorte, 'dd/MM/yyyy', 'it-IT') || null;
      }
      keyValueArray.push(this.buildKeyValue('Morto il:', dataMorte));
    }

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

  // Popolamento accordion Dati Ditta Individuale della Persona Fisica e le eventuali Attività Ateco (Anagrafe Tributaria)
  public prepareTemplatesPersonaFisicaDittaIndividualeAT() {
    // DATI DITTA INDIVIDUALE
    const templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'DATI DITTA INDIVIDUALE';
    const keyValueArray = new Array<KeyValue>();

    let denominazione = this.personaFisica.impresaIndividuale && this.personaFisica.impresaIndividuale.denominazione || null;
    keyValueArray.push(this.buildKeyValue('Denominazione:', denominazione));

    let pIva = this.personaFisica.impresaIndividuale && this.personaFisica.impresaIndividuale.partitaIva || null;
    keyValueArray.push(this.buildKeyValue('Partita IVA:', pIva));

    this.prepareKeyValueArrayIndirizzoAT(keyValueArray, (this.personaFisica.impresaIndividuale && this.personaFisica.impresaIndividuale.sedeLegale) ? this.personaFisica.impresaIndividuale.sedeLegale.indirizzo : null, true);

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    if (this.ateco[0].fonteDato === FonteDatoAnagrafico.ANAGRAFE_TRIBUTARIA) {
      this.prepareTemplatesCodiciAteco(this.ateco);
    }
  }

  public prepareTemplatesPersonaFisicaDatiCameraCommercio() {
    this.templates = [];
    // DATI DITTA INDIVIDUALE
    let templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'DATI DITTA INDIVIDUALE';
    let keyValueArray = new Array<KeyValue>();

    let formaGiuridica = this.personaFisica.impresaIndividuale.formaGiuridica;
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
    let templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'SEDE LEGALE';
    let keyValueArray = new Array<KeyValue>();

    this.prepareKeyValueArrayIndirizzoCameraDiCommercio(keyValueArray, (this.personaFisica.impresaIndividuale && this.personaFisica.impresaIndividuale.sedeLegale) ? this.personaFisica.impresaIndividuale.sedeLegale : null);

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    if (this.ateco[0].fonteDato === FonteDatoAnagrafico.CAMERA_COMMERCIO) {
      this.prepareTemplatesCodiciAteco(this.ateco);
    }
  }

  public prepareTemplatesPersonaGiuridicaAnagrificaRappresentanteLegaleAT() {
    this.templates = [];
    // DATI PERSONA GIURIDICA
    let templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'DATI PERSONA GIURIDICA';
    let keyValueArray = new Array<KeyValue>();

    let denominazione = this.personaGiuridica.denominazione || null;
    keyValueArray.push(this.buildKeyValue('Denominazione:', denominazione));

    let formaGiuridica = this.personaGiuridica.formaGiuridica || null;
    keyValueArray.push(this.buildKeyValue('Forma giuridica:', formaGiuridica));

    let codiceFiscale = this.personaGiuridica.codiceFiscale || null;
    keyValueArray.push(this.buildKeyValue('Codice Fiscale:', codiceFiscale));

    let partitaIVA = this.personaGiuridica.partitaIva || null;
    keyValueArray.push(this.buildKeyValue('Partita IVA:', partitaIVA));

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    // RAPPRESENTANTE LEGALE
    templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'RAPPRESENTANTE LEGALE';
    keyValueArray = new Array<KeyValue>();

    let nominativo = this.personaGiuridica.rappresentanteLegale && this.personaGiuridica.rappresentanteLegale.nominativo || null;
    keyValueArray.push(this.buildKeyValue('Nome e Cognome:', nominativo));

    let rappresentanteCF = this.personaGiuridica.rappresentanteLegale && this.personaGiuridica.rappresentanteLegale.codiceFiscale || null;
    keyValueArray.push(this.buildKeyValue('Codice Fiscale:', rappresentanteCF));

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);
  }

  private prepareTemplatesPersonaGiuridicaSedeLegaleAT() {
    // SEDE LEGALE
    let templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'SEDE LEGALE';
    let keyValueArray = new Array<KeyValue>();

    this.prepareKeyValueArrayIndirizzoAT(keyValueArray, this.personaGiuridica.sedeLegale ? this.personaGiuridica.sedeLegale.indirizzo : null);

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    if (this.ateco != null && this.ateco[0].fonteDato === FonteDatoAnagrafico.ANAGRAFE_TRIBUTARIA) {
      this.prepareTemplatesCodiciAteco(this.ateco);
    }
  }

  // Popolamento accordion Dati Persona Giuridica e Iscrizione alla Camera di Commercio della Persona Giuridica (Camera di Commercio)
  public prepareTemplatesPersonaGiuridicaDatiCameraDiCommercio() {
    this.templates = [];
    // DATI PERSONA GIURIDICA
    let templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'DATI PERSONA GIURIDICA';
    let keyValueArray = new Array<KeyValue>();

    let oggettoSociale = this.personaGiuridica.oggettoSociale || null;
    keyValueArray.push(this.buildA4gAccordionItem(this.buildKeyValue('Oggetto sociale:', oggettoSociale), true));

    let dataCostituzione = this.personaGiuridica.dataCostituzione || null;
    let dataCostituzioneFrmt = null;
    if (dataCostituzione) {
      dataCostituzioneFrmt = formatDate(dataCostituzione, 'dd/MM/yyyy', 'it-IT');
    }
    keyValueArray.push(this.buildKeyValue('Data costituzione:', dataCostituzioneFrmt));

    let dataTermine = this.personaGiuridica.dataTermine || null;
    let dataTermineFrmt = null;
    if (dataTermine) {
      dataTermineFrmt = formatDate(dataTermine, 'dd/MM/yyyy', 'it-IT');
    }
    keyValueArray.push(this.buildKeyValue('Data termine:', dataTermineFrmt));

    let capitaleSociale = this.personaGiuridica.capitaleSocialeDeliberato || null;
    let capitaleSocialeStr = null;
    if (capitaleSociale) {
      capitaleSocialeStr = capitaleSociale.toLocaleString() + " euro";
    }
    keyValueArray.push(this.buildKeyValue('Capitale soc. deliberato:', capitaleSocialeStr));

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    // REPERTORIO ECONOMICO AMMINISTRATIVO (REA)
    templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'REPERTORIO ECONOMICO AMMINISTRATIVO (REA)';
    keyValueArray = new Array<KeyValue>();

    this.prepareKeyValueIRE(keyValueArray, this.personaGiuridica.sedeLegale ? this.personaGiuridica.sedeLegale.iscrizioneRegistroImprese : null);

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    // REGISTRO IMPRESA (// FAS-ANA-09)
    if (this.sezioniImpresa.iscrizioniSezione.length > 0) {
      this.prepareKeyValueRegistroImp(this.sezioniImpresa ? this.sezioniImpresa : null);
    }

  }

  // Popolamento accordion Dati Sede Legale della Persona Giuridica (Camera di Commercio)
  public prepareTemplatesPersonaGiuridicaDatiSedeLegaleCameraDiCommercio() {
    // SEDE LEGALE
    let templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'SEDE LEGALE';
    let keyValueArray = new Array<KeyValue>();

    this.prepareKeyValueArrayIndirizzoCameraDiCommercio(keyValueArray, this.personaGiuridica.sedeLegale ? this.personaGiuridica.sedeLegale : null);

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);

    if (this.ateco != null && this.ateco[0].fonteDato === FonteDatoAnagrafico.CAMERA_COMMERCIO) {
      this.prepareTemplatesCodiciAteco(this.ateco);
    }
  }

  private prepareKeyValueArrayIndirizzoCameraDiCommercio(keyValueArray: Array<KeyValue>, sedeLegale: SedeDto) {
    // Concateno i valori dell'indirizzo proviente da Parix
    let indirizzoSedeLegale = this.costruisciIndirizzoSedeLegale(sedeLegale);
    // Creo una variabile che contenga gli stessi valori dell'indirizzo Parix e setto l'indirizzo costruito in toponimo, così da poter
    // risfruttare prepareKeyValueArrayIndirizzoAT per le logiche comuni
    let indirizzoToSend = Object.assign({}, sedeLegale.indirizzoCameraCommercio);
    indirizzoToSend.toponimo = indirizzoSedeLegale;

    this.prepareKeyValueArrayIndirizzoAT(keyValueArray, indirizzoToSend, true);

    let codiceIstat = indirizzoToSend ? indirizzoToSend.codiceIstat : null;
    keyValueArray.push(this.buildKeyValue('Cod. ISTAT comune:', codiceIstat));

    let telefono = sedeLegale ? sedeLegale.telefono : null;
    keyValueArray.push(this.buildKeyValue('Telefono:', telefono));

    let indirizzoPec = sedeLegale && sedeLegale.indirizzoPec || null;
    keyValueArray.push(this.buildKeyValue('PEC:', indirizzoPec));
  }

  private prepareKeyValueArrayIndirizzoAT(keyValueArray: Array<KeyValue>, indirizzoDto: IndirizzoDto, indirizzoSedeLegale?: boolean) {
    let indirizzo = indirizzoDto && indirizzoDto.toponimo || null;
    if (indirizzoSedeLegale) {
      keyValueArray.push(this.buildKeyValue('Indirizzo sede legale:', indirizzo));
    }
    else {
      keyValueArray.push(this.buildKeyValue('Indirizzo:', indirizzo));
    }

    let comune = indirizzoDto ? indirizzoDto.comune : null;
    keyValueArray.push(this.buildKeyValue('Comune:', comune));

    let cap = indirizzoDto ? indirizzoDto.cap : null;
    keyValueArray.push(this.buildKeyValue('CAP:', cap));

    let provincia = indirizzoDto ? indirizzoDto.provincia : null;
    keyValueArray.push(this.buildKeyValue('Provincia:', provincia));
  }

  private prepareKeyValueIRE(keyValueArray: Array<KeyValue>, iscrizioneRegistroImprese: IscrizioneRepertorioEconomicoDto) {
    let dataIscrizione = iscrizioneRegistroImprese && iscrizioneRegistroImprese.dataIscrizione || null;
    let dataIscrizioneFrmt = null;
    if (dataIscrizione) {
      dataIscrizioneFrmt = formatDate(dataIscrizione, 'dd/MM/yyyy', 'it-IT');
    }
    keyValueArray.push(this.buildKeyValue('Data Iscrizione:', dataIscrizioneFrmt));

    let codiceRea = iscrizioneRegistroImprese && iscrizioneRegistroImprese.codiceRea || null;
    keyValueArray.push(this.buildKeyValue('Codice REA:', codiceRea));

    let provinciaRea = iscrizioneRegistroImprese && iscrizioneRegistroImprese.provinciaRea || null;
    keyValueArray.push(this.buildKeyValue('Provincia REA:', provinciaRea));
  }

  private prepareKeyValueRegistroImp(sezioniImpresa: PersonaDto) {
    // FAS-ANA-09 - BR1
    // Per ogni iscrizione, il sistema registra le seguenti informazioni:
    // descrizione, data di iscrizione
    // Per l’iscrizione alla sezione speciale: qualifica; se è un coltivatore diretto (vale solo per i Piccoli imprenditori)
    this.setTabDefault();

    const templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'REGISTRO IMPRESA';
    const keyValueArray = new Array<A4gAccordionField>();
    let keyValue = new A4gAccordionField();
    let mvalue: string;

    sezioniImpresa.iscrizioniSezione.forEach(sezione => {
      keyValue.mkey = this.translateService.instant(sezione.sezione);
      mvalue = formatDate(sezione.dataIscrizione, 'dd/MM/yyyy', 'it-IT');
      if (sezione.qualifica) { mvalue = mvalue + ", " + sezione.qualifica }
      if (sezione.coltivatoreDiretto) { mvalue = mvalue + ", Coltivatore Diretto" }
      keyValue.mvalue = mvalue || null;

      keyValueArray.push(keyValue);
      keyValue = new A4gAccordionField();
    });

    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);
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

  private costruisciIndirizzoSedeLegale(sedeLegale: SedeDto): string {
    let indirizzo = "";
    let frazione = sedeLegale && sedeLegale.indirizzoCameraCommercio && sedeLegale.indirizzoCameraCommercio.frazione || null;
    let toponimo = sedeLegale && sedeLegale.indirizzoCameraCommercio && sedeLegale.indirizzoCameraCommercio.toponimo || null;
    let via = sedeLegale && sedeLegale.indirizzoCameraCommercio && sedeLegale.indirizzoCameraCommercio.via || null;
    let civico = sedeLegale && sedeLegale.indirizzoCameraCommercio && sedeLegale.indirizzoCameraCommercio.civico || null;
    if (frazione) { indirizzo = indirizzo + frazione + ' '; }
    if (toponimo) { indirizzo = indirizzo + toponimo + ' '; }
    if (via) { indirizzo = indirizzo + via + ', '; }
    if (civico) { indirizzo = indirizzo + civico; }
    return indirizzo;
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

  setTabDefault() {
    this.tabPersoneConCarica = false;
    this.tabRegistroImpresa = false;
    this.tabUnitaTecnicoEconomiche = false;
  }

  public handleClick() {
    this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.DATI);
  }

  public update() {
    this.updateError = null;
    const cuaa = this.route.snapshot.paramMap.get('cuaa');
    if (cuaa) {
      this.anagraficaFascicoloService.aggiorna(cuaa).pipe(
        takeUntil(this.componentDestroyed$),
        catchError(error => {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error,
            this.translateService.instant('FAS_ANA.' + error.error.message)
          ));
          return EMPTY;
        }),
        switchMap((res) => {
          const fascicoloDto = res.fascicoloDto;
          const anomalies = res.anomalies;
          this.fascicoloDettaglioService.fascicoloCorrente.next(fascicoloDto);
          if (Array.isArray(anomalies) && anomalies.length) {
            for (const anomaly of anomalies) {
              let severityMessage = A4gSeverityMessage.warn;
              if (anomaly === "CUAA_MISMATCH_ANAGRAFE_TRIBUTARIA_CAA") {
                severityMessage = A4gSeverityMessage.info;
                this.router.navigate([this.router.url.replace(cuaa, fascicoloDto.cuaa)]);
              }
              this.messageService.add(A4gMessages.getToast(
                'tst', severityMessage,
                this.translateService.instant("FAS_ANA.CONTROLLI_COMPLETEZZA_ANOMALIE." + anomaly)));
            }
          }
          return this.anagraficaFascicoloService.getFirmatario(cuaa);
        }),
        catchError(error => {
          A4gMessages.handleError(this.messageService, error, A4gMessages.ERRORE_GENERICO);
          return EMPTY;
        }),
        switchMap(firmatario => {
          if (firmatario) {
            this.fascicoloDettaglioService.firmatario.next(firmatario);
          }
          return of(cuaa);
        }),
        switchMap((cuaa: string) => {
          return this.mediatorService.deleteEsitiControlloCompletezza(cuaa);
        }),
        catchError(error => {
          A4gMessages.handleError(this.messageService, error, A4gMessages.ERRORE_GENERICO);
          return EMPTY;
        })
      ).subscribe(res => {
        this.updated.next();

        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
      });
    }
  }

  public onUpdateError(message: string): void {
    this.updateError = message;
  }
}
