import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';

import { HttpClient, HttpHeaders, HttpRequest, HttpEvent, HttpEventType, HttpErrorResponse } from '@angular/common/http';
import { catchError, last, map, tap } from 'rxjs/operators';

const httpOptions = {
  //headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class UploadService {
  constructor(private http: HttpClient
  ) { }


  public uploadFile(serviceUrl, uploadedFile, id): Observable<any> {
    console.log("Invoco upload");

    let input = new FormData();
    input.append('file', uploadedFile);
    input.append('id', id);

    return this.http.post<any>(serviceUrl, input, httpOptions).pipe(
      map(event => this.getEventMessage(event, uploadedFile)),
      tap(message => this.showProgress(message)),
      last(), // return last (completed) message to caller
      catchError(this.handleError('upload File'))
    );
  }

  /** Return distinct message for sent, upload progress, & response events */
  private getEventMessage(event: HttpEvent<any>, file: File) {
    console.log("Invoco getEventMessage " + event);

    switch (event.type) {
      case HttpEventType.Sent:
        return `Uploading file "${file.name}" of size ${file.size}.`;

      case HttpEventType.UploadProgress:
        // Compute and show the % done:
        const percentDone = Math.round(100 * event.loaded / event.total);
        return `File "${file.name}" is ${percentDone}% uploaded.`;

      case HttpEventType.Response:
        return `File "${file.name}" was completely uploaded!`;

      default:
        return `File "${file.name}" surprising upload event: ${event.type}.`;
    }
  }

  showProgress(arg0: any): any {
    //throw new Error("Method not implemented.");
    console.log(arg0);
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      console.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

}