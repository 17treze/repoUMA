import { FascicoloAgsDto } from 'src/app/a4g-common/classi/FascicoloAgsDto';
import { Params } from '@angular/router';
import { ErrorService } from './../../../a4g-common/services/error.service';
import { HttpClientDichiarazioneConsumiUmaService } from '../../core-uma/services/http-client-dichiarazione-consumi-uma.service';
import { TipoRichiedenteUma } from '../../../a4g-common/classi/TipoRichiedenteUma-enum';
import { Router, ActivatedRoute } from '@angular/router';
import { AnagraficaFascicoloService } from '../../../fascicolo/creazione-fascicolo/anagrafica-fascicolo.service';
import { MessageService } from 'primeng/api';
import { Component, OnInit, ViewEncapsulation, OnDestroy } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { EMPTY, forkJoin, Observable, of, Subscription } from 'rxjs';
import { FascicoloCorrente } from 'src/app/fascicolo/fascicoloCorrente';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
// import { switchMap } from 'rxjs/internal/operators/switchMap';
import { FascicoloService } from 'src/app/fascicolo/fascicolo.service';
import { Fascicolo } from 'src/app/a4g-common/classi/Fascicolo';
import { FascicoloLazio } from 'src/app/a4g-common/classi/FascicoloLazio';
import { PersonaAgsDto } from '../../core-uma/models/dto/PersonaAgsDto';
import { HttpClientDomandaUmaService } from '../../core-uma/services/http-client-domanda-uma.service';
import { catchError, map } from 'rxjs/operators';
import { UMA_MESSAGES } from '../../uma.messages';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';

@Component({
  selector: 'app-richiedente-uma',
  templateUrl: './richiedente-uma.component.html',
  styleUrls: ['./richiedente-uma.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RichiedenteUmaComponent implements OnInit, OnDestroy {

  subscription: Subscription;
  creaRichiestaSubscription: Subscription;
  creaDichiarazioneSubscription: Subscription;

  richiedenteForm: FormGroup;
  soggetti: Array<PersonaAgsDto>;
  cuaa: string;
  TIPO_RICHIEDENTE: keyof typeof TipoRichiedenteUma; // in base al tipo (parametro dell'url) posso creare una nuova Domanda o una Dichiarazione Consumi

  constructor(
    public dateUtilService: DateUtilService,
    private messageService: MessageService,
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private fascicoloCorrente: FascicoloCorrente,
    private router: Router,
    private route: ActivatedRoute,
    private httpClientDomandaUmaService: HttpClientDomandaUmaService,
    private httpClientDichiarazioneConsumiUmaService: HttpClientDichiarazioneConsumiUmaService,
    private fascicoloService: FascicoloService,
    private errorService: ErrorService
  ) { }

  ngOnInit() {
    this.soggetti = [];
    this.initForm();
    this.setSubscriptions();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    if (this.creaDichiarazioneSubscription) {
      this.creaDichiarazioneSubscription.unsubscribe();
    }
    if (this.creaRichiestaSubscription) {
      this.creaRichiestaSubscription.unsubscribe();
    }
  }

  setSubscriptions() {
    // Valorizzazione del FascicoloCorrente (BehaviouralSubject di FascicoloService) per valorizzare l'intestazione del fascicolo
    // a fronte del caricamento del nuovo  modulo (uma)
    this.subscription = this.route.params.pipe(
      catchError((err: ErrorDTO) => {
        this.errorService.showError(err);
        return EMPTY;
      }),
      map((params: Params) => {
        this.TIPO_RICHIEDENTE = params['tipoRichiesta'];
        return this.fascicoloService.getFascicoloLazio(params['idFascicolo']); // 
      }),
      catchError((err: ErrorDTO) => {
        this.errorService.showError(err);
        return EMPTY;
      }),
    ).subscribe((obs: Observable<FascicoloLazio>) => {
      map ((fascicolo: FascicoloLazio) => {
        console.log('fascciolo: ' + fascicolo);
        if (fascicolo.data?.cuaa) {
          console.log('Cuaa: ' + fascicolo.data.cuaa);
          this.fascicoloCorrente.fascicoloLazio = fascicolo;
          this.cuaa = fascicolo.data.cuaa;
          this.richiedenteForm.get('richiedente').setValue(fascicolo);
        }
        else {
          this.errorService.showErrorWithMessage(fascicolo.text);
        }
      })
    }, error => this.errorService.showError(error));
  }

  initForm() {
    this.richiedenteForm = new FormGroup({
      richiedente: new FormControl(null)
    });
  }

  getLabel() {
    if (this.TIPO_RICHIEDENTE === TipoRichiedenteUma.richiesta) {
      return 'UMA.CREA_DOMANDA';
    } else {
      return 'UMA.CREA_DICHIARAZIONE';
    }
  }

  getTitle() {
    if (this.TIPO_RICHIEDENTE === TipoRichiedenteUma.richiesta) {
      return 'UMA.RICHIEDENTE_DOMANDA_SCELTA';
    } else {
      return 'UMA.DICHIARAZIONE_CONSUMI.DICHIARAZIONE_CONSUMI';
    }
  }

  onClick() {
    const selection: PersonaAgsDto = this.richiedenteForm.get('richiedente').value;
    if (selection == null) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, UMA_MESSAGES.noRichiedenteUmaSelezionato));
      return;
    }
    if (this.TIPO_RICHIEDENTE === TipoRichiedenteUma.richiesta) {
      return this.creaRichiestaUma(selection);
    } else {
      return this.creaDichiarazioneUma(selection);
    }
  }

  creaRichiestaUma(persona: PersonaAgsDto) {
    this.creaRichiestaSubscription = this.httpClientDomandaUmaService.presentaDomanda(this.cuaa, persona.codiceFiscale).subscribe((idDomanda: number) => {
      this.router.navigate(['./richiesta', idDomanda], { relativeTo: this.route.parent });
    }, (error: ErrorDTO) => this.errorService.showError(error));
  }

  creaDichiarazioneUma(persona: PersonaAgsDto) {
    this.creaDichiarazioneSubscription = this.httpClientDichiarazioneConsumiUmaService.presentaDichiarazioneConsumi(this.cuaa, persona.codiceFiscale).subscribe((idDichiarazione: number) => {
      this.router.navigate(['./dichiarazione-consumi', idDichiarazione], { relativeTo: this.route.parent });
    }, (error: ErrorDTO) => this.errorService.showError(error));
  }
}
