import { ActivatedRoute, Params } from "@angular/router";
import { catchError, map, takeUntil, tap } from "rxjs/operators";
import { combineLatest, Observable, Subject, throwError } from "rxjs";


export class ActivateRouteSupport {

  public static findParamsAndExecute(callback: (params: Params) => void,
                                     componentDestroyed$: Subject<boolean>,
                                     route: ActivatedRoute) {
    route.params.pipe(
      tap(
        (queryParams) => callback(queryParams)
      ),
      takeUntil(componentDestroyed$),
    ).subscribe(queryParams => {
      console.log("queryParams", queryParams);
    }, error => {
      console.log("error", error);
    });
  }

  public static findQueryParamsAndExecute(callback: (params: Params) => void,
                                          componentDestroyed$: Subject<boolean>,
                                          route: ActivatedRoute) {
    route.queryParams.pipe(
      tap(
        (queryParams) => callback(queryParams)
      ),
      takeUntil(componentDestroyed$),
    ).subscribe(queryParams => {
      console.log("queryParams", queryParams);
    }, error => {
      console.log("error", error);
    });
  }

  public static observeParams(componentDestroyed$: Subject<boolean>,
                              route: ActivatedRoute): Observable<{ path: Params, query: Params}> {
    return combineLatest([route.params, route.queryParams]).pipe(
      catchError(err => {
        console.log('error', err);
        return throwError(err);
      }),
      tap(([path, query]) => {
        console.log('Path: ', path, 'Query: ', query);
      }),
      takeUntil(componentDestroyed$),
      map(([path, query]) => {
        return { path, query };
      })
    );
  }

}
