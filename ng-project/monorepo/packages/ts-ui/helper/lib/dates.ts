import { Objects } from "./objects";

export class DateUtil {

    static parseDate(value: any): Date {
        if (Objects.isNull(value)) return undefined;
        else if (value instanceof Date) return value;
        else if (typeof value === 'string') return new Date(value);
        else if (typeof value == 'number') return new Date(value);
        else return undefined;
    }

    static isDate(input: any): input is Date {
        return Object.prototype.toString.call(input) === '[object Date]';
    }

}