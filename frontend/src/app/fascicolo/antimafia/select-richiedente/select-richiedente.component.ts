import {
  Component,
  OnInit,
  Output,
  Input,
  OnDestroy
} from "@angular/core";
import { EventEmitter } from "@angular/core";
import { AntimafiaService } from "../antimafia.service";
import { RappresentanteLegale } from "../classi/rappresentanteLegale";
import { Configuration } from "../../../app.constants";
import { HttpClient } from "@angular/common/http";
import { MessageService } from "primeng/api";
import {
  A4gMessages,
  A4gSeverityMessage
} from "src/app/a4g-common/a4g-messages";
import { LoaderService } from "src/app/loader.service";
import { AuthService } from "src/app/auth/auth.service";
import { Observable, EMPTY, concat } from "rxjs";
import { switchMap } from "rxjs/operators";

@Component({
  selector: "app-select-richiedente",
  templateUrl: "./select-richiedente.component.html",
  styleUrls: ["./select-richiedente.component.css"]
})
export class SelectRichiedenteComponent implements OnInit, OnDestroy {
  rappresentantiLegali: Array<RappresentanteLegale>;
  selectedCUAADD: string;
  utenteConnesso: string;
  codiciCaricheRappresentanteLegale = [
    "ACR",
    "ADP",
    "AMD",
    "AMG",
    "AMM",
    "AMP",
    "AMS",
    "APR",
    "ART",
    "ATI",
    "AUN",
    "AUP",
    "CDP",
    "CDT",
    "CLR",
    "CM",
    "CMS",
    "COA",
    "COD",
    "COG",
    "COL",
    "COM",
    "COP",
    "COV",
    "COZ",
    "CRT",
    "CST",
    "CUE",
    "CUF",
    "CUM",
    "IN",
    "LER",
    "LGR",
    "LGT",
    "LI",
    "LIG",
    "LR2",
    "LRF",
    "LRT",
    "LSA",
    "MPP",
    "OAS",
    "OPN",
    "PAD",
    "PCA",
    "PCD",
    "PCE",
    "PCG",
    "PCO",
    "PDC",
    "PDI",
    "PED",
    "PEO",
    "PG",
    "PGD",
    "PGE",
    "PGS",
    "PM",
    "PPP",
    "PRE",
    "PRP",
    "PRQ",
    "PSE",
    "PSS",
    "PTE",
    "RAF",
    "RAP",
    "RAS",
    "RES",
    "RFM",
    "RIN",
    "RIT",
    "RSS",
    "SAO",
    "SAP",
    "SCR",
    "SFC",
    "SLR",
    "SOA",
    "SOL",
    "SOR",
    "SOT",
    "SOU",
    "SPR",
    "TI",
    "TI2",
    "TIT",
    "TTE",
    "TU",
    "UM1",
    "VPC"
  ];

  @Input()
  cuaaImpresa: string;

  @Output()
  selectedCuaa: EventEmitter<string> = new EventEmitter<string>();

  personeSede: any;
  personeGiuridicheSede: Array<any> = [];
  callsPersonaGiuridica: Array<Observable<any>> = [];
  constructor(
    private http: HttpClient,
    private _configuration: Configuration,
    private antimafiaService: AntimafiaService,
    private messages: MessageService,
    private loader: LoaderService,
    private authService: AuthService
  ) { }

  ngOnInit() {
    console.log("ngOnInit cuaaImpresa " + this.cuaaImpresa);
    if (!this.cuaaImpresa || this.cuaaImpresa.length == 0) return;
    this.loader.setTimeout(480000); //otto minuti
    let user = this.authService.getUserFromSession();
    if (user != null) {
      //TODO prendere il CF del profilo selezionato
      // get utente connesso. salvalo in var di classe. dentro a carica rapp legale se esiste , mettilo di deault, altrimenti leva mano urlGetSSO
      this.utenteConnesso = user.codiceFiscale;
      this.caricaRappresentantiLegali();
    }
  }

  ngOnDestroy(): void {
    this.loader.resetTimeout();
  }

  public getDettaglioCompletoImpresa(cuaa: string): Observable<any> {


    const url = this._configuration.UrlGetAnagraficaImpresa.replace(
      "${cuaa}",
      String(cuaa)
    );
    return this.http.get<any>(url).pipe(
      switchMap(data => {
        console.log("getDettaglioCompletoImpresa cuaa : " + cuaa);
        if (data.header.esito === "OK") {
          const params = {
            numeroREASede:
              data.dati.listaimprese.estremiimpresa[0].datiiscrizionerea[0].nrea,
            provinciaSede:
              data.dati.listaimprese.estremiimpresa[0].datiiscrizionerea[0].cciaa
          };
          const paramsIn = encodeURIComponent(JSON.stringify(params));
          const urlParix = this._configuration.UrlGetDettaglioCompletoImpresa + paramsIn;
          console.log("getDettaglioCompletoImpresa paramsIn : " + paramsIn);
          return this.antimafiaService.rappresentatiLegali(urlParix);
        } else {
          if (data.dati.errore.msgerr === "NESSUNA IMPRESA TROVATA") {
            this.messages.add(A4gMessages.getToast("tst", A4gSeverityMessage.error, "Nessuna impresa trovata presso la Camera di Commercio"));
          } else {
            this.messages.add(A4gMessages.getToast("tst", A4gSeverityMessage.error, data.dati.errore.msgerr));
            return EMPTY;
          }
        }
      })
    );
  }


  private caricaRappresentantiLegali() {
    this.getDettaglioCompletoImpresa(this.cuaaImpresa).subscribe(
      data => {
        this.rappresentantiLegali = [];
        this.personeSede = data.dati.datiimpresa.personesede.persona;
        for (const persona of data.dati.datiimpresa.personesede.persona) {
          persona.cariche.carica.forEach(carica => {
            if (
              this.codiciCaricheRappresentanteLegale.indexOf(carica.ccarica) !== -1
            ) {
              if (persona.personafisica) {
                this.rappresentantiLegali.push(
                  new RappresentanteLegale(
                    persona.personafisica.codicefiscale,
                    persona.personafisica.nome +
                    " " +
                    persona.personafisica.cognome +
                    " - " +
                    carica.descrizione
                  )
                );
              }
              if (persona.personagiuridica) {
                this.callsPersonaGiuridica.push(this.getDettaglioCompletoImpresa(persona.personagiuridica.codicefiscale));
              }
            }
          });
        }

        if (this.rappresentantiLegali && this.rappresentantiLegali[0]) {
          const rappresentanteLegaleConnesso = this.rappresentantiLegali.find(
            r => {
              return r.codiceFiscale === this.utenteConnesso;
            }
          );
          if (rappresentanteLegaleConnesso) {
            this.selectedCUAADD =
              rappresentanteLegaleConnesso.codiceFiscale;
          } else {
            this.selectedCUAADD = this.rappresentantiLegali[0].codiceFiscale;
          }
        }

      },
      error => {
        console.log("Error", error);
        let errMsg = error.error.message;
        this.messages.add(
          A4gMessages.getToast(
            "generic",
            A4gSeverityMessage.error,
            "Errore in recupero rappresentanti: " + errMsg
          )
        );
      },
      () => {
        concat(...this.callsPersonaGiuridica).subscribe({
          next: dataDettaglioCompletoImpresa => {
            //console.log("dataDettaglioCompletoImpresa: " + dataDettaglioCompletoImpresa);
            for (const personaSedePersonaGiuridica of dataDettaglioCompletoImpresa.dati.datiimpresa.personesede.persona) {
              for (const caricaPersonaGiuridica of personaSedePersonaGiuridica.cariche.carica) {
                if (
                  this.codiciCaricheRappresentanteLegale.indexOf(caricaPersonaGiuridica.ccarica) !== -1
                ) {
                  if (personaSedePersonaGiuridica.personafisica) {
                    console.log("personagiuridica push: " + personaSedePersonaGiuridica.personafisica.codicefiscale);
                    this.rappresentantiLegali.push(
                      new RappresentanteLegale(
                        personaSedePersonaGiuridica.personafisica.codicefiscale,
                        personaSedePersonaGiuridica.personafisica.nome +
                        " " +
                        personaSedePersonaGiuridica.personafisica.cognome +
                        " - " +
                        caricaPersonaGiuridica.descrizione
                      )
                    );
                  }
                }
              }
            }
          }
        });
      }
    );
  }

  Save() {
    if (this.selectedCUAADD) {
      console.log("emit " + this.selectedCUAADD);
      this.selectedCuaa.emit(this.selectedCUAADD);
    }
  }
}
