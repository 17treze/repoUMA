import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs';
import { switchMap, takeUntil } from 'rxjs/operators';
import { FascicoloDettaglioService } from 'src/app/shared/services/fascicolo-dettaglio.service';
import { FascicoloDaCuaa } from '../models/FascicoloCuaa';

@Component({
  selector: 'app-container-fascicolo-dettaglio',
  templateUrl: './container-fascicolo-dettaglio.component.html',
  styleUrls: ['./container-fascicolo-dettaglio.component.css']
})
export class ContainerFascicoloDettaglioComponent implements OnInit {

  private cuaa = '';
  protected componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    private fascicoloDettaglioService: FascicoloDettaglioService,
    protected route: ActivatedRoute
  ) { }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  ngOnInit() {
    this.route.params.pipe(
      switchMap(params => {
        this.cuaa = params.cuaa;
        return this.route.queryParams;
      }),
      takeUntil(this.componentDestroyed$),
      ).subscribe(queryParams => {
        let fascicoloCorrente: FascicoloDaCuaa = new FascicoloDaCuaa();
        fascicoloCorrente.cuaa = this.cuaa;
        this.fascicoloDettaglioService.fascicoloCorrente.next(fascicoloCorrente);
      });
  }

}
