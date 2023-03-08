export class Localization {

    public static itCalendar(): any {
        let it: any = {
            firstDayOfWeek: 0,
            dayNames: ["Domenica", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"],
            dayNamesShort: ["Dom", "Lun", "Mar", "Mer", "Gio", "Ven", "Sab"],
            dayNamesMin: ["Do", "Lu", "Ma", "Me", "Gi", "Ve", "Sa"],
            monthNames: ["Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"],
            monthNamesShort: ["Gen", "Feb", "Mar", "Apr", "Mag", "Giu", "Lug", "Ago", "Set", "Ott", "Nov", "Dic"],
            today: 'Oggi',
            clear: 'Cancella'
        };
        return it;
    }

}
