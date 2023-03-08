import { NuovoAnnoCampagnaModel } from './../models/lavorazione/nuovoAnnoCampagna.model';
import { HttpClient } from '@angular/common/http';
import { GisCostants } from 'src/app/gis/shared/gis.constants';
import { MapService } from './../components/mappa/map.service';
import { Injectable, ElementRef } from '@angular/core';
import { catchError, } from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';

@Injectable()
export class GestioneCampagnaService {
    selectAnnoCampagna: HTMLButtonElement;
    constructor(private mapService: MapService, private gisConstants: GisCostants, private http: HttpClient) { }

    setColorCampagna(campagna) {
        const year = campagna;
        if (year) {
            const diff = year - 2022;
            const h = (diff * 10) + 270;
            return 'hsl(' + h + 'deg, 100%, 50%)';
        }
    }

    controlloAnnoCampagna(lav) {
        this.selectAnnoCampagna = document.querySelector('.select-campagna > select') as HTMLButtonElement;
        if (this.mapService.annoCampagna !== lav.campagna) {
            return false;
        } else {
            return true;
        }
    }

    setAnnoCampagna(annoCampagna) {
        if (annoCampagna) {
            if (annoCampagna != this.mapService.annoCampagna) {
                this.inputSelectAnnoCampagna();
                this.mapService.annoCampagna = annoCampagna;
                this.selectAnnoCampagna.dispatchEvent(new Event('change'));
            }
        }
    }

    inputSelectAnnoCampagna() {
        if (this.selectAnnoCampagna) {
            const options = this.selectAnnoCampagna['options'];
            for (let i = 0; i < options.length; i++) {
                if (options[i].label == this.mapService.annoCampagna) {
                    let element = this.selectAnnoCampagna;
                    element['value'] = options[i].label;
                    // tslint:disable-next-line: radix
                    return parseInt(options[i].label);
                }
            }
        }
    }

    disabilitaSelectAnnoCampagna() {
        this.selectAnnoCampagna = document.querySelector('.select-campagna > select') as HTMLButtonElement;
        if (this.selectAnnoCampagna) {
            this.selectAnnoCampagna.disabled = true;
        }
        console.log(this.selectAnnoCampagna);
    }

    attivaSelectAnnoCampagna() {
        this.selectAnnoCampagna = document.querySelector('.select-campagna > select') as HTMLButtonElement;
        if (this.selectAnnoCampagna) {
            this.selectAnnoCampagna.disabled = false;
        }
        console.log(this.selectAnnoCampagna);
    }

    cambioCampagna(idLavorazione, body): Observable<NuovoAnnoCampagnaModel> {
        const url = this.gisConstants.pathLavorazioneSuolo + '/' + idLavorazione + '/cambioCampagna';
        return this.http.put<object>(url, body, { observe: 'response' }).
            pipe(
                ((data: any) => {
                    return data;
                }), catchError(error => {
                    return throwError(error);
                })
            );
    }
}
