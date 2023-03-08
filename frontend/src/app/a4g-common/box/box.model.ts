export interface ButtonBox {
    label: string;
    icon: string;
    id: string;
    type: ButtonTypeEnum;
}

export enum ButtonTypeEnum {
    file = 'file',
    button = 'button'
}

export interface HeaderBox {
    title: string;
    subtitle: string;
    icon: string;
}

export interface EnableBox {
    enableHeader: boolean;
    enableButton: boolean;
}

export interface ClickButton {
    event: Event;
    idButton: string;
    typeButton: ButtonTypeEnum;
    componentName?: string;
    index?: number;
    tipoElemento?: string;
}