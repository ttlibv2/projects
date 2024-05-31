import {JsonObject} from "../models/common";

export class Objects {


  static parseDate(value: any): Date {
    if(Objects.isNull(value)) return null;
    else if(value instanceof Date) return value;
    else if(typeof value === 'string') return new Date(value);
    else if(typeof value == 'number') return new Date(value);
    else return null;
  }


  static booleanValue(value: any, def: boolean = false, str: string = 'true', num: number = 1): boolean {
    if(typeof value ==='boolean') return value;
    else if(typeof value === 'string') return value === str;
    else if(typeof value === 'number') return  value === num;
    else return def;
  }

  static isNull(obj: any): boolean {
    return obj === null || obj === void 0;
  }

  static isBlank(str: string): boolean {
    return Objects.isNull(str) || str.trim().length == 0;
  }

  static isEmpty(object: any): boolean {
    if(Objects.isNull(object)) return true;
    else if(typeof object == 'string') return object.length == 0;
    else if (typeof object == 'object') return Object.keys(object).length == 0;
    else return false;
  }

  static notEmpty(object: any): boolean {
    return !Objects.isEmpty(object);
  }

  static notNull(object: any): boolean {
    return !Objects.isNull(object);
  }

  static ifNotNull<E>(object: E, callback: (val: E) => any): void {
    if(Objects.notNull(object)) callback(object);
  }

  static ifListNotEmpty<E>(array: E[], callback: (items: E[]) => any) {
      if(Objects.notEmpty(array)) callback(array);
  }

}
