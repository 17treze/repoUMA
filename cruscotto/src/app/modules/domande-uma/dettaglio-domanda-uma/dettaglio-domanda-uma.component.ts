import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng-lts';
import { forkJoin } from 'rxjs';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { DomandeUmaService } from '../domande-uma.service';
import { CarburanteRichiesto } from '../models/carburante-richiesto';
import { DettaglioCarburante } from '../models/carburante-richiesto copy';
import { DomandaUma } from '../models/domanda-uma';
import { DomandeUmaPaginate } from '../models/domande-uma-paginate';
import { TipoCarburante } from '../models/tipo-carburante';

@Component({
  selector: 'app-dettaglio-domanda-uma',
  templateUrl: './dettaglio-domanda-uma.component.html',
  styleUrls: ['./dettaglio-domanda-uma.component.css'],
})
export class DettaglioDomandaUmaComponent implements OnInit {
  constructor(
    private activatedRoute: ActivatedRoute,
    private umaService: DomandeUmaService,
    private messageService: MessageService
  ) { }

  domanda?: DomandaUma;
  carburanti = [];
  rettifiche: Array<DomandaUma> = [];

  ngOnInit() {
    const id = this.activatedRoute.snapshot.paramMap.get('idDomanda');
    this.fetchDomanda(id);
  }

  fetchDomanda(id: string) {
    this.umaService.getDomandaUma(id).subscribe((domanda) => {
      this.domanda = domanda;
      this.fetchCarburanteData(domanda);
      this.fetchRettifiche(domanda);
    });
  }

  fetchRettifiche(domanda: DomandaUma) {
    this.umaService.getRettificheUma(domanda.cuaa, [domanda.campagna]).subscribe((rettifiche: DomandeUmaPaginate) => {
      this.rettifiche = rettifiche.risultati;
    }, error => this.showError());
  }

  fetchCarburanteData(domanda: DomandaUma) {
    forkJoin([
      this.umaService.getCarburantePrelevato(domanda.cuaa, domanda.campagna.toString()),
      this.umaService.getCarburanteRicevuto(domanda.cuaa, domanda.campagna),
    ]).subscribe(([prelevato, ricevuto]) => {
      this.buildCarburanteDetails(
        domanda.carburanteRichiesto,
        prelevato,
        ricevuto
      );
    });
  }

  buildCarburanteDetails(
    assegnato: CarburanteRichiesto,
    prelevato: any,
    ricevuto: any
  ) {
    Object.values(TipoCarburante).forEach(tipo => {
      if (tipo != TipoCarburante.GASOLIO_TERZI) { // gasolio terzi è incluso nella sezione del gasolio e non va mostrato
        const carburante: DettaglioCarburante = this.buildCarburanteDetailsByType(tipo, assegnato, prelevato, ricevuto);
        this.carburanti.push(carburante);
      }
    });
  }

  buildCarburanteDetailsByType(tipo: TipoCarburante, assegnato: CarburanteRichiesto, prelevato: any, ricevuto: any) {
    return {
      tipo: tipo,
      assegnato: tipo === TipoCarburante.GASOLIO ? (assegnato ? this.safeNumber(assegnato[tipo]) + this.safeNumber(assegnato.gasolioTerzi) : 0) : (assegnato ? assegnato[tipo] : 0),
      prelevato: this.safeSumOfType(prelevato, tipo),
      ricevuto: this.safeSumOfType(ricevuto, tipo),
    }
  }

  safeSumOfType(infoCarburante: any, tipo: TipoCarburante) {
    if (!infoCarburante || !infoCarburante.length)
      return 0;

    return infoCarburante
      .map(p => p.carburante && p.carburante.hasOwnProperty(tipo) ? p.carburante[tipo] : 0)
      .reduce((a, b) => this.safeNumber(a) + this.safeNumber(b));
  }

  safeNumber(b: any) {
    if (!b)
      return 0;

    return Number(b);
  }

  private showError() {
    this.messageService.add(
      A4gMessages.getToast(
        'toast-dettaglio-domande-uma',
        A4gSeverityMessage.error,
        A4gMessages.erroreRecuperoRettificheUma
      )
    );
  }
}
