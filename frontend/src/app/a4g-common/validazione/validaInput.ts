
export class validaInput {

    static validaCuaaParziale(cuaa: string, accettaNoVal: boolean) {
        if (validaInput.campoNonValorizzato(cuaa)) {
            return accettaNoVal;
        }

        var myregexp1 = /^[0-9]+$/i;
        var myregexp2 = /^[a-zA-Z0-9]+$/i;
        if (cuaa.length >= 3 &&
            ((cuaa.length <= 16 && myregexp2.test(cuaa) == true) ||
                (cuaa.length <= 11 && myregexp1.test(cuaa) == true)))
            return true;
        else
            return false;
    }

    static validaCuaaIntero(cuaa: string, accettaNoVal: boolean) {
        if (validaInput.campoNonValorizzato(cuaa)) {
            return true;
        }

        var myregexp1 = /^[0-9]+$/i;
        var myregexp2 = /^[a-zA-Z0-9]+$/i;
        if ((cuaa.length === 16 && myregexp2.test(cuaa) == true) ||
            (cuaa.length === 11 && myregexp1.test(cuaa) == true))
            return true;
        else
            return false;
    }

    static validaCf(cuaa: string, accettaNoVal: boolean) {
        if (validaInput.campoNonValorizzato(cuaa)) {
            return true;
        }

        var myregexp1 = /^[a-zA-Z0-9]+$/i;
        if (cuaa.length === 16 && myregexp1.test(cuaa) == true)
            return true;
        else
            return false;
    }

    static validaDenominazione(userIn: string, accettaNoVal: boolean) {
        if (accettaNoVal && validaInput.campoNonValorizzato(userIn))
            return true;

        var myregexp3 = /^[a-zA-Z0-9"&/()'ìèéòàù°,. -]+$/i;
        if (validaInput.validaTestoRicerca(userIn) && myregexp3.test(userIn)) {
            return true;
        }
    }

    static validaTestoRicerca(userIn: string) {
        if (validaInput.campoNonValorizzato(userIn))
            return true;
        else
            return userIn.trim().length >= 3;
    }

    static campoNonValorizzato(userIn: string): boolean {
        return (!userIn);
    }

    static validaTelefono(telefono: string, accettaNoVal: boolean) {
        if (validaInput.campoNonValorizzato(telefono)) {
            return accettaNoVal;
        }

        var myregexp1 = /^[0-9]+$/i;
        if (myregexp1.test(telefono) == true)
            return true;
        else
            return false;
    }

    static validaEmail(email: string, accettaNoVal: boolean) {
        if (validaInput.campoNonValorizzato(email)) {
            return accettaNoVal;
        }

        var myregexp1 = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/i;
        if (myregexp1.test(email) == true)
            return true;
        else
            return false;
    }

    static validaNome(userIn: string, accettaNoVal: boolean) {
        if (accettaNoVal && validaInput.campoNonValorizzato(userIn))
            return true;

        var myregexp3 = /^[a-zA-Z'ìèéòàù ]+$/i;
        if (validaInput.validaTestoRicerca(userIn) && myregexp3.test(userIn))
            return true;
    }

    static validaCognome(userIn: string, accettaNoVal: boolean) {
        if (accettaNoVal && validaInput.campoNonValorizzato(userIn))
            return true;

        var myregexp3 = /^[a-zA-Z'ìèéòàù ]+$/i;
        if (validaInput.validaTestoRicerca(userIn) && myregexp3.test(userIn))
            return true;
    }

}