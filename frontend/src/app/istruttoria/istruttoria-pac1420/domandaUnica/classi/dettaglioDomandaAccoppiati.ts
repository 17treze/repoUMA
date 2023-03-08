import { RiferimentiCartografici, InformazioniColtivazione, Particella } from "../domain/richiestaSuperficie";
import { DichiarazioneDu } from "../domain/dichiarazioneDu";

export class DettaglioDomandaAccoppiati {
    idDomanda: string;
    controlliSostegno: DettaglioControlliSostegno;
    datiIstruttoriaACS: DettaglioDatiIstruttoriaACS;
    datiIstruttoriaACZ: DettaglioDatiIstruttoriaACZ;
    datiParticellaACS: DatiParticella;
    datiDomandaACS: DatiDomandaACS;
    datiDomandaACZ: DatiDomandaACZ;
    datiSuperficiImpegnate: SuperficiImpegnateACS;
    dichiarazioni: Array<DichiarazioneDu>;
    datiDisciplinaFinanziaria: DatiInterventi;
}

export class DettaglioControlliSostegno {
    errors: string[];
    warnings: string[];
    infos: string[];
    successes: string[];
}

export class DettaglioDatiIstruttoriaACS {
    controlloSigecoLoco: boolean;
    controlloAntimafia: boolean;
    superficieDeterminataSoiaM8: number;
    superficieDeterminataFrumentoM9: number;
    superficieDeterminataProteaginoseM10: number;
    superficieDeterminataLeguminoseM11: number;
    superficieDeterminataPomodoroM14: number;
    superficieDeterminataOlivoStandardM15: number;
    superficieDeterminataOlivoPendenzaM16: number;
    superficieDeterminataOlivoQualitaM17: number;
}

export class DettaglioDatiIstruttoriaACZ {
    controlloSigecoLoco: boolean;
    controlloAntimafia: boolean;
}

export class DatiParticella {
    m8: DettaglioDatiParticella[];
    m9: DettaglioDatiParticella[];
    m10: DettaglioDatiParticella[];
    m11: DettaglioDatiParticella[];
    m14: DettaglioDatiParticella[];
    m15: DettaglioDatiParticella[];
    m16: DettaglioDatiParticella[];
    m17: DettaglioDatiParticella[];
}

export class DettaglioDatiParticella {
    codComune: string;
    foglio: string;
    particella: string;
    sub: string;
    codColtura: string;
    descColtura: string;
    controlloRegioni: boolean;
    controlloColture: boolean;
    controlloAnomCoord: boolean;
    supImpegnata: number;
    supRichiesta: number;
    supControlliLoco: number;
    supEleggibileGis: number;
    supDeterminata: number;
    superficieAnomalieCoordinamento: number;
}

export class DatiDomandaACS {
    sintesiCalcolo: DettagliCalcolo[];
    dettaglioCalcolo: DettagliInterventiACS;

}

export class DatiDomandaACZ {
    sintesiCalcolo: DettagliCalcolo[];
    dettaglioCalcolo: DettagliInterventiACZ;

}

export class DettagliInterventiACS {
    m8: DatiInterventi[];
    m9: DatiInterventi[];
    m10: DatiInterventi[];
    m11: DatiInterventi[];
    m14: DatiInterventi[];
    m15: DatiInterventi[];
    m16: DatiInterventi[];
    m17: DatiInterventi[];
}

export class DettagliInterventiACZ {
    int310: DatiInterventi[];
    int311: DatiInterventi[];
    int313: DatiInterventi[];
    int315: DatiInterventi[];
    int320: DatiInterventi[];
    int321: DatiInterventi[];
    int316: DatiInterventi[];
    int318: DatiInterventi[];
    int322: DatiInterventi[];
}

export class DatiInterventi {
    input: DettagliCalcolo[];
    output: DettagliCalcolo[];
}

export class DettagliCalcolo {
    codice: any;
    valore: any;
}

export class SuperficiImpegnateACS {
    supRichiesta: number;
    supRichiestaNetta: number;
    m8: DatiSupImpegnata;
    m9: DatiSupImpegnata;
    m10: DatiSupImpegnata;
    m11: DatiSupImpegnata;
    m14: DatiSupImpegnata;
    m15: DatiSupImpegnata;
    m16: DatiSupImpegnata;
    m17: DatiSupImpegnata;
}

export class DatiSupImpegnata {
    supRichiesta: number;
    supRichiestaNetta: number;
    superficiImpegnate: DettaglioSupImpegnata[];
}

export class DettaglioSupImpegnata {
    datiCatastali: Particella;
    datiColtivazione: InformazioniColtivazione;
    riferimentiCartografici: RiferimentiCartografici;
    supRichiesta: number;
    supRichiestaNetta: number;
}

export class SuperficiImpegnateSintesi {
    supRichiestaTotale: number;
    m8: number;
    m9: number;
    m10: number;
    m11: number;
    m14: number;
    m15: number;
    m16: number;
    m17: number;
}