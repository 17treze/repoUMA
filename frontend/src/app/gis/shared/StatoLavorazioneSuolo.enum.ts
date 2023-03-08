import { EnumBase } from "./enumBase";

export enum StatoLavorazioneSuolo {
    IN_CREAZIONE = 'IN_CREAZIONE',
    IN_CORSO = 'IN_CORSO',
    SOSPESA = 'SOSPESA',
    CONSOLIDATA_SU_A4S = 'CONSOLIDATA_SU_A4S',
    CONSOLIDATA_SU_AGS = 'CONSOLIDATA_SU_AGS',
    CHIUSA = 'CHIUSA',
    IN_MODIFICA = 'IN_MODIFICA',
    CONSOLIDAMENTO_IN_CORSO = 'CONSOLIDAMENTO_IN_CORSO',
    ERRORE_CONSOLIDAMENTO = 'ERRORE_CONSOLIDAMENTO',
    CONSOLIDATA_AGS_PROBLEMI_SUOLO = 'CONSOLIDATA_AGS_PROBLEMI_SUOLO',
    CONSOLIDATA_AGS_PROBLEMI_PARTICELLE = 'CONSOLIDATA_AGS_PROBLEMI_PARTICELLE',
}

export class StatoLavorazioneSuoloDecode extends EnumBase {
    static lista = [
        { name: 'In creazione', value: StatoLavorazioneSuolo.IN_CREAZIONE },
        { name: 'In corso', value: StatoLavorazioneSuolo.IN_CORSO },
        { name: 'Sospesa', value: StatoLavorazioneSuolo.SOSPESA },
        { name: 'Consolidata su A4S', value: StatoLavorazioneSuolo.CONSOLIDATA_SU_A4S },
        { name: 'Consolidata su AGS', value: StatoLavorazioneSuolo.CONSOLIDATA_SU_AGS },
        { name: 'In Consolidamento', value: StatoLavorazioneSuolo.CONSOLIDAMENTO_IN_CORSO },
        { name: 'Chiusa', value: StatoLavorazioneSuolo.CHIUSA },
        { name: 'In modifica', value: StatoLavorazioneSuolo.IN_MODIFICA },
        { name: 'Errore consolidamento', value: StatoLavorazioneSuolo.ERRORE_CONSOLIDAMENTO },
        { name: 'Consolidata AGS problemi suolo', value: StatoLavorazioneSuolo.CONSOLIDATA_AGS_PROBLEMI_SUOLO },
        { name: 'Consolidata AGS problemi particelle', value: StatoLavorazioneSuolo.CONSOLIDATA_AGS_PROBLEMI_PARTICELLE }
    ];

    static decode(input): string {
      return super.innerDecode(input, StatoLavorazioneSuoloDecode.lista);
    }
}

