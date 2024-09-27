import { HttpParams } from "@angular/common/http";
import { Type } from "@angular/core";
import { BaseModel } from "./base-model";
import { Callback } from 'ts-ui/helper';

export type Severity = 'primary' | 'secondary' | 'success' | 'info' | 'danger' | 'help' | 'warning' | 'contrast';

export type ResponseToModel<E> = (response: any) => E;
export type JsonObject = Record<string, any>;
export type ClientParams = HttpParams | {
  [param: string]: string | number | boolean | ReadonlyArray<string | number | boolean>;
};









// export class Page<E = any> extends BaseModel {
//   limit?: number;
//   total?: number;
//   total_page?: number;
//   current_page?: number;
//   is_first?: boolean;
//   is_last?: boolean;
//   data?: E[] = [];

//   static from<E>(json: JsonObject, callback: Callback<any, E>): Page<E> {
//     const data = (json['data'] ?? []).map((item: any) => callback(item));
//     return BaseModel.fromJson(Page<E>, { ...json, data });
//   }

// }

// export interface MessageObj extends Message {
//   severity?: Severity;
//   code?: string;
//   details?: string[];
// }

export interface ErrorResponse {
  [key: string]: any;
  code?: string;
  summary?: string;
  details?: any;
}

export interface SocialLink {
  link: string;
  icon: string;
  label: string;
}

export interface ImageObject {
  [name: string]: {
    md5hash?: string;
    ts_id?: number;
    base64?: string;
  };
}

// export interface Pageable {
//   [field: string]: any;
//   size?: number;
//   offset?: number;
//   page?: number;
// }

export interface IStorage {
  /**
   * Remove all entry data
   * */
  clear_all(): void;

  /**
   * Remove data without field
   * @param field the field
   * */
  clear(field: string): void;

  get_nameModel(modelType: Type<any>): string;

  write_string(field: string, value: number): void;

  write_number(field: string, value: number): void;

  write_json(field: string, json: object): void;

  write_boolean(field: string, value: boolean): void;

  write_model<E extends BaseModel>(model: E): string;

  write_any<E>(field: string, value: E): void;

  read_any<E>(field: string, callback?: (data: any) => E, defaultValue?: () => E): any;

  read_string(field: string): string;

  read_int(field: string): number;

  read_num(field: string): number;

  read_json(field: string): JsonObject;

  read_model<E extends BaseModel>(modelType: Type<E>, field?: string): E;

}