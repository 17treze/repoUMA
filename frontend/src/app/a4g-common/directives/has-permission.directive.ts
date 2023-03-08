import { StatoIstruttoriaEnum } from '../../istruttoria/istruttoria-pac1420/domandaUnica/cruscotto-sostegno/StatoIstruttoriaEnum';
import { Directive, Input, ViewContainerRef, TemplateRef, ElementRef } from '@angular/core';
import { TypePermission } from 'src/app/a4g-common/directives/typePermission';
import { TipoIstruttoriaEnum } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/TipoIstruttoriaEnum';
import {TabCommonComponent} from "../../istruttoria/istruttoria-pac1420/domandaUnica/sostegno-shared/TabCommonComponent";

/**
 * @whatItDoes Nasconde in modo condizionale l'HTML sui cui è inserito
 * 
 *
 * @howToUse
 * ```
 *     <some-element *a4gHasPermission="'ANTICIPI'">...</some-element>
 *
 *     <some-element *jhiHasAnyAuthority="['ROLE_ADMIN', 'ROLE_USER']">...</some-element>
 * ```
 */

@Directive({
  selector: '[a4gHasPermission]'
})
export class HasPermissionDirective {

  private type: TypePermission;
  @Input() annoCampagna: string;
  @Input() statoSostegno: string;
  @Input() tipoIstruttoria: string;
  constructor(private el: ElementRef) { }

  @Input()
  set a4gHasPermission(value: string) {
    this.type = TypePermission[value];
    this.updateView();
  }

  private updateView(): void {
    switch (this.type) {
      //cioè visibile / hidden=true
      case TypePermission.MODIFICABILE: this.el.nativeElement.hidden = !this.checkModificabile();
        break;
      //cioè non visibile / hidden=true
      case TypePermission.NON_MODIFICABILE: this.el.nativeElement.hidden = this.checkModificabile();
        break;
    }
  }

  private checkModificabile(): boolean {
    if (this.statoSostegno === StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO ||
      this.statoSostegno === StatoIstruttoriaEnum.NON_AMMISSIBILE ||
      this.statoSostegno === StatoIstruttoriaEnum.NON_LIQUIDABILE) {
      return false;
    }
    return TabCommonComponent.anticipiAttivi(
        TipoIstruttoriaEnum[this.tipoIstruttoria], parseInt(this.annoCampagna));
  }
}
