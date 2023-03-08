import { Injectable } from '@angular/core';
import { RicevutoViewModel } from '../../models/viewModels/RicevutoViewModel';
import * as moment from 'moment';
import { TrasferimentoDto } from '../../models/dto/CarburanteRicevutoDto';

@Injectable({
  providedIn: 'root'
})
export class RicevutiBuilderService {

  constructor() { }

  ricevutiDtoToRicevutiViewModelBuilder(ricevuti: Array<TrasferimentoDto>): Array<RicevutoViewModel> {
    const ricevutiViewModel: Array<RicevutoViewModel> = [];
    if (ricevuti && ricevuti.length) {
      ricevuti.forEach((ricevuto: TrasferimentoDto) => {
        const ricevutoViewModel: RicevutoViewModel = {} as RicevutoViewModel;
        ricevutoViewModel.benzina = ricevuto.carburante.benzina;
        ricevutoViewModel.gasolio = ricevuto.carburante.gasolio;
        ricevutoViewModel.gasolioSerre = ricevuto.carburante.gasolioSerre;
        ricevutoViewModel.data = moment(ricevuto.data).format('DD/MM/YYYY');
        ricevutoViewModel.cuaa = ricevuto.mittente.cuaa;
        ricevutoViewModel.denominazione = ricevuto.mittente.denominazione;
        ricevutiViewModel.push(ricevutoViewModel);
      });
    }
    return ricevutiViewModel;
  }
}
