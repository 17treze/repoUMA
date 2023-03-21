export interface DomandaCollegata {
	 id: number,
	 cuaa: string,
	 tipoDomanda: string,
	 idDomanda: number,
	 dtDomanda: string,
	 campagna: number,
	 importoRichiesto: number,
	 dtBdnaOp: Date,
	 dtBdna: Date,
	 protocollo: string,
	 statoBdna: string,
	 misurePsr: string,
	 dtInizioSilenzioAssenso: Date,
	 dtInizioEsitoNegativo: Date,
	 dtFineSilenzioAssenso: Date,
	 dtFineEsitoNegativo: Date
}
