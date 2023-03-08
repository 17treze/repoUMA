
export interface DomandaUmaFabbisognoViewModel {
    id: number;
    key: DomandaUmaFabbisognoRowEnum; /** row key to find correctly a row */
    field: string;                    /** model of a row -> using with a colspan */
    tipologiaFabbisogno: string;
    benzina: number;
    gasolio: number;
    gasolioSerre: number;
    gasolioTerzi: number;
    note: string;
    colspan?: string;
    disabled?: boolean;
}

export enum DomandaUmaFabbisognoRowEnum {
    RESIDUO = 'RESIDUO',
    AMMISSIBILE = 'AMMISSIBILE',
    ASSEGNATO_RICHIESTO = 'ASSEGNATO_RICHIESTO',
    NOTE = 'NOTE'
}
