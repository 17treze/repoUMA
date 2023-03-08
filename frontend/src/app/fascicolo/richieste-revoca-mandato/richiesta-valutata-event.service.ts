import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";

@Injectable()
export class RichiestaValutataEventService {
    private subject = new Subject<boolean>();

    public sendEvent(message: boolean) {
        this.subject.next(message);
    }

    public getEventObservable(): Observable<any> {
        return this.subject.asObservable();
    }
}