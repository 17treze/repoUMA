import { FormatConverterService } from 'src/app/uma/shared-uma/services/format-converter.service';
import { Injectable } from '@angular/core';
import * as moment from 'moment';
@Injectable({
  providedIn: 'root'
})
export class DateUtilService {

  constructor(
    private formatConverterService: FormatConverterService
  ) { }

  getCurrentYear(): number {
    return new Date().getFullYear();
  }

  getCurrentMonth(): number {
    return new Date().getUTCMonth() + 1; // months from 1-12
  }

  getCurrentDay(): number {
    return new Date().getUTCDate();
  }

  getToday(): Date {
    return new Date();
  }

  toDateFrom(dd: number, mm: number, yyyy: number): Date {
    const date = new Date(yyyy, mm - 1, dd);
    return date;
  }

  toStringFromDate(date: Date, format: string): string {
    const stringified = moment(date, format).format(format);
    return stringified;
  }

  isSameYear(d1: Date, d2: Date): boolean {
    const isSame: boolean = d1.getFullYear() === d2.getFullYear();
    return isSame;
  }

  isSameDates(d1: Date, d2: Date): boolean {
    const isSame: boolean = moment(d1.valueOf()).isSame(d2.valueOf());
    return isSame;
  }

  isSameOrBeforeDates(d1: Date, d2: Date): boolean {
    const isSameOrBefore: boolean = moment(d1.valueOf()).isSameOrBefore(d2.valueOf());
    return isSameOrBefore;
  }

  isSameOrAfterDates(d1: Date, d2: Date): boolean {
    const isSameOrAfter: boolean = moment(d1.valueOf()).isSameOrAfter(d2.valueOf());
    return isSameOrAfter;
  }

  isAfterDates(d1: Date, d2: Date): boolean {
    const isAfter: boolean = moment(d1.valueOf()).isAfter(d2.valueOf());
    return isAfter;
  }

  isBeforeDates(d1: Date, d2: Date): boolean {
    const isBefore: boolean = moment(d1.valueOf()).isBefore(d2.valueOf());
    return isBefore;
  }

  // ottiene una lista di anni (fino all'anno corrente) a partire dall'anno di partenza passato in input
  getListYearsFrom(yearToStart: number): Array<number> {
    const currentYear = this.getCurrentYear();
    const startYear = yearToStart;
    const yearsArray = [];
    if (currentYear < yearToStart) { // se nel 2021 mi occorre la lista dal 2022 in poi, inserisco solo il 2022
      yearsArray.push(yearToStart);
    }
    for (let i = startYear; i <= currentYear; i++) {
      yearsArray.push(i);
    }
    return yearsArray;
  }

  // conversione in Date a partire dal formato 01 Gennaio 2020
  toDate(_DDMeseYYYY: string): Date {
    const tokens = _DDMeseYYYY.split(' ');
    const day = tokens[0];
    const month = this.toMonthNumber(tokens[1]);
    const year = tokens[2];
    return new Date(this.formatConverterService.toNumber(year), month, this.formatConverterService.toNumber(day));
  }

  // calcolo indice a partire dal nome del mese
  toMonthNumber(nameOfMonthIta: string) {
    if (nameOfMonthIta == null) {
      return -1;
    }
    const monthsIta = ['GENNAIO', 'FEBBRAIO', 'MARZO', 'APRILE', 'MAGGIO', 'GIUGNO', 'LUGLIO', 'AGOSTO', 'SETTEMBRE', 'OTTOBRE', 'NOVEMBRE', 'DICEMBRE'];
    return monthsIta.indexOf(nameOfMonthIta.trim().toUpperCase());
  }

  /** normalizazione ed eliminazione dell'offset */
  toDateWithNoOffset(date: Date): Date {
    return new Date(date.getTime() + Math.abs(date.getTimezoneOffset() * 60000));
  }

  /** to LocalDate */
  toLocalDate(date: Date): string {
    return moment(date).format('YYYY-MM-DDTHH:mm:ss.SSS');
  }
}
