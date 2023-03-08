import { Component, OnInit } from '@angular/core';
import { InterventoDuPremioAggregato } from '../../../../classi/InterventoDuPremioAggregato';
import { MessageService } from 'primeng/api';
import { ConfigurazioneIstruttoriaService } from '../../shared/configurazione.istruttoria.service';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-istruttoria-acs',
  templateUrl: './istruttoria-acs.component.html',
  styleUrls: ['./istruttoria-acs.component.css']
})
export class IstruttoriaAcsComponent implements OnInit {

  interventoAggregato: InterventoDuPremioAggregato;
  edit: boolean = false;
  numeroIstruttorie: number;
  annoCampagna: number;
  interventoForm: any;
  importiForm: FormGroup;

  constructor(private messageService: MessageService,
              private route: ActivatedRoute,
              private confIstruttoriaService: ConfigurazioneIstruttoriaService,
              private fb: FormBuilder) { }

  ngOnInit() {
   this.annoCampagna = this.route.snapshot.params.annoCampagna;
    this.confIstruttoriaService.getConfigurazioneIstruttoriaAcs(this.annoCampagna).subscribe(x => {
       this.interventoAggregato = x;
       this.createForm();
    });
  }

  private createForm() {
    this.importiForm = this.fb.group({
      importi: this.fb.array(this.interventoAggregato.interventoDuPremi.map(datum => this.generateDatumFormGroup(datum)))
    });
  }

  private generateDatumFormGroup(datum) {
    return this.fb.group({
      codiceAgea: this.fb.control(datum.intervento.codiceAgea),
      valoreUnitarioIntervento: this.fb.control(datum.valoreUnitarioIntervento, [Validators.required, Validators.min(0), Validators.pattern(/^-?\d{0,3}(\.?\d{0,2})$/)])
    });
  }
  
  public modifica() {
    this.edit = true;
  }

  public annulla() {
    this.edit = false;
    this.createForm();
  }

  public salva() {
    console.log(this.interventoAggregato);
    console.log(this.importiForm);
    this.messageService.add({ key: 'checkSalvataggio', sticky: true, severity: 'warn', summary: 'Operazione di Salvataggio', detail: 'Sei sicuro di voler salvare?' });
  }

  public confermaSalvataggio() {
    this.edit = false;

    let array = this.importiForm.controls.importi.value;
    let i = 0;
    for (i = 0; i < array.length; i++) {
      if (typeof array[i].valoreUnitarioIntervento === "string") {
        let numero = Number(array[i].valoreUnitarioIntervento);
        let intervento=this.interventoAggregato.interventoDuPremi.find(intervento => intervento.intervento.codiceAgea === array[i].codiceAgea);
        intervento.valoreUnitarioIntervento=numero;
      }
    }

    console.log(this.interventoAggregato);

    this.messageService.clear('checkSalvataggio');
    this.confIstruttoriaService.setConfigurazioneIstruttoriaAcs(this.annoCampagna, this.interventoAggregato).subscribe(x => {
      this.interventoAggregato = x;
    });
  }

  public rejectSalvataggio() {
    this.messageService.clear('checkSalvataggio');
  }
}
