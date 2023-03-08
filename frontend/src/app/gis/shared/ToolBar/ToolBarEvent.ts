import { EventEmitter, Injectable } from '@angular/core';
import { enumTool } from '../../components/mappa/gisTools/enumTools';

@Injectable()
export class ToolBarEvent {
    selectChangeEmitter: EventEmitter<any> = new EventEmitter();
    public toolEditActive: boolean;
    exclusiveActiveTool: enumTool;
}
