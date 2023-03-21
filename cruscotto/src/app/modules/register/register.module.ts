import {NgModule} from '@angular/core';
import {RegisterRoutingModule} from "./register-routing.module";
import {RegisterService} from "./register.service";
import {RegisterComponent as HomeRegisterComponent} from './register.component';
import {SharedModule} from "../../shared/shared.module";
import {CheckboxModule} from 'primeng-lts/checkbox';

@NgModule({
  declarations: [HomeRegisterComponent],
  imports: [
    SharedModule,
    RegisterRoutingModule,
    CheckboxModule
  ],
  providers: [
    RegisterService
  ],
  exports: [
    HomeRegisterComponent
  ]
})
export class RegisterModule { }
