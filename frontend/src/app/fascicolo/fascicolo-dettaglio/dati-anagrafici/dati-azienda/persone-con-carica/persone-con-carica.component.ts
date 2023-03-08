import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { AnagraficaFascicoloService } from '../../../../creazione-fascicolo/anagrafica-fascicolo.service';
import { IndirizzoDto, PersonaFisicaConCaricaDto, PersonaGiuridicaConCaricaDto, VerificaCodiceFiscaleEnum } from '../../../../creazione-fascicolo/dto/PersonaDto';
import { ActivatedRoute } from '@angular/router';
import { Subject, Observable, interval, merge, throwError, EMPTY, combineLatest } from 'rxjs';
import { takeUntil, switchMap, skipWhile, tap, first, timeout, catchError, map, take } from 'rxjs/operators';
import { MessageService } from 'primeng-lts';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';

@Component({
  selector: 'app-persone-con-carica',
  templateUrl: './persone-con-carica.component.html',
  styleUrls: ['./persone-con-carica.component.css']
})
export class PersoneConCaricaComponent implements OnInit, OnDestroy {
  @Input() public updated$: Observable<void>;
  @Output() public updateError = new EventEmitter<string>();
  public cols: any[];
  public cols2: any[];
  public personaFisicaConCarica: PersonaFisicaConCaricaDto[];
  public personaGiuridicaConCarica: PersonaGiuridicaConCaricaDto[];
  public verificaCodiceFiscaleEnum = VerificaCodiceFiscaleEnum;
  private cuaa = '';
  private idValidazione: number = undefined;
  protected componentDestroyed$: Subject<boolean> = new Subject();

  @ViewChild('tableFisiche', { static: true }) tableFisiche;
  @ViewChild('tableGiuridiche', { static: true }) tableGiuridiche;

  constructor(
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private messageService: MessageService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.setCols();

    this.cuaa = this.route.snapshot.paramMap.get('cuaa');

    this.route.queryParams
      .pipe(
        takeUntil(this.componentDestroyed$)
      )
      .subscribe(queryParams => {
        const paramIdVal: string = queryParams['id-validazione'];

        this.idValidazione = paramIdVal ? parseInt(paramIdVal) : 0;

        this.getPersoneGiuridicheConCarica(this.cuaa, this.idValidazione);
        this.getPersoneFisicheConCarica(this.cuaa, this.idValidazione)
          .pipe(first())
          .subscribe();
      });
    
    this.updated$
      .pipe(
        takeUntil(this.componentDestroyed$),
        switchMap(response => this.getPersoneFisicheConCarica(this.cuaa, this.idValidazione))
      )
      .subscribe(
        response => this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, 'Aggiornamento dati da anagrafe tributaria terminato con successo')),
        error => this.updateError.emit('L’aggiornamento delle persone con carica con i dati di anagrafe tributaria è fallito: rieseguire l’aggiornamento di tutta la sezione anagrafica')
      )
  }
  
  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  private setCols() {
    this.cols = [
      { field: '', header: '' },
      { field: 'cognome', header: 'Cognome' },
      { field: 'nome', header: 'Nome' },
      { field: 'codiceFiscale', header: 'Codice Fiscale' },
      { field: 'cariche', header: 'Carica' },
      { field: 'verificaCodiceFiscale', header: 'Anomalie' },
    ];
    this.cols2 = [
      { field: '', header: '' },
      { field: 'denominazione', header: 'Denominazione' },
      { field: 'codiceFiscale', header: 'Codice Fiscale' },
      { field: 'cariche', header: 'Carica' }
    ];
  }

  private getPersoneFisicheConCarica(cuaa: string, idValidazione: number): Observable<any> {
    return combineLatest([
      this.anagraficaFascicoloService.getPersoneFisicheConCaricaCached(cuaa, idValidazione)
        .pipe(
          tap(this.salva.bind(this))
        ),
      this.anagraficaFascicoloService.getPersoneFisicheConCarica(cuaa, idValidazione, true)
        .pipe(
          tap(this.salva.bind(this))
        ),
    ]).pipe(
      catchError(error => {
        this.personaFisicaConCarica = [];

        throw error;
      })
    );
  }

  private salva(persone: PersonaFisicaConCaricaDto[]): void {
    this.personaFisicaConCarica = persone;
    if (!this.personaFisicaConCarica) { this.personaFisicaConCarica = []; }
    if (this.personaFisicaConCarica && this.personaFisicaConCarica.length > 0) {
      this.personaFisicaConCarica.sort((a, b): number => {
        if (a.cognome > b.cognome) { return 1; }
        if (a.cognome < b.cognome) { return -1; }
        return 0;
      });
    }
    this.ordinaPersoneFisicheDataCariche();
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

  private getPersoneGiuridicheConCarica(cuaa: string, idValidazione: number) {
    this.anagraficaFascicoloService.getPersoneGiuridicheConCarica(cuaa, idValidazione).subscribe(response => {
      this.personaGiuridicaConCarica = response;
      if (!this.personaGiuridicaConCarica) { this.personaGiuridicaConCarica = []; }
      if (this.personaGiuridicaConCarica && this.personaGiuridicaConCarica.length > 0) {
        this.personaGiuridicaConCarica.sort((a, b): number => {
          if (a.denominazione > b.denominazione) { return 1; }
          if (a.denominazione < b.denominazione) { return -1; }
          return 0;
        });
      }
      this.ordinaPersoneGiuridicheDataCariche();
    }, err => {
      this.personaGiuridicaConCarica = [];
    });
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

  public costruisciIndirizzo(indirizzoDto: IndirizzoDto): string {
    let indirizzo = '';
    const frazione = indirizzoDto && indirizzoDto.frazione || null;
    const toponimo = indirizzoDto && indirizzoDto.toponimo || null;
    const via = indirizzoDto && indirizzoDto.via || null;
    const civico = indirizzoDto && indirizzoDto.civico || null;
    if (frazione) { indirizzo = indirizzo + frazione + ' '; }
    if (toponimo) { indirizzo = indirizzo + toponimo + ' '; }
    if (via) { indirizzo = indirizzo + via + ', '; }
    if (civico) { indirizzo = indirizzo + civico; }
    return indirizzo;
  }

  public filter(e) {
    // tableFisiche
    this.tableFisiche.value.forEach((e) => {
      e['descrizione'] = e['cariche'][0]['descrizione'];
      e['dataInizio'] = e['cariche'][0]['dataInizio'];
    });
    this.tableFisiche.filterGlobal(e.target.value, 'contains');
    // tableGiuridiche
    this.tableGiuridiche.value.forEach((e) => {
      e['descrizione'] = e['cariche'][0]['descrizione'];
      e['dataInizio'] = e['cariche'][0]['dataInizio'];
    });
    this.tableGiuridiche.filterGlobal(e.target.value, 'contains');
  }

}
