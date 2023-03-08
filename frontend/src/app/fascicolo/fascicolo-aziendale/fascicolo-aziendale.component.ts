import { MandatoService } from 'src/app/fascicolo/mandato.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import * as moment from 'moment';
import { MessageService } from 'primeng-lts/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';

@Component({
  selector: 'app-fascicolo-aziendale',
  templateUrl: './fascicolo-aziendale.component.html',
  styleUrls: ['./fascicolo-aziendale.component.scss']
})
export class FascicoloAziendaleComponent implements OnInit {

  public showLinkElencoFascicoli = true;
  public showLinkCostituisci = true;
  public showLinkMigra = true;
  public showLinkRevoche = true;
  public showLinkRevocaOrdinaria = true;
  public showLinkTrasferimenti = true;
  public showLinkRiapri = false;
  public badgeRichiesteRevocaImmediata = 0;

  constructor(private router: Router, private messageService: MessageService, private route: ActivatedRoute, private mandatoService: MandatoService) { }

  ngOnInit() {
    this.messageService.clear();
    if (localStorage.getItem('selectedRole') === 'gestoreutenti') {
      this.router.navigate(['/funzioniPat/amministrazione'], { replaceUrl: true });
      return;
    }
    if (localStorage.getItem('selectedRole') === 'istruttoredu' || localStorage.getItem('selectedRole') === 'istruttoreamf'
      || localStorage.getItem('selectedRole') === 'viewer_altro_ente' || localStorage.getItem('selectedRole') === 'viewer_pat') {
      this.router.navigate(['/funzioniPat/gestioneIstruttoria'], { replaceUrl: true });
      return;
    }
    this.setShowLink();
    this.getRichiesteDaValutare(false);
  }

  private setShowLink() {
    if (localStorage.getItem('selectedRole') === 'responsabile_fascicolo_pat') {
      this.showLinkCostituisci = false;
      this.showLinkMigra = false;
      this.showLinkRevoche = true;
      this.showLinkRevocaOrdinaria = false;
      this.showLinkTrasferimenti = false;
    }
    if (localStorage.getItem('selectedRole') === 'caa') {
      this.showLinkRiapri = true;
    }
  }

  private getRichiesteDaValutare(valutata: boolean) {
    this.mandatoService.getRichiesteRevocheImmediate(valutata).subscribe(resp => {
      if (resp) {
        this.badgeRichiesteRevocaImmediata = resp.length;
      }
    }, err => {
      this.messageService.add(A4gMessages.getToast(
        'tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
    });
  }

  onRevocaOrdinaria() {
    const today = moment(new Date);
    if (today.isAfter(new Date().setFullYear(new Date().getFullYear(), 10, 30)) &&
      today.isSameOrBefore(moment(new Date().setFullYear(new Date().getFullYear(), 11, 31)))) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, 'Il nuovo mandato per revoca ordinaria può essere inserito nel periodo di tempo dal 01/01 al 30/11'));
    } else {
      this.router.navigate(['./revocaOrdinaria'], { relativeTo: this.route.parent.parent });
    }
  }

}
