import { Component, OnInit } from '@angular/core';
import { enumTool } from './enumTools';

export abstract class GisButton {
    public mainName: enumTool;

    /*constructor(mainToolName: enumTool, otherToolNames: enumTool[],
        hasSnap: boolean, isExclusive: boolean, toolBar: enumTool[], private toolBarService: ToolBarService, toolCaption: string = null) {
        this.mainToolName = mainToolName;
    }*/
    constructor(mainName: enumTool) {
        this.mainName = mainName;
    }

    abstract get canUse(): boolean;
    abstract click();

}
