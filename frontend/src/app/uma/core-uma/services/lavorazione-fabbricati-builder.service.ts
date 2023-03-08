import { FormatConverterService } from './../../shared-uma/services/format-converter.service';
import { Injectable } from '@angular/core';
import { DichiarazioneDto } from '../models/dto/DichiarazioneDto';
import { DichiarazioneFabbricatoDto } from '../models/dto/DichiarazioneFabbricatoDto';
import { FabbisognoDto } from '../models/dto/FabbisognoDto';
import { FabbricatoDto } from '../models/dto/FabbricatoDto';
import { LavorazioneDto } from '../models/dto/LavorazioneDto';
import { RaggruppamentoLavorazioneDto } from '../models/dto/RaggruppamentoDto';
import { TipoCarburante } from '../models/enums/TipoCarburante.enum';
import { LavorazioneFabbricatiViewModel } from '../models/viewModels/LavorazioneFabbricatiViewModel';
import { RaggruppamentoLavorazioneFabbricatoViewModel } from '../models/viewModels/RaggruppamentoLavorazioneFabbricatoViewModel';
import { ConverterUnitaDiMisuraService } from './converter-unita-di-misura.service';

@Injectable({
  providedIn: 'root'
})
export class LavorazioneFabbricatiBuilderService {

  constructor(
    private converterUnitaDiMisuraService: ConverterUnitaDiMisuraService,
    private formatConverterService: FormatConverterService
  ) { }

  fabbricatiDtoToRaggruppamentiLavorazioneViewModel(raggruppamenti: Array<RaggruppamentoLavorazioneDto>, fabbisogniFabbricati: Array<DichiarazioneFabbricatoDto>): Array<RaggruppamentoLavorazioneFabbricatoViewModel> {
    const raggruppamentiVM: Array<RaggruppamentoLavorazioneFabbricatoViewModel> = [];
    if (!raggruppamenti || !raggruppamenti.length) {
      return raggruppamentiVM;
    }
    raggruppamenti.forEach((raggruppamento: RaggruppamentoLavorazioneDto) => {
      const raggruppamentoVM = {} as RaggruppamentoLavorazioneFabbricatoViewModel;
      raggruppamentoVM.indice = raggruppamento.indice;
      raggruppamentoVM.nome = raggruppamento.nome;
      raggruppamentoVM.descrizioneCompleta = raggruppamento.indice + ' ' + raggruppamento.nome;
      // ricavo n lavorazioni dal raggruppamento
      const lavorazioni = this.getLavorazioniFromFabbricati(raggruppamento);
      // replico le lavorazioni trovate al passo precedente quanti sono i fabbricati, settando a ciascuna il proprio fabbricato di appartenenza
      // aggiungo i fabbisogni a ciascuna lavorazione
      raggruppamentoVM.lavorazioni = this.addFabbisogniToFabbricati(this.replicaLavorazioni(lavorazioni, raggruppamento.fabbricati), fabbisogniFabbricati);
      raggruppamentiVM.push(raggruppamentoVM);
    });
    return raggruppamentiVM;
  }

  private replicaLavorazioni(lavorazioniVM: Array<LavorazioneFabbricatiViewModel>, fabbricati: Array<FabbricatoDto>): Array<LavorazioneFabbricatiViewModel> {
    if (!fabbricati || !fabbricati.length) {
      return lavorazioniVM;
    }
    const lavorazioniOut: Array<LavorazioneFabbricatiViewModel> = [];
    lavorazioniVM.forEach((lav: LavorazioneFabbricatiViewModel) => {
      fabbricati.forEach((fabb: FabbricatoDto) => {
        const lavorazioneWithFabbricato = { ...lav } as LavorazioneFabbricatiViewModel;
        lavorazioneWithFabbricato.comuneProvincia = fabb.comune + '(' + fabb.siglaProvincia + ')';
        lavorazioneWithFabbricato.idFabbricato = fabb.id;
        lavorazioneWithFabbricato.particella = fabb.particella;
        lavorazioneWithFabbricato.subalterno = fabb.subalterno;
        lavorazioneWithFabbricato.volume = fabb.volume.toString();
        lavorazioniOut.push(lavorazioneWithFabbricato);
      });
    });
    return lavorazioniOut;
  }

  private getLavorazioniFromFabbricati(raggruppamento: RaggruppamentoLavorazioneDto): Array<LavorazioneFabbricatiViewModel> {
    if (!raggruppamento.lavorazioni || !raggruppamento.lavorazioni.length) {
      return [];
    }
    const lavorazioniFabbricatiVM: Array<LavorazioneFabbricatiViewModel> = [];
    raggruppamento.lavorazioni.forEach((lavorazione: LavorazioneDto) => {
      const lavorazioneFabbrVM: LavorazioneFabbricatiViewModel = {} as LavorazioneFabbricatiViewModel;
      lavorazioneFabbrVM.id = lavorazione.id;
      lavorazioneFabbrVM.indice = lavorazione.indice;
      lavorazioneFabbrVM.descrizioneCompleta = raggruppamento.indice + '.' + lavorazione.indice + ' ' + lavorazione.nome; // concatenazione di tipo 1.1, 1.2, ecc.
      lavorazioneFabbrVM.unitaDiMisura = this.converterUnitaDiMisuraService.decodificaUnitaDiMisura(lavorazione.unitaDiMisura);
      lavorazioneFabbrVM.tipo = lavorazione.tipologia;
      lavorazioneFabbrVM.superficieMassima = raggruppamento.superficieMassima;
      lavorazioniFabbricatiVM.push(lavorazioneFabbrVM);
    });
    return lavorazioniFabbricatiVM;
  }

  private addFabbisogniToFabbricati(lavorazioniFabbrVM: Array<LavorazioneFabbricatiViewModel>, fabbisogniFabbricati: Array<DichiarazioneFabbricatoDto>): Array<LavorazioneFabbricatiViewModel> {
    if (!fabbisogniFabbricati || !fabbisogniFabbricati.length) {
      return lavorazioniFabbrVM;
    }
    // per ogni fabbricato, ciclo le dichiarazioni;
    // trovata la dichiarazione relativa alla lavorazione che sto analizzando (in input), setto la relativa quantita di fabbisogno dichiarato 
    fabbisogniFabbricati.forEach((fabbisognoFabbricato: DichiarazioneFabbricatoDto) => {

      // setto i fabbisogni se ci sono
      if (fabbisognoFabbricato.dichiarazioni && fabbisognoFabbricato.dichiarazioni.length) {
        lavorazioniFabbrVM.forEach((lavFabb: LavorazioneFabbricatiViewModel) => {

          if (fabbisognoFabbricato.idFabbricato == lavFabb.idFabbricato) {
            fabbisognoFabbricato.dichiarazioni.forEach((dichiarazione: DichiarazioneDto) => {

              if (dichiarazione.lavorazioneId == lavFabb.id) { // trovata la lavorazione, setto il fabbisogno
                dichiarazione.fabbisogni.forEach((fabbisogno: FabbisognoDto) => {
                  const quantita = fabbisogno.quantita.toString().replace('.', ',');
                  lavFabb[fabbisogno.carburante] = quantita;
                  /** vale solo per le lavorazioni sotto serra il calcolo dei mesi */
                  const quantNumber = this.formatConverterService.toNumber(quantita);
                  const volNumber = this.formatConverterService.toNumber(lavFabb.volume);
                  lavFabb.mesi = quantNumber > 0 && volNumber > 0 && this.formatConverterService.isMultipleBy(quantNumber, volNumber) ? quantNumber / volNumber : null;
                });
              }
            });
          }
        });
      }
    });
    return lavorazioniFabbrVM;
  }

  public lavorazioniViewModelToDichiarazioniFabbricatoDtoBuilder(lavorazioniFabbricatiVM: Array<LavorazioneFabbricatiViewModel>): Array<DichiarazioneFabbricatoDto> {
    const dichiarazioniFabbricatiTotali: Array<DichiarazioneFabbricatoDto> = [];
    // filtro le lavorazioni senza carburante dichiarato
    lavorazioniFabbricatiVM
      .filter((lavorazioneVM: LavorazioneFabbricatiViewModel) => lavorazioneVM[TipoCarburante.GASOLIO] != null)
      .forEach((lavorazioneWithDichiarazioniVM: LavorazioneFabbricatiViewModel) => {
        let nuovaDichiarazioneFabbricatoDto: DichiarazioneFabbricatoDto;
        let nuovaDichiarazioneDto: DichiarazioneDto;

        // se presente una dichiarazioneFabbricatoDto. aggiorna questa altrimenti la creo nuova
        const nuoveDichiarazioniFabbrTrovate = dichiarazioniFabbricatiTotali.filter((dicFabb: DichiarazioneFabbricatoDto) => dicFabb.idFabbricato == lavorazioneWithDichiarazioniVM.idFabbricato);
        if (nuoveDichiarazioniFabbrTrovate.length) {
          nuovaDichiarazioneFabbricatoDto = nuoveDichiarazioniFabbrTrovate[0];
        } else {
          nuovaDichiarazioneFabbricatoDto = {} as DichiarazioneFabbricatoDto;
          nuovaDichiarazioneFabbricatoDto.idFabbricato = lavorazioneWithDichiarazioniVM.idFabbricato;
          nuovaDichiarazioneFabbricatoDto.dichiarazioni = [];
        }

        // se presente una dichiarazioneDto. aggiorna questa altrimenti la creo nuova
        const nuoveDichiarazioniTrovate = nuovaDichiarazioneFabbricatoDto.dichiarazioni.filter((dic: DichiarazioneDto) => dic.lavorazioneId == lavorazioneWithDichiarazioniVM.id);
        if (nuoveDichiarazioniTrovate.length) {
          nuovaDichiarazioneDto = nuoveDichiarazioniTrovate[0];
        } else {
          nuovaDichiarazioneDto = {} as DichiarazioneDto;
          nuovaDichiarazioneDto.lavorazioneId = lavorazioneWithDichiarazioniVM.id;
          nuovaDichiarazioneDto.fabbisogni = [];
        }

        nuovaDichiarazioneDto.fabbisogni.push(this.buildFabbisogno(lavorazioneWithDichiarazioniVM[TipoCarburante.GASOLIO], TipoCarburante.GASOLIO));
        nuovaDichiarazioneFabbricatoDto.dichiarazioni.push(nuovaDichiarazioneDto);

        // aggiungo solo i fabbisogni con dichiarazioni
        if (nuovaDichiarazioneFabbricatoDto.dichiarazioni && nuovaDichiarazioneFabbricatoDto.dichiarazioni.length) {
          // aggiungo solo le nuoveDichiarazioni, le altre vanno solo aggiornate senza push
          if (!nuoveDichiarazioniFabbrTrovate.length) {
            dichiarazioniFabbricatiTotali.push(nuovaDichiarazioneFabbricatoDto);
          }
        }
      });
    return dichiarazioniFabbricatiTotali;
  }

  private buildFabbisogno(quantitaCarburante: string | number, carburante: keyof typeof TipoCarburante): FabbisognoDto {
    const quantita = quantitaCarburante.toString().replace(',', '.');
    const fabbisogno: FabbisognoDto = { quantita, carburante };
    return fabbisogno;
  }

}
