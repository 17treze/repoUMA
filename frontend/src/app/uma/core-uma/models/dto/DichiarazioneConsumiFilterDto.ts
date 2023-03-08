import { StatoDichiarazioneConsumiEnum } from 'src/app/a4g-common/classi/enums/uma/StatoDichiarazioneConsumi.enum';

export interface DichiarazioneConsumiFilterDto {
    cuaa: string;
    campagna: number;
    stati: Array<keyof typeof StatoDichiarazioneConsumiEnum>;
}
