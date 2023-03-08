import { EsitoValidazioneParticellaCatastoEnum } from './../../classi/enums/dotazione.-tecnica/EsitoValidazioneParticellaCatasto.enum';
import { DatiCatastaliDto } from './../../classi/dto/dotazione-tecnica/DettaglioFabbricatoDto';
import { DatiCatastaliVM, InfoCatastoVm, ParticellaVm } from './../../classi/viewModels/FabbricatoVM';
import { FormatConverterService } from 'src/app/uma/shared-uma/services/format-converter.service';
import { MotoreDto } from './../../classi/dto/dotazione-tecnica/MotoreDto';
import { DettaglioMacchinaDto } from './../../classi/dto/dotazione-tecnica/DettaglioMacchinaDto';
import { SelectItem } from 'primeng/api';
import { Injectable } from '@angular/core';
import { ClasseFunzionaleDto, SottoTipoDto, TipologiaDto } from '../../classi/dto/dotazione-tecnica/TipologiaDto';
import { TipologiaPossessoEnum } from '../../classi/enums/dotazione.-tecnica/TipologiaPossesso.enum';
import { AlimentazioneEnum } from '../../classi/enums/dotazione.-tecnica/Alimentazione.enum';
import { DettaglioMacchinaVM } from '../../classi/viewModels/MacchinarioVM';
import { DettaglioFabbricatoDto, DettaglioFabbricatiStrumentaliDto, DatiSerreProtezioniDto, DettaglioZootecniaStoccaggioDto } from '../../classi/dto/dotazione-tecnica/DettaglioFabbricatoDto';
import { FabbricatoTypeEnum } from '../../classi/enums/dotazione.-tecnica/FabbricatoTipologia.enum';
import { DettaglioFabbricatoVM, DettaglioFabbricatoStrumentaleVM, DettaglioSerreVM, TipiStruttureVM, DettaglioStruttureZootecniaStoccaggioVM, DettaglioStruttureZootecniaAreeScoperteStalleVM } from '../../classi/viewModels/FabbricatoVM';
@Injectable({
  providedIn: 'root'
})
export class DotazioneTecnicaBuilderService {

  constructor(
    private formatConverterService: FormatConverterService
  ) { }

  toDettaglioMacchinaVM(dettaglioMacchinaDto: DettaglioMacchinaDto): DettaglioMacchinaVM {
    const dettaglioVM = {} as DettaglioMacchinaVM;
    // Mezzo
    dettaglioVM.id = dettaglioMacchinaDto.id;
    dettaglioVM.tipologiaMacchinario = this.tipologiaDtoToSelectItemConverter({ id: dettaglioMacchinaDto.sottotipo.id, descrizione: dettaglioMacchinaDto.sottotipo.descrizione });
    dettaglioVM.classeFunzionaleMacchinario = dettaglioMacchinaDto.sottotipo.classiFunzionali && dettaglioMacchinaDto.sottotipo.classiFunzionali.length && this.tipologiaDtoToSelectItemConverter({ id: dettaglioMacchinaDto.sottotipo.classiFunzionali[0].id, descrizione: dettaglioMacchinaDto.sottotipo.classiFunzionali[0].descrizione });
    dettaglioVM.sottoTipologiaMacchinario = dettaglioMacchinaDto.sottotipo.classiFunzionali[0].tipologie && dettaglioMacchinaDto.sottotipo.classiFunzionali[0].tipologie.length && this.tipologiaDtoToSelectItemConverter({ id: dettaglioMacchinaDto.sottotipo.classiFunzionali[0].tipologie[0].id, descrizione: dettaglioMacchinaDto.sottotipo.classiFunzionali[0].tipologie[0].descrizione });
    dettaglioVM.marcaMacchinario = dettaglioMacchinaDto.marca;
    dettaglioVM.targa = dettaglioMacchinaDto.targa;
    dettaglioVM.modello = dettaglioMacchinaDto.modello;
    dettaglioVM.numeroMatricola = dettaglioMacchinaDto.numeroMatricola;
    dettaglioVM.numeroTelaio = dettaglioMacchinaDto.numeroTelaio;
    dettaglioVM.annoCostruzione = dettaglioMacchinaDto.annoCostruzione;
    dettaglioVM.switchMotore = dettaglioMacchinaDto.motore && dettaglioMacchinaDto.motore.alimentazione ? true : false;
    // Motore
    dettaglioVM.marcaMotore = dettaglioMacchinaDto.motore ? dettaglioMacchinaDto.motore.marca : null;
    dettaglioVM.tipo = dettaglioMacchinaDto.motore ? dettaglioMacchinaDto.motore.tipo : null;
    dettaglioVM.alimentazione = dettaglioMacchinaDto.motore ? this.tipologiaDtoToSelectItemConverter({ id: dettaglioMacchinaDto.motore.alimentazione, descrizione: dettaglioMacchinaDto.motore.alimentazione }) : null;
    dettaglioVM.potenza = dettaglioMacchinaDto.motore ? dettaglioMacchinaDto.motore.potenza : null;
    dettaglioVM.numeroMotore = dettaglioMacchinaDto.motore ? dettaglioMacchinaDto.motore.matricola : null;
    // Possesso
    dettaglioVM.tipologiaPossesso = this.tipologiaDtoToSelectItemConverter({ id: dettaglioMacchinaDto.tipoPossesso, descrizione: dettaglioMacchinaDto.tipoPossesso });
    dettaglioVM.codiceFiscale = dettaglioMacchinaDto.codiceFiscale;
    dettaglioVM.ragioneSociale = dettaglioMacchinaDto.ragioneSociale;
    // Flag migrazione
    dettaglioVM.flagMigrato = dettaglioMacchinaDto.flagMigrato;
    return dettaglioVM;
  }

  toDettaglioMacchinaDto(dettaglioMacchinaVm: DettaglioMacchinaVM, idMacchina?: number): DettaglioMacchinaDto {
    const dettaglioMacchinaDto = {} as DettaglioMacchinaDto;
    dettaglioMacchinaDto.id = idMacchina != null ? idMacchina : null;
    // Sezione Mezzo
    dettaglioMacchinaDto.sottotipo = this.buildSottoTipo(dettaglioMacchinaVm);
    dettaglioMacchinaDto.marca = dettaglioMacchinaVm.marcaMacchinario;
    dettaglioMacchinaDto.targa = dettaglioMacchinaVm.targa;
    dettaglioMacchinaDto.modello = dettaglioMacchinaVm.modello;
    //dettaglioMacchinaDto.targa = ?
    dettaglioMacchinaDto.numeroMatricola = dettaglioMacchinaVm.numeroMatricola;
    dettaglioMacchinaDto.numeroTelaio = dettaglioMacchinaVm.numeroTelaio;
    dettaglioMacchinaDto.annoCostruzione = dettaglioMacchinaVm.annoCostruzione;
    // Sezione Motore
    if (dettaglioMacchinaVm.alimentazione != null) {
      dettaglioMacchinaDto.motore = {} as MotoreDto;
      dettaglioMacchinaDto.motore.marca = dettaglioMacchinaVm.marcaMotore;
      dettaglioMacchinaDto.motore.tipo = dettaglioMacchinaVm.tipo;
      dettaglioMacchinaDto.motore.alimentazione = AlimentazioneEnum[dettaglioMacchinaVm.alimentazione.toString()];
      dettaglioMacchinaDto.motore.matricola = dettaglioMacchinaVm.numeroMotore;
      dettaglioMacchinaDto.motore.potenza = dettaglioMacchinaVm.potenza;
    } else {
      dettaglioMacchinaDto.motore = null; // caso di macchinari senza motore
    }

    if (dettaglioMacchinaVm.tipologiaPossesso != null) {
      // Sezione Possesso
      dettaglioMacchinaDto.tipoPossesso = TipologiaPossessoEnum[dettaglioMacchinaVm.tipologiaPossesso.toString()];
      dettaglioMacchinaDto.codiceFiscale = dettaglioMacchinaVm.codiceFiscale;
      dettaglioMacchinaDto.ragioneSociale = dettaglioMacchinaVm.ragioneSociale;
    }
	
    // Flag migrazione
    dettaglioMacchinaDto.flagMigrato = dettaglioMacchinaVm.flagMigrato;

    return dettaglioMacchinaDto;
  }

  toParticelleDto(particelleVm: Array<ParticellaVm>): Array<DatiCatastaliDto> {
    const datiCatastaliDto: Array<DatiCatastaliDto> = [];
    particelleVm.forEach((vm: ParticellaVm) => {
      const dto = {} as DatiCatastaliDto;
      Object.assign(dto, vm);
      dto.tipologia = vm.tipologia as string;
      datiCatastaliDto.push(dto);
    });
    return datiCatastaliDto;
  }

  toParticelleVm(datiCatastaliDto: Array<DatiCatastaliDto>): Array<ParticellaVm> {
    const particelleVm: Array<ParticellaVm> = [];
    datiCatastaliDto.forEach((datoCatastale: DatiCatastaliDto) => {
      let vm = {} as ParticellaVm;
      Object.assign(vm, datoCatastale);
      vm.tipologia = this.stringToSelectItemConverter(datoCatastale.tipologia).value;
      particelleVm.push(vm);
    });
    return particelleVm;
  }

  toDettaglioFabbricatoDto(vm: DettaglioFabbricatoVM, type: keyof typeof FabbricatoTypeEnum, particelle: Array<DatiCatastaliDto>): DettaglioFabbricatoDto {
    let dto: DettaglioFabbricatoDto | any = {}; // | DettaglioFabbricatiStrumentaliDto | DatiSerreProtezioniDto | DettaglioZootecniaStoccaggioDto
    if (type === FabbricatoTypeEnum.STRUMENTALE) {
      dto = {} as DettaglioFabbricatiStrumentaliDto;
      dto.datiCatastali = particelle;
      dto.id = vm.id ? vm.id.toString() : null;
      dto.type = type;
      dto.sottotipo = { id: vm.sottoTipologiaFabbricato.toString(), descrizione: null, tipologie: [] };
      const fabbricati = vm.fabbricati;
      dto.denominazione = fabbricati.denominazione;
      dto.comune = fabbricati.comune;
      dto.descrizione = fabbricati.descrizione;
      dto.indirizzo = fabbricati.indirizzo;
      dto.volume = fabbricati.volume;
      dto.superficie = fabbricati.superficie;
      dto.superficieCoperta = fabbricati.superficieCoperta;
      dto.superficieScoperta = fabbricati.superficieScoperta;
      dto.tipoConduzione = fabbricati.tipoConduzione;
    } else if (type === FabbricatoTypeEnum.SERRA) {
      dto = {} as DatiSerreProtezioniDto;
      dto.datiCatastali = particelle;
      dto.id = vm.id ? vm.id.toString() : null;
      dto.type = type;
      dto.sottotipo = { id: vm.sottoTipologiaFabbricato.toString(), descrizione: null, tipologie: [] };
      const serre = vm.serre;
      dto.denominazione = serre.denominazione;
      dto.comune = serre.comune;
      dto.descrizione = serre.descrizione;
      dto.indirizzo = serre.indirizzo;
      dto.volume = serre.volume;
      dto.superficie = serre.superficie;
      dto.annoAcquisto = serre.annoAcquisto;
      dto.annoCostruzione = serre.annoCostruzione;
      dto.impiantoRiscaldamento = serre.impiantoRiscaldamento;
      dto.tipologiaMateriale = serre.tipologiaMateriale;
      dto.titoloConformitaUrbanistica = serre.titoloConformitaUrbanistica;
      dto.tipoConduzione = serre.tipoConduzione;
    } else if (type === FabbricatoTypeEnum.STOCCAGGIO) {
      dto = {} as DettaglioZootecniaStoccaggioDto;
      dto.datiCatastali = particelle;
      dto.id = vm.id ? vm.id.toString() : null;
      dto.type = type;
      dto.sottotipo = { id: vm.sottoTipo.toString(), descrizione: null, tipologie: [] };
      const stoccaggio = vm.strutture.stoccaggioLetami ? vm.strutture.stoccaggioLetami : vm.strutture.stoccaggioLiquami;
      dto.denominazione = stoccaggio.denominazione;
      dto.indirizzo = stoccaggio.indirizzo;
      dto.comune = stoccaggio.comune;
      dto.volume = stoccaggio.volume;
      dto.superficie = stoccaggio.superficie;
      dto.descrizione = stoccaggio.descrizione;
      dto.tipoConduzione = stoccaggio.tipoConduzione;
      dto.altezza = stoccaggio.altezza;
      dto.copertura = stoccaggio.copertura;
    } else if (type === FabbricatoTypeEnum.FABBRICATO) {
      dto = {} as DettaglioFabbricatoDto;
      dto.datiCatastali = particelle;
      dto.id = vm.id ? vm.id.toString() : null;
      dto.type = type;
      dto.sottotipo = { id: vm.sottoTipo.toString(), descrizione: null, tipologie: [] };
      const fabbricato = vm.strutture.stalle ? vm.strutture.stalle : vm.strutture.areeScoperte;
      dto.denominazione = fabbricato.denominazione;
      dto.indirizzo = fabbricato.indirizzo;
      dto.comune = fabbricato.comune;
      dto.volume = fabbricato.volume;
      dto.superficie = fabbricato.superficie;
      dto.descrizione = fabbricato.descrizione;
      dto.tipoConduzione = fabbricato.tipoConduzione;
    }
    return dto;
  }

  toDettaglioFabbricatoVM(dto: DettaglioFabbricatoDto | DettaglioFabbricatiStrumentaliDto | DatiSerreProtezioniDto | DettaglioZootecniaStoccaggioDto, type: keyof typeof FabbricatoTypeEnum): DettaglioFabbricatoVM {
    let dettaglioFabbricatoVM = {} as DettaglioFabbricatoVM;
    dettaglioFabbricatoVM.type = dto.type;
    // (dto.datiCatastali.tipologia as unknown as SelectItem) = this.stringToSelectItemConverter(dto.datiCatastali.tipologia.toUpperCase());
    // dettaglioFabbricatoVM.datiCatastali = dto.datiCatastali as unknown as DatiCatastaliVM;
    dettaglioFabbricatoVM.id = this.formatConverterService.toNumber(dto.id);
    dettaglioFabbricatoVM.tipologiaFabbricato = this.tipologiaDtoToSelectItemConverter({ id: dto.sottotipo.id, descrizione: dto.sottotipo.descrizione }); // per fabbricato e stoccaggio corrisponde alla sottotipologia (la tipologia va ricavata non torna dal BE) 
    dettaglioFabbricatoVM.sottoTipologiaFabbricato = dto.sottotipo.tipologie && dto.sottotipo.tipologie.length && this.tipologiaDtoToSelectItemConverter({ id: dto.sottotipo.tipologie[0].id, descrizione: dto.sottotipo.tipologie[0].descrizione }); // per fabbricato e stoccaggio corrisponde al sottotipo 
    if (type === FabbricatoTypeEnum.STRUMENTALE) {
      let vm = {} as DettaglioFabbricatoStrumentaleVM;
      vm.denominazione = dto.denominazione;
      vm.indirizzo = dto.indirizzo;
      vm.comune = dto.comune;
      vm.volume = dto.volume;
      vm.superficie = dto.superficie;
      vm.superficieCoperta = (dto as DettaglioFabbricatiStrumentaliDto).superficieCoperta;
      vm.superficieScoperta = (dto as DettaglioFabbricatiStrumentaliDto).superficieScoperta;
      vm.tipoConduzione = { label: '', value: dto.tipoConduzione };
      vm.descrizione = dto.descrizione;
      dettaglioFabbricatoVM.fabbricati = vm;
    } else if (type === FabbricatoTypeEnum.SERRA) {
      let vm = {} as DettaglioSerreVM;
      vm.denominazione = dto.denominazione;
      vm.indirizzo = dto.indirizzo;
      vm.comune = dto.comune;
      vm.volume = dto.volume;
      vm.superficie = dto.superficie;
      vm.impiantoRiscaldamento = (dto as DatiSerreProtezioniDto).impiantoRiscaldamento;
      vm.annoCostruzione = (dto as DatiSerreProtezioniDto).annoCostruzione;
      vm.tipologiaMateriale = (dto as DatiSerreProtezioniDto).tipologiaMateriale;
      vm.annoAcquisto = (dto as DatiSerreProtezioniDto).annoAcquisto;
      vm.titoloConformitaUrbanistica = (dto as DatiSerreProtezioniDto).titoloConformitaUrbanistica;
      vm.tipoConduzione = { label: '', value: dto.tipoConduzione };
      vm.descrizione = dto.descrizione;
      dettaglioFabbricatoVM.serre = vm;
    } else if (type === FabbricatoTypeEnum.STOCCAGGIO) {
      let sruttureZootecniaVm = {} as TipiStruttureVM;
      if (dettaglioFabbricatoVM.tipologiaFabbricato.label.toUpperCase() === 'STOCCAGGIO LETAMI') {
        dettaglioFabbricatoVM.sottoTipo = dettaglioFabbricatoVM.sottoTipologiaFabbricato;
        dettaglioFabbricatoVM.sottoTipologiaFabbricato = dettaglioFabbricatoVM.tipologiaFabbricato;
        let zootecniaStoccaggioVm = {} as DettaglioStruttureZootecniaStoccaggioVM;
        zootecniaStoccaggioVm.denominazione = dto.denominazione;
        zootecniaStoccaggioVm.indirizzo = dto.indirizzo;
        zootecniaStoccaggioVm.comune = dto.comune;
        zootecniaStoccaggioVm.volume = dto.volume;
        zootecniaStoccaggioVm.superficie = dto.superficie;
        zootecniaStoccaggioVm.altezza = (dto as DettaglioZootecniaStoccaggioDto).altezza;
        zootecniaStoccaggioVm.copertura = { label: (dto as DettaglioZootecniaStoccaggioDto).copertura, value: (dto as DettaglioZootecniaStoccaggioDto).copertura };
        zootecniaStoccaggioVm.tipoConduzione = { label: '', value: dto.tipoConduzione };
        zootecniaStoccaggioVm.descrizione = dto.descrizione;
        sruttureZootecniaVm.stoccaggioLetami = zootecniaStoccaggioVm;
      } else if (dettaglioFabbricatoVM.tipologiaFabbricato.label.toUpperCase() === 'STOCCAGGIO LIQUAMI') {
        dettaglioFabbricatoVM.sottoTipo = dettaglioFabbricatoVM.sottoTipologiaFabbricato;
        dettaglioFabbricatoVM.sottoTipologiaFabbricato = dettaglioFabbricatoVM.tipologiaFabbricato;
        let zootecniaStoccaggioVm = {} as DettaglioStruttureZootecniaStoccaggioVM;
        zootecniaStoccaggioVm.denominazione = dto.denominazione;
        zootecniaStoccaggioVm.indirizzo = dto.indirizzo;
        zootecniaStoccaggioVm.comune = dto.comune;
        zootecniaStoccaggioVm.volume = dto.volume;
        zootecniaStoccaggioVm.superficie = dto.superficie;
        zootecniaStoccaggioVm.altezza = (dto as DettaglioZootecniaStoccaggioDto).altezza;
        zootecniaStoccaggioVm.copertura = { label: (dto as DettaglioZootecniaStoccaggioDto).copertura, value: (dto as DettaglioZootecniaStoccaggioDto).copertura };
        zootecniaStoccaggioVm.tipoConduzione = { label: '', value: dto.tipoConduzione };
        zootecniaStoccaggioVm.descrizione = dto.descrizione;
        sruttureZootecniaVm.stoccaggioLiquami = zootecniaStoccaggioVm;
      }
      dettaglioFabbricatoVM.strutture = sruttureZootecniaVm;
    } else if (type === FabbricatoTypeEnum.FABBRICATO) {
      let sruttureZootecniaVm = {} as TipiStruttureVM;
      if (dettaglioFabbricatoVM.tipologiaFabbricato.label.toUpperCase() === 'AREE SCOPERTE') {
        dettaglioFabbricatoVM.sottoTipo = dettaglioFabbricatoVM.sottoTipologiaFabbricato;
        dettaglioFabbricatoVM.sottoTipologiaFabbricato = dettaglioFabbricatoVM.tipologiaFabbricato;
        let zootecniaAreeScoperteStalleVm = {} as DettaglioStruttureZootecniaAreeScoperteStalleVM;
        zootecniaAreeScoperteStalleVm.denominazione = dto.denominazione;
        zootecniaAreeScoperteStalleVm.indirizzo = dto.indirizzo;
        zootecniaAreeScoperteStalleVm.comune = dto.comune;
        zootecniaAreeScoperteStalleVm.volume = dto.volume;
        zootecniaAreeScoperteStalleVm.superficie = dto.superficie;
        zootecniaAreeScoperteStalleVm.tipoConduzione = { label: '', value: dto.tipoConduzione };
        zootecniaAreeScoperteStalleVm.descrizione = dto.descrizione;
        sruttureZootecniaVm.areeScoperte = zootecniaAreeScoperteStalleVm;
      } else if (dettaglioFabbricatoVM.tipologiaFabbricato.label.toUpperCase() === 'STALLE') {
        dettaglioFabbricatoVM.sottoTipo = dettaglioFabbricatoVM.sottoTipologiaFabbricato;
        dettaglioFabbricatoVM.sottoTipologiaFabbricato = dettaglioFabbricatoVM.tipologiaFabbricato;
        let zootecniaAreeScoperteStalleVm = {} as DettaglioStruttureZootecniaAreeScoperteStalleVM;
        zootecniaAreeScoperteStalleVm.denominazione = dto.denominazione;
        zootecniaAreeScoperteStalleVm.indirizzo = dto.indirizzo;
        zootecniaAreeScoperteStalleVm.comune = dto.comune;
        zootecniaAreeScoperteStalleVm.volume = dto.volume;
        zootecniaAreeScoperteStalleVm.superficie = dto.superficie;
        zootecniaAreeScoperteStalleVm.tipoConduzione = { label: '', value: dto.tipoConduzione };
        zootecniaAreeScoperteStalleVm.descrizione = dto.descrizione;
        sruttureZootecniaVm.stalle = zootecniaAreeScoperteStalleVm;
      }
      dettaglioFabbricatoVM.strutture = sruttureZootecniaVm;
    }
    return dettaglioFabbricatoVM;
  }

  tipologiaDtoToSelectItemConverter(tipologiaDto: TipologiaDto): SelectItem {
    const selectItem = {} as SelectItem;
    selectItem.label = tipologiaDto.descrizione ? tipologiaDto.descrizione.replace(/[_]/g, " ") : tipologiaDto.descrizione;
    selectItem.value = tipologiaDto.id;
    return selectItem;
  }

  stringToSelectItemConverter(value: string): SelectItem {
    const selectItem = {} as SelectItem;
    selectItem.label = value;
    selectItem.value = value;
    return selectItem;
  }

  tipologiaDtoToSelectItemArrayConverter(tipologieDto: Array<TipologiaDto>): Array<SelectItem> {
    const arraySelectItem: Array<SelectItem> = [];
    tipologieDto.forEach((tipologia: TipologiaDto) => {
      const selectItem = this.tipologiaDtoToSelectItemConverter(tipologia);
      arraySelectItem.push(selectItem);
    });
    return arraySelectItem;
  }

  classeFunzionaleDtoToSelectItemArrayConverter(classeFunzionale: Array<TipologiaDto>): Array<SelectItem> {
    const arraySelectItem: Array<SelectItem> = [];
    classeFunzionale.forEach((tipologia: TipologiaDto) => {
      const selectItem = this.tipologiaDtoToSelectItemConverter(tipologia);
      arraySelectItem.push(selectItem);
    });
    return arraySelectItem;
  }

  buildSottoTipo(dettaglioMacchinaVm: DettaglioMacchinaVM): SottoTipoDto {
    return {
      id: dettaglioMacchinaVm.sottoTipologiaMacchinario.toString(),
      descrizione: null,
      tipologie: [],
      classiFunzionali: []
    };
  }

  buildParticellaInTrentino(datiCatastali: DatiCatastaliVM, infoCatasto: InfoCatastoVm): ParticellaVm {
    let vm = {} as ParticellaVm;
    vm = datiCatastali;
    vm.indirizzo = infoCatasto.indirizzo;
    vm.superficie = infoCatasto.superficie;
    vm.consistenza = infoCatasto.consistenza;
    vm.categoria = infoCatasto.categoria;
    vm.note = infoCatasto.note;
    vm.inTrentino = true;
    vm.esito = EsitoValidazioneParticellaCatastoEnum.VALIDA;
    return vm;
  }

  buildParticellaFuoriTrentino(datiCatastali: DatiCatastaliVM): ParticellaVm {
    let vm = {} as ParticellaVm;
    vm = datiCatastali;
    vm.inTrentino = false;
    return vm;
  }
}
