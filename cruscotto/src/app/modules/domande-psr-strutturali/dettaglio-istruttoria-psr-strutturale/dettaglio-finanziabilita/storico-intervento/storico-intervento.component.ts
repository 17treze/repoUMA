import { Component, Input, OnChanges } from '@angular/core';
import { PsrInterventoDettaglioDto } from '../../../models/dettaglio-istruttoria-srt';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-storico-intervento',
  templateUrl: './storico-intervento.component.html',
  styleUrls: ['./storico-intervento.component.css'],
})
export class StoricoInterventoComponent implements OnChanges {
  @Input() dettaglioIntervento: PsrInterventoDettaglioDto;
  @Input() tipologia: 'ACCONTO' | 'SALDO' | 'FINANZIABILITA' = 'FINANZIABILITA';
  @Input() idDomandaPagamento: number;

  specializedLabel: string;

  constructor(private router: Router,
              private route: ActivatedRoute) {}

  ngOnChanges(): void {
    this.specializedLabel = {
      ACCONTO: ' acconto',
      SALDO: ' saldo',
      FINANZIABILITA: '',
    }[this.tipologia];
  }

  goToFatture(): void {
    this.router.navigate(['./intervento', this.dettaglioIntervento.idIntervento], {relativeTo: this.route });
  }
}
