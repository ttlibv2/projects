import crypto from 'crypto-js';
import { ElementRef, TemplateRef, Type } from "@angular/core";
import { BiFunction, Callback } from './function';
import { JsonAny, JsonKeyType } from "./common";

export class Objects {

  static assign<E>(objectClass: Type<E>, source: any, excludeFields?: string[]): E;
  static assign<E>(target: E, source: any, excludeFields?: string[]): E
  static assign<E>(object: E | Type<E>, source: any, excludeFields: string[] = []): E {
    const target: any = object instanceof Type ? new object() : object;
    if (Objects.isObject(target) && Objects.isObject(source)) {
      for (const key of Object.keys(source)) {
        if (!excludeFields.includes(key)) {
          const get = `get_${key}`, set = `set_${key}`;
          const srcVal = typeof source[get] === 'function' ? source[get]() : source[key];
          if (typeof target[set] === 'function') target[set](srcVal);
          else if (!Objects.isObject(srcVal)) target[key] = srcVal;
          else if (key in target) Objects.assign(target[key], srcVal);
          else target[key] = srcVal;
        }
      }
    }
    return target;
  }

  static parseI18N(str: any, prefix: string = '@@'): string | undefined {
    return Objects.isString(str) && str.startsWith(prefix) && !str.includes(' ') ? str.replace(prefix, '') : undefined;
  }

  static parseDate(value: any): Date {
    if (Objects.isNull(value)) return null;
    else if (value instanceof Date) return value;
    else if (typeof value === 'string') return new Date(value);
    else if (typeof value == 'number') return new Date(value);
    else return null;
  }

  static booleanValue(value: any, defaultNull?: boolean): boolean {
    if (Objects.isNull(value)) return defaultNull;
    else if (typeof value === 'boolean') return value;
    else if ([1, 0].includes(value)) return value === 1;
    else if (["1", "0"].includes(value)) return value === "1";
    else return null;
  }

  static allNotNull(...values: any[]): boolean {
    return !values.some(value => Objects.isNull(value));
  }

  static anyNotNull(...values: any[]): boolean {
    return values.some(value => Objects.notNull(value));
  }

  static anyNotBlank(...values: string[]): boolean {
    return values.some(value => Objects.notBlank(value));
  }

  static anyBlank(...values: string[]): boolean {
    return values.some(value => Objects.isBlank(value));
  }

  static anyTrue(...values: boolean[]): boolean {
    return values.some(value => value === true);
  }

  static anyFalse(...values: boolean[]): boolean {
    return values.some(value => value === false);
  }

  /**
   * Returns true if object is null or undefined
   * @param obj the object validate
   * */
  static isNull(obj: any): obj is null | undefined {
    return obj === null || obj === void 0;
  }

  static isBool(value: any): value is boolean {
    return typeof value === 'boolean';
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

  static isFalse(object: any): object is boolean {
    return object === false;
  }

  static isTrue(object: any): object is boolean {
    return object === true;
  }

  static isTemplateRef(object: any): object is TemplateRef<any> {
    return object instanceof TemplateRef;
  }

  static isArray(value: any): value is any[] {
    return Array.isArray(value);
  }

  static isJson(value: any): boolean {
    return Objects.isObject(value);
  }

  static isScalar(value: any): boolean {
    return value != null && (typeof value === 'string' || typeof value === 'number' || typeof value === 'bigint' || typeof value === 'boolean');
  }

  static isString(value: any, empty: boolean = true): value is string {
    return Objects.notNull(value) && typeof value === 'string' && (empty || value !== '');
  }

  static isPrintableCharacter(char: string = ''): boolean {
    return Objects.notEmpty(char) && char.length === 1 && !!char.match(/\S| /);
  }

  static isNumber(value: any): value is number {
    return Objects.notNull(value) && typeof value === 'number' && !isNaN(value);
  }

  static isStringNotBlank(value: any): value is string {
    return Objects.isString(value) && value.length > 0;
  }

  static isObject<T = object>(value: any): value is T {
    return Objects.notNull(value) && !Objects.isArray(value) && typeof value == 'object';
  }

  static isObjectNotEmpty(value: any): boolean {
    return Objects.isObject(value) && Objects.notEmpty(value);
  }

  static isUrl(str: string): boolean {
    try {
      let protocol = new URL(str).protocol;
      return protocol == 'http:' || protocol == 'https:';
    }
    catch (_) { return false; }
  }

  static isLetter(char: string): boolean {
    return /^[a-zA-Z\u00C0-\u017F]$/.test(char);
  }

  static isDate(input: any): input is Date {
    return Object.prototype.toString.call(input) === '[object Date]';
  }

  static isFunction(object: any): object is Function {
    return Objects.notNull(object) && typeof object === 'function'
  }

  static localeComparator(): (val1: string, val2: string) => number {
    //performance gain using Int.Collator. It is not recommended to use localeCompare against large arrays.
    return new Intl.Collator(undefined, { numeric: true }).compare;
  }

  static matchRegex(str: string, regex?: RegExp): boolean {
    if (!!regex) {
      const match = regex.test(str);
      regex.lastIndex = 0;
      return match;
    }
    return false;
  }

  static minifyCSS(css?: string): string | undefined {
    return css ? css
      .replace(/\/\*(?:(?!\*\/)[\s\S])*\*\/|[\r\n\t]+/g, '')
      .replace(/ {2,}/g, ' ')
      .replace(/ ([{:}]) /g, '$1')
      .replace(/([;,]) /g, '$1')
      .replace(/ !/g, '!')
      .replace(/: /g, ':') : css;
  }

  static removeAccents(str: string): string {
    // Regular expression to check for any accented characters 'Latin-1 Supplement' and 'Latin Extended-A'
    const accentCheckRegex = /[\xC0-\xFF\u0100-\u017E]/;

    if (str && accentCheckRegex.test(str)) {
      const accentsMap: { [key: string]: RegExp } = {
        A: /[\xC0-\xC5\u0100\u0102\u0104]/g,
        AE: /[\xC6]/g,
        C: /[\xC7\u0106\u0108\u010A\u010C]/g,
        D: /[\xD0\u010E\u0110]/g,
        E: /[\xC8-\xCB\u0112\u0114\u0116\u0118\u011A]/g,
        G: /[\u011C\u011E\u0120\u0122]/g,
        H: /[\u0124\u0126]/g,
        I: /[\xCC-\xCF\u0128\u012A\u012C\u012E\u0130]/g,
        IJ: /[\u0132]/g,
        J: /[\u0134]/g,
        K: /[\u0136]/g,
        L: /[\u0139\u013B\u013D\u013F\u0141]/g,
        N: /[\xD1\u0143\u0145\u0147\u014A]/g,
        O: /[\xD2-\xD6\xD8\u014C\u014E\u0150]/g,
        OE: /[\u0152]/g,
        R: /[\u0154\u0156\u0158]/g,
        S: /[\u015A\u015C\u015E\u0160]/g,
        T: /[\u0162\u0164\u0166]/g,
        U: /[\xD9-\xDC\u0168\u016A\u016C\u016E\u0170\u0172]/g,
        W: /[\u0174]/g,
        Y: /[\xDD\u0176\u0178]/g,
        Z: /[\u0179\u017B\u017D]/g,

        a: /[\xE0-\xE5\u0101\u0103\u0105]/g,
        ae: /[\xE6]/g,
        c: /[\xE7\u0107\u0109\u010B\u010D]/g,
        d: /[\u010F\u0111]/g,
        e: /[\xE8-\xEB\u0113\u0115\u0117\u0119\u011B]/g,
        g: /[\u011D\u011F\u0121\u0123]/g,
        i: /[\xEC-\xEF\u0129\u012B\u012D\u012F\u0131]/g,
        ij: /[\u0133]/g,
        j: /[\u0135]/g,
        k: /[\u0137,\u0138]/g,
        l: /[\u013A\u013C\u013E\u0140\u0142]/g,
        n: /[\xF1\u0144\u0146\u0148\u014B]/g,
        p: /[\xFE]/g,
        o: /[\xF2-\xF6\xF8\u014D\u014F\u0151]/g,
        oe: /[\u0153]/g,
        r: /[\u0155\u0157\u0159]/g,
        s: /[\u015B\u015D\u015F\u0161]/g,
        t: /[\u0163\u0165\u0167]/g,
        u: /[\xF9-\xFC\u0169\u016B\u016D\u016F\u0171\u0173]/g,
        w: /[\u0175]/g,
        y: /[\xFD\xFF\u0177]/g,
        z: /[\u017A\u017C\u017E]/g
      };

      for (let key in accentsMap) {
        str = str.replace(accentsMap[key], key);
      }
    }

    return str;
  }

  static sort<T>(value1: T, value2: T, order: number = 1, comparator: (val1: T, val2: T) => number, nullSortOrder: number = 1): number {
    const result = Objects.compare(value1, value2, comparator, order);
    let finalSortOrder = order;

    // nullSortOrder == 1 means Excel like sort nulls at bottom
    if (Objects.isEmpty(value1) || Objects.isEmpty(value2)) {
      finalSortOrder = nullSortOrder === 1 ? order : nullSortOrder;
    }

    return finalSortOrder * result;
  }

  static getNotBlank(...strings: string[]): string {
    return strings.find(s => Objects.notBlank(s));
  }

  static equals(obj1: any, obj2: any, field?: Function | string): boolean {

    if (Objects.notNull(field)) {
      return Objects.resolveFieldData(obj1, field)
        === Objects.resolveFieldData(obj2, field);
    }
    else if (obj1 === obj2) return true;
    else if (Objects.isNull(obj1) && Objects.notNull(obj2)) return false;
    else if (Objects.notNull(obj1) && Objects.isNull(obj2)) return false;
    else if (typeof obj1 !== typeof obj2) return false;
    else if (typeof obj1 == 'object' && typeof obj2 == 'object') {


      // check equals()
      if ('equals' in obj1 && typeof obj1['equals'] === 'function') {
        return obj1['equals'](obj2);
      }

      else if ('equals' in obj2 && typeof obj2['equals'] === 'function') {
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

  static allNull(...objects: any[]): boolean {
    return !objects.some(o => Objects.notNull(o));
  }

  static anyNull(...objects: any[]): boolean {
    return objects.some(o => Objects.isNull(o));
  }

  static anyNotEmpty(...objects: any[][]): boolean {
    return objects.some(o => Objects.notBlank(o));
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

  static valueToMap<K, V>(data: Map<any, any> | any[] | any, callback: Callback<any, V | [string, V]>): Map<K, V> {
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


  static download(fileName: string, content: Blob) {
    const win = document.defaultView || window;

    if (!win) {
      console.warn('There is no `window` associated with the current `document`');
      return;
    }

    const element = document.createElement('a');
    const url = win.URL.createObjectURL(content);
    element.setAttribute('href', url);
    element.setAttribute('download', fileName);
    element.style.display = 'none';
    document.body.appendChild(element);

    element.dispatchEvent(
      new MouseEvent('click', {
        bubbles: false,
        cancelable: true,
        view: win,
      })
    );

    document.body.removeChild(element);

    win.setTimeout(() => {
      win.URL.revokeObjectURL(url);
    }, 0);
  }

  static mergeDeep(target: any, ...sources: any[]): any {
    target = target || {};
    sources.forEach(src => mergeObject(target, src));
    return target;
  }

  static arrayToJson<E, K extends JsonKeyType, V>(array: E[], mapFunction: BiFunction<E, number, [K, V]>): JsonAny {
    return Object.fromEntries(array.map((item, index) => mapFunction(item, index)));
  }

  static getter(object: any, field: string): any {
    if (!Objects.isObject(object)) {
      throw new Error(`The data [${object}] not object`);
    }
    else if (typeof object[`get_${field}`] === 'function') {
      return object[`get_${field}`]();
    }
    else {
      return object[field];
    }
  }

  static setter(object: any, field: string, value: any): any {
    if (!Objects.isObject(object)) {
      throw new Error(`The data [${object}] not object`);
    }
    else if (typeof object[`set_${field}`] === 'function') {
      object[`set_${field}`](value);
      return object;
    }
    else {
      object[field] = value;
      return object;
    }
  }

  static fillArray(length: number): Array<number> {
    return Array.from({ length }, (v, i) => i);
  }

  static parseFlex(flex: number | string): string {
    const { isNull, isString, isNumber } = Objects;
    if (isNull(flex)) return undefined;
    else if (isNumber(flex)) return `${flex} ${flex}`;
    else {
      if (/^\d+(\.\d+)?(px|em|rem|%)$/.test(flex)) {
        return `0 0 ${flex}`;
      }
      else return flex;
    }
  }

  static ngClassToJson(prop: string | string[] | Set<string> | { [klass: string]: any; }) {
    if (Objects.isString(prop)) return { [prop]: true };
    else if (Objects.isArray(prop)) return Objects.arrayToJson(prop, i => [i, true]);
    else if (prop instanceof Set) return Objects.arrayToJson([...prop], i => [i, true]);
    else return prop;
  }

  static setStyle(ref: ElementRef<HTMLElement> | HTMLElement, cssName: string, cssValue: any, priority?: 'important' | '' | undefined): void {
    const element = ref instanceof ElementRef ? ref.nativeElement : ref;
    element?.style?.setProperty(cssName, cssValue, priority);
  }

  /**
   * Resovle data
   * @param object {<T> | Function}
   * @param params the params use for function
   *  */
  static resolve<T>(object: T | ((...params: any[]) => T), ...params: any[]): T {
    return Objects.isFunction(object) ? object(...params) : object;
  }

  /**
   * Compare object
   * @param object1
   * @param object2
   * @param order
   * */
  static compare<T = any>(object1: T, object2: T, comparator: (val1: T, val2: T) => number, order: number = 1): number {
    const { isEmpty, isString } = Objects;
    const emptyValue1 = isEmpty(object1), emptyValue2 = isEmpty(object2);
    if (emptyValue1 && emptyValue2) return 0;
    else if (emptyValue1) return order;
    else if (emptyValue2) return -order;
    else if (isString(object1) && isString(object2)) return comparator(object1, object2);
    else return object1 < object2 ? -1 : object1 > object2 ? 1 : 0;
  }



}


function mergeObject(target: any, source: any): any {
  if (Objects.isObject(target) && Objects.isObject(source)) {

    Object.keys(source).forEach((key: any) => {

      //-- source[key] is object
      if (Objects.isObject(source[key])) {
        if (key in target) target[key] = mergeObject(target[key], source[key]);
        else Object.assign(target, { [key]: source[key] });
      }

      // else 
      else {
        Object.assign(target, { [key]: source[key] });
      }
    });
  }
  return target;
}