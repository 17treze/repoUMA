import { PanelEvent } from './../../../shared/PanelEvent';
import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { enumListener } from './enumListeners';
import { ToolBarService } from '../../../shared/ToolBar/toolBar.service';

export abstract class GisListener implements OnInit, OnDestroy {
    @ViewChild('identifyPanel') identifyPanel: ElementRef;
    subscription: any;

    constructor(private listenerName: enumListener,
        private toolBarService: ToolBarService, public panelEvent: PanelEvent) {

        if (this.toolBarService.listenerGroup) {
            if (listenerName) {
                this.toolBarService.listenerGroup.push(listenerName);
            }
        }
    }

    ngOnInit() {
    }

    ngOnDestroy() {
        if (this.isActive) {
            this.enableListener();
          }
    }

    abstract get canUseListener(): boolean;

    get isActive(): boolean {
        return this.toolBarService.activeListener === this.listenerName;
    }

    checkIdentifyVisibility () {
        if (this.identifyPanel &&  this.identifyPanel.nativeElement) {
            this.panelEvent.panelIdentifyVisible = this.identifyPanel.nativeElement.hidden && this.panelEvent.showWmsResults;
        }
    }

    enableListener() {
        console.log('enableListener', this.listenerName, 'isActive ', this.isActive);
        if (this.isActive) {
            console.log('Disattivo activeListener per bottone', this.toolBarService.activeListener);
            this.toolBarService.activeListener = null;
            if (this.panelEvent) {
                this.panelEvent.identifyTableContent = [];
            }
        } else {
            if (this.canUseListener) {
                if (this.panelEvent) {
                    this.panelEvent.identifyTableContent = [];
                }
                const res = this.toolBarService.rimuoviListaInteraction(this.toolBarService.editToolGroup, true, true);
                this.toolBarService.activeListener = this.listenerName;
            }
        }
        this.checkIdentifyVisibility();
    }


}
