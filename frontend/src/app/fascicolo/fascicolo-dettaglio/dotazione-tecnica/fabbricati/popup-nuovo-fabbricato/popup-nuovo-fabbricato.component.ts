import { DatiCatastaliDto } from './../../../../../a4g-common/classi/dto/dotazione-tecnica/DettaglioFabbricatoDto';
import { FormatConverterService } from 'src/app/uma/shared-uma/services/format-converter.service';
import { AmbitoTipologia, SottoTipologiaZootecnia, TipologiaFabbricato } from './../../../../../a4g-common/classi/enums/dotazione.-tecnica/AmbitoTipologia.enum';
import { Component, EventEmitter, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { ClassificazioneFabbricatoVM, DatiCatastaliVM, DettaglioFabbricatoVM, InfoCatastoVm, ParticellaVm } from 'src/app/a4g-common/classi/viewModels/FabbricatoVM';
import { DotazioneTecnicaBuilderService } from 'src/app/a4g-common/services/builders/dotazione-tecnica-builder.service';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { FormService } from 'src/app/a4g-common/services/form.service';
import { HttpClientTipologieService } from 'src/app/a4g-common/services/http-client-tipologie.service';
import { TipologicheService } from 'src/app/a4g-common/services/tipologiche.service';
import { Accordion, MessageService, SelectItem } from 'primeng-lts';
import { DotazioneTecnicaService } from '../../dotazione-tecnica.service';
import { FascicoloDettaglioService } from 'src/app/fascicolo/fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import * as _ from 'lodash';
import { SottoTipoDto, TipologiaDto } from 'src/app/a4g-common/classi/dto/dotazione-tecnica/TipologiaDto';
import { DettaglioFabbricatoDto } from 'src/app/a4g-common/classi/dto/dotazione-tecnica/DettaglioFabbricatoDto';
import { FabbricatoTypeEnum } from 'src/app/a4g-common/classi/enums/dotazione.-tecnica/FabbricatoTipologia.enum';
import { FormFieldMap } from 'src/app/uma/core-uma/models/viewModels/FormFieldMap';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { StatoFascicoloEnum } from 'src/app/fascicolo/creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { FascicoloDaCuaa } from 'src/app/a4g-common/classi/FascicoloCuaa';
@Component({
  selector: 'app-popup-nuovo-fabbricato',
  templateUrl: './popup-nuovo-fabbricato.component.html',
  styleUrls: ['./popup-nuovo-fabbricato.component.scss']
})
export class PopupNuovoFabbricatoComponent implements OnInit, OnDestroy {
  @Output() onHideDialog = new EventEmitter();
  @ViewChild('accordion') accordion: Accordion;

  display: boolean;
  READONLY_MODE: boolean; /** se sono in viusalizzazione è true, se sono in modifica o in creazione è false */
  EDIT_MODE: boolean;     /** se sono in visualizzazione o in modifica è true, in creazione è false */
  fabbricatoForm: FormGroup;
  particelleList: Array<ParticellaVm>;
  cols: Array<any>;
  particellaModificaInCorso: DatiCatastaliVM; /** particella da modificare, dopo aver scelto la modifica dalla tabella */

  cuaa: string;
  dettaglioFabbricato: DettaglioFabbricatoVM;
  classificazioneFabbricatoVM: ClassificazioneFabbricatoVM;
  idValidazione: string;

  fabbricatoEnabled: boolean; // index == 0
  serreEnabled: boolean;      // index == 1
  struttureEnabled: boolean;  // index == 2
  tipologiaFabbricato = TipologiaFabbricato;
  sottoTipologiaZootecnia = SottoTipologiaZootecnia;
  sottoTipoLogiaSelected: keyof typeof SottoTipologiaZootecnia;
  activeTab: number;

  // Subscription
  getTipologieSubscription: Subscription;
  getSottoTipologieSubscription: Subscription;
  getSottoTipoSubscription: Subscription;
  postFabbricatoSubscription: Subscription;

  infoCatasto: InfoCatastoVm = {} as InfoCatastoVm;
  subalternoList: SelectItem[];

  constructor(
    public tipologicheService: TipologicheService,
    private httpClientTipologieService: HttpClientTipologieService,
    private dotazioneTecnicaBuilderService: DotazioneTecnicaBuilderService,
    private dotazioneTecnicaService: DotazioneTecnicaService,
    private formService: FormService,
    private errorService: ErrorService,
    private messageService: MessageService,
    private fascicoloDettaglioService: FascicoloDettaglioService,
    private formatConverterService: FormatConverterService
  ) { }

  ngOnDestroy(): void {
    if (this.getTipologieSubscription) {
      this.getTipologieSubscription.unsubscribe();
    }
    if (this.getSottoTipologieSubscription) {
      this.getSottoTipologieSubscription.unsubscribe();
    }
    if (this.getSottoTipoSubscription) {
      this.getSottoTipoSubscription.unsubscribe();
    }
    if (this.postFabbricatoSubscription) {
      this.postFabbricatoSubscription.unsubscribe();
    }
  }

  ngOnInit() {
    this.initForm();
    this.initData();
    this.getTipologia();
  }

  open(readonly: boolean, cuaa: string, dettaglioFabbricatoVM?: DettaglioFabbricatoVM, datiCatastali?: Array<DatiCatastaliDto>, idValidazione?: string) {
    this.display = true;
    this.cuaa = cuaa;
    this.dettaglioFabbricato = dettaglioFabbricatoVM;
    this.particelleList = [];
    this.particellaModificaInCorso = null; // Pulsante Aggiungi/Aggiorna Particella -> if != null Aggiorna, else Aggiungi
    // click su AGGIUNGI -> Creazione
    if (this.dettaglioFabbricato == null) {
      this.READONLY_MODE = false;
      this.EDIT_MODE = false;
      this.fabbricatoForm.get('tipologiaFabbricato').enable();
      this.fabbricatoForm.get('sottoTipologiaFabbricato').enable();
      this.fabbricatoForm.get('sottoTipo').enable();
      this.initData();
      this.initForm();
    } else { // caso di Visualizzazione (readonly == true) o Modifica (readonly == false) fabbricato
      this.READONLY_MODE = readonly;
      this.EDIT_MODE = true;
      this.initData();
      //TODO: Verificare il builder per particelleList
      this.particelleList = this.dotazioneTecnicaBuilderService.toParticelleVm(datiCatastali);
      if (dettaglioFabbricatoVM.type === FabbricatoTypeEnum.STRUMENTALE) {
        this.setupFabbricatoStrumentale();
        this.getSottoTipologieFabbricatiOSerre(this.dettaglioFabbricato.tipologiaFabbricato);
      } else if (dettaglioFabbricatoVM.type === FabbricatoTypeEnum.SERRA) {
        this.setupSerre();
        this.getSottoTipologieFabbricatiOSerre(this.dettaglioFabbricato.tipologiaFabbricato);
      } else { // STOCCAGGIO || FABBRICATO
        const tipologia: SelectItem = this.tipologicheService.tipologiaFabbricato.filter((t: SelectItem) => t.label == TipologiaFabbricato.STRUTTURE_ZOOTECNICHE.split('_').join(' '))[0];
        this.dettaglioFabbricato.tipologiaFabbricato = tipologia;
        this.checkOpenZootecniaTab(dettaglioFabbricatoVM);
        this.getSottoTipologieZootecnia();
        this.getSottoTipoByValueSottoTipologia(this.dettaglioFabbricato.sottoTipologiaFabbricato);
      }
      this.initForm(this.dettaglioFabbricato);
    }
  }

  public onSelectTrentinoInOut($event: boolean) {
    console.log('Selezionato: ', $event);
    this.fabbricatoForm.get('datiCatastali').reset();
    
    this.onSelectTipologiaCatasto();
  }

  public onSelectTipologiaCatasto($event?: SelectItem) {
    if (!this.fabbricatoForm.get('fuoriTrentino').value) {
      this.setCatastoInTrentino();
      this.getElencoSubalterniParticellaCatasto();
    } else {
      this.setCatastoFuoriTrentino();
    }
  }

  public getElencoSubalterniParticellaCatasto() {
    const particella = this.fabbricatoForm.get('datiCatastali').value.particella;
    const comuneCatastale = this.fabbricatoForm.get('datiCatastali').value.comune;
    const tipologia = this.fabbricatoForm.get('datiCatastali.tipologia').value;
    if (particella && comuneCatastale && tipologia === 'EDIFICIALE') {
      this.subalternoList = [];
      this.dotazioneTecnicaService.getElencoSubalterniParticellaCatasto(particella, comuneCatastale)
        .subscribe(resp => {
          if (resp) {
            resp.forEach(sub => {
              this.subalternoList.push({ label: sub, value: sub });
            });
          }
        });
    }
  }

  public onSelectTipologia($event: SelectItem) {
    // reset accordion
    this.closeAccordion();
    // reset della sottoTipologia e del sottoTipo
    this.fabbricatoForm.get('sottoTipologiaFabbricato').reset();
    this.fabbricatoForm.get('sottoTipo').reset();

    this.fabbricatoForm.get('fabbricati').reset();
    this.fabbricatoForm.get('serre').reset();
    this.fabbricatoForm.get('strutture').reset();
    if ($event.value == null) {
      this.tipologicheService.sottoTipologiaFabbricato = [];
      this.tipologicheService.sottoTipologiaFabbricato.unshift({ label: 'Seleziona una sotto tipologia', value: null });
      return;
    }
    this.tipologicheService.sottoTipoFabbricato = [];
    this.tipologicheService.sottoTipoFabbricato.unshift({ label: 'Seleziona un sotto tipo', value: null });
    // popolamento sottotipologia nel caso di selezione di una tipologia con value != null (escludo il valore null di default)
    const tipologia: SelectItem = this.tipologicheService.tipologiaFabbricato.filter((tipologia: SelectItem) => tipologia.value == $event.value)[0];
    this.getSottoTipologie(tipologia);
    this.formService.validateForm(this.fabbricatoForm);
  }

  public onSelectSottoTipologia($event: SelectItem) {
    this.formService.validateForm(this.fabbricatoForm.get('serre') as FormGroup);
    this.formService.validateForm(this.fabbricatoForm.get('fabbricati') as FormGroup);
    this.formService.validateForm(this.fabbricatoForm.get('strutture') as FormGroup);
    this.fabbricatoForm.get('sottoTipo').reset();
    this.onSelectTipologiaCatasto();
    if ($event.value == null) {
      this.tipologicheService.sottoTipoFabbricato = [];
      this.tipologicheService.sottoTipoFabbricato.unshift({ label: 'Seleziona un sotto tipo', value: null });
      this.struttureEnabled = false;
      return;
    }
    const sottotipologia: SelectItem = this.tipologicheService.sottoTipologiaFabbricato && this.tipologicheService.sottoTipologiaFabbricato.filter((sottoTipologia: SelectItem) => sottoTipologia.value == $event.value)[0];
    this.getSottoTipoByValueSottoTipologia(sottotipologia);
    this.formService.validateForm(this.fabbricatoForm);
    //this.fabbricatoForm.get('sottoTipo').reset();
  }

  public onSelectSottoTipo($event: SelectItem) {
    if ($event.value != null) {
      this.formService.validateForm(this.fabbricatoForm.get('strutture') as FormGroup);
      this.struttureEnabled = true;
    }
  }

  public aggiungiParticellaFuoriTrentino($event: Event) {
    $event.preventDefault(); // impedisce l'onsubmit della form intera
    // validazione form prima di fare l'inserimento nella lista
    this.formService.validateForm(this.fabbricatoForm.get('datiCatastali') as FormGroup);
    const invalidFields = this.formService.getInvalids(this.fabbricatoForm.get('datiCatastali') as FormGroup);
    console.log('ADDING PARTICELLA FUORI TRENTINO', this.fabbricatoForm.get('datiCatastali').value);
    console.log('INVALID FIELDS....', invalidFields);
    if (!this.fabbricatoForm.get('datiCatastali').valid) {
      this.validateAllFormFields(this.fabbricatoForm.get('datiCatastali') as FormGroup);
      this.errorService.showErrorWithMessage('E’ necessario compilare i campi obbligatori!', 'tst-fabbricati');
      return false;
    } else { // if (this.fabbricatoForm.get('datiCatastali').valid)
      const particella = this.fabbricatoForm.get('datiCatastali').value;
      const particellaFuoriTn = this.dotazioneTecnicaBuilderService.buildParticellaFuoriTrentino(particella);
      this.particelleList.push(particellaFuoriTn);
      this.fabbricatoForm.get('datiCatastali').reset();
      return true;
    }
  }

  public aggiornaParticellaFuoriTrentino($event: Event) {
    if (this.aggiungiParticellaFuoriTrentino($event)) {
      this.eliminaParticella(this.particellaModificaInCorso);
      this.particellaModificaInCorso = null;
    }
  }

  public aggiungiParticellaInTrentino($event: Event) {
    $event.preventDefault(); // impedisce l'onsubmit della form intera
    // validazione form prima di fare l'inserimento nella lista
    this.formService.validateForm(this.fabbricatoForm.get('datiCatastali') as FormGroup);
    const invalidFields = this.formService.getInvalids(this.fabbricatoForm.get('datiCatastali') as FormGroup);
    console.log('ADDING PARTICELLA IN TRENTINO', this.fabbricatoForm.get('datiCatastali').value);
    console.log('INVALID FIELDS....', invalidFields);
    if (!this.fabbricatoForm.get('datiCatastali').valid) {
      this.validateAllFormFields(this.fabbricatoForm.get('datiCatastali') as FormGroup);
      this.errorService.showErrorWithMessage('E’ necessario compilare i campi obbligatori!', 'tst-fabbricati');
      return;
    } else {
      const comuneCatastale = this.fabbricatoForm.get('datiCatastali').value.comune;
      const particella = this.fabbricatoForm.get('datiCatastali').value.particella.trim();
      const denominatore = this.fabbricatoForm.get('datiCatastali').value.denominatore;
      const tipologia = this.fabbricatoForm.get('datiCatastali').value.tipologia;
      const sub = this.fabbricatoForm.get('datiCatastali').value.subalterno;
      this.dotazioneTecnicaService.verificaParticellaCatasto(particella, denominatore, tipologia, comuneCatastale, sub)
        .subscribe((res: DatiCatastaliDto) => {
          if (res) {
            this.infoCatasto.categoria = res.categoria;
            this.infoCatasto.consistenza = res.consistenza;
            this.infoCatasto.indirizzo = res.indirizzo;
            this.infoCatasto.note = res.note;
            this.infoCatasto.superficie = res.superficie?.toString();
            this.messageService.add({ key: 'aggiungiParticellaInTrentino', sticky: true, severity: 'warn', summary: 'Comune catastale ' + res.comune + ', ' + 'Particella ' + res.particella + ', ' + 'Denominatore ' + res.denominatore + ', ' + 'Tipologia ' + res.tipologia + ', ' + 'Sub ' + res.sub, detail: 'Si desidera procedere con l’inserimento dei dati recuperati?' });
          } else {
            this.messageService.add(A4gMessages.getToast('tst-fabbricati', A4gSeverityMessage.warn, 'Particella non trovata'));
          }
        }, err => {
          this.messageService.add(A4gMessages.getToast('tst-fabbricati', A4gSeverityMessage.error, err.error.message));
        });
    }
  }

  public onConfirmAggiornaDichiarazione() {
    const particella = this.fabbricatoForm.get('datiCatastali').value;
    console.log('Particella da aggiungere: ', particella);
    const particellaInTn = this.dotazioneTecnicaBuilderService.buildParticellaInTrentino(particella, this.infoCatasto);
    this.particelleList.push(particellaInTn);
    this.onRejectAggiornaDichiarazione();
  }

  public onRejectAggiornaDichiarazione() {
    this.fabbricatoForm.get('datiCatastali').reset();
    this.messageService.clear('aggiungiParticellaInTrentino');
  }

  public eliminaParticella(particellaDaEliminare: DatiCatastaliVM) {
    this.particelleList = this.particelleList.filter((particella: DatiCatastaliVM) => {
      if (this.equalsParticella(particella, particellaDaEliminare)) {
        return false;
      } else {
        return true;
      }
    });
  }

  public modificaParticella(particellaDaModificare: DatiCatastaliVM) {
    this.particellaModificaInCorso = null;
    if (!particellaDaModificare.inTrentino) { // Fuori trentino
      this.fabbricatoForm.get('fuoriTrentino').setValue(true);
    } else {
      this.fabbricatoForm.get('fuoriTrentino').setValue(false);
    }
    // Setto la form della particella con i dati della particella da modificare
    this.fabbricatoForm.get('datiCatastali').get('sezione').setValue(particellaDaModificare.sezione);
    this.fabbricatoForm.get('datiCatastali').get('foglio').setValue(particellaDaModificare.foglio);
    this.fabbricatoForm.get('datiCatastali').get('subalterno').setValue(particellaDaModificare.subalterno);
    this.fabbricatoForm.get('datiCatastali').get('particella').setValue(particellaDaModificare.particella);
    this.fabbricatoForm.get('datiCatastali').get('tipologia').setValue(particellaDaModificare.tipologia);
    this.fabbricatoForm.get('datiCatastali').get('comune').setValue(particellaDaModificare.comuneCatastale);
    this.fabbricatoForm.get('datiCatastali').get('denominatore').setValue(particellaDaModificare.denominatore);
    // Salvo la particella da modificare in modo da poterla rimuovere a modifica completata
    this.particelleList.forEach((particella: DatiCatastaliVM) => {
      if (this.equalsParticella(particella, particellaDaModificare)) {
        this.particellaModificaInCorso = particella;
      }
    });
  }

  public annullaModifica($event: Event) {
    $event.preventDefault(); // impedisce l'onsubmit della form intera
    this.particellaModificaInCorso = null;
    // resetto la form della particella
    this.fabbricatoForm.get('datiCatastali').get('sezione').setValue(null);
    this.fabbricatoForm.get('datiCatastali').get('foglio').setValue(null);
    this.fabbricatoForm.get('datiCatastali').get('subalterno').setValue(null);
    this.fabbricatoForm.get('datiCatastali').get('particella').setValue(null);
    this.fabbricatoForm.get('datiCatastali').get('tipologia').setValue(null);
    this.fabbricatoForm.get('datiCatastali').get('comune').setValue(null);
    this.fabbricatoForm.get('datiCatastali').get('denominatore').setValue(null);
    this.formService.resetForm(this.fabbricatoForm.get('datiCatastali') as FormGroup);
  }

  public onSelectAccordion($event: any) { }

 

  public isSelctedStruttureZootecniche() {
    const tipologiaSelected = this.fabbricatoForm.get('tipologiaFabbricato').value;
    if (tipologiaSelected == null) {
      return false;
    }
    const tipologia: SelectItem = this.tipologicheService.tipologiaFabbricato?.filter((tipologia: SelectItem) => tipologia.value == tipologiaSelected)[0];
    if (tipologia?.label.split(' ').join('_') === TipologiaFabbricato.STRUTTURE_ZOOTECNICHE) {
      return true;
    }
    return false;
  }

  // Calcolo del campo volume per lo stoccaggio = superficie * altezza
  public calcVolume(type: 'LIQUAMI' | 'LETAMI') {
    if (type === 'LIQUAMI') {
      const sup = this.formatConverterService.toNumber(this.fabbricatoForm.get('strutture.stoccaggioLiquami.superficie').value);
      const h = this.formatConverterService.toNumber(this.fabbricatoForm.get('strutture.stoccaggioLiquami.altezza').value);
      this.fabbricatoForm.get('strutture.stoccaggioLiquami.volume').setValue(sup * h);
    } else if (type === 'LETAMI') {
      const sup = this.formatConverterService.toNumber(this.fabbricatoForm.get('strutture.stoccaggioLetami.superficie').value);
      const h = this.formatConverterService.toNumber(this.fabbricatoForm.get('strutture.stoccaggioLetami.altezza').value);
      this.fabbricatoForm.get('strutture.stoccaggioLetami.volume').setValue(sup * h);
    }
  }

  public annulla() {
    this.closeDialog(false);
  }

  public onSubmit($event: Event) {
    $event.preventDefault();
    // Disabilito le parti di form che non devono essere salvate
    const tipologiaSelected = this.fabbricatoForm.get('tipologiaFabbricato').value;
    const tipologia: SelectItem = this.tipologicheService.tipologiaFabbricato.filter((tipologia: SelectItem) => tipologia.value == tipologiaSelected)[0];
    const type: keyof typeof FabbricatoTypeEnum = this.disableValidatorsByTipologiaAndGetType(tipologia.label.split(' ').join('_') as keyof typeof TipologiaFabbricato);

    // Valido la form escludendo datiCatastali che non è necessario, in quanto validato sul pulsante aggiungi particella -> valido solo la lista di particelle
    this.formService.validateFormWithSkip(this.fabbricatoForm, ['datiCatastali']);
    const invalidFields: Array<FormFieldMap> = this.formService.getInvalids(this.fabbricatoForm);
    console.log('INVALID FIELDS....', invalidFields);

    if (!this.particelleList || !this.particelleList.length) {
      this.errorService.showErrorWithMessage('Attenzione! Particella non presente o non valida: non è possibile procedere con l’inserimento.', 'tst-fabbricati');
      return;
    }

    // Escludo dalla validazione il campo datiCatastali
    if ((!this.fabbricatoForm.valid && invalidFields.length > 1) || (!this.fabbricatoForm.valid && invalidFields.length == 1 && invalidFields[0].name != 'datiCatastali')) {
      this.validateAllFormFields(this.fabbricatoForm);
      this.errorService.showErrorWithMessage('E’ necessario compilare i campi obbligatori!', 'tst-fabbricati');
      return;
    } else { // se valida non tenendo presente i datiCatastali
      let vm: DettaglioFabbricatoVM = this.fabbricatoForm.value;
      if (this.EDIT_MODE) { // Nel caso sono in update setto l'id al dto
        vm.id = this.dettaglioFabbricato.id;
        vm.tipologiaFabbricato = this.classificazioneFabbricatoVM.tipologiaFabbricato;
        vm.sottoTipologiaFabbricato = this.classificazioneFabbricatoVM.sottoTipologiaFabbricato;
        vm.sottoTipo = this.classificazioneFabbricatoVM.sottoTipo;
      }
      const dettaglioFabbricatoDto: DettaglioFabbricatoDto = this.dotazioneTecnicaBuilderService.toDettaglioFabbricatoDto(vm, type, this.dotazioneTecnicaBuilderService.toParticelleDto(this.particelleList));
      console.log('SAVING FABBRICATO....', dettaglioFabbricatoDto);
      this.postFabbricatoSubscription = this.dotazioneTecnicaService.postFabbricato(this.cuaa, dettaglioFabbricatoDto)
        .subscribe(idFabbricato => {
          const p: FascicoloDaCuaa = this.fascicoloDettaglioService.fascicoloCorrente.value;
          if (p.stato !== StatoFascicoloEnum.IN_AGGIORNAMENTO) {
            p.stato = StatoFascicoloEnum.IN_AGGIORNAMENTO;
            this.fascicoloDettaglioService.fascicoloCorrente.next(p);
          }
          this.messageService.add(A4gMessages.getToast('tst-fabbricati', A4gSeverityMessage.success, "Il fabbricato è stato inserito con successo!"));
          this.closeDialog(true);
        }, error => this.errorService.showError(error, 'tst-fabbricati'));
    }
  }

  public disabledAccordionParticelle(): boolean {
    if (this.isSelctedStruttureZootecniche()) {
      return !this.fabbricatoForm.get('sottoTipo').value;
    } else {
      return this.fabbricatoForm.get('sottoTipologiaFabbricato').invalid;
    }
  }

  private getSottoTipologie($event: SelectItem) {
    if ($event.label.split(' ').join('_') === TipologiaFabbricato.STRUTTURE_ZOOTECNICHE) {
      this.getSottoTipologieZootecnia();
    } else if ($event.label.split(' ').join('_') === TipologiaFabbricato.FABBRICATI_STRUMENTALI) {
      // this.openAccordionByIndex(0);
      this.setupFabbricatoStrumentale();
      this.getSottoTipologieFabbricatiOSerre($event);
    } else if ($event.label.split(' ').join('_') === TipologiaFabbricato.SERRE_E_PROTEZIONI) {
      this.setupSerre();
      this.getSottoTipologieFabbricatiOSerre($event);
    } else {
      this.errorService.showErrorWithMessage(`Non è possibile caricare le tipologie di fabbricato relative a ${$event.label}`, 'tst-fabbricato');
    }
  }

  private getSottoTipologieZootecnia() {
    this.getTipologieSubscription = this.httpClientTipologieService.getTipologia(AmbitoTipologia.ZOOTECNIA).subscribe((sottoTipologie: Array<TipologiaDto>) => {
      if (sottoTipologie?.length) {
        this.tipologicheService.setSottoTipologiaFabbricato(this.dotazioneTecnicaBuilderService.tipologiaDtoToSelectItemArrayConverter(sottoTipologie));
      } else {
        this.errorService.showErrorWithMessage('Non è possibile caricare le tipologie di fabbricato', 'tst-fabbricato');
      }
    }, error => this.errorService.showError(error, 'tst-fabbricato'));
  }

  private getSottoTipologieFabbricatiOSerre($event: SelectItem) {
    this.getSottoTipologieSubscription = this.httpClientTipologieService.getSottoTipologieByIdTipologia($event.value).subscribe((sottotipologie: SottoTipoDto) => {
      if (sottotipologie?.tipologie?.length) {
        this.tipologicheService.setSottoTipologiaFabbricato(this.dotazioneTecnicaBuilderService.tipologiaDtoToSelectItemArrayConverter(sottotipologie.tipologie));
      } else {
        this.errorService.showErrorWithMessage(`Non è possibile caricare le tipologie di fabbricato relative a ${$event.label}`, 'tst-fabbricato');
      }
      this.disableTipologie();
    }, error => this.errorService.showError(error, 'tst-fabbricato'));
  }

  private setupFabbricatoStrumentale() {
    this.fabbricatoEnabled = true;
    if (!this.READONLY_MODE) {
      this.enableFabbricato();
      this.disableSerre();
      this.disableZootecnia();
      this.fabbricatoForm.get('sottoTipo').clearValidators();
    }
  }

  private setupSerre() {
    this.serreEnabled = true;
    if (!this.READONLY_MODE) {
      this.enableSerre();
      this.disableFabbricato();
      this.disableZootecnia();
      this.fabbricatoForm.get('sottoTipo').clearValidators();
    }
  }

  private checkOpenZootecniaTab(dettaglioFabbricatoVM: DettaglioFabbricatoVM) {
    if (!this.READONLY_MODE) {
      this.disableFabbricato();
      this.disableSerre();
      this.enableZootecnia();
      this.fabbricatoForm.get('sottoTipo').setValidators([Validators.required]);
      if (dettaglioFabbricatoVM.sottoTipologiaFabbricato.label.split(' ').join('_') === this.sottoTipologiaZootecnia.STOCCAGGIO_LETAMI) {
        this.sottoTipoLogiaSelected = this.sottoTipologiaZootecnia.STOCCAGGIO_LETAMI;
        this.activeTab = 0;
      } else if (dettaglioFabbricatoVM.sottoTipologiaFabbricato.label.split(' ').join('_') === this.sottoTipologiaZootecnia.STOCCAGGIO_LIQUAMI) {
        this.sottoTipoLogiaSelected = this.sottoTipologiaZootecnia.STOCCAGGIO_LIQUAMI;
        this.activeTab = 1;
      } else if (dettaglioFabbricatoVM.sottoTipologiaFabbricato.label.split(' ').join('_') === this.sottoTipologiaZootecnia.AREE_SCOPERTE) {
        this.sottoTipoLogiaSelected = this.sottoTipologiaZootecnia.AREE_SCOPERTE;
        this.activeTab = 2;
      } else if (dettaglioFabbricatoVM.sottoTipologiaFabbricato.label.split(' ').join('_') === this.sottoTipologiaZootecnia.STALLE) {
        this.sottoTipoLogiaSelected = this.sottoTipologiaZootecnia.STALLE;
        this.activeTab = 3;
      }
      this.enableTipoZootecniaByIndex(this.activeTab);
    }
  }

  private initData() {
    this.closeAccordion();
    this.cols = [
      { field: '', header: '' },
      { field: 'particella', header: 'Numero particella' },
      { field: 'denominatore', header: 'Denominatore' },
      { field: 'tipologia', header: 'Tipologia' },
      { field: 'inTrentino', header: 'In Trentino' },
      { field: 'esito', header: 'Esito' },
      { field: '', header: 'Azioni' },
    ];
    this.tipologicheService.setTipoConduzioneFabbricato();
    this.tipologicheService.setCopertura();
    this.tipologicheService.setTipologiaCatasto();
  }

  private getTipologia() {
    this.getTipologieSubscription = this.httpClientTipologieService.getTipologia(AmbitoTipologia.FABBRICATI).subscribe((tipologie: Array<TipologiaDto>) => {
      if (tipologie && tipologie.length) {
        this.tipologicheService.setTipologiaFabbricato(this.dotazioneTecnicaBuilderService.tipologiaDtoToSelectItemArrayConverter(tipologie));
      } else {
        this.errorService.showErrorWithMessage('Non è possibile caricare un ambito per il fabbricato', 'tst-fabbricato');
      }
    }, error => this.errorService.showError(error, 'tst-fabbricato'));
  }

  private initForm(dettaglioFabbricatoVm?: DettaglioFabbricatoVM) {
    this.fabbricatoForm = new FormGroup({
      tipologiaFabbricato: new FormControl({ value: dettaglioFabbricatoVm?.tipologiaFabbricato?.value ? dettaglioFabbricatoVm.tipologiaFabbricato.value : null, disabled: this.READONLY_MODE }, [Validators.required]),
      sottoTipologiaFabbricato: new FormControl({ value: dettaglioFabbricatoVm?.sottoTipologiaFabbricato?.value ? dettaglioFabbricatoVm.sottoTipologiaFabbricato.value : null, disabled: this.READONLY_MODE }, [Validators.required]),
      sottoTipo: new FormControl({ value: dettaglioFabbricatoVm?.sottoTipo?.value ? dettaglioFabbricatoVm.sottoTipo.value : null, disabled: this.READONLY_MODE }), /** required gestito manualmente solo per strutture */
      fuoriTrentino: new FormControl({ value: dettaglioFabbricatoVm ? dettaglioFabbricatoVm.fuoriTrentino : false, disabled: this.READONLY_MODE }),
      datiCatastali: new FormGroup({
        sezione: new FormControl({ value: dettaglioFabbricatoVm?.datiCatastali?.sezione ? dettaglioFabbricatoVm.datiCatastali.sezione : null, disabled: this.READONLY_MODE }),
        foglio: new FormControl({ value: dettaglioFabbricatoVm?.datiCatastali?.foglio ? dettaglioFabbricatoVm.datiCatastali.foglio : null, disabled: this.READONLY_MODE }),
        subalterno: new FormControl({ value: dettaglioFabbricatoVm?.datiCatastali?.subalterno ? dettaglioFabbricatoVm.datiCatastali.subalterno : null, disabled: this.READONLY_MODE }),
        particella: new FormControl({ value: dettaglioFabbricatoVm?.datiCatastali?.particella ? dettaglioFabbricatoVm.datiCatastali.particella : null, disabled: this.READONLY_MODE }, [Validators.required]),
        tipologia: new FormControl({ value: dettaglioFabbricatoVm?.datiCatastali?.tipologia?.value ? dettaglioFabbricatoVm.datiCatastali.tipologia.value : null, disabled: this.READONLY_MODE }, [Validators.required]),
        comune: new FormControl({ value: dettaglioFabbricatoVm?.datiCatastali?.comuneCatastale ? dettaglioFabbricatoVm.datiCatastali.comuneCatastale : null, disabled: this.READONLY_MODE }),
        denominatore: new FormControl({ value: dettaglioFabbricatoVm?.datiCatastali?.denominatore ? dettaglioFabbricatoVm.datiCatastali.denominatore : null, disabled: this.READONLY_MODE }),
      }),
      fabbricati: new FormGroup({
        denominazione: new FormControl({ value: dettaglioFabbricatoVm?.fabbricati?.denominazione ? dettaglioFabbricatoVm.fabbricati.denominazione : null, disabled: this.READONLY_MODE }),
        indirizzo: new FormControl({ value: dettaglioFabbricatoVm?.fabbricati?.indirizzo ? dettaglioFabbricatoVm.fabbricati.indirizzo : null, disabled: this.READONLY_MODE }),
        comune: new FormControl({ value: dettaglioFabbricatoVm?.fabbricati?.comune ? dettaglioFabbricatoVm.fabbricati.comune : null, disabled: this.READONLY_MODE }, [Validators.required]),
        volume: new FormControl({ value: dettaglioFabbricatoVm?.fabbricati?.volume ? dettaglioFabbricatoVm.fabbricati.volume : null, disabled: this.READONLY_MODE }),
        superficie: new FormControl({ value: dettaglioFabbricatoVm?.fabbricati?.superficie ? dettaglioFabbricatoVm.fabbricati.superficie : null, disabled: this.READONLY_MODE }, [Validators.required]),
        superficieCoperta: new FormControl({ value: dettaglioFabbricatoVm?.fabbricati?.superficieCoperta ? dettaglioFabbricatoVm.fabbricati.superficieCoperta : null, disabled: this.READONLY_MODE }),
        superficieScoperta: new FormControl({ value: dettaglioFabbricatoVm?.fabbricati?.superficieScoperta ? dettaglioFabbricatoVm.fabbricati.superficieScoperta : null, disabled: this.READONLY_MODE }),
        tipoConduzione: new FormControl({ value: dettaglioFabbricatoVm?.fabbricati?.tipoConduzione?.value ? dettaglioFabbricatoVm.fabbricati.tipoConduzione.value : null, disabled: this.READONLY_MODE }, [Validators.required]),
        descrizione: new FormControl({ value: dettaglioFabbricatoVm?.fabbricati?.descrizione ? dettaglioFabbricatoVm.fabbricati.descrizione : null, disabled: this.READONLY_MODE }),
      }),
      serre: new FormGroup({
        denominazione: new FormControl({ value: dettaglioFabbricatoVm?.serre?.denominazione ? dettaglioFabbricatoVm.serre.denominazione : null, disabled: this.READONLY_MODE }),
        indirizzo: new FormControl({ value: dettaglioFabbricatoVm?.serre?.indirizzo ? dettaglioFabbricatoVm.serre.indirizzo : null, disabled: this.READONLY_MODE }),
        comune: new FormControl({ value: dettaglioFabbricatoVm?.serre?.comune ? dettaglioFabbricatoVm.serre.comune : null, disabled: this.READONLY_MODE }, [Validators.required]),
        volume: new FormControl({ value: dettaglioFabbricatoVm?.serre?.volume ? dettaglioFabbricatoVm.serre.volume : null, disabled: this.READONLY_MODE }, [Validators.required]),
        superficie: new FormControl({ value: dettaglioFabbricatoVm?.serre?.superficie ? dettaglioFabbricatoVm.serre.superficie : null, disabled: this.READONLY_MODE }),
        impiantoRiscaldamento: new FormControl({ value: dettaglioFabbricatoVm?.serre?.impiantoRiscaldamento ? dettaglioFabbricatoVm.serre.impiantoRiscaldamento : null, disabled: this.READONLY_MODE }),
        annoCostruzione: new FormControl({ value: dettaglioFabbricatoVm?.serre?.annoCostruzione ? dettaglioFabbricatoVm.serre.annoCostruzione : null, disabled: this.READONLY_MODE }),
        tipologiaMateriale: new FormControl({ value: dettaglioFabbricatoVm?.serre?.tipologiaMateriale ? dettaglioFabbricatoVm.serre.tipologiaMateriale : null, disabled: this.READONLY_MODE }),
        annoAcquisto: new FormControl({ value: dettaglioFabbricatoVm?.serre?.annoAcquisto ? dettaglioFabbricatoVm.serre.annoAcquisto : null, disabled: this.READONLY_MODE }),
        titoloConformitaUrbanistica: new FormControl({ value: dettaglioFabbricatoVm?.serre?.titoloConformitaUrbanistica ? dettaglioFabbricatoVm.serre.titoloConformitaUrbanistica : null, disabled: this.READONLY_MODE }),
        tipoConduzione: new FormControl({ value: dettaglioFabbricatoVm?.serre?.tipoConduzione?.value ? dettaglioFabbricatoVm.serre.tipoConduzione.value : null, disabled: this.READONLY_MODE }, [Validators.required]),
        descrizione: new FormControl({ value: dettaglioFabbricatoVm?.serre?.descrizione ? dettaglioFabbricatoVm.serre.descrizione : null, disabled: this.READONLY_MODE }),
      }),
      strutture: new FormGroup({
        stoccaggioLiquami: new FormGroup({
          denominazione: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLiquami?.denominazione ? dettaglioFabbricatoVm.strutture.stoccaggioLiquami.denominazione : null, disabled: this.READONLY_MODE }),
          indirizzo: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLiquami?.indirizzo ? dettaglioFabbricatoVm.strutture.stoccaggioLiquami.indirizzo : null, disabled: this.READONLY_MODE }),
          comune: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLiquami?.comune ? dettaglioFabbricatoVm.strutture.stoccaggioLiquami.comune : null, disabled: this.READONLY_MODE }, [Validators.required]),
          copertura: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLiquami?.copertura ? dettaglioFabbricatoVm.strutture.stoccaggioLiquami.copertura.value : null, disabled: this.READONLY_MODE }, [Validators.required]),
          superficie: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLiquami?.superficie ? dettaglioFabbricatoVm.strutture.stoccaggioLiquami.superficie : null, disabled: this.READONLY_MODE }, [Validators.required]),
          altezza: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLiquami?.altezza ? dettaglioFabbricatoVm.strutture.stoccaggioLiquami.altezza : null, disabled: this.READONLY_MODE }, [Validators.required]),
          volume: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLiquami?.volume ? dettaglioFabbricatoVm.strutture.stoccaggioLiquami.volume : null, disabled: this.READONLY_MODE }),
          tipoConduzione: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLiquami?.tipoConduzione?.value ? dettaglioFabbricatoVm.strutture.stoccaggioLiquami.tipoConduzione.value : null, disabled: this.READONLY_MODE }, [Validators.required]),
          descrizione: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLiquami?.descrizione ? dettaglioFabbricatoVm.strutture.stoccaggioLiquami.descrizione : null, disabled: this.READONLY_MODE }),
        }),
        stoccaggioLetami: new FormGroup({
          denominazione: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLetami?.denominazione ? dettaglioFabbricatoVm.strutture.stoccaggioLetami.denominazione : null, disabled: this.READONLY_MODE }),
          indirizzo: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLetami?.indirizzo ? dettaglioFabbricatoVm.strutture.stoccaggioLetami.indirizzo : null, disabled: this.READONLY_MODE }),
          comune: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLetami?.comune ? dettaglioFabbricatoVm.strutture.stoccaggioLetami.comune : null, disabled: this.READONLY_MODE }, [Validators.required]),
          copertura: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLetami?.copertura ? dettaglioFabbricatoVm.strutture.stoccaggioLetami.copertura.value : null, disabled: this.READONLY_MODE }, [Validators.required]),
          altezza: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLetami?.altezza ? dettaglioFabbricatoVm.strutture.stoccaggioLetami.altezza : null, disabled: this.READONLY_MODE }, [Validators.required]),
          superficie: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLetami?.superficie ? dettaglioFabbricatoVm.strutture.stoccaggioLetami.superficie : null, disabled: this.READONLY_MODE }, [Validators.required]),
          volume: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLetami?.volume ? dettaglioFabbricatoVm.strutture.stoccaggioLetami.volume : null, disabled: this.READONLY_MODE }),
          tipoConduzione: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLetami?.tipoConduzione?.value ? dettaglioFabbricatoVm.strutture.stoccaggioLetami.tipoConduzione.value : null, disabled: this.READONLY_MODE }, [Validators.required]),
          descrizione: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stoccaggioLetami?.descrizione ? dettaglioFabbricatoVm.strutture.stoccaggioLetami.descrizione : null, disabled: this.READONLY_MODE }),
        }),
        areeScoperte: new FormGroup({
          denominazione: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.areeScoperte?.denominazione ? dettaglioFabbricatoVm.strutture.areeScoperte.denominazione : null, disabled: this.READONLY_MODE }),
          indirizzo: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.areeScoperte?.indirizzo ? dettaglioFabbricatoVm.strutture.areeScoperte.indirizzo : null, disabled: this.READONLY_MODE }),
          comune: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.areeScoperte?.comune ? dettaglioFabbricatoVm.strutture.areeScoperte.comune : null, disabled: this.READONLY_MODE }, [Validators.required]),
          superficie: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.areeScoperte?.superficie ? dettaglioFabbricatoVm.strutture.areeScoperte.superficie : null, disabled: this.READONLY_MODE }, [Validators.required]),
          tipoConduzione: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.areeScoperte?.tipoConduzione?.value ? dettaglioFabbricatoVm.strutture.areeScoperte.tipoConduzione.value : null, disabled: this.READONLY_MODE }, [Validators.required]),
          descrizione: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.areeScoperte?.descrizione ? dettaglioFabbricatoVm.strutture.areeScoperte.descrizione : null, disabled: this.READONLY_MODE }),
        }),
        stalle: new FormGroup({
          denominazione: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stalle?.denominazione ? dettaglioFabbricatoVm.strutture.stalle.denominazione : null, disabled: this.READONLY_MODE }),
          indirizzo: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stalle?.indirizzo ? dettaglioFabbricatoVm.strutture.stalle.indirizzo : null, disabled: this.READONLY_MODE }),
          comune: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stalle?.comune ? dettaglioFabbricatoVm.strutture.stalle.comune : null, disabled: this.READONLY_MODE }, [Validators.required]),
          volume: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stalle?.volume ? dettaglioFabbricatoVm.strutture.stalle.volume : null, disabled: this.READONLY_MODE }),
          superficie: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stalle?.superficie ? dettaglioFabbricatoVm.strutture.stalle.superficie : null, disabled: this.READONLY_MODE }, [Validators.required]),
          tipoConduzione: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stalle?.tipoConduzione?.value ? dettaglioFabbricatoVm.strutture.stalle.tipoConduzione.value : null, disabled: this.READONLY_MODE }, [Validators.required]),
          descrizione: new FormControl({ value: dettaglioFabbricatoVm?.strutture?.stalle?.descrizione ? dettaglioFabbricatoVm.strutture.stalle.descrizione : null, disabled: this.READONLY_MODE }),
        })
      })
    });
  }

  private disableFabbricato() {
    this.fabbricatoForm.get('fabbricati').disable();
  }

  private disableSerre() {
    this.fabbricatoForm.get('serre').disable();
  }

  private disableZootecnia() {
    this.fabbricatoForm.get('strutture').disable();
  }

  private setCatastoInTrentino() {
    this.fabbricatoForm.get('datiCatastali.comune').setValidators([Validators.required, Validators.pattern('^([0-9])+$')]);
    this.fabbricatoForm.get('datiCatastali.sezione').clearValidators();
    this.fabbricatoForm.get('datiCatastali.foglio').clearValidators();
    this.formService.validateForm(this.fabbricatoForm.get('datiCatastali') as FormGroup);
    this.fabbricatoForm.updateValueAndValidity();
  }

  private setCatastoFuoriTrentino() {
    this.fabbricatoForm.get('datiCatastali.sezione').setValidators([Validators.required]);
    this.fabbricatoForm.get('datiCatastali.foglio').setValidators([Validators.required, Validators.pattern('^([0-9])+$')]);
    this.fabbricatoForm.get('datiCatastali.comune').clearValidators();
    this.fabbricatoForm.get('datiCatastali.subalterno').clearValidators();
    this.formService.validateForm(this.fabbricatoForm.get('datiCatastali') as FormGroup);
    this.fabbricatoForm.updateValueAndValidity();
  }

  private disableTipoStrutturaByIndex(index: number) {
    if (index === 0) { // Stoccaggio Letami
      this.fabbricatoForm.get('strutture.stoccaggioLiquami').disable();
      this.fabbricatoForm.get('strutture.areeScoperte').disable();
      this.fabbricatoForm.get('strutture.stalle').disable();
    } else if (index === 1) { // Stoccaggio Liquami
      this.fabbricatoForm.get('strutture.stoccaggioLetami').disable();
      this.fabbricatoForm.get('strutture.areeScoperte').disable();
      this.fabbricatoForm.get('strutture.stalle').disable();
    } else if (index === 2) { // Aree scoperte
      this.fabbricatoForm.get('strutture.stoccaggioLiquami').disable();
      this.fabbricatoForm.get('strutture.stoccaggioLetami').disable();
      this.fabbricatoForm.get('strutture.stalle').disable();
    } else if (index === 3) { // Stalle
      this.fabbricatoForm.get('strutture.stoccaggioLiquami').disable();
      this.fabbricatoForm.get('strutture.stoccaggioLetami').disable();
      this.fabbricatoForm.get('strutture.areeScoperte').disable();
    }
  }

  private enableFabbricato() {
    this.fabbricatoForm.get('fabbricati').enable();
  }

  private enableSerre() {
    this.fabbricatoForm.get('serre').enable();
  }

  private enableZootecnia() {
    this.fabbricatoForm.get('strutture').enable();
  }

  private disableTipologie() {
    // la tipologia, la sottotipologia e il sottotipo sono sempre disabiliti in modifica
    if (this.EDIT_MODE) {
      this.classificazioneFabbricatoVM = {} as ClassificazioneFabbricatoVM;
      this.classificazioneFabbricatoVM.tipologiaFabbricato = this.fabbricatoForm.get('tipologiaFabbricato').value;
      this.classificazioneFabbricatoVM.sottoTipologiaFabbricato = this.fabbricatoForm.get('sottoTipologiaFabbricato').value;
      this.classificazioneFabbricatoVM.sottoTipo = this.fabbricatoForm.get('sottoTipo').value;

      this.fabbricatoForm.get('tipologiaFabbricato').disable();
      this.fabbricatoForm.get('sottoTipologiaFabbricato').disable();
      this.fabbricatoForm.get('sottoTipo').disable();
    }
  }

  private enableTipoZootecniaByIndex(index: number) {
    if (index === 0) { // Stoccaggio Letami
      this.fabbricatoForm.get('strutture.stoccaggioLetami').enable();
      this.fabbricatoForm.get('strutture.stoccaggioLiquami').disable();
      this.fabbricatoForm.get('strutture.areeScoperte').disable();
      this.fabbricatoForm.get('strutture.stalle').disable();
    } else if (index === 1) { // Stoccaggio Liquami
      this.fabbricatoForm.get('strutture.stoccaggioLetami').disable();
      this.fabbricatoForm.get('strutture.stoccaggioLiquami').enable();
      this.fabbricatoForm.get('strutture.areeScoperte').disable();
      this.fabbricatoForm.get('strutture.stalle').disable();
    } else if (index === 2) { // Aree scoperte
      this.fabbricatoForm.get('strutture.areeScoperte').enable();
      this.fabbricatoForm.get('strutture.stoccaggioLiquami').disable();
      this.fabbricatoForm.get('strutture.stoccaggioLetami').disable();
      this.fabbricatoForm.get('strutture.stalle').disable();
    } else if (index === 3) { // Stalle
      this.fabbricatoForm.get('strutture.stalle').enable();
      this.fabbricatoForm.get('strutture.stoccaggioLiquami').disable();
      this.fabbricatoForm.get('strutture.stoccaggioLetami').disable();
      this.fabbricatoForm.get('strutture.areeScoperte').disable();
    }
  }

  private closeAccordion() {
    this.fabbricatoEnabled = false;
    this.serreEnabled = false;
    this.struttureEnabled = false;
  }

  private getSottoTipoByValueSottoTipologia(sottotipologia: SelectItem) {
    if (sottotipologia?.label.split(' ').join('_') === this.sottoTipologiaZootecnia.STOCCAGGIO_LETAMI) {
      this.struttureEnabled = true;
      this.sottoTipoLogiaSelected = this.sottoTipologiaZootecnia.STOCCAGGIO_LETAMI;
      this.activeTab = 0;
      this.enableTipoZootecniaByIndex(this.activeTab);
      this.fabbricatoForm.get('sottoTipo').setValidators([Validators.required]);
      this.getSottoTipiFabbricato(sottotipologia);
      return;
    } else if (sottotipologia?.label.split(' ').join('_') === this.sottoTipologiaZootecnia.STOCCAGGIO_LIQUAMI) {
      this.struttureEnabled = true;
      this.sottoTipoLogiaSelected = this.sottoTipologiaZootecnia.STOCCAGGIO_LIQUAMI;
      this.activeTab = 1;
      this.enableTipoZootecniaByIndex(this.activeTab);
      this.fabbricatoForm.get('sottoTipo').setValidators([Validators.required]);
      this.getSottoTipiFabbricato(sottotipologia);
      return;
    } else if (sottotipologia?.label.split(' ').join('_') === this.sottoTipologiaZootecnia.AREE_SCOPERTE) {
      this.struttureEnabled = true;
      this.sottoTipoLogiaSelected = this.sottoTipologiaZootecnia.AREE_SCOPERTE;
      this.activeTab = 2;
      this.enableTipoZootecniaByIndex(this.activeTab);
      this.fabbricatoForm.get('sottoTipo').setValidators([Validators.required]);
      this.getSottoTipiFabbricato(sottotipologia);  
      return;
    } else if (sottotipologia?.label.split(' ').join('_') === this.sottoTipologiaZootecnia.STALLE) {
      this.struttureEnabled = true;
      this.sottoTipoLogiaSelected = this.sottoTipologiaZootecnia.STALLE;
      this.activeTab = 3;
      this.enableTipoZootecniaByIndex(this.activeTab);
      this.fabbricatoForm.get('sottoTipo').setValidators([Validators.required]);
      this.getSottoTipiFabbricato(sottotipologia);
      //this.fabbricatoForm.get('sottoTipo').setValue({ label: this.tipologicheService.sottoTipologiaFabbricato[0]['label'], value: this.tipologicheService.sottoTipologiaFabbricato[0]['value']}); 
      return;
    } else {
      this.fabbricatoForm.get('sottoTipo').clearValidators();
    }
  }

  private getSottoTipiFabbricato($event: SelectItem) {
    this.getSottoTipoSubscription = this.httpClientTipologieService.getSottoTipologieByIdTipologia($event.value).subscribe((sottotipi: SottoTipoDto) => {
      if (sottotipi?.tipologie?.length) {
        this.tipologicheService.setSottoTipoFabbricato(this.dotazioneTecnicaBuilderService.tipologiaDtoToSelectItemArrayConverter(sottotipi.tipologie));
      } else {
        this.errorService.showErrorWithMessage(`Non è possibile caricare le sotto tipologie di fabbricato relative a ${$event.label}`, 'tst-fabbricato');
      }
      this.disableTipologie();
    }, error => this.errorService.showError(error, 'tst-fabbricato'));
  }

  private validateAllFormFields(formGroup: FormGroup) { //{1}
    Object.keys(formGroup.controls).forEach(field => {  //{2}
      const control = formGroup.get(field);             //{3}
      if (control instanceof FormControl) {             //{4}
        control.markAsTouched({ onlySelf: true });
      } else if (control instanceof FormGroup) {        //{5}
        this.validateAllFormFields(control);            //{6}
      }
    });
  }

  private equalsParticella(p1: DatiCatastaliVM, p2: DatiCatastaliVM) {
    if (p1.particella === p2.particella
      && p1.denominatore === p2.denominatore
      && p1.foglio === p2.foglio
      && p1.sezione === p2.sezione
      && p1.tipologia === p2.tipologia) {
      return true;
    } else {
      return false;
    }
  }

  private disableValidatorsByTipologiaAndGetType(tipologia: keyof typeof TipologiaFabbricato): keyof typeof FabbricatoTypeEnum {
    if (tipologia === TipologiaFabbricato.STRUTTURE_ZOOTECNICHE) {
      this.disableFabbricato();
      this.disableSerre();
      this.disableTipoStrutturaByIndex(this.activeTab);
      if (this.activeTab === 0 || this.activeTab === 1) {
        return FabbricatoTypeEnum.STOCCAGGIO; // stoccaggio letami e liquami
      } else {
        return FabbricatoTypeEnum.FABBRICATO; // aree scoperte e stalle
      }
    } else if (tipologia === TipologiaFabbricato.SERRE_E_PROTEZIONI) {
      this.disableFabbricato();
      this.disableZootecnia();
      return FabbricatoTypeEnum.SERRA; // serre e protezioni
    } else if (tipologia === TipologiaFabbricato.FABBRICATI_STRUMENTALI) {
      this.disableSerre();
      this.disableZootecnia();
      return FabbricatoTypeEnum.STRUMENTALE; // fabbricati strumentali
    }
  }

  private closeDialog(refresh: boolean) {
    this.fabbricatoForm.reset();
    this.cuaa = null;
    this.dettaglioFabbricato = null;
    this.onHideDialog.emit(refresh);
    this.display = false;
  }

  // Non necessario
  private openAccordionByIndex(indexToOpen: number) {
    this.accordion.tabs.forEach((tab, index) => {
      if (index === indexToOpen) {
        tab.selected = true;
      } else {
        tab.selected = false;
      }
    });
  }
}
