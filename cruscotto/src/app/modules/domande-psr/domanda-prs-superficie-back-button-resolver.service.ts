import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { BackDetail } from '../../layout/back/back.component';
import { DomandePsrService } from './domande-psr.service';

@Injectable({ providedIn: 'root' })
export class DomandaPrsSuperficieBackButtonResolverService implements Resolve<any> {

  constructor(private domandePsrStrutturaliService: DomandePsrService) {
  }

  resolve(route: ActivatedRouteSnapshot,
          state: RouterStateSnapshot): Observable<BackDetail> {
    const idDomanda = +route.paramMap.get('idDomanda');
    const tipologia = route.paramMap.get('tipologia');
    const codiceOperazione = route.paramMap.get('codiceOperazione');

    return this.domandePsrStrutturaliService.getDomandaPsrByNumeroDomanda(idDomanda).pipe(
      map((domandaSuperficie) => {
        const title = route.data.backSubtitle || tipologia.toUpperCase();

        return {
          icon: route.data.backIcon || 'assets/icons/svg/svg-psr-superficie/intervento' + codiceOperazione + '.svg',
          title: title.replace('_PAT', ''),
          subtitle: `CUAA: ${domandaSuperficie.cuaa}`
        };
      })
    );
  }
}
