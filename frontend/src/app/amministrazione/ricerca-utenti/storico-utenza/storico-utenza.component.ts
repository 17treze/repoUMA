import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {Subject} from 'rxjs';
import {switchMap, takeUntil} from 'rxjs/operators';
import {Labels} from 'src/app/app.labels';
import {DateSupport} from '../../../a4g-common/utility/date-support';
import { IstruttoriaPerStorico } from '../../gestione-utenze/model/istruttoria-per-storico';
import { Utente } from '../../gestione-utenze/model/utente';
import { GestioneUtenzeService } from '../../gestione-utenze/gestione-utenze.service';

@Component({
  selector: 'app-storico-utenza',
  templateUrl: './storico-utenza.component.html',
  styleUrls: ['./storico-utenza.component.css']
})
export class StoricoUtenzaComponent implements OnInit, OnDestroy {

  private idUtente: string;
  public istruttorie: IstruttoriaPerStorico;
  public utente: Utente;
  private componentDestroyed$: Subject<boolean> = new Subject();
  public intestazioni = Labels;
  public DATE_FORMAT = DateSupport.PATTERN_DATE_2;
  public motiviDisattivazione: any = {
    'ALTRO': 'Altro',
    'PENSIONAMENTO': 'Pensionamento',
    'FINE_RAPPORTO': 'Fine rapporto'
  };

  constructor(
    private route: ActivatedRoute,
    private gestioneUtenzeService: GestioneUtenzeService,
  ) { }

  ngOnInit() {
    this.callServices();
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  private callServices(): void {
    this.route.paramMap.pipe(
        switchMap((params: ParamMap) => {
          this.idUtente = params.get('idUtenza');
          return this.gestioneUtenzeService.getStorico(this.idUtente);
        }),
        takeUntil(this.componentDestroyed$)
    ).subscribe(response => {
        this.istruttorie = response.istruttorie;
        this.utente = response.utente;
    });
  }


}
