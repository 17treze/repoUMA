import { UtenteDistributoreDto } from '../../models/dto/DistributoreDto';
import { Injectable } from '@angular/core';
import { SelectItem } from 'primeng-lts';

@Injectable({
  providedIn: 'root'
})
export class DistributoriBuilderService {

  constructor() { }

  distributoreDtoToDistributoreVmBuilder(distributoriDto: Array<UtenteDistributoreDto>): Array<SelectItem> {
    const distributoriVM: Array<SelectItem> = [];
    if (distributoriDto && distributoriDto.length) {
      distributoriDto.forEach((distributoreDto: UtenteDistributoreDto) => {
        const distributoreVM: SelectItem = {} as SelectItem;
        distributoreVM.label = distributoreDto.denominazioneAzienda + ' - ' + distributoreDto.indirizzo + ', ' + distributoreDto.comune + ', ' + distributoreDto.provincia;
        distributoreVM.value = distributoreDto.id;
        distributoriVM.push(distributoreVM);
      });
    }
    return distributoriVM;
  }

}
