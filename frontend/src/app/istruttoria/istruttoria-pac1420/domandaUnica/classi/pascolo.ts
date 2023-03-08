import { CuaaDenominazione } from "../domain/cuaa-denominazione";
import { stringify } from "@angular/compiler/src/util";
import { PascoloDao } from "../domain/pascolo-dao";
import { DatiIstruttoriaPascoliDTO } from "../domain/dati-istruttoria-pascoli-dto";

export class Pascolo {
    tipoPascolo: string;
    codicePascolo: string;
    descPascolo: string;

    superficie: number;
    id: number;
    version: number;
    private static testDentroProvincia: RegExp = new RegExp(/^\d{0,3}TN\d{0,3}$/);
    private static testFuoriProvincia: RegExp = new RegExp(/^\d{0,3}BZ\d{0,3}$/);
    private static testPascoloAziendale: RegExp = new RegExp(/Aziendale/);

    constructor(id: number, version: number, codicePascolo: string, superficie: number, descPascolo: string, tipoPascolo?: string) {
        this.codicePascolo = codicePascolo;
        this.descPascolo = descPascolo;
        this.superficie = superficie;
        if (tipoPascolo === '' || tipoPascolo === undefined || tipoPascolo === null) {
            this.tipoPascolo = 'PascoloAziendale';
        } else {
            this.tipoPascolo = tipoPascolo;
        }
        this.id=id;
        this.version=version;
    }

    public static checkCodicePascolo(codicePascolo: string): string {
        let ret: string;
        if (this.testDentroProvincia.test(codicePascolo)) { ret = PascoloDentroProvincia.name; }
        if (this.testFuoriProvincia.test(codicePascolo)) { ret = PascoloFuoriProvincia.name; }
        if (this.testPascoloAziendale.test(codicePascolo)) { ret = PascoloAziendale.name; }
        return ret;

    }

    // metodo statico che converte un array di pascoliDao in un array di pascoli per inserimento dati istruttoria
    public static ArrPascoloDaoToArrPascolo(array: Array<PascoloDao>): Array<Pascolo> {
        let ret: Array<Pascolo> = [];
        if(array){
            for (let pascoloDao of array) {
                if (pascoloDao.datiIstruttoriaPascoli == null)
                    pascoloDao.datiIstruttoriaPascoli = new DatiIstruttoriaPascoliDTO();
    
                if (pascoloDao.tipoPascolo === 'MALGA TN')  {
                    ret.push(new PascoloDentroProvincia(pascoloDao.datiIstruttoriaPascoli.id, pascoloDao.datiIstruttoriaPascoli.version, pascoloDao.datiIstruttoriaPascoli.cuaaResponsabile, pascoloDao.codicePascolo, pascoloDao.datiIstruttoriaPascoli.superficieDeterminata, pascoloDao.descPascolo));
                }
                if ( pascoloDao.tipoPascolo ==='MALGA FUORI PROV') {
                    ret.push(new PascoloFuoriProvincia(pascoloDao.datiIstruttoriaPascoli.id, pascoloDao.datiIstruttoriaPascoli.version, pascoloDao.datiIstruttoriaPascoli.esitoControlloMantenimento, pascoloDao.datiIstruttoriaPascoli.cuaaResponsabile, pascoloDao.codicePascolo, pascoloDao.datiIstruttoriaPascoli.superficieDeterminata, pascoloDao.descPascolo));
                }
                if ( pascoloDao.tipoPascolo ==='AZIENDALE') {
                    ret.push(new PascoloAziendale(pascoloDao.datiIstruttoriaPascoli.id, pascoloDao.datiIstruttoriaPascoli.version, pascoloDao.datiIstruttoriaPascoli.verificaDocumentazione, pascoloDao.codicePascolo, pascoloDao.datiIstruttoriaPascoli.superficieDeterminata, pascoloDao.descPascolo));
                }
            }
        }

        return ret;
    }
}


export class PascoloDentroProvincia extends Pascolo {
    man4: string; //responsabile cuaa

    constructor(id: number, version: number, man4: string, codicePascolo: string, superficie: number , descrizionePascolo: string, tipoPascolo?: string) {
        super(id, version, codicePascolo, superficie, descrizionePascolo, 'PascoloDentroProvincia');
        this.man4 = man4;
        if (tipoPascolo === '' || tipoPascolo === undefined || tipoPascolo === null) {
            this.tipoPascolo = 'PascoloDentroProvincia';
        } else {
            this.tipoPascolo = tipoPascolo;
        }
    }
}

export class PascoloFuoriProvincia extends PascoloDentroProvincia {
    man5: string; //esito (positivo, negativo)
    constructor(id: number, version: number,  man5: string, man4: string, codicePascolo: string, superficie: number, descrizionePascolo: string) {
        super(id, version, man4, codicePascolo, superficie, descrizionePascolo, 'PascoloFuoriProvincia');
        this.man5 = man5;

    }
}

export class PascoloAziendale extends Pascolo {
    man3: string; // documentazione (presente, assente, incompleta)

    constructor(id: number, version: number,  man3: string, codicePascolo: string, superficie: number, descrizionePascolo: string) {
        super(id, version, codicePascolo, superficie, descrizionePascolo);
        this.man3 = man3;

    }
}


