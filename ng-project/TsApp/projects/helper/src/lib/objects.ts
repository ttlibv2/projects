import * as crypto from 'crypto-js';
import {Type} from "@angular/core";

export class Objects {
  

  static assign(target: any, source: any): any {
    if(Objects.isObject(target) && Objects.isObject(source)) {
      for(const key of Object.keys(source)) {
        const srcFncGet = source[`get_${key}`];
        const srcVal = typeof srcFncGet === 'function' ? srcFncGet() : source[key];
        if(typeof target[`set_${key}`] === 'function') target[`set_${key}`](srcVal);
        else if(!Objects.isObject(srcVal)) target[key] = srcVal;
        else if(key in target) Objects.assign(target[key], srcVal);
        else target[key] = srcVal;
      }
    }
    else return target;
  }


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

  static anyBlank(...values: string[]): boolean {
    return values.some(value => Objects.isBlank(value));
  }

  /**
   * Returns true if object is null or undefined
   * @param obj the object validate
   * */
  static isNull(obj: any): boolean {
    return obj === null || obj === void 0;
  }

  static isBlank(str: string): boolean {
    return Objects.isNull(str) || `${str}`.trim().length == 0;
  }

  static notBlank(str: any): boolean {
    return !Objects.isBlank(str);
  }

  static isEmpty(object: any): boolean {
    if(Objects.isNull(object)) return true;
    else if(typeof object == 'string') return object.length == 0;
    else if(Objects.isArray(object)) return object.length == 0;
    else if (typeof object == 'object') return Object.keys(object).length == 0;
    else return false;
  }

  static notEmpty(object: any): boolean {
    return !Objects.isEmpty(object);
  }

  static notNull(object: any): boolean {
    return !Objects.isNull(object);
  }

  static isArray(value: any): value is [] {
    return Array.isArray(value);
  }

  static isObject(value: any): boolean {
    return Objects.notNull(value) && typeof value == 'object' && !Objects.isArray(value);
  }

  static isUrl(str: string): boolean {
    try {
      let protocol = new URL(str).protocol;
      return protocol == 'http:' || protocol == 'https:';
    }
    catch (_) { return false; }
  }

  static isFunction(object: any): boolean {
    return Objects.notNull(object) && typeof object === 'function'
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

  static isClass(object: any): boolean {
    return Objects.isObject(object) && object.constructor.name !== 'Object';
  }

  static isClassOf(object: any, cls: Type<any>): boolean {
    return Objects.isClass(object) && object.constructor.name === cls.name;
  }

  static extractValueNotNull<E>(json: any): any {
    if(!Objects.isObject(json)) return json;
    else return Object.fromEntries(Object.keys(json).filter(k => Objects.notNull(json[k])).map(k => [k, json[k]]) );
  }

}