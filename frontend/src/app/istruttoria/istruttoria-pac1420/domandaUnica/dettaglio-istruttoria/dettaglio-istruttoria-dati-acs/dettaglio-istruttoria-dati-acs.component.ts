import { Component, OnInit, Output, EventEmitter, Input, SimpleChanges } from '@angular/core';
import { InterventoDuPremioAggregato } from '../../classi/InterventoDuPremioAggregato';
import { InterventoDuPremio } from '../../classi/InterventoDuPremio';
import { FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { ConfirmationService } from 'primeng/api';
import { A4gMessages } from 'src/app/a4g-common/a4g-messages';

@Component({
  selector: 'app-dettaglio-istruttoria-dati-acs',
  templateUrl: './dettaglio-istruttoria-dati-acs.component.html',
  styleUrls: ['./dettaglio-istruttoria-dati-acs.component.css'],
  providers: [ConfirmationService]
})
export class DettaglioIstruttoriaDatiAcsComponent implements OnInit {
  @Output() aggiornaDomandeCounters = new EventEmitter();
  production: boolean;
  controlloRicev = false;
  showSostegno = false;
  controllo: any;
  //gestione dei service per il recupero dei dati affidata al parent per questioni di visualizzazione/sincronizzazione dei dati
  @Input() premioAggregatoACS: InterventoDuPremioAggregato;
  premiIntervento: Array<InterventoDuPremio> = new Array();
  @Output() aggiornaPremioAggregatoACS = new EventEmitter();
  updateForm: FormGroup;

  editable = false; // variabile che rende editabile i dati di dettaglio istruttoria
  constructor(private fb: FormBuilder, private confirmationService: ConfirmationService) { }

  ngOnInit() {

  }

  setSostegnoPremi(p: InterventoDuPremioAggregato) {
    this.premiIntervento = p.interventoDuPremi;
  }

  get premi(): FormArray {
    return this.updateForm.get('premi') as FormArray;
  }

  addFeature(formState: string): void {
    this.premi.push(this.fb.control(formState, [Validators.required, Validators.pattern(/^-?\d{0,9}(\.?\d{0,2})$/)]));
  }

  isValid(i: number): boolean {
    let fa: FormArray = <FormArray>this.updateForm.get('premi');


    return fa.controls[i].valid;
  }

  modifica(): void {
    this.confirmationService.confirm({
      message: 'Attenzione! Sei davvero sicuro di voler modificare questi dati?',
      acceptLabel: 'Sì',
      rejectLabel: 'No',
      rejectVisible: true,
      accept: () => {
        this.editable = true;


        this.updateForm = this.fb.group({
          premi: this.fb.array([this.fb.control(this.premioAggregatoACS.interventoDuPremi[0].valoreUnitarioIntervento.toString(), Validators.required)])
        });
        for (let i: number = 1; i < this.premioAggregatoACS.interventoDuPremi.length; i++) {
          this.addFeature(this.premioAggregatoACS.interventoDuPremi[i].valoreUnitarioIntervento.toString());
        }
      }
    });

  }
  annullaModifica(): void {
    this.editable = false;
  }

  salva(): void {
    let fa: FormArray = this.updateForm.get('premi') as FormArray;
    let premiAggregato: InterventoDuPremioAggregato = new InterventoDuPremioAggregato();
    let premi: Array<InterventoDuPremio> = new Array();
    let i: number = 0;

    let valid: boolean = true;
    fa.controls.forEach(v => {
      if (!v.valid) {
        valid = v.valid;
      } else if (isNaN(v.value)) {
        valid = false;
      }
      let premio: InterventoDuPremio = new InterventoDuPremio();
      premio.valoreUnitarioIntervento = v.value;

      premio.intervento = this.premioAggregatoACS.interventoDuPremi[i].intervento;
      premio.id = this.premioAggregatoACS.interventoDuPremi[i].id;

      premi.push(premio);

      premiAggregato.interventoDuPremi = premi;
      i++;
    });

    if (!valid) {
      this.throwError("Attenzione! I valori inseriti non risultano corretti.");
    } else {
      this.aggiornaPremioAggregatoACS.emit(premiAggregato);
    }

  }

  throwError(message: string) {
    this.confirmationService.confirm({
      message: message,
      acceptLabel: 'Annulla',
      rejectVisible: false,
      accept: () => {
        this.editable = true;
      }
    });
  }

  ngOnChanges(simpleChanges: SimpleChanges) {
    if (simpleChanges.premioAggregatoACS.currentValue) {
      this.setSostegnoPremi(this.premioAggregatoACS);
      this.editable = false;
    }
  }

}
