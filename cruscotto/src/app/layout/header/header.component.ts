import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AppagService } from 'src/app/shared/services/appag-service';
const HOME_PATH = '/';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  public logoVisible: boolean;

  hideHeaderIcons = false;
  isFromAppag = false;
  isHomePage = false;

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private appagService: AppagService) {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(({ urlAfterRedirects }: NavigationEnd) => {
        this.isHomePage = this.router.url.split('?')[0] === HOME_PATH;

        const rt = this.getChildRoute(this.activatedRoute);
        this.hideHeaderIcons = rt.snapshot.data.hideHeaderIcons != null ? rt.snapshot.data.hideHeaderIcons : false;
      });

  }
  
  ngOnInit() {
    this.isFromAppag = this.appagService.isFromAppag();
  }

  getChildRoute(activatedRoute: ActivatedRoute) {
    if (activatedRoute.firstChild) {
      return this.getChildRoute(activatedRoute.firstChild);
    } else {
      return activatedRoute;
    }
  }

  onVisible(visible: boolean) {
    this.logoVisible = !visible;
  }

  goAppag() {
    this.appagService.returnAppag();
  }
}
