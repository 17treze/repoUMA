import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { A4gCostanti } from 'src/app/a4g-common/a4g-costanti';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-menu-caa',
  templateUrl: './menu-caa.component.html',
  styleUrls: ['./menu-caa.component.css']
})

export class MenuCaaComponent implements OnInit {
  funzioni = Array<MenuItem>();
  title: String;

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.title = this.route.snapshot.data[A4gCostanti.ROUTE_DATA_BREADCRUMB].toUpperCase();
    this.funzioni = new Array<MenuItem>(
      { routerLink: 'fascicoloAziendale', label: 'Fascicolo Aziendale' },
      { routerLink: 'gestioneAzienda', label: 'Domande' },
      { routerLink: 'scadenze', label: 'Scadenze' }
    );
  }
}
