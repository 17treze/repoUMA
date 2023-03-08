import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { ErrorDTO } from './../../../../a4g-common/interfaces/error.model';
import { ErrorService } from './../../../../a4g-common/services/error.service';
import { DichiarazioneConsumiDto } from 'src/app/uma/core-uma/models/dto/DichiarazioneConsumiDto';
import { Component, OnInit, SimpleChanges, OnChanges, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EMPTY, Subscription } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { TipoIntestazioneUma } from 'src/app/a4g-common/classi/enums/uma/TipoDocumentoUma.enum';
import { HttpClientAnagraficaService } from 'src/app/uma/shared-uma/services/http-client-anagrafica.service';
import { UmaLabels } from 'src/app/uma/uma.labels';
import { HttpClientDichiarazioneConsumiUmaService } from '../../services/http-client-dichiarazione-consumi-uma.service';
import { HttpClientDomandaUmaService } from '../../services/http-client-domanda-uma.service';

@Component({
  selector: 'app-intestazione-uma',
  templateUrl: './intestazione-uma.component.html',
  styleUrls: ['./intestazione-uma.component.scss']
})
export class IntestazioneUmaComponent implements OnInit, OnChanges, OnDestroy {
  richiestaOrDichiarazione: RichiestaCarburanteDto | DichiarazioneConsumiDto;
  title: string;
  routerSubscription: Subscription;
  tipoIntestazioneEnum: typeof TipoIntestazioneUma;
  isRichiestaOrRettifica: boolean;

  @Input() ctx: keyof typeof TipoIntestazioneUma;

  constructor(
    private httpClientDomandaUma: HttpClientDomandaUmaService,
    private httpClientDichiarazioneConsumiUmaService: HttpClientDichiarazioneConsumiUmaService,
    private httpClientAnagraficaService: HttpClientAnagraficaService,
    private activeRoute: ActivatedRoute,
    private errorService: ErrorService
  ) { }

  ngOnInit() {
    this.tipoIntestazioneEnum = TipoIntestazioneUma;
    this.routerSubscription = this.activeRoute.params
      .pipe(
        switchMap(params => {
          const idRichiestaOrDichiarazione = params['idDomanda'] || params['id']; // idDomanda = idRichiesta; id = idDichiarazione
          let obs = null;
          if (params['idDomanda'] != null) {
            this.isRichiestaOrRettifica = true;
            obs = this.httpClientDomandaUma.getDomandaById(idRichiestaOrDichiarazione);
          } else {
            this.isRichiestaOrRettifica = false;
            obs = this.httpClientDichiarazioneConsumiUmaService.getDichiarazioneConsumiById(idRichiestaOrDichiarazione)
          }
          return obs;
        }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-intestazione-uma');
          return EMPTY;
        }),
        switchMap((richiestaOrDichiarazione: RichiestaCarburanteDto | DichiarazioneConsumiDto) => {
          this.richiestaOrDichiarazione = richiestaOrDichiarazione;
          return this.httpClientAnagraficaService.getFascicoloAgs(this.richiestaOrDichiarazione.cuaa)
        }))
      .subscribe(fascicolo => { // TODO usa solo la denominazione non dovrebbe essere necessario oltre
        this.richiestaOrDichiarazione.denominazione = fascicolo.denominazione;
        if (this.ctx == TipoIntestazioneUma.GESTIONE_CARBURANTE_IN_ESUBERO) 
        {
          this.setTemplateIntestazioneByType(TipoIntestazioneUma.GESTIONE_CARBURANTE_IN_ESUBERO);
        } else {
          this.setTemplateIntestazioneByType(this.isRichiestaOrRettifica ? TipoIntestazioneUma.RICHIESTA : TipoIntestazioneUma.DICHIARAZIONE_CONSUMI);
        }
      }, (error: ErrorDTO) => this.errorService.showError(error, 'tst-intestazione-uma'));
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes && changes.ctx && changes.ctx.currentValue) {
      const type: keyof typeof TipoIntestazioneUma = changes.ctx.currentValue;
      this.setTemplateIntestazioneByType(type);
    }
  }

  ngOnDestroy(): void {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }

  private setTemplateIntestazioneByType(type: keyof typeof TipoIntestazioneUma) {
    if (type === TipoIntestazioneUma.GESTIONE_CARBURANTE_IN_ESUBERO) {
      this.title = UmaLabels.INTESTAZIONE.titleGestioneCarburanteInEsubero;
    } else if (type === TipoIntestazioneUma.DICHIARAZIONE_CONSUMI) {
      this.title = UmaLabels.INTESTAZIONE.titleDichiarazione;
    } else {
      // RICHIESTA / RETTIFICA
      if (!this.richiestaOrDichiarazione || (this.richiestaOrDichiarazione as RichiestaCarburanteDto).idRettificata == null) {
        this.title = UmaLabels.INTESTAZIONE.titleRichiesta;
      } else {
        this.title = UmaLabels.INTESTAZIONE.titleRettifica;
      }
    }
  }
}
