import { Component, OnInit, AfterContentInit, Input, ContentChildren, QueryList, TemplateRef } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { PrimeTemplate } from 'primeng/api';
import { HackMenuItem } from './hack-menu-item';

@Component({
  selector: 'app-hack-ptab-menu',
  templateUrl: './hack-ptab-menu.component.html',
  styleUrls: ['./hack-ptab-menu.component.css']
})
export class HackPtabMenuComponent implements AfterContentInit {

    @Input() model: MenuItem[];

    @Input() activeItem: MenuItem;

    @Input() popup: boolean;

    @Input() style: any;

    @Input() styleClass: string;

    @ContentChildren(PrimeTemplate) templates: QueryList<any>;

    itemTemplate: TemplateRef<any>;

    ngAfterContentInit() {
        this.templates.forEach((item) => {
            switch(item.getType()) {
                case 'item':
                    this.itemTemplate = item.template;
                break;
                
                default:
                    this.itemTemplate = item.template;
                break;
            }
        });
    }

    itemClick(event: Event, item: MenuItem) {
        if(item.disabled) {
            event.preventDefault();
            return;
        }

        if(!item.url) {
            event.preventDefault();
        }

        if(item.command) {
            item.command({
                originalEvent: event,
                item: item
            });
        }

        this.activeItem = item;
    }
}

