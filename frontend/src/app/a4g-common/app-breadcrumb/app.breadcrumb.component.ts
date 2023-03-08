import { Component, OnDestroy, OnInit } from '@angular/core';

import { Injectable } from '@angular/core';

import { Router, ActivatedRoute, NavigationEnd, PRIMARY_OUTLET, NavigationStart } from "@angular/router";

import { filter, takeUntil } from 'rxjs/operators';
import { A4gCostanti } from '../a4g-costanti';
import { Configuration } from '../../../app/app.constants';
import { IBreadcrumb } from '../classi/IBreadcrumb';
import { AppBreadcrumbService } from './app.breadcrumb.service';
import { Subject } from 'rxjs';
@Component({
    selector: 'app-breadcrumb',
    templateUrl: './app.breadcrumb.component.html',
    styleUrls: ['./app.breadcrumb.component.css']
})
@Injectable()
export class AppBreadcrumbComponent implements OnInit, OnDestroy {
    public breadcrumbs: IBreadcrumb[];
    home: IBreadcrumb;
    protected componentDestroyed$: Subject<boolean> = new Subject();
    isGis: boolean;

    /**
     * @class DetailComponent
     * @constructor
     */
    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private configuration: Configuration,
        private breadcrumbService: AppBreadcrumbService
    ) {
        this.breadcrumbs = [];
        this.home = {
            params: null,
            label: null,
            url: configuration.FrontendUrl + 'home'
        }
        this.router.events.subscribe((event: any) => {
            if (event instanceof NavigationEnd) {
              if (event.url === '/gis' || event.url === '/funzioniPat/gis' || event.url === '/funzioniCaa/gis') {
                this.isGis = true;
              } else {
                this.isGis = false;
              }
            }
          });
    }

    /**
     * Let's go!
     *
     * @class DetailComponent
     * @method ngOnInit
     */
    ngOnInit() {
        // Navigation Back
        this.breadcrumbService.breadCrumbUpdateSteps$
        .pipe(takeUntil(this.componentDestroyed$))
        .subscribe((steps: number) => {
            let numOfBreadCrumbsToRemove = Number(steps);
            if (!isNaN(numOfBreadCrumbsToRemove)) {
                while (numOfBreadCrumbsToRemove > 0) {
                    // rimuovo tante breadcrumbs quanti sono gli step passati in input
                    this.breadcrumbs.pop();
                    numOfBreadCrumbsToRemove--;
                }
            }
        });
        // Navigation Start
        this.router.events.pipe(
            takeUntil(this.componentDestroyed$),
            filter(event => event instanceof NavigationStart)
        ).subscribe((event: NavigationStart) => {
            // se la navigazione è all'indietro 
            // (location.back() o history.back() o freccia indietro del browser)  
            // elimino ultima breadcrumb
            if (event.navigationTrigger === 'popstate') {
                this.breadcrumbs.pop();
            }
        });
        // Navigation End
        this.router.events.pipe(
            takeUntil(this.componentDestroyed$),
            filter(event => event instanceof NavigationEnd)
        ).subscribe(() => {
            let root: ActivatedRoute = this.activatedRoute.root;
            // this.breadcrumbs = [...this.breadcrumbs, ...this.getBreadcrumbs(root)];
            this.breadcrumbs = this.getBreadcrumbs(root);
        });
    }

    ngOnDestroy() {
        this.componentDestroyed$.next(true);
        this.componentDestroyed$.complete();
    }

    /**
     * Returns array of IBreadcrumb objects that represent the breadcrumb
     *
     * @class DetailComponent
     * @method getBreadcrumbs
     * @param {ActivateRoute} route
     * @param {string} url
     * @param {IBreadcrumb[]} breadcrumbs
     */
    private getBreadcrumbs(route: ActivatedRoute, url: string = "", breadcrumbs: IBreadcrumb[] = []): IBreadcrumb[] {
        // get the query parameters
        let queryParams = route.snapshot.queryParamMap.keys.map(qpKey => {
            const qpVal = route.snapshot.queryParamMap.get(qpKey);
            if (!qpVal) {
                return "";
            }
            return `${qpKey}=${qpVal}`;
        }).join("&");
        queryParams = queryParams ? `?${queryParams}` : "";
        //get the child routes
        let children: ActivatedRoute[] = route.children;
        //return if there are no more children
        if (children.length === 0) {
            return breadcrumbs;
        }
        //iterate over each children
        for (let child of children) {
            //verify primary route
            if (child.outlet !== PRIMARY_OUTLET) {
                continue;
            }
            const childRouteURL: string = child.snapshot.url.map(segment => segment.path).join("/");
            //append route URL to URL
            if (!childRouteURL) {
                return this.getBreadcrumbs(child, url, breadcrumbs);
            }
            //verify the custom data property "breadcrumb" is specified on the route
            if (!child.snapshot.data.hasOwnProperty(A4gCostanti.ROUTE_DATA_BREADCRUMB) || child.snapshot.data[A4gCostanti.ROUTE_DATA_BREADCRUMB] == '') {
                return this.getBreadcrumbs(child, url, breadcrumbs);
            }
            const routeURL: string = route.snapshot.url.map(segment => segment.path).join("/");
            const label: string = child.snapshot.data[A4gCostanti.ROUTE_DATA_BREADCRUMB];
            
            const baseUrl = this.configuration.FrontendUrl.endsWith('/') ? this.configuration.FrontendUrl.substring(0, this.configuration.FrontendUrl.length - 1) : this.configuration.FrontendUrl;
            const newUrlPart: string = [url, routeURL].filter(f => f).join('/');
            const completeUrl: string = [baseUrl, newUrlPart, childRouteURL, queryParams].filter(f => f).join('/');
            //get the route's URL segment
            let newbreadcrumb: IBreadcrumb = {
                label: label,
                params: child.snapshot.params,
                url: completeUrl
            };
            breadcrumbs.push(newbreadcrumb);
            return this.getBreadcrumbs(child, newUrlPart, breadcrumbs);
        }
    }

    public breadcrumbsPresente(): boolean {
        return this.breadcrumbs.length > 0;
    }
}
