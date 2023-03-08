import { ParticellaDao } from "./particella-dao";
import { DatiIstruttoriaPascoliDTO } from "./dati-istruttoria-pascoli-dto";
import { DatiInputDto } from "./dati-input-dto";
import { DatiOutputDto } from "./dati-output-dto";
import { VariabiliCalcoliDto } from "./variabili-calcoli-dto";
import { Controllo } from "./controllo";

export class PascoloDao {
    codicePascolo: string;
    descPascolo: string;
    tipoPascolo: string;
    esitoMan: boolean;
    supNettaPascolo: number;
    listaParticelle: Array<ParticellaDao>;
    datiIstruttoriaPascoli: DatiIstruttoriaPascoliDTO;
    datiInput?: Array<VariabiliCalcoliDto>;
    datiOutput?: Array<VariabiliCalcoliDto>;
    listaEsitiPascolo?: Array<Controllo>;
    }
