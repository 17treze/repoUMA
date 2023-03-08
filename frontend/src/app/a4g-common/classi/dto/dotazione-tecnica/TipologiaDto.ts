export interface TipologiaDto {
    id: string;
    descrizione: string;
}

export interface ClasseFunzionaleDto extends TipologiaDto {
    tipologie: Array<TipologiaDto>; /** Lista di un elemento relativo alla classe funzionale della tipologia identificata da id/descrizione */
}

export interface SottoTipoDto extends TipologiaDto {
    tipologie: Array<TipologiaDto>; /** Lista di un elemento relativo al sottotipo della tipologia identificata da id/descrizione */
    classiFunzionali: Array<ClasseFunzionaleDto>;
}