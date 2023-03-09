import { FormatConverterService } from 'src/app/uma/shared-uma/services/format-converter.service';
import { CarburantePrelevabileVM } from 'src/app/uma/core-uma/models/viewModels/CarburantePrelevabileViewModel';
import { Injectable } from '@angular/core';
import { PrelievoDto } from '../../models/dto/PrelievoDto';
import { PrelievoViewModel } from '../../models/viewModels/PrelievoViewModel';
import * as moment from 'moment';
import { PrelievoCarburanteVM } from '../../models/viewModels/PrelievoCarburanteViewModel';
import { CarburanteDto } from '../../models/dto/CarburanteDto';
// import { TipoCarburante } from '../../../../../../../cruscotto/src/app/modules/domande-uma/models/tipo-carburante';
import { SelectItem } from 'primeng-lts';

@Injectable({
  providedIn: 'root'
})
export class PrelieviBuilderService {

  constructor(
    private formatConverterService: FormatConverterService
  ) { }

  prelieviDtoToPrelieviViewModelBuilder(prelievi: Array<PrelievoDto>): Array<PrelievoViewModel> {
    const prelieviViewModel: Array<PrelievoViewModel> = [];
    if (prelievi && prelievi.length) {
      prelievi.forEach((prelievo: PrelievoDto) => {
        let prelievoViewModel: PrelievoViewModel = {} as PrelievoViewModel;
        prelievoViewModel.benzina = prelievo.carburante && prelievo.carburante.benzina;
        prelievoViewModel.gasolio = prelievo.carburante && prelievo.carburante.gasolio;
        prelievoViewModel.gasolioSerre = prelievo.carburante && prelievo.carburante.gasolioSerre;
        prelievoViewModel.data = moment(prelievo.data).format('DD/MM/YYYY');
        prelievoViewModel.distributoreNome = prelievo.distributore && prelievo.distributore.denominazione;
        prelievoViewModel.distributoreNomeEsteso = prelievo.distributore && prelievo.distributore.denominazione + ' - ' + prelievo.distributore.indirizzo + ', ' + prelievo.distributore.comune + ', ' + prelievo.distributore.provincia;
        prelievoViewModel.distributoreIndirizzo = prelievo.distributore && prelievo.distributore.indirizzo + ' ' + prelievo.distributore.comune;
        prelievoViewModel.distributoreIdentificativo = prelievo.distributore && prelievo.distributore.identificativo;
        prelievoViewModel.distributoreId = prelievo.distributore && prelievo.distributore.id;
        prelievoViewModel.estremiDocumentoFiscale = prelievo.estremiDocumentoFiscale;
        prelievoViewModel.isConsegnato = !!prelievo.isConsegnato;
        prelievoViewModel.id = prelievo.id;
        prelieviViewModel.push(prelievoViewModel);
      });
    }
    return prelieviViewModel;
  }

  prelievoCarburanteVmToPrelievoDtoBuilder(prelievoCarburanteVM: PrelievoCarburanteVM): PrelievoDto {
    let prelievoDto = {} as PrelievoDto;
    if (prelievoCarburanteVM != null) {
      /** elimino property carburante */
      const { carburante, ...temp } = prelievoCarburanteVM;
      /** converto carburante e assegno a dto*/
      prelievoDto = { ...temp, carburante: this.carburantePrelevabileArrayVmToCarburanteDto(prelievoCarburanteVM.carburante) };
      prelievoDto.distributore = { identificativo: prelievoCarburanteVM.identificativoDistributore };
      prelievoDto.isConsegnato = !!prelievoDto.isConsegnato;
    }
    return prelievoDto;
  }

  carburantePrelevabileArrayVmToCarburanteDto(carburantePrelevabileVm: Array<CarburantePrelevabileVM>): CarburanteDto {
    const carburanteDto = {} as CarburanteDto;
    carburanteDto.benzina = null; // this.formatConverterService.toNumber(carburantePrelevabileVm.filter((carBen: CarburantePrelevabileVM) => carBen.carburante.toLowerCase() == TipoCarburante.BENZINA.toLowerCase())[0].prelevato);
    carburanteDto.gasolio = null; // this.formatConverterService.toNumber(carburantePrelevabileVm.filter((carBen: CarburantePrelevabileVM) => carBen.carburante.toLowerCase() == TipoCarburante.GASOLIO.toLowerCase())[0].prelevato);
    carburanteDto.gasolioSerre = null; // this.formatConverterService.toNumber(carburantePrelevabileVm.filter((carBen: CarburantePrelevabileVM) => carBen.carburante.toLowerCase().split(' ').join('') == TipoCarburante.GASOLIO_SERRE.toLowerCase())[0].prelevato);
    return carburanteDto;
  }

  prelieviViewModelFilterByUtente(prelievi: Array<PrelievoViewModel>, distributoriVM: Array<SelectItem>): Array<PrelievoViewModel> {
    if (!prelievi || !prelievi.length) {
      return [];
    }
    if (!distributoriVM || !distributoriVM.length) {
      return prelievi;
    }
    prelievi.forEach((prelievo: PrelievoViewModel) => {
      prelievo.readonly = false;
      prelievo.hiddenCircle = false;
      /** se i distributori non appartengono all'utente, nascondo il nome per privacy e non posso effettuare modifiche */
      if (distributoriVM.filter((dis: SelectItem) => dis.value != null).map((distributore: SelectItem) => distributore.value).indexOf(prelievo.distributoreIdentificativo) < 0) {
        prelievo.distributoreNomeEsteso = '';
        prelievo.readonly = true;     /** sola lettupra per icona di modifica */
        prelievo.hiddenCircle = true; /** eliminazione icona di delete */
      }
    });
    return prelievi;
  }

  prelievoViewModelPrelievoDtoNoDistributoriBuilder(prelievi: Array<PrelievoViewModel>): Array<PrelievoDto> {
    const prelieviDto: Array<PrelievoDto> = [];
    if (prelievi && prelievi.length) {
      prelievi.forEach(prelievo => {
        let prelievoDto: PrelievoDto = {} as PrelievoDto;
        prelievoDto.id = prelievo.id;
        prelievoDto.carburante = new CarburanteDto();
        prelievoDto.carburante.gasolio = this.formatConverterService.isEmptyString(prelievo.gasolio) ? 0 : prelievo.gasolio;
        prelievoDto.carburante.benzina = this.formatConverterService.isEmptyString(prelievo.benzina) ? 0 : prelievo.benzina;
        prelievoDto.carburante.gasolioSerre = this.formatConverterService.isEmptyString(prelievo.gasolioSerre) ? 0 : prelievo.gasolioSerre;
        prelievoDto.isConsegnato = prelievo.isConsegnato;
        prelieviDto.push(prelievoDto);
      });
    }
    return prelieviDto;
  }

  prelieviDtoToPrelieviViewModelWithDenominazioneBuilder(prelievi: Array<PrelievoDto>): Array<PrelievoViewModel> {
    const prelieviViewModel: Array<PrelievoViewModel> = [];
    if (prelievi && prelievi.length) {
      prelievi.forEach((prelievo: PrelievoDto) => {
        let prelievoViewModel: PrelievoViewModel = {} as PrelievoViewModel;
        prelievoViewModel.benzina = prelievo.carburante.benzina;
        prelievoViewModel.gasolio = prelievo.carburante.gasolio;
        prelievoViewModel.gasolioSerre = prelievo.carburante.gasolioSerre;
        prelievoViewModel.data = moment(prelievo.data).format('DD/MM/YYYY');
        prelievoViewModel.isConsegnato = !!prelievo.isConsegnato;
        prelievoViewModel.id = prelievo.id;
        prelievoViewModel.cuaa = prelievo.cuaa;
        prelievoViewModel.denominazione = prelievo.denominazione == 'NO_DENOM' ? '' : prelievo.denominazione;
        prelieviViewModel.push(prelievoViewModel);
      });
    }
    return prelieviViewModel;
  }
}
