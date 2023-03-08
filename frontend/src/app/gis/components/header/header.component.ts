import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { ConfirmationService } from 'primeng/api';
import { Configuration } from 'src/app/app.constants';
import { AuthService } from 'src/app/auth/auth.service';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'gis-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class HeaderComponent implements OnInit {
  topbarMenuActive: false;
  topbarMenuClick: boolean;
  activeTopbarItem: Element;
  userId: string;
  private _serviceSubscription: any;

  constructor(private authService: AuthService, private router: Router, private configuration: Configuration,
    private confirmationService: ConfirmationService) {
    this._serviceSubscription = this.authService.onUserChange.subscribe(
      (next) => {
          this.userId = next.identificativo;
      }
  );
  }

  ngOnInit() {
    this.authService.getUserNew(false).subscribe((user) => {
      this.userId = user.identificativo;
    });
  }

  redirectModificaUtente() {
    this.router.navigate(['/utenti/modificaUtente']);
  //   this.confirmationService.confirm({
  //     message: "L'utente " + this.userId +  " ha già un profilo associato. Modificare il profilo corrente",
  //     header: 'Attenzione',
  //     key: 'dialogUtente',
  //     icon: 'pi pi-exclamation-triangle',
  //     accept: () => {
  //       setTimeout(() => {
  //         this.router.navigate(['/utenti/modificaUtente']);
  //         console.log( this.router.navigate(['/utenti/modificaUtente']))
  //       });
  //     },
  //     reject: () => {
  //       return;
  //   }
  // });
  }

  onTopbarRootItemClick(event: Event, item: Element) {
    if (this.activeTopbarItem === item) {
      this.activeTopbarItem = null;
    } else {
      this.activeTopbarItem = item;
    }

    event.preventDefault();
  }

  onWrapperClick() {
    this.topbarMenuActive = false;
    this.topbarMenuClick = false;
    // this.topbarMenuButtonClick = false;
  }

  onTopbarMenuClick(event: Event) {
    this.topbarMenuClick = true;
  }
  
}
