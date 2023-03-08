import { Component, OnInit } from '@angular/core';
import { validaInput } from '../../../a4g-common/validazione/validaInput';
import { UtentiService } from '../../utenti.service';
import { NgForm } from '@angular/forms';
import { IParentInteraction } from '../../classi/IParentInteraction';
import { IChildInteraction } from '../../classi/IChildInteaction';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-titolare',
  templateUrl: './titolare.component.html',
  styleUrls: ['./titolare.component.css']//,
  /*styles: [`
  :host ::ng-deep .ui-message {
      margin-left: .25em;
  }
`]*/
})
export class TitolareComponent implements OnInit, IChildInteraction {
  index: number;
  selfRef: any;
  //interface for Parent-Child interaction
  compInteraction: IParentInteraction;
  isValid: boolean;
  codeResponsabilita: string;
  title: string;
  disabled: boolean;

  submitted: boolean;
  cfPersona: string;
  cuaa: string;
  denominazione: string;

  constructor(private utentiService: UtentiService, private messageService: MessageService) { }

  ngOnInit() {
    console.log('TitolareComponent cfPersona ' + this.cfPersona);
    this.isValid = false;
    this.submitted = false;
  }

  cuaachange(event: any) {
    this.denominazione = '';
    if (this.isValid) {
      this.isValid = false;
    }
    this.submitted = false;
  }

  onSubmit(f: NgForm) {
    this.submitted = true;
    if (f.invalid) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_DATI));
      return;
    }

    if (!(validaInput.campoNonValorizzato(this.cuaa))) {
      if (validaInput.validaCuaaIntero(this.cuaa, true), true) {
        this.utentiService.checkFascicoloValido(this.cuaa)
          .subscribe((next) => {
            console.log('Fascicolo valido ' + next);
            this.checkRappresentante();
          },
            error => {
              A4gMessages.handleError(this.messageService, error, A4gMessages.ERRORE_GENERICO),
                this.isValid = false;
            });
      } else {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_DATI));
      }
    } else {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_DATI));
    }
  }

  checkRappresentante() {
    this.utentiService.getAziendaRappresentata(this.cuaa, this.cfPersona)
      .subscribe((next) => {
        console.log('Rappresentante ok ' + next);
        this.isValid = true;
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        this.denominazione = next.mvalue;
      },
        error => {
          A4gMessages.handleError(this.messageService, error, A4gMessages.ERRORE_GENERICO),
            this.isValid = false;
        });
  }

  removeMe() {
    this.compInteraction.remove(this.index);
  }

  setDisabled(input: boolean) {
    this.disabled = input;
  }

}

