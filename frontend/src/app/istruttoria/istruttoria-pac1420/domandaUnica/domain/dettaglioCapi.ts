export class DettaglioCapi {
    codiceIntervento: any;
    codiceSpecie: string;
    cuaaIntestatario: string;
    datiAllevamento: any;
    datiDetentore: any;
    datiProprietario: any;
    id: number;
    richiesteAllevamentoDuEsito: CapoRichiesto[];
    stato: string;
    count: number;
}


export class CapoRichiesto {
    id: number;
    capoId: number;
    codiceCapo: string;
    esito: string;
    messaggio: string;
    duplicato: boolean;
    controlloSuperato: boolean;
    controlloNonSuperato: boolean;
    stato: string;
    capoBdn: CapoBdn;
}

export class CapoBdn {
    aziendaCodice: string;
    capoLatteBdn: CapoLatteBdn;
    codice: string;
    cuaa: string;
    dataCsv: Date;
    dtFineDetenzione: Date;
    dtInizioDetenzione: Date;
    dtNascita: Date;
    intervento: string;
    razzaCodice: string;
    sesso: string;
}

export class DetenzioneBdn {
    allevamentoMontagna: boolean;
    codiceAsl: string;
    cuaa: string;
    dtFineDetenzione: Date;
    dtInizioDetenzione: Date;
    vaccaDtComAutoritaIngresso: Date;
    vaccaDtIngresso: Date;
    vaccaDtInserimentoBdnIngresso: Date;
}

export class CapoLatteBdn {
    detenzioniBdn: Array<DetenzioneBdn>;
    vitelloBdn: VitelloBdn;
}

export class VitelloBdn {
    codVitello: string;
    dtInserimentoBdnNascita: Date;
    dtNascita: Date;
    flagDelegatoNascitaVitello: string;
    flagProrogaMarcatura: string;
    tipoOrigine: string;
}