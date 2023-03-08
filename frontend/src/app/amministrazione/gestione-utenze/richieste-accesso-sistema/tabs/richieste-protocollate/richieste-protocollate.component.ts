import {Component, OnInit} from '@angular/core';
import {MessageService} from 'primeng/api';
import {A4gMessages, A4gSeverityMessage} from '../../../../../../app/a4g-common/a4g-messages';
import {SharedService} from '../../shared-service.service';
import {ActivatedRoute, Router, Params} from '@angular/router';
import {GestioneUtenzeService} from '../../../gestione-utenze.service';
import {RichiesteAccessoSistemaTabComponent} from "../richieste-accesso-sistema-tab/richieste-accesso-sistema-tab.component";

@Component({
  selector: 'app-richieste-protocollate',
  templateUrl: './richieste-protocollate.component.html',
  styleUrls: ['./richieste-protocollate.component.css']
})
export class RichiesteProtocollateComponent extends RichiesteAccessoSistemaTabComponent implements OnInit {

  constructor(protected gestioneUtenzeService: GestioneUtenzeService,
              protected messages: MessageService,
              protected sharedService: SharedService,
              protected router: Router,
              protected route: ActivatedRoute) {
    super(gestioneUtenzeService, messages, sharedService, router, route);
    this.setCols();
    this.stato = this.intestazioni.protocollata;
    this.defaultSortingColumn = 'dtProtocollazione';
    this.defaultSortingOrder = 'ASC';
  }

  ngOnInit() {
    this.setupRouting('protocollate');
  }

  public changeStatus(idDomanda: number) {
    this.gestioneUtenzeService.putPresaInCarico(idDomanda).subscribe(result => {
      this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
      const queryParams: Params = { 
        tabselected: 'inlavorazione'
      };
      this.router.navigate(
        [],
        {
          replaceUrl: false,
          queryParams: queryParams,
          queryParamsHandling: 'merge'
        });
    }, err => {
      this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.changeStatusKo));
    });
  }


  protected setCols() {
    this.cols = [
      { field: "idProtocollo", header: this.intestazioni.nrProtocollo },
      { field: "dtProtocollazione", header: this.intestazioni.dtProtocollazione },
      { field: "nome", header: this.intestazioni.nome },
      { field: "cognome", header: this.intestazioni.cognome },
      { field: "codiceFiscale", header: this.intestazioni.codiceFiscale },
      { field: null, header: this.intestazioni.contatti },
      { field: null, header: this.intestazioni.azioni }
    ];
  }

}
