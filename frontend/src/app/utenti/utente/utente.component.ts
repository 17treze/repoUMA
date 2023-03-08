import { HttpClient } from '@angular/common/http';
import { Component, ComponentFactory, ComponentFactoryResolver, ComponentRef, OnDestroy, OnInit, ViewChild, ViewContainerRef, ViewRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import * as FileSaver from 'file-saver';
import { MenuItem, MessageService, SelectItem, TreeNode } from 'primeng/api';
import { Observable } from 'rxjs';
import { concatMap } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { EnteCaa } from 'src/app/a4g-common/classi/EnteSedeCaa';
import { Persona } from 'src/app/a4g-common/classi/Persona';
import { validaInput } from 'src/app/a4g-common/validazione/validaInput';
import { Configuration } from 'src/app/app.constants';
import { LoaderService } from 'src/app/loader.service';
import { CodiceResponsabilita } from '../classi/CodiceResponsabilita';
import { DatiAnagrafici } from '../classi/datiAnagrafici';
import { DatiDomanda, TipoDomandaRegistrazione } from '../classi/DatiDomanda';
import { Firma } from '../classi/firma';
import { IChildInteraction } from '../classi/IChildInteaction';
import { IParentInteraction } from '../classi/IParentInteraction';
import { ResponsabilitaAltriEnti } from '../classi/responsabilita-altri';
import { ResponsabilitaCaa } from '../classi/responsabilita-caa';
import { ResponsabilitaConsulente } from '../classi/responsabilita-consulente';
import { ResponsabilitaPat } from '../classi/responsabilita-pat';
import { ResponsabilitaRichieste } from '../classi/responsabilita-richieste';
import { ResponsabilitaTitolare } from '../classi/responsabilita-titolare';
import { DistributoreComponent } from '../responsabilita/distributore/distributore.component';
import { AltriEntiComponent } from '../responsabilita/altri-enti/altri-enti.component';
import { CAAComponent } from '../responsabilita/caa/caa.component';
import { ConsulenteComponent } from '../responsabilita/consulente/consulente.component';
import { LegaleComponent } from '../responsabilita/legale/legale.component';
import { PATComponent } from '../responsabilita/pat/pat.component';
import { TitolareComponent } from '../responsabilita/titolare/titolare.component';
import { UtentiService } from '../utenti.service';
import { ResponsabilitaDistributore } from '../classi/responsabilita-distributore';
import { Distributore } from 'src/app/a4g-common/classi/distributore';

@Component({
  selector: 'app-utente',
  templateUrl: './utente.component.html',
  styleUrls: ['./utente.component.css']
})

export class UtenteComponent implements OnInit, IParentInteraction, OnDestroy {
  public selectedResponsabilita: any;
  public responsabilitaList: SelectItem[];
  public datiDomanda: DatiDomanda;
  public datiAnagraficiUtente: DatiAnagrafici;
  public cfReadonly = false;
  public canChangeCf: boolean;
  public responsabilitaNonValide: boolean;
  public inserimentoDisabled: boolean = false;
  public viewPrivacy: boolean = false;
  public viewModuloDomanda: boolean = false;
  public checked1: boolean = false;
  public checked2: boolean = false;
  public saveOk: boolean = false;
  public blockCheckPrivacyOk = false;
  public informativaPrivacyFile: File;
  public applicativi: MenuItem[];
  public selectedApplicativi: MenuItem[];
  public canProtocol: boolean = true;

  private canAdd: boolean;
  private hasPrivacyProtocollata: boolean = false;
  private firmaPrivacy: Firma;
  private informativaPrivacyFirmataPDF: File;
  private idRichiesta: number;
  private persona: Persona;
  private index: number = 0;
  private componentsReferences = [];
  private controlliDatiAnagrafici = [];
  private legaleRappresentanteErrorList = [];
  private caaErrorList = [];
  private patErrorList = [];
  private altriEntiErrorList = [];
  private consulenteErrorList = [];
  private distributoreErrorList = [];

  @ViewChild('viewContainerRef', { read: ViewContainerRef }) VCR: ViewContainerRef;

  constructor(private loader: LoaderService,
    private utentiService: UtentiService,
    private CFR: ComponentFactoryResolver,
    private messageService: MessageService,
    private _configuration: Configuration,
    private http: HttpClient, private messages: MessageService,
    private configuration: Configuration,
    private translateService: TranslateService
  ) { }

  ngOnInit() {
    this.datiDomanda = new DatiDomanda();
    this.loader.setTimeout(480000); //otto minuti
    this.setServizi();
    this.setResponsabilitaList();
    this.setControlliDatiAnagrafici();
    this.canAdd = false;
    this.canChangeCf = true;
    this.responsabilitaNonValide = true;
    if (!this.idRichiesta) {
      this.caricaDatiAnagrafici();
    }
  }

  private getProfiliUtenteConnesso() {
    this.utentiService.getProfiliUtenteConnesso().subscribe(
      profiloList => {
        if (profiloList && profiloList.some(element => element.responsabilita === 'TITOLARE_AZIENDA_AGRICOLA')) {
          this.aggiungiResponsabilitaTitolareLegaleRappresentante();
        }
      });
  }

  private aggiungiResponsabilitaTitolareLegaleRappresentante() {
    let cfPersona: string;
    if (this.datiAnagraficiUtente && validaInput.validaCuaaIntero(this.datiAnagraficiUtente.codiceFiscale, false)) {
      cfPersona = this.datiAnagraficiUtente.codiceFiscale;
    } else {
      return;
    }
    const componentFactory = this.CFR.resolveComponentFactory(LegaleComponent);
    const currentComponent = this.createComponent(componentFactory);
    (currentComponent as LegaleComponent).cfPersona = cfPersona;
  }

  private setControlliDatiAnagrafici() {
    this.controlliDatiAnagrafici = [];
    this.controlliDatiAnagrafici.push('cognome');
    this.controlliDatiAnagrafici.push('nome');
    this.controlliDatiAnagrafici.push('codiceFiscale');
    this.controlliDatiAnagrafici.push('email');
    this.controlliDatiAnagrafici.push('telefono');
  }

  private setServizi() {
    this.applicativi = [
      { label: 'SRT', title: 'SRTrento - Misure strutturali' },
      { label: 'A4G', title: 'A4G - Nuovo Sistema Informativo Agricoltura' },
      { label: 'AGS', title: 'SIAP - Sistema Informativo Agricoltura' }
    ];
  }

  private setResponsabilitaList() {
    this.responsabilitaList = [];
    //this.responsabilitaList.push({ label: 'Titolare Impresa', value: { id: 1, name: 'Titolare Impresa', code: CodiceResponsabilita.TI } });
    this.responsabilitaList.push({ label: this.translateService.instant('gestioneUtenze.TI_LR'), value: { id: 2, name: this.translateService.instant('gestioneUtenze.TI_LR'), code: CodiceResponsabilita.LR } });
    this.responsabilitaList.push({ label: 'Libero professionista/Consulente', value: { id: 3, name: 'Libero professionista/Consulente', code: CodiceResponsabilita.LPC } });
    this.responsabilitaList.push({ label: 'Dipendente/Collaboratore PAT', value: { id: 4, name: 'Dipendente/Collaboratore PAT', code: CodiceResponsabilita.PAT } });
    this.responsabilitaList.push({ label: 'Dipendente/Collaboratore CAA', value: { id: 5, name: 'Dipendente/Collaboratore CAA', code: CodiceResponsabilita.CAA } });
    this.responsabilitaList.push({ label: 'Dipendente/Collaboratore Altri Enti', value: { id: 6, name: 'Dipendente/Collaboratore Altri Enti', code: CodiceResponsabilita.ALTRI } });
    this.responsabilitaList.push({ label: 'Dipendente/Collaboratore Distributore', value: { id: 7, name: 'Dipendente/Collaboratore Distributore', code: CodiceResponsabilita.DIPENDENTE_DISTRIBUTORE } });
    this.selectedResponsabilita = this.responsabilitaList[0].value;
  }
  
  ngOnDestroy(): void {
    this.loader.resetTimeout();
  }

  caricaDatiAnagrafici() {
    this.utentiService.getDatiAnagraficiUtente().subscribe(
      (result) => {
        this.datiAnagraficiUtente = result;
        this.cfReadonly = this.datiAnagraficiUtente.codiceFiscale != null && this.datiAnagraficiUtente.codiceFiscale.length > 0;
        if (this.datiAnagraficiUtente && validaInput.validaCuaaIntero(this.datiAnagraficiUtente.codiceFiscale, false)) {
          this.canAdd = true;
        }
        this.verificaPrivacy(this.datiAnagraficiUtente.codiceFiscale);
        this.getProfiliUtenteConnesso();
        this.recuperaDomandaApprovata(this.datiAnagraficiUtente.codiceFiscale);
      }
    );
  }

  recuperaDomandaApprovata(codiceFiscale) {
    this.utentiService.getUltimaDomandaUtenteCorrente('APPROVATA', TipoDomandaRegistrazione.COMPLETA).subscribe(
      (result) => {
        if (result) {
          console.log('Result recuperaDomandaApprovata: ', result);

          // Valorizzo dati anagrafici utente.
          // TODO: Cognome e nome in sola lettura?
          this.datiAnagraficiUtente = result.datiAnagrafici;

          // Valorizzo applicativi preselezionati
          this.selectedApplicativi = [];
          result.servizi.forEach(element => {
            const applicativo = this.applicativi.find(p => p.label === element);
            this.selectedApplicativi.push(applicativo);
          });

          if (result.responsabilitaRichieste.responsabilitaAltriEnti) {
            result.responsabilitaRichieste.responsabilitaAltriEnti.forEach(
              item => this.aggiungiResponsabilitaAltriEnti(item)
            );
          }
          if (result.responsabilitaRichieste.responsabilitaCaa) {
            result.responsabilitaRichieste.responsabilitaCaa.forEach(
              item => this.aggiungiResponsabilitaCAA(item)
            );
          }
          if (result.responsabilitaRichieste.responsabilitaConsulente) {
            result.responsabilitaRichieste.responsabilitaConsulente.forEach(
              item => this.aggiungiResponsabilitaConsulente(item)
            );
          }
          /*
          if (result.responsabilitaRichieste.responsabilitaLegaleRappresentante) {
            this.aggiungiResponsabilitaLegaleRappresentante(result.responsabilitaRichieste.responsabilitaLegaleRappresentante);
          }
          */
          if (result.responsabilitaRichieste.responsabilitaPat) {
            result.responsabilitaRichieste.responsabilitaPat.forEach(
              item => this.aggiungiResponsabilitaPat(item)
            );
          }
          /*
          if (result.responsabilitaRichieste.responsabilitaTitolare) {
            result.responsabilitaRichieste.responsabilitaTitolare.forEach(
              item => this.aggiungiResponsabilitaTitolare(item)
            );
          }
          */
          if (result.responsabilitaRichieste.responsabilitaDistributore) {
            result.responsabilitaRichieste.responsabilitaDistributore.forEach(
              item => this.aggiungiResponsabilitaDistributore(item)
            );
          }
        }
      }
    );
  }

  aggiungiResponsabilitaAltriEnti(responsabilita: ResponsabilitaAltriEnti) {
    const componentFactory = this.CFR.resolveComponentFactory(AltriEntiComponent);
    const component = this.createComponent(componentFactory) as AltriEntiComponent;
    component.title = 'Dipendente/Collaboratore Altri Enti';
    component.codeResponsabilita = CodiceResponsabilita.ALTRI as any;
    component.denominazione = responsabilita.denominazione;
    component.piva = responsabilita.piva;
    component.dirigente = responsabilita.dirigente;
    component.note = responsabilita.note;
    this.responsabilitaNonValide = false;
  }

  aggiungiResponsabilitaCAA(responsabilita: ResponsabilitaCaa) {
    const componentFactory = this.CFR.resolveComponentFactory(CAAComponent);
    const component = this.createComponent(componentFactory) as CAAComponent;
    component.title = 'Dipendente / Collaboratore CAA';
    component.codeResponsabilita = CodiceResponsabilita.CAA as any;
    component.responsabile = responsabilita.responsabile;
    const entiSedi: number[] = [];
    responsabilita.sedi.forEach(item => entiSedi.push((item as any).identificativo));
    component.setSelectedIds(entiSedi);
    this.responsabilitaNonValide = false;
  }

  aggiungiResponsabilitaConsulente(responsabilita: ResponsabilitaConsulente) {
    const componentFactory = this.CFR.resolveComponentFactory(ConsulenteComponent);
    const component = this.createComponent(componentFactory) as ConsulenteComponent;
    component.title = 'Libero professionista/Consulente';
    component.codeResponsabilita = CodiceResponsabilita.LPC as any;
    component.ordine = responsabilita.ordine;
    component.iscrizione = responsabilita.iscrizione;
    component.cuaa = responsabilita.cuaa;
    this.responsabilitaNonValide = false;
  }

  aggiungiResponsabilitaLegaleRappresentante(responsabilita: ResponsabilitaTitolare[]) {
    const cfPersona = this.datiAnagraficiUtente.codiceFiscale;
    const componentFactory = this.CFR.resolveComponentFactory(LegaleComponent);
    const component = this.createComponent(componentFactory) as LegaleComponent;
    (component as LegaleComponent).cfPersona = cfPersona;
    component.title = this.translateService.instant('gestioneUtenze.TI_LR');
    component.codeResponsabilita = CodiceResponsabilita.LR as any;
    responsabilita.forEach(r => component.addElencoResponsabilita(r));
    this.responsabilitaNonValide = false;
  }

  aggiungiResponsabilitaTitolare(responsabilita: ResponsabilitaTitolare) {
    const cfPersona = this.datiAnagraficiUtente.codiceFiscale;
    const componentFactory = this.CFR.resolveComponentFactory(LegaleComponent);
    const component = this.createComponent(componentFactory) as LegaleComponent;
    (component as LegaleComponent).cfPersona = cfPersona;
    component.title = this.translateService.instant('gestioneUtenze.TI_LR');
    component.codeResponsabilita = CodiceResponsabilita.TI as any;
    component.addElencoResponsabilita(responsabilita);
    this.responsabilitaNonValide = false;
  }

  aggiungiResponsabilitaPat(responsabilita: ResponsabilitaPat) {
    const componentFactory = this.CFR.resolveComponentFactory(PATComponent);
    const component = this.createComponent(componentFactory) as PATComponent;
    component.title = 'Dipendente/Collaboratore PAT';
    component.codeResponsabilita = CodiceResponsabilita.PAT as any;
    component.matricola = responsabilita.matricola;
    component.dipartimento = responsabilita.dipartimento;
    component.dirigente = responsabilita.dirigente;
    component.note = responsabilita.note;
    this.responsabilitaNonValide = false;
  }

  aggiungiResponsabilitaDistributore(responsabilita: ResponsabilitaDistributore) {
    const componentFactory = this.CFR.resolveComponentFactory(DistributoreComponent);
    const component = this.createComponent(componentFactory) as DistributoreComponent;
    component.title = 'Dipendente / Collaboratore Distributore';
    component.codeResponsabilita = CodiceResponsabilita.DIPENDENTE_DISTRIBUTORE as any;
    component.responsabile = responsabilita.responsabile;
    const distributori: number[] = [];
    responsabilita.distributori.forEach(item => distributori.push((item as Distributore).id));
    component.setSelectedIds(distributori);
    this.responsabilitaNonValide = false;
  }


  verificaPrivacy(cf) {
    this.utentiService.ricercaPersone({ codiceFiscale: cf }).subscribe(persone => {
      if (persone && persone.length > 0 && persone[0]) {
        console.log(persone[0]);
        this.persona = persone[0];
        this.datiDomanda.autorizzazioni = persone[0].nrProtocolloPrivacyGenerale;
        if (this.datiDomanda.autorizzazioni && this.datiDomanda.autorizzazioni.length > 0) {
          this.viewPrivacy = true;
          this.checked1 = true;
          this.hasPrivacyProtocollata = true;
        }
      }
    },
      error => {
        console.error('Errore in ricercaPersone: ' + error);
        const errMsg = error.error.message;
        this.messages.add(A4gMessages.getToast('generic', A4gSeverityMessage.error, 'Errore in recupero Persona: ' + errMsg));
      }
    );
  }

  // Gestione componenti responsabilita

  addComponent() {
    if (this.componentsReferences && this.componentsReferences.length > 0 && this.componentsReferences.filter(component => !component.instance.isValid).length > 0) {
      return this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, 'Per aggiungere un’altra responsabilità è necessario completare la responsabilità precedentemente aggiunta o eliminarla'));
    }

    let cfPersona: string;

    if (this.datiAnagraficiUtente && validaInput.validaCuaaIntero(this.datiAnagraficiUtente.codiceFiscale, false)) {
      cfPersona = this.datiAnagraficiUtente.codiceFiscale;
    } else {
      return;
    }

    switch (this.selectedResponsabilita.code) {
      case CodiceResponsabilita.TI: {
        const componentFactory = this.CFR.resolveComponentFactory(TitolareComponent);
        const currentComponent = this.createComponent(componentFactory);
        (currentComponent as TitolareComponent).cfPersona = cfPersona;
        (currentComponent as TitolareComponent).cuaa = cfPersona;

        if (!currentComponent.isValid) {
          currentComponent.removeMe(true);
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('NO_RESPONSABILITA') + cfPersona));
        }
        break;
      }
      case CodiceResponsabilita.LR: {
        const array = this.componentsReferences.filter(component => component.instance.codeResponsabilita == CodiceResponsabilita.LR);
        if (array.length > 0) {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.responsabilitaPresente));
          break;
        }
        const componentFactory = this.CFR.resolveComponentFactory(LegaleComponent);//(TitolareComponent);
        const currentComponent = this.createComponent(componentFactory);
        (currentComponent as LegaleComponent).cfPersona = cfPersona;

        /* Correttiva #253
        if (!currentComponent.isValid) {
          currentComponent.removeMe(true);
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('NO_RESPONSABILITA') + cfPersona));
        }
        */

        break;
      }
      case CodiceResponsabilita.LPC: {
        const componentFactory = this.CFR.resolveComponentFactory(ConsulenteComponent);
        const currentComponent = this.createComponent(componentFactory);
        break;
      }
      case CodiceResponsabilita.PAT: {
        const componentFactory = this.CFR.resolveComponentFactory(PATComponent);
        const currentComponent = this.createComponent(componentFactory);
        break;
      }
      case CodiceResponsabilita.CAA: {
        const componentFactory = this.CFR.resolveComponentFactory(CAAComponent);
        const currentComponent = this.createComponent(componentFactory);
        break;
      }
      case CodiceResponsabilita.ALTRI: {
        const componentFactory = this.CFR.resolveComponentFactory(AltriEntiComponent);
        const currentComponent = this.createComponent(componentFactory);
        break;
      }
      case CodiceResponsabilita.DIPENDENTE_DISTRIBUTORE: {
        const componentFactory = this.CFR.resolveComponentFactory(DistributoreComponent);
        const currentComponent = this.createComponent(componentFactory);
        break;
      }
      default: {
        break;
      }
    }
    this.canChangeCf = false;

    this.checkEsisteResponsbilita();
  }

  createComponent(componentFactory: ComponentFactory<IChildInteraction>): IChildInteraction {
    const componentRef: ComponentRef<IChildInteraction> = this.VCR.createComponent(componentFactory, 0);
    const currentComponent = componentRef.instance;

    currentComponent.selfRef = currentComponent;
    currentComponent.index = ++this.index;

    currentComponent.title = this.selectedResponsabilita.name;
    currentComponent.codeResponsabilita = this.selectedResponsabilita.code;
    currentComponent.setDisabled(this.inserimentoDisabled);

    // prividing parent Component reference to get access to parent class methods
    currentComponent.compInteraction = this;
    // add reference for newly created component
    this.componentsReferences.unshift(componentRef);
    return currentComponent;
  }

  remove(index: number) {
    if (this.VCR.length <= 0) {
      return;
    }

    // removing component from container
    const componentRef = this.componentsReferences.filter(x => x.instance.index === index)[0] as ViewRef;
    if (!componentRef) {
      return;
    }

    componentRef.destroy();
    this.componentsReferences = this.componentsReferences.filter(component => component.instance.index !== index);
    if (this.VCR.length <= 0) {
      this.canChangeCf = true;
    }

    this.checkEsisteResponsbilita();
  }

  private checkEsisteResponsbilita() {
    if (!this.componentsReferences || this.componentsReferences.length === 0) {
      this.responsabilitaNonValide = true;
    } else {
      this.responsabilitaNonValide = false;
    }

    console.log('responsabilitaNonValide ' + (this.responsabilitaNonValide));
  }

  modificaStatoRichiesta() {
    this.inserimentoDisabled = !this.inserimentoDisabled;
    this.saveOk = false;
    this.viewModuloDomanda = false;
    this.checked2 = false;

    if (this.checked1) {
      this.blockCheckPrivacyOk = true;
    }

    this.componentsReferences.forEach((item, index) => {
      (item.instance as IChildInteraction).setDisabled(this.inserimentoDisabled);
    });
  }

  private enableRecursive(node: TreeNode, isSelectable: boolean) {
    node.selectable = isSelectable;
    if (node.children) {
      node.children.forEach(childNode => {
        this.enableRecursive(childNode, isSelectable);
      });
    }
  }

  onSubmit(f: NgForm) {

    console.log('Salva');

    this.messageService.clear();

    if (f.invalid) { // controlla anagrafica + esistenza check non sovrebbe mai entrare per disabled
      console.log(f);
      if (!this.checkDatiAnagrafici(f) || !this.checkProfiliRichiesti(f)) {
        return;
      } else {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_DATI));
      }
      return;
    }

    if (!this.checkResponsabilita()) {
      return;
    }

    this.datiDomanda.datiAnagrafici = this.datiAnagraficiUtente;
    this.datiDomanda.servizi = this.popolaApplicativi();  //TODO servizi -> applicativi
    this.datiDomanda.responsabilitaRichieste = this.popolaResponsabilita();
    this.datiDomanda.tipoDomandaRegistrazione = TipoDomandaRegistrazione.COMPLETA;

    console.log(this.datiDomanda.responsabilitaRichieste);

    this.legaleRappresentanteErrorList = [];
    this.caaErrorList = [];
    this.patErrorList = [];
    this.altriEntiErrorList = [];
    this.consulenteErrorList = [];
    this.distributoreErrorList = [];
    if (this.datiDomanda.responsabilitaRichieste.responsabilitaLegaleRappresentante) {
      this.verificaValiditaLegaleRappresentante();
    }
    if (this.datiDomanda.responsabilitaRichieste.responsabilitaCaa) {
      this.verificaValiditaCaa();
    }
    if (this.datiDomanda.responsabilitaRichieste.responsabilitaPat) {
      this.verificaValiditaPat();
    }
    if (this.datiDomanda.responsabilitaRichieste.responsabilitaAltriEnti) {
      this.verificaValiditaAltriEnti();
    }
    if (this.datiDomanda.responsabilitaRichieste.responsabilitaConsulente) {
      this.verificaValiditaConsulente();
    }
    if (this.datiDomanda.responsabilitaRichieste.responsabilitaDistributore) {
      this.verificaValiditaDistributore();
    }

    if (this.verificaAssenzaErrorListResponsabilita()
      && ((this.datiDomanda.responsabilitaRichieste.responsabilitaAltriEnti && this.datiDomanda.responsabilitaRichieste.responsabilitaAltriEnti.length > 0)
        || (this.datiDomanda.responsabilitaRichieste.responsabilitaCaa && this.datiDomanda.responsabilitaRichieste.responsabilitaCaa.length > 0)
        || (this.datiDomanda.responsabilitaRichieste.responsabilitaConsulente && this.datiDomanda.responsabilitaRichieste.responsabilitaConsulente.length > 0)
        || (this.datiDomanda.responsabilitaRichieste.responsabilitaLegaleRappresentante && this.datiDomanda.responsabilitaRichieste.responsabilitaLegaleRappresentante.length > 0)
        || (this.datiDomanda.responsabilitaRichieste.responsabilitaPat && this.datiDomanda.responsabilitaRichieste.responsabilitaPat.length > 0)
        || (this.datiDomanda.responsabilitaRichieste.responsabilitaTitolare && this.datiDomanda.responsabilitaRichieste.responsabilitaTitolare.length > 0) 
        || (this.datiDomanda.responsabilitaRichieste.responsabilitaDistributore && this.datiDomanda.responsabilitaRichieste.responsabilitaDistributore.length > 0))) {
      if (this.datiDomanda.id == null) {
        this.inserisci();
      } else {
        this.aggiorna();
      }
      this.rimuoviAllegati();
    } else {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, 'La sezione Responsabilità non è compilata correttamente'));
    }
  }

  private verificaAssenzaErrorListResponsabilita() {
    return this.caaErrorList.length === 0
      && this.patErrorList.length === 0
      && this.altriEntiErrorList.length === 0
      && this.consulenteErrorList.length === 0
      && this.legaleRappresentanteErrorList.length === 0
      && this.distributoreErrorList.length === 0;
  }

  private verificaValiditaConsulente() {
    this.datiDomanda.responsabilitaRichieste.responsabilitaConsulente.forEach(
      element => {
        if (!element.ordine && !this.consulenteErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.ORDINE_APPARTENENZA'))) {
          this.consulenteErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.ORDINE_APPARTENENZA'));
        }
        if (!element.iscrizione && !this.consulenteErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.NUMERO_ISCRIZIONE'))) {
          this.consulenteErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.NUMERO_ISCRIZIONE'));
        }
        if (!element.cuaa && !this.consulenteErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.CUAA'))) {
          this.consulenteErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.CUAA'));
        }
        if (!element.denominazione && !this.consulenteErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.DESC_IMPRESA'))) {
          this.consulenteErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.DESC_IMPRESA'));
        }
        if (!element.rappresentante && !this.consulenteErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.RAPP_LEGALE'))) {
          this.consulenteErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.RAPP_LEGALE'));
        }
        if (!element.allegato && !this.consulenteErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.AUTORIZZAZIONE'))) {
          this.consulenteErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.AUTORIZZAZIONE'));
        }
      }
    );
    if (this.consulenteErrorList.length > 0) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, 'Per la responabilità "Libero professionista/Consulente" non sono stati compilati i campi: ' + this.consulenteErrorList.toString()));
    }
  }

  private verificaValiditaAltriEnti() {
    this.datiDomanda.responsabilitaRichieste.responsabilitaAltriEnti.forEach(
      element => {
        if (!element.denominazione && !this.altriEntiErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.DENOMINAZIONE_ENTE'))) {
          this.altriEntiErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.DENOMINAZIONE_ENTE'));
        }
        if (!element.piva && !this.altriEntiErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.PARTITA_IVA'))) {
          this.altriEntiErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.PARTITA_IVA'));
        }
        if (!element.dirigente && !this.altriEntiErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.RESPONSABILE'))) {
          this.altriEntiErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.RESPONSABILE'));
        }
        if (!element.note && !this.altriEntiErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.motivazioneRichiesta'))) {
          this.altriEntiErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.motivazioneRichiesta'));
        }
        if (!element.allegato && !this.altriEntiErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.AUTORIZZAZIONE'))) {
          this.altriEntiErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.AUTORIZZAZIONE'));
        }
      }
    );
    if (this.altriEntiErrorList.length > 0) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, 'Per la responabilità "Dipendente/Collaboratore Altri Enti" non sono stati compilati i campi: ' + this.altriEntiErrorList.toString()));
    }
  }

  private verificaValiditaPat() {
    this.datiDomanda.responsabilitaRichieste.responsabilitaPat.forEach(
      element => {
        if (!element.matricola && !this.patErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.MATRICOLA'))) {
          this.patErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.MATRICOLA'));
        }
        if (!element.dirigente && !this.patErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.DIRIGENTE'))) {
          this.patErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.DIRIGENTE'));
        }
        if (!element.dipartimento && !this.patErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.STRUTTURA'))) {
          this.patErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.STRUTTURA'));
        }
        if (!element.note && !this.patErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.motivazioneRichiesta'))) {
          this.patErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.motivazioneRichiesta'));
        }
        if (!this.datiDomanda.responsabilitaRichieste.responsabilitaPat[0].allegato && !this.patErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.AUTORIZZAZIONE'))) {
          this.patErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.AUTORIZZAZIONE'));
        }
      }
    );
    if (this.patErrorList.length > 0) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, 'Per la responabilità "Dipendente/Collaboratore PAT" non sono stati compilati i campi: ' + this.patErrorList.toString()));
    }
  }

  private verificaValiditaCaa() {
    this.datiDomanda.responsabilitaRichieste.responsabilitaCaa.forEach(
      element => {
        if (element.sedi.length === 0 && !this.caaErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.CAA_SEDI'))) {
          this.caaErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.CAA_SEDI'));
        }
        if (!element.responsabile && !this.caaErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.RESPONSABILE'))) {
          this.caaErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.RESPONSABILE'));
        }
        if (!element.allegato && !this.caaErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.AUTORIZZAZIONE'))) {
          this.caaErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.AUTORIZZAZIONE'));
        }
        if (this.caaErrorList.length > 0) {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, 'Per la responabilità "Dipendente/Collaboratore CAA" non sono stati compilati i campi: ' + this.caaErrorList.toString()));
        }
      }
    );
  }

  private verificaValiditaLegaleRappresentante() {
    this.datiDomanda.responsabilitaRichieste.responsabilitaLegaleRappresentante.forEach(
      element => {
        if (!element.cuaa && !this.legaleRappresentanteErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.CAA_SEDI'))) {
          this.legaleRappresentanteErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.CAA_SEDI'));
        }
        if (!element.denominazione && !this.legaleRappresentanteErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.DESC_IMPRESA'))) {
          this.legaleRappresentanteErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.DESC_IMPRESA'));
        }
      }
    );
    if (this.legaleRappresentanteErrorList.length > 0) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, 'Per la responabilità "Titolare Impresa / Legale Rappresentante" non sono stati compilati i campi: ' + this.legaleRappresentanteErrorList.toString()));
    }
  }

  private verificaValiditaDistributore() {
    this.datiDomanda.responsabilitaRichieste.responsabilitaDistributore.forEach(
      element => {
        if (!element.distributori.length) {
          this.distributoreErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.DISTRIBUTORI'));
        }
        if (!element.responsabile && !this.distributoreErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.RESPONSABILE'))) {
          this.distributoreErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.RESPONSABILE'));
        }
        if (!element.allegato && !this.distributoreErrorList.includes(this.translateService.instant('RICHIESTE_ACCESSO.AUTORIZZAZIONE'))) {
          this.distributoreErrorList.push(this.translateService.instant('RICHIESTE_ACCESSO.AUTORIZZAZIONE'));
        }
        if (this.distributoreErrorList.length > 0) {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, 'Per la responabilità "Dipendente/Collaboratore Distributore" non sono stati compilati i campi: ' + this.distributoreErrorList.toString()));
        }
      }
    );
  }

  checkDatiAnagrafici(f: NgForm): boolean {
    let valid = true;
    let datiAnagraficiMancanti = '';

    this.controlliDatiAnagrafici.forEach(childNode => {
      if (!f.form.controls[childNode] || !f.form.controls[childNode].valid) {
        valid = false;
        datiAnagraficiMancanti = datiAnagraficiMancanti + childNode + ', ';
      }
    });

    if (!valid) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_DATI_ANAGRAFICI(datiAnagraficiMancanti.slice(0, -2))));
      return false;
    }

    console.log(JSON.stringify(this.datiAnagraficiUtente));

    return true;
  }

  checkProfiliRichiesti(f: NgForm): boolean {
    console.log('checkProfiliRichiesti');
    console.log(this.selectedApplicativi);
    if (!this.selectedApplicativi || this.selectedApplicativi.length === 0) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_SERVIZI));
      return false;
    }
    return true;
  }

  checkResponsabilita(): boolean {
    if (!this.componentsReferences || this.componentsReferences.length === 0) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_RESPONSABILITA));
      return false;
    }
    return true;
  }

  private popolaResponsabilita(): ResponsabilitaRichieste {
    const responsabilitaRichieste = new ResponsabilitaRichieste();
    this.componentsReferences.filter(component => component.instance.codeResponsabilita == CodiceResponsabilita.TI && component.instance.isValid).forEach((item, index) => {
      if (!responsabilitaRichieste.responsabilitaTitolare)
        responsabilitaRichieste.responsabilitaTitolare = new Array<ResponsabilitaTitolare>();
      responsabilitaRichieste.responsabilitaTitolare.push(new ResponsabilitaTitolare((item.instance as TitolareComponent).cuaa, (item.instance as TitolareComponent).denominazione));
    });

    this.componentsReferences.filter(component => component.instance.codeResponsabilita === CodiceResponsabilita.LR && component.instance.isValid).forEach((item, index) => {
      if (!responsabilitaRichieste.responsabilitaLegaleRappresentante)
        responsabilitaRichieste.responsabilitaLegaleRappresentante = new Array<ResponsabilitaTitolare>();
      (item.instance as LegaleComponent).elencoCariche
        .forEach(carica => {
          responsabilitaRichieste.responsabilitaLegaleRappresentante.push(new ResponsabilitaTitolare(carica.cuaa, carica.denominazione));
        });
    });

    console.log('Selected');
    this.componentsReferences.filter(component => component.instance.codeResponsabilita === CodiceResponsabilita.CAA).forEach((item, index) => {
      const comp = item.instance as CAAComponent;
      if (!responsabilitaRichieste.responsabilitaCaa) {
        responsabilitaRichieste.responsabilitaCaa = new Array<ResponsabilitaCaa>();
      }
      console.log(comp);
      responsabilitaRichieste.responsabilitaCaa.push(new ResponsabilitaCaa(comp.index, comp.responsabile, this.popolaSedi(comp.selectedNodes), comp.fileAutorizzazione));
      //comp.fileAutorizzazione = null;
    });

    this.componentsReferences.filter(component => component.instance.codeResponsabilita === CodiceResponsabilita.PAT).forEach((item, index) => {
      const comp = item.instance as PATComponent;
      if (!responsabilitaRichieste.responsabilitaPat) {
        responsabilitaRichieste.responsabilitaPat = new Array<ResponsabilitaPat>();
      }
      console.log(comp);
      responsabilitaRichieste.responsabilitaPat.push(new ResponsabilitaPat(comp.index, comp.matricola, comp.dirigente, comp.dipartimento, comp.fileAutorizzazione, comp.note));
      //comp.fileAutorizzazione = null;
    });

    this.componentsReferences.filter(component => component.instance.codeResponsabilita === CodiceResponsabilita.ALTRI).forEach((item, index) => {
      const comp = item.instance as AltriEntiComponent;
      if (!responsabilitaRichieste.responsabilitaAltriEnti) {
        responsabilitaRichieste.responsabilitaAltriEnti = new Array<ResponsabilitaAltriEnti>();
      }
      console.log(comp);
      responsabilitaRichieste.responsabilitaAltriEnti.push(new ResponsabilitaAltriEnti(comp.index, comp.denominazione, comp.piva, comp.dirigente, comp.fileAutorizzazione, comp.note));
      //comp.fileAutorizzazione = null;
    });

    this.componentsReferences.filter(component => component.instance.codeResponsabilita === CodiceResponsabilita.LPC).forEach((item, index) => {
      const comp = item.instance as ConsulenteComponent;
      if (!responsabilitaRichieste.responsabilitaConsulente) {
        responsabilitaRichieste.responsabilitaConsulente = new Array<ResponsabilitaConsulente>();
      }
      console.log(comp);
      responsabilitaRichieste.responsabilitaConsulente.push(new ResponsabilitaConsulente(comp.index, comp.ordine, comp.iscrizione, comp.cuaa, comp.denominazione, comp.rappresentante, comp.fileAutorizzazione));
      //comp.fileAutorizzazione = null;
    });

    this.componentsReferences.filter(component => component.instance.codeResponsabilita === CodiceResponsabilita.DIPENDENTE_DISTRIBUTORE).forEach((item, index) => {
      const comp = item.instance as DistributoreComponent;
      if (!responsabilitaRichieste.responsabilitaDistributore) {
        responsabilitaRichieste.responsabilitaDistributore = new Array<ResponsabilitaDistributore>();
      }
      responsabilitaRichieste.responsabilitaDistributore.push(new ResponsabilitaDistributore(comp.index, comp.responsabile, comp.selectedDistributors, comp.fileAutorizzazione));
    });

    return responsabilitaRichieste;
  }

  private rimuoviAllegati() {
    console.log('Selected');
    this.componentsReferences.filter(component => component.instance.codeResponsabilita === CodiceResponsabilita.CAA).forEach((item, index) => {
      const comp = item.instance as CAAComponent;
      comp.fileAutorizzazione = null;
      comp.icon = 'ui-icon-file-upload';
      comp.isValid = false;
      comp.uploadOk = false;
    });

    this.componentsReferences.filter(component => component.instance.codeResponsabilita === CodiceResponsabilita.PAT).forEach((item, index) => {
      const comp = item.instance as PATComponent;
      comp.fileAutorizzazione = null;
      comp.icon = 'ui-icon-file-upload';
      comp.isValid = false;
      comp.uploadOk = false;
    });

    this.componentsReferences.filter(component => component.instance.codeResponsabilita === CodiceResponsabilita.ALTRI).forEach((item, index) => {
      const comp = item.instance as AltriEntiComponent;
      comp.fileAutorizzazione = null;
      comp.icon = 'ui-icon-file-upload';
      comp.isValid = false;
      comp.uploadOk = false;
    });

    this.componentsReferences.filter(component => component.instance.codeResponsabilita === CodiceResponsabilita.LPC).forEach((item, index) => {
      const comp = item.instance as ConsulenteComponent;
      comp.fileAutorizzazione = null;
      comp.icon = 'ui-icon-file-upload';
      comp.isValid = false;
      comp.uploadOk = false;
    });

    this.componentsReferences.filter(component => component.instance.codeResponsabilita === CodiceResponsabilita.DIPENDENTE_DISTRIBUTORE).forEach((item, index) => {
      const comp = item.instance as DistributoreComponent;
      comp.fileAutorizzazione = null;
      comp.icon = 'ui-icon-file-upload';
      comp.uploadOk = false;
    });
  }

  private popolaApplicativi(): string[] {
    const applicativi = [];
    console.log(this.selectedApplicativi);
    this.selectedApplicativi.forEach(element => {
      applicativi.push(element.label);
    });
    return applicativi;
  }

  private popolaSedi(selectedSedi: TreeNode[]): Array<EnteCaa> {
    const sedi = new Array<EnteCaa>();

    if (selectedSedi) {
      selectedSedi.forEach((item, index) => {

        if (item.data) {
          sedi.push(item.data as EnteCaa);
        }
      });
    }
    return sedi;
  }

  inserisci() {
    console.log('Inzio POST');
    this.utentiService.inserisciDomanda(this.datiDomanda)
      .subscribe(
        idDomanda => {
          console.log('Inserimento della domanda ' + idDomanda + ' avvenuta con successo'), this.modificaStatoRichiesta(),
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK)),
            this.datiDomanda.id = idDomanda,
            this.ricaricaDati();
          this.saveOk = true;
        },
        error => {
          console.error('Errore in inserisciDomanda: ' + error),
            A4gMessages.handleError(this.messageService, error, A4gMessages.SALVATAGGIO_RICHIESTA_UTENTE);
        }
      );
  }


  aggiorna() {
    console.log('Inzio PUT');
    this.utentiService.aggiornaDomanda(this.datiDomanda)
      .subscribe(
        idDomanda => {
          console.log('Aggiornamento della domanda ' + idDomanda + ' avvenuta con successo'), this.modificaStatoRichiesta(),
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK)),
            this.datiDomanda.id = idDomanda,
            this.ricaricaDati();
          this.saveOk = true;
        },
        error => {
          console.error('Errore in aggiornaDomanda: ' + error),
            A4gMessages.handleError(this.messageService, error, A4gMessages.SALVATAGGIO_RICHIESTA_UTENTE);
        }
      );
  }

  ricaricaDati() {

  }

  private blobToFile = (theBlob: Blob, fileName: string): File => {
    const b: any = theBlob;
    // A Blob() is almost a File() - it's just missing the two properties below which we will add
    b.lastModifiedDate = new Date();
    b.name = fileName;
    // Cast to a File() type
    return <File>theBlob;
  }

  downloadInformativaPrivacy(): any {
    this.http.get(this._configuration.UrlGetInfoPrivacy, {
      responseType: 'blob'
    }).subscribe(response => {
      FileSaver.saveAs(response, 'informativa_privacy.pdf');
      this.viewPrivacy = true;
      this.informativaPrivacyFile = this.blobToFile(response, 'informativa_privacy.pdf');
    });
  }

  sottoScriviPrivacyCheck(e: any) {
    console.log('Evento sottoscrivi privacy', e.checked);
    if (e.checked) {
      this.sottoScriviPrivacy();
    }
  }

  sottoScriviPrivacy(): any {
    this.utentiService.sottoScriviPrivacy(this.informativaPrivacyFile).subscribe(
      (response: Firma) => {
        this.firmaPrivacy = response;
        console.log('Firmata privacy con xml', this.firmaPrivacy.xml);
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK))
        //
        const byteCharacters = atob(this.firmaPrivacy.pdf);
        const byteNumbers = new Array(byteCharacters.length);
        for (let i = 0; i < byteCharacters.length; i++) {
          byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);
        const blob = new Blob([byteArray], { type: 'application/pdf' });
        console.log(blob);

        //utilizzato per l'allegato firma pdf
        this.informativaPrivacyFirmataPDF = this.blobToFile(blob, 'informativa_privacy_firmata.pdf');

        FileSaver.saveAs(blob, 'informativa_privacy_firmata.pdf');

        //
        this.blockCheckPrivacyOk = true;
      },
      error => {
        console.error('Errore in aggiornaDomanda: ' + error);
        A4gMessages.handleError(this.messageService, error, A4gMessages.FIRMA_PRIVACY);
        this.checked1 = false;
      }
    );
  }

  downloadModuloRichiestaAccesso(): any {
    console.log(this.datiDomanda);
    if (this.saveOk) {
      this.domandaPDF().subscribe(
        (stampa) => {
          this.viewModuloDomanda = true;
        },
        (err) => {
          console.log('Errore stampando il modulo', err);
          A4gMessages.handleError(this.messageService, err, A4gMessages.STAMPA_RICHIESTA_UTENTE);
        }
      );
    }
  }

  sottoScriviDomandaCheck(e: any) {
    console.log('Evento sottoscrivi domanda', e.checked);
    if (e.checked) {
      this.sottoScriviDomanda();
    }
  }

  sottoScriviDomanda(): any {
    this.utentiService.sottoScriviDomanda(this.datiDomanda.id).subscribe(
      (response: any) => {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK))
      },
      error => {
        console.error('Errore in aggiornaDomanda: ' + error);
        A4gMessages.handleError(this.messageService, error, A4gMessages.FIRMA_RICHIESTA_UTENTE);
        this.checked2 = false;
      }
    );
  }

  domandaPDF(): Observable<Blob> {
    return this.utentiService.getDomanda(this.datiDomanda.id, this.datiDomanda.datiAnagrafici.codiceFiscale);
  }

  startProtocollazioneAsincrona() {
    this.canProtocol = false;
    // this.clearWarnStartProtocollazione();
    if (this.hasPrivacyProtocollata) {
      //privacy già protocollata, protocollare solo la richiesta
      this.utentiService.protocollaDomanda(this.datiDomanda.id).subscribe(
        () => {
          this.messages.add(
            A4gMessages.getToast(
              'tst-protocolla',
              A4gSeverityMessage.success,
              this.translateService.instant('RICHIESTE_ACCESSO.protocollazioneInCarico')
            )
          );
        },
        error => {
          this.messages.add(
            A4gMessages.getToast(
              'tst',
              A4gSeverityMessage.error,
              A4gMessages.PROTOCOLLA_INFORMATIVA_ANTIMAFIA_ERROR
            )
          );
          this.canProtocol = true;
        });
    } else {
      //costruzione documentazione privacy da protocollare
      const info = this.getDocumentazioneInformativaPrivacy();
      const allegati: any[] = new Array<any>();
      allegati.push(this.informativaPrivacyFirmataPDF);
      allegati.push(this.getInfoPrivacyFirmataXML());

      //controllo sull'esistenza della persona da fare prima di effettuare la protocollazione privacy
      if (A4gMessages.isUndefinedOrNull(this.persona) || A4gMessages.isUndefinedOrNull(this.persona.id)) {
        console.log('Persona non esistente; fornisco i dati per la creazione');
        this.persona = new Persona();
        this.persona.codiceFiscale = this.datiAnagraficiUtente.codiceFiscale;
        this.persona.nome = this.datiAnagraficiUtente.nome;
        this.persona.cognome = this.datiAnagraficiUtente.cognome;
      }

      // protocollazione dati domanda di accesso
      this.utentiService.protocollaDomanda(this.datiDomanda.id).pipe(
        concatMap((result) => {
          return this.utentiService.protocollaInformativaPrivacyDichiarazioneAntimafia(
            this.persona.nome, this.persona.cognome,
            this.persona.codiceFiscale,
            info, this.informativaPrivacyFile, allegati);
        })
      ).subscribe(
        resp => {
          this.messages.add(
            A4gMessages.getToast(
              'tst-protocolla',
              A4gSeverityMessage.success,
              this.translateService.instant('RICHIESTE_ACCESSO.protocollazioneInCarico'))
          );
        }, error => {
          this.messages.add(
            A4gMessages.getToast(
              'tst',
              A4gSeverityMessage.error,
              A4gMessages.PROTOCOLLA_DOMANDA_ACCESSO_ERROR));
        }
      );
    }
  }

  // costruzione della documentazione per l'informativa privacy da protocollare
  private getDocumentazioneInformativaPrivacy() {
    const oggetto: string =
      this._configuration.ProtocollaPrivacyOggetto +
      ' - ' +
      this.datiAnagraficiUtente.nome +
      ' ' +
      this.datiAnagraficiUtente.cognome +
      ' - ' +
      this.datiAnagraficiUtente.codiceFiscale;
    const tipologiaDocumento: string = this._configuration.tipologiaDocumentoProtocolloPrivacy;
    const nomeFile = 'InformativaGeneralePrivacy.pdf';
    const mittente = {
      name: this.datiAnagraficiUtente.nome,
      surname: this.datiAnagraficiUtente.cognome,
      email: A4gMessages.isUndefinedOrNull(this.datiAnagraficiUtente.email)
        ? null
        : this.datiAnagraficiUtente.email,
      nationalIdentificationNumber: this.datiAnagraficiUtente.codiceFiscale,
      description:
        this.datiAnagraficiUtente.nome +
        ' ' +
        this.datiAnagraficiUtente.cognome +
        ' - ' +
        this.datiAnagraficiUtente.codiceFiscale
    };
    const documentazionePrivacy = {
      mittente: mittente,
      oggetto: oggetto,
      tipologiaDocumentoPrincipale: tipologiaDocumento
    };


    return JSON.stringify(documentazionePrivacy);
  }

  onCloseMessageProtocolla() {
    //redirect alla home
    window.location.href = this.configuration.IndexPage;
  }
  // commentata perche' la protocollazione e' asincrona
  // private clearWarnStartProtocollazione() {
  //   this.messages.clear('warn-start-protocollazione');
  // }

  //costruscie un BLOB a partire da una stringa "xml" che poi verrà convertito in un tipo FILE da passare come allegato al servizio di protocollazione
  private getInfoPrivacyFirmataXML(): File {

    //Nella maggior parte dei browser la funzione atob(utilizzata in questo caso per trasformare la firma XML in BLOB) 
    //causa un'eccezione Character Out Of Range dovuta a dei caratteri che superano l'intervallo di un byte a 8 bit(0x00 ~0xFF).
    //Per ovviare a questo problema chiamato "problema Unicode" e poiché i DOMStrings sono stringhe codificate a 16 bit, 
    //viene fatto un replace con una particolare REGEXP sulla stringa che rappresenta l'XML per poi trasformarla in UTF-8 ed infine codificarla.
    const byteCharactersXml = atob(btoa(encodeURIComponent(this.firmaPrivacy.xml).replace(/%([0-9A-F]{2})/g, (match, p1) => {
      return String.fromCharCode(parseInt(p1, 16));
    })));
    const byteNumbersXml = new Array(byteCharactersXml.length);
    for (let i = 0; i < byteCharactersXml.length; i++) {
      byteNumbersXml[i] = byteCharactersXml.charCodeAt(i);
    }
    const byteArrayXml = new Uint8Array(byteNumbersXml);
    const blobXml = new Blob([byteArrayXml], { type: 'application/xml' });
    console.log(blobXml);

    return this.blobToFile(blobXml, 'informativa_privacy_firmata.xml');
  }

}
