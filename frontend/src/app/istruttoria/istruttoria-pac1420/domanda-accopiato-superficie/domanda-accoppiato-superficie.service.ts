import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Configuration } from "src/app/app.constants";
import { InterventoSuperficieDto } from "./classi/interventoSuperficieDto";
import { BehaviorSubject, Observable } from "rxjs";
import { Cached } from "src/assets/decorators/cached";

@Injectable({
    providedIn: 'root'
})
export class DomandaAccoppiatoSuperficieService {

    public totaleSuperficiePerInterventoBs = new BehaviorSubject<Array<InterventoSuperficieDto>>(null);
    public totaleSuperficiePerIntervento = this.totaleSuperficiePerInterventoBs.asObservable();

    constructor(private configuration: Configuration, private http: HttpClient) {}

    closeTotaleSuperficiePerIntervento() {
		this.totaleSuperficiePerInterventoBs.next(null);
    }
    
    @Cached()
    public getTotaleSuperficiePerIntervento(annoCampagna: number): Observable<InterventoSuperficieDto[]> {
        return this.http.get<Array<InterventoSuperficieDto>>(this.configuration.getUrlTotaleSuperficiePerIntervento(annoCampagna));
    }
}


