import { ConverterUnitaDiMisuraService } from './converter-unita-di-misura.service';
import { FabbisognoDto } from '../models/dto/FabbisognoDto';
import { DichiarazioneDto } from '../models/dto/DichiarazioneDto';
import { Injectable } from '@angular/core';
import { LavorazioneDto } from '../models/dto/LavorazioneDto';
import { RaggruppamentoLavorazioneDto } from '../models/dto/RaggruppamentoDto';
import { RaggruppamentoLavorazioneViewModel } from '../models/viewModels/RaggruppamentoLavorazioneViewModel';
import { LavorazioneViewModel } from '../models/viewModels/LavorazioneVIewModel';
import { TipoCarburante } from '../models/enums/TipoCarburante.enum';

@Injectable({
  providedIn: 'root'
})
export class LavorazioniBuiderService {

  constructor(
    private converterUnitaDiMisuraService: ConverterUnitaDiMisuraService
  ) { }

  raggruppamentoLavorazioneDtoToRaggruppmanetoLavorazioneViewModel(raggruppamenti: Array<RaggruppamentoLavorazioneDto>, dichiarazioni: Array<DichiarazioneDto>): Array<RaggruppamentoLavorazioneViewModel> {
    const raggruppamentiVM: Array<RaggruppamentoLavorazioneViewModel> = [];
    if (!raggruppamenti || !raggruppamenti.length) {
      return raggruppamentiVM;
    }
    raggruppamenti.forEach((raggruppamento: RaggruppamentoLavorazioneDto) => {
      const raggruppamentoVM = {} as RaggruppamentoLavorazioneViewModel;
      raggruppamentoVM.indice = raggruppamento.indice;
      raggruppamentoVM.nome = raggruppamento.nome;
      raggruppamentoVM.superficieMassima = raggruppamento.superficieMassima;
      raggruppamentoVM.descrizioneCompleta = raggruppamento.indice + ' ' + raggruppamento.nome;
      raggruppamentoVM.lavorazioni = this.getLavorazioni(raggruppamento, dichiarazioni);
      raggruppamentiVM.push(raggruppamentoVM);
    });
    return raggruppamentiVM;
  }

  private getLavorazioni(raggruppamento: RaggruppamentoLavorazioneDto, dichiarazioni: Array<DichiarazioneDto>): Array<LavorazioneViewModel> {
    if (!raggruppamento.lavorazioni || !raggruppamento.lavorazioni.length) {
      return [];
    }
    const lavorazioniVM: Array<LavorazioneViewModel> = [];
    raggruppamento.lavorazioni.forEach((lavorazione: LavorazioneDto) => {
      let lavorazioneVM: LavorazioneViewModel = {} as LavorazioneViewModel;
      lavorazioneVM.id = lavorazione.id;
      lavorazioneVM.indice = lavorazione.indice;
      lavorazioneVM.descrizioneCompleta = raggruppamento.indice + '.' + lavorazione.indice + ' ' + lavorazione.nome; // concatenazione di tipo 1.1, 1.2, ecc.
      lavorazioneVM.unitaDiMisura = this.converterUnitaDiMisuraService.decodificaUnitaDiMisura(lavorazione.unitaDiMisura);
      lavorazioneVM.tipo = lavorazione.tipologia;
      lavorazioneVM.superficieMassima = raggruppamento.superficieMassima;
      lavorazioneVM = this.addFabbisogniToLavorazioni(dichiarazioni, lavorazione, lavorazioneVM);
      lavorazioniVM.push(lavorazioneVM);
    });
    return lavorazioniVM;
  }

  private addFabbisogniToLavorazioni(dichiarazioni: Array<DichiarazioneDto>, lavorazione: LavorazioneDto, lavorazioneVM: LavorazioneViewModel): LavorazioneViewModel {
    if (!dichiarazioni || !dichiarazioni.length) {
      return lavorazioneVM;
    }
    const dichiarazioniTrovate: Array<DichiarazioneDto> = dichiarazioni.filter((dichiarazione: DichiarazioneDto) => lavorazione.id == dichiarazione.lavorazioneId);
    if (dichiarazioniTrovate && dichiarazioniTrovate.length) {
      const fabbisogni: Array<FabbisognoDto> = dichiarazioniTrovate[0].fabbisogni;
      if (fabbisogni && fabbisogni.length) {
        fabbisogni.forEach((fabbisogno: FabbisognoDto) => {
          const quantita = fabbisogno.quantita.toString().replace('.', ',');
          lavorazioneVM[fabbisogno.carburante] = quantita;
        });
      }
    }
    return lavorazioneVM;
  }

  public lavorazioniViewModelToDichiarazioniDtoBuilder(lavorazioneViewArray: Array<LavorazioneViewModel>): Array<DichiarazioneDto> {
    const dichiarazioni: Array<DichiarazioneDto> = [];
    lavorazioneViewArray
      .filter((lavorazioneVM: LavorazioneViewModel) => (lavorazioneVM[TipoCarburante.GASOLIO] != null) || (lavorazioneVM[TipoCarburante.BENZINA] != null))
      .forEach((lavorazioneWithDichiarazione: LavorazioneViewModel) => {
        const dichiarazione = {} as DichiarazioneDto;
        dichiarazione.fabbisogni = [];
        dichiarazione.lavorazioneId = lavorazioneWithDichiarazione.id;
        if (lavorazioneWithDichiarazione[TipoCarburante.GASOLIO] != null) {
          dichiarazione.fabbisogni.push(this.buildFabbisogno(lavorazioneWithDichiarazione[TipoCarburante.GASOLIO], TipoCarburante.GASOLIO));
        }
        if (lavorazioneWithDichiarazione[TipoCarburante.BENZINA] != null) {
          dichiarazione.fabbisogni.push(this.buildFabbisogno(lavorazioneWithDichiarazione[TipoCarburante.BENZINA], TipoCarburante.BENZINA));
        }
        // aggiungo solo le dichiarazioni con fabbisogni
        if (dichiarazione.fabbisogni && dichiarazione.fabbisogni.length) {
          dichiarazioni.push(dichiarazione);
        }
      });
    return dichiarazioni;
  }

  private buildFabbisogno(quantitaCarburante: string | number, carburante: keyof typeof TipoCarburante): FabbisognoDto {
    const quantita = quantitaCarburante.toString().replace(',', '.');
    const fabbisogno: FabbisognoDto = { quantita, carburante };
    return fabbisogno;
  }
}
