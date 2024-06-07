import * as crypto from 'crypto-js';

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

  /**
   * Returns true if object is null or undefined
   * @param obj the object validate
   * */
  static isNull(obj: any): boolean {
    return obj === null || obj === void 0;
  }

  static isBlank(str: string): boolean {
    return Objects.isNull(str) || str.trim().length == 0;
  }

  static notBlank(str: any): boolean {
    return !Objects.isBlank(str);
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

  static isArray(value: any, empty = true): boolean {
    return Array.isArray(value) && (empty || value.length !== 0);
  }

  static isObject(value: any, empty = true): boolean {
    const hasObject: boolean = typeof value == 'object' && Objects.notNull(value) && !Objects.isArray(value);
    return hasObject && (empty || Object.keys(value).length !== 0);
  }

  static isUrl(str: string): boolean {
    try {
      let protocol = new URL(str).protocol;
      return protocol == 'http:' || protocol == 'https:';
    }
    catch (_) { return false; }
  }




  static ifNotNull<E>(object: E, callback: (val: E) => any): void {
    if(Objects.notNull(object)) callback(object);
  }

  static ifListNotEmpty<E>(array: E[], callback: (items: E[]) => any) {
      if(Objects.notEmpty(array)) callback(array);
  }

  static encodeBase64(string: string): string {
    const wa = crypto.enc.Utf8.parse(string);
    return crypto.enc.Base64.stringify(wa);
  }

  static decodeBase64(string: string): string {
    const wa = crypto.enc.Base64.parse(string);
    return wa.toString(CryptoJS.enc.Utf8);
  }

  static assign(target: any, source: any): any {
    if(Objects.isObject(target) && Objects.isObject(source)) {
      Object.keys(source).forEach(sourceKey => {
        const fncSet = target[`set_${sourceKey}`];
        const sourceValue = source[sourceKey];
        if(typeof fncSet === 'function') fncSet(sourceValue);
        else if(Objects.isArray(sourceValue)) target[sourceKey] = sourceValue;
        else if(!Objects.isObject(sourceValue))target[sourceKey] = sourceValue;
        else Objects.assign(target[sourceKey], source[sourceKey]);
      });
    }
  }
}
