import { KeyValuePair } from './../classi/KeyValuePair';

export class A4gMultiTable {
    datasource: any;
    title: string;
    header?: Array<KeyValuePair>;
}
export class A4GTitle {
    name: string;
    width: string;
    align: AlignTypeEnum;
}
export class A4gMultiTableColumn {
    field: string;                          /** object property to display */
    suffix?: string;                        /** optional, if present specify an object property to display after field */
    header: string;                         /** name of column */
    sortable: boolean;                      /** if true, column is sortable */
    disabled?: boolean;                     /** deafult = false - if true, column input is disabled */
    type: ColumnTypeEnum;                   /** Template definition for column */
    font?: FontTypeEnum;                    /** BOLD | ITALIC | LIGHT | NORMAL */
    icon?: string;                          /** optional, if present specify name of the icon */
    alignCellHorizontal?: keyof typeof AlignTypeEnum;    /** left | right | center */
    width: string;                          /** column width - sum of all columns input width must be 100% */
    alignHeader?: AlignTypeEnum;            /** left | right | center */
    inputOpts?: A4gMultiTableInputOptions;
    buttonOpts?: A4gMultiTableButtonOptions;
}

export interface A4gMultiTableInputOptions {
    pattern?: string;                       /** pattern of input field */
    maxLength: string;                      /** maxLength of input field */
    upperLimitField?: string;               /** field wich contains maximum value for input */
    action?: ActionByInput;                 /** action triggered when input change */
    max?: number;                           /** max number value of field */
    min?: number;                           /** min number value of field */
}
export interface ActionByInput {
    type: ActionInputEnum;
    byField: any;
    index: number;
}
export interface A4gMultiTableButtonOptions {
    id: string;                         /** id of the button */
    icon: string;                       /** icon of the button */
    disabled?: boolean;                 /** true if the button is disabled */
    disabledBy?: string;                /** based on this object propery, the button will be disabled or not */
    hiddenBy?: string;                  /** based on this object property, the button will be hidden or not */
}

export enum ColumnTypeEnum {
    INPUT_NUMBER = 'Input_Number',
    INPUT_NUMBER_ACTION = 'Input_Number_Action',
    READONLY = 'Readonly',
    ICON_BUTTON_SQUARE = 'Icon_Button_Square',
    ICON_BUTTON_CIRCLE = 'Icon_Button_Circle',
    CHECKBOX = 'Checkbox'
}
export enum ActionInputEnum {
    MULTIPLY_BY = 'MULTIPLY_BY'
}

export enum FontTypeEnum {
    BOLD = 'BOLD',
    ITALIC = 'ITALIC',
    LIGHT = 'LIGHT',
    NORMAL = 'NORMAL'
}

export enum AlignTypeEnum {
    LEFT = 'LEFT',
    RIGHT = 'RIGHT',
    CENTER = 'CENTER'
}

