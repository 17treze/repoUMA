import { RegisterComponent } from './../register/register.component';
import { environment } from './../../../environments/environment';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng-lts';
import { Component, OnInit, ViewChild } from '@angular/core';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { DatiAnagrafici, DatiDomanda, ResponsabilitaRichieste, ServiziTypeEnum, TitolareImpresa } from '../register/datiAnagrafici';
import { EMPTY, forkJoin, Subject } from 'rxjs';
import { catchError, takeUntil, switchMap } from 'rxjs/operators';
import { DatiAperturaFascicoloDto } from '../fascicolo-dettaglio/models/anagrafica-fascicolo';
import { AnagraficaFascicoloService } from 'src/app/shared/services/anagrafica-fascicolo.service';

@Component({
  selector: 'app-register-crea-fascicolo',
  templateUrl: './register-crea-fascicolo.component.html',
  styleUrls: ['./register-crea-fascicolo.component.css']
})
export class RegisterCreaFascicoloComponent implements OnInit {

  private datiAnagraficiUtente: DatiAnagrafici;
  private isPrivacyFirmata: boolean;
  private datiFascicolo: DatiAperturaFascicoloDto;
  private componentDestroyed$: Subject<boolean> = new Subject();

  @ViewChild(RegisterComponent) childRegisterComponent: RegisterComponent;

  constructor(
    private messageService: MessageService,
    private translateService: TranslateService,
    private anagraficaFascicoloService: AnagraficaFascicoloService
  ) { }

  ngOnInit(): void {
  }

  public setDatiAnagraficiUtente(event) {
    this.datiAnagraficiUtente = event;
  }

  public setPrivacyFirmata(event) {
    this.isPrivacyFirmata = event;
  }

  public setDatiFascicoloOutput(event) {
    this.datiFascicolo = event;
  }

  public creaUtenteEFascicolo() {
    if (this.verificaCompletezzaDatiAnagrafici()) {
      this.messageService.add(A4gMessages.getToast('tst-crea-utente-fascicolo', A4gSeverityMessage.warn, this.translateService.instant('common.CAMPI_OBBLIGATORI_KO')));
    } else {
      const datiDomanda: DatiDomanda = new DatiDomanda();
      datiDomanda.datiAnagrafici = this.datiAnagraficiUtente;
      datiDomanda.servizi = [ServiziTypeEnum.A4G];
      datiDomanda.responsabilitaRichieste = this.popolaResponsabilita();

      this.childRegisterComponent.sottoscriviDomandaToObservable(datiDomanda).pipe(
        takeUntil(this.componentDestroyed$))
        .subscribe();
    }
  }

  public verificaCompletezzaDatiAnagrafici() {
    return !this.datiAnagraficiUtente ||
      !this.datiAnagraficiUtente.codiceFiscale ||
      !this.datiAnagraficiUtente.cognome ||
      !this.datiAnagraficiUtente.email ||
      !this.datiAnagraficiUtente.nome ||
      !this.datiAnagraficiUtente.telefono ||
      !this.isPrivacyFirmata ||
      !this.datiFascicolo;
  }

  private popolaResponsabilita(): ResponsabilitaRichieste {
    const responsabilitaRichieste = new ResponsabilitaRichieste();
    responsabilitaRichieste.responsabilitaLegaleRappresentante = [];
    responsabilitaRichieste.responsabilitaLegaleRappresentante.push(new TitolareImpresa(this.datiFascicolo.codiceFiscale, this.datiFascicolo.denominazioneFascicolo));
    return responsabilitaRichieste;
  }

}
