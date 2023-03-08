import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { IstruttoriaService } from '../istruttoria.service';
import { DomandaIstruttoriaDettaglio } from '../domain/domandaIstruttoriaDettaglio';

@Injectable()
export class DomandaIstruttoriaDettaglioResolve implements Resolve<DomandaIstruttoriaDettaglio> {

    constructor(private istruttoriaService: IstruttoriaService) { }

    resolve(route: ActivatedRouteSnapshot) {
        return this.istruttoriaService.getDettaglioDomanda(route.paramMap.get('idDomanda'), null, null);
    }
}
