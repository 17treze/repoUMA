import { CreazioneFascicoloService } from './../../creazione-fascicolo/shared/crea-fascicolo.service';
import { AnagraficaFascicoloService } from './../../creazione-fascicolo/anagrafica-fascicolo.service';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng-lts/api';
import { Router, ActivatedRoute } from '@angular/router';
import { Fascicolo, TipoDetenzione } from 'src/app/a4g-common/classi/Fascicolo';
import { Component, OnInit } from '@angular/core';
import { Labels } from 'src/app/app.labels';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';

@Component({
  selector: 'app-ricerca-fascicoli-da-migrare',
  templateUrl: './ricerca-fascicoli-da-migrare.component.html',
  styleUrls: ['./ricerca-fascicoli-da-migrare.component.scss']
})
export class RicercaFascicoliDaMigrareComponent implements OnInit {

  public cols: any;
  public elementiTotali = 0;
  public fascicoliList: Array<Fascicolo>;
  public intestazioni = Labels;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private translateService: TranslateService,
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private creazioneFascicoloService: CreazioneFascicoloService
  ) { }

  ngOnInit() {
    this.cols = [
      {
        field: 'cuaa',
        header: Labels.cuaaSigla
      },
      {
        field: 'denominazione',
        header: Labels.denominazioneImpresa
      },
      {
        field: 'stato',
        header: Labels.stato
      },
      {
        field: 'tipoDetenzione',
        header: Labels.tipoDetenzione
      },
      {
        field: 'caa',
        header: Labels.sportelloMandatario
      },
      {
        field: null,
        header: Labels.azioni
      }
    ];
  }

  public onSearch(fascicoli: Array<Fascicolo>) {
    this.elementiTotali = fascicoli ? fascicoli.length : 0;
    this.fascicoliList = fascicoli;
  }

  public migraFascicolo(fascicolo: Fascicolo) {
    this.anagraficaFascicoloService.getCheckMigraFascicolo(fascicolo.cuaa, fascicolo.stato, fascicolo.tipoDetenzione, fascicolo.caacodice)
      .subscribe(res => {
        this.creazioneFascicoloService.anagraficaTributaria = res;
        this.router.navigate([`./cuaa/${fascicolo.cuaa}/migra`], { relativeTo: this.route });
      }, err => {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.info, this.translateService.instant('EXC_APRI_FASCICOLO.' + err.error.message)));
      })
  }

}
