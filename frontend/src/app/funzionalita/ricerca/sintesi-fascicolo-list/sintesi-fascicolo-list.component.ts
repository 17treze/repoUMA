import { Component, OnInit, Input } from '@angular/core';
import { Fascicolo } from '../../../a4g-common/classi/Fascicolo';
import { Router, ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-sintesi-fascicolo-list',
  templateUrl: './sintesi-fascicolo-list.component.html',
  styleUrls: ['./sintesi-fascicolo-list.component.css']
})
export class SintesiFascicoloListComponent implements OnInit {
  cols: any[];
  selectedFascicolo: Fascicolo;

  @Input()
  public fascicoli: Array<Fascicolo>;
  @Input()
  public visible: boolean;
  constructor(private router: Router, private service: MessageService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.cols = [
      { field: 'cuaa', header: 'CUAA' },
      { field: 'denominazione', header: 'Descrizione Impresa' }
    ];

  }

  onRowSelect(event) {
    // if (this.selectedFascicolo.stato.match('VALIDO|IN LAVORAZIONE|IN ANOMALIA')) {
    this.router.navigate([`../fascicolo/${this.selectedFascicolo.idFascicolo}/presentazioneIstanze`], { relativeTo: this.route });
    
    
    //this.router.navigate(['../../pippo/' + this.selectedFascicolo.idFascicolo]);
    
    // } else{
    //   this.service.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.CUAA_NON_ATTIVO(this.selectedFascicolo.cuaa)));
    //   return;
    // }
  }
}
