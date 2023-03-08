import { MessageService } from 'primeng/api';
import { Injectable } from '@angular/core';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { EMPTY, throwError } from 'rxjs';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';

@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  constructor(
    private messageService: MessageService
  ) { }

  public showError = (error: ErrorDTO, keyToast: string = 'tst') => {
    console.log("Errore: ", error);
    const e = error && error.error && error.error.message;
    const statusValid = error && error.error && error.error.status != 403;
    if (e && statusValid) {
      this.messageService.add(A4gMessages.getToast(keyToast, A4gSeverityMessage.error, e));
    }
  }

  public showErrorWithMessage = (message: string, keyToast: string = 'tst') => {
    console.log("Errore: ", message);
    if (message) {
      this.messageService.add(A4gMessages.getToast(keyToast, A4gSeverityMessage.error, message));
    }
  }

  public handleError = (error: ErrorDTO) => {
    console.log("Errore: ", error);
    const e = error && error.error && error.error.message;
    if (e) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, e));
    }
    return throwError(error);
  }

  public handleErrorAndStop = (error: ErrorDTO) => {
    console.log("Errore: ", error);
    const e = error && error.error && error.error.message;
    if (e) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, e));
    }
    return EMPTY;
  }

}
