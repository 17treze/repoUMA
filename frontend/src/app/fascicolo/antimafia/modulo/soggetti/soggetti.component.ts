import { Component, OnInit, ViewChild } from "@angular/core";
import { DichiarazioneAntimafiaService } from "../../dichiarazione-antimafia.service";
import { AntimafiaService } from "../../antimafia.service";
import {
  DatiDichiarazione,
  Carica,
  Richiedente
} from "../../classi/datiDichiarazione";
import { DichiarazioneAntimafia } from "../../classi/dichiarazioneAntimafia";
import { SoggettiImpresa } from "../../classi/datiDichiarazione";
import { Labels } from "../../../../app.labels";
import { Utils } from "../antimafia.utils";
import { StepActionComponent } from "../step-action/step-action.component";
import { AntimafiaValidationService } from "../antimafia-validation.service";
import { StatoValidazione } from "../../classi/statoValidazione";
import { MessageService } from "primeng/api";
import {
  A4gMessages,
  A4gSeverityMessage
} from "src/app/a4g-common/a4g-messages";
import { LoaderService } from 'src/app/loader.service';
@Component({
  selector: "app-soggetti",
  templateUrl: "./soggetti.component.html",
  styleUrls: ["./soggetti.component.css"]
})
export class SoggettiComponent implements OnInit {
  cols: any[];
  textTitle: string;
  intestazioni = Labels;

  listaSoggetti: SoggettiImpresa[];
  hasDirettoreTecnicoNominato: boolean;
  listaSoggettiTabella: SoggettiImpresa[];
  dichiarazioneAntimafia: DichiarazioneAntimafia;
  hasDirettoreTecnicoValue: boolean;
  @ViewChild(StepActionComponent, { static: true })
  private stepComponent: StepActionComponent;
  msgErrore: string;
  labels = Labels;

  constructor(
    private dichiarazioneAntimafiaService: DichiarazioneAntimafiaService,
    private antimafiaService: AntimafiaService,
    private validator: AntimafiaValidationService,
    private messageService: MessageService,
    private loader: LoaderService
  ) { }

  ngOnInit() {
    this.loader.setTimeout(480000); //otto minuti
    this.dichiarazioneAntimafia = this.dichiarazioneAntimafiaService.getDichiarazioneAntimafia();
    this.listaSoggetti = this.popolaDati(this.dichiarazioneAntimafia);
    if (this.isImpresaDittaIndividuale()) {
      this.listaSoggettiTabella = this.popolaSoggettiImportanti(
        this.listaSoggetti,
        this.dichiarazioneAntimafia.datiDichiarazione.richiedente
      );
    } else {
      this.listaSoggettiTabella = this.popolaSoggettiAltri(this.listaSoggetti);
    }
    Utils.setInizioCarica(
      this.listaSoggettiTabella,
      this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa
        .estremiCCIAA.dataIscrizione
    );
    this.hasDirettoreTecnicoNominato = this.checkDirettoreTecnicoNominato(
      this.listaSoggetti
    );
    this.hasDirettoreTecnicoValue = this.dichiarazioneAntimafia.assenzaDt;
    this.textTitle = Labels.datiAcquisitiTrib;
    this.cols = [
      { field: "cognome", header: this.intestazioni.cognome },
      { field: "nome", header: this.intestazioni.nome },
      { field: "codiceFiscale", header: this.intestazioni.codiceFiscale },
      { field: "dataNascita", header: this.intestazioni.dataNascita },
      { field: "comuneNascita", header: this.intestazioni.comuneNascita },
      { field: "provinciaNascita", header: this.intestazioni.provinciaNascita },
      { field: "sesso", header: this.intestazioni.sesso },
      { field: null, header: this.intestazioni.residenzaComune },
      { field: null, header: this.intestazioni.residenzaVia },
      { field: null, header: this.intestazioni.residenzaProvincia },
      { field: null, header: this.intestazioni.residenzaCAP },
      { field: null, header: this.intestazioni.carica },
      { field: null, header: this.intestazioni.dataInizioCarica },
      { field: null, header: this.intestazioni.dataFineCarica }
    ];
  }

  isImpresaDittaIndividuale(): boolean {
    if (
      this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa
        .formaGiuridicaCodice === "DI"
    ) {
      return true;
    }
    return false;
  }

  conferma(): void {
    // setto il valore rispettivo della checkbox
    this.dichiarazioneAntimafia.assenzaDt = this.hasDirettoreTecnicoValue;
    const validatorResponse = this.validator.validaSoggettiCarica(
      this.dichiarazioneAntimafia
    );
    if (validatorResponse.esito === StatoValidazione.ERROR) {
      this.msgErrore = validatorResponse.messaggio;
      this.messageService.add(
        A4gMessages.getToast(
          "errorAssenzaDt",
          A4gSeverityMessage.error,
          this.msgErrore
        )
      );
      return;
    }
    console.log("Inzio PUT");

    console.log(this.dichiarazioneAntimafia);
    if (this.isImpresaDittaIndividuale()) {
      this.antimafiaService
        .aggiornaDichiarazioneAntimafia(this.dichiarazioneAntimafia)
        .subscribe(
          aggiornaDichiarazioneEsito => {
            console.log(
              "Modifica alla dichiarazione " +
              aggiornaDichiarazioneEsito.dichiarazione.id +
              " avvenuta con successo"
            );
            if (aggiornaDichiarazioneEsito.esito != undefined) {
              A4gMessages.getToast(
                "generico",
                A4gSeverityMessage.error,
                aggiornaDichiarazioneEsito.esito
              );
            }
          },
          err => {
            let error = "Errore in aggiornaDichiarazioneAntimafia: " + err;
            console.error(error);
            A4gMessages.getToast("generico", A4gSeverityMessage.error, error);
          },
          () => this.stepComponent.goNext()
        );
    } else {
      this.stepComponent.goNext();
    }
  }

  checkDirettoreTecnicoNominato(soggettiImpresa: SoggettiImpresa[]): boolean {
    return soggettiImpresa.some(element =>
      element.carica.some(carica => carica.codice === "DT")
    );
  }

  popolaSoggettiAltri(soggettiImpresa: SoggettiImpresa[]): SoggettiImpresa[] {
    return Utils.soggettiAppiattito(soggettiImpresa);
  }

  popolaSoggettiImportanti(
    soggettiImpresa: SoggettiImpresa[],
    richiedente: Richiedente
  ): SoggettiImpresa[] {
    // filtro soggetti che siano solo TITOLARI o DIRETTORE TECNICO
    soggettiImpresa.forEach(sogg => {
      sogg.carica = sogg.carica.filter(carica =>
        carica.codice.match("DT|TIT|TI")
      );
      return sogg;
    });
    // ritorno solo i soggetti che carica valorizzata
    return soggettiImpresa.filter(sogg => sogg.carica.length > 0);
  }
  popolaDati(dichiarazione: DichiarazioneAntimafia): any {
    let dichiarazioneDettagli: DatiDichiarazione;

    if (dichiarazione != null) {
      dichiarazioneDettagli = dichiarazione.datiDichiarazione;
    } else {
      console.log("dichiarazioneDettagli assente ");
      return [];
    }
    if (dichiarazioneDettagli.dettaglioImpresa.soggettiImpresa != null) {
      return dichiarazioneDettagli.dettaglioImpresa.soggettiImpresa;
    } else {
      return [];
    }
  }

  ngOnDestroy(): void {
    this.loader.resetTimeout();
  }
}
