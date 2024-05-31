import { HttpParams } from "@angular/common/http";


export type ResponseToModel<E> = (response: any) => E;
export type JsonObject = {[field: string]: any};
export type ClientParams = HttpParams | {
  [param: string]: string | number | boolean | ReadonlyArray<string | number | boolean>;
};

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

