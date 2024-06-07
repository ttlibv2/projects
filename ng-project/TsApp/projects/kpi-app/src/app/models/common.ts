import {HttpParams} from "@angular/common/http";
import {Type} from "@angular/core";
import {BaseModel} from "./base-model";
import {Objects} from "../utils/objects";
import {Message} from "primeng/api";


export type ResponseToModel<E> = (response: any) => E;
export type JsonObject = { [field: string]: any };
export type ClientParams = HttpParams | {
  [param: string]: string | number | boolean | ReadonlyArray<string | number | boolean>;
};

export type Severity = 'error' | 'warn' | 'info' | 'success' | 'primary' | 'help';

export interface Page<E> {
  limit?: number;
  total_page?: number;
  current_page?: number;
  is_fisrt?: boolean;
  is_last?: boolean;
  data?: E[];
}

export interface MessageObj extends Message {
  severity?: Severity;
  code?: string;
  details?: string[];
}

export interface SocialLink {
  link: string;
  icon: string;
  label: string;
}

export interface ImageObject {
  [name: string]: number;
}

export interface Pageable {
  [field: string]: any;
}

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

  set_string(field: string, value: number): void;

  set_number(field: string, value: number): void;

  set_json(field: string, json: object): void;

  set_boolean(field: string, value: boolean): void;

  set_model<E extends BaseModel>(model: E): string;

  set_any<E>(field: string, value: E): void;

  get_any<E>(field: string, callback?: (data: any) => E, defaultValue?: () => E): any;

  get_string(field: string): string;

  get_int(field: string): number;

  get_num(field: string): number;

  get_json(field: string): JsonObject;

  get_model<E extends BaseModel>(modelType: Type<E>): E;

}
