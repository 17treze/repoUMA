import { SharedUmaModule } from './../shared-uma/shared-uma.module';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { NgModule, Type } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IntestazioneUmaComponent } from './components/intestazione-uma/intestazione-uma.component';
import { GestioneDomandaUmaService } from '../features/gestione-domanda-uma/gestione-domanda-uma.service';
import { HttpClientUmaCoreService } from './services/http-client-uma-core.service';
import { ProtocollazioneRichiestaCarburanteDialogService } from './services/protocollazione-richiesta-carburante-dialog.service';
import { DtoBuilderService } from './services/dto-builder.service';
import { HttpClientDomandaUmaService } from './services/http-client-domanda-uma.service';
import { HttpClientLavorazioniUmaService } from './services/http-client-lavorazioni-uma.service';
import { HttpClientMacchineUmaService } from './services/http-client-macchine-uma.service';
import { FileService } from './services/file.service';
import { ConverterUnitaDiMisuraService } from './services/converter-unita-di-misura.service';
import { HttpClientDichiarazioneConsumiUmaService } from './services/http-client-dichiarazione-consumi-uma.service';
import { IndiceUmaService } from './services/indice-uma.service';
import { A4gToastComponent } from './components/a4g-toast/a4g-toast.component';
import { A4gToastService } from './components/a4g-toast/a4g-toast.service';
import { HttpClientClienteUmaService } from './services/http-client-cliente-uma.service';
import { HttpClientTrasferimentiCarburanteService } from './services/http-client-trasferimenti-carburante.service';
import { LavorazioniBuiderService } from './services/lavorazioni-builder.service';
import { LavorazioneFabbricatiBuilderService } from './services/lavorazione-fabbricati-builder.service';
import { PrelieviBuilderService } from './services/builders/prelievi-builder.service';
import { RicevutiBuilderService } from './services/builders/ricevuti-builder.service';
import { ConsumiBuilderService } from './services/builders/consumi-builder.service';
import { DistributoriBuilderService } from './services/builders/distributori-builder.service';
import { HttpClientDistributoriService } from './services/http-client-distributori.service';
import { RichiestaCarburanteBuilderService } from './services/builders/richiesta-carburante-builder.service';
import { TrasferitiBuilderService } from './services/builders/trasferiti-builder.service';

const MODULES: Array<Type<any>> = [
  CommonModule,
  SharedUmaModule,
  A4gCommonModule
];

const COMPONENTS: Array<Type<any>> = [
  IntestazioneUmaComponent
];

const ENTRY_COMPONENTS: Array<Type<any>> = [
  A4gToastComponent
];

const PIPES: Array<Type<any>> = [];

const DIRECTIVES: Array<Type<any>> = [];

const PROVIDERS: Array<Type<any>> = [];

const SERVICES: Array<Type<any>> = [
  GestioneDomandaUmaService,
  IndiceUmaService,
  HttpClientUmaCoreService,
  HttpClientDomandaUmaService,
  HttpClientMacchineUmaService,
  HttpClientLavorazioniUmaService,
  DtoBuilderService,
  ProtocollazioneRichiestaCarburanteDialogService,
  FileService,
  ConverterUnitaDiMisuraService,
  HttpClientDichiarazioneConsumiUmaService,
  A4gToastService,
  HttpClientClienteUmaService,
  HttpClientTrasferimentiCarburanteService,
  LavorazioniBuiderService,
  LavorazioneFabbricatiBuilderService,
  PrelieviBuilderService,
  RicevutiBuilderService,
  TrasferitiBuilderService,
  ConsumiBuilderService,
  DistributoriBuilderService,
  HttpClientDistributoriService,
  RichiestaCarburanteBuilderService
];

@NgModule({

  imports: [
    ...MODULES
  ],
  declarations: [
    ...ENTRY_COMPONENTS,
    ...COMPONENTS,
    ...PIPES,
    ...DIRECTIVES,
  ],
  entryComponents: [
    ...ENTRY_COMPONENTS
  ],
  providers: [
    ...SERVICES,
    ...PROVIDERS
  ],
  exports: [
    ...MODULES,
    ...COMPONENTS,
    ...ENTRY_COMPONENTS,
    ...PIPES,
    ...DIRECTIVES
  ],
})
export class CoreUmaModule { }
