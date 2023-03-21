import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { DomandePsrStrutturaliService } from './domande-psr-strutturali.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { BackDetail } from '../../layout/back/back.component';
import { getIconForDatiPagamentoByTipologia } from './domande-psr-strutturali-utils';

@Injectable({ providedIn: 'root' })
export class DettagliDomandaPrsStrutturaleBackButtonResolver implements Resolve<any> {

  constructor(private domandePsrStrutturaliService: DomandePsrStrutturaliService) {
  }

  resolve(route: ActivatedRouteSnapshot,
          state: RouterStateSnapshot): Observable<BackDetail> {
    const idDomanda = +route.paramMap.get('idDomanda');
    const tipologia = route.paramMap.get('tipologia');

    return this.domandePsrStrutturaliService.getAziendaDaDomandaPSRStrutturale(idDomanda).pipe(
      map((domandaStrutturale) => {
        return {
          icon: route.data.backIcon || getIconForDatiPagamentoByTipologia(tipologia),
          title: route.data.backSubtitle || tipologia.toUpperCase(),
          subtitle: `CUAA: ${domandaStrutturale[0].cuaa}`
        };
      })
    );
  }
}
