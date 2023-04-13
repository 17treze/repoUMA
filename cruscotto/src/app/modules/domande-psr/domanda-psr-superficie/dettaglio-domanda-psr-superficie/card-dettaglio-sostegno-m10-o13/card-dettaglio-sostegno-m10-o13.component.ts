import { Component, Input, OnInit } from '@angular/core';
import { DomandePsrService } from '../../../domande-psr.service';
import { ImpegnoZooPascoloPsr } from '../../../models/impegno-zoo-pascolo-psr';

@Component({
  selector: 'app-card-dettaglio-sostegno-m10-o13',
  templateUrl: './card-dettaglio-sostegno-m10-o13.component.html',
  styleUrls: ['./card-dettaglio-sostegno-m10-o13.component.css']
})
export class CardDettaglioSostegnoM10O13Component implements OnInit {

  @Input()
  idDomanda: number;

  zooPascoliPsr: ImpegnoZooPascoloPsr[];

  constructor(private domandePrsService: DomandePsrService) { }

  ngOnInit() {
    this.domandePrsService.getImpegniZooPascoliByIdDomanda(this.idDomanda).subscribe( zooPascoliPsr => {
      this.zooPascoliPsr = zooPascoliPsr;
    });
  }

}
