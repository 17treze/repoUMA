import { Component, OnInit, OnDestroy } from '@angular/core';
import { MessageService } from 'primeng-lts';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { TranslateService } from '@ngx-translate/core';
import { EMPTY, Subject } from 'rxjs';
import { IFascicolo } from '../../domande-uniche/models/fascicolo.model';
import { DomandeUnicheService } from '../../domande-uniche/domande-uniche.service';
import { AziendaAgricolaService } from 'src/app/shared/services/azienda-agricola.service';
import { AppagService } from 'src/app/shared/services/appag-service';
import { map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-lista-cuaa',
  templateUrl: './lista-cuaa.component.html',
  styleUrls: ['./lista-cuaa.component.css'],
})
export class ListaCuaComponent implements OnInit, OnDestroy {
  public listaCuaa: IFascicolo[];
  public selectedCuaa: IFascicolo;

  private componentDestroyed$: Subject<boolean> = new Subject();
  isFromAppag = false;

  constructor(
    private domandeUnicheService: DomandeUnicheService,
    private aziendaAgricolaService: AziendaAgricolaService,
    private messageService: MessageService,
    private translateService: TranslateService,
    private appagService: AppagService
  ) {}

  ngOnInit() {
    this.isFromAppag = this.appagService.isFromAppag();
    if (this.isFromAppag) {
      this.loadFascicoloByCuaa();
    } else {
      this.loadAllFascicoli();
    }
  }

  loadFascicoloByCuaa() {
    this.domandeUnicheService
      .getFascicoloCuaa(this.appagService.fromAppagCuaa())
      .subscribe((fascicolo) => {
        this.aziendaAgricolaService.setSelectedCuaa(fascicolo);
      });
  }

  loadAllFascicoli() {
    this.domandeUnicheService
      .getListaCuaa()
      .pipe(
        map((lista) => {
          if (!lista) {
            return EMPTY;
          }

          this.listaCuaa = lista?.sort((a, b) =>
            a.denominazione.localeCompare(b.denominazione)
          );
          return lista;
        }),
        switchMap(() => this.aziendaAgricolaService.getSelectedCuaa())
      )
      .subscribe((fascicolo) => {
        if (!fascicolo) {
          this.aziendaAgricolaService.setSelectedCuaa(this.listaCuaa[0]);
          this.messageService.add(
            A4gMessages.getToast(
              'defaultToast',
              A4gSeverityMessage.error,
              this.translateService.instant('errorsMsg.BRCIDU000')
            )
          );
        } else {
          this.selectedCuaa = fascicolo;
        }
      });
  }

  public onChange(event) {
    this.aziendaAgricolaService.setSelectedCuaa(event.value);
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }
}
