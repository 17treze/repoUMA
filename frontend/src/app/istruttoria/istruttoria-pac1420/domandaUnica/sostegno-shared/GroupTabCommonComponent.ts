import { from, Subscription, Subject } from "rxjs";
import { map, mergeMap, takeUntil, filter } from "rxjs/operators";
import { ActivatedRoute, UrlTree, UrlSegment, Router, NavigationEnd } from "@angular/router";
import { MenuItem } from "primeng/api";
import { SostegnoDu } from "../classi/SostegnoDu";
import { IstruttoriaService } from "../istruttoria.service";
import { IstruttoriaDomandaUnicaFilter } from "../classi/IstruttoriaDomandaUnicaFilter";
import { Istruttoria } from "src/app/a4g-common/classi/Istruttoria";
import { StatoIstruttoriaEnum } from "../cruscotto-sostegno/StatoIstruttoriaEnum";
import { TipoIstruttoriaEnum } from "../classi/TipoIstruttoriaEnum";

export abstract class GroupTabCommonComponent {
    public menu2: Array<MenuItem>;

    // stato avanzamento processo 
    timeout: number = 30000;
    processiInCorsoLabel: string;
    processoDiControllo: Subscription;
    datiIstruttoriaCorrente: Istruttoria;

    public activeTab: MenuItem;
    public itemBadgesCount: { [k: string]: any } = {};
    protected componentDestroyed$: Subject<boolean> = new Subject();
    protected statoIstruttoria: StatoIstruttoriaEnum = undefined;

    constructor(
        protected sostegno: SostegnoDu,
        protected tipoIstruttoriaEnum: TipoIstruttoriaEnum,
        protected activatedRoute: ActivatedRoute,
        protected istruttoriaService: IstruttoriaService,
        protected router: Router) {
        this.router.events.pipe(
            takeUntil(this.componentDestroyed$),
            filter(event => event instanceof NavigationEnd)
        ).subscribe(event => {
            let ne: NavigationEnd = <NavigationEnd>event;
            this.activeTab = this.getActiveTabMenuItem(ne.url);
        });
        //this.statoIstruttoria = this.activatedRoute.snapshot.paramMap.get('annoCampagna');
    }

    public changeTab(event) {
        this.activeTab = this.menu2[event.index];
        this.router.navigate([this.menu2[event.index].id], { relativeTo: this.activatedRoute });
        this.aggiornaContatori();
    }
    
    aggiornaContatori() {
        from(this.getListaStati()).pipe(
            map(stato => {
                let istruttoriaDomandaUnicaFilter: IstruttoriaDomandaUnicaFilter = new IstruttoriaDomandaUnicaFilter();
                istruttoriaDomandaUnicaFilter.campagna = Number(this.activatedRoute.snapshot.paramMap.get('annoCampagna'));
                istruttoriaDomandaUnicaFilter.sostegno = this.sostegno;
                istruttoriaDomandaUnicaFilter.tipo = this.tipoIstruttoriaEnum;
                istruttoriaDomandaUnicaFilter.stato = stato;
                return istruttoriaDomandaUnicaFilter;
            }),
            mergeMap(filter => {
                return this.istruttoriaService.countIstruttorieDU(filter).pipe(
                    map(count => {
                        this.itemBadgesCount[filter.stato] = count;
                    })
                );
            })
        ).subscribe();
    }

    public getChildUrlSegment(url: string): string {
        let urlTree: UrlTree = this.router.parseUrl(url);
        let urlSegment: UrlSegment = urlTree.root.children.primary.segments[6];
        if (urlSegment) {
            return urlSegment.path;
        }
        return "";
    }

    public getActiveTabMenuItem(url: string): MenuItem {
        let childUrlSegment = this.getChildUrlSegment(url);
        if (childUrlSegment.length > 0) {
            return this.menu2.find(elem => elem.id === childUrlSegment);
        } else return this.menu2[0];
    }

    getListaStati(): string[] {
        return this.menu2.map(x => x.id);
    }

    ngOnInit() {
        this.datiIstruttoriaCorrente = this.activatedRoute.snapshot.data['istruttoria'];
        this.aggiornaContatori();
      }
    
    ngOnDestroy(): void {
        this.componentDestroyed$.next(true);
        this.componentDestroyed$.complete();
        if (this.processoDiControllo) {
            this.processoDiControllo.unsubscribe();
        }
    }
}
