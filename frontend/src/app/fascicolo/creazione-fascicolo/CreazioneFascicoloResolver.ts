import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { ImpresaDto } from './dto/ImpresaDto';
import { AnagraficaFascicoloService } from './anagrafica-fascicolo.service';
import { of } from 'rxjs';

@Injectable()
export class CreazioneFascicoloResolver implements Resolve<ImpresaDto> {

    constructor(private anagraficaFascicoloService: AnagraficaFascicoloService) { }

    resolve(route: ActivatedRouteSnapshot) {
        if (!this.anagraficaFascicoloService.anagraficaImpresa) {
            return this.anagraficaFascicoloService.getAnagraficaImpresa(route.paramMap.get('cuaa'));
        } else {
            return of(this.anagraficaFascicoloService.anagraficaImpresa);
        }
    }
}