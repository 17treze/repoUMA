import { Injectable } from '@angular/core';
import * as moment from 'moment';
import { TrasferimentoDto } from '../../models/dto/CarburanteRicevutoDto';
import { TrasferitoViewModel } from '../../models/viewModels/TrasferitoViewModel';

@Injectable({
  providedIn: 'root'
})
export class TrasferitiBuilderService {

  constructor() { }

  trasferitiDtoToRicevutiViewModelBuilder(trasferiti: Array<TrasferimentoDto>): Array<TrasferitoViewModel> {
    const ricevutiViewModel: Array<TrasferitoViewModel> = [];
    if (trasferiti && trasferiti.length) {
      trasferiti.forEach((trasferito: TrasferimentoDto) => {
        const trasferitoViewModel: TrasferitoViewModel = {} as TrasferitoViewModel;
        trasferitoViewModel.id = trasferito.id;
        trasferitoViewModel.benzina = trasferito.carburante.benzina;
        trasferitoViewModel.gasolio = trasferito.carburante.gasolio;
        trasferitoViewModel.gasolioSerre = trasferito.carburante.gasolioSerre;
        trasferitoViewModel.data = moment(trasferito.data).format('DD/MM/YYYY');
        trasferitoViewModel.cuaa = trasferito.destinatario.cuaa;
        trasferitoViewModel.denominazione = trasferito.destinatario.denominazione;
        ricevutiViewModel.push(trasferitoViewModel);
      });
    }
    return ricevutiViewModel;
  }
}
