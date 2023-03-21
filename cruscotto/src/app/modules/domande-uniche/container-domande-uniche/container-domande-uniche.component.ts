import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng-lts';
import { DomandeUnicheService } from '../domande-uniche.service';
import { takeUntil } from 'rxjs/operators';
import { IFascicolo } from '../models/fascicolo.model';
import { Subject, Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { AziendaAgricolaService } from 'src/app/shared/services/azienda-agricola.service';

@Component({
  selector: 'app-container-domande-uniche',
  templateUrl: './container-domande-uniche.component.html',
  styleUrls: ['./container-domande-uniche.component.css']
})
export class ContainerDomandeUnicheComponent implements OnInit {


  constructor(
) { }

  ngOnInit() {
  }


}
