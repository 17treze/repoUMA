import { MessageService } from 'primeng/api';
import { UnitaTecnicoEconomicheDto } from './../../../../creazione-fascicolo/dto/UnitaTecnicoEconomicheDto';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AnagraficaFascicoloService } from 'src/app/fascicolo/creazione-fascicolo/anagrafica-fascicolo.service';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { StringSupport } from 'src/app/a4g-common/utility/string-support';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-unita-tecnico-economiche',
  templateUrl: './unita-tecnico-economiche.component.html',
  styleUrls: ['./unita-tecnico-economiche.component.css']
})
export class UnitaTecnicoEconomicheComponent implements OnInit, OnDestroy {
  public cols: any[];
  public cols2: any[];
  public unitaTecnicoEconomiche: UnitaTecnicoEconomicheDto[];
  private cuaa: string;
  protected componentDestroyed$: Subject<boolean> = new Subject();
  private idValidazione: number = undefined;

  constructor(
    private route: ActivatedRoute,
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.cuaa = this.route.snapshot.paramMap.get('cuaa');
    this.setCols();
    this.route.queryParams.pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(queryParams => {
      const paramIdVal: string = queryParams['id-validazione'];
      if (paramIdVal) {
        this.idValidazione = Number.parseInt(paramIdVal);
      } else {
        this.idValidazione = 0;
      }
      this.getUnitaTecnicoEconomiche(this.cuaa);
    });
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  private setCols() {
    this.cols = [
      { field: 'identificativoUte', header: 'Identificativo UTE' },
      { field: 'attivita', header: 'Attività' },
      { field: 'indirizzo', header: 'Indirizzo' },
    ];
  }

  private getUnitaTecnicoEconomiche(cuaa: string) {
    this.anagraficaFascicoloService.getUnitaTecnicoEconomiche(cuaa, this.idValidazione)
    .pipe(takeUntil(this.componentDestroyed$))
    .subscribe(
      resp => {
        if (resp) {
          this.unitaTecnicoEconomiche = resp.sort((a, b) =>
          (a.identificativoUte > b.identificativoUte) ? 1 
          : ((b.identificativoUte > a.identificativoUte) ? -1 : 0));
        }
      }, err => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, A4gMessages.errorGetUnitaTecnicoEconomiche));
    });
  }

  public getIndirizzo(unitaTE: UnitaTecnicoEconomicheDto) {
    const toponimo = StringSupport.isNullOrEmpty(unitaTE.toponimo);
    const via = StringSupport.isNullOrEmpty(unitaTE.via);
    const numeroCivico = StringSupport.isNullOrEmpty(unitaTE.numeroCivico);
    let indirizzo = '';
    if (!via) {
      if (!toponimo) {
        if (!numeroCivico) {
          indirizzo = unitaTE.toponimo + ' ' + unitaTE.via + ' ' + unitaTE.numeroCivico + ' - ' + unitaTE.comune;
        } else {
          indirizzo = unitaTE.toponimo + ' ' + unitaTE.via + ' - ' + unitaTE.comune;
        }
      } else {
        if (!numeroCivico) {
          indirizzo = unitaTE.via + ' ' + unitaTE.numeroCivico + ' - ' + unitaTE.comune;
        } else {
          indirizzo = unitaTE.via + ' - ' + unitaTE.comune;
        }
      }
    }
    return indirizzo;
  }
}
