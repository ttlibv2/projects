
export class Objects {

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

  static matchRegex(str: string, regex?: RegExp): boolean {
    if (!!regex) {
      const match = regex.test(str);
      regex.lastIndex = 0;
      return match;
    }
    return false;
  }

  static getNotBlank(...strings: string[]): string {
    return strings.find(s => Objects.notBlank(s));
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

  static fillArray(length: number): Array<number> {
    return Array.from({ length }, (v, i) => i);
  }

  /**
   * Resovle data
   * @param object {<T> | Function}
   * @param params the params use for function
   *  */
  static resolve<T>(object: T | ((...params: any[]) => T), ...params: any[]): T {
    return Objects.isFunction(object) ? object(...params) : object;
  }

  static equals(obj1: any, obj2: any): boolean {
    if(obj1 === obj2) return true;
    else if(obj1 && !obj2) return false;
    else if(obj2 && !obj1) return false;
    else if(typeof obj1 != typeof obj2) return false;
    else if(typeof obj1 == 'object' && typeof obj2 == 'object') {

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
          const keys = Object.keys(obj1);
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



}