import { Component, Input, OnInit, HostListener, ElementRef, ViewChild, NgModule, Renderer2, OnDestroy, ModuleWithProviders } from '@angular/core';
import { trigger, state, style, transition, animate, AnimationEvent } from '@angular/animations';
import { MenuItem } from 'primeng/api';
import { A4gMessages } from './a4g-messages';
import * as FileSaver from "file-saver";
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';


@Component({
    selector: '[action-submenu]',
    template: `
        <ng-template ngFor let-child let-i="index" [ngForOf]="(root ? item : item.items)">

            <li *ngIf="child.separator" class="ui-menu-separator ui-widget-content" [ngClass]="{'ui-helper-hidden': child.visible === false}">
            <li *ngIf="!child.separator" [ngClass]="{'active-menuitem': isActive(i)}" >
                <a [href]="child.url||'#'" (click)="itemClick($event,child,i)" class="ripplelink">
                    
                    <span class="menuitem-text">{{child.label}}<span (click)="badgeClick($event)" [class]="child.badgeStyle" *ngIf="this.badgeStylezed" ></span></span>
                    
                    <i class="material-icons layout-submenu-toggler" *ngIf="child.items">keyboard_arrow_down</i>
                    <span class="menuitem-badge" *ngIf="child.badge">{{this.badgeCounters}}</span>
                </a>

                <ul action-submenu [badgeCounters]="badgeCounters" [badgeData]="badgeData" [badgeStylezed]="badgeStylezed" [item]="child" *ngIf="child.items" [visible]="isActive(i)" [reset]="reset" [parentActive]="isActive(i)"
                    [@children]="root ? isActive(i) ?
                    'visible' : 'hidden' : isActive(i) ? 'visibleAnimated' : 'hiddenAnimated'" (@children.start)="onAnimationStart($event)"></ul>
            </li>
            
        </ng-template>
    `,
    animations: [
        trigger('children', [
            state('hiddenAnimated', style({
                height: '0px'
            })),
            state('visibleAnimated', style({
                height: '*'
            })),
            state('visible', style({
                height: '*',
                'z-index': 1000,
                'position': 'absolute',
                width: '170px'
            })),
            state('hidden', style({
                height: '0px',
                'z-index': '*'
            })),
            transition('visibleAnimated => hiddenAnimated', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)')),
            transition('hiddenAnimated => visibleAnimated', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)'))
        ])
    ]
})
export class ActionSubMenuComponent implements OnDestroy {

    @Input() item: MenuItem;

    @Input() root: boolean;

    @Input() visible: boolean;

    @Input() badgeCounters: any;

    @Input() badgeStylezed: any;

    @Input() badgeData: any;

    parentContainer: ElementRef;

    _reset: boolean;

    _parentActive: boolean;

    activeIndex: number;

    resetMenu: boolean;

    documentClickListener: any;

    constructor(public childContaier: ElementRef, public renderer: Renderer2, private http: HttpClient) {
    }

    badgeClick(evt: Event) {
        evt.stopPropagation();
        evt.preventDefault();

        if (!A4gMessages.isUndefinedOrNull(this.badgeData.file)) {
            this.downloadFile(this.badgeData.file, this.badgeData.fileName);
        } else if (!A4gMessages.isUndefinedOrNull(this.badgeData.fileDownloadLink)) {
            this.getFile(this.badgeData.fileDownloadLink).subscribe(file => {
                this.readByteFile(file).then(
                    (fileBase64: any) => {
                        this.downloadFile(fileBase64, this.badgeData.fileName);
                    });

            },
                err => {
                    console.error(err);
                });
        }
    }

    public getFile(url: string): Observable<any> {
        return this.http.get(url, {
            responseType: "blob"
        })
    }

    private downloadFile(file: any, fileName: string) {
        if (!A4gMessages.isUndefinedOrNull(file)) {
            let size: number = file.toString().length;

            let byteCharacters: string = atob(file.toString());
            let byteArrays: any = [];

            for (
                let offset: number = 0;
                offset < byteCharacters.length;
                offset += size
            ) {
                let slice: string = byteCharacters.slice(offset, offset + size);
                let byteNumbers: any = new Array(slice.length);
                for (let i: number = 0; i < slice.length; i++) {
                    byteNumbers[i] = slice.charCodeAt(i);
                }
                let byteArray: Uint8Array = new Uint8Array(byteNumbers);
                byteArrays.push(byteArray);
            }

            let blob: Blob = new Blob(byteArrays, { type: "blob" });

            FileSaver.saveAs(blob, fileName);
        }
    }

    private readByteFile(file: File) {
        return new Promise((resolve, reject) => {
            let reader = new FileReader();
            let error = "Errore nell' estrapolare i byte dal file " + file.name;
            let value;
            if (A4gMessages.isUndefinedOrNull(file)) {
                reject(error);
            } else {
                reader.readAsDataURL(file);
                reader.onloadend = event => {
                    value = reader.result;
                    if (value == null) {
                        reject(error);
                    } else {
                        resolve(value.toString().split(",")[1]);
                    }
                };
            }
        });
    }

    itemClick(event: Event, item: MenuItem, index: number) {


        if (this.root) {
            event.preventDefault();
        }

        // avoid processing disabled items
        if (item.disabled) {
            event.preventDefault();
            return true;
        }

        // activate current item and deactivate active sibling if any
        if (item.routerLink || item.items || item.command || item.url) {
            this.activeIndex = (this.activeIndex as number === index) ? -1 : index;
        }

        // execute command
        if (item.command) {
            item.command({ originalEvent: event, item: item });
            this.activeIndex = null;
        }

        // prevent hash change
        if (item.items || (!item.url && !item.routerLink)) {
            event.preventDefault();
        }

    }


    isActive(index: number): boolean {
        return this.activeIndex === index;
    }

    @Input() get reset(): boolean {
        return this._reset;
    }

    set reset(val: boolean) {
        this._reset = val;

        if (this._reset) {
            this.activeIndex = null;
        }
    }

    @Input() get parentActive(): boolean {
        return this._parentActive;
    }


    set parentActive(val: boolean) {
        this._parentActive = val;

        if (!this._parentActive) {
            this.activeIndex = null;
        }
    }

    ngOnDestroy() {
        this.unbindDocumentClickListener();
    }

    onAnimationStart(event: AnimationEvent) {
        switch (event.toState) {
            case 'visible':
            case 'visibleAnimated':
                this.bindDocumentClickListener();
                break;

            case 'hidden':
            case 'hiddenAnimated':
                this.unbindDocumentClickListener();
                break;
        }
    }


    bindDocumentClickListener() {
        if (!this.documentClickListener) {
            this.documentClickListener = this.renderer.listen('document', 'click', (event) => {
                const clickedInside = this.childContaier.nativeElement.contains(event.target);
                if (!clickedInside) {
                    if (!this.isDescendant(this.parentContainer, event.target)) {
                        this.hide();
                    }
                }
            });
        }
    }

    unbindDocumentClickListener() {
        if (this.documentClickListener) {
            this.documentClickListener();
            this.documentClickListener = null;
        }
    }

    hide() {
        this.reset = true;

    }

    private isDescendant(parent, child) {
        let node = child;
        while (node !== null) {
            if (node === parent) {
                return true;
            } else {
                node = node.parentNode;
            }
        }
        return false;
    }
}

@Component({
    selector: 'action-menu',
    template: `
        <div class="layout-sidebar" [ngClass]="{'layout-sidebar-active': true}" >
            <ul action-submenu [badgeCounters]="badgeCounters" [badgeData] = "badgeData" [badgeStylezed]="badgeStylezed" [item]="item" root="false" class="layout-menu action" visible="true" [reset]="reset" parentActive="true" ></ul>
        </div>
    `
})
export class ActionMenuComponent implements OnInit {

    @Input() reset: boolean;

    @Input() badgeCounters: any;

    @Input() badgeStylezed: any;

    @Input() item: any[];

    @Input() badgeData: any;

    constructor() {
    }

    ngOnInit() {

    }
}
