import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { AntimafiaService } from '../antimafia.service';
import { DichiarazioneAntimafia } from '../classi/dichiarazioneAntimafia';

@Injectable()
export class DichiarazioneResolve implements Resolve<DichiarazioneAntimafia> {

  constructor(private antimafiaService: AntimafiaService) {}

  resolve(route: ActivatedRouteSnapshot) {
    console.log("Carica antimafia da db " + route.paramMap.get('idDichiarazione'));
    return this.antimafiaService.getDichiarazioneAntimafia(route.paramMap.get('idDichiarazione'));
    }
}