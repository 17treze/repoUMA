import { ErrorHandler, Injectable, NgZone } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Injector } from '@angular/core';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
    constructor(
        private injector: Injector) {
    }

    handleError(error: HttpErrorResponse) {
        let router: Router = this.injector.get(Router);
        const ngZone = this.injector.get(NgZone);
        console.log(error);
        if (error.status != null) {
            ngZone.run(() => {
                router.navigate(['/errorPage/' + (error.status.toString())], { skipLocationChange: true });
            });
        };
    }
}