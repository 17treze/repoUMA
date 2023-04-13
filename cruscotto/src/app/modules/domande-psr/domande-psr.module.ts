import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DomandePsrRoutingModule } from './domande-psr-routing.module';
import { ContainerDomandePsrComponent } from './container-domande-psr/container-domande-psr.component';
import { ListaDomandePsrComponent } from './lista-domande-psr/lista-domande-psr.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { DomandaPsrSuperficieComponent } from './domanda-psr-superficie/domanda-psr-superficie.component';
import { DettaglioDomandaPsrSuperficieComponent } from './domanda-psr-superficie/dettaglio-domanda-psr-superficie/dettaglio-domanda-psr-superficie.component';
import { IstruttoriaDomandaPsrSuperficieComponent } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/istruttoria-domanda-psr-superficie.component';
import { CardDettaglioSostegnoComponent } from './domanda-psr-superficie/dettaglio-domanda-psr-superficie/card-dettaglio-sostegno/card-dettaglio-sostegno.component';
import { DescrizioneByCodicePipe } from './domanda-psr-superficie/dettaglio-domanda-psr-superficie/card-dettaglio-sostegno/descrizione-by-codice.pipe';
import { ConvertCodiceOperazioneToReadableStringPipe } from './domanda-psr-superficie/dettaglio-domanda-psr-superficie/card-dettaglio-sostegno/convert-codice-operazione-to-readable-string.pipe';
import { DividerComponent } from '../common/divider/divider.component';
import { CardDettaglioSostegnoM10O14Component } from './domanda-psr-superficie/dettaglio-domanda-psr-superficie/card-dettaglio-sostegno-m10-o14/card-dettaglio-sostegno-m10-o14.component';
import { CardDettaglioSostegnoM10O11Component } from './domanda-psr-superficie/dettaglio-domanda-psr-superficie/card-dettaglio-sostegno-m10-o11/card-dettaglio-sostegno-m10-o11.component';
import { CardDettaglioSostegnoM11O21Component } from './domanda-psr-superficie/dettaglio-domanda-psr-superficie/cardM11/card-dettaglio-sostegno-m11-o21/card-dettaglio-sostegno-m11-o21.component';
import { CodDestinazioneToDescriptionPipe } from './domanda-psr-superficie/dettaglio-domanda-psr-superficie/cod-destinazione-to-description.pipe';
import { CardDettaglioSostegnoM11O11Component } from './domanda-psr-superficie/dettaglio-domanda-psr-superficie/cardM11/card-dettaglio-sostegno-m11-o11/card-dettaglio-sostegno-m11-o11.component';
import { SquareMetersToHectarsPipe } from './domanda-psr-superficie/dettaglio-domanda-psr-superficie/square-meters-to-hectars.pipe';
import { CardDettaglioSostegnoM13O11Component } from './domanda-psr-superficie/dettaglio-domanda-psr-superficie/card-dettaglio-sostegno-m13-o11/card-dettaglio-sostegno-m13-o11.component';
import { CardDettaglioSostegnoM10O12Component } from './domanda-psr-superficie/dettaglio-domanda-psr-superficie/card-dettaglio-sostegno-m10-o12/card-dettaglio-sostegno-m10-o12.component';
import { CardDettaglioSostegnoM10O13Component } from './domanda-psr-superficie/dettaglio-domanda-psr-superficie/card-dettaglio-sostegno-m10-o13/card-dettaglio-sostegno-m10-o13.component';
import { CardDettaglioPagamentoComponent } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/card-dettaglio-pagamento/card-dettaglio-pagamento.component';
import { CardDettaglioPagamentoAmountComponent } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/card-dettaglio-pagamento/card-dettaglio-pagamento-amount/card-dettaglio-pagamento-amount.component';
import { IstruttoriaDomandaPsrSuperficieCostiComponent } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/istruttoria-domanda-psr-superficie-costi/istruttoria-domanda-psr-superficie-costi.component';
import { IstruttoriaDomandaPsrSuperficieDebitiComponent } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/istruttoria-domanda-psr-superficie-debiti/istruttoria-domanda-psr-superficie-debiti.component';
import { DettaglioPagamentoPsrSuperficieComponent } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie.component';
import { HeaderDettaglioPagamentoPsrSuperficieComponent } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/header-dettaglio-pagamento-psr-superficie/header-dettaglio-pagamento-psr-superficie.component';
import { DettaglioTotaliComponent } from './domanda-psr-superficie/dettaglio-domanda-psr-superficie/dettaglio-totali/dettaglio-totali.component';
import { DettagloEsitoFinaleDomandaPsrM10O13Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m10-o13/dettaglo-esito-finale-domanda-psr-m10-013/dettaglo-esito-finale-domanda-psr-component-m10-o13.component';
import { DettaglioPagamentoPsrSuperficieM10O14Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m10-o14/dettaglio-pagamento-psr-superficie-m10-o14.component';
import { DettaglioPagamentoPsrSuperficieM10O13Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m10-o13/dettaglio-pagamento-psr-superficie-m10-o13.component';
import { CalculatedAmountComponent } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/card-dettaglio-pagamento/calculated-amount/calculated-amount.component';
import { RazzaDiBestiameDomandaPsrComponent } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m10-o13/razza-di-bestiame-domanda-psr/razza-di-bestiame-domanda-psr.component';
import { DettaglioPagamentoPsrSuperficieM10O11Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m10-o11/dettaglio-pagamento-psr-superficie-m10-o11.component';
import { InterventoM10O11Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m10-o11/intervento-m10-o11/intervento-m10-o11.component';
import { DettagloEsitoFinaleDomandaPsrM10O11Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m10-o11/dettaglo-esito-finale-domanda-psr-m10-o11/dettaglo-esito-finale-domanda-psr-m10-o11.component';
import { DettaglioPagamentoPsrSuperficieM10O12Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m10-o12/dettaglio-pagamento-psr-superficie-m10-o12.component';
import { DettaglioMalgaDomandaPsrComponent } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m10-o12/dettaglio-malga-domanda-psr/dettaglio-malga-domanda-psr.component';
import { AppDettagloEsitoFinaleDomandaPsrM10O12Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m10-o12/app-dettaglo-esito-finale-domanda-psr-m10-o12/app-dettaglo-esito-finale-domanda-psr-m10-o12.component';
import { DettaglioPagamentoPsrSuperficieM11Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m11/dettaglio-pagamento-psr-superficie-m11.component';
import { SostegnoMantenimentoPsrM11Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m11/sostegno-mantenimento-psr-m11/sostegno-mantenimento-psr-m11.component';
import { EsitoFinalePsrM11Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m11/esito-finale-psr-m11/esito-finale-psr-m11.component';
import { DettaglioPagamentoPsrSuperficieM13O11Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m13-o11/dettaglio-pagamento-psr-superficie-m13-o11.component';
import { DatiAziendaliM13O11Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m13-o11/dati-aziendali-m13-o11/dati-aziendali-m13-o11.component';
import { SistemaAgricoloM13O11Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m13-o11/sistema-agricolo-m13-o11/sistema-agricolo-m13-o11.component';
import { GestionePluriennaleSanzioneM13O11Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m13-o11/gestione-pluriennale-sanzione-m13-o11/gestione-pluriennale-sanzione-m13-o11.component';
import { CalcoloPremioM13O11Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m13-o11/calcolo-premio-m13-o11/calcolo-premio-m13-o11.component';
import { EsitoFinaleM13O11Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m13-o11/esito-finale-m13-o11/esito-finale-m13-o11.component';
import { BooleanIntegerToItalianAssertionPipe } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m13-o11/boolean-integer-to-italian-assertion.pipe';
import { RiduzioniControlloInLocoM13O11Component } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie-m13-o11/riduzioni-controllo-in-loco-m13-o11/riduzioni-controllo-in-loco-m13-o11.component';
import { SwipeModule } from 'ng-swipe';

@NgModule({
  declarations: [
    ContainerDomandePsrComponent,
    ListaDomandePsrComponent,
    DomandaPsrSuperficieComponent,
    DettaglioDomandaPsrSuperficieComponent,
    IstruttoriaDomandaPsrSuperficieComponent,
    CardDettaglioSostegnoComponent,
    DescrizioneByCodicePipe,
    ConvertCodiceOperazioneToReadableStringPipe,
    DividerComponent,
    CardDettaglioSostegnoM10O14Component,
    CardDettaglioSostegnoM10O11Component,
    CardDettaglioSostegnoM11O21Component,
    CardDettaglioSostegnoM11O11Component,
    CodDestinazioneToDescriptionPipe,
    SquareMetersToHectarsPipe,
    CardDettaglioSostegnoM13O11Component,
    CardDettaglioSostegnoM10O12Component,
    CardDettaglioSostegnoM10O13Component,
    CardDettaglioPagamentoComponent,
    CardDettaglioPagamentoAmountComponent,
    IstruttoriaDomandaPsrSuperficieCostiComponent,
    IstruttoriaDomandaPsrSuperficieDebitiComponent,
    DettaglioTotaliComponent,
    DettaglioPagamentoPsrSuperficieComponent,
    HeaderDettaglioPagamentoPsrSuperficieComponent,
    RazzaDiBestiameDomandaPsrComponent,
    DettagloEsitoFinaleDomandaPsrM10O13Component,
    DettaglioPagamentoPsrSuperficieM10O14Component,
    DettaglioPagamentoPsrSuperficieM10O13Component,
    CalculatedAmountComponent,
    DettaglioPagamentoPsrSuperficieM10O11Component,
    InterventoM10O11Component,
    DettagloEsitoFinaleDomandaPsrM10O11Component,
    DettaglioPagamentoPsrSuperficieM10O12Component,
    DettaglioMalgaDomandaPsrComponent,
    AppDettagloEsitoFinaleDomandaPsrM10O12Component,
    DettaglioPagamentoPsrSuperficieM11Component,
    SostegnoMantenimentoPsrM11Component,
    EsitoFinalePsrM11Component,
    DettaglioPagamentoPsrSuperficieM13O11Component,
    DatiAziendaliM13O11Component,
    SistemaAgricoloM13O11Component,
    GestionePluriennaleSanzioneM13O11Component,
    CalcoloPremioM13O11Component,
    EsitoFinaleM13O11Component,
    BooleanIntegerToItalianAssertionPipe,
    RiduzioniControlloInLocoM13O11Component
    ],
  imports: [
    SharedModule,
    CommonModule,
    DomandePsrRoutingModule,
    SwipeModule
  ]
})
export class DomandePsrModule { }
