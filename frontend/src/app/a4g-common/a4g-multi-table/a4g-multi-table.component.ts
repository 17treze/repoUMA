import { Component, ElementRef, Input, OnChanges, OnInit, Output, QueryList, SimpleChanges, ViewChildren } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { KeyValuePair } from '../classi/KeyValuePair';
import { A4gMultiTableColumn, ColumnTypeEnum, ActionByInput } from './a4g-multi-table-model';
import { A4gMultiTableService } from './a4g-multi-table.service';
@Component({
  selector: 'app-a4g-multi-table',
  templateUrl: './a4g-multi-table.component.html',
  styleUrls: ['./a4g-multi-table.component.scss']
})
export class A4gMultiTableComponent implements OnInit, OnChanges {

  @Input() cols: Array<A4gMultiTableColumn>;  /** columns definition */
  @Input() values: Array<any>;                /** datasource */
  @Input() header: Array<KeyValuePair>;       /** header (key-values) of the table */
  @Input() title: string;                     /** optional, title of the table */
  @Input() titles: string;                    /** optional, array of titles of the table with a width param */
  @Input() readonly: boolean;                 /** optional, default = false, if true, inputs are disabled */
  @Input() customSort: boolean;               /** optional, default = true, if false, custom sort is enabled */
  @ViewChildren("inputRef", { read: ElementRef }) inputRefs: QueryList<ElementRef>;

  /** @param isChanged  is true if at least one input changes */
  /** @param isValid    is true if validations on inputs are valid */
  @Output() changedValueForm = new EventEmitter<{ isChanged: boolean, isValid: boolean, index?: number, action?: ActionByInput }>();

  /** @param element  x-coordinate displayed row */
  /** @param col      y-coordinate column where element is clicked */
  @Output() clickIcon = new EventEmitter<{ element: any, col: A4gMultiTableColumn }>();

  /** @param element  x-coordinate displayed row */
  /** @param col      y-coordinate column where element is clicked */
  @Output() clickCheckbox = new EventEmitter<{ element: any, col: A4gMultiTableColumn }>();

  columnType = ColumnTypeEnum;

  private isValidForm: boolean;

  constructor(
    public a4gMultiTableService: A4gMultiTableService
  ) { }

  ngOnInit() {
    this.isValidForm = true;
  }

  ngOnChanges(changes: SimpleChanges): void {
  }

  onChangeInputNumber($event, inputRef: any, index?: number, action?: ActionByInput) {
    this.isValidForm = !this.inputRefs['_results']
      .filter((element: ElementRef) => !element.nativeElement.disabled)
      .filter((input: ElementRef) => input.nativeElement.validity.patternMismatch || input.nativeElement.validity.max || input.nativeElement.validity.maxVal)
      .length
    this.changedValueForm.emit({ isChanged: true, isValid: this.isValidForm, index, action: action?.type ? { type: action.type, byField: action.byField, index: action.index } : undefined });
  }

  onBlurInputNumber(element: any, inputRef: any) {
  }

  onBlurTextArea(element: any, txtAreaInpuRef: any) {
  }

  onChangeTextArea(element: any, txtAreaInpuRef: any) {
    this.changedValueForm.emit({ isChanged: true, isValid: this.isValidForm });
  }

  onClickIcon($event: Event, element: any, col: A4gMultiTableColumn) {
    this.clickIcon.emit({ element, col });
    // reset validity
    this.onChangeInputNumber($event, element);
  }

  onClickButton(element: any, col: A4gMultiTableColumn) {
    this.clickIcon.emit({ element, col });
  }

  onChangeCheckbox(element: any, col: A4gMultiTableColumn) {
    this.clickCheckbox.emit({ element, col });
  }
}
