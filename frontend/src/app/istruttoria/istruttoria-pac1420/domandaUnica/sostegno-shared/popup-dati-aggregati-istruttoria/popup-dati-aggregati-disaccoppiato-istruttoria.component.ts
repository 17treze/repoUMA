import { Component, Input, Output, EventEmitter } from '@angular/core';
import { DatiAggregatiIstruttoriaModel, DatiAggregatiIstruttoriaTotaliModel } from './models/dati-aggregati-istruttoria.model';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';

@Component({
  selector: 'app-popup-dati-aggregati-disaccoppiato-istruttoria',
  templateUrl: 'popup-dati-aggregati-disaccoppiato-istruttoria.component.html',
})
export class PopupDatiAggregatiDisaccoppiatoIstruttoriaComponent {

  public tipoSostegno: SostegnoDu = SostegnoDu.DISACCOPPIATO;
  public datiTotali: DatiAggregatiIstruttoriaTotaliModel = undefined;

  @Input() public popupVisibile = false;
  @Input() public annoCampagna: number;
  @Output() public chiudiPopup = new EventEmitter();

  private datiAggregatiAnticipoModel: DatiAggregatiIstruttoriaModel[] = undefined;
  private datiAggregatiSaldoModel: DatiAggregatiIstruttoriaModel[] = undefined;
  private datiAggregatiIntegrazioneModel: DatiAggregatiIstruttoriaModel[] = undefined;

  constructor() {
  }

  public setDatiAggregatiAnticipoModel(datiAggregatiIstruttoriaModel: DatiAggregatiIstruttoriaModel[]) {
    this.datiAggregatiAnticipoModel = datiAggregatiIstruttoriaModel;
    this.tryComputeTotals();
  }

  public setDatiAggregatiSaldoModel(datiAggregatiIstruttoriaModel: DatiAggregatiIstruttoriaModel[]) {
    this.datiAggregatiSaldoModel = datiAggregatiIstruttoriaModel;
    this.tryComputeTotals();
  }

  public setDatiAggregatiIntegrazioneModel(datiAggregatiIstruttoriaModel: DatiAggregatiIstruttoriaModel[]) {
    this.datiAggregatiIntegrazioneModel = datiAggregatiIstruttoriaModel;
    this.tryComputeTotals();
  }

  private tryComputeTotals() {
    if (!this.datiAggregatiAnticipoModel || !this.datiAggregatiSaldoModel || !this.datiAggregatiIntegrazioneModel) {
      return;
    }
    let tmpDatiTotali: DatiAggregatiIstruttoriaTotaliModel = new DatiAggregatiIstruttoriaTotaliModel();
    let pagamentoAutorizzatoAnticipo = this.datiAggregatiAnticipoModel.find(e => e.keyDesc === StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO).valoreNetto;
    let pagamentoAutorizzatoSaldo = this.datiAggregatiSaldoModel.find(e => e.keyDesc === StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO).valoreNetto;
    let pagamentoAutorizzatoIntegrazione = this.datiAggregatiIntegrazioneModel.find(e => e.keyDesc === StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO).valoreNetto;
    tmpDatiTotali.pagamentoAutorizzato = pagamentoAutorizzatoAnticipo + pagamentoAutorizzatoSaldo + pagamentoAutorizzatoIntegrazione;

    let totCalcoloAnticipo = this.datiAggregatiAnticipoModel.find(e => e.keyDesc === 'TOT_ANTICIPO_DISACCOPPIATO').valoreNetto;
    let totCalcoloSaldo = this.datiAggregatiSaldoModel.find(e => e.keyDesc === 'TOT_SALDO_DISACCOPPIATO').valoreNetto;
    let totCalcoloIntegrazione = this.datiAggregatiIntegrazioneModel.find(e => e.keyDesc === 'TOT_INTEGRAZIONE_DISACCOPPIATO').valoreNetto;
    tmpDatiTotali.totaleCalcolato = totCalcoloAnticipo + totCalcoloSaldo + totCalcoloIntegrazione;

    let percent = (tmpDatiTotali.pagamentoAutorizzato / tmpDatiTotali.totaleCalcolato) * 100;
    tmpDatiTotali.percentualePagamento = percent.toFixed(2);
    this.datiTotali = tmpDatiTotali;
  }
  
  public closePopup() {
    this.chiudiPopup.emit();
  }

  
}
