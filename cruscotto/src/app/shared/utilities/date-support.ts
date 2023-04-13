import * as moment from 'moment';

export class DateSupport {

    public static PATTERN_DATE: string = 'YYYY-MM-DD';
    public static PATTERN_DATE_2: string = 'dd MMMM yyyy';

    public static convertToPatternDate(onlyDate: Date, pattern = DateSupport.PATTERN_DATE): string {
        return moment(onlyDate).format(pattern);
    }

    public static convertToPatternTime(date: Date): string {
        return date.toISOString();
    }

    public static convertStringToOnlyDate(onlyDate: string, pattern = DateSupport.PATTERN_DATE): Date {
        if (onlyDate) {
            return moment(onlyDate, pattern).toDate();
        } else {
            return null;
        }
    }

    /** 
     * Compares two Date objects and returns e number value that represents 
     * the result:
     * 0 if the two dates are equal.
     * 1 if the first date is greater than second.
     * -1 if the first date is less than second.
     * @param date1 First date object to compare.
     * @param date2 Second date object to compare.
     */
    public static compareDate(date1: Date, date2: Date): number
    {
        // With Date object we can compare dates them using the >, <, <= or >=.
        // The ==, !=, ===, and !== operators require to use date.getTime(),
        // so we need to create a new instance of Date with 'new Date()'
        let d1 = new Date(date1); let d2 = new Date(date2);

        // Check if the dates are equal
        let same = d1.getTime() === d2.getTime();
        if (same) return 0;

        // Check if the first is greater than second
        if (d1 > d2) return 1;
        
        // Check if the first is less than second
        if (d1 < d2) return -1;
    }

}
