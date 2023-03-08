import { Injectable } from '@angular/core';
import { ConsuntivoDto, InfoAllegatoConsuntivoDto } from '../../models/dto/ConsuntivoDto';
import { AllegatoMotivazioneView, MotivazioneConsuntivoDto } from '../../models/dto/MotivazioneConsuntivoDto';
import { TipoCarburanteConsuntivo } from '../../models/enums/TipoCarburanteConsuntivo.enum';
import { TipoConsuntivo } from '../../models/enums/TipoConsuntivo';
import { DichiarazioneConsumiVM } from '../../models/viewModels/DichiarazioneConsumiViewModel';
import * as _ from 'lodash';
import { FormatConverterService } from 'src/app/uma/shared-uma/services/format-converter.service';
import { DichiarazioneConsumiPatchDto } from '../../models/dto/DichiarazioneConsumiDto';
@Injectable({
  providedIn: 'root'
})
export class ConsumiBuilderService {

  constructor(
    private formatSrv: FormatConverterService
  ) { }

  public toAllegatoMotivazione(consuntivoDto: ConsuntivoDto, idDichiarazione: string): Array<AllegatoMotivazioneView> {
    if (!consuntivoDto || !consuntivoDto.infoAllegati || !consuntivoDto.infoAllegati.length) {
      return null;
    }
    const allegatoMotivazioneList: Array<AllegatoMotivazioneView> = [];
    consuntivoDto.infoAllegati.forEach((allegatoConsuntivoDto: InfoAllegatoConsuntivoDto) => {
      const allegatoMotivazione = {} as AllegatoMotivazioneView;
      allegatoMotivazione.descrizione = allegatoConsuntivoDto.descrizione || undefined;
      allegatoMotivazione.name = allegatoConsuntivoDto.nome;
      allegatoMotivazione.idAllegato = allegatoConsuntivoDto.id;
      allegatoMotivazione.idConsuntivo = consuntivoDto.id;
      allegatoMotivazione.idDichiarazione = idDichiarazione;
      allegatoMotivazione.tipoDocumento = allegatoConsuntivoDto.tipoDocumento || undefined;
      allegatoMotivazioneList.push(allegatoMotivazione);
    });
    return allegatoMotivazioneList;
  }

  // Allegati con byte (aggiunti da FE)
  public toFile(allegatiMotivazione: Array<AllegatoMotivazioneView>): Array<File> {
    const fileArray: Array<File> = [];
    if (!allegatiMotivazione || !allegatiMotivazione.length) {
      return fileArray;
    }
    allegatiMotivazione.forEach((allegato: AllegatoMotivazioneView) => {
      if (allegato.file) {
        fileArray.push(allegato.file);
      }
    });
    return fileArray;
  }

  // Allegati con byte (aggiunti da FE)
  public toFileWithMetaDati(allegatiMotivazione: Array<AllegatoMotivazioneView>, separatore: string): Array<File> {
    const fileArray: Array<File> = [];
    if (!allegatiMotivazione || !allegatiMotivazione.length) {
      return fileArray;
    }
    // Concanteno i metadati con i separatori
    allegatiMotivazione.forEach(allegato => {
      if (allegato.file) {
        const myNewFile = new File([allegato.file], allegato.file.name + separatore + allegato.tipoDocumento + separatore + allegato.descrizione, { type: allegato.file.type });
        allegato.file = myNewFile;
        fileArray.push(allegato.file);
      }
    });
    return fileArray;
  }

  // Allegati senza byte (già presenti sul BE)
  public toInfoMetadatoConsuntivo(allegatiMotivazione: Array<AllegatoMotivazioneView>): Array<InfoAllegatoConsuntivoDto> {
    const fileArray: Array<InfoAllegatoConsuntivoDto> = [];
    if (!allegatiMotivazione || !allegatiMotivazione.length) {
      return fileArray;
    }
    allegatiMotivazione.forEach((allegato: AllegatoMotivazioneView) => {
      if (!allegato.file) {
        const infoAllegato = {} as InfoAllegatoConsuntivoDto;
        infoAllegato.id = allegato.idAllegato;
        fileArray.push(infoAllegato);
      }
    });
    return fileArray;
  }

  public buildConsuntiviDto(dichiarazione: DichiarazioneConsumiVM): Array<ConsuntivoDto> {
    const consuntiviDto: Array<ConsuntivoDto> = [];
    const { accisaMotivo, ...withoutAccisa } = dichiarazione; // omit accisaMotivo
    for (const prop in withoutAccisa) {
      if (prop === 'ammissibileGasolioContoTerzi') {
        consuntiviDto.push(this.buildConsuntivoDto(withoutAccisa[prop], TipoCarburanteConsuntivo.GASOLIO_TERZI, TipoConsuntivo.AMMISSIBILE));
      } else if (prop.indexOf('consumato') > -1) {
        consuntiviDto.push(this.setConsumato(prop, withoutAccisa[prop]));
      } else if (prop.indexOf('rimanenza') > -1) {
        consuntiviDto.push(this.setRimanenza(prop, withoutAccisa[prop]));
      } else if (prop.indexOf('recupero') > -1) {
        consuntiviDto.push(this.setRecupero(prop, withoutAccisa[prop]));
      }
    }
    return consuntiviDto;
  }

  public buildConsuntivoDto(quantita: string, tipoCarburante: keyof typeof TipoCarburanteConsuntivo, tipo: keyof typeof TipoConsuntivo, motivo?: MotivazioneConsuntivoDto): ConsuntivoDto {
    const consuntivoDto = {} as ConsuntivoDto;
    consuntivoDto.quantita = this.formatSrv.toNumberOrNull(quantita); // set a null se il campo è stato cancellato
    consuntivoDto.carburante = tipoCarburante;
    consuntivoDto.tipo = tipo;
    consuntivoDto.motivazione = motivo && motivo.motivazione || undefined;
    consuntivoDto.infoAllegati = motivo && motivo.allegati && this.toInfoMetadatoConsuntivo(motivo.allegati) || undefined;  // allegati senza byte -> solo riferimenti, già presenti sul BE
    return consuntivoDto;
  }

  /** @deprecated */
  public buildAllegatiConsuntivoDto(motivo: MotivazioneConsuntivoDto): Array<string> {
    let idAllegati: Array<string> = [];
    idAllegati = motivo && motivo.allegati && this.toInfoMetadatoConsuntivo(motivo.allegati).map((allegato: InfoAllegatoConsuntivoDto) => allegato.id);  // allegati senza byte -> solo riferimenti, già presenti sul BE
    return idAllegati;
  }

  public buildDichiarazioneConsumiPatch(motivazioneAccisa: string, dataConduzione: string): DichiarazioneConsumiPatchDto {
    const patch = {} as DichiarazioneConsumiPatchDto;
    patch.dataConduzione = dataConduzione;
    patch.motivazioneAccisa = motivazioneAccisa;
    return patch;
  }

  private setConsumato(property: string, value: string): ConsuntivoDto {
    switch (property) {
      case 'consumatoGasolioContoProprio':
        return this.buildConsuntivoDto(value, TipoCarburanteConsuntivo.GASOLIO, TipoConsuntivo.CONSUMATO);
      case 'consumatoGasolioContoTerzi':
        return this.buildConsuntivoDto(value, TipoCarburanteConsuntivo.GASOLIO_TERZI, TipoConsuntivo.CONSUMATO);
      case 'consumatoBenzina':
        return this.buildConsuntivoDto(value, TipoCarburanteConsuntivo.BENZINA, TipoConsuntivo.CONSUMATO);
      case 'consumatoGasolioSerre':
        return this.buildConsuntivoDto(value, TipoCarburanteConsuntivo.GASOLIO_SERRE, TipoConsuntivo.CONSUMATO);
      default: break;
    }
  }

  private setRimanenza(property: string, value: string): ConsuntivoDto {
    switch (property) {
      case 'rimanenzaDicembreGasolioContoProprio':
        return this.buildConsuntivoDto(value, TipoCarburanteConsuntivo.GASOLIO, TipoConsuntivo.RIMANENZA);
      case 'rimanenzaDicembreGasolioContoTerzi':
        return this.buildConsuntivoDto(value, TipoCarburanteConsuntivo.GASOLIO_TERZI, TipoConsuntivo.RIMANENZA);
      case 'rimanenzaDicembreBenzina':
        return this.buildConsuntivoDto(value, TipoCarburanteConsuntivo.BENZINA, TipoConsuntivo.RIMANENZA);
      case 'rimanenzaDicembreGasolioSerre':
        return this.buildConsuntivoDto(value, TipoCarburanteConsuntivo.GASOLIO_SERRE, TipoConsuntivo.RIMANENZA);
      default: break;
    }
  }

  private setRecupero(property: string, value: string): ConsuntivoDto {
    switch (property) {
      case 'recuperoGasolioContoProprio':
        return this.buildConsuntivoDto(value, TipoCarburanteConsuntivo.GASOLIO, TipoConsuntivo.RECUPERO);
      case 'recuperoGasolioContoTerzi':
        return this.buildConsuntivoDto(value, TipoCarburanteConsuntivo.GASOLIO_TERZI, TipoConsuntivo.RECUPERO);
      case 'recuperoBenzina':
        return this.buildConsuntivoDto(value, TipoCarburanteConsuntivo.BENZINA, TipoConsuntivo.RECUPERO);
      case 'recuperoGasolioSerre':
        return this.buildConsuntivoDto(value, TipoCarburanteConsuntivo.GASOLIO_SERRE, TipoConsuntivo.RECUPERO);
      default: break;
    }
  }
}
