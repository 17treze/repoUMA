//oggetto che va spedito al backend per richiedere CUAA e denominazioni per l'autocompletamento

export class AutocompleteElencoDomandeParams {
    statoSostegno : string;
    statoDomanda: string;
    sostegno: string;
    annoCampagna: string;
    tipo: string;
}
