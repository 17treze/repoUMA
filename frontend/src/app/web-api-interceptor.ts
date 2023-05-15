import { environment } from '../environments/environment';
import {
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './auth/auth.service';

@Injectable()
export class WebApiInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.headers.has('Authorization')) {
      return next.handle(req);
    }

  //  const Authorization = 'Bearer '+sessionStorage.getItem("access_token");

  
    const Authorization = 'Bearer '+this.authService.getAccessToken();
   /*
    let headers = new HttpHeaders();
    headers = new HttpHeaders({ 'Access-Control-Allow-origin': environment.frontendUrl, 'Authorization': accessToken ?? '' });
   */
    let accessControlHeader = new HttpHeaders();
    accessControlHeader = new HttpHeaders({ 'Access-Control-Allow-origin': environment.frontendUrl});

    const clonedReq = req.clone({
      setHeaders: {Authorization},
      headers: accessControlHeader
    });

    return next.handle(clonedReq);
  }
}
