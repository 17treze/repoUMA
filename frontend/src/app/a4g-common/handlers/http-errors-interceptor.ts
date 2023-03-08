import { Injectable } from "@angular/core";
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from "@angular/common/http";
import { EMPTY, Observable, throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import { MessageService } from "primeng/api";
import { TranslateService } from "@ngx-translate/core";
import { A4gMessages, A4gSeverityMessage } from "../a4g-messages";

@Injectable({
    providedIn: 'root'
})
export class HttpErrorsInterceptor implements HttpInterceptor {
    constructor(
        private messageService: MessageService,
        private translateService: TranslateService
    ) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if(request.headers.get('skipErrors')) {
            return next
                .handle(request)
                .pipe(
                    catchError(error => EMPTY)
                );
        }

        return next
            .handle(request)
            .pipe(
                catchError((error: any) => {
                    if(error instanceof HttpErrorResponse) {
                        const response = error as HttpErrorResponse;

                        // https://github.com/angular/angular/issues/19888
                        if(response.error instanceof Blob && response.error.type === "application/json") {
                            return new Promise<any>((resolve, reject) => {
                                const reader = new FileReader();

                                reader.onload = (event: Event) => {
                                    try {
                                        const message = JSON.parse((<any>event.target).result);
                                        const newResponse = {
                                            ...response,
                                            error: message
                                        };

                                        reject(newResponse);
                                    } catch(exception) {
                                        reject(response);
                                    }
                                }
                                reader.onerror = (event: Event) => {
                                    reject(response);
                                };

                                reader.readAsText(response.error);
                            })
                        }

                        // Se non autorizzato
                        if(response.status === 403) {
                            const text = this.translateService.instant("HttpErrorMessages.403");
                            const message = A4gMessages.getToast('tst', A4gSeverityMessage.error, text);

                            this.messageService.add(message);
                        }

                        // Se errore lato client
                        /*if(response.status === 0) {
                            window.location.reload();

                            return EMPTY;
                        }*/
                    }

                    return throwError(error);
                })
            );
    }
}