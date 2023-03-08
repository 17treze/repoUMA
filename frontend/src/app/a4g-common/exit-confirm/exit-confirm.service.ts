import { Injectable } from "@angular/core";
import { BehaviorSubject, merge, Observable, Subject } from "rxjs";
import { switchMap, tap } from "rxjs/operators";
import { Actions } from "./actions.model";

@Injectable({
    providedIn: "root",
})
export class ExitConfirmService {
    private _visible$ = new BehaviorSubject(false);
    private _actions$ = new Subject<Actions>();
    private _saved$ = new Subject<void>();
    private _discarded$ = new Subject<void>();
    private _cancelled$ = new Subject<void>();

    public get visible$(): Observable<boolean> {
        return this._visible$;
    }

    constructor() {
        this._actions$
            .pipe(
                tap(() => this._visible$.next(true)),
                switchMap((action) =>
                    merge(
                        this._saved$.pipe(tap(() => action.save?.())),
                        this._discarded$.pipe(tap(() => action.discard?.())),
                        this._cancelled$.pipe(tap(() => action.cancel?.()))
                    )
                ),
                tap(() => this._visible$.next(false))
            )
            .subscribe();
    }

    public ask(actions: Actions): void {
        this._actions$.next(actions);
    }

    public save(): void {
        this._saved$.next();
    }
    public discard(): void {
        this._discarded$.next();
    }
    public cancel(): void {
        this._cancelled$.next();
    }
}
