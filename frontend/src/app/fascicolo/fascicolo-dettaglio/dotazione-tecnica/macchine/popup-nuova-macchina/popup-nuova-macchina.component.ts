import { AnagraficaFascicoloService } from 'src/app/fascicolo/creazione-fascicolo/anagrafica-fascicolo.service';
import { TipologiaPossessoEnum } from './../../../../../a4g-common/classi/enums/dotazione.-tecnica/TipologiaPossesso.enum';
import { ClasseFunzionaleDto } from './../../../../../a4g-common/classi/dto/dotazione-tecnica/TipologiaDto';
import { DettaglioMacchinaVM } from './../../../../../a4g-common/classi/viewModels/MacchinarioVM';
import { ErrorService } from './../../../../../a4g-common/services/error.service';
import { FormService } from './../../../../../a4g-common/services/form.service';
import { AccordionTab } from 'primeng/accordion';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { Component, ElementRef, OnInit, ViewChild, OnDestroy, EventEmitter, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import * as FileSaver from 'file-saver';
import { Accordion, Dialog, MessageService, SelectItem } from 'primeng-lts';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { TipologicheService } from 'src/app/a4g-common/services/tipologiche.service';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { FileConfigModel } from 'src/app/uma/core-uma/models/config/file-config.model';
import { FileService } from 'src/app/uma/core-uma/services/file.service';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { DotazioneTecnicaBuilderService } from 'src/app/a4g-common/services/builders/dotazione-tecnica-builder.service';
import * as _ from 'lodash';
import { FascicoloDettaglioService } from 'src/app/fascicolo/fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import { FormFieldMap } from 'src/app/uma/core-uma/models/viewModels/FormFieldMap';
import { HttpClientTipologieService } from 'src/app/a4g-common/services/http-client-tipologie.service';
import { EMPTY, Observable, Subscription } from 'rxjs';
import { HttpClientMacchineService } from 'src/app/a4g-common/services/http-client-macchine.service';
import { SottoTipoDto, TipologiaDto } from 'src/app/a4g-common/classi/dto/dotazione-tecnica/TipologiaDto';
import { DettaglioMacchinaDto } from 'src/app/a4g-common/classi/dto/dotazione-tecnica/DettaglioMacchinaDto';
import { catchError, switchMap } from 'rxjs/operators';
import { MediatorService } from 'src/app/fascicolo/mediator.service';
import { StatoFascicoloEnum } from 'src/app/fascicolo/creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { FascicoloDaCuaa } from 'src/app/a4g-common/classi/FascicoloCuaa';
import { ClassificazioneMacchinarioVM } from 'src/app/a4g-common/classi/viewModels/MacchinarioVM';
import { AmbitoTipologia } from 'src/app/a4g-common/classi/enums/dotazione.-tecnica/AmbitoTipologia.enum';
import { PersonaFisicaDto } from 'src/app/fascicolo/creazione-fascicolo/dto/PersonaDto';
import { Configuration } from 'src/app/app.constants';
import { HttpClient } from '@angular/common/http';
@Component({
  selector: 'app-popup-nuova-macchina',
  templateUrl: './popup-nuova-macchina.component.html',
  styleUrls: ['./popup-nuova-macchina.component.scss']
})
export class PopupNuovaMacchinaComponent implements OnInit, OnDestroy {
  @ViewChild('dialogNuovaMacchina') dialog: Dialog;
  @ViewChild('fileCaricato') file: ElementRef;
  @ViewChild('accordion') accordion: Accordion;

  @Output() onHideDialog = new EventEmitter();

  cuaa: string;
  idMacchina: number;
  fileConfig: FileConfigModel;
  fileCaricato: boolean;
  allegato: { file: File, name: string };
  currentYear: number;
  macchinaForm: FormGroup;
  motoreDisabled: boolean;
  showSwitchMotore: boolean;
  showCF: boolean;
  showRagioneSociale: boolean;
  readOnlyRagioneSociale: boolean = false;
  display: boolean;
  READONLY_MODE: boolean; /** se sono in viusalizzazione è true, se sono in modifica o in creazione è false */
  EDIT_MODE: boolean;     /** se sono in visualizzazione o in modifica è true, in creazione è false */

  dettaglioMacchina: DettaglioMacchinaVM;
  classificazioneMacchinarioVM: ClassificazioneMacchinarioVM;
  idValidazione: string;

  // Subscription
  getTipologieSubscription: Subscription;
  getSottoTipologieSubscription: Subscription;
  getClasseFunzionaleSubscription: Subscription;
  getAllegatoSubscription: Subscription;
  postMacchinaSubscription: Subscription;

  constructor(
    public tipologicheService: TipologicheService,
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private fileService: FileService,
    private messageService: MessageService,
    private dateUtilService: DateUtilService,
    private formService: FormService,
    private errorService: ErrorService,
    private dotazioneTecnicaBuilderService: DotazioneTecnicaBuilderService,
    private httpClientTipologieService: HttpClientTipologieService,
    private fascicoloDettaglioService: FascicoloDettaglioService,
    private httpClientMacchineService: HttpClientMacchineService,
    private httpClientMediatorService: MediatorService,
    private _configuration: Configuration,
    private http: HttpClient
  ) { }

  ngOnInit() {
    this.getTipologieSubscription = this.httpClientTipologieService.getTipologia(AmbitoTipologia.MACCHINE).subscribe((tipologie: Array<TipologiaDto>) => {
      if (tipologie?.length) {
        this.tipologicheService.setTipologiaMacchinario(this.dotazioneTecnicaBuilderService.tipologiaDtoToSelectItemArrayConverter(tipologie));
      } else {
        this.errorService.showErrorWithMessage('Non è possibile caricare le tipologie di macchinario', 'tst-macchinario');
      }
    }, error => this.errorService.showError(error, 'tst-macchinario'));

    this.tipologicheService.setAlimentazione();
    this.tipologicheService.setTipologiaDiPossesso();
    this.fileConfig = this.fileService.loadConfigPdf();
    this.currentYear = this.dateUtilService.getCurrentYear();
    this.initForm();
  }

  ngOnDestroy(): void {
    if (this.getTipologieSubscription) {
      this.getTipologieSubscription.unsubscribe();
    }
    if (this.getSottoTipologieSubscription) {
      this.getSottoTipologieSubscription.unsubscribe();
    }
    if (this.getAllegatoSubscription) {
      this.getAllegatoSubscription.unsubscribe();
    }
    if (this.postMacchinaSubscription) {
      this.postMacchinaSubscription.unsubscribe();
    }
  }

  open(readonly: boolean = false, cuaa: string, dettaglioMacchinaVM?: DettaglioMacchinaVM, idValidazione?: string) {
    this.showCF = true;
    this.display = true;
    this.cuaa = cuaa;
    this.dettaglioMacchina = dettaglioMacchinaVM;
    // caso click su AGGIUNGI -> Nuova Macchina
    if (this.dettaglioMacchina == null) {

      this.showSwitchMotore = false; //si occupa di mostrare il tasto di switch
      this.motoreDisabled = false; //serve a disabilitare solo l'accordion relativo al motore
      this.showCF = false;

      this.READONLY_MODE = false;
      this.EDIT_MODE = false;
      this.allegato = { file: null, name: '' };
      this.fileCaricato = false;

      this.idMacchina = null;
      this.macchinaForm.get('tipologiaMacchinario').enable();
      this.macchinaForm.get('classeFunzionaleMacchinario').enable();
      this.macchinaForm.get('sottoTipologiaMacchinario').enable();
      this.enableForm();
      this.initForm();
    } else { // caso di Visualizzazione (readonly == true) o Modifica (readonly == false) macchinario
      this.openAllAccordion();
      this.disableMotoreForm();
      this.READONLY_MODE = readonly;
      this.EDIT_MODE = true;
      this.idMacchina = dettaglioMacchinaVM.id;
      this.idValidazione = idValidazione ? idValidazione : null;
      this.validaFormMacchinario(dettaglioMacchinaVM.tipologiaMacchinario.label);
      //ToDo: aggiornare label per switcher motore
      this.showSwitchMotore = (dettaglioMacchinaVM.tipologiaMacchinario.label === "IMPIANTI ED ATTREZZATURE DESTINATI AD ESSERE IMPIEGATI NELLE ATTIVITA' AGRICOLE E FORESTALI") || (dettaglioMacchinaVM.tipologiaMacchinario.label === "MACCHINE PER LA PRIMA TRASFORMAZIONE DEI PRODOTTI AGRICOLI") ? true : false; // nel caso di queste categorie lo switch del motore deve essere visibile

      this.initForm(dettaglioMacchinaVM);
      // risetto this.allegato.file per tenerlo nella post in caso di update
      this.getAllegatoSubscription = this.httpClientMacchineService.getAllegato(this.cuaa, this.idMacchina.toString(), this.idValidazione)
        .pipe(
          catchError(error => {
            this.errorService.showError(error, 'tst-macchine');
            return EMPTY;
          }),
          switchMap(allegato => {
            if (allegato != null) {
              const uploadHelper = new UploadHelper(this.fileConfig.fileExt, this.fileConfig.maxSize);
              const file: File = uploadHelper.blobToFile(new Blob([allegato]), `${this.idMacchina.toString()}` + '_documento_possesso.pdf');
              this.allegato = { file: file, name: 'documento_possesso.pdf' };
              this.fileCaricato = true;
            }
            return this.httpClientTipologieService.getSottoTipologieByIdClasseFunzionale(dettaglioMacchinaVM.classeFunzionaleMacchinario.value);
          }))
        .subscribe((sottotipologie: SottoTipoDto) => {
          if (sottotipologie?.tipologie?.length) {
            this.tipologicheService.setSottoTipologiaMacchinario(this.dotazioneTecnicaBuilderService.tipologiaDtoToSelectItemArrayConverter(sottotipologie.tipologie));
          } else {
            this.errorService.showErrorWithMessage(`Non è possibile caricare le sottotipologie di macchinario relative a ${dettaglioMacchinaVM.tipologiaMacchinario.label}`, 'tst-macchinario');
          }
          if (this.EDIT_MODE) {
            // la tipologia e la sottotipologia sono sempre disabilite in modifica - le salvo in un oggetto temporaneao per risettaro in fase di salvataggio
            this.classificazioneMacchinarioVM = {} as ClassificazioneMacchinarioVM;
            this.classificazioneMacchinarioVM.tipologiaMacchinario = this.macchinaForm.get('tipologiaMacchinario').value;
            this.classificazioneMacchinarioVM.sottoTipologiaMacchinario = this.macchinaForm.get('sottoTipologiaMacchinario').value;
            if (this.dettaglioMacchina.flagMigrato == 0) {
              this.macchinaForm.get('tipologiaMacchinario').disable();
              this.macchinaForm.get('sottoTipologiaMacchinario').disable();
            } else {
              this.macchinaForm.get('tipologiaMacchinario').enable();
              this.macchinaForm.get('sottoTipologiaMacchinario').enable();
            }
          }
          if (this.READONLY_MODE == true) {
            this.disableForm();
          } else {
            this.enableForm(dettaglioMacchinaVM);
          }
        }, error => this.errorService.showError(error, 'tst-macchine'));

      this.httpClientTipologieService.getClassiFunzionaliByIdTipologia(dettaglioMacchinaVM.tipologiaMacchinario.value).subscribe((classeFunzionale: ClasseFunzionaleDto) => {
        if (classeFunzionale?.tipologie?.length) {
          this.tipologicheService.setClasseFunzionaleMacchinario(this.dotazioneTecnicaBuilderService.classeFunzionaleDtoToSelectItemArrayConverter(classeFunzionale.tipologie));
          if (this.dettaglioMacchina.flagMigrato == 0) {
            this.macchinaForm.get('classeFunzionaleMacchinario').disable();
          } else {
            this.macchinaForm.get('classeFunzionaleMacchinario').enable();
          }
        }
      });
    }
  }

  onSelectAccordion($event: any) {
    // console.log("aperto accordion:", $event.index);
  }

  onSelectTipologia($event: any) {
    const tipologia: SelectItem = this.tipologicheService.tipologiaMacchinario.filter(tipologia => tipologia.value == $event.value)[0];
    if (tipologia?.label === 'MACCHINE E ATTREZZATURE ESCLUSE DA CARBURANTE AGEVOLATO PER USO AGRICOLO'
      || tipologia?.label === 'MACCHINE AGRICOLE OPERATRICI TRAINATE'
      || tipologia?.label === 'ATTREZZATURE PORTATE O SEMIPORTATE'
      || tipologia?.label === 'RIMORCHI AGRICOLI CON MASSA COMPLESSIVA A PIENO CARICO FINO A 1,5T'
      || tipologia?.label === 'RIMORCHI AGRICOLI CON MASSA COMPLESSIVA A PIENO CARICO SUPERIORE A 1,5T') { /** Macchinari senza motore */
      this.disableMotoreForm();
      this.motoreDisabled = true;
      this.closeAccordionByIndex(-1);
    } else { /** Macchinari con motore */
      this.enableMotoreForm();
      this.motoreDisabled = false;
    }
    //Tipologie che hanno il motore opzionale
    if (tipologia?.label === "IMPIANTI ED ATTREZZATURE DESTINATI AD ESSERE IMPIEGATI NELLE ATTIVITA' AGRICOLE E FORESTALI"
      || tipologia?.label === "MACCHINE PER LA PRIMA TRASFORMAZIONE DEI PRODOTTI AGRICOLI") {
      this.showSwitchMotore = true;
      this.macchinaForm.get('switchMotore').setValue(true);
    } else {
      this.showSwitchMotore = false;
    }
    //CONTROLLO VALIDAZIONI MACCHINARIO
    this.validaFormMacchinario(tipologia.label);
    this.formService.validateForm(this.macchinaForm);
    //RESET DELLA CLASSE FUNZIONALE
    this.macchinaForm.get('classeFunzionaleMacchinario').reset();
    this.tipologicheService.classeFunzionaleMacchinario = [];
    //RESET DELLA SOTTOTIPOLIGIA
    this.macchinaForm.get('sottoTipologiaMacchinario').reset();
    this.tipologicheService.sottoTipologiaMacchinario = [];
    // popolamento sottotipologia nel caso di selezione di una tipologia con value != null (escludo il valore null di default)
    if ($event.value != null) {
      this.getClasseFunzionaleSubscription = this.httpClientTipologieService.getClassiFunzionaliByIdTipologia($event.value).subscribe((classiFunzionali: ClasseFunzionaleDto) => {
        if (classiFunzionali?.tipologie?.length) {
          this.tipologicheService.setClasseFunzionaleMacchinario(this.dotazioneTecnicaBuilderService.tipologiaDtoToSelectItemArrayConverter(classiFunzionali.tipologie));
          if (this.tipologicheService.classeFunzionaleMacchinario.length == 1) {
            this.macchinaForm.get('classeFunzionaleMacchinario').setValue({ label: this.tipologicheService.classeFunzionaleMacchinario[0]['label'], value: this.tipologicheService.classeFunzionaleMacchinario[0]['value'] });
            this.getSottoTipologieSubscription = this.httpClientTipologieService.getSottoTipologieByIdClasseFunzionale(this.tipologicheService.classeFunzionaleMacchinario[0]['value']).subscribe((sottotipologie: SottoTipoDto) => {
              if (sottotipologie?.tipologie?.length) {
                this.tipologicheService.setSottoTipologiaMacchinario(this.dotazioneTecnicaBuilderService.tipologiaDtoToSelectItemArrayConverter(sottotipologie.tipologie));
                if (this.tipologicheService.sottoTipologiaMacchinario.length == 1) {
                  this.macchinaForm.get('sottoTipologiaMacchinario').setValue({ label: this.tipologicheService.sottoTipologiaMacchinario[0]['label'], value: this.tipologicheService.sottoTipologiaMacchinario[0]['value'] });
                }
              } else {
                this.errorService.showErrorWithMessage(`Non è possibile caricare le sottotipologie di macchinario relative a ${$event.label}`, 'tst-macchinario');
              }
            }, error => this.errorService.showError(error, 'tst-macchinario'));
            //this.macchinaForm.get('classeFunzionaleMacchinario').setValue(this.tipologicheService.classeFunzionaleMacchinario[this.tipologicheService.classeFunzionaleMacchinario.length-1]['label']);
          }
        } else {
          this.errorService.showErrorWithMessage(`Non è possibile caricare le classi funzionali di macchinario relative a ${$event.label}`, 'tst-macchinario');
        }
      }, error => this.errorService.showError(error, 'tst-macchinario'));
    }

  }

  onSelectClasseFunzionale($event: any) {
    const classeFunzionale: SelectItem = this.tipologicheService.classeFunzionaleMacchinario.filter(classeFunzionale => classeFunzionale.value == $event.value)[0];
    // reset della sottotipologia

    this.macchinaForm.get('sottoTipologiaMacchinario').reset();
    this.tipologicheService.sottoTipologiaMacchinario = [];
    // popolamento sottotipologia nel caso di selezione di una tipologia con value != null (escludo il valore null di default)
    if ($event.value != null) {
      this.getSottoTipologieSubscription = this.httpClientTipologieService.getSottoTipologieByIdClasseFunzionale($event.value).subscribe((sottotipologie: SottoTipoDto) => {
        if (sottotipologie?.tipologie?.length) {
          this.tipologicheService.setSottoTipologiaMacchinario(this.dotazioneTecnicaBuilderService.tipologiaDtoToSelectItemArrayConverter(sottotipologie.tipologie));
          if (this.tipologicheService.sottoTipologiaMacchinario.length == 1) {
            this.macchinaForm.get('sottoTipologiaMacchinario').setValue(this.tipologicheService.sottoTipologiaMacchinario[0].value);
          }
        } else {
          this.errorService.showErrorWithMessage(`Non è possibile caricare le sottotipologie di macchinario relative a ${$event.label}`, 'tst-macchinario');
        }
      }, error => this.errorService.showError(error, 'tst-macchinario'));
    }

  }

  onSelectSottoTipologia($event: any) {
  }

  onSelectAlimentazione($event: any) {
    if (this.macchinaForm.get('alimentazione').value == "BENZINA") {
      this.macchinaForm.get('potenza').setValidators([Validators.required]);
    }
    
    this.formService.validateForm(this.macchinaForm);
  }

  onChangeNumeroTelaio($event: any) {
    if (_.isEmpty(this.macchinaForm.get('numeroTelaio').value)) {
      this.macchinaForm.get('numeroMotore').setValidators([Validators.required]);
    }
    else {
      this.macchinaForm.get('numeroMotore').setValidators([]);
    }
    this.formService.validateForm(this.macchinaForm);
  }

  onChangeNumeroMotore($event: any) {
    if (_.isEmpty(this.macchinaForm.get('numeroMotore').value)) {
      this.macchinaForm.get('numeroTelaio').setValidators([Validators.required]);
    }
    else {
      this.macchinaForm.get('numeroTelaio').setValidators([]);
    }
    this.formService.validateForm(this.macchinaForm);
  }

  onSelectTipologiaPossesso($event: any) {
    this.showCF = this.isTPRequired($event.value);
  }

  isTPRequired(tipologiaPossesso: string){
    let required = false;
    this.macchinaForm.get('ragioneSociale').clearValidators();
    this.macchinaForm.get('codiceFiscale').clearValidators();
    if(tipologiaPossesso != TipologiaPossessoEnum.PROPRIETA && tipologiaPossesso != TipologiaPossessoEnum.COMPROPRIETA && tipologiaPossesso != null){
      required = true;
      this.macchinaForm.get('ragioneSociale').setValidators([Validators.required]);
      this.macchinaForm.get('codiceFiscale').setValidators([Validators.required]);
    }
    this.macchinaForm.get('ragioneSociale').setValue('');
    this.macchinaForm.get('codiceFiscale').setValue('');
    this.formService.validateForm(this.macchinaForm);
    return required;
  }

  timer = 0;

  onKeyupCF($event: string) {
    window.clearTimeout(this.timer);

    this.timer = window.setTimeout(() => {
      if ($event.length == 11 || $event.length == 16) {
        this.onChangeCF($event);
      }
    }, 500);
  }
  onChangeCF($event: string) {
    this.showRagioneSociale = true;
    console.log(this.readOnlyRagioneSociale);
    this.readOnlyRagioneSociale = false;
    document.getElementById("ragioneSociale-input")?.removeAttribute('readonly');
    if (this.macchinaForm.get('codiceFiscale').value.length == 11 || this.macchinaForm.get('codiceFiscale').value.length == 16) {
      this.getPersona(this.macchinaForm.get('codiceFiscale').value).subscribe(
        (obj) => {
          if (obj.risposta != null) {
            this.readOnlyRagioneSociale = true;
            document.getElementById("ragioneSociale-input").setAttribute('readonly', 'readonly');
            if (obj.risposta.persona != null) {
              this.macchinaForm.get('ragioneSociale').setValue(obj.risposta.persona.dittaIndividuale.denominazione);
              //this.macchinaForm.get('ragioneSociale').disable({ onlySelf: true });
            } else {
              this.macchinaForm.get('ragioneSociale').setValue(obj.risposta.soggetto.denominazione.denominazione);
              //this.macchinaForm.get('ragioneSociale').disable({ onlySelf: true });
            }
          }
          else {
            this.errorService.showErrorWithMessage('Codice Fiscale non presente in anagrafe tributaria');
            this.macchinaForm.get('ragioneSociale').setValue('');
          }
        }), error => this.errorService.showError(error, 'tst-macchine');
    } else {
      this.macchinaForm.get('ragioneSociale').enable();
      this.macchinaForm.get('ragioneSociale').setValue('');
    }
  }

  public getPersona(coficeFiscale: string): Observable<any> {
    const url = this._configuration.UrlGetAnagrafeTributaria.replace(
      "${codiceFiscale}",
      String(coficeFiscale)
    );
    return this.http.get(url);
  }
  onSubmit() {
    this.salvaMacchina();
  }

  closeDialog(refresh: boolean) {
    this.closeAllAccordion();
    this.READONLY_MODE = false;
    this.allegato = { file: null, name: '' };
    this.fileCaricato = false;
    this.motoreDisabled = true;

    this.onHideDialog.emit(refresh);
    this.display = false;
  }

  annulla() {
    this.closeDialog(false);
  }

  //SALVATAGGIO MACCHINARIO
  salvaMacchina() {
    const tipologia: SelectItem = this.tipologicheService.tipologiaMacchinario.filter(tipologia => tipologia.value == this.macchinaForm.get('tipologiaMacchinario').value)[0]

    //CONTROLLO VALIDAZIONI MACCHINARIO
    this.validaFormMacchinario(tipologia.label);
    this.isTPRequired(this.macchinaForm.get('tipologiaPossesso').value);
     
    this.formService.validateForm(this.macchinaForm);
    const invalidFields = this.formService.getInvalids(this.macchinaForm);
    console.log('INVALID FIELDS....', invalidFields);
    // apertura accordion in corrispondenza dela campo errato
    if (!this.macchinaForm.valid) {
      this.closeAllAccordion();
      this.openPanelsByField(invalidFields);
      this.errorService.showErrorWithMessage(A4gMessages.MACCHINARI.datiNonValidi, 'tst-macchinario');
      return;
    }
    // controllo su file caricato
    /*if (!this.fileCaricato) {
      this.closeAllAccordion();
      this.openAccordionByIndex(2);
      this.errorService.showErrorWithMessage(A4gMessages.MACCHINARI.documentoNonPresente, 'tst-macchinario');
      return;
    }*/
    const dettaglioMacchinaVm: DettaglioMacchinaVM = this.macchinaForm.value;
    dettaglioMacchinaVm.flagMigrato = this.dettaglioMacchina != null ? this.dettaglioMacchina.flagMigrato : 1;
    if (this.EDIT_MODE && dettaglioMacchinaVm.flagMigrato == 0) { // Nel caso sono in update e il macchinario non è stato migrato risetto nel dto la tipologia/classeFunzionale/sottotipologia (perchè disabilitate)
      dettaglioMacchinaVm.tipologiaMacchinario = this.classificazioneMacchinarioVM.tipologiaMacchinario as any;
      dettaglioMacchinaVm.classeFunzionaleMacchinario = this.classificazioneMacchinarioVM.classeFunzionaleMacchinario as any;
      dettaglioMacchinaVm.sottoTipologiaMacchinario = this.classificazioneMacchinarioVM.sottoTipologiaMacchinario as any;
    }
    const dettaglioMacchinaDto: DettaglioMacchinaDto = this.dotazioneTecnicaBuilderService.toDettaglioMacchinaDto(dettaglioMacchinaVm, this.idMacchina); // se idMacchina == null, sono in creazione altrimenti sono in update
    // TODO chiamata al mediator
    this.postMacchinaSubscription = this.httpClientMediatorService.postMacchina(this.cuaa, dettaglioMacchinaDto, this.allegato).subscribe((res: number) => {
      const p: FascicoloDaCuaa = this.fascicoloDettaglioService.fascicoloCorrente.value;
      if (p.stato != StatoFascicoloEnum.IN_AGGIORNAMENTO) {
        p.stato = StatoFascicoloEnum.IN_AGGIORNAMENTO;
        this.fascicoloDettaglioService.fascicoloCorrente.next(p);
      }
      this.messageService.add(A4gMessages.getToast('tst-macchine', A4gSeverityMessage.success, A4gMessages.MACCHINARI.salvataggioOK));
      this.macchinaForm.reset();
      this.closeDialog(true);
    }, error => this.errorService.showError(error, 'tst-macchine'));
  }

  caricaAllegato() {
    document.getElementById('fileCaricato').click();
  }

  onFileChange(event: Event) {
    this.fileCaricato = false;
    if ((event.target as HTMLInputElement).files && (event.target as HTMLInputElement).files.length) {
      const file: File = (event.target as HTMLInputElement).files[0];
      const uploadHelper = new UploadHelper(this.fileConfig.fileExt, this.fileConfig.maxSize);
      if (uploadHelper.isValidFileExtension(file, UMA_MESSAGES.FILE_ERRORS.EXT_PDF) && uploadHelper.isValidFileSize(file, UMA_MESSAGES.FILE_ERRORS.SIZE)) {
        this.fileCaricato = true;
        this.allegato.file = file;
        this.allegato.name = file.name;
      }
      this.showErrors(uploadHelper.getErrors());
    }
  }

  eliminaAllegato($event?: Event) {
    this.fileCaricato = false;
    this.allegato.file = null;
    this.allegato.name = null;
  }

  visualizzaAllegato() {
    // caso di allegato appena caricato
    if (this.fileCaricato && this.allegato && this.allegato.file != null) {
      const fileURL = URL.createObjectURL(this.allegato.file);
      FileSaver.saveAs(this.allegato.file, this.allegato.name);
    } else { // se è vuoto allegato.file (il byte) nel caso di modifica
      this.getAllegatoSubscription = this.httpClientMacchineService.getAllegato(this.cuaa, this.idMacchina.toString(), this.idValidazione).subscribe((byte: string | any) => {
        const fileURL = URL.createObjectURL(byte);
        window.open(fileURL);
      }, error => this.errorService.showError(error, 'tst-macchinario'));
    }
  }

  enableMotore($event: { checked: boolean, originalEvent: Event }) {
    if ($event && $event.checked) {
      this.motoreDisabled = false;
      this.enableMotoreForm();
    } else {
      this.motoreDisabled = true;
      this.disableMotoreForm();
      this.resetMotore();
    }
  }

  validaFormMacchinario(tipologia: String) {
    this.macchinaForm.get('targa').setValidators([]);
    this.macchinaForm.get('alimentazione').setValidators([]);
    this.macchinaForm.get('numeroTelaio').setValidators([]);
    this.macchinaForm.get('numeroMotore').setValidators([]);
    this.macchinaForm.get('potenza').setValidators([]);
    this.macchinaForm.get('tipologiaPossesso').setValidators([]);
    if (tipologia === 'MACCHINE E ATTREZZATURE ESCLUSE DA CARBURANTE AGEVOLATO PER USO AGRICOLO') {
    } else if (tipologia === 'TRATTRICI AGRICOLE' || tipologia === 'MACCHINE AGRICOLE OPERATRICI SEMOVENTI A DUE O PIU ASSI') {
      this.motoreDisabled = false; //Mostro sezione motore
      this.macchinaForm.get('tipologiaPossesso').setValidators([Validators.required]); //verifico tipologia possesso
      this.macchinaForm.get('targa').setValidators([Validators.required]);// verifico targa
      this.macchinaForm.get('alimentazione').setValidators([Validators.required]);//verifico alimentazione
      if (this.macchinaForm.get('alimentazione').value == "BENZINA") {
        this.macchinaForm.get('potenza').setValidators([Validators.required]);
      }//Verifico la potenza se alimentazione=benzina
    } else if (tipologia === 'MACCHINE AGRICOLE OPERATRICI SEMOVENTI AD UN ASSE') {
      this.motoreDisabled = false; //Mostro sezione motore
      this.macchinaForm.get('tipologiaPossesso').setValidators([Validators.required]); //verifico tipologia possesso
      this.macchinaForm.get('alimentazione').setValidators([Validators.required]);//verifico alimentazione
      this.macchinaForm.get('numeroTelaio').setValidators([Validators.required]);//verifico numero telaio
      if (this.macchinaForm.get('alimentazione').value == "BENZINA") {
        this.macchinaForm.get('potenza').setValidators([Validators.required]);
      }//Verifico la potenza se alimentazione=benzina
    } else if (tipologia === 'MACCHINE AGRICOLE OPERATRICI TRAINATE' || tipologia === 'ATTREZZATURE PORTATE O SEMIPORTATE' || tipologia == 'RIMORCHI AGRICOLI CON MASSA COMPLESSIVA A PIENO CARICO FINO A 1,5T') {
      this.macchinaForm.get('tipologiaPossesso').setValidators([Validators.required]); //verifico tipologia possesso
      this.macchinaForm.get('numeroTelaio').setValidators([Validators.required]); //verifico numero telaio
      if (this.macchinaForm.get('alimentazione').value == "BENZINA") {
        this.macchinaForm.get('potenza').setValidators([Validators.required]);
      }//Verifico la potenza se alimentazione=benzina
    } else if (tipologia === 'RIMORCHI AGRICOLI CON MASSA COMPLESSIVA A PIENO CARICO SUPERIORE A 1,5T') {
      this.macchinaForm.get('tipologiaPossesso').setValidators([Validators.required]); //verifico tipologia possesso
      this.macchinaForm.get('targa').setValidators([Validators.required]); // verifico targa
      if (this.macchinaForm.get('alimentazione').value == "BENZINA") {
        this.macchinaForm.get('potenza').setValidators([Validators.required]);
      }//Verifico la potenza se alimentazione=benzina
    } else if (tipologia === 'MACCHINE OPERATRICI ADIBITE E ATTREZZATE PERMANENTEMENTE PER LAVORI AGRICOLI') {
      this.motoreDisabled = false; //Mostro sezione motore
      this.macchinaForm.get('tipologiaPossesso').setValidators([Validators.required]); //verifico tipologia possesso
      this.macchinaForm.get('targa').setValidators([Validators.required]);// verifico targa
      this.macchinaForm.get('alimentazione').setValidators([Validators.required]);//verifico alimentazione
      if (this.macchinaForm.get('alimentazione').value == "BENZINA") {
        this.macchinaForm.get('potenza').setValidators([Validators.required]);
      }//Verifico la potenza se alimentazione=benzina
    } else if (tipologia === "IMPIANTI ED ATTREZZATURE DESTINATI AD ESSERE IMPIEGATI NELLE ATTIVITA' AGRICOLE E FORESTALI"
      || tipologia === "MACCHINE PER LA PRIMA TRASFORMAZIONE DEI PRODOTTI AGRICOLI"
      || tipologia === "IMPIANTI DI RISCALDAMENTO DELLE SERRE E DEI LOCALI ADIBITI AD ATTIVITA' DI PRODUZIONE") {
      this.motoreDisabled = false; //Mostro sezione motore
      this.macchinaForm.get('tipologiaPossesso').setValidators([Validators.required]); //verifico tipologia possesso
      if (!this.motoreDisabled) { //se il motore é disabilitato non eseguire il controllo sull'alimentazione
        this.macchinaForm.get('alimentazione').setValidators([Validators.required]);//verifico alimentazione
      }//Condizione alternata per numeroTelaio e numeroMotore
      if (_.isEmpty(this.macchinaForm.get('numeroTelaio').value) && _.isEmpty(this.macchinaForm.get('numeroMotore').value)) {
        this.macchinaForm.get('numeroTelaio').setValidators([Validators.required]);
        this.macchinaForm.get('numeroMotore').setValidators([Validators.required]);
      }
      if (this.macchinaForm.get('alimentazione').value == "BENZINA") {
        this.macchinaForm.get('potenza').setValidators([Validators.required]);
      }
    }
  }

  private openPanelsByField(fields: Array<FormFieldMap>) {
    fields.forEach((field: FormFieldMap) => {
      // Sezione Mezzo Agricolo
      const sezioneMezzoAgricoloFields = ['tipologiaMacchinario', 'sottoTipologiaMacchinario', 'marcaMacchinario', 'modello', 'targa', 'numeroMatricola',
        'numeroTelaio', 'annoCostruzione'];
      const sezioneMezzoAgricloInvalid = !!fields.filter((field: FormFieldMap) => sezioneMezzoAgricoloFields.indexOf(field.name) > -1).length;
      if (sezioneMezzoAgricloInvalid) {
        this.openAccordionByIndex(0);
        return;
      }
      // Sezione Motore
      const sezioneMotoreFields = ['marcaMotore', 'tipo', 'alimentazione', 'potenza', 'numeroMotore'];
      const sezioneMotoreFieldsInvalid = !!fields.filter((field: FormFieldMap) => sezioneMotoreFields.indexOf(field.name) > -1).length;
      if (sezioneMotoreFieldsInvalid) {
        this.openAccordionByIndex(1);
        return;
      }
      // Sezione Possesso
      const sezionePossessoFields = ['tipologiaPossesso'];
      const sezionePossessoFieldsInvalid = !!fields.filter((field: FormFieldMap) => sezionePossessoFields.indexOf(field.name) > -1).length;
      if (sezionePossessoFieldsInvalid) {
        this.openAccordionByIndex(2);
        return;
      }
    });
  }

  private showErrors(errors: Array<string>) {
    if (!errors || !errors.length) {
      return;
    }
    errors.forEach((errMessage: string) => {
      this.messageService.add(A4gMessages.getToast('tst-macchinario', A4gSeverityMessage.error, errMessage));
    });
  }

  private initForm(dettaglioMacchinaVM: DettaglioMacchinaVM = null/*, targaEnabled: boolean = true*/) {
    if (dettaglioMacchinaVM == null)
      this.tipologicheService.resetClassiFunzionaliSottoTipologieMacchinario();
    this.macchinaForm = new FormGroup({
      // sezione mezzo agricolo
      tipologiaMacchinario: new FormControl({ value: dettaglioMacchinaVM?.tipologiaMacchinario ? dettaglioMacchinaVM.tipologiaMacchinario.value : null, disabled: this.READONLY_MODE }, [Validators.required]),
      sottoTipologiaMacchinario: new FormControl({ value: dettaglioMacchinaVM?.sottoTipologiaMacchinario ? dettaglioMacchinaVM.sottoTipologiaMacchinario.value : null, disabled: this.READONLY_MODE }, [Validators.required]),
      classeFunzionaleMacchinario: new FormControl({ value: dettaglioMacchinaVM?.classeFunzionaleMacchinario ? dettaglioMacchinaVM.classeFunzionaleMacchinario.value : null, disabled: this.READONLY_MODE }, [Validators.required]),
      marcaMacchinario: new FormControl({ value: dettaglioMacchinaVM ? dettaglioMacchinaVM.marcaMacchinario : null, disabled: this.READONLY_MODE }),
      targa: new FormControl({ value: dettaglioMacchinaVM ? dettaglioMacchinaVM.targa : null, disabled: this.READONLY_MODE }),
      modello: new FormControl({ value: dettaglioMacchinaVM ? dettaglioMacchinaVM.modello : null, disabled: this.READONLY_MODE }),
      numeroMatricola: new FormControl({ value: dettaglioMacchinaVM ? dettaglioMacchinaVM.numeroMatricola : null, disabled: this.READONLY_MODE }),
      numeroTelaio: new FormControl({ value: dettaglioMacchinaVM ? dettaglioMacchinaVM.numeroTelaio : null, disabled: this.READONLY_MODE }),
      annoCostruzione: new FormControl({ value: dettaglioMacchinaVM ? dettaglioMacchinaVM.annoCostruzione : null, disabled: this.READONLY_MODE }),
      switchMotore: new FormControl({ value: dettaglioMacchinaVM ? dettaglioMacchinaVM.switchMotore : null, disabled: this.READONLY_MODE }),
      // sezione Motore
      marcaMotore: new FormControl({ value: dettaglioMacchinaVM && dettaglioMacchinaVM.marcaMotore ? dettaglioMacchinaVM.marcaMotore : null, disabled: this.READONLY_MODE }),
      tipo: new FormControl({ value: dettaglioMacchinaVM && dettaglioMacchinaVM.tipo ? dettaglioMacchinaVM.tipo : null, disabled: this.READONLY_MODE }),
      alimentazione: new FormControl({ value: dettaglioMacchinaVM?.alimentazione ? dettaglioMacchinaVM.alimentazione.value : null, disabled: this.READONLY_MODE }),
      potenza: new FormControl({ value: dettaglioMacchinaVM?.potenza ? dettaglioMacchinaVM.potenza : null, disabled: this.READONLY_MODE }),
      numeroMotore: new FormControl({ value: dettaglioMacchinaVM ? dettaglioMacchinaVM.numeroMotore : null, disabled: this.READONLY_MODE }),
      // sezione Possesso
      tipologiaPossesso: new FormControl({ value: dettaglioMacchinaVM?.tipologiaPossesso ? dettaglioMacchinaVM.tipologiaPossesso.value : null, disabled: this.READONLY_MODE }),
      codiceFiscale: new FormControl({ value: dettaglioMacchinaVM && dettaglioMacchinaVM.codiceFiscale ? dettaglioMacchinaVM.codiceFiscale : null, disabled: this.READONLY_MODE }),
      ragioneSociale: new FormControl({ value: dettaglioMacchinaVM && dettaglioMacchinaVM.ragioneSociale ? dettaglioMacchinaVM.ragioneSociale : null, disabled: this.READONLY_MODE }),
    });
  }

  private disableMotoreForm() {
    this.macchinaForm.get('marcaMotore').disable();
    this.macchinaForm.get('tipo').disable();
    this.macchinaForm.get('alimentazione').disable();
    this.macchinaForm.get('potenza').disable();
    this.macchinaForm.get('numeroMotore').disable();
    this.disableValidatorsMotoreForm();
  }

  private disableValidatorsMotoreForm() {
    this.macchinaForm.get('marcaMotore').setValidators([]);
    this.macchinaForm.get('tipo').setValidators([]);
    this.macchinaForm.get('alimentazione').setValidators([]);
    this.macchinaForm.get('potenza').setValidators([]);
    this.macchinaForm.get('numeroMotore').setValidators([]);
  }

  private resetMotore() {
    this.macchinaForm.get('marcaMotore').reset();
    this.macchinaForm.get('tipo').reset();
    this.macchinaForm.get('alimentazione').reset();
    this.macchinaForm.get('potenza').reset();
    this.macchinaForm.get('numeroMotore').reset();
  }

  private enableMotoreForm() {
    this.macchinaForm.get('marcaMotore').enable();
    this.macchinaForm.get('tipo').enable();
    this.macchinaForm.get('alimentazione').enable();
    this.macchinaForm.get('potenza').enable();
    this.macchinaForm.get('numeroMotore').enable();
    this.enableValidatorsMotoreForm();
  }

  private enableValidatorsMotoreForm() {
    this.macchinaForm.get('alimentazione').setValidators([Validators.required]);
    //this.macchinaForm.get('potenza').setValidators([Validators.required, Validators.pattern('^([0-9]+\\.?[0-9]*)$')]);
  }

  private enableForm(dettaglioMacchinaVM?: DettaglioMacchinaVM) {
    // Mezzo agricolo
    this.macchinaForm.get('marcaMacchinario').enable();
    this.macchinaForm.get('modello').enable();
    this.macchinaForm.get('numeroMatricola').enable();
    this.macchinaForm.get('numeroTelaio').enable();
    this.macchinaForm.get('annoCostruzione').enable();
    // Motore
    if (dettaglioMacchinaVM?.alimentazione) {
      this.enableMotoreForm();
    }
    // Possesso
    this.macchinaForm.get('tipologiaPossesso').enable();
  }

  private disableForm() {
    // Mezzo agricolo
    this.macchinaForm.get('marcaMacchinario').disable();
    this.macchinaForm.get('modello').disable();
    this.macchinaForm.get('numeroMatricola').disable();
    this.macchinaForm.get('numeroTelaio').disable();
    this.macchinaForm.get('annoCostruzione').disable();
    // Motore
    this.disableMotoreForm();
    // Possesso
    this.macchinaForm.get('tipologiaPossesso').disable();
  }

  private closeAllAccordion() {
    this.accordion.tabs.forEach((tab, index) => {
      tab.selected = false;
    });
  }

  private openAllAccordion() {
    this.accordion.tabs.forEach((tab: AccordionTab, index: number) => {
      if (!tab.disabled) {
        tab.selected = true;
      }
    });
  }

  private openAccordionByIndex(indexToOpen: number) {
    this.accordion.tabs.forEach((tab: AccordionTab, index: number) => {
      if (!tab.disabled && index == indexToOpen) {
        tab.selected = true;
      }
    });
  }

  private closeAccordionByIndex(indexToClose: number) {
    this.accordion.tabs.forEach((tab: AccordionTab, index: number) => {
      if (index == indexToClose) {
        tab.selected = false;
      }
    });
  }
}
