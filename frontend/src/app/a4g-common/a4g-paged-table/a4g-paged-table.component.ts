import { Table, SortableColumn, TableCheckbox, SortIcon, SelectableRow } from "primeng/table";
import {
  Component,
  OnInit,
  Input,
  Output,
  ViewChild,
  ContentChildren,
  QueryList,
  AfterContentInit,
  NgModule,
  EventEmitter,
  Directive
} from "@angular/core";
import { PrimeTemplate } from "primeng/api";
import { DomHandler } from "primeng/dom";
import { CheckboxModule } from "primeng/checkbox";
import { Labels } from "../../../app/app.labels";
import { TableModule, TableService } from "primeng/table";
import { ObjectUtils } from "primeng/utils";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { Subject } from 'rxjs';


@Component({
  selector: "app-a4g-paged-table",
  templateUrl: "./a4g-paged-table.component.html",
  styleUrls: ["./a4g-paged-table.component.css"],
  providers: [DomHandler, ObjectUtils, TableService, Table]
})
export class A4gPagedTableComponent extends Table implements OnInit, AfterContentInit {
  @Output() isSelectedAllChange: EventEmitter<boolean> = new EventEmitter();
  @Output() onLazyLoadPage: EventEmitter<any> = new EventEmitter();
  _selectedItems: any[] = [];
  @Output() selectedItemsChange: EventEmitter<any> = new EventEmitter();
  _isSelectedAll: boolean;

  @Input() filterGlobal(value, matchMode) {
    this.pTable.filterGlobal(value, matchMode);
  }

  @Input() public sizeArray = null;

  @ViewChild("pTable", { static: true })
  pTable: Table;

  intestazioni = Labels;
  displaySelectAll: boolean = false;

  @ContentChildren(PrimeTemplate) templates: QueryList<PrimeTemplate>;

  selectionPageSubject = new Subject<any>();
  ngOnInit() {
    if (!this._selectedItems) {
      this._selectedItems = [];
    }
  }

  @Input()
  get selectedItems() {
    return this._selectedItems;
  }
  // selezione puntuale: scatena un evento nella checkbox della pagina
  set selectedItems(val) {
    this._selectedItems = val;
    this.selectedItemsChange.emit(this._selectedItems);
    this.selectionPageSubject.next();
  }

  @Input()
  get isSelectedAll() {
    return this._isSelectedAll;
  }

  ngAfterContentInit() {
    this.pTable.templates = this.templates;
    this.pTable.ngAfterContentInit();
  }
  // seleziona tutto:
  // emit necessario per fare ponte tra componente nativo primeng e implementazione di a4g paged table
  set isSelectedAll(val) {
    this._isSelectedAll = val;
    this.isSelectedAllChange.emit(this._isSelectedAll);
  }
}

@Component({
  selector: "app-a4g-paged-table-checkbox",
  template: `
    <p-checkbox
      [(ngModel)]="isPageSelected"
      (click)="selezionaTutto()"
      binary="true"
    ></p-checkbox>
  `
})
export class A4gPagedTableCheckbox implements OnInit {
  isPageSelected: boolean = false;

  constructor(
    public parentTable: A4gPagedTableComponent,
    public tableService: TableService
  ) { }

  ngOnInit() {
    // evento scatenato dal cambio pagina e lazy load 
    this.parentTable.pTable.tableService.valueSource$.subscribe(pageInfo => {
      if (pageInfo) {
        const jsonArrayselectedItems = this.parentTable.selectedItems.map(variable => JSON.stringify(variable));
        const jsonArrayvalue = this.parentTable.pTable.value.map(variable => JSON.stringify(variable));
        // isPageSelected varia in base agli elementi selezionati in pagina
        if (jsonArrayvalue.length > 0 && jsonArrayvalue.every(v => jsonArrayselectedItems.includes(v))) {
          this.isPageSelected = true;
        } else {
          this.isPageSelected = false;
        }
      }
    });

    this.parentTable.selectionPageSubject.subscribe(response => {
      const jsonArrayselectedItems = this.parentTable.selectedItems.map(variable => JSON.stringify(variable));
      const jsonArrayvalue = this.parentTable.pTable.value.map(variable => JSON.stringify(variable));

      if (jsonArrayvalue.every(v => jsonArrayselectedItems.includes(v))) {
        this.isPageSelected = true;
      } else {
        this.isPageSelected = false;
        this.parentTable.displaySelectAll = false;
      }
      this.parentTable.isSelectedAll = false;
    });
  }

  // selezione pagina:
  selezionaTutto() {
    this.parentTable.displaySelectAll = this.isPageSelected;
    if (this.isPageSelected) {
      // seleziona gli elementi in pagina - utilizzato json.stringify per confrontare oggetti 
      let value = this.parentTable.selectedItems.concat(this.parentTable.value);
      let jsonArray = value.map(variable => JSON.stringify(variable));
      this.parentTable.selectedItems = value.filter((item, index) => jsonArray.indexOf(JSON.stringify(item)) == index);
    } else {
      // codice commentato: mantiene selezione per pagina 
      // const jsonArrayvalue = this.parentTable.pTable.value.map(variable => JSON.stringify(variable));
      // let newSelectedArray = this.parentTable.selectedItems.filter((item) => !jsonArrayvalue.includes(JSON.stringify(item)));
      // this.parentTable.selectedItems = newSelectedArray;

      // quando deseleziono pagina, cancella tutti i selezionati 
      this.parentTable.selectedItems = [];
    }
  }

}


@Directive({
  selector: '[pSortableColumnWrapper]',
  providers: [DomHandler],
  host: {
    '[class.ui-sortable-column]': 'isEnabled()',
    '[class.ui-state-highlight]': 'sorted'
  }
})
export class SortableColumnWrapper extends SortableColumn {

  @Input("pSortableColumnWrapper") field: string;

  constructor(public parentTable: A4gPagedTableComponent) {
    super(parentTable.pTable);
  }
}


@Directive({
  selector: '[pSelectableRowWrapper]',
  providers: [DomHandler],
  host: {
    '[class.ui-state-highlight]': 'selected'
  }
})
export class SelectableRowWrapper extends SelectableRow {

  @Input("pSelectableRowWrapper") data: any;

  constructor(public parentTable: A4gPagedTableComponent, public tableService: TableService) {
    super(parentTable.pTable, tableService);
  }
}


@Component({
  selector: 'p-tableCheckbox-wrapper',
  template: `
    <div class="ui-chkbox ui-widget" (click)="onClick($event)">
        <div class="ui-helper-hidden-accessible">
            <input type="checkbox" [checked]="checked" (focus)="onFocus()" (blur)="onBlur()" [disabled]="disabled">
        </div>
        <div #box [ngClass]="{'ui-chkbox-box ui-widget ui-state-default':true,
            'ui-state-active':checked, 'ui-state-disabled':disabled}">
            <span class="ui-chkbox-icon ui-clickable" [ngClass]="{'pi pi-check':checked}"></span>
        </div>
    </div>
  `
}
)
export class TableCheckboxWrapper extends TableCheckbox {

  constructor(public parentTable: A4gPagedTableComponent, public tableService: TableService) {
    super(parentTable.pTable, tableService);
  }

}


@Component({
  selector: 'p-sortIcon-wrapper',
  template: `
    <a href="#" (click)="onClick($event)" class="ui-table-sort-icon">
        <i class="ui-sortable-column-icon pi pi-fw" [ngClass]="{'pi-sort-up': sortOrder === 1, 'pi-sort-down': sortOrder === -1, 'pi-sort': sortOrder === 0}"></i>
    </a>
  `
})
export class SortIconWrapper extends SortIcon {
  constructor(public parentTable: A4gPagedTableComponent) {
    super(parentTable.pTable);
  }
}


@NgModule({
  imports: [CommonModule, FormsModule, CheckboxModule, TableModule],
  exports: [A4gPagedTableCheckbox, A4gPagedTableComponent, SortableColumnWrapper, TableCheckboxWrapper, SortIconWrapper, SelectableRowWrapper],
  declarations: [A4gPagedTableCheckbox, A4gPagedTableComponent, SortableColumnWrapper, TableCheckboxWrapper, SortIconWrapper, SelectableRowWrapper]
})
export class A4gPagedTableModule { }