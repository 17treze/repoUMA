export class Persona {
    id: number;
    versione: number;
    descrizione: string;
    codiceFiscale: string;
    nome: string;
    cognome: string;
    nrProtocolloPrivacyGenerale: string;

    static toDtos(incs: any): Persona[] {
        if (!incs) return null;
        let list: Persona[] = [];
        for (let inc of incs) {
            if (!inc) continue;
            list.push(Persona.toDto(inc));
        }
        return list;
    }

    static toDto(inc: any): Persona {
        if (!inc) return null;
        let dati: Persona = new Persona();
        dati.id = inc.id;
        dati.versione = inc.versione;
        dati.descrizione = inc.descrizione;
        dati.codiceFiscale = inc.codiceFiscale;
        dati.nome = inc.nome;
        dati.cognome = inc.cognome;
        dati.nrProtocolloPrivacyGenerale = inc.nrProtocolloPrivacyGenerale;
        return dati;
    }
}