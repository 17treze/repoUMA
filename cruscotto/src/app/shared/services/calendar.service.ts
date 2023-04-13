import { Injectable } from '@angular/core';
import { LocaleSettings } from 'primeng-lts';
@Injectable({
  providedIn: 'root'
})
export class CalendarService {
  localeITA: LocaleSettings;

  constructor() { }

  /** settaggio locale Italiano per p-calendar
   * @param locale
   */
  configITA() {
    this.localeITA = {
      firstDayOfWeek: 1,
      dayNames: ['domenica', 'lunedi', 'martedi', 'mercoledi', 'giovedi', 'venerdi', 'sabato'],
      dayNamesShort: ['dom', 'lun', 'mar', 'mer', 'gio', 'ven', 'sab'],
      dayNamesMin: ['Dom', 'Lun', 'Mar', 'Mer', 'Gio', 'Ven', 'Sab'],
      monthNames: ['Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio', 'Giugno', 'Luglio', 'Agosto', 'Settembre',
        'Ottobre', 'Novembre', 'Dicembre'],
      monthNamesShort: ['Gen', 'Feb', 'Mar', 'Apr', 'Mag', 'Giu', 'Lug', 'Ago', 'Sep', 'Ott', 'Nov', 'Dic'],
      today: 'Oggi',
      clear: 'Svuota'
    };
  }


}
