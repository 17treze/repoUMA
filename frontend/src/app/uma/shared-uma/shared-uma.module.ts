import { NgModule, Type } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientProxyService } from './services/http-client-proxy.service';
import { NullCheckService } from './services/null-check.service';
import { FormatConverterService } from './services/format-converter.service';

const MODULES: Array<Type<any>> = [
  CommonModule
];

const COMPONENTS: Array<Type<any>> = [];

const ENTRY_COMPONENTS: Array<Type<any>> = [];

const PIPES: Array<Type<any>> = [];

const DIRECTIVES: Array<Type<any>> = [];

const PROVIDERS: Array<Type<any>> = [];

const SERVICES: Array<Type<any>> = [
  HttpClientProxyService,
  NullCheckService,
  FormatConverterService
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
export class SharedUmaModule { }
