import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FascicoloDettaglioService } from 'src/app/fascicolo/fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import { FascicoloDettaglio } from 'src/app/fascicolo/shared/fascicolo.model';
import { Subscription } from 'rxjs';
import { alimentazioneType, MacchinaDto, classeType } from 'src/app/fascicolo/creazione-fascicolo/dto/MacchinaDto';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MessageService } from 'primeng-lts';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { Localization } from 'src/app/a4g-common/utility/localization';

@Component({
  selector: 'app-dettaglio-macchina',
  templateUrl: './dettaglio-macchina.component.html',
  styleUrls: ['./dettaglio-macchina.component.scss']
})
export class DettaglioMacchinaComponent implements OnInit, OnDestroy {
  public cols: any[];
  public cols2: any[];
  private cuaa = '';
  private idMacchina = '';
  public macchinario: MacchinaDto;
  public modificaMacchina: FormGroup;
  public alimentazioneOptions: alimentazioneType[];
  public tipologiaOptions: classeType[];
  public modificaAttiva: boolean = false;
  public language: any = Localization.itCalendar();

  macchineSubscription: Subscription;

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
    this.idMacchina = this.route.snapshot.paramMap.get('idMacchina');
    this.getMacchina(this.idMacchina);
    this.initValue();
    this.initForm();
  }

  ngOnDestroy() {
    if (this.macchineSubscription) {
      this.macchineSubscription.unsubscribe();
    }
  }

  initValue() {
    this.alimentazioneOptions = [
      { id: "BENZ", alimentazione: "BENZINA" }, 
      { id: "GASOL", alimentazione: "GASOLIO" }, 
      { id: "ELETTR", alimentazione: "ELETTRICO" }, 
      { id: "GPL", alimentazione: "GPL" }, 
      { id: "METANO", alimentazione: "METANO" }, 
      { id: "IBRIDO", alimentazione: "IBRIDO" },
      { id: "B/ETA", alimentazione: "BENZINA / ETANOLO" }, 
      { id: "B/GPL", alimentazione: "BENZINA / GPL" }, 
      { id: "B/MET", alimentazione: "BENZINA / METANO" }, 
      { id: "B/OLIO", alimentazione: "BENZINA / OLIO" }, 
      { id: "B/WANK", alimentazione: "BENZINA / WANKEL" }, 
      { id: "G - GPL", alimentazione: "GPL G" }, 
      { id: "G - MET", alimentazione: "METANO G" }, 
      { id: "GNL", alimentazione: "GAS NATURALE LIQUEFATTO" }, 
      { id: "MISCELA", alimentazione: "MISCELA" }, 
      { id: "PETROLIO", alimentazione: "PETROLIO" }
      ];
      this.tipologiaOptions = [
        { id: "1", tipologia: "TRATTRICI" }, 
        { id: "2", tipologia: "RIMORCHI SUP. A 15 QLI" }, 
        { id: "3", tipologia: "MOTOFALCIATRICI" }, 
        { id: "4", tipologia: "TIPOLOGIA 4" }, 
        { id: "5", tipologia: "TIPOLOGIA 5" }, 
        { id: "6", tipologia: "TIPOLOGIA 6" },
        { id: "7", tipologia: "TIPOLOGIA 7" }, 
        { id: "99", tipologia: "ALTRE MACCHINE" }
        ];
  }

  initForm() {
    this.modificaMacchina = this.fb.group({
      alimentazioneMacchina: [this.macchinario.alimentazione, Validators.required],
      modelloMacchina: [this.macchinario.descrizione, Validators.required],
      tipologiaMacchina:  [this.macchinario.classe, Validators.required],
      targaMacchina: [this.macchinario.targa, Validators.required],
      possessoMacchina: [this.macchinario.possesso, Validators.required],
      marcaMacchina:  [this.macchinario.marca, Validators.required],
      dataIscrizione: [this.macchinario.dataIscrizione, Validators.required]
    });
  }

  public handleClick() {
    this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.LISTA_MACCHINARI);
  }

  private getMacchina(idMacchina: string) {
    // servizio da fare
    // this.dotazioneTecnicaService.getMacchinaById(idMacchina)
    //   .subscribe((macchina: MacchinaDto) => {
    //     this.macchine = macchina;
    //   }, error => this.errorService.showError(error, 'tst-macchine'));

    // dati mocckati
    this.initDati();
  }

  private initDati() {
    // dati mocckati
    this.macchinario = new MacchinaDto;
    this.macchinario.idAgs = Number(this.idMacchina);
    this.macchinario.descrizione = "FiatAgri G210";
    this.macchinario.targa = "XX00000";
    this.macchinario.classe = { id: "1", tipologia: "TRATTRICI" };
    this.macchinario.marca = "Fiat";
    this.macchinario.alimentazione = { id: "GASOL", alimentazione: "GASOLIO" };
    this.macchinario.possesso = "Possesso";
    this.macchinario.motore = "Motore";
    this.macchinario.rimorchio = "Si";
    this.macchinario.immatricolazione = null; //data ?
    this.macchinario.documentoPossesso = "Documento Possesso";
    this.macchinario.dataIscrizione = "01/01/2021";
    this.macchinario.dataChiusura = null;
  }

  public deleteMacchina(id: number) {
    console.log("Cancello macchina id: " + id.toString())
  }
  
  public dettaglioMacchina(id: number) {
    console.log("Dettaglio macchina id: " + id.toString())
    this.router.navigate([id.toString() + "/dettaglio-macchina"]);
  }

  modificaDettaglio () {
    this.initForm();
    this.modificaAttiva = true;
  }

  annullaModifica () {
    this.modificaAttiva = false;
  }

  public onSubmit() {
    //chiamata al servizio di modifica macchina
    console.log("submit " + this.idMacchina)
    if (this.modificaMacchina.invalid) {
      this.messageService.add(A4gMessages.getToast('tst-macchine', A4gSeverityMessage.error, "Dati non validi"));
    } else {
      this.salva();
    }
  }

  salva() {
      this.macchinario.idAgs = Number(this.idMacchina);
      this.macchinario.classe = this.modificaMacchina.value.tipologiaMacchina;
      this.macchinario.descrizione = this.modificaMacchina.value.modelloMacchina;
      this.macchinario.targa = this.modificaMacchina.value.targaMacchina;
      this.macchinario.alimentazione = this.modificaMacchina.value.alimentazioneMacchina;
      this.macchinario.possesso = this.modificaMacchina.value.possessoMacchina;
      this.macchinario.marca = this.modificaMacchina.value.marcaMacchina;
      this.macchinario.dataIscrizione = this.modificaMacchina.value.dataIscrizione;

      this.messageService.add(A4gMessages.getToast('tst-macchine', A4gSeverityMessage.success, "Dati salvati"));
      this.modificaAttiva = false;
  }

}