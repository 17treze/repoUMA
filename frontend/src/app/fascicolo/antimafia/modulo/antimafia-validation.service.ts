import { Injectable } from "@angular/core";
import {
  Richiedente,
  DettaglioImpresa,
  AziendaCollegata,
  SoggettiImpresa,
  AllegatoDicFamConv
} from "../classi/datiDichiarazione";
import {
  ResponseValidazione,
  StatoValidazione
} from "../classi/statoValidazione";
import { DichiarazioneAntimafia } from "../classi/dichiarazioneAntimafia";
import { Messages } from "src/app/app.messages";
import { A4gMessages } from "src/app/a4g-common/a4g-messages";
import { Persona } from "src/app/a4g-common/classi/Persona";

@Injectable({
  providedIn: "root"
})
export class AntimafiaValidationService {
  constructor() { }

    // RICHIEDENTE
    validaProcedimenti(procedimenti: string[]): ResponseValidazione {
      const response: ResponseValidazione = { esito: StatoValidazione.OK };
      if (!procedimenti || procedimenti.length === 0) {
        response.esito = StatoValidazione.ERROR;
        response.messaggio = Messages.PROCEDIMENTI_ERR;
        return response;
      }
      return response;
    }

  // RICHIEDENTE
  validaRichiedente(richiedente: Richiedente): ResponseValidazione {
    const response: ResponseValidazione = { esito: StatoValidazione.OK };
    if (richiedente === undefined || richiedente === null) {
      response.esito = StatoValidazione.ERROR;
      response.messaggio = Messages.RICHIEDENTE_ERR;
      return response;
    }
    return response;
  }

  // IMPRESA
  validaImpresa(dettaglioImpresa: DettaglioImpresa): ResponseValidazione {
    const response: ResponseValidazione = { esito: StatoValidazione.OK };
    if (dettaglioImpresa === undefined || dettaglioImpresa === null) {
      response.esito = StatoValidazione.ERROR;
      response.messaggio = Messages.IMPRESA_ERR;
      return response;
    }
    return response;
  }

  // SOGGETTI CON CARICA
  validaSoggettiCarica(
    dichiarazione: DichiarazioneAntimafia
  ): ResponseValidazione {
    const response: ResponseValidazione = { esito: StatoValidazione.OK };
    const dettaglioImpresa = dichiarazione.datiDichiarazione.dettaglioImpresa;

    if (
      !dettaglioImpresa.soggettiImpresa ||
      dettaglioImpresa.soggettiImpresa.length === 0
    ) {
      response.esito = StatoValidazione.ERROR;
      response.messaggio = Messages.SOGGETTI_ERR;
    }

    if (dettaglioImpresa.formaGiuridicaCodice === "DI") {
      const isDirettoreTecnicoNominato = dettaglioImpresa.soggettiImpresa.some(
        soggetto => soggetto.carica.some(carica => carica.codice === "DT")
      );

      const isChecked = dichiarazione.assenzaDt;
      if (!isDirettoreTecnicoNominato) {
        if (!isChecked) {
          response.esito = StatoValidazione.ERROR;
          response.messaggio = A4gMessages.DT_ASSENTE;
          return response;
        }
      }
    }
    return response;
  }

  // AZIENDE COLLEGATE
  validaAziendeCollegate(
    aziendeCollegate: AziendaCollegata[]
  ): ResponseValidazione {
    const response: ResponseValidazione = { esito: StatoValidazione.OK };
    return response;
  }

  // FAMILIARI CONVIVENTI
  validaFamiliariConviventiCheckDichiarazione(
    soggettiImpresa: SoggettiImpresa[],
    isSalvaFamigliare?: boolean
  ): ResponseValidazione {
    const response: ResponseValidazione = { esito: StatoValidazione.OK };

    if (soggettiImpresa === null || soggettiImpresa.length === 0) {
      response.esito = StatoValidazione.ERROR;
      response.messaggio = Messages.SOGGETTI_ERR;
      return response;
    }
    const soggettiConFamiliariDichiarati = soggettiImpresa.filter(s => {
      return s.carica.some(c => c.selezionato === true);
    });

    // deve esserci almeno una checkbox su un qualsiasi soggetto
    if (soggettiConFamiliariDichiarati.length === 0) {
      response.esito = StatoValidazione.ERROR;
      response.messaggio = A4gMessages.FAMILIARI_DIC;
      return response;
    }

    // se ci sono pi? dichiarazioni assicurarsi che 1-NON ci sono fam conv 2-ne viene dichiarato almeno uno valido
    soggettiConFamiliariDichiarati.forEach(s => {
      if (
        s.familiariConviventi === undefined ||
        s.familiariConviventi == null ||
        s.familiariConviventi.length === 0
      ) {
        // se trovo una carica che non ha dichiarazione true
        if (!s.carica.some(c => c.dichiarazione === true)) {
          response.esito = StatoValidazione.ERROR;
          // response.messaggio = 'dichiarare familiari conviventi per '.concat(s.codiceFiscale);
          if (isSalvaFamigliare)
            response.messaggio = A4gMessages.FAMILIARI_ERR_SPUNTA;
          else response.messaggio = A4gMessages.FAMILIARI_ERR(s.codiceFiscale);
          return response;
        }
      }
    });

    return response;
  }

  validaFamiliariConviventiInserimentoCF(
    soggettiImpresa: SoggettiImpresa[]
  ): ResponseValidazione {
    const response: ResponseValidazione = { esito: StatoValidazione.OK };

    if (soggettiImpresa === null || soggettiImpresa.length === 0) {
      response.esito = StatoValidazione.ERROR;
      response.messaggio = Messages.SOGGETTI_ERR;
      return response;
    }
    let soggetto = soggettiImpresa.find(soggetto =>
      soggetto.familiariConviventi
        ? soggetto.familiariConviventi.some(fam => fam.codiceFiscale == null)
        : false
    );
    if (soggetto) {
      response.esito = StatoValidazione.ERROR;
      response.messaggio = A4gMessages.FAMILIARI_CF_ASSENTE(
        soggetto.codiceFiscale
      );
      return response;
    }
    return response;
  }

  validaFamiliariConviventiGradoDiParentela(
    soggettiImpresa: SoggettiImpresa[]
  ): ResponseValidazione {
    const response: ResponseValidazione = { esito: StatoValidazione.OK };
    if (soggettiImpresa === null || soggettiImpresa.length === 0) {
      response.esito = StatoValidazione.ERROR;
      response.messaggio = Messages.SOGGETTI_ERR;
      return response;
    }
    let soggetto = soggettiImpresa.find(soggetto =>
      soggetto.familiariConviventi
        ? soggetto.familiariConviventi.some(fam => fam.gradoParentela == null)
        : false
    );
    if (soggetto) {
      response.esito = StatoValidazione.ERROR;
      response.messaggio = A4gMessages.FAMILIARI_PARENT(soggetto.codiceFiscale);
      return response;
    }
    return response;
  }

  // formato in input time in millisecond
  validaFamiliariConviventiMaggiorenni(
    dataNascita: string,
    codiceFiscale: string
  ): ResponseValidazione {
    const response: ResponseValidazione = { esito: StatoValidazione.OK };
    if (dataNascita === null || dataNascita === undefined) {
      response.esito = StatoValidazione.ERROR;
      return response;
    }
    const now = new Date();
    const diffInMs: number =
      Date.parse(now.toDateString()) - Number(dataNascita);

    if (diffInMs < 567993600000) {
      response.esito = StatoValidazione.ERROR;
      response.messaggio = A4gMessages.FAMILIARI_MAGGIO(codiceFiscale);
      return response;
    }
    return response;
  }
  // questa ? condivisa tra familiari e verifica e invio
  validaFamiliariConviventiAllegati(
    soggettiImpresa: SoggettiImpresa[]
  ): ResponseValidazione {
    const response: ResponseValidazione = { esito: StatoValidazione.OK };
    if (soggettiImpresa === null || soggettiImpresa.length === 0) {
      response.esito = StatoValidazione.ERROR;
      response.messaggio = Messages.SOGGETTI_ERR;
      return response;
    }
    // per ogni soggetto carica dichiarato, esiste un allegato.
    const soggettiConFamiliariDichiarati = soggettiImpresa.filter(s => {
      return s.carica.some(c => c.selezionato === true);
    });
    soggettiConFamiliariDichiarati.forEach(soggetto => {
      // solo cariche con selezionato
      const caricheSelezionate = soggetto.carica.filter(s => s.selezionato);

      // ci deve essrre almeno un file. => href OR lista allegato OR inclusivo
      caricheSelezionate.forEach(carica => {
        if (!carica.href) {
          response.esito = StatoValidazione.ERROR;
          response.messaggio = A4gMessages.ALLEGATO_MANCANTE(
            soggetto.codiceFiscale
          );
          return response;
        }
      });
    });
    return response;
  }

  // VERIFICA E INVIO - data uploat pdf firmato : dtUploadPdfFirmato
  // questo metodo mi dice se esiste un file
  validaVerificaDichiarazioneAllegata(uploadResult: Date): ResponseValidazione {
    const response: ResponseValidazione = { esito: StatoValidazione.OK };
    // non ho caricato alcun doc
    if (uploadResult === null || uploadResult === undefined) {
      response.esito = StatoValidazione.ERROR;
      response.messaggio = A4gMessages.DICHIARAZIONE_MANCANTE;
      return response;
    }
    return response;
  }

  // Upload familiari conviventi
  validaAllegatoSize(file: File) {
    const response: ResponseValidazione = { esito: StatoValidazione.OK };
    if (A4gMessages.UPLOAD_FILE_MAX_SIZE_FILE < 0) {
      return response;
    }

    var fileSizeinMB = file.size / (1024 * 1000);
    var size = Math.round(fileSizeinMB * 100) / 100;

    if (size > A4gMessages.UPLOAD_FILE_MAX_SIZE_FILE) {
      response.esito = StatoValidazione.ERROR;
      response.messaggio = A4gMessages.FILE_MAXSIZE(
        A4gMessages.UPLOAD_FILE_MAX_SIZE_FILE
      );
    }
    return response;
  }

  //Upload familiari conviventi
  validaAllegatoType(file: File, isFirmaDigitale: boolean) {
    const response: ResponseValidazione = { esito: StatoValidazione.OK };
    if (file.name !== undefined) {
      if (
        isFirmaDigitale &&
        !(
          file.name.endsWith(A4gMessages.UPLOAD_FILE_TYPE_PDFP7M) ||
          file.name.endsWith(A4gMessages.UPLOAD_FILE_TYPE_PDF)
        )
      ) {
        response.esito = StatoValidazione.ERROR;
        response.messaggio = A4gMessages.FILE_TYPE(
          A4gMessages.UPLOAD_FILE_TYPE_PDF +
          " oppure " +
          A4gMessages.UPLOAD_FILE_TYPE_PDFP7M
        );
        return response;
      } else if (
        !isFirmaDigitale &&
        !file.name.endsWith(A4gMessages.UPLOAD_FILE_TYPE_PDF)
      ) {
        response.esito = StatoValidazione.ERROR;
        response.messaggio = A4gMessages.FILE_TYPE(
          A4gMessages.UPLOAD_FILE_TYPE_PDF
        );
        return response;
      } else {
        return response;
      }
    }
  }

  validaVerificaInformativaPrivacy(
    persone: Persona[],
    codiceFiscale: string
  ): ResponseValidazione {
    const response: ResponseValidazione = { esito: StatoValidazione.OK };
    if (!persone || persone.length > 1) {
      response.esito = StatoValidazione.ERROR;
      response.messaggio = A4gMessages.ERRORE_GENERICO;
    } else {
      //errore nel caso in cui la lista è vuola o
      //il nrProtocolloPrivacyGenerale non è valorizzato
      if (
        persone.length == 0 ||
        (persone[0].nrProtocolloPrivacyGenerale ? false : true)
      ) {
        response.esito = StatoValidazione.ERROR;
        response.messaggio = A4gMessages.INFORMATIVA_PRIVACY_ASSENTE(
          codiceFiscale
        );
      }
    }
    return response;
  }
}
