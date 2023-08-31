import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Labels} from '../../../app.labels';
import {MessageService, TreeNode} from 'primeng/api';
import {UtentiService} from '../../../utenti/utenti.service';
import {A4gMessages, A4gSeverityMessage} from '../../../a4g-common/a4g-messages';
import {forkJoin, Subject} from 'rxjs';
import {IstruttoriaProfiloUtenteSenzaDomanda} from '../../../a4g-common/classi/istruttoria-profilo-utente';
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
import {EnteTreeNode} from '../../../a4g-common/classi/EntiSediCaaPrimeNgTree';
import {EnteCaa} from 'src/app/a4g-common/classi/EnteSedeCaa';
import {GestioneUtenzeService} from '../../gestione-utenze/gestione-utenze.service';
import {catchError, switchMap, takeUntil} from 'rxjs/operators';
import {DatiUtente, Profilo} from '../dto/Profili';
import {TranslateService} from '@ngx-translate/core';

class ProfiliPresentation {
  responsabilita: string;
  profili: Profilo[];
}

@Component({
  selector: 'modifica-configurazione-utente-dialog',
  templateUrl: './modifica-configurazione-utente-dialog.component.html',
  styleUrls: ['./modifica-configurazione-utente-dialog.component.scss']
})
export class ModificaConfigurazioneUtenteDialogComponent implements OnInit, OnDestroy {

  public display: boolean = false;
  @Output() displayChange = new EventEmitter();

  private entiCaaTreeNode: EnteTreeNode[];
  private componentDestroyed$: Subject<boolean> = new Subject();
  private isDisplayMotivazioneDisattivazione = false;

  public datiUtente: DatiUtente;
  public labels = Labels;
  public selectedSediNodes: TreeNode[];
  public responsabilitaPresentationSingleProfileNoRuoli: ProfiliPresentation[];
  public responsabilitaPresentationSingleProfile: ProfiliPresentation[];
  public responsabilitaPresentationMultiProfile: ProfiliPresentation[];
  public profiloUtenteFormGroup: FormGroup;

  constructor(
    private gestioneUtenzeService: GestioneUtenzeService,
    private utentiService: UtentiService,
    private messageService: MessageService,
    private translateService: TranslateService) {
      let profiloUtenteFormControls = {
        'variazioniRichiesta': new FormControl('', Validators.required),
        'disabled-MultiProfileInput': new FormControl({
          value: false, disabled: false
        })
      };
      this.profiloUtenteFormGroup = new FormGroup(profiloUtenteFormControls);
  }

  private handleUfficiCaaOB(uffici: EnteCaa[]): void {
    this.entiCaaTreeNode = EnteTreeNode.getTreeNodes(uffici);
  }

  private handleProfiliOB(profili: Profilo[]): void {
    let profiliHm = new Map<string, Profilo[]>();
    for (let profilo of profili) {
      if (profiliHm.has(profilo.responsabilita)) {
        profiliHm.get(profilo.responsabilita).push(profilo);
      } else {
        profiliHm.set(profilo.responsabilita, [profilo]);
      }
    }
    this.responsabilitaPresentationSingleProfileNoRuoli = [];
    this.responsabilitaPresentationSingleProfile = [];
    this.responsabilitaPresentationMultiProfile = [];
    for (let key of Array.from(profiliHm.keys()).sort()) {
      let pp: ProfiliPresentation = new ProfiliPresentation();
      let profili: Profilo[] = profiliHm.get(key).sort();
      pp.responsabilita = key;
      pp.profili = profili;

      if (profili.length === 1) {
        if (profili[0].haRuoli) {
          this.responsabilitaPresentationSingleProfile.push(pp);
        } else {
          this.responsabilitaPresentationSingleProfileNoRuoli.push(pp);
        }
      } else {
        this.responsabilitaPresentationMultiProfile.push(pp);
      }
    }
    this.responsabilitaPresentationSingleProfileNoRuoli.sort((a, b) => (a.responsabilita > b.responsabilita) ? 1 : -1);
    this.responsabilitaPresentationSingleProfile.sort((a, b) => (a.profili[0] > b.profili[0]) ? 1 : -1);
    this.responsabilitaPresentationMultiProfile.sort((a, b) => (a.responsabilita > b.responsabilita) ? 1 : -1);

    for (let pp of this.responsabilitaPresentationSingleProfile) {
      let profilo: Profilo = pp.profili[0];
      this.profiloUtenteFormGroup.controls[profilo.id] = new FormControl(false);
      this.profiloUtenteFormGroup.controls['disabled-' + profilo.id] = new FormControl({
        value: false, disabled: true
      });
    }
    for (let pp of this.responsabilitaPresentationMultiProfile) {
      pp.profili.forEach(p => {
        this.profiloUtenteFormGroup.controls[p.id] = new FormControl(false);
        this.profiloUtenteFormGroup.controls['disabled-' + p.id] = new FormControl({
          value: false, disabled: true
        });
      });
    }
  }

  private isExistDisabledProfiles(): boolean {
    let isExistDisabledProfiles = false;
    for (let key in this.profiloUtenteFormGroup.controls) {
      if (key.startsWith("disabled-") && this.profiloUtenteFormGroup.controls[key].value) {
        console.log("found enabled 'disable button' for input: " + key);
        isExistDisabledProfiles = true;
        break;
      }
    }
    return isExistDisabledProfiles;
  }

  public disableInput(input: Profilo, toDisable: boolean) {
    if (toDisable) {
      // console.log("disable input: " + input.id);
      this.profiloUtenteFormGroup.controls[input.id].disable();
      this.profiloUtenteFormGroup.controls['disabled-' + input.id].setValue(true);
      this.profiloUtenteFormGroup.controls['disabled-' + input.id].enable();
      if (!this.isDisplayMotivazioneDisattivazione) {
        this.isDisplayMotivazioneDisattivazione = true;
        // console.log('ADD CONTROL motivazioneDisattivazione');
        this.profiloUtenteFormGroup.addControl(
          'motivazioneDisattivazione',
          new FormControl(null, Validators.required));
      }
    } else {
      // console.log("enable input: " + input.id);
      this.profiloUtenteFormGroup.controls[input.id].enable();
      this.profiloUtenteFormGroup.controls['disabled-' + input.id].setValue(false);
      this.profiloUtenteFormGroup.controls['disabled-' + input.id].enable();
      if (!this.isExistDisabledProfiles()) {
        this.isDisplayMotivazioneDisattivazione = false;
        // console.log('REMOVE CONTROL motivazioneDisattivazione');
        this.profiloUtenteFormGroup.removeControl('motivazioneDisattivazione');
      }
    }
  }

  public disableInputList(inputList: Profilo[], toDisable: boolean, groupByDisableControlName: string) {
    if (groupByDisableControlName) {
      this.profiloUtenteFormGroup.controls[groupByDisableControlName].setValue(toDisable);
      if (toDisable) {
        this.profiloUtenteFormGroup.controls[groupByDisableControlName].enable();
      }
    }
    let anyActiveProfile = false;
    for (let input of inputList) {
      if (groupByDisableControlName && !anyActiveProfile && this.profiloUtenteFormGroup.controls[input.id].value) {
        this.profiloUtenteFormGroup.controls[groupByDisableControlName].enable();
        anyActiveProfile = true;
      }
      this.disableInput(input, toDisable);
    }
    if (!anyActiveProfile) {
      this.profiloUtenteFormGroup.controls[groupByDisableControlName].disable();
    }
  }

  public enableDisableProfileInput(inputName: string, toEnable: boolean) {
    if (toEnable) {
      this.profiloUtenteFormGroup.controls["disabled-" + inputName].enable();
    } else {
      this.profiloUtenteFormGroup.controls["disabled-" + inputName].disable();
    }
  }

  public enableDisableProfileInputByControlsList(inputName: string, toDisableList: any[], toEnable: boolean) {
    if (toEnable) {
      this.profiloUtenteFormGroup.controls["disabled-" + inputName].enable();
    } else {
      this.profiloUtenteFormGroup.controls["disabled-" + inputName].disable();
    }
    let anyActiveProfile: boolean = toDisableList.some(c => {
      let formControl: AbstractControl = this.profiloUtenteFormGroup.controls[c.id];
      return formControl.value;
    });
    if (!anyActiveProfile) {
      this.profiloUtenteFormGroup.controls["disabled-MultiProfileInput"].disable();
    } else {
      this.profiloUtenteFormGroup.controls["disabled-MultiProfileInput"].enable();
    }
  }

  private getProfiloCaaFormControl(): Profilo {
    let profiloCaaControlFound: Profilo[] = this.responsabilitaPresentationSingleProfile.map(
      pp => pp.profili).find(prof => !!prof.find(p => p.identificativo === "caa"));
      if (profiloCaaControlFound && profiloCaaControlFound.length > 0) {
        return profiloCaaControlFound[0];
      }
      return null;
  }

  private popolaDatiDomanda(): void {
    this.profiloUtenteFormGroup.reset();
    // let sediAssegnate = this.datiUtente.sedi;
    /*
    let multiProfileControls: Profilo[] = undefined;
    for (let selectedIdProfilo of this.datiUtente.profili) {
      let profiloUtenteFormControl = this.profiloUtenteFormGroup.controls[selectedIdProfilo.id];
      if (profiloUtenteFormControl) {
        profiloUtenteFormControl.setValue(true);
        this.disableInput(selectedIdProfilo, selectedIdProfilo.disabled);
        if (selectedIdProfilo.identificativo === 'caa') {
          // preseleziona sedi
          // this.selectedSediNodes = EnteTreeNode.selectEntiSedi(
          //  this.entiCaaTreeNode, sediAssegnate.map(sede => sede.id));
          // preselezeziona utente caa
          let profileAssigned: boolean = this.selectedSediNodes.length > 0;
          this.profiloUtenteFormGroup.controls[selectedIdProfilo.id].setValue(profileAssigned);
        } 
        else {
          // profiloUtente.disable();
          let isMultiProfile = this.responsabilitaPresentationMultiProfile.some(
            resp => resp.profili.some(prof => prof.id === selectedIdProfilo.id));
          if (isMultiProfile) {
            if (multiProfileControls === undefined) {
              multiProfileControls = [];
              this.responsabilitaPresentationMultiProfile.forEach(resp => resp.profili.forEach(
                prof => {
                  let userProf = this.datiUtente.profili.find(p => p.id === prof.id);
                  if (userProf) {
                    multiProfileControls.push(userProf);
                  }
                }));
            }
            if (selectedIdProfilo.disabled) {
              this.disableInputList(multiProfileControls, true, 'disabled-MultiProfileInput');
            }
          }
        }
      } else {
        console.log("[WARN] id controllo mancante: " + selectedIdProfilo.id)
      }
    }
    if (this.profiloUtenteFormGroup.controls['motivazioneDisattivazione']) {
      this.profiloUtenteFormGroup.controls['motivazioneDisattivazione'].setValue(this.datiUtente.motivazioneDisattivazione);
    }
    */
  }

  public isDisplayMotivation(): boolean {
    let motDisatControl = this.profiloUtenteFormGroup.controls['motivazioneDisattivazione'];
    return motDisatControl && this.isDisplayMotivazioneDisattivazione;
  }

  ngOnInit() {
  }

  public onClose() {
    this.display = false;
    this.displayChange.emit(false);
  }

  public onOpenOnUserID(identificativo: string) {
    this.gestioneUtenzeService.getDatiUtenteByCodiceFiscale(identificativo).pipe(
      switchMap((resp) => {
        this.datiUtente = resp;
        return forkJoin([
          this.utentiService.getUfficiCaa().pipe(
            catchError(e => {
              return undefined;
            })),
          this.utentiService.getProfiliUtente().pipe(
            catchError(e => {
              return undefined;
            }))
        ]);
      }),
      takeUntil(this.componentDestroyed$)
    ).subscribe(resp => {
      this.handleUfficiCaaOB(<EnteCaa[]>resp[0]);
      this.handleProfiliOB(<Profilo[]>resp[1]);
      this.popolaDatiDomanda();
      this.display = true;
      this.displayChange.emit(true);
    });
  }

  private isProfileSelected(profileId: number): boolean {
    return this.profiloUtenteFormGroup.controls[profileId].value;
  }

  private isProfileDisabled(profileId: number): boolean {
    return this.profiloUtenteFormGroup.controls['disabled-' + profileId].value;
  }
  

  public onSave(): void {
    if (!this.datiUtente.id) {
      this.messageService.add(A4gMessages.getToast(
        'tst', A4gSeverityMessage.error, A4gMessages.changeStatusKo));
      return;
    }
    if (!this.profiloUtenteFormGroup.valid) {
      this.messageService.add(A4gMessages.getToast(
        'tst', A4gSeverityMessage.warn, A4gMessages.inputRequiredInvalid));
      return;
    }
    let profiloCaa = this.getProfiloCaaFormControl();
    if (profiloCaa && this.profiloUtenteFormGroup.controls[profiloCaa.id]
      && this.profiloUtenteFormGroup.controls[profiloCaa.id].value
      && this.selectedSediNodes.length === 0) {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.warn,
          this.translateService.instant("RICHIESTE_ACCESSO.seProfiloCaaAlmeno1Sede")));
        return;
    }
    let istruttoriaProfiloUtente = new IstruttoriaProfiloUtenteSenzaDomanda();
    istruttoriaProfiloUtente.profili = [];
    istruttoriaProfiloUtente.profiliDaDisattivare = [];
    istruttoriaProfiloUtente.sedi = [];
    let allResponsabilitaPresentationProfile = this.responsabilitaPresentationMultiProfile.concat(
      this.responsabilitaPresentationSingleProfile);
    for (let pp of allResponsabilitaPresentationProfile) {
      pp.profili.forEach(p => {
        let id = p.id;
        if (this.isProfileSelected(id)) {
          if (this.isProfileDisabled(id)) {
            istruttoriaProfiloUtente.profiliDaDisattivare.push(id);
          } else {
            istruttoriaProfiloUtente.profili.push(id);
          }
        }
      });
    }
    if (this.selectedSediNodes) {
      this.selectedSediNodes.forEach((item, index) => {
        if (item.data) {
          istruttoriaProfiloUtente.sedi.push(item.data.id);
        }
      });
    }
    istruttoriaProfiloUtente.variazioneRichiesta = this.profiloUtenteFormGroup.controls['variazioniRichiesta'].value;
    if (this.profiloUtenteFormGroup.controls['motivazioneDisattivazione']) {
      istruttoriaProfiloUtente.motivazioneDisattivazione = this.profiloUtenteFormGroup.controls['motivazioneDisattivazione'].value;
    } else {
      istruttoriaProfiloUtente.motivazioneDisattivazione = null;
    }
    this.utentiService.postIstruttoriaProfiloUtenteByUtente(this.datiUtente.id, istruttoriaProfiloUtente)
      .subscribe(result => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        this.onClose();
      }, err => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, A4gMessages.changeStatusKo));
      });
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }
}
