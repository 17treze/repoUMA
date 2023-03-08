import {Pipe, PipeTransform} from '@angular/core';
import {SostegnoDu} from "../../istruttoria/istruttoria-pac1420/domandaUnica/classi/SostegnoDu";

@Pipe({
    pure: false,
    name: 'supportoDescription'
})
export class SupportoDescriptionPipe implements PipeTransform {

    transform(value: string): string {
        let result;
        switch (value) {
            case SostegnoDu.DISACCOPPIATO:
                result = 'Disaccoppiato';
                break;
            case SostegnoDu.SUPERFICIE:
                result = 'Accoppiato superficie';
                break;
            case SostegnoDu.ZOOTECNIA:
                result = 'Accoppiato zootecnia';
                break;
            default:
                result = '';
                break;
        }
        return result;
    }

}
