import { ConfigurazioneIstruttoriaService } from './../../../sostegno-shared/configurazione-istruttoria/shared/configurazione.istruttoria.service';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { MessageService } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Istruttoria } from 'src/app/a4g-common/classi/Istruttoria';
import { DatiIstruttoria } from '../../../domain/datiIstruttoria';
import { DomandaIstruttoriaDettaglio } from '../../../domain/domandaIstruttoriaDettaglio';
import { Pascolo, PascoloDentroProvincia, PascoloFuoriProvincia, PascoloAziendale } from '../../../classi/pascolo';
import { DatiIstruttoriaPascoliDTO } from '../../../domain/dati-istruttoria-pascoli-dto';
import { IstruttoriaDomandaUnica } from '../../../classi/IstruttoriaDomandaUnica';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';

@Component({
  selector: 'app-inserimento-dati-istruttoria',
  templateUrl: './inserimento-dati-istruttoria.component.html',
  styleUrls: ['./inserimento-dati-istruttoria.component.css']
})
export class InserimentoDatiIstruttoriaComponent implements OnInit {
  datiIstruttoriaDaInserire: DatiIstruttoria;
  idDomandaCorrente: number;
  domandaCorrenteDettaglio: DomandaIstruttoriaDettaglio;
  // arrayPascoli: Array<Pascolo> = [new PascoloDentroProvincia("cuaa", "123TN321" , 0 ), new PascoloFuoriProvincia("Positivo", "cuaa" , "193BZ399" , 20), new PascoloAziendale("Presente", "Pascolo Aziendale Cles",  30)]; //array di prova per testare i vari pascoli
  arrayPascoli: Array<Pascolo> = [];
  formPascoli: FormGroup;
  private _serviceSubscription;
  
  istruttoriaDUCorrente: IstruttoriaDomandaUnica;

  public istruttoriaCorrente: Istruttoria;//TODO da togliere

  public showCheckboxAnnulloRitardoPresentazione = false;


  scelteMAN3 = [{ label: '', value: '' }, { label: 'Presente', value: 'PRESENTE' }, { label: 'Incompleta', value: 'INCOMPLETA' }, { label: 'Assente', value: 'ASSENTE' }];
  scelteMAN5 = [{ label: '', value: '' }, { label: 'Positivo', value: 'POSITIVO' }, { label: 'Negativo', value: 'NEGATIVO' }];

  constructor(
      private route: ActivatedRoute, 
      private istruttoriaDettaglioService: IstruttoriaDettaglioService,
      private messageService: MessageService,
      private fb: FormBuilder,
      private confService: ConfigurazioneIstruttoriaService
    ) { }

  ngOnInit() {
    this.istruttoriaDUCorrente = this.route.snapshot.data['domandaIstruttoria'];
    this.caricaDatiDettaglio();
    this.caricaDatiDettaglioPascoli();
    this.controllaRitardo();
  }

  private caricaDatiDettaglioPascoli() {
    let configForm: { [key: string]: any; } = {};
    this.istruttoriaDettaglioService.getDatiIstruttoriaDUDatiPascolo(this.istruttoriaDUCorrente.id.toString()).subscribe((dati) => { 
      console.log(dati);
      this.arrayPascoli = Pascolo.ArrPascoloDaoToArrPascolo(dati);
      for (let id in this.arrayPascoli) { //ciclo l'array di pascoli e configuro il configForm in base al tipo del pascolo. Nella versione definitiva questa porzione di codice va eseguita dopo la chiamata ai servizi rest e dopo aver istanziato correttamente i vari tipi di pascolo in base al loro codice.
        let pascolo = this.arrayPascoli[id];
        configForm[id + 'codicePascolo'] = [pascolo.codicePascolo];
        configForm[id + 'superficie'] = [pascolo.superficie, Validators.pattern(/^\d*(\.?\d*)$/)];

        if (pascolo instanceof PascoloDentroProvincia) {

          configForm[id + 'man4'] = [pascolo.man4, Validators.pattern(/^[a-zA-Z0-9]{16}|^[a-zA-Z0-9]{11}$/)];

        }

        if (pascolo instanceof PascoloFuoriProvincia) {
          configForm[id + 'man4'] = [pascolo.man4];
          configForm[id + 'man5'] = [pascolo.man5];
        }
        if (pascolo instanceof PascoloAziendale) {

          configForm[id + 'man3'] = [pascolo.man3];

        }

      }
      this.formPascoli = this.fb.group(configForm);

    });
  }

  private caricaDatiDettaglio() {
    this.istruttoriaDettaglioService.getDatiIstruttoriaDU(this.istruttoriaDUCorrente.id.toString()).subscribe( resp => {
      if (resp) {
        this.datiIstruttoriaDaInserire = resp;
      } else {
        this.datiIstruttoriaDaInserire = new DatiIstruttoria();
      }
    });
  }

  onSubmit(f: NgForm) {
    console.log('Submit');
    console.log(this.datiIstruttoriaDaInserire);
    this.istruttoriaDettaglioService.saveOrUpdateDatiIstruttoriaDU(this.istruttoriaDUCorrente.id.toString(), this.datiIstruttoriaDaInserire)
      .subscribe(
        x => {
          this.datiIstruttoriaDaInserire=x;
          console.log('Salvataggio dati Istruttoria avvenuto con successo'),
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        },
        error => {
          console.error('Errore in salvataggio dati Istruttoria: ' + error),
            console.error(error),
            A4gMessages.handleError(this.messageService, error, A4gMessages.ERRORE_SALVATAGGIO_DATI_ISTRUTTORIA);
        }
      );
  }

  public consume() {
    this.cleanup();
  }

  private cleanup() {
    this._serviceSubscription.unsubscribe();
  }

  salvaDatiIstruttoria() {
    if (this.formPascoli.valid) {
      let formV = this.formPascoli.getRawValue();
      let arr = new Array<DatiIstruttoriaPascoliDTO>();
      for (let pascolo of this.arrayPascoli) {
        if (pascolo.tipoPascolo === 'PascoloDentroProvincia') {
          const pascoloDentroProvincia: PascoloDentroProvincia = <PascoloDentroProvincia>pascolo;
          arr.push(new DatiIstruttoriaPascoliDTO("", "", pascolo.descPascolo, pascolo.superficie, pascoloDentroProvincia.man4, pascolo.id, pascolo.version));
        }
        if (pascolo.tipoPascolo === 'PascoloFuoriProvincia') {
          const pascoloFuoriProvincia: PascoloFuoriProvincia = <PascoloFuoriProvincia>pascolo;
          arr.push(new DatiIstruttoriaPascoliDTO(pascoloFuoriProvincia.man5, "",
            pascolo.descPascolo, pascolo.superficie, pascoloFuoriProvincia.man4, pascolo.id, pascolo.version));
        }
        if (pascolo.tipoPascolo === 'PascoloAziendale') {
          const pascoloAziendale: PascoloAziendale = <PascoloAziendale>pascolo;
          arr.push(new DatiIstruttoriaPascoliDTO("", pascoloAziendale.man3, pascolo.descPascolo, pascolo.superficie,"", pascolo.id, pascolo.version));
        }
      }
      this.istruttoriaDettaglioService.saveDatiIstruttoriaPascoli(this.istruttoriaDUCorrente.id, arr).subscribe((dati) => {
        this.caricaDatiDettaglioPascoli();
      });
    }
  }

  cleanBpsImportoSanzioniAnnoPrecedente(e: any) {
    console.log(e)
    if (e === false) {
      this.datiIstruttoriaDaInserire.bpsImportoSanzioniAnnoPrecedente = null;
    }
  }

  cleanGioImportoSanzioniAnnoPrecedente(e: any) {
    if (e === false) {
      this.datiIstruttoriaDaInserire.gioImportoSanzioniAnnoPrecedente = null;
    }
  }

  getIstruttoriaCorrente(): Istruttoria {
    return this.istruttoriaCorrente;
  }

  getAnnoRiferimento () {
    return this.istruttoriaDUCorrente.domanda.campagna;
  }

  private controllaRitardo() {
    this.confService.getConfigurazioneIstruttorie(this.istruttoriaDUCorrente.domanda.campagna)
      .subscribe(conf => {
        // controlla che la domanda sia di modifica
        if (String(this.istruttoriaDUCorrente.domanda['codModuloDomanda']).includes('BPS_ART_15')) {
          // e che sia stata presentata dopo la data di scadenza
          if (this.istruttoriaDUCorrente.domanda.dtPresentazione > conf.dtScadenzaDomandeIniziali) {
            this.showCheckboxAnnulloRitardoPresentazione = true;
          }
        }
      });
  }

}
