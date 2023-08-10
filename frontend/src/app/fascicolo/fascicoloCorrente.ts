import { FascicoloAgsDto } from 'src/app/a4g-common/classi/FascicoloAgsDto';
import { Injectable } from "@angular/core";
import { Fascicolo } from "../a4g-common/classi/Fascicolo";
import { FascicoloLazio } from 'src/app/a4g-common/classi/FascicoloLazio';

@Injectable({
    providedIn: "root"
})
export class FascicoloCorrente {
    public fascicolo: Fascicolo;
    public fascicoloLegacy: FascicoloAgsDto;
    public fascicoloLazio: FascicoloLazio;

    public isPersonaFisica(cuaa: string): boolean {
        return cuaa && cuaa.length === 16;
      }
}
