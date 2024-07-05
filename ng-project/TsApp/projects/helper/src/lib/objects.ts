import * as crypto from 'crypto-js';
import { Type } from "@angular/core";
import { Callback } from './function';

export class Objects {



  static assign(target: any, source: any): any {
    if (Objects.isObject(target) && Objects.isObject(source)) {
      for (const key of Object.keys(source)) {
        const srcFncGet = source[`get_${key}`];
        const srcVal = typeof srcFncGet === 'function' ? srcFncGet() : source[key];
        if (typeof target[`set_${key}`] === 'function') target[`set_${key}`](srcVal);
        else if (!Objects.isObject(srcVal)) target[key] = srcVal;
        else if (key in target) Objects.assign(target[key], srcVal);
        else target[key] = srcVal;
      }
    }
    else return target;
  }


  static parseDate(value: any): Date {
    if (Objects.isNull(value)) return null;
    else if (value instanceof Date) return value;
    else if (typeof value === 'string') return new Date(value);
    else if (typeof value == 'number') return new Date(value);
    else return null;
  }


  static booleanValue(value: any, def: boolean = false, str: string = 'true', num: number = 1): boolean {
    if (typeof value === 'boolean') return value;
    else if (typeof value === 'string') return value === str;
    else if (typeof value === 'number') return value === num;
    else return def;
  }

  static anyBlank(...values: string[]): boolean {
    return values.some(value => Objects.isBlank(value));
  }

  /**
   * Returns true if object is null or undefined
   * @param obj the object validate
   * */
  static isNull(obj: any): obj is null | undefined {
    return obj === null || obj === void 0;
  }

  static isBlank(str: string): boolean {
    return Objects.isNull(str) || `${str}`.trim().length == 0;
  }

  static notBlank(str: any): boolean {
    return !Objects.isBlank(str);
  }

  static isEmpty(object: any): boolean {
    if (Objects.isNull(object)) return true;
    else if (typeof object == 'string') return object.length == 0;
    else if (Objects.isArray(object)) return object.length == 0;
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

  static isDate(input: any) {
    return Object.prototype.toString.call(input) === '[object Date]';
  }

  static isFunction(object: any): object is Function {
    return Objects.notNull(object) && typeof object === 'function'
  }

  static equals(obj1: any, obj2: any, field?: Function | string): boolean {

    if (Objects.notNull(field)) {
      return Objects.resolveFieldData(obj1, field) === Objects.resolveFieldData(obj2, field);
    }
    else if (obj1 === obj2) return true;
    else if (Objects.isNull(obj1) && Objects.notNull(obj2)) return false;
    else if (Objects.notNull(obj1) && Objects.isNull(obj2)) return false;
    else if (typeof obj1 !== typeof obj2) return false;
    else if (typeof obj1 == 'object' && typeof obj2 == 'object') {


      // check equals()
      if('equals' in obj1 && typeof obj1['equals'] === 'function') {
        return obj1['equals'](obj2);
      }

      else if('equals' in obj2 && typeof obj2['equals'] === 'function') {
        return obj2['equals'](obj1);
      }

      else {

        const arrA = Array.isArray(obj1), arrB = Array.isArray(obj2);
        const dateA = Objects.isDate(obj1), dateB = Objects.isDate(obj2);
        const regexpA = obj1 instanceof RegExp, regexpB = obj2 instanceof RegExp;
  
        let i, length, key;
  
        // check array
        if (arrA && arrB) {
          length = obj1.length;
          if (length != obj2.length) return false;
          for (i = length; i-- !== 0;) if (!Objects.equals(obj1[i], obj2[i])) return false;
          return true;
        }
        else if (arrA != arrB) return false;
  
        // check date
        else if (dateA != dateB) return false;
        else if (dateA && dateB) return obj1.getTime() == obj2.getTime();
  
        // check regex
        else if (regexpA != regexpB) return false;
        else if (regexpA && regexpB) return obj1.toString() == obj2.toString();
  
        // other 
        else {
          var keys = Object.keys(obj1);
          length = keys.length;
  
          if (length !== Object.keys(obj2).length) return false;
  
          for (i = length; i-- !== 0;) if (!Object.prototype.hasOwnProperty.call(obj2, keys[i])) return false;
  
          for (i = length; i-- !== 0;) {
            key = keys[i];
            if (!Objects.equals(obj1[key], obj2[key])) return false;
          }
  
          return true;
        }
      }
    }
    else return obj1 !== obj1 && obj2 !== obj2;
  }

  static resolveFieldData(data: any, field: Function | string): any {
    if (Objects.anyNull(data, field)) return null;
    else if (Objects.isFunction(field)) return field(data);
    else if (!field.includes('.')) return data[field];
    else {
      const fields: string[] = field.split('.');
      let value = data;
      for (let i = 0, len = fields.length; i < len; ++i) {
        if (value == null) return null;
        else value = value[fields[i]];
      }
      return value;
    }
  }

  static ifNotNull<E>(object: E, callback: (val: E) => any): void {
    if (Objects.notNull(object)) callback(object);
  }

  static ifListNotEmpty<E>(array: E[], callback: (items: E[]) => any) {
    if (Objects.notEmpty(array)) callback(array);
  }

  static encodeBase64(string: string): string {
    const wa = crypto.enc.Utf8.parse(string);
    return crypto.enc.Base64.stringify(wa);
  }

  static decodeBase64(string: string): string {
    const wa = crypto.enc.Base64.parse(string);
    return wa.toString(CryptoJS.enc.Utf8);
  }

  static anyNull(...objects: any[]): boolean {
    return objects.some(o => Objects.isNull(o));
  }

  static isClass(object: any): boolean {
    return Objects.isObject(object) && object.constructor.name !== 'Object';
  }

  static isClassOf(object: any, cls: Type<any>): boolean {
    return Objects.isClass(object) && object.constructor.name === cls.name;
  }

  static extractValueNotNull<E>(json: any): any {
    if (!Objects.isObject(json)) return json;
    else return Object.fromEntries(Object.keys(json).filter(k => Objects.notNull(json[k])).map(k => [k, json[k]]));
  }

  static valueToMap<K, V>(data: any, callback: Callback<any, V | [string, V]>): Map<K, V> {
    if (Objects.isNull(data)) return new Map();
    else if (data instanceof Map) {
      const arrays: any[] = [...data.keys()].map((k: any) => [k, callback(data.get(k))]);
      return new Map(arrays);
    }//
    else if (Objects.isArray(data)) {
      const arrays: any[] = data.map(item => callback(item));
      return new Map(arrays);
    }//
    else {
      const arrays: any[] = Object.keys(data).map(k => [k, callback(data[k])]);
      return new Map(arrays);
    }

  }

  static delFieldJson(json: Record<string, any>, ...fields: string[]): any {
    fields.forEach(field => delete json[field]);
    return json;
  }

  static createElement(document: Document, tagName: string, ...attributeValues: string[]): HTMLElement {
    const el = document.createElement(tagName);
    let indexNext = 0;
    while (indexNext < attributeValues.length) {
      let attr = attributeValues[indexNext++];
      let value = attributeValues[indexNext++];
      el.setAttribute(attr, value);
    }

    return el;
  }


  static mergeDeep(target: any, source: any): any {
    let output = Object.assign({}, target);
    if (Objects.isObject(target) && Objects.isObject(source)) {
      Object.keys(source).forEach((key: any) => {
        if (Objects.isObject(source[key])) {
          if (!(key in target)) {
            Object.assign(output, {[key]: source[key]});
          } else {
            output[key] = Objects.mergeDeep(target[key], source[key]);
          }
        } else {
          Object.assign(output, {[key]: source[key]});
        }
      });
    }
    return output;
  }
}