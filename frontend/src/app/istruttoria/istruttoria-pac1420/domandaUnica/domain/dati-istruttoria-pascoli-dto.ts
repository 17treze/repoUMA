export class DatiIstruttoriaPascoliDTO {

    esitoControlloMantenimento?:string;
    verificaDocumentazione?:string;
    descrizionePascolo?: string;
    superficieDeterminata: number;
    cuaaResponsabile?: string;
    id:number;
    version:number;

    constructor(esito?: string, documentazione?: string, descrizionePascolo?:string, superficieDeterminata?:number, cuaaResponsabile?: string, id?:number, version?:number){
        this.esitoControlloMantenimento = esito ;
        this.verificaDocumentazione = documentazione;
        this.descrizionePascolo = descrizionePascolo;
        this.superficieDeterminata = superficieDeterminata;
        this.cuaaResponsabile = cuaaResponsabile;
        this.id = id;
        this.version = version;
    }
    
}
