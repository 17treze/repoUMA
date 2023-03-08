import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { ButtonBox, ButtonTypeEnum, ClickButton, EnableBox, HeaderBox } from './box.model';
import * as FileSaver from "file-saver";
import { FileTypeEnum } from 'src/app/uma/shared-uma/models/enums/FileType-enum';
import { FileBox } from '../classi/FileBox';
@Component({
  selector: 'app-box',
  templateUrl: './box.component.html',
  styleUrls: ['./box.component.scss']
})
export class BoxComponent implements OnInit {
  @Input() name: string;
  @Input() header: HeaderBox;
  @Input() button: ButtonBox;
  @Input() enableBox: EnableBox;
 
  @Input() document: FileBox;

  @Output() onClickButton = new EventEmitter<ClickButton>();

  constructor() { }

  ngOnInit() {
  }

  public visualizzaAllegato() {
    if (this.document.file != null) {
      const fileURL = URL.createObjectURL(this.document.file);
      FileSaver.saveAs(this.document.file, this.document.file.name);
      if (this.document.file.type === FileTypeEnum.PDF) {
        window.open(fileURL);
      }
    }
  }

  clickButton($event: Event, id: string, type: ButtonTypeEnum) {
    const clickEvent: ClickButton = { event: $event, idButton: id, typeButton: type }
    if (this.name) {
      clickEvent.componentName = this.name;
    }
    this.onClickButton.emit(clickEvent);
  }
}
