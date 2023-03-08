export class DatiCartaServizi {

    popupVisibile: boolean;
    fileDaPopup: File;
    descrizioneAllegatoCartaServiziFile: string;
    verificaUploadCartaServizi: boolean;
    tipoFile: TipoFileEnum;

}

export enum TipoFileEnum {
    CARTA_SERVIZI = 1,
    ALTRO = 2
}
