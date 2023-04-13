import { HttpClient } from "@angular/common/http";
import { EventEmitter, Output, Injectable} from "@angular/core";
import { EMPTY, Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { UTENTE_SERVER } from "src/app/app.constants";
import { Utente } from "src/app/modules/register/utente.model";

@Injectable()
export class AuthService {
    private urlGetSSO = `${UTENTE_SERVER}/utenti/utente`;
    @Output() onUserChange = new EventEmitter<Utente>();

    constructor(private http: HttpClient) {}

    public getUser(force: boolean = false): Observable<Utente> {
        if (force || !sessionStorage.getItem("user")) {
            return this.http.get<Utente>(this.urlGetSSO).pipe(
                catchError(err => {
                    console.error("Errore in recupero utente: " + err);
                    return EMPTY;
                }),
                tap(res => this.setUser(res))
            );
        } else {
            return of(JSON.parse(sessionStorage.getItem("user")));
        }
    }

    public setUser(user: Utente) {
        if (sessionStorage.getItem("user")) {
          let utenteLoggato: Utente = JSON.parse(sessionStorage.getItem("user"));
          if (utenteLoggato.identificativo != user.identificativo) {
              sessionStorage.clear();
          }
        } else {
          sessionStorage.clear();
        }
        sessionStorage.setItem("user", JSON.stringify(user));
        this.onUserChange.emit(user);
    }
}
