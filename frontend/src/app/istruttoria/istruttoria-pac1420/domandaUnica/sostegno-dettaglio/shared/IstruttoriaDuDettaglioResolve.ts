import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { IstruttoriaDomandaUnica } from '../../classi/IstruttoriaDomandaUnica';
import { IstruttoriaService } from '../../istruttoria.service';

@Injectable()
export class IstruttoriaDuDettaglioResolve implements Resolve<IstruttoriaDomandaUnica> {

    constructor(private istruttoriaService: IstruttoriaService) { }

    resolve(route: ActivatedRouteSnapshot) {
        return this.istruttoriaService.getIstruttoriaDU(route.paramMap.get('idIstruttoria'));
    }
}
