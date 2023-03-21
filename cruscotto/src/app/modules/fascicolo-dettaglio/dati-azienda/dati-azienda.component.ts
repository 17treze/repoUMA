import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng-lts';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-dati-azienda',
  templateUrl: './dati-azienda.component.html',
  styleUrls: ['./dati-azienda.component.css']
})
export class DatiAziendaComponent implements OnInit {

  public menuItems: MenuItem[];
  private idValidazione = 0;
  protected componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    protected router: Router,
    protected route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.getIdValidazione();

    this.menuItems = [
      {
        label: 'Dati Anagrafe Tributaria',
        routerLink: 'anagrafe-tributaria',
        queryParams: this.idValidazione ? { 'id-validazione': this.idValidazione } : {},
        queryParamsHandling: 'merge'
      }, {
        label: 'Dati Camera di Commercio',
        routerLink: 'anagrafe-camera-commercio',
        queryParams: this.idValidazione ? { 'id-validazione': this.idValidazione } : {},
        queryParamsHandling: 'merge'
      }, {
        label: 'Persone con carica',
        routerLink: 'persone-carica',
        queryParams: this.idValidazione ? { 'id-validazione': this.idValidazione } : {},
        queryParamsHandling: 'merge'
      }, {
        label: 'Unita Tecnico Economiche',
        routerLink: 'unita-tecnico-economiche',
        queryParams: this.idValidazione ? { 'id-validazione': this.idValidazione } : {},
        queryParamsHandling: 'merge'
      }, {
        label: 'Modalità pagamento',
        routerLink: 'modalita-pagamento',
        queryParams: this.idValidazione ? { 'id-validazione': this.idValidazione } : {},
        queryParamsHandling: 'merge'
      },
    ];
  }

  private getIdValidazione() {
    this.route.queryParams.pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(queryParams => {
      const paramIdVal: string = queryParams['id-validazione'];
      if (paramIdVal) {
        this.idValidazione = Number.parseInt(paramIdVal);
      } else {
        this.idValidazione = 0;
      }
    });
  }

}
