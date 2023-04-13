import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { InfoGeneraliPSRStrutturale } from "../models/info-generali-domanda-psr-strutturale";
import { DomandePsrStrutturaliService } from "../domande-psr-strutturali.service";
import { ActivatedRoute, Router } from "@angular/router";
import { TranslateService } from "@ngx-translate/core";
import { DateService } from "../../../shared/utilities/date.service";

@Component({
  selector: 'app-header-domanda-psr-strutturale-alt',
  templateUrl: './header-domanda-psr-strutturale-alt.component.html',
})
export class HeaderDomandaPsrStrutturaleComponentAlt implements OnInit, OnChanges {

  @Input()
  public domandaPsrStrutturale: InfoGeneraliPSRStrutturale;

  public datiProtocollo: { numero: any; data: string };

  public dataProtocollazione: Date;


  constructor(private domandePsrStrutturaliService: DomandePsrStrutturaliService,
              private route: ActivatedRoute,
              private router: Router,
              private translateService: TranslateService,
              private dateService: DateService) { }

  ngOnInit() {
  }

  ngOnChanges(): void {
    if (this.domandaPsrStrutturale) {
      this.dataProtocollazione = this.dateService.fromIso(this.domandaPsrStrutturale.dataProgetto);
      this.datiProtocollo = {
        numero: this.domandaPsrStrutturale.protocolloProgetto,
        data: this.dateService.toIsoDate(this.dataProtocollazione)
      };
    }
  }

}
