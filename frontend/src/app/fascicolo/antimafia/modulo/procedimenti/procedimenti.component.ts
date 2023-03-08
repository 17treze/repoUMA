import { Component, OnInit, ViewChild } from "@angular/core";
import { StepActionComponent } from "../step-action/step-action.component";
import { AntimafiaValidationService } from "../antimafia-validation.service";
import { StatoValidazione } from "../../classi/statoValidazione";
import { MessageService } from "primeng/api";
import { A4gSeverityMessage, A4gMessages } from "src/app/a4g-common/a4g-messages";
import { DichiarazioneAntimafiaService } from "../../dichiarazione-antimafia.service";
import { DichiarazioneAntimafia } from "../../classi/dichiarazioneAntimafia";
import { AntimafiaService } from "../../antimafia.service";

@Component({
  selector: "app-procedimenti",
  templateUrl: "./procedimenti.component.html",
  styles: []
})
export class ProcedimentiComponent implements OnInit {
  @ViewChild(StepActionComponent, { static: true })
  private stepComponent: StepActionComponent;
  dichirazioneAntimafia: DichiarazioneAntimafia;

  constructor(
    private validator: AntimafiaValidationService,
    private messageService: MessageService,
    private dichiarazioneAntimafiaService: DichiarazioneAntimafiaService,
    private antimafiaService: AntimafiaService
    ) {}

  ngOnInit() {
    this.dichirazioneAntimafia = this.dichiarazioneAntimafiaService.getDichiarazioneAntimafia();
    this.antimafiaService.recuperaProcedimenti(this.dichirazioneAntimafia.id).subscribe(proc => {
      this.dichirazioneAntimafia.procedimenti = proc;
    });
  }

  conferma(userForm) {
    console.log('userForm.dirty: ',userForm.dirty);
    const validatorResponse = this.validator.validaProcedimenti(
      this.dichirazioneAntimafia.procedimenti
    );
    if (validatorResponse.esito === StatoValidazione.ERROR) {
      this.messageService.add(
        A4gMessages.getToast(
          "errorAssenzaProcedimento",
          A4gSeverityMessage.error,
          validatorResponse.messaggio
        )
      );
      return;
    }
    if (userForm.dirty) {
      //salvataggio dati
      this.antimafiaService.inserisciProcedimenti(this.dichirazioneAntimafia.id, this.dichirazioneAntimafia.procedimenti).subscribe();
    }
    this.stepComponent.goNext();
  }

}
