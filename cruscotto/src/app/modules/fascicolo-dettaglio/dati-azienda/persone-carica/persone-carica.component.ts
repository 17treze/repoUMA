import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng-lts';
import { EMPTY, forkJoin, Subject } from 'rxjs';
import { catchError, switchMap, takeUntil } from 'rxjs/operators';

import { A4gMessages, A4gSeverityMessage } from '../../../../shared/a4g-messages';
import { PersonaFisicaConCaricaDto, PersonaGiuridicaConCaricaDto } from '../../../../../app/shared/models/persona';
import { AnagraficaFascicoloService } from '../../../../../app/shared/services/anagrafica-fascicolo.service';
import { A4gAccordion } from '../../a4g-accordion-tab/a4g-accordion.model';
import { KeyValue } from '../../models/KeyValue';
import { FascicoloDettaglioService } from 'src/app/shared/services/fascicolo-dettaglio.service';

@Component({
  selector: 'app-persone-carica',
  templateUrl: './persone-carica.component.html',
  styleUrls: ['./persone-carica.component.scss']
})
export class PersoneCaricaComponent implements OnInit, OnDestroy {
  public cuaa = '';
  public templates: A4gAccordion[] = [];
  private componentDestroyed$: Subject<boolean> = new Subject();
  private idValidazione = 0;
  public personaFisicaConCarica: PersonaFisicaConCaricaDto[];
  public personaGiuridicaConCarica: PersonaGiuridicaConCaricaDto[];

  constructor(
    protected route: ActivatedRoute,
    private messageService: MessageService,
    private translateService: TranslateService,
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private fascicoloDettaglioService: FascicoloDettaglioService) { }

  ngOnInit() {
    this.getIdValidazione();
    this.fascicoloDettaglioService.fascicoloCorrente.pipe(
      switchMap(fascicoloCorrente => {
        this.cuaa = fascicoloCorrente.cuaa;
        return forkJoin([
          this.anagraficaFascicoloService.getPersoneFisicheConCarica(this.cuaa, this.idValidazione),
          this.anagraficaFascicoloService.getPersoneGiuridicheConCarica(this.cuaa, this.idValidazione)
        ]);
      }),
      catchError(err => {
        this.personaFisicaConCarica = [];

        if (err.status === 403) {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('HttpErrorMessages.403')));
        } else {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error,
            this.translateService.instant(err.error.message)));
        }
        return EMPTY;
      }),
      takeUntil(this.componentDestroyed$)
    ).subscribe(([carichePersonaFisica, carichePersonaGiuridica]) => {
      this.handleGetPersoneFisicheConCarica(carichePersonaFisica);
      this.handleGetPersoneGiuridicheConCarica(carichePersonaGiuridica);
    });
  }

  private getIdValidazione() {
    this.route.queryParams.subscribe(queryParams => this.idValidazione = queryParams['id-validazione']);
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  private ordinaPersoneFisicheDataCariche() {
    this.personaFisicaConCarica.forEach(
      (persona) => {
        if (persona.cariche && persona.cariche.length > 0) {
          persona.cariche.sort((a, b): number => {
            if (a.dataInizio > b.dataInizio) { return 1; }
            if (a.dataInizio < b.dataInizio) { return -1; }
            return 0;
          });
        }
      }
    );
  }

  private ordinaPersoneGiuridicheDataCariche() {
    this.personaGiuridicaConCarica.forEach(
      (persona) => {
        if (persona.cariche && persona.cariche.length > 0) {
          persona.cariche.sort((a, b): number => {
            if (a.dataInizio > b.dataInizio) { return 1; }
            if (a.dataInizio < b.dataInizio) { return -1; }
            return 0;
          });
        }
      }
    );
  }

  private handleGetPersoneFisicheConCarica(response: PersonaFisicaConCaricaDto[]) {
    this.personaFisicaConCarica = response;
    if (!this.personaFisicaConCarica) { this.personaFisicaConCarica = []; }
    if (this.personaFisicaConCarica && this.personaFisicaConCarica.length > 0) {
      this.personaFisicaConCarica.sort((a, b): number => {
        if (a.cognome > b.cognome) { return 1; }
        if (a.cognome < b.cognome) { return -1; }
        return 0;
      });
      this.ordinaPersoneFisicheDataCariche();
      this.prepareTemplatesPersoneFisicheConCarica();
    }
  }

  public prepareTemplatesPersoneFisicheConCarica() {
    const templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'FascicoloAziendale.PERSONE_CON_CARICA.PERSONE_FISICHE';
    const keyValueArray = new Array<KeyValue>();

    this.personaFisicaConCarica.forEach((persona) => {
      const denominazione = persona.cognome + ' ' + persona.nome || null;
      keyValueArray.push(this.buildKeyValue('Cognome e nome:', denominazione));
      const carica = persona.cariche[0].descrizione || null;
      keyValueArray.push(this.buildKeyValue('Carica:', carica));
    });
    templateToAdd.fields = keyValueArray;
    this.templates.push(templateToAdd);
  }

  private handleGetPersoneGiuridicheConCarica(response: PersonaGiuridicaConCaricaDto[]) {
    this.personaGiuridicaConCarica = response;
    if (!this.personaGiuridicaConCarica) { this.personaGiuridicaConCarica = []; }
    if (this.personaGiuridicaConCarica && this.personaGiuridicaConCarica.length > 0) {
      this.personaGiuridicaConCarica.sort((a, b): number => {
        if (a.denominazione > b.denominazione) { return 1; }
        if (a.denominazione < b.denominazione) { return -1; }
        return 0;
      });
      this.ordinaPersoneGiuridicheDataCariche();
      this.prepareTemplatesPersoneGiuridicheConCarica();
    }
  }

  public prepareTemplatesPersoneGiuridicheConCarica() {
    const templateToAdd = {} as A4gAccordion;
    templateToAdd.headerTitle = 'FascicoloAziendale.PERSONE_CON_CARICA.PERSONE_GIURIDICHE';

    this.personaGiuridicaConCarica.forEach((persona) => {
      const keyValueArray = new Array<KeyValue>();
      const denominazione = persona.denominazione || null;
      keyValueArray.push(this.buildKeyValue('Denominazione:', denominazione));
      const carica = persona.cariche[0].descrizione || null;
      keyValueArray.push(this.buildKeyValue('Carica:', carica));
      templateToAdd.fields = keyValueArray;
    });
    this.templates.push(templateToAdd);
  }

  buildKeyValue(key: string, value: string) {
    const keyValue = new KeyValue();
    keyValue.mkey = key;
    keyValue.mvalue = value;
    return keyValue;
  }
}
