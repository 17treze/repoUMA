export enum YesNoEnum {
    YES = "YES",
    NO = "NO"
}

export enum RiservaEnum {
    A_GIOVANE_AGRICOLTORE = "A_GIOVANE_AGRICOLTORE",
    B_NUOVO_AGRICOLTORE = "B_NUOVO_AGRICOLTORE",
    C_ABBANDONO_TERRE = "C_ABBANDONO_TERRE",
    D_COMPENSAZIONE_SVANTAGGI_SPECIFICI = "D_COMPENSAZIONE_SVANTAGGI_SPECIFICI",
    F_PROVVEDIMENTI_AMMINISTRATIVI_DECISIONI_GIUDIZIARIE = "F_PROVVEDIMENTI_AMMINISTRATIVI_DECISIONI_GIUDIZIARIE",
    NON_RICHIESTA = "NON_RICHIESTA"
}

export enum AnomalieWarnEnum {
    BPS_RIDUZIONI = 'BRIDUSDC020',
    BPS_SANZIONI_INF_10 = 'BRIDUSDC021_INF_10',
    BPS_SANZIONI_SUP_10 = 'BRIDUSDC021_SUP_10',
    GREENING_RIDUZIONI = 'BRIDUSDC026',
    GREENING_SANZIONI = 'BRIDUSDC055',
    GIOVANE_NO_REQUISITI= 'BRIDUSDC028',
    GIOVANE_RIDUZIONI = 'BRIDUSDC059',
    GIOVANE_SANZIONI = 'BRIDUSDC060',
    //IMPORTO_DA_RECUPERARE = 'BRIDUSDC134',
    RIDUZIONE_DA_CAPPING = 'BRIDUSDC043',
    RIDUZIONE_PER_RITARDO = 'BRIDUSDC036'
}

export enum AnomalieWarnDebitiEnum {
    IMPORTO_DA_RECUPERARE = 'BRIDUSDL134',
    IMPORTO_NULLO = 'BRIDUSDL135',
    IMPORTO_IRRILEVANTE = 'BRIDUSDL135_IRR',
}

export enum AnomalieInfoEnum {
    BPS_SUPERFICE_MINIMA = 'BRIDUSDC035',
    BPS_PRESENZA_MAN = 'BRIDUSDC019',
    BPS_ANOMALIE_COORDINAMENTO = 'BRIDUSDC135',
    BPS_SCONTO_SANZIONE = 'BRIDUSDC032',
    BPS_RECIDIVA = 'BRIDUSDC029',
    BPS_RECUPERO_SCONTO = 'BRIDUSDC030',
    GREENING_RINUNCIA = 'BRIDUSDC023',
    GREENING_BIOLOGICA = 'BRIDUSDC024',
    GREENING_NO_IMPEGNI= 'BRIDUSDC025_NO_IMPEGNI',
    GREENING_DIV = 'BRIDUSDC025_DIV',
    GREENING_DIV_EFA = 'BRIDUSDC025_DIV_EFA',
    GREENING_ESENTE = 'BRIDUSDC033',
    GREENING_ESENTE_EFA = 'BRIDUSDC117',
    GIOVANE_SCONTO_SANZIONE = 'BRIDUSDC057',
    GIOVANE_RECIDIVA = 'BRIDUSDC056',
    GIOVANE_RECUPERO_SCONTO = 'BRIDUSDC058',
    RIDUZIONI_SENZA_SANZIONI = 'BRIDUSDC021'
}

export enum AnomalieErrorCalcoloKoEnum {
    NO_INFO_AGRICOLTORE_ATTIVO = 'BRIDUSDC009',
    AGRICOLTORE_NON_ATTIVO = 'BRIDUSDC010',
    NESSUN_TITOLO_PRESENTE = 'BRIDUSDC011',
    SUPERFICIE_MINIMA_RICHIESTA = 'BRIDUSDC012',
    CONTROLLO_IN_LOCO_APERTO = 'BRIDUSDC034',
    INSERIRE_INFORMAZIONI_MANTENIMENTO = 'BRIDUSDC067',
    DOMANDA_ANNO_SCORSO_NON_LIQUIDATA = 'BRIDUSDC109',
    IMPORTO_DA_RECUPERARE = 'BRIDUSDC031'
}

export enum AnomalieErrorLiquidabileKoEnum {
    IBAN_NON_VALIDO = 'BRIDUSDL037',
    AGRICOLTORE_DECEDUTO = 'BRIDUSDL038',
    AZIENDA_SOSPESA_DAI_PAGAMENTI = 'BRIDUSDL039',
    DATI_EREDE_NON_CERTIFICATI = 'BRIDUNVL129'
}

export enum AnomaliaInterSostegnoEnum {
    NO_IMPORTO_MINIMO_ERROR = 'BRIDUSDS040',
    IMPORTO_MAGGIORE_DI_25000_ERROR = 'BRIDUSDS049',
    IMPORTO_MINORE_DI_25000_INFO = 'BRIDUSDS049',
    NO_DICHIARAZIONE_ANTIMAFIA_INFO = 'BRIDUSDS050'
}

export class IstruttoriaFiltroRicercaDomande {
    idDatiSettore: number;
    statoDomanda: string;
    sostegno: string;
    statoSostegno: string;
    anomalie: YesNoEnum;
    anomalieWARNING: string[];
    anomalieINFO: string[];
    anomalieERROR: string[];
    impresa: string;
    cuaa: string;
    numero_domanda: number;
    denominazione: string;
    pascoli: YesNoEnum;
    campione: YesNoEnum;
    giovane: YesNoEnum;
    riservaNazionale: RiservaEnum;
    interventi: string[];
    bloccataBool: YesNoEnum;
    erroreCalcolo: YesNoEnum;
    integrazione: YesNoEnum;
}
