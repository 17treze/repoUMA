export class DettaglioPagamentoM10O13 {
  dettaglioRazze: DettaglioRazza[];
  esitoFinaleDettaglioPagamentoPsr: EsitoFinaleDettaglioPagamentoPsrM10O13;
}

export class DettaglioPagamentoM10O11 {
  interventoBase: InterventoM10O11;
  interventoMagg: InterventoM10O11;
  esitoFinale: EsitoFinaleM10O11;
}

export class InterventoM10O11 {
  title: string;
  caricoBestiame: number;
  superficieRichiesta: number;
  superficieAmmissibile: number;
  percentualeScostamento: number;
  aliquotaSostegno: number;
  importoAmmissibile: number;
  importoSanzioneSovradichiarazione: number;
  importoCalcolatoIntervento: number;
}

export class EsitoFinaleM10O11 {
  importoRiduzioneMancatoRispettoImpegni: number;
  importoRiduzioneRitardataPresentazione: number;
  coefficienteRiduzioneSuperamentoBudget: number;
  importoCalcolato: number;
}

export class DettaglioPagamentoM10O12 {
  dettaglioMalghe: DettaglioMalga[];
  esitoFinale: EsitoFinaleM10O12;
}

export class DettaglioMalga {
  codiceAlpeggio: string;
  caricoDiBestiameDellaMalga: number;
  superficieRichiesta: number;
  superficieAmmissibile: number;
  percentualeDiScostamento: number;
  aliquotaDiSostegno: number;
  importoAmmissibile: number;
  importoDellaSanzionePerSovradichiarazione: number;
  percentualeRiduzioneMancatoRispettoImpegniMalga: number;
  importoRiduzioneMancatoRispettoImpegniMalga: number;
  importoCalcolatoMalga: number;
}

export class EsitoFinaleM10O12 {
  importoRiduzionePerRitardataPresentazione: number;
  coefficienteRiduzionePerSuperamentoBudget: number;
  importoCalcolato: number;
  importoLiquidatoPrecedentemente: number;
  importoCalcolatoLiquidato: number;
  premioAntiLiq: number;
  premioTotLiquidato: number;
  percentualeRiduzioneMancatoRispettoImpegniBaseline: number;
  importoRiduzioneMancatoRispettoImpegniBaseline: number;
}

export class DettaglioPagamentoM10O14 {
  aliquotaSostegno: number;
  coefficienteDiRiduzionePerSuperamentoBudget: number;
  importoAmmissibile: number;
  importoCalcolato: number;
  importoRiduzionePerRitardataPresentazione: number;
  importoSanzione: number;
  percentualeRiduzioneMancatoRispettoImpegniSpecifici: number;
  percentualeRiduzioneMancatoRispettoImpegniBaseline: number;
  importoRiduzioneMancatoRispettoImpegni: number;
  percentualeDiScostamento: number;
  superficieAmmissibile: number;
  superficieRichiesta: number;
}

export class EsitoFinaleDettaglioPagamentoPsrM10O13 {
  percentualeRiduzioneMancatoRispettoImpegniSpecifici: number;
  percentualeRiduzioneMancatoRispettoImpegniBaseline: number;
  importoRiduzioneMancatoRispettoImpegni: number;
  importoRiduzionePerRitardataPresentazione: number;
  coefficienteRiduzionePerSuperamentoBudget: number;
  importoTotale: number;
}

export class DettaglioRazza {
  nome: string;
  caricoBestiame: number;
  caricoBestiameAccertatoControlloInLoco: number;
  animaliRichiesti: number;
  animaliAmmissibili: number;
  percentualeScostamento: number;
  aliquotaSostegno: number;
  importoAmmissibile: number;
  importoSanzioneSovradichiarazione: number;
  importoCalcolatoIntervento: number;
}

export class DettaglioPagamentoM11 {
  sostegniMantenimentoPsr: DettaglioSostegnoMantenimentoPsr[];
  esitoFinaleDettaglioPagamentoPsr: EsitoFinaleDettaglioPagamentoPsr11;
}

export class DettaglioSostegnoMantenimentoPsr {
  nomeSostegno: string;
  superficieRichiesta: number;
  superficieAmmissibile: number;
  percentualeScostamento: number;
  aliquotaSostegno: number;
  importoAmmissibile: number;
  importoSanzioneSovradichiarazione: number;
  calcolatoIntervento: number;
}

export class EsitoFinaleDettaglioPagamentoPsr11 {
  percentualeRiduzioneMancatoRispettoImpegniBaseline: number;
  importoRiduzioneMancatoRispettoImpegniBaselineIntroduzione: number;
  importoRiduzioneMancatoRispettoImpegniBaselineMantenimento: number;
  importoRiduzioneRitardataPresentazione: number;
  coefficienteRiduzioneLineareSuperamentoBudget: number;
  importoCalcolatoIntroduzione: number;
  importoCalcolatoMantenimento: number;
  importoCalcolatoTotale: number;
  importoLiquidatoPagamentiPrecedentiIntroduzione: number;
  importoLiquidatoPagamentiPrecedentiMantenimento: number;
  importoCalcolatoLiquidatoIntroduzione: number;
  importoCalcolatoLiquidatoMantenimento: number;
  importoCalcolatoLiquidatoTotale: number;
}

export class DettaglioPagamentoM1311 {
  datiAziendali: DatiAziendali;
  sistemiAgricoli: SistemaAgricolo[];
  gestionePluriennaleSanzioni: GestionePluriennaleSanzioni;
  calcoloDegressivitaPremio: CalcoloDegressivitaPremio;
  esitoFinale: EsitoFinaleDettaglioPagamentoEsitoPsrM1311;
  riduzioniControlloInLoco: RiduzioniControlloInLoco
}

export class DatiAziendali {
  caricoDiBestiame: number;
  superficieForaggera: number;
  animaliInAlpeggio: number;
  aziendaTransumante: string;
  coefficientePendenzaAltitudine: number;
  presenzaSanzioniAnniPrecendenti: string;
  caricoBestiameAccertatoControllo: string;
  superficieForaggeraAccertataControllo: string;
}

export class SistemaAgricolo {
  tableName: string;
  superficieRichiesta: number;
  superficieAmmissibile: number;
  percentualeScostamento: number;
  aliquotaSostegno: number;
  importoAmmissibile: number;
  importoSanzioneSovradichiarazione: number;
  importoCalcolatoIntervento: number;

}

export class GestionePluriennaleSanzioni {
  presenzaSanzAnniPrec: string;
  presenzaSanzioniAnnoCorrente: string;
  presenzaRecidiva: string;
  sanzioneScontataPrimaInfrazione: number
  sanzioneRecidiva: number;
  sanzioneComplessiva: number;
}

export class CalcoloDegressivitaPremio {
  importoCalcolatoAziendale: number;
  superficieAziendaleOggettoDiContributo: number;
  importoCalcolatoAziendaleDopoDegressivita: number;

}

export class EsitoFinaleDettaglioPagamentoEsitoPsrM1311 {
  importoRiduzionePerRitardataPresentazione: number;
  coefficienteRiduzionePerSuperamentoBudget: number;
  coefficienteRiduzionePerSuperamentoLimiteEttaro: number;
  importoCalcolato: number;
  importoLiquidatoPrecedentemente: number;
  importoTotaleGiaLiquidato: number;
  premioantliq: number;
  premiototliquidato: number;
}

export class RiduzioniControlloInLoco {
  importoRiduzioneMancatoRispettoImpegniUba: number;
  percentualeRiduzioneMancatoRispettoImpegniZootecnico: number;
  importoRiduzioneMancatoRispettoImpegniZootecnico: number;
} 