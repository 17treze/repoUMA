import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { IFascicolo } from 'src/app/modules/domande-uniche/models/fascicolo.model';
import { DomandeUnicheService } from 'src/app/modules/domande-uniche/domande-uniche.service';
import { map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AziendaAgricolaService {
  private selectedCuaaSubject = new BehaviorSubject<IFascicolo>(null);

  constructor(
    private domandeUnicheService: DomandeUnicheService,
  ) { }


  getSelectedCuaa(cuaa?: string): Observable<IFascicolo> {
    if (cuaa == undefined) {
      return this.selectedCuaaSubject.asObservable();
    } else {
      if (!this.selectedCuaaSubject.getValue()) {
        return this.domandeUnicheService
          .getFascicoloCuaa(cuaa)
          .pipe(tap((fascicolo) => this.setSelectedCuaa(fascicolo)));
      }
      return this.selectedCuaaSubject.asObservable();
    }
  }

  setSelectedCuaa(data: IFascicolo) {
    this.selectedCuaaSubject.next(data);
  }
  
}
