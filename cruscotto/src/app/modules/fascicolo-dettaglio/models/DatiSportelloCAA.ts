export class DatiCAA {
    id: number;
    identificativo: number;
    denominazione: string;
    // sede: Sede;
    indirizzo: Indirizzo;
    codiceFiscale: string;
    partitaIVA: string;
    societaServizi: string;
    sportelli: SportelloCAA[];
    estremiAtto: string;

    static toDto(inc: any): DatiCAA {
        if (!inc) return null;
        let dati: DatiCAA = new DatiCAA();
        dati.id = inc.id;
        dati.identificativo = inc.identificativo;
        dati.denominazione = inc.denominaizone;
        // dati.sede = Sede.toDto(inc.sede);
        dati.indirizzo = Indirizzo.toDto(inc.indirizzo);
        dati.codiceFiscale = inc.codiceFiscale;
        dati.societaServizi = inc.societaServizi;
        dati.sportelli = SportelloCAA.toDtos(inc.sportelli);
        dati.estremiAtto = inc.estremiAtto;
        return dati;
    }
}

// export class Sede {
//     codice: string;
//     denominazione: string;
//     indirizzo: Indirizzo;
//     codiceFiscale: string;
//     estremiAtto: string;

//     static toDto(inc: any): Sede {
//         if (!inc) return null;
//         let dati: Sede = new Sede();
//         dati.codice = inc.codice;
//         dati.denominazione = inc.denominazione;
//         dati.indirizzo = inc.indirizzo;
//         dati.codiceFiscale = inc.codiceFiscale;
//         dati.estremiAtto = inc.estremiAtto;
//         return dati;
//     }
// }

export class Indirizzo {
    via: string;
    denominazioneComune: string;
    denominazioneProvincia: string;
    siglaProvincia: string;
    cap: string;
    toponimo: string;

    static toDto(inc: any): Indirizzo {
        if (!inc) return null;
        let dati: Indirizzo = new Indirizzo();
        dati.via = inc.via;
        dati.denominazioneComune = inc.denominazioneComune;
        dati.denominazioneProvincia = inc.denominazioneProvincia;
        dati.siglaProvincia = inc.siglaProvincia;
        dati.cap = inc.cap;
        dati.toponimo = inc.toponimo;
        return dati;
    }
}

export class SportelloCAA {
    id: number;
    identificativo: number;
    denominazione: string;
    indirizzo: Indirizzo;
    cap: string;
    comune: string;
    provincia: string;
    telefono: string;
    email: string;

    static toDtos(incs: any[]): SportelloCAA[] {
        if (!incs) return null;
        let sportelli: SportelloCAA[] = [];
        for (let inc of incs) {
            if (!inc) continue;
            let sportello: SportelloCAA = SportelloCAA.toDto(inc);
            sportelli.push(sportello);
        }
        return sportelli;
    }

    static toDto(inc: any): SportelloCAA {
        if (!inc) return null;
        let dati: SportelloCAA = new SportelloCAA();
        dati.id = inc.id;
        dati.identificativo = inc.identificativo;
        dati.denominazione = inc.denominazione;
        dati.indirizzo = inc.indirizzo;
        dati.cap = inc.cap;
        dati.comune = inc.comune;
        dati.provincia = inc.provincia;
        dati.telefono = inc.telefono;
        dati.email = inc.email;
        return dati;
    }
}
