import {Component, OnInit} from '@angular/core';
import {DomandePsrStrutturaliService} from '../domande-psr-strutturali.service';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {Subject} from 'rxjs';
import {MenuItem} from "primeng-lts";
import {TranslateService} from "@ngx-translate/core";
import {DateService} from "../../../shared/utilities/date.service";
import {ActivateRouteSupport} from "../../../shared/utilities/activate-route.support";
import {InfoGeneraliPSRStrutturale} from "../models/info-generali-domanda-psr-strutturale";

@Component({
  selector: 'app-container-domande-psr-strutturali',
  templateUrl: './container-domande-psr-strutturali.component.html',
  styleUrls: ['./container-domande-psr-strutturali.component.css']
})
export class ContainerDomandePsrStrutturaliComponent implements OnInit {

  constructor(
    ) { }
    
      ngOnInit() {
      }
    
    
    }