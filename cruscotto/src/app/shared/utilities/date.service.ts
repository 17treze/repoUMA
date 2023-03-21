import * as moment from 'moment';
import { Injectable } from "@angular/core";
import { TranslateService } from "@ngx-translate/core";

@Injectable({
  providedIn: "root"
})
export class DateService {

  public patternDate: string;
  public patternDateTime: string;

  constructor(private translateService: TranslateService) {
    this.patternDate = translateService.instant("common.dateFormat");
    this.patternDateTime = translateService.instant("common.dateTimeFormat");
    console.log(this.patternDate);
  }

  public fromIso(str: string): Date {
    return moment(str, moment.ISO_8601).toDate();
  }

  public toIsoDate(date: Date): string {
    return moment(date).format(this.patternDate);
  }

  public toIsoDateTime(date: Date): string {
    return moment(date).format(this.patternDateTime);
  }

  public getDateTimeFromString(date?: string) {
    if (!date) return 0;

    return new Date(date).getTime();
  }

  public isAfterDates(d1: Date, d2: Date): boolean {
    const isAfter: boolean = moment(d1.valueOf()).isAfter(d2.valueOf());
    return isAfter;
  }

  public isBeforeDates(d1: Date, d2: Date): boolean {
    const isBefore: boolean = moment(d1.valueOf()).isBefore(d2.valueOf());
    return isBefore;
  }

  public getToday(): Date {
    return new Date();
  }

}
