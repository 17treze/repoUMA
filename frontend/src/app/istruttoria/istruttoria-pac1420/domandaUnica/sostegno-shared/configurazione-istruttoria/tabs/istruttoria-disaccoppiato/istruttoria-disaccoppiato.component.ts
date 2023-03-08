import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DettaglioIstruttoria } from '../../../../classi/DettaglioIstruttoria';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { MessageService } from 'primeng/api';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ConfigurazioneIstruttoriaService } from '../../shared/configurazione.istruttoria.service';

@Component({
  selector: 'app-istruttoria-disaccoppiato',
  templateUrl: './istruttoria-disaccoppiato.component.html',
  styleUrls: ['./istruttoria-disaccoppiato.component.css']
})

export class IstruttoriaDisaccoppiatoComponent implements OnInit {

  private annoCampagna: number;
  public dettaglioIstruttoria = new DettaglioIstruttoria;
  public edit: boolean = false;
  public importiForm: FormGroup;

  constructor(
    private confIstruttoriaService: ConfigurazioneIstruttoriaService,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private fb: FormBuilder
  ) { }

  ngOnInit() {
    this.annoCampagna = this.route.snapshot.params.annoCampagna;
    this.getDettaglioIstruttoria();
  }

  private getDettaglioIstruttoria() {
    this.confIstruttoriaService.getConfigurazioneIstruttoriaDisaccoppiato(this.annoCampagna)
      .subscribe(
        result => {
          if (result === null) {
            this.dettaglioIstruttoria.campagna = this.annoCampagna;
          } else {
            this.dettaglioIstruttoria.id = result.id;
            this.dettaglioIstruttoria.campagna = result.campagna;
            this.multiply100(result);
            this.dettaglioIstruttoria.limiteGiovane = result.limiteGiovane;
          }
          this.createForm();
        },
        error => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        }
      )
  }

  private createForm() {
    this.importiForm = new FormGroup({
      datiIstruttoria: new FormGroup({
        percentualeIncrementoGiovane: this.fb.control(this.dettaglioIstruttoria.percentualeIncrementoGiovane, [Validators.required, Validators.min(0), Validators.max(100), Validators.pattern(/^-?\d{0,3}(\.?\d{0,2})$/)]),
        percentualeIncrementoGreening: this.fb.control(this.dettaglioIstruttoria.percentualeIncrementoGreening, [Validators.required, Validators.min(0), Validators.max(100), Validators.pattern(/^-?\d{0,3}(\.?\d{0,2})$/)]),
        percentualeRiduzioneLineareArt51Par2: this.fb.control(this.dettaglioIstruttoria.percentualeRiduzioneLineareArt51Par2, [Validators.required, Validators.min(0), Validators.max(100), Validators.pattern(/^-?\d{0,3}(\.?\d{0,2})$/)]),
        percentualeRiduzioneLineareArt51Par3: this.fb.control(this.dettaglioIstruttoria.percentualeRiduzioneLineareArt51Par3, [Validators.required, Validators.min(0), Validators.max(100), Validators.pattern(/^-?\d{0,3}(\.?\d{0,2})$/)]),
        percentualeRiduzioneLineareMassimaleNetto: this.fb.control(this.dettaglioIstruttoria.percentualeRiduzioneLineareMassimaleNetto, [Validators.required, Validators.min(0), Validators.max(100), Validators.pattern(/^-?\d{0,3}(\.?\d{0,2})$/)]),
        percentualeRiduzioneTitoli: this.fb.control(this.dettaglioIstruttoria.percentualeRiduzioneTitoli, [Validators.required, Validators.min(0), Validators.max(100), Validators.pattern(/^-?\d{0,3}(\.?\d{0,2})$/)]),
        limiteGiovane: this.fb.control(this.dettaglioIstruttoria.limiteGiovane, [Validators.required, Validators.min(0), Validators.pattern(/^-?\d{0,3}(\.?\d{0,2})$/)])
      })
    });
  }

  public modifica() {
    this.edit = true;

  }
  public annulla() {
    this.edit = false;
    this.getDettaglioIstruttoria();
  }

  public salva() {
    this.messageService.add({ key: 'checkSalvataggio', sticky: true, severity: 'warn', summary: 'Operazione di Salvataggio', detail: 'Sei sicuro di voler salvare?' });
  }

  public confermaSalvataggio() {
    this.edit = false;
    this.messageService.clear('checkSalvataggio');
    this.dettaglioIstruttoria.limiteGiovane = this.importiForm.value.datiIstruttoria.limiteGiovane;
    this.dettaglioIstruttoria.percentualeIncrementoGiovane = this.importiForm.value.datiIstruttoria.percentualeIncrementoGiovane;
    this.dettaglioIstruttoria.percentualeIncrementoGreening = this.importiForm.value.datiIstruttoria.percentualeIncrementoGreening;
    this.dettaglioIstruttoria.percentualeRiduzioneLineareArt51Par2 = this.importiForm.value.datiIstruttoria.percentualeRiduzioneLineareArt51Par2;
    this.dettaglioIstruttoria.percentualeRiduzioneLineareArt51Par3 = this.importiForm.value.datiIstruttoria.percentualeRiduzioneLineareArt51Par3;
    this.dettaglioIstruttoria.percentualeRiduzioneLineareMassimaleNetto = this.importiForm.value.datiIstruttoria.percentualeRiduzioneLineareMassimaleNetto;
    this.dettaglioIstruttoria.percentualeRiduzioneTitoli = this.importiForm.value.datiIstruttoria.percentualeRiduzioneTitoli;
    this.setDettaglioIstruttoria(this.annoCampagna, this.dettaglioIstruttoria);
  }

  public rejectSalvataggio() {
    this.messageService.clear('checkSalvataggio');
  }

  private setDettaglioIstruttoria(annoCampagna: number, dettaglioIstruttoria: DettaglioIstruttoria) {
    this.divide100(dettaglioIstruttoria);
    this.confIstruttoriaService.setConfigurazioneIstruttoriaDisaccoppiato(annoCampagna, dettaglioIstruttoria)
      .subscribe(
        result => {
          this.multiply100(dettaglioIstruttoria);
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        },
        error => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.salvataggioDatiKo));
        }
      )
  }

  private divide100(dettaglioIstruttoria: DettaglioIstruttoria) {
    this.dettaglioIstruttoria.percentualeIncrementoGiovane = dettaglioIstruttoria.percentualeIncrementoGiovane / 100;
    this.dettaglioIstruttoria.percentualeIncrementoGreening = dettaglioIstruttoria.percentualeIncrementoGreening / 100;
    this.dettaglioIstruttoria.percentualeRiduzioneLineareArt51Par2 = dettaglioIstruttoria.percentualeRiduzioneLineareArt51Par2 / 100;
    this.dettaglioIstruttoria.percentualeRiduzioneLineareArt51Par3 = dettaglioIstruttoria.percentualeRiduzioneLineareArt51Par3 / 100;
    this.dettaglioIstruttoria.percentualeRiduzioneLineareMassimaleNetto = dettaglioIstruttoria.percentualeRiduzioneLineareMassimaleNetto / 100;
    this.dettaglioIstruttoria.percentualeRiduzioneTitoli = dettaglioIstruttoria.percentualeRiduzioneTitoli / 100;
  }

  private multiply100(dettaglioIstruttoria: DettaglioIstruttoria) {
    this.dettaglioIstruttoria.percentualeIncrementoGiovane = this.roundTo(dettaglioIstruttoria.percentualeIncrementoGiovane);
    this.dettaglioIstruttoria.percentualeIncrementoGreening = this.roundTo(dettaglioIstruttoria.percentualeIncrementoGreening);
    this.dettaglioIstruttoria.percentualeRiduzioneLineareArt51Par2 = this.roundTo(dettaglioIstruttoria.percentualeRiduzioneLineareArt51Par2);
    this.dettaglioIstruttoria.percentualeRiduzioneLineareArt51Par3 = this.roundTo(dettaglioIstruttoria.percentualeRiduzioneLineareArt51Par3);
    this.dettaglioIstruttoria.percentualeRiduzioneLineareMassimaleNetto = this.roundTo(dettaglioIstruttoria.percentualeRiduzioneLineareMassimaleNetto);
    this.dettaglioIstruttoria.percentualeRiduzioneTitoli = this.roundTo(dettaglioIstruttoria.percentualeRiduzioneTitoli);
  }

  private roundTo(value: number) {
    return Math.round(value * 1e2 * 1e2) / 1e2;
  }

}