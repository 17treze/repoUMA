export class MacchinaDto {
    idAgs: number;
    descrizione: string;
    targa: string;
    classe: classeType;                 //in attesa di servizio     // tipo valore?
    marca: string;
    alimentazione: alimentazioneType;   //in attesa di servizio     // tipo valore?
    possesso: string;
    motore?: string;                    //in attesa di servizio
    rimorchio?: string;                 //in attesa di servizio
    immatricolazione?: string;          //in attesa di servizio     //data ?
    documentoPossesso?: string;         //in attesa di servizio
    dataIscrizione?: string;            //in attesa di servizio
    dataChiusura?: string;              //in attesa di servizio
}

export class FiltroMacchina {
    cuaa: string;
    data: string;
    tipiCarburante: Array<string>;
}

export class alimentazioneType {    //in attesa di servizio
    id: string;
    alimentazione: string;
}

export class classeType {           //in attesa di servizio
    id: string;
    tipologia: string;
}