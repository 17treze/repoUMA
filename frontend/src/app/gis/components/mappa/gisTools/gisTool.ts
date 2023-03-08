import { Component, HostListener, OnInit } from '@angular/core';
import { enumTool } from './enumTools';
import { enumControls } from './enumControls';
import { ToolBarService } from '../../../shared/ToolBar/toolBar.service';

export abstract class GisTool implements OnInit {
    subscription: any;

    constructor(protected mainToolName: enumTool[], private otherToolNames: enumTool[],
        private hasSnap: boolean,
        private isExclusive: boolean,
        protected clearSelection: boolean,
        protected toolBar: enumTool[],
        private toolBarService: ToolBarService) {
        if (toolBar) {
            if (mainToolName) {
                mainToolName.forEach(element => {
                    if (element) {
                        toolBar.push(element);
                    }
                });
            }
            if (otherToolNames) {
                otherToolNames.forEach(element => {
                    if (element) {
                        toolBar.push(element);
                    }
                });
            }
            if (hasSnap) {
                if (!toolBar.includes(enumTool.snapEl)) {
                    toolBar.push(enumTool.snapEl);
                }
            }
        }
    }

    ngOnInit() {
        document.addEventListener('keydown', ({ key }) => {
            if (key === 'Escape' && this.toolBarService.activeTool) {
                const res = this.toolBarService.activeTool;
                if (res && this.mainToolName.find(x => x === res) !== undefined) {
                    this.toolBarService.rimuoviListaInteraction([res], this.clearSelection, true);
                    this.enableTool(enumTool[res]);
                    console.log(res);
                }
            }
        });
    }

    abstract get canUseTool(): boolean;
    abstract declareTool(toolName: enumTool): any[];

    isActive(): boolean {
        return this.toolBarService.activeTool === this.mainToolName[0]; // this.toolBarService.getInteractionFomName(this.mainToolName[0]);
    }

    enableTool(toolName: enumTool = null) {
        if (!toolName) {
            toolName = this.mainToolName[0];
        }
        // this.toolBarService1.misuraStyle = false;

        console.log('enableTool', toolName);

        const isInInteraction = this.toolBarService.isInInteraction(toolName);
        if (isInInteraction) {
            this.toolBarService.exclusiveActiveTool = null;
            const toolDadisabilitare = [toolName];
            if (this.otherToolNames) {
                this.otherToolNames.forEach(element => {
                    toolDadisabilitare.push(element);
                });
            }
            if (this.hasSnap) {
                toolDadisabilitare.push(enumTool.snapEl);
            }
            this.toolBarService.rimuoviListaInteraction(toolDadisabilitare, this.clearSelection, true);

            if (toolName === enumTool.drawNewEl ||
                toolName === enumTool.drawRegularEl ||
                toolName === enumTool.drawHoleEl ||
                toolName === enumTool.lineSplit ||
                toolName === enumTool.polySplit ||
                toolName === enumTool.closeHole ||
                toolName === enumTool.coverHole ||
                toolName === enumTool.drawBuffer) {
                this.setContextMenu(false);
            }
        } else {
            if (this.canUseTool || toolName === enumTool.misuraEl || toolName === enumTool.drawAdl || toolName === enumTool.modifyAdl) {
                const tools = this.declareTool(toolName);
                if (tools && tools.length > 0) {
                    if (tools.length === 1) {
                        ToolBarService.setInteractionProperties(tools[0], toolName);
                    }
                    this.toolBarService.addInteraction(tools, this.isExclusive, this.hasSnap, this.clearSelection, this.toolBar);

                    if (toolName === enumTool.drawNewEl ||
                        toolName === enumTool.drawRegularEl ||
                        toolName === enumTool.drawHoleEl ||
                        toolName === enumTool.lineSplit ||
                        toolName === enumTool.polySplit ||
                        toolName === enumTool.closeHole ||
                        toolName === enumTool.coverHole ||
                        toolName === enumTool.drawBuffer ||
                        toolName === enumTool.drawAdl) {
                        this.setContextMenu(true);
                    } else {
                        this.setContextMenu(false);
                    }
                }
            }
        }
    }

    reloadTool(toolName: enumTool = null) {
        if (!toolName) {
            toolName = this.mainToolName[0];
        }

        if (this.canUseTool || toolName === enumTool.misuraEl) {
            const tools = this.declareTool(toolName);
            if (tools && tools.length === 1) {
                ToolBarService.setInteractionProperties(tools[0], toolName);
            }
            this.toolBarService.addInteraction(tools, this.isExclusive, this.hasSnap, this.clearSelection, this.toolBar);
        }
    }

    setContextMenu(active: boolean) {
        const contextMenu = this.toolBarService.getControlFromName(enumControls.contextMenu)[0];
        contextMenu.disabled = active;
    }
}
