import { Component, OnInit } from '@angular/core';
import { InterventoDuPremioAggregato } from '../../../../classi/InterventoDuPremioAggregato';
import { MessageService } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';
import { ConfigurazioneIstruttoriaService } from '../../shared/configurazione.istruttoria.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InterventoDuPremio } from "../../../../classi/InterventoDuPremio";
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';

@Component({
  selector: 'app-istruttoria-acz',
  templateUrl: './istruttoria-acz.component.html',
  styleUrls: ['./istruttoria-acz.component.css']
})
export class IstruttoriaAczComponent implements OnInit {

  public interventoAggregato: InterventoDuPremioAggregato;
  public interventiVaccheDaLatte: Array<InterventoDuPremio>;
  public interventiMacellazione: Array<InterventoDuPremio>;
  public interventiOviCaprini: Array<InterventoDuPremio>;
  public edit: boolean = false;
  public importiForm: FormGroup;
  public form1Valid: boolean = true;
  public form2Valid: boolean = true;
  public form3Valid: boolean = true;

  private annoCampagna: number;
  private valoriVaccheDaLatte: Array<string> = ["310", "311", "313", "322"]
  private valoriMacellazione: Array<string> = ["315", "316", "318"];
  private valoriOviCaprini: Array<string> = ["320", "321"];

  constructor(
    private messageService: MessageService,
    private route: ActivatedRoute,
    private confIstruttoriaService: ConfigurazioneIstruttoriaService,
    private fb: FormBuilder
  ) { }

  ngOnInit() {
    this.annoCampagna = this.route.snapshot.params.annoCampagna;
    this.caricaArrayDaServizio();
  }

  private caricaArrayDaServizio() {
    this.confIstruttoriaService.getConfigurazioneIstruttoriaAcz(this.annoCampagna)
      .subscribe(
        x => {
          this.interventoAggregato = x;

          //Temporaneo: elimina i valori non presenti nelle tabelle
          this.eliminaValoriNonUtili();

          this.interventiVaccheDaLatte = this.caricaArrayVaccheDaLatte();
          this.interventiMacellazione = this.caricaArrayMacellazione();
          this.interventiOviCaprini = this.caricaArrayOviCaprini();
          this.interventoAggregato.interventoDuPremi = [...this.interventiVaccheDaLatte, ...this.interventiMacellazione, ...this.interventiOviCaprini]

          //Ordino per priorità il valori da vilsualizzare in tabella
          // this.ordinaPerPriorita(this.interventoAggregato.interventoDuPremi);
          this.ordinaPerPriorita(this.interventiVaccheDaLatte);
          this.ordinaPerPriorita(this.interventiMacellazione);
          this.ordinaPerPriorita(this.interventiOviCaprini);

          this.createForm();
        },
        erro => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        });
  }

  private caricaArrayVaccheDaLatte(): Array<InterventoDuPremio> {
    let array = [...this.interventoAggregato.interventoDuPremi];
    let i = 0;
    let max = array.length;
    for (i = max - 1; i >= 0; i--) {
      let codiceAgea = array[i].intervento.codiceAgea;
      if (!this.valoriVaccheDaLatte.includes(codiceAgea)) {
        array.splice(i, 1)
      }
    }
    return array;
  }

  private caricaArrayMacellazione(): Array<InterventoDuPremio> {
    let array = [...this.interventoAggregato.interventoDuPremi];
    let i = 0;
    let max = array.length;
    for (i = max - 1; i >= 0; i--) {
      let codiceAgea = array[i].intervento.codiceAgea;
      if (!this.valoriMacellazione.includes(codiceAgea)) {
        array.splice(i, 1)
      }
    }
    return array;
  }

  private caricaArrayOviCaprini(): Array<InterventoDuPremio> {
    let array = [...this.interventoAggregato.interventoDuPremi];
    let i = 0;
    let max = array.length;
    for (i = max - 1; i >= 0; i--) {
      let codiceAgea = array[i].intervento.codiceAgea;
      if (!this.valoriOviCaprini.includes(codiceAgea)) {
        array.splice(i, 1)
      }
    }
    return array;
  }

  private createForm() {
    this.importiForm = this.fb.group({
      importiOviCaprini: this.fb.array(this.interventiOviCaprini.map(datum => this.generateDatumFormGroup(datum)))
    });
  }

  private generateDatumFormGroup(datum) {
    return this.fb.group({
      codiceAgea: this.fb.control(datum.intervento.codiceAgea),
      priorita: this.fb.control(datum.priorita),
      valoreUnitarioIntervento: this.fb.control(datum.valoreUnitarioIntervento, [Validators.required, Validators.min(0), Validators.pattern(/^-?\d{0,3}(\.?\d{0,2})$/)])
    });
  }

  public modifica() {
    this.edit = true;
  }

  public annulla() {
    this.edit = false;
    this.caricaArrayDaServizio();
  }

  public salva() {
    if (!this.form1Valid || !this.form2Valid || !this.form3Valid)
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, 'aaa'));
    else
      this.messageService.add({ key: 'checkSalvataggio', sticky: true, severity: 'warn', summary: 'Operazione di Salvataggio', detail: 'Sei sicuro di voler salvare?' });
  }

  public confermaSalvataggio() {
    this.edit = false;
    this.messageService.clear('checkSalvataggio');
    let arrayOviCaprini = this.importiForm.controls.importiOviCaprini.value;
    let i = 0;
    for (i = 0; i < arrayOviCaprini.length; i++) {
      if (typeof arrayOviCaprini[i].valoreUnitarioIntervento === "string") {
        let numero = Number(arrayOviCaprini[i].valoreUnitarioIntervento);
        let intervento = this.interventoAggregato.interventoDuPremi.find(intervento => intervento.intervento.codiceAgea === arrayOviCaprini[i].codiceAgea);
        intervento.valoreUnitarioIntervento = numero;
      }
    }
    this.confIstruttoriaService.setConfigurazioneIstruttoriaAcz(this.annoCampagna, this.interventoAggregato)
      .subscribe(
        x => {
          this.interventoAggregato = x;
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
          window.location.reload();
        },
        err => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.salvataggioDatiKo));
        });
  }

  public rejectSalvataggio() {
    this.messageService.clear('checkSalvataggio');
  }

  onSubmit() {
    console.log(this.importiForm.value);
    console.log(this.interventoAggregato);
    console.log(this.importiForm.valid);
  }

  ordinaPerPriorita(interventoDuPremi: Array<InterventoDuPremio>) {
    interventoDuPremi.sort(function (a, b) {
      var nameA = a.priorita;
      var nameB = b.priorita;
      if (nameA < nameB) {
        return -1;
      }
      if (nameA > nameB) {
        return 1;
      }
      return 0;
    });
  }

  public eliminaValoriNonUtili() {
    this.edit = false;

    let array = this.interventoAggregato.interventoDuPremi;
    let i = 0;
    let max = array.length;
    for (i = max - 1; i >= 0; i--) {
      let codiceAgea = array[i].intervento.codiceAgea;
      if (!this.valoriVaccheDaLatte.includes(codiceAgea) && !this.valoriMacellazione.includes(codiceAgea) && !this.valoriOviCaprini.includes(codiceAgea)) {
        this.interventoAggregato.interventoDuPremi.splice(i, 1)
      }
    }
  }

  setValidForm1(validForm: boolean) {
    this.form1Valid = validForm;
  }

  setValidForm2(validForm: boolean) {
    this.form2Valid = validForm;
  }

  setValidForm3(validForm: boolean) {
    this.form3Valid = validForm;
  }

}
