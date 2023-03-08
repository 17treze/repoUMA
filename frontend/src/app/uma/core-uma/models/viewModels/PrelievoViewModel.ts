export interface PrelievoViewModel {
    distributoreIdentificativo: number;
    distributoreId: number;
    distributoreNome: string;
    distributoreNomeEsteso?: string;
    distributoreIndirizzo: string;
    readonly: boolean;                  /** true se appartiente all'utente distributore, false altrimenti -> prelievo in sola lettura */
    hiddenCircle: boolean;              /** false se appartiente all'utente distributore, true altrimenti -> nascosta icona delete */
    isConsegnato: boolean;
    estremiDocumentoFiscale: string;
    benzina: number;
    gasolio: number;
    gasolioSerre: number;
    data: string;
    id: number;
    cuaa: string;
    denominazione: string;
}