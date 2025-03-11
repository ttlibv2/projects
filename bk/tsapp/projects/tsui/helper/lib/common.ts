export type JsonKeyType = string | number | symbol;
export type JsonAny<E =any> = {[field: JsonKeyType]: E};
export type AssignObject<E = any> = JsonAny | E | Partial<E>;