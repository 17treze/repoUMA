import { Component, OnInit, ViewChild } from '@angular/core';
import { Labels } from '../../../../app.labels';
import { DichiarazioneAntimafiaService } from '../../dichiarazione-antimafia.service';
import { DichiarazioneAntimafia } from '../../classi/dichiarazioneAntimafia';
import { AziendaCollegata } from '../../classi/datiDichiarazione';
import { StepActionComponent } from '../step-action/step-action.component';
import { AntimafiaValidationService } from '../antimafia-validation.service';
import { StatoValidazione } from '../../classi/statoValidazione';
import { Utils } from '../antimafia.utils';
import { AntimafiaService } from '../../antimafia.service';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-aziende',
  templateUrl: './aziende.component.html',
  styleUrls: ['./aziende.component.css']
})
export class AziendeComponent implements OnInit {
  intestazioni = Labels;
  cols: any[];
  loading: boolean = false;

  dichiarazioneAntimafia: DichiarazioneAntimafia;

  aziendeCollegate: AziendaCollegata[] = [];
  @ViewChild(StepActionComponent, { static: true })
  private stepComponent: StepActionComponent;
  textTitle: string;

  constructor(
    private dichiarazioneAntimafiaService: DichiarazioneAntimafiaService,
    private antimafiaService: AntimafiaService,
    private messages: MessageService,
    private validator: AntimafiaValidationService
  ) {}

  ngOnInit() {
    this.dichiarazioneAntimafia = this.dichiarazioneAntimafiaService.getDichiarazioneAntimafia();
    if (!this.isImpresaDittaIndividuale()) {
      this.aziendeCollegate = this.popolaDati(this.dichiarazioneAntimafia);
    }
    console.log(this.aziendeCollegate);
    this.textTitle=Labels.datiAcquisiti;
    this.cols = [
      {field: 'denominazione', header:this.intestazioni.denominazione},
      {field: 'codiceFiscale', header:this.intestazioni.codiceFiscale},
      {field: 'comuneSede', header:this.intestazioni.comune },
      {field: 'indirizzoSede', header:this.intestazioni.indirizzo},
      {field: 'civicoSede', header:this.intestazioni.civico},
      {field: 'provinciaSede', header:this.intestazioni.provinciaSigla},
      {field: 'capSede', header:this.intestazioni.cap},
      {field: null, header:this.intestazioni.carica},
      {field: null, header:this.intestazioni.dataInizioCarica},
      {field: null, header:this.intestazioni.dataFineCarica},
      {field: 'selezionato', header:this.intestazioni.dichiarazione}
    ];    
  }


  isImpresaDittaIndividuale(): boolean {
    return Utils.isImpresaDittaIndividuale(this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa
      .formaGiuridicaCodice);
  }

    // get soggetti con carica
    popolaDati(dichiarazione: DichiarazioneAntimafia): AziendaCollegata[] {
      let aziende: AziendaCollegata[] = [];

      if (dichiarazione != null) {
        aziende = dichiarazione.datiDichiarazione.dettaglioImpresa.aziendeCollegate;
      } else {
        console.log('dichiarazione Dettagli assente');
        return [];
      }

      if (aziende !== undefined && aziende.length > 0) {
        // if (aziende.length > 0) {
          return this.duplicaAziendeConCariche(aziende);
        // }
      } else {
        return [];
      }
    }
    // duplica aziende collegate con più di una carica
    duplicaAziendeConCariche(aziendeCollegate: AziendaCollegata[]): AziendaCollegata[] {
      const aziendeCollegateFlat: AziendaCollegata[] = [];
      aziendeCollegate.forEach(azienda => {
          azienda.carica.forEach(carica => {
            const ac: AziendaCollegata = Object.assign({}, azienda);
            ac.carica = [carica];
            aziendeCollegateFlat.push(ac);
          });
      });
      const aziendeCollegateFinal = this.assegnaDataInizioCarica(aziendeCollegateFlat);
      return aziendeCollegateFinal;
    }

    // assegna data inizio carica da CCIAA se non esiste in dettaglioImpresa.carica
    assegnaDataInizioCarica(aziendeCollegate: AziendaCollegata[]): AziendaCollegata[] {
      const aziendeCollegateFlat: AziendaCollegata[] = [];
      aziendeCollegate.forEach(azienda => {
          azienda.carica.forEach(carica => {
            if (!(carica.dataInizio !== undefined && carica.dataInizio != null && carica.dataInizio.length > 0 )) {
              carica.dataInizio = this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa.estremiCCIAA.dataIscrizione;
            }
            aziendeCollegateFlat.push(azienda);
          });
      });
      return aziendeCollegateFlat;
    }

    conferma() {
      const validatorResponse = this.validator.validaAziendeCollegate(this.aziendeCollegate);
      if (validatorResponse.esito !== StatoValidazione.OK) {
        alert(validatorResponse.messaggio);
        return;
      }
      this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa.aziendeCollegate = this.aziendeCollegate;
      this.loading = true;
      this.antimafiaService.aggiornaDichiarazioneAntimafia(this.dichiarazioneAntimafia)
      .subscribe(
        aggiornaDichiarazioneEsito => {
          console.log("Modifica alla dichiarazione " + aggiornaDichiarazioneEsito.dichiarazione.id + " avvenuta con successo");
          if (aggiornaDichiarazioneEsito.esito != undefined) {
            this.messages.add(A4gMessages.getToast("generico", A4gSeverityMessage.error, aggiornaDichiarazioneEsito.esito))
          }
        },
        err => {
          let error = 'Errore in aggiornaDichiarazioneAntimafia: ' + err;
          console.error(error);
          this.messages.add(A4gMessages.getToast("generico", A4gSeverityMessage.error, error));
        },
        () => {
          this.loading = false;
          this.stepComponent.goNext();
        });
    }

  /*Descrizione impresa, La Valletta SAS
Provincia dell’impresa, Trento
Numero di iscrizione CCIA,1234563
Data di iscrizione, 27/08/1998
Oggetto sociale, Società Agricola Semplice La Vallett SAS
Forma giuridica,SAS
Estremi atto di costituzione,ATTO N.34567 del 28/12/1998
Capitale sociale, 1.000.000.00 €
Durata della società, ILLIMITATA
Codice fiscale impresa, LLLMMMB08L378K
Partita iva, 123455678
Sede legale, Via Pelegrini 37 Trento (TN)
Pec, lavalletta@arubapec.it*/


}
