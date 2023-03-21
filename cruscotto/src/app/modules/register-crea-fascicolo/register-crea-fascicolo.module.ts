import { CreaFascicoloModule } from './../crea-fascicolo/crea-fascicolo.module';
import { RegisterCreaFascicoloComponent as HomeRegisterCreaFascicoloComponent } from './register-crea-fascicolo.component';
import { RegisterModule } from './../register/register.module';
import { SharedModule } from './../../shared/shared.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';


import { RegisterCreaFascicoloRoutingModule } from './register-crea-fascicolo-routing.module';


@NgModule({
  declarations: [HomeRegisterCreaFascicoloComponent],
  imports: [
    CommonModule,
    SharedModule,
    RegisterCreaFascicoloRoutingModule,
    RegisterModule,
    CreaFascicoloModule
  ]
})
export class RegisterCreaFascicoloModule { }
