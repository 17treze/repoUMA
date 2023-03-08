import { Component, OnInit, Output, EventEmitter, Input, SimpleChanges } from '@angular/core';
import { Validators, FormBuilder, FormGroup, FormArray, AbstractControl } from '@angular/forms';
import { IstruttoriaCorrente } from '../../istruttoriaCorrente';
import { IstruttoriaService } from '../../istruttoria.service';
import { ConfirmationService } from 'primeng/api';
import { ActivatedRoute, Router } from '@angular/router';
import { Configuration } from 'src/app/app.constants';
import { InterventoDuPremio } from '../../classi/InterventoDuPremio';
import { SostegnoDuDi } from '../../classi/SostegnoDuDi';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { A4gMessages } from 'src/app/a4g-common/a4g-messages';
import { InterventoDuPremioAggregato } from '../../classi/InterventoDuPremioAggregato';

@Component({
  selector: 'app-dettaglio-istruttoria-dati-acz',
  templateUrl: './dettaglio-istruttoria-dati-acz.component.html',
  styleUrls: ['./dettaglio-istruttoria-dati-acz.component.css'],
  providers: [ConfirmationService]
})
export class DettaglioIstruttoriaDatiAczComponent implements OnInit {

  @Output() aggiornaDomandeCounters = new EventEmitter();
  production: boolean;
  controlloRicev = false;
  showSostegno = false;
  controllo: any;
  it: any;
  //gestione dei service per il recupero dei dati affidata al parent per questioni di visualizzazione/sincronizzazione dei dati
  @Input() premioAggregato: InterventoDuPremioAggregato;
  premiIntervento: Array<InterventoDuPremio> = new Array();
  @Output() aggiornaPremioAggregato = new EventEmitter();
  updateForm: FormGroup;

  editable = false; // variabile che rende editabile i dati di dettaglio istruttoria
  constructor(private istruttoriaCorrente: IstruttoriaCorrente, private fb: FormBuilder,
    private route: ActivatedRoute, private istruttoriaService: IstruttoriaService, private confirmationService: ConfirmationService,
    private router: Router, private conf: Configuration) {


  }

  ngOnInit() {

    this.it = {// imposto i valori del calendario
      firstDayOfWeek: 1,
      dayNames: ['domenica', 'lunedi', 'martedi', 'mercoledi', 'giovedi', 'venerdi', 'sabato'],
      dayNamesShort: ['dom', 'lun', 'mar', 'mer', 'gio', 'ven', 'sab'],
      dayNamesMin: ['Dom', 'Lun', 'Mar', 'Mer', 'Gio', 'Ven', 'Sab'],
      monthNames: ['Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio', 'Giugno', 'Luglio', 'Agosto', 'Settembre',
        'Ottobre', 'Novembre', 'Dicembre'],
      monthNamesShort: ['Gen', 'Feb', 'Mar', 'Apr', 'Mag', 'Giu', 'Lug', 'Ago', 'Sep', 'Ott', 'Nov', 'Dic'],
      today: 'Oggi',
      clear: 'Svuota'
    };
  }

  setSostegnoPremi(p: InterventoDuPremioAggregato) {
    this.premiIntervento = p.interventoDuPremi;

    this.premioAggregato.sostegnoDuDi.dtAperturaDomanda = new Date(this.premioAggregato.sostegnoDuDi.dtAperturaDomanda);
    this.premioAggregato.sostegnoDuDi.dtChiusuraDomanda = new Date(this.premioAggregato.sostegnoDuDi.dtChiusuraDomanda);
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
          premi: this.fb.array([this.fb.control(this.premioAggregato.interventoDuPremi[0].valoreUnitarioIntervento.toString(), Validators.required)])
        });
        for (let i: number = 1; i < this.premioAggregato.interventoDuPremi.length; i++) {
          this.addFeature(this.premioAggregato.interventoDuPremi[i].valoreUnitarioIntervento.toString());
        }
      }
    });

  }
  annullaModifica(): void {
    this.editable = false;
  }

  salva(): void {
    let fa: FormArray = this.updateForm.get('premi') as FormArray;
    let sostegnoPremi: SostegnoDuDi = new SostegnoDuDi();
    let premiAggregato: InterventoDuPremioAggregato = new InterventoDuPremioAggregato();
    let premi: Array<InterventoDuPremio> = new Array();
    let i: number = 0;
    if (A4gMessages.isUndefinedOrNull(this.premioAggregato.sostegnoDuDi.dtAperturaDomanda) || A4gMessages.isUndefinedOrNull(this.premioAggregato.sostegnoDuDi.dtChiusuraDomanda)) {
      this.throwError("Attenzione! I valori inseriti non risultano corretti.");
      return;
    }
    if ((this.premioAggregato.sostegnoDuDi.dtAperturaDomanda.getTime() > this.premioAggregato.sostegnoDuDi.dtChiusuraDomanda.getTime())) {
      this.throwError("Attenzione! La data di apertura domanda non può essere maggiore della data chiusura domanda.");
      return;
    }

    let valid: boolean = true;
    fa.controls.forEach(v => {
      if (!v.valid) {
        valid = v.valid;
      } else if (isNaN(v.value)) {
        valid = false;
      }
      let premio: InterventoDuPremio = new InterventoDuPremio();
      premio.valoreUnitarioIntervento = v.value;

      premio.intervento = this.premioAggregato.interventoDuPremi[i].intervento;
      premio.id = this.premioAggregato.interventoDuPremi[i].id;

      premi.push(premio);

      sostegnoPremi.dtAperturaDomanda = this.premioAggregato.sostegnoDuDi.dtAperturaDomanda;
      sostegnoPremi.dtChiusuraDomanda = this.premioAggregato.sostegnoDuDi.dtChiusuraDomanda;
      sostegnoPremi.identificativoSostegno = SostegnoDu.ZOOTECNIA;
      premiAggregato.interventoDuPremi = premi;
      premiAggregato.sostegnoDuDi = sostegnoPremi;
      i++;
    });

    if (!valid) {
      this.throwError("Attenzione! I valori inseriti non risultano corretti.");
    } else {
      this.aggiornaPremioAggregato.emit(premiAggregato);
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
    if (simpleChanges.premioAggregato.currentValue) {
      this.setSostegnoPremi(this.premioAggregato);
      this.editable = false;
    }
  }

}
