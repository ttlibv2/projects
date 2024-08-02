export type JsonKeyType = string | number | symbol;
export type JsonAny = {[field: JsonKeyType]: any};
export type AssignObject<E = any> = JsonAny | E | Partial<E>;