import { Component, OnInit, Output, EventEmitter, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatiDomanda } from '../../../utenti/classi/DatiDomanda';
import { Labels } from '../../../app.labels';
import { MessageService, TreeNode } from 'primeng/api';
import { UtentiService } from '../../../utenti/utenti.service';
import { A4gMessages, A4gSeverityMessage } from '../../../a4g-common/a4g-messages';
import { takeUntil, switchMap, catchError } from 'rxjs/operators';
import { forkJoin, empty, of, Subject } from 'rxjs';
import { IstruttoriaProfiloUtente } from '../../../a4g-common/classi/istruttoria-profilo-utente';
import { GestioneUtenzeService } from '../gestione-utenze.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { EnteTreeNode } from '../../../a4g-common/classi/EntiSediCaaPrimeNgTree';
import { EnteCaa } from 'src/app/a4g-common/classi/EnteSedeCaa';
import { DatiAnagrafici } from 'src/app/utenti/classi/datiAnagrafici';
import { Profilo } from '../../ricerca-utenti/dto/Profili';
import { TranslateService } from '@ngx-translate/core';

class ProfiliPresentation {
  responsabilita: string;
  profili: Profilo[];
}

@Component({
  selector: 'assegnazione-configurazioni-dialog',
  templateUrl: './assegnazione-configurazioni-dialog.component.html',
  styleUrls: ['./assegnazione-configurazioni-dialog.component.scss']
})
export class AssegnazioneConfigurazioniDialogComponent implements OnInit, OnDestroy {
  public display: boolean = false;
 
  @Output() displayChange = new EventEmitter();

  private entiCaaTreeNode: EnteTreeNode[];
  private componentDestroyed$: Subject<boolean> = new Subject();
  private istruttoriaProfiloUtente: IstruttoriaProfiloUtente = new IstruttoriaProfiloUtente();

  public datiDomanda: DatiDomanda;
  public labels = Labels;
  public displayAssegnazioneConfigurazioneDialog: boolean = false;
  public selectedNodes: TreeNode[];
  public responsabilitaPresentationSingleProfileNoRuoli: ProfiliPresentation[];
  public responsabilitaPresentationSingleProfile: ProfiliPresentation[];
  public responsabilitaPresentationMultiProfile: ProfiliPresentation[];
  public profiloUtenteFormGroup: FormGroup;
  public profiliSrt: string[];
  public profiliAgs: string[];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private gestioneUtenzeService: GestioneUtenzeService,
    private utentiService: UtentiService,
    private messageService: MessageService,
    private translateService: TranslateService) {
      this.profiloUtenteFormGroup = new FormGroup({});
      this.profiloUtenteFormGroup.controls['variazioniRichiesta'] = new FormControl('', Validators.required);
  }

  private handleUfficiCaaOB(uffici: EnteCaa[]): void {
    this.entiCaaTreeNode = EnteTreeNode.getTreeNodes(uffici);
  }

  private handleProfiliOB(profili: Profilo[]): void {
    let profiliHm = new Map<string, Profilo[]>();
    for (let profilo of profili) {
      if (profiliHm.has(profilo.responsabilita)) {
        // let p: Profilo = new Profilo(profilo.id, profilo.identificativo, profilo.descrizione, profilo.responsabilita, profilo.haRuoli);
        profiliHm.get(profilo.responsabilita).push(profilo);
      } else {
        // let p: Ruolo = new Ruolo(profilo.id, profilo.identificativo, profilo.descrizione);
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
      let formControl = new FormControl(false);
      if (profilo.identificativo === "azienda" && !this.hasCuaa(this.datiDomanda)) {
        formControl.disable();
      }
      this.profiloUtenteFormGroup.controls[profilo.id] = formControl;
    }
    for (let pp of this.responsabilitaPresentationMultiProfile) {
      pp.profili.forEach(p => this.profiloUtenteFormGroup.controls[p.id] = new FormControl(false));
    }
  }

  private handleIstruttoriaOB(respIstruttoriaProfiloUtente: IstruttoriaProfiloUtente): void {
    Object.assign(this.istruttoriaProfiloUtente, respIstruttoriaProfiloUtente);
    this.selectedNodes = EnteTreeNode.selectEntiSedi(this.entiCaaTreeNode, this.istruttoriaProfiloUtente.sedi);
    this.profiloUtenteFormGroup.controls['variazioniRichiesta'].setValue(this.istruttoriaProfiloUtente.variazioneRichiesta);
    for (let selectedIdProfilo of this.istruttoriaProfiloUtente.profili) {
      this.profiloUtenteFormGroup.controls[selectedIdProfilo].setValue(true);
    }
  }

  private hasCuaa(datiDomanda: DatiDomanda): boolean {
    let hasCuaa: boolean = !!(
      datiDomanda.responsabilitaRichieste.responsabilitaLegaleRappresentante && (
        datiDomanda.responsabilitaRichieste.responsabilitaLegaleRappresentante.some(e => !!e.cuaa))
      || datiDomanda.responsabilitaRichieste.responsabilitaTitolare && (
        datiDomanda.responsabilitaRichieste.responsabilitaTitolare.some(e => !!e.cuaa)
      ));
    return hasCuaa;
  }

  private popolaDatiDomanda(): void {
    let respRichieste = this.datiDomanda.responsabilitaRichieste;
    let hasRichiestaResponsabilitaLegale: boolean = this.hasCuaa(this.datiDomanda);
    const hasDistributors: boolean = this.hasDistributors(this.datiDomanda);
    for (let pp of this.responsabilitaPresentationSingleProfile) {
      if (pp.profili[0].identificativo === 'azienda'
          && hasRichiestaResponsabilitaLegale) {
        this.profiloUtenteFormGroup.controls[pp.profili[0].id].setValue(true);
      } else {
        this.profiloUtenteFormGroup.controls[pp.profili[0].id].setValue(false);
      }
      if(pp.profili[0].identificativo === 'operatore_distributore' && hasDistributors)
        this.profiloUtenteFormGroup.controls[pp.profili[0].id].setValue(true);
    }
    if (respRichieste.responsabilitaCaa) {
      for (let rt of respRichieste.responsabilitaCaa) {
        // preseleziona sedi
        this.selectedNodes = EnteTreeNode.selectEntiSedi(this.entiCaaTreeNode, rt.sedi.map(sede => sede.id));
        // preselezeziona utente caa
        let profiloCaa = this.getProfiloCaaFormControl();
        if (profiloCaa) {
          this.profiloUtenteFormGroup.controls[profiloCaa.id].setValue(this.selectedNodes.length > 0);
        }
      }
    }
  }

  hasDistributors(datiDomanda: DatiDomanda): boolean {
    try { return datiDomanda.responsabilitaRichieste.responsabilitaDistributore[0].distributori.length > 0; }
    catch(e) { return false; }
  }
  private updateProfiliSrt(dati: DatiAnagrafici) {
    forkJoin([
      this.utentiService.getProfiliSrt(dati).pipe(
        catchError(e => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDatiSrt));
          return empty();
        }))
    ]).pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(result => {
      this.getProfiliSrt(result[0]);
    }, err => {
      console.log(err);
    });
  }

  getProfiliSrt(result) {
    this.profiliSrt = [];
    if (result) {
      result.forEach(
        element => {
          this.profiliSrt.push(element.ruolo);
        });
      console.log('Profili SRT:');
      console.log(this.profiliSrt);
    }
  }

  private updateProfiliAgs(dati: DatiAnagrafici) {
    forkJoin([
      this.utentiService.getProfiliAgs(dati).pipe(
        catchError(e => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDatiAgs));
          return empty();
        }))
    ]).pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(result => {
      this.getProfiliAgs(result[0]);
    }, err => {
      console.log(err);
    });
  }

  getProfiliAgs(result) {
    this.profiliAgs = [];
    if (result) {
      result.forEach(
        element => {
          this.profiliAgs.push(element.utenza);
        });
    }
  }

  ngOnInit() {
    this.selectedNodes = [];
    this.route.params.pipe(
      switchMap((params) => {
        let idUtenza: Number = params['idUtenza'];
        return this.gestioneUtenzeService.getDatiAnagraficiUtente(idUtenza);
      }),
      switchMap((datiDomanda) => {
        this.datiDomanda = datiDomanda;
        console.log("loading from services");
        return forkJoin([
          this.utentiService.getUfficiCaa().pipe(
            catchError(error => {
              A4gMessages.handleError(this.messageService, error, A4gMessages.erroreRecuperoDati);
              return undefined;
            })),
          this.utentiService.getProfiliUtente().pipe(
            catchError(error => {
              A4gMessages.handleError(this.messageService, error, A4gMessages.erroreRecuperoDati);
              return undefined;
            })),
          this.gestioneUtenzeService.getIstruttoriaProfiloByDomanda(datiDomanda.id).pipe(
            catchError(e => {
              this.istruttoriaProfiloUtente.idDomanda = this.datiDomanda.id;
              this.selectedNodes = [];
              return of(null);
            })
          )]);
      }),
      switchMap((resp) => {
        this.handleUfficiCaaOB(<EnteCaa[]>resp[0]);
        this.handleProfiliOB(<Profilo[]>resp[1]);
        if (resp[2]) {
          this.handleIstruttoriaOB(resp[2]);
        } else {
          this.popolaDatiDomanda();
        }
        return this.route.queryParams;
      }),
      takeUntil(this.componentDestroyed$)
    ).subscribe(
      (queryParams) => {
        if (queryParams['dialog'] === '1') {
          this.open();
        }
      }, error => {
        A4gMessages.handleError(this.messageService, error, A4gMessages.erroreRecuperoDati);
        return this.route.queryParams;
      }
    );
  }

  public hide() {
    this.display = false;
  }

  public onHide() {
    this.displayChange.emit(false);
  }

  public onShow() {
    this.displayChange.emit(true);
  }

  public open() {
    this.display = true;
    this.updateProfiliSrt(this.datiDomanda.datiAnagrafici);
    this.updateProfiliAgs(this.datiDomanda.datiAnagrafici);
  }

  private getProfiloCaaFormControl(): Profilo {
    let profiloCaaControlFound: Profilo[] = this.responsabilitaPresentationSingleProfile.map(
      pp => pp.profili).find(prof => !!prof.find(p => p.identificativo === "caa"));
      if (profiloCaaControlFound && profiloCaaControlFound.length > 0) {
        return profiloCaaControlFound[0];
      }
      return null;
  }

  public onSave(): void {
    this.profiloUtenteFormGroup.updateValueAndValidity();
    if (!this.profiloUtenteFormGroup.valid) {
      this.messageService.add(A4gMessages.getToast(
        'tst', A4gSeverityMessage.warn, A4gMessages.inputRequiredInvalid));
      return;
    }
    let profiloCaa = this.getProfiloCaaFormControl();
    if (profiloCaa && this.profiloUtenteFormGroup.controls[profiloCaa.id]
      && this.profiloUtenteFormGroup.controls[profiloCaa.id].value
      && this.selectedNodes.length === 0) {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.warn, this.translateService.instant("RICHIESTE_ACCESSO.seProfiloCaaAlmeno1Sede")));
        return;
    }
    this.istruttoriaProfiloUtente.sedi = [];
    this.selectedNodes.forEach((item, index) => {
      if (item.data) {
        this.istruttoriaProfiloUtente.sedi.push(item.data.id);
      }
    });

    this.istruttoriaProfiloUtente.profili = [];
    for (let pp of this.responsabilitaPresentationMultiProfile.concat(this.responsabilitaPresentationSingleProfile)) {
      pp.profili.forEach(p => {
        let id = p.id;
        if (this.profiloUtenteFormGroup.controls[id].value) {
          this.istruttoriaProfiloUtente.profili.push(id);
        }
      });
    }    
    this.istruttoriaProfiloUtente.variazioneRichiesta = this.profiloUtenteFormGroup.controls['variazioniRichiesta'].value;
    if (this.istruttoriaProfiloUtente.id) {
      this.utentiService.putIstruttoriaProfilo(this.istruttoriaProfiloUtente).subscribe(result => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        this.router.navigate(['./'], { relativeTo: this.route.parent, queryParams: { tabselected: 'inlavorazione' } });
      }, err => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, A4gMessages.changeStatusKo));
      });
    } else {
      this.istruttoriaProfiloUtente.idDomanda = this.datiDomanda.id;
      this.utentiService.postIstruttoriaProfilo(this.istruttoriaProfiloUtente).subscribe(result => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        this.router.navigate(['./'], { relativeTo: this.route.parent, queryParams: { tabselected: 'inlavorazione' } });
      }, err => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, A4gMessages.changeStatusKo));
      });
    }
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }
}
