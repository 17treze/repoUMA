import { Injectable } from '@angular/core';
import { SelectItem } from 'primeng-lts';
import { DistributoreCarburanteDto } from 'src/app/uma/shared-uma/models/dto/DistributoreCarburanteDto';

@Injectable({
  providedIn: 'root'
})
export class DistributoriCarburanteBuilderService {

  constructor() { }

  distributoreDtoToDistributoreVmBuilder(distributoriDto: Array<DistributoreCarburanteDto>): Array<SelectItem> {
    const distributoriVM: Array<SelectItem> = [];
    if (distributoriDto && distributoriDto.length) {
      distributoriDto.forEach((distributoreDto: DistributoreCarburanteDto) => {
        const distributoreVM: SelectItem = {} as SelectItem;
        distributoreVM.label = distributoreDto.denominazione + ' - ' + distributoreDto.indirizzo + ', ' + distributoreDto.comune + ', ' + distributoreDto.provincia;
        distributoreVM.value = distributoreDto.id;
        distributoriVM.push(distributoreVM);
      });
    }
    return distributoriVM;
  }

}
