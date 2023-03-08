import { Injectable, Injector } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize, tap } from 'rxjs/operators';
import { LoaderService } from './loader.service';

@Injectable({
  providedIn: 'root'
})
export class LoaderInterceptorService implements HttpInterceptor {

  private counter = 0;

  constructor(private loaderService: LoaderService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if(req.headers.get('skipLoader')) {
      return next.handle(req);
    }

    if (!this.loaderService.isLoading) {
      this.loaderService.show();
    }
    this.counter++;
    // console.log('counter: ', this.counter);
    return next.handle(req).pipe(
      tap((event: HttpEvent<any>) => {
        if (event instanceof HttpResponse) {
          // this.hideLoader();
          // console.log('Http event: ', event);
        }
      }, (err: any) => {
        this.hideLoader();
      }),
      finalize(() => this.hideLoader()));
  }

  private hideLoader() {
    this.counter--;
    if (this.counter <= 0) {
      this.loaderService.hide();
    }
  }
}