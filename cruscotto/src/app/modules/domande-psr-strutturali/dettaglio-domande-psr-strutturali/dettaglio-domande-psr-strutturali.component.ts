import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { DomandePsrStrutturaliService } from '../domande-psr-strutturali.service';
import { InfoGeneraliPSRStrutturale } from '../models/info-generali-domanda-psr-strutturale';
import { DettaglioPSRStrutturale } from '../models/dettaglio-domanda-psr-strutturale';
import { TranslateService } from '@ngx-translate/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-dettaglio-domande-psr-strutturali',
  encapsulation: ViewEncapsulation.None,
  templateUrl: './dettaglio-domande-psr-strutturali.component.html',
  styleUrls: ['./dettaglio-domande-psr-strutturali.component.scss']
})
export class DettaglioDomandaPsrStruttComponent implements OnInit {
  public interventi: Array<string> = [];
  public numeroDomandaIntegrativa: string;
  public dataDomandaIntegrativa: string;
  public importoCalcolato: number;
  public importoLiquidato: number;

  @Input() public domandaPsrStrutturale: InfoGeneraliPSRStrutturale;
  private listaDettagliDomanda: DettaglioPSRStrutturale[] = [];

  constructor(private domandePSRService: DomandePsrStrutturaliService,
    protected route: ActivatedRoute,
    private translate: TranslateService) { }

  ngOnInit(): void {
    // this.elementiSostegno = SostegnoSuperfici.calcolaElementiSostegnoSuperficiFrom(this.domandaUnica);

    // let idProgetto: number = this.domandaPsrStrutturale.idProgetto;
    let idProgetto: number = Number(this.route.snapshot.paramMap.get('idDomanda'));
    // idProgetto = 15079;
    // idProgetto = 10366;
    this.caricaDettaglioPSRStrutt(idProgetto);

    console.log(this.domandaPsrStrutturale);
  }

  private caricaDettaglioPSRStrutt(idProgetto: number): any {
    this.domandePSRService.getPSRStrutturaliDettaglio(idProgetto).subscribe(
      (dettagliDomanda: DettaglioPSRStrutturale[]) => {
        this.listaDettagliDomanda = dettagliDomanda;
        if (this.listaDettagliDomanda == null) {
          // nessuna domanda
          }
        if (this.listaDettagliDomanda.length == 0) {
            // nessuna domanda
          }
        }
    ), err => {
      // servizio non disponibile
      console.error(err);
    }
  }

}
