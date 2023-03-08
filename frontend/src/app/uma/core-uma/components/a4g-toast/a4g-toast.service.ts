import { A4GToastModel, A4GToastSeverityEnum } from './a4g-toast.model';
import { ElementRef, Injectable, Renderer2 } from '@angular/core';
import { MessageService } from 'primeng/api';
import { A4gToastComponent } from './a4g-toast.component';

@Injectable({
  providedIn: 'root'
})
export class A4gToastService {

  constructor(
    private messageService: MessageService,
    private renderer: Renderer2, 
    private el: ElementRef) { }

  openFromComponent(componentType: A4gToastComponent, data: A4GToastModel) {
    // const toast = this.renderer.createElement(componentType);
    this.addSingle(data);
  }

  addSingle(a4gToastModel: A4GToastModel) {
    a4gToastModel = this.setSummary(a4gToastModel);
    this.messageService.add({ severity: a4gToastModel.severity, summary: a4gToastModel.summary, detail: a4gToastModel.detail });
  }

  addMultiple(a4gToastModels: Array<A4GToastModel>) {
    // set summary TODO:
    this.messageService.addAll(a4gToastModels);
  }

  clear(key: string = undefined) {
    this.messageService.clear(key);
  }

  setSummary(a4gToastModel: A4GToastModel): A4GToastModel {
    switch (a4gToastModel.severity) {
      case A4GToastSeverityEnum.error: {
        a4gToastModel.summary = 'Errore!';
        break;
      }
      case A4GToastSeverityEnum.info: {
        a4gToastModel.summary = 'Informazione';
        break;
      }
      case A4GToastSeverityEnum.success: {
        a4gToastModel.summary = 'Avviso!';
        break;
      }
      case A4GToastSeverityEnum.warn: {
        a4gToastModel.summary = 'Attenzione!';
        break;
      }
      default:
        a4gToastModel.summary = 'Informazione';
        break;
    }
    return a4gToastModel;
  }

}
