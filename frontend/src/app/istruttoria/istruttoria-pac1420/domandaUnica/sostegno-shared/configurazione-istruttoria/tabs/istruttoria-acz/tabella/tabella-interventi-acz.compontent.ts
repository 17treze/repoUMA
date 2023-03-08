import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { InterventoDuPremio } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/InterventoDuPremio';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { InterventoDuPremioAggregato } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/InterventoDuPremioAggregato';

@Component({
  selector: 'app-tabella-interventi-acz',
  templateUrl: './tabella-interventi-acz.compontent.html',
  styleUrls: ['./tabella-interventi-acz.compontent.css']
})
export class TabellaInterventiAczComponent implements OnInit {

  @Input() interventiControllo: Array<InterventoDuPremio>;
  @Input() interventoAggregato: InterventoDuPremioAggregato;
  @Input() edit: boolean;
  @Input() intestazioneTabella: string;
  @Output() formValid = new EventEmitter();

  public valoreUnitarioIntervento: Array<any>;
  public importiTabellaForm: FormGroup;

  private valoriFormValidi: boolean = true;

  constructor(private fb: FormBuilder) { }

  ngOnInit() {
    this.createForm();
    this.riordinaValoriPriorità();
    this.aggiornaValoreImporti();
  }

  private createForm() {
    this.importiTabellaForm = this.fb.group({
      importiControllo: this.fb.array(this.interventiControllo.map(datum => this.generateDatumFormGroup(datum)))
    });
  }

  private riordinaValoriPriorità() {
    let i = 0;
    for (i = 0; i < this.interventiControllo.length; i++) {
      this.interventiControllo[i].priorita = i + 1;
    }
  }

  private generateDatumFormGroup(datum) {
    return this.fb.group({
      codiceAgea: this.fb.control(datum.intervento.codiceAgea),
      priorita: this.fb.control(datum.priorita),
      valoreUnitarioInterventoTabella: this.fb.control(datum.valoreUnitarioIntervento, [Validators.required, Validators.min(0), Validators.pattern(/^-?\d{0,3}(\.?\d{0,2})$/)])
    });
  }

  ordinaArrayPerPriorita(interventoDuPremi: Array<InterventoDuPremio>) {
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

  public onRowReorder(event) {
    this.ordinaPrioritaTabella();
  }

  public onKey(event){
    this.aggiornaValoreImporti();
  }

  private ordinaPrioritaTabella() {
    let interventiControllo;
    interventiControllo = this.interventiControllo;

    let i = 0;
    for (i = 0; i < this.interventiControllo.length; i++) {
      let intervento = this.interventoAggregato.interventoDuPremi.find(intervento => intervento.intervento.codiceAgea === interventiControllo[i].intervento.codiceAgea);
      intervento.priorita = i + 1;
      interventiControllo[i].priorita = i + 1;
    }
  }

  private aggiornaValoreImporti() {
    const formV = this.importiTabellaForm;
    if (formV.invalid) {
      this.valoriFormValidi = false;
    } else {
      this.valoriFormValidi = true;
      let i = 0
      //aggiorno gli importi con i valori dei campi
      for (i = 0; i < this.importiTabellaForm.value.importiControllo.length; i++) {
        if (typeof this.importiTabellaForm.value.importiControllo[i].valoreUnitarioInterventoTabella === "string") {
          let numero = Number(this.importiTabellaForm.value.importiControllo[i].valoreUnitarioInterventoTabella);
          let codiceAgea = this.importiTabellaForm.value.importiControllo[i].codiceAgea;
          let intervento = this.interventoAggregato.interventoDuPremi.find(intervento => intervento.intervento.codiceAgea === codiceAgea);
          intervento.valoreUnitarioIntervento = numero;
        }
      }
    }
    this.formValid.emit(this.valoriFormValidi);
  }

}