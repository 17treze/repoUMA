export class EredeDto {
    id: number;
    cuaa: string;
    cfErede: string;
    cognome: string;
    nome: string;
    dataNascita: Date;
    comuneNascita: string;
    provinciaNascita: string;
    indirizzoResidenza: string;
    comuneResidenza: string;
    provinciaResidenza: string;
    capResidenza: string;

    static toDtos(incs: any): EredeDto[] {
        if (!incs) {
            return null;
        }
        const list: EredeDto[] = [];
        for (const inc of incs) {
            if (!inc) {
                continue;
            }
            list.push(EredeDto.toDto(inc));
        }
        return list;
    }

    static toDto(inVal: any): EredeDto {
        if (!inVal) {
            return null;
        }
        const dati: EredeDto = new EredeDto();
        dati.id = inVal.id;
        dati.cuaa = inVal.cuaa;
        dati.cfErede = inVal.cfErede;
        dati.cognome = inVal.cognome;
        dati.nome = inVal.nome;
        dati.dataNascita = inVal.dataNascita;
        dati.comuneNascita = inVal.luogoNascita;
        dati.indirizzoResidenza = inVal.indirizzo?.descrizioneEstesa ? inVal.indirizzo.descrizioneEstesa : null;
        dati.comuneResidenza = inVal.indirizzo?.comune ? inVal.indirizzo.comune : null;
        dati.provinciaResidenza = inVal.indirizzo?.provincia ? inVal.indirizzo.provincia : null;
        dati.capResidenza = inVal.indirizzo?.cap ? inVal.indirizzo.cap : null;
        return dati;
    }
}
