import { Pipe, PipeTransform, Injectable } from '@angular/core';

@Pipe({ name: 'fixDichiarazioni' })
@Injectable()
export class FixDichiarazioni implements PipeTransform {
    transform(value: string): string {
        return value.replace('DICH_', '').replace('_', ' ');
    }
}
@Pipe({
    name: 'tipoFilter'
})
@Injectable()
export class TipoFilter implements PipeTransform {
    transform(items: any[], field: string, value: string): any[] {
        if (!items) {
            return [];
        }
        if (!field || !value) {
            return items;
        }

        return items.filter(singleItem =>
            singleItem[field].toLowerCase().includes(value.toLowerCase())
        );
    }
}
