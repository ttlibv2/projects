import {Injectable, Type} from '@angular/core';
import {Objects} from "../utils/objects";
import {IStorage, JsonObject} from "../models/common";
import {BaseModel} from "../models/base-model";
const { booleanValue, isNull } = Objects;

@Injectable({providedIn: 'root'})
export class StorageService implements IStorage{
  secretKey: string = 'encryptSecretKey';

  private encrypt(json: string): string {

    // return crypto.AES.encrypt(json, this.secretKey)
    //   .toString(crypto.format.Hex);
    return json;
  }

  private decrypt(data: string): string {
    // return crypto.AES.decrypt(data, this.secretKey)
    //   .toString(crypto.enc.Utf8);
    return data;
  }

  /**
   * Remove all entry data
   * */
  clear_all(): void {
    localStorage.clear();
  }

  /**
   * Remove data without field
   * @param field the field
   * */
  clear(field: string): void {
    localStorage.removeItem(field);
  }

  private private_set(field: string, data: string): void {
    if(isNull(data)) localStorage.removeItem(field);
    else localStorage.setItem(field, this.encrypt(data));
  }

  get_nameModel(modelType: Type<any>): string {
    return modelType.name.toLowerCase();
  }

  set_string(field: string, value: number): void {
    this.private_set(field, `string::${value}`);
  }

  set_number(field: string, value: number): void {
    this.private_set(field, `number::${value}`);
  }

  set_json(field: string, json: object): void {
    this.private_set(field, 'json::'+JSON.stringify(json));
  }

  set_boolean(field: string, value: boolean): void {
    this.private_set(field, `boolean::${value}`);
  }

  set_model<E extends BaseModel>(model: E): string {
    const localName: string = model.constructor.name.toLowerCase();
    this.set_any(localName, model);
    return localName;
  }

  set_any<E>(field: string, value: E): void {
    if (Objects.isNull(value)) localStorage.removeItem(field);
    else if(typeof value === 'function') throw new Error(`The value not support function`);
    else if(value instanceof BaseModel) this.private_set(field, 'json::'+value.toString());
    else {
      const typeName = typeof value == 'object' ? 'json' : typeof value;
      const data: string = typeName === 'json' ? JSON.stringify(value) : value.toString();
      this.private_set(field, typeName + '::' + data);
    }
  }

  get_any<E>(field: string, callback?: (data: any) => E, defaultValue?:() => E): any {
    const item = localStorage.getItem(field);
    if (item == null) return isNull(defaultValue) ? null : defaultValue();
    else {
      const object: string = this.decrypt(item);
      const prefix = object.substring(0, object.indexOf('::'));
      const value = object.substring(prefix.length + 2);

      if(prefix === 'number') return parseFloat(value);
      else if(prefix === 'string') return `${value}`;
      else if(prefix === 'boolean') return booleanValue(value);
      else if(prefix === 'json') {
        const jsonData = JSON.parse(value);
        const callbackNew: (data: any) => E = callback ?? (data => data);
        return callbackNew(jsonData);
      }
      else throw new Error(`The data of field only support [number, string, boolean, json]`);
    }
  }

  get_string(field: string): string {
    const callback = (str: string) => str;
    return this.get_any(field, callback);
  }

  get_int(field: string): number {
    const callback = (str: string) => parseInt(str);
    return this.get_any(field, callback);
  }

  get_num(field: string): number {
    const callback = (str: string) => parseFloat(str);
    return this.get_any(field, callback);
  }

  get_json(field: string): JsonObject {
    const callback = (object: any) => object;
    return this.get_any(field, callback);
  }

  get_model<E extends BaseModel>(modelType: Type<E>): E {
    const name = this.get_nameModel(modelType);
    const callback = (json: JsonObject) => new modelType().update(json);
    return this.get_any(name, callback, () => new modelType());
  }

}
