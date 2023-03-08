import { FascicoloAgsDto } from 'src/app/a4g-common/classi/FascicoloAgsDto';
import { Injectable } from "@angular/core";
import { Fascicolo } from "../a4g-common/classi/Fascicolo";

@Injectable({
    providedIn: "root"
})
export class FascicoloCorrente {
    public fascicolo: Fascicolo;
    public fascicoloLegacy: FascicoloAgsDto;

    public isPersonaFisica(cuaa: string): boolean {
        return cuaa && cuaa.length === 16;
      }
}