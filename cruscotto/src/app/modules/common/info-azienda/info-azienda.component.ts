import { Component, OnInit, Input } from '@angular/core';
import { AziendaAgricolaService } from '../../../shared/services/azienda-agricola.service';
import { Observable } from 'rxjs';
import { IFascicolo } from '../../domande-uniche/models/fascicolo.model';

@Component({
  selector: 'app-info-azienda',
  templateUrl: './info-azienda.component.html',
  styleUrls: ['./info-azienda.component.css']
})
export class InfoAziendaComponent implements OnInit {

  constructor(
    private domandeUnicheService: AziendaAgricolaService
  ) { }

  @Input() title: string;
  public domandaUnica: Observable<IFascicolo>;

  ngOnInit() {
    this.domandaUnica = this.domandeUnicheService.getSelectedCuaa();
  }

}
