import { FlowIstruttoriaDUEnum } from './FlowIstruttoriaDUEnum';

// CALCOLO_PREMIO_ACCOPPIATO_ZOOTECNIA | CONTROLLO_LIQUIDABILITA_ACCOPPIATO_ZOOTECNIA | LIQUIDAZIONE
export class AzCountFilter {
    tipo: FlowIstruttoriaDUEnum;
    idDatiSettore: number;
}

export class AzCountResult {
    richiesti: number;
    integrazioniAmmisibilita: number;
    controlliSuperati: number;
    controlliNonSuperati: number;
    controlliLiquidabilitaNonSuperati: number;
    nonAmmissibili: number;
    liquidabili: number;
    nonLiquidabili: number;
    pagamentoNonAutorizzato: number;
    pagamentoAutorizzato: number;
    controlliIntersostegnoSuperati: number;
}