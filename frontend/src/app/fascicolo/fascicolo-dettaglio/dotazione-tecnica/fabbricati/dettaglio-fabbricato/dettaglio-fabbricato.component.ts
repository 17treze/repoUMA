import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FascicoloDettaglioService } from 'src/app/fascicolo/fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import { FascicoloDettaglio } from 'src/app/fascicolo/shared/fascicolo.model';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MessageService } from 'primeng-lts';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { Localization } from 'src/app/a4g-common/utility/localization';
import { FabbricatoDto } from 'src/app/fascicolo/creazione-fascicolo/dto/FabbricatoDto';

@Component({
  selector: 'app-dettaglio-fabbricato',
  templateUrl: './dettaglio-fabbricato.component.html',
  styleUrls: ['./dettaglio-fabbricato.component.scss']
})
export class DettaglioFabbricatoComponent implements OnInit, OnDestroy {
  public cols: any[];
  public cols2: any[];
  private cuaa = '';
  private idFabbricato = '';
  public fabbricato: FabbricatoDto;
  public modificaFabbricato: FormGroup;
  public modificaAttiva: boolean = false;
  public language: any = Localization.itCalendar();

  fabbricatiSubscription: Subscription;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private fascicoloDettaglioService: FascicoloDettaglioService,
    private messageService: MessageService,
    private router: Router
  ) {}

  ngOnInit() {
    this.modificaAttiva = false;
    this.cuaa = this.route.snapshot.paramMap.get('cuaa');
    this.idFabbricato = this.route.snapshot.paramMap.get('idFabbricato');
    this.getFabbricato(this.idFabbricato);
    this.initForm();
  }

  ngOnDestroy() {
    if (this.fabbricatiSubscription) {
      this.fabbricatiSubscription.unsubscribe();
    }
  }

  initForm() {
    this.modificaFabbricato = this.fb.group({
      destinazioneFabbricato: [this.fabbricato.destinazione, Validators.required],
      tipologiaFabbricato: [this.fabbricato.tipologia, Validators.required],
      ubicazioneFabbricato:  [this.fabbricato.ubicazione, Validators.required],
      utilizzatoriFabbricato: [this.fabbricato.utilizzatori, Validators.required],
      nrPostiFabbricato: [this.fabbricato.nrPosti, Validators.required],
      superficieCopertaFabbricato:  [this.fabbricato.superficieCoperta, Validators.required],
      superficieScopertaFabbricato: [this.fabbricato.superficieScoperta, Validators.required],
      volumeFabbricato: [this.fabbricato.volume, Validators.required]
    });
  }

  public handleClick() {
    this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.LISTA_FABBRICATI);
  }

  private getFabbricato(idFabbricato: string) {
    // servizio da fare
    // this.dotazioneTecnicaService.getFabbricatoById(idFabbricato)
    //   .subscribe((fabbricato: FabbricatoDto) => {
    //     this.fabbricato = fabbricato;
    //   }, error => this.errorService.showError(error, 'tst-fabbricati'));

    // dati mocckati
    this.initDati();
  }

  private initDati() {
    // dati mocckati
    this.fabbricato = new FabbricatoDto;
    this.fabbricato.id = Number(this.idFabbricato);
    this.fabbricato.destinazione = "Agricola";
    this.fabbricato.tipologia = "Proprietà";
    this.fabbricato.ubicazione = "Trento";
    this.fabbricato.utilizzatori = "Utilizzatori";
    this.fabbricato.volume = 777;
    this.fabbricato.nrPosti = 5;
    this.fabbricato.superficieCoperta = 333;
    this.fabbricato.superficieScoperta = 444;
  }

  public deleteFabbricato(id: number) {
    console.log("Cancello fabbricato id: " + id.toString())
  }
  
  public dettaglioFabbricato(id: number) {
    console.log("Dettaglio fabbricato id: " + id.toString())
    this.router.navigate([id.toString() + "/dettaglio-fabbricato"]);
  }

  modificaDettaglio () {
    this.initForm();
    this.modificaAttiva = true;
  }

  annullaModifica () {
    this.modificaAttiva = false;
  }

  public onSubmit() {
    //chiamata al servizio di modifica fabbricato
    console.log("submit " + this.idFabbricato)
    if (this.modificaFabbricato.invalid) {
      this.messageService.add(A4gMessages.getToast('tst-fabbricati', A4gSeverityMessage.error, "Dati non validi"));
    } else {
      this.salva();
    }
  }

  salva() {
      this.fabbricato.id = Number(this.idFabbricato);
      this.fabbricato.destinazione = this.modificaFabbricato.value.destinazioneFabbricato;
      this.fabbricato.tipologia = this.modificaFabbricato.value.tipologiaFabbricato;
      this.fabbricato.ubicazione = this.modificaFabbricato.value.ubicazioneFabbricato;
      this.fabbricato.utilizzatori = this.modificaFabbricato.value.utilizzatoriFabbricato;
      this.fabbricato.volume = this.modificaFabbricato.value.volumeFabbricato;
      this.fabbricato.nrPosti = this.modificaFabbricato.value.nrPostiFabbricato;
      this.fabbricato.superficieCoperta = this.modificaFabbricato.value.superficieCopertaFabbricato;
      this.fabbricato.superficieScoperta = this.modificaFabbricato.value.superficieScopertaFabbricato;

      this.messageService.add(A4gMessages.getToast('tst-fabbricati', A4gSeverityMessage.success, "Dati salvati"));
      this.modificaAttiva = false;
  }

}