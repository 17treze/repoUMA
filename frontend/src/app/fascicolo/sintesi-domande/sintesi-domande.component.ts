import { Component, OnInit } from "@angular/core";
import { Labels } from "src/app/app.labels";
import { AntimafiaService } from "../antimafia/antimafia.service";
import { DichiarazioneAntimafia } from "../antimafia/classi/dichiarazioneAntimafia";
import { FascicoloCorrente } from "../fascicoloCorrente";
import { DichiarazioneAntimafiaFilter } from "../antimafia/classi/dichiarazioneAntimafiaFilter";
import { DatePipe } from "@angular/common";
import { SoggettiImpresa } from "../antimafia/classi/datiDichiarazione";
import { A4gMessages, A4gSeverityMessage } from "src/app/a4g-common/a4g-messages";
import { MessageService } from "primeng/api";
import { TipoNotaEnum } from "src/app/istruttoria/istruttoria-antimafia/dto/TipoNotaEnum";
import { Router, NavigationExtras, ActivatedRoute } from "@angular/router";
import { FascicoloService } from '../fascicolo.service';

@Component({
  selector: "app-sintesi-domande",
  templateUrl: "./sintesi-domande.component.html",
  styleUrls: ["./sintesi-domande.component.css"],
  providers: [DatePipe]
})
export class SintesiDomandeComponent implements OnInit {
  labels = Labels;
  private dichiarazioniAntimafia: Array<DichiarazioneAntimafia> = [];
  dichiarazioniAntimafiaTable: Array<DichiarazioneAntimafia> = [];
  menuAction: any[] = new Array();
  menuActionFull: any[] = new Array();
  menuActionMin: any[] = new Array();
  dichiarazioneSelezionata: DichiarazioneAntimafia;
  indexSelectedFromMenu: number;

  constructor(
    private antimafiaService: AntimafiaService,
    private fascicoloService: FascicoloService,
    private fascicoloCorrente: FascicoloCorrente,
    private messages: MessageService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    const dichiarazioneAntimafiaParams = new DichiarazioneAntimafiaFilter();

    this.route.params.subscribe(params => {
      this.fascicoloService.getFascicolo(params['idFascicolo']).subscribe(fascicolo => {
        this.fascicoloCorrente.fascicolo = fascicolo;
        dichiarazioneAntimafiaParams.setAzienda(
          this.fascicoloCorrente.fascicolo.cuaa
        );
        dichiarazioneAntimafiaParams.idFascicolo = this.fascicoloCorrente.fascicolo.idFascicolo;
        dichiarazioneAntimafiaParams.statiDichiarazione = [
          "PROTOCOLLATA",
          "CONTROLLATA",
          "CONTROLLO_MANUALE",
          "POSITIVO",
          "NEGATIVO",
          "VERIFICA_PERIODICA",
          "RIFIUTATA"
        ];

        this.antimafiaService
          .getDichiarazioniAntimafia(dichiarazioneAntimafiaParams)
          .subscribe(
            next => {
              this.dichiarazioniAntimafia = next ? next : [];
              this.dichiarazioniAntimafiaTable = this.dichiarazioniAntimafia;
            },
            err => console.log("error: " + err)
          );
      });
    });

    this.menuAction = [
      {
        label: Labels.azioni.toUpperCase(),
        items: [
          {
            label: Labels.SCARICA,
            command: () => {
              this.downloadCertificazioneAntimafia();
            },
            separator: false
          },
          { separator: true },
          {
            label: Labels.DOMANDE_COLLEGATE,
            command: () => {
              this.domandeCollegate();
            },
            separator: false
          }
        ]
      }
    ];

    this.menuActionFull = [
      {
        label: Labels.azioni.toUpperCase(),
        items: [
          {
            label: Labels.SCARICA,
            command: () => {
              this.downloadCertificazioneAntimafia();
            },
            separator: false
          },
          { separator: true },
          {
            label: Labels.DETTAGLIO_RIFIUTO,
            command: () => {
              this.dettaglioRifiuto();
            },
            separator: false
          }
        ]
      }
    ];

    this.menuActionMin = [
      {
        label: Labels.azioni.toUpperCase(),
        items: [
          {
            label: Labels.SCARICA,
            command: () => {
              this.downloadCertificazioneAntimafia();
            },
            separator: false
          }
        ]
      }
    ];

  }

  onDropdownMenuOpen(i: number) {
    this.dichiarazioneSelezionata = this.dichiarazioniAntimafiaTable[i];
  }

  private downloadCertificazioneAntimafia() {
    this.antimafiaService.getDichiarazioneAntimafiaWithPdfFirmato(this.dichiarazioneSelezionata.id.toString()).subscribe(dichiarazione => {
      if (!dichiarazione.pdfFirmato) { return }
      this.antimafiaService.downloadDichiarazioneAntimafia(dichiarazione.pdfFirmato, dichiarazione.tipoPdfFirmato == null ? 'pdf' : dichiarazione.tipoPdfFirmato);
    });
  }

  private blobToFile = (theBlob: Blob, fileName: string): File => {
    var b: any = theBlob;
    //A Blob() is almost a File() - it's just missing the two properties below which we will add
    b.lastModifiedDate = new Date();
    b.name = fileName;
    //Cast to a File() type
    return <File>theBlob;
  };

  private getCaricheImportanti(
    dichiarazioneAntimafia: DichiarazioneAntimafia
  ): SoggettiImpresa[] {
    let soggettiImpresa = dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa.soggettiImpresa.map(
      x => Object.assign({}, x)
    );
    const soggImpresa = JSON.parse(JSON.stringify(soggettiImpresa));
    return soggImpresa
      .filter(
        c => (c.carica = c.carica.filter(carica => carica.codice.match("DT")))
      )
      .filter(c => (c.carica ? c.carica.length !== 0 : false));
  }

  private getSoggettiSelezionati(
    dichiarazioneAntimafia: DichiarazioneAntimafia
  ): SoggettiImpresa[] {
    // nel caso di più cariche duplico l'elemento per poterlo visualizzare correttamente
    let soggettiImpresa = dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa.soggettiImpresa.map(
      x => Object.assign({}, x)
    );
    const soggImpresa = JSON.parse(JSON.stringify(soggettiImpresa));
    return soggImpresa
      .filter(c => (c.carica = c.carica.filter(carica => carica.selezionato)))
      .filter(c => (c.carica ? c.carica.length !== 0 : false));
  }

  public dettaglioRifiuto() {

    this.antimafiaService.getNoteDichiarazioneAntimafia(this.dichiarazioneSelezionata.id, TipoNotaEnum.RIFIUTO_DICHIARAZIONE_ANTIMAFIA).subscribe((listNote: Array<any>) => {
      if (!A4gMessages.isUndefinedOrNull(listNote)) {
        listNote.forEach(nota => {
          let notaDichiarazioneAntimafia = JSON.parse(nota.nota);
          if (!A4gMessages.isUndefinedOrNull(nota) && !A4gMessages.isUndefinedOrNull(notaDichiarazioneAntimafia)) {

            if (!A4gMessages.isUndefinedOrNull(notaDichiarazioneAntimafia.nrProtocolloComunicazione)) {
              this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.info, Labels.COMUNICAZIONE_NR_PROTOCOLLO + ": " + notaDichiarazioneAntimafia.nrProtocolloComunicazione));
            }
            if (!A4gMessages.isUndefinedOrNull(notaDichiarazioneAntimafia.noteDiChiusura)) {
              this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.info, Labels.NOTE_DI_CHIUSURA + ": " + notaDichiarazioneAntimafia.noteDiChiusura));
            }
          }
        },
          (err: any) => {
            this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, err.error.message));
          }
        );
      }
    });
  }

  public domandeCollegate() {
    var params = {
      cuaa: this.fascicoloCorrente.fascicolo.cuaa
    };
    //let paramsIn = encodeURIComponent(JSON.stringify(params));
    let navigationExtras: NavigationExtras = {
      queryParams: {
        params: JSON.stringify(params)
      },
      relativeTo: this.route.parent
    };
    this.router.navigate(["./dettaglioDomandeCollegate"], navigationExtras);
  }
}
