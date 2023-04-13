import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { forkJoin } from 'rxjs';
import { DateService } from '../../../../app/shared/utilities/date.service';
import { DichiarazioneAntimafiaService } from '../dichiarazione-antimafia.service';
import { DichiarazioneAntimafia } from '../models/dichiarazione-antimafia';
import { DomandaCollegata } from '../models/domanda-collegata';
import { DomandaCollegataTypeEnum } from '../models/domanda-collegata-filter';

@Component({
  selector: 'app-domande-collegate',
  templateUrl: './domande-collegate.component.html',
  styleUrls: ['./domande-collegate.component.css'],
})
export class DomandeCollegateComponent implements OnInit {
  @Input()
  dichiarazione?: DichiarazioneAntimafia;

  cuaa = '';
  domandeWrapper = [];
  domandePsrSuperficie = [];
  domandeUniche = [];

  constructor(
    private antimafiaService: DichiarazioneAntimafiaService,
    private activatedRoute: ActivatedRoute,
    private dateService: DateService
  ) {}

  ngOnInit() {
    this.cuaa = this.activatedRoute.snapshot.paramMap.get('cuaa');
    this.fetchDomandeCollegate();
  }

  public scroll(id: string) {
    (<HTMLDivElement>document.getElementById(id)).scrollIntoView({ behavior: 'smooth', block: 'start', inline: 'nearest' });
  }

  fetchDomandeCollegate() {
    forkJoin([
      this.antimafiaService.getAntimafiaDomandeCollegate(this.cuaa, DomandaCollegataTypeEnum.DOMANDA_UNICA),
      this.antimafiaService.getAntimafiaDomandeCollegate(this.cuaa, DomandaCollegataTypeEnum.PSR_SUPERFICIE_EU),
    ]).subscribe(([domandeUniche, domandePsrSuperficie]) => {
      this.domandePsrSuperficie = domandePsrSuperficie ? this.sortAntimafiaApplicationsByDate(domandePsrSuperficie) : []; 
      this.domandeUniche = domandeUniche ? this.sortAntimafiaApplicationsByDate(domandeUniche) : [];
      
      this.domandeWrapper.push({ type: DomandaCollegataTypeEnum.PSR_SUPERFICIE_EU, list: this.domandePsrSuperficie });
      this.domandeWrapper.push({ type: DomandaCollegataTypeEnum.DOMANDA_UNICA, list: this.domandeUniche });
    });
  }
  
  sortAntimafiaApplicationsByDate(applications: DomandaCollegata[]): DomandaCollegata[] {
    return applications.sort((a, b) => {
      return this.dateService.getDateTimeFromString(b.dtDomanda) - this.dateService.getDateTimeFromString(a.dtDomanda);
    });
  }

  formatStatus(status: string) {
    if (!status) return status;

    const formattedStatus = status.replace(/_/g, ' ');
    const lowerCaseFormattedStatus = formattedStatus.toLowerCase();
    const finalStatusWithFirstCapitalLetter = lowerCaseFormattedStatus[0].toUpperCase() + lowerCaseFormattedStatus.slice(1);
    return finalStatusWithFirstCapitalLetter;
  }
}
