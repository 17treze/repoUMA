import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class DataService {

    public readonly richiesta = {

        allegato: {
            getted: new Subject<any>(),
            deleted: new Subject<any>(),
            uploaded: new Subject<any>(),
            listed: new Subject<any>()
        }
    }
    public readonly dichiarato = {

        allegato: {
            getted: new Subject<any>(),
            deleted: new Subject<any>(),
            uploaded: new Subject<any>(),
            listed: new Subject<any>()
        }
    }
}