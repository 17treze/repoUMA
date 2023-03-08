import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { EnableBox, HeaderBox, ButtonBox, ClickButton, ButtonTypeEnum } from '../box/box.model';
import { FileBox } from '../classi/FileBox';

@Component({
  selector: 'app-box-allegati',
  templateUrl: './box-allegati.component.html',
  styleUrls: ['./box-allegati.component.scss']
})
export class BoxAllegatiComponent implements OnInit {
  @Input() enableBox: EnableBox;
  @Input() header: HeaderBox;
  @Input() button: ButtonBox;
  @Input() name: string;

  @Input() mainFile: FileBox;

  @Input() attachments: Array<FileBox>;

  @Output() onClickButton = new EventEmitter<ClickButton>();

  constructor() { }

  ngOnInit() {
  }

  public visualizzaAllegato() {
  }

  clickButton($event: Event, id: string, type: ButtonTypeEnum, index?: number, tipo?: string) {
    const clickEvent: ClickButton = { event: $event, idButton: id, typeButton: type , index: index == null ? null : index, tipoElemento: tipo == null ? null : tipo}
    if (this.name) {
      clickEvent.componentName = this.name;
    }
    this.onClickButton.emit(clickEvent);
  }
}
