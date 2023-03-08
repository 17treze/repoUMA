import { Component, OnInit } from '@angular/core';
import { AppBreadcrumbService } from 'src/app/a4g-common/app-breadcrumb/app.breadcrumb.service';

@Component({
  selector: 'app-clienti-conto-terzi-container',
  templateUrl: './clienti-conto-terzi-container.component.html',
  styleUrls: ['./clienti-conto-terzi-container.component.scss']
})
export class ClientiContoTerziContainerComponent implements OnInit {

  constructor(
    private breadCrumbService: AppBreadcrumbService
  ) { }

  ngOnInit() {
 
  }

}
