export class AttributiLayer {

    id: number;
    nome: string;
    valoreDefault: string;
    editabile: boolean;
    constructor(id, nome, valoreDefault, editabile) {
        this.id = id;
        this.nome = nome;
        this.valoreDefault = valoreDefault;
        this.editabile = editabile;
    }

}
