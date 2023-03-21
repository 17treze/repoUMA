import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'codDestinazioneToDescription'
})
export class CodDestinazioneToDescriptionPipe implements PipeTransform {

  transform(value: string): string {
    if (value.startsWith('M10')) {
      switch (value) {
        case 'M10.1.1_base': {
          return 'Base';
        }
        case 'M10.1.1_nat_sup_1k': {
          return 'Natura 2000 > 1000 slm';
        }
        case 'M10.1.1_nat_inf_1k': {
          return 'Natura 2000 < 1000 slm';
        }
        case 'M10.1.1_rds': {
          return 'Ricchi di specie';
        }
        case 'M10.1.2_75': {
          return 'Mandrie con < 15 UBA in lattazione';
        }
        case 'M10.1.2_90': {
          return 'Mandrie con > 15 UBA in lattazione';
        }
      }
    }

    if (value.startsWith('M11.')) {
      if (value.endsWith('.1_Arboree')) {
        return 'Arboree';
      }
      if (value.endsWith('.1_Orticole')) {
        return 'Orticole';
      }
      if (value.endsWith('.1_prato')) {
        return 'Prato';
      }
      if (value.endsWith('.1_ViteMelo')) {
        return 'Vite Melo';
      }
    }

    if (value.startsWith('M13')) {
      switch (value) {
        case 'M13_sa123': {
          return 'Sistemi Zootecnici';
        }
        case 'M13_sa4': {
          return 'Arboricoltura intensiva';
        }
        case 'M13_sa5': {
          return 'Arboricoltura estensiva';
        }
        case 'M13_sa6': {
          return 'Ortofloricoltura';
        }
      }
    }
    return value;
  }
}
