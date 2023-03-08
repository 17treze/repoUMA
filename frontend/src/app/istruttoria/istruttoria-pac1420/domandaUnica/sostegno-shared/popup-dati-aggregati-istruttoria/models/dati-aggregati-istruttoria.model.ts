export class DatiAggregatiIstruttoriaModel {
    public keyDesc: string;
    public stato: string; // TipoProcesso enum?
    public numeroDomande: number;
    public valoreLordo: number;
    public valoreNetto: number;
}

export class DatiAggregatiIstruttoriaTotaliModel {
    public pagamentoAutorizzato: number;
    public totaleCalcolato: number;
    public percentualePagamento: string;
}
