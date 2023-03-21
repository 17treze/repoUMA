import { PlatformLocation } from '@angular/common';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { isNullOrUndefined } from 'util';
import { Stack } from './stack';

export interface BackDetail {
  icon: string;
  title: string;
  subtitle: string;
}

@Component({
  selector: 'app-back',
  templateUrl: './back.component.html',
  styleUrls: ['./back.component.css']
})
export class BackComponent implements OnInit {

  static readonly ROUTE_DATA_BREADCRUMB = 'breadcrumb';
  static readonly ROUTE_DATA_BACK_DETAILS = 'backButtonDetail';

  @Output() visible = new EventEmitter<boolean>();
  public label: string = undefined;
  public details?: BackDetail;

  private stack = new Stack();


  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    location: PlatformLocation
    ) {
      this.router.events
        .pipe(filter(event => event instanceof NavigationEnd))
        .subscribe(({urlAfterRedirects}: NavigationEnd) => {

          const childRoute = this.getChildRoute(this.activatedRoute.root);

          this.details = childRoute.snapshot.data[BackComponent.ROUTE_DATA_BACK_DETAILS];
          this.label = this.findLabel(this.activatedRoute.root);

          const urlTree = this.router.parseUrl(urlAfterRedirects);
          const urlWithoutParams = urlTree.root.children['primary'] ? urlTree.root.children['primary'].segments.map(it => it.path).join('/') : urlAfterRedirects;
          if (urlAfterRedirects.includes(Stack.TAB_PARAM))
            this.stack.pushIfNotExist(urlAfterRedirects);
          else
            this.stack.pushIfNotExist(urlWithoutParams);
          this.visible.emit(!!this.label);
        });
      location.onPopState(() => {
          // pressed browser back
          this.stack.pop(); // simulo il back anche nello stack
      });

  }

  ngOnInit() {
    this.stack = new Stack();
  }

  private findLabel(route: ActivatedRoute):string {
    const children: ActivatedRoute[] = route.children;

    if (children.length === 0) {
      return ;
    }

    for (const child of children) {
      const label = child.snapshot.data[BackComponent.ROUTE_DATA_BREADCRUMB];
      if (!isNullOrUndefined(label)) {
        return label;
      }
      return this.findLabel(child);
    }
  }

  getChildRoute(activatedRoute: ActivatedRoute) {
    if (activatedRoute.firstChild) {
      return this.getChildRoute(activatedRoute.firstChild);
    } else {
      return activatedRoute;
    }
  }

  public goToPrevPage() {
    const currentPath = this.stack.pop();
    if (currentPath.includes(Stack.TAB_PARAM))
      this.stack.pop();
    
    this.router.navigateByUrl(this.stack.pop() || '', { relativeTo: this.activatedRoute });
  }


}
